package net.akashi.perk_weapons.EventHandlers;

import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = PerkWeapons.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerTickEventHandler {
	private static final Field foodDataTickTimerField;

	static {
		try {
			foodDataTickTimerField = FoodData.class.getDeclaredField("tickTimer");
			foodDataTickTimerField.setAccessible(true);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	private static int getTickTimer(FoodData foodData) throws IllegalAccessException {
		return foodDataTickTimerField.getInt(foodData);
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) throws IllegalAccessException {
		if (event.phase == TickEvent.Phase.END)
			return;

		Player player = event.player;
		if (player.hasEffect(ModEffects.PHALANX.get())) {
			FoodData foodData = player.getFoodData();
			int tickTimer = getTickTimer(foodData);
			if (foodData.getFoodLevel() == 20 && tickTimer == 10) {
				player.heal(Math.min(1.0F, foodData.getSaturationLevel() / 6));
				return;
			}
			if (foodData.getFoodLevel() >= 18 && tickTimer == 80) {
				player.heal(1.0F);
			}
		}
	}
}
