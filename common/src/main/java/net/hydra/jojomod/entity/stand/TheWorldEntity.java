package net.hydra.jojomod.entity.stand;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class TheWorldEntity extends FollowingStandEntity {
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
            AGOGO_SKIN = 12,
            BETA = 13,
            ARCADE_SKIN_2 = 14,
            FOUR_DEE_EXPERIENCE = 15,
            ULTIMATE_SKIN = 16,
            ULTIMATE_KARS_SKIN = 17,
            SCARLET = 18,
            THE_NETHER = 19;

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

    public static final byte
            ASSAULT = 39,
            ASSAULT_PUNCH = 40;
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            byte idle = getIdleAnimation();
            byte animation = getAnimation();
            if (animation != 12) {
                this.hideFists.startIfStopped(this.tickCount);
            } else {
                this.hideFists.stop();
            }
            if (animation == IMPALE) {
                this.impale.startIfStopped(this.tickCount);
            } else {
                this.impale.stop();
            }
            if (((this.getSkin() == TheWorldEntity.HERITAGE_SKIN) || (this.getSkin() == TheWorldEntity.ARCADE_SKIN))
                    && animation != KICK_BARRAGE && animation != KICK_BARRAGE_WINDUP && animation != KICK_BARRAGE_END
            &&  animation != FINAL_ATTACK &&  animation != FINAL_ATTACK_WINDUP && idle != 4) {
                this.hideLegEntirely.startIfStopped(this.tickCount);
            } else {
                this.hideLegEntirely.stop();
            }
            if (animation != KICK_BARRAGE) {
                this.hideLeg.startIfStopped(this.tickCount);
                this.kick_barrage.stop();
            } else {
                this.hideLeg.stop();
                this.kick_barrage.startIfStopped(this.tickCount);
            }
            if (animation == TIME_STOP) {
                this.timeStopAnimationState.startIfStopped(this.tickCount);
            } else {
                this.timeStopAnimationState.stop();
            }
            if (animation == TIME_STOP_RELEASE) {
                this.timeStopReleaseAnimation.startIfStopped(this.tickCount);
            } else {
                 this.timeStopReleaseAnimation.stop();
            }
            if (animation == BLOCK_GRAB) {
                this.blockGrabAnimation.startIfStopped(this.tickCount);
            } else {
                this.blockGrabAnimation.stop();
            }
            if (animation == BLOCK_THROW) {
                this.blockThrowAnimation.startIfStopped(this.tickCount);
            } else {
                this.blockThrowAnimation.stop();
            }
            if (animation == ITEM_GRAB) {
                this.itemGrabAnimation.startIfStopped(this.tickCount);
            } else {
                this.itemGrabAnimation.stop();
            }
            if (animation == ITEM_THROW) {
                this.itemThrowAnimation.startIfStopped(this.tickCount);
            } else {
                this.itemThrowAnimation.stop();
            }
            if (animation == BLOCK_RETRACT) {
                this.blockRetractAnimation.startIfStopped(this.tickCount);
            } else {
                this.blockRetractAnimation.stop();
            }
            if (animation == ITEM_RETRACT) {
                this.itemRetractAnimation.startIfStopped(this.tickCount);
            } else {
                this.itemRetractAnimation.stop();
            }
            if (animation == ENTITY_GRAB) {
                this.entityGrabAnimation.startIfStopped(this.tickCount);
            } else {
                this.entityGrabAnimation.stop();
            }
            if (animation == ASSAULT) {
                this.assault.startIfStopped(this.tickCount);
            } else {
                this.assault.stop();
            }
            if (animation == ASSAULT_PUNCH) {
                this.assault_punch.startIfStopped(this.tickCount);
            } else {
                this.assault_punch.stop();
            }
            if (animation == KICK_BARRAGE_WINDUP) {
                this.kick_barrage_windup.startIfStopped(this.tickCount);
            } else {
                this.kick_barrage_windup.stop();
            }
            if (animation == KICK_BARRAGE_END) {
                this.kick_barrage_end.startIfStopped(this.tickCount);
            } else {
                this.kick_barrage_end.stop();
            }

            if (animation == FINAL_ATTACK_WINDUP) {
                this.finalKickWindup.startIfStopped(this.tickCount);
            } else {
                this.finalKickWindup.stop();
            }
            if (animation == FINAL_ATTACK) {
                this.finalKick.startIfStopped(this.tickCount);
            } else {
                this.finalKick.stop();
            }
            if (animation == PHASE_GRAB) {
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
