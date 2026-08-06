package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.stand.powers.PowersManhattanTransfer;
import net.hydra.jojomod.stand.powers.PowersSilverChariot;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SilverChariotEntity extends FollowingStandEntity {
    public SilverChariotEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public static final byte
            DEFAULT_SILVER_CHARIOT = 1;

    public static final short
            CONTROL_MODE = 0,
            SELF_CARRY_MODE = 1,
            HALF_CONTROL_MODE = 2;

    public final AnimationState sc = new AnimationState();
    public final AnimationState scBarrageCharge = new AnimationState();
    public final AnimationState scBarrage = new AnimationState();
    public final AnimationState scBarrageDamage = new AnimationState();
    public final AnimationState scBlock = new AnimationState();

    public static final byte
            SC_ = 40,
            SC_BLOCK = 41,
            SC_BARRAGE_CHARGE = 42,
            SC_BARRAGE = 43,
            SC_ATTACK_1 = 44,
            SC_ATTACK_2 = 45,
            SC_ATTACK_3 = 46,
            SC_IDLE_1 = 47,
            SC_IDLE_2 = 48,
            SC_IDLE_3 = 49,
            SC_IDLE_4 = 50,
            SC_BLOCK_BROKEN = 51,
            SC_BARRAGE_DAMAGE = 52,
            SC_MINING = 53,
            SC_ARMOR_SHED = 40,
            SC_RAPIER_SHOT = 40;

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        byte animationState = getAnimation();

        if (this.getUser() != null) {
            if (animationState == BLOCK) {
                this.scBlock.startIfStopped(this.tickCount);
            } else {
                this.scBlock.stop();
            }
            if (animationState == BARRAGE_CHARGE) {
                this.scBarrageCharge.startIfStopped(this.tickCount);
            } else {
                this.scBarrageCharge.stop();
            }
            if (animationState == BARRAGE) {
                this.scBarrage.startIfStopped(this.tickCount);
            } else {
                this.scBarrage.stop();
            }
            if (animationState == HURT_BY_BARRAGE) {
                this.scBarrageDamage.startIfStopped(this.tickCount);
            } else {
                this.scBarrageDamage.stop();
            }
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean lockPos() {
        return false;
    }

    @Override
    public boolean forceVisualRotation() {
        return false;
    }

    @Override
    public boolean canBeHitByProjectile() {
        if (this.getUserData(this.getUser()) != null) {
            if (this.getUserData(this.getUser()).roundabout$getStandPowers() instanceof PowersSilverChariot PSC) {
                // if (isDesummoning) {
                //    return false;
                // }
            }
        }
        return true;
    }

    @Override
    public boolean hasNoPhysics() {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return true;
    }

    @Override
    public boolean skipAttackInteraction(Entity $$0) {
        return false;
    }



    @Override
    public boolean isControlledByLocalInstance() {
        /*
        LivingEntity user = this.getUser();
        if (user != null) {
            Entity ent = this.getUserData(user).roundabout$getStandPowers().getPilotingStand();
            if (ent != null && ent.is(this)) {
                return (user instanceof Player $$0 ? $$0.isLocalPlayer() : this.isEffectiveAi());
            }
        }
        */
        return super.isControlledByLocalInstance();
    }

    @Override
    public void travel(Vec3 vec3) {
        /*
        super.travel(vec3);
        if (this.isControlledByLocalInstance()) {
            if (this.getUser() instanceof Player PE && this.level().isClientSide()) {
                C2SPacketUtil.updatePilot(this);
            }
        }
         */
        super.travel(vec3);
    }

    @Override
    public void tick() {
        super.tick();
    }
}
