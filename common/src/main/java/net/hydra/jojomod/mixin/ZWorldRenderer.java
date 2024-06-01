package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

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
        if(((TimeStop) level).inTimeStopRange(entity)) {
            args.set(5, 0.0F);
        }
    }

    @ModifyArgs(
            method = "renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/blockentity/BlockEntityRenderDispatcher;render(Lnet/minecraft/world/level/block/entity/BlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V"))
    private void doNotDeltaTickBlockWhenTimeIsStopped(Args args) {
        BlockEntity entity = args.get(0);
        if(((TimeStop) level).inTimeStopRange(entity.getBlockPos())) {
            args.set(1, 0.0F);
        }
    }

}
