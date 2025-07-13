package net.akashi.perk_weapons.EventHandlers;

import net.akashi.perk_weapons.PerkWeapons;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PerkWeapons.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingTickEventHandler {
	@SubscribeEvent
	public static void onLivingTick(LivingEvent.LivingTickEvent event) {
		LivingEntity entity = event.getEntity();

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
