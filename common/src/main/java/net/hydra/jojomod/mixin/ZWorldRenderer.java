package net.hydra.jojomod.mixin;


import net.hydra.jojomod.access.IBlockEntityClientAccess;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.event.powers.StandUserClient;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
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
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.sql.Time;

@Mixin(LevelRenderer.class)
public class ZWorldRenderer {
    /**Get rid of the animation jitter of stopped mobs and chests thru deltatick*/

    @Shadow
    @Final
    private ClientLevel level;

    @ModifyArgs(
            method = "renderEntity(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;render(Lnet/minecraft/world/entity/Entity;DDDFFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"))
    private void doNotDeltaTickEntityWhenTimeIsStopped(Args args) {
        Entity entity = args.get(0);
        if(((TimeStop) level).inTimeStopRange(entity) && ((TimeStop) level).CanTimeStopEntity(entity) && !(entity instanceof FishingHook)) {
            args.set(5, ((IEntityDataSaver) entity).getPreTSTick());
        } else {
            ((IEntityDataSaver) entity).setPreTSTick();
        }
    }

    @ModifyArgs(
            method = "renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/blockentity/BlockEntityRenderDispatcher;render(Lnet/minecraft/world/level/block/entity/BlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V"))
    private void doNotDeltaTickBlockWhenTimeIsStopped(Args args) {
        BlockEntity entity = args.get(0);
        if(((TimeStop) level).inTimeStopRange(entity.getBlockPos()) && !(level.getBlockState(entity.getBlockPos()).is(Blocks.MOVING_PISTON))) {
            args.set(1, ((IBlockEntityClientAccess)entity).getPreTSTick());
        } else {
            ((IBlockEntityClientAccess)entity).setPreTSTick();
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
