package net.hydra.jojomod.networking.c2s;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgePilotPacket {
    private final float getX;
    private final float getY;
    private final float getZ;
    private final float getYRot;
    private final float getXRot;
    private final int getStand;
    public ForgePilotPacket(float getX, float getY, float getZ, float getYRot, float getXRot, int getStand){
        this.getX = getX;
        this.getY = getY;
        this.getZ = getZ;
        this.getYRot = getYRot;
        this.getXRot = getXRot;
        this.getStand = getStand;
    }
    public ForgePilotPacket(FriendlyByteBuf buf){
        this.getX = buf.readFloat();
        this.getY = buf.readFloat();
        this.getZ = buf.readFloat();
        this.getYRot = buf.readFloat();
        this.getXRot = buf.readFloat();
        this.getStand = buf.readInt();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeFloat(getX);
        buf.writeFloat(getY);
        buf.writeFloat(getZ);
        buf.writeFloat(getYRot);
        buf.writeFloat(getXRot);
        buf.writeInt(getStand);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ServerLevel level = (ServerLevel) player.level();
                MainUtil.handleMovePilot(getX,getY,getZ,getYRot,getXRot,player,getStand);
            }
        });
        return true;
    }
}

