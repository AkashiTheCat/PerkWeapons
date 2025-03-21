package net.akashi.perk_weapons.Registry;

import net.akashi.perk_weapons.Bows.*;
import net.akashi.perk_weapons.Crossbows.*;
import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Spears.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.akashi.perk_weapons.Registry.ModCreativeTabs.INGREDIENTS_TAB;
import static net.akashi.perk_weapons.Registry.ModCreativeTabs.SPEAR_TAB;
import static net.akashi.perk_weapons.Registry.ModCreativeTabs.BOW_TAB;
import static net.akashi.perk_weapons.Registry.ModCreativeTabs.addToTab;

public class ModItems {
	//Simple items
	public static final DeferredRegister<Item> ITEMS =
			DeferredRegister.create(ForgeRegistries.ITEMS, PerkWeapons.MODID);
	public static final RegistryObject<Item> ANCIENT_SPIKE = addToTab(
			ITEMS.register("ancient_spike",
					() -> new Item(new Item.Properties().rarity(Rarity.RARE))), INGREDIENTS_TAB);
	public static final RegistryObject<Item> WITHER_SPINE = addToTab(
			ITEMS.register("wither_spine",
					() -> new Item(new Item.Properties().rarity(Rarity.EPIC))), INGREDIENTS_TAB);
	public static final RegistryObject<Item> GILDED_BLACKSTONE_HANDLE = addToTab(
			ITEMS.register("gilded_blackstone_handle",
					() -> new Item(new Item.Properties().rarity(Rarity.RARE))), INGREDIENTS_TAB);
	public static final RegistryObject<Item> GUARDIANS_EYE = addToTab(
			ITEMS.register("guardians_eye",
					() -> new Item(new Item.Properties().rarity(Rarity.RARE))), INGREDIENTS_TAB);
	public static final RegistryObject<Item> DRAGON_FANG = addToTab(
			ITEMS.register("dragon_fang",
					() -> new Item(new Item.Properties().rarity(Rarity.EPIC))), INGREDIENTS_TAB);
	public static final RegistryObject<Item> HUNTERS_REMAINS = addToTab(
			ITEMS.register("hunters_remains",
					() -> new Item(new Item.Properties().rarity(Rarity.RARE))), INGREDIENTS_TAB);
	public static final RegistryObject<Item> TOTEM_OF_EVOKING = addToTab(
			ITEMS.register("totem_of_evoking",
					() -> new Item(new Item.Properties().rarity(Rarity.EPIC))), INGREDIENTS_TAB);

	public static final RegistryObject<Item> REPAIRER = addToTab(
			ITEMS.register("repairer", () -> new Item(new Item.Properties()
					.rarity(Rarity.UNCOMMON).stacksTo(16))), INGREDIENTS_TAB);

	//Blocks
	public static final RegistryObject<BlockItem> FURNACE_CORE = addToTab(
			ITEMS.register("furnace_core", () -> new BlockItem(ModBlocks.FURNACE_CORE.get(),
					new Item.Properties().rarity(Rarity.UNCOMMON))), INGREDIENTS_TAB);

	//Spears
	public static final RegistryObject<BaseSpearItem> IRON_SPEAR = addToTab(
			ITEMS.register("iron_spear",
					() -> new BaseSpearItem(8, 1.1f,
							7, 2.5F, false,
							new Item.Properties().durability(256))), SPEAR_TAB);
	public static final RegistryObject<BaseSpearItem> GOLDEN_SPEAR = addToTab(
			ITEMS.register("golden_spear",
					() -> new BaseSpearItem(5, 1.6f,
							6, 2.5F, false,
							new Item.Properties().durability(32))), SPEAR_TAB);
	public static final RegistryObject<BaseSpearItem> DIAMOND_SPEAR = addToTab(
			ITEMS.register("diamond_spear",
					() -> new BaseSpearItem(9, 1.1f,
							8, 2.5F, false,
							new Item.Properties().durability(640))), SPEAR_TAB);
	public static final RegistryObject<BaseSpearItem> NETHERITE_SPEAR = addToTab(
			ITEMS.register("netherite_spear",
					() -> new BaseSpearItem(10, 1.1f,
							9, 2.5F, false,
							new Item.Properties().durability(1280).fireResistant())), SPEAR_TAB);
	public static final RegistryObject<BaseSpearItem> SPEAR_MEGALODON = addToTab(
			ITEMS.register("megalodon",
					() -> new MegalodonItem(9, 1.2f,
							6, 2.5F, true,
							new Item.Properties().durability(1280).rarity(Rarity.UNCOMMON))), SPEAR_TAB);
	public static final RegistryObject<BaseSpearItem> SPEAR_CONDUIT_GUARD = addToTab(
			ITEMS.register("conduit_guard",
					() -> new ConduitGuardItem(9, 1.1f,
							9, 2.5F, true,
							new Item.Properties().durability(1280).rarity(Rarity.UNCOMMON))), SPEAR_TAB);
	public static final RegistryObject<BaseSpearItem> PIGLINS_WARSPEAR = addToTab(
			ITEMS.register("piglins_warspear",
					() -> new PiglinsWarSpearItem(5, 1.2f,
							8, 2.5F, false,
							new Item.Properties().durability(640).rarity(Rarity.UNCOMMON))), SPEAR_TAB);
	public static final RegistryObject<BaseSpearItem> DRAGON_STRIKE = addToTab(
			ITEMS.register("dragon_strike", () -> new DragonStrikeItem(10, 1.2f,
					10, 2.5F, false,
					new Item.Properties().durability(1280)
							.fireResistant().rarity(Rarity.UNCOMMON))), SPEAR_TAB);
	public static final RegistryObject<BaseSpearItem> SCOURGE = addToTab(
			ITEMS.register("scourge", () -> new ScourgeItem(10, 1.2f,
					10, 2.5F, false,
					new Item.Properties().durability(1280)
							.fireResistant().rarity(Rarity.UNCOMMON))),
			SPEAR_TAB);

