package net.akashi.perk_weapons.Config.Properties.Enchantment;

import net.neoforged.neoforge.common.ModConfigSpec;

public class EnchantmentProperties {
	public ModConfigSpec.BooleanValue ALLOW_ON_TABLE;
	public ModConfigSpec.BooleanValue ALLOW_ON_BOOK;
	public ModConfigSpec.BooleanValue IS_DISCOVERABLE;
	public ModConfigSpec.BooleanValue IS_TREASURE_ONLY;
	public ModConfigSpec.BooleanValue IS_TRADEABLE;
	public ModConfigSpec.ConfigValue<String> RARITY;

	public EnchantmentProperties(ModConfigSpec.Builder builder, String name,
	                             boolean defaultAllowOnTable, boolean defaultAllowOnBook,
	                             boolean defaultIsDiscoverable, boolean defaultIsTreasureOnly,
	                             boolean defaultIsTradeable, String defaultRarity,
	                             boolean shouldPop) {
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
		RARITY = builder.comment("Rarity of " + name)
				.define("rarity", defaultRarity);
		if (shouldPop)
			builder.pop();
	}
}
