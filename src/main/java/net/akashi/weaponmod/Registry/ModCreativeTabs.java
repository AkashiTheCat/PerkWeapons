package net.akashi.weaponmod.Registry;

import net.akashi.weaponmod.WeaponMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = WeaponMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModCreativeTabs {
	public static final DeferredRegister<CreativeModeTab> TABS
			= DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WeaponMod.MODID);
	public static final List<Supplier<? extends ItemLike>> INGREDIENTS_TAB = new ArrayList<>();
	public static final List<Supplier<? extends ItemLike>> SPEAR_TAB = new ArrayList<>();
	public static final List<Supplier<? extends ItemLike>> BOW_TAB = new ArrayList<>();
	private static final RegistryObject<CreativeModeTab> ACTUAL_INGREDIENTS_TAB = TABS.register("ingredients_tab",
			() -> CreativeModeTab.builder()
					.title(Component.translatable(WeaponMod.MODID+"itemGroup.ingredients_tab"))
					.icon(ModItems.ANCIENT_SPIKE.get()::getDefaultInstance)
					.displayItems((displayParams, output) ->
							INGREDIENTS_TAB.forEach(itemLike -> output.accept(itemLike.get())))
					.build()
	);
	private static final RegistryObject<CreativeModeTab> ACTUAL_SPEAR_TAB = TABS.register("spear_tab",
			() -> CreativeModeTab.builder()
					.title(Component.translatable(WeaponMod.MODID+"itemGroup.spear_tab"))
					.icon(ModItems.PLACEHOLDER.get()::getDefaultInstance)
					.displayItems((displayParams, output) ->
							SPEAR_TAB.forEach(itemLike -> output.accept(itemLike.get())))
					.build()
	);
	private static final RegistryObject<CreativeModeTab> ACTUAL_BOW_TAB = TABS.register("bow_tab",
			() -> CreativeModeTab.builder()
					.title(Component.translatable(WeaponMod.MODID+"itemGroup.bow_tab"))
					.icon(ModItems.PLACEHOLDER.get()::getDefaultInstance)
					.displayItems((displayParams, output) ->
							BOW_TAB.forEach(itemLike -> output.accept(itemLike.get())))
					.build()
	);

	public static <T extends Item> RegistryObject<T> addToTab(
			RegistryObject<T> itemLike,
			List<Supplier<? extends ItemLike>> TAB) {
		TAB.add(itemLike);
		return itemLike;
	}
}
