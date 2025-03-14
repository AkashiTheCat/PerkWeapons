package net.akashi.perk_weapons.Entities.Projectiles.Arrows;

import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownSpear;
import net.akashi.perk_weapons.Network.OutOfSightExplosionSyncPacket;
import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Registry.ModPackets;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;
import org.joml.Vector3f;


public class ExplosiveArrow extends BaseArrow {
	public static final ResourceLocation EXPLOSIVE_ARROW_LOCATION =
			new ResourceLocation(PerkWeapons.MODID, "textures/entity/projectiles/explosive_arrow.png");
	private int fuseTime = 30;
	private int attachedEntityID = 0;

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
	public void setPierceLevel(byte pPierceLevel) {
		super.setPierceLevel((byte) 1);
	}

	@Override
	public byte getPierceLevel() {
		return 1;
	}

	@Override
	protected ItemStack getPickupItem() {
		return null;
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
			if (this.fuseTime == 0) {
				this.explode();
			}
		}

		Entity entity = level().getEntity(this.attachedEntityID);
		if (entity == null)
			return;

		this.setPos(entity.position());
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		super.onHitEntity(pResult);
		Entity entity = pResult.getEntity();
		if (entity.isAlive()) {
			this.attachedEntityID = entity.getId();
			this.setNoPhysics(true);
			this.setInvisible(true);
		} else {
			this.setDeltaMovement(this.getDeltaMovement().multiply(
					-0.01D, -0.1D, -0.01D
			));
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.fuseTime = pCompound.getInt("fuseTime");
		this.attachedEntityID = pCompound.getInt("attID");
	}

	@Override
	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.putInt("fuseTime", this.fuseTime);
		pCompound.putInt("attID", this.attachedEntityID);
	}

	private void putVec3(CompoundTag tag, String key, Vec3 vec3) {
		tag.putDouble(key + "_x", vec3.x);
		tag.putDouble(key + "_y", vec3.y);
		tag.putDouble(key + "_z", vec3.z);
	}

	private Vec3 readVec3(CompoundTag tag, String key) {
		double x = tag.getDouble(key + "_x");
		double y = tag.getDouble(key + "_y");
		double z = tag.getDouble(key + "_z");
		return new Vec3(x, y, z);
	}

	public void explode() {
		Level level = this.level();
		if (!level.isClientSide()) {
			level.explode(this.getOwner(), this.getX(), this.getY(), this.getZ(),
					4.0F, Level.ExplosionInteraction.NONE);

			if (this.getOwnerSqrDistance() > 64 * 64) {
				if (this.getOwner() instanceof Player player) {
					ModPackets.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
							new OutOfSightExplosionSyncPacket(this.getX(), this.getY(), this.getZ(), player.getId()));
				}
			}
		}
		this.discard();
	}

	@Override
	public ResourceLocation getArrowTexture() {
		return EXPLOSIVE_ARROW_LOCATION;
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double pDistance) {
		return pDistance < 140 * 140;
	}

	@Override
	public boolean shouldRender(double pX, double pY, double pZ) {
		return !this.isInvisible();
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
