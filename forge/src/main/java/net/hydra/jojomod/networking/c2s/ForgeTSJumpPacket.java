package net.hydra.jojomod.networking.c2s;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeTSJumpPacket {
    private final boolean TSJump;
    public ForgeTSJumpPacket(boolean TSJump){
        this.TSJump = TSJump;
    }
    public ForgeTSJumpPacket(FriendlyByteBuf buf){
        this.TSJump = buf.readBoolean();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeBoolean(TSJump);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ServerLevel level = (ServerLevel) player.level();
                ((StandUser) player).roundabout$setTSJump(TSJump);
            }
        });
        return true;
    }
}
