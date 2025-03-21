package net.akashi.perk_weapons.Entities.Projectiles.Spears;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import static net.akashi.perk_weapons.Spears.ScourgeItem.*;

public class ThrownScourge extends ThrownSpear {
	public ThrownScourge(EntityType<? extends ThrownSpear> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public ThrownScourge(Level pLevel, LivingEntity pShooter, ItemStack pStack, EntityType<? extends ThrownSpear> spearType) {
		super(pLevel, pShooter, pStack, spearType);
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		if (pResult.getEntity() instanceof LivingEntity entity) {
			entity.addEffect(new MobEffectInstance(MobEffects.WITHER, WITHER_DURATION, WITHER_LEVEL - 1));
			entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, SLOWNESS_DURATION, SLOWNESS_LEVEL - 1));
		}
		super.onHitEntity(pResult);
	}

	@Override
	public byte getLoyaltyLevel() {
		return pickup == Pickup.ALLOWED ? super.getLoyaltyLevel() : 0;
	}
}
