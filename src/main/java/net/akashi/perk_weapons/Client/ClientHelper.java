package net.akashi.perk_weapons.Client;

import net.akashi.perk_weapons.Bows.BaseBowItem;
import net.akashi.perk_weapons.Util.IPerkItem;
import net.akashi.perk_weapons.Util.ModelOverrides;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public class ClientHelper {
	public static <T extends BaseBowItem> void registerBowPropertyOverrides(T bowItem) {
		ItemProperties.register(bowItem, ModelOverrides.PULLING, (stack, world, living, value) ->
				living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0f : 0.0f);
		ItemProperties.register(bowItem, ModelOverrides.PULL, (stack, world, shooter, value) ->
				shooter != null && shooter.getUseItem() == stack ? bowItem.getDrawProgress(shooter) : 0.0f);
	}
	public static <T extends IPerkItem> void registerPerkBowPropertyOverrides(T perkItem) {
		ItemProperties.register((Item) perkItem, ModelOverrides.PERK, (stack, world, player, value) ->
				player != null ? perkItem.getPerkLevel((Player) player,stack) : 0.0f);
		ItemProperties.register((Item) perkItem, ModelOverrides.PERK_MAX, (stack, world, player, value) ->
				player != null && perkItem.isPerkMax((Player) player,stack) ? 1.0f : 0.0f);
	}
}

