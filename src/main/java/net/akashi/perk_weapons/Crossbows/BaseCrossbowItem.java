package net.akashi.perk_weapons.Crossbows;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import net.akashi.perk_weapons.Client.ClientHelper;
import net.akashi.perk_weapons.Config.Properties.Bow.BowProperties;
import net.akashi.perk_weapons.Config.Properties.Crossbow.CrossbowProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.BaseArrow;
import net.akashi.perk_weapons.Network.ArrowVelocitySyncPacket;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Registry.ModPackets;
import net.akashi.perk_weapons.Util.EnchantmentValidator;
import net.akashi.perk_weapons.Util.IDoubleLineCrosshairItem;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.Predicate;

import static net.minecraft.world.item.enchantment.Enchantments.*;

public class BaseCrossbowItem extends CrossbowItem implements IDoubleLineCrosshairItem {

	public static final UUID MOVEMENT_SPEED_UUID = UUID.fromString("DB3F25A3-255C-8F4A-B293-EA1BA59D27CE");
	public static Predicate<ItemStack> SUPPORTED_PROJECTILE = (stack) -> stack.is(Items.ARROW);
	public Multimap<Attribute, AttributeModifier> AttributeModifiers;
	public boolean onlyAllowMainHand = false;
	private static final String TAG_CHARGED = "Charged";
	private static final String TAG_CHARGED_PROJECTILES = "ChargedProjectiles";
	protected int AMMO_CAPACITY = 1;
	protected int MAX_CHARGE_TICKS = 25;
	protected float DAMAGE = 10.0F;
	protected float VELOCITY = 4.0F;
	protected float INACCURACY = 1.0F;

	protected final List<Enchantment> GeneralEnchants = new ArrayList<>(Arrays.asList(
			QUICK_CHARGE,
			MULTISHOT,
			PIERCING,
			POWER_ARROWS,
			MENDING,
			UNBREAKING,
			MOB_LOOTING
	));

	protected final List<Enchantment> ConflictEnchants = new ArrayList<>();


	public BaseCrossbowItem(Properties pProperties) {
		super(pProperties);
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerCrossbowPropertyOverrides(this);
	}

