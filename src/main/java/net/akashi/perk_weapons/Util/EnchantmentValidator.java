package net.akashi.perk_weapons.Util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Map;
import java.util.Set;

public class EnchantmentValidator {
	static public boolean canBookEnchant(ItemStack stack, ItemStack book,
	                                     Set<Enchantment> ConflictEnchants) {
		Map<Enchantment, Integer> enchantments = book.getAllEnchantments();
		Map<Enchantment, Integer> stackEnchantments = stack.getAllEnchantments();
		boolean hasConflictEnchantmentOnStack = stackEnchantments.keySet().stream()
				.anyMatch(ConflictEnchants::contains);
		for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
			Enchantment enchantment = entry.getKey();
			if (ConflictEnchants.contains(enchantment) && hasConflictEnchantmentOnStack) {
				return false;
			}
		}
		return true;
	}

	static public boolean canApplyAtTable(Enchantment enchantment,
	                                      Set<Enchantment> GeneralEnchants,
	                                      Set<Enchantment> ConflictEnchants) {
		return GeneralEnchants.contains(enchantment) || ConflictEnchants.contains(enchantment);
	}
}
