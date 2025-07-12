package net.akashi.perk_weapons.Client.Events;

import net.akashi.perk_weapons.Client.GUI.CoolDownIndicatorHud;
import net.akashi.perk_weapons.Client.GUI.DoubleLineCrossHair;
import net.akashi.perk_weapons.Client.GUI.PerkIndicatorHud;
import net.akashi.perk_weapons.Client.Renderer.BaseArrowRenderer;
import net.akashi.perk_weapons.Client.Renderer.BeholderBeamRenderer;
import net.akashi.perk_weapons.Client.Renderer.EndboreWandererPerkProjectileRenderer;
import net.akashi.perk_weapons.Client.Renderer.ThrownSpearRenderer;
import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PerkWeapons.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
	@SubscribeEvent
	public static void registerRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntities.THROWN_SPEAR.get(), ThrownSpearRenderer::new);
		event.registerEntityRenderer(ModEntities.THROWN_MEGALODON.get(), ThrownSpearRenderer::new);
		event.registerEntityRenderer(ModEntities.THROWN_CONDUIT_GUARD.get(), ThrownSpearRenderer::new);
		event.registerEntityRenderer(ModEntities.THROWN_DRAGON_STRIKE.get(), ThrownSpearRenderer::new);
		event.registerEntityRenderer(ModEntities.THROWN_SCOURGE.get(), ThrownSpearRenderer::new);
		event.registerEntityRenderer(ModEntities.THROWN_NETHER_GUIDE.get(), ThrownSpearRenderer::new);

		event.registerEntityRenderer(ModEntities.BASE_ARROW.get(), BaseArrowRenderer::new);
		event.registerEntityRenderer(ModEntities.PURGATORY_ARROW.get(), BaseArrowRenderer::new);
		event.registerEntityRenderer(ModEntities.PERK_GAINING_ARROW.get(), BaseArrowRenderer::new);
		event.registerEntityRenderer(ModEntities.EXPLOSIVE_ARROW.get(), BaseArrowRenderer::new);
		event.registerEntityRenderer(ModEntities.FROST_HUNTER_ARROW.get(), BaseArrowRenderer::new);
		event.registerEntityRenderer(ModEntities.STAR_SHOOTER_ARROW.get(), BaseArrowRenderer::new);
		event.registerEntityRenderer(ModEntities.DEVOURER_ARROW.get(), BaseArrowRenderer::new);
		event.registerEntityRenderer(ModEntities.INCINERATOR_ARROW.get(), BaseArrowRenderer::new);
		event.registerEntityRenderer(ModEntities.QUEEN_BEE_ARROW.get(), BaseArrowRenderer::new);
		event.registerEntityRenderer(ModEntities.PALADIN_ARROW.get(), BaseArrowRenderer::new);
		event.registerEntityRenderer(ModEntities.ENDBORE_WANDERER_ARROW.get(), BaseArrowRenderer::new);

		event.registerEntityRenderer(ModEntities.BEHOLDER_BEAM_SRC.get(), BeholderBeamRenderer::new);
		event.registerEntityRenderer(ModEntities.ENDBORE_WANDERER_PERK_PROJECTILE.get(), EndboreWandererPerkProjectileRenderer::new);
	}

	@SubscribeEvent
	public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
		event.registerAboveAll("perk_hud", PerkIndicatorHud.INDICATOR_BAR);
		event.registerAboveAll("cooldown_hud", CoolDownIndicatorHud.INDICATOR_BAR);
		event.registerAbove(VanillaGuiOverlay.CROSSHAIR.id(), "crosshair", DoubleLineCrossHair.CROSSHAIR);
	}
}
