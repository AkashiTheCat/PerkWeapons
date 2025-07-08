package net.akashi.perk_weapons.Config.Properties.Crossbow;

import net.minecraftforge.common.ForgeConfigSpec;

public class LiberatorProperties extends CrossbowProperties {
	public ForgeConfigSpec.IntValue PIERCE_LEVEL;
	public ForgeConfigSpec.IntValue MULTISHOT_BONUS;
	public ForgeConfigSpec.IntValue CAPACITY_REGICIDE;

	public LiberatorProperties(ForgeConfigSpec.Builder builder, String name,
	                           int defaultChargeTime, double defaultDamage,
	                           double defaultVelocity, double defaultInaccuracy,
	                           int defaultAmmoCapacity, int defaultFireInterval,
	                           double defaultSpeedModifier, byte defaultPierceLevel,
	                           int defaultMultiShotBonus, int defaultCapacityRegicide,
	                           boolean onlyAllowMainHand) {
		super(builder, name, defaultChargeTime, defaultDamage, defaultVelocity, defaultInaccuracy,
				defaultAmmoCapacity, defaultFireInterval, defaultSpeedModifier, onlyAllowMainHand, false);
		PIERCE_LEVEL = builder.comment("Pierce Level Of " + name + "'s Arrow")
				.defineInRange("PierceLevel", defaultPierceLevel, 0, 127);
		MULTISHOT_BONUS = builder.comment("The MultiSot Bonus Level Of " + name)
				.defineInRange("MultiShotBonus", defaultMultiShotBonus, 0, 250);
		CAPACITY_REGICIDE = builder.comment("Ammo Capacity When Regicide Is Enchanted")
				.defineInRange("Capacity_Regicide", defaultCapacityRegicide, 1, 64);
		builder.pop();
	}
}
