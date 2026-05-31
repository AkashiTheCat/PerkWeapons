package net.akashi.perk_weapons.EventHandlers;

import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Spears.PiglinsWarSpearItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import static net.akashi.perk_weapons.Spears.PiglinsWarSpearItem.getPlayerArmorCount;
import static net.akashi.perk_weapons.Spears.PiglinsWarSpearItem.setArmorCount;

@EventBusSubscriber(modid = PerkWeapons.MODID, bus = EventBusSubscriber.Bus.GAME)
public class EquipmentChangeEventHandler {
	@SubscribeEvent
	public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
		Entity entity = event.getEntity();
		if (entity.getType() == EntityType.PLAYER && !entity.level().isClientSide()) {
			Player player = (Player) entity;
			handlePiglinsWarSpearCase(player);
		}
	}

	public static void handlePiglinsWarSpearCase(Player player) {
		ItemStack itemStack = player.getMainHandItem();
		if (!(itemStack.getItem() instanceof PiglinsWarSpearItem)) {
			itemStack = player.getOffhandItem();
		}
		if (itemStack.getItem() instanceof PiglinsWarSpearItem) {
			setArmorCount(itemStack, getPlayerArmorCount(player));
		}
	}
}
