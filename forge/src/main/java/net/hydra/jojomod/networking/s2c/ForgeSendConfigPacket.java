package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeSendConfigPacket {
    private final boolean dedicated;
    private final String serial;

    public ForgeSendConfigPacket(boolean dedicated, String serial){
        this.dedicated = dedicated;
        this.serial = serial;
    }
    public ForgeSendConfigPacket(FriendlyByteBuf buf){
        this.dedicated = buf.readBoolean();
        this.serial = buf.readUtf();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeBoolean(dedicated);
        buf.writeUtf(serial);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ClientNetworking.initialize(dedicated,serial);
        });
        return true;
    }
}
