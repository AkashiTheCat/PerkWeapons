package net.akashi.perk_weapons.Crossbows;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.akashi.perk_weapons.Config.Properties.Crossbow.CrossbowProperties;
import net.akashi.perk_weapons.Config.Properties.Crossbow.PaladinProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.BaseArrow;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.PaladinArrow;
import net.akashi.perk_weapons.Registry.ModAttributes;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Registry.ModSoundEvents;
import net.akashi.perk_weapons.Util.ICoolDownItem;
import net.akashi.perk_weapons.Util.IPerkItem;
import net.akashi.perk_weapons.Util.SoundEventHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaladinItem extends AutoLoadingCrossbowItem implements IPerkItem, ICoolDownItem {
	protected static final SoundEventHolder SHOOTING_SOUND = new SoundEventHolder(ModSoundEvents.PALADIN_FIRE.get(),
			0.7F, 1.0F);

	public static final UUID KNOCKBACK_RESISTANCE_UUID = UUID.fromString("47e87eb7-7a3f-738c-13a9-7e6fdfa9b838");
	public static final UUID MAGIC_RESISTANCE_UUID = UUID.fromString("936e61dd-10ea-7a2a-d612-44acf75457bd");
	public static final UUID DAMAGE_RESISTANCE_UUID = UUID.fromString("0c513885-1e62-9178-31b0-716dcda83873");
	private final List<Multimap<Attribute, AttributeModifier>> PerkLevelAttributeModifiers = new ArrayList<>();

	public static final String TAG_CUSTOM_CHARGED = "charged1";
	public static final String TAG_LAST_HIT = "last_hit";

	public static float KNOCKBACK_RESISTANCE = 1;
	public static float MAGIC_RESISTANCE = 50;
	public static float DAMAGE_RESISTANCE = -30;
	public static byte MAX_PERK_LEVEL = 10;
	public static float RELOAD_REDUCTION_PER_LEVEL = 0.07f;
	public static float DAMAGE_RESISTANCE_PER_LEVEL = 10F;
	public static int PERK_CLEAR_TIME_WITHOUT_HIT = 60;

	public PaladinItem(Properties pProperties) {
		super(pProperties);
		buildAttributeModifierMap();
		RemoveGeneralEnchant(Enchantments.MULTISHOT);
	}

	public PaladinItem(int maxChargeTicks, float damage, float velocity,
	                   float inaccuracy, int ammoCapacity, int fireInterval,
	                   float speedModifier, boolean onlyAllowMainHand,
	                   Properties pProperties) {
		super(maxChargeTicks, damage, velocity, inaccuracy, ammoCapacity, fireInterval,
				speedModifier, onlyAllowMainHand, pProperties);
		RemoveGeneralEnchant(Enchantments.MULTISHOT);
	}

	private void buildAttributeModifierMap() {
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		if (AttributeModifiers != null)
			builder.putAll(AttributeModifiers);
		if (KNOCKBACK_RESISTANCE != 0.0) {
			builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(KNOCKBACK_RESISTANCE_UUID,
					"Knockback Resistance", KNOCKBACK_RESISTANCE, AttributeModifier.Operation.ADDITION));
			this.onlyAllowMainHand = true;
		}
		if (MAGIC_RESISTANCE != 0.0) {
			builder.put(ModAttributes.MAGIC_RESISTANCE.get(), new AttributeModifier(MAGIC_RESISTANCE_UUID,
					"Magic Resistance", MAGIC_RESISTANCE, AttributeModifier.Operation.ADDITION));
			this.onlyAllowMainHand = true;
		}
		if (DAMAGE_RESISTANCE != 0.0 || DAMAGE_RESISTANCE_PER_LEVEL != 0.0) {
			builder.put(ModAttributes.DAMAGE_RESISTANCE.get(), new AttributeModifier(DAMAGE_RESISTANCE_UUID,
					"Damage Resistance", DAMAGE_RESISTANCE, AttributeModifier.Operation.ADDITION));
			this.onlyAllowMainHand = true;
		}
		AttributeModifiers = builder.build();

		buildPerkLevelAttributeModifierList();
	}

	private void buildPerkLevelAttributeModifierList() {
		PerkLevelAttributeModifiers.clear();
		PerkLevelAttributeModifiers.add(0, AttributeModifiers);
		for (int i = 1; i <= MAX_PERK_LEVEL; i++) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			if (AttributeModifiers != null)
				builder.putAll(AttributeModifiers);
			builder.put(ModAttributes.DAMAGE_RESISTANCE.get(), new AttributeModifier(DAMAGE_RESISTANCE_UUID,
					"Damage Resistance", DAMAGE_RESISTANCE_PER_LEVEL * i, AttributeModifier.Operation.ADDITION));
			PerkLevelAttributeModifiers.add(i, builder.build());
		}
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		byte perkLevel = (byte) getPerkLevel(null, stack);
		return slot == EquipmentSlot.MAINHAND ? PerkLevelAttributeModifiers.get(perkLevel) : ImmutableMultimap.of();
	}

	@Override
	public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
		super.inventoryTick(stack, level, entity, slotId, isSelected);
		if (!isSelected) {
			setPerkLevel(stack, (byte) 0);
			return;
		}
		if (level.getGameTime() - getLastHitTime(stack) >= PERK_CLEAR_TIME_WITHOUT_HIT) {
			setPerkLevel(stack, (byte) 0);
		}
	}

	@Override
	protected Projectile getProjectile(Level level, LivingEntity shooter, ItemStack crossbowStack) {
		BaseCrossbowItem crossbowItem = (BaseCrossbowItem) crossbowStack.getItem();
		ItemStack ammoStack = crossbowItem.getLastChargedProjectile(crossbowStack);

		if (ammoStack.is(Items.FIREWORK_ROCKET)) {
			return new FireworkRocketEntity(level, ammoStack, shooter, shooter.getX(),
					shooter.getEyeY() - (double) 0.15F, shooter.getZ(), true);
		}

		BaseArrow arrow = new PaladinArrow(ModEntities.PALADIN_ARROW.get(), level, shooter);
		if (ammoStack.is(Items.SPECTRAL_ARROW)) {
			arrow.setSpectralArrow(true);
		} else {
			arrow.setEffectsFromItem(ammoStack);
		}
		arrow.setIgnoreInvulnerableTime(true);
		arrow.setShotFromCrossbow(true);

		return arrow;
	}

	@Override
	public int getMaxChargeTicks(ItemStack crossbowStack) {
		byte perkLevel = (byte) getPerkLevel(null, crossbowStack);
		int reloadTicks = (int) Math.ceil(super.getMaxChargeTicks(crossbowStack) * (1 - (RELOAD_REDUCTION_PER_LEVEL * perkLevel)));
		System.out.println(reloadTicks);
		return reloadTicks;
	}

	@Override
	public int getCrossbowEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
		if (enchantment == Enchantments.PIERCING)
			return stack.getEnchantmentLevel(enchantment) + 1;
		return super.getCrossbowEnchantmentLevel(stack, enchantment);
	}

	@Override
	public void gainPerkLevel(LivingEntity entity, ItemStack stack) {
		IPerkItem.super.gainPerkLevel(entity, stack);
		setLastHitTime(stack, entity.level().getGameTime());
	}

	@Override
	public byte getMaxPerkLevel() {
		return MAX_PERK_LEVEL;
	}

	@Override
	public float getCoolDownProgress(LivingEntity owner, ItemStack stack) {
		if (getPerkLevel(owner, stack) == 0)
			return 0;
		float ratio = (float) (owner.level().getGameTime() - getLastHitTime(stack)) / PERK_CLEAR_TIME_WITHOUT_HIT;
		return Math.max(1 - ratio, 0);
	}

	@Override
	protected @NotNull SoundEventHolder getShootSound(LivingEntity shooter, ItemStack crossbowStack) {
		return SHOOTING_SOUND;
	}


	@Override
	public boolean isCrossbowCharged(ItemStack crossbowStack) {
		CompoundTag nbt = crossbowStack.getOrCreateTag();
		return nbt.contains(TAG_CUSTOM_CHARGED) && nbt.getBoolean(TAG_CUSTOM_CHARGED);
	}

	@Override
	public void setCrossbowCharged(ItemStack crossbowStack, boolean charged) {
		CompoundTag tag = crossbowStack.getOrCreateTag();
		tag.putBoolean(TAG_CUSTOM_CHARGED, charged);
	}

	protected long getLastHitTime(ItemStack stack) {
		CompoundTag nbt = stack.getOrCreateTag();
		return nbt.contains(TAG_LAST_HIT) ? nbt.getLong(TAG_LAST_HIT) : 0;
	}

	protected void setLastHitTime(ItemStack stack, long time) {
		CompoundTag nbt = stack.getOrCreateTag();
		nbt.putLong(TAG_LAST_HIT, time);
	}

	@Override
	public void updateAttributesFromConfig(CrossbowProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof PaladinProperties pProperties) {
			KNOCKBACK_RESISTANCE = pProperties.KNOCKBACK_RESISTANCE.get().floatValue();
			MAGIC_RESISTANCE = pProperties.MAGIC_RESISTANCE.get().floatValue();
			DAMAGE_RESISTANCE = pProperties.DAMAGE_RESISTANCE.get().floatValue();
			RELOAD_REDUCTION_PER_LEVEL = pProperties.RELOAD_REDUCTION_PER_LEVEL.get().floatValue();
			DAMAGE_RESISTANCE_PER_LEVEL = pProperties.DAMAGE_RESISTANCE_PER_LEVEL.get().floatValue();
			MAX_PERK_LEVEL = pProperties.MAX_PERK_LEVEL.get().byteValue();
			PERK_CLEAR_TIME_WITHOUT_HIT = pProperties.PERK_CLEAR_TIME_WITHOUT_HIT.get();
			buildAttributeModifierMap();
		}
	}
}
