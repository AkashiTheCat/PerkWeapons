package net.akashi.weaponmod.Registry;

import net.akashi.weaponmod.Block.Entity.FurnaceCoreBlockEntity;
import net.akashi.weaponmod.WeaponMod;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
			DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, WeaponMod.MODID);
	public static final RegistryObject<BlockEntityType<FurnaceCoreBlockEntity>> FURNACE_CORE_BLOCK_ENTITY =
			BLOCK_ENTITIES.register("furnace_core_block_entity",
					() -> BlockEntityType.Builder.of(FurnaceCoreBlockEntity::new, ModBlocks.FURNACE_CORE.get()).build(null));
}
