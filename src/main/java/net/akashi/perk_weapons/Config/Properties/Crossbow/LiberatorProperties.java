package net.akashi.perk_weapons.Config.Properties.Crossbow;

import net.neoforged.neoforge.common.ModConfigSpec;

public class LiberatorProperties extends CrossbowProperties {
	public ModConfigSpec.IntValue PIERCE_LEVEL;
	public ModConfigSpec.IntValue MULTISHOT_BONUS;
	public ModConfigSpec.IntValue CAPACITY_REGICIDE;

	public LiberatorProperties(ModConfigSpec.Builder builder, String name,
	                           int defaultChargeTime, double defaultDamage,
	                           double defaultVelocity, double defaultInaccuracy,
	                           int defaultAmmoCapacity, int defaultFireInterval,
	                           double defaultQuickChargeMultiplier, double defaultSpeedModifier,
	                           byte defaultPierceLevel, int defaultMultiShotBonus,
	                           int defaultCapacityRegicide, boolean onlyAllowMainHand) {
		super(builder, name, defaultChargeTime, defaultDamage, defaultVelocity, defaultInaccuracy, defaultAmmoCapacity,
				defaultFireInterval, defaultQuickChargeMultiplier, defaultSpeedModifier, onlyAllowMainHand, false);
		PIERCE_LEVEL = builder.comment("Pierce Level Of " + name + "'s Arrow")
				.defineInRange("PierceLevel", defaultPierceLevel, 0, 127);
		MULTISHOT_BONUS = builder.comment("The MultiSot Bonus Level Of " + name)
				.defineInRange("MultiShotBonus", defaultMultiShotBonus, 0, 250);
		CAPACITY_REGICIDE = builder.comment("Ammo Capacity When Regicide Is Enchanted")
				.defineInRange("Capacity_Regicide", defaultCapacityRegicide, 1, 64);
		builder.pop();
	}
}
