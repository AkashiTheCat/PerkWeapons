package net.akashi.perk_weapons.Bows;

import net.akashi.perk_weapons.Client.ClientHelper;
import net.akashi.perk_weapons.Config.Properties.Bow.BowProperties;
import net.akashi.perk_weapons.Config.Properties.Bow.ForestKeeperProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.PerkUpdateArrow;
import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Util.IPerkItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpectralArrowItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.minecraft.world.item.enchantment.Enchantments.PUNCH_ARROWS;

@Mod.EventBusSubscriber(modid = PerkWeapons.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForestKeeperItem extends BaseBowItem implements IPerkItem {
	public static byte MAX_PERK_LEVEL = 5;
	public static float PERK_BUFF = 0.1F;
	public static int PERK_DROP_INTERVAL = 40;
	public static boolean ENABLE_SLOWDOWN_REMOVAL = true;
	private static float PERK_DROP_PER_TICK = (float) 1 / PERK_DROP_INTERVAL;
	private static final Map<UUID, Float> PERK_LEVEL_MAP = new HashMap<>();

	public ForestKeeperItem(Properties properties) {
		super(properties);
		RemoveGeneralEnchant(PUNCH_ARROWS);
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerPerkBowPropertyOverrides(this);
	}

	public ForestKeeperItem(int drawTime, float projectileDamage, float velocity,
	                        float inaccuracy, float speedModifier, float zoomFactor,
	                        boolean onlyMainHand, Properties properties) {
		super(drawTime, projectileDamage, velocity, inaccuracy, speedModifier, zoomFactor, onlyMainHand, properties);
		RemoveGeneralEnchant(PUNCH_ARROWS);
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerPerkBowPropertyOverrides(this);
	}


	@Override
	public AbstractArrow createArrow(Level level, ArrowItem arrowItem, ItemStack bowStack, ItemStack arrowStack, Player player) {
		PerkUpdateArrow arrow = new PerkUpdateArrow(ModEntities.PERK_UPDATE_ARROW.get(), level, player);
		if (arrowItem instanceof SpectralArrowItem) {
			arrow.setSpectralArrow(true);
		} else {
			arrow.setEffectsFromItem(arrowStack);
		}
		byte perkLevel = (byte) Math.ceil(getPerkLevel(player, bowStack));
		arrow.setBaseDamage(PROJECTILE_DAMAGE * (1 + PERK_BUFF * perkLevel) / VELOCITY);
		return arrow;
	}

	@Override
	public void updateAttributesFromConfig(BowProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof ForestKeeperProperties fProperties) {
			MAX_PERK_LEVEL = fProperties.MAX_PERK_LEVEL.get().byteValue();
			PERK_DROP_INTERVAL = fProperties.PERK_DROP_INTERVAL.get();
			PERK_BUFF = fProperties.PERK_DAMAGE_BUFF.get().floatValue();
			ENABLE_SLOWDOWN_REMOVAL = fProperties.ENABLE_SLOWDOWN_REMOVAL.get();
			PERK_DROP_PER_TICK = (float) 1 / PERK_DROP_INTERVAL;
		}
	}

	@Override
	public byte getMaxPerkLevel() {
		return MAX_PERK_LEVEL;
	}

	@Override
	public float getPerkLevel(LivingEntity entity, ItemStack stack) {
		return Math.max(0F, PERK_LEVEL_MAP.getOrDefault(entity.getUUID(), 0F));
	}

	@Override
	public boolean isPerkMax(LivingEntity entity, ItemStack stack) {
		return ((byte) Math.ceil(getPerkLevel(entity, stack))) == MAX_PERK_LEVEL;
	}

	@Override
	public void gainPerkLevel(LivingEntity entity, ItemStack stack) {
		if (PERK_LEVEL_MAP.containsKey(entity.getUUID())) {
			PERK_LEVEL_MAP.put(entity.getUUID(), (float) Math.min((Math.ceil(getPerkLevel(entity, stack)) + 1), MAX_PERK_LEVEL));
		} else {
			PERK_LEVEL_MAP.put(entity.getUUID(), 1F);
		}
	}

	@SubscribeEvent
	public static void onPlayerHurt(LivingHurtEvent event) {
		if (!event.getEntity().level().isClientSide()) {
			ForestKeeperItem.initPerkLevel(event.getEntity());
		}
	}


	@SubscribeEvent
	public static void onEntityTick(LivingEvent.LivingTickEvent event) {
		LivingEntity entity = event.getEntity();
		if (!entity.level().isClientSide()) {
			if (PERK_LEVEL_MAP.containsKey(entity.getUUID())) {
				float perkLevel = PERK_LEVEL_MAP.get(entity.getUUID());
				if (perkLevel > 0) {
					PERK_LEVEL_MAP.put(entity.getUUID(), perkLevel - PERK_DROP_PER_TICK);
				} else if (perkLevel != 0F) {
					initPerkLevel(entity);
				}
			}
		}
	}


	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (!event.side.isClient() && event.phase == TickEvent.Phase.END) {
			Player player = event.player;
			if (PERK_LEVEL_MAP.containsKey(player.getUUID())) {
				float perkLevel = PERK_LEVEL_MAP.get(player.getUUID());
				if (perkLevel > 0) {
					PERK_LEVEL_MAP.put(player.getUUID(), perkLevel - PERK_DROP_PER_TICK);
				} else if (perkLevel != 0F) {
					initPerkLevel(player);
				}
			}
		}
	}

	public static void initPerkLevel(LivingEntity entity) {
		PERK_LEVEL_MAP.put(entity.getUUID(), 0.0F);
	}
}
