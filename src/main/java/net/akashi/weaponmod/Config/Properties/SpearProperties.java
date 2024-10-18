package net.akashi.weaponmod.Config.Properties;

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
				.defineInRange("Damage", defaultMeleeDamage,0.1d,100.0d);
		ATTACK_SPEED = builder.comment("Attack Speed Of " + name)
				.defineInRange("Speed", defaultAttackSpeed,0.1d,4.0d);
		RANGED_DAMAGE = builder.comment("Ranged Damage(Throw Damage) Of " + name)
				.defineInRange("Ranged", defaultRangedDamage,0.1d,100.0d);
		VELOCITY = builder.comment("Projectile Velocity Of " + name)
				.defineInRange("Velocity", defaultVelocity,0.1d,10.0d);
		if(shouldPop){
			builder.pop();
		}
	}
}
