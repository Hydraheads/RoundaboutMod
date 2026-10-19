package net.hydra.jojomod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersLyinEyes;
import net.hydra.jojomod.util.LyinEyesScanner;
import net.hydra.jojomod.util.LyinEyesScanner.BlockCategory;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class LyinEyesMapOverlay {
    private static final ResourceLocation MAP_ICONS = new ResourceLocation(Roundabout.MOD_ID, "textures/gui/nematode_map.png");

    private static final int RADIUS = 20;
    private static final int TEX_ICON_SIZE = 8;
    private static final int RENDER_BLOCK_SIZE = 5;

    // icon positions on the texture sheet
    private static final int ARROW_U = 10, ARROW_V = 0;
    private static final int SOLID_U = 0, SOLID_V = 0;
    private static final int LIQUID_U = 0, LIQUID_V = 8;

    public static void renderMap(GuiGraphics guiGraphics, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.level == null) return;

        if (((StandUser) player).roundabout$getStandPowers() instanceof PowersLyinEyes lyinEyes) {
            float opacity = lyinEyes.getMapOpacity();
            if (opacity <= 0.0F) return;

            guiGraphics.setColor(1.0F, 1.0F, 1.0F, opacity);
            try {
                // it's scanning time
                BlockCategory[][] mapData = LyinEyesScanner.scanFeetLevel(player, mc.level, RADIUS);

                // center the map on the screen.
                int screenWidth = mc.getWindow().getGuiScaledWidth();
                int screenHeight = mc.getWindow().getGuiScaledHeight();
                int mapDisplaySize = mapData.length * RENDER_BLOCK_SIZE;
                int startX = (screenWidth - mapDisplaySize) / 2;
                int startY = (screenHeight - mapDisplaySize) / 2;

                // place the player arrow in the center
                int centerX = startX + (RADIUS * RENDER_BLOCK_SIZE);
                int centerY = startY + (RADIUS * RENDER_BLOCK_SIZE);
                float rotAngle = player.getYRot() - 180.0f;

                guiGraphics.pose().pushPose();
                if (ConfigManager.getClientConfig().lyinEyesSettings.spinMapInsteadOfArrow) {
                    guiGraphics.pose().translate(centerX, centerY, 0);
                    guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(-rotAngle));
                    guiGraphics.pose().translate(-centerX, -centerY, 0);
                }

                // simple 2d arrow for loop, this is where the blocks get drawn
                for (int x = 0; x < mapData.length; x++) {
                    for (int z = 0; z < mapData[x].length; z++) {
                        BlockCategory category = mapData[x][z];
                        // air gets skipped
                        if (category == BlockCategory.AIR) continue;

                        int drawX = startX + (x * RENDER_BLOCK_SIZE);
                        int drawY = startY + (z * RENDER_BLOCK_SIZE);

                        if (category == BlockCategory.SOLID) {
                            guiGraphics.blit(MAP_ICONS, drawX, drawY, RENDER_BLOCK_SIZE, RENDER_BLOCK_SIZE, SOLID_U, SOLID_V, TEX_ICON_SIZE, TEX_ICON_SIZE, 256, 256);
                        } else if (category == BlockCategory.LIQUID) {
                            guiGraphics.blit(MAP_ICONS, drawX, drawY, RENDER_BLOCK_SIZE, RENDER_BLOCK_SIZE, LIQUID_U, LIQUID_V, TEX_ICON_SIZE, TEX_ICON_SIZE, 256, 256);
                        }
                    }
                }

                guiGraphics.pose().popPose();

                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(centerX + (RENDER_BLOCK_SIZE / 2.0f), centerY + (RENDER_BLOCK_SIZE / 2.0f), 0);

                // rotate the arrow based on where the player looks
                if (!ConfigManager.getClientConfig().lyinEyesSettings.spinMapInsteadOfArrow) {
                    guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(rotAngle));
                }

                // time to draw the arrow
                guiGraphics.blit(MAP_ICONS, -4, -6, ARROW_U, ARROW_V, 8, 12, 256, 256);

                guiGraphics.pose().popPose();
            } finally {
                guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.disableBlend();
            }
        }
    }
}