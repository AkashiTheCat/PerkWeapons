package net.akashi.perk_weapons.Config.Properties.Bow;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ForestKeeperProperties extends BowProperties {
	public ModConfigSpec.IntValue MAX_PERK_LEVEL;
	public ModConfigSpec.IntValue PERK_DROP_INTERVAL;
	public ModConfigSpec.DoubleValue PERK_DAMAGE_BUFF;

	public ForestKeeperProperties(ModConfigSpec.Builder builder, String name,
	                              int defaultDrawTime, double defaultDamage, double defaultVelocity,
	                              double defaultInaccuracy, int defaultMaxPerkLevel,
	                              int defaultPerkDropInterval, double defaultPerkDamageBuff, double defaultSpeedModifier,
	                              double defaultZoomFactor, boolean onlyMainHand) {
		super(builder, name, defaultDrawTime, defaultDamage, defaultVelocity,
				defaultInaccuracy, defaultSpeedModifier, defaultZoomFactor, onlyMainHand, false);
		MAX_PERK_LEVEL = builder.comment("The Max Perk Level Can " + name + " reach")
				.defineInRange("MaxPerkLevel", defaultMaxPerkLevel, 0, 127);
		PERK_DROP_INTERVAL = builder.comment("Perk Drop Interval(In Ticks) Of " + name)
				.defineInRange("PerkDropInterval", defaultPerkDropInterval, 0, Integer.MAX_VALUE);
		PERK_DAMAGE_BUFF = builder.comment("The Damage Bonus Percent Each Perk Level Will Provide")
				.defineInRange("PerkBonus", defaultPerkDamageBuff, 0, 10);
		builder.pop();
	}
}
