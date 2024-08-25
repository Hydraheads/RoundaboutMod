package net.hydra.jojomod.networking.c2s;

import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeSingleByteC2SPacket {
    private final byte dataContext;

    public ForgeSingleByteC2SPacket(byte dataContext){
        this.dataContext = dataContext;
    }
    public ForgeSingleByteC2SPacket(FriendlyByteBuf buf){
        this.dataContext = buf.readByte();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeByte(dataContext);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ServerLevel level = (ServerLevel) player.level();
                MainUtil.handleSingleBytePacketC2S(player, dataContext);
            }
        });
        return true;
    }
}