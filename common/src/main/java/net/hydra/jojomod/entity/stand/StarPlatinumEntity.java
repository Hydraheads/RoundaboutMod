package net.hydra.jojomod.entity.stand;

import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Unique;

public class StarPlatinumEntity extends StandEntity {

    public StarPlatinumEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    @Unique
    private static final EntityDataAccessor<Float> FINGER_LENGTH = SynchedEntityData.defineId(StarPlatinumEntity.class,
            EntityDataSerializers.FLOAT);
    @Unique
    private static final EntityDataAccessor<Boolean> IS_SCOPING = SynchedEntityData.defineId(StarPlatinumEntity.class,
            EntityDataSerializers.BOOLEAN);

    public static final byte
            PART_3_SKIN = 1,
            PART_3_MANGA_SKIN = 2,
            OVA_SKIN = 3,
            GREEN_SKIN = 4,
            BASEBALL_SKIN = 5,
            PART_4_SKIN = 6,
            PART_6_SKIN = 7,
            ATOMIC_SKIN = 8;

    public Component getSkinName(byte skinId){
        if (skinId == PART_3_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.base");
        } else if (skinId == PART_3_MANGA_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.manga");
        } else if (skinId == OVA_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.ova");
        } else if (skinId == GREEN_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.green");
        } else if (skinId == BASEBALL_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.baseball");
        } else if (skinId == PART_4_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.part_4");
        } else if (skinId == PART_6_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.part_6");
        } else if (skinId == ATOMIC_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.atomic");
        }
        return Component.translatable(  "skins.roundabout.star_platinum.base");
    }

    public final AnimationState timeStopAnimationState = new AnimationState();
    public final AnimationState timeStopReleaseAnimation = new AnimationState();
    public final AnimationState blockGrabAnimation = new AnimationState();
    public final AnimationState blockThrowAnimation = new AnimationState();
    public final AnimationState itemGrabAnimation = new AnimationState();
    public final AnimationState itemThrowAnimation = new AnimationState();
    public final AnimationState blockRetractAnimation = new AnimationState();
    public final AnimationState itemRetractAnimation = new AnimationState();
    public final AnimationState entityGrabAnimation = new AnimationState();
    public final AnimationState hideFists = new AnimationState();
    public final AnimationState hideLeg = new AnimationState();
    public final AnimationState kick_barrage = new AnimationState();
    public final AnimationState impale = new AnimationState();
    public final AnimationState starFinger = new AnimationState();
    public final AnimationState finalPunch = new AnimationState();
    public final AnimationState finalPunchWindup = new AnimationState();
    public final AnimationState phaseGrab = new AnimationState();
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            if (this.getAnimation() != 12) {
                this.hideFists.startIfStopped(this.tickCount);
            } else {
                this.hideFists.stop();
            }
            if (this.getAnimation() != 80) {
                this.hideLeg.startIfStopped(this.tickCount);
                this.kick_barrage.stop();
            } else {
                this.hideLeg.stop();
                this.kick_barrage.startIfStopped(this.tickCount);
            }
            if (this.getAnimation() == 10) {
                this.blockLoinAnimationState.startIfStopped(this.tickCount);
            } else {
                this.blockLoinAnimationState.stop();
            }
            if (this.getAnimation() == 30) {
                this.timeStopAnimationState.startIfStopped(this.tickCount);
            } else {
                this.timeStopAnimationState.stop();
            }
            if (this.getAnimation() == 31) {
                this.timeStopReleaseAnimation.startIfStopped(this.tickCount);
            } else {
                this.timeStopReleaseAnimation.stop();
            }
            if (this.getAnimation() == 32) {
                this.blockGrabAnimation.startIfStopped(this.tickCount);
            } else {
                this.blockGrabAnimation.stop();
            }
            if (this.getAnimation() == 33) {
                this.blockThrowAnimation.startIfStopped(this.tickCount);
            } else {
                this.blockThrowAnimation.stop();
            }
            if (this.getAnimation() == 34) {
                this.itemGrabAnimation.startIfStopped(this.tickCount);
            } else {
                this.itemGrabAnimation.stop();
            }
            if (this.getAnimation() == 35) {
                this.itemThrowAnimation.startIfStopped(this.tickCount);
            } else {
                this.itemThrowAnimation.stop();
            }

            if (this.getAnimation() == 36) {
                this.blockRetractAnimation.startIfStopped(this.tickCount);
            } else {
                this.blockRetractAnimation.stop();
            }

            if (this.getAnimation() == 37) {
                this.itemRetractAnimation.startIfStopped(this.tickCount);
            } else {
                this.itemRetractAnimation.stop();
            }

            if (this.getAnimation() == 38) {
                this.entityGrabAnimation.startIfStopped(this.tickCount);
            } else {
                this.entityGrabAnimation.stop();
            }

            if (this.getAnimation() == 81) {
                this.impale.startIfStopped(this.tickCount);
            } else {
                this.impale.stop();
            }

            if (this.getAnimation() == 82) {
                this.starFinger.startIfStopped(this.tickCount);
            } else {
                this.starFinger.stop();
            }

            if (this.getAnimation() == 85) {
                this.finalPunchWindup.startIfStopped(this.tickCount);
            } else {
                this.finalPunchWindup.stop();
            }
            if (this.getAnimation() == 86) {
                this.finalPunch.startIfStopped(this.tickCount);
            } else {
                this.finalPunch.stop();
            }
            if (this.getAnimation() == 87) {
                this.phaseGrab.startIfStopped(this.tickCount);
            } else {
                this.phaseGrab.stop();
            }
        }
    }

    public float fingerInterpolation = 1F;
    public final void setFingerLength(float length) {
        this.entityData.set(FINGER_LENGTH, length);
    }

    public final void setScoping(boolean scope) {
        this.entityData.set(IS_SCOPING, scope);
    }

    public final float getFingerLength() {
        return this.entityData.get(FINGER_LENGTH);
    }
    public final boolean getScoping() {
        return this.entityData.get(IS_SCOPING);
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FINGER_LENGTH, 1F);
        this.entityData.define(IS_SCOPING, false);
    }

    public int tsReleaseTime = 0;
    @Override
    public void tick(){
        if (!this.level().isClientSide){
            if (this.getAnimation() == 31) {
                tsReleaseTime++;
                if (tsReleaseTime > 24){
                    this.setAnimation((byte) 0);
                    tsReleaseTime = 0;
                }
            }
        }
        super.tick();
    }

}
