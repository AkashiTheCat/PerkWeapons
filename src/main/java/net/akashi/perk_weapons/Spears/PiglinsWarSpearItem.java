package net.akashi.perk_weapons.Spears;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.akashi.perk_weapons.Config.Properties.Spear.PiglinsWarSpearProperties;
import net.akashi.perk_weapons.Config.Properties.Spear.SpearProperties;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.world.item.enchantment.Enchantments.FIRE_ASPECT;

public class PiglinsWarSpearItem extends BaseSpearItem {
	public static List<ImmutableMultimap<Attribute, AttributeModifier>> MODIFIERS = new ArrayList<>();
	public static String TAG_ARMOR_COUNT = "armor_count";
	public static float DAMAGE_RATIO_BONUS = 0.1f;
	public static float SPEED_RATIO_BONUS = 0.1f;
	private static List<Item> ALLOWED_ARMORS = new ArrayList<>(Arrays.asList(
			Items.GOLDEN_HELMET,
			Items.GOLDEN_CHESTPLATE,
			Items.GOLDEN_LEGGINGS,
			Items.GOLDEN_BOOTS));

	public PiglinsWarSpearItem(Properties pProperties) {
		super(pProperties);
		AddGeneralEnchant(FIRE_ASPECT);
	}

	public PiglinsWarSpearItem(float attackDamage, float attackSpeed, float throwDamage, float projectileVelocity,
	                           int maxChargeTicks, boolean isAdvanced, Properties pProperties) {
		super(attackDamage, attackSpeed, throwDamage, projectileVelocity, maxChargeTicks, isAdvanced, pProperties);
		AddGeneralEnchant(FIRE_ASPECT);
	}

	@Override
	protected float getProjectileBaseDamage(ItemStack stack) {
		return THROW_DAMAGE * (1 + getArmorCount(stack) * DAMAGE_RATIO_BONUS);
	}

	@Override
	protected void buildAttributeModifiers() {
		super.buildAttributeModifiers();
		for (int i = 0; i < 5; i++) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier",
					MELEE_DAMAGE * (1 + i * DAMAGE_RATIO_BONUS) - 1, AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier",
					MELEE_SPEED * (1 + i * DAMAGE_RATIO_BONUS) - 4, AttributeModifier.Operation.ADDITION));
			MODIFIERS.add(builder.build());
		}
	}

	@Override
	public void updateAttributesFromConfig(SpearProperties properties) {
		if (properties instanceof PiglinsWarSpearProperties pProperties) {
			DAMAGE_RATIO_BONUS = pProperties.DAMAGE_BONUS.get().floatValue();
			SPEED_RATIO_BONUS = pProperties.SPEED_BONUS.get().floatValue();
			ALLOWED_ARMORS = PiglinsWarSpearProperties.convertStringsToItems(pProperties.getArmorList());
		}
		super.updateAttributesFromConfig(properties);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		if (slot == EquipmentSlot.MAINHAND) {
			return MODIFIERS.get(getArmorCount(stack));
		}
		return super.getAttributeModifiers(slot, stack);
	}

	public static int getArmorCount(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		return tag.contains(TAG_ARMOR_COUNT) ? tag.getInt(TAG_ARMOR_COUNT) : 0;
	}

	public static void setArmorCount(ItemStack stack, int count) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putInt(TAG_ARMOR_COUNT, count);
	}

	public static int getPlayerArmorCount(Player player) {
		int count = 0;
		for (ItemStack armor : player.getArmorSlots()) {
			if (ALLOWED_ARMORS.stream().anyMatch(pArmor -> pArmor.equals(armor.getItem()))) {
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
		List<Component> list = super.getPerkDescriptions(stack, level);

		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.piglins_warspear_perk_1")));
		list.add(TooltipHelper.getAttackSpeedModifier(SPEED_RATIO_BONUS));
		list.add(TooltipHelper.getAttackDamageModifier(DAMAGE_RATIO_BONUS));

		return list;
	}
}
