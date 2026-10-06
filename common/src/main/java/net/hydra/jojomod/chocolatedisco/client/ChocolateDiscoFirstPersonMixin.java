package net.hydra.jojomod.chocolatedisco.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.chocolatedisco.client.ChocolateDiscoAnimationState;
import net.hydra.jojomod.chocolatedisco.client.renderers.ChocolateDiscoFeatureRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.hydra.jojomod.chocolatedisco.ChocolateDiscoEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(ItemInHandRenderer.class)
public abstract class ChocolateDiscoFirstPersonMixin {

    private static final Map<UUID, Integer> animationStartTicks =
            new HashMap<>();

    private static final float ANIMATION_LENGTH = 1.4F;
    private static final float FIRST_PERSON_OPEN_LENGTH = 0.49F;

    /*
     * ================================================================
     * CHOCOLATE DISCO SKINS
     * ================================================================
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

    private static final ResourceLocation[] TEXTURES = {
            texture("chocolate_disco_default"),
            texture("chocolate_disco_black_white"),
            texture("chocolate_disco_p_cubed"),
            texture("chocolate_disco_battleship"),
            texture("chocolate_disco_ball"),
            texture("chocolate_disco_checkers"),
            texture("chocolate_disco_tablet"),
            texture("chocolate_disco_neopolitan"),
            texture("chocolate_disco_chocolate")
    };

    private static final ResourceLocation[] SLIM_TEXTURES = {
            texture("chocolate_disco_default_slim"),
            texture("chocolate_disco_black_white_slim"),
            texture("chocolate_disco_p_cubed_slim"),
            texture("chocolate_disco_battleship_slim"),
            texture("chocolate_disco_ball_slim"),
            texture("chocolate_disco_checkers_slim"),
            texture("chocolate_disco_tablet_slim"),
            texture("chocolate_disco_neopolitan_slim"),
            texture("chocolate_disco_chocolate_slim")
    };

    private static ResourceLocation texture(String name) {
        return new ResourceLocation(
                "roundabout",
                "textures/entity/" + name + ".png"
        );
    }

    /*
     * ================================================================
     * RENDER CHOCOLATE DISCO + LEFT ARM
     * ================================================================
     */

    @Inject(
            method = "renderHandsWithItems",
            at = @At("HEAD")
    )
    private void chocolateDiscoRenderLeftArm(
            float tickDelta,
            PoseStack poseStack,
            MultiBufferSource.BufferSource buffer,
            LocalPlayer player,
            int packedLight,
            CallbackInfo ci
    ) {
        Minecraft minecraft = Minecraft.getInstance();

        if (!minecraft.options.getCameraType().isFirstPerson()) {
            return;
        }

        ChocolateDiscoEntity stand = findChocolateDisco(player);

        if (stand == null) {
            return;
        }

        if (!ChocolateDiscoAnimationState.isActive(player.getUUID())
                && stand.getAnimation() != 1) {
            return;
        }

        float elapsed = getAnimationTime(player);

        if (elapsed < 0.0F || elapsed > ANIMATION_LENGTH) {
            return;
        }

        poseStack.pushPose();

        applyLeftArmPose(
                poseStack,
                elapsed
        );

        if (ChocolateDiscoFeatureRenderer.model != null) {
            poseStack.pushPose();

            poseStack.translate(
                    -0.2F,
                    -0.3F,
                    -0.4F
            );

            poseStack.mulPose(
                    Axis.XP.rotationDegrees(270.0F)
            );

            poseStack.mulPose(
                    Axis.YP.rotationDegrees(180.0F)
            );

            poseStack.scale(
                    0.45F,
                    0.45F,
                    0.45F
            );

            ResourceLocation texture =
                    getChocolateDiscoTexture(
                            player,
                            stand
                    );

            VertexConsumer vertexConsumer =
                    buffer.getBuffer(
                            RenderType.entityCutoutNoCull(texture)
                    );

            float openProgress =
                    Math.max(
                            0.0F,
                            Math.min(
                                    1.0F,
                                    elapsed / FIRST_PERSON_OPEN_LENGTH
                            )
                    );

            ChocolateDiscoFeatureRenderer.model.setOpenProgress(
                    openProgress
            );

            ChocolateDiscoFeatureRenderer.model.renderToBuffer(
                    poseStack,
                    vertexConsumer,
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    1.0F,
                    1.0F,
                    1.0F,
                    1.0F
            );

            poseStack.popPose();
        }

        renderPlayerArm(
                poseStack,
                buffer,
                packedLight,
                0.0F,
                0.0F,
                HumanoidArm.LEFT
        );

        poseStack.popPose();
    }

