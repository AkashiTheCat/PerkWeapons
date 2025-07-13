package net.akashi.perk_weapons.Spears;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.akashi.perk_weapons.Client.ClientHelper;
import net.akashi.perk_weapons.Config.Properties.Spear.ScourgeProperties;
import net.akashi.perk_weapons.Config.Properties.Spear.SpearProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownScourge;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownSpear;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;

import static net.minecraft.world.item.enchantment.Enchantments.FIRE_ASPECT;

public class ScourgeItem extends BaseSpearItem {
	public static final String TAG_LAST_USED = "lastUsed";
	public static final String TAG_SHOTS_REMAIN = "shotsRemain";
	public static final String TAG_BUFFED = "buffed";

	protected Multimap<Attribute, AttributeModifier> AttributeModifiersBuffed;

	private static int ABILITY_COOLDOWN = 600;
	public static int WITHER_DURATION = 40;
	public static int WITHER_LEVEL = 3;
	public static int SLOWNESS_DURATION = 40;
	public static int SLOWNESS_LEVEL = 2;
	public static int ABILITY_BUFF_DURATION = 260;
	public static float ABILITY_ATTACK_SPEED_BONUS = 0.3F;
	public static int ABILITY_SHOTS_INTERVAL = 10;
	public static int ABILITY_SHOTS_COUNT = 3;
	public static int PIERCE_LEVEL = 127;

	public ScourgeItem(Properties pProperties) {
		super(pProperties);
		AddGeneralEnchant(FIRE_ASPECT);

		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerScourgePropertyOverrides(this);
	}

	public ScourgeItem(float attackDamage, float attackSpeed, float throwDamage, float projectileVelocity,
	                   int maxChargeTicks, boolean isAdvanced, Properties pProperties) {
		super(attackDamage, attackSpeed, throwDamage, projectileVelocity, maxChargeTicks, isAdvanced, pProperties);
		AddGeneralEnchant(FIRE_ASPECT);

		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerScourgePropertyOverrides(this);
	}

