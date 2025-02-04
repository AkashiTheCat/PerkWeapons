package net.akashi.perk_weapons.Registry;

import net.akashi.perk_weapons.Enchantments.MeltDownEnchantment;
import net.akashi.perk_weapons.Enchantments.StarShooterEnchantment;
import net.akashi.perk_weapons.PerkWeapons;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {
	public static final DeferredRegister<Enchantment> ENCHANTMENTS =
			DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, PerkWeapons.MODID);
	public static final RegistryObject<MeltDownEnchantment> MELT_DOWN_ARROW =
			ENCHANTMENTS.register("melt_down",
					() -> new MeltDownEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.BOW,
							new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND}));
	public static final RegistryObject<StarShooterEnchantment> STAR_SHOOTER =
			ENCHANTMENTS.register("star_shooter",
					() -> new StarShooterEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.BOW,
							new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND}));
}
