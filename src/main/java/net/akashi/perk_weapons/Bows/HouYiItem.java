package net.akashi.perk_weapons.Bows;

import net.akashi.perk_weapons.Config.Properties.Bow.BowProperties;
import net.akashi.perk_weapons.Config.Properties.Bow.HouYiProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.BaseArrow;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.StarShooterArrow;
import net.akashi.perk_weapons.Registry.ModEnchantments;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpectralArrowItem;
import net.minecraft.world.level.Level;

import java.util.List;

public class HouYiItem extends BaseBowItem {
	public static float DAMAGE_MODIFIER_STAR_SHOOTER = -0.5f;

	public HouYiItem(Properties properties) {
		super(properties);
	}

	public HouYiItem(int drawTime, float projectileDamage, float velocity, float inaccuracy,
	                 float speedModifier, float zoomFactor, boolean onlyMainHand, Properties properties) {
		super(drawTime, projectileDamage, velocity, inaccuracy, speedModifier, zoomFactor, onlyMainHand, properties);
	}

	@Override
	public AbstractArrow createArrow(Level level, ArrowItem arrowItem, ItemStack bowStack, ItemStack arrowStack, Player player) {
		BaseArrow arrow;
		if (bowStack.getEnchantmentLevel(ModEnchantments.STAR_SHOOTER.get()) != 0) {
			arrow = new StarShooterArrow(ModEntities.STAR_SHOOTER_ARROW.get(), level, player);
		} else {
			arrow = new BaseArrow(ModEntities.BASE_ARROW.get(), level, player);
		}

		arrow.setBaseDamage((PROJECTILE_DAMAGE / VELOCITY));
		arrow.setNoGravity(true);

		if (arrowItem instanceof SpectralArrowItem) {
			arrow.setSpectralArrow(true);
		} else {
			arrow.setEffectsFromItem(arrowStack);
		}
		return arrow;
	}

	@Override
	public double getDamageMultiplier(ItemStack stack) {
		return super.getDamageMultiplier(stack) * (1 + (stack.getEnchantmentLevel(ModEnchantments.STAR_SHOOTER.get()) > 0 ?
				DAMAGE_MODIFIER_STAR_SHOOTER : 0));
	}

	@Override
	public void updateAttributesFromConfig(BowProperties properties) {
		AddGeneralEnchant(ModEnchantments.STAR_SHOOTER.get());
		if (properties instanceof HouYiProperties hProperties) {
			DAMAGE_MODIFIER_STAR_SHOOTER = hProperties.STAR_SHOOTER_DAMAGE_MODIFIER.get().floatValue();
			StarShooterArrow.DAMAGE_MODIFIER_PER_METER = hProperties.STAR_SHOOTER_DAMAGE_MODIFIER_PER_METER.get().floatValue();
			StarShooterArrow.DAMAGE_MODIFIER_MAX = hProperties.STAR_SHOOTER_DAMAGE_MODIFIER_MAX.get().floatValue();
			StarShooterArrow.DAMAGE_MODIFIER_MIN = hProperties.STAR_SHOOTER_DAMAGE_MODIFIER_MIN.get().floatValue();
		}
		super.updateAttributesFromConfig(properties);
	}

	@Override
	public Component getWeaponDescription(ItemStack stack, Level level) {
		return TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.hou_yi"));
	}

	@Override
	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		List<Component> list = super.getPerkDescriptions(stack, level);

		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.hou_yi_perk_1")));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.when_enchanted",
				TooltipHelper.convertToEmbeddedElement(ModEnchantments.STAR_SHOOTER.get(), 1))));

		list.add(TooltipHelper.getArrowDamageModifier(DAMAGE_MODIFIER_STAR_SHOOTER));

		list.add(TooltipHelper.getRatioModifierWithStyle("tooltip.perk_weapons.hou_yi_perk_2",
				StarShooterArrow.DAMAGE_MODIFIER_PER_METER));

		list.add(TooltipHelper.setSubPerkStyle(Component.translatable("tooltip.perk_weapons.hou_yi_perk_3",
				Component.literal(TooltipHelper.getPercentageWithSign(StarShooterArrow.DAMAGE_MODIFIER_MIN))
						.withStyle(ChatFormatting.GRAY),
				Component.literal(TooltipHelper.getPercentageWithSign(StarShooterArrow.DAMAGE_MODIFIER_MAX))
						.withStyle(ChatFormatting.GRAY)
		)));

		return list;
	}
}
