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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.minecraft.resources.ResourceLocation;

@EventBusSubscriber(modid = PerkWeapons.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
	@SubscribeEvent
	public static void registerRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntities.THROWN_SPEAR.get(), ThrownSpearRenderer::new);
		event.registerEntityRenderer(ModEntities.THROWN_MEGALODON.get(), ThrownSpearRenderer::new);
		event.registerEntityRenderer(ModEntities.THROWN_CONDUIT_GUARD.get(), ThrownSpearRenderer::new);
		event.registerEntityRenderer(ModEntities.THROWN_DRAGON_STRIKE.get(), ThrownSpearRenderer::new);
		event.registerEntityRenderer(ModEntities.THROWN_SCOURGE.get(), ThrownSpearRenderer::new);
		event.registerEntityRenderer(ModEntities.THROWN_NETHER_GUIDE.get(), ThrownSpearRenderer::new);
		event.registerEntityRenderer(ModEntities.THROWN_CENTURION.get(), ThrownSpearRenderer::new);

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
	public static void registerGuiLayers(RegisterGuiLayersEvent event) {
		event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(PerkWeapons.MODID, "perk_hud"), PerkIndicatorHud.INDICATOR_BAR);
		event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(PerkWeapons.MODID, "cooldown_hud"), CoolDownIndicatorHud.INDICATOR_BAR);
		event.registerAbove(VanillaGuiLayers.CROSSHAIR, ResourceLocation.fromNamespaceAndPath(PerkWeapons.MODID, "crosshair"), DoubleLineCrossHair.CROSSHAIR);
	}
}
