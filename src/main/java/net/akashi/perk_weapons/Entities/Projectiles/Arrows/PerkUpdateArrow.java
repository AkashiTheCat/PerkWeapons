package net.akashi.perk_weapons.Entities.Projectiles.Arrows;

import net.akashi.perk_weapons.Util.IPerkItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class PerkUpdateArrow extends BaseArrow {
	public PerkUpdateArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public PerkUpdateArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, double pX, double pY, double pZ) {
		super(pEntityType, pLevel, pX, pY, pZ);
	}

	public PerkUpdateArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, LivingEntity pShooter) {
		super(pEntityType, pLevel, pShooter);
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		if (!this.level().isClientSide()) {
			Entity owner = this.getOwner();
			if (owner instanceof Player player) {
				IPerkItem perkItem = null;
				if (player.getMainHandItem().getItem() instanceof IPerkItem fItem) {
					perkItem = fItem;
				} else if (player.getOffhandItem().getItem() instanceof IPerkItem fItem) {
					perkItem = fItem;
				}
				if (perkItem != null) {
					perkItem.gainPerkLevel();
				}
			}
		}
		super.onHitEntity(pResult);
	}
}
