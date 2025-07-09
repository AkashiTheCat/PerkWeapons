package net.akashi.perk_weapons.Client.Renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.akashi.perk_weapons.Entities.Projectiles.Arrows.EndboreWandererPerkProjectile;
import net.minecraft.client.model.ShulkerBulletModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class EndboreWandererPerkProjectileRenderer extends EntityRenderer<EndboreWandererPerkProjectile> {
	private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/entity/shulker/spark.png");
	private static final RenderType RENDER_TYPE = RenderType.entityTranslucent(TEXTURE_LOCATION);
	private final ShulkerBulletModel<EndboreWandererPerkProjectile> model;

	public EndboreWandererPerkProjectileRenderer(EntityRendererProvider.Context pContext) {
		super(pContext);
		this.model = new ShulkerBulletModel<>(pContext.bakeLayer(ModelLayers.SHULKER_BULLET));
	}

	protected int getBlockLightLevel(ShulkerBullet pEntity, BlockPos pPos) {
		return 15;
	}

	@Override
	public void render(EndboreWandererPerkProjectile pEntity, float pEntityYaw, float pPartialTicks,
	                   PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
		pPoseStack.pushPose();
		float f = Mth.rotLerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot());
		float f1 = Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot());
		float f2 = (float)pEntity.tickCount + pPartialTicks;
		pPoseStack.translate(0.0F, 0.15F, 0.0F);
		pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(f2 * 0.1F) * 180.0F));
		pPoseStack.mulPose(Axis.XP.rotationDegrees(Mth.cos(f2 * 0.1F) * 180.0F));
		pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(f2 * 0.15F) * 360.0F));
		pPoseStack.scale(-0.5F, -0.5F, 0.5F);
		this.model.setupAnim(pEntity, 0.0F, 0.0F, 0.0F, f, f1);
		VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(TEXTURE_LOCATION));
		this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		pPoseStack.scale(1.5F, 1.5F, 1.5F);
		VertexConsumer vertexconsumer1 = pBuffer.getBuffer(RENDER_TYPE);
		this.model.renderToBuffer(pPoseStack, vertexconsumer1, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.15F);
		pPoseStack.popPose();
		super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull EndboreWandererPerkProjectile pEntity) {
		return TEXTURE_LOCATION;
	}
}
