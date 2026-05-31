package net.akashi.perk_weapons;

import net.akashi.perk_weapons.Config.ModClientConfigs;
import net.akashi.perk_weapons.Config.ModCommonConfigs;
import net.akashi.perk_weapons.Registry.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(PerkWeapons.MODID)
public class PerkWeapons {
	public static final String MODID = "perk_weapons";

	public PerkWeapons(IEventBus bus, ModContainer modContainer) {

		ModItems.ITEMS.register(bus);
		ModBlocks.BLOCKS.register(bus);
		ModBlockEntities.BLOCK_ENTITIES.register(bus);
		ModCreativeTabs.TABS.register(bus);
		ModEntities.ENTITIES.register(bus);
		ModEffects.EFFECTS.register(bus);
		ModAttributes.ATTRIBUTES.register(bus);
		ModSoundEvents.SOUND_EVENTS.register(bus);

		modContainer.registerConfig(ModConfig.Type.CLIENT, ModClientConfigs.SPEC);
		modContainer.registerConfig(ModConfig.Type.COMMON, ModCommonConfigs.SPEC);
	}
}
