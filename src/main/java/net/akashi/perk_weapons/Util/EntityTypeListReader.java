package net.akashi.perk_weapons.Util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class EntityTypeListReader {
	public static List<EntityType<? extends Mob>> convertStringsToEntityType(List<String> entityTypeIds) {
		List<EntityType<? extends Mob>> entityTypes = new ArrayList<>();

		for (String entityTypeID : entityTypeIds) {
			ResourceLocation resourceLocation = new ResourceLocation(entityTypeID);
			EntityType<? extends Mob> entityType = (EntityType<? extends Mob>)
					ForgeRegistries.ENTITY_TYPES.getValue(resourceLocation);
			if (entityType != null) {
				entityTypes.add(entityType);
			}
		}
		return entityTypes;
	}
}
