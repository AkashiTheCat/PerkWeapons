package net.akashi.perk_weapons.Client;

import net.akashi.perk_weapons.Bows.BaseBowItem;
import net.akashi.perk_weapons.Crossbows.BaseCrossbowItem;
import net.akashi.perk_weapons.Crossbows.BeholderItem;
import net.akashi.perk_weapons.Crossbows.LiberatorItem;
import net.akashi.perk_weapons.Registry.ModEnchantments;
import net.akashi.perk_weapons.Spears.BaseSpearItem;
import net.akashi.perk_weapons.Spears.NetherGuideItem;
import net.akashi.perk_weapons.Spears.ScourgeItem;
import net.akashi.perk_weapons.Util.IPerkItem;
import net.akashi.perk_weapons.Util.ModelOverrides;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public class ClientHelper {
	public static void registerSpearPropertyOverrides(BaseSpearItem spearItem) {
		ItemProperties.register(spearItem, ModelOverrides.CHARGING, (stack, level, entity, value) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0f : 0.0f);
	}

	public static void registerBowPropertyOverrides(BaseBowItem bowItem) {
		ItemProperties.register(bowItem, ModelOverrides.PULLING, (stack, world, living, value) ->
				living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0f : 0.0f);
		ItemProperties.register(bowItem, ModelOverrides.PULL, (stack, world, shooter, value) ->
				shooter != null && shooter.getUseItem() == stack ? bowItem.getDrawProgress(shooter) : 0.0f);
	}

	public static <T extends Item & IPerkItem> void registerPerkItemPropertyOverrides(T perkItem) {
		ItemProperties.register(perkItem, ModelOverrides.PERK, (stack, world, entity, value) ->
				entity != null ? perkItem.getPerkLevel(entity, stack) : 0.0f);
		ItemProperties.register(perkItem, ModelOverrides.PERK_MAX, (stack, world, entity, value) ->
				entity != null && perkItem.isPerkMax(entity, stack) ? 1.0f : 0.0f);
	}

	public static void registerOppressorPropertyOverrides(BeholderItem Item) {
		ItemProperties.register(Item, ModelOverrides.PERK, (stack, world, entity, value) ->
				entity != null && Item.isPerkActivating(stack) ? 1.0F : 0.0F);
	}

	public static void registerScourgePropertyOverrides(ScourgeItem Item) {
		ItemProperties.register(Item, ModelOverrides.PERK, (stack, world, entity, value) ->
				entity != null && Item.isBuffed(stack) ? 1.0F : 0.0F);
	}

	public static void registerLiberatorPropertyOverrides(LiberatorItem Item) {
		ItemProperties.register(Item, ModelOverrides.REGICIDE, (stack, world, entity, value) ->
				entity != null && Item.getEnchantmentLevel(stack, ModEnchantments.REGICIDE.get()) > 0 ? 1.0F : 0.0F);
	}

	public static void registerCrossbowPropertyOverrides(BaseCrossbowItem crossbow) {
		ItemProperties.register(crossbow, ModelOverrides.PULL, (stack, world, entity, value) -> {
			if (entity != null)
				return crossbow.getChargeProgress(entity, stack);
			return 0.0f;
		});
		ItemProperties.register(crossbow, ModelOverrides.PULLING, (stack, world, entity, value) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0f : 0.0f);
		ItemProperties.register(crossbow, ModelOverrides.CHARGED, (stack, world, entity, value) ->
				crossbow.isCrossbowCharged(stack) ? 1.0f : 0.0f);
		ItemProperties.register(crossbow, ModelOverrides.FIREWORK, (stack, world, entity, value) ->
				crossbow.isFireworkCharged(stack) ? 1.0f : 0.0f);
	}

	public static void registerNetherGuidePropertyOverrides(NetherGuideItem Item) {
		ItemProperties.register(Item, ModelOverrides.SWITCHING, (stack, world, entity, value) -> {
			if (entity instanceof Player player && player.getCooldowns().isOnCooldown(Item)) {
				return 1.0f;
			}
			return 0.0f;
		});
	}
}

