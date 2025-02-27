package net.akashi.perk_weapons.Bows;

import net.akashi.perk_weapons.Client.ClientHelper;
import net.akashi.perk_weapons.Config.Properties.Bow.BowProperties;
import net.akashi.perk_weapons.Config.Properties.Bow.ElfsHarpProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.PerkUpdateArrow;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Util.IPerkItem;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
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
	public static String TAG_PERK_LEVEL = "perk_level";
	public static byte MAX_PERK_LEVEL = 3;
	public static float PERK_BUFF = 1.0F;
	public static int GLOWING_TIME = 100;

	public ElfsHarpItem(Properties properties) {
		super(properties);
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerPerkBowPropertyOverrides(this);
	}

	public ElfsHarpItem(int drawTime, float projectileDamage, float velocity, float inaccuracy, float speedModifier,
	                    float zoomFactor, boolean onlyMainHand, Properties properties) {
		super(drawTime, projectileDamage, velocity, inaccuracy, speedModifier, zoomFactor, onlyMainHand, properties);
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerPerkBowPropertyOverrides(this);
	}

	@Override
	public byte getMaxPerkLevel() {
		return MAX_PERK_LEVEL;
	}

	@Override
	public float getPerkLevel(LivingEntity entity, ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		return tag.contains(TAG_PERK_LEVEL) ? tag.getInt(TAG_PERK_LEVEL) : 0;
	}

	public void setPerkLevel(ItemStack stack, float level) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putFloat(TAG_PERK_LEVEL, level);
	}

	@Override
	public boolean isPerkMax(LivingEntity entity, ItemStack stack) {
		return ((byte) Math.ceil(getPerkLevel(entity, stack))) == MAX_PERK_LEVEL;
	}

	@Override
	public void gainPerkLevel(LivingEntity entity, ItemStack stack) {
		float level = getPerkLevel(entity, stack);
		setPerkLevel(stack, level + 1);
	}

	@Override
	public AbstractArrow createArrow(Level level, ArrowItem arrowItem, ItemStack bowStack, ItemStack arrowStack, Player player) {
		PerkUpdateArrow arrow = new PerkUpdateArrow(ModEntities.PERK_UPDATE_ARROW.get(), level, player);
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
			setPerkLevel(bowStack, 0);
		} else {
			arrow.setMagicDamage((float) (PROJECTILE_DAMAGE * (powerLevel > 0 ? 1 + 0.25 * powerLevel : 1)));
		}
		return arrow;
	}

	@Override
	public void updateAttributesFromConfig(BowProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof ElfsHarpProperties eProperties) {
			MAX_PERK_LEVEL = eProperties.MAX_PERK_LEVEL.get().byteValue();
			PERK_BUFF = eProperties.PERK_BUFF.get().floatValue();
			GLOWING_TIME = eProperties.GLOWING_TIME.get();
		}
	}

	@Override
	public Component getWeaponDescription(ItemStack stack, Level level) {
		return TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.elfs_harp"));
	}

	@Override
	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		List<Component> list = new ArrayList<>();

		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.elfs_harp_perk_1")));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.elfs_harp_perk_2",
				TooltipHelper.setSubPerkStyle(Component.translatable("tooltip.perk_weapons.effect_format",
						MobEffects.GLOWING.getDisplayName(), 1,
						TooltipHelper.convertTicksToSeconds(GLOWING_TIME))))));

		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.elfs_harp_perk_3",
				TooltipHelper.convertToEmbeddedElement(1))));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.elfs_harp_perk_4",
				TooltipHelper.convertToEmbeddedElement(MAX_PERK_LEVEL))));

		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.elfs_harp_perk_5")));
		list.add(TooltipHelper.getArrowDamageModifier(PERK_BUFF));

		list.add(TooltipHelper.setSubPerkStyle(Component.translatable("tooltip.perk_weapons.elfs_harp_perk_6")));

		return list;
	}
}
