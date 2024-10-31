package net.akashi.perk_weapons.Config;

import net.akashi.perk_weapons.Client.Events.BowZoomHandler;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClientConfigs {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static ForgeConfigSpec.BooleanValue ENABLE_ZOOM;

	static {
		BUILDER.push("Client");
		ENABLE_ZOOM = BUILDER.comment("Enable Bow Aiming Zoom")
				.define("Zoom", true);
		SPEC = BUILDER.build();
	}

	@SubscribeEvent
	public static void onConfigLoad(ModConfigEvent event) {
		if (event.getConfig().getSpec() != SPEC)
			return;
		BowZoomHandler.setZoom(ENABLE_ZOOM.get());
	}
}
