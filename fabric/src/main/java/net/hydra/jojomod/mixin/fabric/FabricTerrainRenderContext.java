package net.hydra.jojomod.mixin.fabric;

import net.fabricmc.fabric.impl.client.indigo.renderer.render.TerrainRenderContext;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TerrainRenderContext.class)

public
class FabricTerrainRenderContext {
/**
    @Inject(method = "tessellateBlock(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/client/resources/model/BakedModel;Lcom/mojang/blaze3d/vertex/PoseStack;)V", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$tessellateBlock(BlockState blockState, BlockPos blockPos, BakedModel model, PoseStack matrixStack, CallbackInfo ci) {
        This code never happens, either
         if(MainUtil.getHiddenBlocks().contains(blockPos)){
                        ci.cancel();
                      }
    }
         */
}
