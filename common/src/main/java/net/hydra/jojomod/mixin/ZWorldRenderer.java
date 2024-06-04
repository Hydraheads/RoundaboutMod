package net.hydra.jojomod.mixin;


import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IBlockEntityClientAccess;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.event.powers.StandUserClient;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.sql.Time;

@Mixin(LevelRenderer.class)
public class ZWorldRenderer {
    /**Get rid of the animation jitter of stopped mobs and chests thru deltatick*/

    @Shadow
    @Final
    private ClientLevel level;


    @Shadow
    @Final
    private EntityRenderDispatcher entityRenderDispatcher;
    @Inject( method = "renderEntity(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V",
            at = @At(value = "HEAD"), cancellable = true)
    private void doNotDeltaTickEntityWhenTimeIsStopped1(Entity $$0, double $$1, double $$2, double $$3, float $$4, PoseStack $$5, MultiBufferSource $$6, CallbackInfo ci) {

        if(((TimeStop) level).inTimeStopRange($$0) && ((TimeStop) level).CanTimeStopEntity($$0) && !($$0 instanceof FishingHook)) {
            $$4 = ((IEntityDataSaver) $$0).getPreTSTick();
            double $$7 = Mth.lerp((double)$$4, $$0.xOld, $$0.getX());
            double $$8 = Mth.lerp((double)$$4, $$0.yOld, $$0.getY());
            double $$9 = Mth.lerp((double)$$4, $$0.zOld, $$0.getZ());
            float $$10 = Mth.lerp($$4, $$0.yRotO, $$0.getYRot());
            this.entityRenderDispatcher
                    .render($$0, $$7 - $$1, $$8 - $$2, $$9 - $$3, $$10, $$4, $$5, $$6, this.entityRenderDispatcher.getPackedLightCoords($$0, $$4));
            ci.cancel();
        } else {
            Minecraft mc = Minecraft.getInstance();
            ((IEntityDataSaver) $$0).setPreTSTick(mc.getFrameTime());
        }
    }



    @ModifyVariable(method = "renderSnowAndRain(Lnet/minecraft/client/renderer/LightTexture;FDDD)V", at = @At(value = "HEAD"), ordinal = 0)
    private float RoundaboutTSRainCancel(float $$1) {
        LivingEntity player = Minecraft.getInstance().player;
        if (player != null){
            if (((TimeStop)player.level()).inTimeStopRange(player)){
                LivingEntity player2 = ((TimeStop)player.level()).inTimeStopRangeEntity(player);
                if (player2 != null){
                    return ((StandUserClient)player2).getPreTSTickDelta();
                }
            }
        }
        return $$1;
    }

    @ModifyVariable(method = "renderClouds(Lcom/mojang/blaze3d/vertex/PoseStack;Lorg/joml/Matrix4f;FDDD)V", at = @At(value = "HEAD"), ordinal = 0)
    private float RoundaboutTSCloudCancel(float $$2) {
        LivingEntity player = Minecraft.getInstance().player;
        if (player != null){
            if (((TimeStop)player.level()).inTimeStopRange(player)){
                LivingEntity player2 = ((TimeStop)player.level()).inTimeStopRangeEntity(player);
                if (player2 != null){
                    return ((StandUserClient)player2).getPreTSTickDelta();
                }
            }
        }
        return $$2;
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
