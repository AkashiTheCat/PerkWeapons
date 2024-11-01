package net.akashi.perk_weapons.Entities.Projectiles.Arrows;

import net.akashi.perk_weapons.Util.EntityTypeListReader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StarShooterArrow extends BaseArrow {
	private static List<EntityType<? extends Mob>> FLYING_ENTITY = new ArrayList<>(Arrays.asList(
			EntityType.PHANTOM,
			EntityType.ENDER_DRAGON,
			EntityType.BLAZE
	));

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
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		if (!this.level().isClientSide() && pResult.getEntity() instanceof LivingEntity livingEntity) {
			BlockPos pos = livingEntity.getOnPos();
			if (FLYING_ENTITY.stream().anyMatch((entity) -> livingEntity.getType() == entity)) {
				this.setBaseDamage(getBaseDamage() * 2);
			} else {
				this.setBaseDamage(getBaseDamage() / 2);
			}
		}
		super.onHitEntity(pResult);
	}

	public static void updateEntityTypeListFromConfig(List<? extends String> EntityTypeStringList) {
		FLYING_ENTITY = EntityTypeListReader.convertStringsToEntityType((List<String>) EntityTypeStringList);
	}
}
