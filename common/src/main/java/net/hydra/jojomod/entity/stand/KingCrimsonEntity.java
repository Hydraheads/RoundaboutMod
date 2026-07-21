package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class KingCrimsonEntity extends FollowingStandEntity {
    public KingCrimsonEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    public static final byte
            RED = 1,
            MANGA_SKIN = 2,
            STARLESS = 3,
            END = 4,
            END_2 = 5,
            HEAVEN = 6,
            AGOGO = 7,
            SPINE_ART = 8,
            GREEN = 9,
            YELLOW = 10,
            AQUA = 11,
            DARK = 12,
            BLACK = 13,
            BETA = 14,
            CONCEPT = 15,
            PART_5_SKIN = 16,
            BLUE = 17,
            VISION = 18;


    public final AnimationState finalPunch = new AnimationState();
    public final AnimationState finalPunchWindup = new AnimationState();
    public final AnimationState hideFists = new AnimationState();
    public final AnimationState impale = new AnimationState();
    public final AnimationState impale2 = new AnimationState();
    public final AnimationState blockGrabAnimation = new AnimationState();
    public final AnimationState blockThrowAnimation = new AnimationState();
    public final AnimationState itemGrabAnimation = new AnimationState();
    public final AnimationState itemThrowAnimation = new AnimationState();
    public final AnimationState blockRetractAnimation = new AnimationState();
    public final AnimationState itemRetractAnimation = new AnimationState();

    public static final byte
            IMPALE_2 = 84;
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            byte animation = getAnimation();
            if (animation != BARRAGE) {
                this.hideFists.startIfStopped(this.tickCount);
            } else {
                this.hideFists.stop();
            }
            if (animation == IMPALE) {
                this.impale.startIfStopped(this.tickCount);
            } else {
                this.impale.stop();
            }
            if (animation == IMPALE_2) {
                this.impale2.startIfStopped(this.tickCount);
            } else {
                this.impale2.stop();
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
