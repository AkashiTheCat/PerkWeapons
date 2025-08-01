package net.akashi.perk_weapons.Spears;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.akashi.perk_weapons.Client.ClientHelper;
import net.akashi.perk_weapons.Config.ModCommonConfigs;
import net.akashi.perk_weapons.Config.Properties.Spear.SpearProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownSpear;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Registry.ModTags;
import net.akashi.perk_weapons.Util.EnchantmentValidator;
import net.akashi.perk_weapons.Util.IDoubleLineCrosshairItem;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.ChatFormatting;
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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static net.minecraft.world.item.enchantment.Enchantments.*;

public class BaseSpearItem extends TridentItem implements Vanishable, IDoubleLineCrosshairItem {
	protected Multimap<Attribute, AttributeModifier> AttributeModifiers;
	protected float VELOCITY = 2.5F;
	protected float MELEE_DAMAGE = 5F;
	protected float MELEE_SPEED = 1.1F;
	protected float THROW_DAMAGE = 5F;
	protected int MAX_CHARGE_TICKS = 10;

	private final Set<Enchantment> GeneralEnchants = new HashSet<>(Set.of(
			POWER_ARROWS,
			KNOCKBACK,
			MOB_LOOTING,
			LOYALTY,
			MENDING,
			UNBREAKING
	));
	private final Set<Enchantment> ConflictEnchants = new HashSet<>(Set.of(
			SMITE,
			BANE_OF_ARTHROPODS,
			SHARPNESS
	));

	public BaseSpearItem(Properties pProperties) {
		super(pProperties);
		buildAttributeModifiers();
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerSpearPropertyOverrides(this);
	}

	public BaseSpearItem(float attackDamage, float attackSpeed, float throwDamage, float projectileVelocity,
	                     int maxChargeTicks, boolean isAdvanced, Properties pProperties) {
		super(pProperties);
		this.VELOCITY = projectileVelocity;
		this.THROW_DAMAGE = throwDamage;
		this.MELEE_DAMAGE = attackDamage;
		this.MELEE_SPEED = attackSpeed;
		this.MAX_CHARGE_TICKS = maxChargeTicks;
		if (isAdvanced) {
			GeneralEnchants.addAll(Arrays.asList(RIPTIDE, CHANNELING));
			ConflictEnchants.add(IMPALING);
		}
		buildAttributeModifiers();
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerSpearPropertyOverrides(this);
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
	public void releaseUsing(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pEntityLiving, int pTimeLeft) {
		if (pEntityLiving instanceof Player player) {
			int usedTicks = this.getUseDuration(pStack) - pTimeLeft;
			if (usedTicks >= getMaxChargeTicks(player, pStack)) {
				int riptideLevel = EnchantmentHelper.getRiptide(pStack);
				if (riptideLevel <= 0 || player.isInWaterOrRain()) {
					ThrownSpear thrownspear = createThrownSpear(pLevel, player, pStack);
					double multiplier = 1 + ModCommonConfigs.SPEAR_POWER_ENCHANT_BUFF_PERCENTAGE.get() *
							pStack.getEnchantmentLevel(POWER_ARROWS);
					thrownspear.setBaseDamage(getProjectileBaseDamage(pStack) * multiplier);

					int slot = player.getMainHandItem().getItem() instanceof BaseSpearItem ?
							player.getInventory().selected : Inventory.SLOT_OFFHAND;

					thrownspear.setReturnSlot(slot);

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
							player.move(MoverType.SELF, new Vec3(0.0D, 1.1999999F, 0.0D));
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
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer,
	                                                       @NotNull InteractionHand pHand) {
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

	@Override
	public float getChokeProgress(LivingEntity shooter, ItemStack stack) {
		return Math.min((float) shooter.getTicksUsingItem() / getMaxChargeTicks(shooter, stack), 1);
	}

	//Enchantments

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return EnchantmentValidator.canApplyAtTable(enchantment, GeneralEnchants, ConflictEnchants);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return EnchantmentValidator.canBookEnchant(stack, book, ConflictEnchants);
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

	protected void buildAttributeModifiers() {
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier",
				MELEE_DAMAGE - 1, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier",
				MELEE_SPEED - 4, AttributeModifier.Operation.ADDITION));
		this.AttributeModifiers = builder.build();
	}

	public void updateAttributesFromConfig(SpearProperties properties) {
		this.VELOCITY = properties.VELOCITY.get().floatValue();
		this.MELEE_DAMAGE = properties.MELEE_DAMAGE.get().floatValue();
		this.MELEE_SPEED = properties.ATTACK_SPEED.get().floatValue();
		this.THROW_DAMAGE = properties.RANGED_DAMAGE.get().floatValue();
		this.MAX_CHARGE_TICKS = properties.MAX_CHARGE_TICKS.get();
		buildAttributeModifiers();
	}

	public ThrownSpear createThrownSpear(Level pLevel, Player player, ItemStack pStack) {
		return new ThrownSpear(ModEntities.THROWN_SPEAR.get(), pLevel, player, pStack);
	}

	protected float getProjectileBaseDamage(ItemStack stack) {
		return THROW_DAMAGE;
	}

	protected int getMaxChargeTicks(LivingEntity owner, ItemStack stack) {
		return MAX_CHARGE_TICKS;
	}

	//Tooltips

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
	                            @NotNull List<Component> tooltip,
	                            @NotNull TooltipFlag isAdvanced) {
		if (level == null || !level.isClientSide()) {
			super.appendHoverText(stack, level, tooltip, isAdvanced);
			return;
		}

		TooltipHelper.addWeaponDescription(tooltip, getWeaponDescription(stack, level));
		TooltipHelper.addPerkDescription(tooltip, getPerkDescriptions(stack, level));

		int sharpnessLevel = stack.getEnchantmentLevel(SHARPNESS);
		double multiplier = 1 + ModCommonConfigs.SPEAR_POWER_ENCHANT_BUFF_PERCENTAGE.get() *
				stack.getEnchantmentLevel(POWER_ARROWS);
		tooltip.add(Component.translatable("tooltip.perk_weapons.attribute_ranged_damage",
						TooltipHelper.convertToEmbeddedElement(getProjectileBaseDamage(stack) * multiplier +
								(sharpnessLevel > 0 ? 0.5 * sharpnessLevel + 0.5 : 0)))
				.withStyle(ChatFormatting.DARK_AQUA));
		tooltip.add(Component.translatable("tooltip.perk_weapons.attribute_velocity",
						TooltipHelper.convertToEmbeddedElement(VELOCITY))
				.withStyle(ChatFormatting.DARK_AQUA));
		tooltip.add(Component.translatable("tooltip.perk_weapons.attribute_spear_charge_time",
						TooltipHelper.convertToEmbeddedElement(TooltipHelper.convertTicksToSeconds(MAX_CHARGE_TICKS)))
				.withStyle(ChatFormatting.DARK_AQUA));

		super.appendHoverText(stack, level, tooltip, isAdvanced);
	}

	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		List<Component> list = new ArrayList<>();
		if (stack.is(ModTags.NO_USING_SLOWDOWN_TAG))
			list.add(TooltipHelper.setEmbeddedElementStyle(Component.translatable("tooltip.perk_weapons.no_using_slowdown_perk")));
		return list;
	}

	public Component getWeaponDescription(ItemStack stack, Level level) {
		return Component.empty();
	}
}
