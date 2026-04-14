package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersMagiciansRed;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.hydra.jojomod.stand.powers.PowersManhattanTransfer;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import java.util.UUID;

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

    protected static final EntityDataAccessor<Integer> TARGET_ID = SynchedEntityData.defineId(ManhattanTransferEntity.class,
            EntityDataSerializers.INT);
    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(TARGET_ID)) {
            super.defineSynchedData();
            this.entityData.define(TARGET_ID, -1);
        }
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
    public boolean canBeHitByProjectile() {return true;}
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
    public boolean canBeSeenAsEnemy() {
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
    @Override
    protected float getFlyingSpeed() {
        return 0.10F;
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

    public StandUser getUserData(LivingEntity User) {
          return ((StandUser) User);
    }

    @Override
    public void tick() {
        validateUUID();
        float pitch = this.getXRot();
        float yaw = this.getYRot();
        super.tick();

            if (horizontalCollision || verticalCollision) {
                this.getUserData(this.getUser()).roundabout$getStandPowers();
                if (this.getUserData(this.getUser()).roundabout$getStandPowers() instanceof PowersManhattanTransfer PM) {

                    if (!PM.isPiloting()) {
                        this.setXRot(pitch + 25);

                        this.setYBodyRot(pitch + 25);

                        this.setYRot(yaw);
                        if (yaw >= -90 && yaw <= 0) {
                            this.setYRot(yaw - 25);
                        }
                        if (yaw <= 90 && yaw > 0) {
                            this.setYRot(yaw + 25);
                        }
                    }
                }
            }
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

    public boolean isInRain() {
        BlockPos $$0 = this.blockPosition();
        return this.level().isRainingAt($$0)
                || this.level().isRainingAt(BlockPos.containing((double)$$0.getX(), this.getBoundingBox().maxY, (double)$$0.getZ()));
    }

    public final AnimationState rain_dodging_manhattan = new AnimationState();

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            if (isInRain()) {
                this.rain_dodging_manhattan.startIfStopped(this.tickCount);
            } if (!isInRain()) {
                this.rain_dodging_manhattan.stop();
            }

        }
    }

}
