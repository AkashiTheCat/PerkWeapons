package net.akashi.weaponmod.Bows;

import net.akashi.weaponmod.Config.Properties.Bow.BowProperties;
import net.akashi.weaponmod.Config.Properties.Bow.ForestKeeperProperties;
import net.akashi.weaponmod.Entities.Projectiles.Arrows.BaseArrow;
import net.akashi.weaponmod.Entities.Projectiles.Arrows.ForestKeeperArrow;
import net.akashi.weaponmod.Registry.ModEntities;
import net.akashi.weaponmod.WeaponMod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpectralArrowItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraft.world.item.enchantment.Enchantments.PUNCH_ARROWS;

@Mod.EventBusSubscriber(modid = WeaponMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForestKeeperItem extends BaseBowItem {
	public static byte MAX_PERK_LEVEL = 5;
	public static float PERK_BUFF = 0.1F;
	public static int PERK_DROP_INTERVAL = 40;
	public static boolean ENABLE_SLOWDOWN_REMOVAL = true;
	private byte perkLevel = 0;
	private int perkDropTimer = 0;

	public ForestKeeperItem(Properties properties) {
		super(properties);
		RemoveGeneralEnchant(PUNCH_ARROWS);
	}

	public ForestKeeperItem(int drawTime, float projectileDamage, float velocity,
	                        float inaccuracy, float speedModifier, float zoomFactor, Properties properties) {
		super(drawTime, projectileDamage, velocity, inaccuracy, speedModifier, zoomFactor, properties);
		RemoveGeneralEnchant(PUNCH_ARROWS);
	}

	@Override
	public AbstractArrow createArrow(Level level, ArrowItem arrowItem, ItemStack bowStack, ItemStack arrowStack, Player player) {
		ForestKeeperArrow arrow = new ForestKeeperArrow(ModEntities.BASE_ARROW.get(), level, player);
		if (arrowItem instanceof SpectralArrowItem) {
			arrow.setSpectralArrow(true);
		} else {
			arrow.setEffectsFromItem(arrowStack);
		}
		arrow.setBaseDamage(PROJECTILE_DAMAGE * (1 + PERK_BUFF * this.perkLevel) / VELOCITY);
		return arrow;
	}

	@Override
	public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
		super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
		if (!level.isClientSide()) {
			if (this.perkDropTimer > 0) {
				perkDropTimer--;
			} else if (perkLevel > 0) {
				perkLevel--;
				perkDropTimer = PERK_DROP_INTERVAL;
			}
		}
	}

	@Override
	public void updateAttributesFromConfig(BowProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof ForestKeeperProperties fProperties) {
			MAX_PERK_LEVEL = fProperties.MAX_PERK_LEVEL.get().byteValue();
			PERK_DROP_INTERVAL = fProperties.PERK_DROP_INTERVAL.get();
			PERK_BUFF = fProperties.PERK_DAMAGE_BUFF.get().floatValue();
			ENABLE_SLOWDOWN_REMOVAL = fProperties.ENABLE_SLOWDOWN_REMOVAL.get();
		}
	}

	@SubscribeEvent
	public static void onPlayerHurt(LivingHurtEvent event) {
		if (event.getEntity() instanceof Player player && !player.level().isClientSide()) {
			player.getInventory().items.forEach((stack) -> {
				if (stack.getItem() instanceof ForestKeeperItem bowItem) {
					bowItem.clearPerkLevel();
				}
			});
		}
	}

	public float getPerkIndicatorAmount() {
		return this.perkLevel - 1 + (float) (perkDropTimer / PERK_DROP_INTERVAL);
	}

	public byte getPerkLevel() {
		return this.perkLevel;
	}

	public void gainPerkLevel() {
		this.perkDropTimer = PERK_DROP_INTERVAL;
		if (this.perkLevel < MAX_PERK_LEVEL) {
			this.perkLevel++;
		}
	}

	public void clearPerkLevel() {
		this.perkLevel = 0;
		this.perkDropTimer = 0;
	}

}
