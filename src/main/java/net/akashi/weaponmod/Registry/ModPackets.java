package net.akashi.weaponmod.Registry;

import net.akashi.weaponmod.Network.ArrowVelocityUpdatePacket;
import net.akashi.weaponmod.Network.OutOfSightExplosionPacket;
import net.akashi.weaponmod.Network.SpearAttributeUpdatePacket;
import net.akashi.weaponmod.WeaponMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPackets {
	public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(WeaponMod.MODID, "main"),
			() -> "1.0",
			s -> true,
			s -> true
	);
	public static void registerPackets() {
		int id = 0;
		NETWORK.registerMessage(id++, SpearAttributeUpdatePacket.class,
				SpearAttributeUpdatePacket::write,
				SpearAttributeUpdatePacket::read,
				SpearAttributeUpdatePacket::handle);
		NETWORK.registerMessage(id++, ArrowVelocityUpdatePacket.class,
				ArrowVelocityUpdatePacket::write,
				ArrowVelocityUpdatePacket::read,
				ArrowVelocityUpdatePacket::handle);
		NETWORK.registerMessage(id++, OutOfSightExplosionPacket.class,
				OutOfSightExplosionPacket::write,
				OutOfSightExplosionPacket::read,
				OutOfSightExplosionPacket::handle);
	}
}
