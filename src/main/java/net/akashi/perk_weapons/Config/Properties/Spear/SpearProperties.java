package net.akashi.perk_weapons.Config.Properties.Spear;

import net.minecraftforge.common.ForgeConfigSpec;

public class SpearProperties {
	public ForgeConfigSpec.DoubleValue MELEE_DAMAGE;
	public ForgeConfigSpec.DoubleValue ATTACK_SPEED;
	public ForgeConfigSpec.DoubleValue RANGED_DAMAGE;
	public ForgeConfigSpec.DoubleValue VELOCITY;
	public ForgeConfigSpec.IntValue MAX_CHARGE_TICKS;

	public SpearProperties(ForgeConfigSpec.Builder builder, String name,
	                       float defaultMeleeDamage, double defaultAttackSpeed,
	                       float defaultRangedDamage, float defaultVelocity,
	                       int defaultMaxChargeTicks, boolean shouldPop) {
		builder.push(name);
		MELEE_DAMAGE = builder.comment("Melee Damage Of " + name)
				.defineInRange("Damage", defaultMeleeDamage, 0.1d, Double.MAX_VALUE);
		ATTACK_SPEED = builder.comment("Attack Speed Of " + name)
				.defineInRange("Speed", defaultAttackSpeed, 0.1d, 20.0d);
		RANGED_DAMAGE = builder.comment("Ranged Damage(Throw Damage) Of " + name)
				.defineInRange("Ranged", defaultRangedDamage, 0.1d, Double.MAX_VALUE);
		VELOCITY = builder.comment("Projectile Velocity Of " + name)
				.defineInRange("Velocity", defaultVelocity, 0.1d, 20.0d);
		MAX_CHARGE_TICKS = builder.comment("Charging Time(Ticks) Before " + name + " Can Be Thrown")
				.defineInRange("ChargeTime", defaultMaxChargeTicks, 0, Integer.MAX_VALUE);
		if (shouldPop) {
			builder.pop();
		}
	}
}
