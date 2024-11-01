package net.akashi.perk_weapons.Bows;

import net.akashi.perk_weapons.Config.Properties.Bow.BowProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.BaseArrow;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.StarShooterArrow;
import net.akashi.perk_weapons.Registry.ModEnchantments;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpectralArrowItem;
import net.minecraft.world.level.Level;

public class HouYiItem extends BaseBowItem {
	public HouYiItem(Properties properties) {
		super(properties);
	}

	public HouYiItem(int drawTime, float projectileDamage, float velocity, float inaccuracy,
	                 float speedModifier, float zoomFactor, Properties properties) {
		super(drawTime, projectileDamage, velocity, inaccuracy, speedModifier, zoomFactor, properties);
	}

	@Override
	public AbstractArrow createArrow(Level level, ArrowItem arrowItem, ItemStack bowStack, ItemStack arrowStack, Player player) {
		BaseArrow arrow;
		if (bowStack.getEnchantmentLevel(ModEnchantments.STAR_SHOOTER.get()) != 0) {
			arrow = new StarShooterArrow(ModEntities.STAR_SHOOTER_ARROW.get(), level, player);
		} else {
			arrow = new BaseArrow(ModEntities.BASE_ARROW.get(), level, player);
		}
		arrow.setNoGravity(true);
		arrow.setBaseDamage(PROJECTILE_DAMAGE / VELOCITY);
		if (arrowItem instanceof SpectralArrowItem) {
			arrow.setSpectralArrow(true);
		} else {
			arrow.setEffectsFromItem(arrowStack);
		}
		return arrow;
	}

	@Override
	public void updateAttributesFromConfig(BowProperties properties) {
		super.updateAttributesFromConfig(properties);
		AddGeneralEnchant(ModEnchantments.STAR_SHOOTER.get());
	}
}
