package net.akashi.perk_weapons.Spears;

import net.akashi.perk_weapons.Config.Properties.Spear.CenturionProperties;
import net.akashi.perk_weapons.Config.Properties.Spear.SpearProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownCenturion;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownSpear;
import net.akashi.perk_weapons.Registry.ModEffects;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CenturionItem extends BaseSpearItem {
	private static double EFFECT_APPLY_RANGE = 16;
	private static int PHALANX_EFFECT_LEVEL = 5;
	private static byte PIERCE_LEVEL = 6;

	public CenturionItem(Properties pProperties) {
		super(pProperties);
	}

	public CenturionItem(float attackDamage, float attackSpeed, float throwDamage, float projectileVelocity,
	                     int maxChargeTicks, boolean isAdvanced, Properties pProperties) {
		super(attackDamage, attackSpeed, throwDamage, projectileVelocity, maxChargeTicks, isAdvanced, pProperties);
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {
		if (!pPlayer.isCrouching()) {
			return InteractionResultHolder.fail(pPlayer.getItemInHand(pHand));
		}
		return super.use(pLevel, pPlayer, pHand);
	}

	@Override
	public void inventoryTick(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull Entity pEntity,
	                          int pSlotId, boolean pIsSelected) {
		super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
		if (pIsSelected && !pLevel.isClientSide() && pLevel.getGameTime() % 60 == 0) {
			double rangeSqr = EFFECT_APPLY_RANGE * EFFECT_APPLY_RANGE;
			AABB searchBB = pEntity.getBoundingBox().inflate(EFFECT_APPLY_RANGE);
			List<Player> players = pLevel.getEntitiesOfClass(Player.class, searchBB,
					e -> e.distanceToSqr(pEntity) <= rangeSqr);
			List<TamableAnimal> tamedAnimals = pLevel.getEntitiesOfClass(TamableAnimal.class, searchBB,
					e -> e.isTame() && e.distanceToSqr(pEntity) <= rangeSqr);
			players.forEach(player -> player.addEffect(new MobEffectInstance(ModEffects.PHALANX.get(),
					80, PHALANX_EFFECT_LEVEL - 1)));
			tamedAnimals.forEach(animal -> animal.addEffect(new MobEffectInstance(ModEffects.PHALANX.get(),
					80, PHALANX_EFFECT_LEVEL - 1)));
		}
	}

	@Override
	public ThrownSpear createThrownSpear(Level pLevel, Player player, ItemStack pStack) {
		var spear = new ThrownCenturion(ModEntities.THROWN_CENTURION.get(), pLevel, player, pStack);
		spear.setPierceLevel(PIERCE_LEVEL);
		return spear;
	}

	@Override
	public void updateAttributesFromConfig(SpearProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof CenturionProperties cProperties) {
			EFFECT_APPLY_RANGE = cProperties.EFFECT_APPLY_RANGE.get();
			PHALANX_EFFECT_LEVEL = cProperties.PHALANX_LEVEL.get();
			PIERCE_LEVEL = cProperties.PIERCE_LEVEL.get().byteValue();
		}
	}
}
