package net.akashi.perk_weapons.Enchantments;

import net.akashi.perk_weapons.Bows.HouYiItem;
import net.akashi.perk_weapons.Bows.PurgatoryItem;
import net.akashi.perk_weapons.Config.ModCommonConfigs;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class MeltDownEnchantment extends Enchantment {
	public MeltDownEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
		super(pRarity, pCategory, pApplicableSlots);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return ModCommonConfigs.ENABLE_MELT_DOWN_ON_TABLE.get() && stack.getItem() instanceof PurgatoryItem;
	}

	@Override
	public boolean isAllowedOnBooks() {
		return true;
	}
}
