package net.akashi.perk_weapons.Bows;

import com.google.common.collect.ImmutableMultimap;
import net.akashi.perk_weapons.Config.ModCommonConfigs;
import net.akashi.perk_weapons.Config.Properties.Bow.BowProperties;
import net.akashi.perk_weapons.Config.Properties.Bow.PurgatoryProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.ExplosiveArrow;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.PurgatoryArrow;
import net.akashi.perk_weapons.Registry.ModEnchantments;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpectralArrowItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.util.UUID;

import static net.minecraft.world.item.enchantment.Enchantments.FLAMING_ARROWS;

public class PurgatoryItem extends BaseBowItem {
	public static final UUID KNOCKBACK_RESISTANCE_UUID = UUID.fromString("8BF105EE-247E-40FD-ABDD-390525C7C7FF");
	public static byte PIERCE_LEVEL = 5;
	public static int FUSE_TIME;

	public PurgatoryItem(Properties properties) {
		super(properties);
		RemoveGeneralEnchant(FLAMING_ARROWS);
	}

	public PurgatoryItem(int drawTime, float projectileDamage, float velocity, float inaccuracy,
	                     float speedModifier, float knockBackResistance,
	                     float zoomFactor, boolean onlyMainHand, Properties properties) {
		super(drawTime, projectileDamage, velocity, inaccuracy, speedModifier, zoomFactor, onlyMainHand, properties);
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(MOVEMENT_SPEED_UUID,
				"Tool modifier", speedModifier, AttributeModifier.Operation.MULTIPLY_TOTAL));
		builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(KNOCKBACK_RESISTANCE_UUID,
				"Tool modifier", knockBackResistance, AttributeModifier.Operation.MULTIPLY_TOTAL));
		this.AttributeModifiers = builder.build();
		RemoveGeneralEnchant(FLAMING_ARROWS);

	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		return super.use(pLevel, pPlayer, pHand);
	}

	@Override
	public AbstractArrow createArrow(Level level, ArrowItem arrowItem, ItemStack bowStack, ItemStack arrowStack, Player player) {
		if (bowStack.getEnchantmentLevel(ModEnchantments.MELT_DOWN_ARROW.get()) != 0) {
			ExplosiveArrow arrow = new ExplosiveArrow(ModEntities.EXPLOSIVE_ARROW.get(), level, player, FUSE_TIME);
			if (arrowItem instanceof SpectralArrowItem) {
				arrow.setSpectralArrow(true);
			} else {
				arrow.setEffectsFromItem(arrowStack);
			}
			return setArrowAttributes(arrow);
		} else {
			PurgatoryArrow arrow = new PurgatoryArrow(ModEntities.PURGATORY_ARROW.get(), level, player);
			if (arrowItem instanceof SpectralArrowItem) {
				arrow.setSpectralArrow(true);
			} else {
				arrow.setEffectsFromItem(arrowStack);
			}
			return setArrowAttributes(arrow);
		}
	}

	@Override
	public void updateAttributesFromConfig(BowProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof PurgatoryProperties pProperties) {
			FUSE_TIME = pProperties.FUSE_TIME.get();
			PIERCE_LEVEL = pProperties.PIERCE_LEVEL.get().byteValue();
		}
		AddGeneralEnchant(ModEnchantments.MELT_DOWN_ARROW.get());
	}

	public AbstractArrow setArrowAttributes(AbstractArrow arrow) {
		arrow.setBaseDamage(PROJECTILE_DAMAGE / VELOCITY);
		arrow.setPierceLevel(PIERCE_LEVEL);
		return arrow;
	}

}
