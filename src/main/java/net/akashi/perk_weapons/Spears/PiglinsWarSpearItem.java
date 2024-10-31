package net.akashi.perk_weapons.Spears;

import net.akashi.perk_weapons.Config.Properties.Spear.PiglinsWarSpearProperties;
import net.akashi.perk_weapons.Config.Properties.Spear.SpearProperties;
import net.akashi.perk_weapons.Network.SpearAttributeUpdateSyncPacket;
import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModPackets;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.world.item.enchantment.Enchantments.FIRE_ASPECT;

@Mod.EventBusSubscriber(modid = PerkWeapons.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PiglinsWarSpearItem extends SpearItem {
	private static float DAMAGE_BONUS = 0.1f;
	private static float SPEED_BONUS = 0.1f;
	private static List<Item> allowedArmor = new ArrayList<>(Arrays.asList(
			Items.GOLDEN_HELMET,
			Items.GOLDEN_CHESTPLATE,
			Items.GOLDEN_LEGGINGS,
			Items.GOLDEN_BOOTS));

	public PiglinsWarSpearItem(boolean isAdvanced, Properties pProperties) {
		super(isAdvanced, pProperties);
		AddGeneralEnchant(FIRE_ASPECT);
	}

	public PiglinsWarSpearItem(float attackDamage, float attackSpeed, float throwDamage, float projectileVelocity, boolean isAdvanced, Properties pProperties) {
		super(attackDamage, attackSpeed, throwDamage, projectileVelocity, isAdvanced, pProperties);
		AddGeneralEnchant(FIRE_ASPECT);
	}

	@Override
	public void updateAttributesFromConfig(SpearProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof PiglinsWarSpearProperties pProperties) {
			DAMAGE_BONUS = pProperties.DAMAGE_BONUS.get().floatValue();
			SPEED_BONUS = pProperties.SPEED_BONUS.get().floatValue();
			allowedArmor = PiglinsWarSpearProperties.convertStringsToItems(pProperties.getArmorList());
		}
	}

	@SubscribeEvent
	public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
		//This will only update attributes when item is in MainHand to avoid performance issue
		if (event.getSlot().isArmor() && event.getEntity() instanceof Player player && !player.level().isClientSide()
				&& player.getMainHandItem().getItem() instanceof PiglinsWarSpearItem item) {
			int count = getArmorCount(player);
			float damageMultiplier = 1 + DAMAGE_BONUS * count;
			float speedMultiplier = 1 + SPEED_BONUS * count;
			//IDK why this is needed to sync to client actually
			//Syncing in this way will probably cause an inevitable display bug if you are hosting a LAN server.(Doesn't affect actual gameplay)
			ModPackets.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
					new SpearAttributeUpdateSyncPacket(player.getId(), damageMultiplier, speedMultiplier));
			item.updateAttributes(damageMultiplier, speedMultiplier);
		}
	}

	private static int getArmorCount(Player player) {
		int count = 0;
		for (ItemStack armor : player.getArmorSlots()) {
			if (allowedArmor.stream().anyMatch(pArmor -> pArmor.equals(armor.getItem()))) {
				count++;
			}
		}
		return count;
	}
}
