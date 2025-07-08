package net.akashi.perk_weapons.mixin;

import net.akashi.perk_weapons.Registry.ModAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Inject(method = "createLivingAttributes", at = @At("RETURN"), cancellable = true)
	private static void injectCustomAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
		AttributeSupplier.Builder builder = cir.getReturnValue();

		builder.add(ModAttributes.MAGIC_RESISTANCE.get(), 0);
		builder.add(ModAttributes.DAMAGE_RESISTANCE.get(), 0);

		cir.setReturnValue(builder);
	}
}
