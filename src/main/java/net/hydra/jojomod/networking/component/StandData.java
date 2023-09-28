package net.hydra.jojomod.networking.component;

import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.entity.StandEntity;
import net.hydra.jojomod.networking.MyComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

public class StandData implements StandComponent {
    private final StandEntity Stand;
    @Nullable
    private LivingEntity User;
    private boolean syncOn;
    public StandData(StandEntity entity) {
        this.Stand = entity;
    }
    private void sync() {
        syncOn = true;
        MyComponents.STAND.sync(this.Stand);
        syncOn = false;
    }

    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player){
        return syncOn;
    };

    public void setUser(LivingEntity StandSet){
        this.User = StandSet;
        this.sync();
    }

    @Nullable
    public LivingEntity getUser(){
        return this.User;
    }


    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        //RoundaboutMod.LOGGER.info("Apply");
        Entity userEntity = buf.readBoolean() ?
                this.Stand.getWorld().getEntityById(buf.readInt()) : null;
        if (userEntity == null || userEntity.isLiving()) {
            this.User = (LivingEntity) userEntity;
        }
    }

    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        //RoundaboutMod.LOGGER.info("Write");
        buf.writeBoolean(this.User != null);
        if (this.User != null) {
            buf.writeInt(this.User.getId());
        }
    }
    @Override
    public void readFromNbt(NbtCompound tag) {

    }

    @Override
    public void writeToNbt(NbtCompound tag) {

    }
}
