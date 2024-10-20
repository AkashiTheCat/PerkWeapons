package net.akashi.weaponmod.Registry;

import net.akashi.weaponmod.Effects.HarmAllEffect;
import net.akashi.weaponmod.WeaponMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
	public static final DeferredRegister<MobEffect> EFFECTS =
			DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, WeaponMod.MODID);
	public static final RegistryObject<MobEffect> HARM_ALL =
			EFFECTS.register("harm_all", HarmAllEffect::new);
}
