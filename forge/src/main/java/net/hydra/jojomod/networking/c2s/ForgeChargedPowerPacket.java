package net.hydra.jojomod.networking.c2s;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeChargedPowerPacket {
    private final byte power;
    private final float chargedTime;

    public ForgeChargedPowerPacket(byte power, float chargedTime){
        this.power = power;
        this.chargedTime = chargedTime;
    }
    public ForgeChargedPowerPacket(FriendlyByteBuf buf){
        this.power = buf.readByte();
        this.chargedTime = buf.readFloat();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeByte(power);
        buf.writeFloat(chargedTime);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ServerLevel level = (ServerLevel) player.level();
                ((StandUser) player).tryChargedPower(power, true, chargedTime);
            }
        });
        return true;
    }
}
