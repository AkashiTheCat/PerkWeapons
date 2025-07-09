package net.akashi.perk_weapons.EventHandlers;

import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModItems;
import net.akashi.perk_weapons.Spears.PiglinsWarSpearItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.akashi.perk_weapons.Spears.PiglinsWarSpearItem.getPlayerArmorCount;
import static net.akashi.perk_weapons.Spears.PiglinsWarSpearItem.setArmorCount;

@Mod.EventBusSubscriber(modid = PerkWeapons.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EquipmentChangeEventHandler {
	@SubscribeEvent
	public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
		if (event.getEntity() instanceof Player player && !player.level().isClientSide()) {
			handlePaladinCase(player);
			handlePiglinsWarSpearCase(player);
		}
	}

	public static void handlePaladinCase(Player player) {
		ItemStack itemStack = ItemStack.EMPTY;
		if (player.getMainHandItem().is(ModItems.PALADIN.get())) {
			itemStack = player.getMainHandItem();
		}
		if (itemStack != ItemStack.EMPTY) {
			CompoundTag nbt = itemStack.getOrCreateTag();
			nbt.putBoolean("Charged", true);
		}
	}

	public static void handlePiglinsWarSpearCase(Player player) {
		ItemStack itemStack = ItemStack.EMPTY;
		if (player.getMainHandItem().getItem() instanceof PiglinsWarSpearItem) {
			itemStack = player.getMainHandItem();
		} else if (player.getOffhandItem().getItem() instanceof PiglinsWarSpearItem) {
			itemStack = player.getOffhandItem();
		}
		if (itemStack != ItemStack.EMPTY) {
			setArmorCount(itemStack, getPlayerArmorCount(player));
		}
	}
}
