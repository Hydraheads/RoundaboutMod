package net.hydra.jojomod.entity;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersKingCrimson;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class KingCrimsonCloneEntity extends CloneEntity {

    public int timer = 0;
    public KingCrimsonCloneEntity(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0, 0.0F));
        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, true));
    }

    @Override
    public boolean hurt(DamageSource $$0, float $$1) {
        if ($$0.is(ModDamageTypes.GO_BEYOND)){
            if (this.getPlayer() != null){
                this.getPlayer().hurt($$0,$$1);
            }
        }
        return super.hurt($$0,$$1);
    }

    public void discardStand(){
        StandEntity SE = ((StandUser)this).roundabout$getStand();
        if (SE != null){
            SE.discard();
        }
    }
    @Override
    public void tick() {
        if (!level().isClientSide()) {
            if (isJumping && onGround()) {
                jumpFromGround();
            }
            if (player == null) {
                discardStand();
                discard();

            } else if (
                    !(((StandUser) player).roundabout$getStandPowers() instanceof PowersKingCrimson pkc &&
                            pkc.timeEraseActive)
            ){
                discardStand();
                discard();
            }
        }
        super.tick();
    }

}
