package net.akashi.perk_weapons.Spears;

import net.akashi.perk_weapons.Config.Properties.Spear.ConduitGuardProperties;
import net.akashi.perk_weapons.Config.Properties.Spear.SpearProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownConduitGuard;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownSpear;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

import static net.minecraft.world.item.enchantment.Enchantments.LOYALTY;

public class ConduitGuardItem extends BaseSpearItem {
	public static int RETURN_TIME = 80;
	public static double VELOCITY_MULTIPLIER = 0.8;

	public static float HOMING_RANGE = 5f;
	public static float MAX_HOMING_ANGLE_COS = 0.707f;
	public static float MAX_TURN_ANGLE_COS = 0.997f;
	public static float MAX_TURN_ANGLE_SIN = 0.070f;
	public static float HOMING_ACCELERATION = 0.005f;

	public ConduitGuardItem(Properties pProperties) {
		super(pProperties);
		RemoveGeneralEnchant(LOYALTY);
	}

	public ConduitGuardItem(float attackDamage, float attackSpeed, float throwDamage, float projectileVelocity,
	                        int maxChargeTicks, boolean isAdvanced, Properties pProperties) {
		super(attackDamage, attackSpeed, throwDamage, projectileVelocity, maxChargeTicks, isAdvanced, pProperties);
		RemoveGeneralEnchant(LOYALTY);
	}

	@Override
	public ThrownSpear createThrownSpear(Level pLevel, Player player, ItemStack pStack) {
		return new ThrownConduitGuard(ModEntities.THROWN_CONDUIT_GUARD.get(), pLevel, player, pStack, RETURN_TIME);
	}

	@Override
	public void updateAttributesFromConfig(SpearProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof ConduitGuardProperties cProperties) {
			RETURN_TIME = cProperties.RETURN_TIME.get();
			VELOCITY_MULTIPLIER = cProperties.VELOCITY_MULTIPLIER.get();
			HOMING_RANGE = cProperties.HOMING_RANGE.get().floatValue();

			MAX_HOMING_ANGLE_COS = (float) Math.cos(Math.toRadians(cProperties.MAX_HOMING_ANGLE.get()));
			double radTurnRate = Math.toRadians(cProperties.MAX_TURN_RATE.get());
			MAX_TURN_ANGLE_COS = (float) Math.cos(radTurnRate);
			MAX_TURN_ANGLE_SIN = (float) Math.sin(radTurnRate);
		}
	}

	@Override
	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		List<Component> list = super.getPerkDescriptions(stack, level);

		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.conduit_guard_perk_1",
				TooltipHelper.convertToEmbeddedElement(HOMING_RANGE))));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.conduit_guard_perk_2")));

		return list;
	}

	@Override
	public Component getWeaponDescription(ItemStack stack, Level level) {
		return TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.conduit_guard"));
	}
}
