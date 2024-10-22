package net.akashi.weaponmod.Block.Entity;

import net.akashi.weaponmod.Registry.ModBlockEntities;
import net.akashi.weaponmod.WeaponMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BlastFurnaceMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FurnaceCoreBlockEntity extends AbstractFurnaceBlockEntity {
	public FurnaceCoreBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(ModBlockEntities.FURNACE_CORE_BLOCK_ENTITY.get(), pPos, pBlockState, RecipeType.BLASTING);
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("container.furnace_core", WeaponMod.MODID);
	}


	@Override
	protected int getBurnDuration(ItemStack pFuel) {
		return Integer.MAX_VALUE;
	}

	@Override
	protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
		return new BlastFurnaceMenu(pContainerId, pInventory, this, this.dataAccess);
	}
}
