package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class BlazeTransferEntity extends ManhattanTransferEntity{
    public BlazeTransferEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public final AnimationState rain_dodging_blazehattan = new AnimationState();
    public final AnimationState forward_blazehattan_incipit = new AnimationState();
    public final AnimationState forward_blazehattan_loop = new AnimationState();
    public final AnimationState back_blazehattan_incipit = new AnimationState();
    public final AnimationState back_blazehattan_loop = new AnimationState();
    public final AnimationState back_blazehattan_stop = new AnimationState();
    public final AnimationState forward_blazehattan_stop = new AnimationState();
    public final AnimationState left_blazehattan_incipit = new AnimationState();
    public final AnimationState left_blazehattan_loop = new AnimationState();
    public final AnimationState left_blazehattan_stop = new AnimationState();
    public final AnimationState right_blazehattan_incipit = new AnimationState();
    public final AnimationState right_blazehattan_loop = new AnimationState();
    public final AnimationState right_blazehattan_stop = new AnimationState();
    public final AnimationState blazehattan_is_loaded = new AnimationState();
    public final AnimationState spinny_boi = new AnimationState();

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        AnimationState rd = this.rain_dodging_blazehattan;
        AnimationState loaded = this.blazehattan_is_loaded;

        AnimationState forBeg = this.forward_blazehattan_incipit;
        AnimationState forLoop = this.forward_blazehattan_loop;
        AnimationState forStop = this.forward_blazehattan_stop;

        AnimationState backBeg = this.back_blazehattan_incipit;
        AnimationState backLoop = this.back_blazehattan_loop;
        AnimationState backStop = this.back_blazehattan_stop;

        AnimationState leftBeg = this.left_blazehattan_incipit;
        AnimationState leftLoop = this.left_blazehattan_loop;
        AnimationState leftStop = this.left_blazehattan_stop;

        AnimationState rightBeg = this.right_blazehattan_incipit;
        AnimationState rightLoop = this.right_blazehattan_loop;
        AnimationState rightStop = this.right_blazehattan_stop;

        AnimationState spinny_boy = this.spinny_boi;


        if (this.level().isClientSide) {
            spinny_boy.startIfStopped(this.tickCount);
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
                    }
                } else {
                    loaded.startIfStopped(this.tickCount);
                    forLoop.stop();
                    forBeg.stop();
                    forStop.stop();
                    backBeg.stop();
                    backLoop.stop();
                    backStop.stop();
                    leftBeg.stop();
                    leftStop.stop();
                    leftLoop.stop();
                    rightBeg.stop();
                    rightStop.stop();
                    rightLoop.stop();
                }
            } else {
                rd.startIfStopped(this.tickCount);
                loaded.stop();
                forLoop.stop();
                forBeg.stop();
                forStop.stop();
                backBeg.stop();
                backLoop.stop();
                backStop.stop();
                leftBeg.stop();
                leftStop.stop();
                leftLoop.stop();
                rightBeg.stop();
                rightStop.stop();
                rightLoop.stop();
            }
        }
    }
}
