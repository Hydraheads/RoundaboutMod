package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRenderDispatcher.class)
public class ZBlockRenderDispatcher {

    /***
    Inject(method = "renderBatched", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$getOcclusionShape(BlockState $$0, BlockPos $$1, BlockAndTintGetter $$2, PoseStack $$3, VertexConsumer $$4, boolean $$5, RandomSource $$6, CallbackInfo ci) {

        //Roundabout.LOGGER.info("ye2");
    }
     if (ClientUtil.hiddenBlocks.contains($$2)) {

     Roundabout.LOGGER.info("ye2");
     cir.setReturnValue(Shapes.empty());
     }
     **/
}
