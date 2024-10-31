package net.akashi.perk_weapons.Config.Properties.Spear;

import net.minecraftforge.common.ForgeConfigSpec;

public class ScourgeProperties extends SpearProperties {
	public ForgeConfigSpec.IntValue HIT_WITHER_DURATION;
	public ForgeConfigSpec.IntValue HIT_WITHER_LEVEL;
	public ForgeConfigSpec.IntValue HIT_SLOWNESS_DURATION;
	public ForgeConfigSpec.IntValue HIT_SLOWNESS_LEVEL;
	public ForgeConfigSpec.IntValue ABILITY_BUFF_DURATION;
	public ForgeConfigSpec.DoubleValue ABILITY_ATTACK_SPEED_BONUS;
	public ForgeConfigSpec.IntValue ABILITY_SHOTS_INTERVAL;
	public ForgeConfigSpec.IntValue ABILITY_SHOTS_COUNT;
	public ForgeConfigSpec.IntValue ABILITY_COOLDOWN;
	public ForgeConfigSpec.IntValue PIERCE_LEVEL;

	public ScourgeProperties(ForgeConfigSpec.Builder builder, String name, float defaultMeleeDamage,
	                         double defaultAttackSpeed, float defaultRangedDamage, float defaultVelocity,
	                         int defaultHitWitherDuration, int defaultHitWitherLevel,
	                         int defaultHitSlownessDuration, int defaultHitSlownessLevel,
	                         int defaultAbilityBuffDuration, double defaultAbilityAttackSpeedBonus,
							 int defaultAbilityShotsInterval, int defaultAbilityShotsCount,
	                         int defaultAbilityCoolDownTime, int defaultPiercingLevel) {
		super(builder, name, defaultMeleeDamage, defaultAttackSpeed, defaultRangedDamage, defaultVelocity, false);
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
		ABILITY_COOLDOWN = builder.comment("How Long In Ticks Will The Crouch+Use Ability's CoolDown Be")
				.defineInRange("AbilityCD", defaultAbilityCoolDownTime, 0, 6000);
		PIERCE_LEVEL = builder.comment("Pierce Level Of " + name)
				.defineInRange("PierceLevel", defaultPiercingLevel, 0, 255);
		builder.pop();
	}
}
