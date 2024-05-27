package net.hydra.jojomod.access;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public interface IPacketAccess {
    /**Server Packets*/
    void StandGuardPointPacket(ServerPlayer sp, float guard, boolean broken);
    void DazeTimePacket(ServerPlayer sp, byte dazeTime);
    void NBTSyncPacket(ServerPlayer sp, CompoundTag NBT);
    void syncCooldownPacket(ServerPlayer sp, int attackTime, int attackTimeMax, int attackTimeDuring,
                            byte activePower, byte activePowerPhase);
    void updateClashPacket(ServerPlayer sp, int id, float clashProgress);
    void stopSoundPacket(ServerPlayer sp, int id);

    void startSoundPacket(ServerPlayer sp, int id, byte soundNo);

    /**Client Packets*/
    void StandGuardCancelClientPacket();
    void StandPowerPacket(byte power);
    void StandPunchPacket(int targetID, byte APP);
    void StandBarrageHitPacket(int targetID, int ATD);

    void updateClashPacket(float clashProgress, boolean clashDone);

}
