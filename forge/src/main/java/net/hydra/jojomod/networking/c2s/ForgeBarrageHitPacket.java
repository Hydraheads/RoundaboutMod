package net.hydra.jojomod.networking.c2s;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeBarrageHitPacket {
    private final int targetEntity;
    private final int hitNumber;

    public ForgeBarrageHitPacket(int targetEntity, int hitNumber){
        this.targetEntity = targetEntity;
        this.hitNumber = hitNumber;
    }
    public ForgeBarrageHitPacket(FriendlyByteBuf buf){
        this.targetEntity = buf.readInt();
        this.hitNumber = buf.readInt();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(targetEntity);
        buf.writeInt(hitNumber);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ServerLevel level = (ServerLevel) player.level();
                Entity TE = player.level().getEntity(targetEntity);
                ((StandUser) player).roundabout$getStandPowers().barrageImpact(TE, hitNumber);
            }
        });
        return true;
    }
}
