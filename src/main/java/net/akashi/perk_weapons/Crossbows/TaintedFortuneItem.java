package net.akashi.perk_weapons.Crossbows;

import net.akashi.perk_weapons.Config.Properties.Crossbow.CrossbowProperties;
import net.akashi.perk_weapons.Config.Properties.Crossbow.TaintedFortuneProperties;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;


public class TaintedFortuneItem extends AutoLoadingCrossbowItem {
	public ItemAttributeModifiers OffhandAttributeModifiers = ItemAttributeModifiers.EMPTY;
	public static float KNOCKBACK_MODIFIER = 1.0f;

	public TaintedFortuneItem(Properties pProperties) {
		super(pProperties);
	}

	public TaintedFortuneItem(int maxChargeTicks, float damage, float velocity, float inaccuracy,
	                          int ammoCapacity, int fireInterval, float speedModifier,
	                          boolean onlyAllowMainHand, Properties pProperties) {
		super(maxChargeTicks, damage, velocity, inaccuracy, ammoCapacity, fireInterval,
				speedModifier, onlyAllowMainHand, pProperties);
	}

	@Override
	protected void buildAttributeModifiers() {
		super.buildAttributeModifiers();
		ItemAttributeModifiers.Builder offHandBuilder = ItemAttributeModifiers.builder();
		if (KNOCKBACK_MODIFIER != 0.0F) {
			offHandBuilder.add(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(ResourceLocation.fromNamespaceAndPath("perk_weapons", "tainted_fortune_knockback"), KNOCKBACK_MODIFIER, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.OFFHAND);
		}
		this.OffhandAttributeModifiers = offHandBuilder.build();
	}

	@Override
	public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers(@NotNull ItemStack stack) {
		ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
		for (ItemAttributeModifiers.Entry entry : this.DefaultAttributeModifiers.modifiers()) {
			builder.add(entry.attribute(), entry.modifier(), entry.slot());
		}
		for (ItemAttributeModifiers.Entry entry : this.OffhandAttributeModifiers.modifiers()) {
			builder.add(entry.attribute(), entry.modifier(), entry.slot());
		}
		return builder.build();
	}

	@Override
	public void updateAttributesFromConfig(CrossbowProperties properties) {
		if (properties instanceof TaintedFortuneProperties tProperties) {
			KNOCKBACK_MODIFIER = tProperties.KNOCKBACK_MODIFIER.get().floatValue();
		}
		super.updateAttributesFromConfig(properties);
	}

	@Override
	public Component getWeaponDescription(ItemStack stack, Level level) {
		return TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.tainted_fortune"));
	}
}
