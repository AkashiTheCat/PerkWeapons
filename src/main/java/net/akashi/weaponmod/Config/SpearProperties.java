package net.akashi.weaponmod.Config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SpearProperties {
	public ForgeConfigSpec.ConfigValue<Float> MELEE_DAMAGE;
	public ForgeConfigSpec.ConfigValue<Float> ATTACK_SPEED;
	public ForgeConfigSpec.ConfigValue<Float> RANGED_DAMAGE;
	public ForgeConfigSpec.ConfigValue<Float> VELOCITY;
	public ForgeConfigSpec.ConfigValue<Integer> DURABILITY;

	SpearProperties(ForgeConfigSpec.Builder builder, String name,
	                float defaultMeleeDamage, float defaultAttackSpeed,
	                float defaultRangedDamage, float defaultVelocity,
	                int defaultDurability) {
		builder.push(name);
		MELEE_DAMAGE = builder.comment("Melee Damage Of " + name)
				.define("Damage", defaultMeleeDamage);
		ATTACK_SPEED = builder.comment("Attack Speed Of " + name)
				.define("Speed", defaultAttackSpeed);
		RANGED_DAMAGE = builder.comment("Ranged Damage(Throw Damage) Of " + name)
				.define("Ranged", defaultRangedDamage);
		VELOCITY = builder.comment("Projectile Velocity Of " + name)
				.define("Speed", defaultVelocity);
		DURABILITY = builder.comment("Durability Of " + name)
				.define("Durability", defaultDurability);
		builder.pop();
	}

}
