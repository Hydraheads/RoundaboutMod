package net.hydra.jojomod.networking.packet.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class DynamicWorldDeregister {
    public static void updateWorlds(Minecraft client, ClientPacketListener handler,
                               FriendlyByteBuf buf, PacketSender responseSender)
    {
        String name = buf.readUtf();
        ResourceKey<Level> LEVEL_KEY = ResourceKey.create(Registries.DIMENSION, Roundabout.location(name));

        LocalPlayer player = client.player;

        player.connection.levels().remove(LEVEL_KEY);
    }
}
