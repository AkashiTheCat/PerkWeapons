package net.akashi.perk_weapons.Registry;

import net.akashi.perk_weapons.PerkWeapons;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModAttributes {
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(
			ForgeRegistries.ATTRIBUTES, PerkWeapons.MODID
	);

	public static final RegistryObject<Attribute> MAGIC_RESISTANCE =
			ATTRIBUTES.register("magic_resistance", () -> new RangedAttribute(
					"attribute.name.generic.magic_resistance", 0.0, -1000.0, 100.0)
					.setSyncable(true));

	public static final RegistryObject<Attribute> DAMAGE_RESISTANCE =
			ATTRIBUTES.register("damage_resistance", () -> new RangedAttribute(
					"attribute.name.generic.damage_resistance", 0.0, -1000.0, 100.0)
					.setSyncable(true));
}
