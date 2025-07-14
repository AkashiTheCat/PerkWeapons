package net.akashi.perk_weapons.EventHandlers;

import net.akashi.perk_weapons.Bows.ForestKeeperItem;
import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PerkWeapons.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingHurtEventHandler {
	@SubscribeEvent
	public static void onEntityHurt(LivingHurtEvent event) {
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
