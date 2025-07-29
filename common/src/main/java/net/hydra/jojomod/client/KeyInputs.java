package net.hydra.jojomod.client;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.PlayerMaskSlots;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class KeyInputs {

    /**Code ran by keys when they are pressed. Leave this fairly generic
     * for stand abilities so they can override requirements to use*/
    public static int roundaboutClickCount = 0;

    public static void summonKey(Player player, Minecraft client){
        StandUser user = ((StandUser) player);
        if (user.roundabout$getStandPowers().canSummonStand() && !user.roundabout$isSealed()) {
            if (((StandUser) player).roundabout$getSummonCD() && roundaboutClickCount == 0) {
                if (user.roundabout$getActive()) {
                    user.roundabout$setSummonCD(8);
                    user.roundabout$setActive(false);
                    user.roundabout$tryPower(PowerIndex.NONE, true);
                } else {
                    user.roundabout$setActive(true);
                    user.roundabout$setSummonCD(2);
                }
                ModPacketHandler.PACKET_ACCESS.standSummonPacket();
            }
            roundaboutClickCount = 2;
        }
    }

    public static void menuKey(Player player, Minecraft client){
        if (((StandUser) player).roundabout$getSummonCD() && roundaboutClickCount == 0
        && !player.isSpectator()) {
            forceSummon(player,true);
            PlayerMaskSlots ms = ((IPlayerEntity)player).roundabout$getMaskInventory();
            ItemStack stack = ms.getItem(0);
            ItemStack stack2 = ms.getItem(1);
            if (!stack.equals(((IPlayerEntity)player).roundabout$getMaskSlot())){
                ms.setItem(0,((IPlayerEntity)player).roundabout$getMaskSlot());
            }
            if (!stack2.equals(((IPlayerEntity)player).roundabout$getMaskVoiceSlot())){
                ms.setItem(1,((IPlayerEntity)player).roundabout$getMaskVoiceSlot());
            }
            C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_BYTE_OPEN_POWER_INVENTORY);
            ((IPlayerEntity)player).roundabout$setUnlockedBonusSkin(false);
        }
        roundaboutClickCount = 2;
    }

    public static void forceSummon(Player player,boolean keyIsDown){
        if (keyIsDown) {
            if (!((StandUser) player).roundabout$getStandDisc().isEmpty() && !((StandUser) player).roundabout$getActive()) {
                if (((StandUser) player).roundabout$getStandPowers().canSummonStand() && !((StandUser) player).roundabout$isSealed()) {
                    if (((StandUser) player).roundabout$getSummonCD() && roundaboutClickCount == 0 &&
                            ConfigManager.getClientConfig().pressingAbilityKeysSummonsStands) {
                        ((StandUser) player).roundabout$setActive(true);
                        ((StandUser) player).roundabout$setSummonCD(2);
                        C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_BYTE_SILENT_SUMMON);

                    }
                }
            }
        }
    }
    public static void strikePose(Player player, Minecraft client, boolean keyIsDown, Options option){
        ClientUtil.strikePose(player,client,keyIsDown,option);
    }
    public static void switchRowsKey(Player player, Minecraft client, boolean keyIsDown, Options option){
        ((StandUser) player).roundabout$getStandPowers().switchRowsKey(keyIsDown, option);
    }
    public static void showEXPKey(Player player, Minecraft client, boolean keyIsDown, Options option){
        ((IPlayerEntity) player).roundabout$showExp(keyIsDown);
    }
    public static void MoveKey4(Player player, Minecraft client, boolean keyIsDown, Options option){
        if (!((StandUser) player).roundabout$isSealed()){
            forceSummon(player,keyIsDown);
            ((StandUser) player).roundabout$getStandPowers().preButtonInput4(keyIsDown, option);
        }
    }
    public static void MoveKey3(Player player, Minecraft client, boolean keyIsDown, Options option){
        if (!((StandUser) player).roundabout$isSealed()) {
            forceSummon(player, keyIsDown);
            ((StandUser) player).roundabout$getStandPowers().preButtonInput3(keyIsDown, option);
        }
    }
    public static void MoveKey2(Player player, Minecraft client, boolean keyIsDown, Options option){
        if (!((StandUser) player).roundabout$isSealed()) {
            forceSummon(player, keyIsDown);
            ((StandUser) player).roundabout$getStandPowers().preButtonInput2(keyIsDown, option);
        }
    }
    public static void MoveKey1(Player player, Minecraft client, boolean keyIsDown, Options option){
        if (!((StandUser) player).roundabout$isSealed()) {
            forceSummon(player, keyIsDown);
            ((StandUser) player).roundabout$getStandPowers().preButtonInput1(keyIsDown, option);
        }
    }



}
