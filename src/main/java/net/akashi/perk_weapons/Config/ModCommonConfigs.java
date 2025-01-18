package net.akashi.perk_weapons.Config;

import net.akashi.perk_weapons.Config.Properties.Bow.*;
import net.akashi.perk_weapons.Config.Properties.Spear.*;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.StarShooterArrow;
import net.akashi.perk_weapons.Registry.ModItems;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCommonConfigs {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;

	//General Configs
	public static ForgeConfigSpec.BooleanValue ENABLE_MELT_DOWN_ON_TABLE;
	public static ForgeConfigSpec.BooleanValue ENABLE_STAR_SHOOTER_ON_TABLE;
	public static ForgeConfigSpec.ConfigValue<List<? extends String>> ALWAYS_AS_FLYING;


	//Spear Configs
	public static SpearProperties IRON_SPEAR_PROPERTIES;
	public static SpearProperties GOLDEN_SPEAR_PROPERTIES;
	public static SpearProperties DIAMOND_SPEAR_PROPERTIES;
	public static SpearProperties NETHERITE_SPEAR_PROPERTIES;
	public static SpearProperties MEGALODON_PROPERTIES;
	public static ConduitGuardProperties CONDUIT_GUARD_PROPERTIES;
	public static PiglinsWarSpearProperties PIGLINS_WARSPEAR_PROPERTIES;
	public static DragonStrikeProperties DRAGON_STRIKE_PROPERTIES;
	public static ScourgeProperties SCOURGE_PROPERTIES;

	//Bow Configs
	public static BowProperties SHORT_BOW_PROPERTIES;
	public static BowProperties LONGBOW_PROPERTIES;
	public static PurgatoryProperties PURGATORY_PROPERTIES;
	public static ForestKeeperProperties FOREST_KEEPER_PROPERTIES;
	public static ElfsHarpProperties ELFS_HARP_PROPERTIES;
	public static FrostHunterProperties FROST_HUNTER_PROPERTIES;
	public static BowProperties HOU_YI_PROPERTIES;
	public static DevourerProperties DEVOURER_PROPERTIES;

	static {
		//General
		BUILDER.push("General");
		ENABLE_MELT_DOWN_ON_TABLE = BUILDER.comment("Set True To Allow Getting Melt Down Enchantment From Enchanting Table")
				.define("EnableMeltDownOnTable", true);
		ENABLE_STAR_SHOOTER_ON_TABLE = BUILDER.comment("Set True To Allow Getting Star Shooter Enchantment From Enchanting Table")
				.define("EnableStarShooterOnTable", true);
		ALWAYS_AS_FLYING = BUILDER.comment("List Of EntityTypes That Are Always Considered As Flying By Star Shooter Enchantment")
				.defineList("FlyingEntities", Arrays.asList("minecraft:phantom", "minecraft:blaze",
						"minecraft:ender_dragon"), obj -> obj instanceof String);
		BUILDER.pop();

		//Spears
		IRON_SPEAR_PROPERTIES = new SpearProperties(BUILDER, "Iron Spear",
				8, 1.1,
				7, 2.5F, true);
		GOLDEN_SPEAR_PROPERTIES = new SpearProperties(BUILDER, "Golden Spear",
				5, 1.6,
				6, 2.5F, true);
		DIAMOND_SPEAR_PROPERTIES = new SpearProperties(BUILDER, "Diamond Spear",
				9, 1.1,
				8, 2.5F, true);
		NETHERITE_SPEAR_PROPERTIES = new SpearProperties(BUILDER, "Netherite Spear",
				10, 1.1,
				9, 2.5F, true);
		MEGALODON_PROPERTIES = new SpearProperties(BUILDER, "Megalodon",
				9, 1.2,
				6, 2.5F, true);
		CONDUIT_GUARD_PROPERTIES = new ConduitGuardProperties(BUILDER, "Conduit Guard",
				9, 1.2,
				6, 2.5F,
				5.0F, 45,
				0.5, 80);
		PIGLINS_WARSPEAR_PROPERTIES = new PiglinsWarSpearProperties(BUILDER, "Piglin's WarSpear",
				5, 1.2,
				8, 2.5F,
				0.15, 0.15);
		DRAGON_STRIKE_PROPERTIES = new DragonStrikeProperties(BUILDER, "Dragon Strike",
				10, 1.2,
				10, 2.5F,
				4.0, 6.0,
				60, 5,
				0.5, 40,
				200);
		SCOURGE_PROPERTIES = new ScourgeProperties(BUILDER, "Scourge",
				8, 1.0,
				8, 2.5F,
				40, 3,
				40, 2,
				120, 0.3,
				10, 3,
				600, 255);

		//Bows
		SHORT_BOW_PROPERTIES = new BowProperties(BUILDER, "Short Bow",
				12, 8,
				2.25, 1.0,
				0.0, 0.05,
				true);
		LONGBOW_PROPERTIES = new BowProperties(BUILDER, "Longbow",
				40, 15,
				4.5, 0.2,
				-0.5, 0.15,
				true);
		PURGATORY_PROPERTIES = new PurgatoryProperties(BUILDER, "Purgatory",
				50, 25,
				4.5, 0.2,
				4, 30,
				-1.0, 0.15);
		FOREST_KEEPER_PROPERTIES = new ForestKeeperProperties(BUILDER, "Forest Keeper",
				12, 8,
				2.25, 1.0,
				5, 40,
				0.1, true,
				0.0, 0.0);
		ELFS_HARP_PROPERTIES = new ElfsHarpProperties(BUILDER, "Elf's Harp",
				20, 10.0,
				3.0, 0.8,
				3, 100,
				1.0, 0.0,
				0.1);
		FROST_HUNTER_PROPERTIES = new FrostHunterProperties(BUILDER, "Frost Hunter",
				24, 9,
				3.0, 0.8,
				160, 1200,
				400, 2,
				true, 0.0,
				0.1);
		HOU_YI_PROPERTIES = new BowProperties(BUILDER, "Hou Yi",
				40, 15,
				4.5, 0.2,
				-0.5, 0.15,
				true);
		DEVOURER_PROPERTIES = new DevourerProperties(BUILDER, "Devourer",
				20, 7,
				3.0, 0.8,
				(byte) 2, 0.0,
				0.1);
		SPEC = BUILDER.build();
	}

	@SubscribeEvent
	public static void onConfigLoad(ModConfigEvent event) {
		if (event.getConfig().getSpec() != SPEC)
			return;
		StarShooterArrow.updateEntityTypeListFromConfig(ALWAYS_AS_FLYING.get());

		ModItems.IRON_SPEAR.get().updateAttributesFromConfig(IRON_SPEAR_PROPERTIES);
		ModItems.GOLDEN_SPEAR.get().updateAttributesFromConfig(GOLDEN_SPEAR_PROPERTIES);
		ModItems.DIAMOND_SPEAR.get().updateAttributesFromConfig(DIAMOND_SPEAR_PROPERTIES);
		ModItems.NETHERITE_SPEAR.get().updateAttributesFromConfig(NETHERITE_SPEAR_PROPERTIES);

		ModItems.SPEAR_MEGALODON.get().updateAttributesFromConfig(MEGALODON_PROPERTIES);
		ModItems.SPEAR_CONDUIT_GUARD.get().updateAttributesFromConfig(CONDUIT_GUARD_PROPERTIES);
		ModItems.PIGLINS_WARSPEAR.get().updateAttributesFromConfig(PIGLINS_WARSPEAR_PROPERTIES);
		ModItems.DRAGON_STRIKE.get().updateAttributesFromConfig(DRAGON_STRIKE_PROPERTIES);
		ModItems.SCOURGE.get().updateAttributesFromConfig(SCOURGE_PROPERTIES);


		ModItems.SHORT_BOW.get().updateAttributesFromConfig(SHORT_BOW_PROPERTIES);
		ModItems.LONGBOW.get().updateAttributesFromConfig(LONGBOW_PROPERTIES);

		ModItems.PURGATORY.get().updateAttributesFromConfig(PURGATORY_PROPERTIES);
		ModItems.FOREST_KEEPER.get().updateAttributesFromConfig(FOREST_KEEPER_PROPERTIES);
		ModItems.ELFS_HARP.get().updateAttributesFromConfig(ELFS_HARP_PROPERTIES);
		ModItems.FROST_HUNTER.get().updateAttributesFromConfig(FROST_HUNTER_PROPERTIES);
		ModItems.HOU_YI.get().updateAttributesFromConfig(HOU_YI_PROPERTIES);
		ModItems.DEVOURER.get().updateAttributesFromConfig(DEVOURER_PROPERTIES);
	}
}
