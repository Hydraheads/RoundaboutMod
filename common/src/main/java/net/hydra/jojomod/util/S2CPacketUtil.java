package net.hydra.jojomod.util;

import net.hydra.jojomod.networking.ServerToClientPackets;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.zetalasis.networking.message.api.ModMessageEvents;

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
}
