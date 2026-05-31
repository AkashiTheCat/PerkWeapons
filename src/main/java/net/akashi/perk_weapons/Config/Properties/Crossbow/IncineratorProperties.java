package net.akashi.perk_weapons.Config.Properties.Crossbow;

import net.neoforged.neoforge.common.ModConfigSpec;

public class IncineratorProperties extends CrossbowProperties {
	public ModConfigSpec.IntValue BLAZE_AMMO_CAPACITY;
	public ModConfigSpec.IntValue BLAZE_RELOAD_INCREMENT;
	public ModConfigSpec.IntValue FIRE_ARROW_KNOCKBACK_BONUS;

	public IncineratorProperties(ModConfigSpec.Builder builder, String name,
	                             int defaultChargeTime, double defaultDamage,
	                             double defaultVelocity, double defaultInaccuracy,
	                             int defaultAmmoCapacity, int defaultFireInterval,
	                             double defaultQuickChargeMultiplier, double defaultSpeedModifier,
	                             int defaultBlazeCapacity, int defaultBlazeReloadInc,
	                             int defaultKnockbackBonus, boolean onlyAllowMainHand) {
		super(builder, name, defaultChargeTime, defaultDamage, defaultVelocity, defaultInaccuracy, defaultAmmoCapacity,
				defaultFireInterval, defaultQuickChargeMultiplier, defaultSpeedModifier, onlyAllowMainHand, false);
		BLAZE_AMMO_CAPACITY = builder.comment("Ammo Capacity Of " + name + " With Blaze Enchanted")
				.defineInRange("BlazeAmmoCapacity", defaultBlazeCapacity, 1, 64);
		BLAZE_RELOAD_INCREMENT = builder.comment("Additional Reload Time Of " + name + " With Blaze Enchanted")
				.defineInRange("BlazeReloadIncrement", defaultBlazeReloadInc, 0, Integer.MAX_VALUE);
		FIRE_ARROW_KNOCKBACK_BONUS = builder.comment("Knockback Bonus When Arrow Hit An Entity On Fire")
				.comment("1 Knockback Equals 1 Punch Level")
				.defineInRange("KnockbackBonus", defaultKnockbackBonus, 0, 16);
		builder.pop();
	}
}
