package net.akashi.perk_weapons.Registry;

import net.akashi.perk_weapons.Entities.BeholderBeamEntity;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.*;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.*;
import net.akashi.perk_weapons.PerkWeapons;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModEntities {
	public static final DeferredRegister<EntityType<?>> ENTITIES =
			DeferredRegister.create(Registries.ENTITY_TYPE, PerkWeapons.MODID);

	//Registry Method
	private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> EntityRegistry(String name, EntityType.EntityFactory<T> factory) {
		return ENTITIES.register(name,
				() -> EntityType.Builder.of(factory, MobCategory.MISC)
						.sized(0.5F, 0.5F)
						.clientTrackingRange(4)
						.setShouldReceiveVelocityUpdates(true)
						.updateInterval(20)
						.build(ResourceLocation.fromNamespaceAndPath(PerkWeapons.MODID, name).toString()));
	}

	private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> EntityRegistry(
			String name, EntityType.EntityFactory<T> factory,
			float w, float h, int updateInterval) {
		return ENTITIES.register(name,
				() -> EntityType.Builder.of(factory, MobCategory.MISC)
						.sized(w, h)
						.clientTrackingRange(4)
						.setShouldReceiveVelocityUpdates(true)
						.updateInterval(updateInterval)
						.build(ResourceLocation.fromNamespaceAndPath(PerkWeapons.MODID, name).toString()));
	}

	//Spears
	public static final DeferredHolder<EntityType<?>, EntityType<ThrownSpear>> THROWN_SPEAR = EntityRegistry("thrown_spear", ThrownSpear::new);
	public static final DeferredHolder<EntityType<?>, EntityType<ThrownMegalodon>> THROWN_MEGALODON = EntityRegistry("thrown_megalodon", ThrownMegalodon::new);
	public static final DeferredHolder<EntityType<?>, EntityType<ThrownConduitGuard>> THROWN_CONDUIT_GUARD = EntityRegistry("thrown_conduit_guard", ThrownConduitGuard::new);
	public static final DeferredHolder<EntityType<?>, EntityType<ThrownDragonStrike>> THROWN_DRAGON_STRIKE = EntityRegistry("thrown_dragon_strike", ThrownDragonStrike::new);
	public static final DeferredHolder<EntityType<?>, EntityType<ThrownScourge>> THROWN_SCOURGE = EntityRegistry("thrown_scourge", ThrownScourge::new);
	public static final DeferredHolder<EntityType<?>, EntityType<ThrownNetherGuide>> THROWN_NETHER_GUIDE = EntityRegistry("thrown_nether_guide", ThrownNetherGuide::new);
	public static final DeferredHolder<EntityType<?>, EntityType<ThrownCenturion>> THROWN_CENTURION = EntityRegistry("thrown_centurion", ThrownCenturion::new);

	//Arrows
	public static final DeferredHolder<EntityType<?>, EntityType<BaseArrow>> BASE_ARROW = EntityRegistry("arrow", BaseArrow::new);
	public static final DeferredHolder<EntityType<?>, EntityType<PurgatoryArrow>> PURGATORY_ARROW = EntityRegistry("purgatory_arrow", PurgatoryArrow::new);
	public static final DeferredHolder<EntityType<?>, EntityType<ExplosiveArrow>> EXPLOSIVE_ARROW = EntityRegistry("explosive_arrow", ExplosiveArrow::new);
	public static final DeferredHolder<EntityType<?>, EntityType<PerkGainingArrow>> PERK_GAINING_ARROW = EntityRegistry("perk_arrow", PerkGainingArrow::new);
	public static final DeferredHolder<EntityType<?>, EntityType<FrostHunterArrow>> FROST_HUNTER_ARROW = EntityRegistry("frost_hunter_arrow", FrostHunterArrow::new);
	public static final DeferredHolder<EntityType<?>, EntityType<StarShooterArrow>> STAR_SHOOTER_ARROW = EntityRegistry("star_shooter_arrow", StarShooterArrow::new);
	public static final DeferredHolder<EntityType<?>, EntityType<DevourerArrow>> DEVOURER_ARROW = EntityRegistry("devourer_arrow", DevourerArrow::new);
	public static final DeferredHolder<EntityType<?>, EntityType<IncineratorArrow>> INCINERATOR_ARROW = EntityRegistry("incinerator_arrow", IncineratorArrow::new);
	public static final DeferredHolder<EntityType<?>, EntityType<QueenBeeArrow>> QUEEN_BEE_ARROW = EntityRegistry("queen_bee_arrow", QueenBeeArrow::new);
	public static final DeferredHolder<EntityType<?>, EntityType<PaladinArrow>> PALADIN_ARROW = EntityRegistry("paladin_arrow", PaladinArrow::new);
	public static final DeferredHolder<EntityType<?>, EntityType<EndboreWandererArrow>> ENDBORE_WANDERER_ARROW = EntityRegistry("endbore_wanderer_arrow", EndboreWandererArrow::new);
	public static final DeferredHolder<EntityType<?>, EntityType<EndboreWandererPerkProjectile>> ENDBORE_WANDERER_PERK_PROJECTILE = EntityRegistry("endbore_wanderer_perk_projectile", EndboreWandererPerkProjectile::new);

	//Util
	public static final DeferredHolder<EntityType<?>, EntityType<BeholderBeamEntity>> BEHOLDER_BEAM_SRC = EntityRegistry(
			"beholder_beam_source", BeholderBeamEntity::new, 0.25F, 0.25F, 20);

}
