package net.akashi.perk_weapons.Config.Properties;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModExplosionProperties {
	public ForgeConfigSpec.DoubleValue INNER_R;
	public ForgeConfigSpec.DoubleValue OUTER_R;
	public ForgeConfigSpec.DoubleValue INNER_DMG;
	public ForgeConfigSpec.DoubleValue OUTER_DMG;
	public ForgeConfigSpec.DoubleValue KNOCKBACK;
	public ForgeConfigSpec.BooleanValue IGNORE_WALL;

	public ModExplosionProperties(ForgeConfigSpec.Builder builder, double innerR, double outerR,
	                              double innerDmg, double outerDmg, double knockback,
	                              boolean ignoreWall) {
		INNER_R = builder.comment("Full Damage Radius Of The Explosion")
				.defineInRange("InnerRadius", innerR, 0, 128);
		OUTER_R = builder.comment("Radius Of The Full Explosion Sphere")
				.defineInRange("OuterRadius", outerR, 0, 128);
		INNER_DMG = builder.comment("Damage Of The Explosion To Entities Within The Inner Radius")
				.defineInRange("InnerDamage", innerDmg, 0, Double.MAX_VALUE);
		OUTER_DMG = builder.comment("Damage Of The Explosion At The Very Edge Of The Explosion Sphere")
				.defineInRange("OuterDamage", outerDmg, 0, Double.MAX_VALUE);
		KNOCKBACK = builder.comment("Knockback Force Of The Explosion")
				.defineInRange("Knockback", knockback, 0, 16);
		IGNORE_WALL = builder.comment("Set True To Allow The Explosion Deals Damage Through Blocks")
				.define("IgnoreWall", ignoreWall);
	}
}
