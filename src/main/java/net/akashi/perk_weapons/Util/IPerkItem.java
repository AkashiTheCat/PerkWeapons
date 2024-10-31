package net.akashi.perk_weapons.Util;

import net.minecraftforge.registries.IForgeRegistry;

public interface IPerkItem {
	/**
	 * 1 Full Indicator Bar's Length is 5
	 */
	int getIndicatorLength();

	/**
	 * Called When PerkOnHitArrow Hits An Entity
	 */
	void gainPerkLevel();
	/**
	 * Used For Model Overrides
	 */
	byte getPerkLevel();

	/**
	 * Used For Model Overrides
	 */
	boolean isPerkMax();
}
