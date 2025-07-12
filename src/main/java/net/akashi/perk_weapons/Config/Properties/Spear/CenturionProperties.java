package net.akashi.perk_weapons.Config.Properties.Spear;

import net.minecraftforge.common.ForgeConfigSpec;

public class CenturionProperties extends SpearProperties {
	public ForgeConfigSpec.DoubleValue EFFECT_APPLY_RANGE;
	public ForgeConfigSpec.IntValue PHALANX_LEVEL;
	public ForgeConfigSpec.IntValue PIERCE_LEVEL;

	public CenturionProperties(ForgeConfigSpec.Builder builder, String name,
	                           float defaultMeleeDamage, double defaultAttackSpeed,
	                           float defaultRangedDamage, float defaultVelocity,
	                           int defaultMaxChargeTicks, double defaultEffectApplyRange,
	                           int defaultPhalanxLevel, int defaultPierceLevel) {
		super(builder, name, defaultMeleeDamage, defaultAttackSpeed, defaultRangedDamage, defaultVelocity,
				defaultMaxChargeTicks, false);
		EFFECT_APPLY_RANGE = builder.comment("Effect Apply Range Of " + name)
				.comment("Players And Tamed Animals Receives Phalanx Effect When You're Holding " + name + " In Hand")
				.defineInRange("EffectApplyRange", defaultEffectApplyRange, 0, 64);
		PHALANX_LEVEL = builder.comment("Phalanx Effect Level Received By Entities Within Effect Range")
				.defineInRange("PhalanxLevel", defaultPhalanxLevel, 0, 255);
		PIERCE_LEVEL = builder.comment("Pierce Level Of " + name)
				.defineInRange("PierceLevel", defaultPierceLevel, 0, 255);
		builder.pop();
	}
}
