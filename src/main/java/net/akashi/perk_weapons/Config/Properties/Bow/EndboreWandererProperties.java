package net.akashi.perk_weapons.Config.Properties.Bow;

import net.minecraftforge.common.ForgeConfigSpec;

public class EndboreWandererProperties extends BowProperties {
	public ForgeConfigSpec.IntValue MAX_PERK_LEVEL;
	public ForgeConfigSpec.IntValue CROUCH_USE_COOLDOWN;
	public ForgeConfigSpec.DoubleValue DAMAGE_BONUS_LEVITATION;

	public ForgeConfigSpec.IntValue PERK_PROJECTILE_LEVITATION_TICKS_ON_HIT;
	public ForgeConfigSpec.DoubleValue PERK_PROJECTILE_HOMING_RANGE;
	public ForgeConfigSpec.DoubleValue PERK_PROJECTILE_MAX_HOMING_ANGLE;
	public ForgeConfigSpec.DoubleValue PERK_PROJECTILE_MAX_TURN_RATE;
	public ForgeConfigSpec.DoubleValue PERK_PROJECTILE_HOMING_ACCELERATION;

	public EndboreWandererProperties(ForgeConfigSpec.Builder builder, String name,
	                                 int defaultDrawTime, double defaultDamage,
	                                 double defaultVelocity, double defaultInaccuracy,
	                                 int defaultMaxPerkLevel, int defaultCrouchUseCoolDown,
	                                 double defaultDamageBonusLevitation, int defaultLevitationTicksOnPerkHit,
	                                 double defaultPerkHomingRange, double defaultMaxPerkHomingAngle,
	                                 double defaultMaxPerkHomingTurnRate, double defaultPerkHomingAcceleration,
	                                 double defaultSpeedModifier, double defaultZoomFactor,
	                                 boolean onlyAllowMainHand) {
		super(builder, name, defaultDrawTime, defaultDamage, defaultVelocity, defaultInaccuracy,
				defaultSpeedModifier, defaultZoomFactor, onlyAllowMainHand, false);
		MAX_PERK_LEVEL = builder.comment("Max Perk Level Of " + name)
				.defineInRange("MaxPerkLevel", defaultMaxPerkLevel, 0, 255);
		CROUCH_USE_COOLDOWN = builder.comment("Cooldown In Ticks Applied On " + name + " After Crouch+Use Ability Activation")
				.defineInRange("CrouchUseCoolDown", defaultCrouchUseCoolDown, 0, Integer.MAX_VALUE);
		DAMAGE_BONUS_LEVITATION = builder.comment("Damage Bonus Ratio When Hit A Target With Levitation Effect")
				.defineInRange("LevitationBonus", defaultDamageBonusLevitation, 0, Double.MAX_VALUE);
		builder.push("Perk Projectile");
		builder.comment("This Part Is For The Special Projectile Fired When " + name + " Reaches Max Perk Level");
		PERK_PROJECTILE_LEVITATION_TICKS_ON_HIT = builder.comment("Duration Of Levitation Effect Applied On Target In Ticks")
				.defineInRange("LevitationDuration", defaultLevitationTicksOnPerkHit, 0, Integer.MAX_VALUE);
		PERK_PROJECTILE_HOMING_RANGE = builder.comment("Homing Range Of The Projectile")
				.defineInRange("HomingRange", defaultPerkHomingRange, 0, 32);
		PERK_PROJECTILE_MAX_HOMING_ANGLE = builder.comment("Maximum Homing Angle (In Degrees) Of The Projectile")
				.comment("Defines The Maximum Angle Between The Projectile's Current Trajectory And The Direction Toward The Target")
				.comment("This Forms A Targeting Cone Together With The Homing Range")
				.defineInRange("MaxHomingAngle", defaultMaxPerkHomingAngle, 0, 180);
		PERK_PROJECTILE_MAX_TURN_RATE = builder.comment("Maximum Turn Rate Of The Projectile (Degrees/Tick)")
				.defineInRange("MaxTurnRate", defaultMaxPerkHomingTurnRate, 0, 180);
		PERK_PROJECTILE_HOMING_ACCELERATION = builder.comment("Acceleration When The Projectile Finds A Target (Meters/Tick^2)")
				.defineInRange("HomingAcceleration", defaultPerkHomingAcceleration, 0, 16);
		builder.pop();
		builder.pop();
	}
}
