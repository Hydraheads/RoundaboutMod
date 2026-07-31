package net.hydra.jojomod.entity;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersKingCrimson;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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
    public int onGroundTime = 0;
    @Override
    public void tick() {
        if (!level().isClientSide()) {
            if (isMovingForward || isBackingUp) {
                if (getNavigation().isDone()) {
                    float direction = isBackingUp ? -1.0F : 1.0F;

                    float yaw = yBodyRot * ((float) Math.PI / 180F);
                    double distance = 20;
                    double x = getX() - Mth.sin(yaw) * distance * direction;
                    double z = getZ() + Mth.cos(yaw) * distance * direction;

                    getNavigation().moveTo(x, getY(), z, 1.0D);
                }
            }
            if (isSprinting && !isBackingUp){
                setSprinting( true);
            } if (isSneaking){
                setPose(Pose.CROUCHING);
            }
            if (onGround()){
                onGroundTime++;
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
        if (!level().isClientSide()) {

            if (isJumping && onGround() && onGroundTime >= 2) {
                jumpFromGround();

                onGroundTime= 0;
                getNavigation().recomputePath();
            }
        }
    }

    @Override
    protected void jumpFromGround() {
        Vec3 $$0 = this.getDeltaMovement();
        this.setDeltaMovement($$0.x, (double)this.getJumpPower(), $$0.z);
        if (this.isSprinting()) {
            float $$1 = this.getYRot() * (float) (Math.PI / 180.0);
            this.setDeltaMovement(this.getDeltaMovement().add((double)(-Mth.sin($$1) * 0.4F), 0, (double)(Mth.cos($$1) * 0.4F)));
        }

        this.hasImpulse = true;
    }
}
