package net.akashi.perk_weapons.Entities.Projectiles.Arrows;

import net.akashi.perk_weapons.Network.OutOfSightExplosionSyncPacket;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Registry.ModPackets;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;
import org.joml.Vector3f;


public class ExplosiveArrow extends BaseArrow {
	private int fuseTime = 30;

	public ExplosiveArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	protected ExplosiveArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, double pX, double pY, double pZ, int fuseTime) {
		super(pEntityType, pLevel, pX, pY, pZ);
		this.fuseTime = fuseTime;
	}

	public ExplosiveArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, LivingEntity pShooter, int fuseTime) {
		super(pEntityType, pLevel, pShooter);
		this.fuseTime = fuseTime;
	}

	public ExplosiveArrow(PlayMessages.SpawnEntity spawnEntity, Level level) {
		this(ModEntities.EXPLOSIVE_ARROW.get(), level);
	}


	@Override
	protected ItemStack getPickupItem() {
		return null;
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		super.onHitEntity(pResult);
		this.explode();
	}

	@Override
	protected void onHit(HitResult pResult) {
		super.onHit(pResult);
	}

	@Override
	protected void onHitBlock(BlockHitResult pResult) {
		super.onHitBlock(pResult);
		this.explode();
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide()) {
			this.level().addParticle(ParticleTypes.SMOKE,
					this.getX(), this.getY(), this.getZ(),
					0.0D, 0.0D, 0.0D);
		} else {
			if (this.fuseTime > 0)
				this.fuseTime--;
		}
		if (this.fuseTime == 0) {
			this.explode();
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.fuseTime = pCompound.getInt("fuseTime");
	}

	@Override
	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.putInt("fuseTime", this.fuseTime);
	}

	public void explode() {
		Level level = this.level();
		if (!level.isClientSide()) {
			level.explode(this.getOwner(), this.getX(), this.getY(), this.getZ(),
					4.0F, Level.ExplosionInteraction.NONE);
		}
		if (this.getOwnerSqrDistance() > 64 * 64) {
			if (this.getOwner() instanceof Player player){
				ModPackets.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
						new OutOfSightExplosionSyncPacket(this.getX(), this.getY(), this.getZ(), player.getId()));
			}
		}
		this.discard();
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double pDistance) {
		return pDistance < 140 * 140;
	}

	@Override
	public boolean shouldRender(double pX, double pY, double pZ) {
		return true;
	}

	private double getOwnerSqrDistance() {
		Entity owner = this.getOwner();
		if (owner instanceof Player) {
			return Vector3f.distanceSquared(
					(float) this.getX(),
					(float) this.getY(),
					(float) this.getZ(),
					(float) owner.getX(),
					(float) owner.getY(),
					(float) owner.getZ());
		}
		return -1;
	}

}