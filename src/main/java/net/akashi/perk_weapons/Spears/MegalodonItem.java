package net.akashi.perk_weapons.Spears;

import com.mojang.blaze3d.shaders.Effect;
import net.akashi.perk_weapons.Config.Properties.Spear.MegalodonProperties;
import net.akashi.perk_weapons.Config.Properties.Spear.SpearProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownMegalodon;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownSpear;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.stringtemplate.v4.ST;

import java.util.ArrayList;
import java.util.List;

public class MegalodonItem extends BaseSpearItem {
	public static int DOLPHINS_GRACE_LEVEL = 1;
	public static int DOLPHINS_GRACE_TICKS = 120;
	public static int HASTE_LEVEL = 1;
	public static int HASTE_TICKS = 120;
	public static int STRENGTH_LEVEL = 1;
	public static int STRENGTH_TICKS = 120;

	public MegalodonItem(boolean isAdvanced, Properties pProperties) {
		super(isAdvanced, pProperties);
	}

	public MegalodonItem(float attackDamage, float attackSpeed, float throwDamage, float projectileVelocity, boolean isAdvanced, Properties pProperties) {
		super(attackDamage, attackSpeed, throwDamage, projectileVelocity, isAdvanced, pProperties);
	}

	@Override
	public void updateAttributesFromConfig(SpearProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof MegalodonProperties mProperties) {
			DOLPHINS_GRACE_LEVEL = mProperties.DOLPHINS_GRACE_LEVEL.get();
			DOLPHINS_GRACE_TICKS = mProperties.DOLPHINS_GRACE_TICKS.get();
			HASTE_LEVEL = mProperties.HASTE_LEVEL.get();
			HASTE_TICKS = mProperties.HASTE_TICKS.get();
			STRENGTH_LEVEL = mProperties.STRENGTH_LEVEL.get();
			STRENGTH_TICKS = mProperties.STRENGTH_TICKS.get();
		}
	}

	@Override
	public ThrownSpear createThrownSpear(Level pLevel, Player player, ItemStack pStack) {
		return new ThrownMegalodon(pLevel, player, pStack, ModEntities.THROWN_MEGALODON.get())
				.setBaseDamage(this.ThrowDamage);
	}

	@Override
	public Component getWeaponDescription(ItemStack stack, Level level) {
		return TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.megalodon"));
	}

	@Override
	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		List<Component> list = new ArrayList<>();
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.megalodon_perk_1")));

		Component dolphin_grace = TooltipHelper.setSubPerkStyle(
				Component.translatable("tooltip.perk_weapons.effect_format",
						MobEffects.DOLPHINS_GRACE.getDisplayName(),
						TooltipHelper.getRomanNumeral(DOLPHINS_GRACE_LEVEL),
						(float) DOLPHINS_GRACE_TICKS / 20));

		Component haste = TooltipHelper.setSubPerkStyle(
				Component.translatable("tooltip.perk_weapons.effect_format",
						MobEffects.DIG_SPEED.getDisplayName(),
						TooltipHelper.getRomanNumeral(HASTE_LEVEL),
						(float) HASTE_TICKS / 20));

		Component strength = TooltipHelper.setSubPerkStyle(
				Component.translatable("tooltip.perk_weapons.effect_format",
						MobEffects.DAMAGE_BOOST.getDisplayName(),
						TooltipHelper.getRomanNumeral(STRENGTH_LEVEL),
						(float) STRENGTH_TICKS / 20));

		if (DOLPHINS_GRACE_LEVEL > 0)
			list.add(dolphin_grace);
		if (HASTE_LEVEL > 0)
			list.add(haste);
		if (STRENGTH_LEVEL > 0)
			list.add(strength);

		return list;
	}
}
