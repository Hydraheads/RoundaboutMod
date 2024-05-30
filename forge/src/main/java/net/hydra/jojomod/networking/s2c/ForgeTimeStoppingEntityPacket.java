package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.event.powers.StandUserClient;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeTimeStoppingEntityPacket {
    private final int entityID;
    private final boolean removal;

    public ForgeTimeStoppingEntityPacket(int entityID, boolean removal){
        this.entityID = entityID;
        this.removal = removal;
    }
    public ForgeTimeStoppingEntityPacket(FriendlyByteBuf buf){
        this.entityID = buf.readInt();
        this.removal = buf.readBoolean();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(entityID);
        buf.writeBoolean(removal);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                Entity timeStoppingEntity = player.level().getEntity(entityID);
                if (timeStoppingEntity instanceof LivingEntity) {
                    ((TimeStop) player.level()).processTSPacket((LivingEntity) timeStoppingEntity, removal);
                }
            }
        });
        return true;
    }
}
