package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeEjectPRunning {
    public ForgeEjectPRunning(){
    }
    public ForgeEjectPRunning(FriendlyByteBuf buf){
    }
    public void toBytes(FriendlyByteBuf buf){
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(ClientUtil::d4cEjectParralelRunningForge);

        return true;
    }
}
