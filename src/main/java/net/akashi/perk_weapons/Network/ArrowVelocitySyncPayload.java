package net.akashi.perk_weapons.Network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.akashi.perk_weapons.PerkWeapons;
import org.jetbrains.annotations.NotNull;

public record ArrowVelocitySyncPayload(
    double velocityX,
    double velocityY,
    double velocityZ,
    int arrowId
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
        ResourceLocation.fromNamespaceAndPath(PerkWeapons.MODID, "arrow_velocity_sync");
    public static final CustomPacketPayload.Type<ArrowVelocitySyncPayload> TYPE = new CustomPacketPayload.Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, ArrowVelocitySyncPayload> STREAM_CODEC =
        StreamCodec.composite(
            ByteBufCodecs.DOUBLE,
            ArrowVelocitySyncPayload::velocityX,
            ByteBufCodecs.DOUBLE,
            ArrowVelocitySyncPayload::velocityY,
            ByteBufCodecs.DOUBLE,
            ArrowVelocitySyncPayload::velocityZ,
            ByteBufCodecs.INT,
            ArrowVelocitySyncPayload::arrowId,
            ArrowVelocitySyncPayload::new
        );

    @Override
    public CustomPacketPayload.@NotNull Type<ArrowVelocitySyncPayload> type() {
        return TYPE;
    }
}