    /*
     * ================================================================
     * HIDE HELD ITEM DURING ANIMATION
     * ================================================================
     */

    @Inject(
            method = "renderArmWithItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void chocolateDiscoRenderArmWithItem(
            AbstractClientPlayer player,
            float partialTick,
            float g,
            InteractionHand hand,
            float h,
            ItemStack itemStack,
            float attackProgress,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            CallbackInfo ci
    ) {
        Minecraft minecraft = Minecraft.getInstance();

        if (!minecraft.options.getCameraType().isFirstPerson()) {
            return;
        }

        if (!(player instanceof LocalPlayer localPlayer)) {
            return;
        }

        ChocolateDiscoEntity stand =
                findChocolateDisco(localPlayer);

        if (!ChocolateDiscoAnimationState.isActive(localPlayer.getUUID())
                && (stand == null || stand.getAnimation() != 1)) {
            return;
        }

        if (hand != InteractionHand.MAIN_HAND) {
            ci.cancel();
            return;
        }

        poseStack.pushPose();

        applyRightArmAnimation(
                poseStack,
                localPlayer
        );

        renderPlayerArm(
                poseStack,
                buffer,
                packedLight,
                0.0F,
                0.0F,
                localPlayer.getMainArm()
        );

        poseStack.popPose();

        ci.cancel();
    }

    /*
     * ================================================================
     * HIDE VANILLA LEFT ARM
     * ================================================================
     */

    @Inject(
            method = "renderPlayerArm",
            at = @At("HEAD"),
            cancellable = true
    )
    private void chocolateDiscoFirstPersonAnimation(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            float equipProgress,
            float attackProgress,
            HumanoidArm arm,
            CallbackInfo ci
    ) {
        Minecraft minecraft = Minecraft.getInstance();

        if (!minecraft.options.getCameraType().isFirstPerson()) {
            return;
        }

        LocalPlayer player = minecraft.player;

        if (player == null) {
            return;
        }

        ChocolateDiscoEntity stand =
                findChocolateDisco(player);

        if (!ChocolateDiscoAnimationState.isActive(player.getUUID())
                && (stand == null || stand.getAnimation() != 1)) {

            animationStartTicks.remove(
                    player.getUUID()
            );

            return;
        }

        if (arm == HumanoidArm.LEFT) {
            ci.cancel();
        }
    }

    /*
     * ================================================================
     * RIGHT ARM ANIMATION
     * ================================================================
     */

    private static void applyRightArmAnimation(
            PoseStack poseStack,
            LocalPlayer player
    ) {
        UUID playerId = player.getUUID();

        animationStartTicks.putIfAbsent(
                playerId,
                player.tickCount
        );

        float elapsed = getAnimationTime(player);

        if (elapsed > ANIMATION_LENGTH) {
            ChocolateDiscoEntity stand =
                    findChocolateDisco(player);

            if (stand != null) {
                stand.setAnimation((byte) 0);
            }

            animationStartTicks.remove(playerId);
            return;
        }

        applyRightArmPose(
                poseStack,
                player
        );
    }

