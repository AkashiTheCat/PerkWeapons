package net.akashi.perk_weapons.Crossbows;

import com.google.common.collect.Lists;
import net.akashi.perk_weapons.Client.ClientHelper;
import net.akashi.perk_weapons.Config.Properties.Crossbow.CrossbowProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.BaseArrow;
import net.akashi.perk_weapons.Network.ArrowVelocitySyncPayload;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Registry.ModTags;
import net.akashi.perk_weapons.Util.EnchantmentUtil;
import net.akashi.perk_weapons.Util.IDoubleLineCrosshairItem;
import net.akashi.perk_weapons.Util.SoundEventHolder;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class BaseCrossbowItem extends CrossbowItem implements IDoubleLineCrosshairItem {
	public static final ResourceLocation MOVEMENT_SPEED_ID = ResourceLocation.fromNamespaceAndPath("perk_weapons", "base_crossbow_movement_speed");
	public static Predicate<ItemStack> SUPPORTED_PROJECTILE = stack -> stack.is(Items.ARROW);
	public ItemAttributeModifiers DefaultAttributeModifiers = ItemAttributeModifiers.EMPTY;
	public boolean ONLY_ALLOW_MAINHAND = false;
	private static final String TAG_CHARGED = "Charged";
	private static final String TAG_CHARGED_PROJECTILES = "ChargedProjectiles";
	protected int AMMO_CAPACITY = 1;
	protected int MAX_CHARGE_TICKS = 25;
	protected int FIRE_INTERVAL = 0;
	protected float DAMAGE = 10.0F;
	protected float VELOCITY = 4.0F;
	protected float INACCURACY = 1.0F;
	protected float QUICK_CHARGE_RELOAD_TIME_REDUCTION = 5;
	protected float SPEED_MODIFIER = 0.0F;

	protected final Set<ResourceKey<Enchantment>> GeneralEnchants = new HashSet<>(Set.of(
			Enchantments.QUICK_CHARGE,
			Enchantments.MULTISHOT,
			Enchantments.PIERCING,
			Enchantments.POWER,
			Enchantments.MENDING,
			Enchantments.UNBREAKING,
			Enchantments.LOOTING
	));

	protected final Set<ResourceKey<Enchantment>> ConflictEnchants = new HashSet<>();

	public BaseCrossbowItem(Properties pProperties) {
		super(pProperties);
		if (FMLEnvironment.dist.isClient()) {
			ClientHelper.registerCrossbowPropertyOverrides(this);
		}
		buildAttributeModifiers();
	}

	/**
	 * To avoid a bug caused by the vanilla equipment update method, if speedModifier!=0, onlyAllowMainHand will be forced set true
	 **/
	public BaseCrossbowItem(int maxChargeTicks, float damage, float velocity,
						float inaccuracy, int ammoCapacity, int fireInterval,
						float speedModifier, boolean onlyAllowMainHand,
						Properties pProperties) {
		super(pProperties);
		this.MAX_CHARGE_TICKS = maxChargeTicks;
		this.DAMAGE = damage;
		this.VELOCITY = velocity;
		this.INACCURACY = inaccuracy;
		this.AMMO_CAPACITY = ammoCapacity;
		this.FIRE_INTERVAL = fireInterval;
		this.ONLY_ALLOW_MAINHAND = onlyAllowMainHand;
		this.SPEED_MODIFIER = speedModifier;
		buildAttributeModifiers();
		if (FMLEnvironment.dist.isClient()) {
			ClientHelper.registerCrossbowPropertyOverrides(this);
		}
	}

	//General overrides
	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(
			@NotNull Level pLevel,
			Player pPlayer,
			@NotNull InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		if (ONLY_ALLOW_MAINHAND && pHand != InteractionHand.MAIN_HAND) {
			return InteractionResultHolder.pass(itemstack);
		}

		if (isCrossbowCharged(itemstack)) {
			shoot(pLevel, pPlayer, pHand, itemstack, DAMAGE, VELOCITY, INACCURACY);
			consumeAndSetCharged(pPlayer, itemstack);
			if (getChargedProjectileAmount(itemstack) > 0) {
				pPlayer.getCooldowns().addCooldown(itemstack.getItem(), FIRE_INTERVAL);
			}
			return InteractionResultHolder.consume(itemstack);
		} else if (canLoadAmmo(pPlayer, itemstack)) {
			if (!isCrossbowCharged(itemstack)) {
				pPlayer.startUsingItem(pHand);
			}

			return InteractionResultHolder.consume(itemstack);
		} else {
			return InteractionResultHolder.fail(itemstack);
		}
	}

	@Override
	public void onUseTick(Level level, @NotNull LivingEntity livingEntity,
	                      @NotNull ItemStack crossbowStack, int useTimeLeft) {
		if (!level.isClientSide()) {
			SoundEventHolder startSoundEvent = this.getStartSound(livingEntity, crossbowStack);
			SoundEventHolder middleSoundEvent = this.getMiddleSound(livingEntity, crossbowStack);
			float progress = getChargeProgress(livingEntity, crossbowStack);

			if (progress >= 0.1F && progress < 0.12F && startSoundEvent.soundEvent != null) {
				level.playSound(null, livingEntity, startSoundEvent.soundEvent, SoundSource.PLAYERS,
						startSoundEvent.volume, startSoundEvent.pitch);
			}

			if (progress >= 0.6F && progress < 0.62F && middleSoundEvent.soundEvent != null) {
				level.playSound(null, livingEntity, middleSoundEvent.soundEvent, SoundSource.PLAYERS,
						middleSoundEvent.volume, middleSoundEvent.pitch);
			}

		}
	}

	@Override
	public void releaseUsing(@NotNull ItemStack crossbowStack, @NotNull Level level,
	                         @NotNull LivingEntity shooter, int useTimeLeft) {
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
				SoundEventHolder endSound = getEndSound(shooter, crossbowStack);
				if (endSound.soundEvent != null) {
					float pitch = endSound.pitch / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F;
					level.playSound(null, shooter, endSound.soundEvent, soundsource, endSound.volume, pitch);
				}
			}
		}
	}
	public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
		return UseAnim.CROSSBOW;
	}

	@Override
	public int getUseDuration(@NotNull ItemStack pStack, @NotNull LivingEntity pEntity) {
		return 72000;
	}

	public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
		return BaseCrossbowItem.SUPPORTED_PROJECTILE;
	}

	public boolean shouldCauseReequipAnimation(@NotNull ItemStack oldStack, @NotNull ItemStack newStack, boolean slotChanged) {
		return slotChanged || !newStack.is(oldStack.getItem());
	}

	public float getChokeProgress(LivingEntity shooter, ItemStack stack) {
		return isCrossbowCharged(stack) ? 1.0f : getChargeProgress(shooter, stack);
	}

	@Override
	public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers(@NotNull ItemStack stack) {
		return this.DefaultAttributeModifiers;
	}

	//Weapon mechanic logics
	protected void shoot(Level level, LivingEntity shooter, InteractionHand hand, ItemStack crossbowStack,
	                     float damage, float velocity, float inaccuracy) {
		if (shooter instanceof Player player && net.neoforged.neoforge.event.EventHooks.onArrowLoose(crossbowStack,
				shooter.level(), player, 1, true) < 0) return;

		int multiShotLevel = getEnchantmentLevel(crossbowStack, Enchantments.MULTISHOT);
		int pierceLevel = getEnchantmentLevel(crossbowStack, Enchantments.PIERCING);
		int powerLevel = getEnchantmentLevel(crossbowStack, Enchantments.POWER);

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

	protected boolean canLoadAmmo(LivingEntity shooter, ItemStack crossbowStack) {
		if (shooter instanceof Player player) {
			return !player.getProjectile(crossbowStack).isEmpty();
		}
		return true;
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

			if (isShooterPlayer && !isCreative && getEnchantmentLevel(crossbowStack, Enchantments.INFINITY) == 0) {
				ammoStack.shrink(1);
				if (ammoStack.isEmpty())
					((Player) shooter).getInventory().removeItem(ammoStack);
			}

		addChargedProjectile(shooter.level(), crossbowStack, ammoToLoad);
		return true;
	}

	public int getMaxChargeTicks(ItemStack crossbowStack) {
		int quickChargeLevel = getEnchantmentLevel(crossbowStack, Enchantments.QUICK_CHARGE);
		return Math.max(1, (int) Math.ceil(MAX_CHARGE_TICKS - QUICK_CHARGE_RELOAD_TIME_REDUCTION * quickChargeLevel)
		);
	}

	public byte getChargeProgressFrom0To10(LivingEntity shooter, ItemStack crossbowStack) {
		return (byte) Math.floor(getChargeProgress(shooter, crossbowStack) * 10);
	}

	public float getChargeProgress(LivingEntity shooter, ItemStack crossbowStack) {
		return Math.min((float) shooter.getTicksUsingItem() / getMaxChargeTicks(crossbowStack), 1.0f);
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
				if (projectile instanceof net.akashi.perk_weapons.Entities.Projectiles.Arrows.BaseArrow baseArrow) {
					baseArrow.setPierceLevel(pierceLevel);
				}
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


			crossbowStack.hurtAndBreak(durabilityCost, shooter, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
		level.addFreshEntity(projectile);

		//Sync velocity to all clients
		PacketDistributor.sendToAllPlayers(new ArrowVelocitySyncPayload(
				projectile.getDeltaMovement().x,
				projectile.getDeltaMovement().y,
				projectile.getDeltaMovement().z,
				projectile.getId()));

		SoundEventHolder shootSound = getShootSound(shooter, crossbowStack);
		if (shootSound.soundEvent != null) {
			level.playSound(null, shooter, shootSound.soundEvent, SoundSource.PLAYERS,
					shootSound.volume, getShotPitch(shootSound.pitch, shooter.getRandom()));
		}
	}

	protected Projectile getProjectile(Level level, LivingEntity shooter, ItemStack crossbowStack) {
		BaseCrossbowItem crossbowItem = (BaseCrossbowItem) crossbowStack.getItem();
		ItemStack ammoStack = crossbowItem.getLastChargedProjectile(level, crossbowStack);

		if (ammoStack.is(Items.FIREWORK_ROCKET)) {
			return new FireworkRocketEntity(level, ammoStack, shooter, shooter.getX(),
					shooter.getEyeY() - (double) 0.15F, shooter.getZ(), true);
		}

		BaseArrow arrow = new BaseArrow(ModEntities.BASE_ARROW.get(), level, shooter);
		if (ammoStack.is(Items.SPECTRAL_ARROW)) {
			arrow.setSpectralArrow(true);
		} else {
			arrow.setEffectsFromItem(ammoStack);
		}
		arrow.setShotFromCrossbow(true);
		return arrow;
	}

	public void addChargedProjectile(Level level, ItemStack crossbowStack, ItemStack ammoStack) {
		List<ItemStack> charged = new ArrayList<>(crossbowStack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY).getItems());
		charged.add(ammoStack.copy());
		crossbowStack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(charged));
	}

	public List<ItemStack> getChargedProjectiles(Level level, ItemStack crossbowStack) {
		List<ItemStack> charged = Lists.newArrayList(crossbowStack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY).getItems());
		if (!charged.isEmpty() || level == null) {
			return charged;
		}

		ListTag listtag = getChargedProjectileListTag(crossbowStack);
		for (int i = 0; i < listtag.size(); ++i) {
			CompoundTag ammoTag = listtag.getCompound(i);
			charged.add(ItemStack.parseOptional(level.registryAccess(), ammoTag));
		}
		return charged;
	}

	public ListTag getChargedProjectileListTag(ItemStack crossbowStack) {
		CompoundTag compoundtag = crossbowStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		return compoundtag.contains(TAG_CHARGED_PROJECTILES, 9) ? compoundtag.getList(TAG_CHARGED_PROJECTILES, 10) : new ListTag();
	}

	public int getChargedProjectileAmount(ItemStack crossbowStack) {
		ChargedProjectiles charged = crossbowStack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
		return !charged.isEmpty() ? charged.getItems().size() : getChargedProjectileListTag(crossbowStack).size();
	}

	public ItemStack getLastChargedProjectile(Level level, ItemStack crossbowStack) {
		List<ItemStack> ammoList = getChargedProjectiles(level, crossbowStack);
		int size = ammoList.size();
		return size > 0 ? ammoList.getLast() : ItemStack.EMPTY;
	}

	public void clearChargedProjectiles(ItemStack crossbowStack) {
		crossbowStack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
		CustomData.update(DataComponents.CUSTOM_DATA, crossbowStack, tag -> tag.putBoolean(TAG_CHARGED, false));
	}

	public void consumeAndSetCharged(LivingEntity shooter, ItemStack crossbowStack) {
		List<ItemStack> charged = new ArrayList<>(crossbowStack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY).getItems());
		if (!charged.isEmpty()) {
			charged.removeLast();
			crossbowStack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(charged));
		}
		setCrossbowCharged(crossbowStack, !charged.isEmpty());
	}

	public boolean isCrossbowCharged(ItemStack crossbowStack) {
		CompoundTag tag = crossbowStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		if (tag.contains(TAG_CHARGED) && tag.getBoolean(TAG_CHARGED)) {
			return true;
		}
		if (this instanceof AutoLoadingCrossbowItem) {
			return false;
		}
		ChargedProjectiles projectiles = crossbowStack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
		return !projectiles.isEmpty();
	}

	public void setCrossbowCharged(ItemStack crossbowStack, boolean charged) {
		CustomData.update(DataComponents.CUSTOM_DATA, crossbowStack, tag -> tag.putBoolean(TAG_CHARGED, charged));
		if (!charged) {
			crossbowStack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
		}
	}

	public boolean isFireworkCharged(ItemStack crossbowStack) {
		ChargedProjectiles charged = crossbowStack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
		if (!charged.isEmpty()) {
			List<ItemStack> items = charged.getItems();
			return !items.isEmpty() && items.getLast().is(Items.FIREWORK_ROCKET);
		}

		if (crossbowStack.getItem() instanceof BaseCrossbowItem crossbowItem) {
			ListTag listtag = crossbowItem.getChargedProjectileListTag(crossbowStack);
			return !listtag.isEmpty() && listtag.getCompound(listtag.size() - 1).getString("id").contains("firework_rocket");
		}
		return false;
	}

	public int getAmmoCapacity(ItemStack crossbowStack) {
		return AMMO_CAPACITY;
	}

	//Miscellaneous
	protected static float getShotPitch(RandomSource pRandom) {
		boolean flag = pRandom.nextBoolean();
		return getRandomShotPitch(1F, flag, pRandom);
	}

	protected static float getShotPitch(float basePitch, RandomSource pRandom) {
		boolean flag = pRandom.nextBoolean();
		return getRandomShotPitch(basePitch, flag, pRandom);
	}

	protected static float getRandomShotPitch(float basePitch, boolean pIsHighPitched, RandomSource pRandom) {
		float f = basePitch * (pIsHighPitched ? 0.63F : 0.43F);
		return 1 / (pRandom.nextFloat() * 0.5F + 1.8F) + f;
	}

	@NotNull
	protected SoundEventHolder getStartSound(LivingEntity shooter, ItemStack crossbowStack) {
		return switch (getEnchantmentLevel(crossbowStack, Enchantments.QUICK_CHARGE)) {
			case 1 -> new SoundEventHolder(SoundEvents.CROSSBOW_QUICK_CHARGE_1, 0.5F, 1F);
			case 2 -> new SoundEventHolder(SoundEvents.CROSSBOW_QUICK_CHARGE_2, 0.5F, 1F);
			case 3 -> new SoundEventHolder(SoundEvents.CROSSBOW_QUICK_CHARGE_3, 0.5F, 1F);
			default -> new SoundEventHolder(SoundEvents.CROSSBOW_LOADING_START, 0.5F, 1F);
		};
	}

	@NotNull
	protected SoundEventHolder getMiddleSound(LivingEntity shooter, ItemStack crossbowStack) {
		return getMaxChargeTicks(crossbowStack) > 20 ? new SoundEventHolder(SoundEvents.CROSSBOW_LOADING_MIDDLE,
				0.5F, 1F) : SoundEventHolder.empty();
	}

	@NotNull
	protected SoundEventHolder getEndSound(LivingEntity shooter, ItemStack crossbowStack) {
		return new SoundEventHolder(SoundEvents.CROSSBOW_LOADING_END);
	}

	@NotNull
	protected SoundEventHolder getShootSound(LivingEntity shooter, ItemStack crossbowStack) {
		return new SoundEventHolder(SoundEvents.CROSSBOW_SHOOT);
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
		this.AMMO_CAPACITY = properties.AMMO_CAPACITY.get();
		this.FIRE_INTERVAL = properties.FIRE_INTERVAL.get();
		this.ONLY_ALLOW_MAINHAND = properties.ONLY_MAINHAND.get();
		this.QUICK_CHARGE_RELOAD_TIME_REDUCTION = (float) (5 * properties.QUICK_CHARGE_MULTIPLIER.get());
		this.SPEED_MODIFIER = properties.SPEED_MODIFIER.get().floatValue();
		buildAttributeModifiers();
	}

	protected void buildAttributeModifiers() {
		if (SPEED_MODIFIER != 0.0F) {
			ItemAttributeModifiers.Builder defaultBuilder = ItemAttributeModifiers.builder();
			AttributeModifier speedModifier = new AttributeModifier(MOVEMENT_SPEED_ID,
					SPEED_MODIFIER, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
			defaultBuilder.add(Attributes.MOVEMENT_SPEED, speedModifier, EquipmentSlotGroup.MAINHAND);
			this.ONLY_ALLOW_MAINHAND = true;
			this.DefaultAttributeModifiers = defaultBuilder.build();
		} else {
			this.DefaultAttributeModifiers = ItemAttributeModifiers.EMPTY;
		}
	}

	//Enchantment related
	public boolean AddGeneralEnchant(Holder<Enchantment> enchantment) {
		if (enchantment == null) {
			return false;
		}
		return enchantment.unwrapKey().map(GeneralEnchants::add).orElse(false);
	}

	public boolean AddGeneralEnchant(ResourceKey<Enchantment> enchantment) {
		return GeneralEnchants.add(enchantment);
	}

	public boolean RemoveGeneralEnchant(Holder<Enchantment> enchantment) {
		if (enchantment == null) {
			return false;
		}
		return enchantment.unwrapKey().map(GeneralEnchants::remove).orElse(false);
	}

	public boolean RemoveGeneralEnchant(ResourceKey<Enchantment> enchantment) {
		return GeneralEnchants.remove(enchantment);
	}

	public boolean AddConflictEnchant(Holder<Enchantment> enchantment) {
		if (enchantment == null) {
			return false;
		}
		return enchantment.unwrapKey().map(ConflictEnchants::add).orElse(false);
	}

	public boolean AddConflictEnchant(ResourceKey<Enchantment> enchantment) {
		return ConflictEnchants.add(enchantment);
	}

	public boolean RemoveConflictEnchant(Holder<Enchantment> enchantment) {
		if (enchantment == null) {
			return false;
		}
		return enchantment.unwrapKey().map(ConflictEnchants::remove).orElse(false);
	}

	public boolean RemoveConflictEnchant(ResourceKey<Enchantment> enchantment) {
		return ConflictEnchants.remove(enchantment);
	}

	public int getEnchantmentLevel(ItemStack stack, ResourceKey<Enchantment> enchantment) {
		return EnchantmentUtil.getLevel(stack, enchantment);
	}

	@Override
	public boolean supportsEnchantment(@NotNull ItemStack stack, @NotNull Holder<Enchantment> enchantment) {
		return EnchantmentUtil.supportsEnchantment(stack, enchantment, GeneralEnchants, ConflictEnchants);
	}

	public boolean isBookEnchantable(@NotNull ItemStack stack, @NotNull ItemStack book) {
		return EnchantmentUtil.canBookEnchant(stack, book, GeneralEnchants, ConflictEnchants);
	}

	//Tooltip descriptions

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull List<Component> tooltip,
	                            @NotNull TooltipFlag isAdvanced) {
		super.appendHoverText(stack, context, tooltip, isAdvanced);

		if (ONLY_ALLOW_MAINHAND) {
			tooltip.add(Component.translatable("tooltip.perk_weapons.only_mainhand")
					.withStyle(ChatFormatting.RED));
		}

		TooltipHelper.addWeaponDescription(tooltip, getWeaponDescription(stack, null));
		TooltipHelper.addPerkDescription(tooltip, getPerkDescriptions(stack, null));

		int powerLevel = getEnchantmentLevel(stack, Enchantments.POWER);
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
		tooltip.add(Component.translatable("tooltip.perk_weapons.crossbow_ammo_capacity",
				TooltipHelper.convertToEmbeddedElement(getAmmoCapacity(stack))).withStyle(ChatFormatting.DARK_AQUA));
		tooltip.add(Component.translatable("tooltip.perk_weapons.crossbow_fire_interval",
				TooltipHelper.convertToEmbeddedElement(TooltipHelper.convertTicksToSeconds(
						FIRE_INTERVAL))).withStyle(ChatFormatting.DARK_AQUA));
		tooltip.add(Component.empty());

		ItemStack ammoStack = getLastChargedProjectile(null, stack);
		if (isCrossbowCharged(stack) && !ammoStack.isEmpty()) {
			Component ammo = ammoStack.getDisplayName().copy().withStyle(ChatFormatting.GRAY);
			tooltip.add(Component.translatable("tooltip.perk_weapons.crossbow_projectile", ammo)
					.withStyle(ChatFormatting.DARK_AQUA));
			if (isAdvanced.isAdvanced() && ammoStack.is(Items.FIREWORK_ROCKET)) {
				List<Component> list1 = Lists.newArrayList();
				Items.FIREWORK_ROCKET.appendHoverText(ammoStack, context, list1, isAdvanced);
				if (!list1.isEmpty()) {
					list1.replaceAll(pSibling -> Component.literal("  ")
							.append(pSibling).withStyle(ChatFormatting.GRAY));
					tooltip.addAll(list1);
				}
			}
		}

		Component ammo = Component.literal(String.valueOf(this.getChargedProjectileAmount(stack)))
				.withStyle(ChatFormatting.GRAY);
		tooltip.add(Component.translatable("tooltip.perk_weapons.crossbow_ammo_amount", ammo)
				.withStyle(ChatFormatting.DARK_AQUA));

		Minecraft mc = Minecraft.getInstance();
		Component crouch = mc.options.keyShift.getTranslatedKeyMessage().copy()
				.withStyle(ChatFormatting.AQUA);
		Component attack = mc.options.keyAttack.getTranslatedKeyMessage().copy()
				.withStyle(ChatFormatting.AQUA);
		tooltip.add(Component.translatable("tooltip.perk_weapons.ammo_unload_hint",
				crouch, attack).withStyle(ChatFormatting.DARK_GRAY));
	}

	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		List<Component> list = new ArrayList<>();
		if (stack.is(ModTags.NO_USING_SLOWDOWN_TAG))
			list.add(TooltipHelper.setEmbeddedElementStyle(Component.translatable("tooltip.perk_weapons.no_using_slowdown_perk")));
		return list;
	}

	public Component getWeaponDescription(ItemStack stack, Level level) {
		return Component.empty();
	}
}
