package net.akashi.perk_weapons.Bows;

import net.akashi.perk_weapons.Client.ClientHelper;
import net.akashi.perk_weapons.Config.Properties.Bow.BowProperties;
import net.akashi.perk_weapons.Config.Properties.Bow.ElfsHarpProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.PerkUpdateArrow;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Util.IPerkItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpectralArrowItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ElfsHarpItem extends BaseBowItem implements IPerkItem {
	public static byte MAX_PERK_LEVEL = 3;
	public static float PERK_BUFF = 1.0F;
	public static int GLOWING_TIME = 100;
	private static final Map<UUID, Byte> PERK_LEVEL_MAP = new HashMap<>();

	public ElfsHarpItem(Properties properties) {
		super(properties);
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerPerkBowPropertyOverrides(this);
	}

	public ElfsHarpItem(int drawTime, float projectileDamage, float velocity, float inaccuracy, float speedModifier, float zoomFactor, Properties properties) {
		super(drawTime, projectileDamage, velocity, inaccuracy, speedModifier, zoomFactor, properties);
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerPerkBowPropertyOverrides(this);
	}

	@Override
	public byte getMaxPerkLevel() {
		return MAX_PERK_LEVEL;
	}

	@Override
	public float getPerkLevel(Player player, ItemStack stack) {
		return PERK_LEVEL_MAP.getOrDefault(player.getUUID(), (byte) 0);
	}

	@Override
	public boolean isPerkMax(Player player, ItemStack stack) {
		return ((byte) Math.ceil(getPerkLevel(player, stack))) == MAX_PERK_LEVEL;
	}

	@Override
	public void gainPerkLevel(Player player, ItemStack stack) {
		if (PERK_LEVEL_MAP.containsKey(player.getUUID())) {
			PERK_LEVEL_MAP.put(player.getUUID(), (byte) (PERK_LEVEL_MAP.get(player.getUUID()) + 1));
		} else {
			PERK_LEVEL_MAP.put(player.getUUID(), (byte) 1);
		}
	}

	@Override
	public AbstractArrow createArrow(Level level, ArrowItem arrowItem, ItemStack bowStack, ItemStack arrowStack, Player player) {
		PerkUpdateArrow arrow = new PerkUpdateArrow(ModEntities.PERK_UPDATE_ARROW.get(), level, player);
		if (!(arrowItem instanceof SpectralArrowItem)) {
			arrow.setEffectsFromItem(arrowStack);
		}
		arrow.setBaseDamage(0.0F);
		arrow.addEffect(new MobEffectInstance(MobEffects.GLOWING, GLOWING_TIME));
		if (isPerkMax(player, bowStack)) {
			arrow.setMagicDamage(PROJECTILE_DAMAGE * (1 + PERK_BUFF));
			arrow.setRenderTrail(true);
			initPerkLevel(player);
		} else {
			arrow.setMagicDamage(PROJECTILE_DAMAGE);
		}
		return arrow;
	}

	@Override
	public void updateAttributesFromConfig(BowProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof ElfsHarpProperties eProperties) {
			MAX_PERK_LEVEL = eProperties.MAX_PERK_LEVEL.get().byteValue();
			PERK_BUFF = eProperties.PERK_BUFF.get().floatValue();
			GLOWING_TIME = eProperties.GLOWING_TIME.get();
		}
	}

	public void initPerkLevel(Player player) {
		PERK_LEVEL_MAP.put(player.getUUID(), (byte) 0);
	}
}
