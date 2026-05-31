package net.akashi.perk_weapons.Registry;

import net.akashi.perk_weapons.Bows.*;
import net.akashi.perk_weapons.Crossbows.*;
import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.SimpleItems.RepairerItem;
import net.akashi.perk_weapons.Spears.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;

import static net.akashi.perk_weapons.Registry.ModCreativeTabs.*;

public class ModItems {
	//Simple items
	public static final DeferredRegister<Item> ITEMS =
			DeferredRegister.create(Registries.ITEM, PerkWeapons.MODID);
	public static final DeferredHolder<Item, Item> ANCIENT_SPIKE = addToTab(
			ITEMS.register("ancient_spike",
					() -> new Item(new Item.Properties().rarity(Rarity.RARE))), INGREDIENTS_TAB);
	public static final DeferredHolder<Item, Item> WITHER_SPINE = addToTab(
			ITEMS.register("wither_spine",
					() -> new Item(new Item.Properties().rarity(Rarity.EPIC))), INGREDIENTS_TAB);
	public static final DeferredHolder<Item, Item> GILDED_BLACKSTONE_HANDLE = addToTab(
			ITEMS.register("gilded_blackstone_handle",
					() -> new Item(new Item.Properties().rarity(Rarity.RARE))), INGREDIENTS_TAB);
	public static final DeferredHolder<Item, Item> GUARDIANS_EYE = addToTab(
			ITEMS.register("guardians_eye",
					() -> new Item(new Item.Properties().rarity(Rarity.RARE))), INGREDIENTS_TAB);
	public static final DeferredHolder<Item, Item> DRAGON_FANG = addToTab(
			ITEMS.register("dragon_fang",
					() -> new Item(new Item.Properties().rarity(Rarity.EPIC))), INGREDIENTS_TAB);
	public static final DeferredHolder<Item, Item> HUNTERS_REMAINS = addToTab(
			ITEMS.register("hunters_remains",
					() -> new Item(new Item.Properties().rarity(Rarity.RARE))), INGREDIENTS_TAB);
	public static final DeferredHolder<Item, Item> TOTEM_OF_EVOKING = addToTab(
			ITEMS.register("totem_of_evoking",
					() -> new Item(new Item.Properties().rarity(Rarity.EPIC))), INGREDIENTS_TAB);

	public static final DeferredHolder<Item, Item> REPAIRER = addToTab(
			ITEMS.register("repairer", () -> new RepairerItem(new Item.Properties()
					.rarity(Rarity.UNCOMMON).stacksTo(16))), INGREDIENTS_TAB);

	//Blocks
	public static final DeferredHolder<Item, BlockItem> FURNACE_CORE = addToTab(
			ITEMS.register("furnace_core", () -> new BlockItem(ModBlocks.FURNACE_CORE.get(),
					new Item.Properties().rarity(Rarity.UNCOMMON))), INGREDIENTS_TAB);

