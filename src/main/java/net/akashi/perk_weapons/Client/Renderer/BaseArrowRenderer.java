package net.akashi.perk_weapons.Client.Renderer;

import net.akashi.perk_weapons.Entities.Projectiles.Arrows.BaseArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SpectralArrowRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BaseArrowRenderer extends ArrowRenderer<BaseArrow> {
	public static final ResourceLocation NORMAL_ARROW_LOCATION = new ResourceLocation("textures/entity/projectiles/arrow.png");
	public static final ResourceLocation TIPPED_ARROW_LOCATION = new ResourceLocation("textures/entity/projectiles/tipped_arrow.png");
	public static final ResourceLocation SPECTRAL_ARROW_LOCATION = new ResourceLocation("textures/entity/projectiles/spectral_arrow.png");
	public BaseArrowRenderer(EntityRendererProvider.Context pContext) {
		super(pContext);
	}

	public ResourceLocation getTextureLocation(BaseArrow pEntity) {
		if(pEntity.isSpectralArrow()){
			return SPECTRAL_ARROW_LOCATION;
		}
		return pEntity.getColor() > 0 ? TIPPED_ARROW_LOCATION : NORMAL_ARROW_LOCATION;
	}
}
