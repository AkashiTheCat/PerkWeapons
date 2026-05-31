package net.akashi.perk_weapons.EventHandlers;

import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModEffects;
import net.akashi.perk_weapons.mixin.FoodDataAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = PerkWeapons.MODID, bus = EventBusSubscriber.Bus.GAME)
public class PlayerTickEventHandler {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		if (player.level().isClientSide())
			return;
		if (player.hasEffect(ModEffects.PHALANX)) {
			FoodData foodData = player.getFoodData();
			int tickTimer = ((FoodDataAccessor) foodData).perk_weapons$getTickTimer();
			if (foodData.getFoodLevel() == 20 && tickTimer == 9) {
				player.heal(Math.min(1.0F, foodData.getSaturationLevel() / 6));
				return;
			}
			if (foodData.getFoodLevel() >= 18 && tickTimer == 79) {
				player.heal(1.0F);
			}
		}
	}
}
