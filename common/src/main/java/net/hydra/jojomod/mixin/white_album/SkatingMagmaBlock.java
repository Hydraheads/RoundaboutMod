package net.hydra.jojomod.mixin.white_album;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersWhiteAlbum;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MagmaBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MagmaBlock.class)
public abstract class SkatingMagmaBlock extends Block {

    //White Album can safely skate over magma
    @Inject(method = "stepOn(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/Entity;)V",
            at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$stepOn(Level $$0, BlockPos $$1, BlockState $$2, Entity $$3, CallbackInfo ci){
        if ($$3 instanceof LivingEntity LE &&
                ((StandUser)LE).roundabout$getStandPowers() instanceof PowersWhiteAlbum PWA && PWA.hasSkatesActivated()){
            ci.cancel();
            super.stepOn($$0, $$1, $$2, $$3);
        }
    }

    public SkatingMagmaBlock(Properties $$0) {
        super($$0);
    }

}
