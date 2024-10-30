package net.akashi.weaponmod.Network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ArrowVelocitySyncPacket {
	private final Vec3 DeltaMovement;
	private final int arrowID;

	public ArrowVelocitySyncPacket(Vec3 deltaMovement, int arrowID) {
		DeltaMovement = deltaMovement;
		this.arrowID = arrowID;
	}

	public static void write(ArrowVelocitySyncPacket packet, FriendlyByteBuf buf) {
		buf.writeDouble(packet.DeltaMovement.x);
		buf.writeDouble(packet.DeltaMovement.y);
		buf.writeDouble(packet.DeltaMovement.z);
		buf.writeInt(packet.arrowID);
	}

	public static ArrowVelocitySyncPacket read(FriendlyByteBuf buf) {
		return new ArrowVelocitySyncPacket(new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()),
				buf.readInt());
	}

	public static void handle(ArrowVelocitySyncPacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			Entity arrow = Minecraft.getInstance().level.getEntity(packet.arrowID);
			if (arrow != null) {
				arrow.setDeltaMovement(packet.DeltaMovement);
			}
		});
		context.get().setPacketHandled(true);
	}
}
