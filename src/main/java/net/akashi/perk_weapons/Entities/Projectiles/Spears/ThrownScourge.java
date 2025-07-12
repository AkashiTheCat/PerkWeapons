package net.akashi.perk_weapons.Entities.Projectiles.Spears;

import net.akashi.perk_weapons.Registry.ModItems;
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
import org.jetbrains.annotations.NotNull;

import static net.akashi.perk_weapons.Spears.ScourgeItem.*;

public class ThrownScourge extends ThrownSpear {
	private static final ItemStack SCOURGE_PERK = new ItemStack(ModItems.SCOURGE_PERK_TEXTURE_HOLDER.get());

	public static final EntityDataAccessor<Boolean> ID_IS_ABILITY_SHOT = SynchedEntityData.defineId(
			ThrownScourge.class, EntityDataSerializers.BOOLEAN);
	public static final String TAG_IS_ABILITY_SHOT = "ability_shot";

	public ThrownScourge(EntityType<? extends ThrownSpear> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public ThrownScourge(EntityType<? extends ThrownSpear> spearType, Level pLevel, LivingEntity pShooter, ItemStack pStack) {
		super(spearType, pLevel, pShooter, pStack);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ID_IS_ABILITY_SHOT, false);
	}

	@Override
	public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.entityData.set(ID_IS_ABILITY_SHOT, pCompound.getBoolean(TAG_IS_ABILITY_SHOT));
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
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

	public boolean isAbilityShot() {
		return this.entityData.get(ID_IS_ABILITY_SHOT);
	}

	@Override
	public ItemStack getItemForRender() {
		if (isAbilityShot())
			return SCOURGE_PERK;
		return super.getItemForRender();
	}
}
