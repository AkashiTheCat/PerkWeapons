package net.akashi.perk_weapons.Config.Properties.Spear;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConduitGuardProperties extends SpearProperties {
	public ForgeConfigSpec.DoubleValue HOMING_RANGE;
	public ForgeConfigSpec.DoubleValue MAX_HOMING_ANGLE;
	public ForgeConfigSpec.DoubleValue MAX_TURN_RATE;
	public ForgeConfigSpec.DoubleValue HOMING_ACCELERATION;
	public ForgeConfigSpec.DoubleValue VELOCITY_MULTIPLIER;
	public ForgeConfigSpec.IntValue RETURN_TIME;

	public ConduitGuardProperties(ForgeConfigSpec.Builder builder, String name,
	                              float defaultMeleeDamage, double defaultAttackSpeed,
	                              float defaultRangedDamage, float defaultVelocity,
	                              int defaultMaxChargeTime, double defaultHomingRange,
	                              double defaultMaxHomingAngle, double defaultMaxTurnRate,
	                              double defaultHomingAcceleration, double defaultVelocityMultiplier,
	                              int defaultReturnTimer) {
		super(builder, name, defaultMeleeDamage, defaultAttackSpeed, defaultRangedDamage, defaultVelocity,
				defaultMaxChargeTime, false);
		HOMING_RANGE = builder.comment("Homing Range Of The Projectile")
				.defineInRange("HomingRange", defaultHomingRange, 0, 32);
		MAX_HOMING_ANGLE = builder.comment("Maximum Homing Angle (In Degrees) Of The Projectile")
				.comment("Defines The Maximum Angle Between The Projectile's Current Trajectory And The Direction Toward The Target")
				.comment("This Forms A Targeting Cone Together With The Homing Range")
				.defineInRange("MaxHomingAngle", defaultMaxHomingAngle, 0, 180);
		MAX_TURN_RATE = builder.comment("Maximum Turn Rate Of The Projectile (Degrees/Tick)")
				.defineInRange("MaxTurnRate", defaultMaxTurnRate, 0, 180);
		HOMING_ACCELERATION = builder.comment("Acceleration When The Projectile Finds A Target (Meters/Tick^2)")
				.defineInRange("HomingAcceleration", defaultHomingAcceleration, 0, 16);
		VELOCITY_MULTIPLIER = builder.comment("Velocity In Water = Velocity * this")
				.defineInRange("VelocityMultiplier", defaultVelocityMultiplier, 0.0d, 10.0d);
		RETURN_TIME = builder.comment("How Long In Ticks Will The Spear Automatically Return If Didn't Hit Anything")
				.defineInRange("ReturnTime", defaultReturnTimer, 0, 600);
		builder.pop();
	}
}
