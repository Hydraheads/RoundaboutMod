package net.hydra.jojomod.access;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public interface IPacketAccess {
    /**Forge and Fabric both use this interface to use their own packet handling code.
     * Every packet function must be written here.*/

    /**Server To Client Packets*/
    void syncCooldownPacket(ServerPlayer sp, int attackTime, int attackTimeMax, int attackTimeDuring,
                            byte activePower, byte activePowerPhase);
    void syncSkillCooldownPacket(ServerPlayer sp, byte moveOnCooldown, int cooldown);
    void syncSkillCooldownPacket(ServerPlayer sp, byte moveOnCooldown, int cooldown, int maxcooldown);
    void stopSoundPacket(ServerPlayer sp, int id, byte soundNo);

    void startSoundPacket(ServerPlayer sp, int id, byte soundNo);

    void sendBundlePacket(ServerPlayer sp, byte context, byte one, byte two, byte three);

    void timeStoppingEntityPacket(ServerPlayer sp, int entityID, double x, double y, double z, double range, int duration, int maxDuration);
    void timeStoppingEntityRemovalPacket(ServerPlayer sp, int entityID);
    void permaCastingEntityPacket(ServerPlayer sp, int entityID, double x, double y, double z, double range, byte context);
    void permaCastingEntityRemovalPacket(ServerPlayer sp, int entityID);
    void resumeTileEntityTSPacket(ServerPlayer sp, Vec3i vec3i);
    void sendFloatPowerPacket(ServerPlayer sp, byte activePower, float data);

    void sendIntPowerPacket(ServerPlayer sp, byte activePower, int data);
    void sendBlipPacket(ServerPlayer sp, byte activePower, int data, Vector3f blip);
    void sendIntPacket(ServerPlayer sp, byte activePower, int data);
    void sendSimpleByte(ServerPlayer sp, byte context);
    void s2cPowerInventorySettings(ServerPlayer sp, int anchorPlace, float distanceOut, float idleOpacity,
                                   float combatOpacity, float enemyOpacity, int anchorPlaceAttack);
    void sendConfig(ServerPlayer sp);

    void sendNewDynamicWorld(ServerPlayer sp, String name, ServerLevel level, @Nullable ServerPlayer player);
    void deregisterDynamicWorld(ServerPlayer sp, String name);

    void ejectPRunning(ServerPlayer sp);

    /**Client To Server Packets*/
    void StandGuardCancelClientPacket();
    void StandPowerPacket(byte power);
    void StandPosPowerPacket(byte power, BlockPos blockPos);
    void StandChargedPowerPacket(byte power, int chargeTime);

    void byteToServerPacket(byte value, byte context);
    void floatToServerPacket(float value, byte context);
    void intToServerPacket(int target, byte context);
    void inventoryToServer(int slotNum, ItemStack stack, byte context);
    void itemContextToServer(byte context, ItemStack stack, byte context2, Vector3f vec);
    void handshake();

    void ackRegisterWorld();
}
