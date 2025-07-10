package net.akashi.perk_weapons.Entities.Projectiles.Arrows;

import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

public class PurgatoryArrow extends BaseArrow {
	public static final ResourceLocation FIRE_ARROW_LOCATION =
			new ResourceLocation(PerkWeapons.MODID, "textures/entity/projectiles/fire_arrow.png");
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
	public ResourceLocation getArrowTexture() {
		return FIRE_ARROW_LOCATION;
	}

	@Override
	protected void onHitEntity(@NotNull EntityHitResult pResult) {
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
