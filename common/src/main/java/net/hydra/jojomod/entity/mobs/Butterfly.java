package net.hydra.jojomod.entity.mobs;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class Butterfly extends AmbientCreature {
    private static final EntityDataAccessor<Integer> VARIANT_ID = SynchedEntityData.defineId(Butterfly.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> RESTING = SynchedEntityData.defineId(Butterfly.class, EntityDataSerializers.BOOLEAN);

    @Nullable
    private BlockPos flowerPos;

    public Butterfly(EntityType<? extends AmbientCreature> entity, Level level) {
        super(entity, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT_ID, 0);
        this.entityData.define(RESTING, false);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.level().isClientSide() && this.isResting()) {
            // Instantly interrupt resting upon taking damage
            this.setResting(false);
            this.flowerPos = null;
        }
        return super.hurt(source, amount);
    }

    public boolean isResting() {
        return this.entityData.get(RESTING);
    }

    public void setResting(boolean resting) {
        this.entityData.set(RESTING, resting);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new PerchOnFlowerGoal(this));
        this.goalSelector.addGoal(2, new ButterflyWanderGoal(this));
    }

    // custom goals start

    static class ButterflyWanderGoal extends Goal {
        private final Butterfly butterfly;

        public ButterflyWanderGoal(Butterfly butterfly) {
            this.butterfly = butterfly;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return !this.butterfly.isResting() && this.butterfly.getNavigation().isDone() && this.butterfly.getRandom().nextInt(10) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return this.butterfly.getNavigation().isInProgress() && !this.butterfly.isResting();
        }

        @Override
        public void start() {
            Vec3 target = this.findFlightTarget();
            if (target != null) {
                this.butterfly.getNavigation().moveTo(this.butterfly.getNavigation().createPath(BlockPos.containing(target), 1), 1.0D);
            }
        }

        @Nullable
        private Vec3 findFlightTarget() {
            BlockPos currentPos = this.butterfly.blockPosition();
            BlockPos.MutableBlockPos targetPos = new BlockPos.MutableBlockPos();

            for (int i = 0; i < 10; ++i) {
                int x = currentPos.getX() + this.butterfly.getRandom().nextInt(16) - 8;
                int y = currentPos.getY() + this.butterfly.getRandom().nextInt(8) - 4;
                int z = currentPos.getZ() + this.butterfly.getRandom().nextInt(16) - 8;

                targetPos.set(x, y, z);
                if (this.butterfly.level().isEmptyBlock(targetPos)) {
                    return Vec3.atCenterOf(targetPos);
                }
            }
            return null;
        }
    }

    static class PerchOnFlowerGoal extends Goal {
        private final Butterfly butterfly;
        private int restTicks;

        public PerchOnFlowerGoal(Butterfly butterfly) {
            this.butterfly = butterfly;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.butterfly.isResting()) {
                return true;
            }
            if (this.butterfly.getRandom().nextInt(40) != 0) {
                return false;
            }
            return this.findNearbyFlower();
        }

        private boolean findNearbyFlower() {
            BlockPos currentPos = this.butterfly.blockPosition();
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

            for (int x = -6; x <= 6; ++x) {
                for (int y = -3; y <= 3; ++y) {
                    for (int z = -6; z <= 6; ++z) {
                        mutable.set(currentPos.getX() + x, currentPos.getY() + y, currentPos.getZ() + z);
                        if (this.butterfly.level().getBlockState(mutable).is(BlockTags.FLOWERS)) {
                            this.butterfly.flowerPos = mutable.immutable();
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        @Override
        public void start() {
            if (this.butterfly.flowerPos != null) {
                this.butterfly.getNavigation().moveTo(
                        this.butterfly.flowerPos.getX() + 0.5D,
                        this.butterfly.flowerPos.getY() + 0.5D,
                        this.butterfly.flowerPos.getZ() + 0.5D,
                        1.0D
                );
            }
        }

        @Override
        public void tick() {
            if (this.butterfly.flowerPos == null) return;

            double distanceSq = this.butterfly.distanceToSqr(Vec3.atCenterOf(this.butterfly.flowerPos));
            if (distanceSq < 1.5D) {
                if (!this.butterfly.isResting()) {
                    this.butterfly.setResting(true);
                    this.butterfly.getNavigation().stop();
                    this.restTicks = 100 + this.butterfly.getRandom().nextInt(101);
                } else {
                    --this.restTicks;
                    if (this.restTicks <= 0) {
                        this.butterfly.setResting(false);
                        this.butterfly.flowerPos = null;
                    }
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.butterfly.flowerPos != null && (this.butterfly.isResting() ? this.restTicks > 0 : this.butterfly.getNavigation().isInProgress());
        }

        @Override
        public void stop() {
            this.butterfly.setResting(false);
            this.butterfly.flowerPos = null;
            this.restTicks = 0;
        }
    }

    // custom goals end

    // For randomized colors

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.entityData.get(VARIANT_ID));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(VARIANT_ID, tag.getInt("Variant"));
    }

    // Body Color
    public DyeColor getBodyColor() {
        return DyeColor.byId(this.entityData.get(VARIANT_ID) & 255);
    }

    // Inner Wing Color
    public DyeColor getInnerWingColor() {
        return DyeColor.byId((this.entityData.get(VARIANT_ID) >> 8) & 255);
    }

    // Outer Wing Color
    public DyeColor getOuterWingColor() {
        return DyeColor.byId((this.entityData.get(VARIANT_ID) >> 16) & 255);
    }

    // Texture variants
    public int getPatternVariant() {
        return (this.entityData.get(VARIANT_ID) >> 24) & 255;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        // randomization
        int randomBody = this.getRandom().nextInt(16);
        int randomInner = this.getRandom().nextInt(16);
        int randomOuter = this.getRandom().nextInt(16);
        int randomTexture = this.getRandom().nextInt(4);

        int packedData = randomBody | (randomInner << 8) | (randomOuter << 16) | (randomTexture << 24);
        this.entityData.set(VARIANT_ID, packedData);

        return super.finalizeSpawn(level, difficulty, spawnType, spawnData, dataTag);
    }

    //animations

    public final AnimationState flyingAnimationState = new AnimationState();

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            if (!this.isResting()) {
                this.flyingAnimationState.startIfStopped(this.tickCount);
            } else {
                this.flyingAnimationState.stop();
            }
        } else {
            this.setNoGravity(!this.isResting());
        }
    }

    // sound stuff

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {

    }

    // attribute builder, like Bats

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.FLYING_SPEED, 0.6F)
                .add(Attributes.MOVEMENT_SPEED, 0.3F);
    }
}
