package net.akashi.perk_weapons.Util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Map;
import java.util.Set;

public class EnchantmentValidator {
	static public boolean canBookEnchant(ItemStack stack, ItemStack book, Set<Enchantment> GeneralEnchants,
	                                     Set<Enchantment> ConflictEnchants) {
		Map<Enchantment, Integer> enchantments = book.getAllEnchantments();
		for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
			Enchantment enchantment = entry.getKey();
			if (GeneralEnchants.stream().anyMatch(GEnchantment -> GEnchantment.equals(enchantment))) {
				continue;
			}
			if (ConflictEnchants.stream().noneMatch(CEnchantment -> stack.getEnchantmentLevel(CEnchantment) > 0)) {
				continue;
			}
			if (ConflictEnchants.stream().anyMatch(CEnchantments -> CEnchantments.equals(enchantment))) {
				return false;
			}
		}
		return true;
	}

	static public boolean canApplyAtTable(ItemStack stack, Enchantment enchantment, Set<Enchantment> GeneralEnchants,
	                                      Set<Enchantment> ConflictEnchants) {
		if (GeneralEnchants.stream().anyMatch(GEnchantment -> GEnchantment.equals(enchantment))) {
			return true;
		}
		if (ConflictEnchants.stream().anyMatch(CEnchantments -> CEnchantments.equals(enchantment))) {
			return ConflictEnchants.stream().noneMatch(CEnchantment -> stack.getEnchantmentLevel(CEnchantment) > 0);
		}
		return false;
	}
}
