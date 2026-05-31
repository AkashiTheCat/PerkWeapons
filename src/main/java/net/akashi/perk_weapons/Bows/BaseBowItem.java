package net.akashi.perk_weapons.Bows;

import net.akashi.perk_weapons.Client.ClientHelper;
import net.akashi.perk_weapons.Config.Properties.Bow.BowProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.BaseArrow;
import net.akashi.perk_weapons.Network.ArrowVelocitySyncPayload;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Registry.ModTags;
import net.akashi.perk_weapons.Util.EnchantmentUtil;
import net.akashi.perk_weapons.Util.IDoubleLineCrosshairItem;
import net.akashi.perk_weapons.Util.SoundEventHolder;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

import static net.minecraft.world.item.enchantment.Enchantments.*;

public class BaseBowItem extends BowItem implements IDoubleLineCrosshairItem {
	protected static final SoundEventHolder SHOOTING_SOUND = new SoundEventHolder(SoundEvents.ARROW_SHOOT, 1.0F, 1.2F);
	public static Predicate<ItemStack> SUPPORTED_PROJECTILE = (stack) -> stack.is(Items.ARROW);
	protected ItemAttributeModifiers DefaultAttributeModifiers = ItemAttributeModifiers.EMPTY;
	public float ZOOM_FACTOR = 0.1f;
	protected boolean ONLY_ALLOW_MAINHAND = false;
	protected float VELOCITY = 3.0F;
	protected int DRAW_TIME = 20;
	protected float PROJECTILE_DAMAGE = 10;
	protected float INACCURACY = 1.0f;
	protected float SPEED_MODIFIER = 0.0f;

	private final Set<ResourceKey<Enchantment>> GeneralEnchants = new HashSet<>(Set.of(
			INFINITY,
			FLAME,
			POWER,
			PUNCH,
			MENDING,
			UNBREAKING,
			LOOTING
		));
	private final Set<ResourceKey<Enchantment>> ConflictEnchants = new HashSet<>();

	public BaseBowItem(Properties properties) {
		super(properties);
		initializeBowItem();
	}

	/**
	 * To avoid a bug caused by the vanilla equipment update method, if speedModifier!=0, onlyAllowMainHand will be forced set true
	 **/
	public BaseBowItem(int drawTime, float projectileDamage, float velocity, float inaccuracy, float speedModifier,
	                   float zoomFactor, boolean onlyAllowMainHand, Properties properties) {
		super(properties);
		this.PROJECTILE_DAMAGE = projectileDamage;
		this.VELOCITY = velocity;
		this.DRAW_TIME = drawTime;
		this.ZOOM_FACTOR = zoomFactor;
		this.INACCURACY = inaccuracy;
		this.ONLY_ALLOW_MAINHAND = onlyAllowMainHand;
		this.SPEED_MODIFIER = speedModifier;
		initializeBowItem();
	}

	private void initializeBowItem() {
		if (FMLEnvironment.dist.isClient()) {
			ClientHelper.registerBowPropertyOverrides(this);
		}
		buildAttributeModifiers();
	}

	//General overrides

