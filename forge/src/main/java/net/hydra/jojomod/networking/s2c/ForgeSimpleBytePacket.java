package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeSimpleBytePacket {
    private final byte activePower;

    public ForgeSimpleBytePacket(byte activePowers){
        this.activePower = activePowers;
    }
    public ForgeSimpleBytePacket(FriendlyByteBuf buf){
        this.activePower = buf.readByte();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeByte(activePower);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                MainUtil.handleSimpleBytePacketS2C(player,activePower);
            }
        });
        return true;
    }
}
