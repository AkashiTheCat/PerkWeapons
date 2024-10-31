package net.akashi.perk_weapons.Registry;

import net.akashi.perk_weapons.Network.*;
import net.akashi.perk_weapons.PerkWeapons;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPackets {
	public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(PerkWeapons.MODID, "main"),
			() -> "1.0",
			s -> true,
			s -> true
	);
	public static void registerPackets() {
		int id = 0;
		NETWORK.registerMessage(id++, SpearAttributeUpdateSyncPacket.class,
				SpearAttributeUpdateSyncPacket::write,
				SpearAttributeUpdateSyncPacket::read,
				SpearAttributeUpdateSyncPacket::handle);
		NETWORK.registerMessage(id++, ArrowVelocitySyncPacket.class,
				ArrowVelocitySyncPacket::write,
				ArrowVelocitySyncPacket::read,
				ArrowVelocitySyncPacket::handle);
		NETWORK.registerMessage(id++, OutOfSightExplosionSyncPacket.class,
				OutOfSightExplosionSyncPacket::write,
				OutOfSightExplosionSyncPacket::read,
				OutOfSightExplosionSyncPacket::handle);
	}
}
