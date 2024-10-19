package net.akashi.weaponmod.Entities.Projectiles;

import net.akashi.weaponmod.Config.ModCommonConfigs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class ThrownConduitGuard extends ThrownSpear {
	private double targetingRange = 5.0;
	private double targetingThreshold = 0.5;
	private boolean velocityChanged = false;
	private int returnTime = 80;
	private static final EntityDataAccessor<Integer> TARGET = SynchedEntityData.defineId(ThrownConduitGuard.class, EntityDataSerializers.INT);

	public ThrownConduitGuard(EntityType<? extends ThrownSpear> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public ThrownConduitGuard(Level pLevel, LivingEntity pShooter, ItemStack pStack, int ReturnSlot,
	                          double targetingRange, double targetingThreshold, int returnTime,
	                          EntityType<? extends ThrownSpear> spearType) {
		super(pLevel, pShooter, pStack, ReturnSlot, spearType);
		this.targetingRange = targetingRange;
		this.targetingThreshold = targetingThreshold;
		this.returnTime = returnTime;
	}

	//Override Methods
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(TARGET, -1);
	}

	@Override
	public void tick() {
		if (this.isInWater() && !this.dealtDamage) {
			if (!this.level().isClientSide()) {
				updateTarget();
			}
			if (!this.velocityChanged) {
				this.setDeltaMovement(getMotionVec().scale(ModCommonConfigs.CONDUIT_GUARD_PROPERTIES.VELOCITY_MULTIPLIER.get()));
				this.velocityChanged = true;
			}
			if (returnTime <= 0) {
				this.dealtDamage = true;
			}
			else {
				returnTime--;
			}
			this.setNoGravity(true);
			// modified from twilight forest's tracking bow
			Entity target = getTarget();
			if (target != null) {

				Vec3 targetVec = getVectorToTarget(target).scale(0.3);
				Vec3 courseVec = getMotionVec();

				// vector lengths
				double courseLen = courseVec.length();
				double targetLen = targetVec.length();
				double totalLen = Math.sqrt(courseLen * courseLen + targetLen * targetLen);

				double dotProduct = courseVec.dot(targetVec) / (courseLen * targetLen); // cosine similarity

				if (dotProduct > targetingThreshold) {

					// add vector to target, scale to match current velocity
					Vec3 newMotion = courseVec.scale(courseLen / (totalLen * 2)).add(targetVec.scale(courseLen / totalLen));

					this.setDeltaMovement(newMotion);

				} else if (!this.level().isClientSide()) {
					// too inaccurate for our intended target, give up on it
					this.setTarget(null);
				}
			}
		} else if (dealtDamage && this.getLoyaltyLevel() == 0) {
			this.setNoPhysics(false);
			this.setDeltaMovement(getMotionVec().scale(0.8).add(0,-0.1,0));
		}
		super.tick();
	}

	@Override
	protected float getWaterInertia() {
		return 0.99F;
	}

	@Override
	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.targetingRange = pCompound.getDouble("targetingRange");
		this.targetingThreshold = pCompound.getDouble("targetingThreshold");
		this.velocityChanged = pCompound.getBoolean("velocityChanged");
	}

	@Override
	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.putDouble("targetingRange", this.targetingRange);
		pCompound.putDouble("targetingThreshold", this.targetingThreshold);
		pCompound.putBoolean("velocityChanged", this.velocityChanged);
	}

	@Override
	public byte getLoyaltyLevel() {
		if (this.isInWater()) {
			return 3;
		}
		return super.getLoyaltyLevel();
	}

	@Override
	protected void onHitBlock(BlockHitResult pResult) {
		super.onHitBlock(pResult);
		this.setSoundEvent(SoundEvents.TRIDENT_HIT_GROUND);
	}


	//New methods
	private void updateTarget() {
		Entity target = getTarget();
		if (target != null && !target.isAlive()) {
			target = null;
			this.setTarget(null);
		}
		if (target == null) {
			AABB positionBB = new AABB(getX() - targetingRange, getY() - targetingRange, getZ() - targetingRange,
					getX() + targetingRange, getY() + targetingRange, getZ() + targetingRange);
			Entity closestTarget = this.level().getNearestEntity(Monster.class, TargetingConditions.forCombat().range(targetingRange),
					null, this.getX(), this.getY(), this.getZ(), positionBB
			);
			if (closestTarget != null && closestTarget != this.getOwner()) {
				setTarget(closestTarget);
			}
		}
	}

	@Nullable
	private Entity getTarget() {
		return this.level().getEntity(this.getEntityData().get(TARGET));
	}

	private void setTarget(@Nullable Entity entity) {
		this.getEntityData().set(TARGET, entity == null ? -1 : entity.getId());
	}

	private Vec3 getVectorToTarget(Entity target) {
		return new Vec3(target.getX() - this.getX(), (target.getY() + target.getEyeHeight()) - this.getY(), target.getZ() - this.getZ());
	}

	private Vec3 getMotionVec() {
		return new Vec3(this.getDeltaMovement().x(), this.getDeltaMovement().y(), this.getDeltaMovement().z());
	}
}
