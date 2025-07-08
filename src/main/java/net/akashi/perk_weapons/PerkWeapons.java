package net.akashi.perk_weapons;

import net.akashi.perk_weapons.Config.ModClientConfigs;
import net.akashi.perk_weapons.Config.ModCommonConfigs;
import net.akashi.perk_weapons.Registry.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(PerkWeapons.MODID)
public class PerkWeapons {
	public static final String MODID = "perk_weapons";

	public PerkWeapons() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		ModItems.ITEMS.register(bus);
		ModBlocks.BLOCKS.register(bus);
		ModBlockEntities.BLOCK_ENTITIES.register(bus);
		ModCreativeTabs.TABS.register(bus);
		ModEntities.ENTITIES.register(bus);
		ModEffects.EFFECTS.register(bus);
		ModEnchantments.ENCHANTMENTS.register(bus);
		ModAttributes.ATTRIBUTES.register(bus);
		ModSoundEvents.SOUND_EVENTS.register(bus);

		ModPackets.registerPackets();

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModCommonConfigs.SPEC,
				PerkWeapons.MODID + "-common.toml");
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ModClientConfigs.SPEC,
				PerkWeapons.MODID + "-client.toml");
	}
}
