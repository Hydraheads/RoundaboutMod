package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IStandUser;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
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

    private void tickStandIn(Entity entity, Entity passenger) {
        if (passenger.isRemoved() || ((IStandUser)passenger).getMaster() != entity) {
            ((IStandUser) passenger).stopStandOut();
            return;
        }
        passenger.resetPosition();
        ++passenger.age;
        ((IStandUser) passenger).tickStandOut();
    }

}
