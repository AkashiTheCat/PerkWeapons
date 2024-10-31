package net.akashi.perk_weapons.Registry;

import net.akashi.perk_weapons.Block.FurnaceCoreBlock;
import net.akashi.perk_weapons.PerkWeapons;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
	public static final DeferredRegister<Block> BLOCKS =
			DeferredRegister.create(ForgeRegistries.BLOCKS, PerkWeapons.MODID);
	public static final RegistryObject<FurnaceCoreBlock> FURNACE_CORE = BLOCKS.register("furnace_core",
			()->new FurnaceCoreBlock(BlockBehaviour.Properties.copy(Blocks.FURNACE)));
}
