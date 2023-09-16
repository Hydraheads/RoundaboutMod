package net.hydra.jojomod.mixin;

import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.access.IStandUser;
import net.hydra.jojomod.entity.StandEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {


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
