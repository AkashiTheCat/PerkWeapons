package net.akashi.perk_weapons.Config.Properties.Spear;

import net.minecraftforge.common.ForgeConfigSpec;

public class MegalodonProperties extends SpearProperties {
	public ForgeConfigSpec.IntValue DOLPHINS_GRACE_LEVEL;
	public ForgeConfigSpec.IntValue DOLPHINS_GRACE_TICKS;
	public ForgeConfigSpec.IntValue HASTE_LEVEL;
	public ForgeConfigSpec.IntValue HASTE_TICKS;
	public ForgeConfigSpec.IntValue STRENGTH_LEVEL;
	public ForgeConfigSpec.IntValue STRENGTH_TICKS;

	public MegalodonProperties(ForgeConfigSpec.Builder builder, String name,
	                           float defaultMeleeDamage, double defaultAttackSpeed,
	                           float defaultRangedDamage, float defaultVelocity,
	                           int defaultDolphinsGraceLevel, int defaultDolphinsGraceTicks,
	                           int defaultHasteLevel, int defaultHasteTicks,
	                           int defaultStrengthLevel, int defaultStrengthTicks) {
		super(builder, name, defaultMeleeDamage, defaultAttackSpeed, defaultRangedDamage,
				defaultVelocity, false);
		DOLPHINS_GRACE_LEVEL = builder.comment("Level Of Dolphin's Grace Effect Applied To Owner On Hit")
				.defineInRange("DolphinsGraceLevel", defaultDolphinsGraceLevel, 0, 255);
		DOLPHINS_GRACE_TICKS = builder.comment("Effect Time In Ticks Of Dolphin's Grace Effect")
				.defineInRange("DolphinsGraceTicks", defaultDolphinsGraceTicks, 0, Integer.MAX_VALUE);
		HASTE_LEVEL = builder.comment("Level Of Haste Effect Applied To Owner On Hit")
				.defineInRange("HasteLevel", defaultHasteLevel, 0, 255);
		HASTE_TICKS = builder.comment("Effect Time In Ticks Of Haste Effect")
				.defineInRange("HasteTicks", defaultHasteTicks, 0, Integer.MAX_VALUE);
		STRENGTH_LEVEL = builder.comment("Level Of Strength Effect Applied To Owner On Hit")
				.defineInRange("StrengthLevel", defaultStrengthLevel, 0, 255);
		STRENGTH_TICKS = builder.comment("Effect Time In Ticks Of Strength Effect")
				.defineInRange("StrengthTicks", defaultStrengthTicks, 0, Integer.MAX_VALUE);
		builder.pop();
	}
}
