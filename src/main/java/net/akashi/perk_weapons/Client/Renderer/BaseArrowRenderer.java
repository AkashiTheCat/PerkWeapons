package net.akashi.perk_weapons.Client.Renderer;

import net.akashi.perk_weapons.Entities.Projectiles.Arrows.BaseArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class BaseArrowRenderer extends ArrowRenderer<BaseArrow> {

	public BaseArrowRenderer(EntityRendererProvider.Context pContext) {
		super(pContext);
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(BaseArrow pEntity) {
		return pEntity.getArrowTexture();
	}
}
