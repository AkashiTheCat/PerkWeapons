package net.akashi.perk_weapons.Block.Entity;

import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BlastFurnaceMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FurnaceCoreBlockEntity extends AbstractFurnaceBlockEntity {
	public FurnaceCoreBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(ModBlockEntities.FURNACE_CORE_BLOCK_ENTITY.get(), pPos, pBlockState, RecipeType.BLASTING);
	}

	@Override
	protected @NotNull Component getDefaultName() {
		return Component.translatable("container.perk_weapons.furnace_core");
	}

	@Override
	protected int getBurnDuration(@NotNull ItemStack pFuel) {
		return Integer.MAX_VALUE;
	}

	@Override
	protected @NotNull AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pInventory) {
		return new BlastFurnaceMenu(pContainerId, pInventory, this, this.dataAccess);
	}
}
