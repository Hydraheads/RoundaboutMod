package net.hydra.jojomod.networking.s2c;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.StandUserClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeGuardUpdate {
    private final float guardPoints;
    private final boolean guardBroken;

    public ForgeGuardUpdate(float guardPoints, boolean guardBroken){
        this.guardPoints = guardPoints;
        this.guardBroken = guardBroken;
    }
    public ForgeGuardUpdate(FriendlyByteBuf buf){
        this.guardPoints = buf.readFloat();
        this.guardBroken = buf.readBoolean();
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeFloat(guardPoints);
        buf.writeBoolean(guardBroken);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                ((StandUser) player).setGuardPoints(guardPoints);
                ((StandUser) player).setGuardBroken(guardBroken);
            }
        });
        return true;
    }
}
