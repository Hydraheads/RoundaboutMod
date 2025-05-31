package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(ItemStack.class)
public class ZItemStack {

    @Shadow @Nullable private CompoundTag tag;

    @Unique
    public CompoundTag roundabout$getTag(){
        return this.tag;
    }

    @Inject(method = "mineBlock(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;)V", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$mineBlock(Level $$0, BlockState $$1, BlockPos $$2, Player $$3, CallbackInfo ci) {
        StandPowers powers = ((StandUser) $$3).roundabout$getStandPowers();
        if (((StandUser) $$3).roundabout$getActive() && powers.canUseMiningStand()) {
            powers.gainExpFromSpecialMining($$1,$$2);
            ci.cancel();
        } else {
            powers.gainExpFromStandardMining($$1,$$2);
        }
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void roundabout$stopUse(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir)
    {
        if (((StandUser)player).roundabout$isParallelRunning())
        {
            cir.setReturnValue(InteractionResultHolder.pass(player.getItemInHand(hand)));
            cir.cancel();
        }
    }
}
