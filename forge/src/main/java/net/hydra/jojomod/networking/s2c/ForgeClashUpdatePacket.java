package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeClashUpdatePacket {
    private final int clashOpID;
    private final float progress;

    public ForgeClashUpdatePacket(int clashOpID, float progress){
        this.clashOpID = clashOpID;
        this.progress = progress;
    }
    public ForgeClashUpdatePacket(FriendlyByteBuf buf){
        this.clashOpID = buf.readInt();
        this.progress = buf.readFloat();;
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(clashOpID);
        buf.writeFloat(progress);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                ((StandUser) player).roundabout$getStandPowers().setClashOp((LivingEntity) player.level().getEntity(clashOpID));
                ((StandUser) player).roundabout$getStandPowers().setClashOpProgress(progress);
            }
        });
        return true;
    }
}
