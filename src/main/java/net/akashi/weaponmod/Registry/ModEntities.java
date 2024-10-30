package net.akashi.weaponmod.Registry;

import net.akashi.weaponmod.Entities.Projectiles.Arrows.BaseArrow;
import net.akashi.weaponmod.Entities.Projectiles.Arrows.ExplosiveArrow;
import net.akashi.weaponmod.Entities.Projectiles.Arrows.PurgatoryArrow;
import net.akashi.weaponmod.Entities.Projectiles.Spears.*;
import net.akashi.weaponmod.WeaponMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
	public static final DeferredRegister<EntityType<?>> ENTITIES =
			DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, WeaponMod.MODID);

	//Registry Method
	private static <T extends ThrownSpear> RegistryObject<EntityType<T>> SpearRegistry(String name, EntityType.EntityFactory<T> factory) {
		return ENTITIES.register(name,
				() -> EntityType.Builder.<T>of(factory, MobCategory.MISC)
						.sized(0.5F, 0.5F)
						.clientTrackingRange(4)
						.setShouldReceiveVelocityUpdates(true)
						.updateInterval(20)
						.build(new ResourceLocation(WeaponMod.MODID, name).toString()));
	}

	private static <T extends BaseArrow> RegistryObject<EntityType<T>> ArrowRegistry(String name, EntityType.EntityFactory<T> factory) {
		return ENTITIES.register(name,
				() -> EntityType.Builder.<T>of(factory, MobCategory.MISC)
						.sized(0.5F, 0.5F)
						.clientTrackingRange(4)
						.setShouldReceiveVelocityUpdates(true)
						.updateInterval(20)
						.build(new ResourceLocation(WeaponMod.MODID, name).toString()));
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

}
