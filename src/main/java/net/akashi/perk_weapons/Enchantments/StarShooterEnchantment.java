package net.akashi.perk_weapons.Enchantments;

import net.akashi.perk_weapons.Bows.HouYiItem;
import net.akashi.perk_weapons.Config.ModCommonConfigs;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class StarShooterEnchantment extends Enchantment {
	public StarShooterEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
		super(pRarity, pCategory, pApplicableSlots);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return ModCommonConfigs.ENABLE_STAR_SHOOTER_ON_TABLE.get() && stack.getItem() instanceof HouYiItem;
	}

}
