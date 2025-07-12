package net.akashi.perk_weapons.Registry;

import net.akashi.perk_weapons.Entities.BeholderBeamEntity;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.*;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.*;
import net.akashi.perk_weapons.PerkWeapons;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
	public static final DeferredRegister<EntityType<?>> ENTITIES =
			DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PerkWeapons.MODID);

	//Registry Method
	private static <T extends Entity> RegistryObject<EntityType<T>> EntityRegistry(String name, EntityType.EntityFactory<T> factory) {
		return ENTITIES.register(name,
				() -> EntityType.Builder.of(factory, MobCategory.MISC)
						.sized(0.5F, 0.5F)
						.clientTrackingRange(4)
						.setShouldReceiveVelocityUpdates(true)
						.updateInterval(20)
						.build(new ResourceLocation(PerkWeapons.MODID, name).toString()));
	}

	private static <T extends Entity> RegistryObject<EntityType<T>> EntityRegistry(
			String name, EntityType.EntityFactory<T> factory,
			float w, float h, int updateInterval) {
		return ENTITIES.register(name,
				() -> EntityType.Builder.of(factory, MobCategory.MISC)
						.sized(w, h)
						.clientTrackingRange(4)
						.setShouldReceiveVelocityUpdates(true)
						.updateInterval(updateInterval)
						.build(new ResourceLocation(PerkWeapons.MODID, name).toString()));
	}

	//Spears
	public static final RegistryObject<EntityType<ThrownSpear>> THROWN_SPEAR = EntityRegistry("thrown_spear", ThrownSpear::new);
	public static final RegistryObject<EntityType<ThrownMegalodon>> THROWN_MEGALODON = EntityRegistry("thrown_megalodon", ThrownMegalodon::new);
	public static final RegistryObject<EntityType<ThrownConduitGuard>> THROWN_CONDUIT_GUARD = EntityRegistry("thrown_conduit_guard", ThrownConduitGuard::new);
	public static final RegistryObject<EntityType<ThrownDragonStrike>> THROWN_DRAGON_STRIKE = EntityRegistry("thrown_dragon_strike", ThrownDragonStrike::new);
	public static final RegistryObject<EntityType<ThrownScourge>> THROWN_SCOURGE = EntityRegistry("thrown_scourge", ThrownScourge::new);
	public static final RegistryObject<EntityType<ThrownNetherGuide>> THROWN_NETHER_GUIDE = EntityRegistry("thrown_nether_guide", ThrownNetherGuide::new);

	//Arrows
	public static final RegistryObject<EntityType<BaseArrow>> BASE_ARROW = EntityRegistry("arrow", BaseArrow::new);
	public static final RegistryObject<EntityType<PurgatoryArrow>> PURGATORY_ARROW = EntityRegistry("purgatory_arrow", PurgatoryArrow::new);
	public static final RegistryObject<EntityType<ExplosiveArrow>> EXPLOSIVE_ARROW = EntityRegistry("explosive_arrow", ExplosiveArrow::new);
	public static final RegistryObject<EntityType<PerkGainingArrow>> PERK_GAINING_ARROW = EntityRegistry("perk_arrow", PerkGainingArrow::new);
	public static final RegistryObject<EntityType<FrostHunterArrow>> FROST_HUNTER_ARROW = EntityRegistry("frost_hunter_arrow", FrostHunterArrow::new);
	public static final RegistryObject<EntityType<StarShooterArrow>> STAR_SHOOTER_ARROW = EntityRegistry("star_shooter_arrow", StarShooterArrow::new);
	public static final RegistryObject<EntityType<DevourerArrow>> DEVOURER_ARROW = EntityRegistry("devourer_arrow", DevourerArrow::new);
	public static final RegistryObject<EntityType<IncineratorArrow>> INCINERATOR_ARROW = EntityRegistry("incinerator_arrow", IncineratorArrow::new);
	public static final RegistryObject<EntityType<QueenBeeArrow>> QUEEN_BEE_ARROW = EntityRegistry("queen_bee_arrow", QueenBeeArrow::new);
	public static final RegistryObject<EntityType<PaladinArrow>> PALADIN_ARROW = EntityRegistry("paladin_arrow", PaladinArrow::new);
	public static final RegistryObject<EntityType<EndboreWandererArrow>> ENDBORE_WANDERER_ARROW = EntityRegistry("endbore_wanderer_arrow", EndboreWandererArrow::new);
	public static final RegistryObject<EntityType<EndboreWandererPerkProjectile>> ENDBORE_WANDERER_PERK_PROJECTILE = EntityRegistry("endbore_wanderer_perk_projectile", EndboreWandererPerkProjectile::new);

	//Util
	public static final RegistryObject<EntityType<BeholderBeamEntity>> BEHOLDER_BEAM_SRC = EntityRegistry(
			"beholder_beam_source", BeholderBeamEntity::new, 0.25F, 0.25F, 20);

}
