package net.akashi.perk_weapons.Config.Properties.Spear;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PiglinsWarSpearProperties extends SpearProperties {
	public ModConfigSpec.ConfigValue<List<? extends String>> ALLOWED_ARMOR;
	public ModConfigSpec.DoubleValue DAMAGE_BONUS;
	public ModConfigSpec.DoubleValue SPEED_BONUS;

	public PiglinsWarSpearProperties(ModConfigSpec.Builder builder, String name,
	                                 float defaultMeleeDamage, double defaultAttackSpeed,
	                                 float defaultRangedDamage, float defaultVelocity,
	                                 int defaultMaxChargeTime, double defaultDamageBonus,
	                                 double defaultSpeedBonus) {
		super(builder, name, defaultMeleeDamage, defaultAttackSpeed, defaultRangedDamage, defaultVelocity,
				defaultMaxChargeTime, false);
		DAMAGE_BONUS = builder.comment("Damage = BaseDamage * (1 + ArmorCount * this)")
				.defineInRange("DamageBonus", defaultDamageBonus, 0, 10);
		SPEED_BONUS = builder.comment("AttackSpeed = BaseAttackSpeed * (1 + ArmorCount * this)")
				.defineInRange("SpeedBonus", defaultSpeedBonus, 0, 10);
		ALLOWED_ARMOR = builder.comment("List Of Armor That Will Buff The Weapon When Equipped")
				.defineList("AllowedArmor", Arrays.asList("minecraft:golden_helmet", "minecraft:golden_chestplate",
						"minecraft:golden_leggings", "minecraft:golden_boots"), () -> "", obj -> obj instanceof String);
		builder.pop();
	}

	@SuppressWarnings("unchecked")
	public List<String> getArmorList() {
		return (List<String>) ALLOWED_ARMOR.get();
	}

	public static List<Item> convertStringsToItems(List<String> itemIds) {
		List<Item> items = new ArrayList<>();

		for (String itemId : itemIds) {
			ResourceLocation resourceLocation = ResourceLocation.parse(itemId);
			Item item = BuiltInRegistries.ITEM.get(resourceLocation);
			items.add(item);
		}
		return items;
	}
}
