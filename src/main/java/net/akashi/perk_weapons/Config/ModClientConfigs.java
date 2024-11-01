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
	public static ForgeConfigSpec.BooleanValue ENABLE_CUSTOM_CROSSHAIR;
	public static ForgeConfigSpec.BooleanValue ENABLE_PERK_INDICATOR;
	public static ForgeConfigSpec.BooleanValue ENABLE_COOLDOWN_INDICATOR;

	static {
		BUILDER.push("Client");
		ENABLE_ZOOM = BUILDER.comment("Enable Bow Aiming Zoom")
				.define("Zoom", true);
		ENABLE_CUSTOM_CROSSHAIR = BUILDER.comment("Enable Custom CrossHairs")
				.define("CustomCrossHairs", true);
		ENABLE_PERK_INDICATOR = BUILDER.comment("Enable Perk Indicators")
				.define("EnablePerkIndicators", true);
		ENABLE_COOLDOWN_INDICATOR = BUILDER.comment("Enable Ability CoolDown Indicators")
				.define("EnableCoolDownIndicators", true);
		SPEC = BUILDER.build();
	}

	@SubscribeEvent
	public static void onConfigLoad(ModConfigEvent event) {
		if (event.getConfig().getSpec() != SPEC)
			return;
		BowZoomHandler.setZoom(ENABLE_ZOOM.get());
	}
}
