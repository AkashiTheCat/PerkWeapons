package net.akashi.weaponmod.Registry;

import net.akashi.weaponmod.Enchantments.MeltDownEnchantment;
import net.akashi.weaponmod.WeaponMod;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {
	public static final DeferredRegister<Enchantment> ENCHANTMENTS =
			DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, WeaponMod.MODID);
	public static final RegistryObject<MeltDownEnchantment> MELT_DOWN_ARROW =
			ENCHANTMENTS.register("melt_down",
					() -> new MeltDownEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.BOW,
							new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND}));
}
