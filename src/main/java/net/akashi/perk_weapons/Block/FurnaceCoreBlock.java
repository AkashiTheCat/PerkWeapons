package net.akashi.perk_weapons.Block;

import com.google.common.collect.ImmutableMap;
import net.akashi.perk_weapons.Block.Entity.FurnaceCoreBlockEntity;
import net.akashi.perk_weapons.Registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Function;

public class FurnaceCoreBlock extends AbstractFurnaceBlock {
	public FurnaceCoreBlock(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return ModBlockEntities.FURNACE_CORE_BLOCK_ENTITY.get().create(pPos, pState);
	}

	@Override
	protected void openContainer(Level pLevel, BlockPos pPos, Player pPlayer) {
		BlockEntity blockentity = pLevel.getBlockEntity(pPos);
		if (blockentity instanceof FurnaceCoreBlockEntity) {
			pPlayer.openMenu((MenuProvider) blockentity);
		}

	}

	@Override
	@Nullable
	public <T extends BlockEntity>
	BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
		return createFurnaceTicker(pLevel, pBlockEntityType, ModBlockEntities.FURNACE_CORE_BLOCK_ENTITY.get());
	}
}
