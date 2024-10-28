package net.akashi.weaponmod.Network;

import net.akashi.weaponmod.Spears.PiglinsWarSpearItem;
import net.akashi.weaponmod.Spears.SpearItem;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SpearAttributeUpdatePacket {
	private final int playerId;
	private final float damageMultiplier;
	private final float speedMultiplier;

	public SpearAttributeUpdatePacket(int playerId, float damageMultiplier, float speedMultiplier) {
		this.playerId = playerId;
		this.damageMultiplier = damageMultiplier;
		this.speedMultiplier = speedMultiplier;
	}

	public static void write(SpearAttributeUpdatePacket msg, FriendlyByteBuf buffer) {
		buffer.writeInt(msg.playerId);
		buffer.writeFloat(msg.damageMultiplier);
		buffer.writeFloat(msg.speedMultiplier);
	}

	public static SpearAttributeUpdatePacket read(FriendlyByteBuf buffer) {
		return new SpearAttributeUpdatePacket(buffer.readInt(), buffer.readFloat(), buffer.readFloat());
	}

	public static void handle(SpearAttributeUpdatePacket msg, Supplier<NetworkEvent.Context> ctx) {
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