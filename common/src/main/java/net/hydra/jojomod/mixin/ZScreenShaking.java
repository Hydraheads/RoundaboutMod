package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class ZScreenShaking {

    @Shadow
    @Final
    Minecraft minecraft;


    @Shadow
    public void renderItemInHand(PoseStack $$0, Camera $$1, float $$2) {}


    @Inject(method = "renderItemInHand", at = @At(value = "HEAD"), cancellable = true)
    private void RoundaboutRenderHandsWithItems(PoseStack $$0, Camera $$1, float $$2, CallbackInfo ci){
        //$$0 is matrcices, $$1 is tickdelta
        if (!cleared) {
            if (minecraft.player != null && ((TimeStop) minecraft.player.level()).CanTimeStopEntity(minecraft.player)) {
                if (this.minecraft.getCameraEntity() != null) {
                    Entity Ent = this.minecraft.getCameraEntity();
                    $$2 = ((IEntityAndData) Ent).getPreTSTick();
                    cleared = true;
                    this.renderItemInHand($$0, $$1, $$2);
                    cleared = false;
                }
                ci.cancel();
            }
        }
    }

    public boolean cleared = false;
    /**Minor code to prevent nauseating barrage shaking effect when getting barraged.*/

    /**The amount of screen shakes has to be negligable for a 20 hits per sec move*/
    @Inject(method = "bobHurt", at = @At(value = "HEAD"), cancellable = true)
    private void RoundaboutiltViewWhenHurt(PoseStack $$0, float $$1, CallbackInfo ci) {
        //$$0 is matrcices, $$1 is tickdelta
            if (minecraft.player != null && ((StandUser) minecraft.player).isDazed()) {
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


    @Shadow
    private void bobHurt(PoseStack $$0, float $$1){}
    @Inject(method = "bobHurt", at = @At(value = "HEAD"), cancellable = true)
    private void RoundaboutiltViewWhenHurt2(PoseStack $$0, float $$1, CallbackInfo ci){
        //$$0 is matrcices, $$1 is tickdelta
        if (!cleared) {
            if (minecraft.player != null && ((TimeStop) minecraft.player.level()).CanTimeStopEntity(minecraft.player)) {
                if (this.minecraft.getCameraEntity() instanceof LivingEntity) {
                    Player $$2 = (Player) this.minecraft.getCameraEntity();
                    $$1 = ((IEntityAndData) $$2).getPreTSTick();
                    cleared = true;
                    this.bobHurt($$0, $$1);
                    cleared = false;
                }
                ci.cancel();
            }
        }
    }

    @Shadow
    private void bobView(PoseStack $$0, float $$1){}


    @Inject(method = "bobView", at = @At(value = "HEAD"), cancellable = true)
    private void RoundaboutBobView(PoseStack $$0, float $$1, CallbackInfo ci) {
        if (!cleared) {
            if (minecraft.player != null && ((TimeStop) minecraft.player.level()).CanTimeStopEntity(minecraft.player)) {
                if (this.minecraft.getCameraEntity() instanceof Player) {
                    Player $$2 = (Player) this.minecraft.getCameraEntity();
                    $$1 = ((IEntityAndData) $$2).getPreTSTick();
                    cleared = true;
                    this.bobView($$0, $$1);
                    cleared = false;
                }
                ci.cancel();
            }
        }
    }
}
