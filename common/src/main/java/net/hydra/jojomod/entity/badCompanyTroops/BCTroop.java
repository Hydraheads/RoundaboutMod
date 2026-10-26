package net.hydra.jojomod.entity.badCompanyTroops;

import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.stand.MultipleTypeStand;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.BadCompanyTactics;
import net.hydra.jojomod.event.index.Tactics;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersBadCompany;
import net.hydra.jojomod.stand.powers.PowersSurvivor;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;

public class BCTroop extends MultipleTypeStand {
    protected BCTroop(EntityType<? extends MultipleTypeStand> entityType, Level world) {
        super(entityType, world);
    }

    private static final EntityDataAccessor<Byte> CURRENT_TACTIC =
            SynchedEntityData.defineId(BCTroop.class, EntityDataSerializers.BYTE);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CURRENT_TACTIC, (byte)0);
    }

    public byte getCurrentTactic() {
        return this.entityData.get(CURRENT_TACTIC);
    }
    public void setCurrentTactic(byte value) {
        this.entityData.set(CURRENT_TACTIC, value);
        if (value == BadCompanyTactics.STOP.id || value == BadCompanyTactics.RETURN.id) {
            getNavigation().stop();
            setTarget(null);
        }
    }

    public boolean validatePowers(LivingEntity user){
        return (((StandUser)user).roundabout$getStandPowers() instanceof PowersBadCompany);
    }

    @Override
    public boolean hasNoPhysics(){
        return false;
    }

    @Override
    public boolean needsActive(){
        return false;
    }

    @Override
    public void remove(RemovalReason $$0) {
        super.remove($$0);
        if (getUser() != null && ((StandUser)getUser()).roundabout$getStandPowers() instanceof PowersBadCompany PBC) {
            PBC.troopRemoved(this);
        }
    }

    @Override
    public boolean isValid(boolean userActive, LivingEntity thisStand, LivingEntity userEntity){
        if (userEntity != null && ((StandUser)userEntity).roundabout$getStandPowers() instanceof PowersBadCompany PBC) {
            if (!PBC.troopOnList(this)) {
                return false;
            }
        }else {
            return false;
        }

        return userEntity.isAlive() && validatePowers(userEntity);
    }

    public static int getTroopValue() {
        return 1;
    }

    public void tick() {
        super.tick();
        if ((getCurrentTactic() == BadCompanyTactics.RETURN.id) && distanceTo(getUser()) < 1.8
                || (getUser() != null && MainUtil.cheapDistanceTo2(this.getX(), this.getZ(), getUser().getX(), getUser().getZ())
                > getMaximumRange())) {
            discard();
        }
        if (fireCooldown > 0) {
            fireCooldown--;
        }else {
            performAttack();
        }

        float pitch = this.getXRot();
        float yaw = this.getYRot();

        this.setXRot(pitch);
        this.setYRot(yaw);
        this.setYBodyRot(yaw);
        this.xRotO = pitch;
        this.yRotO = yaw;
        setBodyRotationY(yaw);

    }

    public static int getMaximumRange() { return 15; }
    public static int getMaximumRangeBullets() { return 18; }

    public boolean wantsToAttack(LivingEntity target, LivingEntity user) {
        if (user == null) return false;
        if (user.distanceToSqr(target) > (getMaximumRangeBullets() * getMaximumRangeBullets())) {
            return false;
        }

        if (user.isAlliedTo(target)) {
            return false;
        }
        if (user instanceof Mob M) {
            if (M.getTarget() == target) { return true; }
        }

        if (user instanceof Raider && target instanceof Raider) {
            return false;
        }

        if ((user instanceof AbstractVillager || user instanceof IronGolem) && (target instanceof AbstractVillager || target instanceof IronGolem)) {
            return false;
        }

        if ((target instanceof TamableAnimal TA && TA.isOwnedBy(user)) || (user instanceof TamableAnimal TA1 && TA1.isOwnedBy(target))) {
            return false;
        }

        return true;
    }

    public int fireCooldown = 0;
    public int getFireCooldownMax() {
        return 5;
    }

    public boolean performAttack() {
        return false;
    }

}
