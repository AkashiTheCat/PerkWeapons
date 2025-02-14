package net.akashi.perk_weapons.Spears;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.akashi.perk_weapons.Config.Properties.Spear.ScourgeProperties;
import net.akashi.perk_weapons.Config.Properties.Spear.SpearProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownScourge;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownSpear;
import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.minecraft.world.item.enchantment.Enchantments.FIRE_ASPECT;

@Mod.EventBusSubscriber(modid = PerkWeapons.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ScourgeItem extends BaseSpearItem {
	public static final String TAG_LAST_USED = "lastUsed";
	public static final String TAG_SHOTS_REMAIN = "shotsRemain";
	public static final String TAG_SPEED_BONUS = "speedBonus";
	private static int ABILITY_COOLDOWN = 600;
	public static int WITHER_DURATION = 40;
	public static int WITHER_LEVEL = 3;
	public static int SLOWNESS_DURATION = 40;
	public static int SLOWNESS_LEVEL = 2;
	public static int ABILITY_BUFF_DURATION = 120;
	public static float ABILITY_ATTACK_SPEED_BONUS = 0.3F;
	public static int ABILITY_SHOTS_INTERVAL = 10;
	public static int ABILITY_SHOTS_COUNT = 3;
	public static int PIERCE_LEVEL = 127;

	public ScourgeItem(boolean isAdvanced, Properties pProperties) {
		super(isAdvanced, pProperties);
		AddGeneralEnchant(FIRE_ASPECT);
	}

	public ScourgeItem(float attackDamage, float attackSpeed, float throwDamage,
	                   float projectileVelocity, boolean isAdvanced, Properties pProperties) {
		super(attackDamage, attackSpeed, throwDamage, projectileVelocity, isAdvanced, pProperties);
		AddGeneralEnchant(FIRE_ASPECT);
	}

	@Override
	public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
		pTarget.addEffect(new MobEffectInstance(MobEffects.WITHER, WITHER_DURATION, WITHER_LEVEL - 1));
		pTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, SLOWNESS_DURATION, SLOWNESS_LEVEL - 1));
		return super.hurtEnemy(pStack, pTarget, pAttacker);
	}

	@Override
	public void updateAttributesFromConfig(SpearProperties properties) {
		super.updateAttributesFromConfig(properties);
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
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		float speedMultiplier = 1.0F + getSpeedBonus(stack);
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", BaseAttackDamage - 1, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", BaseAttackSpeed * speedMultiplier - 4, AttributeModifier.Operation.ADDITION));
		return slot == EquipmentSlot.MAINHAND ? builder.build() : super.getAttributeModifiers(slot, stack);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		Level level = pPlayer.level();
		if (pPlayer.isCrouching() && !level.isClientSide()) {
			ItemStack stack = pPlayer.getItemInHand(pHand);

			shootAbilitySpear(level, pPlayer, stack);
			setAbilityShotsRemain(stack, ABILITY_SHOTS_COUNT - 1);
			setLastAbilityUsedTime(stack, level.getGameTime());
			setSpeedBonus(stack, ABILITY_ATTACK_SPEED_BONUS);

			pPlayer.getCooldowns().addCooldown(this, ABILITY_COOLDOWN);
			pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
					SoundEvents.WITHER_AMBIENT, pPlayer.getSoundSource(),
					1.0F, 1.0F);
		}
		return super.use(pLevel, pPlayer, pHand);
	}

	@Override
	public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
		super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
		if (!level.isClientSide()) {
			long tickPassed = level.getGameTime() - getLastAbilityUsedTime(stack);
			int shotsRemain = getAbilityShotsRemain(stack);
			if (shotsRemain > 0 && tickPassed % ABILITY_SHOTS_INTERVAL == 0) {
				shootAbilitySpear(level, player, stack);
				setAbilityShotsRemain(stack, shotsRemain - 1);
			}
			if (getSpeedBonus(stack) != 0.0F && tickPassed > ABILITY_BUFF_DURATION) {
				setSpeedBonus(stack, 0.0F);
			}
		}
	}

	@Override
	public ThrownSpear createThrownSpear(Level pLevel, Player player, ItemStack pStack) {
		ThrownSpear spear = new ThrownScourge(pLevel, player, pStack, ModEntities.THROWN_SCOURGE.get())
				.setBaseDamage(ThrowDamage);
		spear.setPierceLevel((byte) PIERCE_LEVEL);
		return spear;
	}

	public void shootAbilitySpear(Level level, Player player, ItemStack stack) {
		ThrownSpear thrownspear = this.createThrownSpear(level, player, stack);
		if (thrownspear instanceof ThrownScourge scourge) {
			scourge.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, ProjectileVelocity, 1.0F);
			scourge.allowPickup = false;
			level.addFreshEntity(scourge);
			level.playSound(null, scourge, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
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

	public void setSpeedBonus(ItemStack stack, float bonus) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putFloat(TAG_SPEED_BONUS, bonus);
	}

	public float getSpeedBonus(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains(TAG_SPEED_BONUS)) {
			return tag.getFloat(TAG_SPEED_BONUS);
		}
		return 0;
	}
}
