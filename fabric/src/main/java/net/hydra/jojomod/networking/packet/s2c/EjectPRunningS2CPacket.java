package net.hydra.jojomod.networking.packet.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersD4C;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class EjectPRunningS2CPacket {
    public static void eject(Minecraft client, ClientPacketListener handler,
                                    FriendlyByteBuf buf, PacketSender responseSender)
    {
        if (client.player != null)
        {
            if (((StandUser)client.player).roundabout$getStandPowers() instanceof PowersD4C d4c)
            {
                d4c.ejectParallelRunning();
            }
        }
    }
}
