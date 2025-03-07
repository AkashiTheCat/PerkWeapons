package net.akashi.perk_weapons.Config.Properties.Bow;

import net.minecraftforge.common.ForgeConfigSpec;

public class DevourerProperties extends BowProperties {
	public ForgeConfigSpec.IntValue PIERCE_LEVEL;

	public DevourerProperties(ForgeConfigSpec.Builder builder, String name,
	                          int defaultDrawTime, double defaultDamage, double defaultVelocity,
	                          double defaultInaccuracy, byte defaultPierceLevel,
	                          double defaultSpeedModifier, double defaultZoomFactor,
	                          boolean onlyMainHand) {
		super(builder, name, defaultDrawTime, defaultDamage, defaultVelocity,
				defaultInaccuracy, defaultSpeedModifier, defaultZoomFactor, onlyMainHand, false);
		PIERCE_LEVEL = builder.comment("Pierce Level Of " + name + "'s Arrow")
				.defineInRange("PierceLevel", defaultPierceLevel, 0, 127);
		builder.pop();
	}
}
