package net.akashi.perk_weapons.Util;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.Set;

public class EnchantmentUtil {
	public static boolean canBookEnchant(ItemStack stack, ItemStack book,
	                                     Set<ResourceKey<Enchantment>> generalEnchants,
	                                     Set<ResourceKey<Enchantment>> conflictEnchants) {
		ItemEnchantments enchantments = book.getTagEnchantments();
		ItemEnchantments stackEnchantments = stack.getTagEnchantments();
		boolean hasConflictEnchantmentOnStack = stackEnchantments.keySet().stream().anyMatch(
				holder -> holder.unwrapKey().map(conflictEnchants::contains).orElse(false));
		for (Holder<Enchantment> enchantment : enchantments.keySet()) {
			if (!stack.supportsEnchantment(enchantment)) {
				return false;
			}
			if (!isCompatibleWithExistingEnchantments(enchantment, stackEnchantments)) {
				return false;
			}
			if (enchantment.unwrapKey().map(conflictEnchants::contains).orElse(false) && hasConflictEnchantmentOnStack) {
				return false;
			}
		}
		return true;
	}

	public static boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment,
	                                          Set<ResourceKey<Enchantment>> generalEnchants,
	                                          Set<ResourceKey<Enchantment>> conflictEnchants) {
		return stack.is(Items.ENCHANTED_BOOK)
				|| isKnownAllowed(generalEnchants, conflictEnchants, enchantment)
				|| enchantment.value().definition().supportedItems().contains(stack.getItemHolder());
	}

	public static int getLevel(ItemStack stack, ResourceKey<Enchantment> enchantment) {
		ItemEnchantments enchantments = stack.getTagEnchantments();
		for (var entry : enchantments.entrySet()) {
			if (entry.getKey().unwrapKey().map(enchantment::equals).orElse(false)) {
				return entry.getIntValue();
			}
		}
		return 0;
	}

	public static int getLevel(ItemStack stack, Holder<Enchantment> enchantment) {
		if (enchantment == null) {
			return 0;
		}
		return stack.getEnchantmentLevel(enchantment);
	}

	private static boolean isKnownAllowed(Set<ResourceKey<Enchantment>> generalEnchants,
	                                      Set<ResourceKey<Enchantment>> conflictEnchants,
	                                      Holder<Enchantment> holder) {
		return containsEnchantment(generalEnchants, holder) || containsEnchantment(conflictEnchants, holder);
	}

	private static boolean isCompatibleWithExistingEnchantments(Holder<Enchantment> enchantment,
	                                                            ItemEnchantments stackEnchantments) {
		for (Holder<Enchantment> existing : stackEnchantments.keySet()) {
			if (!existing.equals(enchantment) && !Enchantment.areCompatible(existing, enchantment)) {
				return false;
			}
		}
		return true;
	}

	private static boolean containsEnchantment(Set<ResourceKey<Enchantment>> enchants, Holder<Enchantment> holder) {
		return holder.unwrapKey().map(enchants::contains).orElse(false);
	}
}