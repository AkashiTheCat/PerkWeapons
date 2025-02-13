package net.akashi.perk_weapons.Crossbows;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.List;

public class MagFedCrossbowItem extends BaseCrossbowItem {
	public MagFedCrossbowItem(Properties pProperties) {
		super(pProperties);
	}

	public MagFedCrossbowItem(int maxChargeTicks, float damage, float velocity, float inaccuracy, float speedModifier,
	                          int ammoCapacity, boolean onlyAllowMainHand, Properties pProperties) {
		super(maxChargeTicks, damage, velocity, inaccuracy, speedModifier, onlyAllowMainHand, pProperties);
		this.AMMO_CAPACITY = ammoCapacity;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		if (pPlayer.isCrouching()) {
			ItemStack itemstack = pPlayer.getItemInHand(pHand);
			if (!pPlayer.getProjectile(itemstack).isEmpty() &&
					getChargedProjectileAmount(itemstack) < getAmmoCapacity(itemstack)) {
				setCrossbowCharged(itemstack, false);
				pPlayer.startUsingItem(pHand);
				return InteractionResultHolder.consume(itemstack);
			}
			return InteractionResultHolder.pass(itemstack);
		}
		return super.use(pLevel, pPlayer, pHand);
	}

	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
		if (!entity.level().isClientSide() && entity instanceof Player player && player.isCrouching()) {
			List<ItemStack> ammoStackList = getChargedProjectiles(stack);
			Level level = player.level();
			for (ItemStack ammoStack : ammoStackList) {
				if (!player.addItem(ammoStack))
					level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(), ammoStack));
			}
			clearChargedProjectiles(stack);
			setCrossbowCharged(stack, false);
		}
		return super.onEntitySwing(stack, entity);
	}
}
