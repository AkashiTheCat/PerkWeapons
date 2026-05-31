package net.akashi.perk_weapons.Config.Properties.Bow;

import net.neoforged.neoforge.common.ModConfigSpec;

public class DevourerProperties extends BowProperties {
	public ModConfigSpec.IntValue PIERCE_LEVEL;

	public DevourerProperties(ModConfigSpec.Builder builder, String name,
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
