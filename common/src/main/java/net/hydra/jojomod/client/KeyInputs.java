package net.hydra.jojomod.client;

import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPowersPlayer;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.FirearmItem;
import net.hydra.jojomod.stand.powers.PowersWhitesnake;
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
    public static int sbaClickCount = 0;

    public static void summonKey(Player player, Minecraft client){
        if (player.isUsingItem() && player.getUseItem().getItem() instanceof FirearmItem fi){
            if (sbaClickCount == 0) {
                sbaClickCount = 2;
                C2SPacketUtil.trySingleBytePacket(PacketDataIndex.RELOAD_GUN);
            }
        } if (PowerTypes.hasStandActivelyEquipped(player)){
            StandUser user = ((StandUser) player);
            if (user.sba$getStandPowers().canSummonStand() && !user.sba$isSealed() && !user.sba$isPossessed()) {
                if (((StandUser) player).sba$getSummonCD() && sbaClickCount == 0) {
                    if (user.sba$getActive()) {
                        user.sba$setSummonCD(8);
                        if (!(user.sba$getStandPowers() instanceof PowersWhitesnake powers
                                && powers.tryRetreatBeforeUnsummon())) {
                            user.sba$setActive(false);
                            user.sba$tryPower(PowerIndex.NONE, true);
                        }
                    } else {
                        user.sba$setActive(true);
                        user.sba$setSummonCD(2);
                    }
                    C2SPacketUtil.standSummonPacket();
                }
                sbaClickCount = 2;
            }
        } else {
            if (PowerTypes.getPowerType(player) != PowerTypes.NONE.ordinal()){
                StandUser user = ((StandUser) player);
                if (!user.sba$isPossessed()){
                    if (((StandUser) player).sba$getSummonCD() && sbaClickCount == 0) {
                        if (user.sba$getActive()) {
                            user.sba$setSummonCD(8);
                            user.sba$setActive(false);
                            user.sba$tryPowerP(PowerIndex.NONE, true);
                        } else {
                            user.sba$setActive(true);
                            user.sba$setSummonCD(2);
                        }
                        C2SPacketUtil.standSummonPacket();
                    }
                    sbaClickCount = 2;
                }
            }
        }
    }

    public static void menuKey(Player player, Minecraft client){
        if (((StandUser) player).sba$getSummonCD() && sbaClickCount == 0
        && !player.isSpectator()) {
            PlayerMaskSlots ms = ((IPlayerEntity)player).sba$getMaskInventory();
            ItemStack stack = ms.getItem(0);
            ItemStack stack2 = ms.getItem(1);
            if (!stack.equals(((IPlayerEntity)player).sba$getMaskSlot())){
                ms.setItem(0,((IPlayerEntity)player).sba$getMaskSlot());
            }
            if (!stack2.equals(((IPlayerEntity)player).sba$getMaskVoiceSlot())){
                ms.setItem(1,((IPlayerEntity)player).sba$getMaskVoiceSlot());
            }
            C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_BYTE_OPEN_POWER_INVENTORY);
            ((IPlayerEntity)player).sba$setUnlockedBonusSkin(false);
        }
        sbaClickCount = 2;
    }

    public static void forceSummon(Player player,boolean keyIsDown){
        if (keyIsDown) {
            if (!((StandUser) player).sba$getStandDisc().isEmpty() && !((StandUser) player).sba$getActive()) {
                if (((StandUser) player).sba$getStandPowers().canSummonStand() && !((StandUser) player).sba$isSealed()) {
                    if (((StandUser) player).sba$getSummonCD() && sbaClickCount == 0 &&
                            ConfigManager.getClientConfig().pressingAbilityKeysSummonsStands) {
                        ((StandUser) player).sba$setActive(true);
                        ((StandUser) player).sba$setSummonCD(2);
                        C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_BYTE_SILENT_SUMMON);

                    }
                }
            }
        }
    }
    public static void strikePose(Player player, Minecraft client, boolean keyIsDown, Options option){
        ClientUtil.strikePose(player,client,keyIsDown,option);
    }
    public static void strikePower(Player player, Minecraft client, boolean keyIsDown, Options option){
        ClientUtil.strikePower(player,client,keyIsDown,option);
    }
    public static void hideIcons(Player player, Minecraft client, boolean keyIsDown, Options option){
        ClientUtil.hideIcons(player,client,keyIsDown,option);
    }
    public static void switchRowsKey(Player player, Minecraft client, boolean keyIsDown, Options option){
        ((StandUser) player).sba$getStandPowers().switchRowsKey(keyIsDown, option);
    }
    public static void showEXPKey(Player player, Minecraft client, boolean keyIsDown, Options option){
        ((IPlayerEntity) player).sba$showExp(keyIsDown);
    }
    public static void MoveKey4(Player player, Minecraft client, boolean keyIsDown, Options option){
        if (!((StandUser)player).sba$isPossessed()) {
            if (PowerTypes.hasStandActivelyEquipped(player)) {
                if (!((StandUser) player).sba$isSealed()) {
                    if (FateTypes.isHuman(player)) {
                        forceSummon(player, keyIsDown);
                    }
                    ((StandUser) player).sba$getStandPowers().preButtonInput4(keyIsDown, option);
                }
            } else {
                ((IPowersPlayer) player).rdbt$getPowers().preButtonInput4(keyIsDown, option);
            }
            if (!FateTypes.isHuman(player)) {
                ((IFatePlayer) player).rdbt$getFatePowers().preButtonInput4(keyIsDown, option);
            }
        }
    }
    public static void MoveKey3(Player player, Minecraft client, boolean keyIsDown, Options option){
        if (!((StandUser)player).sba$isPossessed()) {
            if (PowerTypes.hasStandActivelyEquipped(player)) {
                if (!((StandUser) player).sba$isSealed()) {
                    if (FateTypes.isHuman(player)) {
                        forceSummon(player, keyIsDown);
                    }
                    ((StandUser) player).sba$getStandPowers().preButtonInput3(keyIsDown, option);
                }
            } else {
                ((IPowersPlayer) player).rdbt$getPowers().preButtonInput3(keyIsDown, option);
            }
            if (!FateTypes.isHuman(player)) {
                ((IFatePlayer) player).rdbt$getFatePowers().preButtonInput3(keyIsDown, option);
            }
        }
    }
    public static void MoveKey2(Player player, Minecraft client, boolean keyIsDown, Options option){
        if (!((StandUser)player).sba$isPossessed()) {
            if (PowerTypes.hasStandActivelyEquipped(player)) {
                if (!((StandUser) player).sba$isSealed()) {
                    if (FateTypes.isHuman(player)) {
                        forceSummon(player, keyIsDown);
                    }
                    ((StandUser) player).sba$getStandPowers().preButtonInput2(keyIsDown, option);
                }
            } else {
                ((IPowersPlayer) player).rdbt$getPowers().preButtonInput2(keyIsDown, option);
            }
            if (!FateTypes.isHuman(player)) {
                ((IFatePlayer) player).rdbt$getFatePowers().preButtonInput2(keyIsDown, option);
            }
        }
    }
    public static void MoveKey1(Player player, Minecraft client, boolean keyIsDown, Options option){
        if (!((StandUser)player).sba$isPossessed()) {
            if (PowerTypes.hasStandActivelyEquipped(player)) {
                if (!((StandUser) player).sba$isSealed()) {
                    if (FateTypes.isHuman(player)) {
                        forceSummon(player, keyIsDown);
                    }
                    ((StandUser) player).sba$getStandPowers().preButtonInput1(keyIsDown, option);
                }
            } else {
                ((IPowersPlayer) player).rdbt$getPowers().preButtonInput1(keyIsDown, option);
            }
            if (!FateTypes.isHuman(player)) {
                ((IFatePlayer) player).rdbt$getFatePowers().preButtonInput1(keyIsDown, option);
            }
        }
    }



}
