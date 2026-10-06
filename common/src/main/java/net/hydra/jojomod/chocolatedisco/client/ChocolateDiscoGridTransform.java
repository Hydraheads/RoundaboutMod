package net.hydra.jojomod.chocolatedisco.client;

import net.hydra.jojomod.chocolatedisco.network.ChocolateDiscoNetworking;
import net.zetalasis.networking.message.api.ModMessageEvents;


import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChocolateDiscoGridTransform {

    private static final float POSITION_SMOOTHING = 0.35F;

    private static final float ROTATION_SMOOTHING = 0.35F;

    private static final Map<UUID, Transform> TRANSFORMS =
            new HashMap<>();

    private static final Map<UUID, Boolean> LOCKED =
            new HashMap<>();


    public static Transform update(Player player) {

        UUID playerId =
                player.getUUID();

        Transform transform =
                TRANSFORMS.get(playerId);


        /*
         * =========================================================
         * AUTOMATIC UNLOCK
         * =========================================================
         */

        if (
                Boolean.TRUE.equals(
                        LOCKED.get(playerId)
                )
                        && transform != null
        ) {

            double dx =
                    player.getX()
                            - transform.x;

            double dz =
                    player.getZ()
                            - transform.z;

            double distanceSquared =
                    dx * dx
                            + dz * dz;


            /*
             * Stay locked while within 30 blocks.
             */
            if (
                    distanceSquared
                            <= 30.0D * 30.0D
            ) {

                return transform;
            }


            /*
             * Leaving the 30-block locked-grid range
             * automatically unlocks the grid.
             */
            LOCKED.put(
                    playerId,
                    false
            );


            ChocolateDiscoSelectionState
                    .clearSelectedCells();

            ChocolateDiscoSelectionState.setSelectedCell(
                    3,
                    6
            );

            /*
             * Tell the server that the grid has
             * automatically unlocked.
             */
            ModMessageEvents.sendToServer(
                    ChocolateDiscoNetworking.GRID_LOCK_STATE,
                    false,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    player.getYRot()
            );
        }


        /*
         * =========================================================
         * NORMAL TRANSFORM UPDATE
         * =========================================================
         */

        double targetX =
                player.blockPosition().getX()
                        + 0.5D;

        double targetY =
                player.blockPosition().getY();

        double targetZ =
                player.blockPosition().getZ()
                        + 0.5D;

        float targetYaw =
                player.getDirection().toYRot();


        /*
         * Create the transform the first time.
         */
        if (transform == null) {

            transform =
                    new Transform(
                            targetX,
                            targetY,
                            targetZ,
                            targetYaw
                    );

            TRANSFORMS.put(
                    playerId,
                    transform
            );

            return transform;
        }


        /*
         * Smooth position.
         */
        transform.x =
                smooth(
                        transform.x,
                        targetX,
                        POSITION_SMOOTHING
                );

        transform.y =
                smooth(
                        transform.y,
                        targetY,
                        POSITION_SMOOTHING
                );

        transform.z =
                smooth(
                        transform.z,
                        targetZ,
                        POSITION_SMOOTHING
                );


        /*
         * Smooth rotation.
         */
        float yawDifference =
                wrapDegrees(
                        targetYaw
                                - transform.yaw
                );

        transform.yaw +=
                yawDifference
                        * ROTATION_SMOOTHING;

        transform.yaw =
                wrapDegrees(
                        transform.yaw
                );


        return transform;
    }


    public static Transform get(
            Player player
    ) {

        return TRANSFORMS.get(
                player.getUUID()
        );
    }


    private static double smooth(
            double current,
            double target,
            float amount
    ) {

        return current
                + (target - current)
                * amount;
    }


    private static float wrapDegrees(
            float degrees
    ) {

        while (
                degrees >= 180.0F
        ) {

            degrees -= 360.0F;
        }


        while (
                degrees < -180.0F
        ) {

            degrees += 360.0F;
        }


        return degrees;
    }


    public static void toggleLock(
            Player player
    ) {

        UUID playerId =
                player.getUUID();

        boolean locked =
                Boolean.TRUE.equals(
                        LOCKED.get(playerId)
                );

        LOCKED.put(
                playerId,
                !locked
        );
    }


    public static boolean isLocked(
            Player player
    ) {

        return Boolean.TRUE.equals(
                LOCKED.get(
                        player.getUUID()
                )
        );
    }


    public static class Transform {

        public double x;
        public double y;
        public double z;

        public float yaw;


        private Transform(
                double x,
                double y,
                double z,
                float yaw
        ) {

            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
        }
    }
}