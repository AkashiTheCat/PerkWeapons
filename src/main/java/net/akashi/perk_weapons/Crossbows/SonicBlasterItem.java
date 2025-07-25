package net.akashi.perk_weapons.Crossbows;

import com.google.common.collect.ImmutableMultimap;
import net.akashi.perk_weapons.Config.Properties.Crossbow.CrossbowProperties;
import net.akashi.perk_weapons.Config.Properties.Crossbow.SonicBlasterProperties;
import net.akashi.perk_weapons.Util.SoundEventHolder;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

import static net.minecraft.world.item.enchantment.Enchantments.*;

public class SonicBlasterItem extends BaseCrossbowItem {
	protected static final SoundEventHolder LOADING_START_SOUND = new SoundEventHolder(SoundEvents.WARDEN_SONIC_CHARGE,
			0.5F, 1F);
	protected static final SoundEventHolder LOADING_END_SOUND = new SoundEventHolder(SoundEvents.WARDEN_HEARTBEAT,
			0.5F, 1F);

	public static final UUID KNOCKBACK_RESISTANCE_UUID = UUID.fromString("add5510b-4b2c-773b-3211-3e42a2331a49");
	public static final String TAG_AMMO_LOADED = "ammo_loaded";
	public static float KNOCKBACK_RESISTANCE = 10;
	public static int MAX_ATTACK_RANGE = 24;
	public static double DAMAGE_RADIUS = 1.0;
	public static int PIERCE_LEVEL = -1;
	public static boolean HAS_KNOCKBACK = false;
	public static double KNOCKBACK_FORCE = 0.6F;

	public SonicBlasterItem(Properties pProperties) {
		super(pProperties);
		RemoveGeneralEnchant(QUICK_CHARGE);
		RemoveGeneralEnchant(MULTISHOT);
		RemoveGeneralEnchant(POWER_ARROWS);
		if (PIERCE_LEVEL == -1) {
			RemoveGeneralEnchant(PIERCING);
		}
		AMMO_CAPACITY = 0;
	}

	public SonicBlasterItem(int maxChargeTicks, float damage, float velocity, float inaccuracy,
	                        int ammoCapacity, int fireInterval, float speedModifier,
	                        boolean onlyAllowMainHand, Properties pProperties) {
		super(maxChargeTicks, damage, velocity, inaccuracy, ammoCapacity, fireInterval,
				speedModifier, onlyAllowMainHand, pProperties);
		AMMO_CAPACITY = 0;
		RemoveGeneralEnchant(QUICK_CHARGE);
		RemoveGeneralEnchant(MULTISHOT);
		RemoveGeneralEnchant(POWER_ARROWS);
		if (PIERCE_LEVEL == -1) {
			RemoveGeneralEnchant(PIERCING);
		}
	}

