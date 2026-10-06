package net.hydra.jojomod.chocolatedisco.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.hydra.jojomod.chocolatedisco.ChocolateDiscoGrid;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoGridAnimationState;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoGridTransform;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoSelectionState;
import net.hydra.jojomod.chocolatedisco.network.ChocolateDiscoNetworking;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChocolateDiscoGridLabelRenderer {

    private static final float LABEL_SCALE = 0.025F;

    /*
     * =====================================================
     * LABEL COLORS
     * =====================================================
     *
     * Exact skin colors:
     *
     * 0 = #D4A72C
     * 1 = #FFFFFF
     * 2 = #5AC4ED
     * 3 = #617BA3
     * 4 = #000000
     * 5 = #C8000D
     * 6 = #56C401
     * 7 = #DEADE5
     * 8 = #7F4E1E
     *
     * Stored as packed RGB integers because Font.drawInBatch
     * uses an integer color.
     */
    private static final int[] LABEL_COLORS = {
            0xD4A72C, // Skin 0 - Default
            0xFFFFFF, // Skin 1 - Black & White
            0x5AC4ED, // Skin 2 - P-Cubed
            0x617BA3, // Skin 3 - Battleship
            0x000000, // Skin 4 - Disco Ball
            0xC8000D, // Skin 5 - Checkers
            0x56C401, // Skin 6 - Tablet
            0xDEADE5, // Skin 7 - Neopolitan
            0x7F4E1E  // Skin 8 - Chocolate
    };

    /*
     * Very dark brown/black outline.
     */
    private static final int OUTLINE = 0x140D05;

    /*
     * Distance of the outline from the main text.
     *
     * Because the text is already scaled very small,
     * this value is intentionally tiny.
     */
    private static final float OUTLINE_OFFSET = 0.75F;

    /*
     * =====================================================
     * UNSUMMON SKIN CACHE
     * =====================================================
     *
     * The Chocolate Disco entity can disappear before the
     * grid/label unsummon animation has completed.
     *
     * This remembers the last skin so the labels don't
     * suddenly change back to gold during unsummon.
     */
    private static final Map<UUID, Integer> LAST_LABEL_SKINS =
            new HashMap<>();

    public static void register() {
        // Platform client event hooks call render(...).
    }

    public static void render(
            PoseStack poseStack,
            MultiBufferSource consumers,
            ClientLevel level
    ) {
        if (level == null || poseStack == null || consumers == null) {
            return;
        }

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

            /*
             * If the entire grid animation is finished,
             * remove the remembered skin.
             */
            if (!ChocolateDiscoGridAnimationState.shouldRender(
                    playerId
            )) {

                LAST_LABEL_SKINS.remove(
                        playerId
                );

                continue;
            }

            /*
             * Only update the cache when the entity actually
             * exists.
             *
             * During unsummon, summonedNow becomes false and
             * the previous skin remains cached.
             */
            if (summonedNow) {

                int skin =
                        getChocolateDiscoSkin(player);

                LAST_LABEL_SKINS.put(
                        playerId,
                        skin
                );
            }

            /*
             * IMPORTANT:
             *
             * Do NOT update the transform here.
             *
             * The grid renderer already updated it.
             *
             * We simply read the exact same transform.
             */
            ChocolateDiscoGridTransform.Transform transform =
                    ChocolateDiscoGridTransform.get(player);

            if (transform == null) {
                continue;
            }

            renderLabels(
                    poseStack,
                    consumers,
                    player,
                    transform
            );
        }
    }

    private static void renderLabels(
            PoseStack poseStack,
            MultiBufferSource consumers,
            Player player,
            ChocolateDiscoGridTransform.Transform transform
    ) {

        Minecraft minecraft =
                Minecraft.getInstance();

        EntityRenderDispatcher dispatcher =
                minecraft.getEntityRenderDispatcher();

        Font font =
                minecraft.font;

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
         * SAME TRANSFORM AS THE GRID
         * =====================================================
         */

        poseStack.translate(
                transform.x - cameraX,
                transform.y + 1.0D - cameraY,
                transform.z - cameraZ
        );

        poseStack.mulPose(
                Axis.YP.rotationDegrees(
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
         * GET LABEL COLOR
         * =====================================================
         */

        int skin =
                LAST_LABEL_SKINS.getOrDefault(
                        player.getUUID(),
                        0
                );

        if (skin < 0 || skin >= LABEL_COLORS.length) {
            skin = 0;
        }

        int labelColor =
                LABEL_COLORS[skin];

        /*
         * =====================================================
         * NUMBER LABELS
         * =====================================================
         *
         * Numbers run from 1 through the current grid size.
         */

        for (int column = 0; column < gridSize; column++) {

            /*
             * Keep the numbers aligned with the columns.
             *
             * 7x7:
             * column 0 -> -3
             * column 3 ->  0
             * column 6 -> +3
             *
             * 15x15:
             * column 0  -> -7
             * column 7  ->  0
             * column 14 -> +7
             */

            double sideOffset =
                    center - column;

            /*
             * Keep the numbers just beyond the far end
             * of the grid.
             *
             * 7x7  -> 10
             * 15x15 -> 18
             */

            double forwardOffset =
                    3.0D + gridSize;

            drawLabel(
                    poseStack,
                    consumers,
                    font,
                    Component.literal(
                            String.valueOf(
                                    column + 1
                            )
                    ),
                    sideOffset,
                    forwardOffset,
                    0.0D,
                    dispatcher,
                    labelColor
            );
        }

        /*
         * =====================================================
         * LETTER LABELS
         * =====================================================
         *
         * A = farthest
         * ...
         * last letter = nearest
         *
         * 7x7  -> A-G
         * 15x15 -> A-O
         */

        for (int row = 0; row < gridSize; row++) {

            /*
             * Keep the letters on the side of the grid.
             */

            double sideOffset =
                    gridSize == 7
                            ? 4.0D
                            : 8.0D;

            /*
             * Keep the same relationship to the grid.
             *
             * 7x7:
             * A = 9
             * B = 8
             * C = 7
             * D = 6
             * E = 5
             * F = 4
             * G = 3
             *
             * 15x15:
             * A = 17
             * B = 16
             * ...
             * O = 3
             */

            double forwardOffset =
                    3.0D
                            + (gridSize - 1 - row);

            drawLabel(
                    poseStack,
                    consumers,
                    font,
                    Component.literal(
                            String.valueOf(
                                    (char) ('A' + row)
                            )
                    ),
                    sideOffset,
                    forwardOffset,
                    0.0D,
                    dispatcher,
                    labelColor
            );
        }

        poseStack.popPose();
    }

    private static void drawLabel(
            PoseStack poseStack,
            MultiBufferSource consumers,
            Font font,
            Component text,
            double sideOffset,
            double forwardOffset,
            double yOffset,
            EntityRenderDispatcher dispatcher,
            int labelColor
    ) {

        poseStack.pushPose();

        poseStack.translate(
                sideOffset,
                yOffset,
                forwardOffset
        );

        poseStack.scale(
                -LABEL_SCALE,
                -LABEL_SCALE,
                LABEL_SCALE
        );

        float width =
                font.width(text);

        /*
         * =====================================================
         * DARK OUTLINE
         * =====================================================
         *
         * Four slightly offset copies create a clean outline
         * around the skin-colored text.
         */

        font.drawInBatch(
                text,
                -width / 2.0F - OUTLINE_OFFSET,
                -OUTLINE_OFFSET,
                OUTLINE,
                false,
                poseStack.last().pose(),
                consumers,
                Font.DisplayMode.NORMAL,
                0,
                15728880
        );

        font.drawInBatch(
                text,
                -width / 2.0F + OUTLINE_OFFSET,
                -OUTLINE_OFFSET,
                OUTLINE,
                false,
                poseStack.last().pose(),
                consumers,
                Font.DisplayMode.NORMAL,
                0,
                15728880
        );

        font.drawInBatch(
                text,
                -width / 2.0F - OUTLINE_OFFSET,
                OUTLINE_OFFSET,
                OUTLINE,
                false,
                poseStack.last().pose(),
                consumers,
                Font.DisplayMode.NORMAL,
                0,
                15728880
        );

        font.drawInBatch(
                text,
                -width / 2.0F + OUTLINE_OFFSET,
                OUTLINE_OFFSET,
                OUTLINE,
                false,
                poseStack.last().pose(),
                consumers,
                Font.DisplayMode.NORMAL,
                0,
                15728880
        );

        /*
         * =====================================================
         * SKIN-COLORED TEXT
         * =====================================================
         */

        font.drawInBatch(
                text,
                -width / 2.0F,
                0.0F,
                labelColor,
                false,
                poseStack.last().pose(),
                consumers,
                Font.DisplayMode.NORMAL,
                0,
                15728880
        );

        poseStack.popPose();
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

        if (skin < 0 || skin >= LABEL_COLORS.length) {
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