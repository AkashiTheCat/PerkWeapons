package net.akashi.perk_weapons.Config.Properties.Bow;

import net.minecraftforge.common.ForgeConfigSpec;

public class ForestKeeperProperties extends BowProperties {
	public ForgeConfigSpec.IntValue MAX_PERK_LEVEL;
	public ForgeConfigSpec.IntValue PERK_DROP_INTERVAL;
	public ForgeConfigSpec.DoubleValue PERK_DAMAGE_BUFF;
	public ForgeConfigSpec.BooleanValue ENABLE_SLOWDOWN_REMOVAL;

	public ForestKeeperProperties(ForgeConfigSpec.Builder builder, String name,
	                              int defaultDrawTime, double defaultDamage, double defaultVelocity,
	                              double defaultInaccuracy, int defaultMaxPerkLevel,
	                              int defaultPerkDropInterval, double defaultPerkDamageBuff,
	                              boolean defaultSlowdownRemovalEnabled, double defaultSpeedModifier,
	                              double defaultZoomFactor, boolean onlyMainHand) {
		super(builder, name, defaultDrawTime, defaultDamage, defaultVelocity,
				defaultInaccuracy, defaultSpeedModifier, defaultZoomFactor, onlyMainHand, false);
		MAX_PERK_LEVEL = builder.comment("The Max Perk Level Can " + name + " reach")
				.defineInRange("MaxPerkLevel", defaultMaxPerkLevel, 0, 255);
		PERK_DROP_INTERVAL = builder.comment("Perk Drop Interval(In Ticks) Of " + name)
				.defineInRange("PerkDropInterval", defaultPerkDropInterval, 0, Integer.MAX_VALUE);
		PERK_DAMAGE_BUFF = builder.comment("The Damage Bonus Percent Each Perk Level Will Provide")
				.defineInRange("PerkBonus", defaultPerkDamageBuff, 0, 10);
		ENABLE_SLOWDOWN_REMOVAL = builder.comment("Set True To Make Player Don't Slowdown When Drawing " + name)
				.define("EnableSlowdownRemoval", defaultSlowdownRemovalEnabled);
		builder.pop();
	}
}
