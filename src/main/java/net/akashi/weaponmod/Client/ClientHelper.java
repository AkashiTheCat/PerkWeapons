package net.akashi.weaponmod.Client;

import net.akashi.weaponmod.Bows.BaseBowItem;
import net.akashi.weaponmod.Util.ModelOverrides;
import net.minecraft.client.renderer.item.ItemProperties;

public class ClientHelper {
	public static <T extends BaseBowItem> void registerLongbowPropertyOverrides(T bowItem) {
		ItemProperties.register(bowItem, ModelOverrides.PULLING, (stack, world, living, value) ->
				living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0f : 0.0f);
		ItemProperties.register(bowItem, ModelOverrides.PULL, (stack, world, shooter, value) ->
				shooter != null && shooter.getUseItem() == stack ? bowItem.getDrawProgress(shooter) : 0.0f);
	}
}

