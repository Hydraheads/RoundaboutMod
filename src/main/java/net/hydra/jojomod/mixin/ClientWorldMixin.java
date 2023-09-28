package net.hydra.jojomod.mixin;

import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.entity.StandEntity;
import net.hydra.jojomod.networking.MyComponents;
import net.hydra.jojomod.networking.component.StandUserComponent;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {


    @Inject(method = "tickEntity", at = @At(value = "TAIL"))
    private void tickEntity2(Entity entity, CallbackInfo ci) {
        if (entity.isLiving()) {
            LivingEntity livingEntity = (LivingEntity) entity;
            StandUserComponent standUserData = MyComponents.STAND_USER.get(livingEntity);

            if (standUserData.hasStandOut()) {
                this.tickStandIn(livingEntity, Objects.requireNonNull(standUserData.getStand()));
            }
        }
    }

    private void tickStandIn(LivingEntity entity, StandEntity passenger) {
        if (passenger.isRemoved() || passenger.getMaster() != entity) {
            passenger.dismountMaster();
            return;
        }
        passenger.resetPosition();
        ++passenger.age;
        passenger.tickStandOut();
    }

}
