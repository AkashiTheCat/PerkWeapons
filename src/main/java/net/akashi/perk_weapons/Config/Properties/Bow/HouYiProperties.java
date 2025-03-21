package net.akashi.perk_weapons.Config.Properties.Bow;

import net.minecraftforge.common.ForgeConfigSpec;

public class HouYiProperties extends BowProperties {
	public ForgeConfigSpec.DoubleValue STAR_SHOOTER_DAMAGE_MODIFIER;
	public ForgeConfigSpec.DoubleValue STAR_SHOOTER_DAMAGE_MODIFIER_PER_METER;
	public ForgeConfigSpec.DoubleValue STAR_SHOOTER_DAMAGE_MODIFIER_MAX;
	public ForgeConfigSpec.DoubleValue STAR_SHOOTER_DAMAGE_MODIFIER_MIN;

	public HouYiProperties(ForgeConfigSpec.Builder builder, String name,
	                       int defaultDrawTime, double defaultDamage,
	                       double defaultVelocity, double defaultInaccuracy,
	                       double defaultSpeedModifier, double defaultZoomFactor,
	                       double SSDmgMod, double SSDmgModPerMeter,
	                       double SSDmgModMax, double SSDmgModMin,
	                       boolean onlyAllowMainHand) {
		super(builder, name, defaultDrawTime, defaultDamage, defaultVelocity, defaultInaccuracy,
				defaultSpeedModifier, defaultZoomFactor, onlyAllowMainHand, false);
		STAR_SHOOTER_DAMAGE_MODIFIER = builder.comment("Damage Modifier When Star Shooter Enchanted (Damage = Base damage * (1 + this))")
				.defineInRange("SSDmgMod", SSDmgMod, -1.0, Double.MAX_VALUE);
		STAR_SHOOTER_DAMAGE_MODIFIER_PER_METER = builder.comment("Final Damage = Damage * (1 + this * ArrowFlyDistance)")
				.defineInRange("SSDmgModPerMeter", SSDmgModPerMeter, -1.0, Double.MAX_VALUE);
		STAR_SHOOTER_DAMAGE_MODIFIER_MAX = builder.comment("Max Damage Modifier The Arrow Can Reach")
				.defineInRange("SSDmgModMax", SSDmgModMax, -1.0, Double.MAX_VALUE);
		STAR_SHOOTER_DAMAGE_MODIFIER_MIN = builder.comment("Min Damage Modifier The Arrow Can Reach")
				.defineInRange("SSDmgModMin", SSDmgModMin, -1.0, Double.MAX_VALUE);
		builder.pop();
	}
}
