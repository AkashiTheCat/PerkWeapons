package net.akashi.perk_weapons.Entities.Projectiles.Arrows;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class StarShooterArrow extends BaseArrow {
	public static float DAMAGE_MODIFIER_PER_METER = 0.03F;
	public static float DAMAGE_MODIFIER_MAX = 4.0F;
	public static float DAMAGE_MODIFIER_MIN = 0.0F;

	public StarShooterArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public StarShooterArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, double pX, double pY, double pZ) {
		super(pEntityType, pLevel, pX, pY, pZ);
	}

	public StarShooterArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, LivingEntity pShooter) {
		super(pEntityType, pLevel, pShooter);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide() && !this.inGround) {
			this.level().addParticle(ParticleTypes.END_ROD,
					this.getX(), this.getY(), this.getZ(),
					0.0D, 0.0D, 0.0D);
		}
		if (!this.level().isClientSide()) {
			this.flyDist += (float) getDeltaMovement().length();
		}
	}

	@Override
	protected void onHitBlock(BlockHitResult pResult) {
		super.onHitBlock(pResult);
		this.flyDist = 0;
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		if (!this.level().isClientSide() && pResult.getEntity() instanceof LivingEntity) {
			this.setBaseDamage(getBaseDamage() * (1 + Math.min(DAMAGE_MODIFIER_MAX,
					Math.max(this.flyDist * DAMAGE_MODIFIER_PER_METER, DAMAGE_MODIFIER_MIN))
			));
		}
		super.onHitEntity(pResult);
	}
}
