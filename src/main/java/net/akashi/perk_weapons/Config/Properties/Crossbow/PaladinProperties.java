package net.akashi.perk_weapons.Config.Properties.Crossbow;

import net.minecraftforge.common.ForgeConfigSpec;

public class PaladinProperties extends CrossbowProperties {
	public ForgeConfigSpec.DoubleValue KNOCKBACK_RESISTANCE;
	public ForgeConfigSpec.DoubleValue MAGIC_RESISTANCE;
	public ForgeConfigSpec.DoubleValue DAMAGE_RESISTANCE;
	public ForgeConfigSpec.IntValue PIERCE_LEVEL_BONUS;
	public ForgeConfigSpec.IntValue MAX_PERK_LEVEL;
	public ForgeConfigSpec.DoubleValue RELOAD_REDUCTION_PER_LEVEL;
	public ForgeConfigSpec.DoubleValue DAMAGE_RESISTANCE_PER_LEVEL;
	public ForgeConfigSpec.IntValue PERK_CLEAR_TIME_WITHOUT_HIT;

	public PaladinProperties(ForgeConfigSpec.Builder builder, String name,
	                         int defaultChargeTime, double defaultDamage,
	                         double defaultVelocity, double defaultInaccuracy,
	                         int defaultAmmoCapacity, int defaultFireInterval,
	                         double defaultQuickChargeMultiplier, double defaultKnockbackResistance,
	                         double defaultMagicResistance, double defaultDamageResistance,
	                         int defaultPierceLevelBonus, int defaultMaxPerkLevel,
	                         double defaultReloadReductionPerLevel, double defaultDamageResistancePerLevel,
	                         int defaultPerkClearTimeWithoutHit, double defaultSpeedModifier,
	                         boolean onlyAllowMainHand) {
		super(builder, name, defaultChargeTime, defaultDamage, defaultVelocity, defaultInaccuracy, defaultAmmoCapacity,
				defaultFireInterval, defaultQuickChargeMultiplier, defaultSpeedModifier, onlyAllowMainHand, false);
		KNOCKBACK_RESISTANCE = builder.comment("Knockback Resistance Value For " + name + " When In Hand")
				.comment("1.0 Means You Won't Take Any Knockback At All")
				.defineInRange("KnockbackResistance", defaultKnockbackResistance, -10.0, 1.0);
		MAGIC_RESISTANCE = builder.comment("Magic Resistance Value For " + name + " When In Hand")
				.comment("Percentage Value")
				.defineInRange("MagicResistance", defaultMagicResistance, -1000.0, 100.0);
		DAMAGE_RESISTANCE = builder.comment("Damage Resistance Value For " + name + " When In Hand")
				.comment("Percentage Value")
				.defineInRange("DamageResistance", defaultDamageResistance, -1000.0, 100.0);
		PIERCE_LEVEL_BONUS = builder.comment("Bonus Level For Piercing Enchantment")
				.defineInRange("PierceLevelBonus", defaultPierceLevelBonus, 0, 255);
		MAX_PERK_LEVEL = builder.comment("Max Perk Level Of " + name)
				.defineInRange("MaxPerkLevel", defaultMaxPerkLevel, 0, 255);
		RELOAD_REDUCTION_PER_LEVEL = builder.comment("Reload Time Reduction Ratio Per Perk Level")
				.defineInRange("ReloadReductionPerLevel", defaultReloadReductionPerLevel, 0.0, 1.0);
		DAMAGE_RESISTANCE_PER_LEVEL = builder.comment("Addition Damage Resistance Per Perk Level")
				.defineInRange("DamageResistancePerLevel", defaultDamageResistancePerLevel, -1000.0, 100.0);
		PERK_CLEAR_TIME_WITHOUT_HIT = builder.comment("After How Many Ticks Without Hitting Anything Will The Perk Level Be Cleared")
				.defineInRange("PerkClearTimeWithoutHit", defaultPerkClearTimeWithoutHit, 0, Integer.MAX_VALUE);
		builder.pop();
	}
}
