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
	private static int ABILITY_COOLDOWN = 600;
	public static int WITHER_DURATION = 40;
	public static int WITHER_LEVEL = 3;
	public static int SLOWNESS_DURATION = 40;
	public static int SLOWNESS_LEVEL = 2;
	public static int ABILITY_BUFF_DURATION = 120;
	public static float ABILITY_ATTACK_SPEED_BONUS = 0.3F;
	public static int ABILITY_SHOTS_INTERVAL = 10;
	public static int ABILITY_SHOTS_COUNT = 3;
	public static int PIERCE_LEVEL = 255;

	private static final Map<UUID, Integer> ABILITY_SHOT_MAP = new HashMap<>();
	private static final Map<UUID, Integer> ABILITY_BUFF_TIME_MAP = new HashMap<>();

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
		CompoundTag tag = stack.getTag();
		float speedMultiplier = 1.0F;
		if (tag != null) {
			speedMultiplier = tag.getFloat("speedBonus") + 1;
		}
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", BaseAttackDamage - 1, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", BaseAttackSpeed * speedMultiplier - 4, AttributeModifier.Operation.ADDITION));
		return slot == EquipmentSlot.MAINHAND ? builder.build() : super.getAttributeModifiers(slot, stack);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		if (pPlayer.isCrouching()) {
			pPlayer.getCooldowns().addCooldown(this, ABILITY_COOLDOWN);
			if (pPlayer.level().isClientSide()) {
				pLevel.playSound(pPlayer, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
						SoundEvents.WITHER_AMBIENT, pPlayer.getSoundSource(),
						1.0F, 1.0F);
			} else {
				ABILITY_SHOT_MAP.put(pPlayer.getUUID(), (ABILITY_SHOTS_COUNT - 1) * ABILITY_SHOTS_INTERVAL);
				ABILITY_BUFF_TIME_MAP.put(pPlayer.getUUID(), ABILITY_BUFF_DURATION);
				ItemStack itemStack = pPlayer.getItemInHand(pHand);
				CompoundTag tag = itemStack.getTag();
				if (tag != null) {
					tag.putFloat("speedBonus", ABILITY_ATTACK_SPEED_BONUS);
				}
			}
		}
		return super.use(pLevel, pPlayer, pHand);
	}

	@Override
	public ThrownSpear createThrownSpear(Level pLevel, Player player, ItemStack pStack) {
		ThrownSpear spear = new ThrownScourge(pLevel, player, pStack, ModEntities.THROWN_SCOURGE.get())
				.setBaseDamage(ThrowDamage);
		spear.setPierceLevel((byte) PIERCE_LEVEL);
		return spear;
	}

	private void ShootSpear(Level level, Player player, ItemStack stack) {
		ThrownSpear thrownspear = this.createThrownSpear(level, player, stack);
		if (thrownspear instanceof ThrownScourge scourge) {
			scourge.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, ProjectileVelocity, 1.0F);
			scourge.allowPickup = false;
			level.addFreshEntity(scourge);
			level.playSound(null, scourge, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (!event.side.isClient() && event.phase == TickEvent.Phase.END) {
			Player player = event.player;

			UUID playerID = player.getUUID();
			ItemStack itemStack = ItemStack.EMPTY;
			if (player.getMainHandItem().getItem() instanceof ScourgeItem) {
				itemStack = player.getMainHandItem();
			} else if (player.getOffhandItem().getItem() instanceof ScourgeItem) {
				itemStack = player.getOffhandItem();
			}

			//Handle ability shots
			if (ABILITY_SHOT_MAP.containsKey(playerID)) {
				int timer = ABILITY_SHOT_MAP.get(playerID);
				if (timer % ABILITY_SHOTS_INTERVAL == 0) {
					if (itemStack != ItemStack.EMPTY) {
						ScourgeItem item = (ScourgeItem) itemStack.getItem();
						item.ShootSpear(player.level(), player, itemStack);
					}
				}
				if (timer != -1) {
					ABILITY_SHOT_MAP.put(playerID, timer - 1);
				}
			}

			//Handle ability attributes buff
			if (ABILITY_BUFF_TIME_MAP.containsKey(player.getUUID())) {
				int time = ABILITY_BUFF_TIME_MAP.get(playerID);
				if (time > 0) {
					ABILITY_BUFF_TIME_MAP.put(playerID, time - 1);
				} else {
					if (itemStack != ItemStack.EMPTY) {
						CompoundTag tag = itemStack.getTag();
						if (tag.getFloat("speedBonus") == ABILITY_ATTACK_SPEED_BONUS) {
							tag.putFloat("speedBonus", 0.0F);
							itemStack.setTag(tag);
						}
					}
				}
			}
		}
	}
}
