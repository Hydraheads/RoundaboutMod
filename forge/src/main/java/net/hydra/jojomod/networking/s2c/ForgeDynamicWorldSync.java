package net.hydra.jojomod.networking.s2c;

import com.google.gson.Gson;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeDynamicWorldSync {
    private final String serial;

    public ForgeDynamicWorldSync(String serial){
        this.serial = serial;
    }
    public ForgeDynamicWorldSync(FriendlyByteBuf buf){
        this.serial = buf.readUtf();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeUtf(serial);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(()->{
            ResourceKey<Level> LEVEL_KEY = ResourceKey.create(Registries.DIMENSION, Roundabout.location(serial));
            Roundabout.LOGGER.info("Got packet for dimension {}", LEVEL_KEY.toString());

            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null)
            {
                Roundabout.LOGGER.error("Errored while synchronizing Dynamic World: \"player\" is null!");
                return;
            }

            player.connection.levels().add(LEVEL_KEY);
        });

        return true;
    }
}
