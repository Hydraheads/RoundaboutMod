package net.hydra.jojomod.mixin.star_platinum;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GameRenderer.class)
public class ScopeGameRenderer {

    /**Scoping influences FOV and bobbing while walking*/

    @Inject(method = "tickFov()V", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$tickfov(CallbackInfo ci) {
        LivingEntity player = Minecraft.getInstance().player;
        if (player != null){
            StandPowers SP = ((StandUser)player).roundabout$getStandPowers();
            if (SP.scopeLevel > 0) {
                ci.cancel();

                float f = 1.0F;
                this.oldFov = this.fov;

                AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer) this.minecraft.getCameraEntity();
                if (abstractclientplayer != null && Minecraft.getInstance().options.getCameraType().isFirstPerson() && abstractclientplayer.isScoping()) {
                    if (SP.scopeLevel == 1) {
                        f = 0.05F;
                    } else if (SP.scopeLevel == 2) {
                        f = 0.0225F;
                    } else {
                        f = 0.01F;
                    }
                } else if (abstractclientplayer != null && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                    if (SP.scopeLevel == 1) {
                        f = 0.3F;
                    } else if (SP.scopeLevel == 2) {
                        f = 0.125F;
                    } else {
                        f = 0.05F;
                    }
                }

                this.fov += (f - this.fov) * 0.5F;
                if (this.fov > 1.5F) {
                    this.fov = 1.5F;
                }

                if (this.fov < 0.01F) {
                    this.fov = 0.01F;
                }
            }
        }
    }
    @Inject(method = "bobView", at = @At(value = "HEAD"), cancellable = true)
    private void RoundaboutBobView(PoseStack $$0, float $$1, CallbackInfo ci) {
        LivingEntity player = Minecraft.getInstance().player;
        if (player != null) {
            StandPowers SP = ((StandUser) player).roundabout$getStandPowers();
            if (SP.scopeLevel > 0 && (!((StandUser)player).roundabout$isParallelRunning())) {
                ci.cancel();
            }
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    @Shadow
    @Final
    Minecraft minecraft;
    @Shadow private float fov;
    @Shadow private float oldFov;
}
