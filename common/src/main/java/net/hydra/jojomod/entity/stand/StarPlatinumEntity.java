package net.hydra.jojomod.entity.stand;

import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
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
            MANGA_PURPLE_SKIN = 2,
            OVA_SKIN = 3,
            GREEN_SKIN = 4,
            BASEBALL_SKIN = 5,
            PART_4_SKIN = 6,
            MANGA_SKIN = 7,
            PART_6_SKIN = 8,
            FIRST_SKIN = 9,
            ATOMIC_SKIN = 10,
            JOJONIUM_SKIN = 11,
            BETA = 12,
            ARCADE = 13,
            FOUR_DEE = 14,
            GREEN_2 = 15,
            JOJOVELLER = 16,
            CROP = 17,
            VOLUME_39 = 18,
            JUMP_13 = 19,
            ARCADE_2 = 20;

    @Override
    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }
    public static Component getSkinNameT(byte skinId){
        if (skinId == PART_3_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.base");
        } else if (skinId == MANGA_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.manga");
        } else if (skinId == MANGA_PURPLE_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.manga_purple");
        } else if (skinId == FIRST_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.first_summon");
        } else if (skinId == OVA_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.ova");
        } else if (skinId == GREEN_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.green");
        } else if (skinId == GREEN_2){
            return Component.translatable(  "skins.roundabout.star_platinum.green_2");
        } else if (skinId == BASEBALL_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.baseball");
        } else if (skinId == PART_4_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.part_4");
        } else if (skinId == PART_6_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.part_6");
        } else if (skinId == ATOMIC_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.atomic");
        } else if (skinId == JOJONIUM_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.jojonium");
        } else if (skinId == BETA){
            return Component.translatable(  "skins.roundabout.star_platinum.beta");
        } else if (skinId == ARCADE){
            return Component.translatable(  "skins.roundabout.star_platinum.arcade");
        } else if (skinId == FOUR_DEE){
            return Component.translatable(  "skins.roundabout.star_platinum.four_dee");
        } else if (skinId == JOJOVELLER){
            return Component.translatable(  "skins.roundabout.star_platinum.jojoveller");
        } else if (skinId == CROP){
            return Component.translatable(  "skins.roundabout.star_platinum.crop");
        } else if (skinId == VOLUME_39){
            return Component.translatable(  "skins.roundabout.star_platinum.volume_39");
        } else if (skinId == JUMP_13){
            return Component.translatable(  "skins.roundabout.star_platinum.jump_13");
        } else if (skinId == ARCADE_2){
            return Component.translatable(  "skins.roundabout.star_platinum.arcade_2");
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
    public final AnimationState kick_barrage_end = new AnimationState();
    public final AnimationState kick_barrage_windup = new AnimationState();
    public final AnimationState impale = new AnimationState();
    public final AnimationState starFinger = new AnimationState();
    public final AnimationState starFinger2 = new AnimationState();
    public final AnimationState finalPunch = new AnimationState();
    public final AnimationState finalPunchWindup = new AnimationState();
    public final AnimationState phaseGrab = new AnimationState();
    public static final byte
            STAR_FINGER = 82,
            STAR_FINGER_2 = 83;
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            if (this.getAnimation() != BARRAGE) {
                this.hideFists.startIfStopped(this.tickCount);
            } else {
                this.hideFists.stop();
            }
            if (this.getAnimation() != KICK_BARRAGE) {
                this.hideLeg.startIfStopped(this.tickCount);
                this.kick_barrage.stop();
            } else {
                this.hideLeg.stop();
                this.kick_barrage.startIfStopped(this.tickCount);
            }
            if (this.getAnimation() == BLOCK) {
                this.blockLoinAnimationState.startIfStopped(this.tickCount);
            } else {
                this.blockLoinAnimationState.stop();
            }
            if (this.getAnimation() == TIME_STOP) {
                this.timeStopAnimationState.startIfStopped(this.tickCount);
            } else {
                this.timeStopAnimationState.stop();
            }
            if (this.getAnimation() == TIME_STOP_RELEASE) {
                this.timeStopReleaseAnimation.startIfStopped(this.tickCount);
            } else {
                this.timeStopReleaseAnimation.stop();
            }
            if (this.getAnimation() == BLOCK_GRAB) {
                this.blockGrabAnimation.startIfStopped(this.tickCount);
            } else {
                this.blockGrabAnimation.stop();
            }
            if (this.getAnimation() == BLOCK_THROW) {
                this.blockThrowAnimation.startIfStopped(this.tickCount);
            } else {
                this.blockThrowAnimation.stop();
            }
            if (this.getAnimation() == ITEM_GRAB) {
                this.itemGrabAnimation.startIfStopped(this.tickCount);
            } else {
                this.itemGrabAnimation.stop();
            }
            if (this.getAnimation() == ITEM_THROW) {
                this.itemThrowAnimation.startIfStopped(this.tickCount);
            } else {
                this.itemThrowAnimation.stop();
            }

            if (this.getAnimation() == BLOCK_RETRACT) {
                this.blockRetractAnimation.startIfStopped(this.tickCount);
            } else {
                this.blockRetractAnimation.stop();
            }

            if (this.getAnimation() == ITEM_RETRACT) {
                this.itemRetractAnimation.startIfStopped(this.tickCount);
            } else {
                this.itemRetractAnimation.stop();
            }

            if (this.getAnimation() == ENTITY_GRAB) {
                this.entityGrabAnimation.startIfStopped(this.tickCount);
            } else {
                this.entityGrabAnimation.stop();
            }


            if (this.getAnimation() == KICK_BARRAGE_WINDUP) {
                this.kick_barrage_windup.startIfStopped(this.tickCount);
            } else {
                this.kick_barrage_windup.stop();
            }

            if (this.getAnimation() == KICK_BARRAGE_END) {
                this.kick_barrage_end.startIfStopped(this.tickCount);
            } else {
                this.kick_barrage_end.stop();
            }

            if (this.getAnimation() == StandEntity.IMPALE) {
                this.impale.startIfStopped(this.tickCount);
            } else {
                this.impale.stop();
            }

            if (this.getAnimation() == STAR_FINGER) {
                this.starFinger.startIfStopped(this.tickCount);
            } else {
                this.starFinger.stop();
            }
            if (this.getAnimation() == STAR_FINGER_2) {
                this.starFinger2.startIfStopped(this.tickCount);
            } else {
                this.starFinger2.stop();
            }


            if (this.getAnimation() == FINAL_ATTACK_WINDUP) {
                this.finalPunchWindup.startIfStopped(this.tickCount);
            } else {
                this.finalPunchWindup.stop();
            }
            if (this.getAnimation() == FINAL_ATTACK) {
                this.finalPunch.startIfStopped(this.tickCount);
            } else {
                this.finalPunch.stop();
            }
            if (this.getAnimation() == PHASE_GRAB) {
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
