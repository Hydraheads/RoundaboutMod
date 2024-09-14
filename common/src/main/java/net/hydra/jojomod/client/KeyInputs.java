package net.hydra.jojomod.client;

import net.hydra.jojomod.client.gui.PowerInventoryScreen;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.world.entity.player.Player;

public class KeyInputs {

    /**Code ran by keys when they are pressed. Leave this fairly generic
     * for stand abilities so they can override requirements to use*/
    public static int roundaboutClickCount = 0;

    public static void summonKey(Player player, Minecraft client){
        if (((StandUser) player).getStandPowers().canSummonStand()) {
            if (((StandUser) player).getSummonCD() && roundaboutClickCount == 0) {
                if (((StandUser) player).getActive()) {
                    ((StandUser) player).setSummonCD(8);
                    ((StandUser) player).setActive(false);
                    ((StandUser) player).tryPower(PowerIndex.NONE, true);
                } else {
                    ((StandUser) player).setActive(true);
                    ((StandUser) player).setSummonCD(2);
                }
                ModPacketHandler.PACKET_ACCESS.standSummonPacket();
            }
            roundaboutClickCount = 2;
        }
    }

    public static void menuKey(Player player, Minecraft client){
        if (((StandUser) player).getSummonCD() && roundaboutClickCount == 0) {
            client.setScreen(new PowerInventoryScreen(player));
            roundaboutClickCount = 2;
        }
    }

    public static void MoveKey4(Player player, Minecraft client, boolean keyIsDown, Options option){
        ((StandUser) player).getStandPowers().buttonInput4(keyIsDown, option);
    }
    public static void MoveKey3(Player player, Minecraft client, boolean keyIsDown, Options option){
        ((StandUser) player).getStandPowers().buttonInput3(keyIsDown, option);
    }
    public static void MoveKey2(Player player, Minecraft client, boolean keyIsDown, Options option){
        ((StandUser) player).getStandPowers().buttonInput2(keyIsDown, option);
    }
    public static void MoveKey1(Player player, Minecraft client, boolean keyIsDown, Options option){
        ((StandUser) player).getStandPowers().buttonInput1(keyIsDown, option);
    }



}
