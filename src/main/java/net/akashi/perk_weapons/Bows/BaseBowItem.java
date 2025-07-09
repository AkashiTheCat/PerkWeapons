package net.akashi.perk_weapons.Bows;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.akashi.perk_weapons.Client.ClientHelper;
import net.akashi.perk_weapons.Config.Properties.Bow.BowProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.BaseArrow;
import net.akashi.perk_weapons.Network.ArrowVelocitySyncPacket;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Registry.ModPackets;
import net.akashi.perk_weapons.Util.EnchantmentValidator;
import net.akashi.perk_weapons.Util.IDoubleLineCrosshairItem;
import net.akashi.perk_weapons.Util.SoundEventHolder;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

import static net.minecraft.world.item.enchantment.Enchantments.*;

public class BaseBowItem extends BowItem implements Vanishable, IDoubleLineCrosshairItem {
	protected static final SoundEventHolder SHOOTING_SOUND = new SoundEventHolder(SoundEvents.ARROW_SHOOT, 1.0F, 1.2F);
	public static final UUID MOVEMENT_SPEED_UUID = UUID.fromString("DB3F25A3-255C-8F4A-B293-EA1BA59D27CE");
	public static Predicate<ItemStack> SUPPORTED_PROJECTILE = (stack) -> stack.is(Items.ARROW);
	public Multimap<Attribute, AttributeModifier> AttributeModifiers;
	public boolean onlyAllowMainHand = false;
	public float VELOCITY = 3.0F;
	public int DRAW_TIME = 20;
	public float PROJECTILE_DAMAGE = 10;
	public float ZOOM_FACTOR = 0.1f;
	public float INACCURACY = 1.0f;

	private final Set<Enchantment> GeneralEnchants = new HashSet<>(Set.of(
			INFINITY_ARROWS,
			FLAMING_ARROWS,
			POWER_ARROWS,
			PUNCH_ARROWS,
			MENDING,
			UNBREAKING,
			MOB_LOOTING
	));
	private final Set<Enchantment> ConflictEnchants = new HashSet<>();

	@Override
	public boolean isValidRepairItem(@NotNull ItemStack pStack, @NotNull ItemStack pRepairCandidate) {
		return super.isValidRepairItem(pStack, pRepairCandidate);
	}

	public BaseBowItem(Properties properties) {
		super(properties);
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerBowPropertyOverrides(this);
	}

