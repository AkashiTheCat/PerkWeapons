package net.akashi.perk_weapons.Config.Properties.Bow;

import net.akashi.perk_weapons.Config.Properties.ModExplosionProperties;
import net.minecraftforge.common.ForgeConfigSpec;

public class PurgatoryProperties extends BowProperties {
	public ForgeConfigSpec.IntValue PIERCE_LEVEL;
	public ForgeConfigSpec.IntValue FUSE_TIME;
	public ForgeConfigSpec.IntValue INTERNAL_EXP_EFFECT_TIME;
	public ForgeConfigSpec.IntValue INTERNAL_EXP_EFFECT_LEVEL;
	public ModExplosionProperties EXPLOSION_PROPERTIES;

	public PurgatoryProperties(ForgeConfigSpec.Builder builder, String name,
	                           int defaultDrawTime, double defaultDamage, double defaultVelocity,
	                           double defaultInaccuracy, int defaultPierceLevel, double defaultSpeedModifier,
	                           double defaultZoomFactor, boolean onlyMainHand,
	                           int fuseTime, double expInnerR, double expOuterR,
	                           double expInnerDmg, double expOuterDmg, double expKnockback,
	                           boolean expIgnoreWall, int internalExpTime, int internalExpLevel) {
		super(builder, name, defaultDrawTime, defaultDamage,
				defaultVelocity, defaultInaccuracy, defaultSpeedModifier, defaultZoomFactor, onlyMainHand, false);
		PIERCE_LEVEL = builder.comment("Pierce Level Of " + name + "'s Arrow")
				.defineInRange("PierceLevel", defaultPierceLevel, 0, 127);

		builder.push("(When Melt Down Enchanted) Explosive Arrow Configs");
		FUSE_TIME = builder.comment("The Fuse Time(In Ticks) Of The Arrow")
				.comment("Arrow Explodes When Its FuseTimer = 0")
				.defineInRange("FuseTime", fuseTime, 0, Integer.MAX_VALUE);
		INTERNAL_EXP_EFFECT_TIME = builder.comment("Duration Of The \"Internal Explosion\" Effect Applied On Hit In Ticks")
				.defineInRange("EffectTime", internalExpTime, 0, Integer.MAX_VALUE);
		INTERNAL_EXP_EFFECT_LEVEL = builder.comment("Level Of The \"Internal Explosion\" Effect Applied On Hit")
				.defineInRange("EffectLevel", internalExpLevel, 0, 255);

		EXPLOSION_PROPERTIES = new ModExplosionProperties(builder, expInnerR, expOuterR,
				expInnerDmg, expOuterDmg, expKnockback, expIgnoreWall);
		builder.pop();

		builder.pop();
	}
}
