package net.hydra.jojomod.entity.mobs;

import net.hydra.jojomod.entity.goals.NightAggroGoal;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.state.BlockState;
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
        this.entityData.define(IS_AGGRO, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Variant")) {
            this.setVariant(tag.getInt("Variant"));
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType spawnType, @Nullable SpawnGroupData spawnData,
                                        @Nullable CompoundTag dataTag) {
        determineSpawnVariant(level, this.blockPosition());
        return super.finalizeSpawn(level, difficulty, spawnType, spawnData, dataTag);
    }

    private void determineSpawnVariant(ServerLevelAccessor level, BlockPos pos) {
        Holder<Biome> biome = level.getBiome(pos);
        if (biome.is(Biomes.DESERT) || biome.is(BiomeTags.IS_BADLANDS)) {
            setVariant(1); // Rattlesnake
        } else if (biome.is(BiomeTags.IS_JUNGLE)) {
            setVariant(2); // Tropical
        } else if (biome.value().coldEnoughToSnow(pos)) {
            setVariant(3); // Cold
        } else if (biome.is(BiomeTags.IS_SAVANNA)) {
            setVariant(4); // Warm
        } else {
            setVariant(0); // Plains/default
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            // only used for client sync so that the snake can play the tongue thing
            boolean hasTarget = this.getTarget() != null && this.getTarget().isAlive();
            if (this.isAggressive() != hasTarget) {
                this.setAggressive(hasTarget);
            }
        } else {
            setupAnimationStates();
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.05D, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        // neutral behavior
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));

        // it's RACIST against chickens and foxes
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Chicken.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Fox.class, true));

        // spiderrrr aggrooo
        this.targetSelector.addGoal(4, new NightAggroGoal<>(this, Player.class));
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (super.doHurtTarget(target)) {
            this.playSound(ModSounds.SNAKE_ATTACK_EVENT, 1.0F, 1.0F);
            if (target instanceof LivingEntity livingTarget) {
                int durationInSeconds = 0;

                // poison scales with difficulty, like cave spiders
                if (this.level().getDifficulty() == Difficulty.NORMAL) {
                    durationInSeconds = 5;
                } else if (this.level().getDifficulty() == Difficulty.HARD) {
                    durationInSeconds = 12;
                }

                if (durationInSeconds > 0) {
                    livingTarget.addEffect(
                            new MobEffectInstance(MobEffects.POISON, durationInSeconds * 20, 0),
                            this
                    );
                }
            }
            return true;
        }
        return false;
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
            setVariant(0); // Plains/literally everywhere else
        }
    }

    // spawn variants end

    // animations and whatnot

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState aggressiveAnimationState = new AnimationState();

    private void setupAnimationStates() {
        if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D) {
            this.idleAnimationState.stop();
            this.walkAnimationState.startIfStopped(this.tickCount);
        } else {
            this.walkAnimationState.stop();
            this.idleAnimationState.startIfStopped(this.tickCount);
        }

        if (this.isAggressive()) {
            this.aggressiveAnimationState.startIfStopped(this.tickCount);
        } else {
            this.aggressiveAnimationState.stop();
        }
    }

    private static final EntityDataAccessor<Boolean> IS_AGGRO =
            SynchedEntityData.defineId(Snake.class, EntityDataSerializers.BOOLEAN);

    public boolean isAggressive() {
        return this.entityData.get(IS_AGGRO);
    }

    public void setAggressive(boolean aggro) {
        this.entityData.set(IS_AGGRO, aggro);
    }

    // animations end

    // sounds
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.SNAKE_IDLE_EVENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSounds.SNAKE_HURT_EVENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.SNAKE_DIE_EVENT;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(ModSounds.SNAKE_STEP_EVENT, 1.0F, 1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.32)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }
}
