package net.hydra.jojomod.mixin.barrage;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class DazeGameRenderer {

    /**Minor code to prevent nauseating barrage shaking effect when getting barraged.*/

    /**The amount of screen shakes has to be negligable for a 20 hits per sec move*/
    @Inject(method = "bobHurt", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$tiltViewWhenHurt(PoseStack $$0, float $$1, CallbackInfo ci) {
        //$$0 is matrcices, $$1 is tickdelta
            if (minecraft.player != null && (((StandUser) minecraft.player).roundabout$isDazed() ||
                    (((IPlayerEntity) minecraft.player).roundabout$getCameraHits() > -1))) {
                assert minecraft.level != null;
                if (minecraft.level.getGameTime() % 5 != 0) {
                    LivingEntity livingEntity = (LivingEntity) minecraft.getCameraEntity();
                    if (livingEntity != null) {
                        float f = (float) livingEntity.hurtTime - $$1;
                        if (f < 0.0f) {
                            return;
                        }
                        f /= ((float) livingEntity.hurtDuration * 2);
                        f = Mth.sin(f * f * f * f * (float) Math.PI);
                        float g = livingEntity.getHurtDir();
                        $$0.mulPose(Axis.YP.rotationDegrees(-g));
                        float h = (float) ((double) (-f) * 14.0 * minecraft.options.damageTiltStrength().get());
                        $$0.mulPose(Axis.ZP.rotationDegrees(h));
                        $$0.mulPose(Axis.YP.rotationDegrees(g));
                    }
                }
                ci.cancel();
            }

    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    @Final
    Minecraft minecraft;

}
