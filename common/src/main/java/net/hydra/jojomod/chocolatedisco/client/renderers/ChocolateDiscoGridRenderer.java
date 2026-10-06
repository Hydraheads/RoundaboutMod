package net.hydra.jojomod.chocolatedisco.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.hydra.jojomod.chocolatedisco.ChocolateDiscoGrid;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoGridAnimationState;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoGridTransform;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoSelectionState;
import net.hydra.jojomod.chocolatedisco.network.ChocolateDiscoNetworking;


import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChocolateDiscoGridRenderer {

    /*
     * =====================================================
     * COLORS
     * =====================================================
     *
     * Chocolate Disco skin colors:
     *
     * 0 = Default       #D4A72C
     * 1 = Black & White #FFFFFF
     * 2 = P-Cubed       #5AC4ED
     * 3 = Battleship    #617BA3
     * 4 = Disco Ball    #000000
     * 5 = Checkers      #C8000D
     * 6 = Tablet        #56C401
     * 7 = Neopolitan    #DEADE5
     * 8 = Chocolate     #7F4E1E
     */

    private static final float[][] GRID_COLORS = {
            { 212.0F / 255.0F, 167.0F / 255.0F,  44.0F / 255.0F }, // 0
            { 255.0F / 255.0F, 255.0F / 255.0F, 255.0F / 255.0F }, // 1
            {  90.0F / 255.0F, 196.0F / 255.0F, 237.0F / 255.0F }, // 2
            {  97.0F / 255.0F, 123.0F / 255.0F, 163.0F / 255.0F }, // 3
            {   0.0F / 255.0F,   0.0F / 255.0F,   0.0F / 255.0F }, // 4
            { 200.0F / 255.0F,   0.0F / 255.0F,  13.0F / 255.0F }, // 5
            {  86.0F / 255.0F, 196.0F / 255.0F,   1.0F / 255.0F }, // 6
            { 222.0F / 255.0F, 173.0F / 255.0F, 229.0F / 255.0F }, // 7
            { 127.0F / 255.0F,  78.0F / 255.0F,  30.0F / 255.0F }  // 8
    };


    private static final Map<UUID, Integer> LAST_GRID_SKINS =
            new HashMap<>();

    /*
     * =====================================================
     * OUTLINE
     * =====================================================
     */

    private static final float OUTLINE_RED = 0.08F;
    private static final float OUTLINE_GREEN = 0.05F;
    private static final float OUTLINE_BLUE = 0.02F;
    private static final float OUTLINE_ALPHA = 0.95F;

    /*
     * =====================================================
     * SELECTED TILE
     * =====================================================
     */

    private static final float SELECTED_ALPHA = 0.30F;

    /*
     * =====================================================
     * GRID SETTINGS
     * =====================================================
     */

    private static final double GRID_HEIGHT = 0.03D;

    private static final double OUTLINE_WIDTH = 0.075D;
    private static final double COLOR_WIDTH = 0.035D;

    private static final double SELECTED_TILE_WIDTH = 0.9D;
    private static final double SELECTED_TILE_HEIGHT = 3.0D;

    public static void register() {
        // Platform client event hooks call render(...).
    }

    public static void render(
            PoseStack poseStack,
            ClientLevel level
    ) {
        if (level == null || poseStack == null) {
            return;
        }

        /*
         * =====================================================
         * RENDER STATE
         * =====================================================
         */

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();

        RenderSystem.setShader(
                GameRenderer::getPositionColorShader
        );

        Tesselator tesselator =
                Tesselator.getInstance();

        BufferBuilder buffer =
                tesselator.getBuilder();

        buffer.begin(
                VertexFormat.Mode.QUADS,
                DefaultVertexFormat.POSITION_COLOR
        );

        for (Player player : level.players()) {

            UUID playerId =
                    player.getUUID();

            if (ChocolateDiscoNetworking.isRedirectActive(
                    playerId
            )) {
                continue;
            }

            boolean summonedNow =
                    hasChocolateDisco(player);

            float revealProgress =
                    ChocolateDiscoGridAnimationState.getRevealProgress(
                            playerId,
                            summonedNow
                    );

            /*
             * Once the animation is completely finished,
             * remove the cached skin.
             */

            if (!ChocolateDiscoGridAnimationState.shouldRender(
                    playerId
            )) {

                LAST_GRID_SKINS.remove(
                        playerId
                );

                continue;
            }

            /*
             * =================================================
             * UPDATE SKIN CACHE
             * =================================================
             *
             * Only update the cache while the entity actually
             * exists.
             *
             * During unsummon the entity can disappear while
             * the grid animation is still running, so the last
             * skin remains available.
             */

            if (summonedNow) {

                int skin =
                        getChocolateDiscoSkin(player);

                LAST_GRID_SKINS.put(
                        playerId,
                        skin
                );
            }

            /*
             * Shared transform.
             */

            ChocolateDiscoGridTransform.Transform transform =
                    ChocolateDiscoGridTransform.update(
                            player
                    );

            renderGrid(
                    poseStack,
                    buffer,
                    player,
                    transform,
                    revealProgress
            );
        }

        tesselator.end();

        /*
         * =====================================================
         * RESTORE MINECRAFT RENDER STATE
         * =====================================================
         */

        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private static void renderGrid(
            PoseStack poseStack,
            BufferBuilder buffer,
            Player player,
            ChocolateDiscoGridTransform.Transform transform,
            float revealProgress
    ) {

        Minecraft minecraft =
                Minecraft.getInstance();

        double cameraX =
                minecraft.gameRenderer
                        .getMainCamera()
                        .getPosition()
                        .x;

        double cameraY =
                minecraft.gameRenderer
                        .getMainCamera()
                        .getPosition()
                        .y;

        double cameraZ =
                minecraft.gameRenderer
                        .getMainCamera()
                        .getPosition()
                        .z;

        poseStack.pushPose();

        /*
         * =====================================================
         * SHARED GRID TRANSFORM
         * =====================================================
         */

        poseStack.translate(
                transform.x - cameraX,
                transform.y + GRID_HEIGHT - cameraY,
                transform.z - cameraZ
        );

        poseStack.mulPose(
                com.mojang.math.Axis.YP.rotationDegrees(
                        -transform.yaw
                )
        );

        /*
         * =====================================================
         * GRID SIZE
         * =====================================================
         */

        int gridSize =
                ChocolateDiscoGridTransform.isLocked(player)
                        ? ChocolateDiscoGrid.LARGE_GRID_SIZE
                        : ChocolateDiscoGrid.GRID_SIZE;

        int center =
                gridSize / 2;

        /*
         * =====================================================
         * GET SKIN COLOR
         * =====================================================
         */

        UUID playerId =
                player.getUUID();

        int skin =
                LAST_GRID_SKINS.getOrDefault(
                        playerId,
                        0
                );

        if (skin < 0 || skin >= GRID_COLORS.length) {
            skin = 0;
        }

        float gridRed =
                GRID_COLORS[skin][0];

        float gridGreen =
                GRID_COLORS[skin][1];

        float gridBlue =
                GRID_COLORS[skin][2];

        /*
         * =====================================================
         * SELECTED TILE INDICATOR
         * =====================================================
         *
         * The indicator now uses the exact same RGB color
         * as the active Chocolate Disco skin.
         */

        if (!ChocolateDiscoSelectionState.isBuildingMode()
                && revealProgress >= 0.999F) {

            int selectedColumn =
                    ChocolateDiscoSelectionState
                            .getSelectedColumn();

            int selectedRow =
                    ChocolateDiscoSelectionState
                            .getSelectedRow();

            double selectedSide =
                    center - selectedColumn;

            double selectedForward =
                    3.0D
                            + (gridSize - 1 - selectedRow);

            drawSelectedTile(
                    poseStack,
                    buffer,
                    selectedSide,
                    selectedForward,
                    gridRed,
                    gridGreen,
                    gridBlue
            );
        }

        /*
         * =====================================================
         * GRID BOUNDS
         * =====================================================
         */

        double halfGrid =
                gridSize / 2.0D;

        double startSide =
                -halfGrid;

        double endSide =
                halfGrid;

        double startForward =
                2.5D;

        double endForward =
                startForward + gridSize;

        /*
         * =====================================================
         * ROLL-OUT / ROLL-UP CLIPPING
         * =====================================================
         */

        double revealedDepth =
                startForward
                        + (revealProgress * gridSize);

        /*
         * =====================================================
         * HORIZONTAL LINES
         * =====================================================
         */

        for (int row = 0; row <= gridSize; row++) {

            double forwardOffset =
                    startForward + row;

            if (forwardOffset > revealedDepth) {
                continue;
            }

            drawHorizontalLine(
                    poseStack,
                    buffer,
                    startSide,
                    forwardOffset,
                    endSide,
                    forwardOffset,
                    gridRed,
                    gridGreen,
                    gridBlue
            );
        }

        /*
         * =====================================================
         * VERTICAL LINES
         * =====================================================
         */

        for (int column = 0; column <= gridSize; column++) {

            double sideOffset =
                    startSide + column;

            double startZ =
                    startForward;

            double endZ =
                    Math.min(
                            endForward,
                            revealedDepth
                    );

            if (endZ <= startZ) {
                continue;
            }

            drawVerticalLine(
                    poseStack,
                    buffer,
                    sideOffset,
                    startZ,
                    sideOffset,
                    endZ,
                    gridRed,
                    gridGreen,
                    gridBlue
            );
        }

        poseStack.popPose();
    }

    /*
     * =====================================================
     * SELECTED TILE
     * =====================================================
     */

    private static void drawSelectedTile(
            PoseStack poseStack,
            BufferBuilder buffer,
            double centerX,
            double centerZ,
            float red,
            float green,
            float blue
    ) {

        PoseStack.Pose pose =
                poseStack.last();

        double halfWidth =
                SELECTED_TILE_WIDTH / 2.0D;

        double minX =
                centerX - halfWidth;

        double maxX =
                centerX + halfWidth;

        double minZ =
                centerZ - halfWidth;

        double maxZ =
                centerZ + halfWidth;

        /*
         * =====================================================
         * FRONT FACE
         * =====================================================
         */

        drawSelectedTileFace(
                pose,
                buffer,
                minX,
                minZ,
                maxX,
                minZ,
                red,
                green,
                blue
        );

        /*
         * =====================================================
         * BACK FACE
         * =====================================================
         */

        drawSelectedTileFace(
                pose,
                buffer,
                maxX,
                maxZ,
                minX,
                maxZ,
                red,
                green,
                blue
        );
    }

    private static void drawSelectedTileFace(
            PoseStack.Pose pose,
            BufferBuilder buffer,
            double x1,
            double z1,
            double x2,
            double z2,
            float red,
            float green,
            float blue
    ) {

        buffer.vertex(
                pose.pose(),
                (float) x1,
                0.01F,
                (float) z1
        ).color(
                red,
                green,
                blue,
                SELECTED_ALPHA
        ).endVertex();

        buffer.vertex(
                pose.pose(),
                (float) x2,
                0.01F,
                (float) z2
        ).color(
                red,
                green,
                blue,
                SELECTED_ALPHA
        ).endVertex();

        buffer.vertex(
                pose.pose(),
                (float) x2,
                (float) SELECTED_TILE_HEIGHT,
                (float) z2
        ).color(
                red,
                green,
                blue,
                SELECTED_ALPHA
        ).endVertex();

        buffer.vertex(
                pose.pose(),
                (float) x1,
                (float) SELECTED_TILE_HEIGHT,
                (float) z1
        ).color(
                red,
                green,
                blue,
                SELECTED_ALPHA
        ).endVertex();
    }

    /*
     * =====================================================
     * HORIZONTAL LINE
     * =====================================================
     */

    private static void drawHorizontalLine(
            PoseStack poseStack,
            BufferBuilder buffer,
            double startX,
            double startZ,
            double endX,
            double endZ,
            float gridRed,
            float gridGreen,
            float gridBlue
    ) {

        double halfOutline =
                OUTLINE_WIDTH / 2.0D;

        drawHorizontalQuad(
                poseStack,
                buffer,
                startX,
                startZ,
                endX,
                endZ,
                halfOutline,
                OUTLINE_RED,
                OUTLINE_GREEN,
                OUTLINE_BLUE,
                OUTLINE_ALPHA
        );

        double halfColor =
                COLOR_WIDTH / 2.0D;

        drawHorizontalQuad(
                poseStack,
                buffer,
                startX,
                startZ,
                endX,
                endZ,
                halfColor,
                gridRed,
                gridGreen,
                gridBlue,
                1.0F
        );
    }

    /*
     * =====================================================
     * HORIZONTAL QUAD
     * =====================================================
     */

    private static void drawHorizontalQuad(
            PoseStack poseStack,
            BufferBuilder buffer,
            double startX,
            double startZ,
            double endX,
            double endZ,
            double halfWidth,
            float red,
            float green,
            float blue,
            float alpha
    ) {

        PoseStack.Pose pose =
                poseStack.last();

        float minZ =
                (float) (startZ - halfWidth);

        float maxZ =
                (float) (startZ + halfWidth);

        buffer.vertex(
                pose.pose(),
                (float) startX,
                0.0F,
                minZ
        ).color(
                red,
                green,
                blue,
                alpha
        ).endVertex();

        buffer.vertex(
                pose.pose(),
                (float) endX,
                0.0F,
                minZ
        ).color(
                red,
                green,
                blue,
                alpha
        ).endVertex();

        buffer.vertex(
                pose.pose(),
                (float) endX,
                0.0F,
                maxZ
        ).color(
                red,
                green,
                blue,
                alpha
        ).endVertex();

        buffer.vertex(
                pose.pose(),
                (float) startX,
                0.0F,
                maxZ
        ).color(
                red,
                green,
                blue,
                alpha
        ).endVertex();
    }

    /*
     * =====================================================
     * VERTICAL LINE
     * =====================================================
     */

    private static void drawVerticalLine(
            PoseStack poseStack,
            BufferBuilder buffer,
            double startX,
            double startZ,
            double endX,
            double endZ,
            float gridRed,
            float gridGreen,
            float gridBlue
    ) {

        double halfOutline =
                OUTLINE_WIDTH / 2.0D;

        drawVerticalQuad(
                poseStack,
                buffer,
                startX,
                startZ,
                endX,
                endZ,
                halfOutline,
                OUTLINE_RED,
                OUTLINE_GREEN,
                OUTLINE_BLUE,
                OUTLINE_ALPHA
        );

        double halfColor =
                COLOR_WIDTH / 2.0D;

        drawVerticalQuad(
                poseStack,
                buffer,
                startX,
                startZ,
                endX,
                endZ,
                halfColor,
                gridRed,
                gridGreen,
                gridBlue,
                1.0F
        );
    }

    /*
     * =====================================================
     * VERTICAL QUAD
     * =====================================================
     */

    private static void drawVerticalQuad(
            PoseStack poseStack,
            BufferBuilder buffer,
            double startX,
            double startZ,
            double endX,
            double endZ,
            double halfWidth,
            float red,
            float green,
            float blue,
            float alpha
    ) {

        PoseStack.Pose pose =
                poseStack.last();

        float minX =
                (float) (startX - halfWidth);

        float maxX =
                (float) (startX + halfWidth);

        buffer.vertex(
                pose.pose(),
                minX,
                0.0F,
                (float) startZ
        ).color(
                red,
                green,
                blue,
                alpha
        ).endVertex();

        buffer.vertex(
                pose.pose(),
                maxX,
                0.0F,
                (float) startZ
        ).color(
                red,
                green,
                blue,
                alpha
        ).endVertex();

        buffer.vertex(
                pose.pose(),
                maxX,
                0.0F,
                (float) endZ
        ).color(
                red,
                green,
                blue,
                alpha
        ).endVertex();

        buffer.vertex(
                pose.pose(),
                minX,
                0.0F,
                (float) endZ
        ).color(
                red,
                green,
                blue,
                alpha
        ).endVertex();
    }

    /*
     * =====================================================
     * GET CHOCOLATE DISCO SKIN
     * =====================================================
     */

    private static int getChocolateDiscoSkin(
            Player player
    ) {

        net.hydra.jojomod.chocolatedisco.ChocolateDiscoEntity disco =
                findChocolateDisco(player);

        if (disco == null) {
            return 0;
        }

        int skin =
                disco.getSkin();

        if (skin < 0 || skin >= GRID_COLORS.length) {
            return 0;
        }

        return skin;
    }

    /*
     * =====================================================
     * FIND CHOCOLATE DISCO
     * =====================================================
     */

    private static net.hydra.jojomod.chocolatedisco.ChocolateDiscoEntity findChocolateDisco(
            Player player
    ) {

        for (Entity entity :
                player.level().getEntities(
                        player,
                        player.getBoundingBox().inflate(2.0D)
                )) {

            if (entity instanceof net.hydra.jojomod.chocolatedisco.ChocolateDiscoEntity chocolateDisco
                    && chocolateDisco.getUser() == player) {

                return chocolateDisco;
            }
        }

        return null;
    }

    /*
     * =====================================================
     * CHECK FOR CHOCOLATE DISCO
     * =====================================================
     */

    private static boolean hasChocolateDisco(
            Player player
    ) {

        return findChocolateDisco(player) != null;
    }
}