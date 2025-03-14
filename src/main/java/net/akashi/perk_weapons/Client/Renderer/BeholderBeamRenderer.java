package net.akashi.perk_weapons.Client.Renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.GuardianRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class BeholderBeamRenderer {
	private static final ResourceLocation BEHOLDER_BEAM_LOCATION = new ResourceLocation(
			"textures/entity/guardian_beam.png");
	private static final RenderType BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(BEHOLDER_BEAM_LOCATION);
	public static final Map<Integer, Integer> RENDER_DATA_MAP = new HashMap<>(32);

	public static void addRenderData(int fromID, int toID) {
		RENDER_DATA_MAP.put(fromID, toID);
	}

	public static void removeRenderData(int fromID) {
		RENDER_DATA_MAP.remove(fromID);
	}

	@SubscribeEvent
	public static void onRenderWorldLast(RenderLevelStageEvent event) {
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS)
			return;
		Minecraft mc = Minecraft.getInstance();
		Level level = mc.level;
		if (level == null)
			return;

		MultiBufferSource buffer = mc.renderBuffers().bufferSource();
		RENDER_DATA_MAP.forEach((from, to) -> {
			Entity eFrom = level.getEntity(from);
			Entity eTo = level.getEntity(to);
			if (eFrom != null && eTo != null) {
				int d = (int) (level.getGameTime() % 100);
				renderBeam(eFrom, eTo,
						event.getPoseStack(), buffer,
						d > 50 ? 100 - d : d,
						event.getPartialTick()
				);
			}
		});
	}

	private static void renderBeam(Entity from, Entity to, PoseStack poseStack, MultiBufferSource buffer,
	                               int discrete, float partialTicks) {
		float f = (float) discrete / 50;
		float f2 = (float) discrete * 0.5F % 1.0F;
		float f3 = from.getBbHeight() * 0.5F;
		poseStack.pushPose();
		poseStack.translate(0.0F, -f3, 0.0F);
		Vec3 vec3 = getPosition(to, (double) to.getBbHeight() * 0.5D, partialTicks);
		Vec3 vec31 = getPosition(from, f3, partialTicks);
		Vec3 vec32 = vec3.subtract(vec31);
		float f4 = (float) (vec32.length() + 1.0D);
		vec32 = vec32.normalize();
		float f5 = (float) Math.acos(vec32.y);
		float f6 = (float) Math.atan2(vec32.z, vec32.x);
		poseStack.mulPose(Axis.YP.rotationDegrees((((float) Math.PI / 2F) - f6) * (180F / (float) Math.PI)));
		poseStack.mulPose(Axis.XP.rotationDegrees(f5 * (180F / (float) Math.PI)));
		float f7 = (float) discrete * 0.05F * -1.5F;
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
		VertexConsumer vertexconsumer = buffer.getBuffer(BEAM_RENDER_TYPE);
		PoseStack.Pose posestack$pose = poseStack.last();
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
		if (from.tickCount % 2 == 0) {
			f31 = 0.5F;
		}

		vertex(vertexconsumer, matrix4f, matrix3f, f11, f4, f12, j, k, l, 0.5F, f31 + 0.5F);
		vertex(vertexconsumer, matrix4f, matrix3f, f13, f4, f14, j, k, l, 1.0F, f31 + 0.5F);
		vertex(vertexconsumer, matrix4f, matrix3f, f17, f4, f18, j, k, l, 1.0F, f31);
		vertex(vertexconsumer, matrix4f, matrix3f, f15, f4, f16, j, k, l, 0.5F, f31);
		poseStack.popPose();
	}

	private static void vertex(VertexConsumer pConsumer, Matrix4f pPose, Matrix3f pNormal,
	                           float pX, float pY, float pZ,
	                           int pRed, int pGreen, int pBlue,
	                           float pU, float pV) {
		pConsumer.vertex(pPose, pX, pY, pZ).color(pRed, pGreen, pBlue, 255)
				.uv(pU, pV).overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(15728880)
				.normal(pNormal, 0.0F, 1.0F, 0.0F)
				.endVertex();
	}

	private static Vec3 getPosition(Entity pLivingEntity, double pYOffset, float pPartialTick) {
		double d0 = Mth.lerp(pPartialTick, pLivingEntity.xOld, pLivingEntity.getX());
		double d1 = Mth.lerp(pPartialTick, pLivingEntity.yOld, pLivingEntity.getY()) + pYOffset;
		double d2 = Mth.lerp(pPartialTick, pLivingEntity.zOld, pLivingEntity.getZ());
		return new Vec3(d0, d1, d2);
	}
}
