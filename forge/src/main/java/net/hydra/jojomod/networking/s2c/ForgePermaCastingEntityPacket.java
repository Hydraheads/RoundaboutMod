package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgePermaCastingEntityPacket {
    private final int entityID;
    private final double x;
    private final double y;
    private final double z;
    private final double range;
    private final byte ctext;

    public ForgePermaCastingEntityPacket(int entityID, double x, double y, double z, double range, byte ctext){
        this.entityID = entityID;
        this.x = x;
        this.y = y;
        this.z = z;
        this.range = range;
        this.ctext = ctext;
    }
    public ForgePermaCastingEntityPacket(FriendlyByteBuf buf){
        this.entityID = buf.readInt();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.range = buf.readDouble();
        this.ctext = buf.readByte();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(entityID);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeDouble(range);
        buf.writeByte(ctext);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ClientUtil.handlePermaCastingEntityPacket(entityID,x,y,z,range,ctext);
        });
        return true;
    }
}

