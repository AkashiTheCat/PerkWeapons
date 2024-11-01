package net.akashi.perk_weapons.Entities.Projectiles.Arrows;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import static net.akashi.perk_weapons.Bows.FrostHunterItem.FROZEN_TIME;

public class FrostHunterArrow extends BaseArrow {
	public FrostHunterArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public FrostHunterArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, double pX, double pY, double pZ) {
		super(pEntityType, pLevel, pX, pY, pZ);
	}

	public FrostHunterArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, LivingEntity pShooter) {
		super(pEntityType, pLevel, pShooter);
	}

	@Override
	public void tick() {
		super.tick();
		if(this.level().isClientSide() && !this.inGround){
			this.level().addParticle(ParticleTypes.SNOWFLAKE,
					this.getX(), this.getY(), this.getZ(),
					0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		super.onHitEntity(pResult);
		pResult.getEntity().setTicksFrozen(FROZEN_TIME * 2);
	}
}
