package net.akashi.perk_weapons.Config.Properties.Crossbow;

import net.minecraftforge.common.ForgeConfigSpec;

public class QueenBeeProperties extends CrossbowProperties {
	public ForgeConfigSpec.IntValue MAX_PERK_LEVEL;
	public ForgeConfigSpec.IntValue POISON_LEVEL;
	public ForgeConfigSpec.IntValue POISON_DURATION;
	public ForgeConfigSpec.IntValue ROYAL_JELLY_LEVEL;
	public ForgeConfigSpec.IntValue ROYAL_JELLY_DURATION;
	public ForgeConfigSpec.IntValue COOLDOWN_CROUCH_USE;

	public QueenBeeProperties(ForgeConfigSpec.Builder builder, String name,
	                          int defaultChargeTime, double defaultDamage,
	                          double defaultVelocity, double defaultInaccuracy,
	                          int defaultAmmoCapacity, int defaultFireInterval,
							  int defaultMaxPerkLevel, int defaultPoisonLevel,
							  int defaultPoisonDuration, int defaultRoyalJellyLevel,
							  int defaultRoyalJellyDuration, int defaultCrouchUseCD,
	                          double defaultSpeedModifier, boolean onlyAllowMainHand) {
		super(builder, name, defaultChargeTime, defaultDamage, defaultVelocity, defaultInaccuracy,
				defaultAmmoCapacity, defaultFireInterval, defaultSpeedModifier, onlyAllowMainHand, false);
		MAX_PERK_LEVEL = builder.comment("Max Perk Level Of " + name)
				.defineInRange("MaxPerkLevel", defaultMaxPerkLevel, 0, 255);
		POISON_LEVEL = builder.comment("Level Of Poison Effect Applied On Target On Arrow Hit")
				.defineInRange("PoisonLevel", defaultPoisonLevel, 0, 255);
		POISON_DURATION = builder.comment("Duration(Ticks) Of Poison Effect Applied On Target On Arrow Hit")
				.defineInRange("PoisonDuration", defaultPoisonDuration, 0, Integer.MAX_VALUE);
		ROYAL_JELLY_LEVEL = builder.comment("Level Of Royal Jelly Effect Granted On Crouch+Use Ability")
				.defineInRange("RoyalJellyLevel", defaultRoyalJellyLevel, 0, 255);
		ROYAL_JELLY_DURATION = builder.comment("Duration(Ticks) Of Royal Jelly Effect Granted On Crouch+Use Ability")
				.defineInRange("RoyalJellyDuration", defaultRoyalJellyDuration, 0, Integer.MAX_VALUE);
		COOLDOWN_CROUCH_USE = builder.comment("Cooldown Ticks Applied On " + name +
						" After A Successful Crouch+Use Ability Activation")
				.defineInRange("CrouchUseCooldown", defaultCrouchUseCD, 0, Integer.MAX_VALUE);
		builder.pop();
	}
}
