package net.akashi.perk_weapons.Util;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ModExplosion {
	public static void createExplosion(Level level, LivingEntity src, double x, double y, double z,
	                                   float innerRadius, float outerRadius,
	                                   float innerDamage, float outerDamage, float knockBackForce,
	                                   boolean ignoreWall) {
		if (level.isClientSide())
			return;

		((ServerLevel) level).sendParticles(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1,
				0, 0, 0, 0);

		level.playSound(null, x, y, z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 2.0F,
				(1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F) * 0.7F);

		AABB EAABB = new AABB(x - outerRadius, y - outerRadius, z - outerRadius,
				x + outerRadius, y + outerRadius, z + outerRadius);
		List<Entity> entities = level.getEntities((Entity) null, EAABB, (e) -> true);

		for (Entity entity : entities) {
			double dx = entity.getX() - x;
			double dy = entity.getY() - y;
			double dz = entity.getZ() - z;
			double distanceSqr = dx * dx + dy * dy + dz * dz;

			if (distanceSqr > outerRadius * outerRadius)
				continue;

			float distance = (float) Math.sqrt(distanceSqr);
			float damage = innerDamage;
			if (distance > innerRadius) {
				float t = (distance - innerRadius) / (outerRadius - innerRadius);
				damage = innerDamage - (innerDamage - outerDamage) * t;
			}

			if (!ignoreWall) {
				AABB entityAABB = entity.getBoundingBox();
				List<Vec3> samplePoints = new ArrayList<>();
				samplePoints.add(entity.getPosition(1.0F));
				samplePoints.add(new Vec3(entityAABB.minX, entityAABB.minY, entityAABB.minZ));
				samplePoints.add(new Vec3(entityAABB.minX, entityAABB.minY, entityAABB.maxZ));
				samplePoints.add(new Vec3(entityAABB.minX, entityAABB.maxY, entityAABB.minZ));
				samplePoints.add(new Vec3(entityAABB.minX, entityAABB.maxY, entityAABB.maxZ));
				samplePoints.add(new Vec3(entityAABB.maxX, entityAABB.minY, entityAABB.minZ));
				samplePoints.add(new Vec3(entityAABB.maxX, entityAABB.minY, entityAABB.maxZ));
				samplePoints.add(new Vec3(entityAABB.maxX, entityAABB.maxY, entityAABB.minZ));
				samplePoints.add(new Vec3(entityAABB.maxX, entityAABB.maxY, entityAABB.maxZ));

				boolean valid = samplePoints.stream().anyMatch(p -> {
					ClipContext context = new ClipContext(
							new Vec3(x, y, z), p,
							ClipContext.Block.COLLIDER,
							ClipContext.Fluid.NONE,
							null
					);
					BlockHitResult result = level.clip(context);
					return result.getType() != HitResult.Type.BLOCK;
				});

				if (!valid) {
					continue;
				}
			}

			if (entity instanceof LivingEntity livingEntity) {
				DamageSource damageSource = level.damageSources().explosion(src, entity);
				livingEntity.hurt(damageSource, damage);

				double knockback = (1.0 - distance / outerRadius) * knockBackForce;
				Vec3 dir = new Vec3(dx, dy, dz).normalize().scale(knockback);
				livingEntity.push(dir.x, dir.y, dir.z);
			}
		}
	}
}
