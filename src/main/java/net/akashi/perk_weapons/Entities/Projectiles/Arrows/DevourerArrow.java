package net.akashi.perk_weapons.Entities.Projectiles.Arrows;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class DevourerArrow extends BaseArrow {
	public DevourerArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public DevourerArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, double pX, double pY, double pZ) {
		super(pEntityType, pLevel, pX, pY, pZ);
	}

	public DevourerArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, LivingEntity pShooter) {
		super(pEntityType, pLevel, pShooter);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide() && !this.inGround) {
			this.level().addParticle(ParticleTypes.SOUL,
					this.getX(), this.getY(), this.getZ(),
					0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	protected void doPostHurtEffects(LivingEntity pLiving) {
		super.doPostHurtEffects(pLiving);
		pLiving.setDeltaMovement(0, 0, 0);
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		super.onHitEntity(pResult);
		Level level = this.level();
		if (!level.isClientSide() && pResult.getEntity() instanceof LivingEntity livingEntity) {
			EvokerFangs evokerFang = new EvokerFangs(level, livingEntity.getX(),
					livingEntity.getY(), livingEntity.getZ(), 0F, 5,
					(LivingEntity) this.getOwner());
			livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 255));
			level.addFreshEntity(evokerFang);
		}
	}
}
