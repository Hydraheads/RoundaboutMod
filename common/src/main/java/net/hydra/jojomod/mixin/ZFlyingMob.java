package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IMob;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FlyingMob.class)
public abstract class ZFlyingMob extends Mob {

    @Inject(method = "checkFallDamage", at = @At(value = "HEAD"), cancellable = true)
    protected void rdbt$checkFallDamage(double $$0, boolean $$1, BlockState $$2, BlockPos $$3, CallbackInfo ci) {
        if (((IMob)this).rdbt$getStolen()){
            super.checkFallDamage($$0,$$1,$$2,$$3);
        }
    }
    @Unique
    public int rdbt$enough = 0;
    @Inject(method = "travel", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$travel(Vec3 $$0, CallbackInfo ci){
        if (((IMob)this).rdbt$getStolen()){
            setDeltaMovement(0,-0.8,0);
            super.travel($$0);
            ci.cancel();
            rdbt$enough++;
            if (rdbt$enough > 100){
                rdbt$enough = 0;
                ((IMob)this).rdbt$setStolen(false);
            }
        }
    }

    protected ZFlyingMob(EntityType<? extends Mob> $$0, Level $$1) {
        super($$0, $$1);
    }
}