	//Spears
	public static final DeferredHolder<Item, BaseSpearItem> IRON_SPEAR = addToTab(
			ITEMS.register("iron_spear", () -> new BaseSpearItem(
					new Item.Properties().durability(256))),
			SPEAR_TAB);
	public static final DeferredHolder<Item, BaseSpearItem> GOLDEN_SPEAR = addToTab(
			ITEMS.register("golden_spear", () -> new BaseSpearItem(
					new Item.Properties().durability(32))),
			SPEAR_TAB);
	public static final DeferredHolder<Item, BaseSpearItem> DIAMOND_SPEAR = addToTab(
			ITEMS.register("diamond_spear", () -> new BaseSpearItem(
					new Item.Properties().durability(640))),
			SPEAR_TAB);
	public static final DeferredHolder<Item, BaseSpearItem> NETHERITE_SPEAR = addToTab(
			ITEMS.register("netherite_spear", () -> new BaseSpearItem(
					new Item.Properties().durability(1280).fireResistant())),
			SPEAR_TAB);
	public static final DeferredHolder<Item, MegalodonItem> SPEAR_MEGALODON = addToTab(
			ITEMS.register("megalodon", () -> new MegalodonItem(
					new Item.Properties().durability(1280).rarity(Rarity.UNCOMMON))),
			SPEAR_TAB);
	public static final DeferredHolder<Item, ConduitGuardItem> SPEAR_CONDUIT_GUARD = addToTab(
			ITEMS.register("conduit_guard", () -> new ConduitGuardItem(
					new Item.Properties().durability(1280).rarity(Rarity.UNCOMMON))),
			SPEAR_TAB);
	public static final DeferredHolder<Item, PiglinsWarSpearItem> PIGLINS_WARSPEAR = addToTab(
			ITEMS.register("piglins_warspear", () -> new PiglinsWarSpearItem(
					new Item.Properties().durability(640).rarity(Rarity.UNCOMMON))),
			SPEAR_TAB);
	public static final DeferredHolder<Item, DragonStrikeItem> DRAGON_STRIKE = addToTab(
			ITEMS.register("dragon_strike", () -> new DragonStrikeItem(
					new Item.Properties().durability(1280).fireResistant().rarity(Rarity.UNCOMMON))),
			SPEAR_TAB);
	public static final DeferredHolder<Item, ScourgeItem> SCOURGE = addToTab(
			ITEMS.register("scourge", () -> new ScourgeItem(
					new Item.Properties().durability(1280).fireResistant().rarity(Rarity.UNCOMMON))),
			SPEAR_TAB);
	public static final DeferredHolder<Item, NetherGuideItem> NETHER_GUIDE = addToTab(
			ITEMS.register("nether_guide", () -> new NetherGuideItem(
					new Item.Properties().durability(1280).rarity(Rarity.UNCOMMON))),
			SPEAR_TAB);
	public static final DeferredHolder<Item, CenturionItem> CENTURION = addToTab(
			ITEMS.register("centurion", () -> new CenturionItem(
					10, 1.2f,
					15, 2.5F,
					40, false,
					new Item.Properties().durability(1280).rarity(Rarity.UNCOMMON))),
			SPEAR_TAB);

	//Bows
	public static final DeferredHolder<Item, BaseBowItem> SHORT_BOW = addToTab(
			ITEMS.register("short_bow",
					() -> new BaseBowItem(12, 8, 2.25F, 1.0F,
							0.0F, 0.05F, false,
							new Item.Properties().durability(640))), BOW_TAB);
	public static final DeferredHolder<Item, BaseBowItem> LONGBOW = addToTab(
			ITEMS.register("longbow",
					() -> new BaseBowItem(40, 15, 4.5F, 0.2F,
							-0.5F, 0.15F, false,
							new Item.Properties().durability(320))), BOW_TAB);
	public static final DeferredHolder<Item, PurgatoryItem> PURGATORY = addToTab(
			ITEMS.register("purgatory",
					() -> new PurgatoryItem(50, 25, 4.5F, 0.2F,
							-1.0F, 1.0F, 0.15F, true,
							new Item.Properties().durability(320).rarity(Rarity.UNCOMMON))), BOW_TAB);
	public static final DeferredHolder<Item, ForestKeeperItem> FOREST_KEEPER = addToTab(
			ITEMS.register("forest_keeper",
					() -> new ForestKeeperItem(12, 8, 2.25F, 1.0F,
							0.0F, 0.0F, true,
							new Item.Properties().durability(1280).rarity(Rarity.UNCOMMON))), BOW_TAB);
	public static final DeferredHolder<Item, ElfsHarpItem> ELFS_HARP = addToTab(
			ITEMS.register("elfs_harp",
					() -> new ElfsHarpItem(20, 10, 3.0F, 0.8F,
							0.0F, 0.1F, true,
							new Item.Properties().durability(960).rarity(Rarity.UNCOMMON))), BOW_TAB);
	public static final DeferredHolder<Item, FrostHunterItem> FROST_HUNTER = addToTab(
			ITEMS.register("frost_hunter",
					() -> new FrostHunterItem(24, 9, 3.0F, 0.8F,
							0.0F, 0.1F, true,
							new Item.Properties().durability(960).rarity(Rarity.UNCOMMON))), BOW_TAB);
	public static final DeferredHolder<Item, HouYiItem> HOU_YI = addToTab(
			ITEMS.register("hou_yi",
					() -> new HouYiItem(40, 15, 4.5F, 0.2F,
							-0.5F, 0.15F, true,
							new Item.Properties().durability(640).rarity(Rarity.UNCOMMON))), BOW_TAB);
	public static final DeferredHolder<Item, DevourerItem> DEVOURER = addToTab(
			ITEMS.register("devourer",
					() -> new DevourerItem(20, 7, 3.0F, 0.8F,
							0.0F, 0.1F, true,
							new Item.Properties().durability(960).rarity(Rarity.UNCOMMON))), BOW_TAB);
	public static final DeferredHolder<Item, EndboreWandererItem> ENDBORE_WANDERER = addToTab(
			ITEMS.register("endbore_wanderer",
					() -> new EndboreWandererItem(12, 9, 2.5F, 0.8F,
							0.0F, 0.0F, false,
							new Item.Properties().durability(1280).rarity(Rarity.UNCOMMON))), BOW_TAB);