	/**
	 * To avoid a bug caused by the vanilla equipment update method, if speedModifier!=0, onlyAllowMainHand will be forced set true
	 **/
	public BaseBowItem(int drawTime, float projectileDamage, float velocity, float inaccuracy, float speedModifier,
	                   float zoomFactor, boolean onlyAllowMainHand, Properties properties) {
		super(properties);
		this.VELOCITY = velocity;
		this.DRAW_TIME = drawTime;
		this.PROJECTILE_DAMAGE = projectileDamage;
		this.ZOOM_FACTOR = zoomFactor;
		this.INACCURACY = inaccuracy;
		this.onlyAllowMainHand = onlyAllowMainHand;
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerBowPropertyOverrides(this);
		if (speedModifier != 0.0F) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(MOVEMENT_SPEED_UUID,
					"Tool modifier", speedModifier, AttributeModifier.Operation.MULTIPLY_TOTAL));
			this.AttributeModifiers = builder.build();
			this.onlyAllowMainHand = true;
		} else {
			this.AttributeModifiers = ImmutableMultimap.of();
		}
	}

	//General overrides

	@Override
	public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
		return BaseBowItem.SUPPORTED_PROJECTILE;
	}

	@Override
	public @NotNull Predicate<ItemStack> getSupportedHeldProjectiles() {
		return BaseBowItem.SUPPORTED_PROJECTILE;
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged || newStack.getItem() != oldStack.getItem();
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		return slot == EquipmentSlot.MAINHAND ? AttributeModifiers : ImmutableMultimap.of();
	}

	@Override
	public void releaseUsing(@NotNull ItemStack pStack, @NotNull Level pLevel,
	                         @NotNull LivingEntity pEntityLiving, int pTimeLeft) {
		if (pEntityLiving instanceof Player player) {
			boolean flag = player.getAbilities().instabuild || pStack.getEnchantmentLevel(INFINITY_ARROWS) > 0;
			ItemStack itemstack = player.getProjectile(pStack);

			int i = this.getUseDuration(pStack) - pTimeLeft;
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(pStack, pLevel, player, i, !itemstack.isEmpty() || flag);
			if (i < DRAW_TIME) return;

			if (!itemstack.isEmpty() || flag) {
				if (itemstack.isEmpty()) {
					itemstack = new ItemStack(Items.ARROW);
				}

				boolean flag1 = player.getAbilities().instabuild
						|| (itemstack.getItem() instanceof ArrowItem
						&& ((ArrowItem) itemstack.getItem()).isInfinite(itemstack, pStack, player));
				if (!pLevel.isClientSide) {
					SoundEventHolder shootSound = getShootingSound(pEntityLiving, pStack);
					if (shootSound.soundEvent != null) {
						pLevel.playSound(null, player, shootSound.soundEvent, SoundSource.PLAYERS, shootSound.volume,
								1.0F / (pLevel.getRandom().nextFloat() * 0.4F + shootSound.pitch) + (float) 10 / DRAW_TIME);
					}

					ArrowItem arrowitem = (ArrowItem) (itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);

					AbstractArrow abstractarrow = createArrow(pLevel, arrowitem, pStack, itemstack, player);
					abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, VELOCITY, INACCURACY);

					abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() * getDamageMultiplier(pStack));

					//Punch
					int punchLevel = pStack.getEnchantmentLevel(PUNCH_ARROWS);
					if (punchLevel > 0) {
						abstractarrow.setKnockback(punchLevel);
					}
					//Flame
					if (pStack.getEnchantmentLevel(FLAMING_ARROWS) > 0) {
						abstractarrow.setSecondsOnFire(100);
					}

					pStack.hurtAndBreak(1, player, (player1) -> {
						player1.broadcastBreakEvent(player.getUsedItemHand());
					});
					if (flag1 || player.getAbilities().instabuild
							&& (itemstack.is(Items.SPECTRAL_ARROW)
							|| itemstack.is(Items.TIPPED_ARROW))) {
						abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
					}
					pLevel.addFreshEntity(abstractarrow);

					//Sync velocity to all clients
					ModPackets.NETWORK.send(PacketDistributor.ALL.noArg(),
							new ArrowVelocitySyncPacket(abstractarrow.getDeltaMovement(), abstractarrow.getId()));
				}

				if (!flag1 && !player.getAbilities().instabuild) {
					itemstack.shrink(1);
					if (itemstack.isEmpty()) {
						player.getInventory().removeItem(itemstack);
					}
				}

				player.awardStat(Stats.ITEM_USED.get(this));
			}
		}
	}

	@NotNull
	protected SoundEventHolder getShootingSound(LivingEntity shooter, ItemStack stack) {
		return SHOOTING_SOUND;
	}

	@Override
	public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
		return UseAnim.BOW;
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		if (onlyAllowMainHand && pHand != InteractionHand.MAIN_HAND) {
			return InteractionResultHolder.pass(itemstack);
		}

		boolean flag = !pPlayer.getProjectile(itemstack).isEmpty();

		InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(
				itemstack, pLevel, pPlayer, pHand, flag);
		if (ret != null) return ret;

		if (!pPlayer.getAbilities().instabuild && !flag) {
			return InteractionResultHolder.fail(itemstack);
		} else {
			pPlayer.startUsingItem(pHand);
			return InteractionResultHolder.consume(itemstack);
		}
	}

	//Enchantments

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return EnchantmentValidator.canApplyAtTable(stack, enchantment, GeneralEnchants, ConflictEnchants);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return EnchantmentValidator.canBookEnchant(stack, book, GeneralEnchants, ConflictEnchants);
	}

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

	//New methods

	public AbstractArrow createArrow(Level level, ArrowItem arrowItem, ItemStack bowStack, ItemStack arrowStack, Player player) {
		BaseArrow arrow = new BaseArrow(ModEntities.BASE_ARROW.get(), level, player);
		if (arrowItem instanceof SpectralArrowItem) {
			arrow.setSpectralArrow(true);
		} else {
			arrow.setEffectsFromItem(arrowStack);
		}
		arrow.setBaseDamage(PROJECTILE_DAMAGE / VELOCITY);
		return arrow;
	}

	public double getDamageMultiplier(ItemStack stack) {
		int powerLevel = stack.getEnchantmentLevel(POWER_ARROWS);
		return 1F + 0.25F * powerLevel;
	}

	public void updateAttributesFromConfig(BowProperties properties) {
		this.DRAW_TIME = properties.DRAW_TIME.get();
		this.PROJECTILE_DAMAGE = properties.DAMAGE.get().floatValue();
		this.VELOCITY = properties.VELOCITY.get().floatValue();
		this.ZOOM_FACTOR = properties.ZOOM_FACTOR.get().floatValue();
		this.INACCURACY = properties.INACCURACY.get().floatValue();
		this.onlyAllowMainHand = properties.ONLY_MAINHAND.get();
		float speedModifier = properties.SPEED_MODIFIER.get().floatValue();
		if (speedModifier != 0.0F) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(MOVEMENT_SPEED_UUID,
					"Tool modifier", speedModifier, AttributeModifier.Operation.MULTIPLY_TOTAL));
			this.AttributeModifiers = builder.build();
			this.onlyAllowMainHand = true;
		} else {
			this.AttributeModifiers = ImmutableMultimap.of();
		}
	}

	public float getDrawProgress(LivingEntity shooter) {
		return shooter.getTicksUsingItem() < DRAW_TIME ? (float) shooter.getTicksUsingItem() / DRAW_TIME : 1;
	}

	@Override
	public float getChokeProgress(LivingEntity shooter, ItemStack stack) {
		return getDrawProgress(shooter);
	}

	//Tooltips

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip,
	                            @NotNull TooltipFlag isAdvanced) {
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

		tooltip.add(Component.translatable("tooltip.perk_weapons.attribute_damage",
						TooltipHelper.convertToEmbeddedElement(PROJECTILE_DAMAGE * getDamageMultiplier(stack)))
				.withStyle(ChatFormatting.DARK_AQUA));
		tooltip.add(Component.translatable("tooltip.perk_weapons.attribute_velocity",
						TooltipHelper.convertToEmbeddedElement(VELOCITY))
				.withStyle(ChatFormatting.DARK_AQUA));
		tooltip.add(Component.translatable("tooltip.perk_weapons.attribute_draw_time",
						TooltipHelper.convertToEmbeddedElement(TooltipHelper.convertTicksToSeconds(DRAW_TIME)))
				.withStyle(ChatFormatting.DARK_AQUA));

		super.appendHoverText(stack, level, tooltip, isAdvanced);
	}

	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		return List.of();
	}

	public Component getWeaponDescription(ItemStack stack, Level level) {
		return Component.empty();
	}
}
