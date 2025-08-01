package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.CancelDataDrivenDropLimits;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.block.InvisiBlockEntity;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersAchtungBaby;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Block.class)
public class ZBlock {

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
    @Inject(method = "playerDestroy", at = @At(value = "HEAD"))
    private void roundabout$playerDestroy(Level $$0, Player $$1, BlockPos $$2, BlockState $$3, BlockEntity $$4, ItemStack $$5, CallbackInfo ci) {
        if ($$1 != null && !$$0.isClientSide()){
            ((StandUser)$$1).roundabout$getStandPowers().onDestroyBlock($$0,$$1,$$2,$$3,$$4,$$5);
        }
    }

}
