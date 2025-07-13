package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModelBlockRenderer.class)
public class ZModelBlockRenderer {

    /**
    Inject(method = "tesselateBlock", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$tesselateBlock(BlockAndTintGetter $$0, BakedModel $$1, BlockState $$2, BlockPos $$3, PoseStack $$4, VertexConsumer $$5, boolean $$6, RandomSource $$7, long $$8, int $$9, CallbackInfo ci) {

        //Roundabout.LOGGER.info("1+");

        /**
        if(MainUtil.getHiddenBlocks().contains($$3)){
            ci.cancel();
        }
    }
     **/
}
