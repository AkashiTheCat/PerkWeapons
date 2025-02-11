package net.akashi.perk_weapons.Config.Properties.Bow;

import net.minecraftforge.common.ForgeConfigSpec;

public class ElfsHarpProperties extends BowProperties {
	public ForgeConfigSpec.IntValue MAX_PERK_LEVEL;
	public ForgeConfigSpec.IntValue GLOWING_TIME;
	public ForgeConfigSpec.DoubleValue PERK_BUFF;

	public ElfsHarpProperties(ForgeConfigSpec.Builder builder, String name,
	                          int defaultDrawTime, double defaultDamage, double defaultVelocity,
	                          double defaultInaccuracy, int defaultMaxPerkLevel,
	                          int defaultGlowingTime, double defaultPerkBuff,
	                          double defaultSpeedModifier, double defaultZoomFactor,
	                          boolean onlyMainHand) {
		super(builder, name, defaultDrawTime, defaultDamage, defaultVelocity,
				defaultInaccuracy, defaultSpeedModifier, defaultZoomFactor, onlyMainHand, false);
		MAX_PERK_LEVEL = builder.comment("How Many Shots does " + name + " Need To Fire A Buffed Shot")
				.defineInRange("MaxPerkLevel", defaultMaxPerkLevel, 0, 127);
		GLOWING_TIME = builder.comment("How Long(In Ticks) Do Hit Entities Glow")
				.defineInRange("GlowingTime", defaultGlowingTime, 0, Integer.MAX_VALUE);
		PERK_BUFF = builder.comment("The Damage Bonus Percent Of The Buffed Shot")
				.defineInRange("PerkBuff", defaultPerkBuff, 0, 255.0);
		builder.pop();
	}
}
