package net.akashi.perk_weapons.Config.Properties.Spear;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PiglinsWarSpearProperties extends SpearProperties {
	public ForgeConfigSpec.ConfigValue<List<? extends String>> ALLOWED_ARMOR;
	public ForgeConfigSpec.DoubleValue DAMAGE_BONUS;
	public ForgeConfigSpec.DoubleValue SPEED_BONUS;

	public PiglinsWarSpearProperties(ForgeConfigSpec.Builder builder, String name,
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
						"minecraft:golden_leggings", "minecraft:golden_boots"), obj -> obj instanceof String);
		builder.pop();
	}

	@SuppressWarnings("unchecked")
	public List<String> getArmorList() {
		return (List<String>) ALLOWED_ARMOR.get();
	}

	public static List<Item> convertStringsToItems(List<String> itemIds) {
		List<Item> items = new ArrayList<>();

		for (String itemId : itemIds) {
			ResourceLocation resourceLocation = new ResourceLocation(itemId);
			Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
			if (item != null) {
				items.add(item);
			}
		}
		return items;
	}
}
