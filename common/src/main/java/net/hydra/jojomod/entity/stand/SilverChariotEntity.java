package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.stand.powers.PowersManhattanTransfer;
import net.hydra.jojomod.stand.powers.PowersSilverChariot;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
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

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
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
        return true;
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
        LivingEntity user = this.getUser();
        if (user != null) {
            Entity ent = this.getUserData(user).roundabout$getStandPowers().getPilotingStand();
            if (ent != null && ent.is(this)) {
                return (user instanceof Player $$0 ? $$0.isLocalPlayer() : this.isEffectiveAi());
            }
        }
        return super.isControlledByLocalInstance();
    }

    @Override
    public void travel(Vec3 vec3) {
        super.travel(vec3);
        if (this.isControlledByLocalInstance()) {
            if (this.getUser() instanceof Player PE && this.level().isClientSide()) {
                C2SPacketUtil.updatePilot(this);
            }
        }

    }

    @Override
    public void tick() {
        super.tick();
    }
}
