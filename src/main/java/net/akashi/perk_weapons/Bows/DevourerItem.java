package net.akashi.perk_weapons.Bows;

import net.akashi.perk_weapons.Config.Properties.Bow.BowProperties;
import net.akashi.perk_weapons.Config.Properties.Bow.DevourerProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.BaseArrow;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.DevourerArrow;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpectralArrowItem;
import net.minecraft.world.level.Level;

public class DevourerItem extends BaseBowItem {
	public static byte PIERCE_LEVEL = 2;

	public DevourerItem(Properties properties) {
		super(properties);
	}

	public DevourerItem(int drawTime, float projectileDamage, float velocity, float inaccuracy,
	                    float speedModifier, float zoomFactor, Properties properties) {
		super(drawTime, projectileDamage, velocity, inaccuracy, speedModifier, zoomFactor, properties);
	}

	@Override
	public AbstractArrow createArrow(Level level, ArrowItem arrowItem, ItemStack bowStack, ItemStack arrowStack, Player player) {
		DevourerArrow arrow = new DevourerArrow(ModEntities.DEVOURER_ARROW.get(), level, player);
		if (arrowItem instanceof SpectralArrowItem) {
			arrow.setSpectralArrow(true);
		} else {
			arrow.setEffectsFromItem(arrowStack);
		}
		arrow.setBaseDamage(PROJECTILE_DAMAGE / VELOCITY);
		arrow.setPierceLevel(PIERCE_LEVEL);
		arrow.setKnockback(0);
		return arrow;
	}

	@Override
	public void updateAttributesFromConfig(BowProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof DevourerProperties dProperties) {
			PIERCE_LEVEL = dProperties.PIERCE_LEVEL.get().byteValue();
		}
	}
}
