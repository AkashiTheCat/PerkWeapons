package net.akashi.perk_weapons.Registry;

import net.akashi.perk_weapons.Block.Entity.FurnaceCoreBlockEntity;
import net.akashi.perk_weapons.PerkWeapons;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
			DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, PerkWeapons.MODID);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FurnaceCoreBlockEntity>> FURNACE_CORE_BLOCK_ENTITY =
			BLOCK_ENTITIES.register("furnace_core_block_entity",
					() -> BlockEntityType.Builder.of(FurnaceCoreBlockEntity::new, ModBlocks.FURNACE_CORE.get()).build(null));
}
