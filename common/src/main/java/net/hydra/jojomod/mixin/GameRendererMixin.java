package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    /**Barrage Dazing was too nauseating, so the amount of screen shakes had to be negligable*/
    @Inject(method = "bobHurt", at = @At(value = "HEAD"), cancellable = true)
    private void RoundaboutiltViewWhenHurt(PoseStack $$0, float $$1, CallbackInfo ci) {
        //$$0 is matrcices, $$1 is tickdelta
        Minecraft client = ((GameRenderer) (Object) this).getMinecraft();
        if (client.player != null && ((StandUser) client.player).isDazed()) {
            assert client.level != null;
            if (client.level.getGameTime() % 5 != 0) {
                LivingEntity livingEntity = (LivingEntity) client.getCameraEntity();
                if (livingEntity != null) {
                    float f = (float) livingEntity.hurtTime - $$1;
                    if (f < 0.0f) {
                        return;
                    }
                    f /= ((float) livingEntity.hurtDuration * 2);
                    f = Mth.sin(f * f * f * f * (float) Math.PI);
                    float g = livingEntity.getHurtDir();
                    $$0.mulPose(Axis.YP.rotationDegrees(-g));
                    float h = (float) ((double) (-f) * 14.0 * client.options.damageTiltStrength().get());
                    $$0.mulPose(Axis.ZP.rotationDegrees(h));
                    $$0.mulPose(Axis.YP.rotationDegrees(g));
                }
            }
            ci.cancel();
        }
    }

}
