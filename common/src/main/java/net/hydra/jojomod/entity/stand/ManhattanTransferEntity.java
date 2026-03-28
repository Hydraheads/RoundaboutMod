package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.Options;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.damagesource.DamageSource;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.stand.powers.PowersManhattanTransfer;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.entity.PathfinderMob;

import java.util.Arrays;
import java.util.List;


import static net.hydra.jojomod.entity.stand.FollowingStandEntity.MOVE_FORWARD;
import static net.hydra.jojomod.entity.stand.FollowingStandEntity.OFFSET_TYPE;
import static net.hydra.jojomod.stand.powers.PowersManhattanTransfer.STAND_BLOCKED;

public class ManhattanTransferEntity extends StandEntity {
    public ManhattanTransferEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }


    public static final byte
            ANIME_SKIN = 1,
            MANGA_SKIN = 2,
            BRAZIL_SKIN = 3,
            RADIOACTIVE_SKIN = 4,
            POLLINATION_SKIN = 5;


    public LivingEntity Target;

    public LivingEntity getTarget() {
        if (this.level().isClientSide) {
            return (LivingEntity) this.level().getEntity(this.entityData.get(TARGET_ID));
        } else {
            if (this.Target != null && this.Target.isRemoved()) {
                this.setFollowing(null);
            }
            return this.Target;
        }
    }

    public void setManhattanTarget(LivingEntity StandSet) {
        this.Target = StandSet;
        int standSetId = -1;
        if (StandSet != null) {
            standSetId = StandSet.getId();
        }
        this.entityData.set(TARGET_ID, standSetId);
    }

    public float lockedYRot = 0;

    public void setSavedSkin(byte skin) {
        this.entityData.set(SAVED_SKIN, skin);
    }

    public byte getSavedSkin() {
        return this.entityData.get(SAVED_SKIN);
    }

    protected static final EntityDataAccessor<Integer> TARGET_ID = SynchedEntityData.defineId(ManhattanTransferEntity.class,
            EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Byte> SAVED_SKIN = SynchedEntityData.defineId(ManhattanTransferEntity.class,
            EntityDataSerializers.BYTE);

    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(TARGET_ID)) {
            super.defineSynchedData();
            this.entityData.define(TARGET_ID, -1);
            this.entityData.define(SAVED_SKIN, (byte) 0);
        }
    }

    public void movement(){
        absMoveTo(this.getX(), this.getY(), this.getZ());

        setDeltaMovement(getForward().scale(0.3));
        setDeltaMovement(getForward().scale(0.3));

    }


    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean lockPos() {
        return false;
    }

    @Override
    public boolean forceVisualRotation() {
        return true;
    }

    public boolean canBeHitByStands() {
        return true;
    }

    @Override
    public boolean hasNoPhysics() {
        return false;
    }

    @Override
    public boolean standHasGravity() {
        return false;
    }

    @Override
    public boolean isInvulnerable() {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return true;
    }

    @Override
    public boolean redirectKnockbackToUser() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean skipAttackInteraction(Entity $$0) {
        return false;
    }

   /* @Override
    public void knockback(double $$0, double $$1, double $$2) {
    }*/

    @Override
    protected float getFlyingSpeed() {
        return 0.10F;
    }

    public boolean stuck = false;

    public static AttributeSupplier.Builder createStandAttributes() {
        return StandEntity.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED,
                        0.01F)
                .add(Attributes.MAX_HEALTH, 1.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);

    }

    protected void onHitBlock(BlockHitResult $$0) {
        if (!this.level().isClientSide()) {

            // yoinked from BladedBowlerHatEntity
           /* Vec3 velocity = this.getDeltaMovement();
            Direction hitDir = $$0.getDirection();
            Vec3 normal = Vec3.atLowerCornerOf(hitDir.getNormal());

            // Makes it bounce
            Vec3 reflected = velocity.subtract(normal.scale(2 * velocity.dot(normal)));

            this.setDeltaMovement(reflected);

            Vec3 hitLoc = $$0.getLocation();
            Vec3 pushOut = normal.scale(0.2);
            this.setPos(hitLoc.x + pushOut.x, hitLoc.y + pushOut.y, hitLoc.z + pushOut.z);*/

        }
    }



    @Override
    public boolean isControlledByLocalInstance() {
        LivingEntity user = this.getUser();
        if (user != null) {
            Entity ent = this.getUserData(user).roundabout$getStandPowers().getPilotingStand();
            if (ent != null && ent.is(this)) {
                return (user instanceof Player $$0 ? $$0.isLocalPlayer() : this.isEffectiveAi());
            }
        }
        return super.isControlledByLocalInstance();
    }

    @Override
    public void travel(Vec3 vec3) {
        super.travel(vec3);
        if (this.isControlledByLocalInstance()) {
            if (this.getUser() instanceof Player PE && this.level().isClientSide()) {
                C2SPacketUtil.updatePilot(this);
            }
        }

    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        this.markHurt();
        return super.hurt(source, amount);
    }

    @Override
    public void die(DamageSource $$0) {

        this.deathTime = 0;

        super.die($$0);
    }

    @Override
    protected void tickDeath() {
        super.die(this.damageSources().generic());
        super.tickDeath();
    }

    protected static final EntityDataAccessor<Float> IDLE_ROTATION = SynchedEntityData.defineId(FollowingStandEntity.class,
            EntityDataSerializers.FLOAT);

    public final void setIdleRotation(float blocks) {
        this.entityData.set(IDLE_ROTATION, blocks);
    }

    public final float getIdleRotation() {
        return this.entityData.get(IDLE_ROTATION);
    }

    @Override
    public void tick() {
        validateUUID();
        float pitch = this.getXRot();
        float yaw = this.getYRot();
        super.tick();

        if (!this.level().isClientSide()) {
            if (!forceVisible) {
                this.setXRot(pitch);
                this.setYRot(yaw);
                this.setYBodyRot(yaw);
                this.xRotO = pitch;
                this.yRotO = yaw;
            }
        }
        nextPathfind++;
        doBasicPathfind();
    }

    int nextPathfind = 1;

    public void doBasicPathfind() {

        Vec3 vec3d = this.getEyePosition(0);
        Vec3 vec3d2 = this.getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x, vec3d2.y, vec3d2.z);
        BlockHitResult blockHit = this.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        BlockPos pos = blockHit.getBlockPos();
        this.navigation.moveTo(pos.getX(), pos.getY(), pos.getZ(), 0);
    }


}
