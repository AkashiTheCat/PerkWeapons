package net.akashi.perk_weapons.Crossbows;

import net.akashi.perk_weapons.Config.Properties.Crossbow.CrossbowProperties;
import net.akashi.perk_weapons.Config.Properties.Crossbow.LiberatorProperties;
import net.akashi.perk_weapons.Registry.ModEnchantments;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import static net.minecraft.world.item.enchantment.Enchantments.MULTISHOT;
import static net.minecraft.world.item.enchantment.Enchantments.PIERCING;

public class LiberatorItem extends MagFedCrossbowItem {
	public static byte PIERCE_LEVEL = 1;
	public static byte MULTISHOT_BONUS = 1;
	public static byte AMMO_CAPACITY_REGICIDE = 2;

	public LiberatorItem(Properties pProperties) {
		super(pProperties);
		this.RemoveGeneralEnchant(PIERCING);
	}

	public LiberatorItem(int maxChargeTicks, float damage, float velocity, float inaccuracy,
	                     float speedModifier, boolean onlyAllowMainHand, Properties pProperties) {
		super(maxChargeTicks, damage, velocity, inaccuracy, speedModifier,
				1, onlyAllowMainHand, pProperties);
		RemoveGeneralEnchant(PIERCING);
	}

	@Override
	public void updateAttributesFromConfig(CrossbowProperties properties) {
		super.updateAttributesFromConfig(properties);
		AddGeneralEnchant(ModEnchantments.REGICIDE.get());
		if (properties instanceof LiberatorProperties lProperties) {
			PIERCE_LEVEL = lProperties.PIERCE_LEVEL.get().byteValue();
			AMMO_CAPACITY_REGICIDE = lProperties.CAPACITY_REGICIDE.get().byteValue();
			MULTISHOT_BONUS = lProperties.MULTISHOT_BONUS.get().byteValue();
		}
	}

	@Override
	protected Projectile getProjectile(Level level, LivingEntity shooter, ItemStack crossbowStack) {
		Projectile projectile = super.getProjectile(level, shooter, crossbowStack);
		if (projectile instanceof AbstractArrow arrow) {
			arrow.setPierceLevel(PIERCE_LEVEL);
			return arrow;
		}
		return projectile;
	}

	@Override
	public int getCrossbowEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
		if (enchantment == MULTISHOT) {
			return stack.getEnchantmentLevel(MULTISHOT) + MULTISHOT_BONUS;
		}
		return super.getEnchantmentLevel(stack, enchantment);
	}

	@Override
	public int getAmmoCapacity(ItemStack crossbowStack) {
		return crossbowStack.getEnchantmentLevel(ModEnchantments.REGICIDE.get()) > 0 ?
				AMMO_CAPACITY_REGICIDE : super.getAmmoCapacity(crossbowStack);
	}
}
