package net.akashi.perk_weapons.Util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IPerkItem {
	String TAG_PERK_LEVEL = "perk_level";

	/**
	 * @return Max Perk Level Of The Item
	 */
	byte getMaxPerkLevel();

	/**
	 * Called When PerkOnHitArrow Hits An Entity
	 */
	default void gainPerkLevel(LivingEntity entity, ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putByte(TAG_PERK_LEVEL, (byte) Math.min((getPerkLevel(entity, stack) + 1), getMaxPerkLevel()));
	}

	default void setPerkLevel(ItemStack stack, byte level) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putByte(TAG_PERK_LEVEL, level);
	}

	/**
	 * Used For Model Overrides
	 */
	default float getPerkLevel(LivingEntity entity, ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		return tag.contains(TAG_PERK_LEVEL) ? tag.getByte(TAG_PERK_LEVEL) : 0;
	}

	/**
	 * Used For Model Overrides
	 */
	default boolean isPerkMax(LivingEntity entity, ItemStack stack) {
		return getPerkLevel(entity, stack) >= getMaxPerkLevel();
	}
}
