package net.akashi.weaponmod.Spears;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.akashi.weaponmod.Config.Properties.SpearProperties;
import net.akashi.weaponmod.Entities.Projectiles.ThrownSpear;
import net.akashi.weaponmod.Registry.ModEntities;
import net.minecraft.core.BlockPos;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.world.item.enchantment.Enchantments.*;

public class SpearItem extends Item implements Vanishable {
	private Multimap<Attribute, AttributeModifier> defaultModifiers;
	public float ProjectileVelocity;
	public float ThrowDamage;
	private final List<Enchantment> GeneralEnchants = new ArrayList<>(Arrays.asList(
			KNOCKBACK,
			MOB_LOOTING,
			LOYALTY
	));
	private final List<Enchantment> ConflictEnchants = new ArrayList<>(Arrays.asList(
			SMITE,
			BANE_OF_ARTHROPODS,
			SHARPNESS
	));

	public SpearItem(boolean isAdvanced, Properties pProperties) {
		super(pProperties);
		this.ProjectileVelocity = 2.5F;
		this.ThrowDamage = 5;
		if (isAdvanced) {
			GeneralEnchants.addAll(Arrays.asList(RIPTIDE, CHANNELING));
			ConflictEnchants.add(IMPALING);
		}
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 5 - 1, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", 1.1 - 4, AttributeModifier.Operation.ADDITION));
		this.defaultModifiers = builder.build();
	}

	public SpearItem(float attackDamage,
	                 float attackSpeed,
	                 float throwDamage,
	                 float projectileVelocity,
	                 boolean isAdvanced,
	                 Properties pProperties) {
		super(pProperties);
		this.ProjectileVelocity = projectileVelocity;
		this.ThrowDamage = throwDamage;
		if (isAdvanced) {
			GeneralEnchants.addAll(Arrays.asList(RIPTIDE, CHANNELING));
			ConflictEnchants.add(IMPALING);
		}
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", attackDamage - 1, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", attackSpeed - 4, AttributeModifier.Operation.ADDITION));
		this.defaultModifiers = builder.build();
	}

	public void updateFromConfig(float attackDamage, float attackSpeed,
	                             float throwDamage, float projectileVelocity) {
		this.ProjectileVelocity = projectileVelocity;
		this.ThrowDamage = throwDamage;
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", attackDamage - 1, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", attackSpeed - 4, AttributeModifier.Operation.ADDITION));
		this.defaultModifiers = builder.build();
	}

	public void updateFromConfig(SpearProperties properties) {
		this.ProjectileVelocity = properties.VELOCITY.get().floatValue();
		this.ThrowDamage = properties.RANGED_DAMAGE.get().floatValue();
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", properties.MELEE_DAMAGE.get().floatValue() - 1, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", properties.ATTACK_SPEED.get().floatValue() - 4, AttributeModifier.Operation.ADDITION));
		this.defaultModifiers = builder.build();
	}

	@Override
	public boolean canAttackBlock(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
		return !pPlayer.isCreative();
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

	@Override
	public UseAnim getUseAnimation(ItemStack pStack) {
		return UseAnim.SPEAR;
	}

	@Override
	public int getUseDuration(ItemStack pStack) {
		return 72000;
	}

	@Override
	public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
		if (pEntityLiving instanceof Player player) {
			int i = this.getUseDuration(pStack) - pTimeLeft;
			if (i >= 10) {
				int j = EnchantmentHelper.getRiptide(pStack);
				if (j <= 0 || player.isInWaterOrRain()) {
					if (!pLevel.isClientSide) {
						pStack.hurtAndBreak(1, player, (pOnBroken) -> {
							pOnBroken.broadcastBreakEvent(pEntityLiving.getUsedItemHand());
						});
						if (j == 0) {

							if (!player.getAbilities().instabuild) {
								player.getInventory().removeItem(pStack);
							}
						}
					}
					ThrownSpear thrownspear = createThrownSpear(pLevel, player, pStack)
							.setBaseDamage(this.ThrowDamage);
					thrownspear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, ProjectileVelocity + (float) j * 0.5F, 1.0F);
					if (player.getAbilities().instabuild) {
						thrownspear.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
					}

					pLevel.addFreshEntity(thrownspear);
					pLevel.playSound((Player) null, thrownspear, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
					player.awardStat(Stats.ITEM_USED.get(this));
					if (j > 0) {
						float f7 = player.getYRot();
						float f = player.getXRot();
						float f1 = -Mth.sin(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
						float f2 = -Mth.sin(f * ((float) Math.PI / 180F));
						float f3 = Mth.cos(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
						float f4 = Mth.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
						float f5 = 3.0F * ((1.0F + (float) j) / 4.0F);
						f1 *= f5 / f4;
						f2 *= f5 / f4;
						f3 *= f5 / f4;
						player.push((double) f1, (double) f2, (double) f3);
						player.startAutoSpinAttack(20);
						if (player.onGround()) {
							float f6 = 1.1999999F;
							player.move(MoverType.SELF, new Vec3(0.0D, (double) 1.1999999F, 0.0D));
						}

						SoundEvent soundevent;
						if (j >= 3) {
							soundevent = SoundEvents.TRIDENT_RIPTIDE_3;
						} else if (j == 2) {
							soundevent = SoundEvents.TRIDENT_RIPTIDE_2;
						} else {
							soundevent = SoundEvents.TRIDENT_RIPTIDE_1;
						}

						pLevel.playSound((Player) null, player, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
					}

				}
			}
		}
	}

	public ThrownSpear createThrownSpear(Level pLevel, Player player, ItemStack pStack) {
		return new ThrownSpear(pLevel, player, pStack, getItemSlotIndex(player, pStack), ModEntities.THROWN_SPEAR.get());
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

	@Override
	public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
		pStack.hurtAndBreak(1, pAttacker, (Player) -> {
			Player.broadcastBreakEvent(EquipmentSlot.MAINHAND);
		});
		return true;
	}

	@Override
	public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
		if ((double) pState.getDestroySpeed(pLevel, pPos) != 0.0D) {
			pStack.hurtAndBreak(2, pEntityLiving, (Player) -> {
				Player.broadcastBreakEvent(EquipmentSlot.MAINHAND);
			});
		}

		return true;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		if (GeneralEnchants.stream().anyMatch(CEnchantment -> CEnchantment.equals(enchantment))) {
			return true;
		}
		return ConflictEnchants.stream().noneMatch(CEnchantment -> stack.getEnchantmentLevel(CEnchantment) > 0);
	}

	@Override
	public int getEnchantmentValue() {
		return 1;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
		return pEquipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
	}

	int getItemSlotIndex(Player player, ItemStack stack) {
		for (int i = 0; i < player.getInventory().items.size(); i++) {
			ItemStack itemInSlot = player.getInventory().getItem(i);
			if (itemInSlot.is(stack.getItem()) && itemInSlot.getCount() == stack.getCount() &&
					itemInSlot.is(stack.getItem())) {
				return i;
			}
		}
		return 0;
	}
}
