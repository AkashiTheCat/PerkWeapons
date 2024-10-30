package net.akashi.weaponmod.Client.Events;

import net.akashi.weaponmod.Client.Renderer.BaseArrowRenderer;
import net.akashi.weaponmod.Client.Renderer.ExplosiveArrowRenderer;
import net.akashi.weaponmod.Client.Renderer.ThrownSpearRenderer;
import net.akashi.weaponmod.Registry.ModEntities;
import net.akashi.weaponmod.WeaponMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WeaponMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
	@SubscribeEvent
	public static void registerRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntities.THROWN_SPEAR.get(), ThrownSpearRenderer::new);
		event.registerEntityRenderer(ModEntities.THROWN_MEGALODON.get(), ThrownSpearRenderer::new);
		event.registerEntityRenderer(ModEntities.THROWN_CONDUIT_GUARD.get(), ThrownSpearRenderer::new);
		event.registerEntityRenderer(ModEntities.THROWN_DRAGON_STRIKE.get(), ThrownSpearRenderer::new);
		event.registerEntityRenderer(ModEntities.THROWN_SCOURGE.get(), ThrownSpearRenderer::new);

		event.registerEntityRenderer(ModEntities.BASE_ARROW.get(), BaseArrowRenderer::new);
		event.registerEntityRenderer(ModEntities.PURGATORY_ARROW.get(), BaseArrowRenderer::new);
		event.registerEntityRenderer(ModEntities.EXPLOSIVE_ARROW.get(), ExplosiveArrowRenderer::new);
	}
}
