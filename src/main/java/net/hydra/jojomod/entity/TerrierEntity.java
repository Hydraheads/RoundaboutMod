package net.hydra.jojomod.entity;

import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class TerrierEntity extends WolfEntity {
    public TerrierEntity(EntityType<? extends WolfEntity> entityType, World world) {
        super(entityType, world);
    }


//world.playSound(null, player.getBlockPos(), ModSounds.SUMMON_SOUND_EVENT, SoundCategory.PLAYERS, 1F, 1F);
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

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        Item item = stack.getItem();
        return item.isFood() && (Objects.requireNonNull(item.getFoodComponent()).isMeat() || stack.isOf(ModItems.COFFEE_GUM));
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.getGoals().removeIf(e -> e.getPriority() == 9);
        this.goalSelector.add(9, new TerrierBegGoal(this, 8.0f));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.hasAngerTime()) {
            return SoundEvents.ENTITY_WOLF_GROWL;
        }
        if (this.random.nextInt(3) == 0) {
            if (this.isTamed() && this.getHealth() < 10.0f) {
                return SoundEvents.ENTITY_WOLF_WHINE;
            }
            return SoundEvents.ENTITY_WOLF_PANT;
        }
        if (!this.isTamed() && Math.random() > 0.9) {
            return ModSounds.TERRIER_SOUND_EVENT;
        } else {
            return SoundEvents.ENTITY_WOLF_AMBIENT;
        }
    }

}
