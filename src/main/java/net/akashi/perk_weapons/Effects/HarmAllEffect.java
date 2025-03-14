package net.akashi.perk_weapons.Effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class HarmAllEffect extends MobEffect {
	public HarmAllEffect() {
		super(MobEffectCategory.HARMFUL, 0x000000);
	}

	@Override
	public void applyInstantenousEffect(@Nullable Entity pSource, @Nullable Entity pIndirectSource,
	                                    LivingEntity target, int pAmplifier, double pHealth) {
		if (target.isAlive())
			target.hurt(target.damageSources().magic(), pAmplifier);
	}

	@Override
	public boolean isInstantenous() {
		return true;
	}
}
