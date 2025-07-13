package net.akashi.perk_weapons.Spears;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.akashi.perk_weapons.Client.ClientHelper;
import net.akashi.perk_weapons.Config.Properties.Spear.NetherGuideProperties;
import net.akashi.perk_weapons.Config.Properties.Spear.SpearProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownNetherGuide;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownSpear;
import net.akashi.perk_weapons.Registry.ModAttributes;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Util.IPerkItem;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NetherGuideItem extends BaseSpearItem implements IPerkItem {
	protected static final UUID DAMAGE_RESISTANCE_UUID = UUID.fromString("c9352abe-0478-3cdc-f44a-e5bba50e6ff5");
	protected static final UUID MOVEMENT_SPEED_UUID = UUID.fromString("46132106-5eea-5134-cecf-72294309fae9");
	private Multimap<Attribute, AttributeModifier> WarpedModeAttributeModifiers;

	private static final byte WARPED_MODE_PERK_LEVEL = 0;
	private static float WARPED_MELEE_DAMAGE_BONUS_RATIO = 0.3F;
	private static float WARPED_THROW_DAMAGE_BONUS_RATIO = 0.3F;
	private static float WARPED_DAMAGE_RESISTANCE = -30F;
	private static float WARPED_MOVEMENT_SPEED_BONUS_RATIO = 0.15F;

	private static final byte CRIMSON_MODE_PERK_LEVEL = 1;
	public static int CRIMSON_WEAKNESS_LEVEL_ON_TARGET_WHEN_HIT = 1;
	public static int CRIMSON_WEAKNESS_DURATION_ON_TARGET_WHEN_HIT = 60;
	public static int CRIMSON_REGENERATION_LEVEL_ON_SELF_WHEN_HIT = 1;
	public static int CRIMSON_REGENERATION_DURATION_ON_SELF_WHEN_HIT = 60;

	private static int MODE_SWITCH_COOLDOWN = 20;

	public NetherGuideItem(Properties pProperties) {
		super(pProperties);
		buildAttributeModifiers();
		if (FMLEnvironment.dist.isClient()) {
			ClientHelper.registerNetherGuidePropertyOverrides(this);
			ClientHelper.registerPerkItemPropertyOverrides(this);
		}
	}

	public NetherGuideItem(float attackDamage, float attackSpeed,
	                       float throwDamage, float projectileVelocity,
	                       int maxChargeTicks, boolean isAdvanced,
	                       Properties pProperties) {
		super(attackDamage, attackSpeed, throwDamage, projectileVelocity, maxChargeTicks, isAdvanced, pProperties);
		buildAttributeModifiers();
		if (FMLEnvironment.dist.isClient()) {
			ClientHelper.registerNetherGuidePropertyOverrides(this);
			ClientHelper.registerPerkItemPropertyOverrides(this);
		}
	}

	private void buildAttributeModifiers() {
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		if (AttributeModifiers != null) {
			for (Map.Entry<Attribute, AttributeModifier> entry : AttributeModifiers.entries()) {
				if (!entry.getKey().equals(Attributes.ATTACK_DAMAGE)) {
					builder.put(entry.getKey(), entry.getValue());
				}
			}
		}
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool Modifier",
				MELEE_DAMAGE * (1 + WARPED_MELEE_DAMAGE_BONUS_RATIO) - 1, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(MOVEMENT_SPEED_UUID, "Movement Speed",
				WARPED_MOVEMENT_SPEED_BONUS_RATIO, AttributeModifier.Operation.MULTIPLY_BASE));
		builder.put(ModAttributes.DAMAGE_RESISTANCE.get(), new AttributeModifier(DAMAGE_RESISTANCE_UUID, "Damage Resistance",
				WARPED_DAMAGE_RESISTANCE, AttributeModifier.Operation.ADDITION));
		WarpedModeAttributeModifiers = builder.build();
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {
		if (pPlayer.isCrouching()) {
			ItemStack itemstack = pPlayer.getItemInHand(pHand);
			if (getPerkLevel(pPlayer, itemstack) == WARPED_MODE_PERK_LEVEL)
				setPerkLevel(itemstack, CRIMSON_MODE_PERK_LEVEL);
			else
				setPerkLevel(itemstack, WARPED_MODE_PERK_LEVEL);
			pPlayer.getCooldowns().addCooldown(itemstack.getItem(), MODE_SWITCH_COOLDOWN);
			return InteractionResultHolder.success(itemstack);
		}
		return super.use(pLevel, pPlayer, pHand);
	}

	@Override
	public boolean hurtEnemy(@NotNull ItemStack pStack, @NotNull LivingEntity pTarget, @NotNull LivingEntity pAttacker) {
		if (this.getPerkLevel(pAttacker, pStack) == CRIMSON_MODE_PERK_LEVEL) {
			pTarget.addEffect(new MobEffectInstance(
					MobEffects.WEAKNESS,
					CRIMSON_WEAKNESS_DURATION_ON_TARGET_WHEN_HIT,
					CRIMSON_WEAKNESS_LEVEL_ON_TARGET_WHEN_HIT - 1)
			);

			pAttacker.addEffect(new MobEffectInstance(
					MobEffects.REGENERATION,
					CRIMSON_REGENERATION_DURATION_ON_SELF_WHEN_HIT,
					CRIMSON_REGENERATION_LEVEL_ON_SELF_WHEN_HIT - 1)
			);
		}
		return super.hurtEnemy(pStack, pTarget, pAttacker);
	}

	@Override
	public ThrownSpear createThrownSpear(Level pLevel, Player player, ItemStack pStack) {
		ThrownNetherGuide spear = new ThrownNetherGuide(ModEntities.THROWN_NETHER_GUIDE.get(), pLevel, player, pStack);
		if (getPerkLevel(player, pStack) == CRIMSON_MODE_PERK_LEVEL)
			spear.setMode((byte) 1);
		return spear;
	}

	@Override
	protected float getProjectileBaseDamage(ItemStack stack) {
		return getPerkLevel(null, stack) == WARPED_MODE_PERK_LEVEL ?
				super.getProjectileBaseDamage(stack) * (1 + WARPED_THROW_DAMAGE_BONUS_RATIO) :
				super.getProjectileBaseDamage(stack);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		return slot == EquipmentSlot.MAINHAND && getPerkLevel(null, stack) == WARPED_MODE_PERK_LEVEL ?
				WarpedModeAttributeModifiers : super.getAttributeModifiers(slot, stack);
	}

	@Override
	public byte getMaxPerkLevel() {
		return 1;
	}

	@Override
	public void updateAttributesFromConfig(SpearProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof NetherGuideProperties nProperties) {
			WARPED_MELEE_DAMAGE_BONUS_RATIO = nProperties.WARPED_MELEE_DAMAGE_BONUS_RATIO.get().floatValue();
			WARPED_THROW_DAMAGE_BONUS_RATIO = nProperties.WARPED_THROW_DAMAGE_BONUS_RATIO.get().floatValue();
			WARPED_DAMAGE_RESISTANCE = nProperties.WARPED_DAMAGE_RESISTANCE.get().floatValue();
			WARPED_MOVEMENT_SPEED_BONUS_RATIO = nProperties.WARPED_MOVEMENT_SPEED_BONUS_RATIO.get().floatValue();

			CRIMSON_WEAKNESS_LEVEL_ON_TARGET_WHEN_HIT = nProperties.CRIMSON_WEAKNESS_LEVEL_ON_TARGET_WHEN_HIT.get();
			CRIMSON_WEAKNESS_DURATION_ON_TARGET_WHEN_HIT = nProperties.CRIMSON_WEAKNESS_DURATION_ON_TARGET_WHEN_HIT.get();
			CRIMSON_REGENERATION_LEVEL_ON_SELF_WHEN_HIT = nProperties.CRIMSON_REGENERATION_LEVEL_ON_SELF_WHEN_HIT.get();
			CRIMSON_REGENERATION_DURATION_ON_SELF_WHEN_HIT = nProperties.CRIMSON_REGENERATION_DURATION_ON_SELF_WHEN_HIT.get();

			MODE_SWITCH_COOLDOWN = nProperties.MODE_SWITCH_COOLDOWN.get();
		}
	}

	@Override
	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		var list = super.getPerkDescriptions(stack, level);

		if (!list.isEmpty())
			list.add(Component.empty());

		list.add(TooltipHelper.getCrouchUseAbilityHint());
		int mode = (int) this.getPerkLevel(null, stack);
		MutableComponent modeSwitchTarget = mode == WARPED_MODE_PERK_LEVEL ?
				Component.translatable("tooltip.perk_weapons.nether_guide_crimson_mode") :
				Component.translatable("tooltip.perk_weapons.nether_guide_warped_mode");

		list.add(Component.translatable("tooltip.perk_weapons.nether_guide_ability_1",
				TooltipHelper.setEmbeddedElementStyle(modeSwitchTarget)).withStyle(ChatFormatting.WHITE));
		if (mode == WARPED_MODE_PERK_LEVEL) {
			list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.nether_guide_crimson_mode_perk_1")));
			list.add(TooltipHelper.setSubPerkStyle(Component.translatable("tooltip.perk_weapons.effect_format",
					MobEffects.WEAKNESS.getDisplayName(),
					TooltipHelper.getRomanNumeral(CRIMSON_WEAKNESS_LEVEL_ON_TARGET_WHEN_HIT),
					TooltipHelper.convertTicksToSeconds(CRIMSON_WEAKNESS_DURATION_ON_TARGET_WHEN_HIT))));
			list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.nether_guide_crimson_mode_perk_2")));
			list.add(TooltipHelper.setSubPerkStyle(Component.translatable("tooltip.perk_weapons.effect_format",
					MobEffects.REGENERATION.getDisplayName(),
					TooltipHelper.getRomanNumeral(CRIMSON_REGENERATION_LEVEL_ON_SELF_WHEN_HIT),
					TooltipHelper.convertTicksToSeconds(CRIMSON_REGENERATION_DURATION_ON_SELF_WHEN_HIT))));
		} else {
			list.add(TooltipHelper.getRatioModifierWithStyle("tooltip.perk_weapons.nether_guide_warped_mode_perk_1",
					WARPED_MOVEMENT_SPEED_BONUS_RATIO));
			list.add(TooltipHelper.getRatioModifierWithStyle("tooltip.perk_weapons.nether_guide_warped_mode_perk_2",
					WARPED_MELEE_DAMAGE_BONUS_RATIO));
			list.add(TooltipHelper.getRatioModifierWithStyle("tooltip.perk_weapons.nether_guide_warped_mode_perk_3",
					WARPED_THROW_DAMAGE_BONUS_RATIO));
			list.add(TooltipHelper.getRatioModifierWithStyle("tooltip.perk_weapons.nether_guide_warped_mode_perk_4",
					WARPED_DAMAGE_RESISTANCE));
		}

		return list;
	}

	@Override
	public Component getWeaponDescription(ItemStack stack, Level level) {
		return TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.nether_guide"));
	}
}
