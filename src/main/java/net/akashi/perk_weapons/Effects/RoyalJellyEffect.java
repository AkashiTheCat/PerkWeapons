package net.akashi.perk_weapons.Effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class RoyalJellyEffect extends MobEffect {
	public RoyalJellyEffect() {
		super(MobEffectCategory.HARMFUL, 0xf9c344);
	}

	@Override
	public boolean isBeneficial() {
		return true;
	}

	@Override
	public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
		return pDuration % 10 == 0;
	}

	@Override
	public void applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
		pLivingEntity.heal(1 + 0.5F * pAmplifier);

		for (MobEffectInstance effectInstance : pLivingEntity.getActiveEffects()) {
			MobEffect effect = effectInstance.getEffect();
			if (!effect.getCategory().equals(MobEffectCategory.HARMFUL)) {
				pLivingEntity.removeEffect(effect);
			}
		}

		if (pLivingEntity instanceof Player p) {
			p.getFoodData().eat((1 + pAmplifier / 2), 1F);
		}
	}
}
