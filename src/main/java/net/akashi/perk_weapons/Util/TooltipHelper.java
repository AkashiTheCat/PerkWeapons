package net.akashi.perk_weapons.Util;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;

public class TooltipHelper {
	public static List<String> ROMAN_NUMERAL_LIST = new ArrayList<>();

	//Initialize roman numeral list from 1-30
	static {
		StringBuilder res = new StringBuilder();
		String temp = "";
		for (int i = 1; i <= 30; i++) {
			int j = i / 10;
			int k = i - j * 10;

			switch (k) {
				case 0:
					res = new StringBuilder("X".repeat(j));
					temp = res.toString();
					break;
				case 4:
					res = new StringBuilder(temp + "IV");
					break;
				case 5:
					res = new StringBuilder(temp + "V");
					break;
				case 9:
					res = new StringBuilder(temp + "IX");
					break;
				default:
					res.append("I");
					break;
			}
			ROMAN_NUMERAL_LIST.add(res.toString());
		}
	}

	public static String getRomanNumeral(int i) {
		if (i < 1 || i > 30) {
			return Integer.toString(i);
		}
		return ROMAN_NUMERAL_LIST.get(i - 1);
	}

	public static float convertTicksToSeconds(int ticks) {
		return (float) ticks / 20;
	}

	public static String getPercentage(float f) {
		return String.format("%.1f%%", f * 100);
	}

	public static String getPercentageWithSign(float f) {
		String s = String.format("%.1f%%", f * 100);
		return f >= 0 ? "+" + s : s;
	}

	public static String getDeltaWithSign(float f) {
		String s = String.format("%.1f", f);
		return f >= 0 ? "+" + s : s;
	}

	public static String getDeltaWithSign(int n) {
		String s = String.valueOf(n);
		return n >= 0 ? "+" + s : s;
	}

	public static MutableComponent getAttackDamageModifier(float deltaRatio) {
		return getRatioModifierWithStyle("tooltip.perk_weapons.attack_damage_modifier", deltaRatio);
	}

	public static MutableComponent getAttackSpeedModifier(float deltaRatio) {
		return getRatioModifierWithStyle("tooltip.perk_weapons.attack_speed_modifier", deltaRatio);
	}

	public static MutableComponent getArrowDamageModifier(float deltaRatio) {
		return getRatioModifierWithStyle("tooltip.perk_weapons.arrow_damage_modifier", deltaRatio);
	}

	public static MutableComponent getRatioModifierWithStyle(String key, float deltaRatio) {
		MutableComponent component = Component.translatable(key, getPercentageWithSign(deltaRatio));
		return deltaRatio >= 0 ? setBuffStyle(component) : setDebuffStyle(component);
	}

	public static MutableComponent getDeltaModifierWithStyle(String key, int delta) {
		MutableComponent component = Component.translatable(key, getDeltaWithSign(delta));
		return delta >= 0 ? setBuffStyle(component) : setDebuffStyle(component);
	}

	public static MutableComponent getDeltaModifierWithStyle(String key, float delta) {
		MutableComponent component = Component.translatable(key, getDeltaWithSign(delta));
		return delta >= 0 ? setBuffStyle(component) : setDebuffStyle(component);
	}

	public static MutableComponent getAmmoCapacityModifier(int delta) {
		return getDeltaModifierWithStyle("tooltip.perk_weapons.ammo_capacity_modifier", delta);
	}

	public static MutableComponent getReloadTimeModifier(int deltaTicks) {
		MutableComponent component = Component.translatable("tooltip.perk_weapons.reload_time_modifier",
				getDeltaWithSign(convertTicksToSeconds(deltaTicks)));
		return deltaTicks <= 0 ? setBuffStyle(component) : setDebuffStyle(component);
	}

