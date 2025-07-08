package net.akashi.perk_weapons.Entities.Projectiles.Spears;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import static net.akashi.perk_weapons.Spears.ScourgeItem.*;

public class ThrownScourge extends ThrownSpear {
	public static final EntityDataAccessor<Boolean> ID_IS_ABILITY_SHOT = SynchedEntityData.defineId(
			ThrownScourge.class, EntityDataSerializers.BOOLEAN);
	public static final String TAG_IS_ABILITY_SHOT = "ability_shot";

	public ThrownScourge(EntityType<? extends ThrownSpear> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public ThrownScourge(Level pLevel, LivingEntity pShooter, ItemStack pStack, EntityType<? extends ThrownSpear> spearType) {
		super(pLevel, pShooter, pStack, spearType);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ID_IS_ABILITY_SHOT, false);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.entityData.set(ID_IS_ABILITY_SHOT, pCompound.getBoolean(TAG_IS_ABILITY_SHOT));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.putBoolean(TAG_IS_ABILITY_SHOT, this.entityData.get(ID_IS_ABILITY_SHOT));
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		if (pResult.getEntity() instanceof LivingEntity entity) {
			entity.addEffect(new MobEffectInstance(MobEffects.WITHER, WITHER_DURATION, WITHER_LEVEL - 1));
			entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, SLOWNESS_DURATION, SLOWNESS_LEVEL - 1));
		}
		super.onHitEntity(pResult);
	}

	@Override
	public byte getLoyaltyLevel() {
		return this.entityData.get(ID_IS_ABILITY_SHOT) ? 0 : super.getLoyaltyLevel();
	}

	public void setIsAbilityShot(boolean isAbilityShot) {
		this.entityData.set(ID_IS_ABILITY_SHOT, isAbilityShot);
	}
}
