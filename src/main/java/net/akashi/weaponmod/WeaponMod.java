package net.akashi.weaponmod;

import net.akashi.weaponmod.Config.ModCommonConfigs;
import net.akashi.weaponmod.Registry.ModCreativeTabs;
import net.akashi.weaponmod.Registry.ModEntities;
import net.akashi.weaponmod.Registry.ModItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(WeaponMod.MODID)
public class WeaponMod {
	public static final String MODID = "weaponmod";

	public WeaponMod() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		ModItems.ITEMS.register(bus);
		ModCreativeTabs.TABS.register(bus);
		ModEntities.ENTITIES.register(bus);

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModCommonConfigs.SPEC, WeaponMod.MODID + "-common.toml");
	}
}
