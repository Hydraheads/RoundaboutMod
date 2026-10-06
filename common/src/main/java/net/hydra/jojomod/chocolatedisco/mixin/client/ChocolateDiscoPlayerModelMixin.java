package net.hydra.jojomod.chocolatedisco.mixin.client;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.hydra.jojomod.chocolatedisco.ChocolateDiscoEntity;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoAnimationState;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(PlayerModel.class)
public class ChocolateDiscoPlayerModelMixin {

    private static final Map<UUID, Float> animationStartTimes = new HashMap<>();

    private static final float ANIMATION_LENGTH = 2.0F;
    private static final float RETURN_LENGTH = 0.3F;

    @Inject(method = "setupAnim", at = @At("TAIL"))
    private void chocolateDiscoAnimation(
            LivingEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch,
            CallbackInfo ci
    ) {
        if (!(entity instanceof AbstractClientPlayer player)) {
            return;
        }

        ChocolateDiscoEntity stand = findChocolateDisco(player);

        if (!ChocolateDiscoAnimationState.isActive(player.getUUID())
                && (stand == null || stand.getAnimation() != 1)) {
            animationStartTimes.remove(player.getUUID());
            return;
        }

        UUID playerId = player.getUUID();

        if (!animationStartTimes.containsKey(playerId)) {
            animationStartTimes.put(playerId, ageInTicks);
        }

        float elapsed =
                (ageInTicks - animationStartTimes.get(playerId)) / 20.0F;

        // Animation hasn't started yet
        if (elapsed < 0.0F) {
            return;
        }

        // Animation finished
        if (elapsed > ANIMATION_LENGTH + RETURN_LENGTH) {
            if (stand != null) {
                stand.setAnimation((byte) 0);
            }

            animationStartTimes.remove(playerId);
            return;
        }
        PlayerModel<?> playerModel =
                (PlayerModel<?>) (Object) this;

        HumanoidModel<?> humanoidModel = playerModel;
        if (elapsed > ANIMATION_LENGTH) {

            float progress =
                    smoothStep(
                            (elapsed - ANIMATION_LENGTH) / RETURN_LENGTH
                    );

            humanoidModel.leftArm.xRot +=
                    -(float) Math.toRadians(
                            lerp(41.842F, 0.0F, progress)
                    );

            humanoidModel.leftArm.yRot +=
                    -(float) Math.toRadians(
                            lerp(-36.4176F, 0.0F, progress)
                    );

            humanoidModel.leftArm.zRot +=
                    -(float) Math.toRadians(
                            lerp(1.2654F, 0.0F, progress)
                    );

            playerModel.leftSleeve.copyFrom(
                    playerModel.leftArm
            );

            return;
        }

        // ============================================================
        // LEFT ARM
        // ============================================================

        float leftX;
        float leftY;
        float leftZ;

        if (elapsed <= 0.75F) {
            float progress = elapsed / 0.75F;

            leftX = lerp(0.0F, 41.842F, progress);
            leftY = lerp(0.0F, -36.4176F, progress);
            leftZ = lerp(0.0F, 1.2654F, progress);
        } else {
            leftX = 41.842F;
            leftY = -36.4176F;
            leftZ = 1.2654F;
        }

        humanoidModel.leftArm.xRot +=
                -(float) Math.toRadians(leftX);

        humanoidModel.leftArm.yRot +=
                -(float) Math.toRadians(leftY);

        humanoidModel.leftArm.zRot +=
                -(float) Math.toRadians(leftZ);

        // ============================================================
        // RIGHT ARM
        // Third-person FRONT view
        // Chocolate Disco is on the RIGHT side of the screen
        // ============================================================

        float rightX;
        float rightY;
        float rightZ;

        if (elapsed <= 0.7F) {

            // Reach across toward Chocolate Disco
            float progress = elapsed / 0.7F;

            rightX = lerp(0.0F, 70.0F, smoothStep(progress));
            rightY = lerp(0.0F, 75.0F, smoothStep(progress));
            rightZ = lerp(0.0F, -10.0F, smoothStep(progress));

        } else if (elapsed <= 1.0F) {

            // Raise the hand for the slap
            float progress = (elapsed - 0.7F) / 0.3F;

            rightX = lerp(70.0F, 130.0F, smoothStep(progress));
            rightY = lerp(75.0F, 65.0F, smoothStep(progress));
            rightZ = -10.0F;

        } else if (elapsed <= 1.3F) {

            // Pull the arm back down
            float progress = (elapsed - 1.0F) / 0.3F;

            rightX = lerp(130.0F, 60.0F, smoothStep(progress));
            rightY = lerp(65.0F, 70.0F, smoothStep(progress));
            rightZ = -10.0F;

        } else {

            // Return toward the final position briefly
            float progress = (elapsed - 1.3F) / 0.7F;

            rightX = lerp(60.0F, 0.0F, smoothStep(progress));
            rightY = lerp(70.0F, 0.0F, smoothStep(progress));
            rightZ = lerp(-10.0F, 0.0F, smoothStep(progress));
        }

        humanoidModel.rightArm.xRot +=
                -(float) Math.toRadians(rightX);

        humanoidModel.rightArm.yRot +=
                -(float) Math.toRadians(rightY);

        humanoidModel.rightArm.zRot +=
        humanoidModel.rightArm.zRot +
                -(float) Math.toRadians(rightZ);

        // Keep sleeves aligned with the animated arms
        playerModel.leftSleeve.copyFrom(playerModel.leftArm);
        playerModel.rightSleeve.copyFrom(playerModel.rightArm);
    }

    private static float smoothStep(float progress) {
        progress = Math.max(0.0F, Math.min(1.0F, progress));
        return progress * progress * (3.0F - 2.0F * progress);
    }

    private static float lerp(
            float start,
            float end,
            float progress
    ) {
        return start + (end - start) * progress;
    }

    private static ChocolateDiscoEntity findChocolateDisco(
            AbstractClientPlayer player
    ) {
        for (Entity entity :
                player.level().getEntities(
                        player,
                        player.getBoundingBox().inflate(2.0D)
                )) {

            if (entity instanceof ChocolateDiscoEntity chocolateDisco
                    && chocolateDisco.getUser() == player) {

                return chocolateDisco;
            }
        }

        return null;
    }
}