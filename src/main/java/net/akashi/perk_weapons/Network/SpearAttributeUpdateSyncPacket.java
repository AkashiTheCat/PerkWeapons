package net.akashi.perk_weapons.Network;

import net.akashi.perk_weapons.Spears.SpearItem;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SpearAttributeUpdateSyncPacket {
	private final int playerId;
	private final float damageMultiplier;
	private final float speedMultiplier;

	public SpearAttributeUpdateSyncPacket(int playerId, float damageMultiplier, float speedMultiplier) {
		this.playerId = playerId;
		this.damageMultiplier = damageMultiplier;
		this.speedMultiplier = speedMultiplier;
	}

	public static void write(SpearAttributeUpdateSyncPacket msg, FriendlyByteBuf buffer) {
		buffer.writeInt(msg.playerId);
		buffer.writeFloat(msg.damageMultiplier);
		buffer.writeFloat(msg.speedMultiplier);
	}

	public static SpearAttributeUpdateSyncPacket read(FriendlyByteBuf buffer) {
		return new SpearAttributeUpdateSyncPacket(buffer.readInt(), buffer.readFloat(), buffer.readFloat());
	}

	public static void handle(SpearAttributeUpdateSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = (Player) Minecraft.getInstance().level.getEntity(msg.playerId);
			if (player != null && !(player instanceof ServerPlayer)) {
				if (player.getMainHandItem().getItem() instanceof SpearItem item) {
					item.updateAttributes(msg.damageMultiplier, msg.speedMultiplier);
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}
}