package net.akashi.weaponmod.Config;

import net.akashi.weaponmod.WeaponMod;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModCommonConfigs {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static SpearProperties IRON_SPEAR_PROPERTIES;
	public static SpearProperties GOLDEN_SPEAR_PROPERTIES;
	public static SpearProperties DIAMOND_SPEAR_PROPERTIES;
	public static SpearProperties NETHERITE_SPEAR_PROPERTIES;
	public static SpearProperties MEGALODON_PROPERTIES;

	static {
		IRON_SPEAR_PROPERTIES = new SpearProperties(BUILDER, "Iron Spear",
				8, 1.1f,
				7, 2.5F,
				256);
		GOLDEN_SPEAR_PROPERTIES = new SpearProperties(BUILDER, "Golden Spear",
				5, 1.6f,
				6, 2.5F,
				32);
		DIAMOND_SPEAR_PROPERTIES = new SpearProperties(BUILDER, "Diamond Spear",
				9, 1.1f,
				8, 2.5F,
				640);
		NETHERITE_SPEAR_PROPERTIES = new SpearProperties(BUILDER, "Netherite Spear",
				10, 1.1f,
				9, 2.5F,
				1280);
		MEGALODON_PROPERTIES = new SpearProperties(BUILDER, "Megalodon",
				9, 1.2f,
				6, 2.5F,
				1280);
		SPEC = BUILDER.build();
	}

}
