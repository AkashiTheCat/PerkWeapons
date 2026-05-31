package net.akashi.perk_weapons.EventHandlers;

import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModAttributes;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = PerkWeapons.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ModAttributesEventHandler {
	@SubscribeEvent
	public static void onLivingEntityHurt(LivingIncomingDamageEvent event) {
		LivingEntity entity = event.getEntity();
		if (entity.getAttribute(ModAttributes.DAMAGE_RESISTANCE) != null) {
			double resistance = entity.getAttributeValue(ModAttributes.DAMAGE_RESISTANCE);
			event.setAmount((float) (event.getAmount() / (1 + resistance / 100)));
		}

		if (event.getSource().is(DamageTypes.MAGIC) && entity.getAttribute(ModAttributes.MAGIC_RESISTANCE) != null) {
			double resistance = entity.getAttributeValue(ModAttributes.MAGIC_RESISTANCE);
			event.setAmount((float) (event.getAmount() / (1 + (resistance / 100))));
		}
	}
}
