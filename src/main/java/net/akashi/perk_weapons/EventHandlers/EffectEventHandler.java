package net.akashi.perk_weapons.EventHandlers;

import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Util.IAttributeModifierEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PerkWeapons.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EffectEventHandler {
	@SubscribeEvent
	public static void onEffectAdded(MobEffectEvent.Added event) {
		MobEffectInstance instance = event.getEffectInstance();

		if (instance.getEffect() instanceof IAttributeModifierEffect effect) {
			LivingEntity entity = event.getEntity();

			var modifiers = effect.getAttributeModifiers(instance.getAmplifier());
			for (var entry : modifiers.entrySet()) {
				AttributeInstance attributeInstance = entity.getAttribute(entry.getKey());
				if (attributeInstance == null)
					continue;
				AttributeModifier modifier = entry.getValue();
				if (attributeInstance.hasModifier(modifier)) {
					attributeInstance.removeModifier(modifier.getId());
				}
				attributeInstance.addTransientModifier(modifier);
			}
		}
	}

	@SubscribeEvent
	public static void onEffectRemoved(MobEffectEvent.Remove event) {
		MobEffectInstance instance = event.getEffectInstance();
		if (instance == null)
			return;

		if (instance.getEffect() instanceof IAttributeModifierEffect effect) {
			removeEffectModifiers(effect, instance.getAmplifier(), event.getEntity());
		}
	}

	@SubscribeEvent
	public static void onEffectExpired(MobEffectEvent.Expired event) {
		MobEffectInstance instance = event.getEffectInstance();
		if (instance == null)
			return;

		if (instance.getEffect() instanceof IAttributeModifierEffect effect) {
			removeEffectModifiers(effect, instance.getAmplifier(), event.getEntity());
		}
	}

	private static void removeEffectModifiers(IAttributeModifierEffect effect, int amplifier, LivingEntity entity) {
		var map = effect.getAttributeModifierUUIDs(amplifier);

		for (var entry : map.entrySet()) {
			AttributeInstance attributeInstance = entity.getAttribute(entry.getKey());
			if (attributeInstance == null)
				continue;
			attributeInstance.removeModifier(entry.getValue());
		}
	}
}
