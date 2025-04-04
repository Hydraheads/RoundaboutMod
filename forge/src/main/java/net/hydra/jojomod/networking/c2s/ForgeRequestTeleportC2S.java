package net.hydra.jojomod.networking.c2s;

import net.hydra.jojomod.entity.stand.D4CEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.world.DynamicWorld;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeRequestTeleportC2S {
    private final String world;

    public ForgeRequestTeleportC2S(String world){
        this.world = world;
    }
    public ForgeRequestTeleportC2S(FriendlyByteBuf buf){
        this.world = buf.readUtf();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeUtf(world);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            ServerPlayer player = context.getSender();

            if (player != null && ((StandUser)player).roundabout$getStand() instanceof D4CEntity)
            {
                ServerLevel level = DynamicWorld.levels.get(world);
                if (level == null)
                    return;

                player.changeDimension(level);
            }
        });
        return true;
    }
}
