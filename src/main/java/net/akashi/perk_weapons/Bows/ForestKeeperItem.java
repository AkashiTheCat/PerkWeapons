package net.akashi.perk_weapons.Bows;

import net.akashi.perk_weapons.Client.ClientHelper;
import net.akashi.perk_weapons.Config.ModCommonConfigs;
import net.akashi.perk_weapons.Config.Properties.Bow.BowProperties;
import net.akashi.perk_weapons.Config.Properties.Bow.ForestKeeperProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.PerkUpdateArrow;
import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Registry.ModItems;
import net.akashi.perk_weapons.Util.INoUseSlowdownItem;
import net.akashi.perk_weapons.Util.IPerkItem;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpectralArrowItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

import javax.tools.Tool;
import java.util.*;

import static net.minecraft.world.item.enchantment.Enchantments.PUNCH_ARROWS;

public class ForestKeeperItem extends BaseBowItem implements IPerkItem, INoUseSlowdownItem {
	public static final String TAG_LAST_PERK_LEVEL_CHANGE_TIME = "last_perk_change";
	public static final String TAG_PERK_LEVEL = "perk_level";
	public static byte MAX_PERK_LEVEL = 5;
	public static float PERK_BUFF = 0.1F;
	public static int PERK_DROP_INTERVAL = 40;
	public static boolean ENABLE_SLOWDOWN_REMOVAL = true;

	public ForestKeeperItem(Properties properties) {
		super(properties);
		RemoveGeneralEnchant(PUNCH_ARROWS);
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerPerkBowPropertyOverrides(this);
	}

	public ForestKeeperItem(int drawTime, float projectileDamage, float velocity,
	                        float inaccuracy, float speedModifier, float zoomFactor,
	                        boolean onlyMainHand, Properties properties) {
		super(drawTime, projectileDamage, velocity, inaccuracy, speedModifier, zoomFactor, onlyMainHand, properties);
		RemoveGeneralEnchant(PUNCH_ARROWS);
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerPerkBowPropertyOverrides(this);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		super.inventoryTick(stack, level, entity, slotId, isSelected);
		int nbtPerkLevel = getNbtPerkLevel(stack);
		if (nbtPerkLevel == 0)
			return;

		if (Math.ceil(getPerkLevel((LivingEntity) entity, stack)) < nbtPerkLevel) {
			setNbtPerkLevel(stack, nbtPerkLevel - 1);
			setLastPerkChangeTime(stack, level.getGameTime());
		}
	}

	@Override
	public AbstractArrow createArrow(Level level, ArrowItem arrowItem, ItemStack bowStack, ItemStack arrowStack,
	                                 Player player) {
		PerkUpdateArrow arrow = new PerkUpdateArrow(ModEntities.PERK_UPDATE_ARROW.get(), level, player);
		if (arrowItem instanceof SpectralArrowItem) {
			arrow.setSpectralArrow(true);
		} else {
			arrow.setEffectsFromItem(arrowStack);
		}

		arrow.setBaseDamage(PROJECTILE_DAMAGE * (1 + PERK_BUFF * getNbtPerkLevel(bowStack)) / VELOCITY);
		return arrow;
	}

	@Override
	public void updateAttributesFromConfig(BowProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof ForestKeeperProperties fProperties) {
			MAX_PERK_LEVEL = fProperties.MAX_PERK_LEVEL.get().byteValue();
			PERK_DROP_INTERVAL = fProperties.PERK_DROP_INTERVAL.get();
			PERK_BUFF = fProperties.PERK_DAMAGE_BUFF.get().floatValue();
			ENABLE_SLOWDOWN_REMOVAL = fProperties.ENABLE_SLOWDOWN_REMOVAL.get();
		}
	}

	@Override
	public byte getMaxPerkLevel() {
		return MAX_PERK_LEVEL;
	}

	@Override
	public float getPerkLevel(LivingEntity entity, ItemStack stack) {
		long timeElapsed = entity.level().getGameTime() - getLastPerkChangeTime(stack);
		return Math.max(getNbtPerkLevel(stack) - (float) timeElapsed / PERK_DROP_INTERVAL, 0);
	}

	@Override
	public boolean isPerkMax(LivingEntity entity, ItemStack stack) {
		return ((byte) Math.ceil(getPerkLevel(entity, stack))) == MAX_PERK_LEVEL;
	}

	@Override
	public void gainPerkLevel(LivingEntity entity, ItemStack stack) {
		int level = getNbtPerkLevel(stack);
		setLastPerkChangeTime(stack, entity.level().getGameTime());
		if (level < getMaxPerkLevel()) {
			setNbtPerkLevel(stack, level + 1);
		}
	}

	public void setNbtPerkLevel(ItemStack stack, int level) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putInt(TAG_PERK_LEVEL, level);
	}

	public int getNbtPerkLevel(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		return tag.contains(TAG_PERK_LEVEL) ? tag.getInt(TAG_PERK_LEVEL) : 0;
	}

	public void setLastPerkChangeTime(ItemStack stack, long time) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putLong(TAG_LAST_PERK_LEVEL_CHANGE_TIME, time);
	}

	public long getLastPerkChangeTime(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		return tag.contains(TAG_LAST_PERK_LEVEL_CHANGE_TIME) ? tag.getLong(TAG_LAST_PERK_LEVEL_CHANGE_TIME) : 0;
	}

	@SubscribeEvent
	public static void onEntityHurt(LivingHurtEvent event) {
		LivingEntity entity = event.getEntity();
		if (!entity.level().isClientSide()) {
			ItemStack stack = ItemStack.EMPTY;
			if (entity.getMainHandItem().getItem() instanceof ForestKeeperItem) {
				stack = entity.getMainHandItem();
			} else if (entity.getOffhandItem().getItem() instanceof ForestKeeperItem) {
				stack = entity.getOffhandItem();
			}

			if (!stack.isEmpty()) {

			}
		}
	}

	@Override
	public Component getWeaponDescription(ItemStack stack, Level level) {
		return TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.forest_keeper"));
	}

	@Override
	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		List<Component> list = new ArrayList<>();

		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.forest_keeper_perk_1")));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.forest_keeper_perk_2",
				TooltipHelper.convertToEmbeddedElement(1))));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.forest_keeper_perk_3",
				TooltipHelper.convertToEmbeddedElement(getMaxPerkLevel()))));
		list.add(TooltipHelper.setSubPerkStyle(Component.translatable("tooltip.perk_weapons.forest_keeper_perk_4",
				TooltipHelper.convertToEmbeddedElement(TooltipHelper.convertTicksToSeconds(PERK_DROP_INTERVAL)))));
		list.add(TooltipHelper.setDebuffStyle(Component.translatable("tooltip.perk_weapons.forest_keeper_perk_5")));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.forest_keeper_perk_6")));
		list.add(TooltipHelper.getArrowDamageModifier(PERK_BUFF));

		return list;
	}
}
