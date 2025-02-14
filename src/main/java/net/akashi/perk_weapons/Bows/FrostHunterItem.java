package net.akashi.perk_weapons.Bows;

import net.akashi.perk_weapons.Config.Properties.Bow.BowProperties;
import net.akashi.perk_weapons.Config.Properties.Bow.FrostHunterProperties;
import net.akashi.perk_weapons.Entities.Hound;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.FrostHunterArrow;
import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Util.ICoolDownItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpectralArrowItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FrostHunterItem extends BaseBowItem implements ICoolDownItem {
	public static int FROZEN_TIME = 160;
	private static int ABILITY_COOLDOWN_TIME = 1200;
	private static int HOUND_LIFETIME = 400;
	private static byte HOUND_COUNT = 2;
	private static boolean ENABLE_HOUND_EFFECT = true;
	private static final Map<UUID, Long> AbilityUseTimeMap = new HashMap<>();

	public FrostHunterItem(Properties properties) {
		super(properties);
	}

	public FrostHunterItem(int drawTime, float projectileDamage, float velocity, float inaccuracy, float speedModifier,
	                       float zoomFactor, boolean onlyMainHand, Properties properties) {
		super(drawTime, projectileDamage, velocity, inaccuracy, speedModifier, zoomFactor, onlyMainHand, properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		boolean flag = !AbilityUseTimeMap.containsKey(pPlayer.getUUID());
		if (pPlayer.isCrouching() &&
				(flag || (pLevel.getGameTime() - AbilityUseTimeMap.get(pPlayer.getUUID()) > ABILITY_COOLDOWN_TIME))) {
			if (!pLevel.isClientSide()) {
				for (int i = 0; i < HOUND_COUNT; i++) {
					Hound hound = new Hound(EntityType.WOLF, pLevel, HOUND_LIFETIME);
					hound.setTame(true);
					hound.setOwnerUUID(pPlayer.getUUID());
					hound.setCustomName(Component.translatable(PerkWeapons.MODID + ".entity.hound"));
					if (ENABLE_HOUND_EFFECT) {
						hound.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, HOUND_LIFETIME, 0));
						hound.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, HOUND_LIFETIME, 0));
						hound.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, HOUND_LIFETIME, 1));
						hound.addEffect(new MobEffectInstance(MobEffects.REGENERATION, HOUND_LIFETIME, 1));
					}
					hound.setPos(pPlayer.getX(), pPlayer.getY(), pPlayer.getZ());
					pLevel.addFreshEntity(hound);
					AbilityUseTimeMap.put(pPlayer.getUUID(), pLevel.getGameTime());
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
		if (!AbilityUseTimeMap.containsKey(player.getUUID()))
			return 1.0f;
		else {
			long timePassed = player.level().getGameTime() - AbilityUseTimeMap.get(player.getUUID());
			if (timePassed > ABILITY_COOLDOWN_TIME) {
				return 1.0f;
			} else {
				return (float) timePassed / ABILITY_COOLDOWN_TIME;
			}
		}
	}
}
