package net.hydra.jojomod.entity.pathfinding;

import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class AnubisPossessorEntity extends GroundPathfindingStandAttackEntity {



    static class KillAllTargets extends NearestAttackableTargetGoal<LivingEntity> {
        public KillAllTargets(AnubisPossessorEntity e) {
            super(e, LivingEntity.class, 0, true, true, livingEntity -> !((StandUser)livingEntity).roundabout$isPossessed() );
        }

        @Override
        protected void findTarget() {
            double dist = 1000;
            AnubisPossessorEntity Mob = ((AnubisPossessorEntity)this.mob);
            for (int i=0;i<Mob.targets.size();i++ ) {
                LivingEntity target = Mob.targets.get(i);
                double tDist = target.distanceTo(this.mob);
                if (dist > tDist && this.mob.getSensing().hasLineOfSight(target) && target.isAlive()) {
                    dist = tDist;
                    this.target = target;
                    if (((AnubisPossessorEntity) this.mob).getUser() instanceof Player P) {
                        S2CPacketUtil.syncPossessor(P,this.target.getId());
                    }
                } else if (!this.mob.getSensing().hasLineOfSight(target) || !target.isAlive()) {
                    Mob.targets.remove(target);
                }
            }
        }

        @Override
        protected double getFollowDistance() {
            return 1000;
        }
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
            if (this.targets.isEmpty() && !this.level().isClientSide()) {
                SU.roundabout$setPossessor(null);
                discard();
            }
        }
    }

    @Override
    protected void registerGoals() {
        addBehaviourGoals();
        this.targetSelector.addGoal(4, new KillAllTargets(this));
    }


    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.8, false));
    }

    public List<LivingEntity> targets = new ArrayList<>();
    public AnubisPossessorEntity(Level $$1, LivingEntity user, List<LivingEntity> targets) {
        super(ModEntities.ANUBIS_POSSESSOR, $$1);
        ((StandUser) user ).roundabout$setPossessor(this);
        this.setUser(user);
        this.targets = targets;
        this.lifeSpan = PowersAnubis.MaxPossessionTime;
        this.setMaxUpStep(1.5F);
    }
    public AnubisPossessorEntity(EntityType<? extends GroundPathfindingStandAttackEntity> $$0, Level $$1) {
        super($$0,$$1);
    }

}

