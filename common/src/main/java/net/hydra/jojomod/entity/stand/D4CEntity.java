package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class D4CEntity extends FollowingStandEntity {
    public D4CEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    public static final byte
            BASE = 1,
            PORTAL = 2,
            SPINE = 3,
            VELLER = 4,
            DEPARTURE = 5,
            PROMO = 6,
            FOUNTAIN_BOY = 7,
            CONCEPT_ART = 8,
            MELON = 9,
            EOH_PINK = 10,
            EOH_PURPLE = 11,
            EOH_YELLOW = 12,
            GOLDEN = 13,
            GOLDEN_V2 = 14,
            CURSED_CARD = 15,
            ULTRA_BRAND = 16,
            KEYHOLDER = 17,
            CHOCOLATE = 18,
            BLACK_WHITE = 19,
            REVERSE = 20,
            INVERSE = 21;

    public final AnimationState finalPunch = new AnimationState();
    public final AnimationState finalPunch2 = new AnimationState();
    public final AnimationState finalPunch3 = new AnimationState();
    public final AnimationState finalPunchWindup = new AnimationState();
    public final AnimationState chop = new AnimationState();
    public final AnimationState impale2 = new AnimationState();
    public final AnimationState drag = new AnimationState();
    public final AnimationState drag2 = new AnimationState();
    public final AnimationState bodyLeap = new AnimationState();

    public final AnimationState hideFists = new AnimationState();

    public static final byte
            FINAL_1 = 82,
            FINAL_2 = 83,
            IMPALE_2 = 50,
            CHOP = 51,
            DRAG = 52,
            DRAG_2 = 53,
            BODY_LEAP = 54;
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        byte anim = this.getAnimation();
        if (anim != BARRAGE && anim != FINAL_1 && anim != FINAL_ATTACK && anim != FINAL_2) {
            this.hideFists.startIfStopped(this.tickCount);
        } else {
            this.hideFists.stop();
        }
        if (anim == FINAL_ATTACK_WINDUP) {
            this.finalPunchWindup.startIfStopped(this.tickCount);
        } else {
            this.finalPunchWindup.stop();
        }
        if (anim == FINAL_ATTACK) {
            this.finalPunch.startIfStopped(this.tickCount);
        } else {
            this.finalPunch.stop();
        }
        if (anim == BODY_LEAP) {
            this.bodyLeap.startIfStopped(this.tickCount);
        } else {
            this.bodyLeap.stop();
        }
        if (anim == DRAG) {
            this.drag.startIfStopped(this.tickCount);
        } else {
            this.drag.stop();
        }
        if (anim == DRAG_2) {
            this.drag2.startIfStopped(this.tickCount);
        } else {
            this.drag2.stop();
        }
        if (anim == FINAL_1) {
            this.finalPunch2.startIfStopped(this.tickCount);
        } else {
            this.finalPunch2.stop();
        }
        if (anim == FINAL_2) {
            this.finalPunch3.startIfStopped(this.tickCount);
        } else {
            this.finalPunch3.stop();
        }
        if (anim == IMPALE_2) {
            this.impale2.startIfStopped(this.tickCount);
        } else {
            this.impale2.stop();
        }
        if (anim == CHOP) {
            this.chop.startIfStopped(this.tickCount);
        } else {
            this.chop.stop();
        }
    }


    @Override
    public void tick(){
        super.tick();
    }

}
