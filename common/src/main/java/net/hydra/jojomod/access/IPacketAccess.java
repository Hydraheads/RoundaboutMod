package net.hydra.jojomod.access;

import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public interface IPacketAccess {
    /**Forge and Fabric both use this interface to use their own packet handling code.
     * Every packet function must be written here.*/

    /**Server Packets*/
    void StandGuardPointPacket(ServerPlayer sp, float guard, boolean broken);
    void DazeTimePacket(ServerPlayer sp, byte dazeTime);
    void NBTSyncPacket(ServerPlayer sp, CompoundTag NBT);
    void syncCooldownPacket(ServerPlayer sp, int attackTime, int attackTimeMax, int attackTimeDuring,
                            byte activePower, byte activePowerPhase);
    void updateClashPacket(ServerPlayer sp, int id, float clashProgress);
    void stopSoundPacket(ServerPlayer sp, int id);

    void startSoundPacket(ServerPlayer sp, int id, byte soundNo);

    void timeStoppingEntityPacket(ServerPlayer sp, int entityID, double x, double y, double z, double range);
    void timeStoppingEntityRemovalPacket(ServerPlayer sp, int entityID);
    void resumeTileEntityTSPacket(ServerPlayer sp, Vec3i vec3i);

    /**Client Packets*/
    void StandGuardCancelClientPacket();
    void StandPowerPacket(byte power);
    void StandPunchPacket(int targetID, byte APP);
    void StandBarrageHitPacket(int targetID, int ATD);

    void updateClashPacket(float clashProgress, boolean clashDone);

    void moveSyncPacket(byte forward, byte strafe);
    void standSummonPacket();
}
