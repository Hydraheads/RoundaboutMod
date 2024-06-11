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
    private final double x;
    private final double y;
    private final double z;
    private final double range;

    public ForgeTimeStoppingEntityPacket(int entityID, double x, double y, double z, double range){
        this.entityID = entityID;
        this.x = x;
        this.y = y;
        this.z = z;
        this.range = range;
    }
    public ForgeTimeStoppingEntityPacket(FriendlyByteBuf buf){
        this.entityID = buf.readInt();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.range = buf.readDouble();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(entityID);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeDouble(range);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                ((TimeStop) player.level()).processTSPacket(entityID,x,y,z,range);
            }
        });
        return true;
    }
}
