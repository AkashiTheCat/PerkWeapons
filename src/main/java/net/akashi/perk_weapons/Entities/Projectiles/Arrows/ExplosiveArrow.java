package net.akashi.perk_weapons.Entities.Projectiles.Arrows;

import net.akashi.perk_weapons.Network.OutOfSightExplosionSyncPacket;
import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModEffects;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Registry.ModPackets;
import net.akashi.perk_weapons.Util.ModExplosion;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;


public class ExplosiveArrow extends BaseArrow {
	public static ResourceLocation EXPLOSIVE_ARROW_LOCATION = new ResourceLocation(PerkWeapons.MODID,
			"textures/entity/projectiles/explosive_arrow.png");
	private int fuseTime = 30;
	private float innerRadius = 2;
	private float outerRadius = 5;
	private float innerDamage = 30;
	private float outerDamage = 15;
	private float expForce = 1;
	private boolean expIgnoreWall = false;
	private int internalExpDuration = 20;
	private int internalExpAmplifier = 0;

	public ExplosiveArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	protected ExplosiveArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, double pX, double pY, double pZ, int fuseTime) {
		super(pEntityType, pLevel, pX, pY, pZ);
		this.fuseTime = fuseTime;
		this.pickup = Pickup.DISALLOWED;
	}

	public ExplosiveArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, LivingEntity pShooter, int fuseTime) {
		super(pEntityType, pLevel, pShooter);
		this.fuseTime = fuseTime;
		this.pickup = Pickup.DISALLOWED;
	}

	public ExplosiveArrow(PlayMessages.SpawnEntity spawnEntity, Level level) {
		this(ModEntities.EXPLOSIVE_ARROW.get(), level);
	}

	public void setExplosionAttributes(float innerRadius, float outerRadius, float innerDamage, float outerDamage,
	                                   float knockback, boolean ignoreWall,
	                                   int internalExpDuration, int internalExpAmplifier) {
		this.innerRadius = innerRadius;
		this.innerDamage = innerDamage;
		this.outerRadius = outerRadius;
		this.outerDamage = outerDamage;
		this.expForce = knockback;
		this.expIgnoreWall = ignoreWall;
		this.internalExpDuration = internalExpDuration;
		this.internalExpAmplifier = internalExpAmplifier;
	}

	@Override
	public void setPierceLevel(byte pPierceLevel) {
		super.setPierceLevel((byte) 127);
	}

	@Override
	public byte getPierceLevel() {
		return 127;
	}

	@Override
	protected @NotNull ItemStack getPickupItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide()) {
			this.level().addParticle(ParticleTypes.SMOKE,
					this.getX(), this.getY(), this.getZ(),
					0.0D, 0.0D, 0.0D);
		} else if (this.fuseTime > 0) {
			this.fuseTime--;
			if (this.fuseTime == 0) {
				this.explode();
			}
		}
	}

	@Override
	protected void onHitEntity(@NotNull EntityHitResult pResult) {
		super.onHitEntity(pResult);
		Entity entity = pResult.getEntity();
		if (entity.isAlive() && entity instanceof LivingEntity livingEntity) {
			livingEntity.addEffect(new MobEffectInstance(ModEffects.INTERNAL_EXPLOSION.get(),
					internalExpDuration, internalExpAmplifier, false, false), this.getOwner());
			this.discard();
		} else {
			this.setDeltaMovement(this.getDeltaMovement().multiply(
					-0.01D, -0.1D, -0.01D));
		}
	}

	@Override
	public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		this.fuseTime = pCompound.getInt("fuseTime");
		this.innerRadius = pCompound.getFloat("innerRadius");
		this.outerRadius = pCompound.getFloat("outerRadius");
		this.innerDamage = pCompound.getFloat("innerDamage");
		this.outerDamage = pCompound.getFloat("outerDamage");
		this.expForce = pCompound.getFloat("expForce");
		this.expIgnoreWall = pCompound.getBoolean("expIgnoreWall");
		this.internalExpDuration = pCompound.getInt("internalExpDuration");
		this.internalExpAmplifier = pCompound.getInt("internalExpAmplifier");
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.putInt("fuseTime", this.fuseTime);
		pCompound.putFloat("innerRadius", this.innerRadius);
		pCompound.putFloat("outerRadius", this.outerRadius);
		pCompound.putFloat("innerDamage", this.innerDamage);
		pCompound.putFloat("outerDamage", this.outerDamage);
		pCompound.putBoolean("expIgnoreWall", this.expIgnoreWall);
		pCompound.putInt("internalExpDuration", this.internalExpDuration);
		pCompound.putInt("internalExpAmplifier", this.internalExpAmplifier);
	}

	public void explode() {
		Level level = this.level();
		ModExplosion.createExplosion(level, (LivingEntity) this.getOwner(), this.getX(), this.getY(), this.getZ(),
				innerRadius, outerRadius, innerDamage, outerDamage, expForce, expIgnoreWall);
		if (!level.isClientSide() && this.getOwnerSqrDistance() > 64 * 64 &&
				this.getOwner() instanceof Player player) {
			ModPackets.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
					new OutOfSightExplosionSyncPacket(this.getX(), this.getY(), this.getZ(), player.getId()));
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
		if (owner != null) {
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
