package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.mixin.WorldTickClient;
import net.hydra.jojomod.mixin.WorldTickServer;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class FollowingStandEntity extends StandEntity{
    /**
     * Initialize Stands
     *
     * @param entityType
     * @param world
     */
    protected FollowingStandEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    protected static final EntityDataAccessor<Integer> ANCHOR_PLACE = SynchedEntityData.defineId(FollowingStandEntity.class,
            EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> ANCHOR_PLACE_ATTACK = SynchedEntityData.defineId(FollowingStandEntity.class,
            EntityDataSerializers.INT);

    /**OFFSET_TYPE specifies if the stand is floating by your side,
     * facing your direction, or detached on its own, for instance.*/
    protected static final EntityDataAccessor<Byte> OFFSET_TYPE = SynchedEntityData.defineId(FollowingStandEntity.class,
            EntityDataSerializers.BYTE);


    protected static final EntityDataAccessor<Float> DISTANCE_OUT = SynchedEntityData.defineId(FollowingStandEntity.class,
            EntityDataSerializers.FLOAT);

    protected static final EntityDataAccessor<Float> SIZE_PERCENT = SynchedEntityData.defineId(FollowingStandEntity.class,
            EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> IDLE_ROTATION = SynchedEntityData.defineId(FollowingStandEntity.class,
            EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> IDLE_Y_OFFSET = SynchedEntityData.defineId(FollowingStandEntity.class,
            EntityDataSerializers.FLOAT);

    /**
     * This is called when setting the anchor place of a stand, which is to say whether it will position itself
     * next to the player to the left, right, front, back, or anywhere in between. Players individually can set
     * this to accommodate their style and FOV settings. Purely cosmetic, as stands will teleport before taking
     * actions..
     */
    public final void setAnchorPlace(Integer degrees) {
        this.entityData.set(ANCHOR_PLACE, degrees);
    }
    public final void setAnchorPlaceAttack(Integer degrees) {
        this.entityData.set(ANCHOR_PLACE_ATTACK, degrees);
    }
    public final void setDistanceOut(float blocks) {
        this.entityData.set(DISTANCE_OUT, blocks);
    }
    public final float getDistanceOut() {
        return this.entityData.get(DISTANCE_OUT);
    }
    public final void setSizePercent(float blocks) {
        this.entityData.set(SIZE_PERCENT, blocks);
    }
    public final float getSizePercent() {
        return this.entityData.get(SIZE_PERCENT);
    }
    public final void setIdleRotation(float blocks) {
        this.entityData.set(IDLE_ROTATION, blocks);
    }
    public final float getIdleRotation() {
        return this.entityData.get(IDLE_ROTATION);
    }
    public final void setIdleYOffset(float blocks) {
        this.entityData.set(IDLE_Y_OFFSET, blocks);
    }
    public final float getIdleYOffset() {
        return this.entityData.get(IDLE_Y_OFFSET);
    }

    public final int getAnchorPlace() {
        return this.entityData.get(ANCHOR_PLACE);
    }

    public final int getAnchorPlaceAttack() {
        return this.entityData.get(ANCHOR_PLACE_ATTACK);
    }

    public final byte getOffsetType() {
        if (this.level().isClientSide()){
            if (ClientUtil.getScreenFreeze()){
                return this.lastOffsetType;
            }
        }
        return this.entityData.get(OFFSET_TYPE);
    } //returns leaning direction


    @Override
    public boolean redirectKnockbackToUser(){
        return true;
    }

    public final void setOffsetType(byte oft) {
        this.entityData.set(OFFSET_TYPE, oft);
    }

    public byte lastOffsetType = 0;

    @Override
    public void tick() {
        validateUUID();
        float pitch = this.getXRot();
        float yaw = this.getYRot();
        byte ot = this.getOffsetType();
        if (this.lastOffsetType != ot) {
            this.lastOffsetType = ot;
        }
        super.tick();

        if (!this.level().isClientSide()) {
            if (!forceVisible) {
                if (OffsetIndex.OffsetStyle(ot) == OffsetIndex.LOOSE_STYLE) {
                    this.setXRot(pitch);
                    this.setYRot(yaw);
                    this.setYBodyRot(yaw);
                    this.xRotO = pitch;
                    this.yRotO = yaw;
                }
            }
        }
    }


    /** Math to determine the position of the stand floating away from its user.
     * Based on Jojovein donut code with great help from Urbancase.*/
    public Vec3 getStandOffsetVector(LivingEntity standUser){
        byte ot = this.getOffsetType();
        if (OffsetIndex.OffsetStyle(ot) == OffsetIndex.FOLLOW_STYLE) {
            return getIdleOffset(standUser);
        } else if (OffsetIndex.OffsetStyle(ot) == OffsetIndex.FIXED_STYLE) {
            Direction direction = ((IGravityEntity)standUser).roundabout$getGravityDirection();
            Vec3 finalized = getAttackOffset(standUser,ot);
            if (direction != Direction.DOWN){
                finalized = RotationUtil.vecPlayerToWorld(finalized.subtract(standUser.position()),direction).add(standUser.position());
            }
            return finalized;
        }
        return new Vec3(this.getX(),this.getY(),this.getZ());
    }



    /** The offset that can potentially can be used for rushes, punches, blocking, etc.
     * Involves the stand being in an L shape away from the user,
     * with the StandModel.java handling the inward rotation*/
    public Vec3 getAttackOffset(LivingEntity standUser, byte ot) {
        if (ot == OffsetIndex.BENEATH) {
            Vec3 frontVectors = FrontVectors(standUser, 180, 0F);
            return new Vec3(frontVectors.x, frontVectors.y-1.1, frontVectors.z);
        } else {
            float distanceFront;
            float standrotDir2 = 0;
            float standrotDir = (float) getPunchYaw(this.getAnchorPlaceAttack(),
                    1);
            if (standrotDir >0){standrotDir2=90;} else if (standrotDir < 0) {standrotDir2=-90;}
            float addY = 0.3F;
            float addXYZ = 0.3F;
            float addXZ = 0.7F;

            if (ot == OffsetIndex.GUARD || ot == OffsetIndex.GUARD_AND_TRACE) {
                addXZ -= 0.015F;
                distanceFront = 1.05F;
            } else if (ot == OffsetIndex.GUARD_FURTHER_RIGHT) {
                addXZ+= 0.15F;
                distanceFront = 1.05F;
            } else {
                distanceFront = ((StandUser) standUser).roundabout$getStandPowers().getDistanceOutAccurate(standUser,((StandUser) standUser).roundabout$getStandReach(),true);
            }

            Vec3 frontVectors = FrontVectors(standUser, 0, distanceFront);

            Vec3 vec3d2 = DamageHandler.getRotationVector(0, standUser.getYHeadRot()+ standrotDir2);
            frontVectors = frontVectors.add(vec3d2.x * addXZ, 0, vec3d2.z * addXZ);
            return new Vec3(frontVectors.x,frontVectors.y + standUser.getEyeHeight(standUser.getPose()) + addY - 1.6,
                    frontVectors.z);
        }
    }



    /**This is the way a stand looks when it is passively floating by you*/
    public Vec3 getIdleOffset(LivingEntity standUser) {
        int vis = this.getFadeOut();
        double r = (((double) vis / MaxFade) * ((standUser.getBbWidth()/2)+this.getDistanceOut()));
        if (r < 0.5) {
            r = 0.5;
        }
        double yawfix = standUser.getYRot();
        yawfix += this.getAnchorPlace();
        if (yawfix > 360) {
            yawfix -= 360;
        } else if (yawfix < 0) {
            yawfix += 360;
        }
        double ang = (yawfix - 180) * Math.PI;

        double mcap = 0.3;
        Vec3 xyz = standUser.getDeltaMovement();
        double yy = xyz.y() * 0.3;
        if (yy > mcap) {
            yy = mcap;
        } else if (yy < -mcap) {
            yy = -mcap;
        }
        if (isSwimming() || isVisuallyCrawling() || isFallFlying()) {
            yy += 1;
        }


        Direction dir = ((IGravityEntity)standUser).roundabout$getGravityDirection();
        Vec3 offset = new Vec3(
                (- (-1 * (r * (Math.sin(ang / 180))))),
                (getIdleYOffset() - yy),
                (-(r * (Math.cos(ang / 180))))
        );
        if (dir != Direction.DOWN){
            offset = RotationUtil.vecPlayerToWorld(offset,dir);
        }

        double x1 = standUser.getX() +offset.x;
        double y1 = standUser.getY() +offset.y;
        double z1 = standUser.getZ() +offset.z;

        return new Vec3(x1, y1, z1);
    }




    /**
     * Unused, will be used to lock a stand onto another mob.
     * Use case example: Killer Queen BTD following someone else
     * Code to tick on follower will be needed in client world mixin
     */

    @Nullable
    public LivingEntity Following;
    public LivingEntity getFollowing() {
        if (this.level().isClientSide){
            return (LivingEntity) this.level().getEntity(this.entityData.get(FOLLOWING_ID));
        } else {
            if (this.Following != null && this.Following.isRemoved()){
                this.setFollowing(null);
            }
            return this.Following;
        }
    }

    /**FOLLOWING_ID is the mob the stand is floating by. This does not have to be
     * the user, for instance, if a stand like killer queen is planted in someone else.*/
    protected static final EntityDataAccessor<Integer> FOLLOWING_ID = SynchedEntityData.defineId(FollowingStandEntity.class,
            EntityDataSerializers.INT);
    @Override
    public boolean canBeHitByStands(){
        return (isRemoteControlled() || this.getFollowing() != this.getUser() ||
                (this.getFollowing() == null && this.getUser() == null));
    }
    @Override
    public void setFollowing(LivingEntity StandSet){
        this.Following = StandSet;
        int standSetId = -1;
        if (StandSet != null){
            standSetId = StandSet.getId();
        }
        this.entityData.set(FOLLOWING_ID, standSetId);
    }
    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(FOLLOWING_ID)) {
            super.defineSynchedData();
            this.entityData.define(FOLLOWING_ID, -1);
            this.entityData.define(OFFSET_TYPE, (byte) 0);
            this.entityData.define(ANCHOR_PLACE, 55);
            this.entityData.define(ANCHOR_PLACE_ATTACK, 55);
            this.entityData.define(DISTANCE_OUT, 1.07F);
            this.entityData.define(MOVE_FORWARD, (byte) 0);
            this.entityData.define(SIZE_PERCENT, 1F);
            this.entityData.define(IDLE_ROTATION, 0F);
            this.entityData.define(IDLE_Y_OFFSET, 0.1F);
        }
    }


    /**
     * Presently, this is how the stand knows to lean in any direction based on player movement.
     * Creates the illusion of floaty movement within the stand.
     * Relevant in stand model code:
     *
     */
    public final void setMoveForward(Byte MF) {
        this.entityData.set(MOVE_FORWARD, MF);
    } //sets leaning direction



    /**The data of stand leaning from player inputs, might go to the user itself at some point.*/
    protected static final EntityDataAccessor<Byte> MOVE_FORWARD = SynchedEntityData.defineId(FollowingStandEntity.class,
            EntityDataSerializers.BYTE);




    public final byte getMoveForward() {
        return this.entityData.get(MOVE_FORWARD);
    } //returns leaning direction



    /**
     * Called every tick in
     *
     * @see WorldTickClient for the client and
     * @see WorldTickServer for the server.
     * Basically, this lets the user/followee tick the stand so that it moves exactly with them.
     * The main purpose for this is to make the smooth visual effect of being ridden.
     * Also, if a stand is perfectly still, it's possible it is just not ticking due to not being mounted properly
     * with a follower.
     */
    public void tickStandOut() {


        byte ot = this.getOffsetType();
        if (lockPos()) {
            this.setDeltaMovement(Vec3.ZERO);
        }
        this.tick();
        if (this.getFollowing() == null) {
            return;
        }

        if (!(OffsetIndex.OffsetStyle(ot) == OffsetIndex.LOOSE_STYLE) || this.isControlledByLocalInstance() ) {
            ((StandUser) this.getFollowing()).roundabout$updateStandOutPosition(this);
        }
    }


}
