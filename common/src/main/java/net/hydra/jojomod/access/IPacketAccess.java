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

    /**Client To Server Packets*/

}
