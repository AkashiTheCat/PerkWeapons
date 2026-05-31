package net.akashi.perk_weapons.Registry;

import net.akashi.perk_weapons.Block.FurnaceCoreBlock;
import net.akashi.perk_weapons.PerkWeapons;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModBlocks {
	public static final DeferredRegister<Block> BLOCKS =
			DeferredRegister.create(Registries.BLOCK, PerkWeapons.MODID);
	public static final DeferredHolder<Block, FurnaceCoreBlock> FURNACE_CORE = BLOCKS.register("furnace_core",
			()->new FurnaceCoreBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FURNACE)));
}
