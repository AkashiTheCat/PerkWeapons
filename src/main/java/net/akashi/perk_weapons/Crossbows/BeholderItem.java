package net.akashi.perk_weapons.Crossbows;

import net.akashi.perk_weapons.Client.ClientHelper;
import net.akashi.perk_weapons.Client.Renderer.BeholderBeamRenderer;
import net.akashi.perk_weapons.Config.Properties.Crossbow.CrossbowProperties;
import net.akashi.perk_weapons.Config.Properties.Crossbow.BeholderProperties;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class BeholderItem extends BaseCrossbowItem {
	public static final String TAG_PERK_ACTIVATING = "perk_activate";
	public static final String TAG_LAST_SOUND_PLAYED = "last_played";
	private static final Predicate<Entity> isVisible =
			entity -> !entity.isSpectator() && entity.isPickable();
	public static int AFFECT_RANGE = 40;
	public static int SLOWNESS_LEVEL = 2;
	public static int WEAKNESS_LEVEL = 1;

	public BeholderItem(Properties pProperties) {
		super(pProperties);
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerOppressorPropertyOverrides(this);
	}

	public BeholderItem(int chargeTicks, float damage, float velocity,
	                    float inaccuracy, float speedModifier,
	                    boolean onlyMainHand, Properties pProperties) {
		super(chargeTicks, damage, velocity, inaccuracy, speedModifier, onlyMainHand, pProperties);
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerOppressorPropertyOverrides(this);
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
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		super.inventoryTick(stack, level, entity, slotId, isSelected);
		boolean isClient = level.isClientSide();
		if (!isSelected || !entity.isCrouching()) {
			if (!isClient && isPerkActivating(stack)) {
				setPerkActivating(stack, false);
			}
			if (isClient && BeholderBeamRenderer.RENDER_DATA_MAP.containsKey(entity.getId())) {
				BeholderBeamRenderer.removeRenderData(entity.getId());
			}
			return;
		}

		Vec3 viewVec = entity.getViewVector(1.0F).normalize();
		Vec3 from = entity.getEyePosition();

		Vec3 to = from.add(viewVec.scale(AFFECT_RANGE));
		AABB searchBox = entity.getBoundingBox().expandTowards(
				viewVec.scale(AFFECT_RANGE)).inflate(1.0D, 1.0D, 1.0D);
		BlockHitResult blockResult = level.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER,
				ClipContext.Fluid.NONE, entity));
		if (blockResult.getType() == HitResult.Type.BLOCK) {
			to = blockResult.getLocation();
		}
		EntityHitResult entityResult = ProjectileUtil.getEntityHitResult(entity, from, to, searchBox,
				isVisible, AFFECT_RANGE * AFFECT_RANGE);

		if (entityResult == null) {
			if (!isClient && isPerkActivating(stack)) {
				setPerkActivating(stack, false);
			}
			if (isClient && BeholderBeamRenderer.RENDER_DATA_MAP.containsKey(entity.getId())) {
				BeholderBeamRenderer.removeRenderData(entity.getId());
			}
			return;
		}


		if (entityResult.getEntity() instanceof LivingEntity target &&
				target.getBoundingBox().clip(from, to).isPresent()) {
			if (!level.isClientSide()) {
				if (level.getGameTime() % 20 == 0) {
					target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 40));
					target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, SLOWNESS_LEVEL - 1));
					target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, WEAKNESS_LEVEL - 1));
				} else {
					if (target.getActiveEffects().stream().noneMatch((effect) -> effect.getEffect() == MobEffects.GLOWING)) {
						target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 40));
					}
					if (target.getActiveEffects().stream().noneMatch((effect) -> effect.getEffect() == MobEffects.MOVEMENT_SLOWDOWN)) {
						target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, SLOWNESS_LEVEL - 1));
					}
					if (target.getActiveEffects().stream().noneMatch((effect) -> effect.getEffect() == MobEffects.WEAKNESS)) {
						target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, WEAKNESS_LEVEL - 1));
					}
				}
			}

			if (!isClient && !isPerkActivating(stack)) {
				setPerkActivating(stack, true);
				long time = level.getGameTime();
				if (time - getLastSoundPlayedTime(stack) > 60) {
					level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
							SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.PLAYERS, 0.6f, 0.9f);
					setLastSoundPlayedTime(stack, time);
				}
			}

			if (isClient && !BeholderBeamRenderer.RENDER_DATA_MAP.containsKey(entity.getId())) {
				BeholderBeamRenderer.addRenderData(entity.getId(), target.getId());
			}

			return;
		}

		if (!isClient && isPerkActivating(stack)) {
			setPerkActivating(stack, false);
		}
		if (isClient && BeholderBeamRenderer.RENDER_DATA_MAP.containsKey(entity.getId())) {
			BeholderBeamRenderer.removeRenderData(entity.getId());
		}
	}

	public void setPerkActivating(ItemStack stack, boolean isActivating) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putBoolean(TAG_PERK_ACTIVATING, isActivating);
	}

	public boolean isPerkActivating(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		return tag.contains(TAG_PERK_ACTIVATING) && tag.getBoolean(TAG_PERK_ACTIVATING);
	}

	public void setLastSoundPlayedTime(ItemStack stack, long time) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putLong(TAG_LAST_SOUND_PLAYED, time);
	}

	public long getLastSoundPlayedTime(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		return tag.contains(TAG_LAST_SOUND_PLAYED) ? tag.getLong(TAG_LAST_SOUND_PLAYED) : 0;
	}


	@Override
	public void updateAttributesFromConfig(CrossbowProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof BeholderProperties bProperties) {
			AFFECT_RANGE = bProperties.AFFECT_RANGE.get();
			SLOWNESS_LEVEL = bProperties.SLOWNESS_LEVEL.get();
			WEAKNESS_LEVEL = bProperties.WEAKNESS_LEVEL.get();
		}
	}

	@Override
	public Component getWeaponDescription(ItemStack stack, Level level) {
		return TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.beholder"));
	}

	@Override
	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		List<Component> list = new ArrayList<>();

		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.beholder_perk_1")));
		list.add(TooltipHelper.setSubPerkStyle(Component.translatable("tooltip.perk_weapons.beholder_perk_2")));
		list.add(TooltipHelper.setSubPerkStyle(Component.translatable("tooltip.perk_weapons.effect_format",
				MobEffects.MOVEMENT_SLOWDOWN.getDisplayName(),
				TooltipHelper.getRomanNumeral(SLOWNESS_LEVEL), 2.0)));
		list.add(TooltipHelper.setSubPerkStyle(Component.translatable("tooltip.perk_weapons.effect_format",
				MobEffects.WEAKNESS.getDisplayName(),
				TooltipHelper.getRomanNumeral(WEAKNESS_LEVEL), 2.0)));
		list.add(TooltipHelper.setSubPerkStyle(Component.translatable("tooltip.perk_weapons.effect_format",
				MobEffects.GLOWING.getDisplayName(),
				TooltipHelper.getRomanNumeral(1), 2.0)));

		return list;
	}
}
