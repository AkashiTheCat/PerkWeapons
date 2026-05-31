package net.akashi.perk_weapons.Registry;

import net.akashi.perk_weapons.PerkWeapons;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModAttributes {
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(
			Registries.ATTRIBUTE, PerkWeapons.MODID
	);

	public static final DeferredHolder<Attribute, Attribute> MAGIC_RESISTANCE =
			ATTRIBUTES.register("magic_resistance", () -> new RangedAttribute(
					"attribute.name.generic.magic_resistance", 0.0, -1000.0, 100.0)
					.setSyncable(true));

	public static final DeferredHolder<Attribute, Attribute> DAMAGE_RESISTANCE =
			ATTRIBUTES.register("damage_resistance", () -> new RangedAttribute(
					"attribute.name.generic.damage_resistance", 0.0, -1000.0, 100.0)
					.setSyncable(true));
}
