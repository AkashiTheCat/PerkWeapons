package net.akashi.perk_weapons.Registry;

import net.akashi.perk_weapons.Effects.HarmAllEffect;
import net.akashi.perk_weapons.Effects.InternalExplosionEffect;
import net.akashi.perk_weapons.Effects.RoyalJellyEffect;
import net.akashi.perk_weapons.PerkWeapons;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
	public static final DeferredRegister<MobEffect> EFFECTS =
			DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, PerkWeapons.MODID);
	public static final RegistryObject<MobEffect> HARM_ALL =
			EFFECTS.register("harm_all", HarmAllEffect::new);
	public static final RegistryObject<MobEffect> INTERNAL_EXPLOSION =
			EFFECTS.register("internal_explosion", InternalExplosionEffect::new);
	public static final RegistryObject<MobEffect> ROYAL_JELLY =
			EFFECTS.register("royal_jelly", RoyalJellyEffect::new);
}