	/**
	 * To avoid a bug caused by the vanilla equipment update method, if speedModifier!=0, onlyAllowMainHand will be forced set true
	 **/
	public BaseCrossbowItem(int maxChargeTicks, float damage, float velocity,
	                        float inaccuracy, float speedModifier, boolean onlyAllowMainHand,
	                        Properties pProperties) {
		super(pProperties);
		this.MAX_CHARGE_TICKS = maxChargeTicks;
		this.DAMAGE = damage;
		this.VELOCITY = velocity;
		this.INACCURACY = inaccuracy;
		this.onlyAllowMainHand = onlyAllowMainHand;
		if (speedModifier != 0.0F) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(MOVEMENT_SPEED_UUID,
					"Tool modifier", speedModifier, AttributeModifier.Operation.MULTIPLY_TOTAL));
			this.AttributeModifiers = builder.build();
			this.onlyAllowMainHand = true;
		} else {
			this.AttributeModifiers = ImmutableMultimap.of();
		}
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerCrossbowPropertyOverrides(this);
	}

	//General overrides
	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		if (onlyAllowMainHand && pHand != InteractionHand.MAIN_HAND) {
			return InteractionResultHolder.pass(itemstack);
		}

		if (isCrossbowCharged(itemstack)) {
			shoot(pLevel, pPlayer, pHand, itemstack, DAMAGE, VELOCITY, INACCURACY);
			consumeAndSetCharged(itemstack);
			return InteractionResultHolder.consume(itemstack);
		} else if (!pPlayer.getProjectile(itemstack).isEmpty()) {
			if (!isCrossbowCharged(itemstack)) {
				pPlayer.startUsingItem(pHand);
			}

			return InteractionResultHolder.consume(itemstack);
		} else {
			return InteractionResultHolder.fail(itemstack);
		}
	}

	@Override
	public void onUseTick(Level level, LivingEntity livingEntity, ItemStack crossbowStack, int useTimeLeft) {
		if (!level.isClientSide()) {
			SoundEvent startSoundEvent = this.getStartSound(crossbowStack);
			SoundEvent middleSoundEvent = this.getMiddleSound(crossbowStack);
			byte progress = getChargeProgressFrom0To10(livingEntity, crossbowStack);

			if (progress == 2) {
				level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
						startSoundEvent, SoundSource.PLAYERS, 0.5F, 1.0F);
			}

			if (progress == 6 && middleSoundEvent != null) {
				level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
						middleSoundEvent, SoundSource.PLAYERS, 0.5F, 1.0F);
			}

		}
	}

	@Override
	public void releaseUsing(ItemStack crossbowStack, Level level, LivingEntity shooter, int useTimeLeft) {
		float progress = getChargeProgress(shooter, crossbowStack);

		if (progress >= 1.0F && !isCrossbowCharged(crossbowStack)) {
			boolean loaded = false;
			while (getChargedProjectileAmount(crossbowStack) < getAmmoCapacity(crossbowStack)) {
				if (!tryLoadAmmo(shooter, crossbowStack)) {
					break;
				}
				loaded = true;
			}
			if (loaded) {
				setCrossbowCharged(crossbowStack, true);
				SoundSource soundsource = shooter instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
				level.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(),
						getEndSound(crossbowStack), soundsource, 1.0F,
						1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
			}
		}
	}

	@Override
	public @NotNull UseAnim getUseAnimation(ItemStack pStack) {
		return UseAnim.CROSSBOW;
	}

	@Override
	public int getUseDuration(ItemStack pStack) {
		return 72000;
	}

	@Override
	public @NotNull Predicate<ItemStack> getSupportedHeldProjectiles() {
		return BaseCrossbowItem.SUPPORTED_PROJECTILE;
	}

	@Override
	public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
		return BaseCrossbowItem.SUPPORTED_PROJECTILE;
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged || newStack.getItem() != oldStack.getItem();
	}

	@Override
	public float getChokeProgress(LivingEntity shooter, ItemStack stack) {
		return isCrossbowCharged(stack) ? 1.0f : getChargeProgress(shooter, stack);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		return slot == EquipmentSlot.MAINHAND ? AttributeModifiers : ImmutableMultimap.of();
	}

	//Weapon mechanic logics
	protected void shoot(Level level, LivingEntity shooter, InteractionHand hand, ItemStack crossbowStack,
	                     float damage, float velocity, float inaccuracy) {
		if (shooter instanceof Player player && net.minecraftforge.event.ForgeEventFactory.onArrowLoose(crossbowStack,
				shooter.level(), player, 1, true) < 0) return;

		int multiShotLevel = getCrossbowEnchantmentLevel(crossbowStack, MULTISHOT);
		int pierceLevel = getCrossbowEnchantmentLevel(crossbowStack, PIERCING);
		int powerLevel = getCrossbowEnchantmentLevel(crossbowStack, POWER_ARROWS);

		int shotAmount = multiShotLevel * 2 + 1;
		int angle = multiShotLevel * -10;

		damage = (float) (damage * (powerLevel > 0 ? 1 + 0.25 * powerLevel : 1));

		boolean isCreative = shooter instanceof Player && ((Player) shooter).getAbilities().instabuild;
		for (int i = 0; i < shotAmount; i++) {
			createProjectile(level, shooter, hand, crossbowStack, isCreative, (byte) pierceLevel,
					damage, velocity, inaccuracy, angle);
			angle += 10;
		}

		awardPlayerStats(level, shooter, crossbowStack);
	}

	//Ammo loading related
	public boolean tryLoadAmmo(LivingEntity shooter, ItemStack crossbowStack) {
		ItemStack ammoStack = shooter.getProjectile(crossbowStack);
		boolean isShooterPlayer = shooter instanceof Player;
		boolean isCreative = isShooterPlayer && ((Player) shooter).getAbilities().instabuild;

		if (ammoStack.isEmpty()) {
			if (isCreative || !isShooterPlayer) {
				ammoStack = new ItemStack(Items.ARROW);
			} else {
				return false;
			}
		}
		ItemStack ammoToLoad = ammoStack.copyWithCount(1);

		if (isShooterPlayer && !isCreative) {
			ammoStack.shrink(1);
			if (ammoStack.isEmpty())
				((Player) shooter).getInventory().removeItem(ammoStack);
		}

		addChargedProjectile(crossbowStack, ammoToLoad);
		return true;
	}

	public int getMaxChargeTicks(ItemStack crossbowStack) {
		int quickChargeLevel = getCrossbowEnchantmentLevel(crossbowStack, QUICK_CHARGE);
		return Math.max(1, quickChargeLevel == 0 ? MAX_CHARGE_TICKS : MAX_CHARGE_TICKS - 5 * quickChargeLevel);
	}

	public byte getChargeProgressFrom0To10(LivingEntity shooter, ItemStack crossbowStack) {
		return (byte) Math.floor(getChargeProgress(shooter, crossbowStack) * 10);
	}

	public float getChargeProgress(LivingEntity shooter, ItemStack crossbowStack) {
		return Math.min((float) shooter.getTicksUsingItem() / (float) getMaxChargeTicks(crossbowStack), 1.0f);
	}

	//Projectile / Charge state related
	protected void createProjectile(Level level, LivingEntity shooter, InteractionHand hand,
	                                ItemStack crossbowStack, boolean isCreativeMode, byte pierceLevel,
	                                float damage, float velocity, float inaccuracy, float projectileAngle) {
		if (level.isClientSide()) {
			return;
		}
		int durabilityCost = 1;
		Projectile projectile = getProjectile(level, shooter, crossbowStack);
		if (projectile instanceof AbstractArrow arrow) {
			if (isCreativeMode || projectileAngle != 0.0F || !(shooter instanceof Player))
				arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

			arrow.setBaseDamage(damage / velocity);
			if (pierceLevel > 0) {
				arrow.setPierceLevel(pierceLevel);
			}

		} else {
			durabilityCost = 3;
		}

		Vec3 vec31 = shooter.getUpVector(1.0F);
		Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(projectileAngle * ((float) Math.PI / 180F),
				vec31.x, vec31.y, vec31.z);
		Vec3 vec3 = shooter.getViewVector(1.0F);
		Vector3f vector3f = vec3.toVector3f().rotate(quaternionf);
		projectile.shoot(vector3f.x(), vector3f.y(), vector3f.z(), velocity, inaccuracy);


		crossbowStack.hurtAndBreak(durabilityCost, shooter, (entity) -> entity.broadcastBreakEvent(hand));
		level.addFreshEntity(projectile);

		//Sync velocity to all clients
		ModPackets.NETWORK.send(PacketDistributor.ALL.noArg(),
				new ArrowVelocitySyncPacket(projectile.getDeltaMovement(), projectile.getId()));

		level.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(),
				getShootSound(crossbowStack), SoundSource.PLAYERS, 1.0F, getShotPitch(shooter.getRandom()));
	}

	protected Projectile getProjectile(Level level, LivingEntity shooter, ItemStack crossbowStack) {
		BaseCrossbowItem crossbowItem = (BaseCrossbowItem) crossbowStack.getItem();
		ItemStack ammoStack = crossbowItem.getLastChargedProjectile(crossbowStack);
		Item ammoItem = ammoStack.getItem();

		if (ammoItem instanceof FireworkRocketItem) {
			return new FireworkRocketEntity(level, ammoStack, shooter, shooter.getX(),
					shooter.getEyeY() - (double) 0.15F, shooter.getZ(), true);
		}

		BaseArrow arrow = new BaseArrow(ModEntities.BASE_ARROW.get(), level, shooter);
		if (ammoItem instanceof SpectralArrowItem) {
			arrow.setSpectralArrow(true);
		} else {
			arrow.setEffectsFromItem(ammoStack);
		}
		arrow.setShotFromCrossbow(true);
		return arrow;
	}

	public void addChargedProjectile(ItemStack crossbowStack, ItemStack ammoStack) {
		CompoundTag compoundtag = crossbowStack.getOrCreateTag();
		ListTag listtag;
		if (compoundtag.contains(TAG_CHARGED_PROJECTILES, 9)) {
			listtag = compoundtag.getList(TAG_CHARGED_PROJECTILES, 10);
		} else {
			listtag = new ListTag();
		}

		CompoundTag ammoTag = new CompoundTag();
		ammoStack.save(ammoTag);
		listtag.add(ammoTag);
		compoundtag.put(TAG_CHARGED_PROJECTILES, listtag);
	}

	public List<ItemStack> getChargedProjectiles(ItemStack crossbowStack) {
		List<ItemStack> list = Lists.newArrayList();
		ListTag listtag = getChargedProjectileListTag(crossbowStack);
		for (int i = 0; i < listtag.size(); ++i) {
			CompoundTag ammoTag = listtag.getCompound(i);
			list.add(ItemStack.of(ammoTag));
		}
		return list;
	}

	public ListTag getChargedProjectileListTag(ItemStack crossbowStack) {
		CompoundTag compoundtag = crossbowStack.getOrCreateTag();
		ListTag listtag;
		if (compoundtag.contains(TAG_CHARGED_PROJECTILES, 9)) {
			listtag = compoundtag.getList(TAG_CHARGED_PROJECTILES, 10);
		} else {
			listtag = new ListTag();
		}
		return listtag;
	}

	public int getChargedProjectileAmount(ItemStack crossbowStack) {
		return getChargedProjectileListTag(crossbowStack).size();
	}

	public ItemStack getLastChargedProjectile(ItemStack crossbowStack) {
		List<ItemStack> ammoList = getChargedProjectiles(crossbowStack);
		int size = ammoList.size();
		return size > 0 ? ammoList.get(ammoList.size() - 1) : ItemStack.EMPTY;
	}

	public void clearChargedProjectiles(ItemStack crossbowStack) {
		CompoundTag compoundtag = crossbowStack.getOrCreateTag();
		ListTag listtag = compoundtag.getList(TAG_CHARGED_PROJECTILES, 9);
		listtag.clear();
		compoundtag.put(TAG_CHARGED_PROJECTILES, listtag);
	}

	public void consumeAndSetCharged(ItemStack crossbowStack) {
		ListTag listtag = getChargedProjectileListTag(crossbowStack);
		if (!listtag.isEmpty())
			listtag.remove(listtag.size() - 1);
		if (listtag.isEmpty()) {
			setCrossbowCharged(crossbowStack, false);
		}
	}

	public boolean isCrossbowCharged(ItemStack crossbowStack) {
		CompoundTag tag = crossbowStack.getOrCreateTag();
		return tag.contains(TAG_CHARGED) && tag.getBoolean(TAG_CHARGED);
	}

	public void setCrossbowCharged(ItemStack crossbowStack, boolean charged) {
		CompoundTag tag = crossbowStack.getOrCreateTag();
		tag.putBoolean(TAG_CHARGED, charged);
	}

	public boolean isFireworkCharged(ItemStack crossbowStack) {
		if (crossbowStack.getItem() instanceof BaseCrossbowItem crossbowItem) {
			return crossbowItem.getLastChargedProjectile(crossbowStack).is(Items.FIREWORK_ROCKET);
		}
		return false;
	}

	public int getAmmoCapacity(ItemStack crossbowStack) {
		return AMMO_CAPACITY;
	}

	//Miscellaneous
	protected static float getShotPitch(RandomSource pRandom) {
		boolean flag = pRandom.nextBoolean();
		return getRandomShotPitch(flag, pRandom);
	}

	protected static float getRandomShotPitch(boolean pIsHighPitched, RandomSource pRandom) {
		float f = pIsHighPitched ? 0.63F : 0.43F;
		return 1.0F / (pRandom.nextFloat() * 0.5F + 1.8F) + f;
	}

	protected SoundEvent getStartSound(ItemStack crossbowStack) {
		return switch (crossbowStack.getEnchantmentLevel(QUICK_CHARGE)) {
			case 1 -> SoundEvents.CROSSBOW_QUICK_CHARGE_1;
			case 2 -> SoundEvents.CROSSBOW_QUICK_CHARGE_2;
			case 3 -> SoundEvents.CROSSBOW_QUICK_CHARGE_3;
			default -> SoundEvents.CROSSBOW_LOADING_START;
		};
	}

	protected SoundEvent getMiddleSound(ItemStack crossbowStack) {
		return getMaxChargeTicks(crossbowStack) > 20 ? SoundEvents.CROSSBOW_LOADING_MIDDLE : null;
	}

	protected SoundEvent getEndSound(ItemStack crossbowStack) {
		return SoundEvents.CROSSBOW_LOADING_END;
	}

	protected SoundEvent getShootSound(ItemStack crossbowStack) {
		return SoundEvents.CROSSBOW_SHOOT;
	}

	protected void awardPlayerStats(Level level, LivingEntity shooter, ItemStack crossbowStack) {
		if (shooter instanceof ServerPlayer serverplayer) {
			if (!level.isClientSide) {
				CriteriaTriggers.SHOT_CROSSBOW.trigger(serverplayer, crossbowStack);
			}

			serverplayer.awardStat(Stats.ITEM_USED.get(crossbowStack.getItem()));
		}
	}

	public void updateAttributesFromConfig(CrossbowProperties properties) {
		this.MAX_CHARGE_TICKS = properties.CHARGE_TIME.get();
		this.DAMAGE = properties.DAMAGE.get().floatValue();
		this.VELOCITY = properties.VELOCITY.get().floatValue();
		this.INACCURACY = properties.INACCURACY.get().floatValue();
		this.onlyAllowMainHand = properties.ONLY_MAINHAND.get();
		float speedModifier = properties.SPEED_MODIFIER.get().floatValue();
		if (speedModifier != 0.0F) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(MOVEMENT_SPEED_UUID,
					"Tool modifier", speedModifier, AttributeModifier.Operation.MULTIPLY_TOTAL));
			this.onlyAllowMainHand = true;
			this.AttributeModifiers = builder.build();
		} else {
			this.AttributeModifiers = ImmutableMultimap.of();
		}
	}

	//Enchantment related
	public boolean AddGeneralEnchant(Enchantment enchantment) {
		return GeneralEnchants.add(enchantment);
	}

	public boolean RemoveGeneralEnchant(Enchantment enchantment) {
		return GeneralEnchants.remove(enchantment);
	}

	public boolean AddConflictEnchant(Enchantment enchantment) {
		return ConflictEnchants.add(enchantment);
	}

	public boolean RemoveConflictEnchant(Enchantment enchantment) {
		return ConflictEnchants.remove(enchantment);
	}

	public int getCrossbowEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
		return stack.getEnchantmentLevel(enchantment);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return EnchantmentValidator.canApplyAtTable(stack, enchantment, GeneralEnchants, ConflictEnchants);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return EnchantmentValidator.canBookEnchant(stack, book, GeneralEnchants, ConflictEnchants);
	}

	//Tooltip descriptions

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
		if (level == null || !level.isClientSide()) {
			super.appendHoverText(stack, level, tooltip, isAdvanced);
			return;
		}

		if (onlyAllowMainHand) {
			tooltip.add(Component.translatable("tooltip.perk_weapons.only_mainhand")
					.withStyle(ChatFormatting.RED));
		}

		TooltipHelper.addWeaponDescription(tooltip, getWeaponDescription(stack, level));
		TooltipHelper.addPerkDescription(tooltip, getPerkDescriptions(stack, level));

		int powerLevel = getCrossbowEnchantmentLevel(stack, POWER_ARROWS);
		float damage = (float) (DAMAGE * (powerLevel > 0 ? 1 + 0.25 * powerLevel : 1));
		tooltip.add(Component.translatable("tooltip.perk_weapons.attribute_damage",
						TooltipHelper.convertToEmbeddedElement(damage))
				.withStyle(ChatFormatting.DARK_AQUA));
		tooltip.add(Component.translatable("tooltip.perk_weapons.attribute_velocity",
						TooltipHelper.convertToEmbeddedElement(VELOCITY))
				.withStyle(ChatFormatting.DARK_AQUA));
		tooltip.add(Component.translatable("tooltip.perk_weapons.attribute_charge_time",
				TooltipHelper.convertToEmbeddedElement(TooltipHelper.convertTicksToSeconds(
						getMaxChargeTicks(stack)))).withStyle(ChatFormatting.DARK_AQUA));
		tooltip.add(Component.empty());

		ItemStack ammoStack = getLastChargedProjectile(stack);
		if (isCrossbowCharged(stack) && !ammoStack.isEmpty()) {
			Component ammo = ammoStack.getDisplayName().copy().withStyle(ChatFormatting.GRAY);
			tooltip.add(Component.translatable("tooltip.perk_weapons.crossbow_projectile", ammo)
					.withStyle(ChatFormatting.DARK_AQUA));
			if (isAdvanced.isAdvanced() && ammoStack.is(Items.FIREWORK_ROCKET)) {
				List<Component> list1 = Lists.newArrayList();
				Items.FIREWORK_ROCKET.appendHoverText(ammoStack, level, list1, isAdvanced);
				if (!list1.isEmpty()) {
					list1.replaceAll(pSibling -> Component.literal("  ")
							.append(pSibling).withStyle(ChatFormatting.GRAY));
					tooltip.addAll(list1);
				}
			}
		}
	}

	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		return List.of();
	}

	public Component getWeaponDescription(ItemStack stack, Level level) {
		return Component.empty();
	}
}