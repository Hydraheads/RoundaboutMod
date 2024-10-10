package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeUpdateDazePacket {
    private final byte dazeTime;

    public ForgeUpdateDazePacket(byte dazeTime){
        this.dazeTime = dazeTime;
    }
    public ForgeUpdateDazePacket(FriendlyByteBuf buf){
        this.dazeTime = buf.readByte();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeByte(dazeTime);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            MainUtil.updateDazePacket(dazeTime);
        });
        return true;
    }
}
