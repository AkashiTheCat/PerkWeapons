package net.akashi.perk_weapons.EventHandlers;

import net.akashi.perk_weapons.Crossbows.BaseCrossbowItem;
import net.akashi.perk_weapons.PerkWeapons;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.Map;
import java.util.WeakHashMap;

@EventBusSubscriber(modid = PerkWeapons.MODID, bus = EventBusSubscriber.Bus.GAME)
public class CrossbowSwingEventHandler {
    private static final Map<Player, Boolean> lastSwingState = new WeakHashMap<>();

    private static boolean detectCurrentSwinging(LivingEntity entity) {
        return entity.swinging && entity.swingTime == 0;
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        boolean currentSwing = detectCurrentSwinging(player);
        boolean prev = lastSwingState.getOrDefault(player, false);

        // detect swing start (transition false -> true)
        if (currentSwing && !prev) {
            // on swing start
            if (player.isCrouching()) {
                ItemStack stack = player.getMainHandItem();
                if (!stack.isEmpty() && stack.getItem() instanceof BaseCrossbowItem crossbow) {
                    Level level = player.level();
                    var ammoList = crossbow.getChargedProjectiles(level, stack);
                    for (ItemStack ammo : ammoList) {
                        if (!player.addItem(ammo.copy())) {
                            level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(), ammo.copy()));
                        }
                    }
                    crossbow.clearChargedProjectiles(stack);
                    crossbow.setCrossbowCharged(stack, false);
                }
            }
        }

        lastSwingState.put(player, currentSwing);
    }
}
