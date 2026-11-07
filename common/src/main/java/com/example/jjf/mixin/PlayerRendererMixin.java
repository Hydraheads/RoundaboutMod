package com.example.jjf.mixin;

import com.example.jjf.client.JJFPlayerWallPose;
import com.example.jjf.zero_gravity.ZeroGravityAccess;
import com.example.jjf.zero_gravity.ZeroGravityState;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.Direction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {

    @Inject(
            method = "setupRotations(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V",
            at = @At("TAIL")
    )
    private void jjf$ceilingRotation(
            AbstractClientPlayer player,
            PoseStack poseStack,
            float ageInTicks,
            float bodyYaw,
            float partialTick,
            CallbackInfo ci
    ) {
        if (!(player instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state =
                access.jjf$getZeroGravityState();

        if (!state.isSuctioned()) {
            return;
        }

        if (state.getSuctionSurface() != Direction.UP) {
            return;
        }


        int flatDirection =
                JJFPlayerWallPose.getFlatCeilingDirection(player);


        if (flatDirection == 0) {
            return;
        }


        double ceilingHeight =
                JJFPlayerWallPose.getCeilingRenderHeight(player);


        double lengthCenter = -0.92D;

        lengthCenter -= flatDirection * 0.50D;

        poseStack.translate(
                0.0D,
                ceilingHeight,
                lengthCenter
        );



        poseStack.mulPose(
                Axis.XP.rotationDegrees(90.0F)
        );
    }
}