	@Override
	protected void buildAttributeModifiers() {
		super.buildAttributeModifiers();
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		if (AttributeModifiers != null)
			builder.putAll(AttributeModifiers);
		if (KNOCKBACK_RESISTANCE != 0.0F) {
			builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(KNOCKBACK_RESISTANCE_UUID,
					"Tool modifier", KNOCKBACK_RESISTANCE, AttributeModifier.Operation.ADDITION));
			this.ONLY_ALLOW_MAINHAND = true;
		}
		this.AttributeModifiers = builder.build();
	}

	@Override
	public void updateAttributesFromConfig(CrossbowProperties properties) {
		VELOCITY = -1;
		if (properties instanceof SonicBlasterProperties sProperties) {
			MAX_ATTACK_RANGE = sProperties.MAX_RANGE.get();
			DAMAGE_RADIUS = sProperties.DAMAGE_RADIUS.get();
			PIERCE_LEVEL = sProperties.PIERCE_LEVEL.get();
			HAS_KNOCKBACK = sProperties.ENABLE_KNOCKBACK.get();
			KNOCKBACK_FORCE = sProperties.KNOCKBACK_FORCE.get();
			KNOCKBACK_RESISTANCE = sProperties.KNOCKBACK_RESISTANCE.get().floatValue();

			if (PIERCE_LEVEL == -1 && this.GeneralEnchants.contains(PIERCING)) {
				RemoveGeneralEnchant(PIERCING);
			}
			if (PIERCE_LEVEL != -1 && !this.GeneralEnchants.contains(PIERCING)) {
				AddGeneralEnchant(PIERCING);
			}
		}
		super.updateAttributesFromConfig(properties);
	}

	@Override
	public void releaseUsing(@NotNull ItemStack crossbowStack, @NotNull Level level, @NotNull LivingEntity shooter, int useTimeLeft) {
		float progress = getChargeProgress(shooter, crossbowStack);

		if (progress >= 1.0F && !isCrossbowCharged(crossbowStack)) {
			setCrossbowCharged(crossbowStack, true);
			setAmmoLoaded(crossbowStack, getAmmoCapacity(crossbowStack));
			SoundSource soundsource = shooter instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
			SoundEventHolder endSound = getEndSound(shooter, crossbowStack);
			if (endSound.soundEvent != null) {
				level.playSound(null, shooter, endSound.soundEvent, soundsource, endSound.volume,
						endSound.pitch / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
			}
		}
	}

	@Override
	protected @NotNull SoundEventHolder getEndSound(LivingEntity shooter, ItemStack crossbowStack) {
		return LOADING_END_SOUND;
	}

	@Override
	protected @NotNull SoundEventHolder getStartSound(LivingEntity shooter, ItemStack crossbowStack) {
		return LOADING_START_SOUND;
	}

	@Override
	protected @NotNull SoundEventHolder getMiddleSound(LivingEntity shooter, ItemStack crossbowStack) {
		return SoundEventHolder.empty();
	}

	@Override
	protected boolean canLoadAmmo(LivingEntity shooter, ItemStack crossbowStack) {
		return true;
	}

	@Override
	public void inventoryTick(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull Entity pEntity,
	                          int pSlotId, boolean pIsSelected) {
		super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
		if (pLevel.isClientSide() && pEntity instanceof Player player && pIsSelected && isCrossbowCharged(pStack) &&
				pLevel.getGameTime() % 40 == 0) {
			pLevel.playSound(player, player, SoundEvents.WARDEN_HEARTBEAT, SoundSource.PLAYERS,
					0.8f, 1.0f);
		}
	}

	@Override
	protected void shoot(Level level, LivingEntity shooter, InteractionHand hand, ItemStack crossbowStack,
	                     float damage, float velocity, float inaccuracy) {
		renderShootingParticles(level, shooter);

		if (!level.isClientSide()) {
			Vec3 startPos = shooter.getEyePosition(1.0F);
			Vec3 viewVec = shooter.getViewVector(1.0F).normalize();
			Vec3 endPos = startPos.add(viewVec.scale(MAX_ATTACK_RANGE));

			AABB searchAABB = shooter.getBoundingBox().expandTowards(viewVec.scale(MAX_ATTACK_RANGE))
					.inflate(DAMAGE_RADIUS);
			List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, searchAABB,
					entity -> !entity.is(shooter));

			Vec3 knockbackVec = new Vec3(viewVec.x, 0.1F, viewVec.z).normalize().scale(KNOCKBACK_FORCE);
			int pierceLevel = getPierceLevel(crossbowStack);
			int allowedEntityCount = pierceLevel < 0 ? -1 : pierceLevel + 1;

			for (int i = 0; allowedEntityCount != 0 && i < entities.size(); i++) {
				Entity entity = entities.get(i);
				if (entity.getBoundingBox().inflate(DAMAGE_RADIUS).clip(startPos, endPos).isEmpty()) {
					continue;
				}

				entity.hurt(shooter.damageSources().sonicBoom(shooter), this.DAMAGE);
				if (HAS_KNOCKBACK)
					entity.addDeltaMovement(knockbackVec);
				allowedEntityCount--;
			}

			SoundSource soundsource = shooter instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
			level.playSound(null, shooter, SoundEvents.WARDEN_SONIC_BOOM, soundsource, 1.0F,
					0.8F + level.random.nextFloat() * 0.4F);

		}

	}

	protected void renderShootingParticles(Level level, LivingEntity shooter) {
		if (level.isClientSide())
			return;

		Vec3 particleXYZ = new Vec3(shooter.getX(), shooter.getBoundingBox().getYsize() * 0.5 + shooter.getY(),
				shooter.getZ());
		float interval = 1.2F;
		Vec3 delta = shooter.getViewVector(1.0F).normalize().scale(interval);
		int particleAmount = Math.round(MAX_ATTACK_RANGE / interval);

		for (int i = 0; i < particleAmount; i++) {
			((ServerLevel) level).sendParticles(ParticleTypes.SONIC_BOOM, particleXYZ.x, particleXYZ.y, particleXYZ.z,
					1, 0, 0, 0, 0);
			particleXYZ = particleXYZ.add(delta);
		}
	}

	private int getPierceLevel(ItemStack crossbowStack) {
		return PIERCE_LEVEL == -1 ? -1 : PIERCE_LEVEL + crossbowStack.getEnchantmentLevel(PIERCING);
	}

	private int getAmmoLoaded(ItemStack crossbowStack) {
		CompoundTag nbt = crossbowStack.getOrCreateTag();
		return nbt.contains(TAG_AMMO_LOADED) ? nbt.getInt(TAG_AMMO_LOADED) : 0;
	}

	private void setAmmoLoaded(ItemStack crossbowStack, int amount) {
		CompoundTag nbt = crossbowStack.getOrCreateTag();
		nbt.putInt(TAG_AMMO_LOADED, amount);
	}

	@Override
	public void consumeAndSetCharged(LivingEntity shooter, ItemStack crossbowStack) {
		int ammoLeft = getAmmoLoaded(crossbowStack) - 1;
		setAmmoLoaded(crossbowStack, ammoLeft);
		if (ammoLeft <= 0) {
			setCrossbowCharged(crossbowStack, false);
		}
	}

	@Override
	public int getChargedProjectileAmount(ItemStack crossbowStack) {
		return getAmmoLoaded(crossbowStack);
	}

	@Override
	public Component getWeaponDescription(ItemStack stack, Level level) {
		return Component.translatable("tooltip.perk_weapons.sonic_blaster").withStyle(ChatFormatting.GRAY);
	}

	@Override
	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		List<Component> list = super.getPerkDescriptions(stack, level);

		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.sonic_blaster_perk_1")));
		list.add(TooltipHelper.setSubPerkStyle(Component.translatable("tooltip.perk_weapons.sonic_blaster_perk_2",
				TooltipHelper.convertToEmbeddedElement(MAX_ATTACK_RANGE))));

		return list;
	}
}
