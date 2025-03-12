package net.akashi.perk_weapons.Spears;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.akashi.perk_weapons.Config.Properties.Spear.SpearProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownSpear;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Util.EnchantmentValidator;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static net.minecraft.world.item.enchantment.Enchantments.*;

public class BaseSpearItem extends TridentItem implements Vanishable {
	public Multimap<Attribute, AttributeModifier> AttributeModifiers;
	public float VELOCITY = 2.5F;
	public float MELEE_DAMAGE = 5F;
	public float MELEE_SPEED = 1.1F;
	public float THROW_DAMAGE = 5F;

	private final List<Enchantment> GeneralEnchants = new ArrayList<>(Arrays.asList(
			KNOCKBACK,
			MOB_LOOTING,
			LOYALTY,
			MENDING
	));
	private final List<Enchantment> ConflictEnchants = new ArrayList<>(Arrays.asList(
			SMITE,
			BANE_OF_ARTHROPODS,
			SHARPNESS
	));

	public BaseSpearItem(Properties pProperties) {
		super(pProperties);
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier",
				5 - 1, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier",
				1.1 - 4, AttributeModifier.Operation.ADDITION));
		this.AttributeModifiers = builder.build();
	}

	public BaseSpearItem(float attackDamage, float attackSpeed, float throwDamage,
	                     float projectileVelocity, boolean isAdvanced, Properties pProperties) {
		super(pProperties);
		this.VELOCITY = projectileVelocity;
		this.THROW_DAMAGE = throwDamage;
		this.MELEE_DAMAGE = attackDamage;
		this.MELEE_SPEED = attackSpeed;
		if (isAdvanced) {
			GeneralEnchants.addAll(Arrays.asList(RIPTIDE, CHANNELING));
			ConflictEnchants.add(IMPALING);
		}
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier",
				attackDamage - 1, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier",
				attackSpeed - 4, AttributeModifier.Operation.ADDITION));
		this.AttributeModifiers = builder.build();
	}

	//General Overrides

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		return slot == EquipmentSlot.MAINHAND ? AttributeModifiers : super.getAttributeModifiers(slot, stack);
	}

	@Override
	public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
		return UseAnim.SPEAR;
	}

	@Override
	public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
		if (pEntityLiving instanceof Player player) {
			int usedTicks = this.getUseDuration(pStack) - pTimeLeft;
			if (usedTicks >= 10) {
				int riptideLevel = EnchantmentHelper.getRiptide(pStack);
				if (riptideLevel <= 0 || player.isInWaterOrRain()) {
					ThrownSpear thrownspear = createThrownSpear(pLevel, player, pStack);
					thrownspear.setBaseDamage(getProjectileBaseDamage(pStack));

					thrownspear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F,
							VELOCITY + (float) riptideLevel * 0.5F, 1.0F);

					if (!pLevel.isClientSide) {
						pStack.hurtAndBreak(1, player, (pOnBroken) -> {
							pOnBroken.broadcastBreakEvent(pEntityLiving.getUsedItemHand());
						});
						if (riptideLevel == 0 && !player.getAbilities().instabuild) {
							player.getInventory().removeItem(pStack);
						}
					}
					if (player.getAbilities().instabuild) {
						thrownspear.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
					}

					pLevel.addFreshEntity(thrownspear);
					pLevel.playSound(null, thrownspear, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS,
							1.0F, 1.0F);
					player.awardStat(Stats.ITEM_USED.get(this));

					if (riptideLevel > 0) {
						float f7 = player.getYRot();
						float f = player.getXRot();
						float f1 = -Mth.sin(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
						float f2 = -Mth.sin(f * ((float) Math.PI / 180F));
						float f3 = Mth.cos(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
						float f4 = Mth.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
						float f5 = 3.0F * ((1.0F + (float) riptideLevel) / 4.0F);
						f1 *= f5 / f4;
						f2 *= f5 / f4;
						f3 *= f5 / f4;
						player.push(f1, f2, f3);
						player.startAutoSpinAttack(20);
						if (player.onGround()) {
							player.move(MoverType.SELF, new Vec3(0.0D, (double) 1.1999999F, 0.0D));
						}

						SoundEvent soundevent;
						if (riptideLevel >= 3) {
							soundevent = SoundEvents.TRIDENT_RIPTIDE_3;
						} else if (riptideLevel == 2) {
							soundevent = SoundEvents.TRIDENT_RIPTIDE_2;
						} else {
							soundevent = SoundEvents.TRIDENT_RIPTIDE_1;
						}

						pLevel.playSound(null, player, soundevent, SoundSource.PLAYERS,
								1.0F, 1.0F);
					}

				}
			}
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
			return InteractionResultHolder.fail(itemstack);
		} else if (EnchantmentHelper.getRiptide(itemstack) > 0 && !pPlayer.isInWaterOrRain()) {
			return InteractionResultHolder.fail(itemstack);
		} else {
			pPlayer.startUsingItem(pHand);
			return InteractionResultHolder.consume(itemstack);
		}
	}

	//Enchantments

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return EnchantmentValidator.canApplyAtTable(stack, enchantment, GeneralEnchants, ConflictEnchants);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return EnchantmentValidator.canBookEnchant(stack, book, GeneralEnchants, ConflictEnchants);
	}

	public boolean AddGeneralEnchant(Enchantment enchantment) {
		return GeneralEnchants.add(enchantment);
	}

	public boolean RemoveGeneralEnchant(Enchantment enchantment) {
		return GeneralEnchants.remove(enchantment);
	}

	public boolean AddConflictEnchant(Enchantment enchantment) {
		return ConflictEnchants.add(enchantment);
	}

	public boolean RemoveConflictEnchant(Enchantment enchantment) {
		return ConflictEnchants.remove(enchantment);
	}

	//New methods

	public void updateAttributesFromConfig(SpearProperties properties) {
		this.VELOCITY = properties.VELOCITY.get().floatValue();
		this.MELEE_DAMAGE = properties.MELEE_DAMAGE.get().floatValue();
		this.MELEE_SPEED = properties.ATTACK_SPEED.get().floatValue();
		this.THROW_DAMAGE = properties.RANGED_DAMAGE.get().floatValue();

		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier",
				this.MELEE_DAMAGE - 1, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier",
				this.MELEE_SPEED - 4, AttributeModifier.Operation.ADDITION));
		AttributeModifiers = builder.build();
	}

	public ThrownSpear createThrownSpear(Level pLevel, Player player, ItemStack pStack) {
		return new ThrownSpear(pLevel, player, pStack, ModEntities.THROWN_SPEAR.get());
	}

	protected float getProjectileBaseDamage(ItemStack stack) {
		return THROW_DAMAGE;
	}

	//Tooltips

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
		if (level == null || !level.isClientSide()) {
			super.appendHoverText(stack, level, tooltip, isAdvanced);
			return;
		}

		TooltipHelper.addWeaponDescription(tooltip, getWeaponDescription(stack, level));
		TooltipHelper.addPerkDescription(tooltip, getPerkDescriptions(stack, level));

		int sharpnessLevel = stack.getEnchantmentLevel(SHARPNESS);
		tooltip.add(Component.translatable("tooltip.perk_weapons.attribute_ranged_damage",
						TooltipHelper.convertToEmbeddedElement(THROW_DAMAGE + sharpnessLevel > 0 ?
								0.5 * sharpnessLevel + 0.5 : 0))
				.withStyle(ChatFormatting.DARK_AQUA));
		tooltip.add(Component.translatable("tooltip.perk_weapons.attribute_velocity",
						TooltipHelper.convertToEmbeddedElement(VELOCITY))
				.withStyle(ChatFormatting.DARK_AQUA));

		super.appendHoverText(stack, level, tooltip, isAdvanced);
	}

	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		return List.of();
	}

	public Component getWeaponDescription(ItemStack stack, Level level) {
		return Component.empty();
	}
}
