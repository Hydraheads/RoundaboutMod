package net.hydra.jojomod.client;

import net.hydra.jojomod.client.gui.PowerInventoryMenu;
import net.hydra.jojomod.client.gui.PowerInventoryScreen;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.HorseInventoryMenu;

public class KeyInputs {

    /**Code ran by keys when they are pressed. Leave this fairly generic
     * for stand abilities so they can override requirements to use*/
    public static int roundaboutClickCount = 0;

    public static void summonKey(Player player, Minecraft client){
        if (((StandUser) player).roundabout$getStandPowers().canSummonStand() && ((StandUser) player).roundabout$getSealedTicks() <= -1) {
            if (((StandUser) player).roundabout$getSummonCD() && roundaboutClickCount == 0) {
                if (((StandUser) player).roundabout$getActive()) {
                    ((StandUser) player).roundabout$setSummonCD(8);
                    ((StandUser) player).roundabout$setActive(false);
                    ((StandUser) player).roundabout$tryPower(PowerIndex.NONE, true);
                } else {
                    ((StandUser) player).roundabout$setActive(true);
                    ((StandUser) player).roundabout$setSummonCD(2);
                }
                ModPacketHandler.PACKET_ACCESS.standSummonPacket();
            }
            roundaboutClickCount = 2;
        }
    }

    public static void menuKey(Player player, Minecraft client){
        if (((StandUser) player).roundabout$getSummonCD() && roundaboutClickCount == 0) {
            roundaboutClickCount = 2;

            PowerInventoryMenu powa = new PowerInventoryMenu(player.getInventory(), true, player);
            player.containerMenu = powa;
            client.setScreen(new PowerInventoryScreen(player));
        }
    }

    public static void forceSummon(Player player,boolean keyIsDown){
        if (keyIsDown) {
            if (!((StandUser) player).roundabout$getStandDisc().isEmpty() && !((StandUser) player).roundabout$getActive()) {
                if (((StandUser) player).roundabout$getStandPowers().canSummonStand() && ((StandUser) player).roundabout$getSealedTicks() <= -1) {
                    if (((StandUser) player).roundabout$getSummonCD() && roundaboutClickCount == 0) {
                        ((StandUser) player).roundabout$setActive(true);
                        ((StandUser) player).roundabout$setSummonCD(2);
                        ModPacketHandler.PACKET_ACCESS.singleByteToServerPacket(PacketDataIndex.SINGLE_BYTE_SILENT_SUMMON);

                    }
                }
            }
        }
    }
    public static void switchRowsKey(Player player, Minecraft client, boolean keyIsDown, Options option){
        ((StandUser) player).roundabout$getStandPowers().switchRowsKey(keyIsDown, option);
    }
    public static void MoveKey4(Player player, Minecraft client, boolean keyIsDown, Options option){
        forceSummon(player,keyIsDown);
        ((StandUser) player).roundabout$getStandPowers().preButtonInput4(keyIsDown, option);
    }
    public static void MoveKey3(Player player, Minecraft client, boolean keyIsDown, Options option){
        forceSummon(player,keyIsDown);
        ((StandUser) player).roundabout$getStandPowers().preButtonInput3(keyIsDown, option);
    }
    public static void MoveKey2(Player player, Minecraft client, boolean keyIsDown, Options option){
        forceSummon(player,keyIsDown);
        ((StandUser) player).roundabout$getStandPowers().preButtonInput2(keyIsDown, option);
    }
    public static void MoveKey1(Player player, Minecraft client, boolean keyIsDown, Options option){
        forceSummon(player,keyIsDown);
        ((StandUser) player).roundabout$getStandPowers().preButtonInput1(keyIsDown, option);
    }



}
