package net.akashi.perk_weapons.Bows;

import net.akashi.perk_weapons.Client.ClientHelper;
import net.akashi.perk_weapons.Config.Properties.Bow.BowProperties;
import net.akashi.perk_weapons.Config.Properties.Bow.ElfsHarpProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.PerkGainingArrow;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Util.IPerkItem;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpectralArrowItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.*;

import static net.minecraft.world.item.enchantment.Enchantments.POWER_ARROWS;

public class ElfsHarpItem extends BaseBowItem implements IPerkItem {
	public static byte MAX_PERK_LEVEL = 3;
	public static float PERK_BUFF = 1.0F;
	public static int GLOWING_TIME = 100;

	public ElfsHarpItem(Properties properties) {
		super(properties);
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerPerkItemPropertyOverrides(this);
	}

	public ElfsHarpItem(int drawTime, float projectileDamage, float velocity, float inaccuracy, float speedModifier,
	                    float zoomFactor, boolean onlyMainHand, Properties properties) {
		super(drawTime, projectileDamage, velocity, inaccuracy, speedModifier, zoomFactor, onlyMainHand, properties);
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerPerkItemPropertyOverrides(this);
	}

	@Override
	public byte getMaxPerkLevel() {
		return MAX_PERK_LEVEL;
	}

	@Override
	public AbstractArrow createArrow(Level level, ArrowItem arrowItem, ItemStack bowStack, ItemStack arrowStack, Player player) {
		PerkGainingArrow arrow = new PerkGainingArrow(ModEntities.PERK_GAINING_ARROW.get(), level, player);
		if (!(arrowItem instanceof SpectralArrowItem)) {
			arrow.setEffectsFromItem(arrowStack);
		}
		arrow.setBaseDamage(0.0F);
		arrow.addEffect(new MobEffectInstance(MobEffects.GLOWING, GLOWING_TIME));

		int powerLevel = bowStack.getEnchantmentLevel(POWER_ARROWS);
		if (isPerkMax(player, bowStack)) {
			arrow.setMagicDamage((float) (PROJECTILE_DAMAGE * (1 + PERK_BUFF) *
					(powerLevel > 0 ? 1 + 0.25 * powerLevel : 1)));
			arrow.setRenderTrail(true);
			setPerkLevel(bowStack, (byte) 0);
		} else {
			arrow.setMagicDamage((float) (PROJECTILE_DAMAGE * (powerLevel > 0 ? 1 + 0.25 * powerLevel : 1)));
		}
		return arrow;
	}

	@Override
	public void updateAttributesFromConfig(BowProperties properties) {
		if (properties instanceof ElfsHarpProperties eProperties) {
			MAX_PERK_LEVEL = eProperties.MAX_PERK_LEVEL.get().byteValue();
			PERK_BUFF = eProperties.PERK_BUFF.get().floatValue();
			GLOWING_TIME = eProperties.GLOWING_TIME.get();
		}
		super.updateAttributesFromConfig(properties);
	}

	@Override
	public Component getWeaponDescription(ItemStack stack, Level level) {
		return TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.elfs_harp"));
	}

	@Override
	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		List<Component> list = super.getPerkDescriptions(stack, level);

		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.elfs_harp_perk_1")));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.elfs_harp_perk_2",
				TooltipHelper.setSubPerkStyle(Component.translatable("tooltip.perk_weapons.effect_format",
						MobEffects.GLOWING.getDisplayName(),
						TooltipHelper.getRomanNumeral(1),
						TooltipHelper.convertTicksToSeconds(GLOWING_TIME))))));

		list.add(Component.empty());

		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.gain_perk_level_on_hit",
				TooltipHelper.convertToEmbeddedElement(1))));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.max_perk_level",
				TooltipHelper.convertToEmbeddedElement(MAX_PERK_LEVEL))));

		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.when_reach_max_perk_level")));
		list.add(TooltipHelper.getArrowDamageModifier(PERK_BUFF));

		list.add(TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.elfs_harp_perk_3")));

		return list;
	}
}
