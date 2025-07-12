package net.akashi.perk_weapons.Config.Properties.Spear;

import net.minecraftforge.common.ForgeConfigSpec;

public class NetherGuideProperties extends SpearProperties {
	public ForgeConfigSpec.DoubleValue WARPED_MELEE_DAMAGE_BONUS_RATIO;
	public ForgeConfigSpec.DoubleValue WARPED_THROW_DAMAGE_BONUS_RATIO;
	public ForgeConfigSpec.DoubleValue WARPED_DAMAGE_RESISTANCE;
	public ForgeConfigSpec.DoubleValue WARPED_MOVEMENT_SPEED_BONUS_RATIO;

	public ForgeConfigSpec.IntValue CRIMSON_WEAKNESS_LEVEL_ON_TARGET_WHEN_HIT;
	public ForgeConfigSpec.IntValue CRIMSON_WEAKNESS_DURATION_ON_TARGET_WHEN_HIT;
	public ForgeConfigSpec.IntValue CRIMSON_REGENERATION_LEVEL_ON_SELF_WHEN_HIT;
	public ForgeConfigSpec.IntValue CRIMSON_REGENERATION_DURATION_ON_SELF_WHEN_HIT;

	public ForgeConfigSpec.IntValue MODE_SWITCH_COOLDOWN;

	public NetherGuideProperties(ForgeConfigSpec.Builder builder, String name,
	                             float defaultMeleeDamage, double defaultAttackSpeed,
	                             float defaultRangedDamage, float defaultVelocity,
	                             double defaultMeleeBonusWarped, double defaultThrowBonusWarped,
	                             double defaultDamageResistanceWarped, double defaultMovementBonusWarped,
	                             int defaultWeaknessLevelCrimson, int defaultWeaknessDurationCrimson,
	                             int defaultRegenLevelCrimson, int defaultRegenDurationCrimson,
	                             int defaultModeSwitchCoolDown) {
		super(builder, name, defaultMeleeDamage, defaultAttackSpeed, defaultRangedDamage, defaultVelocity,
				false);

		WARPED_MELEE_DAMAGE_BONUS_RATIO = builder.comment("Melee Damage Bonus Ratio Of Warped Mode")
				.defineInRange("WarpedMeleeBonus", defaultMeleeBonusWarped, -1, 10);
		WARPED_THROW_DAMAGE_BONUS_RATIO = builder.comment("Throw Damage Bonus Ratio Of Warped Mode")
				.defineInRange("WarpedThrowBonus", defaultThrowBonusWarped, -1, 10);
		WARPED_DAMAGE_RESISTANCE = builder.comment("Damage Resistance Provided By Warped Mode(%)")
				.defineInRange("WarpedDamageResistance", defaultDamageResistanceWarped, -1000, 100);
		WARPED_MOVEMENT_SPEED_BONUS_RATIO = builder.comment("Movement Speed Bonus Ratio Of Warped Mode")
				.defineInRange("WarpedMovementBonus", defaultMovementBonusWarped, -1, 10);

		CRIMSON_WEAKNESS_LEVEL_ON_TARGET_WHEN_HIT = builder.comment("Level Of Weakness Effect Applied On Target In Crimson Mode")
				.defineInRange("CrimsonWeaknessLevel", defaultWeaknessLevelCrimson, 0, 255);
		CRIMSON_WEAKNESS_DURATION_ON_TARGET_WHEN_HIT = builder.comment("Duration(Ticks) Of Weakness Effect Applied On Target In Crimson Mode")
				.defineInRange("CrimsonWeaknessDuration", defaultWeaknessDurationCrimson, 0, Integer.MAX_VALUE);
		CRIMSON_REGENERATION_LEVEL_ON_SELF_WHEN_HIT = builder.comment("Level Of Regeneration Effect Applied On Owner On Hit In Crimson Mode")
				.defineInRange("CrimsonRegenLevel", defaultRegenLevelCrimson, 0, 255);
		CRIMSON_REGENERATION_DURATION_ON_SELF_WHEN_HIT = builder.comment("Duration(Ticks) Of Regeneration Effect Applied On Owner On Hit In Crimson Mode")
				.defineInRange("CrimsonRegenDuration", defaultRegenDurationCrimson, 0, Integer.MAX_VALUE);

		MODE_SWITCH_COOLDOWN = builder.comment("Cooldown In Ticks Of Mode Switching")
				.defineInRange("ModeSwitchCoolDown", defaultModeSwitchCoolDown, 0, Integer.MAX_VALUE);

		builder.pop();
	}
}
