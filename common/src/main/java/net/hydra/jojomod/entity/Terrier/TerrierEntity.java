package net.hydra.jojomod.entity.Terrier;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class TerrierEntity extends Wolf {
    public TerrierEntity(EntityType<? extends Wolf> entityType, Level world) {
        super(entityType, world);
    }


//world.playSound(null, player.getBlockPos(), ModSounds.SUMMON_SOUND_EVENT, SoundCategory.PLAYERS, 1F, 1F);
    public static boolean canSpawn(EntityType<TerrierEntity> terrierEntityEntityType, ServerLevelAccessor serverWorldAccess, MobSpawnType spawnReason, BlockPos blockPos, RandomSource random) {
        return serverWorldAccess.getBlockState(blockPos.below()).is(BlockTags.RABBITS_SPAWNABLE_ON) && Wolf.isBrightEnoughToSpawn(serverWorldAccess, blockPos);
    }
    @Override
    @Nullable
    public Wolf getBreedOffspring(ServerLevel serverWorld, AgeableMob passiveEntity) {
        UUID uUID;
        Wolf wolfEntity = ModEntities.TERRIER_DOG.create(serverWorld);
        if (wolfEntity != null && (uUID = this.getOwnerUUID()) != null) {
            wolfEntity.setOwnerUUID(uUID);
            wolfEntity.setTame(true);
        }
        return wolfEntity;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        Item item = stack.getItem();
        return item.isEdible() && (Objects.requireNonNull(item.getFoodProperties()).isMeat() || stack.is(ModItems.COFFEE_GUM));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.getAvailableGoals().removeIf(e -> e.getPriority() == 9);
        this.goalSelector.addGoal(9, new TerrierBegGoal(this, 8.0f));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isAngry()) {
            return SoundEvents.WOLF_GROWL;
        }
        if (this.random.nextInt(3) == 0) {
            if (this.isTame() && this.getHealth() < 10.0f) {
                return SoundEvents.WOLF_WHINE;
            }
            return SoundEvents.WOLF_PANT;
        }
        if (!this.isTame() && Math.random() > 0.9) {
            return ModSounds.TERRIER_SOUND_EVENT;
        } else {
            return SoundEvents.WOLF_AMBIENT;
        }
    }

}
