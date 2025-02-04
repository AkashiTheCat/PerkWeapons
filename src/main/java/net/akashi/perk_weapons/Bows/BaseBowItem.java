package net.akashi.perk_weapons.Bows;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.akashi.perk_weapons.Client.ClientHelper;
import net.akashi.perk_weapons.Config.Properties.Bow.BowProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.BaseArrow;
import net.akashi.perk_weapons.Network.ArrowVelocitySyncPacket;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Registry.ModPackets;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;
import java.util.function.Predicate;

import static net.minecraft.world.item.enchantment.Enchantments.*;

public class BaseBowItem extends ProjectileWeaponItem implements Vanishable {
	public static final UUID MOVEMENT_SPEED_UUID = UUID.fromString("DB3F25A3-255C-8F4A-B293-EA1BA59D27CE");
	public Multimap<Attribute, AttributeModifier> AttributeModifiers;
	public float VELOCITY = 3.0F;
	public int DRAW_TIME = 20;
	public float PROJECTILE_DAMAGE = 10;
	public float ZOOM_FACTOR = 0.1f;
	public float INACCURACY = 1.0f;

	private final List<Enchantment> GeneralEnchants = new ArrayList<>(Arrays.asList(
			INFINITY_ARROWS,
			FLAMING_ARROWS,
			POWER_ARROWS,
			PUNCH_ARROWS
	));
	private final List<Enchantment> ConflictEnchants = new ArrayList<>();

	public BaseBowItem(Properties properties) {
		super(properties);
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerBowPropertyOverrides(this);
	}

