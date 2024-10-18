package net.akashi.weaponmod.Config;

import net.akashi.weaponmod.Config.Properties.ConduitGuardProperties;
import net.akashi.weaponmod.Config.Properties.SpearProperties;
import net.akashi.weaponmod.Registry.ModItems;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.common.ForgeConfigSpec;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCommonConfigs {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static SpearProperties IRON_SPEAR_PROPERTIES;
	public static SpearProperties GOLDEN_SPEAR_PROPERTIES;
	public static SpearProperties DIAMOND_SPEAR_PROPERTIES;
	public static SpearProperties NETHERITE_SPEAR_PROPERTIES;
	public static SpearProperties MEGALODON_PROPERTIES;
	public static ConduitGuardProperties CONDUIT_GUARD_PROPERTIES;

	static {
		IRON_SPEAR_PROPERTIES = new SpearProperties(BUILDER, "Iron Spear",
				8, 1.1,
				7, 2.5F, true);
		GOLDEN_SPEAR_PROPERTIES = new SpearProperties(BUILDER, "Golden Spear",
				5, 1.6,
				6, 2.5F, true);
		DIAMOND_SPEAR_PROPERTIES = new SpearProperties(BUILDER, "Diamond Spear",
				9, 1.1,
				8, 2.5F, true);
		NETHERITE_SPEAR_PROPERTIES = new SpearProperties(BUILDER, "Netherite Spear",
				10, 1.1,
				9, 2.5F, true);
		MEGALODON_PROPERTIES = new SpearProperties(BUILDER, "Megalodon",
				9, 1.2,
				6, 2.5F, true);
		CONDUIT_GUARD_PROPERTIES = new ConduitGuardProperties(BUILDER, "Conduit Guard",
				9, 1.2,
				6, 2.5F,
				5.0F, 45,
				0.5, 80
		);
		SPEC = BUILDER.build();
	}

	@SubscribeEvent
	public static void onConfigLoad(ModConfigEvent event) {
		if (event.getConfig().getSpec() != SPEC)
			return;
		ModItems.IRON_SPEAR.get().updateFromConfig(IRON_SPEAR_PROPERTIES);
		ModItems.GOLDEN_SPEAR.get().updateFromConfig(GOLDEN_SPEAR_PROPERTIES);
		ModItems.DIAMOND_SPEAR.get().updateFromConfig(DIAMOND_SPEAR_PROPERTIES);
		ModItems.NETHERITE_SPEAR.get().updateFromConfig(NETHERITE_SPEAR_PROPERTIES);
		ModItems.SPEAR_MEGALODON.get().updateFromConfig(MEGALODON_PROPERTIES);
		ModItems.SPEAR_CONDUIT_GUARD.get().updateFromConfig(CONDUIT_GUARD_PROPERTIES);
	}
}
