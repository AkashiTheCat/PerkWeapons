package net.akashi.perk_weapons.Client.GUI;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.akashi.perk_weapons.Config.ModClientConfigs;
import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Util.ICoolDownItem;
import net.akashi.perk_weapons.Util.IDoubleLineCrosshairItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraft.client.gui.LayeredDraw;

@OnlyIn(Dist.CLIENT)
public class CoolDownIndicatorHud {
	public static final ResourceLocation HUD_TEXTURE = ResourceLocation.fromNamespaceAndPath(PerkWeapons.MODID, "textures/gui/hud.png");
	public static final LayeredDraw.Layer INDICATOR_BAR = (guiGraphics, partialTick) -> {
		//Check Indicators Enabled
		if (!ModClientConfigs.ENABLE_COOLDOWN_INDICATOR.get()) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		if (player == null)
			return;
		int screenWidth = mc.getWindow().getGuiScaledWidth();
		int screenHeight = mc.getWindow().getGuiScaledHeight();

		ItemStack stack = ItemStack.EMPTY;
		if (player.getMainHandItem().getItem() instanceof ICoolDownItem) {
			stack = player.getMainHandItem();
		} else if (player.getOffhandItem().getItem() instanceof ICoolDownItem) {
			stack = player.getOffhandItem();
		}

		if (!stack.isEmpty()) {
			PoseStack poseStack = guiGraphics.pose();
			poseStack.pushPose();

			ICoolDownItem coolDownItem = (ICoolDownItem) stack.getItem();
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.5F);
			RenderSystem.setShaderTexture(0, HUD_TEXTURE);


			int startX = Math.round((float) screenWidth / 2);
			if (coolDownItem instanceof IDoubleLineCrosshairItem && ModClientConfigs.ENABLE_CUSTOM_CROSSHAIR.get()) {
				startX += 32;
			} else {
				startX += 10;
			}
			int startY = Math.round((float) screenHeight / 2) - 10;

			byte drawHeight = (byte) (coolDownItem.getCoolDownProgress(player, stack) * 21);
			guiGraphics.blit(HUD_TEXTURE, startX, startY, 0, 7, 2, 21);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.8F);
			guiGraphics.blit(HUD_TEXTURE, startX, startY + 21 - drawHeight, 3, 7, 2, drawHeight);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			poseStack.popPose();
		}
	};
}
