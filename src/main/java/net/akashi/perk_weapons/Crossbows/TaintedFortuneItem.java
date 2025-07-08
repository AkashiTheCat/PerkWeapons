package net.akashi.perk_weapons.Crossbows;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.akashi.perk_weapons.Config.Properties.Crossbow.CrossbowProperties;
import net.akashi.perk_weapons.Config.Properties.Crossbow.TaintedFortuneProperties;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;


public class TaintedFortuneItem extends AutoLoadingCrossbowItem {
	public static final UUID KNOCKBACK_UUID = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
	public Multimap<Attribute, AttributeModifier> OffhandAttributeModifiers;
	public static float KNOCKBACK_MODIFIER = 1.0f;

	public TaintedFortuneItem(Properties pProperties) {
		super(pProperties);
	}

	public TaintedFortuneItem(int maxChargeTicks, float damage, float velocity, float inaccuracy,
	                          int ammoCapacity, int fireInterval, float speedModifier,
	                          boolean onlyAllowMainHand, Properties pProperties) {
		super(maxChargeTicks, damage, velocity, inaccuracy, ammoCapacity, fireInterval,
				speedModifier, onlyAllowMainHand, pProperties);
		if (KNOCKBACK_MODIFIER != 0.0F) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(KNOCKBACK_UUID,
					"Tool modifier", KNOCKBACK_MODIFIER, AttributeModifier.Operation.ADDITION));
			this.OffhandAttributeModifiers = builder.build();
		}

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

	@Override
	public Component getWeaponDescription(ItemStack stack, Level level) {
		return TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.tainted_fortune"));
	}
}
