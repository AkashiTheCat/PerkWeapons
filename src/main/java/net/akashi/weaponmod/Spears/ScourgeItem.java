package net.akashi.weaponmod.Spears;

import net.akashi.weaponmod.Config.Properties.ScourgeProperties;
import net.akashi.weaponmod.Config.Properties.SpearProperties;
import net.akashi.weaponmod.Entities.Projectiles.ThrownScourge;
import net.akashi.weaponmod.Entities.Projectiles.ThrownSpear;
import net.akashi.weaponmod.Registry.ModEffects;
import net.akashi.weaponmod.Registry.ModEntities;
import net.akashi.weaponmod.Registry.ModItems;
import net.akashi.weaponmod.WeaponMod;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.brewing.PotionBrewEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraft.world.item.enchantment.Enchantments.FIRE_ASPECT;

@Mod.EventBusSubscriber(modid = WeaponMod.MODID , bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ScourgeItem extends SpearItem {
	private static int AbilityCoolDownTime = 200;
	private long lastAbilityUseTime = 0;
	public static int WITHER_DURATION = 40;
	public static int WITHER_LEVEL = 3;
	public static int SLOWNESS_DURATION = 40;
	public static int SLOWNESS_LEVEL = 2;
	public static float INIT_AFFECT_CLOUD_RADIUS = 3.0F;
	public static float MAX_AFFECT_CLOUD_RADIUS = 5.0F;
	public static int AFFECT_CLOUD_DURATION = 60;
	public static int AFFECT_CLOUD_LEVEL = 2;
	public static int PIERCE_LEVEL = 255;

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
			INIT_AFFECT_CLOUD_RADIUS = sProperties.INIT_AFFECT_CLOUD_RADIUS.get().floatValue();
			MAX_AFFECT_CLOUD_RADIUS = sProperties.MAX_AFFECT_CLOUD_RADIUS.get().floatValue();
			AFFECT_CLOUD_DURATION = sProperties.AFFECT_CLOUD_DURATION.get();
			AFFECT_CLOUD_LEVEL = sProperties.AFFECT_CLOUD_EFFECT_LEVEL.get();
			AbilityCoolDownTime = sProperties.ABILITY_COOLDOWN_TIME.get();
			PIERCE_LEVEL = sProperties.PIERCE_LEVEL.get();
		}
	}


	@Override
	public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
		if (slotIndex == selectedIndex && !level.isClientSide()) {
			player.removeEffect(MobEffects.WITHER);
		}
		super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		if (pLevel.getGameTime() - this.lastAbilityUseTime > AbilityCoolDownTime
				&& pPlayer.isCrouching() && pHand == InteractionHand.MAIN_HAND) {
			if (!pLevel.isClientSide()) {
				spawnAreaEffect(pLevel, pPlayer);
				this.lastAbilityUseTime = pLevel.getGameTime();
			} else {
				pLevel.playSound(pPlayer, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
						SoundEvents.WITHER_AMBIENT, pPlayer.getSoundSource(),
						1.0F, 1.0F);
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

	public void spawnAreaEffect(Level level, Player player) {
		AreaEffectCloud areaeffectcloud = new AreaEffectCloud(level, player.getX(), player.getY(), player.getZ());
		areaeffectcloud.setOwner(player);
		areaeffectcloud.setRadius(INIT_AFFECT_CLOUD_RADIUS);
		areaeffectcloud.setDuration(AFFECT_CLOUD_DURATION);
		areaeffectcloud.setRadiusPerTick((MAX_AFFECT_CLOUD_RADIUS - areaeffectcloud.getRadius())
				/ (float) areaeffectcloud.getDuration());
		areaeffectcloud.addEffect(new MobEffectInstance(MobEffects.WITHER, 40, AFFECT_CLOUD_LEVEL - 1));
		level.addFreshEntity(areaeffectcloud);
	}

	@SubscribeEvent
	public static void onLivingDamage(LivingHurtEvent event) {
		if (event.getEntity() instanceof Player player) {
			if(!player.level().isClientSide()){
				if (player.getMainHandItem().getItem() instanceof ScourgeItem) {
					if (event.getSource().is(DamageTypes.WITHER)) {
						event.setCanceled(true);
						player.removeEffect(MobEffects.WITHER);
					}
				}
			}
		}
	}
}
