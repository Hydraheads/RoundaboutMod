package net.hydra.jojomod.entity.mobs;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.jetbrains.annotations.Nullable;

public class Snake extends Animal {
    private static final EntityDataAccessor<Integer> VARIANT_ID = SynchedEntityData.defineId(Snake.class, EntityDataSerializers.INT);

    public Snake(EntityType<? extends Animal> entity, Level level) {
        super(entity, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT_ID, 0);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawnType, @Nullable SpawnGroupData spawnData,
                                        @Nullable CompoundTag dataTag) {
        determineSpawnVariant();
        return super.finalizeSpawn(level, difficulty, spawnType, spawnData, dataTag);
    }

    // for the different spawn variants

    public int getVariant() {
        return this.entityData.get(VARIANT_ID);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT_ID, variant);
    }

    private void determineSpawnVariant() {
        Holder<Biome> biome = this.level().getBiome(this.blockPosition());
        BlockPos pos = this.blockPosition();
        if (biome.is(Biomes.DESERT) || biome.is(BiomeTags.IS_BADLANDS)) {
            setVariant(1); // Rattlesnake
        } else if (biome.is(BiomeTags.IS_JUNGLE)) {
            setVariant(2); // Tropical
        } else if (biome.value().coldEnoughToSnow(pos)) {
            setVariant(3); // Cold
        } else if (biome.is(BiomeTags.IS_SAVANNA)) {
            setVariant(4); // Warm
        } else {
            setVariant(0); // Plains/literally everything else
        }
    }

    // spawn variants end

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.45).add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }
}
