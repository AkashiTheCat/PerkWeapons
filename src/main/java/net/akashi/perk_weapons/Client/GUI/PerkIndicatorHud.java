package net.akashi.perk_weapons.Client.GUI;

import com.mojang.blaze3d.systems.RenderSystem;
import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Util.IPerkItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import static net.akashi.perk_weapons.Bows.ForestKeeperItem.MAX_PERK_LEVEL;


public class PerkIndicatorHud {
	public static final ResourceLocation HUD_TEXTURE = new ResourceLocation(PerkWeapons.MODID, "textures/gui/hud.png");

	public static final IGuiOverlay INDICATOR_BAR = ((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;

		ItemStack stack = ItemStack.EMPTY;
		if (player.getMainHandItem().getItem() instanceof IPerkItem) {
			stack = player.getMainHandItem();
		} else if (player.getOffhandItem().getItem() instanceof IPerkItem) {
			stack = player.getOffhandItem();
		}

		if (stack != ItemStack.EMPTY) {
			IPerkItem perkItem = (IPerkItem) stack.getItem();
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.5F);
			RenderSystem.setShaderTexture(0, HUD_TEXTURE);

			int startX;
			int startY;
			int centerX = screenWidth / 2;
			int centerY = screenHeight / 2;


			startY = centerY + 10;
			if (MAX_PERK_LEVEL < 5) {
				startX = centerX - 3 * MAX_PERK_LEVEL;
			} else {
				startX = centerX - 15;
			}

			int renderedCount = 0;

			//Render Indicator BackGround
			for (int j = 0; renderedCount < MAX_PERK_LEVEL; j++) {
				for (int i = 0; i < 5 && renderedCount < MAX_PERK_LEVEL; i++) {
					guiGraphics.blit(HUD_TEXTURE, startX + i * 6, startY + j * 3, 0, 3, 5, 2);
					renderedCount++;
				}
			}

			//Render Indicator
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.8F);
			int n = perkItem.getIndicatorLength() / 5;
			int lastLen = perkItem.getIndicatorLength() % 5;

			renderedCount = 0;
			for (int j = 0; renderedCount < n; j++) {
				for (int i = 0; i < 5 && renderedCount < n; i++) {
					guiGraphics.blit(HUD_TEXTURE, startX + i * 6, startY + j * 3, 0, 0, 5, 2);
					renderedCount++;
				}
			}
			guiGraphics.blit(HUD_TEXTURE, startX + (renderedCount % 5) * 6, startY + (renderedCount / 5) * 3, 0, 0, lastLen, 2);
		}
	});
}
