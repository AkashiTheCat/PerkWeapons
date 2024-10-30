package net.akashi.weaponmod.Registry;

import net.akashi.weaponmod.Bows.BaseBowItem;
import net.akashi.weaponmod.Bows.ForestKeeperItem;
import net.akashi.weaponmod.Bows.PurgatoryItem;
import net.akashi.weaponmod.Spears.*;
import net.akashi.weaponmod.WeaponMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.akashi.weaponmod.Registry.ModCreativeTabs.INGREDIENTS_TAB;
import static net.akashi.weaponmod.Registry.ModCreativeTabs.SPEAR_TAB;
import static net.akashi.weaponmod.Registry.ModCreativeTabs.BOW_TAB;
import static net.akashi.weaponmod.Registry.ModCreativeTabs.addToTab;

public class ModItems {
	//simple item registry
	public static final DeferredRegister<Item> ITEMS =
			DeferredRegister.create(ForgeRegistries.ITEMS, WeaponMod.MODID);
	public static final RegistryObject<Item> ANCIENT_SPIKE = addToTab(
			ITEMS.register("ancient_spike",
					() -> new Item(new Item.Properties().rarity(Rarity.RARE))), INGREDIENTS_TAB);
	public static final RegistryObject<Item> WITHER_SPINE = addToTab(
			ITEMS.register("wither_spine",
					() -> new Item(new Item.Properties().rarity(Rarity.EPIC))), INGREDIENTS_TAB);
	public static final RegistryObject<Item> GILDED_BLACKSTONE_HANDLE = addToTab(
			ITEMS.register("gilded_blackstone_handle",
					() -> new Item(new Item.Properties().rarity(Rarity.RARE))), INGREDIENTS_TAB);
	public static final RegistryObject<Item> PRISMARINE_FIN = addToTab(
			ITEMS.register("prismarine_fin",
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

	//Block item registry
	public static final RegistryObject<BlockItem> FURNACE_CORE = addToTab(
			ITEMS.register("furnace_core",
					() -> new BlockItem(ModBlocks.FURNACE_CORE.get(), new Item.Properties())), INGREDIENTS_TAB);

	//Spear item registry
	public static final RegistryObject<SpearItem> IRON_SPEAR = addToTab(
			ITEMS.register("iron_spear",
					() -> new SpearItem(8, 1.1f,
							7, 2.5F, false,
							new Item.Properties().durability(256))), SPEAR_TAB);
	public static final RegistryObject<SpearItem> GOLDEN_SPEAR = addToTab(
			ITEMS.register("golden_spear",
					() -> new SpearItem(5, 1.6f,
							6, 2.5F, false,
							new Item.Properties().durability(32))), SPEAR_TAB);
	public static final RegistryObject<SpearItem> DIAMOND_SPEAR = addToTab(
			ITEMS.register("diamond_spear",
					() -> new SpearItem(9, 1.1f,
							8, 2.5F, false,
							new Item.Properties().durability(640))), SPEAR_TAB);
	public static final RegistryObject<SpearItem> NETHERITE_SPEAR = addToTab(
			ITEMS.register("netherite_spear",
					() -> new SpearItem(10, 1.1f,
							9, 2.5F, false,
							new Item.Properties().durability(1280).fireResistant())), SPEAR_TAB);
	public static final RegistryObject<SpearItem> SPEAR_MEGALODON = addToTab(
			ITEMS.register("megalodon",
					() -> new MegalodonItem(9, 1.2f,
							6, 2.5F, true,
							new Item.Properties().durability(1280))), SPEAR_TAB);
	public static final RegistryObject<SpearItem> SPEAR_CONDUIT_GUARD = addToTab(
			ITEMS.register("conduit_guard",
					() -> new ConduitGuardItem(9, 1.1f,
							9, 2.5F, true,
							new Item.Properties().durability(1280))), SPEAR_TAB);
	public static final RegistryObject<SpearItem> PIGLINS_WARSPEAR = addToTab(
			ITEMS.register("piglins_warspear",
					() -> new PiglinsWarSpearItem(5, 1.2f,
							8, 2.5F, false,
							new Item.Properties().durability(640))), SPEAR_TAB);
	public static final RegistryObject<SpearItem> DRAGON_STRIKE = addToTab(
			ITEMS.register("dragon_strike",
					() -> new DragonStrikeItem(10, 1.2f,
							10, 2.5F, false,
							new Item.Properties().durability(1280).fireResistant())), SPEAR_TAB);
	public static final RegistryObject<SpearItem> SCOURGE = addToTab(
			ITEMS.register("scourge",
					() -> new ScourgeItem(10, 1.2f,
							10, 2.5F, false,
							new Item.Properties().durability(1280).fireResistant())), SPEAR_TAB);

	//Bow item registry
	public static final RegistryObject<BaseBowItem> SHORT_BOW = addToTab(
			ITEMS.register("short_bow",
					() -> new BaseBowItem(12, 8, 2.25F, 1.0F, 0.0F, 0.05F,
							new Item.Properties().durability(640))), BOW_TAB);
	public static final RegistryObject<BaseBowItem> LONGBOW = addToTab(
			ITEMS.register("longbow",
					() -> new BaseBowItem(40, 15, 4.5F, 0.2F, -0.5F, 0.15F,
							new Item.Properties().durability(320))), BOW_TAB);
	public static final RegistryObject<PurgatoryItem> PURGATORY = addToTab(
			ITEMS.register("purgatory",
					() -> new PurgatoryItem(50, 25, 4.5F, 0.2f, -1.0F, 1.0F, 0.15F,
							new Item.Properties().durability(640))), BOW_TAB);
	public static final RegistryObject<ForestKeeperItem> FOREST_KEEPER = addToTab(
			ITEMS.register("forest_keeper",
					() -> new ForestKeeperItem(12, 8, 2.25F, 1.0F, 0.0F, 0.0F,
							new Item.Properties().durability(1280))), BOW_TAB);
}
