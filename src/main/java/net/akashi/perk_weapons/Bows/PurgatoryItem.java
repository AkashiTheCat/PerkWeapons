package net.akashi.perk_weapons.Bows;

import com.google.common.collect.ImmutableMultimap;
import net.akashi.perk_weapons.Config.Properties.Bow.BowProperties;
import net.akashi.perk_weapons.Config.Properties.Bow.PurgatoryProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.ExplosiveArrow;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.PurgatoryArrow;
import net.akashi.perk_weapons.Registry.ModEffects;
import net.akashi.perk_weapons.Registry.ModEnchantments;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.minecraft.world.item.enchantment.Enchantments.FLAMING_ARROWS;
import static net.minecraft.world.item.enchantment.Enchantments.UNBREAKING;

public class PurgatoryItem extends BaseBowItem {
	public static final UUID KNOCKBACK_RESISTANCE_UUID = UUID.fromString("8BF105EE-247E-40FD-ABDD-390525C7C7FF");
	public static double KNOCKBACK_RESISTANCE = 10;
	public static byte PIERCE_LEVEL = 5;
	public static int FUSE_TIME = 30;
	public static float EXP_INNER_R = 2;
	public static float EXP_INNER_DMG = 30;
	public static float EXP_OUTER_R = 5;
	public static float EXP_OUTER_DMG = 5;
	public static float EXP_KNOCKBACK = 1;
	public static boolean EXP_IGNORE_WALL = false;
	public static int INTERNAL_EXP_DURATION = 20;
	public static int INTERNAL_EXP_LEVEL = 1;

	public PurgatoryItem(Properties properties) {
		super(properties);
		buildAttributeModifierMap();
		RemoveGeneralEnchant(FLAMING_ARROWS);
		RemoveGeneralEnchant(UNBREAKING);
	}

	public PurgatoryItem(int drawTime, float projectileDamage, float velocity, float inaccuracy,
	                     float speedModifier, float knockBackResistance,
	                     float zoomFactor, boolean onlyMainHand, Properties properties) {
		super(drawTime, projectileDamage, velocity, inaccuracy, speedModifier, zoomFactor, onlyMainHand, properties);
		KNOCKBACK_RESISTANCE = knockBackResistance;
		buildAttributeModifierMap();
		RemoveGeneralEnchant(FLAMING_ARROWS);
		RemoveGeneralEnchant(UNBREAKING);
	}

	private void buildAttributeModifierMap() {
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		if (AttributeModifiers != null)
			builder.putAll(AttributeModifiers);
		builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(
				KNOCKBACK_RESISTANCE_UUID, "Knockback Resistance", KNOCKBACK_RESISTANCE,
				AttributeModifier.Operation.ADDITION
		));
		AttributeModifiers = builder.build();
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {
		return super.use(pLevel, pPlayer, pHand);
	}

	@Override
	protected SoundEvent getShootingSound() {
		return SoundEvents.BLAZE_SHOOT;
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
			arrow.setExplosionAttributes(EXP_INNER_R, EXP_OUTER_R, EXP_INNER_DMG, EXP_OUTER_DMG,
					EXP_KNOCKBACK, EXP_IGNORE_WALL, INTERNAL_EXP_DURATION, INTERNAL_EXP_LEVEL - 1);
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
			KNOCKBACK_RESISTANCE = pProperties.KNOCKBACK_RESISTANCE.get();
			FUSE_TIME = pProperties.FUSE_TIME.get();
			PIERCE_LEVEL = pProperties.PIERCE_LEVEL.get().byteValue();
			EXP_INNER_R = pProperties.EXPLOSION_PROPERTIES.INNER_R.get().floatValue();
			EXP_OUTER_R = pProperties.EXPLOSION_PROPERTIES.OUTER_R.get().floatValue();
			EXP_INNER_DMG = pProperties.EXPLOSION_PROPERTIES.INNER_DMG.get().floatValue();
			EXP_OUTER_DMG = pProperties.EXPLOSION_PROPERTIES.OUTER_DMG.get().floatValue();
			EXP_KNOCKBACK = pProperties.EXPLOSION_PROPERTIES.KNOCKBACK.get().floatValue();
			EXP_IGNORE_WALL = pProperties.EXPLOSION_PROPERTIES.IGNORE_WALL.get();
			INTERNAL_EXP_DURATION = pProperties.INTERNAL_EXP_EFFECT_TIME.get();
			INTERNAL_EXP_LEVEL = pProperties.INTERNAL_EXP_EFFECT_LEVEL.get();
			buildAttributeModifierMap();
		}
		AddGeneralEnchant(ModEnchantments.MELT_DOWN_ARROW.get());
	}

	public AbstractArrow setArrowAttributes(AbstractArrow arrow) {
		arrow.setBaseDamage(PROJECTILE_DAMAGE / VELOCITY);
		arrow.setPierceLevel(PIERCE_LEVEL);
		return arrow;
	}

	@Override
	public Component getWeaponDescription(ItemStack stack, Level level) {
		return TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.purgatory"));
	}

	@Override
	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		List<Component> list = new ArrayList<>();
		list.add(TooltipHelper.setEmbeddedElementStyle(Component.translatable("tooltip.perk_weapons.purgatory_perk_1")));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.purgatory_perk_2")));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.purgatory_perk_3",
				TooltipHelper.convertToEmbeddedElement(PIERCE_LEVEL))));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.when_enchanted",
				TooltipHelper.convertToEmbeddedElement(ModEnchantments.MELT_DOWN_ARROW.get(), 1))));
		list.add(TooltipHelper.setDebuffStyle(Component.translatable("tooltip.perk_weapons.purgatory_perk_4")));
		list.add(TooltipHelper.setSubPerkStyle(Component.translatable("tooltip.perk_weapons.purgatory_perk_5",
				TooltipHelper.convertToEmbeddedElement(TooltipHelper.convertTicksToSeconds(FUSE_TIME)))));
		list.add(TooltipHelper.setSubPerkStyle(Component.translatable("tooltip.perk_weapons.purgatory_perk_6")));

		list.add(TooltipHelper.setSubPerkStyle(Component.translatable("tooltip.perk_weapons.effect_format",
				ModEffects.INTERNAL_EXPLOSION.get().getDisplayName(),
				TooltipHelper.getRomanNumeral(1),
				TooltipHelper.convertTicksToSeconds(INTERNAL_EXP_DURATION))));

		return list;
	}
}
