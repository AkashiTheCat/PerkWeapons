package net.akashi.perk_weapons.Entities.Projectiles.Arrows;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BaseArrow extends AbstractArrow {
	public static final ResourceLocation NORMAL_ARROW_LOCATION = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/projectiles/arrow.png");
	public static final ResourceLocation TIPPED_ARROW_LOCATION = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/projectiles/tipped_arrow.png");
	public static final ResourceLocation SPECTRAL_ARROW_LOCATION = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/projectiles/spectral_arrow.png");
	private static final EntityDataAccessor<Integer> ID_EFFECT_COLOR = SynchedEntityData.defineId(BaseArrow.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> ID_IS_SPECTRAL = SynchedEntityData.defineId(BaseArrow.class, EntityDataSerializers.BOOLEAN);
	private float magicDamage = 0.0F;
	private boolean ignoreInvulnerableTime = false;
	private byte customPierceLevel = 0;
	private int customKnockback = 0;
	private boolean shotFromCrossbow = false;

	public BaseArrow(EntityType<? extends BaseArrow> entityType, Level level) {
		super(entityType, level);
	}

	public BaseArrow(EntityType<? extends BaseArrow> entityType, Level level, double x, double y, double z) {
		super(entityType, x, y, z, level, new ItemStack(Items.ARROW), new ItemStack(Items.BOW));
	}

	public BaseArrow(EntityType<? extends BaseArrow> entityType, Level level, LivingEntity shooter) {
		super(entityType, shooter, level, new ItemStack(Items.ARROW), resolveFiringWeapon(shooter));
	}

	private static ItemStack resolveFiringWeapon(LivingEntity shooter) {
		ItemStack weapon = shooter.getUseItem();
		if (weapon.isEmpty()) {
			weapon = shooter.getMainHandItem();
		}
		if (weapon.isEmpty()) {
			weapon = shooter.getOffhandItem();
		}
		return weapon.isEmpty() ? new ItemStack(Items.BOW) : weapon;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
		super.defineSynchedData(builder);
		builder.define(ID_EFFECT_COLOR, -1);
		builder.define(ID_IS_SPECTRAL, false);
	}

	public void setSpectralArrow(boolean spectralArrow) {
		this.entityData.set(ID_IS_SPECTRAL, spectralArrow);
	}

	public void setEffectsFromItem(ItemStack stack) {
		ItemStack pickupItem = stack.copyWithCount(1);
		if (stack.is(Items.SPECTRAL_ARROW)) {
			setSpectralArrow(true);
			setPickupItemStack(pickupItem);
			return;
		}

		setSpectralArrow(false);
		if (stack.is(Items.TIPPED_ARROW)) {
			setPickupItemStack(pickupItem);
		} else {
			setPickupItemStack(new ItemStack(Items.ARROW));
		}
	}

	public void addEffect(MobEffectInstance effect) {
		if (this.entityData.get(ID_IS_SPECTRAL)) {
			return;
		}
		PotionContents potionContents = getPotionContents().withEffectAdded(effect);
		this.getPickupItemStackOrigin().set(DataComponents.POTION_CONTENTS, potionContents);
		updateColor();
	}

	public java.util.List<MobEffectInstance> getEffects() {
		return getPotionContents().customEffects();
	}

	public ResourceLocation getArrowTexture() {
		if (this.entityData.get(ID_IS_SPECTRAL)) {
			return SPECTRAL_ARROW_LOCATION;
		}
		return getColor() != -1 ? TIPPED_ARROW_LOCATION : NORMAL_ARROW_LOCATION;
	}

	public static int getCustomColor(ItemStack stack) {
		PotionContents potionContents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
		return potionContents.equals(PotionContents.EMPTY) ? -1 : potionContents.getColor();
	}

	public int getColor() {
		return this.entityData.get(ID_EFFECT_COLOR);
	}

	private PotionContents getPotionContents() {
		return this.getPickupItemStackOrigin().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
	}

	private void updateColor() {
		PotionContents potionContents = getPotionContents();
		this.entityData.set(ID_EFFECT_COLOR, potionContents.equals(PotionContents.EMPTY) ? -1 : potionContents.getColor());
	}

	@Override
	protected void setPickupItemStack(@NotNull ItemStack stack) {
		super.setPickupItemStack(stack);
		updateColor();
	}

	public void setMagicDamage(float damageAmount) {
		this.magicDamage = damageAmount;
	}

	public void setIgnoreInvulnerableTime(boolean ignoreInvulnerableTime) {
		this.ignoreInvulnerableTime = ignoreInvulnerableTime;
	}

	public void setPierceLevel(byte pierceLevel) {
		this.customPierceLevel = pierceLevel;
	}

	public byte getPierceLevel() {
		return this.customPierceLevel;
	}

	public void setKnockback(int knockback) {
		this.customKnockback = knockback;
	}

	public int getKnockback() {
		return this.customKnockback;
	}

	public void setShotFromCrossbow(boolean shotFromCrossbow) {
		this.shotFromCrossbow = shotFromCrossbow;
	}


	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putFloat("magicDamage", this.magicDamage);
		tag.putBoolean("ignoreInvulnerableTime", this.ignoreInvulnerableTime);
		tag.putByte("perkWeaponsPierceLevel", this.customPierceLevel);
		tag.putInt("perkWeaponsKnockback", this.customKnockback);
		tag.putBoolean("perkWeaponsShotFromCrossbow", this.shotFromCrossbow);
		tag.putBoolean("perkWeaponsSpectral", this.entityData.get(ID_IS_SPECTRAL));
	}

	@Override
	public void readAdditionalSaveData(@NotNull CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.magicDamage = tag.getFloat("magicDamage");
		this.ignoreInvulnerableTime = tag.getBoolean("ignoreInvulnerableTime");
		this.customPierceLevel = tag.getByte("perkWeaponsPierceLevel");
		this.customKnockback = tag.getInt("perkWeaponsKnockback");
		this.shotFromCrossbow = tag.getBoolean("perkWeaponsShotFromCrossbow");
		this.entityData.set(ID_IS_SPECTRAL, tag.getBoolean("perkWeaponsSpectral"));
		updateColor();
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide) {
			if (this.inGround) {
				if (this.inGroundTime % 5 == 0) {
					makeParticle(1);
				}
			} else {
				makeParticle(2);
				if (this.entityData.get(ID_IS_SPECTRAL)) {
					this.level().addParticle(ParticleTypes.INSTANT_EFFECT, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
				}
			}
		} else if (this.inGround && this.inGroundTime != 0 && !this.entityData.get(ID_IS_SPECTRAL)
				&& !getPotionContents().equals(PotionContents.EMPTY) && this.inGroundTime >= 600) {
			this.level().broadcastEntityEvent(this, (byte) 0);
			setPickupItemStack(new ItemStack(Items.ARROW));
		}
	}

	private void makeParticle(int particleAmount) {
		int color = this.getColor();
		if (color == -1 || particleAmount <= 0) {
			return;
		}

		for (int i = 0; i < particleAmount; i++) {
			this.level().addParticle(
					ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, color),
					this.getRandomX(0.5D),
					this.getRandomY(),
					this.getRandomZ(0.5D),
					0.0D,
					0.0D,
					0.0D
			);
		}
	}

	@Override
	protected void onHitEntity(@NotNull EntityHitResult result) {
		if (ignoreInvulnerableTime) {
			result.getEntity().invulnerableTime = 0;
		}
		super.onHitEntity(result);
		if (magicDamage > 0.0F) {
			result.getEntity().hurt(this.damageSources().magic(), magicDamage);
		}
	}

	@Override
	protected void doKnockback(@NotNull LivingEntity target, @NotNull DamageSource damageSource) {
		super.doKnockback(target, damageSource);
		if (this.customKnockback <= 0) {
			return;
		}

		double resistanceScale = Math.max(0.0D, 1.0D - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
		Vec3 knockback = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale(this.customKnockback * 0.6D * resistanceScale);
		if (knockback.lengthSqr() > 0.0D) {
			target.push(knockback.x, 0.1D, knockback.z);
		}
	}

	@Override
	protected void doPostHurtEffects(@NotNull LivingEntity target) {
		super.doPostHurtEffects(target);
		Entity entity = this.getEffectSource();
		PotionContents potionContents = getPotionContents();

		if (potionContents.potion().isPresent()) {
			for (MobEffectInstance effect : potionContents.potion().get().value().getEffects()) {
				target.addEffect(
						new MobEffectInstance(
								effect.getEffect(),
								Math.max(effect.mapDuration(duration -> duration / 8), 1),
								effect.getAmplifier(),
								effect.isAmbient(),
								effect.isVisible()),
						entity);
			}
		}

		for (MobEffectInstance effect : potionContents.customEffects()) {
			target.addEffect(effect, entity);
		}
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == 0) {
			int color = this.getColor();
			if (color != -1) {
				float red = (float) (color >> 16 & 255) / 255.0F;
				float green = (float) (color >> 8 & 255) / 255.0F;
				float blue = (float) (color & 255) / 255.0F;

				for (int i = 0; i < 20; i++) {
					this.level().addParticle(
							ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, red, green, blue),
							this.getRandomX(0.5D),
							this.getRandomY(),
							this.getRandomZ(0.5D),
							0.0D,
							0.0D,
							0.0D
					);
				}
			}
			return;
		}
		super.handleEntityEvent(id);
	}

	@Override
	protected @NotNull ItemStack getDefaultPickupItem() {
		return new ItemStack(Items.ARROW);
	}
}
