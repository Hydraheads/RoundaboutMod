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
    @Nullable
    private LivingEntity Following;
    private boolean syncOn;
    public StandData(StandEntity entity) {
        this.Stand = entity;
    }
    public void sync() {
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
        this.Following = StandSet;
        this.sync();
    }

    public void setFollowing(LivingEntity StandSet){
        this.Following = StandSet;
        this.sync();
    }

    @Nullable
    public LivingEntity getUser(){
        return this.User;
    }
    @Nullable
    public LivingEntity getFollowing(){
        return this.Following;
    }


    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        Entity userEntity = buf.readBoolean() ?
                this.Stand.getWorld().getEntityById(buf.readInt()) : null;
        if (userEntity == null || userEntity.isLiving()) {
            this.User = (LivingEntity) userEntity;


            Entity followEntity = buf.readBoolean() ?
                    this.Stand.getWorld().getEntityById(buf.readInt()) : null;
            if (followEntity == null || followEntity.isLiving()) {
                this.Following = (LivingEntity) followEntity;

            }
        }
    }


    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        //RoundaboutMod.LOGGER.info("Write");
        buf.writeBoolean(this.User != null);
        if (this.User != null) {
            buf.writeInt(this.User.getId());
            buf.writeBoolean(this.Following != null);
            if (this.Following != null) {
                buf.writeInt(this.Following.getId());
            }
        }
    }
    @Override
    public void readFromNbt(NbtCompound tag) {

    }

    @Override
    public void writeToNbt(NbtCompound tag) {

    }
}
