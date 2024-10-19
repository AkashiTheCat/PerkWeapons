package net.akashi.weaponmod.Spears;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.akashi.weaponmod.Config.ModCommonConfigs;
import net.akashi.weaponmod.Config.Properties.PiglinsWarSpearProperties;
import net.akashi.weaponmod.Config.Properties.SpearProperties;
import net.akashi.weaponmod.Entities.Projectiles.ThrownSpear;
import net.akashi.weaponmod.Registry.ModEntities;
import net.akashi.weaponmod.Registry.ModItems;
import net.akashi.weaponmod.WeaponMod;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.world.item.enchantment.Enchantments.FIRE_ASPECT;

public class PiglinsWarSpearItem extends SpearItem {
	private Player LastPlayer = null;
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
	public void updateAttributes(SpearProperties properties) {
		super.updateAttributes(properties);
		if (properties instanceof PiglinsWarSpearProperties pProperties) {
			DAMAGE_BONUS = pProperties.DAMAGE_BONUS.get().floatValue();
			SPEED_BONUS = pProperties.SPEED_BONUS.get().floatValue();
			allowedArmor = PiglinsWarSpearProperties.convertStringsToItems(pProperties.getArmorList());
		}
	}

	private Player getPlayer() {
		return LastPlayer;
	}

	private int getGoldArmorCount(Player player) {
		int count = 0;
		for (ItemStack armor : player.getArmorSlots()) {
			if (allowedArmor.stream().anyMatch(pArmor -> pArmor.equals(armor.getItem()))) {
				count++;
			}
		}
		return count;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		if (slot == EquipmentSlot.MAINHAND) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

			PiglinsWarSpearProperties properties = ModCommonConfigs.PIGLINS_WARSPEAR_PROPERTIES;
			float attackDamage = properties.MELEE_DAMAGE.get().floatValue();
			float attackSpeed = properties.ATTACK_SPEED.get().floatValue();
			Player player = getPlayer();
			if (player != null) {
				int count = getGoldArmorCount(player);
				attackDamage = attackDamage * (1 + DAMAGE_BONUS * count);
				attackSpeed = attackSpeed * (1 + SPEED_BONUS * count);
			}
			builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", attackDamage - 1, AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", attackSpeed - 4, AttributeModifier.Operation.ADDITION));
			return builder.build();
		}
		return super.getAttributeModifiers(slot, stack);
	}

	@Override
	public ThrownSpear createThrownSpear(Level pLevel, Player player, ItemStack pStack) {
		float Damage = this.ThrowDamage * (1 + DAMAGE_BONUS * getGoldArmorCount(player));
		return new ThrownSpear(pLevel, player, pStack, getItemSlotIndex(player, pStack), ModEntities.THROWN_SPEAR.get())
				.setBaseDamage(Damage);
	}

	@Override
	public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
		LastPlayer = player;
		super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
	}
}