	//Crossbows
	public static final DeferredHolder<Item, BeholderItem> BEHOLDER = addToTab(
			ITEMS.register("beholder", () -> new BeholderItem(new Item.Properties()
					.durability(640)
					.rarity(Rarity.UNCOMMON))),
			CROSSBOW_TAB);
	public static final DeferredHolder<Item, LiberatorItem> LIBERATOR = addToTab(
			ITEMS.register("liberator", () -> new LiberatorItem(new Item.Properties()
					.durability(640)
					.rarity(Rarity.UNCOMMON))),
			CROSSBOW_TAB);
	public static final DeferredHolder<Item, TaintedFortuneItem> TAINTED_FORTUNE = addToTab(
			ITEMS.register("tainted_fortune", () -> new TaintedFortuneItem(new Item.Properties()
					.durability(1280)
					.rarity(Rarity.UNCOMMON))),
			CROSSBOW_TAB);
	public static final DeferredHolder<Item, SonicBlasterItem> SONIC_BLASTER = addToTab(
			ITEMS.register("sonic_blaster", () -> new SonicBlasterItem(new Item.Properties()
					.durability(640)
					.rarity(Rarity.UNCOMMON))),
			CROSSBOW_TAB);
	public static final DeferredHolder<Item, IncineratorItem> INCINERATOR = addToTab(
			ITEMS.register("incinerator", () -> new IncineratorItem(new Item.Properties()
					.durability(640)
					.rarity(Rarity.UNCOMMON))),
			CROSSBOW_TAB);
	public static final DeferredHolder<Item, QueenBeeCrossbowItem> QUEEN_BEE_CROSSBOW = addToTab(
			ITEMS.register("queen_bee_crossbow", () -> new QueenBeeCrossbowItem(new Item.Properties()
					.durability(1280)
					.rarity(Rarity.UNCOMMON))),
			CROSSBOW_TAB);
	public static final DeferredHolder<Item, PaladinItem> PALADIN = addToTab(
			ITEMS.register("paladin", () -> new PaladinItem(new Item.Properties()
					.durability(1280)
					.rarity(Rarity.UNCOMMON))),
			CROSSBOW_TAB);

	//Special Util
	public static final DeferredHolder<Item, Item> SCOURGE_PERK_TEXTURE_HOLDER =
			ITEMS.register("scourge_perk_texture_holder", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> NETHER_GUIDE_CRIMSON_TEXTURE_HOLDER =
			ITEMS.register("nether_guide_crimson_texture_holder", () -> new Item(new Item.Properties()));
}
