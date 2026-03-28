package net.hydra.jojomod.entity.pathfinding;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersMagiciansRed;
import net.hydra.jojomod.stand.powers.PowersTusk;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

class TuskHoleAttackGoal extends MeleeAttackGoal {
    public TuskHoleAttackGoal(PathfinderMob $$0, double $$1, boolean $$2) {super($$0, $$1, $$2);}

    private boolean isAct3() {
        if (this.mob instanceof GroundPathfindingStandAttackEntity GPSA) {
            if (GPSA.getUser() != null) {
                StandUser standUser = (StandUser) GPSA.getUser();
                if (standUser.roundabout$getStandPowers() instanceof PowersTusk PT) {
                    if (PT.getAct() == 3) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    @Override
    public boolean canUse() {
        if (isAct3()) {return false;}
        return super.canUse();
    }
    @Override
    public boolean canContinueToUse() {
        if (isAct3()) {return false;}
        return super.canContinueToUse();
    }
}


public class TuskHoleEntity extends GroundPathfindingStandAttackEntity {
    public TuskHoleEntity(EntityType<? extends GroundPathfindingStandAttackEntity> $$0, Level $$1) {
        super($$0, $$1);
    }


    int lifespan = 150;

    private LivingEntity nearest;
    public TuskHoleEntity(EntityType<? extends GroundPathfindingStandAttackEntity> $$0, Level $$1, LivingEntity user) {
        super($$0, $$1);
        this.setUser(user);
    }
    public TuskHoleEntity(Level $$1, LivingEntity user) {
        super(ModEntities.TUSK_HOLE, $$1);
        this.setUser(user);
    }


    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(1,new TuskHoleAttackGoal(this,1.0,false));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        LivingEntity user = this.getUser();
        if (user != null &&
                ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersTusk PT) {

            if (target.hurt(ModDamageTypes.of(this.level(),ModDamageTypes.EXPLOSIVE_STAND,this,this.getUser()),PT.getHoleDamage(target) ) ){
                if (MainUtil.getMobBleed(target)) {
                    MainUtil.makeBleed(target,1,180,this.getUser());
                }
                
                if (!target.isAlive()) {
                    LivingEntity nearest = MainUtil.findClosestEntity(this.level(),target.getPosition(0),10,livingEntity -> (
                            livingEntity.isAlive() && !livingEntity.equals(this.getUser()) && !livingEntity.equals(target) && livingEntity.isAttackable()) && !(livingEntity instanceof Player P && P.isCreative())
                    );
                    if (nearest != null) {
                        Vec3 dir = nearest.getPosition(0).subtract(target.getPosition(0)).normalize();
                        MainUtil.takeLiteralUnresistableKnockbackWithY(target,dir.x*0.9,dir.y*1,dir.z*0.9);
                        this.setDeltaMovement(target.getDeltaMovement());
                        this.nearest = nearest;
                        return true;
                    }
                }
            }

        }
        this.discard();
        return true;
    }

    @Override
    public void tick() {
        boolean client = this.level().isClientSide();
        LivingEntity $$0 = this.getUser();
        super.tick();

        if (!client && nearest != null) {
            if (this.distanceTo(nearest) < 0.5) {
                this.doHurtTarget(nearest);
                nearest = null;
            }
        }

        if (!client && this.tickCount %2 == 0) {
            if ($$0 != null && ((StandUser) $$0).roundabout$getStandPowers() instanceof PowersTusk PT) {
                Vec3 $$2 = this.getDeltaMovement();
                double $$3 = this.getX() + $$2.x;
                double $$4 = this.getY() + $$2.y;
                double $$5 = this.getZ() + $$2.z;
                float xrand = (float) (Math.random()*1 - 0.5);
                float zrand = (float) (Math.random()*1 - 0.5);
                this.level().addParticle(ModParticles.BUBBLE_TRAIL, $$3, $$4 + 0.5, $$5, 0.0, 0.0, 0.0);

                ((ServerLevel) this.level()).sendParticles(ModParticles.BUBBLE_TRAIL, $$3,
                        $$4 + 0.1, $$5,
                        0,
                        xrand,
                        0.5,
                        zrand,
                        0.1);
            }
        }
    }
}
