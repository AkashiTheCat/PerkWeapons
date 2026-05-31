package net.akashi.perk_weapons.Config.Properties.Bow;

import net.neoforged.neoforge.common.ModConfigSpec;

public class FrostHunterProperties extends BowProperties {
	public ModConfigSpec.IntValue FROZEN_TIME;
	public ModConfigSpec.IntValue ABILITY_COOLDOWN_TIME;
	public ModConfigSpec.IntValue HOUND_LIFETIME;
	public ModConfigSpec.IntValue HOUND_COUNT;
	public ModConfigSpec.BooleanValue ENABLE_HOUND_EFFECT;

	public FrostHunterProperties(ModConfigSpec.Builder builder, String name,
	                             int defaultDrawTime, double defaultDamage, double defaultVelocity,
	                             double defaultInaccuracy, int defaultFrozenTime,
	                             int defaultCoolDown, int defaultHoundLifeTime,
	                             int defaultHoundCount, boolean defaultHoundEffectEnabled,
	                             double defaultSpeedModifier, double defaultZoomFactor,
	                             boolean onlyMainHand) {
		super(builder, name, defaultDrawTime, defaultDamage, defaultVelocity,
				defaultInaccuracy, defaultSpeedModifier, defaultZoomFactor, onlyMainHand,false);
		FROZEN_TIME = builder.comment("Frozen Time(In Ticks) When Hit An Entity")
				.defineInRange("FrozenTime", defaultFrozenTime, 0, Integer.MAX_VALUE);
		ABILITY_COOLDOWN_TIME = builder.comment("Cooldown Of The Crouch+Use Ability In Ticks")
				.defineInRange("AbilityCD", defaultCoolDown, 0, 6000);
		HOUND_LIFETIME = builder.comment("Summoned Hounds Will Despawn After this Amount Of Ticks")
				.defineInRange("LifeTime", defaultHoundLifeTime, 0, Integer.MAX_VALUE);
		HOUND_COUNT = builder.comment("Count Of Summoned Hounds")
				.defineInRange("HoundCount", defaultHoundCount, 0, 20);
		ENABLE_HOUND_EFFECT = builder.comment("Set True To Enable Potion Effects On Summoned Hounds")
				.define("HoundEffects", defaultHoundEffectEnabled);
		builder.pop();
	}
}
