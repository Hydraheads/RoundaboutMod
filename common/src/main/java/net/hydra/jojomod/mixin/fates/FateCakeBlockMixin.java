package net.hydra.jojomod.mixin.fates;

import net.hydra.jojomod.event.index.FateTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CakeBlock.class)
public class FateCakeBlockMixin {
    @Shadow @Final public static IntegerProperty BITES;

    /**Vamps only eat cake for fun*/
    @Inject(method = "eat(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/InteractionResult;", at = @At(value = "HEAD"), cancellable = true)
    private static void rdbt$eat(LevelAccessor $$0, BlockPos $$1, BlockState $$2, Player $$3,CallbackInfoReturnable<InteractionResult> cir) {
        if (FateTypes.hasBloodHunger($$3)){
                $$3.awardStat(Stats.EAT_CAKE_SLICE);
                int $$4 = $$2.getValue(BITES);
                $$0.gameEvent($$3, GameEvent.EAT, $$1);
                if ($$4 < 6) {
                    $$0.setBlock($$1, $$2.setValue(BITES, Integer.valueOf($$4 + 1)), 3);
                } else {
                    $$0.removeBlock($$1, false);
                    $$0.gameEvent($$3, GameEvent.BLOCK_DESTROY, $$1);
                }

                cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
