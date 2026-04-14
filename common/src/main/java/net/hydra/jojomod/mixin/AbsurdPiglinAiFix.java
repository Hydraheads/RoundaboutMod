package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(PiglinAi.class)
public class AbsurdPiglinAiFix {
    @Inject(method = "setAngerTarget",
            at = @At(value = "HEAD"), cancellable = true)
    private static <T extends Mob> void roundabout$setAngerTarget(AbstractPiglin $$0, LivingEntity $$1, CallbackInfo ci) {

        if ($$0 != null && $$1 != null) {
            /**Flesh buds prevent aggro on the planter*/
            UUID fleshPlanter = (((StandUser)$$0).rdbt$getFleshBud());
            if (fleshPlanter != null && ($$1.getUUID().equals(fleshPlanter) ||
                    (((StandUser)$$1).rdbt$getFleshBud() != null && ((StandUser)$$1).rdbt$getFleshBud().equals(fleshPlanter)))){
                ci.cancel();
                return;
            }
            /**Stands that react to aggro like hey ya and wonder of u*/
            ((StandUser)$$1).roundabout$getStandPowers().reactToAggro(((Mob) (Object)$$0));
        }
    }
}
