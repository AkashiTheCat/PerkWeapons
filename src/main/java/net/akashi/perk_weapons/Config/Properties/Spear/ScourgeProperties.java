package net.akashi.perk_weapons.Config.Properties.Spear;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ScourgeProperties extends SpearProperties {
	public ModConfigSpec.IntValue HIT_WITHER_DURATION;
	public ModConfigSpec.IntValue HIT_WITHER_LEVEL;
	public ModConfigSpec.IntValue HIT_SLOWNESS_DURATION;
	public ModConfigSpec.IntValue HIT_SLOWNESS_LEVEL;
	public ModConfigSpec.IntValue ABILITY_BUFF_DURATION;
	public ModConfigSpec.DoubleValue ABILITY_ATTACK_SPEED_BONUS;
	public ModConfigSpec.IntValue ABILITY_SHOTS_INTERVAL;
	public ModConfigSpec.IntValue ABILITY_SHOTS_COUNT;
	public ModConfigSpec.IntValue ABILITY_COOLDOWN;
	public ModConfigSpec.IntValue PIERCE_LEVEL;

	public ScourgeProperties(ModConfigSpec.Builder builder, String name,
	                         float defaultMeleeDamage, double defaultAttackSpeed,
	                         float defaultRangedDamage, float defaultVelocity,
	                         int defaultMaxChargeTime, int defaultHitWitherDuration,
	                         int defaultHitWitherLevel, int defaultHitSlownessDuration,
	                         int defaultHitSlownessLevel, int defaultAbilityBuffDuration,
	                         double defaultAbilityAttackSpeedBonus, int defaultAbilityShotsInterval,
	                         int defaultAbilityShotsCount, int defaultAbilityCoolDownTime,
	                         int defaultPiercingLevel) {
		super(builder, name, defaultMeleeDamage, defaultAttackSpeed, defaultRangedDamage, defaultVelocity,
				defaultMaxChargeTime, false);
		HIT_WITHER_LEVEL = builder.comment("Level Of Wither Effect On Hit")
				.defineInRange("HitWitherLevel", defaultHitWitherLevel, 0, 255);
		HIT_WITHER_DURATION = builder.comment("Duration Of Wither Effect On Hit(In Ticks)")
				.defineInRange("HitWitherDuration", defaultHitWitherDuration, 0, Integer.MAX_VALUE);
		HIT_SLOWNESS_LEVEL = builder.comment("Level Of Slowness Effect On Hit")
				.defineInRange("HitSlownessLevel", defaultHitSlownessLevel, 0, 255);
		HIT_SLOWNESS_DURATION = builder.comment("Duration Of Slowness Effect On Hit(In Ticks)")
				.defineInRange("HitSlownessDuration", defaultHitSlownessDuration, 0, Integer.MAX_VALUE);
		ABILITY_BUFF_DURATION = builder.comment("The Duration(In Ticks) Of The Attack Speed Bonus Of The Crouch+Use Ability")
				.defineInRange("AbilityBuffDuration", defaultAbilityBuffDuration, 0, Integer.MAX_VALUE);
		ABILITY_ATTACK_SPEED_BONUS = builder.comment("Attack Speed Bonus Of The Crouch+Use Ability")
				.defineInRange("AttackSpeedBonus", defaultAbilityAttackSpeedBonus, 0, 10);
		ABILITY_SHOTS_INTERVAL = builder.comment("The Interval(In Ticks) Between Shots From The Crouch+Use Ability")
				.defineInRange("ShotsInterval", defaultAbilityShotsInterval, 0, Integer.MAX_VALUE);
		ABILITY_SHOTS_COUNT = builder.comment("The Number Of Shots From The Crouch+Use Ability")
				.defineInRange("ShotsCount", defaultAbilityShotsCount, 0, 255);
		ABILITY_COOLDOWN = builder.comment("Cooldown Of The Crouch+Use Ability In Ticks")
				.defineInRange("AbilityCD", defaultAbilityCoolDownTime, 0, 6000);
		PIERCE_LEVEL = builder.comment("Pierce Level Of " + name)
				.defineInRange("PierceLevel", defaultPiercingLevel, 0, 127);
		builder.pop();
	}
}
