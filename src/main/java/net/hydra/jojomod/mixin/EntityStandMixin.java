package net.hydra.jojomod.mixin;

import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.access.IStandUser;
import net.hydra.jojomod.entity.StandEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(Entity.class)
public class EntityStandMixin implements IStandUser {

    //This Code gives every entity the potential to be a stand user.
    //It provides movement sync code for stands as well, by offering a modified copy of mount code
    private StandEntity standOut;


    @Override
    public void setDI(int forward, int strafe){
        //RoundaboutMod.LOGGER.info("MF:"+ forward);
        if (standOut != null){
            if (!((Entity) (Object) this).isSneaking() && ((Entity) (Object) this).isSprinting()){
                forward*=2;}
            standOut.setMoveForward(forward);
        }
    }

    @Override
    public StandEntity getStandOut() {
        return this.standOut;
    }

    @Override
    public boolean hasStandOut() {
        return (standOut != null && standOut.isAlive() && !standOut.isRemoved());
    }

    @Override
    public void updateStandOutPosition(StandEntity passenger) {
        this.updateStandOutPosition(passenger, Entity::setPosition);
    }

    @Override
    public void updateStandOutPosition(StandEntity passenger, Entity.PositionUpdater positionUpdater) {
        if (!(this.hasStandOut())) {
            return;
        }
        Vec3d grabPos = passenger.getStandOffsetVector(((Entity) (Object) this));
        positionUpdater.accept(passenger, grabPos.x, grabPos.y, grabPos.z);
        passenger.setYaw(((Entity) (Object) this).getHeadYaw());
        passenger.setPitch(((Entity) (Object) this).getPitch());
        passenger.setBodyYaw(((Entity) (Object) this).getHeadYaw());
        passenger.setHeadYaw(((Entity) (Object) this).getHeadYaw());
    }

    @Inject(method = "teleportPassengers", at = @At(value = "TAIL"))
    private void teleportStandOut(CallbackInfo ci) {
        if (hasStandOut()){
            this.updateStandOutPosition(standOut, Entity::refreshPositionAfterTeleport);
        }
    }

    @Override
    public void onStandOutLookAround(StandEntity passenger) {
    }





    @Override
    public void addStandOut(StandEntity passenger) {
            this.standOut = passenger;
        //this.emitGameEvent(GameEvent.ENTITY_MOUNT, passenger);
    }

    @Override
    public void removeStandOut() {
        this.standOut = null;
        //this.emitGameEvent(GameEvent.ENTITY_DISMOUNT, passenger);
    }


}
