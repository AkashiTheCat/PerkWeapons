package net.akashi.perk_weapons.Config.Properties.Spear;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CenturionProperties extends SpearProperties {
	public ModConfigSpec.DoubleValue EFFECT_APPLY_RANGE;
	public ModConfigSpec.IntValue PHALANX_LEVEL;
	public ModConfigSpec.IntValue PIERCE_LEVEL;

	public CenturionProperties(ModConfigSpec.Builder builder, String name,
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
