package net.hydra.jojomod.mixin.stand_users;

import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
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
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class PowersItemStack {

    /**Stand code to gain experience from mining blocks.*/

    @Inject(method = "mineBlock(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;)V", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$mineBlock(Level $$0, BlockState $$1, BlockPos $$2, Player $$3, CallbackInfo ci) {
        StandPowers powers = ((StandUser) $$3).roundabout$getStandPowers();
        if (PowerTypes.hasStandActive($$3) && powers.canUseMiningStand()) {
            powers.gainExpFromSpecialMining($$1, $$2);
            ci.cancel();
        } else if (PowerTypes.isBrawling($$3)){
            ci.cancel();
        } else {
            powers.gainExpFromStandardMining($$1,$$2);
        }
    }
    @Inject(method = "hurt", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$fortHurt(int $$0, RandomSource $$1, ServerPlayer $$2, CallbackInfoReturnable<Boolean> cir) {
        if ($$2 != null){
            if ($$2.hasEffect(ModEffects.FORTIFICATION)){
                cir.setReturnValue(false);
            }
        }
    }
    @Inject(method = "hurtAndBreak", at = @At(value = "HEAD"), cancellable = true)
    protected <T extends LivingEntity> void roundabout$fortHurtAndBreak(int $$0, T $$1, Consumer<T> $$2, CallbackInfo ci) {
        if ($$1 != null){
            if ($$1.hasEffect(ModEffects.FORTIFICATION)){
                ci.cancel();
            }
        }
    }

}
