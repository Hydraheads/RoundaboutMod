package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeSimpleBytePacket {
    private final byte activePower;

    public ForgeSimpleBytePacket(byte activePowers){
        this.activePower = activePowers;
    }
    public ForgeSimpleBytePacket(FriendlyByteBuf buf){
        this.activePower = buf.readByte();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeByte(activePower);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            MainUtil.handleSimpleBytePacketS2C(activePower);
        });
        return true;
    }
}
