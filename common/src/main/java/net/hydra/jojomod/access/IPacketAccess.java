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
    void sendBundlePacket(ServerPlayer sp, byte context, byte one, byte two, byte three);
    void timeStoppingEntityPacket(ServerPlayer sp, int entityID, double x, double y, double z, double range, int duration, int maxDuration);
    void timeStoppingEntityRemovalPacket(ServerPlayer sp, int entityID);
    void permaCastingEntityPacket(ServerPlayer sp, int entityID, double x, double y, double z, double range, byte context);
    void permaCastingEntityRemovalPacket(ServerPlayer sp, int entityID);
    void resumeTileEntityTSPacket(ServerPlayer sp, Vec3i vec3i);
    void sendFloatPowerPacket(ServerPlayer sp, byte activePower, float data);
    void sendIntPowerPacket(ServerPlayer sp, byte activePower, int data);
    void sendIntPacket(ServerPlayer sp, byte activePower, int data);
    void sendSimpleByte(ServerPlayer sp, byte context);
    void sendNewDynamicWorld(ServerPlayer sp, String name, ServerLevel level, @Nullable ServerPlayer player);
    void deregisterDynamicWorld(ServerPlayer sp, String name);
    void ejectPRunning(ServerPlayer sp);

    /**Client To Server Packets*/

}
