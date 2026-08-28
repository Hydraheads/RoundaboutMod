package net.hydra.jojomod.client;

import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersSilverChariot;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class SilverChariotClient {
    private static boolean cameraActive;
    private static int pendingTicks;
    private static boolean lookInitialized;
    private static float cameraYaw;
    private static float cameraPitch;
    private static boolean miningActive;
    private static BlockPos miningPos;
    private static CameraType previousCameraType;

    private SilverChariotClient() {
    }

    public static void enter() {
        cameraActive = true;
        pendingTicks = 20;
        lookInitialized = false;
        previousCameraType = null;
    }

    public static void enforceCamera(Entity stand) {
        cameraActive = true;
        pendingTicks = 0;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && minecraft.getCameraEntity() != minecraft.player) {
            minecraft.setCameraEntity(minecraft.player);
        }
        ClientUtil.setCameraEntity(stand);
        applyLook(stand);
    }

    public static void turnCamera(Entity stand, double yawDelta, double pitchDelta) {
        initializeLook(stand);
        cameraYaw += (float) (yawDelta * 0.15D);
        cameraPitch = Mth.clamp(cameraPitch + (float) (pitchDelta * 0.15D), -90.0F, 90.0F);
        applyLook(stand);
    }

    public static void applyLook(Entity stand) {
        if (stand == null) return;
        initializeLook(stand);
        stand.setYRot(cameraYaw);
        stand.setXRot(cameraPitch);
        stand.yRotO = cameraYaw;
        stand.xRotO = cameraPitch;
        if (stand instanceof LivingEntity living) {
            living.setYHeadRot(cameraYaw);
        }
    }

    public static void rotateLookForGravityChange(Entity stand, Direction oldGravity, Direction newGravity) {
        if (stand == null || oldGravity == newGravity) return;
        initializeLook(stand);
        Vec3 oldWorldLook = RotationUtil.vecPlayerToWorld(
                RotationUtil.rotToVec(cameraYaw, cameraPitch), oldGravity);
        Quaternionf gravityChange = RotationUtil.getRotationBetween(oldGravity, newGravity);
        Vector3f rotatedLook = new Vector3f(
                (float) oldWorldLook.x, (float) oldWorldLook.y, (float) oldWorldLook.z);
        rotatedLook.rotate(gravityChange);
        Vec3 newLocalLook = RotationUtil.vecWorldToPlayer(
                new Vec3(rotatedLook), newGravity);
        Vec2 rotation = RotationUtil.vecToRot(newLocalLook);
        cameraYaw = rotation.x;
        cameraPitch = Mth.clamp(rotation.y, -90.0F, 90.0F);
    }

    public static boolean hasCameraLook() {
        return cameraActive && lookInitialized;
    }
    public static float getCameraYaw() {
        return cameraYaw;
    }

    public static float getCameraPitch() {
        return cameraPitch;
    }

    private static void initializeLook(Entity stand) {
        if (lookInitialized || stand == null) return;
        cameraYaw = stand.getYRot();
        cameraPitch = stand.getXRot();
        lookInitialized = true;
    }

    public static void exit() {
        if (!cameraActive) return;
        stopMining();
        cameraActive = false;
        pendingTicks = 0;
        lookInitialized = false;
        ClientUtil.setCameraEntity(null);
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) minecraft.setCameraEntity(minecraft.player);
        restoreCameraType(minecraft);
    }

    public static void clear() {
        stopMining();
        cameraActive = false;
        pendingTicks = 0;
        lookInitialized = false;
        ClientUtil.setCameraEntity(null);
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) minecraft.setCameraEntity(minecraft.player);
        restoreCameraType(minecraft);
    }

    public static void exitIfInactive() {
        if (pendingTicks > 0) {
            pendingTicks--;
            return;
        }
        exit();
    }

    public static boolean handleMining(Minecraft minecraft, boolean attackHeld) {
        PowersSilverChariot powers = getControlPowers(minecraft);
        if (powers == null) return false;
        updateMining(minecraft, powers, attackHeld);
        return true;
    }

    public static boolean tryMining(Minecraft minecraft) {
        PowersSilverChariot powers = getControlPowers(minecraft);
        if (powers == null) return false;
        boolean blockTargeted = hasMiningTarget(minecraft, powers);
        updateMining(minecraft, powers, true);
        return blockTargeted;
    }

    public static boolean rejectsBodyMiningTarget(Minecraft minecraft, BlockPos pos) {
        PowersSilverChariot powers = getControlPowers(minecraft);
        if (powers == null) return false;
        BlockHitResult hit = getMiningHit(minecraft, powers);
        return hit == null || !hit.getBlockPos().equals(pos);
    }

    private static PowersSilverChariot getControlPowers(Minecraft minecraft) {
        if (minecraft.player == null || minecraft.level == null || minecraft.gameMode == null) {
            return null;
        }
        if (((StandUser) minecraft.player).roundabout$getStandPowers() instanceof PowersSilverChariot powers && powers.isPiloting()) {
            return powers;
        }
        return null;
    }

    private static boolean hasMiningTarget(Minecraft minecraft, PowersSilverChariot powers) {
        if (((StandUser) minecraft.player).roundabout$isGuardInput()) return false;
        BlockHitResult hit = getMiningHit(minecraft, powers);
        return hit != null && !minecraft.level.getBlockState(hit.getBlockPos()).isAir();
    }

    private static BlockHitResult getMiningHit(Minecraft minecraft, PowersSilverChariot powers) {
        LivingEntity stand = powers.getPilotingStand();
        HitResult result = stand == null ? null
                : stand.pick(PowersSilverChariot.SILVER_CHARIOT_STANDARD_PUNCH_RANGE, 0.0F, false);
        if (!(result instanceof BlockHitResult blockHit) || result.getType() != HitResult.Type.BLOCK
                || entityIsCloser(stand, blockHit)) return null;
        return blockHit;
    }

    private static boolean entityIsCloser(LivingEntity stand, BlockHitResult blockHit) {
        EntityHitResult entityHit = MainUtil.rayCastEntityHitResult(
                stand, PowersSilverChariot.SILVER_CHARIOT_STANDARD_PUNCH_RANGE);
        if (entityHit == null || entityHit.getEntity() == Minecraft.getInstance().player) return false;
        Vec3 eye = stand.getEyePosition(0.0F);
        return eye.distanceToSqr(entityHit.getLocation()) <= eye.distanceToSqr(blockHit.getLocation());
    }

    private static boolean updateMining(Minecraft minecraft, PowersSilverChariot powers, boolean attackHeld) {
        if (!attackHeld) {
            stopMining();
            if (powers.getActivePower() == PowerIndex.MINING) {
                powers.tryPower(PowerIndex.NONE, true);
                powers.tryPowerPacket(PowerIndex.NONE);
            }
            return false;
        }
        BlockHitResult hit = getMiningHit(minecraft, powers);
        if (hit == null || ((StandUser) minecraft.player).roundabout$isGuardInput()) {
            if (miningActive || powers.getActivePower() == PowerIndex.MINING) {
                stopMining();
                powers.tryPower(PowerIndex.NONE, true);
                powers.tryPowerPacket(PowerIndex.NONE);
            }
            return false;
        }
        BlockPos pos = hit.getBlockPos();
        if (minecraft.level.getBlockState(pos).isAir()) return false;
        if (!miningActive || !pos.equals(miningPos)) {
            if (miningActive) stopMining();
            if (!powers.canUseMiningStand()
                    || (powers.getActivePower() != PowerIndex.NONE
                    && powers.getActivePower() != PowerIndex.MINING
                    && powers.getAttackTimeDuring() != -1)) return false;
            if (powers.getActivePower() != PowerIndex.MINING) {
                ((StandUser) minecraft.player).roundabout$tryPower(PowerIndex.MINING, true);
                powers.tryPowerPacket(PowerIndex.MINING);
            }
            miningActive = minecraft.gameMode.startDestroyBlock(pos, hit.getDirection());
            miningPos = miningActive ? pos.immutable() : null;
        } else if (minecraft.gameMode.continueDestroyBlock(pos, hit.getDirection())) {
            minecraft.particleEngine.crack(pos, hit.getDirection());
        }
        return true;
    }

    private static void stopMining() {
        Minecraft minecraft = Minecraft.getInstance();
        if (miningActive && minecraft.gameMode != null) minecraft.gameMode.stopDestroyBlock();
        miningActive = false;
        miningPos = null;
    }

    private static void restoreCameraType(Minecraft minecraft) {
        if (previousCameraType != null) {
            minecraft.options.setCameraType(previousCameraType);
        }
        previousCameraType = null;
    }

    public static boolean shouldBeVisibleToUser(Player player, Entity targetEntity, float tick) {
        Vec3 eyeP = player.getEyePosition(tick);
        Vec3 lookD = player.getViewVector(tick).normalize();

        Vec3 entityPosition = targetEntity.getBoundingBox()
                .getCenter();

        Vec3 directionToEntity = entityPosition
                .subtract(eyeP)
                .normalize();

        if (lookD.dot(directionToEntity) < 0.0F) {
            return false;
        }

        BlockHitResult hit = player.level().clip(
                new ClipContext(
                        eyeP,
                        entityPosition,
                        ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE,
                        player
                )
        );

        if (hit.getType() != HitResult.Type.MISS) {
            return false;
        }

        return true;
    }
}
