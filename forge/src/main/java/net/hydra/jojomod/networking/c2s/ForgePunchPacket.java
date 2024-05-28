package net.hydra.jojomod.networking.c2s;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgePunchPacket {
    private final int targetEntity;
    private final byte APP;

    public ForgePunchPacket(int targetEntity, byte APP){
        this.targetEntity = targetEntity;
        this.APP = APP;
    }
    public ForgePunchPacket(FriendlyByteBuf buf){
        this.targetEntity = buf.readInt();
        this.APP = buf.readByte();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(targetEntity);
        buf.writeByte(APP);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ServerLevel level = (ServerLevel) player.level();
                Entity TE = player.level().getEntity(targetEntity);
                ((StandUser) player).getStandPowers().setActivePowerPhase(APP);
                ((StandUser) player).getStandPowers().punchImpact(TE);
            }
        });
        return true;
    }
}
