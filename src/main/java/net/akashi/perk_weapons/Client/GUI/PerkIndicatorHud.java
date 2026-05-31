package net.akashi.perk_weapons.Client.GUI;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.akashi.perk_weapons.Config.ModClientConfigs;
import net.akashi.perk_weapons.PerkWeapons;
import net.akashi.perk_weapons.Registry.ModItems;
import net.akashi.perk_weapons.Util.IPerkItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraft.client.gui.LayeredDraw;

@OnlyIn(Dist.CLIENT)
public class PerkIndicatorHud {
	public static final ResourceLocation HUD_TEXTURE = ResourceLocation.fromNamespaceAndPath(PerkWeapons.MODID, "textures/gui/hud.png");

	public static final LayeredDraw.Layer INDICATOR_BAR = (guiGraphics, partialTick) -> {
		if (!ModClientConfigs.ENABLE_PERK_INDICATOR.get()) {
			return;
		}

		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		if (player == null) {
			return;
		}

		int screenWidth = mc.getWindow().getGuiScaledWidth();
		int screenHeight = mc.getWindow().getGuiScaledHeight();

		ItemStack stack = ItemStack.EMPTY;
		if (player.getMainHandItem().getItem() instanceof IPerkItem) {
			stack = player.getMainHandItem();
		} else if (player.getOffhandItem().getItem() instanceof IPerkItem) {
			stack = player.getOffhandItem();
		}

		if (stack.is(ModItems.NETHER_GUIDE.get()) && !ModClientConfigs.ENABLE_MODE_INDICATOR_ON_NETHER_GUIDE.get()) {
			return;
		}

		if (stack.isEmpty()) {
			return;
		}

		PoseStack poseStack = guiGraphics.pose();
		poseStack.pushPose();

		IPerkItem perkItem = (IPerkItem) stack.getItem();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.5F);
		RenderSystem.setShaderTexture(0, HUD_TEXTURE);

		int centerX = Math.round((float) screenWidth / 2);
		int centerY = Math.round((float) screenHeight / 2);
		byte maxPerkLevel = perkItem.getMaxPerkLevel();
		int startY = centerY + 20;
		int startX = maxPerkLevel < 5 ? centerX - 3 * maxPerkLevel : centerX - 15;
		if (DoubleLineCrossHair.isVanillaCrosshairDisabled) {
			startX++;
		}

		int renderedCount = 0;
		for (int j = 0; renderedCount < maxPerkLevel; j++) {
			for (int i = 0; i < 5 && renderedCount < maxPerkLevel; i++) {
				guiGraphics.blit(HUD_TEXTURE, startX + i * 6, startY + j * 3, 0, 3, 5, 2);
				renderedCount++;
			}
		}

		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.8F);
		float perkLevel = perkItem.getPerkLevel(player, stack);
		int fullBars = (int) Math.floor(perkLevel);
		int lastLen = Math.round((perkLevel - fullBars) * 5);

		renderedCount = 0;
		for (int j = 0; renderedCount < fullBars; j++) {
			for (int i = 0; i < 5 && renderedCount < fullBars; i++) {
				guiGraphics.blit(HUD_TEXTURE, startX + i * 6, startY + j * 3, 0, 0, 5, 2);
				renderedCount++;
			}
		}
		if (lastLen > 0) {
			guiGraphics.blit(HUD_TEXTURE, startX + (renderedCount % 5) * 6,
					startY + (renderedCount / 5) * 3, 0, 0, lastLen, 2);
		}
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		poseStack.popPose();
	};
}
