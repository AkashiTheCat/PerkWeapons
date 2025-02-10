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

public class LiberatorItem extends BaseCrossbowItem {
	public static byte PIERCE_LEVEL = 1;
	public static byte MULTISHOT_BONUS = 1;
	public static byte AMMO_CAPACITY_REGICIDE = 2;

	public LiberatorItem(Properties pProperties) {
		super(pProperties);
		this.RemoveGeneralEnchant(PIERCING);
	}

	public LiberatorItem(int maxChargeTicks, float damage, float velocity, float inaccuracy,
	                     float speedModifier, boolean onlyAllowMainHand, Properties pProperties) {
		super(maxChargeTicks, damage, velocity, inaccuracy, speedModifier, onlyAllowMainHand, pProperties);
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
	public void setCrossbowCharged(ItemStack crossbowStack, boolean charged) {
		if (charged) {
			super.setCrossbowCharged(crossbowStack, true);
			return;
		}
		int amount = getAmmoAmount(crossbowStack);
		if (amount > 1) {
			setAmmoAmount(crossbowStack, amount - 1);
			return;
		}
		super.setCrossbowCharged(crossbowStack, false);
	}

	@Override
	public boolean tryLoadAmmo(LivingEntity shooter, ItemStack crossbowStack) {
		ItemStack ammoStack = shooter.getProjectile(crossbowStack);
		boolean isShooterPlayer = shooter instanceof Player;
		boolean isCreative = isShooterPlayer && ((Player) shooter).getAbilities().instabuild;

		if (ammoStack.isEmpty()) {
			if (isCreative) {
				ammoStack = new ItemStack(Items.ARROW);
			} else {
				return false;
			}
		}
		ItemStack ammoToLoad = ItemStack.EMPTY;
		int count = getAmmoCapacity(crossbowStack);

		if (isShooterPlayer && !isCreative) {
			if (ammoStack.getCount() >= count) {
				ammoToLoad = ammoStack.copyWithCount(1);
				ammoStack.shrink(count);
			}
			if (ammoStack.isEmpty())
				((Player) shooter).getInventory().removeItem(ammoStack);
		}

		setChargedProjectile(crossbowStack, ammoToLoad);
		setAmmoAmount(crossbowStack, count);
		setCrossbowCharged(crossbowStack, true);
		return true;
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

	public void setAmmoAmount(ItemStack crossbowStack, int count) {
		CompoundTag tag = crossbowStack.getOrCreateTag();
		tag.putInt("ammo_count", count);
	}

	public int getAmmoAmount(ItemStack crossbowStack) {
		CompoundTag tag = crossbowStack.getOrCreateTag();
		if (tag.contains("ammo_count")) {
			return tag.getInt("ammo_count");
		}
		return 0;
	}

	public int getAmmoCapacity(ItemStack crossbowStack) {
		return crossbowStack.getEnchantmentLevel(ModEnchantments.REGICIDE.get()) > 0 ? AMMO_CAPACITY_REGICIDE : 1;
	}
}
