package net.hydra.jojomod.networking;

import net.hydra.jojomod.access.IPacketAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class ForgePackets implements IPacketAccess {
    @Override
    public void StandGuardPointPacket(ServerPlayer sp, float guard, boolean broken) {

    }

    @Override
    public void DazeTimePacket(ServerPlayer sp, byte dazeTime) {

    }

    @Override
    public void NBTSyncPacket(ServerPlayer sp, CompoundTag NBT) {

    }

    @Override
    public void syncCooldownPacket(ServerPlayer sp, int attackTime, int attackTimeMax, int attackTimeDuring, byte activePower, byte activePowerPhase) {

    }

    @Override
    public void updateClashPacket(ServerPlayer sp, int id, float clashProgress) {

    }

    @Override
    public void stopSoundPacket(ServerPlayer sp, int id) {

    }

    @Override
    public void startSoundPacket(ServerPlayer sp, int id, byte soundNo) {

    }

    @Override
    public void StandGuardClientPacket() {

    }

    @Override
    public void StandGuardCancelClientPacket() {

    }

    @Override
    public void StandAttackPacket() {

    }

    @Override
    public void StandBarragePacket() {

    }

    @Override
    public void StandPunchPacket(int targetID, byte APP) {

    }

    @Override
    public void StandBarrageHitPacket(int targetID, int ATD) {

    }

    @Override
    public void updateClashPacket(float clashProgress, boolean clashDone) {

    }
}