	public static void addWeaponDescription(List<Component> tooltip, Component description) {
		if (description.equals(Component.empty()))
			return;

		Component keyShift = Component.translatable("tooltip.perk_weapons.key_shift")
				.withStyle(ChatFormatting.AQUA);
		Component keyHint = Component.translatable("tooltip.perk_weapons.description_key_hint", keyShift)
				.withStyle(ChatFormatting.DARK_GRAY);
		tooltip.add(Component.translatable("tooltip.perk_weapons.weapon_description_title")
				.withStyle(ChatFormatting.GOLD));

		if (!Screen.hasShiftDown()) {
			tooltip.add(keyHint);
		} else {
			tooltip.add(description);
		}

		tooltip.add(Component.empty());
	}

	public static void addPerkDescription(List<Component> tooltip, List<Component> descriptions) {
		if (descriptions.isEmpty())
			return;

		Component keyShift = Component.translatable("tooltip.perk_weapons.key_shift")
				.withStyle(ChatFormatting.AQUA);
		Component keyHint = Component.translatable("tooltip.perk_weapons.perk_description_key_hint", keyShift)
				.withStyle(ChatFormatting.DARK_GRAY);
		tooltip.add(Component.translatable("tooltip.perk_weapons.perk_description_title")
				.withStyle(ChatFormatting.GOLD));

		if (!Screen.hasShiftDown()) {
			tooltip.add(keyHint);
		} else {
			tooltip.addAll(descriptions);
		}

		tooltip.add(Component.empty());
	}

	public static MutableComponent getCrouchUseAbilityHint() {
		Minecraft mc = Minecraft.getInstance();
		Component crouch = mc.options.keyShift.getTranslatedKeyMessage().copy()
				.withStyle(ChatFormatting.AQUA);
		Component use = mc.options.keyUse.getTranslatedKeyMessage().copy()
				.withStyle(ChatFormatting.AQUA);
		return setKeyHintStyle(Component.translatable("tooltip.perk_weapons.crouch_use_key",
				crouch, use));
	}

	public static MutableComponent getCoolDownTip(int cd_ticks) {
		return Component.translatable("tooltip.perk_weapons.cd", convertTicksToSeconds(cd_ticks))
				.withStyle(ChatFormatting.GRAY);
	}

	public static MutableComponent convertToEmbeddedElement(int n) {
		return TooltipHelper.setEmbeddedElementStyle(Component.literal(String.valueOf(n)));
	}

	public static MutableComponent convertToEmbeddedElement(float f) {
		return TooltipHelper.setEmbeddedElementStyle(Component.literal(String.format("%.1f", f)));
	}

	public static MutableComponent convertToEmbeddedElement(double d) {
		return convertToEmbeddedElement((float) d);
	}

	public static MutableComponent convertToEmbeddedElement(Enchantment enchantment, int level) {
		return setEmbeddedElementStyle(enchantment.getFullname(level).copy());
	}

	public static MutableComponent convertToEmbeddedPercentage(double d) {
		return setEmbeddedElementStyle(Component.literal(getPercentage((float) d)));
	}

	public static MutableComponent convertToEmbeddedPercentage(float f) {
		return setEmbeddedElementStyle(Component.literal(getPercentage(f)));
	}

	public static MutableComponent setEmbeddedElementStyle(MutableComponent component) {
		return component.withStyle(ChatFormatting.YELLOW);
	}

	public static MutableComponent setKeyHintStyle(MutableComponent component) {
		return component.withStyle(ChatFormatting.DARK_GRAY);
	}

	public static MutableComponent setPerkStyle(MutableComponent component) {
		return component.withStyle(ChatFormatting.LIGHT_PURPLE);
	}

	public static MutableComponent setSubPerkStyle(MutableComponent component) {
		return component.withStyle(ChatFormatting.DARK_AQUA);
	}

	public static MutableComponent setBuffStyle(MutableComponent component) {
		return component.withStyle(ChatFormatting.DARK_GREEN);
	}

	public static MutableComponent setDebuffStyle(MutableComponent component) {
		return component.withStyle(ChatFormatting.RED);
	}

	public static MutableComponent setCommentStyle(MutableComponent component) {
		return component.withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
	}

}
