package net.hydra.jojomod.networking.c2s;

import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeFloatC2SPacket {
    private final float data;
    private final byte dataContext;

    public ForgeFloatC2SPacket(float data, byte dataContext){
        this.data = data;
        this.dataContext = dataContext;
    }
    public ForgeFloatC2SPacket(FriendlyByteBuf buf){
        this.data = buf.readFloat();
        this.dataContext = buf.readByte();;
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeFloat(data);
        buf.writeByte(dataContext);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ServerLevel level = (ServerLevel) player.level();
                MainUtil.handleFloatPacketC2S(player, data, dataContext);
            }
        });
        return true;
    }
}
