package net.akashi.perk_weapons.Registry;

import net.akashi.perk_weapons.Enchantments.*;
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
	public static final RegistryObject<BaseEnchantment> MELT_DOWN_ARROW =
			ENCHANTMENTS.register("melt_down",
					() -> new MeltDownEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.BOW,
							new EquipmentSlot[]{EquipmentSlot.MAINHAND}));
	public static final RegistryObject<BaseEnchantment> STAR_SHOOTER =
			ENCHANTMENTS.register("star_shooter",
					() -> new StarShooterEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.BOW,
							new EquipmentSlot[]{EquipmentSlot.MAINHAND}));
	public static final RegistryObject<BaseEnchantment> REGICIDE =
			ENCHANTMENTS.register("regicide",
					() -> new RegicideEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.CROSSBOW,
							new EquipmentSlot[]{EquipmentSlot.MAINHAND}));
	public static final RegistryObject<BaseEnchantment> BLAZE =
			ENCHANTMENTS.register("blaze",
					() -> new BlazeEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.CROSSBOW,
							new EquipmentSlot[]{EquipmentSlot.MAINHAND}));
}
