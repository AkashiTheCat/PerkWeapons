package net.akashi.perk_weapons.Config.Properties.Bow;

import net.minecraftforge.common.ForgeConfigSpec;

public class FrostHunterProperties extends BowProperties {
	public ForgeConfigSpec.IntValue FROZEN_TIME;
	public ForgeConfigSpec.IntValue ABILITY_COOLDOWN_TIME;
	public ForgeConfigSpec.IntValue HOUND_LIFETIME;
	public ForgeConfigSpec.IntValue HOUND_COUNT;
	public ForgeConfigSpec.BooleanValue ENABLE_HOUND_EFFECT;

	public FrostHunterProperties(ForgeConfigSpec.Builder builder, String name,
	                             int defaultDrawTime, double defaultDamage, double defaultVelocity,
	                             double defaultInaccuracy, int defaultFrozenTime,
	                             int defaultCoolDown, int defaultHoundLifeTime,
	                             int defaultHoundCount, boolean defaultHoundEffectEnabled,
	                             double defaultSpeedModifier, double defaultZoomFactor) {
		super(builder, name, defaultDrawTime, defaultDamage, defaultVelocity,
				defaultInaccuracy, defaultSpeedModifier, defaultZoomFactor, false);
		FROZEN_TIME = builder.comment("Frozen Time(In Ticks) When Hit An Entity")
				.defineInRange("FrozenTime", defaultFrozenTime, 0, Integer.MAX_VALUE);
		ABILITY_COOLDOWN_TIME = builder.comment("How Long In Ticks Will The Crouch+Use Ability's CoolDown Be")
				.defineInRange("AbilityCD", defaultCoolDown, 0, 6000);
		HOUND_LIFETIME = builder.comment("Summoned Hounds Will Despawn After this Ticks")
				.defineInRange("LifeTime", defaultHoundLifeTime, 0, Integer.MAX_VALUE);
		HOUND_COUNT = builder.comment("Count Of Summoned Hounds")
				.defineInRange("HoundCount", defaultHoundCount, 0, 20);
		ENABLE_HOUND_EFFECT = builder.comment("Set True To Enable Potion Effects On Summoned Hounds")
				.define("HoundEffects", defaultHoundEffectEnabled);
		builder.pop();
	}
}
