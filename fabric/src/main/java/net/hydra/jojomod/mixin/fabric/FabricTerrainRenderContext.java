package net.hydra.jojomod.mixin.fabric;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.TerrainRenderContext;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TerrainRenderContext.class)

public class FabricTerrainRenderContext {
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
