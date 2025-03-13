package net.akashi.perk_weapons.Config.Properties.Bow;

import net.minecraftforge.common.ForgeConfigSpec;

public class HouYiProperties extends BowProperties {
	public ForgeConfigSpec.DoubleValue STAR_SHOOTER_DAMAGE_MODIFIER;
	public ForgeConfigSpec.DoubleValue STAR_SHOOTER_AIR_DAMAGE_MODIFIER;

	public HouYiProperties(ForgeConfigSpec.Builder builder, String name,
	                       int defaultDrawTime, double defaultDamage,
	                       double defaultVelocity, double defaultInaccuracy,
	                       double defaultSpeedModifier, double defaultZoomFactor,
	                       double SSDamageModifier, double SSAirDamageModifier,
	                       boolean onlyAllowMainHand) {
		super(builder, name, defaultDrawTime, defaultDamage, defaultVelocity, defaultInaccuracy,
				defaultSpeedModifier, defaultZoomFactor, onlyAllowMainHand, false);
		STAR_SHOOTER_DAMAGE_MODIFIER = builder.comment("Damage Modifier When Star Shooter Enchanted (Damage = Base damage * (1 + this))")
				.defineInRange("SSDamageModifier", SSDamageModifier, -1.0, Double.MAX_VALUE);
		STAR_SHOOTER_AIR_DAMAGE_MODIFIER = builder.comment("Damage Modifier To Entities In Air When Star Shooter Enchanted")
				.defineInRange("SSAirDamageModifier", SSAirDamageModifier, -1.0, Double.MAX_VALUE);
		builder.pop();
	}
}
