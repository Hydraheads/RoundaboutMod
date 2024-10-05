package net.hydra.jojomod.networking.c2s;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeClashUpdatePacketC2S {
    private final float clashProg;
    private final boolean clashDone;

    public ForgeClashUpdatePacketC2S(float clashProg, boolean clashDone){
        this.clashProg = clashProg;
        this.clashDone = clashDone;
    }
    public ForgeClashUpdatePacketC2S(FriendlyByteBuf buf){
        this.clashProg = buf.readFloat();
        this.clashDone = buf.readBoolean();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeFloat(clashProg);
        buf.writeBoolean(clashDone);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                if (((StandUser) player).roundabout$isClashing()){
                    ((StandUser) player).roundabout$getStandPowers().setClashProgress(clashProg);
                    ((StandUser) player).roundabout$getStandPowers().setClashDone(clashDone);
                }
            }
        });
        return true;
    }
}
