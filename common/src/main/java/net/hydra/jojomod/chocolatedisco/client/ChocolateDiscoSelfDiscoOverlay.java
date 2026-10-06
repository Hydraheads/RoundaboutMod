package net.hydra.jojomod.chocolatedisco.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import net.hydra.jojomod.chocolatedisco.ChocolateDiscoEntity;

public class ChocolateDiscoSelfDiscoOverlay {

    /*
     * ================================================================
     * SKIN COLORS
     * ================================================================
     *
     * These match the Chocolate Disco world-grid colors.
     *
     * 0 = Default
     * 1 = Black & White
     * 2 = P-Cubed
     * 3 = Battleship
     * 4 = Disco Ball
     * 5 = Checkers
     * 6 = Tablet
     * 7 = Neopolitan
     * 8 = Chocolate
     */

    private static final int[] SKIN_COLORS = {
            0xFFD4A72C, // 0 - Default
            0xFFFFFFFF, // 1 - Black & White
            0xFF5AC4ED, // 2 - P-Cubed
            0xFF617BA3, // 3 - Battleship
            0xFF000000, // 4 - Disco Ball
            0xFFC8000D, // 5 - Checkers
            0xFF56C401, // 6 - Tablet
            0xFFDEADE5, // 7 - Neopolitan
            0xFF7F4E1E  // 8 - Chocolate
    };

    /*
     * Dark outline around the grid.
     */
    private static final int OUTLINE = 0xFF140D05;

    /*
     * ================================================================
     * TIMING
     * ================================================================
     */

    private static final long FORMATION_TIME_MS = 2000L;
    private static final long TOTAL_TIME_MS = 3000L;

    /*
     * ================================================================
     * LINE SETTINGS
     * ================================================================
     */

    private static final int LINE_THICKNESS = 4;
    private static final int OUTLINE_THICKNESS = 8;

    /*
     * Animation start time.
     */
    private static long startTime = -1L;


    /*
     * ================================================================
     * REGISTER
     * ================================================================
     */

    public static void register() {
        // Platform client event hooks call render(...).
    }


    /*
     * ================================================================
     * START
     * ================================================================
     */

    public static void start() {

        startTime = System.currentTimeMillis();
    }


    /*
     * ================================================================
     * RENDER
     * ================================================================
     */

    public static void render(
            GuiGraphics guiGraphics,
            float tickDelta
    ) {

        if (startTime < 0L) {
            return;
        }

        long elapsed =
                System.currentTimeMillis()
                        - startTime;

        /*
         * Entire Self Disco animation is finished.
         */

        if (elapsed >= TOTAL_TIME_MS) {

            startTime = -1L;

            return;
        }

        Minecraft minecraft =
                Minecraft.getInstance();

        /*
         * Only render in first person.
         */

        if (!minecraft.options
                .getCameraType()
                .isFirstPerson()) {

            return;
        }

        Player player =
                minecraft.player;

        if (player == null) {
            return;
        }

        /*
         * ============================================================
         * GET SKIN COLOR
         * ============================================================
         *
         * Use the same Chocolate Disco skin that the player currently
         * has equipped.
         */

        int skinColor =
                getSkinColor(player);


        /*
         * ============================================================
         * SCREEN SIZE
         * ============================================================
         */

        int screenWidth =
                minecraft.getWindow()
                        .getGuiScaledWidth();

        int screenHeight =
                minecraft.getWindow()
                        .getGuiScaledHeight();

        int left = 0;
        int top = 0;
        int right = screenWidth;
        int bottom = screenHeight;


        /*
         * ============================================================
         * FORMATION PROGRESS
         * ============================================================
         *
         * 0.0 = nothing visible
         * 1.0 = entire grid visible
         */

        float formationProgress;

        if (elapsed <= 0L) {

            formationProgress = 0.0F;

        } else if (elapsed >= FORMATION_TIME_MS) {

            formationProgress = 1.0F;

        } else {

            formationProgress =
                    (float) elapsed
                            / (float) FORMATION_TIME_MS;
        }

        formationProgress =
                smoothStep(
                        formationProgress
                );


        /*
         * ============================================================
         * BOTTOM -> TOP REVEAL
         * ============================================================
         */

        int visibleBottom =
                bottom;

        int visibleTop =
                bottom
                        - (int) (
                        (bottom - top)
                                * formationProgress
                );


        /*
         * ============================================================
         * GRID POSITION
         * ============================================================
         */

        int vertical1 =
                left
                        + (right - left) / 3;

        int vertical2 =
                left
                        + ((right - left) * 2) / 3;

        int horizontal1 =
                top
                        + (bottom - top) / 3;

        int horizontal2 =
                top
                        + ((bottom - top) * 2) / 3;


        /*
         * ============================================================
         * DRAW VERTICAL LINES
         * ============================================================
         */

        drawVerticalLine(
                guiGraphics,
                vertical1,
                visibleTop,
                visibleBottom,
                skinColor
        );

        drawVerticalLine(
                guiGraphics,
                vertical2,
                visibleTop,
                visibleBottom,
                skinColor
        );


        /*
         * ============================================================
         * DRAW HORIZONTAL LINES
         * ============================================================
         */

        if (horizontal1 >= visibleTop) {

            drawHorizontalLine(
                    guiGraphics,
                    horizontal1,
                    left,
                    right,
                    skinColor
            );
        }

        if (horizontal2 >= visibleTop) {

            drawHorizontalLine(
                    guiGraphics,
                    horizontal2,
                    left,
                    right,
                    skinColor
            );
        }
    }


