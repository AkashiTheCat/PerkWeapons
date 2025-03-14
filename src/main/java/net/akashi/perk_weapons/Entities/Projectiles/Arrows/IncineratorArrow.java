package net.akashi.perk_weapons.Entities.Projectiles.Arrows;

import net.akashi.perk_weapons.Crossbows.IncineratorItem;
import net.akashi.perk_weapons.PerkWeapons;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class IncineratorArrow extends BaseArrow {
	public static final ResourceLocation FIRE_ARROW_LOCATION =
			new ResourceLocation(PerkWeapons.MODID, "textures/entity/projectiles/fire_arrow.png");
	public IncineratorArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public IncineratorArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, double pX, double pY, double pZ) {
		super(pEntityType, pLevel, pX, pY, pZ);
	}

	public IncineratorArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, LivingEntity pShooter) {
		super(pEntityType, pLevel, pShooter);
	}

	@Override
	public ResourceLocation getArrowTexture() {
		return FIRE_ARROW_LOCATION;
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		pResult.getEntity().setSecondsOnFire(5);
		if (pResult.getEntity().isOnFire()) {
			this.setKnockback(this.getKnockback() + IncineratorItem.FIRE_ARROW_KNOCKBACK_BONUS);
		}
		super.onHitEntity(pResult);
	}
}
