package net.akashi.perk_weapons.Registry;

import net.akashi.perk_weapons.PerkWeapons;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantments {
	public static final ResourceKey<Enchantment> MELT_DOWN_ARROW_KEY = key("melt_down");
	public static final ResourceKey<Enchantment> STAR_SHOOTER_KEY = key("star_shooter");
	public static final ResourceKey<Enchantment> REGICIDE_KEY = key("regicide");
	public static final ResourceKey<Enchantment> BLAZE_KEY = key("blaze");

	private static ResourceKey<Enchantment> key(String name) {
		return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(PerkWeapons.MODID, name));
	}
}
