package net.akashi.perk_weapons.Client.Events;

import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;

@EventBusSubscriber(modid = PerkWeapons.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class MovementInputUpdateEventHandler {
	@SubscribeEvent
	public static void onMovementInputUpdate(MovementInputUpdateEvent event) {
		Player player = event.getEntity();
		if (!player.isUsingItem() || player.isPassenger()) {
			return;
		}

		ItemStack itemStack = player.getUseItem();
		if (itemStack.is(ModTags.NO_USING_SLOWDOWN_TAG)) {
			event.getInput().leftImpulse *= 5.0F;
			event.getInput().forwardImpulse *= 5.0F;
		}
	}
}
