package net.akashi.perk_weapons.Crossbows;

import net.akashi.perk_weapons.Config.Properties.Crossbow.CrossbowProperties;
import net.akashi.perk_weapons.Config.Properties.Crossbow.IncineratorProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.IncineratorArrow;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import static net.minecraft.world.item.enchantment.Enchantments.*;

public class IncineratorItem extends BaseCrossbowItem {
	public static int FIRE_ARROW_KNOCKBACK_BONUS = 1;
	public static int AMMO_CAPACITY = 7;

	public IncineratorItem(Properties pProperties) {
		super(pProperties);
		AddGeneralEnchant(FLAMING_ARROWS);
	}

	public IncineratorItem(int maxChargeTicks, float damage, float velocity, float inaccuracy,
	                       float speedModifier, boolean onlyAllowMainHand, Properties pProperties) {
		super(maxChargeTicks, damage, velocity, inaccuracy, speedModifier, onlyAllowMainHand, pProperties);
		AddGeneralEnchant(FLAMING_ARROWS);
	}

	@Override
	public void updateAttributesFromConfig(CrossbowProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof IncineratorProperties IProperties) {
			AMMO_CAPACITY = IProperties.AMMO_CAPACITY.get();
			FIRE_ARROW_KNOCKBACK_BONUS = IProperties.FIRE_ARROW_KNOCKBACK_BONUS.get();
		}
	}

	@Override
	protected SoundEvent getShootSound(ItemStack crossbowStack) {
		return SoundEvents.BLAZE_SHOOT;
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
	protected Projectile getProjectile(Level level, LivingEntity shooter, ItemStack crossbowStack) {
		BaseCrossbowItem crossbowItem = (BaseCrossbowItem) crossbowStack.getItem();
		ItemStack ammoStack = crossbowItem.getChargedProjectile(crossbowStack);
		Item ammoItem = ammoStack.getItem();

		if (ammoItem instanceof FireworkRocketItem) {
			return new FireworkRocketEntity(level, ammoStack, shooter, shooter.getX(),
					shooter.getEyeY() - (double) 0.15F, shooter.getZ(), true);
		}

		IncineratorArrow arrow = new IncineratorArrow(ModEntities.INCINERATOR_ARROW.get(), level, shooter);
		if (ammoItem instanceof SpectralArrowItem) {
			arrow.setSpectralArrow(true);
		} else {
			arrow.setEffectsFromItem(ammoStack);
		}
		arrow.setShotFromCrossbow(true);
		if (crossbowStack.getEnchantmentLevel(FLAMING_ARROWS) > 0)
			arrow.setSecondsOnFire(100);

		return arrow;
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
		return AMMO_CAPACITY;
	}
}
