package net.akashi.perk_weapons.Config.Properties.Crossbow;

import net.minecraftforge.common.ForgeConfigSpec;

public class OppressorProperties extends CrossbowProperties {
	public ForgeConfigSpec.IntValue SLOWNESS_LEVEL;
	public ForgeConfigSpec.IntValue AFFECT_RANGE;

	public OppressorProperties(ForgeConfigSpec.Builder builder, String name,
	                           int defaultChargeTime, double defaultDamage,
	                           double defaultVelocity, double defaultInaccuracy,
	                           double defaultSpeedModifier, byte defaultSlownessLevel,
	                           int defaultAffectRange, boolean onlyAllowMainHand) {
		super(builder, name, defaultChargeTime, defaultDamage, defaultVelocity,
				defaultInaccuracy, defaultSpeedModifier, onlyAllowMainHand, false);
		SLOWNESS_LEVEL = builder.comment("Level Of Slowness Effect On Target When Crouch+Use Ability Activates")
				.defineInRange("SlownessLevel", defaultSlownessLevel, 0, 255);
		AFFECT_RANGE = builder.comment("Max Affect Distance Of Crouch+Use Ability")
				.defineInRange("AffectRange", defaultAffectRange, 0, 128);
	}
}
