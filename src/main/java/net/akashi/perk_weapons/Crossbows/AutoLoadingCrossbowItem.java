package net.akashi.perk_weapons.Crossbows;

import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AutoLoadingCrossbowItem extends BaseCrossbowItem {
	protected static final String TAG_RELOAD_BEGIN = "reload_begin";

	public AutoLoadingCrossbowItem(Properties pProperties) {
		super(pProperties);
	}

	public AutoLoadingCrossbowItem(int maxChargeTicks, float damage, float velocity,
	                               float inaccuracy, int ammoCapacity, int fireInterval,
	                               float speedModifier, boolean onlyAllowMainHand,
	                               Properties pProperties) {
		super(maxChargeTicks, damage, velocity, inaccuracy, ammoCapacity, fireInterval,
				speedModifier, onlyAllowMainHand, pProperties);
	}


	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		if (onlyAllowMainHand && pHand != InteractionHand.MAIN_HAND) {
			return InteractionResultHolder.pass(itemstack);
		}

		if (isCrossbowCharged(itemstack)) {
			shoot(pLevel, pPlayer, pHand, itemstack, DAMAGE, VELOCITY, INACCURACY);
			consumeAndSetCharged(pPlayer, itemstack);
			return InteractionResultHolder.consume(itemstack);
		} else {
			return InteractionResultHolder.fail(itemstack);
		}
	}

	@Override
	public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity,
	                          int slotId, boolean isSelected) {
		super.inventoryTick(stack, level, entity, slotId, isSelected);
		if (level.isClientSide() || !(entity instanceof LivingEntity e)) {
			return;
		}

		if (getChargedProjectileAmount(stack) < AMMO_CAPACITY && tryLoadAmmo(e, stack)) {
			setReloadBeginTime(stack, level.getGameTime());
			return;
		}

		if (!isCrossbowCharged(stack) && isAmmoLoaded(stack)) {
			if (isSelected || e.getOffhandItem().is(this)) {
				this.onUseTick(level, e, stack, 0);
			}

			if (getChargeProgress(e, stack) >= 1.0f)
				setCrossbowCharged(stack, true);
		}
	}

	@Override
	public float getChargeProgress(LivingEntity shooter, ItemStack crossbowStack) {
		long passedTime = shooter.level().getGameTime() - getReloadBeginTime(crossbowStack);
		return isAmmoLoaded(crossbowStack) ? Math.min((float) passedTime / getMaxChargeTicks(crossbowStack), 1.0f) : 0;
	}

	public boolean isAmmoLoaded(ItemStack crossbowStack) {
		return getLastChargedProjectile(crossbowStack) != ItemStack.EMPTY;
	}

	public long getReloadBeginTime(ItemStack crossbowStack) {
		CompoundTag tag = crossbowStack.getOrCreateTag();
		return tag.contains(TAG_RELOAD_BEGIN) ? tag.getLong(TAG_RELOAD_BEGIN) : 0;
	}

	public void setReloadBeginTime(ItemStack crossbowStack, long reloadBeginTime) {
		CompoundTag tag = crossbowStack.getOrCreateTag();
		tag.putLong(TAG_RELOAD_BEGIN, reloadBeginTime);
	}

	@Override
	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		var list = super.getPerkDescriptions(stack, level);
		list.add(TooltipHelper.setEmbeddedElementStyle(Component.translatable("tooltip.perk_weapons.auto_loading_crossbow_perk")));
		return list;
	}
}
