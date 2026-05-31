package net.akashi.perk_weapons.Spears;

import net.akashi.perk_weapons.PerkWeapons;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.akashi.perk_weapons.Config.Properties.Spear.DragonStrikeProperties;
import net.akashi.perk_weapons.Config.Properties.Spear.SpearProperties;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownDragonStrike;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownSpear;
import net.akashi.perk_weapons.Registry.ModAttributes;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.akashi.perk_weapons.Registry.ModEntities;
import net.akashi.perk_weapons.Util.ICoolDownItem;
import net.akashi.perk_weapons.Util.TooltipHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static net.minecraft.world.item.enchantment.Enchantments.LOYALTY;

public class DragonStrikeItem extends BaseSpearItem implements ICoolDownItem {
	public static final String TAG_LAST_USED = "lastUsed";
	private static double MAGIC_RESISTANCE = 50;
	public static float INIT_AFFECT_CLOUD_RADIUS = 4.0F;
	public static float MAX_AFFECT_CLOUD_RADIUS = 6.0F;
	public static int AFFECT_CLOUD_DURATION = 60;
	public static int EFFECT_DAMAGE = 5;
	public static int RETURN_TIME = 40;
	private static int COOLDOWN_TICKS = 200;

	public DragonStrikeItem(Properties pProperties) {
		super(pProperties);
		this.RemoveGeneralEnchant(LOYALTY);
	}

	public DragonStrikeItem(float attackDamage, float attackSpeed, float throwDamage, float projectileVelocity,
	                        int maxChargeTicks, boolean isAdvanced, Properties pProperties) {
		super(attackDamage, attackSpeed, throwDamage, projectileVelocity, maxChargeTicks, isAdvanced, pProperties);
		this.RemoveGeneralEnchant(LOYALTY);
	}

	@Override
	protected void buildAttributeModifiers() {
		super.buildAttributeModifiers();
		ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
		for (ItemAttributeModifiers.Entry entry : this.DefaultAttributeModifiers.modifiers()) {
			builder.add(entry.attribute(), entry.modifier(), entry.slot());
		}
		builder.add(ModAttributes.MAGIC_RESISTANCE, new AttributeModifier(
				ResourceLocation.fromNamespaceAndPath(PerkWeapons.MODID, "dragon_strike_magic_resistance"), MAGIC_RESISTANCE,
				AttributeModifier.Operation.ADD_VALUE
		), EquipmentSlotGroup.MAINHAND);
		this.DefaultAttributeModifiers = builder.build();
	}

	@Override
	public void updateAttributesFromConfig(SpearProperties properties) {
		if (properties instanceof DragonStrikeProperties dProperties) {
			MAGIC_RESISTANCE = dProperties.MAGIC_RESISTANCE.get();
			COOLDOWN_TICKS = dProperties.ABILITY_COOLDOWN_TIME.get();
			INIT_AFFECT_CLOUD_RADIUS = dProperties.INIT_AFFECT_RADIUS.get().floatValue();
			MAX_AFFECT_CLOUD_RADIUS = dProperties.MAX_AFFECT_RADIUS.get().floatValue();
			AFFECT_CLOUD_DURATION = dProperties.AFFECT_DURATION.get();
			EFFECT_DAMAGE = dProperties.EFFECT_DAMAGE.get();
			RETURN_TIME = dProperties.RETURN_TIME.get();
		}
		super.updateAttributesFromConfig(properties);
	}

	@Override
	public ThrownSpear createThrownSpear(Level pLevel, Player player, ItemStack pStack) {
		return new ThrownDragonStrike(ModEntities.THROWN_DRAGON_STRIKE.get(), pLevel, player, pStack, RETURN_TIME);
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {
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
						entity.push(knockbackDirection.x, 0.5, knockbackDirection.z);
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

	@Override
	public float getCoolDownProgress(LivingEntity entity, ItemStack stack) {
		return (float) Math.min((double) (entity.level().getGameTime() - getLastAbilityUsedTime(stack)) / COOLDOWN_TICKS, 1.0);
	}

	public void setLastAbilityUsedTime(ItemStack stack, Long time) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putLong(TAG_LAST_USED, time));
	}

	public long getLastAbilityUsedTime(ItemStack stack) {
		CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
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
		List<Component> list = super.getPerkDescriptions(stack, level);
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.dragon_strike_perk_1")));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.dragon_strike_perk_2")));

		list.add(Component.empty());
		list.add(TooltipHelper.getCrouchUseAbilityHint());
		list.add(TooltipHelper.getCoolDownTip(COOLDOWN_TICKS));
		list.add(TooltipHelper.setPerkStyle(Component.translatable("tooltip.perk_weapons.dragon_strike_ability_1")));

		return list;
	}
}
