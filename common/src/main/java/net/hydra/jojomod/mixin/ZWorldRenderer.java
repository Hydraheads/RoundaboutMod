package net.hydra.jojomod.mixin;


import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IClientLevelData;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.event.powers.StandUserClient;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FishingHook;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class ZWorldRenderer {
    /**Get rid of the animation jitter of stopped mobs and chests thru deltatick*/

    @Shadow
    @Final
    private ClientLevel level;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Final
    private EntityRenderDispatcher entityRenderDispatcher;
    @Inject( method = "renderEntity(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V",
            at = @At(value = "HEAD"), cancellable = true)
    private void doNotDeltaTickEntityWhenTimeIsStopped1(Entity $$0, double $$1, double $$2, double $$3, float $$4, PoseStack $$5, MultiBufferSource $$6, CallbackInfo ci) {

        if(((TimeStop) level).inTimeStopRange($$0) && ((TimeStop) level).CanTimeStopEntity($$0) && !($$0 instanceof FishingHook)) {
            double $$7 = Mth.lerp((double)$$4, $$0.xOld, $$0.getX());
            double $$8 = Mth.lerp((double)$$4, $$0.yOld, $$0.getY());
            double $$9 = Mth.lerp((double)$$4, $$0.zOld, $$0.getZ());
            float $$10 = Mth.lerp($$4, $$0.yRotO, $$0.getYRot());
            $$4 = ((IEntityAndData) $$0).getPreTSTick();
            this.entityRenderDispatcher
                    .render($$0, $$7 - $$1, $$8 - $$2, $$9 - $$3, $$10, $$4, $$5, $$6, this.entityRenderDispatcher.getPackedLightCoords($$0, $$4));
            ci.cancel();
        } else {
            Minecraft mc = Minecraft.getInstance();
            ((IEntityAndData) $$0).setPreTSTick(mc.getFrameTime());
        }
    }



    @ModifyVariable(method = "renderSnowAndRain(Lnet/minecraft/client/renderer/LightTexture;FDDD)V", at = @At(value = "HEAD"), ordinal = 0)
    private float RoundaboutTSRainCancel(float $$1) {
        LivingEntity player = Minecraft.getInstance().player;
        if (player != null){
            if (((TimeStop)player.level()).inTimeStopRange(player)){
                //Roundabout.LOGGER.info(String.valueOf(player.level().getGameTime()));
               return this.minecraft.level.getRainLevel(((IEntityAndData)player).getPreTSTick());
            }
        }
        return $$1;
    }

    @ModifyVariable(method = "renderClouds(Lcom/mojang/blaze3d/vertex/PoseStack;Lorg/joml/Matrix4f;FDDD)V", at = @At(value = "HEAD"), ordinal = 0)
    private float RoundaboutTSCloudCancel(float $$2) {
        LivingEntity player = Minecraft.getInstance().player;
        if (player != null){
            if (((TimeStop)player.level()).inTimeStopRange(player)){
               return ((IEntityAndData)player).getPreTSTick();
            }
        }
        return $$2;
    }

    /**Interpolate Time during/after timestop or made in heaven*/
    @Inject(method = "renderLevel", at = @At(value = "HEAD"), cancellable = true)
    private void roundaboutRenderLevel(PoseStack $$0, float $$1, long $$2, boolean $$3, Camera $$4,
                                       GameRenderer $$5, LightTexture $$6, Matrix4f $$7, CallbackInfo ci){
        IClientLevelData levelTimeData = ((IClientLevelData)this.level.getLevelData());
        if (levelTimeData.getRoundaboutInterpolatingDaytime()){
            /*If a timestop is active, don't tick through a MIH or instead interpolate back*/
            if (!levelTimeData.getRoundaboutTimeStopInitialized()){
                long dayTime =levelTimeData.getRoundaboutDayTimeMinecraft();
                long dayTimeOld = levelTimeData.getRoundaboutDayTimeActual();
                if (Math.abs(dayTimeOld - dayTime) > 1L){
                    levelTimeData.setRoundaboutDayTimeActual((long) Mth.lerp((double)$$1, dayTimeOld, dayTime));
                } else {
                    levelTimeData.setRoundaboutInterpolatingDaytime(false);
                }
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    private void RoundaboutCancelRenderTicks(CallbackInfo ci){
        LivingEntity player = Minecraft.getInstance().player;
        if (player != null) {
            if (((TimeStop) player.level()).inTimeStopRange(player)) {
                ci.cancel();
            }
        }
    }
}
