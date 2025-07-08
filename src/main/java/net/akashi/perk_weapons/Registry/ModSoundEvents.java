package net.akashi.perk_weapons.Registry;

import net.akashi.perk_weapons.PerkWeapons;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSoundEvents {
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
			DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, PerkWeapons.MODID);

	public static final RegistryObject<SoundEvent> PALADIN_FIRE =
			registerSoundEvent("paladin_fire");

	private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
		ResourceLocation id = new ResourceLocation(PerkWeapons.MODID, name);
		return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
	}
}
