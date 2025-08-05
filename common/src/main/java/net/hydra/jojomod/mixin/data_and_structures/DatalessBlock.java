package net.hydra.jojomod.mixin.data_and_structures;

import net.hydra.jojomod.access.CancelDataDrivenDropLimits;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Block.class)
public class DatalessBlock {

    /**I am fed up with data driven drops literally not allowing complex modded operations
     * this is a mod not a datapack
     * it should be able to use modding, not data pack stuff*/
    @Inject(method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;)Ljava/util/List;",
            at = @At(value = "HEAD"), cancellable = true)
    private static void roundabout$getDrops(BlockState $$0, ServerLevel $$1, BlockPos $$2, BlockEntity $$3, CallbackInfoReturnable<List<ItemStack>> cir) {
        if ($$0.getBlock() instanceof CancelDataDrivenDropLimits CL){
            cir.setReturnValue(CL.getRealDrops($$0,$$1,$$2,$$3));
        }
    }
    @Inject(method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;",
            at = @At(value = "HEAD"), cancellable = true)
    private static void roundabout$getDrops(BlockState $$0, ServerLevel $$1, BlockPos $$2, BlockEntity $$3, Entity $$4, ItemStack $$5, CallbackInfoReturnable<List<ItemStack>> cir) {
        if ($$0.getBlock() instanceof CancelDataDrivenDropLimits CL){
            cir.setReturnValue(CL.getRealDrops($$0,$$1,$$2,$$3,$$4,$$5));
        }
    }
}
