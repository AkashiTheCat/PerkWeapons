package net.akashi.perk_weapons.Util;

import net.minecraft.sounds.SoundEvent;

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

	public SoundEventHolder(SoundEvent soundEvent, float volume, float pitch) {
		this.soundEvent = soundEvent;
		this.volume = volume;
		this.pitch = pitch;
	}

	public static SoundEventHolder empty() {
		return emptyHolder;
	}
}
