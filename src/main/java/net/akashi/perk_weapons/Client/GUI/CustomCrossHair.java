package net.akashi.perk_weapons.Client.GUI;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.akashi.perk_weapons.Bows.BaseBowItem;
import net.akashi.perk_weapons.Config.ModClientConfigs;
import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Util.ICoolDownItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = PerkWeapons.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CustomCrossHair {
	public static boolean isVanillaCrosshairDisabled = false;
	public static final ResourceLocation HUD_TEXTURE = new ResourceLocation(PerkWeapons.MODID, "textures/gui/hud.png");
	public static final IGuiOverlay CROSSHAIR = ((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
		//Check CrossHairs Enabled
		if (!ModClientConfigs.ENABLE_CUSTOM_CROSSHAIR.get()) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;

		byte flag = 0;
		ItemStack stack = ItemStack.EMPTY;
		if (player.getMainHandItem().getItem() instanceof BaseBowItem) {
			stack = player.getMainHandItem();
			flag = 1;
		} else if (player.getOffhandItem().getItem() instanceof BaseBowItem) {
			stack = player.getOffhandItem();
			flag = 1;
		}

		switch (flag) {
			case 1:
				BaseBowItem bowItem = (BaseBowItem) stack.getItem();
				RenderBowCrossHair(player, bowItem, guiGraphics, screenWidth, screenHeight);
				break;
			default:
				if (isVanillaCrosshairDisabled) {
					isVanillaCrosshairDisabled = false;
				}
				break;
		}
	});

	private static void RenderBowCrossHair(Player player, BaseBowItem bowItem, GuiGraphics guiGraphics, int screenWidth, int screenHeight) {
		if (!isVanillaCrosshairDisabled) {
			isVanillaCrosshairDisabled = true;
		}

		float progress = bowItem.getDrawProgress(player);

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
		int leftStartX = centerX + Math.round(progress * 13) - 27;
		int rightStartX = centerX - Math.round(progress * 13) + 16;

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
