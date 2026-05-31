package net.akashi.perk_weapons.Network;

import net.akashi.perk_weapons.PerkWeapons;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber(modid = PerkWeapons.MODID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkEvents {

    @SubscribeEvent
    public static void registerPayloadHandlers(final RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");

        registrar.playToClient(
                ArrowVelocitySyncPayload.TYPE,
                ArrowVelocitySyncPayload.STREAM_CODEC,
                (payload, context) -> {
                    if (FMLEnvironment.dist == Dist.CLIENT) {
                        ClientPayloadHandlers.handle(payload, context);
                    }
                });

        registrar.playToClient(
                OutOfSightExplosionSyncPayload.TYPE,
                OutOfSightExplosionSyncPayload.STREAM_CODEC,
                (payload, context) -> {
                    if (FMLEnvironment.dist == Dist.CLIENT) {
                        ClientPayloadHandlers.handle(payload, context);
                    }
                });
    }
}
