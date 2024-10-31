package net.akashi.perk_weapons.Client.Renderer;

import net.akashi.perk_weapons.Entities.Projectiles.Arrows.ExplosiveArrow;
import net.akashi.perk_weapons.PerkWeapons;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ExplosiveArrowRenderer extends ArrowRenderer<ExplosiveArrow> {
	public static final ResourceLocation EXPLOSIVE_ARROW_LOCATION =
			new ResourceLocation(PerkWeapons.MODID, "textures/entity/projectiles/explosive_arrow.png");

	public ExplosiveArrowRenderer(EntityRendererProvider.Context pContext) {
		super(pContext);
	}

	@Override
	public ResourceLocation getTextureLocation(ExplosiveArrow pEntity) {
		return EXPLOSIVE_ARROW_LOCATION;
	}
}
