package net.hydra.jojomod.mixin.stand_users;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class PowersBlock {

    /**Mandom code to incur mining penalty. Design to be flexibly hooked into other stands.*/
    @Inject(method = "playerDestroy", at = @At(value = "HEAD"))
    private void roundabout$playerDestroy(Level $$0, Player $$1, BlockPos $$2, BlockState $$3, BlockEntity $$4, ItemStack $$5, CallbackInfo ci) {
        if ($$1 != null && !$$0.isClientSide()){
            ((StandUser)$$1).roundabout$getStandPowers().onDestroyBlock($$0,$$1,$$2,$$3,$$4,$$5);
        }
    }
}
