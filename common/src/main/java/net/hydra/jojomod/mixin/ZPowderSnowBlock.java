package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public class ZPowderSnowBlock {

    @Inject(method = "canEntityWalkOnPowderSnow(Lnet/minecraft/world/entity/Entity;)Z", at = @At(value = "HEAD"), cancellable = true)
    private static void roundabout$isEmpty(Entity $$0, CallbackInfoReturnable<Boolean> cir) {
        if ($$0 instanceof LivingEntity LE){
            if (((StandUser)LE).roundabout$isBubbleEncased()){
                cir.setReturnValue(true);
            }
        }
    }
}
