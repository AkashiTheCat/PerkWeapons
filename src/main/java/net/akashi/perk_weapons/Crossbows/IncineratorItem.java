package net.akashi.perk_weapons.Crossbows;

import net.akashi.perk_weapons.Config.Properties.Crossbow.CrossbowProperties;
import net.akashi.perk_weapons.Config.Properties.Crossbow.IncineratorProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.IncineratorArrow;
import net.akashi.perk_weapons.Registry.ModEnchantments;
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

public class IncineratorItem extends MagFedCrossbowItem {
	public static int FIRE_ARROW_KNOCKBACK_BONUS = 1;
	public static int BLAZE_AMMO_CAPACITY = 10;

	public IncineratorItem(Properties pProperties) {
		super(pProperties);
		AddGeneralEnchant(FLAMING_ARROWS);
	}

	public IncineratorItem(int maxChargeTicks, float damage, float velocity, float inaccuracy,
	                       float speedModifier, boolean onlyAllowMainHand, Properties pProperties) {
		super(maxChargeTicks, damage, velocity, inaccuracy, speedModifier,
				7, onlyAllowMainHand, pProperties);
		AddGeneralEnchant(FLAMING_ARROWS);
	}

	@Override
	public void updateAttributesFromConfig(CrossbowProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof IncineratorProperties IProperties) {
			AMMO_CAPACITY = IProperties.NORMAL_AMMO_CAPACITY.get();
			BLAZE_AMMO_CAPACITY = IProperties.BLAZE_AMMO_CAPACITY.get();
			FIRE_ARROW_KNOCKBACK_BONUS = IProperties.FIRE_ARROW_KNOCKBACK_BONUS.get();
		}
		AddGeneralEnchant(ModEnchantments.BLAZE.get());
	}

	@Override
	protected SoundEvent getShootSound(ItemStack crossbowStack) {
		return SoundEvents.BLAZE_SHOOT;
	}

	@Override
	protected Projectile getProjectile(Level level, LivingEntity shooter, ItemStack crossbowStack) {
		BaseCrossbowItem crossbowItem = (BaseCrossbowItem) crossbowStack.getItem();
		ItemStack ammoStack = crossbowItem.getLastChargedProjectile(crossbowStack);
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

	public int getAmmoCapacity(ItemStack crossbowStack) {
		return crossbowStack.getEnchantmentLevel(ModEnchantments.BLAZE.get()) > 0 ?
				BLAZE_AMMO_CAPACITY : super.getAmmoCapacity(crossbowStack);
	}
}
