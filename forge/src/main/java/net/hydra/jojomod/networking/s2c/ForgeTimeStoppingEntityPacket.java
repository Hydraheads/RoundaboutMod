package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeTimeStoppingEntityPacket {
    private final int entityID;
    private final double x;
    private final double y;
    private final double z;
    private final double range;
    private final int duration;
    private final int maxDuration;

    public ForgeTimeStoppingEntityPacket(int entityID, double x, double y, double z, double range, int duration, int maxDuration){
        this.entityID = entityID;
        this.x = x;
        this.y = y;
        this.z = z;
        this.range = range;
        this.duration = duration;
        this.maxDuration = maxDuration;
    }
    public ForgeTimeStoppingEntityPacket(FriendlyByteBuf buf){
        this.entityID = buf.readInt();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.range = buf.readDouble();
        this.duration = buf.readInt();
        this.maxDuration = buf.readInt();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(entityID);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeDouble(range);
        buf.writeInt(duration);
        buf.writeInt(maxDuration);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ClientUtil.handleTimeStoppingEntityPacket(entityID,x,y,z,range,duration,maxDuration);
        });
        return true;
    }
}
