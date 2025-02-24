package net.akashi.perk_weapons.Spears;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.akashi.perk_weapons.Config.Properties.Spear.PiglinsWarSpearProperties;
import net.akashi.perk_weapons.Config.Properties.Spear.SpearProperties;
import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.world.item.enchantment.Enchantments.FIRE_ASPECT;

@Mod.EventBusSubscriber(modid = PerkWeapons.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PiglinsWarSpearItem extends BaseSpearItem {
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

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		CompoundTag tag = stack.getTag();
		float speedMultiplier = 1.0F;
		float damageMultiplier = 1.0F;
		if (tag != null) {
			speedMultiplier = tag.getFloat("speedMultiplier");
			damageMultiplier = tag.getFloat("damageMultiplier");
		}
		if (speedMultiplier == 1.0F && damageMultiplier == 1.0F) {
			super.getAttributeModifiers(slot, stack);
		}
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", BaseAttackDamage * damageMultiplier - 1, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", BaseAttackSpeed * speedMultiplier - 4, AttributeModifier.Operation.ADDITION));
		return slot == EquipmentSlot.MAINHAND ? builder.build() : super.getAttributeModifiers(slot, stack);
	}

	@SubscribeEvent
	public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
		if (event.getEntity() instanceof Player player && !player.level().isClientSide()) {
			ItemStack itemStack = ItemStack.EMPTY;
			if (player.getMainHandItem().getItem() instanceof PiglinsWarSpearItem) {
				itemStack = player.getMainHandItem();
			} else if (player.getOffhandItem().getItem() instanceof PiglinsWarSpearItem) {
				itemStack = player.getOffhandItem();
			}
			if (itemStack != ItemStack.EMPTY) {
				int count = getArmorCount(player);
				float damageMultiplier = 1 + DAMAGE_BONUS * count;
				float speedMultiplier = 1 + SPEED_BONUS * count;
				CompoundTag tag = itemStack.getTag();
				if (tag != null) {
					if (tag.getFloat("damageMultiplier") != damageMultiplier ||
							tag.getFloat("speedMultiplier") != speedMultiplier) {
						tag.putFloat("damageMultiplier", damageMultiplier);
						tag.putFloat("speedMultiplier", speedMultiplier);
						itemStack.setTag(tag);
					}
				}
			}
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

	@Override
	public Component getWeaponDescription(ItemStack stack, Level level) {
		return TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.piglins_warspear"));
	}

	@Override
	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		List<Component> list = new ArrayList<>();

		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.piglins_warspear_perk_1")));
		list.add(TooltipHelper.setSubPerkStyle(Component.translatable("tooltip.perk_weapons.piglins_warspear_perk_2",
				TooltipHelper.getPercentage(SPEED_BONUS))));
		list.add(TooltipHelper.setSubPerkStyle(Component.translatable("tooltip.perk_weapons.piglins_warspear_perk_3",
				TooltipHelper.getPercentage(DAMAGE_BONUS))));

		return list;
	}
}
