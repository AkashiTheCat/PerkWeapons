package net.akashi.perk_weapons.Client.GUI;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.akashi.perk_weapons.Bows.BaseBowItem;
import net.akashi.perk_weapons.Config.ModClientConfigs;
import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Util.IDoubleLineCrosshairItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PerkWeapons.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class DoubleLineCrossHair {
	public static boolean isVanillaCrosshairDisabled = false;
	public static final ResourceLocation HUD_TEXTURE = new ResourceLocation(PerkWeapons.MODID, "textures/gui/hud.png");
	public static final IGuiOverlay CROSSHAIR = ((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
		//Check CrossHairs Enabled
		if (!ModClientConfigs.ENABLE_CUSTOM_CROSSHAIR.get()) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;

		boolean isItemMatched = false;
		ItemStack stack = ItemStack.EMPTY;
		if (player.getMainHandItem().getItem() instanceof IDoubleLineCrosshairItem) {
			stack = player.getMainHandItem();
			isItemMatched = true;
		} else if (player.getOffhandItem().getItem() instanceof IDoubleLineCrosshairItem) {
			stack = player.getOffhandItem();
			isItemMatched = true;
		}

		if (isItemMatched) {
			RenderCrossHair(player, stack, guiGraphics, screenWidth, screenHeight);
		} else if (isVanillaCrosshairDisabled) {
			isVanillaCrosshairDisabled = false;
		}
	});

	private static void RenderCrossHair(Player player, ItemStack stack, GuiGraphics guiGraphics,
	                                    int screenWidth, int screenHeight) {
		if (!isVanillaCrosshairDisabled) {
			isVanillaCrosshairDisabled = true;
		}

		float progress = ((IDoubleLineCrosshairItem) stack.getItem()).getChokeProgress(player, stack);

		PoseStack poseStack = RenderSystem.getModelViewStack();
		poseStack.pushPose();

		RenderSystem.assertOnRenderThread();
		RenderSystem.enableBlend();

		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.8F);
		RenderSystem.setShaderTexture(0, HUD_TEXTURE);
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
				GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
				GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);

		int centerX = Math.round((float) screenWidth / 2);
		int startY = Math.round((float) screenHeight / 2) - 1;
		int leftStartX = (int) (centerX + Math.floor(progress * 13) - 27);
		int rightStartX = (int) (centerX - Math.floor(progress * 13) + 16);

		//render center
		guiGraphics.blit(HUD_TEXTURE, centerX - 1, startY, 19, 0, 3, 3);

		//render left
		guiGraphics.blit(HUD_TEXTURE, leftStartX, startY, 6, 0, 12, 3);

		//render right
		guiGraphics.blit(HUD_TEXTURE, rightStartX, startY, 23, 0, 12, 3);
		poseStack.popPose();
	}

	@SubscribeEvent
	public static void onRenderVanillaCrosshair(RenderGuiOverlayEvent event) {
		if (ModClientConfigs.ENABLE_CUSTOM_CROSSHAIR.get() && isVanillaCrosshairDisabled
				&& event.getOverlay().id() == VanillaGuiOverlay.CROSSHAIR.id()) {
			event.setCanceled(true);
		}
	}
}
