package net.akashi.weaponmod.Spears;

import net.akashi.weaponmod.Config.SpearProperties;
import net.akashi.weaponmod.Entities.Projectiles.ThrownMegalodon;
import net.akashi.weaponmod.Entities.Projectiles.ThrownSpear;
import net.akashi.weaponmod.Registry.ModEntities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MegalodonItem extends SpearItem{
	public MegalodonItem(float attackDamage, float attackSpeed, float throwDamage, float projectileVelocity, boolean isAdvanced, Properties pProperties) {
		super(attackDamage, attackSpeed, throwDamage, projectileVelocity, isAdvanced, pProperties);
	}

	public MegalodonItem(SpearProperties spearProperties, boolean isAdvanced, Properties pProperties) {
		super(spearProperties, isAdvanced, pProperties);
	}

	@Override
	public ThrownSpear createThrownSpear(Level pLevel, Player player, ItemStack pStack) {
		return new ThrownMegalodon(pLevel, player, pStack, getItemSlotIndex(player, pStack), ModEntities.THROWN_MEGALODON.get());
	}
}
