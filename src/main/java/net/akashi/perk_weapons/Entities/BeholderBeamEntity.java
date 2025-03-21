package net.akashi.perk_weapons.Entities;

import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownSpear;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class BeholderBeamEntity extends Entity {
	private static final EntityDataAccessor<Integer> ID_SRC_ID = SynchedEntityData.defineId(
			BeholderBeamEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> ID_TARGET_ID = SynchedEntityData.defineId(
			BeholderBeamEntity.class, EntityDataSerializers.INT);
	private static final String TAG_SRC_ID = "src";
	private static final String TAG_TARGET_ID = "target";

	public BeholderBeamEntity(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public BeholderBeamEntity(Level level, Entity src, Entity target) {
		super(ModEntities.BEHOLDER_BEAM_SRC.get(), level);
		this.entityData.set(ID_SRC_ID, src.getId());
		this.entityData.set(ID_TARGET_ID, target.getId());
		this.setNoGravity(true);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(ID_SRC_ID, 0);
		this.entityData.define(ID_TARGET_ID, 0);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag pCompound) {
		this.entityData.set(ID_SRC_ID, pCompound.getInt(TAG_SRC_ID));
		this.entityData.set(ID_TARGET_ID, pCompound.getInt(TAG_TARGET_ID));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag pCompound) {
		pCompound.putInt(TAG_SRC_ID, this.entityData.get(ID_SRC_ID));
		pCompound.putInt(TAG_TARGET_ID, this.entityData.get(ID_TARGET_ID));
	}

	@Override
	public void tick() {
		super.tick();
		Entity src = getSrcEntity();
		if (src != null) {
			this.setPos(src.getX(), src.getBbHeight() / 2 + src.getY(), src.getZ());
		}
	}

	@Nullable
	public Entity getSrcEntity() {
		return this.level().getEntity(this.entityData.get(ID_SRC_ID));
	}

	@Nullable
	public Entity getTargetEntity() {
		return this.level().getEntity(this.entityData.get(ID_TARGET_ID));
	}
}
