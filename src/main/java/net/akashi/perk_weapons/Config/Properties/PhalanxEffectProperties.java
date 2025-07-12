package net.akashi.perk_weapons.Config.Properties;

import net.minecraftforge.common.ForgeConfigSpec;

public class PhalanxEffectProperties {
	public ForgeConfigSpec.DoubleValue KNOCKBACK_RESISTANCE_PER_LEVEL;
	public ForgeConfigSpec.DoubleValue ATTACK_SPEED_PER_LEVEL;

	public PhalanxEffectProperties(ForgeConfigSpec.Builder builder, String name,
	                               double defaultKnockbackResistancePerLevel,
	                               double defaultAttackSpeedBonusPerLevel) {
		KNOCKBACK_RESISTANCE_PER_LEVEL = builder.comment("Knockback Resistance Provided By " + name + " Effect Per Level")
				.defineInRange("KnockBackResistancePerLevel", defaultKnockbackResistancePerLevel, -100, 10);
		ATTACK_SPEED_PER_LEVEL = builder.comment("Attack Speed Bonus Provided By " + name + " Effect Per Level")
				.defineInRange("AttackSpeedPerLevel", defaultAttackSpeedBonusPerLevel, -16, 16);
	}
}
