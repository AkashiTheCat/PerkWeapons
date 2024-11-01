package net.akashi.perk_weapons.Enchantments;

import net.akashi.perk_weapons.Config.ModCommonConfigs;
import net.akashi.perk_weapons.Util.EntityTypeListReader;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

import static net.akashi.perk_weapons.Util.EntityTypeListReader.convertStringsToEntityType;

public class StarShooterEnchantment extends Enchantment {
	public StarShooterEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
		super(pRarity, pCategory, pApplicableSlots);
	}

	@Override
	public int getMinCost(int pEnchantmentLevel) {
		return pEnchantmentLevel * 40;
	}

	@Override
	public int getMaxCost(int pEnchantmentLevel) {
		return this.getMinCost(pEnchantmentLevel) + 80;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return ModCommonConfigs.ENABLE_STAR_SHOOTER_ON_TABLE.get();
	}

	@Override
	public boolean isAllowedOnBooks() {
		return true;
	}
}
