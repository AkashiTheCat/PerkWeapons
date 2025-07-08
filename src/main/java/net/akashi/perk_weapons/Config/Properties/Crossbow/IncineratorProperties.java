package net.akashi.perk_weapons.Config.Properties.Crossbow;

import net.minecraftforge.common.ForgeConfigSpec;

public class IncineratorProperties extends CrossbowProperties {
	public ForgeConfigSpec.IntValue BLAZE_AMMO_CAPACITY;
	public ForgeConfigSpec.IntValue BLAZE_RELOAD_INCREMENT;
	public ForgeConfigSpec.IntValue FIRE_ARROW_KNOCKBACK_BONUS;

	public IncineratorProperties(ForgeConfigSpec.Builder builder, String name,
	                             int defaultChargeTime, double defaultDamage,
	                             double defaultVelocity, double defaultInaccuracy,
	                             int defaultAmmoCapacity, int defaultFireInterval,
	                             double defaultSpeedModifier, int defaultBlazeCapacity,
	                             int defaultBlazeReloadInc, int defaultKnockbackBonus,
	                             boolean onlyAllowMainHand) {
		super(builder, name, defaultChargeTime, defaultDamage, defaultVelocity, defaultInaccuracy,
				defaultAmmoCapacity, defaultFireInterval, defaultSpeedModifier, onlyAllowMainHand, false);
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
