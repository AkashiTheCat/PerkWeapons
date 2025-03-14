package net.akashi.perk_weapons.Entities.Projectiles.Arrows;

import net.akashi.perk_weapons.Crossbows.IncineratorItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class IncineratorArrow extends BaseArrow {
	public IncineratorArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public IncineratorArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, double pX, double pY, double pZ) {
		super(pEntityType, pLevel, pX, pY, pZ);
	}

	public IncineratorArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, LivingEntity pShooter) {
		super(pEntityType, pLevel, pShooter);
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		if (pResult.getEntity().isOnFire()) {
			this.setKnockback(this.getKnockback() + IncineratorItem.FIRE_ARROW_KNOCKBACK_BONUS);
		}
		super.onHitEntity(pResult);
	}
}
