package net.akashi.perk_weapons.Registry;

import net.akashi.perk_weapons.Effects.HarmAllEffect;
import net.akashi.perk_weapons.Effects.InternalExplosionEffect;
import net.akashi.perk_weapons.Effects.PhalanxEffect;
import net.akashi.perk_weapons.Effects.RoyalJellyEffect;
import net.akashi.perk_weapons.PerkWeapons;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModEffects {
	public static final DeferredRegister<MobEffect> EFFECTS =
			DeferredRegister.create(Registries.MOB_EFFECT, PerkWeapons.MODID);
	public static final DeferredHolder<MobEffect, HarmAllEffect> HARM_ALL =
			EFFECTS.register("harm_all", HarmAllEffect::new);
	public static final DeferredHolder<MobEffect, InternalExplosionEffect> INTERNAL_EXPLOSION =
			EFFECTS.register("internal_explosion", InternalExplosionEffect::new);
	public static final DeferredHolder<MobEffect, RoyalJellyEffect> ROYAL_JELLY =
			EFFECTS.register("royal_jelly", RoyalJellyEffect::new);
	public static final DeferredHolder<MobEffect, PhalanxEffect> PHALANX =
			EFFECTS.register("phalanx", PhalanxEffect::new);
}
