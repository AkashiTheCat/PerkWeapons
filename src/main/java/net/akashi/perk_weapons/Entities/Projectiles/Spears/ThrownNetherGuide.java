package net.akashi.perk_weapons.Entities.Projectiles.Spears;

import net.akashi.perk_weapons.Registry.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

import static net.akashi.perk_weapons.Spears.NetherGuideItem.*;

public class ThrownNetherGuide extends ThrownSpear {
	private static final ItemStack NETHER_GUIDE_CRIMSON = new ItemStack(
			ModItems.NETHER_GUIDE_CRIMSON_TEXTURE_HOLDER.get());

	private static final byte WARPED_MODE = 0;
	private static final byte CRIMSON_MODE = 1;
	private static final String TAG_MODE = "mode";
	private static final EntityDataAccessor<Byte> ID_MODE = SynchedEntityData.defineId(ThrownSpear.class,
			EntityDataSerializers.BYTE);

	public ThrownNetherGuide(EntityType<? extends ThrownSpear> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public ThrownNetherGuide(EntityType<? extends ThrownSpear> spearType, Level pLevel, LivingEntity pShooter, ItemStack pStack) {
		super(spearType, pLevel, pShooter, pStack);
	}

	public void setMode(byte mode) {
		this.entityData.set(ID_MODE, mode);
	}

	public byte getMode() {
		return this.entityData.get(ID_MODE);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ID_MODE, (byte) 0);
	}

	@Override
	public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.getEntityData().set(ID_MODE, pCompound.getByte(TAG_MODE));
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.putByte(TAG_MODE, this.entityData.get(ID_MODE));
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		super.onHitEntity(pResult);
		Entity entity = pResult.getEntity();
		if (this.getMode() == CRIMSON_MODE) {
			if (entity instanceof LivingEntity e) {
				e.addEffect(new MobEffectInstance(
						MobEffects.WEAKNESS,
						CRIMSON_WEAKNESS_DURATION_ON_TARGET_WHEN_HIT,
						CRIMSON_WEAKNESS_LEVEL_ON_TARGET_WHEN_HIT - 1)
				);
			}

			Entity owner = this.getOwner();
			if (owner instanceof LivingEntity livingOwner) {
				livingOwner.addEffect(new MobEffectInstance(
						MobEffects.REGENERATION,
						CRIMSON_REGENERATION_DURATION_ON_SELF_WHEN_HIT,
						CRIMSON_REGENERATION_LEVEL_ON_SELF_WHEN_HIT - 1)
				);
			}
		}
	}

	@Override
	public ItemStack getItemForRender() {
		if (this.getMode() == CRIMSON_MODE)
			return NETHER_GUIDE_CRIMSON;
		return super.getItemForRender();
	}
}
