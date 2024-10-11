package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgePowerFloatPacket {
    private final byte activePower;
    private final float data;

    public ForgePowerFloatPacket(byte activePowers, float data){
        this.activePower = activePowers;
        this.data = data;
    }
    public ForgePowerFloatPacket(FriendlyByteBuf buf){
        this.activePower = buf.readByte();
        this.data = buf.readFloat();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeByte(activePower);
        buf.writeFloat(data);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ClientUtil.handlePowerFloatPacket(activePower,data);
        });
        return true;
    }
}
