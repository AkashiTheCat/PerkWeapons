package net.akashi.perk_weapons.Crossbows;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.akashi.perk_weapons.Config.Properties.Crossbow.CrossbowProperties;
import net.akashi.perk_weapons.Config.Properties.Crossbow.TaintedFortuneProperties;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class TaintedFortuneItem extends BaseCrossbowItem {
	public static final UUID KNOCKBACK_UUID = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
	public static final String TAG_RELOAD_BEGIN = "reload_begin";
	public static final String TAG_LAST_TRY_LOAD = "last_try";
	public Multimap<Attribute, AttributeModifier> OffhandAttributeModifiers;
	public static float KNOCKBACK_MODIFIER = 1.0f;

	public TaintedFortuneItem(Properties pProperties) {
		super(pProperties);
	}

	public TaintedFortuneItem(int maxChargeTicks, float damage, float velocity, float inaccuracy,
	                          float speedModifier, boolean onlyAllowMainHand, Properties pProperties) {
		super(maxChargeTicks, damage, velocity, inaccuracy, speedModifier, onlyAllowMainHand, pProperties);
		if (KNOCKBACK_MODIFIER != 0.0F) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(KNOCKBACK_UUID,
					"Tool modifier", KNOCKBACK_MODIFIER, AttributeModifier.Operation.ADDITION));
			this.OffhandAttributeModifiers = builder.build();
		}

	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		if (onlyAllowMainHand && pHand != InteractionHand.MAIN_HAND) {
			return InteractionResultHolder.pass(itemstack);
		}

		if (isCrossbowCharged(itemstack)) {
			shoot(pLevel, pPlayer, pHand, itemstack, DAMAGE, VELOCITY, INACCURACY);
			consumeAndSetCharged(itemstack);
			return InteractionResultHolder.consume(itemstack);
		} else {
			return InteractionResultHolder.fail(itemstack);
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
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

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		if (slot == EquipmentSlot.MAINHAND) {
			return this.AttributeModifiers;
		}
		if (slot == EquipmentSlot.OFFHAND) {
			return this.OffhandAttributeModifiers;
		}
		return ImmutableMultimap.of();
	}

	@Override
	public void updateAttributesFromConfig(CrossbowProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof TaintedFortuneProperties tProperties) {
			KNOCKBACK_MODIFIER = tProperties.KNOCKBACK_MODIFIER.get().floatValue();
			if (KNOCKBACK_MODIFIER != 0.0F) {
				ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
				builder.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(KNOCKBACK_UUID,
						"Tool modifier", KNOCKBACK_MODIFIER, AttributeModifier.Operation.ADDITION));
				this.OffhandAttributeModifiers = builder.build();
			}
		}
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
	public Component getWeaponDescription(ItemStack stack, Level level) {
		return TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.tainted_fortune"));
	}

	@Override
	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		List<Component> list = new ArrayList<>();

		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.tainted_fortune_perk_1")));

		return list;
	}
}
