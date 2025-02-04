package net.akashi.perk_weapons.Util;

import net.minecraft.world.entity.LivingEntity;
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
	void gainPerkLevel(LivingEntity entity, ItemStack stack);
	/**
	 * Used For Model Overrides
	 */
	float getPerkLevel(LivingEntity entity, ItemStack stack);

	/**
	 * Used For Model Overrides
	 */
	boolean isPerkMax(LivingEntity entity,ItemStack stack);
}
