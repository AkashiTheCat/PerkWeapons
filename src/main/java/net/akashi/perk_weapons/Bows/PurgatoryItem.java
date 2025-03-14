package net.akashi.perk_weapons.Bows;

import com.google.common.collect.ImmutableMultimap;
import net.akashi.perk_weapons.Config.ModCommonConfigs;
import net.akashi.perk_weapons.Config.Properties.Bow.BowProperties;
import net.akashi.perk_weapons.Config.Properties.Bow.PurgatoryProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.ExplosiveArrow;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.PurgatoryArrow;
import net.akashi.perk_weapons.Registry.ModEnchantments;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpectralArrowItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.minecraft.world.item.enchantment.Enchantments.FLAMING_ARROWS;
import static net.minecraft.world.item.enchantment.Enchantments.UNBREAKING;

public class PurgatoryItem extends BaseBowItem {
	public static final UUID KNOCKBACK_RESISTANCE_UUID = UUID.fromString("8BF105EE-247E-40FD-ABDD-390525C7C7FF");
	public static byte PIERCE_LEVEL = 5;
	public static int FUSE_TIME;

	public PurgatoryItem(Properties properties) {
		super(properties);
		RemoveGeneralEnchant(FLAMING_ARROWS);
		RemoveGeneralEnchant(UNBREAKING);
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
		RemoveGeneralEnchant(UNBREAKING);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		return super.use(pLevel, pPlayer, pHand);
	}

	@Override
	protected void onPlayerBowShoot(Level level, Player player) {
		if (!level.isClientSide()) {
			Vec3 viewVec = player.getViewVector(0);
			Vec3 pos = player.getPosition(0).add(viewVec.normalize().scale(8.0));
			level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.CREEPER_PRIMED, SoundSource.PLAYERS,
					1.0f, 1.0f);
		}
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

		return list;
	}
}
