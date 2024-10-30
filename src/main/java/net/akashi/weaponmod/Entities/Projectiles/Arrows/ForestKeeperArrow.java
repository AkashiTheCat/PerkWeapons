package net.akashi.weaponmod.Entities.Projectiles.Arrows;

import net.akashi.weaponmod.Bows.ForestKeeperItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class ForestKeeperArrow extends BaseArrow {
	public ForestKeeperArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public ForestKeeperArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, double pX, double pY, double pZ) {
		super(pEntityType, pLevel, pX, pY, pZ);
	}

	public ForestKeeperArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, LivingEntity pShooter) {
		super(pEntityType, pLevel, pShooter);
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		if (!this.level().isClientSide()) {
			Entity owner = this.getOwner();
			if (owner instanceof Player player) {
				ForestKeeperItem bowItem = null;
				if (player.getMainHandItem().getItem() instanceof ForestKeeperItem fItem) {
					bowItem = fItem;
				} else if (player.getOffhandItem().getItem() instanceof ForestKeeperItem fItem) {
					bowItem = fItem;
				}
				if (bowItem != null) {
					bowItem.gainPerkLevel();
				}
			}
		}
		super.onHitEntity(pResult);
	}
}
