package net.akashi.perk_weapons.Config.Properties.Crossbow;

import net.minecraftforge.common.ForgeConfigSpec;

public class IncineratorProperties extends CrossbowProperties {
	public ForgeConfigSpec.IntValue AMMO_CAPACITY;
	public ForgeConfigSpec.IntValue FIRE_ARROW_KNOCKBACK_BONUS;

	public IncineratorProperties(ForgeConfigSpec.Builder builder, String name,
	                             int defaultChargeTime, double defaultDamage,
	                             double defaultVelocity, double defaultInaccuracy,
	                             double defaultSpeedModifier, int defaultCapacity,
	                             int defaultKnockbackBonus, boolean onlyAllowMainHand) {
		super(builder, name, defaultChargeTime, defaultDamage, defaultVelocity, defaultInaccuracy,
				defaultSpeedModifier, onlyAllowMainHand, false);
		AMMO_CAPACITY = builder.comment("Ammo Capacity Of " + name)
				.defineInRange("AmmoCapacity", defaultCapacity, 1, 64);
		FIRE_ARROW_KNOCKBACK_BONUS = builder.comment("Knockback Bonus When Arrow Hit An Entity On Fire")
				.comment("1 Knockback Equals 1 Punch Level")
				.defineInRange("KnockbackBonus", defaultKnockbackBonus, 0, 16);
		builder.pop();
	}
}
