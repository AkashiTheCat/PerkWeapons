package net.akashi.perk_weapons.Enchantments;

import net.akashi.perk_weapons.Registry.ModItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public class StarShooterEnchantment extends BaseEnchantment {
	public StarShooterEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
		super(pRarity, pCategory, pApplicableSlots);
	}

	@Override
	public boolean canApplyAtEnchantingTable(@NotNull ItemStack stack) {
		return super.canApplyAtEnchantingTable(stack) && stack.is(ModItems.HOU_YI.get());
	}
}
