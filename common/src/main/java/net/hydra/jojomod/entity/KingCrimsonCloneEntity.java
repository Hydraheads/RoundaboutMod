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
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class KingCrimsonCloneEntity extends CloneEntity {

    public int timer = 0;
    public KingCrimsonCloneEntity(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
        this.goalSelector.addGoal(1, new OpenDoorGoal(this, true));
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        ((GroundPathNavigation)this.getNavigation()).setCanPassDoors(true);
    }

    protected static final EntityDataAccessor<Boolean> JUMPING = SynchedEntityData.defineId(KingCrimsonCloneEntity.class,
            EntityDataSerializers.BOOLEAN);
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
    public final void setIsJumping(boolean jumping) {
        this.entityData.set(JUMPING, jumping);
    }
    public final boolean getIsJumping() {
        return this.entityData.get(JUMPING);
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

    @Override
    public float getSpeed(){
        float spd = super.getSpeed();
        if (isSneaking){
            spd*=0.3F;
        }
        return spd;
    }

    @Override
    public void die(DamageSource source) {
        if (!this.level().isClientSide && this.level().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getPlayer() instanceof ServerPlayer sp
        && ((StandUser)sp).roundabout$getStandPowers() instanceof PowersKingCrimson pkc) {

            if (ClientNetworking.getAppropriateConfig().kingCrimsonSettings.skipPastDeath) {
                if (!pkc.fakedDeath) {
                    if (!this.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                        dropInventoryAsFakeItems(player);
                    }

                    double range = pkc.getSkipBonusRange();
                    double rangeSqr = range * range;

                    Component message = this.getCombatTracker().getDeathMessage();

                    for (ServerPlayer player : ((ServerLevel) level()).players()) {
                        if (player.distanceToSqr(this) <= rangeSqr) {
                            player.sendSystemMessage(message);
                        }
                    }

                    pkc.fakedDeath = true;
                }
            }
        }
        super.die(source);
    }



    private void dropInventoryAsFakeItems(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.isEmpty()) {
                spawnFakeItem(stack);
            }
        }

        for (ItemStack stack : player.getInventory().armor) {
            if (!stack.isEmpty()) {
                spawnFakeItem(stack);
            }
        }

        for (ItemStack stack : player.getInventory().offhand) {
            if (!stack.isEmpty()) {
                spawnFakeItem(stack);
            }
        }
    }

    private void spawnFakeItem(ItemStack stack) {
        float angle = this.random.nextFloat() * ((float)Math.PI * 2F);
        float speed = this.random.nextFloat() * 0.5F;

        double vx = -Mth.sin(angle) * speed;
        double vz = Mth.cos(angle) * speed;

        FakeItemEntity item = new FakeItemEntity(
                ModEntities.FAKE_ITEM,
                level()
        );

        item.setItem(stack.copy());
        item.setPos(getX(), getEyeY() - 0.3, getZ());
        item.setDeltaMovement(vx, 0.2F, vz);

        item.host = getPlayer();

        level().addFreshEntity(item);
    }

    public void discardStand(){
        StandEntity SE = ((StandUser)this).roundabout$getStand();
        if (SE != null){
            SE.discard();
        }
    }
    public boolean contains = false;
    public int onGroundTime = 0;
    @Override
    public void tick() {
        if (!level().isClientSide()) {
            if (!contains) {
                contains = true;
                ActiveCloneManager.add(this);
            }
            if (isMovingForward || isBackingUp) {
                if (getNavigation().isDone()) {
                    float direction = isBackingUp ? -1.0F : 1.0F;

                    float yaw = yBodyRot * ((float) Math.PI / 180F);
                    double distance = 20;
                    double x = getX() - Mth.sin(yaw) * distance * direction;
                    double z = getZ() + Mth.cos(yaw) * distance * direction;
                    double speed = 1.0D;
                    getNavigation().moveTo(x, getY(), z, speed);
                }
            }

            // This code basically makes it stop at ledges while sneaking
            if (isSneaking) {
                float yaw = getYRot() * ((float)Math.PI / 180F);

                double step = 0.5D;

                double checkX = getX() - Mth.sin(yaw) * step;
                double checkZ = getZ() + Mth.cos(yaw) * step;

                BlockPos belowAhead = BlockPos.containing(
                        checkX,
                        getY() - 1.0,
                        checkZ
                );

                BlockState state = level().getBlockState(belowAhead);

                if (!state.blocksMotion()) {
                    getNavigation().stop();
                    return;
                }
            }
            LivingEntity target = getTarget();
            boolean closeToTarget =
                    target != null && distanceToSqr(target) <= 25.0D;

            // If we've reached melee range once, never jump again.
            if (getIsJumping()) {
                if (closeToTarget) {
                    setIsJumping(false);
                }
                Vec3 forward = getLookAngle().normalize().scale(0.5);

                AABB forwardBox = getBoundingBox().move(forward).move(0.0, 1.0, 0.0);

                //Your fated self stops jumping if it is going to collide, if this is removed
                //then it does a really funny tree leaf skidding thing
                if (!level().noCollision(this, forwardBox)) {
                    setIsJumping(false);
                }
            }

            // Resume sprinting automatically when no longer close.
            if (isSprinting && !isBackingUp && !closeToTarget) {
                setSprinting(true);
            } else {
                setSprinting(false);
            }
            setShiftKeyDown(isSneaking);
            if (isSneaking){
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
            if (getIsJumping() && onGround() && onGroundTime >= 2) {
                jumpFromGround();

                onGroundTime= 0;
                getNavigation().recomputePath();
            }
        }
    }
    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(JUMPING)) {
            super.defineSynchedData();
            this.entityData.define(JUMPING, false);
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
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.28).add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.ATTACK_DAMAGE, 1).
                add(Attributes.FOLLOW_RANGE, 48.0D);
    }
}
