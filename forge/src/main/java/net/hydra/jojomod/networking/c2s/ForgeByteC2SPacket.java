package net.hydra.jojomod.networking.c2s;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeByteC2SPacket {
    private final byte data;
    private final byte dataContext;

    public ForgeByteC2SPacket(byte data, byte dataContext){
        this.data = data;
        this.dataContext = dataContext;
    }
    public ForgeByteC2SPacket(FriendlyByteBuf buf){
        this.data = buf.readByte();
        this.dataContext = buf.readByte();;
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeByte(data);
        buf.writeByte(dataContext);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ServerLevel level = (ServerLevel) player.level();
                MainUtil.handleBytePacketC2S(player, data, dataContext);
            }
        });
        return true;
    }
}
