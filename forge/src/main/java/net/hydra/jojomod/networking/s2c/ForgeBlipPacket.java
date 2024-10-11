package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class ForgeBlipPacket {
    private final byte activePower;
    private final int data;
    private final Vector3f vec;

    public ForgeBlipPacket(byte activePowers, int data, Vector3f vec){
        this.activePower = activePowers;
        this.data = data;
        this.vec = vec;
    }
    public ForgeBlipPacket(FriendlyByteBuf buf){
        this.activePower = buf.readByte();
        this.data = buf.readInt();
        this.vec = buf.readVector3f();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeByte(activePower);
        buf.writeInt(data);
        buf.writeVector3f(vec);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ClientUtil.handleBlipPacketS2C(data,activePower,vec);
        });
        return true;
    }
}
