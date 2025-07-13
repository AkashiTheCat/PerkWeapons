package net.akashi.perk_weapons.Bows;

import net.akashi.perk_weapons.Client.ClientHelper;
import net.akashi.perk_weapons.Config.Properties.Bow.BowProperties;
import net.akashi.perk_weapons.Config.Properties.Bow.EndboreWandererProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.BaseArrow;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.EndboreWandererArrow;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.EndboreWandererPerkProjectile;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Util.IPerkItem;
import net.akashi.perk_weapons.Util.SoundEventHolder;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpectralArrowItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EndboreWandererItem extends BaseBowItem implements IPerkItem {
	protected SoundEventHolder SHOOTING_SOUND_PERK = new SoundEventHolder(SoundEvents.SHULKER_SHOOT);

	private static byte MAX_PERK_LEVEL = 3;
	private static int CROUCH_USE_COOLDOWN_TICKS = 10;

	public static float DAMAGE_BONUS_LEVITATION = 0.5F;
	public static int PERK_PROJECTILE_LEVITATION_TICKS_ON_HIT = 60;
	public static float PERK_PROJECTILE_HOMING_RANGE = 8;
	public static float MAX_HOMING_ANGLE_COS_VALUE = (float) Math.cos(Math.toRadians(70));
	public static float MAX_PROJECTILE_TURN_ANGLE_PER_TICK_COS_VALUE = (float) Math.cos(Math.toRadians(7));
	public static float MAX_PROJECTILE_TURN_ANGLE_PER_TICK_SIN_VALUE = (float) Math.sin(Math.toRadians(7));
	public static float HOMING_ACCELERATION = 0.025F;

	public EndboreWandererItem(Properties properties) {
		super(properties);
		if (FMLEnvironment.dist.isClient()) {
			ClientHelper.registerPerkItemPropertyOverrides(this);
		}
	}

	public EndboreWandererItem(int drawTime, float projectileDamage, float velocity,
	                           float inaccuracy, float speedModifier, float zoomFactor,
	                           boolean onlyAllowMainHand, Properties properties) {
		super(drawTime, projectileDamage, velocity, inaccuracy, speedModifier, zoomFactor,
				onlyAllowMainHand, properties);
		if (FMLEnvironment.dist.isClient()) {
			ClientHelper.registerPerkItemPropertyOverrides(this);
		}
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {
		if (pPlayer.isCrouching() && !pLevel.isClientSide()) {
			ItemStack itemStack = pPlayer.getItemInHand(pHand);
			if (isPerkMax(pPlayer, itemStack)) {
				pLevel.playSound(null, pPlayer, SoundEvents.SHULKER_SHOOT, SoundSource.PLAYERS,
						1.0F, 1.0F);

				ThrownEnderpearl thrownenderpearl = new ThrownEnderpearl(pLevel, pPlayer);
				thrownenderpearl.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(),
						0.0F, 1.5F, 1.0F);
				pLevel.addFreshEntity(thrownenderpearl);

				pPlayer.getCooldowns().addCooldown(itemStack.getItem(), CROUCH_USE_COOLDOWN_TICKS);
				setPerkLevel(itemStack, (byte) 0);

				return InteractionResultHolder.success(itemStack);
			}
		}
		return super.use(pLevel, pPlayer, pHand);
	}

	@Override
	protected @NotNull SoundEventHolder getShootingSound(LivingEntity entity, ItemStack stack) {
		return isPerkMax(entity, stack) ? SHOOTING_SOUND_PERK : super.getShootingSound(entity, stack);
	}

	@Override
	public AbstractArrow createArrow(Level level, ArrowItem arrowItem, ItemStack bowStack,
	                                 ItemStack arrowStack, Player player) {
		BaseArrow arrow;
		if (this.isPerkMax(player, bowStack)) {
			arrow = new EndboreWandererPerkProjectile(ModEntities.ENDBORE_WANDERER_PERK_PROJECTILE.get(), level, player);
			arrow.setNoGravity(true);
			this.setPerkLevel(bowStack, (byte) 0);
		} else {
			arrow = new EndboreWandererArrow(ModEntities.ENDBORE_WANDERER_ARROW.get(), level, player);
		}

		if (arrowItem instanceof SpectralArrowItem) {
			arrow.setSpectralArrow(true);
		} else {
			arrow.setEffectsFromItem(arrowStack);
		}
		arrow.setBaseDamage(PROJECTILE_DAMAGE / VELOCITY);
		return arrow;
	}

	@Override
	public byte getMaxPerkLevel() {
		return MAX_PERK_LEVEL;
	}

	@Override
	public void updateAttributesFromConfig(BowProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof EndboreWandererProperties eProperties) {
			MAX_PERK_LEVEL = eProperties.MAX_PERK_LEVEL.get().byteValue();
			CROUCH_USE_COOLDOWN_TICKS = eProperties.CROUCH_USE_COOLDOWN.get();
			DAMAGE_BONUS_LEVITATION = eProperties.DAMAGE_BONUS_LEVITATION.get().floatValue();
			PERK_PROJECTILE_LEVITATION_TICKS_ON_HIT = eProperties.PERK_PROJECTILE_LEVITATION_TICKS_ON_HIT.get();
			PERK_PROJECTILE_HOMING_RANGE = eProperties.PERK_PROJECTILE_HOMING_RANGE.get().floatValue();
			HOMING_ACCELERATION = eProperties.PERK_PROJECTILE_HOMING_ACCELERATION.get().floatValue();

			MAX_HOMING_ANGLE_COS_VALUE = (float) Math.cos(Math.toRadians(eProperties.PERK_PROJECTILE_MAX_HOMING_ANGLE.get()));
			double radTurnRate = Math.toRadians(eProperties.PERK_PROJECTILE_MAX_TURN_RATE.get());
			MAX_PROJECTILE_TURN_ANGLE_PER_TICK_COS_VALUE = (float) Math.cos(radTurnRate);
			MAX_PROJECTILE_TURN_ANGLE_PER_TICK_SIN_VALUE = (float) Math.sin(radTurnRate);
		}
	}

	@Override
	public Component getWeaponDescription(ItemStack stack, Level level) {
		return TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.endbore_wanderer"));
	}

	@Override
	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		var list = super.getPerkDescriptions(stack, level);
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.endbore_wanderer_perk_1",
				TooltipHelper.convertToEmbeddedPercentage(1 + DAMAGE_BONUS_LEVITATION),
				TooltipHelper.setEmbeddedElementStyle(MobEffects.LEVITATION.getDisplayName().copy()))));

		list.add(Component.empty());

		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.gain_perk_level_on_hit",
				TooltipHelper.convertToEmbeddedElement(1))));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.max_perk_level",
				TooltipHelper.convertToEmbeddedElement(MAX_PERK_LEVEL))));

		list.add(Component.translatable("tooltip.perk_weapons.when_reach_max_perk_level").withStyle(ChatFormatting.GRAY));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.endbore_wanderer_perk_2")));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.endbore_wanderer_perk_3")));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.endbore_wanderer_perk_4",
				TooltipHelper.convertToEmbeddedElement(PERK_PROJECTILE_HOMING_RANGE))));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.endbore_wanderer_perk_5")));
		list.add(TooltipHelper.setSubPerkStyle(Component.translatable("tooltip.perk_weapons.effect_format",
				MobEffects.LEVITATION.getDisplayName(),
				TooltipHelper.getRomanNumeral(1),
				TooltipHelper.convertTicksToSeconds(PERK_PROJECTILE_LEVITATION_TICKS_ON_HIT))));
		list.add(TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.endbore_wanderer_perk_6")));

		list.add(Component.empty());
		list.add(TooltipHelper.getCrouchUseAbilityHint());
		list.add(Component.translatable("tooltip.perk_weapons.when_reach_max_perk_level").withStyle(ChatFormatting.GRAY));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.endbore_wanderer_ability_1")));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.endbore_wanderer_ability_2")));

		return list;
	}
}
