package net.akashi.perk_weapons.Entities.Projectiles.Arrows;

import net.akashi.perk_weapons.Util.HomingHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static net.akashi.perk_weapons.Bows.EndboreWandererItem.*;

public class EndboreWandererPerkProjectile extends EndboreWandererArrow {
	private static final String TAG_TARGET_ID = "target_id";
	private static final EntityDataAccessor<Integer> ID_TARGET = SynchedEntityData.defineId(
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
	public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.getEntityData().set(ID_TARGET, pCompound.getInt(TAG_TARGET_ID));
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.putInt(TAG_TARGET_ID, this.getEntityData().get(ID_TARGET));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(ID_TARGET, -1);
	}

	@Override
	protected void onHitBlock(@NotNull BlockHitResult pResult) {
		super.onHitBlock(pResult);
		this.discard();
	}

	@Override
	protected void onHitEntity(@NotNull EntityHitResult pResult) {
		LivingEntity e = (LivingEntity) pResult.getEntity();
		e.addEffect(new MobEffectInstance(MobEffects.LEVITATION, PERK_PROJECTILE_LEVITATION_TICKS_ON_HIT, 0));
		super.onHitEntity(pResult);
		this.discard();
	}

	@Override
	public void tick() {
		super.tick();
		HomingHelper.applyHoming(
				this,
				this::getTarget,
				this::setTarget,
				PERK_PROJECTILE_HOMING_RANGE,
				MAX_HOMING_ANGLE_COS_VALUE,
				MAX_PROJECTILE_TURN_ANGLE_PER_TICK_COS_VALUE,
				MAX_PROJECTILE_TURN_ANGLE_PER_TICK_SIN_VALUE,
				HOMING_ACCELERATION
		);
		if (this.level().isClientSide()) {
			this.level().addParticle(ParticleTypes.END_ROD,
					this.getX(), this.getY(), this.getZ(),
					0.0D, 0.0D, 0.0D);
		}
	}

	@Nullable
	private Entity getTarget() {
		return this.level().getEntity(this.getEntityData().get(ID_TARGET));
	}

	private void setTarget(@Nullable Entity entity) {
		this.getEntityData().set(ID_TARGET, entity == null ? -1 : entity.getId());
	}
}
