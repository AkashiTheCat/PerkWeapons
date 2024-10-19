package net.akashi.weaponmod.Spears;

import net.akashi.weaponmod.Config.ModCommonConfigs;
import net.akashi.weaponmod.Config.Properties.ConduitGuardProperties;
import net.akashi.weaponmod.Entities.Projectiles.ThrownConduitGuard;
import net.akashi.weaponmod.Entities.Projectiles.ThrownSpear;
import net.akashi.weaponmod.Registry.ModEntities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static net.minecraft.world.item.enchantment.Enchantments.LOYALTY;

public class ConduitGuardItem extends SpearItem{
	public ConduitGuardItem(boolean isAdvanced, Properties pProperties) {
		super(isAdvanced, pProperties);
	}

	public ConduitGuardItem(float attackDamage, float attackSpeed, float throwDamage, float projectileVelocity, boolean isAdvanced, Properties pProperties) {
		super(attackDamage, attackSpeed, throwDamage, projectileVelocity, isAdvanced, pProperties);
	}

	@Override
	public ThrownSpear createThrownSpear(Level pLevel, Player player, ItemStack pStack) {
		ConduitGuardProperties properties = ModCommonConfigs.CONDUIT_GUARD_PROPERTIES;
		return new ThrownConduitGuard(pLevel, player, pStack, getItemSlotIndex(player, pStack),
				properties.TRACKING_RANGE.get(),
				properties.getMaxTrackingAngleInDotProductForm(),
				properties.RETURN_TIME.get(),
				ModEntities.THROWN_CONDUIT_GUARD.get())
				.setBaseDamage(this.ThrowDamage);
	}
}
