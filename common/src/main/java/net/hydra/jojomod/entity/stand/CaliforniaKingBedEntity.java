package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.Roundabout;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class CaliforniaKingBedEntity extends FollowingStandEntity {
    public CaliforniaKingBedEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    public static final byte
            PART_8_SKIN = 1,
            SUNSHINE = 2,
            EGYPT = 3,
            SPOOKY = 4,
            EXPERIENCE = 5,
            CARD_SUIT = 6,
            COVER = 7,
            SPINE_ART = 8,
            HEAVEN = 9,
            BLUE = 10;

    public final AnimationState fall_brace = new AnimationState();
    public final AnimationState sleep = new AnimationState();
    public static final byte
            FALL_BRACE = 82,
            SLEEP = 83;


    protected static final EntityDataAccessor<Float> YAW_FORCE = SynchedEntityData.defineId(CaliforniaKingBedEntity.class,
            EntityDataSerializers.FLOAT);

    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(YAW_FORCE)) {
            super.defineSynchedData();
            this.entityData.define(YAW_FORCE, 0F);
        }
    }
    public final void setYawForce(float blocks) {
        this.entityData.set(YAW_FORCE, blocks);
    }
    public final float getYawForce() {
        return this.entityData.get(YAW_FORCE);
    }
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            if (this.getAnimation() == FALL_BRACE) {
                this.fall_brace.startIfStopped(this.tickCount);
            } else {
                this.fall_brace.stop();
            }
            if (this.getAnimation() == SLEEP) {
                this.sleep.startIfStopped(this.tickCount);
            } else {
                this.sleep.stop();
            }
        }
    }
    @Override
    public float getDistanceOutModified() {return getDistanceOut()*1.15F;}
    @Override
    public float getAnchorPlaceModified() {return getAnchorPlace()+10;}

    public BlockPos bedBlockBind = null;
    public UUID bedUUID = null;


    public byte lastanim = 0;
    public void tick(){
        super.tick();
        if (this.getAnimation() == SLEEP) {
            assertYaw();
        }
        if (lastanim != getAnimation()) {
            refreshDimensions();
        }
        lastanim = getAnimation();
    }
    @Override
    public EntityDimensions getDimensions(Pose pose) {
        if (this.getAnimation() == SLEEP) {
            return EntityDimensions.fixed(3F,0.7F);
        }

        return super.getDimensions(pose);
    }

    public float yaw = 0;
    public void assertYaw(){
        if (this.level().isClientSide()) {
            yaw = getYawForce();
        }
        float rand = (float) (Math.random()*0.0001F);
        float yaw2= yaw+rand;
        setYRot(yaw2);
        setYHeadRot(yaw2);
        setYBodyRot(yaw2);
        yRotO = yaw2;
        yHeadRotO = yaw2;
        yBodyRotO = yaw2;
        setXRot(0.01F+rand); // if desired
    }
}
