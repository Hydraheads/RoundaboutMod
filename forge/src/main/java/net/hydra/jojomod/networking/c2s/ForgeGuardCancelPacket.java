package net.hydra.jojomod.networking.c2s;

import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeGuardCancelPacket {
    public ForgeGuardCancelPacket(){
    }
    public ForgeGuardCancelPacket(FriendlyByteBuf buf){
    }
    public void toBytes(FriendlyByteBuf buf){
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                if (((StandUser) player).isGuarding() || ((StandUser) player).isBarraging()){
                    ((StandUser) player).tryPower(PowerIndex.NONE,true);
                }
            }
        });
        return true;
    }
}
