package net.akashi.perk_weapons.Client.GUI;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.akashi.perk_weapons.Bows.BaseBowItem;
import net.akashi.perk_weapons.Config.ModClientConfigs;
import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Util.ICoolDownItem;
import net.akashi.perk_weapons.Util.IPerkItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

@OnlyIn(Dist.CLIENT)
public class CoolDownIndicatorHud {
	public static final ResourceLocation HUD_TEXTURE = new ResourceLocation(PerkWeapons.MODID, "textures/gui/hud.png");
	public static final IGuiOverlay INDICATOR_BAR = ((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
		//Check Indicators Enabled
		if (!ModClientConfigs.ENABLE_COOLDOWN_INDICATOR.get()) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		Level level = mc.level;

		ItemStack stack = ItemStack.EMPTY;
		if (player.getMainHandItem().getItem() instanceof ICoolDownItem) {
			stack = player.getMainHandItem();
		} else if (player.getOffhandItem().getItem() instanceof ICoolDownItem) {
			stack = player.getOffhandItem();
		}

		if (stack != ItemStack.EMPTY) {
			PoseStack poseStack = RenderSystem.getModelViewStack();
			poseStack.pushPose();

			ICoolDownItem coolDownItem = (ICoolDownItem) stack.getItem();
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.5F);
			RenderSystem.setShaderTexture(0, HUD_TEXTURE);


			int startX = Math.round((float) screenWidth / 2);
			if (coolDownItem instanceof BaseBowItem && ModClientConfigs.ENABLE_CUSTOM_CROSSHAIR.get()) {
				startX += 32;
			} else {
				startX += 10;
			}
			int startY = Math.round((float) screenHeight / 2) - 10;

			byte drawHeight = (byte) (coolDownItem.getCoolDownProgress(level, player) * 21);
			guiGraphics.blit(HUD_TEXTURE, startX, startY, 0, 7, 2, 21);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.8F);
			guiGraphics.blit(HUD_TEXTURE, startX, startY, 3, 7, 2, drawHeight);
			poseStack.popPose();
		}
	});
}
