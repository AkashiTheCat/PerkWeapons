package net.akashi.perk_weapons.Util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;

public interface IPerkItem {
	/**
	 *
	 * @return Max Perk Level Of The Item
	 */
	byte getMaxPerkLevel();

	/**
	 * Called When PerkOnHitArrow Hits An Entity
	 */
	void gainPerkLevel(Player player, ItemStack stack);
	/**
	 * Used For Model Overrides
	 */
	float getPerkLevel(Player player,ItemStack stack);

	/**
	 * Used For Model Overrides
	 */
	boolean isPerkMax(Player player,ItemStack stack);
}
