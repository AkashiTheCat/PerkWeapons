package net.akashi.perk_weapons.Bows;

import net.akashi.perk_weapons.Client.ClientHelper;
import net.akashi.perk_weapons.Config.Properties.Bow.BowProperties;
import net.akashi.perk_weapons.Config.Properties.Bow.ForestKeeperProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.PerkGainingArrow;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Util.IPerkItem;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpectralArrowItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static net.minecraft.world.item.enchantment.Enchantments.PUNCH_ARROWS;

public class ForestKeeperItem extends BaseBowItem implements IPerkItem {
	public static final String TAG_LAST_PERK_LEVEL_CHANGE_TIME = "last_perk_change";
	public static byte MAX_PERK_LEVEL = 5;
	public static float PERK_BUFF = 0.1F;
	public static int PERK_DROP_INTERVAL = 40;

	public ForestKeeperItem(Properties properties) {
		super(properties);
		RemoveGeneralEnchant(PUNCH_ARROWS);
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerPerkItemPropertyOverrides(this);
	}

	public ForestKeeperItem(int drawTime, float projectileDamage, float velocity,
	                        float inaccuracy, float speedModifier, float zoomFactor,
	                        boolean onlyMainHand, Properties properties) {
		super(drawTime, projectileDamage, velocity, inaccuracy, speedModifier, zoomFactor, onlyMainHand, properties);
		RemoveGeneralEnchant(PUNCH_ARROWS);
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerPerkItemPropertyOverrides(this);
	}

	@Override
	public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity,
	                          int slotId, boolean isSelected) {
		super.inventoryTick(stack, level, entity, slotId, isSelected);
		int perkLevel = (int) getPerkLevel(null, stack);
		if (perkLevel == 0)
			return;

		if (Math.ceil(getPerkLevel((LivingEntity) entity, stack)) < perkLevel) {
			setPerkLevel(stack, (byte) (perkLevel - 1));
			setLastPerkChangeTime(stack, level.getGameTime());
		}
	}

	@Override
	public AbstractArrow createArrow(Level level, ArrowItem arrowItem, ItemStack bowStack, ItemStack arrowStack,
	                                 Player player) {
		PerkGainingArrow arrow = new PerkGainingArrow(ModEntities.PERK_GAINING_ARROW.get(), level, player);
		if (arrowItem instanceof SpectralArrowItem) {
			arrow.setSpectralArrow(true);
		} else {
			arrow.setEffectsFromItem(arrowStack);
		}

		arrow.setBaseDamage(PROJECTILE_DAMAGE * (1 + PERK_BUFF * getPerkLevel(player, bowStack)) / VELOCITY);
		return arrow;
	}

	@Override
	public void updateAttributesFromConfig(BowProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof ForestKeeperProperties fProperties) {
			MAX_PERK_LEVEL = fProperties.MAX_PERK_LEVEL.get().byteValue();
			PERK_DROP_INTERVAL = fProperties.PERK_DROP_INTERVAL.get();
			PERK_BUFF = fProperties.PERK_DAMAGE_BUFF.get().floatValue();
		}
	}

	@Override
	public byte getMaxPerkLevel() {
		return MAX_PERK_LEVEL;
	}

	public void setLastPerkChangeTime(ItemStack stack, long time) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putLong(TAG_LAST_PERK_LEVEL_CHANGE_TIME, time);
	}

	public long getLastPerkChangeTime(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		return tag.contains(TAG_LAST_PERK_LEVEL_CHANGE_TIME) ? tag.getLong(TAG_LAST_PERK_LEVEL_CHANGE_TIME) : 0;
	}

	@Override
	public Component getWeaponDescription(ItemStack stack, Level level) {
		return TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.forest_keeper"));
	}

	@Override
	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		List<Component> list = super.getPerkDescriptions(stack, level);

		if (!list.isEmpty())
			list.add(Component.empty());

		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.gain_perk_level_on_hit",
				TooltipHelper.convertToEmbeddedElement(1))));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.max_perk_level",
				TooltipHelper.convertToEmbeddedElement(getMaxPerkLevel()))));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.forest_keeper_perk_1")));
		list.add(TooltipHelper.getArrowDamageModifier(PERK_BUFF));

		list.add(TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.forest_keeper_perk_2",
				TooltipHelper.convertToEmbeddedElement(TooltipHelper.convertTicksToSeconds(PERK_DROP_INTERVAL)))));
		list.add(TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.forest_keeper_perk_3")));

		return list;
	}
}
