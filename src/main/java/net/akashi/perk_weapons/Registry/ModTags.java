package net.akashi.perk_weapons.Registry;

import net.akashi.perk_weapons.PerkWeapons;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
	public static final TagKey<Item> REPAIRER_TAG = TagKey.create(Registries.ITEM,
			new ResourceLocation(PerkWeapons.MODID, "repairer"));
}
