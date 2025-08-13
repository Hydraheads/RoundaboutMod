package net.hydra.jojomod.mixin.forge;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScreenEffectRenderer.class)
public class ForgeScreenEffectRenderer {
    /**Block Breaking Speed Decreases when your hand is stone*/
    @Inject(method = "getOverlayBlock(Lnet/minecraft/world/entity/player/Player;)Lorg/apache/commons/lang3/tuple/Pair;", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private static void roundabout$getOverlayBlock(Player player, CallbackInfoReturnable<Pair<BlockState, BlockPos>> cir) {
        Direction gravityDirection = GravityAPI.getGravityDirection(player);
        if (gravityDirection == Direction.DOWN) return;

        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        Vector3f multipliers = RotationUtil.vecPlayerToWorld(player.getBbWidth() * 0.8F, 0.1F, player.getBbWidth() * 0.8F, gravityDirection);


        for(int i = 0; i < 8; ++i) {
            double d0 = player.getX() + (double)(((float)((i >> 0) % 2) - 0.5F) * multipliers.x());
            double d1 = player.getEyeY() + (double)(((float)((i >> 1) % 2) - 0.5F) * multipliers.y());
            double d2 = player.getZ() + (double)(((float)((i >> 2) % 2) - 0.5F) * multipliers.z());
            blockpos$mutableblockpos.set(d0, d1, d2);
            BlockState blockstate = player.level().getBlockState(blockpos$mutableblockpos);
            if (blockstate.getRenderShape() != RenderShape.INVISIBLE && blockstate.isViewBlocking(player.level(), blockpos$mutableblockpos)) {
                cir.setReturnValue(org.apache.commons.lang3.tuple.Pair.of(blockstate, blockpos$mutableblockpos.immutable()));
            }
        }

        cir.setReturnValue(null);
    }
}
