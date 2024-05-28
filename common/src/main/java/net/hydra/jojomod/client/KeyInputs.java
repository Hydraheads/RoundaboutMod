package net.hydra.jojomod.client;

import net.hydra.jojomod.client.gui.PowerInventoryScreen;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class KeyInputs {

    //This is what the keys do, what code they run

    public static void summonKey(Player player, Minecraft client){
        if (((StandUser) player).getSummonCD()) {
            if (((StandUser) player).getActive()){
                ((StandUser) player).setSummonCD(8);
            } else {
                ((StandUser) player).setSummonCD(2);
            }
            ModPacketHandler.PACKET_ACCESS.standSummonPacket();
        }
    }

    public static void menuKey(Player player, Minecraft client){
        client.setScreen(new PowerInventoryScreen(player));
    }

    public static void specialMoveKey(Player player, Minecraft client){
        player.sendSystemMessage(Component.nullToEmpty("Special Move"));
    }

}
