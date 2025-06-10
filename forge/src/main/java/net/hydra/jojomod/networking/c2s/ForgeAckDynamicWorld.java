package net.hydra.jojomod.networking.c2s;

import net.hydra.jojomod.advancement.criteria.ModCriteria;
import net.hydra.jojomod.entity.stand.D4CEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersD4C;
import net.hydra.jojomod.world.DynamicWorld;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeAckDynamicWorld {
    public ForgeAckDynamicWorld(){}
    public ForgeAckDynamicWorld(FriendlyByteBuf buf){}
    public void toBytes(FriendlyByteBuf buf){}

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();

        context.getSender().server.execute(()-> {
            ServerPlayer player = context.getSender();

            if (player != null && ((StandUser)player).roundabout$getStand() instanceof D4CEntity)
            {
                DynamicWorld world = PowersD4C.queuedWorldTransports.remove(player.getId());
                if (world != null && world.getLevel() != null) {
                    player.teleportTo(world.getLevel(), player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
                    ((StandUser)player).roundabout$summonStand(world.getLevel(), true, false);
                    ModCriteria.DIMENSION_HOP_TRIGGER.trigger(player);
                }
            }
        });
        return true;
    }
}
