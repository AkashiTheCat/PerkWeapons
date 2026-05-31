package net.akashi.perk_weapons.EventHandlers;

import net.akashi.perk_weapons.PerkWeapons;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = PerkWeapons.MODID, bus = EventBusSubscriber.Bus.GAME)
public class LivingTickEventHandler {
	@SubscribeEvent
	public static void onLivingTick(EntityTickEvent.Post event) {
		if (!(event.getEntity() instanceof LivingEntity entity)) {
			return;
		}

		if (entity.getType() != EntityType.WOLF || entity.level().isClientSide)
			return;

		Wolf wolf  = (Wolf) entity;
		CompoundTag data = wolf.getPersistentData();

		if (data.contains("DespawnTick")) {
			int ticksLeft = data.getInt("DespawnTick");
			if (ticksLeft <= 0) {
				wolf.discard();
			} else {
				data.putInt("DespawnTick", ticksLeft - 1);
			}
		}
	}
}
