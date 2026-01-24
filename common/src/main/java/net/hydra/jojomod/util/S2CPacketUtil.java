package net.hydra.jojomod.util;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.VampireData;
import net.hydra.jojomod.networking.ServerToClientPackets;
import net.hydra.jojomod.stand.powers.PowersCream;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.zetalasis.networking.message.api.ModMessageEvents;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

/**it is NOT okay to have client only classes here
 *  This is where we send packets from the server to the client :O
 *  See other class, ServerToClientPackets
 *   */
public class S2CPacketUtil {

    public static void synchDaze(Player player, byte time){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.SyncDaze.value,
                    time
            );
        }
    }

    public static void heelExtend(Player player, int entid){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.HeelExtend.value,
                    entid
            );
        }
    }
    public static void refreshCooldowns(Player player){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.RefreshAllCooldowns.value
            );
        }
    }

    public static void beamVampireData(Player player){
        if (player instanceof ServerPlayer SP) {
            VampireData data = ((IPlayerEntity)player).rdbt$getVampireData();
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.UpdateVampireData.value,
                    data.vampireLevel,
                    data.bloodExp,
                    data.animalExp,
                    data.monsterEXP,
                    data.npcExp,
                    data.timeSinceAnimal,
                    data.timeSinceMonster,
                    data.timeSinceNpc,

                    data.strengthLevel,
                    data.dexterityLevel,
                    data.resilienceLevel,

                    data.hypnotismLevel,
                    data.superHearingLevel,
                    data.bloodSpeedLevel,

                    data.graftingLevel,
                    data.fleshBudLevel,
                    data.daggerSplatterLevel,

                    data.jumpLevel,
                    data.ripperEyesLevel,
                    data.freezeLevel
            );
        }
    }
    public static void beamVampireData2(Player player){
        if (player instanceof ServerPlayer SP) {
            VampireData data = ((IPlayerEntity)player).rdbt$getVampireData();
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.UpdateVampireData2.value,
                    data.vampireLevel,
                    data.bloodExp,
                    data.animalExp,
                    data.monsterEXP,
                    data.npcExp,
                    data.timeSinceAnimal,
                    data.timeSinceMonster,
                    data.timeSinceNpc
            );
        }
    }

    public static void beamVampireTimings(Player player){
        if (player instanceof ServerPlayer SP) {
            VampireData data = ((IPlayerEntity)player).rdbt$getVampireData();
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.UpdateVampireData3.value,
                    data.timeSinceAnimal,
                    data.timeSinceMonster,
                    data.timeSinceNpc
            );
        }
    }
    public static void synchGuard(Player player, float guardPoints, boolean guardBroken){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.SyncGuard.value,
                    guardPoints,
                    guardBroken
            );
        }
    }
    public static void updateBarrageClashS2C(Player player, int id, float clashProgress){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.UpdateBarrageClash.value,
                    id,
                    clashProgress
            );
        }
    }
    public static void affirmCooldownsS2C(Player player){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.AffirmAllCooldowns.value
            );
        }
    }

    public static void sendConfigPacket(Player player){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.SendConfig.value,
                    ConfigManager.serializeConfig()
            );
        }
    }
    public static void sendPlaySoundPacket(Player player, int entID, byte soundID){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.PlaySound.value,
                    entID,
                    soundID
            );
        }
    }
    public static void sendCancelSoundPacket(Player player, int entID, byte soundID){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.StopSound.value,
                    entID,
                    soundID
            );
        }
    }
    public static void sendBlipPacket(Player player, byte activePower, int data, Vector3f blip){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.Blip.value,
                    data,
                    activePower,
                    blip
            );
        }
    }
    public static void sendCooldownSyncPacket(Player player, byte moveOnCooldown, int cooldown){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.SyncCooldown.value,
                    moveOnCooldown,
                    cooldown
            );
        }
    }
    public static void sendMaxCooldownSyncPacket(Player player, byte moveOnCooldown, int cooldown, int maxCooldown){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.SyncCooldownMax.value,
                    moveOnCooldown,
                    cooldown,
                    maxCooldown
            );
        }
    }
    public static void sendActivePowerPacket(Player player, byte activePower){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.SyncActivePower.value,
                    activePower
            );
        }
    }
    public static void sendActivePowerFatePacket(Player player, byte activePower){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.SyncActivePowerFate.value,
                    activePower
            );
        }
    }
    public static void sendActivePowerPowersPacket(Player player, byte activePower){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.SyncActivePowerPowers.value,
                    activePower
            );
        }
    }

    public static void sendPowerInventorySettings(Player player, int anchorPlace, float distanceOut, float idleOpacity,
                                                  float combatOpacity, float enemyOpacity, int anchorPlaceAttack){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.SyncPowerInventory.value,
                    anchorPlace,
                    distanceOut,
                    idleOpacity,
                    combatOpacity,
                    enemyOpacity,
                    anchorPlaceAttack
            );
        }
    }
    public static void sendIntPowerDataPacket(Player player, byte activePower, int data){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.IntPowerData.value,
                    activePower,
                    data
            );
        }
    }
    public static void sendGenericIntToClientPacket(Player player, byte context, int data){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.IntToClient.value,
                    context,
                    data
            );
        }
    }
    public static void sendGenericIntIntToClientPacket(Player player, byte context, int data1, int data){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.DoubleIntToClient.value,
                    context,
                    data1,
                    data
            );
        }
    }
    public static void sendSimpleByteToClientPacket(Player player, byte context){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.SimpleByteToClient.value,
                    context
            );
        }
    }
    public static void sendByteBundleToClientPacket(Player player, byte context, byte data1, byte data2){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.ByteBundleToClient.value,
                    context,
                    data1,
                    data2
            );
        }
    }
    public static void addTSEntity(Player player, int entityID, double x, double y, double z, double range,
                                   int duration, int maxDuration){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.AddTSEntity.value,
                    entityID,
                    x,
                    y,
                    z,
                    range,
                    duration,
                    maxDuration
            );
        }
    }
    public static void removeTSEntity(Player player, int entityID){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.RemoveTSEntity.value,
                    entityID
            );
        }
    }
    public static void addPCEntity(Player player, int entityID, double x, double y, double z, double range,
                                   byte context){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.AddPCEntity.value,
                    entityID,
                    x,
                    y,
                    z,
                    range,
                    context
            );
        }
    }
    public static void removePCEntity(Player player, int entityID){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.RemovePCEntity.value,
                    entityID
            );
        }
    }
    public static void resumeTileEntityTSPacket(Player player, Vec3i vec3i){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.ResumeTileEntityTS.value,
                    vec3i.getX(),
                    vec3i.getY(),
                    vec3i.getZ()
            );
        }
    }
    public static void sendNewDyanmicWorldPacket(Player player, String name){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.SendNewDynamicWorld.value,
                    name
            );
        }
    }
    public static void ejectParallelRunningPacket(Player player){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.EjectPRunning.value
            );
        }
    }
    public static void creamUpdateTimer(Player player, int time){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.CreamUpdateTimer.value,
                    time
            );
        }
    }
    public static void vampireMessage(Player player){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.VampireMessage.value
            );
        }
    }
    public static void gunRecoil(Player player, String string){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.GunRecoil.value, string
            );
        }
    }
    public static void creamUpdateTransformTimer(Player player, int time){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.CreamUpdateTransformTimer.value,
                    time
            );
        }
    }
    public static void creamUpdateTransformDirection(Player player, int transformDirectionValue){
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.CreamUpdateTransformDirection.value,
                    transformDirectionValue
            );
        }
    }

    public static void syncPossessor(Player player, int i) {
        if (player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.SyncPossessor.value,
                    i
            );
        }
    }

    public static void shatterIce(int entityId) {
            ModMessageEvents.sendToAll(
                    ServerToClientPackets.S2CPackets.MESSAGES.ShatterIce.value,
                    entityId
            );
    }

    public static void sync_allies(Player player,String allies) {
        if(player instanceof ServerPlayer SP) {
            ModMessageEvents.sendToPlayer(SP,
                    ServerToClientPackets.S2CPackets.MESSAGES.SyncAllies.value,
                    allies
            );
        }
    }
}
