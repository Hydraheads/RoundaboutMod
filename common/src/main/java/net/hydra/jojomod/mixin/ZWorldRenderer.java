package net.hydra.jojomod.mixin;


import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IClientLevelData;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

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

    @Shadow
    private Frustum capturedFrustum;

    @Shadow
    private Frustum cullingFrustum;

    @Shadow
    @Final
    private Vector3d frustumPos = null;

    @Shadow
    @Final
    private RenderBuffers renderBuffers;

    @Shadow
    public boolean shouldShowEntityOutlines() {
        return false;
    }

    @Shadow
    public boolean isChunkCompiled(BlockPos p_202431_) {
        return false;
    }

    @Inject( method = "doesMobEffectBlockSky",
            at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$doesMobEffectBlockSky(Camera $$0, CallbackInfoReturnable<Boolean> cir) {


    }
        @Inject( method = "renderEntity(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V",
            at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$doNotDeltaTickEntityWhenTimeIsStopped1(Entity $$0, double $$1, double $$2, double $$3, float $$4, PoseStack $$5, MultiBufferSource $$6, CallbackInfo ci) {

        if(((TimeStop) level).inTimeStopRange($$0) && ((TimeStop) level).CanTimeStopEntity($$0) && !($$0 instanceof FishingHook)) {
            double $$7 = Mth.lerp((double)$$4, $$0.xOld, $$0.getX());
            double $$8 = Mth.lerp((double)$$4, $$0.yOld, $$0.getY());
            double $$9 = Mth.lerp((double)$$4, $$0.zOld, $$0.getZ());
            float $$10 = Mth.lerp($$4, $$0.yRotO, $$0.getYRot());
            $$4 = ((IEntityAndData) $$0).roundabout$getPreTSTick();
            this.entityRenderDispatcher
                    .render($$0, $$7 - $$1, $$8 - $$2, $$9 - $$3, $$10, $$4, $$5, $$6, this.entityRenderDispatcher.getPackedLightCoords($$0, $$4));
            ci.cancel();
        } else {
            Minecraft mc = Minecraft.getInstance();
            ((IEntityAndData) $$0).roundabout$setPreTSTick(mc.getFrameTime());
        }
    }

    @ModifyVariable(method = "renderSnowAndRain(Lnet/minecraft/client/renderer/LightTexture;FDDD)V", at = @At(value = "HEAD"), ordinal = 0)
    private float roundabout$TSRainCancel(float $$1) {
        LivingEntity player = Minecraft.getInstance().player;
        if (player != null){
            if (((TimeStop)player.level()).inTimeStopRange(player)){
                //Roundabout.LOGGER.info(String.valueOf(player.level().getGameTime()));
               return this.minecraft.level.getRainLevel(((IEntityAndData)player).roundabout$getPreTSTick());
            }
        }
        return $$1;
    }

    @Inject( method = "renderClouds",
            at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$renderClouds(PoseStack $$0, Matrix4f $$1, float $$2, double $$3, double $$4, double $$5, CallbackInfo ci) {

    }

    @ModifyVariable(method = "renderClouds(Lcom/mojang/blaze3d/vertex/PoseStack;Lorg/joml/Matrix4f;FDDD)V", at = @At(value = "HEAD"), ordinal = 0)
    private float roundabout$TSCloudCancel(float $$2) {
        LivingEntity player = Minecraft.getInstance().player;
        if (player != null){
            if (((TimeStop)player.level()).inTimeStopRange(player)){
               return ((IEntityAndData)player).roundabout$getPreTSTick();
            }
        }
        return $$2;
    }

    /**Interpolate Time during/after timestop or made in heaven*/
    @Inject(method = "renderLevel", at = @At(value = "HEAD"), cancellable = true)
    private void roundaboutRenderLevel(PoseStack $$0, float $$1, long $$2, boolean $$3, Camera $$4,
                                       GameRenderer $$5, LightTexture $$6, Matrix4f $$7, CallbackInfo ci){
        IClientLevelData levelTimeData = ((IClientLevelData)this.level.getLevelData());
        if (levelTimeData.roundabout$getRoundaboutInterpolatingDaytime()){
            /*If a timestop is active, don't tick through a MIH or instead interpolate back*/
            if (!levelTimeData.roundabout$getRoundaboutTimeStopInitialized()){
                long dayTime =levelTimeData.roundabout$getRoundaboutDayTimeMinecraft();
                long dayTimeOld = levelTimeData.roundabout$getRoundaboutDayTimeActual();
                if (Math.abs(dayTimeOld - dayTime) > 1L){
                    levelTimeData.roundabout$setRoundaboutDayTimeActual((long) Mth.lerp((double)$$1, dayTimeOld, dayTime));
                } else {
                    levelTimeData.roundabout$setRoundaboutInterpolatingDaytime(false);
                }
            }
        }
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endLastBatch()V",ordinal = 0, shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void roundabout$RenderLevelEntities(PoseStack $$0, float $$1, long $$2, boolean $$3, Camera $$4,
                                                GameRenderer $$5, LightTexture $$6, Matrix4f $$7, CallbackInfo ci){
    /*
        Vec3 vec3 = $$4.getPosition();
        double d0 = vec3.x();
        double d1 = vec3.y();
        double d2 = vec3.z();
        boolean flag = this.capturedFrustum != null;
        Frustum frustum;
        if (flag) {
            frustum = this.capturedFrustum;
            frustum.prepare(this.frustumPos.x, this.frustumPos.y, this.frustumPos.z);
        } else {
            frustum = this.cullingFrustum;
        }
        for(Entity entity : this.level.entitiesForRendering()) {
            if (this.entityRenderDispatcher.shouldRender(entity, frustum, d0, d1, d2) || entity.hasIndirectPassenger(this.minecraft.player)) {
                MultiBufferSource multibuffersource;
                if (this.shouldShowEntityOutlines() && this.minecraft.shouldEntityAppearGlowing(entity)) {
                    multibuffersource = this.renderBuffers.outlineBufferSource();
                } else {
                    multibuffersource = this.renderBuffers.bufferSource();
                }

                BlockPos blockpos = entity.blockPosition();
                if ((this.level.isOutsideBuildHeight(blockpos.getY()) || this.isChunkCompiled(blockpos)) && (entity != $$4.getEntity() || $$4.isDetached() || $$4.getEntity() instanceof LivingEntity && ((LivingEntity)$$4.getEntity()).isSleeping()) && (!(entity instanceof LocalPlayer) || $$4.getEntity() == entity || (entity == minecraft.player && !minecraft.player.isSpectator()))) {
                    SethanRenderer.renderEntity(this.entityRenderDispatcher, entity, d0, d1, d2, $$1, $$0, multibuffersource);
                }
            }
        }
     */
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
