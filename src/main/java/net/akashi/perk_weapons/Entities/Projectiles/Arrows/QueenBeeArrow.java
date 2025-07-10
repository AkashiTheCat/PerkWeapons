package net.akashi.perk_weapons.Entities.Projectiles.Arrows;

import net.akashi.perk_weapons.PerkWeapons;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class QueenBeeArrow extends PerkGainingArrow {
	public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(PerkWeapons.MODID,
			"/textures/entity/projectiles/queen_bee_arrow.png");

	public QueenBeeArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public QueenBeeArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, double pX, double pY, double pZ) {
		super(pEntityType, pLevel, pX, pY, pZ);
	}

	public QueenBeeArrow(EntityType<? extends BaseArrow> pEntityType, Level pLevel, LivingEntity pShooter) {
		super(pEntityType, pLevel, pShooter);
	}

	@Override
	public ResourceLocation getArrowTexture() {
		return TEXTURE_LOCATION;
	}
}
