package net.akashi.perk_weapons.Entities.Projectiles.Arrows;

import net.akashi.perk_weapons.Bows.EndboreWandererItem;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class EndboreWandererArrow extends PerkUpdateArrow {
	public EndboreWandererArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public EndboreWandererArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, double pX, double pY, double pZ) {
		super(pEntityType, pLevel, pX, pY, pZ);
	}

	public EndboreWandererArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, LivingEntity pShooter) {
		super(pEntityType, pLevel, pShooter);
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		LivingEntity e = (LivingEntity) pResult.getEntity();
		if (e.hasEffect(MobEffects.LEVITATION)) {
			this.setBaseDamage(this.getBaseDamage() * (1 + EndboreWandererItem.DAMAGE_BONUS_LEVITATION));
		}
		super.onHitEntity(pResult);
	}
}