    /*
     * ================================================================
     * GET CURRENT SKIN COLOR
     * ================================================================
     */

    private static int getSkinColor(
            Player player
    ) {

        ChocolateDiscoEntity disco =
                findChocolateDisco(player);

        if (disco == null) {
            return SKIN_COLORS[0];
        }

        int skin =
                disco.getSkin();

        if (skin < 0 || skin >= SKIN_COLORS.length) {
            skin = 0;
        }

        return SKIN_COLORS[skin];
    }


    /*
     * ================================================================
     * FIND CHOCOLATE DISCO
     * ================================================================
     */

    private static ChocolateDiscoEntity findChocolateDisco(
            Player player
    ) {

        for (
                Entity entity :
                player.level().getEntities(
                        player,
                        player.getBoundingBox()
                                .inflate(2.0D)
                )
        ) {

            if (
                    entity instanceof ChocolateDiscoEntity
                            chocolateDisco
                            && chocolateDisco.getUser()
                            == player
            ) {

                return chocolateDisco;
            }
        }

        return null;
    }


    /*
     * ================================================================
     * VERTICAL LINE
     * ================================================================
     */

    private static void drawVerticalLine(
            GuiGraphics guiGraphics,
            int x,
            int top,
            int bottom,
            int skinColor
    ) {

        /*
         * Dark outline.
         */

        int outlinePadding =
                (OUTLINE_THICKNESS - LINE_THICKNESS) / 2;

        guiGraphics.fill(
                x - outlinePadding,
                top,
                x + LINE_THICKNESS + outlinePadding,
                bottom,
                OUTLINE
        );

        /*
         * Skin-colored center.
         */

        guiGraphics.fill(
                x,
                top,
                x + LINE_THICKNESS,
                bottom,
                skinColor
        );
    }


    /*
     * ================================================================
     * HORIZONTAL LINE
     * ================================================================
     */

    private static void drawHorizontalLine(
            GuiGraphics guiGraphics,
            int y,
            int left,
            int right,
            int skinColor
    ) {

        /*
         * Dark outline.
         */

        int outlinePadding =
                (OUTLINE_THICKNESS - LINE_THICKNESS) / 2;

        guiGraphics.fill(
                left,
                y - outlinePadding,
                right,
                y + LINE_THICKNESS + outlinePadding,
                OUTLINE
        );

        /*
         * Skin-colored center.
         */

        guiGraphics.fill(
                left,
                y,
                right,
                y + LINE_THICKNESS,
                skinColor
        );
    }


    /*
     * ================================================================
     * SMOOTH STEP
     * ================================================================
     */

    private static float smoothStep(
            float progress
    ) {

        progress =
                Math.max(
                        0.0F,
                        Math.min(
                                1.0F,
                                progress
                        )
                );

        return progress * progress *
                (3.0F - 2.0F * progress);
    }
}