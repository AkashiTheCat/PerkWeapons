package net.akashi.perk_weapons.Util;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Map;

public interface IAttributeModifierEffect {
	Map<Holder<Attribute>, AttributeModifier> getAttributeModifiers(int pAmplifier);

	Map<Holder<Attribute>, ResourceLocation> getAttributeModifierIds(int pAmplifier);
}