	//Bows
	public static final RegistryObject<BaseBowItem> SHORT_BOW = addToTab(
			ITEMS.register("short_bow",
					() -> new BaseBowItem(12, 8, 2.25F, 1.0F,
							0.0F, 0.05F, false,
							new Item.Properties().durability(640))), BOW_TAB);
	public static final RegistryObject<BaseBowItem> LONGBOW = addToTab(
			ITEMS.register("longbow",
					() -> new BaseBowItem(40, 15, 4.5F, 0.2F,
							-0.5F, 0.15F, false,
							new Item.Properties().durability(320))), BOW_TAB);
	public static final RegistryObject<PurgatoryItem> PURGATORY = addToTab(
			ITEMS.register("purgatory",
					() -> new PurgatoryItem(50, 25, 4.5F, 0.2F,
							-1.0F, 1.0F, 0.15F, true,
							new Item.Properties().rarity(Rarity.UNCOMMON))), BOW_TAB);
	public static final RegistryObject<ForestKeeperItem> FOREST_KEEPER = addToTab(
			ITEMS.register("forest_keeper",
					() -> new ForestKeeperItem(12, 8, 2.25F, 1.0F,
							0.0F, 0.0F, true,
							new Item.Properties().durability(1280).rarity(Rarity.UNCOMMON))), BOW_TAB);

	public static final RegistryObject<ElfsHarpItem> ELFS_HARP = addToTab(
			ITEMS.register("elfs_harp",
					() -> new ElfsHarpItem(20, 10, 3.0F, 0.8F,
							0.0F, 0.1F, true,
							new Item.Properties().durability(960).rarity(Rarity.UNCOMMON))), BOW_TAB);
	public static final RegistryObject<FrostHunterItem> FROST_HUNTER = addToTab(
			ITEMS.register("frost_hunter",
					() -> new FrostHunterItem(24, 9, 3.0F, 0.8F,
							0.0F, 0.1F, true,
							new Item.Properties().durability(960).rarity(Rarity.UNCOMMON))), BOW_TAB);
	public static final RegistryObject<HouYiItem> HOU_YI = addToTab(
			ITEMS.register("hou_yi",
					() -> new HouYiItem(40, 15, 4.5F, 0.2F,
							-0.5F, 0.15F, true,
							new Item.Properties().durability(640).rarity(Rarity.UNCOMMON))), BOW_TAB);
	public static final RegistryObject<DevourerItem> DEVOURER = addToTab(
			ITEMS.register("devourer",
					() -> new DevourerItem(20, 7, 3.0F, 0.8F,
							0.0F, 0.1F, true,
							new Item.Properties().durability(960).rarity(Rarity.UNCOMMON))), BOW_TAB);

	//Crossbows
	public static final RegistryObject<BeholderItem> OPPRESSOR = addToTab(
			ITEMS.register("beholder",
					() -> new BeholderItem(37, 12, 4.4F,
							0.5F, -0.5F, true,
							new Item.Properties().durability(640).rarity(Rarity.UNCOMMON))), BOW_TAB);
	public static final RegistryObject<LiberatorItem> LIBERATOR = addToTab(
			ITEMS.register("liberator",
					() -> new LiberatorItem(50, 10, 2.4F,
							1.0F, 0.0F, false,
							new Item.Properties().durability(640).rarity(Rarity.UNCOMMON))), BOW_TAB);
	public static final RegistryObject<TaintedFortuneItem> TAINTED_FORTUNE = addToTab(
			ITEMS.register("tainted_fortune",
					() -> new TaintedFortuneItem(25, 8, 2.0F,
							1.0F, 0.0F, false,
							new Item.Properties().durability(1280).rarity(Rarity.UNCOMMON))), BOW_TAB);
	public static final RegistryObject<SonicBlasterItem> SONIC_BLASTER = addToTab(
			ITEMS.register("sonic_blaster",
					() -> new SonicBlasterItem(37, 20, -1F,
							0.0F, -0.5F, true,
							new Item.Properties().durability(640).rarity(Rarity.UNCOMMON))), BOW_TAB);
	public static final RegistryObject<IncineratorItem> INCINERATOR = addToTab(
			ITEMS.register("incinerator",
					() -> new IncineratorItem(80, 11, 3.2F,
							0.8F, -0.25F, true,
							new Item.Properties().rarity(Rarity.UNCOMMON))), BOW_TAB);

}
