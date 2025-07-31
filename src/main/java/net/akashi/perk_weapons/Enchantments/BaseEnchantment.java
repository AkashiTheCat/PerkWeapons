package net.akashi.perk_weapons.Enchantments;

import net.akashi.perk_weapons.Config.Properties.Enchantment.EnchantmentProperties;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public class BaseEnchantment extends Enchantment {
	protected boolean ALLOW_ON_TABLE = true;
	protected boolean ALLOW_ON_BOOK = true;
	protected boolean IS_DISCOVERABLE = true;
	protected boolean IS_TREASURE_ONLY = false;
	protected boolean IS_TRADEABLE = true;

	protected Rarity RARITY = Rarity.UNCOMMON;

	public BaseEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
		super(pRarity, pCategory, pApplicableSlots);
	}

	public void updateFromConfig(EnchantmentProperties properties) {
		ALLOW_ON_TABLE = properties.ALLOW_ON_TABLE.get();
		ALLOW_ON_BOOK = properties.ALLOW_ON_BOOK.get();
		IS_DISCOVERABLE = properties.IS_DISCOVERABLE.get();
		IS_TREASURE_ONLY = properties.IS_TREASURE_ONLY.get();
		IS_TRADEABLE = properties.IS_TRADEABLE.get();
		RARITY = properties.RARITY.get();
	}

	@Override
	public @NotNull Rarity getRarity() {
		return RARITY;
	}

	@Override
	public boolean isTradeable() {
		return IS_TRADEABLE;
	}

	@Override
	public boolean isTreasureOnly() {
		return IS_TREASURE_ONLY;
	}

	@Override
	public boolean canApplyAtEnchantingTable(@NotNull ItemStack stack) {
		return ALLOW_ON_TABLE && super.canApplyAtEnchantingTable(stack);
	}

	@Override
	public boolean isAllowedOnBooks() {
		return ALLOW_ON_BOOK;
	}

	@Override
	public boolean isDiscoverable() {
		return IS_DISCOVERABLE;
	}
}
