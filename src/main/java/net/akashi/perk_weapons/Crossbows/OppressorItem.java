package net.akashi.perk_weapons.Crossbows;

import net.akashi.perk_weapons.Config.Properties.Crossbow.CrossbowProperties;
import net.akashi.perk_weapons.Config.Properties.Crossbow.OppressorProperties;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class OppressorItem extends BaseCrossbowItem {
	private static final Predicate<Entity> isVisible =
			entity -> !entity.isSpectator() && entity.isPickable();
	public static int AFFECT_RANGE = 32;
	public static int SLOWNESS_LEVEL = 1;

	public OppressorItem(Properties pProperties) {
		super(pProperties);
	}

	public OppressorItem(int chargeTicks, float damage, float velocity,
	                     float inaccuracy, float speedModifier,
	                     boolean onlyMainHand, Properties pProperties) {
		super(chargeTicks, damage, velocity, inaccuracy, speedModifier, onlyMainHand, pProperties);
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		if (pHand != InteractionHand.MAIN_HAND) {
			return InteractionResultHolder.pass(itemstack);
		}
		return super.use(pLevel, pPlayer, pHand);
	}

	@Override
	public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
		super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
		if (slotIndex != selectedIndex || !player.isCrouching()) {
			return;
		}

		Vec3 viewVec = player.getViewVector(1.0F).normalize();
		Vec3 from = player.getEyePosition();

		Vec3 to = from.add(viewVec.scale(AFFECT_RANGE));
		AABB searchBox = player.getBoundingBox().expandTowards(
				viewVec.scale(AFFECT_RANGE)).inflate(1.0D, 1.0D, 1.0D);
		BlockHitResult blockResult = level.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER,
				ClipContext.Fluid.NONE, player));
		if (blockResult.getType() == HitResult.Type.BLOCK) {
			to = blockResult.getLocation();
		}
		EntityHitResult entityResult = ProjectileUtil.getEntityHitResult(player, from, to, searchBox,
				isVisible, AFFECT_RANGE * AFFECT_RANGE);
		if (entityResult == null) {
			return;
		}


		if (entityResult.getEntity() instanceof LivingEntity target &&
				target.getBoundingBox().clip(from, to).isPresent()) {
			if (!level.isClientSide()) {
				if (level.getGameTime() % 5 == 0) {
					level.playSound(null, player.getX(), player.getY(), player.getZ(),
							SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.7F, 0.8F);
				}
				if (level.getGameTime() % 20 == 0) {
					target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 40));
					target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1));
				} else {
					if (target.getActiveEffects().stream().noneMatch((effect) -> effect.getEffect() == MobEffects.GLOWING)) {
						target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 40));
					}
					if (target.getActiveEffects().stream().noneMatch((effect) -> effect.getEffect() == MobEffects.MOVEMENT_SLOWDOWN)) {
						target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, SLOWNESS_LEVEL - 1));
					}
				}
			} else {
				//Render particle trail
				Vec3 particleXYZ = new Vec3(player.getX(), player.getBoundingBox().getYsize() * 0.5 + player.getY(),
						player.getZ());
				Vec3 targetXYZ = new Vec3(target.getX(), target.getBoundingBox().getYsize() * 0.5 + target.getY(),
						target.getZ());
				float interval = 1.2F;
				Vec3 delta = targetXYZ.subtract(particleXYZ).normalize().scale(interval);
				double distanceSqr = particleXYZ.distanceTo(targetXYZ);
				int particleAmount = (int) Math.round(distanceSqr / interval);

				for (int i = 0; i < particleAmount; i++) {
					level.addParticle(ParticleTypes.ELECTRIC_SPARK,
							particleXYZ.x + (level.random.nextFloat() - 0.5) * 0.25,
							particleXYZ.y + (level.random.nextFloat() - 0.5) * 0.25,
							particleXYZ.z + (level.random.nextFloat() - 0.5) * 0.25,
							0, 0, 0);
					particleXYZ = particleXYZ.add(delta);
				}
			}
		}
	}

	@Override
	public void updateAttributesFromConfig(CrossbowProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof OppressorProperties oProperties) {
			AFFECT_RANGE = oProperties.AFFECT_RANGE.get();
			SLOWNESS_LEVEL = oProperties.SLOWNESS_LEVEL.get();
		}
	}
}
