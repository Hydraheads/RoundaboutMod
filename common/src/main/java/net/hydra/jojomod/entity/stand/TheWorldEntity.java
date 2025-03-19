package net.hydra.jojomod.entity.stand;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class TheWorldEntity extends StandEntity {
    public TheWorldEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    public static final byte
            PART_3_SKIN = 1,
            MANGA_SKIN = 2,
            HERITAGE_SKIN = 3,
            OVA_SKIN = 4,
            BLACK_SKIN = 5,
            PART_7_SKIN = 6,
            PART_7_BLUE = 8,
            OVER_HEAVEN = 7,
            DARK_SKIN = 9,
            AQUA_SKIN = 10,
            ARCADE_SKIN = 11,
            AGOGO_SKIN = 12;

    @Override
    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }
    public static Component getSkinNameT(byte skinId){
        if (skinId == PART_3_SKIN){
            return Component.translatable(  "skins.roundabout.the_world.base");
        } else if (skinId == MANGA_SKIN){
            return Component.translatable(  "skins.roundabout.the_world.manga");
        } else if (skinId == HERITAGE_SKIN){
            return Component.translatable(  "skins.roundabout.the_world.heritage");
        } else if (skinId == OVA_SKIN){
            return Component.translatable(  "skins.roundabout.the_world.ova");
        } else if (skinId == PART_7_SKIN){
            return Component.translatable(  "skins.roundabout.the_world.part_7");
        } else if (skinId == BLACK_SKIN){
            return Component.translatable(  "skins.roundabout.the_world.black");
        } else if (skinId == PART_7_BLUE){
            return Component.translatable(  "skins.roundabout.the_world.blue_part_7");
        } else if (skinId == OVER_HEAVEN){
            return Component.translatable(  "skins.roundabout.the_world.over_heaven");
        } else if (skinId == DARK_SKIN){
            return Component.translatable(  "skins.roundabout.the_world.dark");
        } else if (skinId == AQUA_SKIN){
            return Component.translatable(  "skins.roundabout.the_world.aqua");
        } else if (skinId == ARCADE_SKIN){
            return Component.translatable(  "skins.roundabout.the_world.arcade");
        } else if (skinId == AGOGO_SKIN){
            return Component.translatable(  "skins.roundabout.the_world.agogo");
        }
        return Component.translatable(  "skins.roundabout.the_world.base");
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
    public final AnimationState hideLegEntirely = new AnimationState();
    public final AnimationState assault = new AnimationState();
    public final AnimationState assault_punch = new AnimationState();
    public final AnimationState kick_barrage = new AnimationState();
    public final AnimationState kick_barrage_end = new AnimationState();
    public final AnimationState kick_barrage_windup = new AnimationState();
    public final AnimationState impale = new AnimationState();
    public final AnimationState finalKick = new AnimationState();
    public final AnimationState finalKickWindup = new AnimationState();
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
            if (this.getAnimation() == 81) {
                this.impale.startIfStopped(this.tickCount);
            } else {
                this.impale.stop();
            }
            if (((this.getSkin() == TheWorldEntity.HERITAGE_SKIN) || (this.getSkin() == TheWorldEntity.ARCADE_SKIN))
                    && this.getAnimation() != 80 && this.getAnimation() != 42 && this.getAnimation() != 43
            &&  this.getAnimation() != 86 &&  this.getAnimation() != 85) {
                this.hideLegEntirely.startIfStopped(this.tickCount);
            } else {
                this.hideLegEntirely.stop();
            }
            if (this.getAnimation() != 80) {
                this.hideLeg.startIfStopped(this.tickCount);
                this.kick_barrage.stop();
            } else {
                this.hideLeg.stop();
                this.kick_barrage.startIfStopped(this.tickCount);
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
            if (this.getAnimation() == 39) {
                this.assault.startIfStopped(this.tickCount);
            } else {
                this.assault.stop();
            }
            if (this.getAnimation() == 40) {
                this.assault_punch.startIfStopped(this.tickCount);
            } else {
                this.assault_punch.stop();
            }
            if (this.getAnimation() == 42) {
                this.kick_barrage_windup.startIfStopped(this.tickCount);
            } else {
                this.kick_barrage_windup.stop();
            }
            if (this.getAnimation() == 43) {
                this.kick_barrage_end.startIfStopped(this.tickCount);
            } else {
                this.kick_barrage_end.stop();
            }

            if (this.getAnimation() == 85) {
                this.finalKickWindup.startIfStopped(this.tickCount);
            } else {
                this.finalKickWindup.stop();
            }
            if (this.getAnimation() == 86) {
                this.finalKick.startIfStopped(this.tickCount);
            } else {
                this.finalKick.stop();
            }
            if (this.getAnimation() == 87) {
                this.phaseGrab.startIfStopped(this.tickCount);
            } else {
                this.phaseGrab.stop();
            }
        }
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
