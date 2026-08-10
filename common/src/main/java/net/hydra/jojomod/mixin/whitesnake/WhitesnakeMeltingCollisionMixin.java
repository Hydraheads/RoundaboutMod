package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.entity.stand.WhitesnakeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.RodBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class WhitesnakeMeltingCollisionMixin {
    @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
            at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$phaseGrabCollision(BlockGetter level, BlockPos pos,
                                                          CollisionContext context,
                                                          CallbackInfoReturnable<VoxelShape> cir) {
        if (!(context instanceof EntityCollisionContext entityContext)) return;
        Entity entity = entityContext.getEntity();
        if (!(entity instanceof WhitesnakeEntity whitesnake)
                || !(whitesnake.isMeltingModeActive() || whitesnake.isSnakeBiteActive())) return;
        Block block = ((BlockState) (Object) this).getBlock();
        if (block instanceof IronBarsBlock || block instanceof FenceBlock || block instanceof FenceGateBlock
                || block instanceof SlabBlock || block instanceof AnvilBlock || block instanceof BellBlock
                || block instanceof RodBlock) {
            cir.setReturnValue(Shapes.empty());
        }
    }
}
