package net.akashi.perk_weapons.Config.Properties.Spear;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;

public class SpearProperties {
	public DoubleValue MELEE_DAMAGE;
	public DoubleValue ATTACK_SPEED;
	public DoubleValue RANGED_DAMAGE;
	public DoubleValue VELOCITY;

	public SpearProperties(ForgeConfigSpec.Builder builder, String name,
	                       float defaultMeleeDamage, double defaultAttackSpeed,
	                       float defaultRangedDamage, float defaultVelocity,
	                       boolean shouldPop) {
		builder.push(name);
		MELEE_DAMAGE = builder.comment("Melee Damage Of " + name)
				.defineInRange("Damage", defaultMeleeDamage,0.1d,Double.MAX_VALUE);
		ATTACK_SPEED = builder.comment("Attack Speed Of " + name)
				.defineInRange("Speed", defaultAttackSpeed,0.1d,20.0d);
		RANGED_DAMAGE = builder.comment("Ranged Damage(Throw Damage) Of " + name)
				.defineInRange("Ranged", defaultRangedDamage,0.1d,Double.MAX_VALUE);
		VELOCITY = builder.comment("Projectile Velocity Of " + name)
				.defineInRange("Velocity", defaultVelocity,0.1d,20.0d);
		if(shouldPop){
			builder.pop();
		}
	}
}
