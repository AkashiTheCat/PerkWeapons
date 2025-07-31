package net.akashi.perk_weapons.Enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class BlazeEnchantment extends BaseEnchantment {
	public BlazeEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
		super(pRarity, pCategory, pApplicableSlots);
	}

	@Override
	public int getMinCost(int pLevel) {
		return 20;
	}

	@Override
	public int getMaxCost(int pLevel) {
		return 50;
	}
}
