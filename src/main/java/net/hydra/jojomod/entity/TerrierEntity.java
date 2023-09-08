package net.hydra.jojomod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class TerrierEntity extends WolfEntity {
    public TerrierEntity(EntityType<? extends WolfEntity> entityType, World world) {
        super(entityType, world);
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
