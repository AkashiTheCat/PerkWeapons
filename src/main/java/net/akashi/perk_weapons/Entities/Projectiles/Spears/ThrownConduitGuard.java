package net.akashi.perk_weapons.Entities.Projectiles.Spears;

import net.akashi.perk_weapons.Config.ModCommonConfigs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

import static net.akashi.perk_weapons.Spears.ConduitGuardItem.*;

public class ThrownConduitGuard extends ThrownSpear {
	private boolean velocityChanged = false;
	private int returnTime = 80;
	private int noClipTime = 10;
	private static final EntityDataAccessor<Integer> TARGET = SynchedEntityData.defineId(ThrownConduitGuard.class, EntityDataSerializers.INT);

	public ThrownConduitGuard(EntityType<? extends ThrownSpear> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public ThrownConduitGuard(Level pLevel, LivingEntity pShooter, ItemStack pStack, int returnTime,
	                          EntityType<? extends ThrownSpear> spearType) {
		super(pLevel, pShooter, pStack, spearType);
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
			} else {
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

				if (dotProduct > TRACKING_THRESHOLD) {

					// add vector to target, scale to match current velocity
					Vec3 newMotion = courseVec.scale(courseLen / (totalLen * 2)).add(targetVec.scale(courseLen / totalLen));

					this.setDeltaMovement(newMotion);

				} else if (!this.level().isClientSide()) {
					// too inaccurate for our intended target, give up on it
					this.setTarget(null);
				}
			}
		} else {
			this.setNoGravity(false);
			if (dealtDamage) {
				if(this.isInWall()){
					noClipTime = 10;
				}
				if (noClipTime > 0) {
					noClipTime--;
				} else {
					this.setNoPhysics(false);
				}
				if (this.getLoyaltyLevel() == 0)
					this.setDeltaMovement(getMotionVec().scale(0.8).add(0, -0.1, 0));
			}
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
		this.velocityChanged = pCompound.getBoolean("velocityChanged");
		this.noClipTime = pCompound.getInt("noclipTime");
	}

	@Override
	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.putBoolean("velocityChanged", this.velocityChanged);
		pCompound.putInt("noclipTime", this.noClipTime);
	}

	@Override
	public byte getLoyaltyLevel() {
		if (this.isInWater() || noClipTime > 0) {
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
			AABB positionBB = new AABB(getX() - TRACKING_RANGE, getY() - TRACKING_RANGE, getZ() - TRACKING_RANGE,
					getX() + TRACKING_RANGE, getY() + TRACKING_RANGE, getZ() + TRACKING_RANGE);
			Entity closestTarget = this.level().getNearestEntity(Monster.class, TargetingConditions.forCombat().range(TRACKING_RANGE),
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
