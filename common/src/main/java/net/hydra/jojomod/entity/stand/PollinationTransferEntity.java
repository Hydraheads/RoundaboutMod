package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class PollinationTransferEntity extends ManhattanTransferEntity{
    public PollinationTransferEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public final AnimationState rain_dodging_flowerhattan = new AnimationState();
    public final AnimationState slow_flowerhattan = new AnimationState();
    public final AnimationState forward_flowerhattan_incipit = new AnimationState();
    public final AnimationState forward_flowerhattan_loop = new AnimationState();
    public final AnimationState back_flowerhattan_incipit = new AnimationState();
    public final AnimationState back_flowerhattan_loop = new AnimationState();
    public final AnimationState back_flowerhattan_stop = new AnimationState();
    public final AnimationState forward_flowerhattan_stop = new AnimationState();
    public final AnimationState left_flowerhattan_incipit = new AnimationState();
    public final AnimationState left_flowerhattan_loop = new AnimationState();
    public final AnimationState left_flowerhattan_stop = new AnimationState();
    public final AnimationState right_flowerhattan_incipit = new AnimationState();
    public final AnimationState right_flowerhattan_loop = new AnimationState();
    public final AnimationState right_flowerhattan_stop = new AnimationState();
    public final AnimationState slow_flowerhattan_back = new AnimationState();
    public final AnimationState slow_flowerhattan_left = new AnimationState();
    public final AnimationState slow_flowerhattan_right = new AnimationState();
    public final AnimationState flowerhattan_is_loaded = new AnimationState();

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        AnimationState rd = this.rain_dodging_flowerhattan;
        AnimationState loaded = this.flowerhattan_is_loaded;

        AnimationState forBeg = this.forward_flowerhattan_incipit;
        AnimationState forLoop = this.forward_flowerhattan_loop;
        AnimationState forStop = this.forward_flowerhattan_stop;

        AnimationState backBeg = this.back_flowerhattan_incipit;
        AnimationState backLoop = this.back_flowerhattan_loop;
        AnimationState backStop = this.back_flowerhattan_stop;

        AnimationState leftBeg = this.left_flowerhattan_incipit;
        AnimationState leftLoop = this.left_flowerhattan_loop;
        AnimationState leftStop = this.left_flowerhattan_stop;

        AnimationState rightBeg = this.right_flowerhattan_incipit;
        AnimationState rightLoop = this.right_flowerhattan_loop;
        AnimationState rightStop = this.right_flowerhattan_stop;

        AnimationState forSlow = this.slow_flowerhattan;
        AnimationState backSlow = this.slow_flowerhattan_back;
        AnimationState leftSlow = this.slow_flowerhattan_left;
        AnimationState rightSlow = this.slow_flowerhattan_right;


        if (this.level().isClientSide) {
            if (!this.isInRain()) {
                if ((W || A || S || D) && isHattanPilotMode) {
                    isPressing = true;
                } else if ((W && S) || (A && D)){
                    isPressing = false;
                }else {
                    isPressing = false;
                }
                rd.stop();
                if (!this.stopsManhattanAnimationsWhenHeldItem) {
                    loaded.stop();
                    if (isPressing) {
                        forSlow.stop();
                        backSlow.stop();
                        leftSlow.stop();
                        rightSlow.stop();
                        if (W) {
                            backLoop.stop();
                            forStop.stop();
                            forBeg.startIfStopped(this.tickCount);
                            forLoop.startIfStopped(this.tickCount);
                        }
                        if (!W) {
                            forBeg.stop();
                            forLoop.stop();
                            forStop.startIfStopped(this.tickCount);
                        }
                        if (S) {
                            forLoop.stop();
                            backStop.stop();
                            backBeg.startIfStopped(this.tickCount);
                            backLoop.startIfStopped(this.tickCount);
                        }
                        if (!S) {
                            backLoop.stop();
                            backBeg.stop();
                            backStop.startIfStopped(this.tickCount);
                        }

                        if (A) {
                            rightLoop.stop();
                            leftStop.stop();
                            leftBeg.startIfStopped(this.tickCount);
                            leftLoop.startIfStopped(this.tickCount);
                        }
                        if (!A) {
                            leftBeg.stop();
                            leftLoop.stop();
                            leftStop.startIfStopped(this.tickCount);
                        }

                        if (D) {
                            leftLoop.stop();
                            rightStop.stop();
                            rightBeg.startIfStopped(this.tickCount);
                            rightLoop.startIfStopped(this.tickCount);
                        }
                        if (!D) {
                            rightBeg.stop();
                            rightLoop.stop();
                            rightStop.startIfStopped(this.tickCount);
                        }
                    } else {
                        if (forLoop.isStarted()) {
                            forBeg.stop();
                            forLoop.stop();
                            backLoop.stop();
                            backBeg.stop();
                            forStop.startIfStopped(this.tickCount);
                        }
                        if (backLoop.isStarted()) {
                            backLoop.stop();
                            backBeg.stop();
                            forBeg.stop();
                            forLoop.stop();
                            backStop.startIfStopped(this.tickCount);
                        }
                        if (leftLoop.isStarted()) {
                            leftLoop.stop();
                            leftBeg.stop();
                            rightBeg.stop();
                            rightLoop.stop();
                            leftStop.startIfStopped(this.tickCount);
                        }
                        if (rightLoop.isStarted()) {
                            leftLoop.stop();
                            leftBeg.stop();
                            rightBeg.stop();
                            rightLoop.stop();
                            rightStop.startIfStopped(this.tickCount);
                        }

                        if (setHatAnimDir == 1) {
                            forSlow.startIfStopped(this.tickCount);
                            backSlow.stop();
                            leftSlow.stop();
                            rightSlow.stop();
                        }
                        if (setHatAnimDir == 2) {
                            backSlow.startIfStopped(this.tickCount);
                            leftSlow.stop();
                            rightSlow.stop();
                            forSlow.stop();
                        }
                        if (setHatAnimDir == 3) {
                            leftSlow.startIfStopped(this.tickCount);
                            rightSlow.stop();
                            forSlow.stop();
                            backSlow.stop();
                        }
                        if (setHatAnimDir == 4) {
                            rightSlow.startIfStopped(this.tickCount);
                            leftSlow.stop();
                            backSlow.stop();
                            forSlow.stop();
                        }
                    }
                } else {
                    loaded.startIfStopped(this.tickCount);
                    forLoop.stop();
                    forBeg.stop();
                    forStop.stop();
                    forSlow.stop();
                    backBeg.stop();
                    backSlow.stop();
                    backLoop.stop();
                    backStop.stop();
                    leftSlow.stop();
                    leftBeg.stop();
                    leftStop.stop();
                    leftLoop.stop();
                    rightBeg.stop();
                    rightSlow.stop();
                    rightStop.stop();
                    rightLoop.stop();
                }
            } else {
                rd.startIfStopped(this.tickCount);
                loaded.stop();
                forLoop.stop();
                forBeg.stop();
                forStop.stop();
                forSlow.stop();
                backBeg.stop();
                backSlow.stop();
                backLoop.stop();
                backStop.stop();
                leftSlow.stop();
                leftBeg.stop();
                leftStop.stop();
                leftLoop.stop();
                rightBeg.stop();
                rightSlow.stop();
                rightStop.stop();
                rightLoop.stop();
            }
        }
    }
}
