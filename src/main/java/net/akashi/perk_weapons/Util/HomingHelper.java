package net.akashi.perk_weapons.Util;

import net.akashi.perk_weapons.Config.ModCommonConfigs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class HomingHelper {
	public static void applyHoming(
			Projectile projectile,
			Supplier<Entity> targetGetter,
			Consumer<Entity> targetSetter,
			float homingRange,
			float maxHomingAngleCos,
			float maxTurnAngleCos,
			float maxTurnAngleSin,
			float homingAcceleration) {
		Entity target = targetGetter.get();
		if (target == null || !target.isAlive()) {
			targetSetter.accept(getClosestValidTarget(projectile, homingRange, maxHomingAngleCos));
			return;
		}
		if (target.distanceToSqr(projectile.getX(), projectile.getY(), projectile.getZ()) > homingRange * homingRange) {
			targetSetter.accept(null);
			return;
		}
		Vec3 motionVec = getMotionVec(projectile);
		double velocity = motionVec.length() + homingAcceleration;

		Vec3 motionVecNormalized = motionVec.normalize();
		Vec3 vecToTarget = getVectorToEntity(projectile, target).normalize();

		double cosTheta = motionVecNormalized.dot(motionVecNormalized);
		if (cosTheta <= maxHomingAngleCos) {
			targetSetter.accept(null);
			return;
		}
		if (cosTheta >= maxTurnAngleCos) {
			projectile.setDeltaMovement(vecToTarget.scale(velocity));
			return;
		}

		Vec3 axis = motionVec.cross(vecToTarget);
		if (axis.lengthSqr() < 1e-6) {
			axis = new Vec3(0, 1, 0);
		} else {
			axis.normalize();
		}

		double dot = axis.dot(motionVecNormalized);
		Vec3 cross = axis.cross(motionVecNormalized);

		Vec3 newMovement = motionVecNormalized.scale(maxTurnAngleCos)
				.add(cross.scale(maxTurnAngleSin))
				.add(axis.scale(dot * (1 - maxTurnAngleCos)))
				.normalize().scale(velocity);
		projectile.setDeltaMovement(newMovement);
	}

	@Nullable
	public static LivingEntity getClosestValidTarget(Projectile projectile, float range, float cosAngle) {
		AABB homingBB = projectile.getBoundingBox().inflate(range);
		Level level = projectile.level();
		Vec3 from = new Vec3(projectile.getX(), projectile.getY(), projectile.getZ());
		Vec3 motionVecNormalized = getMotionVec(projectile).normalize();
		List<Entity> blackListEntities = new ArrayList<>(List.of(projectile));
		if (projectile.getOwner() != null) {
			blackListEntities.add(projectile.getOwner());
		}

		Function<Entity, Boolean> entityValidator = entity -> {
			if (blackListEntities.contains(entity) || !entity.closerThan(projectile, range))
				return false;
			Vec3 to = entity.getEyePosition();
			Vec3 vecToE = to.subtract(from).normalize();
			if (motionVecNormalized.dot(vecToE) < cosAngle)
				return false;

			BlockHitResult blockResult = level.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER,
					ClipContext.Fluid.NONE, projectile));
			return blockResult.getType() != HitResult.Type.BLOCK;
		};

		List<LivingEntity> validEntities;
		if (ModCommonConfigs.HOMING_WEAPON_ONLY_TRACK_MONSTERS.get()) {
			validEntities = new ArrayList<>(level.getEntitiesOfClass(Monster.class,
					homingBB, entityValidator::apply));
		} else {
			validEntities = level.getEntitiesOfClass(LivingEntity.class,
					homingBB, entityValidator::apply);
		}
		if (validEntities.isEmpty()) {
			return null;
		}

		return validEntities.stream().min(Comparator.comparingDouble(projectile::distanceToSqr))
				.orElse(null);
	}

	private static Vec3 getVectorToEntity(Entity from, Entity to) {
		if (to == null || from == null)
			return Vec3.ZERO;
		return new Vec3(
				to.getX() - from.getX(),
				(to.getY() + to.getEyeHeight()) - from.getY(),
				to.getZ() - from.getZ()
		);
	}

	private static Vec3 getMotionVec(Entity e) {
		return new Vec3(e.getDeltaMovement().x(), e.getDeltaMovement().y(), e.getDeltaMovement().z());
	}
}
