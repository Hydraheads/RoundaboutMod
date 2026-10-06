package net.hydra.jojomod.chocolatedisco.client;

import java.util.UUID;

public class ChocolateDiscoAnimationState {

    /*
     * ================================================================
     * NORMAL CHOCOLATE DISCO SUMMON ANIMATION
     * ================================================================
     */

    private static UUID activePlayer;
    private static long startTime = -1L;

    private static final long ANIMATION_LENGTH_MS = 1400L;

    /*
     * ================================================================
     * SELF DISCO / SHIFT+V ANIMATION
     * ================================================================
     */

    private static UUID selfDiscoPlayer;
    private static long selfDiscoStartTime = -1L;

    private static final long SELF_DISCO_LENGTH_MS = 3000L;

    /*
     * ================================================================
     * NORMAL SUMMON ANIMATION
     * ================================================================
     */

    public static void start(UUID playerUUID) {
        activePlayer = playerUUID;
        startTime = System.currentTimeMillis();
    }

    public static boolean isActive(UUID playerUUID) {

        if (
                activePlayer == null
                        || !activePlayer.equals(playerUUID)
        ) {
            return false;
        }

        if (
                startTime < 0L
                        || System.currentTimeMillis() - startTime
                        >= ANIMATION_LENGTH_MS
        ) {

            activePlayer = null;
            startTime = -1L;

            return false;
        }

        return true;
    }

    public static void stop(UUID playerUUID) {

        if (
                activePlayer != null
                        && activePlayer.equals(playerUUID)
        ) {

            activePlayer = null;
            startTime = -1L;
        }
    }

    /*
     * ================================================================
     * SUMMON ANIMATION PROGRESS
     * ================================================================
     */

    public static float getProgress(UUID playerUUID) {

        if (
                activePlayer == null
                        || !activePlayer.equals(playerUUID)
                        || startTime < 0L
        ) {

            return 0.0F;
        }

        long elapsed =
                System.currentTimeMillis()
                        - startTime;

        final long OPEN_LENGTH_MS = 233L;
        final long CLOSE_START_MS = 1167L;

        if (elapsed <= 0L) {
            return 0.0F;
        }

        /*
         * Opening
         */
        if (elapsed < OPEN_LENGTH_MS) {

            float progress =
                    (float) elapsed
                            / (float) OPEN_LENGTH_MS;

            return progress
                    * progress
                    * (3.0F - 2.0F * progress);
        }

        /*
         * Fully open
         */
        if (elapsed < CLOSE_START_MS) {
            return 1.0F;
        }

        /*
         * Closing
         */
        if (elapsed < ANIMATION_LENGTH_MS) {

            float progress =
                    (float) (
                            elapsed
                                    - CLOSE_START_MS
                    )
                            / (float) (
                            ANIMATION_LENGTH_MS
                                    - CLOSE_START_MS
                    );

            progress =
                    Math.max(
                            0.0F,
                            Math.min(
                                    1.0F,
                                    progress
                            )
                    );

            return 1.0F
                    - (
                    progress
                            * progress
                            * (3.0F - 2.0F * progress)
            );
        }

        return 0.0F;
    }

    /*
     * ================================================================
     * SELF DISCO
     * ================================================================
     */

    public static void startSelfDisco(UUID playerUUID) {

        selfDiscoPlayer = playerUUID;
        selfDiscoStartTime = System.currentTimeMillis();
    }

    public static boolean isSelfDiscoActive(UUID playerUUID) {

        if (
                selfDiscoPlayer == null
                        || !selfDiscoPlayer.equals(playerUUID)
        ) {

            return false;
        }

        if (
                selfDiscoStartTime < 0L
                        || System.currentTimeMillis()
                        - selfDiscoStartTime
                        >= SELF_DISCO_LENGTH_MS
        ) {

            selfDiscoPlayer = null;
            selfDiscoStartTime = -1L;

            return false;
        }

        return true;
    }

    public static void stopSelfDisco(UUID playerUUID) {

        if (
                selfDiscoPlayer != null
                        && selfDiscoPlayer.equals(playerUUID)
        ) {

            selfDiscoPlayer = null;
            selfDiscoStartTime = -1L;
        }
    }
}