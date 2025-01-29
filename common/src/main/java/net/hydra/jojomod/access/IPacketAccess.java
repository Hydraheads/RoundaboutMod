package net.hydra.jojomod.access;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public interface IPacketAccess {
    /**Forge and Fabric both use this interface to use their own packet handling code.
     * Every packet function must be written here.*/

    /**Server To Client Packets*/
    void StandGuardPointPacket(ServerPlayer sp, float guard, boolean broken);
    void DazeTimePacket(ServerPlayer sp, byte dazeTime);
    void syncCooldownPacket(ServerPlayer sp, int attackTime, int attackTimeMax, int attackTimeDuring,
                            byte activePower, byte activePowerPhase);
    void syncSkillCooldownPacket(ServerPlayer sp, byte moveOnCooldown, int cooldown);
    void updateClashPacket(ServerPlayer sp, int id, float clashProgress);
    void stopSoundPacket(ServerPlayer sp, int id, byte soundNo);

    void startSoundPacket(ServerPlayer sp, int id, byte soundNo);

    void sendBundlePacket(ServerPlayer sp, byte context, byte one, byte two, byte three);

    void timeStoppingEntityPacket(ServerPlayer sp, int entityID, double x, double y, double z, double range, int duration, int maxDuration);
    void timeStoppingEntityRemovalPacket(ServerPlayer sp, int entityID);
    void permaCastingEntityPacket(ServerPlayer sp, int entityID, double x, double y, double z, double range, byte context);
    void permaCastingEntityRemovalPacket(ServerPlayer sp, int entityID);
    void resumeTileEntityTSPacket(ServerPlayer sp, Vec3i vec3i);
    void sendFloatPowerPacket(ServerPlayer sp, byte activePower, float data);

    void updatePilot(LivingEntity entity);
    void sendIntPowerPacket(ServerPlayer sp, byte activePower, int data);
    void sendBlipPacket(ServerPlayer sp, byte activePower, int data, Vector3f blip);
    void sendIntPacket(ServerPlayer sp, byte activePower, int data);
    void sendSimpleByte(ServerPlayer sp, byte context);
    void s2cPowerInventorySettings(ServerPlayer sp, int anchorPlace, float distanceOut, float idleOpacity,
                                   float combatOpacity, float enemyOpacity);
    void sendConfig(ServerPlayer sp);

    /**Client To Server Packets*/
    void StandGuardCancelClientPacket();
    void StandPowerPacket(byte power);
    void StandPosPowerPacket(byte power, BlockPos blockPos);
    void StandChargedPowerPacket(byte power, int chargeTime);
    void StandPunchPacket(int targetID, byte APP);
    void StandBarrageHitPacket(int targetID, int ATD);

    void updateClashPacket(float clashProgress, boolean clashDone);

    void moveSyncPacket(byte forward, byte strafe);
    void timeStopFloat(boolean TSJump);
    void standSummonPacket();
    void glaivePacket(ItemStack glaive, int target);
    void byteToServerPacket(byte value, byte context);
    void floatToServerPacket(float value, byte context);
    void intToServerPacket(int target, byte context);
    void singleByteToServerPacket(byte context);
    void inventoryToServer(int slotNum, ItemStack stack, byte context);
    void itemContextToServer(byte context, ItemStack stack, byte context2, Vector3f vec);
    void handshake();
}
