package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IBlockState;
import net.hydra.jojomod.access.IBlockStateBase;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class ZBlockStateBase implements IBlockStateBase {


    @Shadow protected abstract BlockState asState();

    /***
    Inject(method = "getOcclusionShape", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$getOcclusionShape(BlockGetter $$0, BlockPos $$1, CallbackInfoReturnable<VoxelShape> cir) {
        if(MainUtil.getHiddenBlocks().contains($$1)){
            cir.setReturnValue(Shapes.empty());
        }
    }
     **/

    @Inject(method = "getRenderShape", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$getBlockState(CallbackInfoReturnable<RenderShape> cir) {
        /**
        if(((IBlockState)this.asState()).roundabout$getInvis()){
            cir.setReturnValue(RenderShape.INVISIBLE);
        }
         **/
    }
    @Inject(method = "entityInside", at = @At("HEAD"), cancellable = true)
    private void roundabout$entityInside(Level level, BlockPos pos, Entity entity, CallbackInfo ci)
    {
        if (entity instanceof LivingEntity LE)
        {
            if (((StandUser)LE).roundabout$isParallelRunning())
            {
                ci.cancel();
            }
        }
    }
}
