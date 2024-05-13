package net.hydra.jojomod.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class GameRendererMixin {

    /**Barrage Dazing was too nauseating, so the amount of screen shakes had to be negligable*/
    @Inject(method = "tiltViewWhenHurt", at = @At(value = "HEAD"), cancellable = true)
    private void RoundaboutiltViewWhenHurt(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        MinecraftClient client = ((GameRenderer) (Object) this).getClient();
        if (client.player != null && ((StandUser) client.player).isDazed()) {
            assert client.world != null;
            if (client.world.getTime() % 5 != 0) {
                LivingEntity livingEntity = (LivingEntity) client.getCameraEntity();
                if (livingEntity != null) {
                    float f = (float) livingEntity.hurtTime - tickDelta;
                    if (f < 0.0f) {
                        return;
                    }
                    f /= ((float) livingEntity.maxHurtTime * 2);
                    f = MathHelper.sin(f * f * f * f * (float) Math.PI);
                    float g = livingEntity.getDamageTiltYaw();
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-g));
                    float h = (float) ((double) (-f) * 14.0 * client.options.getDamageTiltStrength().getValue());
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(h));
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(g));
                }
            }
            ci.cancel();
        }
    }

}
