package net.hydra.jojomod.networking.c2s;

import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.Networking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeHandshakePacket {

    public ForgeHandshakePacket(){
    }
    public ForgeHandshakePacket(FriendlyByteBuf buf){
    }
    public void toBytes(FriendlyByteBuf buf){
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                Networking.sendConfigToPlayer(player);
            }
        });
        return true;
    }
}
