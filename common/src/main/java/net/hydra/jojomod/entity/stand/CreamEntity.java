package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Unique;

public class CreamEntity extends FollowingStandEntity {

    public CreamEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public static final byte
            PART_3_SKIN = 1;

    public final AnimationState blockGrabAnimation = new AnimationState();
    public final AnimationState blockThrowAnimation = new AnimationState();
    public final AnimationState itemGrabAnimation = new AnimationState();
    public final AnimationState itemThrowAnimation = new AnimationState();
    public final AnimationState blockRetractAnimation = new AnimationState();
    public final AnimationState itemRetractAnimation = new AnimationState();
    public final AnimationState entityGrabAnimation = new AnimationState();
    public final AnimationState hideFists = new AnimationState();
    public final AnimationState hideLeg = new AnimationState();
    public final AnimationState finalPunch = new AnimationState();
    public final AnimationState finalPunchWindup = new AnimationState();
    public final AnimationState phaseGrab = new AnimationState();
    public final AnimationState creamVoidEat = new AnimationState();

    public static final byte
            CREAM_EAT_VOID = 100;

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            if (this.getAnimation() != BARRAGE) {
                this.hideFists.startIfStopped(this.tickCount);
            } else {
                this.hideFists.stop();
            }
            if (this.getAnimation() == BLOCK) {
                this.blockLoinAnimationState.startIfStopped(this.tickCount);
            } else {
                this.blockLoinAnimationState.stop();
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
            if (this.getAnimation() == CREAM_EAT_VOID) {
                this.creamVoidEat.startIfStopped(this.tickCount);
            } else {
                this.creamVoidEat.stop();
            }
        }
    }

    private int creamEatVoidStart = 0;

    @Override
    public Vec3 getIdleOffset(LivingEntity standUser) {
        if (this.getAnimation() == CREAM_EAT_VOID) {
            int animationTick = this.creamEatVoidStart;
            double y;

            if (animationTick <= 40) {
                y = standUser.getY() + 1 + ((-0.5 - 2) * ((double) animationTick / 40.0));
            } else {
                y = standUser.getY();
            }

            return new Vec3(standUser.getX(), y, standUser.getZ());
        } else {
            return super.getIdleOffset(standUser);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getAnimation() == CREAM_EAT_VOID) {
            creamEatVoidStart++;
        } else {
            creamEatVoidStart = 0;
        }
    }
}
