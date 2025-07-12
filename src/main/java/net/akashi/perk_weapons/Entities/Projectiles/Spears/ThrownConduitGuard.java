package net.akashi.perk_weapons.Entities.Projectiles.Spears;

import net.akashi.perk_weapons.Util.HomingHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static net.akashi.perk_weapons.Spears.ConduitGuardItem.*;

public class ThrownConduitGuard extends ThrownSpear {
	private static final String TAG_TARGET_ID = "target_id";
	private static final EntityDataAccessor<Integer> TARGET = SynchedEntityData.defineId(ThrownConduitGuard.class,
			EntityDataSerializers.INT);

	private boolean velocityChanged = false;
	private boolean reachedShore = false;
	private int returnTime = 80;
	private int noClipTime = 0;

	public ThrownConduitGuard(EntityType<? extends ThrownSpear> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public ThrownConduitGuard(EntityType<? extends ThrownSpear> spearType, Level pLevel,
	                          LivingEntity pShooter, ItemStack pStack, int returnTime) {
		super(spearType, pLevel, pShooter, pStack);
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
			if (!this.velocityChanged) {
				this.setDeltaMovement(getMotionVec().scale(VELOCITY_MULTIPLIER));
				this.velocityChanged = true;
			}
			if (returnTime <= 0) {
				this.dealtDamage = true;
			} else {
				returnTime--;
			}
			this.setNoGravity(true);

			HomingHelper.applyHoming(
					this,
					this::getTarget,
					this::setTarget,
					HOMING_RANGE,
					MAX_HOMING_ANGLE_COS,
					MAX_TURN_ANGLE_COS,
					MAX_TURN_ANGLE_SIN,
					HOMING_ACCELERATION
			);
		} else {
			this.setNoGravity(false);
			if (dealtDamage) {
				if (this.isInWall() && !this.reachedShore) {
					noClipTime = 7;
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
	public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.velocityChanged = pCompound.getBoolean("velocityChanged");
		this.noClipTime = pCompound.getInt("noclipTime");
		this.getEntityData().set(TARGET, pCompound.getInt(TAG_TARGET_ID));
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.putBoolean("velocityChanged", this.velocityChanged);
		pCompound.putInt("noclipTime", this.noClipTime);
		pCompound.putInt(TAG_TARGET_ID, this.getEntityData().get(TARGET));
	}

	@Override
	public byte getLoyaltyLevel() {
		if (this.isInWater() || noClipTime > 0) {
			return 3;
		}
		return super.getLoyaltyLevel();
	}

	@Override
	protected void onHitBlock(@NotNull BlockHitResult pResult) {
		super.onHitBlock(pResult);
		this.setSoundEvent(SoundEvents.TRIDENT_HIT_GROUND);
		if (!this.isInWater()) {
			this.reachedShore = true;
		}
	}


	//New methods
	@Nullable
	private Entity getTarget() {
		return this.level().getEntity(this.getEntityData().get(TARGET));
	}

	private void setTarget(@Nullable Entity entity) {
		this.getEntityData().set(TARGET, entity == null ? -1 : entity.getId());
	}

	private Vec3 getMotionVec() {
		return new Vec3(this.getDeltaMovement().x(), this.getDeltaMovement().y(), this.getDeltaMovement().z());
	}
}
