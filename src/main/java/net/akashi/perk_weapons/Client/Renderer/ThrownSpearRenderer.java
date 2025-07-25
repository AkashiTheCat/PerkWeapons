package net.akashi.perk_weapons.Client.Renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.akashi.perk_weapons.Entities.Projectiles.Spears.ThrownSpear;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class ThrownSpearRenderer<T extends ThrownSpear> extends EntityRenderer<T> {
	private final ItemRenderer itemRenderer;

	public ThrownSpearRenderer(EntityRendererProvider.Context pContext) {
		super(pContext);
		this.itemRenderer = Minecraft.getInstance().getItemRenderer();
	}

	public void render(@NotNull T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack,
	                   @NotNull MultiBufferSource pBuffer, int pPackedLight) {
		pPoseStack.pushPose();

		doRenderTransformations(pEntity, pPartialTicks, pPoseStack);
		Vector3f nextRotateAxis = new Vector3f(1.0f, 1.0f, 0.0f);
		nextRotateAxis.normalize();
		pPoseStack.mulPose(new Quaternionf().setAngleAxis(Mth.PI, nextRotateAxis.x, nextRotateAxis.y, nextRotateAxis.z));
		pPoseStack.translate(-0.3d, -0.6d, 0.0d);

		ItemStack weapon = pEntity.getItemForRender();
		BakedModel bakedModel = this.itemRenderer.getModel(weapon, pEntity.level(), null, pEntity.getId());
		this.itemRenderer.render(weapon, ItemDisplayContext.GROUND, false, pPoseStack, pBuffer, pPackedLight,
				OverlayTexture.NO_OVERLAY, bakedModel);
		pPoseStack.popPose();
		super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
	}

	public @NotNull ResourceLocation getTextureLocation(@NotNull T pEntity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}

	//from Spartan Weaponry
	protected void doRenderTransformations(T entity, float partialTicks, PoseStack matrixStack) {
		matrixStack.scale(2.2f, 2.2f, 2.2f);
		matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0f));
		matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) - 45.0f));
	}
}