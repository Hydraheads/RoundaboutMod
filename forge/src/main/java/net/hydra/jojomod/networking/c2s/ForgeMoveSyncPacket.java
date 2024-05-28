package net.hydra.jojomod.networking.c2s;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeMoveSyncPacket {
    private final byte forward;
    private final byte strafe;

    public ForgeMoveSyncPacket(byte forward, byte strafe){
        this.forward = forward;
        this.strafe = strafe;
    }
    public ForgeMoveSyncPacket(FriendlyByteBuf buf){
        this.forward = buf.readByte();
        this.strafe = buf.readByte();;
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeByte(forward);
        buf.writeByte(strafe);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ServerLevel level = (ServerLevel) player.level();
                ((StandUser) player).setDI(forward, strafe);
            }
        });
        return true;
    }
}
