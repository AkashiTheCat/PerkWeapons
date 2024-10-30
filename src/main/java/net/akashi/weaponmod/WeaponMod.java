package net.akashi.weaponmod;

import net.akashi.weaponmod.Config.ModClientConfigs;
import net.akashi.weaponmod.Config.ModCommonConfigs;
import net.akashi.weaponmod.Registry.*;
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
		ModBlocks.BLOCKS.register(bus);
		ModBlockEntities.BLOCK_ENTITIES.register(bus);
		ModCreativeTabs.TABS.register(bus);
		ModEntities.ENTITIES.register(bus);
		ModEffects.EFFECTS.register(bus);
		ModEnchantments.ENCHANTMENTS.register(bus);

		ModPackets.registerPackets();

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModCommonConfigs.SPEC, WeaponMod.MODID + "-common.toml");
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ModClientConfigs.SPEC, WeaponMod.MODID + "-client.toml");
	}
}
