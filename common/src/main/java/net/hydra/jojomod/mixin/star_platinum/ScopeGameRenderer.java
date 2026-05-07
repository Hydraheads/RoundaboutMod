package net.hydra.jojomod.mixin.star_platinum;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.item.FirearmItem;
import net.hydra.jojomod.item.JackalRifleItem;
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
            FatePowers fp = ((IFatePlayer)player).rdbt$getFatePowers();
            float zoomMod = fp.zoomMod();


            float zoomMod2 = zoomMod - 1;
            zoomMod2*=Minecraft.getInstance().options.fovEffectScale().get();
            zoomMod = 1+zoomMod2;

            if (player.getUseItem().getItem() instanceof JackalRifleItem) {
                AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer) this.minecraft.getCameraEntity();
                if (abstractclientplayer != null && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                    zoomMod *= 0.2F;
                }
            }
            if (SP.scopeLevel > 0 || zoomMod != 1) {
                ci.cancel();

                float f = 1.0F;
                this.oldFov = this.fov;

                AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer) this.minecraft.getCameraEntity();
                if (abstractclientplayer != null && Minecraft.getInstance().options.getCameraType().isFirstPerson() && abstractclientplayer.isScoping()) {
                    if (SP.scopeLevel == 1) {
                        f = 0.05F;
                    } else if (SP.scopeLevel == 2) {
                        f = 0.0225F;
                    } else if (SP.scopeLevel > 2) {
                        f = 0.01F;
                    }
                } else if (abstractclientplayer != null && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                    if (SP.scopeLevel == 1) {
                        f = 0.3F;
                    } else if (SP.scopeLevel == 2) {
                        f = 0.125F;
                    } else if (SP.scopeLevel > 2) {
                        f = 0.05F;
                    }
                }

                f*= zoomMod;

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
            if (ClientUtil.disableBobbing(player)){
                ci.cancel();
                return;
            }
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
