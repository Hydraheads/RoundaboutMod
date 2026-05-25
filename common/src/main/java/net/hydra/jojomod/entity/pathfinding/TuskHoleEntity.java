package net.hydra.jojomod.entity.pathfinding;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersTusk;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.List;


public class TuskHoleEntity extends GroundPathfindingStandAttackEntity {
    public TuskHoleEntity(EntityType<? extends GroundPathfindingStandAttackEntity> $$0, Level $$1) {
        super($$0, $$1);
    }


    private int timeInHole = 0;
    public int getTimeInHole() {
        return timeInHole;
    }
    private static final EntityDataAccessor<Boolean> VORTEX = SynchedEntityData.defineId(TuskHoleEntity.class, EntityDataSerializers.BOOLEAN);
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        if (!this.entityData.hasItem(VORTEX)) {
            this.entityData.define(VORTEX, false);
        }
    }

    int lifespan = 150;
    public boolean isVortex() {return entityData.get(VORTEX);}

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
    protected float getStandingEyeHeight(Pose $$0, EntityDimensions $$1) {
        return 0.6F;
    }

    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(1,new TuskHoleAttackGoal(this,1.0F,false));
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

            if (target.hurt(ModDamageTypes.of(this.level(),ModDamageTypes.PENETRATING_STAND,this,this.getUser()),PT.getHoleDamage(target) ) ){
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
    public void onEnd() {
        if (this.isVortex()) {
            Vec3 pos = this.getPosition(0);
            List<Entity> targets = MainUtil.genHitbox(this.level(), pos.x, pos.y, pos.z, 1, 1, 1);
            targets.removeIf(entity -> !entity.isAttackable() || entity.isInvulnerable() || entity.equals(this.getUser()));

            for (Entity target : targets) {
                if (target.hurt(ModDamageTypes.of(this.level(), ModDamageTypes.STAND), 2)) {
                    if (target instanceof LivingEntity LE) {
                        LE.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, 20));
                        LE.addEffect(new MobEffectInstance(ModEffects.BLEED, 20));
                    }
                }
            }
        }
    }

    @Override
    public void tick() {
        boolean client = this.level().isClientSide();
        LivingEntity $$0 = this.getUser();
        super.tick();

        if (this.level().isClientSide()) {
            if (isPiloted()) {
                this.timeInHole += 1;
            } else {
                this.timeInHole = 0;
            }
        }

        if (this.getUser() != null) { // doubles lifespan during act 3
            if (((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersTusk PT) {
                if (PT.getAct() == 3 && !this.isVortex()) {
                    if (!this.level().isClientSide() && this.tickCount % 2 == 0) {
                        this.lifeSpan ++;
                    }
                }
            }
        }


        if (!client) {
            if (!isVortex()) {
                if (isInWater()) {
                    vortexify();
                }

                if (nearest != null) {
                    if (this.distanceTo(nearest) < 0.5) {
                        this.doHurtTarget(nearest);
                        nearest = null;
                    }
                }

                if ($$0 != null && ((StandUser) $$0).roundabout$getStandPowers() instanceof PowersTusk) {
                    Vec3 vec3 = this.getDeltaMovement();
                    double x = this.getX() + vec3.x;
                    double y = this.getY();
                    double z = this.getZ() + vec3.z;

                    BlockPos check = this.blockPosition();
                    for (int i=0;i<3;i++) {
                        check.below();
                        if (this.level().getBlockState(check).isSolid()) {
                            break;
                        }
                    }

                    BlockState bs = this.level().getBlockState(this.blockPosition());
                    y = y%1;
                    if (!bs.isAir()) {
                        double height = bs.getBlock().getVisualShape(bs,this.level(),this.blockPosition(), CollisionContext.of(this)).max(Direction.Axis.Y);
                        y += height-y;
                    }

                    ((ServerLevel) this.level()).sendParticles(ModParticles.TUSK_HOLE, x, check.getY() + y  + 0.04, z, 0, 0, 0, 0, 0.1);
                }
            } else {
                Vec3 pos = this.getPosition(0);


                if (this.tickCount % 2 == 0) {
                    float radius = 7.5F;
                    List<Entity> targets = MainUtil.genHitbox(this.level(),
                            pos.x, pos.y, pos.z,
                            radius, radius, radius);
                    targets.removeIf(target -> {
                        if (target instanceof Player P && P.getAbilities().flying) {return true;}
                        if (target.equals(this) || target.equals(this.getUser())) {return true;}
                        if (target.distanceTo(this) > radius) {return true;}
                        if ( ((!target.isAttackable() || !target.isAlive()) || target instanceof ItemEntity)   && target instanceof StandEntity) {return true;}

                        if (target.isInWater()) {
                            Vec3 start = pos;
                            Vec3 end = target.getPosition(0);
                            BlockHitResult blockHit = this.level().clip(new ClipContext(start, end,
                                    ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                            FluidState fluidState = this.level().getFluidState(blockHit.getBlockPos());
                            if (fluidState.is(Fluids.EMPTY)) {
                                return true;
                            }
                        } else {
                            return true;
                        }
                        return false;
                    });
                    for (Entity target : targets) {
                        Vec3 dir = target.getPosition(0).subtract(pos);
                        float strength = (float) dir.length();
                        dir = dir.normalize();
                        MainUtil.takeKnockbackWithY(target, (strength / radius) * 2, dir.x, dir.y, dir.z);
                    }
                }
            }
        }
    }

    public void vortexify() {
        lifeSpan = 20;
        this.entityData.set(VORTEX,true);
    }

    @Override
    protected void goDownInWater() {}
    @Override
    public boolean isNoGravity() {return super.isNoGravity() || this.isVortex();}

    private boolean isPiloted() {
        if (this.getUser() instanceof Player P) {
            StandUser SU = (StandUser) this.getUser();
            if (SU.roundabout$getStandPowers() instanceof PowersTusk PT && PT.isPiloting() && PT.getPilotingStand().equals(this)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canPickUpLoot() {
        if (isPiloted() && this.getUser() instanceof Player P) {
            return P.getInventory().getFreeSlot() != -1;
        }
        return false;
    }

    @Override
    public boolean wantsToPickUp(ItemStack $$0) {
        return true;
    }

    @Override
    protected void pickUpItem(ItemEntity $$0) {
       if (isPiloted() && this.getUser() instanceof Player P) {
           P.getInventory().add($$0.getItem());
       }
    }
}

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
    protected void checkAndPerformAttack(LivingEntity $$0, double $$1) {
        double $$2 = 2;
        if ($$1 <= $$2) {
            this.mob.doHurtTarget($$0);
        }


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