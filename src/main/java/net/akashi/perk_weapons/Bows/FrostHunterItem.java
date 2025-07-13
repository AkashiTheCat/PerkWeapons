package net.akashi.perk_weapons.Bows;

import net.akashi.perk_weapons.Config.Properties.Bow.BowProperties;
import net.akashi.perk_weapons.Config.Properties.Bow.FrostHunterProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.FrostHunterArrow;
import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Util.ICoolDownItem;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpectralArrowItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FrostHunterItem extends BaseBowItem implements ICoolDownItem {
	public static int FROZEN_TIME = 160;
	private static int ABILITY_COOLDOWN_TIME = 600;
	private static int HOUND_LIFETIME = 600;
	private static byte HOUND_COUNT = 2;
	private static boolean ENABLE_HOUND_EFFECT = true;
	private static final Map<UUID, Integer> CD_MAP = new HashMap<>(32);

	public FrostHunterItem(Properties properties) {
		super(properties);
	}

	public FrostHunterItem(int drawTime, float projectileDamage, float velocity, float inaccuracy, float speedModifier,
	                       float zoomFactor, boolean onlyMainHand, Properties properties) {
		super(drawTime, projectileDamage, velocity, inaccuracy, speedModifier, zoomFactor, onlyMainHand, properties);
	}

	@Override
	public void inventoryTick(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull Entity pEntity,
	                          int pSlotId, boolean pIsSelected) {
		super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
		if (!pLevel.isClientSide() && pEntity instanceof Player player && pIsSelected) {
			UUID uuid = player.getUUID();
			if (CD_MAP.containsKey(uuid)) {
				int ticks = CD_MAP.get(uuid);
				if (ticks > 0)
					CD_MAP.put(uuid, ticks - 1);
			}
		}
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {
		boolean flag = !CD_MAP.containsKey(pPlayer.getUUID());
		if (pPlayer.isCrouching() && (flag || CD_MAP.get(pPlayer.getUUID()) == 0)) {
			if (!pLevel.isClientSide()) {
				for (int i = 0; i < HOUND_COUNT; i++) {
					Wolf hound = EntityType.WOLF.create(pLevel);
					if (hound != null) {
						hound.setTame(true);
						hound.setOwnerUUID(pPlayer.getUUID());
						hound.setCustomName(Component.translatable("entity." + PerkWeapons.MODID + ".hound"));

						if (ENABLE_HOUND_EFFECT) {
							hound.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, HOUND_LIFETIME, 0));
							hound.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, HOUND_LIFETIME, 0));
							hound.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, HOUND_LIFETIME, 1));
							hound.addEffect(new MobEffectInstance(MobEffects.REGENERATION, HOUND_LIFETIME, 1));
						}

						hound.setPos(pPlayer.getX(), pPlayer.getY(), pPlayer.getZ());
						pLevel.addFreshEntity(hound);

						hound.getPersistentData().putInt("DespawnTick", HOUND_LIFETIME);
						CD_MAP.put(pPlayer.getUUID(), ABILITY_COOLDOWN_TIME);
					}
				}
			}
		}
		return super.use(pLevel, pPlayer, pHand);
	}

	@Override
	public AbstractArrow createArrow(Level level, ArrowItem arrowItem, ItemStack bowStack, ItemStack arrowStack, Player player) {
		FrostHunterArrow arrow = new FrostHunterArrow(ModEntities.FROST_HUNTER_ARROW.get(), level, player);
		if (arrowItem instanceof SpectralArrowItem) {
			arrow.setSpectralArrow(true);
		} else {
			arrow.setEffectsFromItem(arrowStack);
		}
		arrow.setIgnoreInvulnerableTime(true);
		arrow.setBaseDamage(PROJECTILE_DAMAGE / VELOCITY);
		return arrow;
	}

	@Override
	public void updateAttributesFromConfig(BowProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof FrostHunterProperties fProperties) {
			FROZEN_TIME = fProperties.FROZEN_TIME.get();
			ABILITY_COOLDOWN_TIME = fProperties.ABILITY_COOLDOWN_TIME.get();
			HOUND_LIFETIME = fProperties.HOUND_LIFETIME.get();
			HOUND_COUNT = fProperties.HOUND_COUNT.get().byteValue();
			ENABLE_HOUND_EFFECT = fProperties.ENABLE_HOUND_EFFECT.get();
		}
	}

	@Override
	public float getCoolDownProgress(LivingEntity player, ItemStack stack) {
		if (!CD_MAP.containsKey(player.getUUID()))
			return 1.0f;
		else {
			int timeRemaining = CD_MAP.get(player.getUUID());
			if (timeRemaining == 0) {
				return 1.0f;
			} else {
				return 1f - (float) timeRemaining / ABILITY_COOLDOWN_TIME;
			}
		}
	}

	@Override
	public Component getWeaponDescription(ItemStack stack, Level level) {
		return TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.frost_hunter"));
	}

	@Override
	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		List<Component> list = super.getPerkDescriptions(stack, level);

		list.add(TooltipHelper.setEmbeddedElementStyle(Component.translatable("tooltip.perk_weapons.arrow_ignore_invulnerable_time_hint")));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.frost_hunter_perk_1",
				TooltipHelper.convertToEmbeddedElement(TooltipHelper.convertTicksToSeconds(FROZEN_TIME)))));

		list.add(Component.empty());

		list.add(TooltipHelper.getCrouchUseAbilityHint());
		list.add(TooltipHelper.getCoolDownTip(ABILITY_COOLDOWN_TIME));
		list.add(Component.translatable("tooltip.perk_weapons.frost_hunter_cd_hint")
				.withStyle(ChatFormatting.GRAY));

		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.frost_hunter_ability_1",
				TooltipHelper.convertToEmbeddedElement(HOUND_COUNT),
				TooltipHelper.convertToEmbeddedElement(TooltipHelper.convertTicksToSeconds(HOUND_LIFETIME)))));

		return list;
	}
}
