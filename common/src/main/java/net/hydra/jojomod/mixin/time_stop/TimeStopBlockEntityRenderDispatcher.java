package net.hydra.jojomod.mixin.time_stop;


import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IBlockEntityAccess;
import net.hydra.jojomod.access.IBlockEntityClientAccess;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(BlockEntityRenderDispatcher.class)
public class TimeStopBlockEntityRenderDispatcher {

    /**A mixin to freeze block entity rendering during time stop*/
    @Inject(
            method = "render",
            at = @At(value = "HEAD"), cancellable = true)
    private <E extends BlockEntity> void roundabout$doNotDeltaTickBlockWhenTimeIsStopped(E $$0, float $$1, PoseStack $$2, MultiBufferSource $$3, CallbackInfo ci) {
        if(((TimeStop) $$0.getLevel()).inTimeStopRange($$0.getBlockPos()) && !($$0.getLevel().getBlockState($$0.getBlockPos()).is(Blocks.MOVING_PISTON))) {
            if (((IBlockEntityAccess)$$0).roundabout$getRoundaboutTimeInteracted()){
                return;
            }
                final float f1 = ((IBlockEntityClientAccess) $$0).roundabout$getPreTSTick();
                BlockEntityRenderer<E> $$4 = this.getRenderer($$0);
                if ($$4 != null) {
                    if ($$0.hasLevel() && $$0.getType().isValid($$0.getBlockState())) {
                        if ($$4.shouldRender($$0, this.camera.getPosition())) {
                            tryRender($$0, () -> setupAndRender($$4, $$0, f1, $$2, $$3));
                        }
                    }
                }
                ci.cancel();
        } else {
            ((IBlockEntityClientAccess)$$0).roundabout$setPreTSTick();
            ((IBlockEntityAccess)$$0).roundabout$setRoundaboutTimeInteracted(false);
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    public Camera camera;
    @Shadow
    private static void tryRender(BlockEntity $$0, Runnable $$1) {
    }
    @Shadow
    private static <T extends BlockEntity> void setupAndRender(BlockEntityRenderer<T> $$0, T $$1, float $$2, PoseStack $$3, MultiBufferSource $$4) {

    }
    @Shadow
    @Nullable
    public <E extends BlockEntity> BlockEntityRenderer<E> getRenderer(E $$0) {
        return null;
    }
}
