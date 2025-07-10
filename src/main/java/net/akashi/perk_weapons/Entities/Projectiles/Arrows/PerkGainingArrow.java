package net.akashi.perk_weapons.Entities.Projectiles.Arrows;

import net.akashi.perk_weapons.Util.IPerkItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class PerkGainingArrow extends BaseArrow {
	private static final EntityDataAccessor<Boolean> ID_RENDER_TRAIL
			= SynchedEntityData.defineId(PerkGainingArrow.class, EntityDataSerializers.BOOLEAN);

	public static int MAX_GAINED_LEVEL_FROM_ONE_ARROW = 1;
	private static final String TAG_GAINED_LEVEL = "gained_level";

	private int gainedLevel = 0;

	public PerkGainingArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public PerkGainingArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, double pX, double pY, double pZ) {
		super(pEntityType, pLevel, pX, pY, pZ);
	}

	public PerkGainingArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, LivingEntity pShooter) {
		super(pEntityType, pLevel, pShooter);
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.putInt(TAG_GAINED_LEVEL, gainedLevel);
	}

	@Override
	public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.gainedLevel = pCompound.getInt(TAG_GAINED_LEVEL);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ID_RENDER_TRAIL, false);
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.inGround && this.entityData.get(ID_RENDER_TRAIL) && this.level().isClientSide()) {
			this.level().addParticle(ParticleTypes.CRIT,
					this.getX(), this.getY(), this.getZ(),
					0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	protected void onHitEntity(@NotNull EntityHitResult pResult) {
		if (!this.level().isClientSide()) {
			Entity owner = this.getOwner();
			if (owner instanceof Player player) {
				ItemStack perkItemStack = ItemStack.EMPTY;
				IPerkItem item = null;
				if (player.getMainHandItem().getItem() instanceof IPerkItem pItem) {
					perkItemStack = player.getMainHandItem();
					item = pItem;
				} else if (player.getOffhandItem().getItem() instanceof IPerkItem pItem) {
					perkItemStack = player.getOffhandItem();
					item = pItem;
				}
				if (perkItemStack != ItemStack.EMPTY && MAX_GAINED_LEVEL_FROM_ONE_ARROW > this.gainedLevel) {
					item.gainPerkLevel(player, perkItemStack);
					gainedLevel++;
				}
			}
		}
		super.onHitEntity(pResult);
	}

	public void setRenderTrail(boolean shouldRender) {
		this.entityData.set(ID_RENDER_TRAIL, shouldRender);
	}

}
