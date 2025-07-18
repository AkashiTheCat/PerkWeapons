package net.akashi.perk_weapons.mixin;

import com.mojang.authlib.GameProfile;
import net.akashi.perk_weapons.Registry.ModTags;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin extends AbstractClientPlayer {
	@Shadow
	public Input input;

	public LocalPlayerMixin(ClientLevel pClientLevel, GameProfile pGameProfile) {
		super(pClientLevel, pGameProfile);
	}

	@Inject(method = "aiStep", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z", shift = At.Shift.BEFORE))
	public void BeforeIsUsingItemInvoked(CallbackInfo ci) {
		//When using item and player is not passenger, the vanilla method will multiply the impulse by 0.2
		if (!this.isPassenger()) {
			ItemStack itemStack = this.getUseItem();
			if (itemStack.is(ModTags.NO_USING_SLOWDOWN_TAG)) {
				//If item matched, this will make the impulse is multiplied back to its origin value
				this.input.leftImpulse *= 5;
				this.input.forwardImpulse *= 5;
			}
		}
	}
}