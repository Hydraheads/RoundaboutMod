package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeTimeStoppingEntityRemovalPacket {
    private final int entityID;

    public ForgeTimeStoppingEntityRemovalPacket(int entityID){
        this.entityID = entityID;
    }
    public ForgeTimeStoppingEntityRemovalPacket(FriendlyByteBuf buf){
        this.entityID = buf.readInt();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(entityID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ClientUtil.processTSRemovePacket(entityID);
        });
        return true;
    }
}
