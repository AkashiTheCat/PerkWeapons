package net.akashi.perk_weapons.Crossbows;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

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
	public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, pLevel, tooltip, flag);
		if (pLevel == null || !pLevel.isClientSide())
			return;

		Component quantity = Component.literal(String.valueOf(this.getChargedProjectileAmount(stack)))
				.withStyle(ChatFormatting.GRAY);
		tooltip.add(Component.translatable("tooltip.perk_weapons.crossbow_ammo_amount", quantity)
				.withStyle(ChatFormatting.DARK_AQUA));

		Minecraft mc = Minecraft.getInstance();
		Component crouch = mc.options.keyShift.getTranslatedKeyMessage().copy()
				.withStyle(ChatFormatting.AQUA);
		Component attack = mc.options.keyAttack.getTranslatedKeyMessage().copy()
				.withStyle(ChatFormatting.AQUA);
		tooltip.add(Component.translatable("tooltip.perk_weapons.mag_fed_crossbow_hint",
				crouch, attack).withStyle(ChatFormatting.DARK_GRAY));
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
