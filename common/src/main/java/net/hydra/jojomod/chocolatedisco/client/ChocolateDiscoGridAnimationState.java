package net.hydra.jojomod.chocolatedisco.client;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
 * ================================================================
 * GRID ROLL ANIMATION STATE
 * ================================================================
 *
 * Tracks, per player, how "unrolled" their Chocolate Disco grid
 * currently is - 0.0 means fully rolled up (nothing drawn),
 * 1.0 means fully laid out.
 *
 * The grid renderer calls getRevealProgress() once per player,
 * every frame, with whatever the CURRENT summon state is. This
 * class detects the moment that state flips (summon/desummon)
 * and animates smoothly from wherever the progress currently
 * sits toward the new target - so interrupting a roll-out with
 * an immediate desummon reverses cleanly instead of snapping.
 */
public class ChocolateDiscoGridAnimationState {

    private static final long ROLL_DURATION_MS = 500L;

    private static final Map<UUID, State> STATES =
            new HashMap<>();

    private static class State {

        boolean summoned = false;

        long transitionStartTime = -1L;

        float startProgress = 0.0F;

        float targetProgress = 0.0F;
    }

    /*
     * ================================================================
     * REVEAL PROGRESS
     * ================================================================
     */

    public static float getRevealProgress(
            UUID playerId,
            boolean summonedNow
    ) {

        State state =
                STATES.computeIfAbsent(
                        playerId,
                        id -> new State()
                );

        if (summonedNow != state.summoned) {

            state.startProgress =
                    currentProgress(state);

            state.targetProgress =
                    summonedNow
                            ? 1.0F
                            : 0.0F;

            state.transitionStartTime =
                    System.currentTimeMillis();

            state.summoned =
                    summonedNow;
        }

        return currentProgress(state);
    }

    /*
     * ================================================================
     * SHOULD RENDER
     * ================================================================
     *
     * True while summoned, and also for the remainder of a
     * roll-up after desummoning so the closing animation can
     * finish playing out.
     */

    public static boolean shouldRender(
            UUID playerId
    ) {

        State state =
                STATES.get(playerId);

        if (state == null) {
            return false;
        }

        return state.summoned
                || currentProgress(state) > 0.001F;
    }

    /*
     * ================================================================
     * CURRENT PROGRESS
     * ================================================================
     */

    private static float currentProgress(
            State state
    ) {

        if (state.transitionStartTime < 0L) {
            return state.targetProgress;
        }

        long elapsed =
                System.currentTimeMillis()
                        - state.transitionStartTime;

        if (elapsed >= ROLL_DURATION_MS) {
            return state.targetProgress;
        }

        float t =
                elapsed / (float) ROLL_DURATION_MS;

        return lerp(
                state.startProgress,
                state.targetProgress,
                smoothStep(t)
        );
    }

    /*
     * ================================================================
     * MATH
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

        return progress * progress
                * (3.0F - 2.0F * progress);
    }

    private static float lerp(
            float start,
            float end,
            float progress
    ) {

        return start
                + (end - start) * progress;
    }
}