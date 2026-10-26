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

import java.util.List;

public class LyinEyesMapOverlay {
    private static final ResourceLocation MAP_ICONS = new ResourceLocation(Roundabout.MOD_ID, "textures/gui/nematode_map.png");

    private static final int RADIUS = 20;
    private static final int TEX_ICON_SIZE = 8;
    private static final int RENDER_BLOCK_SIZE = 5;
    private static final int TEX_HIGHLIGHTER_ICON_SIZE = 12;

    // icon positions on the texture sheet
    private static final int ARROW_U = 10, ARROW_V = 0;
    private static final int SOLID_U = 0, SOLID_V = 0;
    private static final int LIQUID_U = 0, LIQUID_V = 8;
    private static final int HOSTILE_OUTLINE_U = 56, HOSTILE_OUTLINE_V = 24;
    private static final int NEUTRAL_OUTLINE_U = 56, NEUTRAL_OUTLINE_V = 12;
    private static final int PASSIVE_OUTLINE_U = 56, PASSIVE_OUTLINE_V = 0;
    private static final int CHEST_U = 32, CHEST_V = 0;
    private static final int PLUS_U = 51, PLUS_V = 7;
    private static final int MINUS_U = 51, MINUS_V = 19;
    private static final int BASE_MOB_U = 20, BASE_MOB_V = 0;

    public static void renderMap(GuiGraphics guiGraphics, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.level == null) return;

        if (((StandUser) player).roundabout$getStandPowers() instanceof PowersLyinEyes lyinEyes) {
            float opacity = lyinEyes.getMapOpacity();
            if (opacity <= 0.0F) return;
            opacity *= ConfigManager.getClientConfig().lyinEyesSettings.mapOpacity;

            guiGraphics.setColor(1.0F, 1.0F, 1.0F, opacity);
            try {
                // it's scanning time
                BlockCategory[][] mapData = LyinEyesScanner.scanFeetLevel(player, mc.level, RADIUS);
                List<LyinEyesScanner.TrackedTarget> targets = LyinEyesScanner.scan3DTargets(player, mc.level, RADIUS);

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

                // for the mobs and chests
                for (LyinEyesScanner.TrackedTarget target : targets) {
                    float exactDrawX = centerX + (target.relX * RENDER_BLOCK_SIZE);
                    float exactDrawY = centerY + (target.relZ * RENDER_BLOCK_SIZE);

                    // base centered position
                    int drawX = (int) (exactDrawX - (7 / 2.0f));
                    int drawY = (int) (exactDrawY - (7 / 2.0f));

                    // chests get weird with float, so i need to add 3 to center them over the block properly
                    int targetDrawX = (target.type == LyinEyesScanner.TargetType.CONTAINER) ? drawX + 3 : drawX;
                    int targetDrawY = (target.type == LyinEyesScanner.TargetType.CONTAINER) ? drawY + 3 : drawY;

                    // first is the container drawing, next is the entity drawing
                    if (target.type == LyinEyesScanner.TargetType.CONTAINER) {
                        guiGraphics.blit(MAP_ICONS, targetDrawX, targetDrawY, 9, 9, CHEST_U, CHEST_V, TEX_HIGHLIGHTER_ICON_SIZE, TEX_HIGHLIGHTER_ICON_SIZE, 256, 256);
                    } else {
                        // neutral base drawing
                        guiGraphics.blit(MAP_ICONS, targetDrawX, targetDrawY, 9, 9, BASE_MOB_U, BASE_MOB_V, TEX_HIGHLIGHTER_ICON_SIZE, TEX_HIGHLIGHTER_ICON_SIZE, 256, 256);

                        // outline thing
                        int outlineU;
                        int outlineV;

                        if (target.type == LyinEyesScanner.TargetType.HOSTILE) {
                            // red outline
                            outlineU = HOSTILE_OUTLINE_U;
                            outlineV = HOSTILE_OUTLINE_V;
                        } else if (target.type == LyinEyesScanner.TargetType.NEUTRAL) {
                            // yellow outline
                            outlineU = NEUTRAL_OUTLINE_U;
                            outlineV = NEUTRAL_OUTLINE_V;
                        } else {
                            // green outline
                            outlineU = PASSIVE_OUTLINE_U;
                            outlineV = PASSIVE_OUTLINE_V;
                        }

                        // put the outline over the normal thing
                        guiGraphics.blit(MAP_ICONS, targetDrawX, targetDrawY, 9, 9, outlineU, outlineV, TEX_HIGHLIGHTER_ICON_SIZE, TEX_HIGHLIGHTER_ICON_SIZE, 256, 256);
                    }

                    // draw the + or -
                    if (target.isAbove) {
                        guiGraphics.blit(MAP_ICONS, targetDrawX + 1, targetDrawY + 1, 7, 7, PLUS_U, PLUS_V, 5, 5, 256, 256);
                    } else if (target.isBelow) {
                        guiGraphics.blit(MAP_ICONS, targetDrawX + 1, targetDrawY + 1, 7, 7, MINUS_U, MINUS_V, 5, 5, 256, 256);
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