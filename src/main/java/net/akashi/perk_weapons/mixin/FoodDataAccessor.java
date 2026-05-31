package net.akashi.perk_weapons.mixin;

import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = FoodData.class, remap = false)
public interface FoodDataAccessor {
	@Accessor("tickTimer")
	int perk_weapons$getTickTimer();
}
