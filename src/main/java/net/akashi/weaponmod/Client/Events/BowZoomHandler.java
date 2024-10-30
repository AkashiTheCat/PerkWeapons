package net.akashi.weaponmod.Client.Events;

import net.akashi.weaponmod.Bows.BaseBowItem;
import net.akashi.weaponmod.WeaponMod;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WeaponMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class BowZoomHandler {
	private static double previousZoomFactor = 1.0;
	private static boolean enableZoom = true;

	@SubscribeEvent
	public static void onFovUpdate(ViewportEvent.ComputeFov event) {
		if (!enableZoom) {
			return;
		}
		Player player = Minecraft.getInstance().player;
		if (player != null) {
			ItemStack item = player.getUseItem();
			if (player.isUsingItem() && item.getItem() instanceof BaseBowItem bow) {
				float drawProgress = bow.getDrawProgress(player);
				double targetZoomFactor = Math.cos(drawProgress * Math.PI) * bow.ZOOM_FACTOR + 1 - bow.ZOOM_FACTOR;
				double smoothedZoomFactor = lerp(0.1, previousZoomFactor, targetZoomFactor);
				previousZoomFactor = smoothedZoomFactor;
				event.setFOV(event.getFOV() * smoothedZoomFactor);
			} else {
				double smoothedZoomFactor = lerp(0.1, previousZoomFactor, 1.0);
				previousZoomFactor = smoothedZoomFactor;
				event.setFOV(event.getFOV() * smoothedZoomFactor);
			}
		}
	}

	public static void setZoom(boolean enable) {
		enableZoom = enable;
	}

	private static double lerp(double delta, double start, double end) {
		return start + delta * (end - start);
	}
}
