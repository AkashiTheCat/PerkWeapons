package net.akashi.perk_weapons.EventHandlers;

import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModAttributes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

@EventBusSubscriber(modid = PerkWeapons.MODID, bus = EventBusSubscriber.Bus.MOD)
public class EntityAttributeModificationEventHandler {
	@SubscribeEvent
	public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
		for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
			if (LivingEntity.class.isAssignableFrom(entityType.getBaseClass())) {
				addLivingAttributes(event, entityType);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void addLivingAttributes(EntityAttributeModificationEvent event, EntityType<?> entityType) {
		EntityType<? extends LivingEntity> livingEntityType = (EntityType<? extends LivingEntity>) entityType;
		event.add(livingEntityType, ModAttributes.MAGIC_RESISTANCE);
		event.add(livingEntityType, ModAttributes.DAMAGE_RESISTANCE);
	}
}
