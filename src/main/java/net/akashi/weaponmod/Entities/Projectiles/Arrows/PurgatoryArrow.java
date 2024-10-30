package net.akashi.weaponmod.Entities.Projectiles.Arrows;

import net.akashi.weaponmod.Registry.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.PlayMessages;

public class PurgatoryArrow extends BaseArrow {
	public PurgatoryArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public PurgatoryArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, double pX, double pY, double pZ) {
		super(pEntityType, pLevel, pX, pY, pZ);
	}

	public PurgatoryArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, LivingEntity pShooter) {
		super(pEntityType, pLevel, pShooter);
	}

	public PurgatoryArrow(PlayMessages.SpawnEntity spawnEntity, Level level){
		this(ModEntities.PURGATORY_ARROW.get(), level);
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		Entity entity = pResult.getEntity();
		if (entity.getType() != EntityType.ENDERMAN) {
			entity.setSecondsOnFire(5);
		}
		super.onHitEntity(pResult);
	}

	@Override
	public void tick() {
		if(this.level().isClientSide()){
			this.level().addParticle(ParticleTypes.FLAME,
					this.getX(), this.getY(), this.getZ(),
					0.0D, 0.0D, 0.0D);
		}
		super.tick();
	}
}
