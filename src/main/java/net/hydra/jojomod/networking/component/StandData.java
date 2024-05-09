package net.hydra.jojomod.networking.component;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.networking.MyComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

/** This code is attached to every single
 * @see StandEntity
 * and is to store the mob it is following on top of its master.*/
public class StandData implements StandComponent {
    private final StandEntity RStand;
    @Nullable
    private LivingEntity RUser;
    @Nullable
    private LivingEntity Following;

    public StandData(StandEntity entity) {
        this.RStand = entity;
    }

    /** Calling sync sends packets which update data on the client side.
     * @see #applySyncPacket */
    public void sync() {
        //RsyncOn = true;
        MyComponents.STAND.sync(this.RStand);
    }

    /** This code is here in case we want to restrict when the syncing happens. Currently,
     * it is unused.*/
    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player){
        return true;
        //return RsyncOn;
    };

    public void setUser(LivingEntity StandSet){
        this.RUser = StandSet;
        this.Following = StandSet;
        this.sync();
    }

    public void setFollowing(LivingEntity StandSet){
        this.Following = StandSet;
        this.sync();
    }

    @Nullable
    public LivingEntity getUser(){
        return this.RUser;
    }
    @Nullable
    public LivingEntity getFollowing(){
        return this.Following;
    }

    /** This is where the server writes out the id of the stand's user and follower target.*/
    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        int usID; if (this.RUser == null){usID=-1;} else {usID = this.RUser.getId();}
        int foID; if (this.Following == null){foID=-1;} else {foID = this.Following.getId();}
        buf.writeInt(usID);
        buf.writeInt(foID);
    }

    /** This is where the client reads the entity ids sent by the server and puts them into code.
     * Basically, it's how the client learns the stand's user and following target.*/
    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        int usID = buf.readInt();
        int foID = buf.readInt();
        this.RUser = (LivingEntity) RStand.getWorld().getEntityById(usID);
        this.Following = (LivingEntity) RStand.getWorld().getEntityById(foID);
    }

    /** If a stand is unloaded, it may need to save the UUID of its user to itself so that
     * when it is reloaded it can exist properly...
     * but for now, I don't think that's necessary, a fresh stand should be spawned instead!*/
    @Override
    public void writeToNbt(NbtCompound tag) {
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
    }

}
