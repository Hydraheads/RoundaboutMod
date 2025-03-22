package net.hydra.jojomod.networking.c2s;

import net.hydra.jojomod.entity.stand.D4CEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.world.DynamicWorld;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeRequestDynamicWorldC2S {

    public ForgeRequestDynamicWorldC2S(){
    }
    public ForgeRequestDynamicWorldC2S(FriendlyByteBuf buf){
    }
    public void toBytes(FriendlyByteBuf buf){
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ServerPlayer player = context.getSender();

            if (player != null && ((StandUser)player).roundabout$getStand() instanceof D4CEntity)
                DynamicWorld.generateD4CWorld(context.getSender().getServer());
        });
        return true;
    }
}
