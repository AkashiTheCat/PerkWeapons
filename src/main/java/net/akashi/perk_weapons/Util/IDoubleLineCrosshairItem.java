package net.akashi.perk_weapons.Util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IDoubleLineCrosshairItem {
	/**
	 * @return [0, 1] 1 indicates the crosshair will be fully choked.
	 */
	float getChokeProgress(LivingEntity shooter, ItemStack stack);
}
