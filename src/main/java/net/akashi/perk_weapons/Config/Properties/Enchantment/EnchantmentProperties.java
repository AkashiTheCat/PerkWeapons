package net.akashi.perk_weapons.Config.Properties.Enchantment;

import net.minecraftforge.common.ForgeConfigSpec;

public class EnchantmentProperties {
	public ForgeConfigSpec.BooleanValue ALLOW_ON_TABLE;
	public ForgeConfigSpec.BooleanValue ALLOW_ON_BOOK;
	public ForgeConfigSpec.BooleanValue IS_DISCOVERABLE;
	public ForgeConfigSpec.BooleanValue IS_TREASURE_ONLY;
	public ForgeConfigSpec.BooleanValue IS_TRADEABLE;

	public EnchantmentProperties(ForgeConfigSpec.Builder builder, String name,
	                             boolean defaultAllowOnTable, boolean defaultAllowOnBook,
	                             boolean defaultIsDiscoverable, boolean defaultIsTreasureOnly,
	                             boolean defaultIsTradeable, boolean shouldPop) {
		builder.push(name);
		ALLOW_ON_TABLE = builder.comment("Allow " + name + " To Appear On Enchantment Table")
				.define("allowOnTable", defaultAllowOnTable);
		ALLOW_ON_BOOK = builder.comment("Allow " + name + " To Be Enchanted On Books Via Enchantment Table")
				.define("allowOnBook", defaultAllowOnBook);
		IS_DISCOVERABLE = builder.comment("Allow " + name + " To Be Enchanted On Loot Table Generated Items")
				.define("isDiscoverable", defaultIsDiscoverable);
		IS_TREASURE_ONLY = builder.comment("Whether " + name + " Should Be Considered A Treasure Enchantment")
				.define("isTreasureOnly", defaultIsTreasureOnly);
		IS_TRADEABLE = builder.comment("Whether " + name + " Can Appear In Villager Trades")
				.define("isTradeable", defaultIsTradeable);
		if (shouldPop)
			builder.pop();
	}
}
