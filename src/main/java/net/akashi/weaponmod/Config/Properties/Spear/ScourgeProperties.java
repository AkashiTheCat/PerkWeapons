package net.akashi.weaponmod.Config.Properties.Spear;

import net.minecraftforge.common.ForgeConfigSpec;

public class ScourgeProperties extends SpearProperties {
	public ForgeConfigSpec.DoubleValue INIT_AFFECT_CLOUD_RADIUS;
	public ForgeConfigSpec.DoubleValue MAX_AFFECT_CLOUD_RADIUS;
	public ForgeConfigSpec.IntValue AFFECT_CLOUD_DURATION;
	public ForgeConfigSpec.IntValue AFFECT_CLOUD_EFFECT_LEVEL;
	public ForgeConfigSpec.IntValue HIT_WITHER_DURATION;
	public ForgeConfigSpec.IntValue HIT_WITHER_LEVEL;
	public ForgeConfigSpec.IntValue HIT_SLOWNESS_DURATION;
	public ForgeConfigSpec.IntValue HIT_SLOWNESS_LEVEL;
	public ForgeConfigSpec.IntValue ABILITY_COOLDOWN_TIME;
	public ForgeConfigSpec.IntValue PIERCE_LEVEL;

	public ScourgeProperties(ForgeConfigSpec.Builder builder, String name, float defaultMeleeDamage,
	                         double defaultAttackSpeed, float defaultRangedDamage, float defaultVelocity,
	                         int defaultHitWitherDuration, int defaultHitWitherLevel,
	                         int defaultHitSlownessDuration, int defaultHitSlownessLevel,
	                         int defaultAbilityAffectCloudDuration, int defaultAbilityEffectLevel,
	                         double defaultInitAbilityAffectCloudRadius, double defaultMaxAbilityAffectCloudRadius,
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
		INIT_AFFECT_CLOUD_RADIUS = builder.comment("Radius Of Spawned Affect Cloud On Crouch+Use")
				.defineInRange("InitAffectCloudRadius", defaultInitAbilityAffectCloudRadius, 0, 10);
		MAX_AFFECT_CLOUD_RADIUS = builder.comment("Max Radius Of Affect Cloud To Grow Up To")
				.defineInRange("MaxAffectCloudRadius", defaultMaxAbilityAffectCloudRadius, 0, 10);
		AFFECT_CLOUD_EFFECT_LEVEL = builder.comment("Level Of Wither Effect Of The Affect Cloud")
				.defineInRange("AffectCloudLevel", defaultAbilityEffectLevel, 0, 255);
		AFFECT_CLOUD_DURATION = builder.comment("Affect Cloud Duration(In Ticks)")
				.defineInRange("AffectCloudDuration", defaultAbilityAffectCloudDuration, 0, 3600);
		ABILITY_COOLDOWN_TIME = builder.comment("How Long In Ticks Will The Crouch+Use Ability's CoolDown Be")
				.defineInRange("AbilityCD", defaultAbilityCoolDownTime, 0, 6000);
		PIERCE_LEVEL = builder.comment("How Many Entities Can The Spear Pierce")
				.defineInRange("PierceLevel", defaultPiercingLevel, 0, 255);
		builder.pop();
	}
}
