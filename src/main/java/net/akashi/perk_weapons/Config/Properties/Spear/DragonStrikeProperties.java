package net.akashi.perk_weapons.Config.Properties.Spear;

import net.minecraftforge.common.ForgeConfigSpec;

public class DragonStrikeProperties extends SpearProperties {
	public ForgeConfigSpec.DoubleValue INIT_AFFECT_RADIUS;
	public ForgeConfigSpec.DoubleValue MAX_AFFECT_RADIUS;
	public ForgeConfigSpec.IntValue AFFECT_DURATION;
	public ForgeConfigSpec.IntValue EFFECT_DAMAGE;
	public ForgeConfigSpec.DoubleValue MAGIC_RESISTANCE;
	public ForgeConfigSpec.IntValue RETURN_TIME;
	public ForgeConfigSpec.IntValue ABILITY_COOLDOWN_TIME;

	public DragonStrikeProperties(ForgeConfigSpec.Builder builder, String name,
	                              float defaultMeleeDamage, double defaultAttackSpeed,
	                              float defaultRangedDamage, float defaultVelocity,
	                              int defaultMaxChargeTime, double defaultInitAffectCloudRadius,
	                              double defaultMaxAffectCloudRadius, int defaultAffectCloudDuration,
	                              int defaultAffectCloudDamage, double defaultMagicResistance,
	                              int defaultReturnTime, int defaultAbilityCoolDownTime) {
		super(builder, name, defaultMeleeDamage, defaultAttackSpeed, defaultRangedDamage, defaultVelocity,
				defaultMaxChargeTime, false);
		INIT_AFFECT_RADIUS = builder.comment("Radius Of Spawned Affect Cloud On Hit")
				.defineInRange("InitAffectCloudRadius", defaultInitAffectCloudRadius, 0, 10);
		MAX_AFFECT_RADIUS = builder.comment("Max Radius Of Affect Cloud To Grow Up To")
				.defineInRange("MaxAffectCloudRadius", defaultMaxAffectCloudRadius, 0, 10);
		AFFECT_DURATION = builder.comment("Affect Cloud Duration(In Ticks)")
				.defineInRange("AffectCloudDuration", defaultAffectCloudDuration, 0, 3600);
		MAGIC_RESISTANCE = builder.comment("Percentage Of Magic Resistance Given When Held In Hand")
				.defineInRange("MagicResistance", defaultMagicResistance, 0, 100);
		EFFECT_DAMAGE = builder.comment("Damage Per Second Of The Affect Cloud Effect")
				.defineInRange("EffectDamage", defaultAffectCloudDamage, 1, Integer.MAX_VALUE);
		RETURN_TIME = builder.comment("How Long In Ticks Will The Spear Automatically Return If Didn't Hit Anything")
				.defineInRange("ReturnTime", defaultReturnTime, 0, 600);
		ABILITY_COOLDOWN_TIME = builder.comment("Cooldown Of The Crouch+Use Ability In Ticks")
				.defineInRange("AbilityCD", defaultAbilityCoolDownTime, 0, 6000);
		builder.pop();
	}
}
