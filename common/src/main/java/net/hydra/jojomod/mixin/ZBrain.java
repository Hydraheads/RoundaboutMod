package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Brain.class)
public class ZBrain<E extends LivingEntity> {

    /**Mob AI works*/
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void roundabout$Render(ServerLevel $$0, E $$1, CallbackInfo ci) {
        if (MainUtil.forceAggression($$1)) {
            if ($$1 instanceof Mob mb && mb.getTarget() != null && !((IMob)mb).roundabout$getFightOrFlight()){
                ci.cancel();
            }
        }
    }
}
