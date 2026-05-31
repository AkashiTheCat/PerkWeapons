package net.akashi.perk_weapons.Config.Properties.Crossbow;

import net.neoforged.neoforge.common.ModConfigSpec;

public class QueenBeeProperties extends CrossbowProperties {
	public ModConfigSpec.IntValue MAX_PERK_LEVEL;
	public ModConfigSpec.IntValue POISON_LEVEL;
	public ModConfigSpec.IntValue POISON_DURATION;
	public ModConfigSpec.IntValue ROYAL_JELLY_LEVEL;
	public ModConfigSpec.IntValue ROYAL_JELLY_DURATION;
	public ModConfigSpec.IntValue COOLDOWN_CROUCH_USE;

	public QueenBeeProperties(ModConfigSpec.Builder builder, String name,
	                          int defaultChargeTime, double defaultDamage,
	                          double defaultVelocity, double defaultInaccuracy,
	                          int defaultAmmoCapacity, int defaultFireInterval,
	                          double defaultQuickChargeMultiplier, int defaultMaxPerkLevel,
	                          int defaultPoisonLevel, int defaultPoisonDuration,
	                          int defaultRoyalJellyLevel, int defaultRoyalJellyDuration,
	                          int defaultCrouchUseCD, double defaultSpeedModifier,
	                          boolean onlyAllowMainHand) {
		super(builder, name, defaultChargeTime, defaultDamage, defaultVelocity, defaultInaccuracy, defaultAmmoCapacity,
				defaultFireInterval, defaultQuickChargeMultiplier, defaultSpeedModifier, onlyAllowMainHand, false);
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
