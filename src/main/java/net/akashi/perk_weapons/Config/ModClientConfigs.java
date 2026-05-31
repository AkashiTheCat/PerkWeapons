package net.akashi.perk_weapons.Config;

import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Client.Events.BowZoomHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = PerkWeapons.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientConfigs {
	public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
	public static final ModConfigSpec SPEC;
	public static ModConfigSpec.BooleanValue ENABLE_ZOOM;
	public static ModConfigSpec.BooleanValue ENABLE_CUSTOM_CROSSHAIR;
	public static ModConfigSpec.BooleanValue ENABLE_PERK_INDICATOR;
	public static ModConfigSpec.BooleanValue ENABLE_COOLDOWN_INDICATOR;
	public static ModConfigSpec.BooleanValue ENABLE_MODE_INDICATOR_ON_NETHER_GUIDE;

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
		ENABLE_MODE_INDICATOR_ON_NETHER_GUIDE = BUILDER.comment("Enable Nether Guide Mode Indicator Below Crosshair")
				.define("EnableNetherGuideIndicator", false);
		SPEC = BUILDER.build();
	}

	@SubscribeEvent
	public static void onConfigLoad(ModConfigEvent event) {
		if (event.getConfig().getSpec() != SPEC)
			return;

		BowZoomHandler.setZoom(ENABLE_ZOOM.get());
	}
}
