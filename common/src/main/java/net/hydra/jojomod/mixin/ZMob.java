package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModGamerules;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensing;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(Mob.class)
public abstract class ZMob extends LivingEntity implements IMob {
    protected ZMob(EntityType<? extends LivingEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Shadow public abstract boolean isAggressive();

    @Shadow @Final protected GoalSelector goalSelector;
    private static final EntityDataAccessor<Boolean> ROUNDABOUT$IS_WORTHY = SynchedEntityData.defineId(Mob.class, EntityDataSerializers.BOOLEAN);

    @Override
    @Unique
    public boolean roundabout$isWorthy() {
        return this.getEntityData().get(ROUNDABOUT$IS_WORTHY);
    }
    @Override
    @Unique
    public void roundabout$setWorthy(boolean $$0) {
        this.getEntityData().set(ROUNDABOUT$IS_WORTHY, $$0);
    }
    @Override
    @Unique
    public boolean roundabout$getIsNaturalStandUser() {
        return roundabout$isNaturalStandUser;
    }
    @Override
    @Unique
    public void roundabout$setIsNaturalStandUser(boolean set) {
        roundabout$isNaturalStandUser = set;
    }
    @Unique
    public boolean roundabout$isNaturalStandUser = false;


    @Inject(method = "dropCustomDeathLoot", at = @At(value = "HEAD"))
    private void roundabout$dropCustomLoot(DamageSource $$0, int $$1, boolean $$2, CallbackInfo ci) {
        if (roundabout$isNaturalStandUser){
            if ($$0.getEntity() != null) {
                if (((Mob)(Object)this) instanceof Enemy && !((StandUser)this).roundabout$getStandDisc().isEmpty()) {
                    if ($$0.getEntity() instanceof Player) {
                        if (!this.level().isClientSide()){
                            ExperienceOrb.award((ServerLevel) this.level(), this.position(), 160);
                        }
                    }

                    this.spawnAtLocation(ModItems.METEORITE.getDefaultInstance());
                    if (this.random.nextDouble() < 0.5) {
                        this.spawnAtLocation(ModItems.METEORITE.getDefaultInstance());
                    }
                    if ($$1 > 0) {
                        for (int i = 0; i < $$1; i++) {
                            if (this.random.nextDouble() < 0.5) {
                                this.spawnAtLocation(ModItems.METEORITE.getDefaultInstance());
                            }
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
    private void initDataTrackerRoundabout(CallbackInfo ci) {
        this.getEntityData().define(ROUNDABOUT$IS_WORTHY, false);
    }


    @ModifyVariable(method = "addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
    public CompoundTag roundabout$addAdditionalSaveData(CompoundTag $$0){
        $$0.putBoolean("roundabout.isWorthy", this.roundabout$isWorthy());
        $$0.putBoolean("roundabout.isNaturalStandUser", this.roundabout$isWorthy());
        return $$0;
    }

    @Inject(method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", at = @At(value = "HEAD"))
    public void roundabout$readAdditionalSaveData(CompoundTag $$0, CallbackInfo ci){
        this.roundabout$setWorthy($$0.getBoolean("roundabout.isWorthy"));
        this.roundabout$setIsNaturalStandUser($$0.getBoolean("roundabout.isNaturalStandUser"));
    }

    @Shadow
    private void maybeDisableShield(Player $$0, ItemStack $$1, ItemStack $$2) {

    }

    /**Minor code, mobs in a barrage should not be attacking*/
    @Inject(method = "doHurtTarget", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$TryAttack(Entity $$0, CallbackInfoReturnable<Boolean> ci) {
        if (((StandUser) this).roundabout$isDazed() ||
                (!((StandUser)this).roundabout$getStandDisc().isEmpty() &&
                        ((StandUser)this).roundabout$getStandPowers().disableMobAiAttack()) || ((StandUser) this).roundabout$isRestrained()) {
            ci.setReturnValue(false);
        } else {
            if (!((StandUser)this).roundabout$getStandDisc().isEmpty()){
                float $$1 = 1F;
                if (this.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE)){
                    $$1 = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
                }
                float $$2 = 0.1F;
                if (this.getAttributes().hasAttribute(Attributes.ATTACK_KNOCKBACK)){
                    $$2 = (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
                }
                if ($$0 instanceof LivingEntity) {
                    $$1 += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity)$$0).getMobType());
                    $$2 += (float)EnchantmentHelper.getKnockbackBonus(this);
                }

                int $$3 = EnchantmentHelper.getFireAspect(this);
                if ($$3 > 0) {
                    $$0.setSecondsOnFire($$3 * 4);
                }

                boolean $$4 = $$0.hurt(this.damageSources().mobAttack(this), $$1);
                if ($$4) {
                    if ($$2 > 0.0F && $$0 instanceof LivingEntity) {
                        ((LivingEntity)$$0)
                                .knockback(
                                        (double)($$2 * 0.5F),
                                        (double)Mth.sin(this.getYRot() * (float) (Math.PI / 180.0)),
                                        (double)(-Mth.cos(this.getYRot() * (float) (Math.PI / 180.0)))
                                );
                        this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));
                    }

                    if ($$0 instanceof Player $$5) {
                        this.maybeDisableShield($$5, this.getMainHandItem(), $$5.isUsingItem() ? $$5.getUseItem() : ItemStack.EMPTY);
                    }

                    this.doEnchantDamageEffects(this, $$0);
                    this.setLastHurtMob($$0);
                }

                ci.setReturnValue($$4);
            }
        }
    }

    @Inject(method = "finalizeSpawn", at = @At(value = "HEAD"))
    private void roundabout$finalizeSpawn(ServerLevelAccessor $$0, DifficultyInstance $$1, MobSpawnType $$2, SpawnGroupData $$3, CompoundTag $$4, CallbackInfoReturnable<SpawnGroupData> cir) {
        RandomSource $$5 = $$0.getRandom();
        if (this.level().getGameRules().getBoolean(ModGamerules.ROUNDABOUT_STAND_USER_MOB_SPAWNS) && $$5.nextFloat() < MainUtil.getStandUserOdds(((Mob)(Object)this))) {
            this.roundabout$setWorthy(true);
            this.roundabout$setIsNaturalStandUser(true);
            int index = (int) (Math.floor(Math.random()* ModItems.STAND_ARROW_POOL.size()));
            ItemStack stack = ModItems.STAND_ARROW_POOL.get(index).getDefaultInstance();
            if (!stack.isEmpty() && stack.getItem() instanceof StandDiscItem){
                ((StandUser)this).roundabout$setStandDisc(stack);
            }
        } else if (this.level().getGameRules().getBoolean(ModGamerules.ROUNDABOUT_WORTHY_MOB_SPAWNS) && $$5.nextFloat() < MainUtil.getWorthyOdds(((Mob)(Object)this))) {
            this.roundabout$setWorthy(true);
        }
    }

    @Nullable
    @Shadow
    public LivingEntity getTarget() {
        return null;
    }

    @Shadow
    public LookControl getLookControl() {
        return null;
    }

    @Shadow
    public double getPerceivedTargetDistanceSquareForMeleeAttack(LivingEntity $$0) {
        return 0;
    }
    @Shadow
    public Sensing getSensing() {
        return null;
    }
    @Shadow
    public PathNavigation getNavigation() {
        return null;
    }

    @Unique
    private double roundabout$speedModifier = 1F;
    @Unique
    private boolean roundabout$followingTargetEvenIfNotSeen;
    @Unique
    private Path roundabout$path;
    @Unique
    private double roundabout$pathedTargetX;
    @Unique
    private double roundabout$pathedTargetY;
    @Unique
    private double roundabout$pathedTargetZ;
    @Unique
    private int roundabout$ticksUntilNextPathRecalculation = 0;
    @Unique
    private int roundabout$ticksUntilNextAttack = 0;
    @Unique
    private final int roundabout$attackInterval = 20;
    @Unique
    private long roundabout$lastCanUseCheck;
    @Unique
    private static long roundabout$COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;

    @Unique
    public void roundabout$resetAtkCD(){
        this.roundabout$ticksUntilNextPathRecalculation = 0;
        this.roundabout$ticksUntilNextAttack = 0;
    }


    @Unique
    protected int roundabout$reducedTickDelay(int $$0) {
        return Mth.positiveCeilDiv($$0, 2);
    }


    @Unique
    protected int roundabout$adjustedTickDelay(int $$0) {
        return roundabout$reducedTickDelay($$0);
    }



    @Unique
    protected double roundabout$getAttackReachSqr(LivingEntity $$0) {
        return (double)(this.getBbWidth() * 2.0F * this.getBbWidth() * 2.0F + $$0.getBbWidth());
    }
    @Unique
    protected void roundabout$checkAndPerformAttack(LivingEntity $$0, double $$1) {
        double $$2 = this.roundabout$getAttackReachSqr($$0);
        if ($$1 <= $$2 && this.roundabout$ticksUntilNextAttack <= 0) {
            this.resetAttackCooldown();
            this.swing(InteractionHand.MAIN_HAND);
            this.doHurtTarget($$0);
        }
    }
    @Unique
    protected void resetAttackCooldown() {
        this.roundabout$ticksUntilNextAttack = 30;
    }

    @Unique
    protected boolean roundabout$mustSee = false;
    @Unique
    private int roundabout$unseenTicks;

    @Shadow
    public void setTarget(@Nullable LivingEntity $$0) {
    }

    @Unique
    protected int roundabout$unseenMemoryTicks = 300;

    @Unique
    protected double roundabout$getFollowDistance() {
        return this.getAttributeValue(Attributes.FOLLOW_RANGE);
    }

    @Unique
    protected boolean roundabout$nav = false;

    @Unique
    public boolean roundabout$canContinueToUse() {
            LivingEntity $$0 = this.getTarget();
        if ($$0 == null && ((this.tickCount - this.getLastHurtByMobTimestamp()) < 10)) {
            $$0 = this.getLastHurtByMob();
        }
            if ($$0 == null) {
                return false;
            } else if (!this.canAttack($$0)) {
                return false;
            } else {
                Team $$1 = this.getTeam();
                Team $$2 = $$0.getTeam();
                if ($$1 != null && $$2 == $$1) {
                    return false;
                } else {
                    double $$3 = this.roundabout$getFollowDistance();
                    if (this.distanceToSqr($$0) > $$3 * $$3) {
                        return false;
                    } else {
                        if (roundabout$mustSee) {
                            if (this.getSensing().hasLineOfSight($$0)) {
                                this.roundabout$unseenTicks = 0;
                            } else if (++this.roundabout$unseenTicks > roundabout$reducedTickDelay(this.roundabout$unseenMemoryTicks)) {
                                return false;
                            }
                        }

                        this.setTarget($$0);
                        return true;
                    }
                }
            }
    }

    @Unique
    public void roundabout$targetVillageEnemies(){
        LivingEntity potentialTarget = null;
        AABB $$0 = this.getBoundingBox().inflate(10.0, 8.0, 10.0);
        List<? extends LivingEntity> $$1 = this.level().getNearbyEntities(LivingEntity.class, MainUtil.attackTargeting, this, $$0);
        float mindist = -1;

        for (LivingEntity $$3 : $$1) {
            if (($$3 instanceof Mob Mb && Mb.getTarget() != null && Mb.getTarget() instanceof AbstractVillager)){
                if (mindist == -1 || this.distanceToSqr($$3) < mindist){
                    mindist = (float) this.distanceToSqr($$3);
                    potentialTarget = $$3;
                }
            }
        }

        if (potentialTarget != null && (!(potentialTarget instanceof Player) || !potentialTarget.isSpectator() && !((Player) potentialTarget).isCreative())){
            this.setTarget(potentialTarget);
        }
    }

    @Unique
    public boolean roundabout$fightOrFlight = false;
    @Unique
    public void roundabout$toggleFightOrFlight(boolean flight){
        roundabout$fightOrFlight = flight;
    }
    @Unique
    public boolean roundabout$getFightOrFlight(){
        return roundabout$fightOrFlight;
    }
    @Unique
    public int roundabout$retractTicks = 140;
    @Unique
    @Override
    public void roundabout$setRetractTicks(int ticks){
        roundabout$retractTicks = ticks;
    }

    @Unique
    public void roundabout$standUserAttraction(){
        if (this.tickCount % 3600 == 0 && Math.random() < 0.1) {
            LivingEntity potentialTarget = null;
            AABB $$0 = this.getBoundingBox().inflate(35.0, 13.0, 35.0);

            List<? extends LivingEntity> $$1 = this.level().getNearbyEntities(LivingEntity.class, MainUtil.followTargetting, this, $$0);
            float mindist = -1;

            for (LivingEntity $$3 : $$1) {
                if (!((StandUser)$$3).roundabout$getStandDisc().isEmpty()){
                    if (mindist == -1 || this.distanceToSqr($$3) < mindist){
                        mindist = (float) this.distanceToSqr($$3);
                        potentialTarget = $$3;
                    }
                }
            }


            if (potentialTarget != null && (!(potentialTarget instanceof Player) || !potentialTarget.isSpectator() && !((Player) potentialTarget).isCreative())) {
                roundabout$standPath = this.getNavigation().createPath(potentialTarget, 2);
                this.getNavigation().moveTo(roundabout$standPath, 1.1);
                roundabout$standAttractionTicks = 100;
            }
        }
    }

    Path roundabout$standPath;
    int roundabout$standAttractionTicks = -1;

    @SuppressWarnings("deprecation")
    @Inject(method = "serverAiStep", at = @At(value = "INVOKE",target="Lnet/minecraft/world/entity/ai/navigation/PathNavigation;tick()V",
    shift= At.Shift.BEFORE))
    private void roundabout$serverAiStep(CallbackInfo ci) {

        /**Passive to neutral stuff for stand users*/
        if (this.isAlive()) {
            if (!((StandUser) this).roundabout$getStandDisc().isEmpty()) {

                Mob mb = ((Mob) (Object) this);
                if (this.getTarget() != null && !this.roundabout$getFightOrFlight()){
                    if (!((StandUser) this).roundabout$getActive()){
                        ((StandUser)this).roundabout$summonStand(this.level(),true,true);
                    }
                    if (roundabout$retractTicks != 100) {
                        roundabout$retractTicks = 100;
                    }
                } else {
                    roundabout$retractTicks = Math.max(roundabout$retractTicks-1,-1);
                    if (roundabout$retractTicks == -1 || this.roundabout$getFightOrFlight()){
                        if (((StandUser) this).roundabout$getActive()){
                            ((StandUser)this).roundabout$summonStand(this.level(),false,false);
                        }
                    }
                }

                if (!(((Mob) (Object) this) instanceof Enemy)
                        && !(((Mob) (Object) this) instanceof NeutralMob) && !this.roundabout$getFightOrFlight()) {
                    if (this.getTarget() != null && this.getTarget() instanceof Player PE && PE.isCreative()){
                        this.setTarget(null);
                    }


                    if (mb instanceof AbstractVillager){
                        if (this.getTarget() == null){
                            if (this.tickCount % 4 == 0){
                                roundabout$targetVillageEnemies();
                            }
                            if (this.getTarget() instanceof AbstractVillager || this.getLastHurtByMob()
                                    instanceof AbstractVillager){
                                this.setTarget(null);
                                this.setLastHurtByMob(null);
                            }
                        }
                    }

                    if (roundabout$canContinueToUse() && this.getTarget() != null) {
                        if (!roundabout$nav) {
                            this.getNavigation().createPath(this.getTarget(), 0);
                            roundabout$nav = true;
                        }

                        LivingEntity $$0 = this.getTarget();
                        if ($$0 != null) {
                            this.getLookControl().setLookAt($$0, 30.0F, 30.0F);
                            double $$1 = this.getPerceivedTargetDistanceSquareForMeleeAttack($$0);
                            this.roundabout$ticksUntilNextPathRecalculation = Math.max(this.roundabout$ticksUntilNextPathRecalculation - 1, 0);
                            if ((this.roundabout$followingTargetEvenIfNotSeen || this.getSensing().hasLineOfSight($$0))
                                    && this.roundabout$ticksUntilNextPathRecalculation <= 0
                                    && (
                                    this.roundabout$pathedTargetX == 0.0 && this.roundabout$pathedTargetY == 0.0 && this.roundabout$pathedTargetZ == 0.0
                                            || $$0.distanceToSqr(this.roundabout$pathedTargetX, this.roundabout$pathedTargetY, this.roundabout$pathedTargetZ) >= 1.0
                                            || this.getRandom().nextFloat() < 0.05F
                            )) {
                                this.roundabout$pathedTargetX = $$0.getX();
                                this.roundabout$pathedTargetY = $$0.getY();
                                this.roundabout$pathedTargetZ = $$0.getZ();
                                this.roundabout$ticksUntilNextPathRecalculation = 4 + this.getRandom().nextInt(7);
                                if ($$1 > 1024.0) {
                                    this.roundabout$ticksUntilNextPathRecalculation += 10;
                                } else if ($$1 > 256.0) {
                                    this.roundabout$ticksUntilNextPathRecalculation += 5;
                                }

                                if (!this.getNavigation().moveTo($$0, this.roundabout$speedModifier)) {
                                    this.roundabout$ticksUntilNextPathRecalculation += 15;
                                }

                                this.roundabout$ticksUntilNextPathRecalculation = this.roundabout$adjustedTickDelay(this.roundabout$ticksUntilNextPathRecalculation);
                            }

                            this.roundabout$ticksUntilNextAttack = Math.max(this.roundabout$ticksUntilNextAttack - 1, 0);
                            this.roundabout$checkAndPerformAttack($$0, $$1);
                        }
                    } else {
                        if (roundabout$nav) {
                            roundabout$nav = false;
                            this.setTarget(null);
                            roundabout$resetAtkCD();
                        }
                    }
                }

                if (this.getTarget() == null){
                    if (roundabout$standAttractionTicks > -1){
                        roundabout$standAttractionTicks = Math.max(roundabout$standAttractionTicks-1,-1);
                        if (roundabout$standPath != null){
                            this.getNavigation().moveTo(roundabout$standPath, 1.1);
                        }
                    }
                    roundabout$standUserAttraction();
                }

                if (mb instanceof TamableAnimal TA){
                    if (this.getTarget() instanceof TamableAnimal TT && TT.getOwner() != null
                            && TA.getOwner() != null && TT.getOwner().is(TA.getOwner())){
                        this.setTarget(null);
                        this.setLastHurtByMob(null);
                    }
                }
            }
        }
    }

    @Unique
    Mob roundabout$Mob;

    @Inject(method = "convertTo(Lnet/minecraft/world/entity/EntityType;Z)Lnet/minecraft/world/entity/Mob;",
            at = @At(value = "HEAD"))
    private <T extends Mob> void roundabout$convertTo1(EntityType<T> $$0, boolean $$1, CallbackInfoReturnable<T> cir) {
        if ($$0 != null) {
            roundabout$Mob = ((Mob)(Object)this);
        }
    }
    @ModifyVariable(method = "convertTo(Lnet/minecraft/world/entity/EntityType;Z)Lnet/minecraft/world/entity/Mob;",
            at = @At(value = "STORE"),ordinal = 1)
    private <T extends Mob> T roundabout$convertTo(Mob value) {
        if (value != null) {
            if (!((StandUser)roundabout$Mob).roundabout$getStandDisc().isEmpty()){
                ((StandUser)value).roundabout$setStandDisc(((StandUser)roundabout$Mob).roundabout$getStandDisc());
            } if (!((IMob)roundabout$Mob).roundabout$isWorthy()){
                ((IMob)value).roundabout$setWorthy(true);
            }
        }
        return (T) value;
    }

    @SuppressWarnings("deprecation")
    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void roundabout$Tick(CallbackInfo ci) {
        if (this.isAlive() && !this.level().isClientSide()) {
            if (!((StandUser) this).roundabout$getStandDisc().isEmpty()) {
                if (!this.roundabout$getFightOrFlight()) {
                    if (this.getTarget() != null){
                        if (this.getVehicle() != null && (this.getVehicle() instanceof Boat ||
                                this.getVehicle() instanceof Minecart)){
                            Vec3 vec = this.getVehicle().getDismountLocationForPassenger(this);
                            this.stopRiding();
                            this.dismountTo(vec.x,vec.y,vec.z);
                        }
                    }
                    ((StandUser) this).roundabout$getStandPowers().tickMobAI(this.getTarget());
                }
            }
        }

        if (this instanceof Enemy || this.getTarget() != null) {
            if (((StandUser) this).roundabout$isRestrained()) {
                int ticks = ((StandUser) this).roundabout$getRestrainedTicks();
                if (ticks < 50) {
                    ticks++;
                    ((StandUser) this).roundabout$setRestrainedTicks(ticks);
                }
                if (ticks >= 50) {
                    if (this.getVehicle() instanceof StandEntity SE && SE.canRestrainWhileMounted()) {
                        if (!SE.getPassengers().isEmpty()) {
                            SE.ejectPassengers();
                        }
                        if (SE.getUser() != null) {
                            //((StandUser)SE.getUser())
                            boolean candoit = true;
                            Vec3 vec3d3 = SE.getUser().getForward();
                            for (var i = 0; i < this.getBbHeight(); i++) {
                                if (this.level().getBlockState(new BlockPos(
                                        (int) vec3d3.x(), (int) (vec3d3.y + i),
                                        (int) vec3d3.z)).isSolid()) {
                                    candoit = false;
                                    break;
                                }
                            }
                            if (candoit) {
                                this.dismountTo(vec3d3.x, vec3d3.y, vec3d3.z);
                            } else {
                                this.dismountTo(SE.getUser().getX(), SE.getUser().getY(), SE.getUser().getZ());
                            }


                        }
                        ((StandUser) this).roundabout$setRestrainedTicks(-1);
                    }
                }
            } else {
                int ticks = ((StandUser) this).roundabout$getRestrainedTicks();
                if (ticks != -1) {
                    ticks = -1;
                    ((StandUser) this).roundabout$setRestrainedTicks(ticks);
                }
            }
        }
    }

}
