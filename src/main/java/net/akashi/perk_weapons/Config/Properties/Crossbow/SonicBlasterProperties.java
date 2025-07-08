package net.akashi.perk_weapons.Config.Properties.Crossbow;

import net.minecraftforge.common.ForgeConfigSpec;

public class SonicBlasterProperties extends CrossbowProperties {
	public ForgeConfigSpec.DoubleValue KNOCKBACK_RESISTANCE;
	public ForgeConfigSpec.IntValue MAX_RANGE;
	public ForgeConfigSpec.DoubleValue DAMAGE_RADIUS;
	public ForgeConfigSpec.IntValue PIERCE_LEVEL;
	public ForgeConfigSpec.BooleanValue ENABLE_KNOCKBACK;
	public ForgeConfigSpec.DoubleValue KNOCKBACK_FORCE;

	public SonicBlasterProperties(ForgeConfigSpec.Builder builder, String name,
	                              int defaultChargeTime, double defaultDamage,
	                              double defaultVelocity, double defaultInaccuracy,
	                              int defaultAmmoCapacity, int defaultFireInterval,
	                              double defaultSpeedModifier, double defaultKnockbackResistance,
	                              int defaultMaxRange, double defaultDamageRadius,
	                              int defaultPierceLevel, boolean defaultEnableKnockback,
	                              double defaultKnockbackForce, boolean onlyAllowMainHand) {
		super(builder, name, defaultChargeTime, defaultDamage, defaultVelocity, defaultInaccuracy,
				defaultAmmoCapacity, defaultFireInterval, defaultSpeedModifier, onlyAllowMainHand, false);
		KNOCKBACK_RESISTANCE = builder.comment("Knockback Resistance Provided By " + name + " When In MainHand")
				.comment("1.0 means You Wont Take Any Knockback At All")
				.defineInRange("KnockbackResistance", defaultKnockbackResistance, -10.0, 1.0);
		MAX_RANGE = builder.comment("Max Attack Range Of The Sonic Boom")
				.defineInRange("MaxRange", defaultMaxRange, 0, 64);
		DAMAGE_RADIUS = builder.comment("Damage Radius Of The Sonic Boom")
				.defineInRange("DamageRadius", defaultDamageRadius, 0.0, 16.0);
		PIERCE_LEVEL = builder.comment("Number Of Entities That The Sonic Boom Can Pierce Through.")
				.comment("Set -1 To Allow Infinite Penetration")
				.defineInRange("PierceLevel", defaultPierceLevel, -1, Integer.MAX_VALUE);
		ENABLE_KNOCKBACK = builder.comment("Set True To Enable Knockback On Sonic Boom Hit")
				.define("EnableKnockback", defaultEnableKnockback);
		KNOCKBACK_FORCE = builder.comment("Knockback Force If Knockback Is Enabled")
				.defineInRange("KnockbackForce", defaultKnockbackForce, 0.0, 10.0);
		builder.pop();
	}

	@Override
	public void topComment(ForgeConfigSpec.Builder builder, String name) {
		builder.comment("Velocity And Inaccuracy Has No Effect On " + name + " Since It Doesn't Fire Projectiles");
	}
}
