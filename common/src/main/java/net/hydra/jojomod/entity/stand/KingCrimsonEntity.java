package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.access.ILivingEntityAccess;
import net.hydra.jojomod.entity.KingCrimsonCloneEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

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
            VISION = 18,
            REAPER = 19,
            NUCLEAR = 20;


    public final AnimationState finalPunch = new AnimationState();
    public final AnimationState finalPunch2 = new AnimationState();
    public final AnimationState finalPunch3 = new AnimationState();
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
    public final AnimationState entityGrabAnimation = new AnimationState();
    public final AnimationState bloodSplashWindup = new AnimationState();
    public final AnimationState bloodSplashThrow = new AnimationState();

    public static final byte
            FINAL_1 = 82,
            FINAL_2 = 83,
            IMPALE_2 = 84,
            BLOOD_SPLASH_WINDUP = 50,
            BLOOD_SPLASH_THROW = 51;
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
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
            if (animation == BLOOD_SPLASH_WINDUP) {
                this.bloodSplashWindup.startIfStopped(this.tickCount);
            } else {
                this.bloodSplashWindup.stop();
            }
            if (animation == BLOOD_SPLASH_THROW) {
                this.bloodSplashThrow.startIfStopped(this.tickCount);
            } else {
                this.bloodSplashThrow.stop();
            }

            if (this.getAnimation() == ITEM_GRAB) {
                this.itemGrabAnimation.startIfStopped(this.tickCount);
            } else {
                this.itemGrabAnimation.stop();
            }
            if (this.getAnimation() == ENTITY_GRAB) {
                this.entityGrabAnimation.startIfStopped(this.tickCount);
            } else {
                this.entityGrabAnimation.stop();
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
            if (this.getAnimation() == FINAL_1) {
                this.finalPunch2.startIfStopped(this.tickCount);
            } else {
                this.finalPunch2.stop();
            }
            if (this.getAnimation() == FINAL_2) {
                this.finalPunch3.startIfStopped(this.tickCount);
            } else {
                this.finalPunch3.stop();
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

    @Override
    public void tryHardTimeEraseRendering(){
        if (getUser() != null && getUser() instanceof KingCrimsonCloneEntity kc){
            if (!turned){
                if (kc.getPlayer() != null && ((StandUser)kc.getPlayer()).roundabout$getStand() instanceof
                        KingCrimsonEntity pl) {
                    turned = true;
                    // Position
                    this.setPos(pl.getX(), pl.getY(), pl.getZ());
                    this.xOld = pl.xOld;
                    this.yOld = pl.yOld;
                    this.zOld = pl.zOld;

                    // Body rotation
                    this.setYRot(pl.getYRot());
                    this.yRotO = pl.yRotO;

                    // Pitch
                    this.setXRot(pl.getXRot());
                    this.xRotO = pl.xRotO;

                    // Body/head rotations
                    this.yBodyRot = pl.yBodyRot;
                    this.yBodyRotO = pl.yBodyRotO;
                    this.yHeadRot = pl.yHeadRot;
                    this.yHeadRotO = pl.yHeadRotO;

                    // Animation
                    this.walkAnimation.setSpeed(pl.walkAnimation.speed());
                    this.walkAnimation.position(pl.walkAnimation.position());
                    ILivingEntityAccess entityAndData = ((ILivingEntityAccess) this);
                    ILivingEntityAccess playerAndData = ((ILivingEntityAccess) pl);

                    entityAndData.roundabout$setLerpXRot(playerAndData.roundabout$getLerpXRot());
                    entityAndData.roundabout$setLerpYRot(playerAndData.roundabout$getLerpYRot());
                    entityAndData.roundabout$setLerp(new Vector3f(
                            (float) playerAndData.roundabout$getLerpX(),
                            (float) playerAndData.roundabout$getLerpY(),
                            (float) playerAndData.roundabout$getLerpZ()
                    ));
                }
            }
        }
    }
}
