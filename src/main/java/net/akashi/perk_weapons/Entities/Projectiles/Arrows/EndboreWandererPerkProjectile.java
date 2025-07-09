package net.akashi.perk_weapons.Entities.Projectiles.Arrows;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static net.akashi.perk_weapons.Bows.EndboreWandererItem.*;

public class EndboreWandererPerkProjectile extends EndboreWandererArrow {
	private static final EntityDataAccessor<Integer> TARGET = SynchedEntityData.defineId(
			EndboreWandererPerkProjectile.class, EntityDataSerializers.INT);

	public EndboreWandererPerkProjectile(EntityType<? extends BaseArrow> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public EndboreWandererPerkProjectile(EntityType<? extends BaseArrow> pEntityType, Level pLevel, double pX, double pY, double pZ) {
		super(pEntityType, pLevel, pX, pY, pZ);
	}

	public EndboreWandererPerkProjectile(EntityType<? extends BaseArrow> pEntityType, Level pLevel, LivingEntity pShooter) {
		super(pEntityType, pLevel, pShooter);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(TARGET, -1);
	}

	@Override
	protected void onHitBlock(@NotNull BlockHitResult pResult) {
		super.onHitBlock(pResult);
		this.discard();
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		LivingEntity e = (LivingEntity) pResult.getEntity();
		e.addEffect(new MobEffectInstance(MobEffects.LEVITATION, PERK_PROJECTILE_LEVITATION_TICKS_ON_HIT, 0));
		super.onHitEntity(pResult);
		this.discard();
	}

	@Override
	public void tick() {
		homingLogic();
		super.tick();
		if (this.level().isClientSide()) {
			this.level().addParticle(ParticleTypes.END_ROD,
					this.getX(), this.getY(), this.getZ(),
					0.0D, 0.0D, 0.0D);
		}
	}

	private void homingLogic() {
		Entity target = getTarget();
		if (target == null || !target.isAlive()) {
			updateTarget();
			return;
		}
		if (target.distanceToSqr(this.getX(), this.getY(), this.getZ()) > PERK_PROJECTILE_HOMING_RANGE *
				PERK_PROJECTILE_HOMING_RANGE) {
			setTarget(null);
			return;
		}
		Vec3 motionVec = getMotionVec();
		double velocity = motionVec.length() + HOMING_ACCELERATION;

		Vec3 motionVecNormalized = motionVec.normalize();
		Vec3 vecToTarget = getVectorToEntity(target).normalize();

		double cosTheta = motionVecNormalized.dot(motionVecNormalized);
		if (cosTheta <= MAX_HOMING_ANGLE_COS_VALUE) {
			setTarget(null);
			return;
		}
		if (cosTheta >= MAX_PROJECTILE_TURN_ANGLE_PER_TICK_COS_VALUE) {
			this.setDeltaMovement(vecToTarget.scale(velocity));
			return;
		}

		Vec3 axis = motionVec.cross(vecToTarget);
		if (axis.lengthSqr() < 1e-6) {
			axis = new Vec3(0, 1, 0);
		} else {
			axis.normalize();
		}

		double cos = MAX_PROJECTILE_TURN_ANGLE_PER_TICK_COS_VALUE;
		double sin = MAX_PROJECTILE_TURN_ANGLE_PER_TICK_SIN_VALUE;

		double dot = axis.dot(motionVecNormalized);
		Vec3 cross = axis.cross(motionVecNormalized);

		Vec3 newMovement = motionVecNormalized.scale(cos)
				.add(cross.scale(sin))
				.add(axis.scale(dot * (1 - cos)))
				.normalize().scale(velocity);
		this.setDeltaMovement(newMovement);
	}

	private void updateTarget() {
		AABB homingBB = this.getBoundingBox().inflate(PERK_PROJECTILE_HOMING_RANGE);
		Level level = this.level();
		Vec3 from = new Vec3(this.getX(), this.getY(), this.getZ());
		Vec3 motionVecNormalized = this.getMotionVec().normalize();
		List<Entity> blackListEntities = new ArrayList<>(List.of(this));
		if (this.getOwner() != null) {
			blackListEntities.add(this.getOwner());
		}
		List<LivingEntity> validEntities = level.getEntitiesOfClass(LivingEntity.class, homingBB,
				entity -> {
					if (blackListEntities.contains(entity) || !entity.closerThan(this, PERK_PROJECTILE_HOMING_RANGE))
						return false;

					Vec3 to = entity.getEyePosition();
					Vec3 vecToE = to.subtract(from).normalize();
					if (motionVecNormalized.dot(vecToE) < MAX_HOMING_ANGLE_COS_VALUE)
						return false;

					BlockHitResult blockResult = level.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER,
							ClipContext.Fluid.NONE, this));
					return blockResult.getType() != HitResult.Type.BLOCK;
				}
		);
		if (validEntities.isEmpty())
			return;

		LivingEntity closest = validEntities.stream().min(Comparator.comparingDouble(this::distanceToSqr))
				.orElse(null);
		setTarget(closest);
	}

	@Nullable
	private Entity getTarget() {
		return this.level().getEntity(this.getEntityData().get(TARGET));
	}

	private void setTarget(@Nullable Entity entity) {
		this.getEntityData().set(TARGET, entity == null ? -1 : entity.getId());
	}

	private Vec3 getVectorToEntity(Entity e) {
		if (e == null)
			return Vec3.ZERO;
		return new Vec3(
				e.getX() - this.getX(),
				(e.getY() + e.getEyeHeight()) - this.getY(),
				e.getZ() - this.getZ()
		);
	}

	private Vec3 getMotionVec() {
		return new Vec3(this.getDeltaMovement().x(), this.getDeltaMovement().y(), this.getDeltaMovement().z());
	}
}
