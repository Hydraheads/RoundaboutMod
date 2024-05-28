package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
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
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                ((StandUser) player).setDazeTime(dazeTime);
            }
        });
        return true;
    }
}
