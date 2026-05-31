package net.akashi.perk_weapons.Util;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.Holder;

public class SoundEventHolder {
	private static final SoundEventHolder emptyHolder = new SoundEventHolder();

	public SoundEvent soundEvent;
	public float volume = 1;
	public float pitch = 1;

	public SoundEventHolder() {
	}

	public SoundEventHolder(SoundEvent soundEvent) {
		this.soundEvent = soundEvent;
	}

	public SoundEventHolder(Holder<SoundEvent> soundEventHolder) {
		this.soundEvent = soundEventHolder.value();
	}

	public SoundEventHolder(SoundEvent soundEvent, float volume, float pitch) {
		this.soundEvent = soundEvent;
		this.volume = volume;
		this.pitch = pitch;
	}

	public SoundEventHolder(Holder<SoundEvent> soundEventHolder, float volume, float pitch) {
		this.soundEvent = soundEventHolder.value();
		this.volume = volume;
		this.pitch = pitch;
	}

	public static SoundEventHolder empty() {
		return emptyHolder;
	}
}
