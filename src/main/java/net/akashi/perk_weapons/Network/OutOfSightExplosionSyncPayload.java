package net.akashi.perk_weapons.Network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.akashi.perk_weapons.PerkWeapons;
import org.jetbrains.annotations.NotNull;

public record OutOfSightExplosionSyncPayload(
    double x,
    double y,
    double z,
    int playerId
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
        ResourceLocation.fromNamespaceAndPath(PerkWeapons.MODID, "explosion_sync");
    public static final CustomPacketPayload.Type<OutOfSightExplosionSyncPayload> TYPE = new CustomPacketPayload.Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, OutOfSightExplosionSyncPayload> STREAM_CODEC =
        StreamCodec.composite(
            ByteBufCodecs.DOUBLE,
            OutOfSightExplosionSyncPayload::x,
            ByteBufCodecs.DOUBLE,
            OutOfSightExplosionSyncPayload::y,
            ByteBufCodecs.DOUBLE,
            OutOfSightExplosionSyncPayload::z,
            ByteBufCodecs.INT,
            OutOfSightExplosionSyncPayload::playerId,
            OutOfSightExplosionSyncPayload::new
        );

    @Override
    public CustomPacketPayload.@NotNull Type<OutOfSightExplosionSyncPayload> type() {
        return TYPE;
    }
}
