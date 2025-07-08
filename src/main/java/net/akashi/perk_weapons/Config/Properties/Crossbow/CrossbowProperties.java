package net.akashi.perk_weapons.Config.Properties.Crossbow;

import net.minecraftforge.common.ForgeConfigSpec;

public class CrossbowProperties {
	public ForgeConfigSpec.IntValue CHARGE_TIME;
	public ForgeConfigSpec.DoubleValue DAMAGE;
	public ForgeConfigSpec.DoubleValue VELOCITY;
	public ForgeConfigSpec.DoubleValue INACCURACY;
	public ForgeConfigSpec.IntValue AMMO_CAPACITY;
	public ForgeConfigSpec.IntValue FIRE_INTERVAL;
	public ForgeConfigSpec.DoubleValue SPEED_MODIFIER;
	public ForgeConfigSpec.BooleanValue ONLY_MAINHAND;

	public CrossbowProperties(ForgeConfigSpec.Builder builder, String name,
	                          int defaultChargeTime, double defaultDamage,
	                          double defaultVelocity, double defaultInaccuracy,
	                          int defaultAmmoCapacity, int defaultFireInterval,
	                          double defaultSpeedModifier, boolean onlyAllowMainHand,
	                          boolean shouldPop) {
		builder.push(name);
		topComment(builder, name);
		CHARGE_TIME = builder.comment("Ammo Charging Time Of " + name + " In Ticks")
				.defineInRange("ChargeTime", defaultChargeTime, 0, Integer.MAX_VALUE);
		DAMAGE = builder.comment("Damage Of " + name)
				.defineInRange("Damage", defaultDamage, 0, Double.MAX_VALUE);
		VELOCITY = builder.comment("Projectile Velocity Of " + name)
				.defineInRange("Velocity", defaultVelocity, 0.1, 20.0);
		INACCURACY = builder.comment("Inaccuracy Of Arrow")
				.defineInRange("Inaccuracy", defaultInaccuracy, 0.0, 20.0);
		AMMO_CAPACITY = builder.comment("Ammo Capacity Of " + name)
				.defineInRange("AmmoCapacity", defaultAmmoCapacity, 0, 128);
		FIRE_INTERVAL = builder.comment("Fire Interval(If Loaded Ammo > 1) In Ticks Of " + name)
				.defineInRange("FireInterval", defaultFireInterval, 0, Integer.MAX_VALUE);
		SPEED_MODIFIER = builder.comment("Speed Modifier When Holding " + name)
				.defineInRange("SpeedModifier", defaultSpeedModifier, -1.0, 1.0);
		ONLY_MAINHAND = builder.comment("Set True To Make " + name + " Only Can Be Used In Main Hand.")
				.comment("This Will Be Automatically Set True If SpeedModifier!=0 To Avoid A Bug Caused By The " +
						"Vanilla Equipment Update Method")
				.define("OnlyMainHand", onlyAllowMainHand);
		if (shouldPop) {
			builder.pop();
		}
	}

	public void topComment(ForgeConfigSpec.Builder builder, String name) {

	}
}
