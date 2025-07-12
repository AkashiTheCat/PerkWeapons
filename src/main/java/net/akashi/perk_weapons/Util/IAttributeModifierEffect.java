package net.akashi.perk_weapons.Util;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Map;
import java.util.UUID;

public interface IAttributeModifierEffect {
	Map<Attribute, AttributeModifier> getAttributeModifiers(int pAmplifier);

	Map<Attribute, UUID> getAttributeModifierUUIDs(int pAmplifier);
}
