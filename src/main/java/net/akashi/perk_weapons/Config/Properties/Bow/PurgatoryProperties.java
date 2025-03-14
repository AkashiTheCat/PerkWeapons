package net.akashi.perk_weapons.Config.Properties.Bow;

import net.minecraftforge.common.ForgeConfigSpec;

public class PurgatoryProperties extends BowProperties {
	public ForgeConfigSpec.IntValue PIERCE_LEVEL;
	public ForgeConfigSpec.IntValue FUSE_TIME;

	public PurgatoryProperties(ForgeConfigSpec.Builder builder, String name,
	                           int defaultDrawTime, double defaultDamage, double defaultVelocity,
	                           double defaultInaccuracy, int defaultPierceLevel, int defaultFuseTime,
	                           double defaultSpeedModifier, double defaultZoomFactor,
	                           boolean onlyMainHand) {
		super(builder, name, defaultDrawTime, defaultDamage,
				defaultVelocity, defaultInaccuracy, defaultSpeedModifier, defaultZoomFactor, onlyMainHand, false);
		PIERCE_LEVEL = builder.comment("Pierce Level Of " + name + "'s Arrow")
				.defineInRange("PierceLevel", defaultPierceLevel, 0, 127);
		FUSE_TIME = builder.comment("The Fuse Time(In Ticks) Of The Arrow When Melt Down Enchanted")
				.comment("Arrow Explodes When Its FuseTimer = 0")
				.defineInRange("FuseTime", defaultFuseTime, 0, Integer.MAX_VALUE);
		builder.pop();
	}
}
