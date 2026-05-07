package net.hydra.jojomod.mixin.effects.warding;

import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class WardingEntityMixin {
    /**Warding cancels being set on fire outside the context of fire or lava*/
    @Inject(method = "setSecondsOnFire(I)V", at = @At(value = "HEAD"),cancellable = true)
    public void rdbt$setSecondsOnFire(int $$0, CallbackInfo ci) {
        if (((net.minecraft.world.entity.Entity)(Object)this) instanceof LivingEntity LE){
            if (LE.hasEffect(ModEffects.WARDING)){
                if (!(isInLava() || MainUtil.isPlayerInFireBlock(LE))){
                    ci.cancel();
                }
            }
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    @Shadow public abstract boolean isInLava();
}