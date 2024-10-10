package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeStopSoundPacket {
    private final int cancelPlayerID;
    private final byte soundID;

    public ForgeStopSoundPacket(int cancelPlayerID, byte soundID){
        this.cancelPlayerID = cancelPlayerID;
        this.soundID = soundID;
    }
    public ForgeStopSoundPacket(FriendlyByteBuf buf){
        this.cancelPlayerID = buf.readInt();
        this.soundID = buf.readByte();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(cancelPlayerID);
        buf.writeByte(soundID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            MainUtil.handleStopSoundPacket(cancelPlayerID,soundID);
        });
        return true;
    }
}
