package net.akashi.perk_weapons.EventHandlers;

import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModAttributes;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PerkWeapons.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModAttributesEventHandler {
	@SubscribeEvent
	public static void onLivingEntityHurt(LivingHurtEvent event) {
		LivingEntity entity = event.getEntity();
		if (entity.getAttribute(ModAttributes.DAMAGE_RESISTANCE.get()) != null) {
			double resistance = entity.getAttributeValue(ModAttributes.DAMAGE_RESISTANCE.get());
			event.setAmount((float) (event.getAmount() / (1 + resistance / 100)));
		}

		if (event.getSource().is(DamageTypes.MAGIC) && entity.getAttribute(ModAttributes.MAGIC_RESISTANCE.get()) != null) {
			double resistance = entity.getAttributeValue(ModAttributes.MAGIC_RESISTANCE.get());
			event.setAmount((float) (event.getAmount() * (1 + (resistance / 100))));
		}
	}
}
