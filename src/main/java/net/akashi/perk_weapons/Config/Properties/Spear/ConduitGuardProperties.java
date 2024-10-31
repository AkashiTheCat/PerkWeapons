package net.akashi.perk_weapons.Config.Properties.Spear;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConduitGuardProperties extends SpearProperties{
	public ForgeConfigSpec.DoubleValue TRACKING_RANGE;
	public ForgeConfigSpec.IntValue TRACKING_ANGLE;
	public ForgeConfigSpec.DoubleValue VELOCITY_MULTIPLIER;
	public ForgeConfigSpec.IntValue RETURN_TIME;

	public ConduitGuardProperties(ForgeConfigSpec.Builder builder, String name,
	                              float defaultMeleeDamage, double defaultAttackSpeed,
	                              float defaultRangedDamage, float defaultVelocity,
	                              float defaultTrackingRange, int defaultTrackingAngle,
	                              double defaultVelocityMultiplier, int defaultReturnTimer) {
		super(builder,name,defaultMeleeDamage,defaultAttackSpeed,defaultRangedDamage,defaultVelocity,false);
		TRACKING_RANGE = builder.comment("Max Tracking Range Of " + name)
				.defineInRange("TrackingRange", defaultTrackingRange, 0.0d, 10.0d);
		TRACKING_ANGLE = builder.comment("Max Allowed Angle Between Thrown Spear And Target")
				.defineInRange("Angle", defaultTrackingAngle, 0, 180);
		VELOCITY_MULTIPLIER = builder.comment("Velocity In Water = Velocity * this")
				.defineInRange("VelocityMultiplier", defaultVelocityMultiplier, 0.0d, 10.0d);
		RETURN_TIME = builder.comment("How Long In Ticks Will The Spear Automatically Return If Didn't Hit Anything")
				.defineInRange("ReturnTime", defaultReturnTimer, 0, 600);
		builder.pop();
	}

	public double getMaxTrackingAngleInDotProductForm(){
		return (double) (90 - TRACKING_ANGLE.get()) /90;
	}
}
