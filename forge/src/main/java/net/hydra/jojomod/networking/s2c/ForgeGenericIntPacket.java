package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeGenericIntPacket {
    private final byte taskNo;
    private final int theInt;

    public ForgeGenericIntPacket(byte taskNo, int theInt){
        this.taskNo = taskNo;
        this.theInt = theInt;
    }
    public ForgeGenericIntPacket(FriendlyByteBuf buf){
        this.taskNo = buf.readByte();
        this.theInt = buf.readInt();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeByte(taskNo);
        buf.writeInt(theInt);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ClientUtil.handleIntPacketS2C(theInt,taskNo);
        });
        return true;
    }
}
