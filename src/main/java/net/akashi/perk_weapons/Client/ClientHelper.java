package net.akashi.perk_weapons.Client;

import net.akashi.perk_weapons.Bows.BaseBowItem;
import net.akashi.perk_weapons.Crossbows.BaseCrossbowItem;
import net.akashi.perk_weapons.Util.IPerkItem;
import net.akashi.perk_weapons.Util.ModelOverrides;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;

public class ClientHelper {
	public static <T extends BaseBowItem> void registerBowPropertyOverrides(T bowItem) {
		ItemProperties.register(bowItem, ModelOverrides.PULLING, (stack, world, living, value) ->
				living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0f : 0.0f);
		ItemProperties.register(bowItem, ModelOverrides.PULL, (stack, world, shooter, value) ->
				shooter != null && shooter.getUseItem() == stack ? bowItem.getDrawProgress(shooter) : 0.0f);
	}

	public static <T extends IPerkItem> void registerPerkBowPropertyOverrides(T perkItem) {
		ItemProperties.register((Item) perkItem, ModelOverrides.PERK, (stack, world, entity, value) ->
				entity != null ? perkItem.getPerkLevel(entity, stack) : 0.0f);
		ItemProperties.register((Item) perkItem, ModelOverrides.PERK_MAX, (stack, world, entity, value) ->
				entity != null && perkItem.isPerkMax(entity, stack) ? 1.0f : 0.0f);
	}

	public static void registerCrossbowPropertyOverrides(BaseCrossbowItem crossbow) {
		ItemProperties.register(crossbow, ModelOverrides.PULL, (stack, world, entity, value) ->
		{
			if (entity != null)
				return crossbow.getChargeProgress(entity, stack);
			return 0.0f;
		});
		ItemProperties.register(crossbow, ModelOverrides.PULLING, (stack, world, entity, value) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0f : 0.0f);
		ItemProperties.register(crossbow, ModelOverrides.CHARGED, (stack, world, entity, value) ->
				BaseCrossbowItem.isCharged(stack) ? 1.0f : 0.0f);
	}
}

