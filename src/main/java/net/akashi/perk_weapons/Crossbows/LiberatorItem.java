package net.akashi.perk_weapons.Crossbows;

import net.akashi.perk_weapons.Client.ClientHelper;
import net.akashi.perk_weapons.Config.Properties.Crossbow.CrossbowProperties;
import net.akashi.perk_weapons.Config.Properties.Crossbow.LiberatorProperties;
import net.akashi.perk_weapons.Registry.ModEnchantments;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.item.enchantment.Enchantments.MULTISHOT;
import static net.minecraft.world.item.enchantment.Enchantments.PIERCING;

public class LiberatorItem extends MagFedCrossbowItem {
	public static byte PIERCE_LEVEL = 1;
	public static byte MULTISHOT_BONUS = 1;
	public static byte AMMO_CAPACITY_REGICIDE = 2;

	public LiberatorItem(Properties pProperties) {
		super(pProperties);
		this.RemoveGeneralEnchant(PIERCING);

		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerLiberatorPropertyOverrides(this);
	}

	public LiberatorItem(int maxChargeTicks, float damage, float velocity, float inaccuracy,
	                     float speedModifier, boolean onlyAllowMainHand, Properties pProperties) {
		super(maxChargeTicks, damage, velocity, inaccuracy, speedModifier,
				1, onlyAllowMainHand, pProperties);
		RemoveGeneralEnchant(PIERCING);

		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerLiberatorPropertyOverrides(this);
	}

	@Override
	public void updateAttributesFromConfig(CrossbowProperties properties) {
		super.updateAttributesFromConfig(properties);
		AddGeneralEnchant(ModEnchantments.REGICIDE.get());
		if (properties instanceof LiberatorProperties lProperties) {
			PIERCE_LEVEL = lProperties.PIERCE_LEVEL.get().byteValue();
			AMMO_CAPACITY_REGICIDE = lProperties.CAPACITY_REGICIDE.get().byteValue();
			MULTISHOT_BONUS = lProperties.MULTISHOT_BONUS.get().byteValue();
		}
	}

	@Override
	protected Projectile getProjectile(Level level, LivingEntity shooter, ItemStack crossbowStack) {
		Projectile projectile = super.getProjectile(level, shooter, crossbowStack);
		if (projectile instanceof AbstractArrow arrow) {
			arrow.setPierceLevel(PIERCE_LEVEL);
			return arrow;
		}
		return projectile;
	}

	@Override
	public int getCrossbowEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
		if (enchantment == MULTISHOT) {
			return stack.getEnchantmentLevel(MULTISHOT) + MULTISHOT_BONUS;
		}
		return super.getEnchantmentLevel(stack, enchantment);
	}

	@Override
	public int getAmmoCapacity(ItemStack crossbowStack) {
		return crossbowStack.getEnchantmentLevel(ModEnchantments.REGICIDE.get()) > 0 ?
				AMMO_CAPACITY_REGICIDE : super.getAmmoCapacity(crossbowStack);
	}

	@Override
	public Component getWeaponDescription(ItemStack stack, Level level) {
		return TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.liberator"));
	}

	@Override
	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		List<Component> list = new ArrayList<>();

		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.liberator_perk_1",
				TooltipHelper.convertToEmbeddedElement(PIERCE_LEVEL))));
		list.add(TooltipHelper.setBuffStyle(Component.translatable("tooltip.perk_weapons.liberator_perk_2",
				MULTISHOT_BONUS, TooltipHelper.convertToEmbeddedElement(MULTISHOT, 1))));

		list.add(Component.empty());
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.when_enchanted",
				TooltipHelper.convertToEmbeddedElement(ModEnchantments.REGICIDE.get(), 1))));
		list.add(TooltipHelper.getAmmoCapacityModifier(AMMO_CAPACITY_REGICIDE - 1));
		return list;
	}
}
