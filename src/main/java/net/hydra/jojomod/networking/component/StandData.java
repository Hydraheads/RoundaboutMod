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
    private final StandEntity RStand;
    @Nullable
    private LivingEntity RUser;
    @Nullable
    private LivingEntity Following;
    //private boolean RsyncOn;

    public StandData(StandEntity entity) {
        this.RStand = entity;
    }
    public void sync() {
        //RsyncOn = true;
        MyComponents.STAND.sync(this.RStand);
    }

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
    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        int usID; if (this.RUser == null){usID=-1;} else {usID = this.RUser.getId();}
        int foID; if (this.Following == null){foID=-1;} else {foID = this.Following.getId();}
        buf.writeInt(usID);
        buf.writeInt(foID);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        int usID = buf.readInt();
        int foID = buf.readInt();
        this.RUser = (LivingEntity) RStand.getWorld().getEntityById(usID);
        this.Following = (LivingEntity) RStand.getWorld().getEntityById(foID);
    }
    @Override
    public void readFromNbt(NbtCompound tag) {
        int usID = tag.getInt("standUser");
        int foID = tag.getInt("standFollowing");

        Entity UserEnt = RStand.getWorld().getEntityById(usID);
        Entity UserFollowingEnt = RStand.getWorld().getEntityById(foID);
        if (UserEnt != null && UserEnt.isLiving()){
            this.setUser((LivingEntity) UserEnt);
        } if (UserFollowingEnt != null && UserFollowingEnt.isLiving()) {
            this.setFollowing((LivingEntity) UserFollowingEnt);
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        if (this.RUser != null){
            tag.putInt("standUser", this.RUser.getId());
        } if (this.Following != null) {
            tag.putInt("standFollowing", this.Following.getId());
        }
    }
}
