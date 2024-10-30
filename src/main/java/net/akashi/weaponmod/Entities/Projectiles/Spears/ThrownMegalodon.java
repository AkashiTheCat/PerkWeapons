package net.akashi.weaponmod.Entities.Projectiles.Spears;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class ThrownMegalodon extends ThrownSpear {
	public ThrownMegalodon(EntityType<? extends ThrownSpear> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public ThrownMegalodon(Level pLevel, LivingEntity pShooter, ItemStack pStack, EntityType<? extends ThrownSpear> spearType) {
		super(pLevel, pShooter, pStack, spearType);
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		Entity entity1 = this.getOwner();
		if (entity1 instanceof Player){
			((Player) entity1).addEffect(new MobEffectInstance(MobEffects.DIG_SPEED,120,0));
			((Player) entity1).addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST,120,0));
			((Player) entity1).addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE,120,0));
		}
		super.onHitEntity(pResult);
	}

	@Override
	protected float getWaterInertia() {
		return 0.99F;
	}
}
