package net.hydra.jojomod.networking.c2s;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeSwitchPowerPacket {
    private final byte power;

    public ForgeSwitchPowerPacket(byte power){
        this.power = power;
    }
    public ForgeSwitchPowerPacket(FriendlyByteBuf buf){
        this.power = buf.readByte();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeByte(power);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ServerLevel level = (ServerLevel) player.level();
                ((StandUser) player).roundabout$tryPower(power, true);
            }
        });
        return true;
    }
}
