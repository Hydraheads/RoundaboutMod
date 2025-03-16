package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.minecraft.network.FriendlyByteBuf;
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

        Roundabout.LOGGER.info("Got packet for new dynamic world {}", serial);

        return true;
    }
}
