package net.akashi.perk_weapons.Spears;

import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownMegalodon;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownSpear;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MegalodonItem extends BaseSpearItem {
	public MegalodonItem(boolean isAdvanced, Properties pProperties) {
		super(isAdvanced, pProperties);
	}

	public MegalodonItem(float attackDamage, float attackSpeed, float throwDamage, float projectileVelocity, boolean isAdvanced, Properties pProperties) {
		super(attackDamage, attackSpeed, throwDamage, projectileVelocity, isAdvanced, pProperties);
	}

	@Override
	public ThrownSpear createThrownSpear(Level pLevel, Player player, ItemStack pStack) {
		return new ThrownMegalodon(pLevel, player, pStack, ModEntities.THROWN_MEGALODON.get())
				.setBaseDamage(this.ThrowDamage);
	}
}
