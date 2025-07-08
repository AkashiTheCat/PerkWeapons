package net.akashi.perk_weapons.Config.Properties.Crossbow;

import net.minecraftforge.common.ForgeConfigSpec;

public class TaintedFortuneProperties extends CrossbowProperties {
	public ForgeConfigSpec.DoubleValue KNOCKBACK_MODIFIER;

	public TaintedFortuneProperties(ForgeConfigSpec.Builder builder, String name,
	                                int defaultChargeTime, double defaultDamage,
	                                double defaultVelocity, double defaultInaccuracy,
	                                int defaultAmmoCapacity, int defaultFireInterval,
	                                double defaultSpeedModifier, double defaultKnockbackBonus,
	                                boolean onlyAllowMainHand) {
		super(builder, name, defaultChargeTime, defaultDamage, defaultVelocity, defaultInaccuracy,
				defaultAmmoCapacity, defaultFireInterval, defaultSpeedModifier, onlyAllowMainHand, false);
		KNOCKBACK_MODIFIER = builder.comment("Knockback Modifier When Holding " + name + " In Offhand")
				.defineInRange("KnockbackModifier", defaultKnockbackBonus, -10.0, 10.0);
		builder.pop();
	}
}
