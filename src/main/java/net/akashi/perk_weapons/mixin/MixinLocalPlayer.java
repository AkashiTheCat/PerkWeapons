package net.akashi.perk_weapons.mixin;

import net.akashi.perk_weapons.Bows.ForestKeeperItem;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({LocalPlayer.class})
public class MixinLocalPlayer {
	public MixinLocalPlayer() {
	}

	@Redirect(method = {"aiStep"},
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z",
					ordinal = 0))
	public boolean aiStep(LocalPlayer localPlayer) {
		if (ForestKeeperItem.ENABLE_SLOWDOWN_REMOVAL &&
				(localPlayer.getUseItem().getItem() instanceof ForestKeeperItem)) {
			return false;
		} else {
			return localPlayer.isUsingItem();
		}
	}
}