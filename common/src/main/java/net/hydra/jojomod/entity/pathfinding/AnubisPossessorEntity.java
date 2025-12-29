package net.hydra.jojomod.entity.pathfinding;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;

public class AnubisPossessorEntity extends GroundPathfindingStandAttackEntity {


    static class KillEverythingGoal
            extends NearestAttackableTargetGoal<LivingEntity> {
        public KillEverythingGoal(AnubisPossessorEntity e) {
            super(e, LivingEntity.class, 0, true, true, livingEntity -> !((StandUser)livingEntity).roundabout$isPossessed() );
        }

        /* look at this to change the attack range
        protected double getAttackReachSqr(LivingEntity $$0) {
            float $$1 = Ravager.this.getBbWidth() - 0.1F;
            return (double)($$1 * 2.0F * $$1 * 2.0F + $$0.getBbWidth());
        } */

    }

    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.8F)
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.JUMP_STRENGTH,3.0)
                .add(Attributes.FOLLOW_RANGE,50.0);
    }


    @Override
    public void tick() {
        super.tick();
        if (this.getUser() != null) {
            StandUser SU = (StandUser) this.getUser();
            if (SU.roundabout$getPossessionTime() <= 0) {
                SU.roundabout$setPossessor(null);
                discard();
            }
        }
    }

    @Override
    protected void registerGoals() {
        addBehaviourGoals();
        this.targetSelector.addGoal(4, new KillEverythingGoal(this));
    }


    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.8, false));
    }

    public AnubisPossessorEntity(Level $$1, LivingEntity user) {
        super(ModEntities.ANUBIS_POSSESSOR, $$1);
        ((StandUser) user ).roundabout$setPossessor(this);
        this.setUser(user);
    }
    public AnubisPossessorEntity(EntityType<? extends GroundPathfindingStandAttackEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

}

