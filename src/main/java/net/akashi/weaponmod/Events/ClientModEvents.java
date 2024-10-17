package net.akashi.weaponmod.Events;

import net.akashi.weaponmod.Registry.ModEntities;
import net.akashi.weaponmod.WeaponMod;
import net.akashi.weaponmod.Client.Renderer.ThrownSpearRenderer;
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
	}
}
