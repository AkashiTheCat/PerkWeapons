package net.akashi.perk_weapons.Registry;

import net.akashi.perk_weapons.PerkWeapons;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModSoundEvents {
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
			DeferredRegister.create(Registries.SOUND_EVENT, PerkWeapons.MODID);

	public static final DeferredHolder<SoundEvent, SoundEvent> PALADIN_FIRE =
			registerSoundEvent("paladin_fire");

	private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
		ResourceLocation id = ResourceLocation.fromNamespaceAndPath(PerkWeapons.MODID, name);
		return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
	}
}
