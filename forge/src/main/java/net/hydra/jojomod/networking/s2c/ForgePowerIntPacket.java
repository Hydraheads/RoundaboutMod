package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgePowerIntPacket {
    private final byte activePower;
    private final int data;

    public ForgePowerIntPacket(byte activePowers, int data){
        this.activePower = activePowers;
        this.data = data;
    }
    public ForgePowerIntPacket(FriendlyByteBuf buf){
        this.activePower = buf.readByte();
        this.data = buf.readInt();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeByte(activePower);
        buf.writeInt(data);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ClientUtil.handlePowerIntPacket(activePower,data);
        });
        return true;
    }
}
