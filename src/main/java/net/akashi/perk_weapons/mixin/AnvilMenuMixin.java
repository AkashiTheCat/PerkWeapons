package net.akashi.perk_weapons.mixin;

import net.akashi.perk_weapons.Config.ModCommonConfigs;
import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModItems;
import net.akashi.perk_weapons.Registry.ModTags;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
	@Shadow
	@Final
	private DataSlot cost;

	@Shadow
	public int repairItemCountCost;


	@Shadow
	protected abstract boolean mayPickup(Player pPlayer, boolean pHasStack);

	@Shadow
	protected abstract void onTake(Player pPlayer, ItemStack pStack);

	@Shadow
	protected abstract boolean isValidBlock(BlockState pState);

	@Shadow
	public abstract void createResult();

	@Shadow
	protected abstract ItemCombinerMenuSlotDefinition createInputSlotDefinitions();

	public AnvilMenuMixin(@Nullable MenuType<?> pType, int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess pAccess) {
		super(pType, pContainerId, pPlayerInventory, pAccess);
	}

	@Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
	public void onCreateResultInvoked(CallbackInfo ci) {
		ItemStack firstStack = this.inputSlots.getItem(0).copy();
		ItemStack secondStack = this.inputSlots.getItem(1);
		if (firstStack.isDamageableItem() && secondStack.is(ModTags.REPAIRER_TAG)) {
			int damage = firstStack.getDamageValue();
			if (damage <= 0) {
				this.resultSlots.setItem(0, ItemStack.EMPTY);
				this.cost.set(0);
				ci.cancel();
			}

			int delta = (int) (ModCommonConfigs.REPAIRER_REPAIR_PERCENTAGE.get() * firstStack.getMaxDamage());
			int count;
			for (count = 0; damage > 0 && count < secondStack.getCount(); count++) {
				damage = Math.max(damage - delta, 0);
			}
			firstStack.setDamageValue(damage);
			this.cost.set(ModCommonConfigs.REPAIRER_LEVEL_COST.get() * count);
			this.repairItemCountCost = count;

			this.resultSlots.setItem(0, firstStack);
			this.broadcastChanges();
			ci.cancel();
		}
	}
}
