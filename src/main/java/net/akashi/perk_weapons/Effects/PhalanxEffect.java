package net.akashi.perk_weapons.Effects;

import net.akashi.perk_weapons.Config.Properties.PhalanxEffectProperties;
import net.akashi.perk_weapons.Util.IAttributeModifierEffect;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.*;

public class PhalanxEffect extends MobEffect implements IAttributeModifierEffect {
	public static final ResourceLocation KNOCKBACK_RESISTANCE_ID = ResourceLocation.fromNamespaceAndPath("perk_weapons", "phalanx_knockback_resistance");
	public static final ResourceLocation ATTACK_SPEED_ID = ResourceLocation.fromNamespaceAndPath("perk_weapons", "phalanx_attack_speed");

	private static double KNOCKBACK_RESISTANCE_PER_LEVEL = 2;
	private static double ATTACK_SPEED_PER_LEVEL = 0.04;

	public PhalanxEffect() {
		super(MobEffectCategory.BENEFICIAL, 0x00a662);
	}

	@Override
	public boolean isBeneficial() {
		return true;
	}

	@Override
	public Map<Holder<Attribute>, AttributeModifier> getAttributeModifiers(int pAmplifier) {
		Map<Holder<Attribute>, AttributeModifier> modifierMap = new HashMap<>();
		modifierMap.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(
				KNOCKBACK_RESISTANCE_ID, KNOCKBACK_RESISTANCE_PER_LEVEL * (pAmplifier + 1),
				AttributeModifier.Operation.ADD_VALUE
		));
		modifierMap.put(Attributes.ATTACK_SPEED, new AttributeModifier(
				ATTACK_SPEED_ID, ATTACK_SPEED_PER_LEVEL * (pAmplifier + 1),
				AttributeModifier.Operation.ADD_VALUE
		));
		return modifierMap;
	}

	@Override
	public Map<Holder<Attribute>, ResourceLocation> getAttributeModifierIds(int pAmplifier) {
		Map<Holder<Attribute>, ResourceLocation> map = new HashMap<>();
		map.put(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE_ID);
		map.put(Attributes.ATTACK_SPEED, ATTACK_SPEED_ID);
		return map;
	}

	public static void updateParamsFromConfig(PhalanxEffectProperties properties) {
		KNOCKBACK_RESISTANCE_PER_LEVEL = properties.KNOCKBACK_RESISTANCE_PER_LEVEL.get();
		ATTACK_SPEED_PER_LEVEL = properties.ATTACK_SPEED_PER_LEVEL.get();
	}
}
