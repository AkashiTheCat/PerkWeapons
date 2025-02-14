package net.akashi.perk_weapons.Util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ICoolDownItem {
	/**
	 * @return [0, 1] 1 indicates fully cooled down.
	 */
	float getCoolDownProgress(LivingEntity owner, ItemStack stack);
}
