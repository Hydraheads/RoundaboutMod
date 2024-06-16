package net.hydra.jojomod.networking;

import net.hydra.jojomod.access.IPacketAccess;
import net.hydra.jojomod.networking.c2s.*;
import net.hydra.jojomod.networking.s2c.*;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public class ForgePackets implements IPacketAccess {
    @Override
    public void StandGuardPointPacket(ServerPlayer sp, float guard, boolean broken) {
        ForgePacketHandler.sendToClient(new ForgeGuardUpdatePacket(guard,broken), sp);
    }

    @Override
    public void DazeTimePacket(ServerPlayer sp, byte dazeTime) {
        ForgePacketHandler.sendToClient(new ForgeUpdateDazePacket(dazeTime), sp);
    }

    @Override
    public void NBTSyncPacket(ServerPlayer sp, CompoundTag NBT) {
        ForgePacketHandler.sendToClient(new ForgeNBTPacket(NBT), sp);
    }

    @Override
    public void syncCooldownPacket(ServerPlayer sp, int attackTime, int attackTimeMax, int attackTimeDuring, byte activePower, byte activePowerPhase) {
        ForgePacketHandler.sendToClient(new ForgeCDSyncPacket(attackTime,attackTimeMax,attackTimeDuring,activePower,activePowerPhase), sp);
    }

    @Override
    public void updateClashPacket(ServerPlayer sp, int id, float clashProgress) {
        ForgePacketHandler.sendToClient(new ForgeClashUpdatePacket(id, clashProgress), sp);
    }

    @Override
    public void stopSoundPacket(ServerPlayer sp, int id) {
        ForgePacketHandler.sendToClient(new ForgeStopSoundPacket(id), sp);
    }

    @Override
    public void startSoundPacket(ServerPlayer sp, int id, byte soundNo) {
        ForgePacketHandler.sendToClient(new ForgePlaySoundPacket(id, soundNo), sp);
    }

    @Override
    public void timeStoppingEntityPacket(ServerPlayer sp, int entityID, double x, double y, double z, double range, float duration, float maxDuration) {
        ForgePacketHandler.sendToClient(new ForgeTimeStoppingEntityPacket(entityID, x,y,z, range, duration, maxDuration), sp);
    }
    @Override
    public void timeStoppingEntityRemovalPacket(ServerPlayer sp, int entityID) {
        ForgePacketHandler.sendToClient(new ForgeTimeStoppingEntityRemovalPacket(entityID), sp);
    }

    @Override
    public void resumeTileEntityTSPacket(ServerPlayer sp, Vec3i vec3i) {
        ForgePacketHandler.sendToClient(new ForgeBlockEntityResumeTSPacket(vec3i), sp);
    }

    @Override
    public void StandGuardCancelClientPacket() {
        ForgePacketHandler.sendToServer(new ForgeGuardCancelPacket());
    }

    @Override
    public void StandPowerPacket(byte power) {
        ForgePacketHandler.sendToServer(new ForgeSwitchPowerPacket(power));

    }
    @Override
    public void StandChargedPowerPacket(byte power, float chargeTime) {
        ForgePacketHandler.sendToServer(new ForgeChargedPowerPacket(power,chargeTime));

    }
    @Override
    public void StandPunchPacket(int targetID, byte APP) {
        ForgePacketHandler.sendToServer(new ForgePunchPacket(targetID,APP));

    }

    @Override
    public void StandBarrageHitPacket(int targetID, int ATD) {
        ForgePacketHandler.sendToServer(new ForgeBarrageHitPacket(targetID,ATD));
    }

    @Override
    public void updateClashPacket(float clashProgress, boolean clashDone) {
        ForgePacketHandler.sendToServer(new ForgeClashUpdatePacketC2S(clashProgress,clashDone));
    }
    @Override
    public void standSummonPacket() {
        ForgePacketHandler.sendToServer(new ForgeSummonPacket());
    }


    @Override
    public void moveSyncPacket(byte forward, byte strafe) {
        ForgePacketHandler.sendToServer(new ForgeMoveSyncPacket(forward, strafe));
    }

    @Override
    public void timeStopFloat(boolean TSJump) {
        ForgePacketHandler.sendToServer(new ForgeTSJumpPacket(TSJump));
    }

}
