package net.akashi.perk_weapons.Client.Renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.akashi.perk_weapons.Entities.BeholderBeamEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class BeholderBeamRenderer extends EntityRenderer<BeholderBeamEntity> {
	private static final ResourceLocation BEAM_LOCATION = new ResourceLocation(
			"textures/entity/guardian_beam.png");
	private static final RenderType BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(BEAM_LOCATION);

	public BeholderBeamRenderer(EntityRendererProvider.Context pContext) {
		super(pContext);
	}

	@Override
	public boolean shouldRender(@NotNull BeholderBeamEntity pLivingEntity, @NotNull Frustum pCamera,
	                            double pCamX, double pCamY, double pCamZ) {
		if (super.shouldRender(pLivingEntity, pCamera, pCamX, pCamY, pCamZ))
			return true;

		Entity target = pLivingEntity.getTargetEntity();
		if (target != null) {
			Vec3 targetPos = this.getPosition(target, target.getBbHeight() * 0.5D, 1.0F);
			Vec3 srcPos = this.getPosition(pLivingEntity, pLivingEntity.getEyeHeight(), 1.0F);
			return pCamera.isVisible(new AABB(srcPos.x, srcPos.y, srcPos.z, targetPos.x, targetPos.y, targetPos.z));
		}

		return false;
	}

	@Override
	protected int getSkyLightLevel(@NotNull BeholderBeamEntity pEntity, @NotNull BlockPos pPos) {
		return 15;
	}

	@Override
	protected int getBlockLightLevel(@NotNull BeholderBeamEntity pEntity, @NotNull BlockPos pPos) {
		return 15;
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull BeholderBeamEntity pEntity) {
		return BEAM_LOCATION;
	}

	@Override
	public void render(@NotNull BeholderBeamEntity pEntity, float pEntityYaw, float pPartialTicks,
	                   @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight) {
		super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
		Entity target = pEntity.getTargetEntity();
		if (target != null) {
			int d = (int) (pEntity.level().getGameTime() % 100);
			d = d > 50 ? 100 - d : d;
			float f = (float) d / 50.0F;
			float f1 = (float) d * 0.5F % 1.0F;
			float f2 = f1 * 0.5F % 1.0F;
			float f3 = pEntity.getEyeHeight();
			pPoseStack.pushPose();
			pPoseStack.translate(0.0F, f3, 0.0F);
			Vec3 vec3 = this.getPosition(target, (double) target.getBbHeight() * 0.5D, pPartialTicks);
			Vec3 vec31 = this.getPosition(pEntity, f3, pPartialTicks);
			Vec3 vec32 = vec3.subtract(vec31);
			float f4 = (float) (vec32.length() + 1.0D);
			vec32 = vec32.normalize();
			float f5 = (float) Math.acos(vec32.y);
			float f6 = (float) Math.atan2(vec32.z, vec32.x);
			pPoseStack.mulPose(Axis.YP.rotationDegrees((((float) Math.PI / 2F) - f6) * (180F / (float) Math.PI)));
			pPoseStack.mulPose(Axis.XP.rotationDegrees(f5 * (180F / (float) Math.PI)));
			float f7 = f1 * 0.05F * -1.5F;
			float f8 = f * f;
			int j = 64 + (int) (f8 * 191.0F);
			int k = 32 + (int) (f8 * 191.0F);
			int l = 128 - (int) (f8 * 64.0F);
			float f11 = Mth.cos(f7 + 2.3561945F) * 0.282F;
			float f12 = Mth.sin(f7 + 2.3561945F) * 0.282F;
			float f13 = Mth.cos(f7 + ((float) Math.PI / 4F)) * 0.282F;
			float f14 = Mth.sin(f7 + ((float) Math.PI / 4F)) * 0.282F;
			float f15 = Mth.cos(f7 + 3.926991F) * 0.282F;
			float f16 = Mth.sin(f7 + 3.926991F) * 0.282F;
			float f17 = Mth.cos(f7 + 5.4977875F) * 0.282F;
			float f18 = Mth.sin(f7 + 5.4977875F) * 0.282F;
			float f19 = Mth.cos(f7 + (float) Math.PI) * 0.2F;
			float f20 = Mth.sin(f7 + (float) Math.PI) * 0.2F;
			float f21 = Mth.cos(f7 + 0.0F) * 0.2F;
			float f22 = Mth.sin(f7 + 0.0F) * 0.2F;
			float f23 = Mth.cos(f7 + ((float) Math.PI / 2F)) * 0.2F;
			float f24 = Mth.sin(f7 + ((float) Math.PI / 2F)) * 0.2F;
			float f25 = Mth.cos(f7 + ((float) Math.PI * 1.5F)) * 0.2F;
			float f26 = Mth.sin(f7 + ((float) Math.PI * 1.5F)) * 0.2F;
			float f29 = -1.0F + f2;
			float f30 = f4 * 2.5F + f29;
			VertexConsumer vertexconsumer = pBuffer.getBuffer(BEAM_RENDER_TYPE);
			PoseStack.Pose posestack$pose = pPoseStack.last();
			Matrix4f matrix4f = posestack$pose.pose();
			Matrix3f matrix3f = posestack$pose.normal();
			vertex(vertexconsumer, matrix4f, matrix3f, f19, f4, f20, j, k, l, 0.4999F, f30);
			vertex(vertexconsumer, matrix4f, matrix3f, f19, 0.0F, f20, j, k, l, 0.4999F, f29);
			vertex(vertexconsumer, matrix4f, matrix3f, f21, 0.0F, f22, j, k, l, 0.0F, f29);
			vertex(vertexconsumer, matrix4f, matrix3f, f21, f4, f22, j, k, l, 0.0F, f30);
			vertex(vertexconsumer, matrix4f, matrix3f, f23, f4, f24, j, k, l, 0.4999F, f30);
			vertex(vertexconsumer, matrix4f, matrix3f, f23, 0.0F, f24, j, k, l, 0.4999F, f29);
			vertex(vertexconsumer, matrix4f, matrix3f, f25, 0.0F, f26, j, k, l, 0.0F, f29);
			vertex(vertexconsumer, matrix4f, matrix3f, f25, f4, f26, j, k, l, 0.0F, f30);
			float f31 = 0.0F;
			if (pEntity.tickCount % 2 == 0) {
				f31 = 0.5F;
			}

			vertex(vertexconsumer, matrix4f, matrix3f, f11, f4, f12, j, k, l, 0.5F, f31 + 0.5F);
			vertex(vertexconsumer, matrix4f, matrix3f, f13, f4, f14, j, k, l, 1.0F, f31 + 0.5F);
			vertex(vertexconsumer, matrix4f, matrix3f, f17, f4, f18, j, k, l, 1.0F, f31);
			vertex(vertexconsumer, matrix4f, matrix3f, f15, f4, f16, j, k, l, 0.5F, f31);
			pPoseStack.popPose();
		}

	}

	private Vec3 getPosition(Entity Entity, double pYOffset, float pPartialTick) {
		double d0 = Mth.lerp(pPartialTick, Entity.xOld, Entity.getX());
		double d1 = Mth.lerp(pPartialTick, Entity.yOld, Entity.getY()) + pYOffset;
		double d2 = Mth.lerp(pPartialTick, Entity.zOld, Entity.getZ());
		return new Vec3(d0, d1, d2);
	}

	private static void vertex(VertexConsumer pConsumer, Matrix4f pPose, Matrix3f pNormal, float pX, float pY, float pZ, int pRed, int pGreen, int pBlue, float pU, float pV) {
		pConsumer.vertex(pPose, pX, pY, pZ).color(pRed, pGreen, pBlue, 255).uv(pU, pV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(pNormal, 0.0F, 1.0F, 0.0F).endVertex();
	}
}
