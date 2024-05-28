package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.event.powers.StandUserClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgePlaySoundPacket {
    private final int startPlayerID;
    private final byte soundQue;

    public ForgePlaySoundPacket(int cancelPlayerID, byte soundQue){
        this.startPlayerID = cancelPlayerID;
        this.soundQue = soundQue;
    }
    public ForgePlaySoundPacket(FriendlyByteBuf buf){
        this.startPlayerID = buf.readInt();
        this.soundQue = buf.readByte();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(startPlayerID);
        buf.writeByte(soundQue);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                Entity User = player.level().getEntity(startPlayerID);
                ((StandUserClient)User).clientQueSound(soundQue);
            }
        });
        return true;
    }
}
