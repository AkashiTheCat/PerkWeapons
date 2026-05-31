package net.akashi.perk_weapons.Util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

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
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag ->
				tag.putFloat(TAG_PERK_LEVEL, (float) Math.min(Math.ceil(getPerkLevel(entity, stack) + 1), getMaxPerkLevel())));
	}

	default void setPerkLevel(ItemStack stack, float level) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putFloat(TAG_PERK_LEVEL, level));
	}

	/**
	 * Used For Model Overrides
	 */
	default float getPerkLevel(LivingEntity entity, ItemStack stack) {
		CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		return tag.contains(TAG_PERK_LEVEL) ? tag.getFloat(TAG_PERK_LEVEL) : 0;
	}

	/**
	 * Used For Model Overrides
	 */
	default boolean isPerkMax(LivingEntity entity, ItemStack stack) {
		return getPerkLevel(entity, stack) >= getMaxPerkLevel();
	}
}
