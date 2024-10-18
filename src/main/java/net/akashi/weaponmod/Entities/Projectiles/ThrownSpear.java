package net.akashi.weaponmod.Entities.Projectiles;

import net.akashi.weaponmod.Registry.ModEntities;
import net.akashi.weaponmod.Registry.ModItems;
import net.akashi.weaponmod.Spears.SpearItem;
import net.akashi.weaponmod.WeaponMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;
import org.stringtemplate.v4.ST;

import javax.annotation.Nullable;

public class ThrownSpear extends AbstractArrow {
	private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(ThrownSpear.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownSpear.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<ItemStack> ID_SPEAR_ITEM = SynchedEntityData.defineId(ThrownSpear.class, EntityDataSerializers.ITEM_STACK);
	public boolean dealtDamage;
	private int ReturnSlot;
	public int clientSideReturnSpearTickCount;
	private float baseDamage = 5.0f;

	public ThrownSpear(EntityType<? extends ThrownSpear> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public ThrownSpear(Level pLevel, LivingEntity pShooter, ItemStack pStack, int ReturnSlot, EntityType<? extends ThrownSpear> spearType) {
		super(spearType, pShooter, pLevel);
		this.entityData.set(ID_SPEAR_ITEM, pStack);
		this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(pStack));
		this.entityData.set(ID_FOIL, pStack.hasFoil());
		this.ReturnSlot = ReturnSlot;
		setKnockback(EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, pShooter));
	}

	public ItemStack getSpearItem() {
		return getPickupItem();
	}

	public ThrownSpear setBaseDamage(float baseDamage) {
		this.baseDamage = baseDamage;
		return this;
	}


	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ID_LOYALTY, (byte) 0);
		this.entityData.define(ID_FOIL, false);
		this.entityData.define(ID_SPEAR_ITEM, ModItems.IRON_SPEAR.get().getDefaultInstance());
	}

	@Override
	protected boolean tryPickup(Player pPlayer) {
		if (!pPlayer.isCreative()) {
			if(pPlayer.getInventory().add(ReturnSlot, this.getPickupItem())){
				return true;
			}
		}
		return super.tryPickup(pPlayer);
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		Entity entity = pResult.getEntity();
		float f = baseDamage;
		if (entity instanceof LivingEntity livingentity) {
			f += EnchantmentHelper.getDamageBonus(getSpearItem(), livingentity.getMobType());
		}

		Entity entity1 = this.getOwner();
		DamageSource damagesource = this.damageSources().trident(this, (Entity) (entity1 == null ? this : entity1));
		this.dealtDamage = true;
		SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
		if (entity.hurt(damagesource, f)) {
			if (entity.getType() == EntityType.ENDERMAN) {
				return;
			}

			if (entity instanceof LivingEntity) {
				LivingEntity livingentity = (LivingEntity) entity;
				if (getKnockback() > 0) {
					double d0 = Math.max(0.0D, 1.0D - livingentity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
					Vec3 vec3 = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double)getKnockback() * 0.6D * d0);
					if (vec3.lengthSqr() > 0.0D) {
						livingentity.push(vec3.x, 0.1D, vec3.z);
					}
				}
				if (entity1 instanceof LivingEntity) {
					EnchantmentHelper.doPostHurtEffects(livingentity, entity1);
					EnchantmentHelper.doPostDamageEffects((LivingEntity) entity1, livingentity);
				}

				this.doPostHurtEffects(livingentity);
			}
		}


		this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
		float f1 = 1.0F;
		if (this.level() instanceof ServerLevel && this.level().isThundering() && this.isChanneling()) {
			BlockPos blockpos = entity.blockPosition();
			if (this.level().canSeeSky(blockpos)) {
				LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this.level());
				if (lightningbolt != null) {
					lightningbolt.moveTo(Vec3.atBottomCenterOf(blockpos));
					lightningbolt.setCause(entity1 instanceof ServerPlayer ? (ServerPlayer) entity1 : null);
					this.level().addFreshEntity(lightningbolt);
					soundevent = SoundEvents.TRIDENT_THUNDER;
					f1 = 5.0F;
				}
			}
		}

		this.playSound(soundevent, f1, 1.0F);
	}

	@Override
	protected ItemStack getPickupItem() {
		return getEntityData().get(ID_SPEAR_ITEM);
	}

	public boolean isChanneling() {
		return EnchantmentHelper.hasChanneling(getSpearItem());
	}

	@Override
	@Nullable
	protected EntityHitResult findHitEntity(Vec3 pStartVec, Vec3 pEndVec) {
		return this.dealtDamage ? null : super.findHitEntity(pStartVec, pEndVec);
	}

	@Override
	public void tick() {
		if (this.inGroundTime > 4) {
			this.dealtDamage = true;
		}

		Entity entity = this.getOwner();
		int loyaltyLevel = getLoyaltyLevel();
		if (loyaltyLevel > 0 && (this.dealtDamage || this.isNoPhysics()) && entity != null) {
			if (!this.isAcceptableReturnOwner()) {
				if (!this.level().isClientSide && this.pickup == Pickup.ALLOWED) {
					this.spawnAtLocation(this.getPickupItem(), 0.1F);
				}
				this.discard();
			} else {
				this.setNoPhysics(true);
				Vec3 vec3 = entity.getEyePosition().subtract(this.position());
				this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015D * (double) loyaltyLevel, this.getZ());
				if (this.level().isClientSide) {
					this.yOld = this.getY();
				}

				double d0 = 0.05D * (double) loyaltyLevel;
				this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(d0)));
				if (this.clientSideReturnSpearTickCount == 0) {
					this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
				}

				++this.clientSideReturnSpearTickCount;
			}
		}

		super.tick();
	}

	private boolean isAcceptableReturnOwner() {
		Entity entity = this.getOwner();
		if (entity != null && entity.isAlive()) {
			return !(entity instanceof ServerPlayer) || !entity.isSpectator();
		} else {
			return false;
		}
	}

	@Override
	protected SoundEvent getDefaultHitGroundSoundEvent() {
		return SoundEvents.TRIDENT_HIT_GROUND;
	}

	@Override
	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		ItemStack spearItem = ModItems.IRON_SPEAR.get().getDefaultInstance();
		if (pCompound.contains("spear", 10)) {
			spearItem = ItemStack.of(pCompound.getCompound("spear"));
		}
		this.dealtDamage = pCompound.getBoolean("dealtdamage");
		this.ReturnSlot = pCompound.getInt("returnslot");
		this.baseDamage = pCompound.getFloat("damage");
		this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(spearItem));
		this.entityData.set(ID_SPEAR_ITEM, spearItem);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.put("spear", getSpearItem().save(new CompoundTag()));
		pCompound.putBoolean("dealtdamage", this.dealtDamage);
		pCompound.putInt("returnslot", this.ReturnSlot);
		pCompound.putFloat("damage", this.baseDamage);
	}

	@Override
	public void tickDespawn() {
		int i = this.entityData.get(ID_LOYALTY);
		if (this.pickup != Pickup.ALLOWED || i <= 0) {
			super.tickDespawn();
		}
	}

	@Override
	public void playerTouch(Player pEntity) {
		if (this.ownedBy(pEntity) || this.getOwner() == null) {
			super.playerTouch(pEntity);
		}
	}

	@Override
	public boolean shouldRender(double pX, double pY, double pZ) {
		return true;
	}

	public boolean isFoil() {
		return this.entityData.get(ID_FOIL);
	}

	public int getLoyaltyLevel(){
		return this.entityData.get(ID_LOYALTY);
	}

}
