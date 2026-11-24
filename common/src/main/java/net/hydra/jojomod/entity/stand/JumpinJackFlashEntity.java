package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.event.powers.jumpingJackFlash.ZeroGravityAccess;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersJumpinJackFlash;
import net.minecraft.world.level.Level;

public class JumpinJackFlashEntity extends FollowingStandEntity {
    private static final EntityDataAccessor<Boolean> ARMS_MODE =
            SynchedEntityData.defineId(JumpinJackFlashEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> ZERO_G_DRIFT =
            SynchedEntityData.defineId(JumpinJackFlashEntity.class, EntityDataSerializers.BOOLEAN);
    private static final byte SPIT_EVENT = 104;
    private static final EntityDataAccessor<Boolean> LOADING_SPIT =
            SynchedEntityData.defineId(JumpinJackFlashEntity.class, EntityDataSerializers.BOOLEAN);
    private int lastLoadSignal;

    private int spitEndTick;

    public boolean isLoadingSpit() {
        return entityData.get(LOADING_SPIT);
    }

    public void refreshSpitLoad() {
        if (!isLoadingSpit()) {
            entityData.set(LOADING_SPIT, true);
        }
        lastLoadSignal = tickCount;
    }

    public void stopSpitLoad() {
        entityData.set(LOADING_SPIT, false);
    }
    public final AnimationState driftAnimationState = new AnimationState();
    public final AnimationState spitAnimationState = new AnimationState();

    public void playSpitAnimation() {
        level().broadcastEntityEvent(this, SPIT_EVENT);
    }

    @Override
    public void handleEntityEvent(byte event) {
        if (event == SPIT_EVENT) {
            spitAnimationState.start(tickCount);
            spitEndTick = tickCount + 16;
        } else {
            super.handleEntityEvent(event);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide()) {
            if (isLoadingSpit() && (tickCount - lastLoadSignal > 8
                    || !(getUser() instanceof Player player)
                    || !(player instanceof StandUser user) || user.roundabout$getStand() != this
                    || !(user.roundabout$getStandPowers() instanceof PowersJumpinJackFlash)
                    || !PowerTypes.hasStandActive(player))) {
                stopSpitLoad();
            }
            entityData.set(ZERO_G_DRIFT, getUser() instanceof ZeroGravityAccess access
                    && access.jjf$getZeroGravityState().isZeroGravity());
        }
    }

    public static final byte JJF_SHOOT_WINDUP_ANIMATION = 81;
    public static final byte JJF_SHOOT_LOOP_ANIMATION = 82;
    public final AnimationState jjfPunchAnimationState = new AnimationState();
    public final AnimationState jjfShootWindupAnimationState = new AnimationState();
    public final AnimationState jjfShootLoopAnimationState = new AnimationState();

    public JumpinJackFlashEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(ARMS_MODE, false);
        entityData.define(LOADING_SPIT, false);
        entityData.define(ZERO_G_DRIFT, false);
    }

    public boolean jjf$isArmsMode() {
        return entityData.get(ARMS_MODE);
    }

    public void jjf$setArmsMode(boolean enabled) {
        entityData.set(ARMS_MODE, enabled);
    }

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        byte animation = getAnimation();
        if (tickCount >= spitEndTick || animation != IDLE || jjf$isArmsMode()) {
            spitAnimationState.stop();
        }
        if (entityData.get(ZERO_G_DRIFT) && animation == IDLE && !jjf$isArmsMode()
                && getBsGrabbed() == 0 && !spitAnimationState.isStarted()) {
            driftAnimationState.startIfStopped(tickCount);
        } else {
            driftAnimationState.stop();
        }
        if (animation == StandEntity.BARRAGE_CHARGE) {
            barrageChargeAnimationState.stop();
            jjfPunchAnimationState.startIfStopped(this.tickCount);
        } else {
            jjfPunchAnimationState.stop();
        }
        if (animation == JJF_SHOOT_WINDUP_ANIMATION) {
            jjfShootWindupAnimationState.startIfStopped(this.tickCount);
        } else {
            jjfShootWindupAnimationState.stop();
        }
        if (animation == JJF_SHOOT_LOOP_ANIMATION) {
            jjfShootLoopAnimationState.startIfStopped(this.tickCount);
        } else {
            jjfShootLoopAnimationState.stop();
        }
    }
}
