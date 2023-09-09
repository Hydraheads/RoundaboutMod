package net.hydra.jojomod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.UUID;

public class TerrierEntity extends WolfEntity {
    public TerrierEntity(EntityType<? extends WolfEntity> entityType, World world) {
        super(entityType, world);
    }



    public static boolean canSpawn(EntityType<TerrierEntity> terrierEntityEntityType, ServerWorldAccess serverWorldAccess, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        return serverWorldAccess.getBlockState(blockPos.down()).isIn(BlockTags.RABBITS_SPAWNABLE_ON) && WolfEntity.isLightLevelValidForNaturalSpawn(serverWorldAccess, blockPos);
    }
    @Override
    @Nullable
    public WolfEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        UUID uUID;
        WolfEntity wolfEntity = ModEntities.TERRIER_DOG.create(serverWorld);
        if (wolfEntity != null && (uUID = this.getOwnerUuid()) != null) {
            wolfEntity.setOwnerUuid(uUID);
            wolfEntity.setTamed(true);
        }
        return wolfEntity;
    }
}
