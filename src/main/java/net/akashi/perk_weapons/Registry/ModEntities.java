package net.akashi.perk_weapons.Registry;

import net.akashi.perk_weapons.Entities.Projectiles.Arrows.*;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.*;
import net.akashi.perk_weapons.PerkWeapons;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
	public static final DeferredRegister<EntityType<?>> ENTITIES =
			DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PerkWeapons.MODID);

	//Registry Method
	private static <T extends ThrownSpear> RegistryObject<EntityType<T>> SpearRegistry(String name, EntityType.EntityFactory<T> factory) {
		return ENTITIES.register(name,
				() -> EntityType.Builder.<T>of(factory, MobCategory.MISC)
						.sized(0.5F, 0.5F)
						.clientTrackingRange(4)
						.setShouldReceiveVelocityUpdates(true)
						.updateInterval(20)
						.build(new ResourceLocation(PerkWeapons.MODID, name).toString()));
	}

	private static <T extends BaseArrow> RegistryObject<EntityType<T>> ArrowRegistry(String name, EntityType.EntityFactory<T> factory) {
		return ENTITIES.register(name,
				() -> EntityType.Builder.<T>of(factory, MobCategory.MISC)
						.sized(0.5F, 0.5F)
						.clientTrackingRange(4)
						.setShouldReceiveVelocityUpdates(true)
						.updateInterval(20)
						.build(new ResourceLocation(PerkWeapons.MODID, name).toString()));
	}

	//Spear Registry
	public static final RegistryObject<EntityType<ThrownSpear>> THROWN_SPEAR = SpearRegistry("thrown_spear", ThrownSpear::new);
	public static final RegistryObject<EntityType<ThrownMegalodon>> THROWN_MEGALODON = SpearRegistry("thrown_megalodon", ThrownMegalodon::new);
	public static final RegistryObject<EntityType<ThrownConduitGuard>> THROWN_CONDUIT_GUARD = SpearRegistry("thrown_conduit_guard", ThrownConduitGuard::new);
	public static final RegistryObject<EntityType<ThrownDragonStrike>> THROWN_DRAGON_STRIKE = SpearRegistry("thrown_dragon_strike", ThrownDragonStrike::new);
	public static final RegistryObject<EntityType<ThrownScourge>> THROWN_SCOURGE = SpearRegistry("thrown_scourge", ThrownScourge::new);

	//Arrow Registry
	public static final RegistryObject<EntityType<BaseArrow>> BASE_ARROW = ArrowRegistry("arrow", BaseArrow::new);
	public static final RegistryObject<EntityType<PurgatoryArrow>> PURGATORY_ARROW = ArrowRegistry("purgatory_arrow", PurgatoryArrow::new);
	public static final RegistryObject<EntityType<ExplosiveArrow>> EXPLOSIVE_ARROW = ArrowRegistry("explosive_arrow", ExplosiveArrow::new);
	public static final RegistryObject<EntityType<PerkUpdateArrow>> PERK_UPDATE_ARROW = ArrowRegistry("perk_arrow", PerkUpdateArrow::new);
	public static final RegistryObject<EntityType<FrostHunterArrow>> FROST_HUNTER_ARROW = ArrowRegistry("frost_hunter_arrow", FrostHunterArrow::new);

}
