package net.akashi.perk_weapons.SimpleItems;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RepairerItem extends Item {
	public RepairerItem(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip,
	                            @NotNull TooltipFlag isAdvanced) {
		tooltip.add(Component.translatable("tooltip.perk_weapons.repairer").withStyle(ChatFormatting.AQUA));
		super.appendHoverText(stack, context, tooltip, isAdvanced);
	}
}
