package net.akashi.perk_weapons.Spears;

import net.akashi.perk_weapons.Config.Properties.Spear.DragonStrikeProperties;
import net.akashi.perk_weapons.Config.Properties.Spear.SpearProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownDragonStrike;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownSpear;
import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Util.ICoolDownItem;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static net.minecraft.world.item.enchantment.Enchantments.LOYALTY;

@Mod.EventBusSubscriber(modid = PerkWeapons.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DragonStrikeItem extends BaseSpearItem implements ICoolDownItem {
	public static final String TAG_LAST_USED = "lastUsed";
	private static double MagicResistance = 0.5;
	public static float INIT_AFFECT_CLOUD_RADIUS = 4.0F;
	public static float MAX_AFFECT_CLOUD_RADIUS = 6.0F;
	public static int AFFECT_CLOUD_DURATION = 60;
	public static int EFFECT_DAMAGE = 5;
	public static int RETURN_TIME = 40;
	private static int COOLDOWN_TICKS = 200;

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
			COOLDOWN_TICKS = dProperties.ABILITY_COOLDOWN_TIME.get();
			INIT_AFFECT_CLOUD_RADIUS = dProperties.INIT_AFFECT_RADIUS.get().floatValue();
			MAX_AFFECT_CLOUD_RADIUS = dProperties.MAX_AFFECT_RADIUS.get().floatValue();
			AFFECT_CLOUD_DURATION = dProperties.AFFECT_DURATION.get();
			EFFECT_DAMAGE = dProperties.EFFECT_DAMAGE.get();
			RETURN_TIME = dProperties.RETURN_TIME.get();
		}
	}


	@Override
	public ThrownSpear createThrownSpear(Level pLevel, Player player, ItemStack pStack) {
		return new ThrownDragonStrike(pLevel, player, pStack, RETURN_TIME,
				ModEntities.THROWN_DRAGON_STRIKE.get())
				.setBaseDamage(this.ThrowDamage);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		ItemStack stack = pPlayer.getItemInHand(pHand);
		if (getCoolDownProgress(pPlayer, stack) == 1.0 && pPlayer.isCrouching()) {
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
				setLastAbilityUsedTime(stack, pLevel.getGameTime());
				pLevel.playSound(null, x, y, z, SoundEvents.ENDER_DRAGON_GROWL, pPlayer.getSoundSource(),
						1.0F, 1.0F);
			} else {
				pLevel.addParticle(ParticleTypes.EXPLOSION_EMITTER, x, y, z,
						1.0, 0.0, 0.0);
			}
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

	@Override
	public float getCoolDownProgress(LivingEntity entity, ItemStack stack) {
		return (float) Math.min((double) (entity.level().getGameTime() - getLastAbilityUsedTime(stack)) / COOLDOWN_TICKS, 1.0);
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

	@Override
	public Component getWeaponDescription(ItemStack stack, Level level) {
		return TooltipHelper.setCommentStyle(Component.translatable("tooltip.perk_weapons.dragon_strike"));
	}

	@Override
	public List<Component> getPerkDescriptions(ItemStack stack, Level level) {
		List<Component> list = new ArrayList<>();
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.dragon_strike_perk_1")));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.dragon_strike_perk_2")));
		list.add(TooltipHelper.getRatioModifierWithStyle("tooltip.perk_weapons.dragon_strike_perk_3",
				(float) MagicResistance));

		list.add(Component.empty());
		list.add(TooltipHelper.getCrouchUseAbilityHint());
		list.add(TooltipHelper.getCoolDownTip(COOLDOWN_TICKS));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.dragon_strike_ability_1")));

		return list;
	}
}
