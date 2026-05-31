package net.akashi.perk_weapons.EventHandlers;

import net.akashi.perk_weapons.Bows.ForestKeeperItem;
import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = PerkWeapons.MODID, bus = EventBusSubscriber.Bus.GAME)
public class LivingHurtEventHandler {
	@SubscribeEvent
	public static void onEntityHurt(LivingIncomingDamageEvent event) {
		LivingEntity entity = event.getEntity();
		if (!entity.level().isClientSide()) {
			ItemStack stack = ItemStack.EMPTY;
			if (entity.getMainHandItem().is(ModItems.FOREST_KEEPER.get())) {
				stack = entity.getMainHandItem();
			} else if (entity.getOffhandItem().is(ModItems.FOREST_KEEPER.get())) {
				stack = entity.getOffhandItem();
			}

			if (!stack.isEmpty()) {
				ForestKeeperItem item = (ForestKeeperItem) stack.getItem();
				item.setPerkLevel(stack, 0);
			}
		}
	}
}