	@Override
	public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
		return BaseBowItem.SUPPORTED_PROJECTILE;
	}

	@Override
	public boolean shouldCauseReequipAnimation(@NotNull ItemStack oldStack, @NotNull ItemStack newStack, boolean slotChanged) {
		return slotChanged || !newStack.is(oldStack.getItem());
	}

	@Override
	public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers(@NotNull ItemStack stack) {
		return this.DefaultAttributeModifiers;
	}

	@Override
	public void releaseUsing(@NotNull ItemStack pStack, @NotNull Level pLevel,
	                         @NotNull LivingEntity pEntityLiving, int pTimeLeft) {
		if (pEntityLiving instanceof Player player) {
				boolean flag = player.getAbilities().instabuild || EnchantmentUtil.getLevel(pStack, INFINITY) > 0;
			ItemStack itemstack = player.getProjectile(pStack);

			int i = this.getUseDuration(pStack, pEntityLiving) - pTimeLeft;
			i = net.neoforged.neoforge.event.EventHooks.onArrowLoose(pStack, pLevel, player, i, !itemstack.isEmpty() || flag);
			if (i < DRAW_TIME) return;

			if (!itemstack.isEmpty() || flag) {
				if (itemstack.isEmpty()) {
					itemstack = new ItemStack(Items.ARROW);
				}

				boolean flag1 = player.getAbilities().instabuild
						|| (itemstack.getItem() instanceof ArrowItem
						&& ((ArrowItem) itemstack.getItem()).isInfinite(itemstack, pStack, player));
				if (!pLevel.isClientSide) {
					SoundEventHolder shootSound = getShootingSound(pEntityLiving, pStack);
					if (shootSound.soundEvent != null) {
						pLevel.playSound(null, player, shootSound.soundEvent, SoundSource.PLAYERS, shootSound.volume,
								1.0F / (pLevel.getRandom().nextFloat() * 0.4F + shootSound.pitch) + (float) 10 / DRAW_TIME);
					}

					ArrowItem arrowitem = (ArrowItem) (itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);

					AbstractArrow abstractarrow = createArrow(pLevel, arrowitem, pStack, itemstack, player);
					abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, VELOCITY, INACCURACY);

					abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() * getDamageMultiplier(pStack));

					if (EnchantmentUtil.getLevel(pStack, FLAME) > 0) {
						abstractarrow.igniteForTicks(100);
					}

					pStack.hurtAndBreak(1, player,
							player.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
					if (flag1 || player.getAbilities().instabuild
							&& (itemstack.is(Items.SPECTRAL_ARROW)
							|| itemstack.is(Items.TIPPED_ARROW))) {
						abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
					}
					pLevel.addFreshEntity(abstractarrow);

					//Sync velocity to all clients
					PacketDistributor.sendToAllPlayers(new ArrowVelocitySyncPayload(
							abstractarrow.getDeltaMovement().x,
							abstractarrow.getDeltaMovement().y,
							abstractarrow.getDeltaMovement().z,
							abstractarrow.getId()));
				}

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

	@NotNull
	protected SoundEventHolder getShootingSound(LivingEntity shooter, ItemStack stack) {
		return SHOOTING_SOUND;
	}

	@Override
	public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
		return UseAnim.BOW;
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pHand);
		if (ONLY_ALLOW_MAINHAND && pHand != InteractionHand.MAIN_HAND) {
			return InteractionResultHolder.pass(itemstack);
		}

		boolean flag = !pPlayer.getProjectile(itemstack).isEmpty();

		return net.neoforged.neoforge.event.EventHooks.onArrowNock(
				itemstack, pLevel, pPlayer, pHand, flag);

	}

	//Enchantments

	@Override
	public boolean supportsEnchantment(@NotNull ItemStack stack, @NotNull Holder<Enchantment> enchantment) {
		return EnchantmentUtil.supportsEnchantment(stack, enchantment, GeneralEnchants, ConflictEnchants);
	}

	@Override
	public boolean isBookEnchantable(@NotNull ItemStack stack, @NotNull ItemStack book) {
		return EnchantmentUtil.canBookEnchant(stack, book, GeneralEnchants, ConflictEnchants);
	}

	public boolean AddGeneralEnchant(ResourceKey<Enchantment> enchantment) {
		return GeneralEnchants.add(enchantment);
	}

	public boolean AddGeneralEnchant(Holder<Enchantment> enchantment) {
		if (enchantment == null) {
			return false;
		}
		return enchantment.unwrapKey().map(this::AddGeneralEnchant).orElse(false);
	}

	public boolean RemoveGeneralEnchant(ResourceKey<Enchantment> enchantment) {
		return GeneralEnchants.remove(enchantment);
	}

	public boolean RemoveGeneralEnchant(Holder<Enchantment> enchantment) {
		if (enchantment == null) {
			return false;
		}
		return enchantment.unwrapKey().map(this::RemoveGeneralEnchant).orElse(false);
	}

	public boolean AddConflictEnchant(ResourceKey<Enchantment> enchantment) {
		return ConflictEnchants.add(enchantment);
	}

	public boolean AddConflictEnchant(Holder<Enchantment> enchantment) {
		if (enchantment == null) {
			return false;
		}
		return enchantment.unwrapKey().map(this::AddConflictEnchant).orElse(false);
	}

	public boolean RemoveConflictEnchant(ResourceKey<Enchantment> enchantment) {
		return ConflictEnchants.remove(enchantment);
	}

	public boolean RemoveConflictEnchant(Holder<Enchantment> enchantment) {
		if (enchantment == null) {
			return false;
		}
		return enchantment.unwrapKey().map(this::RemoveConflictEnchant).orElse(false);
	}

	//New methods

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

	public double getDamageMultiplier(ItemStack stack) {
		int powerLevel = EnchantmentUtil.getLevel(stack, POWER);
		return 1F + 0.25F * powerLevel;
	}

	public void updateAttributesFromConfig(BowProperties properties) {
		this.DRAW_TIME = properties.DRAW_TIME.get();
		this.PROJECTILE_DAMAGE = properties.DAMAGE.get().floatValue();
		this.VELOCITY = properties.VELOCITY.get().floatValue();
		this.ZOOM_FACTOR = properties.ZOOM_FACTOR.get().floatValue();
		this.INACCURACY = properties.INACCURACY.get().floatValue();
		this.ONLY_ALLOW_MAINHAND = properties.ONLY_MAINHAND.get();
		this.SPEED_MODIFIER = properties.SPEED_MODIFIER.get().floatValue();
		buildAttributeModifiers();
	}

	public void buildAttributeModifiers() {
		if (SPEED_MODIFIER != 0.0F) {
			ItemAttributeModifiers.Builder defaultBuilder = ItemAttributeModifiers.builder();
			AttributeModifier speedModifier = new AttributeModifier(
					ResourceLocation.fromNamespaceAndPath("perk_weapons", "bow_speed_modifier"),
					SPEED_MODIFIER, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
			defaultBuilder.add(Attributes.MOVEMENT_SPEED, speedModifier, EquipmentSlotGroup.MAINHAND);
			this.DefaultAttributeModifiers = defaultBuilder.build();
			this.ONLY_ALLOW_MAINHAND = true;
		} else {
			this.DefaultAttributeModifiers = ItemAttributeModifiers.EMPTY;
		}
	}

	public float getDrawProgress(LivingEntity shooter) {
		return shooter.getTicksUsingItem() < DRAW_TIME ? (float) shooter.getTicksUsingItem() / DRAW_TIME : 1;
	}

	@Override
	public float getChokeProgress(LivingEntity shooter, ItemStack stack) {
		return getDrawProgress(shooter);
	}

	//Tooltips

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull List<Component> tooltip,
	                            @NotNull TooltipFlag isAdvanced) {
		if (ONLY_ALLOW_MAINHAND) {
			tooltip.add(Component.translatable("tooltip.perk_weapons.only_mainhand")
					.withStyle(ChatFormatting.RED));
		}
		TooltipHelper.addWeaponDescription(tooltip, getWeaponDescription(stack, null));
		TooltipHelper.addPerkDescription(tooltip, getPerkDescriptions(stack, null));

		tooltip.add(Component.translatable("tooltip.perk_weapons.attribute_damage",
						TooltipHelper.convertToEmbeddedElement(PROJECTILE_DAMAGE * getDamageMultiplier(stack)))
				.withStyle(ChatFormatting.DARK_AQUA));
		tooltip.add(Component.translatable("tooltip.perk_weapons.attribute_velocity",
						TooltipHelper.convertToEmbeddedElement(VELOCITY))
				.withStyle(ChatFormatting.DARK_AQUA));
		tooltip.add(Component.translatable("tooltip.perk_weapons.attribute_draw_time",
						TooltipHelper.convertToEmbeddedElement(TooltipHelper.convertTicksToSeconds(DRAW_TIME)))
				.withStyle(ChatFormatting.DARK_AQUA));

		super.appendHoverText(stack, context, tooltip, isAdvanced);
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