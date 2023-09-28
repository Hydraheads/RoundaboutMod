package net.hydra.jojomod.networking.component;

import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.entity.StandEntity;
import net.hydra.jojomod.networking.MyComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class StandUserData implements StandUserComponent {
    //StandUserComponent standUserData = (StandUserComponent) MyComponents.STAND.get(player);
    private final LivingEntity User;
    @Nullable
    private StandEntity Stand;

    private boolean syncOn;
    public StandUserData(LivingEntity entity) {
        this.User = entity;
    }
    private void sync() {
        syncOn = true;
        MyComponents.STAND_USER.sync(this.User);
        syncOn = false;
    }

    public void standMount(StandEntity StandSet){
        //RoundaboutMod.LOGGER.info("MF4");
        this.Stand = StandSet;
        StandSet.setMaster(User);
        this.sync();
    }
    public void setStand(StandEntity StandSet){
        this.Stand = StandSet;
        this.sync();
    }

    @Nullable
    public StandEntity getStand(){
        return this.Stand;
    }
    public boolean hasStandOut() {
        return (Stand != null && Stand.isAlive() && !Stand.isRemoved());
    }

    public void setDI(int forward, int strafe){
        //RoundaboutMod.LOGGER.info("MF:"+ forward);
        if (Stand != null){
            if (!User.isSneaking() && User.isSprinting()){
                forward*=2;}
            Stand.setMoveForward(forward);
        }
    }

    public void updateStandOutPosition(StandEntity passenger) {
        this.updateStandOutPosition(passenger, Entity::setPosition);
    }

    public void updateStandOutPosition(StandEntity passenger, Entity.PositionUpdater positionUpdater) {
        if (!(this.hasStandOut())) {
            return;
        }
        Vec3d grabPos = passenger.getStandOffsetVector(User);
        positionUpdater.accept(passenger, grabPos.x, grabPos.y, grabPos.z);
        passenger.setYaw(User.getHeadYaw());
        passenger.setPitch(User.getPitch());
        passenger.setBodyYaw(User.getHeadYaw());
        passenger.setHeadYaw(User.getHeadYaw());
    }



    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player){
        return syncOn;
    };

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        //RoundaboutMod.LOGGER.info("Apply");
        Entity standEntity = buf.readBoolean() ?
                this.User.getWorld().getEntityById(buf.readInt()) : null;
        if (standEntity == null || standEntity.isLiving()) {
            this.Stand = (StandEntity) standEntity;
        }

    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        //RoundaboutMod.LOGGER.info("Write");
        buf.writeBoolean(this.Stand != null);
        if (this.Stand != null) {
            buf.writeInt(this.Stand.getId());
        }
    }
    public void onStandOutLookAround(StandEntity passenger) {
    }

    public void removeStandOut() {
        this.Stand = null;
        //this.emitGameEvent(GameEvent.ENTITY_DISMOUNT, passenger);
    }







    public void readFromNbt(NbtCompound tag) {

    }

    public void writeToNbt(NbtCompound tag) {

    }

}
