package net.akashi.perk_weapons.Enchantments;

import net.akashi.perk_weapons.Bows.PurgatoryItem;
import net.akashi.perk_weapons.Config.ModCommonConfigs;
import net.akashi.perk_weapons.Crossbows.IncineratorItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class BlazeEnchantment extends Enchantment {
	public BlazeEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
		super(pRarity, pCategory, pApplicableSlots);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return ModCommonConfigs.ENABLE_BLAZE_ON_TABLE.get() && stack.getItem() instanceof IncineratorItem;
	}
}
