package net.akashi.perk_weapons.Bows;

import net.akashi.perk_weapons.Client.ClientHelper;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.PerkUpdateArrow;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Util.IPerkItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpectralArrowItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class ElfsHarpItem extends BaseBowItem implements IPerkItem {
	public static byte MAX_PERK_LEVEL = 3;
	public static float PERK_BUFF = 1.0F;
	public static int GLOWING_TIME = 100;
	private byte perkLevel = 0;

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
	public int getIndicatorLength() {
		return 5 * perkLevel;
	}

	@Override
	public void gainPerkLevel() {
		if (perkLevel < MAX_PERK_LEVEL) {
			perkLevel++;
		}
	}

	@Override
	public byte getPerkLevel() {
		return this.perkLevel;
	}

	@Override
	public boolean isPerkMax() {
		System.out.println(perkLevel);
		return perkLevel == MAX_PERK_LEVEL;
	}

	@Override
	public AbstractArrow createArrow(Level level, ArrowItem arrowItem, ItemStack bowStack, ItemStack arrowStack, Player player) {
		PerkUpdateArrow arrow = new PerkUpdateArrow(ModEntities.PERK_UPDATE_ARROW.get(), level, player);
		if (!(arrowItem instanceof SpectralArrowItem)) {
			arrow.setEffectsFromItem(arrowStack);
		}
		arrow.setBaseDamage(0.0F);
		arrow.addEffect(new MobEffectInstance(MobEffects.GLOWING, GLOWING_TIME));
		if (perkLevel == MAX_PERK_LEVEL) {
			arrow.setMagicDamage(PROJECTILE_DAMAGE * (1 + PERK_BUFF));
			perkLevel = 0;
		} else {
			arrow.setMagicDamage(PROJECTILE_DAMAGE);
		}
		return arrow;
	}
}
