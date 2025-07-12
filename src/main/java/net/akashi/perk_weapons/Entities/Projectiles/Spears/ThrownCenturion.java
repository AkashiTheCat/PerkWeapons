package net.akashi.perk_weapons.Entities.Projectiles.Spears;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class ThrownCenturion extends ThrownSpear {
	public ThrownCenturion(EntityType<? extends ThrownSpear> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public ThrownCenturion(EntityType<? extends ThrownSpear> spearType, Level pLevel,
	                       LivingEntity pShooter, ItemStack pStack) {
		super(spearType, pLevel, pShooter, pStack);
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		Entity entity = pResult.getEntity();
		if (entity.getType() == EntityType.PLAYER) {
			((Player) entity).disableShield(false);
		}
		super.onHitEntity(pResult);
	}
}
