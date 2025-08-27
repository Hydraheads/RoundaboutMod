package net.hydra.jojomod.mixin.survivor;

import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Brain.class)
public class SurvivorBrain<E extends LivingEntity> {

    /**Mob AI works. Survivor AND stand user mobs disable AI of a mob's brain, so it can remain focused on
     * fighting instead of doing other random ai tasks. Does not apply to enemy mobs, mostly just designed for
     * things that are not normally aggressive*/
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void roundabout$Render(ServerLevel $$0, E $$1, CallbackInfo ci) {
        if (MainUtil.forceAggression($$1)) {
            if ($$1 instanceof Mob mb && mb.getTarget() != null
                    && !(mb instanceof Enemy) && (mb instanceof AbstractVillager || mb instanceof Animal)
                    && !mb.isInWater()
            && !((IMob)mb).roundabout$getFightOrFlight()){
                ci.cancel();
            }
        }
    }
}
