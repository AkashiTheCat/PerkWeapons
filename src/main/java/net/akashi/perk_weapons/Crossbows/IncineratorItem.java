package net.akashi.perk_weapons.Crossbows;

import net.akashi.perk_weapons.Config.Properties.Crossbow.CrossbowProperties;
import net.akashi.perk_weapons.Config.Properties.Crossbow.IncineratorProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.IncineratorArrow;
import net.akashi.perk_weapons.Registry.ModEnchantments;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Util.SoundEventHolder;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.minecraft.world.item.enchantment.Enchantments.*;

public class IncineratorItem extends BaseCrossbowItem {
	public static int FIRE_ARROW_KNOCKBACK_BONUS = 1;
	public static int BLAZE_AMMO_CAPACITY = 10;
	public static int BLAZE_RELOAD_ADDITION = 10;

	public IncineratorItem(Properties pProperties) {
		super(pProperties);
		RemoveGeneralEnchant(MULTISHOT);
		RemoveGeneralEnchant(UNBREAKING);
		AddConflictEnchant(MULTISHOT);
	}

	public IncineratorItem(int maxChargeTicks, float damage, float velocity, float inaccuracy,
	                       int ammoCapacity, int fireInterval, float speedModifier,
	                       boolean onlyAllowMainHand, Properties pProperties) {
		super(maxChargeTicks, damage, velocity, inaccuracy, ammoCapacity, fireInterval,
				speedModifier, onlyAllowMainHand, pProperties);
		RemoveGeneralEnchant(MULTISHOT);
		RemoveGeneralEnchant(UNBREAKING);
		AddConflictEnchant(MULTISHOT);
	}

	@Override
	public void updateAttributesFromConfig(CrossbowProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof IncineratorProperties IProperties) {
			BLAZE_AMMO_CAPACITY = IProperties.BLAZE_AMMO_CAPACITY.get();
			BLAZE_RELOAD_ADDITION = IProperties.BLAZE_RELOAD_INCREMENT.get();
			FIRE_ARROW_KNOCKBACK_BONUS = IProperties.FIRE_ARROW_KNOCKBACK_BONUS.get();
		}
		AddConflictEnchant(ModEnchantments.BLAZE.get());
	}

	@Override
	protected @NotNull SoundEventHolder getShootSound(ItemStack crossbowStack) {
		return new SoundEventHolder(SoundEvents.BLAZE_SHOOT);
	}

	@Override
	protected Projectile getProjectile(Level level, LivingEntity shooter, ItemStack crossbowStack) {
		BaseCrossbowItem crossbowItem = (BaseCrossbowItem) crossbowStack.getItem();
		ItemStack ammoStack = crossbowItem.getLastChargedProjectile(crossbowStack);

		if (ammoStack.is(Items.FIREWORK_ROCKET)) {
			return new FireworkRocketEntity(level, ammoStack, shooter, shooter.getX(),
					shooter.getEyeY() - (double) 0.15F, shooter.getZ(), true);
		}

		IncineratorArrow arrow = new IncineratorArrow(ModEntities.INCINERATOR_ARROW.get(), level, shooter);
		if (ammoStack.is(Items.SPECTRAL_ARROW)) {
			arrow.setSpectralArrow(true);
		} else {
			arrow.setEffectsFromItem(ammoStack);
		}
		arrow.setShotFromCrossbow(true);

		return arrow;
	}

	@Override
	public int getMaxChargeTicks(ItemStack crossbowStack) {
		if (crossbowStack.getEnchantmentLevel(ModEnchantments.BLAZE.get()) > 0) {
			return super.getMaxChargeTicks(crossbowStack) + BLAZE_RELOAD_ADDITION;
		}
		return super.getMaxChargeTicks(crossbowStack);
	}

	public int getAmmoCapacity(ItemStack crossbowStack) {
		return crossbowStack.getEnchantmentLevel(ModEnchantments.BLAZE.get()) > 0 ?
				BLAZE_AMMO_CAPACITY : super.getAmmoCapacity(crossbowStack);
	}

	@Override
	public Component getWeaponDescription(ItemStack stack, Level level) {
		return TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.incinerator"));
	}

	@Override
	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		List<Component> list = super.getPerkDescriptions(stack, level);

		list.add(TooltipHelper.setEmbeddedElementStyle(Component.translatable("tooltip.perk_weapons.incinerator_perk_1")));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.incinerator_perk_2")));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.incinerator_perk_3")));
		list.add(Component.empty());
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.when_enchanted",
				TooltipHelper.convertToEmbeddedElement(ModEnchantments.BLAZE.get(), 1))));
		list.add(TooltipHelper.getAmmoCapacityModifier(BLAZE_AMMO_CAPACITY - AMMO_CAPACITY));
		list.add(TooltipHelper.getChargeTimeModifier(BLAZE_RELOAD_ADDITION));

		return list;
	}
}
