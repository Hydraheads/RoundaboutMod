package net.hydra.jojomod.networking;

import net.hydra.jojomod.access.IPacketAccess;
import net.hydra.jojomod.networking.c2s.*;
import net.hydra.jojomod.networking.s2c.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

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
    public void syncCooldownPacket(ServerPlayer sp, int attackTime, int attackTimeMax, int attackTimeDuring, byte activePower, byte activePowerPhase) {
        ForgePacketHandler.sendToClient(new ForgeCDSyncPacket(attackTime,attackTimeMax,attackTimeDuring,activePower,activePowerPhase), sp);
    }

    @Override
    public void syncSkillCooldownPacket(ServerPlayer sp, byte moveOnCooldown, int cooldown) {
        ForgePacketHandler.sendToClient(new ForgeSkillCDSyncPacket(moveOnCooldown,cooldown), sp);
    }

    @Override
    public void updateClashPacket(ServerPlayer sp, int id, float clashProgress) {
        ForgePacketHandler.sendToClient(new ForgeClashUpdatePacket(id, clashProgress), sp);
    }

    @Override
    public void stopSoundPacket(ServerPlayer sp, int id, byte soundId) {
        ForgePacketHandler.sendToClient(new ForgeStopSoundPacket(id,soundId), sp);
    }

    @Override
    public void startSoundPacket(ServerPlayer sp, int id, byte soundNo) {
        ForgePacketHandler.sendToClient(new ForgePlaySoundPacket(id, soundNo), sp);
    }

    @Override
    public void timeStoppingEntityPacket(ServerPlayer sp, int entityID, double x, double y, double z, double range, int duration, int maxDuration) {
        ForgePacketHandler.sendToClient(new ForgeTimeStoppingEntityPacket(entityID, x,y,z, range, duration, maxDuration), sp);
    }
    @Override
    public void timeStoppingEntityRemovalPacket(ServerPlayer sp, int entityID) {
        ForgePacketHandler.sendToClient(new ForgeTimeStoppingEntityRemovalPacket(entityID), sp);
    }
    @Override
    public void permaCastingEntityPacket(ServerPlayer sp, int entityID, double x, double y, double z, double range, byte context) {
        ForgePacketHandler.sendToClient(new ForgePermaCastingEntityPacket(entityID, x,y,z, range, context), sp);
    }
    @Override
    public void permaCastingEntityRemovalPacket(ServerPlayer sp, int entityID) {
        ForgePacketHandler.sendToClient(new ForgePermaCastingEntityRemovalPacket(entityID), sp);
    }

    @Override
    public void resumeTileEntityTSPacket(ServerPlayer sp, Vec3i vec3i) {
        ForgePacketHandler.sendToClient(new ForgeBlockEntityResumeTSPacket(vec3i), sp);
    }

    @Override
    public void sendFloatPowerPacket(ServerPlayer sp, byte activePower, float data) {
        ForgePacketHandler.sendToClient(new ForgePowerFloatPacket(activePower,data), sp);
    }
    @Override
    public void sendIntPowerPacket(ServerPlayer sp, byte activePower, int data) {
        ForgePacketHandler.sendToClient(new ForgePowerIntPacket(activePower,data), sp);
    }
    @Override
    public void sendBundlePacket(ServerPlayer sp, byte context, byte one, byte two, byte three) {
        ForgePacketHandler.sendToClient(new ForgeBundlePacket(context,one,two,three), sp);
    }
    @Override
    public void sendBlipPacket(ServerPlayer sp, byte activePower, int data, Vector3f blip){
        ForgePacketHandler.sendToClient(new ForgeBlipPacket(activePower,data, blip), sp);
    }
    @Override
    public void sendIntPacket(ServerPlayer sp, byte activePower, int data) {
        ForgePacketHandler.sendToClient(new ForgeGenericIntPacket(activePower,data), sp);
    }
    @Override
    public void sendSimpleByte(ServerPlayer sp, byte context) {
        ForgePacketHandler.sendToClient(new ForgeSimpleBytePacket(context), sp);
    }
    @Override
    public void s2cPowerInventorySettings(ServerPlayer sp, int anchorPlace, float distanceOut, float idleOpacity,
                                          float combatOpacity, float enemyOpacity) {
        ForgePacketHandler.sendToClient(new ForgeS2CPowerInventorySettingsPacket(anchorPlace,
                distanceOut, idleOpacity, combatOpacity, enemyOpacity), sp);
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
    public void StandPosPowerPacket(byte power, BlockPos blockPos) {
        ForgePacketHandler.sendToServer(new ForgePosPowerPacket(power,blockPos));

    }
    @Override
    public void StandChargedPowerPacket(byte power, int chargeTime) {
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
    public void glaivePacket(ItemStack glaive, int target) {
        ForgePacketHandler.sendToServer(new ForgeGlaivePacket(target,glaive));
    }

    @Override
    public void byteToServerPacket(byte value, byte context) {
        ForgePacketHandler.sendToServer(new ForgeByteC2SPacket(value, context));
    }
    @Override
    public void singleByteToServerPacket(byte context) {
        ForgePacketHandler.sendToServer(new ForgeSingleByteC2SPacket(context));
    }
    @Override
    public void floatToServerPacket(float value, byte context) {
        ForgePacketHandler.sendToServer(new ForgeFloatC2SPacket(value, context));
    }
    @Override
    public void intToServerPacket(int value, byte context) {
        ForgePacketHandler.sendToServer(new ForgeIntC2SPacket(value, context));
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
