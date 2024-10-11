package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.Vec3i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeBlockEntityResumeTSPacket {

    private final Vec3i vec3i;
    public ForgeBlockEntityResumeTSPacket(Vec3i vec3i){
        this.vec3i = vec3i;
    }
    public ForgeBlockEntityResumeTSPacket(FriendlyByteBuf buf){
        this.vec3i = new Vec3i(buf.readInt(),buf.readInt(),buf.readInt());
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(vec3i.getX());
        buf.writeInt(vec3i.getY());
        buf.writeInt(vec3i.getZ());
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ClientUtil.handleEntityResumeTsPacket(vec3i);
        });
        return true;
    }
}
