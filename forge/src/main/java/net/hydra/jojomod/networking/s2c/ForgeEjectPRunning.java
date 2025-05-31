package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersD4C;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeEjectPRunning {
    public ForgeEjectPRunning(){
    }
    public ForgeEjectPRunning(FriendlyByteBuf buf){
    }
    public void toBytes(FriendlyByteBuf buf){
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(()->{
            Minecraft client = Minecraft.getInstance();

            if (client.player != null)
            {
                if (((StandUser)client.player).roundabout$getStandPowers() instanceof PowersD4C d4c)
                {
                    d4c.ejectParallelRunning();
                }
            }
        });

        return true;
    }
}
