package net.hydra.jojomod.entity.mobs;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.hydra.jojomod.entity.ModEntities;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.IntFunction;

public class FalconEntity extends ShoulderRidingEntity implements VariantHolder<FalconEntity.Variant>, FlyingAnimal {
    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(FalconEntity.class, EntityDataSerializers.INT);

    private static final Set<Item> TAME_FOOD = Sets.newHashSet(Items.CHICKEN, Items.BEEF, Items.RABBIT);

    private static final EntityDataAccessor<Byte> ANIM = SynchedEntityData.defineId(StrayCatEntity.class,
            EntityDataSerializers.BYTE);

    private final byte
        IDLE = 0,
        FLAPPING = 1,
        FLYING = 2;

    public Byte getAnimation() {
        return this.entityData.get(ANIM);
    }

    public void setAnimation(Byte anim) {
        this.entityData.set(ANIM, anim);
    }

    public final AnimationState sitAnimation = new AnimationState();
    public final AnimationState flappingAnimation = new AnimationState();
    public final AnimationState flyingAnimation = new AnimationState();
    public final AnimationState flyingFlapAnimation = new AnimationState();

    public FalconEntity(EntityType<? extends FalconEntity> $$0, Level $$1) {
        super($$0, $$1);
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
    }

    public static boolean canSpawn(EntityType<FalconEntity> FalconEntityEntityType, ServerLevelAccessor serverWorldAccess, MobSpawnType spawnReason, BlockPos blockPos, RandomSource random) {
        return serverWorldAccess.getBlockState(blockPos.below()).is(BlockTags.PARROTS_SPAWNABLE_ON) && Wolf.isBrightEnoughToSpawn(serverWorldAccess, blockPos);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 14.0D).add(Attributes.FLYING_SPEED, (double)0.6F).add(Attributes.MOVEMENT_SPEED, (double)0.3F);
    }

    protected @NotNull PathNavigation createNavigation(Level p_29417_) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, p_29417_);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        return flyingpathnavigation;
    }

    protected @NotNull BodyRotationControl createBodyControl() {
        return new FalconFlyingRotationControl( this);
    }

    private int flyingTicks = 0;

    private void setupAnimationStates() {
        byte animation = getAnimation();

        if (animation == FLAPPING) {
            flappingAnimation.startIfStopped(tickCount);
        }else {
            flappingAnimation.stop();
        }

        if (animation == FLYING) {
            flyingAnimation.startIfStopped(tickCount);
            if (tickCount % 110 == 30) {
                flyingFlapAnimation.startIfStopped(tickCount);
            }
        }else {
            flyingAnimation.stop();
            flyingFlapAnimation.stop();
        }

        if (isOrderedToSit()) {
            sitAnimation.startIfStopped(tickCount);
        }else {
            sitAnimation.stop();
        }
    }

    public void tick() {
        super.tick();
        if (level().isClientSide) {
            setupAnimationStates();
        } else {
            if (!onGround()) {
                flyingTicks++;
                if (isPlaning()) {
                    setAnimation(FLYING);
                }else {
                    setAnimation(FLAPPING);
                }
            }else {
                flyingTicks = 0;
                setAnimation(IDLE);
            }
        }


    }

    @Override
    public void aiStep() {
        super.aiStep();
        Vec3 vec3 = this.getDeltaMovement();
        if (!this.onGround() && vec3.y < 0.0D) {
            this.setDeltaMovement(vec3.multiply(1.0D, isPlaning() ? 0.4D : 0.6D, 1.0D));
        }
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_29389_, DifficultyInstance p_29390_, MobSpawnType p_29391_, SpawnGroupData p_29392_,CompoundTag p_29393_) {
        this.setVariant(Util.getRandom(Variant.values(), p_29389_.getRandom()));
        if (p_29392_ == null) {
            p_29392_ = new AgeableMob.AgeableMobGroupData(false);
        }

        return super.finalizeSpawn(p_29389_, p_29390_, p_29391_, p_29392_, p_29393_);
    }

    public boolean canMate(Animal p_28127_) {
        if (!this.isTame()) {
            return false;
        } else if (!(p_28127_ instanceof FalconEntity)) {
            return false;
        } else {
            FalconEntity falcon = (FalconEntity)p_28127_;
            return falcon.isTame() && super.canMate(p_28127_);
        }
    }

    @Override
    public FalconEntity getBreedOffspring(ServerLevel level, AgeableMob p_148913_) {
        FalconEntity falcon = ModEntities.FALCON.create(level);
        if (falcon != null) {
            falcon.setVariant(this.random.nextBoolean() ? this.getVariant() : ((FalconEntity)p_148913_).getVariant());
        }

        return falcon;
    }

    public FalconEntity.Variant getVariant() {
        return FalconEntity.Variant.byId(this.entityData.get(DATA_VARIANT_ID));
    }

    public void setVariant(FalconEntity.Variant p_262613_) {
        this.entityData.set(DATA_VARIANT_ID, p_262613_.id);
    }

    public void addAdditionalSaveData(CompoundTag p_29422_) {
        super.addAdditionalSaveData(p_29422_);
        p_29422_.putInt("Variant", this.getVariant().id);
    }

    public void readAdditionalSaveData(CompoundTag p_29402_) {
        super.readAdditionalSaveData(p_29402_);
        this.setVariant(Variant.byId(p_29402_.getInt("Variant")));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VARIANT_ID, 0);
        this.entityData.define(ANIM, (byte) 0);
    }

    public boolean isFlying() {
        return !this.onGround();
    }

    public boolean isPlaning() {return flyingTicks >= 25; }

    protected void checkFallDamage(double p_29370_, boolean p_29371_, BlockState p_29372_, BlockPos p_29373_) {
    }


    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 5.0F, 1.0F, true));
        this.goalSelector.addGoal(2, new FalconWanderGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LandOnOwnersShoulderGoal(this));
        this.goalSelector.addGoal(3, new FollowMobGoal(this, 1.0D, 3.0F, 7.0F));
        this.goalSelector.addGoal(10, new BreedGoal(this, 0.8D));

    }

    public InteractionResult mobInteract(Player p_29414_, InteractionHand p_29415_) {
        ItemStack itemstack = p_29414_.getItemInHand(p_29415_);
        if (!this.isTame() && TAME_FOOD.contains(itemstack.getItem())) {
            if (!p_29414_.getAbilities().instabuild) {
                itemstack.shrink(1);
            }

            if (!this.isSilent()) {
                this.level().playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.PARROT_EAT, this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            }

            if (!this.level().isClientSide) {
                if (this.random.nextInt(10) == 0) {
                    this.tame(p_29414_);
                    this.level().broadcastEntityEvent(this, (byte)7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte)6);
                }
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else if (!this.isFlying() && this.isTame() && this.isOwnedBy(p_29414_)) {
            if (!this.level().isClientSide) {
                this.setOrderedToSit(!this.isOrderedToSit());
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            return super.mobInteract(p_29414_, p_29415_);
        }
    }

    static class FalconWanderGoal extends WaterAvoidingRandomFlyingGoal {
        public FalconWanderGoal(PathfinderMob p_186224_, double p_186225_) {
            super(p_186224_, p_186225_);
        }

        @javax.annotation.Nullable
        protected Vec3 getPosition() {
            Vec3 vec3 = null;
            if (this.mob.isInWater()) {
                vec3 = LandRandomPos.getPos(this.mob, 15, 15);
            }

            if (this.mob.getRandom().nextFloat() >= this.probability) {
                vec3 = this.getTreePos();
            }

            return vec3 == null ? super.getPosition() : vec3;
        }

        @javax.annotation.Nullable
        private Vec3 getTreePos() {
            BlockPos blockpos = this.mob.blockPosition();
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();

            for(BlockPos blockpos1 : BlockPos.betweenClosed(Mth.floor(this.mob.getX() - 3.0D), Mth.floor(this.mob.getY() - 6.0D), Mth.floor(this.mob.getZ() - 3.0D), Mth.floor(this.mob.getX() + 3.0D), Mth.floor(this.mob.getY() + 6.0D), Mth.floor(this.mob.getZ() + 3.0D))) {
                if (!blockpos.equals(blockpos1)) {
                    BlockState blockstate = this.mob.level().getBlockState(blockpos$mutableblockpos1.setWithOffset(blockpos1, Direction.DOWN));
                    boolean flag = blockstate.getBlock() instanceof LeavesBlock || blockstate.is(BlockTags.LOGS);
                    if (flag && this.mob.level().isEmptyBlock(blockpos1) && this.mob.level().isEmptyBlock(blockpos$mutableblockpos.setWithOffset(blockpos1, Direction.UP))) {
                        return Vec3.atBottomCenterOf(blockpos1);
                    }
                }
            }

            return null;
        }
    }

    public static enum Variant implements StringRepresentable {
        PET_SHOP(0, "pet_shop"),
        APLOMADO(1, "aplomado"),
        GERIFALTE(2, "gerifalte"),
        CARACARA(3, "caracara");

        public static final Codec<Variant> CODEC = StringRepresentable.fromEnum(Variant::values);
        private static final IntFunction<Variant> BY_ID = ByIdMap.continuous(Variant::getId, values(),
                ByIdMap.OutOfBoundsStrategy.CLAMP);
        final int id;
        private final String name;

        private Variant(int p_262571_, String p_262693_) {
            this.id = p_262571_;
            this.name = p_262693_;
        }

        public int getId() {
            return this.id;
        }

        public static Variant byId(int p_262643_) {
            return BY_ID.apply(p_262643_);
        }

        public String getSerializedName() {
            return this.name;
        }
    }

    // Phantom like movement

    Vec3 moveTargetPoint;
    BlockPos anchorPoint;
    //Phantom.AttackPhase attackPhase;

    class FalconFlyingRotationControl extends BodyRotationControl {
        private final FalconEntity falcon;

        public FalconFlyingRotationControl(FalconEntity mob) {
            super(mob);
            falcon = mob;
        }

        public void clientTick() {
            falcon.yHeadRot = falcon.yBodyRot;
            falcon.yBodyRot = falcon.getYRot();
        }
    }

    class FalconFlyingMoveControl extends MoveControl {
        private float speed = 0.6F;
        private final FalconEntity falcon;

        public FalconFlyingMoveControl(FalconEntity $$0) {
            super($$0);
            falcon = $$0;
        }

        public void tick() {
            if (!falcon.isPlaning()) {
                super.tick();
                return;
            }

            if (falcon.horizontalCollision) {
                falcon.setYRot(falcon.getYRot() + 180.0F);
                this.speed = 0.1F;
            }

            double $$0 = falcon.moveTargetPoint.x - falcon.getX();
            double $$1 = falcon.moveTargetPoint.y - falcon.getY();
            double $$2 = falcon.moveTargetPoint.z - falcon.getZ();
            double $$3 = Math.sqrt($$0 * $$0 + $$2 * $$2);
            if (Math.abs($$3) > (double)1.0E-5F) {
                double $$4 = (double)1.0F - Math.abs($$1 * (double)0.7F) / $$3;
                $$0 *= $$4;
                $$2 *= $$4;
                $$3 = Math.sqrt($$0 * $$0 + $$2 * $$2);
                double $$5 = Math.sqrt($$0 * $$0 + $$2 * $$2 + $$1 * $$1);
                float $$6 = falcon.getYRot();
                float $$7 = (float)Mth.atan2($$2, $$0);
                float $$8 = Mth.wrapDegrees(falcon.getYRot() + 90.0F);
                float $$9 = Mth.wrapDegrees($$7 * (180F / (float)Math.PI));
                falcon.setYRot(Mth.approachDegrees($$8, $$9, 4.0F) - 90.0F);
                falcon.yBodyRot = falcon.getYRot();
                if (Mth.degreesDifferenceAbs($$6, falcon.getYRot()) < 3.0F) {
                    this.speed = Mth.approach(this.speed, 2.2F, 0.005F * (1.8F / this.speed));
                } else {
                    this.speed = Mth.approach(this.speed, 0.6F, 0.025F);
                }

                float $$10 = (float)(-(Mth.atan2(-$$1, $$3) * (double)(180F / (float)Math.PI)));
                falcon.setXRot($$10);
                float $$11 = falcon.getYRot() + 90.0F;
                double $$12 = (double)(this.speed * Mth.cos($$11 * ((float)Math.PI / 180F))) * Math.abs($$0 / $$5);
                double $$13 = (double)(this.speed * Mth.sin($$11 * ((float)Math.PI / 180F))) * Math.abs($$2 / $$5);
                double $$14 = (double)(this.speed * Mth.sin($$10 * ((float)Math.PI / 180F))) * Math.abs($$1 / $$5);
                Vec3 $$15 = falcon.getDeltaMovement();
                falcon.setDeltaMovement($$15.add((new Vec3($$12, $$14, $$13)).subtract($$15).scale(0.2)));
            }

        }
    }
}