	@Override
	public boolean hurtEnemy(@NotNull ItemStack pStack, LivingEntity pTarget, @NotNull LivingEntity pAttacker) {
		pTarget.addEffect(new MobEffectInstance(MobEffects.WITHER, WITHER_DURATION, WITHER_LEVEL - 1));
		pTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, SLOWNESS_DURATION, SLOWNESS_LEVEL - 1));
		return super.hurtEnemy(pStack, pTarget, pAttacker);
	}

	@Override
	protected void buildAttributeModifiers() {
		super.buildAttributeModifiers();
		float speedMultiplier = 1.0F + ABILITY_ATTACK_SPEED_BONUS;
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier",
				MELEE_DAMAGE - 1, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier",
				MELEE_SPEED * speedMultiplier - 4, AttributeModifier.Operation.ADDITION));
		AttributeModifiersBuffed = builder.build();
	}

	@Override
	public void updateAttributesFromConfig(SpearProperties properties) {
		if (properties instanceof ScourgeProperties sProperties) {
			WITHER_DURATION = sProperties.HIT_WITHER_DURATION.get();
			WITHER_LEVEL = sProperties.HIT_WITHER_LEVEL.get();
			SLOWNESS_DURATION = sProperties.HIT_SLOWNESS_DURATION.get();
			SLOWNESS_LEVEL = sProperties.HIT_SLOWNESS_LEVEL.get();
			ABILITY_BUFF_DURATION = sProperties.ABILITY_BUFF_DURATION.get();
			ABILITY_ATTACK_SPEED_BONUS = sProperties.ABILITY_ATTACK_SPEED_BONUS.get().floatValue();
			ABILITY_COOLDOWN = sProperties.ABILITY_COOLDOWN.get();
			ABILITY_SHOTS_INTERVAL = sProperties.ABILITY_SHOTS_INTERVAL.get();
			ABILITY_SHOTS_COUNT = sProperties.ABILITY_SHOTS_COUNT.get();
			PIERCE_LEVEL = sProperties.PIERCE_LEVEL.get();
		}
		super.updateAttributesFromConfig(properties);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		return slot == EquipmentSlot.MAINHAND && isBuffed(stack) ? AttributeModifiersBuffed :
				super.getAttributeModifiers(slot, stack);
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {
		Level level = pPlayer.level();
		if (pPlayer.isCrouching() && !level.isClientSide()) {
			ItemStack stack = pPlayer.getItemInHand(pHand);

			setBuffed(stack, true);

			shootAbilitySpears(level, pPlayer, stack);
			setAbilityShotsRemain(stack, ABILITY_SHOTS_COUNT - 1);
			setLastAbilityUsedTime(stack, level.getGameTime());

			pPlayer.getCooldowns().addCooldown(this, ABILITY_COOLDOWN);
			pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
					SoundEvents.WITHER_AMBIENT, pPlayer.getSoundSource(),
					1.0F, 1.0F);
		}
		return super.use(pLevel, pPlayer, pHand);
	}

	@Override
	public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity,
	                          int slotId, boolean isSelected) {
		super.inventoryTick(stack, level, entity, slotId, isSelected);
		if (!level.isClientSide() && entity.getType() == EntityType.PLAYER) {
			long tickPassed = level.getGameTime() - getLastAbilityUsedTime(stack);
			int shotsRemain = getAbilityShotsRemain(stack);
			if (shotsRemain > 0 && tickPassed % ABILITY_SHOTS_INTERVAL == 0) {
				shootAbilitySpears(level, (Player) entity, stack);
				setAbilityShotsRemain(stack, shotsRemain - 1);
			}
			if (isBuffed(stack) && tickPassed > ABILITY_BUFF_DURATION) {
				setBuffed(stack, false);
			}
		}
	}

	@Override
	public ThrownSpear createThrownSpear(Level pLevel, Player player, ItemStack pStack) {
		ThrownSpear spear = new ThrownScourge(ModEntities.THROWN_SCOURGE.get(), pLevel, player, pStack);
		spear.setPierceLevel((byte) PIERCE_LEVEL);
		return spear;
	}

	public void shootAbilitySpears(Level level, Player player, ItemStack stack) {
		for (int angle = -10; angle <= 10; angle += 10) {
			ThrownSpear thrownspear = this.createThrownSpear(level, player, stack);
			if (thrownspear instanceof ThrownScourge scourge) {
				Vec3 vec31 = player.getUpVector(1.0F);
				Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(angle * ((float) Math.PI / 180F),
						vec31.x, vec31.y, vec31.z);
				Vec3 vec3 = player.getViewVector(1.0F);
				Vector3f vector3f = vec3.toVector3f().rotate(quaternionf);
				scourge.shoot(vector3f.x(), vector3f.y(), vector3f.z(), VELOCITY, 1.0F);
				scourge.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
				scourge.setIsAbilityShot(true);
				level.addFreshEntity(scourge);
				level.playSound(null, scourge, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS,
						1.0F, 1.0F);
			}
		}
	}

	public void setLastAbilityUsedTime(ItemStack stack, Long time) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putLong(TAG_LAST_USED, time);
	}

	public long getLastAbilityUsedTime(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains(TAG_LAST_USED)) {
			return tag.getLong(TAG_LAST_USED);
		}
		return 0;
	}

	public void setAbilityShotsRemain(ItemStack stack, int amount) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putInt(TAG_SHOTS_REMAIN, amount);
	}

	public int getAbilityShotsRemain(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains(TAG_SHOTS_REMAIN)) {
			return tag.getInt(TAG_SHOTS_REMAIN);
		}
		return 0;
	}

	public void setBuffed(ItemStack stack, boolean buffed) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putBoolean(TAG_BUFFED, buffed);
	}

	public boolean isBuffed(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		return tag.contains(TAG_BUFFED) && tag.getBoolean(TAG_BUFFED);
	}

	@Override
	public Component getWeaponDescription(ItemStack stack, Level level) {
		return TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.scourge"));
	}

	@Override
	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		List<Component> list = super.getPerkDescriptions(stack, level);

		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.scourge_perk_1",
				TooltipHelper.convertToEmbeddedElement(PIERCE_LEVEL))));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.scourge_perk_2")));
		list.add(TooltipHelper.setSubPerkStyle(Component.translatable("tooltip.perk_weapons.effect_format",
				MobEffects.WITHER.getDisplayName(),
				TooltipHelper.getRomanNumeral(WITHER_LEVEL),
				TooltipHelper.convertTicksToSeconds(WITHER_DURATION))));
		list.add(TooltipHelper.setSubPerkStyle(Component.translatable("tooltip.perk_weapons.effect_format",
				MobEffects.MOVEMENT_SLOWDOWN.getDisplayName(),
				TooltipHelper.getRomanNumeral(SLOWNESS_LEVEL),
				TooltipHelper.convertTicksToSeconds(SLOWNESS_DURATION))));

		list.add(Component.empty());
		list.add(TooltipHelper.getCrouchUseAbilityHint());
		list.add(TooltipHelper.getCoolDownTip(ABILITY_COOLDOWN));

		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.scourge_ability_1",
				TooltipHelper.convertToEmbeddedElement(ABILITY_SHOTS_COUNT * 3))));
		list.add(TooltipHelper.getAttackSpeedModifier(ABILITY_ATTACK_SPEED_BONUS)
				.append(Component.translatable("tooltip.perk_weapons.seconds_append",
						TooltipHelper.convertTicksToSeconds(ABILITY_BUFF_DURATION))));

		return list;
	}
}
