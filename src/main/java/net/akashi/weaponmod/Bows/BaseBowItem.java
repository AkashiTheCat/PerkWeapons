package net.akashi.weaponmod.Bows;

import net.akashi.weaponmod.Client.ClientHelper;
import net.akashi.weaponmod.Config.Properties.Bow.BowProperties;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.function.Predicate;

import static net.minecraft.world.item.enchantment.Enchantments.*;

public class BaseBowItem extends ProjectileWeaponItem implements Vanishable {
	public float VELOCITY = 3.0F;
	public int DRAW_TIME = 20;
	public float PROJECTILE_DAMAGE = 10;
	public float ZOOM_FACTOR = 0.1f;

	public BaseBowItem(Properties properties) {
		super(properties);
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerLongbowPropertyOverrides(this);
	}

	public BaseBowItem(int drawTime, float projectileDamage, float velocity, float zoomFactor, Properties properties) {
		super(properties);
		this.VELOCITY = velocity;
		this.DRAW_TIME = drawTime;
		this.PROJECTILE_DAMAGE = projectileDamage;
		this.ZOOM_FACTOR = zoomFactor;
		if (FMLEnvironment.dist.isClient())
			ClientHelper.registerLongbowPropertyOverrides(this);
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
					AbstractArrow abstractarrow = arrowitem.createArrow(pLevel, itemstack, player);
					abstractarrow = createArrow(abstractarrow);
					abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, VELOCITY, 1.0F);

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

	public AbstractArrow createArrow(AbstractArrow arrow) {
		arrow.setBaseDamage(PROJECTILE_DAMAGE / VELOCITY);
		return arrow;
	}

	public void updateAttributesFromConfig(BowProperties properties) {
		this.DRAW_TIME = properties.DRAW_TIME.get();
		this.PROJECTILE_DAMAGE = properties.DAMAGE.get().floatValue();
		this.VELOCITY = properties.VELOCITY.get().floatValue();
		this.ZOOM_FACTOR = properties.ZOOM_FACTOR.get().floatValue();
	}

	public float getDrawProgress(LivingEntity shooter) {
		return shooter.getTicksUsingItem() < DRAW_TIME ? (float) shooter.getTicksUsingItem() / DRAW_TIME : 1;
	}

	public int getDefaultProjectileRange() {
		return 15;
	}
}
