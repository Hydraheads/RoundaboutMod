package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IStandUser;
import net.hydra.jojomod.entity.StandEntity;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {


    @Inject(method = "tickEntity", at = @At(value = "TAIL"))
    private void teleportStandOut(Entity entity, CallbackInfo ci) {

        if (((IStandUser) entity).hasStandOut()){
            this.tickStandIn(entity,((IStandUser) entity).getStandOut());
        }
    }

    private void tickStandIn(Entity entity, StandEntity passenger) {
        if (passenger.isRemoved() || passenger.getMaster() != entity) {
            passenger.dismountMaster();
            return;
        }
        passenger.resetPosition();
        ++passenger.age;
        //Profiler profiler = this.getProfiler();
        //profiler.push(() -> Registries.ENTITY_TYPE.getId(passenger.getType()).toString());
        //profiler.visit("tickPassenger");
        passenger.tickStandOut();
        //profiler.pop();
    }

}
