package net.hydra.jojomod.mixin.soft_and_wet;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersWhiteAlbum;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public class SoftAndWetPowderSnowBlock {

    /**With the encasement bubble, you are able to walk on powder snow.*/
    @Inject(method = "canEntityWalkOnPowderSnow(Lnet/minecraft/world/entity/Entity;)Z", at = @At(value = "HEAD"), cancellable = true)
    private static void roundabout$isEmpty(Entity $$0, CallbackInfoReturnable<Boolean> cir) {
        if ($$0 instanceof LivingEntity LE){
            StandUser user = ((StandUser)LE);
            if (user.roundabout$isBubbleEncased()){
                cir.setReturnValue(true);
            } else if (user.roundabout$getStandPowers() instanceof PowersWhiteAlbum PW && PW.hasSkatesActivated()){
                cir.setReturnValue(true);
            }
        }
    }
}
