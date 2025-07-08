package net.akashi.perk_weapons.Crossbows;

import net.akashi.perk_weapons.Client.ClientHelper;
import net.akashi.perk_weapons.Config.Properties.Crossbow.CrossbowProperties;
import net.akashi.perk_weapons.Config.Properties.Crossbow.QueenBeeProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.BaseArrow;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.QueenBeeArrow;
import net.akashi.perk_weapons.Registry.ModEffects;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Util.IPerkItem;
import net.akashi.perk_weapons.Util.SoundEventHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;

public class QueenBeeCrossbowItem extends BaseCrossbowItem implements IPerkItem {
	public static final String TAG_PERK_LEVEL = "perk_level";

	private static int POISON_LEVEL = 4;
	private static int POISON_TICKS = 100;
	private static byte MAX_PERK_LEVEL = 7;
	private static int ROYAL_JELLY_LEVEL = 1;
	private static int ROYAL_JELLY_TICKS = 80;
	private static int CROUCH_USE_COOLDOWN_TICKS = 40;

	public QueenBeeCrossbowItem(Properties pProperties) {
		super(pProperties);
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerPerkItemPropertyOverrides(this);
	}

	public QueenBeeCrossbowItem(int maxChargeTicks, float damage, float velocity,
	                            float inaccuracy, int ammoCapacity, int fireInterval,
	                            float speedModifier, boolean onlyAllowMainHand,
	                            Properties pProperties) {
		super(maxChargeTicks, damage, velocity, inaccuracy, ammoCapacity, fireInterval,
				speedModifier, onlyAllowMainHand, pProperties);
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerPerkItemPropertyOverrides(this);
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {
		ItemStack stack = pPlayer.getItemInHand(pHand);
		if (pPlayer.isCrouching() && isPerkMax(pPlayer, stack)) {
			pPlayer.addEffect(new MobEffectInstance(
					ModEffects.ROYAL_JELLY.get(), ROYAL_JELLY_TICKS, ROYAL_JELLY_LEVEL - 1
			));
			setNbtPerkLevel(stack, 0);
			pPlayer.getCooldowns().addCooldown(stack.getItem(), CROUCH_USE_COOLDOWN_TICKS);
			pLevel.playSound(null, pPlayer, SoundEvents.HONEY_DRINK, SoundSource.PLAYERS,
					1F, 1F);
			return InteractionResultHolder.success(stack);
		}
		return super.use(pLevel, pPlayer, pHand);
	}

	@Override
	protected Projectile getProjectile(Level level, LivingEntity shooter, ItemStack crossbowStack) {
		BaseCrossbowItem crossbowItem = (BaseCrossbowItem) crossbowStack.getItem();
		ItemStack ammoStack = crossbowItem.getLastChargedProjectile(crossbowStack);

		if (ammoStack.is(Items.FIREWORK_ROCKET)) {
			return new FireworkRocketEntity(level, ammoStack, shooter, shooter.getX(),
					shooter.getEyeY() - (double) 0.15F, shooter.getZ(), true);
		}

		BaseArrow arrow = new QueenBeeArrow(ModEntities.QUEEN_BEE_ARROW.get(), level, shooter);
		if (ammoStack.is(Items.SPECTRAL_ARROW)) {
			arrow.setSpectralArrow(true);
		} else {
			arrow.setEffectsFromItem(ammoStack);
		}
		arrow.setShotFromCrossbow(true);

		arrow.addEffect(new MobEffectInstance(
				MobEffects.POISON, POISON_TICKS, POISON_LEVEL - 1
		));
		return arrow;
	}

	@Override
	public byte getMaxPerkLevel() {
		return MAX_PERK_LEVEL;
	}

	@Override
	public void gainPerkLevel(LivingEntity entity, ItemStack stack) {
		setNbtPerkLevel(stack, Math.min(getNbtPerkLevel(stack) + 1, getMaxPerkLevel()));
	}

	@Override
	public float getPerkLevel(LivingEntity entity, ItemStack stack) {
		return getNbtPerkLevel(stack);
	}

	@Override
	public boolean isPerkMax(LivingEntity entity, ItemStack stack) {
		return getNbtPerkLevel(stack) == getMaxPerkLevel();
	}

	private void setNbtPerkLevel(ItemStack stack, int level) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putByte(TAG_PERK_LEVEL, (byte) level);
	}

	private int getNbtPerkLevel(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		return tag.getByte(TAG_PERK_LEVEL);
	}

	@Override
	protected @NotNull SoundEventHolder getStartSound(ItemStack crossbowStack) {
		return new SoundEventHolder(SoundEvents.BEE_POLLINATE, 1.0F, 1.0F);
	}

	@Override
	protected @NotNull SoundEventHolder getMiddleSound(ItemStack crossbowStack) {
		return SoundEventHolder.empty();
	}

	@Override
	protected @NotNull SoundEventHolder getEndSound(ItemStack crossbowStack) {
		return SoundEventHolder.empty();
	}

	@Override
	protected @NotNull SoundEventHolder getShootSound(ItemStack crossbowStack) {
		return new SoundEventHolder(SoundEvents.BEE_STING, 0.7F, 1.0F);
	}

	@Override
	public void updateAttributesFromConfig(CrossbowProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof QueenBeeProperties qProperties) {
			POISON_LEVEL = qProperties.POISON_LEVEL.get();
			POISON_TICKS = qProperties.POISON_DURATION.get();
			MAX_PERK_LEVEL = qProperties.MAX_PERK_LEVEL.get().byteValue();
			ROYAL_JELLY_LEVEL = qProperties.ROYAL_JELLY_LEVEL.get();
			ROYAL_JELLY_TICKS = qProperties.ROYAL_JELLY_DURATION.get();
			CROUCH_USE_COOLDOWN_TICKS = qProperties.COOLDOWN_CROUCH_USE.get();
		}
	}
}
