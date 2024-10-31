package net.akashi.perk_weapons.Spears;

import net.akashi.perk_weapons.Config.Properties.Spear.ScourgeProperties;
import net.akashi.perk_weapons.Config.Properties.Spear.SpearProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownScourge;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownSpear;
import net.akashi.perk_weapons.Network.SpearAttributeUpdateSyncPacket;
import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Registry.ModPackets;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import static net.minecraft.world.item.enchantment.Enchantments.FIRE_ASPECT;

@Mod.EventBusSubscriber(modid = PerkWeapons.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ScourgeItem extends SpearItem {
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

	private int buffTimer = -1;
	private int shootTimer = 0;
	private int perkShotsRemain = 0;
	private Player Owner = null;

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
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int SlotId, boolean isSelected) {
		if (shootTimer == 0 && perkShotsRemain > 0 && this.Owner != null) {
			ShootSpear(level, this.Owner, stack);
			if (!level.isClientSide()) {
				shootTimer = ABILITY_SHOTS_INTERVAL;
				perkShotsRemain--;
			}
		}
		if (!level.isClientSide()) {
			if (buffTimer == ABILITY_BUFF_DURATION) {
				updateAttributes(1, 1 + ABILITY_ATTACK_SPEED_BONUS);
				if (this.Owner != entity)
					ModPackets.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) this.Owner),
							new SpearAttributeUpdateSyncPacket(entity.getId(), 1, 1 + ABILITY_ATTACK_SPEED_BONUS));
			}
			if (buffTimer == 0) {
				updateAttributes(1, 1);
				if (this.Owner != entity)
					ModPackets.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) this.Owner),
							new SpearAttributeUpdateSyncPacket(entity.getId(), 1, 1));
			}

			if (shootTimer > 0) {
				shootTimer--;
			}
			if (buffTimer >= 0) {
				buffTimer--;
			}
		}
		super.inventoryTick(stack, level, entity, SlotId, isSelected);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		if (pPlayer.isCrouching() && pHand == InteractionHand.MAIN_HAND) {
			this.Owner = pPlayer;
			pPlayer.getCooldowns().addCooldown(this, ABILITY_COOLDOWN);
			if (pPlayer.level().isClientSide()) {
				pLevel.playSound(pPlayer, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
						SoundEvents.WITHER_AMBIENT, pPlayer.getSoundSource(),
						1.0F, 1.0F);
			} else {
				perkShotsRemain = ABILITY_SHOTS_COUNT;
				buffTimer = ABILITY_BUFF_DURATION;
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
}
