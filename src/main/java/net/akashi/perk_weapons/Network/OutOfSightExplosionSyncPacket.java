package net.akashi.perk_weapons.Network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OutOfSightExplosionSyncPacket {
	private final double x;
	private final double y;
	private final double z;
	private final int PlayerID;

	public OutOfSightExplosionSyncPacket(double x, double y, double z, int PlayerID) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.PlayerID = PlayerID;
	}

	public static void write(OutOfSightExplosionSyncPacket packet, FriendlyByteBuf buf) {
		buf.writeDouble(packet.x);
		buf.writeDouble(packet.y);
		buf.writeDouble(packet.z);
		buf.writeInt(packet.PlayerID);
	}

	public static OutOfSightExplosionSyncPacket read(FriendlyByteBuf buf) {
		return new OutOfSightExplosionSyncPacket(buf.readDouble(), buf.readDouble(), buf.readDouble(),
				buf.readInt());
	}

	@OnlyIn(Dist.CLIENT)
	public static void handle(OutOfSightExplosionSyncPacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			if (context.get().getDirection().getOriginationSide().isClient())
				return;

			Level level = Minecraft.getInstance().level;
			if (level == null) {
				return;
			}
			level.addAlwaysVisibleParticle(ParticleTypes.EXPLOSION_EMITTER, true,
					packet.x, packet.y, packet.z, 0, 0, 0);

			Entity entity = level.getEntity(packet.PlayerID);
			if (entity instanceof Player player) {
				player.playSound(SoundEvents.GENERIC_EXPLODE, 0.1f, 0.7f);
			}
		});
		context.get().setPacketHandled(true);
	}
}
