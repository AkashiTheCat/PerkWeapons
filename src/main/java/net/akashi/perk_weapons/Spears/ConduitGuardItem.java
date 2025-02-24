package net.akashi.perk_weapons.Spears;

import net.akashi.perk_weapons.Config.ModCommonConfigs;
import net.akashi.perk_weapons.Config.Properties.Spear.ConduitGuardProperties;
import net.akashi.perk_weapons.Config.Properties.Spear.SpearProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownConduitGuard;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownSpear;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.item.enchantment.Enchantments.LOYALTY;

public class ConduitGuardItem extends BaseSpearItem {
	public static double TRACKING_RANGE = 5.0;
	public static double TRACKING_THRESHOLD = 0.5;
	public static int RETURN_TIME = 80;

	public ConduitGuardItem(boolean isAdvanced, Properties pProperties) {
		super(isAdvanced, pProperties);
		RemoveGeneralEnchant(LOYALTY);
	}

	public ConduitGuardItem(float attackDamage, float attackSpeed, float throwDamage, float projectileVelocity,
	                        boolean isAdvanced, Properties pProperties) {
		super(attackDamage, attackSpeed, throwDamage, projectileVelocity, isAdvanced, pProperties);
		RemoveGeneralEnchant(LOYALTY);
	}

	@Override
	public ThrownSpear createThrownSpear(Level pLevel, Player player, ItemStack pStack) {
		return new ThrownConduitGuard(pLevel, player, pStack, RETURN_TIME, ModEntities.THROWN_CONDUIT_GUARD.get())
				.setBaseDamage(this.ThrowDamage);
	}

	@Override
	public void updateAttributesFromConfig(SpearProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof ConduitGuardProperties cProperties) {
			TRACKING_RANGE = cProperties.TRACKING_RANGE.get();
			TRACKING_THRESHOLD = cProperties.getMaxTrackingAngleInDotProductForm();
			RETURN_TIME = cProperties.RETURN_TIME.get();
		}
	}

	@Override
	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		List<Component> list = new ArrayList<>();

		Component trackingRange = Component.literal(String.valueOf(TRACKING_RANGE)).withStyle(ChatFormatting.WHITE);
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.conduit_guard_perk_1",
				trackingRange)));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.conduit_guard_perk_2")));

		return list;
	}

	@Override
	public Component getWeaponDescription(ItemStack stack, Level level) {
		return TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.conduit_guard"));
	}
}
