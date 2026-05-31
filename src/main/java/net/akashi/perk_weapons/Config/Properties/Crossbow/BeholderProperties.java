package net.akashi.perk_weapons.Config.Properties.Crossbow;

import net.neoforged.neoforge.common.ModConfigSpec;

public class BeholderProperties extends CrossbowProperties {
	public ModConfigSpec.IntValue SLOWNESS_LEVEL;
	public ModConfigSpec.IntValue WEAKNESS_LEVEL;
	public ModConfigSpec.IntValue AFFECT_RANGE;

	public BeholderProperties(ModConfigSpec.Builder builder, String name,
	                          int defaultChargeTime, double defaultDamage,
	                          double defaultVelocity, double defaultInaccuracy,
	                          int defaultAmmoCapacity, int defaultFireInterval,
	                          double defaultQuickChargeMultiplier, double defaultSpeedModifier,
	                          byte defaultSlownessLevel, byte defaultWeaknessLevel,
	                          int defaultAffectRange, boolean onlyAllowMainHand) {
		super(builder, name, defaultChargeTime, defaultDamage, defaultVelocity, defaultInaccuracy, defaultAmmoCapacity,
				defaultFireInterval, defaultQuickChargeMultiplier, defaultSpeedModifier, onlyAllowMainHand, false);
		SLOWNESS_LEVEL = builder.comment("Level Of Slowness Effect On Target When Ability Activates")
				.defineInRange("SlownessLevel", defaultSlownessLevel, 0, 255);
		WEAKNESS_LEVEL = builder.comment("Level Of Weakness Effect On Target When Ability Activates")
				.defineInRange("WeaknessLevel", defaultWeaknessLevel, 0, 255);
		AFFECT_RANGE = builder.comment("Max Affect Distance Of The Ability")
				.defineInRange("AffectRange", defaultAffectRange, 0, 128);
		builder.pop();
	}
}
