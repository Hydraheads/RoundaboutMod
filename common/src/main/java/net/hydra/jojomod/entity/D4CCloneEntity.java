package net.hydra.jojomod.entity;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.navigation.ActiveCloneManager;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersKingCrimson;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class D4CCloneEntity extends CloneEntity {

    public int timer = 0;
    public D4CCloneEntity(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
        this.goalSelector.addGoal(1, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(3, new FloatGoal(this));
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        ((GroundPathNavigation)this.getNavigation()).setCanPassDoors(true);
    }

    protected static final EntityDataAccessor<Boolean> JUMPING = SynchedEntityData.defineId(D4CCloneEntity.class,
            EntityDataSerializers.BOOLEAN);
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0, 0.0F));
    }

    public void addBehaviourGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, false));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, 5, false, false,
                $$0 -> $$0 instanceof Enemy && !($$0 instanceof Creeper)));
    }



    public boolean contains = false;
    public int onGroundTime = 0;
    @Override
    public void tick() {
//        if (!contains) {
//            contains = true;
//            ActiveCloneManager.add(this);
//        }
        super.tick();
    }
    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(JUMPING)) {
            super.defineSynchedData();
            this.entityData.define(JUMPING, false);
        }
    }

    private void redirectAggroBackToPlayer() {
        if (level().isClientSide() || this.getPlayer() == null) {
            return;
        }

        AABB search = getBoundingBox().inflate(64.0);

        for (Mob mob : level().getEntitiesOfClass(Mob.class, search)) {
            if (mob.getTarget() == this || (mob.getTarget() != null && mob.getTarget().getId() == this.getId())) {
                if (mob.distanceTo(this.getPlayer()) < 25 && (!(mob instanceof Monster) || mob instanceof EnderMan) &&
                mob.hasLineOfSight(this.getPlayer())){
                    ((StandUser) mob).roundabout$aggressivelyEnforceAggro(player);
                }
            }
        }
    }

    @Override
    public void remove(RemovalReason $$0) {
        redirectAggroBackToPlayer();
        super.remove($$0);
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
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.27).add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.ATTACK_DAMAGE, 1).
                add(Attributes.FOLLOW_RANGE, 48.0D);
    }
}
