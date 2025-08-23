package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersMagiciansRed;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class MagiciansRedEntity extends FollowingStandEntity {
    public MagiciansRedEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public final AnimationState finalKick = new AnimationState();
    public final AnimationState finalPunch = new AnimationState();
    public final AnimationState finalKickWindup = new AnimationState();
    public final AnimationState lash1 = new AnimationState();
    public final AnimationState lash2 = new AnimationState();
    public final AnimationState lash3 = new AnimationState();
    public final AnimationState flamethrower_charge = new AnimationState();
    public final AnimationState flamethrower_shoot = new AnimationState();
    public final AnimationState fireball_charge = new AnimationState();
    public final AnimationState fireball_shoot = new AnimationState();
    public final AnimationState red_bind = new AnimationState();
    public final AnimationState fire_crash = new AnimationState();
    public final AnimationState life_detector = new AnimationState();

    public final AnimationState hideLash = new AnimationState();
    public final AnimationState hideFlames = new AnimationState();
    public final AnimationState cycleFlames = new AnimationState();

    public boolean emitsFlameCycle(){
        byte skn = this.getSkin();
        return switch (skn) {
            case ABLAZE, DREAD_ABLAZE, LIGHTER_ABLAZE, BLUE_ABLAZE, PURPLE_ABLAZE, GREEN_ABLAZE, JOJONIUM_ABLAZE,
                 SKELETAL-> true;
            default -> false;
        };
    }

    public boolean isPutOutByWater(){
        if (this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR){
            return this.getUser().isUnderWater() || PMR.isInRain();
        }
        return false;
    }
    public boolean emitsLight(){
        if (ConfigManager.getClientConfig().magiciansRedLashesMakeItEmmissive && (this.lash1.isStarted() || this.lash2.isStarted() || lash3.isStarted())){
            return true;
        }
        if (isPutOutByWater()){
                return false;
        }
        byte skn = this.getSkin();
        if (!ConfigManager.getClientConfig().magiciansRedTexturesMakeItEmmissive){
            return false;
        }
        return switch (skn) {
            case ABLAZE, DREAD_ABLAZE, LIGHTER_ABLAZE, BLUE_ABLAZE, PURPLE_ABLAZE, GREEN_ABLAZE, MAGMA_SKIN,
                    MANGA_SKIN, JOJONIUM_ABLAZE, DEBUT_SKIN, SKELETAL -> true;
            default -> false;
        };
    }
    private boolean isInRain() {
        BlockPos $$0 = this.blockPosition();
        return this.level().isRainingAt($$0)
                || this.level().isRainingAt(BlockPos.containing((double)$$0.getX(), this.getBoundingBox().maxY, (double)$$0.getZ()));
    }
    public static final byte
            PART_3_SKIN = 1,
            BLUE_SKIN = 2,
            PURPLE_SKIN = 3,
            GREEN_SKIN = 4,
            DREAD_SKIN = 5,
            BLUE_ACE_SKIN = 6,
            DREAD_BEAST_SKIN = 7,
            MAGMA_SKIN = 8,
            MANGA_SKIN = 9,
            LIGHTER_SKIN = 10,
            OVA_SKIN = 11,
            ABLAZE = 12,
            LIGHTER_ABLAZE = 13,
            BLUE_ABLAZE = 14,
            PURPLE_ABLAZE = 15,
            GREEN_ABLAZE = 16,
            DREAD_ABLAZE = 17,
            SIDEKICK = 18,
            BETA = 19,
            JOJONIUM = 20,
            JOJONIUM_ABLAZE = 21,
            DEBUT_SKIN = 22,
            SKELETAL = 23;

    public static final byte
            FLAMETHROWER_LASH_1 = 41,
            FLAMETHROWER_LASH_2 = 42,
            FLAMETHROWER_LASH_3 = 43,
            FLAMETHROWER_CHARGE = 45,
            FLAMETHROWER_SHOOT = 46,
            FIREBALL_CHARGE = 47,
            FIREBALL_SHOOT = 48,
            RED_BIND = 49,
            LIFE_DETECTOR = 51,
            FIRE_CRASH = 60,
            CHARGED_PUNCH = 89;
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {

            if (emitsFlameCycle() && !isPutOutByWater()){
                this.cycleFlames.startIfStopped(this.tickCount);
                this.hideFlames.stop();
            } else {
                this.cycleFlames.stop();
                this.hideFlames.startIfStopped(this.tickCount);
            }

            byte animation = this.getAnimation();
            if (animation >= FLAMETHROWER_LASH_1 && animation <= FLAMETHROWER_LASH_3) {
                this.hideLash.stop();
                if (animation == FLAMETHROWER_LASH_1) {
                    this.lash1.startIfStopped(this.tickCount);
                } else {
                    this.lash1.stop();
                }
                if (animation == FLAMETHROWER_LASH_2) {
                    this.lash2.startIfStopped(this.tickCount);
                } else {
                    this.lash2.stop();
                }
                if (animation == FLAMETHROWER_LASH_3) {
                    this.lash3.startIfStopped(this.tickCount);
                } else {
                    this.lash3.stop();
                }
            } else {
                this.hideLash.startIfStopped(this.tickCount);
                this.lash1.stop();
                this.lash2.stop();
                this.lash3.stop();
            }

            if (this.getAnimation() == FLAMETHROWER_CHARGE) {
                this.flamethrower_charge.startIfStopped(this.tickCount);
            } else {
                this.flamethrower_charge.stop();
            }

            if (this.getAnimation() == FLAMETHROWER_SHOOT) {
                this.flamethrower_shoot.startIfStopped(this.tickCount);
            } else {
                this.flamethrower_shoot.stop();
            }

            if (this.getAnimation() == FIREBALL_CHARGE) {
                this.fireball_charge.startIfStopped(this.tickCount);
            } else {
                this.fireball_charge.stop();
            }

            if (this.getAnimation() == FIREBALL_SHOOT) {
                this.fireball_shoot.startIfStopped(this.tickCount);
            } else {
                this.fireball_shoot.stop();
            }
            if (this.getAnimation() == RED_BIND) {
                this.red_bind.startIfStopped(this.tickCount);
            } else {
                this.red_bind.stop();
            }
            if (this.getAnimation() == LIFE_DETECTOR) {
                this.life_detector.startIfStopped(this.tickCount);
            } else {
                this.life_detector.stop();
            }

            if (this.getAnimation() == FIRE_CRASH) {
                this.fire_crash.startIfStopped(this.tickCount);
            } else {
                this.fire_crash.stop();
            }
            if (this.getAnimation() == FINAL_ATTACK_WINDUP) {
                this.finalKickWindup.startIfStopped(this.tickCount);
            } else {
                this.finalKickWindup.stop();
            }
            if (this.getAnimation() == FINAL_ATTACK) {
                this.finalKick.startIfStopped(this.tickCount);
            } else {
                this.finalKick.stop();
            }
            if (this.getAnimation() == CHARGED_PUNCH) {
                this.finalPunch.startIfStopped(this.tickCount);
            } else {
                this.finalPunch.stop();
            }
        }
    }
}