	public BaseBowItem(int drawTime, float projectileDamage, float velocity, float inaccuracy, float speedModifier,
	                   float zoomFactor, Properties properties) {
		super(properties);
		this.VELOCITY = velocity;
		this.DRAW_TIME = drawTime;
		this.PROJECTILE_DAMAGE = projectileDamage;
		this.ZOOM_FACTOR = zoomFactor;
		this.INACCURACY = inaccuracy;
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerBowPropertyOverrides(this);
		if (speedModifier != 0.0F) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(MOVEMENT_SPEED_UUID,
					"Tool modifier", speedModifier, AttributeModifier.Operation.MULTIPLY_TOTAL));
			this.AttributeModifiers = builder.build();
		}
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		if (GeneralEnchants.stream().anyMatch(GEnchantment -> GEnchantment.equals(enchantment))) {
			return true;
		}
		if (ConflictEnchants.stream().anyMatch(CEnchantments -> CEnchantments.equals(enchantment))) {
			return ConflictEnchants.stream().noneMatch(CEnchantment -> stack.getEnchantmentLevel(CEnchantment) > 0);
		}
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		Map<Enchantment, Integer> enchantments = book.getAllEnchantments();
		for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
			Enchantment enchantment = entry.getKey();
			if (GeneralEnchants.stream().anyMatch(GEnchantment -> GEnchantment.equals(enchantment))) {
				continue;
			} else if (ConflictEnchants.stream().anyMatch(CEnchantments -> CEnchantments.equals(enchantment))) {
				if (ConflictEnchants.stream().noneMatch(CEnchantment -> stack.getEnchantmentLevel(CEnchantment) > 0)) {
					continue;
				}
			}
			return false;
		}
		return true;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		return (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) && AttributeModifiers != null ?
				AttributeModifiers : super.getAttributeModifiers(slot, stack);
	}

	@Override
	public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
		if (pEntityLiving instanceof Player player) {
			boolean flag = player.getAbilities().instabuild || pStack.getEnchantmentLevel(INFINITY_ARROWS) > 0;
			ItemStack itemstack = player.getProjectile(pStack);

			int i = this.getUseDuration(pStack) - pTimeLeft;
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(pStack, pLevel, player, i, !itemstack.isEmpty() || flag);
			if (i < DRAW_TIME) return;

			if (!itemstack.isEmpty() || flag) {
				if (itemstack.isEmpty()) {
					itemstack = new ItemStack(Items.ARROW);
				}

				boolean flag1 = player.getAbilities().instabuild
						|| (itemstack.getItem() instanceof ArrowItem
						&& ((ArrowItem) itemstack.getItem()).isInfinite(itemstack, pStack, player));
				if (!pLevel.isClientSide) {
					ArrowItem arrowitem = (ArrowItem) (itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);

					AbstractArrow abstractarrow = createArrow(pLevel, arrowitem, pStack, itemstack, player);
					abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, VELOCITY, INACCURACY);

					//Handle Enchantments
					//Power
					int powerLevel = pStack.getEnchantmentLevel(POWER_ARROWS);
					if (powerLevel > 0) {
						abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + (double) powerLevel * 0.5D + 0.5D);
					}
					//Punch
					int punchLevel = pStack.getEnchantmentLevel(PUNCH_ARROWS);
					if (punchLevel > 0) {
						abstractarrow.setKnockback(punchLevel);
					}
					//Flame
					if (pStack.getEnchantmentLevel(FLAMING_ARROWS) > 0) {
						abstractarrow.setSecondsOnFire(100);
					}

					pStack.hurtAndBreak(1, player, (player1) -> {
						player1.broadcastBreakEvent(player.getUsedItemHand());
					});
					if (flag1 || player.getAbilities().instabuild
							&& (itemstack.is(Items.SPECTRAL_ARROW)
							|| itemstack.is(Items.TIPPED_ARROW))) {
						abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
					}
					pLevel.addFreshEntity(abstractarrow);

					//Sync velocity to all clients
					ModPackets.NETWORK.send(PacketDistributor.ALL.noArg(),
							new ArrowVelocitySyncPacket(abstractarrow.getDeltaMovement(), abstractarrow.getId()));
				}

				pLevel.playSound((Player) null, player.getX(), player.getY(), player.getZ(),
						SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F,
						1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + (float) 10 / DRAW_TIME);
				if (!flag1 && !player.getAbilities().instabuild) {
					itemstack.shrink(1);
					if (itemstack.isEmpty()) {
						player.getInventory().removeItem(itemstack);
					}
				}

				player.awardStat(Stats.ITEM_USED.get(this));
			}
		}
	}

	@Override
	public int getUseDuration(ItemStack pStack) {
		return 72000;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack pStack) {
		return UseAnim.BOW;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		boolean flag = !pPlayer.getProjectile(itemstack).isEmpty();

		InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, pLevel, pPlayer, pHand, flag);
		if (ret != null) return ret;

		if (!pPlayer.getAbilities().instabuild && !flag) {
			return InteractionResultHolder.fail(itemstack);
		} else {
			pPlayer.startUsingItem(pHand);
			return InteractionResultHolder.consume(itemstack);
		}
	}

	@Override
	public Predicate<ItemStack> getAllSupportedProjectiles() {
		return ARROW_ONLY;
	}

	@Override
	public int getDefaultProjectileRange() {
		return 15;
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

	public AbstractArrow createArrow(Level level, ArrowItem arrowItem, ItemStack bowStack, ItemStack arrowStack, Player player) {
		BaseArrow arrow = new BaseArrow(ModEntities.BASE_ARROW.get(), level, player);
		if (arrowItem instanceof SpectralArrowItem) {
			arrow.setSpectralArrow(true);
		} else {
			arrow.setEffectsFromItem(arrowStack);
		}
		arrow.setBaseDamage(PROJECTILE_DAMAGE / VELOCITY);
		return arrow;
	}

	public void updateAttributesFromConfig(BowProperties properties) {
		this.DRAW_TIME = properties.DRAW_TIME.get();
		this.PROJECTILE_DAMAGE = properties.DAMAGE.get().floatValue();
		this.VELOCITY = properties.VELOCITY.get().floatValue();
		this.ZOOM_FACTOR = properties.ZOOM_FACTOR.get().floatValue();
		this.INACCURACY = properties.INACCURACY.get().floatValue();
		float speedModifier = properties.SPEED_MODIFIER.get().floatValue();
		if (speedModifier != 0.0F) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(MOVEMENT_SPEED_UUID,
					"Tool modifier", speedModifier, AttributeModifier.Operation.MULTIPLY_TOTAL));
			this.AttributeModifiers = builder.build();
		}

	}

	public float getDrawProgress(LivingEntity shooter) {
		return shooter.getTicksUsingItem() < DRAW_TIME ? (float) shooter.getTicksUsingItem() / DRAW_TIME : 1;
	}

}
