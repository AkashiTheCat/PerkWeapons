package net.akashi.weaponmod.Registry;

import net.akashi.weaponmod.Network.SpearAttributeUpdatePacket;
import net.akashi.weaponmod.WeaponMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPackages {
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
	}
}
