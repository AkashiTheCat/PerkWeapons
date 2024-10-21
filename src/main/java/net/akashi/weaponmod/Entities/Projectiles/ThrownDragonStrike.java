package net.akashi.weaponmod.Entities.Projectiles;

import net.akashi.weaponmod.Config.ModCommonConfigs;
import net.akashi.weaponmod.Config.Properties.DragonStrikeProperties;
import net.akashi.weaponmod.Registry.ModEffects;
import net.akashi.weaponmod.Spears.DragonStrikeItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import static net.akashi.weaponmod.Spears.DragonStrikeItem.*;

public class ThrownDragonStrike extends ThrownSpear {
	private int returnTime = 40;

	public ThrownDragonStrike(EntityType<? extends ThrownSpear> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.setNoGravity(true);
	}

	public ThrownDragonStrike(Level pLevel, LivingEntity pShooter, ItemStack pStack,
	                          int returnTime, EntityType<? extends ThrownSpear> spearType) {
		super(pLevel, pShooter, pStack, spearType);
		this.returnTime = returnTime;
		this.setNoGravity(true);
	}

	@Override
	public void tick() {
		if (returnTime <= 0) {
			this.dealtDamage = true;
		} else {
			returnTime--;
		}
		super.tick();
	}

	@Override
	protected void onHitBlock(BlockHitResult pResult) {
		spawnAreaEffect();
		super.onHitBlock(pResult);
	}

	@Override
	protected void onHitEntity(EntityHitResult pResult) {
		spawnAreaEffect();
		super.onHitEntity(pResult);
	}

	@Override
	public byte getLoyaltyLevel() {
		return 3;
	}

	public void spawnAreaEffect() {
		if (this.getOwner() instanceof Player player && !this.level().isClientSide()) {
			AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level(), this.getBlockX(), this.getBlockY(), this.getBlockZ());
			areaeffectcloud.setOwner(player);
			areaeffectcloud.setParticle(ParticleTypes.DRAGON_BREATH);
			areaeffectcloud.setRadius(INIT_AFFECT_CLOUD_RADIUS);
			areaeffectcloud.setDuration(AFFECT_CLOUD_DURATION);
			areaeffectcloud.setRadiusPerTick((MAX_AFFECT_CLOUD_RADIUS - areaeffectcloud.getRadius())
					/ (float) areaeffectcloud.getDuration());
			areaeffectcloud.addEffect(new MobEffectInstance(ModEffects.HARM_ALL.get(), 1, EFFECT_DAMAGE));
			this.level().addFreshEntity(areaeffectcloud);
		}
	}
}
