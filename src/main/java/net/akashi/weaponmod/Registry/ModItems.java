package net.akashi.weaponmod.Registry;

import net.akashi.weaponmod.Entities.Projectiles.ThrownSpear;
import net.akashi.weaponmod.Spears.ConduitGuardItem;
import net.akashi.weaponmod.Spears.MegalodonItem;
import net.akashi.weaponmod.Spears.SpearItem;
import net.akashi.weaponmod.WeaponMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
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
	public static final RegistryObject<Item> PLACEHOLDER = ITEMS.register("placeholder",
			() -> new Item(new Item.Properties()));
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
	public static final RegistryObject<Item> FURNACE_CORE = addToTab(
			ITEMS.register("furnace_core",
					() -> new Item(new Item.Properties())), INGREDIENTS_TAB);
	public static final RegistryObject<Item> TOTEM_OF_EVOKING = addToTab(
			ITEMS.register("totem_of_evoking",
					() -> new Item(new Item.Properties().rarity(Rarity.EPIC))), INGREDIENTS_TAB);

	//spear item registry
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

}
