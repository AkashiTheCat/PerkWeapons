package net.akashi.perk_weapons.Network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandlers {
	public static void handle(ArrowVelocitySyncPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			Level level = Minecraft.getInstance().level;
			if (level == null) {
				return;
			}

			Entity arrow = level.getEntity(payload.arrowId());
			if (arrow != null) {
				arrow.setDeltaMovement(new Vec3(payload.velocityX(), payload.velocityY(), payload.velocityZ()));
			}
		}).exceptionally(ex -> {
			context.disconnect(Component.literal("packet handling error"));
			return null;
		});
	}

	public static void handle(OutOfSightExplosionSyncPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			Level level = Minecraft.getInstance().level;
			if (level == null) {
				return;
			}

			Entity entity = level.getEntity(payload.playerId());
			if (entity instanceof Player player) {
				level.addAlwaysVisibleParticle(
						ParticleTypes.EXPLOSION_EMITTER, true,
						payload.x(), payload.y(), payload.z(),
						0, 0, 0);
				player.playSound(SoundEvents.GENERIC_EXPLODE.value(), 0.1f, 0.7f);
			}
		}).exceptionally(ex -> {
			context.disconnect(Component.literal("packet handling error"));
			return null;
		});
	}
}
