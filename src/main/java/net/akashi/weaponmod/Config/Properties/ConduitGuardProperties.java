package net.akashi.weaponmod.Config.Properties;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConduitGuardProperties extends SpearProperties{
	public ForgeConfigSpec.DoubleValue TRACKING_RANGE;
	public ForgeConfigSpec.IntValue TRACKING_ANGLE;

	public ConduitGuardProperties(ForgeConfigSpec.Builder builder, String name,
	                              float defaultMeleeDamage, double defaultAttackSpeed,
	                              float defaultRangedDamage, float defaultVelocity,
	                              float defaultTrackingRange, int defaultTrackingAngle) {
		super(builder,name,defaultMeleeDamage,defaultAttackSpeed,defaultRangedDamage,defaultVelocity,false);
		TRACKING_RANGE = builder.comment("Max Tracking Range Of " + name)
				.defineInRange("TrackingRange", defaultTrackingRange, 0.0d, 10.0d);
		TRACKING_ANGLE = builder.comment("Max Allowed Angle Between Thrown Spear And Target")
				.defineInRange("Angle", defaultTrackingAngle, 0, 180);
		builder.pop();
	}

	public double getMaxTrackingAngleInDotProductForm(){
		return (double) (90 - TRACKING_ANGLE.get()) /90;
	}
}
