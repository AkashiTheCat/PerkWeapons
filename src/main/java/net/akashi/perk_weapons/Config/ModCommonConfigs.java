package net.akashi.perk_weapons.Config;

import net.akashi.perk_weapons.Bows.BaseBowItem;
import net.akashi.perk_weapons.Config.Properties.Bow.*;
import net.akashi.perk_weapons.Config.Properties.Crossbow.*;
import net.akashi.perk_weapons.Config.Properties.ModExplosionProperties;
import net.akashi.perk_weapons.Config.Properties.Spear.*;
import net.akashi.perk_weapons.Crossbows.BaseCrossbowItem;
import net.akashi.perk_weapons.Effects.InternalExplosionEffect;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.PerkGainingArrow;
import net.akashi.perk_weapons.Registry.ModItems;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.common.ForgeConfigSpec;

import static net.minecraft.world.item.ProjectileWeaponItem.ARROW_ONLY;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCommonConfigs {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;

	//General Configs
	public static ForgeConfigSpec.BooleanValue ENABLE_MELT_DOWN_ON_TABLE;
	public static ForgeConfigSpec.BooleanValue ENABLE_STAR_SHOOTER_ON_TABLE;
	public static ForgeConfigSpec.BooleanValue ENABLE_REGICIDE_ON_TABLE;
	public static ForgeConfigSpec.BooleanValue ENABLE_BLAZE_ON_TABLE;
	public static ForgeConfigSpec.BooleanValue BOW_ACCEPT_ALL_ARROW;
	public static ForgeConfigSpec.BooleanValue CROSSBOW_ACCEPT_ALL_ARROW;
	public static ForgeConfigSpec.BooleanValue CROSSBOW_ACCEPT_FIREWORK;
	public static ForgeConfigSpec.BooleanValue HOMING_WEAPON_ONLY_TRACK_MONSTERS;
	public static ForgeConfigSpec.IntValue PERK_ARROW_MAX_LEVEL_GAIN_PER_ARROW;
	public static ForgeConfigSpec.DoubleValue SPEAR_POWER_ENCHANT_BUFF_PERCENTAGE;
	public static ForgeConfigSpec.IntValue REPAIRER_LEVEL_COST;
	public static ForgeConfigSpec.DoubleValue REPAIRER_REPAIR_PERCENTAGE;

	//Effect Configs
	public static ModExplosionProperties INTERNAL_EXP_PROPERTIES;


	//Spear Configs
	public static SpearProperties IRON_SPEAR_PROPERTIES;
	public static SpearProperties GOLDEN_SPEAR_PROPERTIES;
	public static SpearProperties DIAMOND_SPEAR_PROPERTIES;
	public static SpearProperties NETHERITE_SPEAR_PROPERTIES;
	public static MegalodonProperties MEGALODON_PROPERTIES;
	public static ConduitGuardProperties CONDUIT_GUARD_PROPERTIES;
	public static PiglinsWarSpearProperties PIGLINS_WARSPEAR_PROPERTIES;
	public static DragonStrikeProperties DRAGON_STRIKE_PROPERTIES;
	public static ScourgeProperties SCOURGE_PROPERTIES;
	public static NetherGuideProperties NETHER_GUIDE_PROPERTIES;

	//Bow Configs
	public static BowProperties SHORT_BOW_PROPERTIES;
	public static BowProperties LONGBOW_PROPERTIES;
	public static PurgatoryProperties PURGATORY_PROPERTIES;
	public static ForestKeeperProperties FOREST_KEEPER_PROPERTIES;
	public static ElfsHarpProperties ELFS_HARP_PROPERTIES;
	public static FrostHunterProperties FROST_HUNTER_PROPERTIES;
	public static HouYiProperties HOU_YI_PROPERTIES;
	public static DevourerProperties DEVOURER_PROPERTIES;
	public static EndboreWandererProperties ENDBORE_WANDERER_PROPERTIES;

	//Crossbow Configs
	public static BeholderProperties BEHOLDER_PROPERTIES;
	public static LiberatorProperties LIBERATOR_PROPERTIES;
	public static TaintedFortuneProperties TAINTED_FORTUNE_PROPERTIES;
	public static SonicBlasterProperties SONIC_BLASTER_PROPERTIES;
	public static IncineratorProperties INCINERATOR_PROPERTIES;
	public static QueenBeeProperties QUEEN_BEE_PROPERTIES;
	public static PaladinProperties PALADIN_PROPERTIES;


	static {
		//General
		BUILDER.push("General");
		ENABLE_MELT_DOWN_ON_TABLE = BUILDER.comment("Set True To Allow Getting Melt Down Enchantment From Enchanting Table")
				.define("EnableMeltDownOnTable", true);
		ENABLE_STAR_SHOOTER_ON_TABLE = BUILDER.comment("Set True To Allow Getting Star Shooter Enchantment From Enchanting Table")
				.define("EnableStarShooterOnTable", true);
		ENABLE_REGICIDE_ON_TABLE = BUILDER.comment("Set True To Allow Getting Regicide Enchantment From Enchanting Table")
				.define("EnableRegicideOnTable", true);
		ENABLE_BLAZE_ON_TABLE = BUILDER.comment("Set True To Allow Getting Blaze Enchantment From Enchanting Table")
				.define("EnableBlazeOnTable", true);
		BOW_ACCEPT_ALL_ARROW = BUILDER.comment("Set True To Allow Modded Bows Use Tipped And Spectral Arrows As Ammo")
				.define("BowAcceptAllArrow", true);
		CROSSBOW_ACCEPT_ALL_ARROW = BUILDER.comment("Set True To Allow Modded Crossbows Use Tipped And Spectral Arrows As Ammo")
				.define("CrossbowAcceptAllArrow", true);
		CROSSBOW_ACCEPT_FIREWORK = BUILDER.comment("Set True To Allow Modded Crossbows Use Fireworks As Ammo")
				.define("CrossbowAcceptFirework", true);
		HOMING_WEAPON_ONLY_TRACK_MONSTERS = BUILDER.comment("Set True To Make Homing Weapons(eg.Conduit Guard) Only Tracks Monsters, Ignoring Passive Mobs")
				.define("HomingOnlyTrackMonsters", false);
		PERK_ARROW_MAX_LEVEL_GAIN_PER_ARROW = BUILDER.comment("Max Perk Level That Can Be Gained From One Perk Arrow Shot")
				.comment("Has Effect When The Arrow Pierces Multiple Enemies")
				.defineInRange("MaxPerkArrowLevelGain", 1, 0, 255);
		SPEAR_POWER_ENCHANT_BUFF_PERCENTAGE = BUILDER.comment("Ranged Damage Buff Ratio Per Power Level When Enchanted" +
				" On Modded Spears").defineInRange("PowerBuff", 0.2, 0, 255);
		REPAIRER_LEVEL_COST = BUILDER.comment("Level Required By Each Repairer To Repair Something On Anvil")
				.defineInRange("RepairerCost", 5, 0, 255);
		REPAIRER_REPAIR_PERCENTAGE = BUILDER.comment("Ratio Of Durability Repaired By Each Repairer")
				.defineInRange("RepairRatio", 0.2, 0, 1);
		BUILDER.pop();

		//Effects
		BUILDER.push("Effect: Internal Explosion");
		INTERNAL_EXP_PROPERTIES = new ModExplosionProperties(BUILDER,
				5, 5, 20, 20,
				1.0, false);
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
		MEGALODON_PROPERTIES = new MegalodonProperties(BUILDER, "Megalodon",
				9, 1.2,
				6, 2.5F,
				1, 120,
				1, 120,
				1, 120);
		CONDUIT_GUARD_PROPERTIES = new ConduitGuardProperties(BUILDER, "Conduit Guard",
				9, 1.2,
				9, 2.5F,
				5.0, 45,
				4, 0.005,
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
				50, 40,
				200);
		SCOURGE_PROPERTIES = new ScourgeProperties(BUILDER, "Scourge",
				8, 1.0,
				8, 2.5F,
				40, 3,
				40, 2,
				120, 0.3,
				10, 3,
				600, 127);
		NETHER_GUIDE_PROPERTIES = new NetherGuideProperties(BUILDER, "Nether Guide",
				10, 1.2,
				9, 2,
				0.3, 0.3,
				-0.3, 0.15,
				1, 60,
				1, 60,
				20);

		//Bows
		SHORT_BOW_PROPERTIES = new BowProperties(BUILDER, "Short Bow",
				12, 8,
				2.25, 1.0,
				0.0, 0.05, false,
				true);
		LONGBOW_PROPERTIES = new BowProperties(BUILDER, "Longbow",
				40, 15,
				4.5, 0.2,
				-0.5, 0.15, true,
				true);
		PURGATORY_PROPERTIES = new PurgatoryProperties(BUILDER, "Purgatory",
				50, 25,
				4.5, 0.2,
				4, -1.0,
				0.15, true,
				1.0, 30,
				3, 6,
				30, 8,
				1.0, false,
				20, 1);
		FOREST_KEEPER_PROPERTIES = new ForestKeeperProperties(BUILDER, "Forest Keeper",
				12, 8,
				2.25, 1.0,
				5, 40,
				0.1, true,
				0.0, 0.0, false);
		ELFS_HARP_PROPERTIES = new ElfsHarpProperties(BUILDER, "Elf's Harp",
				20, 10.0,
				3.0, 0.8,
				3, 100,
				1.0, 0.0,
				0.1, false);
		FROST_HUNTER_PROPERTIES = new FrostHunterProperties(BUILDER, "Frost Hunter",
				24, 9,
				3.0, 0.8,
				160, 600,
				600, 2,
				true, 0.0,
				0.1, false);
		HOU_YI_PROPERTIES = new HouYiProperties(BUILDER, "Hou Yi",
				40, 15,
				4.5, 0.2,
				-0.5, 0.15,
				-0.5, 0.03,
				4.0, -1.0,
				true);
		DEVOURER_PROPERTIES = new DevourerProperties(BUILDER, "Devourer",
				20, 7,
				3.0, 0.8,
				(byte) 2, 0.0,
				0.1, true);
		ENDBORE_WANDERER_PROPERTIES = new EndboreWandererProperties(BUILDER, "Endbore Wanderer",
				12, 9,
				2.5, 1.0,
				3, 10,
				0.5, 60,
				8, 70,
				7, 0.025,
				0.0, 0.0, false);

		//Crossbows
		BEHOLDER_PROPERTIES = new BeholderProperties(BUILDER, "Beholder",
				37, 12.0,
				4.4, 0.5,
				1, 0,
				1.0, -0.5,
				(byte) 2, (byte) 1, 40,
				true);
		LIBERATOR_PROPERTIES = new LiberatorProperties(BUILDER, "Liberator",
				50, 10.0,
				2.4, 1.0,
				1, 0,
				1.0, 0.0,
				(byte) 1, 1,
				2, false);
		TAINTED_FORTUNE_PROPERTIES = new TaintedFortuneProperties(BUILDER, "Tainted Fortune",
				25, 10.0,
				2.0, 1.2,
				1, 0,
				1.0, 0.0,
				1.0, false);
		SONIC_BLASTER_PROPERTIES = new SonicBlasterProperties(BUILDER, "Sonic Blaster",
				37, 20.0,
				0.1, 0.0,
				1, 0,
				1.0, -0.5,
				1.0, 24,
				1.0, -1,
				false, 0.6,
				true);
		INCINERATOR_PROPERTIES = new IncineratorProperties(BUILDER, "Incinerator",
				80, 13.0,
				3.2, 0.8,
				7, 10,
				1.0, -0.25,
				10, 10,
				1, true);
		QUEEN_BEE_PROPERTIES = new QueenBeeProperties(BUILDER, "Queen Bee",
				10, 8D,
				2.0, 1.2,
				1, 0,
				1.0, 7,
				4, 100,
				1, 80,
				40, 0.0,
				false);
		PALADIN_PROPERTIES = new PaladinProperties(BUILDER, "Paladin",
				20, 10.0,
				3.5, 1.0,
				1, 0,
				0.5, 1.0,
				50, -30,
				10, 0.07,
				10, 100,
				-0.3, true);

		SPEC = BUILDER.build();
	}

	@SubscribeEvent
	public static void onConfigLoad(ModConfigEvent event) {
		if (event.getConfig().getSpec() != SPEC)
			return;

		if (BOW_ACCEPT_ALL_ARROW.get()) {
			BaseBowItem.SUPPORTED_PROJECTILE = ARROW_ONLY;
		}

		if (CROSSBOW_ACCEPT_ALL_ARROW.get()) {
			BaseCrossbowItem.SUPPORTED_PROJECTILE = ARROW_ONLY;
		}
		if (CROSSBOW_ACCEPT_FIREWORK.get()) {
			BaseCrossbowItem.SUPPORTED_PROJECTILE = BaseCrossbowItem.SUPPORTED_PROJECTILE.or(
					(ammoStack) -> ammoStack.is(Items.FIREWORK_ROCKET));
		}

		InternalExplosionEffect.updateAttributesFromConfig(INTERNAL_EXP_PROPERTIES);

		ModItems.IRON_SPEAR.get().updateAttributesFromConfig(IRON_SPEAR_PROPERTIES);
		ModItems.GOLDEN_SPEAR.get().updateAttributesFromConfig(GOLDEN_SPEAR_PROPERTIES);
		ModItems.DIAMOND_SPEAR.get().updateAttributesFromConfig(DIAMOND_SPEAR_PROPERTIES);
		ModItems.NETHERITE_SPEAR.get().updateAttributesFromConfig(NETHERITE_SPEAR_PROPERTIES);

		ModItems.SPEAR_MEGALODON.get().updateAttributesFromConfig(MEGALODON_PROPERTIES);
		ModItems.SPEAR_CONDUIT_GUARD.get().updateAttributesFromConfig(CONDUIT_GUARD_PROPERTIES);
		ModItems.PIGLINS_WARSPEAR.get().updateAttributesFromConfig(PIGLINS_WARSPEAR_PROPERTIES);
		ModItems.DRAGON_STRIKE.get().updateAttributesFromConfig(DRAGON_STRIKE_PROPERTIES);
		ModItems.SCOURGE.get().updateAttributesFromConfig(SCOURGE_PROPERTIES);
		ModItems.NETHER_GUIDE.get().updateAttributesFromConfig(NETHER_GUIDE_PROPERTIES);

		ModItems.SHORT_BOW.get().updateAttributesFromConfig(SHORT_BOW_PROPERTIES);
		ModItems.LONGBOW.get().updateAttributesFromConfig(LONGBOW_PROPERTIES);

		ModItems.PURGATORY.get().updateAttributesFromConfig(PURGATORY_PROPERTIES);
		ModItems.FOREST_KEEPER.get().updateAttributesFromConfig(FOREST_KEEPER_PROPERTIES);
		ModItems.ELFS_HARP.get().updateAttributesFromConfig(ELFS_HARP_PROPERTIES);
		ModItems.FROST_HUNTER.get().updateAttributesFromConfig(FROST_HUNTER_PROPERTIES);
		ModItems.HOU_YI.get().updateAttributesFromConfig(HOU_YI_PROPERTIES);
		ModItems.DEVOURER.get().updateAttributesFromConfig(DEVOURER_PROPERTIES);
		ModItems.ENDBORE_WANDERER.get().updateAttributesFromConfig(ENDBORE_WANDERER_PROPERTIES);

		ModItems.OPPRESSOR.get().updateAttributesFromConfig(BEHOLDER_PROPERTIES);
		ModItems.LIBERATOR.get().updateAttributesFromConfig(LIBERATOR_PROPERTIES);
		ModItems.TAINTED_FORTUNE.get().updateAttributesFromConfig(TAINTED_FORTUNE_PROPERTIES);
		ModItems.SONIC_BLASTER.get().updateAttributesFromConfig(SONIC_BLASTER_PROPERTIES);
		ModItems.INCINERATOR.get().updateAttributesFromConfig(INCINERATOR_PROPERTIES);
		ModItems.QUEEN_BEE_CROSSBOW.get().updateAttributesFromConfig(QUEEN_BEE_PROPERTIES);
		ModItems.PALADIN.get().updateAttributesFromConfig(PALADIN_PROPERTIES);

		PerkGainingArrow.MAX_GAINED_LEVEL_FROM_ONE_ARROW = PERK_ARROW_MAX_LEVEL_GAIN_PER_ARROW.get();
	}
}
