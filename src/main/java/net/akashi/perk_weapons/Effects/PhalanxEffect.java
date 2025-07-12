package net.akashi.perk_weapons.Effects;

import net.akashi.perk_weapons.Config.Properties.PhalanxEffectProperties;
import net.akashi.perk_weapons.Util.IAttributeModifierEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.*;

public class PhalanxEffect extends MobEffect implements IAttributeModifierEffect {
	public static final UUID KNOCKBACK_RESISTANCE_UUID = UUID.fromString("d9a3314b-a155-d1fa-50b4-59f14fafbdd8");
	public static final UUID ATTACK_SPEED_UUID = UUID.fromString("1aed4d3b-87ee-1e37-3ad6-2128d647d65b");

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
	public Map<Attribute, AttributeModifier> getAttributeModifiers(int pAmplifier) {
		Map<Attribute, AttributeModifier> modifierMap = new HashMap<>();
		modifierMap.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(
				KNOCKBACK_RESISTANCE_UUID, "Knockback Resistance",
				KNOCKBACK_RESISTANCE_PER_LEVEL * (pAmplifier + 1),
				AttributeModifier.Operation.ADDITION
		));
		modifierMap.put(Attributes.ATTACK_SPEED, new AttributeModifier(
				ATTACK_SPEED_UUID, "Attack Speed",
				ATTACK_SPEED_PER_LEVEL * (pAmplifier + 1),
				AttributeModifier.Operation.ADDITION
		));
		return modifierMap;
	}

	@Override
	public Map<Attribute, UUID> getAttributeModifierUUIDs(int pAmplifier) {
		Map<Attribute, UUID> map = new HashMap<>();
		map.put(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE_UUID);
		map.put(Attributes.ATTACK_SPEED, ATTACK_SPEED_UUID);
		return map;
	}

	public static void updateParamsFromConfig(PhalanxEffectProperties properties) {
		KNOCKBACK_RESISTANCE_PER_LEVEL = properties.KNOCKBACK_RESISTANCE_PER_LEVEL.get();
		ATTACK_SPEED_PER_LEVEL = properties.ATTACK_SPEED_PER_LEVEL.get();
	}
}