    private static void applyRightArmPose(
            PoseStack poseStack,
            LocalPlayer player
    ) {
        float elapsed = getAnimationTime(player);

        if (elapsed > ANIMATION_LENGTH) {
            return;
        }

        poseStack.translate(
                0.35F,
                0.0F,
                0.90F
        );

        float rightX;
        float rightY;
        float rightZ;

        if (elapsed <= 0.49F) {

            float progress =
                    smoothStep(elapsed / 0.49F);

            rightX = lerp(0.0F, -30.0F, progress);
            rightY = lerp(0.0F, -35.0F, progress);
            rightZ = lerp(0.0F, -3.0F, progress);

        } else if (elapsed <= 0.665F) {

            float progress =
                    smoothStep(
                            (elapsed - 0.49F) / 0.175F
                    );

            rightX = lerp(-30.0F, -25.0F, progress);
            rightY = lerp(-35.0F, -50.0F, progress);
            rightZ = -3.0F;

        } else if (elapsed <= 0.805F) {

            float progress =
                    smoothStep(
                            (elapsed - 0.665F) / 0.14F
                    );

            rightX = lerp(-25.0F, -15.0F, progress);
            rightY = -50.0F;
            rightZ = -3.0F;

        } else {

            float progress =
                    smoothStep(
                            (elapsed - 0.805F) / 0.595F
                    );

            rightX = lerp(-15.0F, 0.0F, progress);
            rightY = lerp(-50.0F, 0.0F, progress);
            rightZ = lerp(-3.0F, 0.0F, progress);
        }

        poseStack.mulPose(
                Axis.XP.rotationDegrees(-rightX)
        );

        poseStack.mulPose(
                Axis.YP.rotationDegrees(-rightY)
        );

        poseStack.mulPose(
                Axis.ZP.rotationDegrees(-rightZ)
        );
    }

    /*
     * ================================================================
     * LEFT ARM ANIMATION
     * ================================================================
     */

    private static void applyLeftArmPose(
            PoseStack poseStack,
            float elapsed
    ) {
        float leftX;
        float leftY;
        float leftZ;

        if (elapsed <= 0.49F) {

            float progress =
                    smoothStep(elapsed / 0.49F);

            leftX = lerp(0.0F, 8.0F, progress);
            leftY = lerp(0.0F, 18.0F, progress);
            leftZ = lerp(0.0F, -2.0F, progress);

        } else if (elapsed <= 0.91F) {

            leftX = 8.0F;
            leftY = 18.0F;
            leftZ = -2.0F;

        } else {

            float progress =
                    smoothStep(
                            (elapsed - 0.91F) / 0.49F
                    );

            leftX = lerp(8.0F, 0.0F, progress);
            leftY = lerp(18.0F, 0.0F, progress);
            leftZ = lerp(-2.0F, 0.0F, progress);
        }

        poseStack.mulPose(
                Axis.XP.rotationDegrees(-leftX)
        );

        poseStack.mulPose(
                Axis.YP.rotationDegrees(-leftY)
        );

        poseStack.mulPose(
                Axis.ZP.rotationDegrees(-leftZ)
        );
    }

    /*
     * ================================================================
     * ANIMATION TIME
     * ================================================================
     */

    private static float getAnimationTime(
            LocalPlayer player
    ) {
        UUID playerId = player.getUUID();

        animationStartTicks.putIfAbsent(
                playerId,
                player.tickCount
        );

        float elapsed =
                player.tickCount
                        - animationStartTicks.get(playerId);

        elapsed +=
                Minecraft.getInstance().getFrameTime();

        return elapsed / 20.0F;
    }

    /*
     * ================================================================
     * MATH
     * ================================================================
     */

    private static float smoothStep(
            float progress
    ) {
        progress = Math.max(
                0.0F,
                Math.min(1.0F, progress)
        );

        return progress * progress *
                (3.0F - 2.0F * progress);
    }

    private static float lerp(
            float start,
            float end,
            float progress
    ) {
        return start + (end - start) * progress;
    }

    /*
     * ================================================================
     * TEXTURE
     * ================================================================
     */

    private static ResourceLocation getChocolateDiscoTexture(
            LocalPlayer player,
            ChocolateDiscoEntity stand
    ) {
        int skin = stand.getSkin();

        if (skin < 0 || skin >= TEXTURES.length) {
            skin = 0;
        }

        return player.getModelName().equals("slim")
                ? SLIM_TEXTURES[skin]
                : TEXTURES[skin];
    }

    /*
     * ================================================================
     * FIND CHOCOLATE DISCO
     * ================================================================
     */

    private static ChocolateDiscoEntity findChocolateDisco(
            LocalPlayer player
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

    /*
     * ================================================================
     * VANILLA ARM
     * ================================================================
     */

    @Shadow
    protected abstract void renderPlayerArm(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            float equipProgress,
            float attackProgress,
            HumanoidArm arm
    );
}
