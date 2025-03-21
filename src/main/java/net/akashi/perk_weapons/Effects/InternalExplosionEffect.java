package net.akashi.perk_weapons.Effects;

import net.akashi.perk_weapons.Config.Properties.ModExplosionProperties;
import net.akashi.perk_weapons.Util.ModExplosion;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class InternalExplosionEffect extends MobEffect {
	public static float EXP_INNER_R = 2;
	public static float EXP_INNER_DMG = 30;
	public static float EXP_OUTER_R = 5;
	public static float EXP_OUTER_DMG = 5;
	public static float EXP_KNOCKBACK = 1;
	public static boolean EXP_IGNORE_WALL = false;

	public InternalExplosionEffect() {
		super(MobEffectCategory.HARMFUL, 0xb70000);
	}

	@Override
	public boolean isBeneficial() {
		return false;
	}

	@Override
	public void applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
		ModExplosion.createExplosion(livingEntity.level(), null,
				livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
				EXP_INNER_R + 0.25F * amplifier, EXP_OUTER_R + 0.5F * amplifier,
				EXP_INNER_DMG + 5 * amplifier, EXP_OUTER_DMG + 2 * amplifier,
				EXP_KNOCKBACK + 0.2F * amplifier, EXP_IGNORE_WALL);
	}

	@Override
	public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
		return pDuration == 2;
	}

	public static void updateAttributesFromConfig(ModExplosionProperties IProperties) {
		EXP_INNER_R = IProperties.INNER_R.get().floatValue();
		EXP_OUTER_R = IProperties.OUTER_R.get().floatValue();
		EXP_INNER_DMG = IProperties.INNER_DMG.get().floatValue();
		EXP_OUTER_DMG = IProperties.OUTER_DMG.get().floatValue();
		EXP_KNOCKBACK = IProperties.KNOCKBACK.get().floatValue();
		EXP_IGNORE_WALL = IProperties.IGNORE_WALL.get();
	}
}
