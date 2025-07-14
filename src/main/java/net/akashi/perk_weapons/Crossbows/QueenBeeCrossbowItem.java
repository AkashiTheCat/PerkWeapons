package net.akashi.perk_weapons.Crossbows;

import net.akashi.perk_weapons.Client.ClientHelper;
import net.akashi.perk_weapons.Config.Properties.Crossbow.CrossbowProperties;
import net.akashi.perk_weapons.Config.Properties.Crossbow.QueenBeeProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.BaseArrow;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.QueenBeeArrow;
import net.akashi.perk_weapons.Registry.ModEffects;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class QueenBeeCrossbowItem extends BaseCrossbowItem implements IPerkItem {
	protected static final SoundEventHolder LOADING_START_SOUND = new SoundEventHolder(SoundEvents.BEE_POLLINATE,
			1.0F, 1.0F);
	protected static final SoundEventHolder SHOOTING_SOUND = new SoundEventHolder(SoundEvents.BEE_STING,
			0.7F, 1.0F);

	private static int POISON_LEVEL = 4;
	private static int POISON_TICKS = 100;
	private static byte MAX_PERK_LEVEL = 7;
	private static int ROYAL_JELLY_LEVEL = 1;
	private static int ROYAL_JELLY_TICKS = 80;
	private static int CROUCH_USE_COOLDOWN_TICKS = 40;

	public QueenBeeCrossbowItem(Properties pProperties) {
		super(pProperties);
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerPerkItemPropertyOverrides(this);
	}

	public QueenBeeCrossbowItem(int maxChargeTicks, float damage, float velocity,
	                            float inaccuracy, int ammoCapacity, int fireInterval,
	                            float speedModifier, boolean onlyAllowMainHand,
	                            Properties pProperties) {
		super(maxChargeTicks, damage, velocity, inaccuracy, ammoCapacity, fireInterval,
				speedModifier, onlyAllowMainHand, pProperties);
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerPerkItemPropertyOverrides(this);
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {
		ItemStack stack = pPlayer.getItemInHand(pHand);
		if (pPlayer.isCrouching() && isPerkMax(pPlayer, stack)) {
			pPlayer.addEffect(new MobEffectInstance(
					ModEffects.ROYAL_JELLY.get(), ROYAL_JELLY_TICKS, ROYAL_JELLY_LEVEL - 1
			));
			setPerkLevel(stack, 0);
			pPlayer.getCooldowns().addCooldown(stack.getItem(), CROUCH_USE_COOLDOWN_TICKS);
			pLevel.playSound(null, pPlayer, SoundEvents.HONEY_DRINK, SoundSource.PLAYERS,
					1F, 1F);
			return InteractionResultHolder.success(stack);
		}
		return super.use(pLevel, pPlayer, pHand);
	}

	@Override
	protected Projectile getProjectile(Level level, LivingEntity shooter, ItemStack crossbowStack) {
		BaseCrossbowItem crossbowItem = (BaseCrossbowItem) crossbowStack.getItem();
		ItemStack ammoStack = crossbowItem.getLastChargedProjectile(crossbowStack);

		if (ammoStack.is(Items.FIREWORK_ROCKET)) {
			return new FireworkRocketEntity(level, ammoStack, shooter, shooter.getX(),
					shooter.getEyeY() - (double) 0.15F, shooter.getZ(), true);
		}

		BaseArrow arrow = new QueenBeeArrow(ModEntities.QUEEN_BEE_ARROW.get(), level, shooter);
		if (ammoStack.is(Items.SPECTRAL_ARROW)) {
			arrow.setSpectralArrow(true);
		} else {
			arrow.setEffectsFromItem(ammoStack);
		}
		arrow.setShotFromCrossbow(true);

		arrow.addEffect(new MobEffectInstance(
				MobEffects.POISON, POISON_TICKS, POISON_LEVEL - 1
		));
		return arrow;
	}

	@Override
	public byte getMaxPerkLevel() {
		return MAX_PERK_LEVEL;
	}

	@Override
	protected @NotNull SoundEventHolder getStartSound(LivingEntity shooter, ItemStack crossbowStack) {
		return LOADING_START_SOUND;
	}

	@Override
	protected @NotNull SoundEventHolder getMiddleSound(LivingEntity shooter, ItemStack crossbowStack) {
		return SoundEventHolder.empty();
	}

	@Override
	protected @NotNull SoundEventHolder getEndSound(LivingEntity shooter, ItemStack crossbowStack) {
		return SoundEventHolder.empty();
	}

	@Override
	protected @NotNull SoundEventHolder getShootSound(LivingEntity shooter, ItemStack crossbowStack) {
		return SHOOTING_SOUND;
	}

	@Override
	public void updateAttributesFromConfig(CrossbowProperties properties) {
		if (properties instanceof QueenBeeProperties qProperties) {
			POISON_LEVEL = qProperties.POISON_LEVEL.get();
			POISON_TICKS = qProperties.POISON_DURATION.get();
			MAX_PERK_LEVEL = qProperties.MAX_PERK_LEVEL.get().byteValue();
			ROYAL_JELLY_LEVEL = qProperties.ROYAL_JELLY_LEVEL.get();
			ROYAL_JELLY_TICKS = qProperties.ROYAL_JELLY_DURATION.get();
			CROUCH_USE_COOLDOWN_TICKS = qProperties.COOLDOWN_CROUCH_USE.get();
		}
		super.updateAttributesFromConfig(properties);
	}

	@Override
	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		var list = super.getPerkDescriptions(stack, level);
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.queen_bee_crossbow_perk_1")));
		list.add(TooltipHelper.setSubPerkStyle(Component.translatable("tooltip.perk_weapons.effect_format",
				MobEffects.POISON.getDisplayName(),
				TooltipHelper.getRomanNumeral(POISON_LEVEL),
				TooltipHelper.convertTicksToSeconds(POISON_TICKS))));

		list.add(Component.empty());

		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.gain_perk_level_on_hit",
				TooltipHelper.convertToEmbeddedElement(1))));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.max_perk_level",
				TooltipHelper.convertToEmbeddedElement(getMaxPerkLevel()))));

		list.add(Component.empty());

		list.add(TooltipHelper.getCrouchUseAbilityHint());
		list.add(Component.translatable("tooltip.perk_weapons.when_reach_max_perk_level").withStyle(ChatFormatting.GRAY));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.queen_bee_crossbow_ability_1")));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.queen_bee_crossbow_ability_2")));
		list.add(TooltipHelper.setSubPerkStyle(Component.translatable("tooltip.perk_weapons.effect_format",
				ModEffects.ROYAL_JELLY.get().getDisplayName(),
				TooltipHelper.getRomanNumeral(ROYAL_JELLY_LEVEL),
				TooltipHelper.convertTicksToSeconds(ROYAL_JELLY_TICKS))));

		return list;
	}

	@Override
	public Component getWeaponDescription(ItemStack stack, Level level) {
		return TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.queen_bee_crossbow"));
	}
}
