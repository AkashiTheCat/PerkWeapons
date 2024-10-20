package net.akashi.weaponmod.Spears;

import com.google.common.collect.ImmutableMultimap;
import net.akashi.weaponmod.Config.ModCommonConfigs;
import net.akashi.weaponmod.Config.Properties.DragonStrikeProperties;
import net.akashi.weaponmod.Config.Properties.SpearProperties;
import net.akashi.weaponmod.Entities.Projectiles.ThrownDragonStrike;
import net.akashi.weaponmod.Entities.Projectiles.ThrownSpear;
import net.akashi.weaponmod.Registry.ModEntities;
import net.akashi.weaponmod.WeaponMod;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

import static net.minecraft.world.item.enchantment.Enchantments.LOYALTY;

@Mod.EventBusSubscriber(modid = WeaponMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DragonStrikeItem extends SpearItem {
	private static double MagicResistance = 0.5;
	private static int knockbackCoolDownTime = 200;
	private long abilityUsedGameTime = 0;

	public DragonStrikeItem(boolean isAdvanced, Properties pProperties) {
		super(isAdvanced, pProperties);
		this.RemoveGeneralEnchant(LOYALTY);
	}

	public DragonStrikeItem(float attackDamage, float attackSpeed, float throwDamage, float projectileVelocity,
	                        boolean isAdvanced, Properties pProperties) {
		super(attackDamage, attackSpeed, throwDamage, projectileVelocity, isAdvanced, pProperties);
		this.RemoveGeneralEnchant(LOYALTY);
	}

	@Override
	public void updateAttributesFromConfig(SpearProperties properties) {
		super.updateAttributesFromConfig(properties);
		if (properties instanceof DragonStrikeProperties dProperties) {
			MagicResistance = dProperties.MAGIC_RESISTANCE.get();
			knockbackCoolDownTime = dProperties.ABILITY_COOLDOWN_TIME.get();
		}
	}


	@Override
	public ThrownSpear createThrownSpear(Level pLevel, Player player, ItemStack pStack) {
		return new ThrownDragonStrike(pLevel, player, pStack, getItemSlotIndex(player, pStack),
				ModCommonConfigs.DRAGON_STRIKE_PROPERTIES.RETURN_TIME.get(), ModEntities.THROWN_DRAGON_STRIKE.get())
				.setBaseDamage(this.ThrowDamage);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		if (pPlayer.isCrouching() && pHand == InteractionHand.MAIN_HAND
				&& (pLevel.getGameTime() - abilityUsedGameTime) > knockbackCoolDownTime) {
			double x = pPlayer.getX();
			double y = pPlayer.getY();
			double z = pPlayer.getZ();
			if (!pLevel.isClientSide()) {
				float radius = 4.0f;

				AABB explosionArea = new AABB(x - radius, y - radius, z - radius,
						x + radius, y + radius, z + radius);
				for (Entity entity : pLevel.getEntities(pPlayer, explosionArea)) {
					if (entity instanceof LivingEntity && entity != pPlayer) {
						Vec3 knockbackDirection = entity.position().subtract(x, y, z).normalize().scale(1.0);
						entity.setDeltaMovement(knockbackDirection.x, 0.5, knockbackDirection.z);
					}
				}
			} else {
				pLevel.addParticle(ParticleTypes.EXPLOSION_EMITTER, x, y, z,
						1.0, 0.0, 0.0);
			}
			this.abilityUsedGameTime = pLevel.getGameTime();
			pLevel.playSound(pPlayer, x, y, z, SoundEvents.ENDER_DRAGON_GROWL, pPlayer.getSoundSource(),
					1.0F, 1.0F);
		}
		return super.use(pLevel, pPlayer, pHand);
	}

	@SubscribeEvent
	public static void onPlayerHurt(LivingHurtEvent event) {
		if (event.getEntity() instanceof Player player) {
			if (event.getSource().is(DamageTypes.MAGIC)) {
				if (player.getMainHandItem().getItem() instanceof DragonStrikeItem) {
					float reducedDamage = event.getAmount() * (1 - (float) MagicResistance);
					event.setAmount(reducedDamage);
				}
			}
		}
	}
}
