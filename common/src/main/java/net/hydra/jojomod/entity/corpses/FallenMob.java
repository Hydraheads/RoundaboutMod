package net.hydra.jojomod.entity.corpses;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.access.IPermaCasting;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.goals.CorpseBuildBreakGoal;
import net.hydra.jojomod.entity.goals.CorpseFollowCommanderGoal;
import net.hydra.jojomod.entity.goals.CorpseStrollGoal;
import net.hydra.jojomod.entity.goals.CorpseTargetGoal;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.Tactics;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersJustice;
import net.hydra.jojomod.item.BodyBagItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.stream.Stream;

public class FallenMob extends PathfinderMob implements NeutralMob {
    public int ticksThroughPhases = 0;
    public int ticksThroughPlacer = 0;
    public Entity placer;
    public Entity controller;
    public boolean diesWhenUncontrolled;
    public LivingEntity corpseTarget;
    public LivingEntity manualTarget;
    public LivingEntity autoTarget;
    public LivingEntity autoTarget2;
    public int spinTicks = 0;
    private static final EntityDataAccessor<Boolean> TICKS_THROUGH_PLACER =
            SynchedEntityData.defineId(FallenMob.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> CONTROLLER =
            SynchedEntityData.defineId(FallenMob.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> FORCED_ROTATION =
            SynchedEntityData.defineId(FallenMob.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> PHASES_FULL =
            SynchedEntityData.defineId(FallenMob.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_ACTIVATED =
            SynchedEntityData.defineId(FallenMob.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_TURNED =
            SynchedEntityData.defineId(FallenMob.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SELECTED =
            SynchedEntityData.defineId(FallenMob.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> TARGET_TACTIC =
            SynchedEntityData.defineId(FallenMob.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> MOVEMENT_TACTIC =
            SynchedEntityData.defineId(FallenMob.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> TEAM_COLOR =
            SynchedEntityData.defineId(FallenMob.class, EntityDataSerializers.BYTE);
    public float getForcedRotation() {
        return this.getEntityData().get(FORCED_ROTATION);
    }
    public void setForcedRotation(float fr){
        this.entityData.set(FORCED_ROTATION, fr);
    }
    public byte getJusticeTeamColor() {
        return this.getEntityData().get(TEAM_COLOR);
    }
    public void setJusticeTeamColor(byte fr){
        this.entityData.set(TEAM_COLOR, fr);
    }

    /**Corpses will attempt to use their owner's scoreboard by default.
     *this can accomplish not killing your allies and their corpses*/
    @Override
    public Team getTeam() {
        if (ClientNetworking.getAppropriateConfig().justiceSettings.corpsesUseOwnerTeam) {
            if (getRealController() != null) {
                return this.level().getScoreboard().getPlayersTeam(getRealController().getScoreboardName());
            }
            return this.level().getScoreboard().getPlayersTeam(this.getScoreboardName());
        } else {
            return super.getTeam();
        }
    }

    public boolean getActivated() {
        return this.getEntityData().get(IS_ACTIVATED);
    }
    public void setActivated(boolean bool){
        this.entityData.set(IS_ACTIVATED, bool);
        if (!getTurned() && bool){
            this.setTurned(true);
            //insert code for cool particles and sfx
            if (!this.level().isClientSide()) {
                this.level().playSound(null, this.getX(), this.getY(),
                        this.getZ(), ModSounds.INHALE_EVENT, this.getSoundSource(), 1, (float) (0.7F + (Math.random() * 0.05)));
                ((ServerLevel) this.level()).sendParticles(ModParticles.FOG_CHAIN, this.getX(),
                        this.getY()+this.getEyeHeight(), this.getZ(),
                        11, 0.3, 0.3, 0.3, 0.3);
            }
        }
    }

    @Override
    public double getMyRidingOffset() {
        if (!getActivated()){
            return super.getMyRidingOffset()+0.2F;
        }
        return super.getMyRidingOffset();
    }
    protected float getEquipmentDropChance(EquipmentSlot $$0) {
        return 0;
    }
    public boolean getTurned() {
        return this.getEntityData().get(IS_TURNED);
    }
    public void setTurned(boolean bool){
        this.entityData.set(IS_TURNED, bool);
    }
    public byte getTargetTactic() {
        return this.getEntityData().get(TARGET_TACTIC);
    }
    public void setTargetTactic(byte byt){
        this.entityData.set(TARGET_TACTIC, byt);
    }
    public byte getMovementTactic() {
        return this.getEntityData().get(MOVEMENT_TACTIC);
    }
    public void setMovementTactic(byte byt){
        this.entityData.set(MOVEMENT_TACTIC, byt);
    }

    public boolean getSelected() {
        return this.getEntityData().get(SELECTED);
    }
    public void setSelected(boolean bool){
        this.entityData.set(SELECTED, bool);
    }
    public boolean getPhasesFull() {
        return this.getEntityData().get(PHASES_FULL);
    }

    private Goal thisBuildBreakGoal;
    public void addBuildBreakGoal(BlockPos $$0, BlockHitResult $$1){
        if(!(thisBuildBreakGoal == null)){
            this.goalSelector.removeGoal(thisBuildBreakGoal);
        }
        thisBuildBreakGoal = new CorpseBuildBreakGoal(this,1.0,$$0,$$1,false);
        this.goalSelector.addGoal(1, thisBuildBreakGoal);
    }
    public void removeBuildBreakGoal(){
        this.goalSelector.removeGoal(thisBuildBreakGoal);
        thisBuildBreakGoal = null;
    }

    public void setPhasesFull(boolean bool){
        ticksThroughPhases = 10;
        this.entityData.set(PHASES_FULL, bool);
    }
    public Entity getRealController() {
        if (this.controller != null){
            return controller;
        } else{
            int ct = this.getEntityData().get(CONTROLLER);
            if (ct > 0){
                return this.level().getEntity(ct);
            }
        }
        return null;
    }
    public int getController() {
        return this.getEntityData().get(CONTROLLER);
    }

    public void setController(int controller){
        this.entityData.set(CONTROLLER, controller);
    }
    public void setController(Entity controller){
        this.controller = controller;
        if (controller !=null){
            this.entityData.set(CONTROLLER, controller.getId());
        } else {
            this.entityData.set(CONTROLLER, 0);

        }
    }
    @Override
    public boolean canBreatheUnderwater(){
        return true;
    }
    public boolean getTicksThroughPlacer() {
        return this.getEntityData().get(TICKS_THROUGH_PLACER);
    }
    public int getPlacer() {
        return this.getEntityData().get(CONTROLLER);
    }
    public void setPlacer(int controller){
        this.entityData.set(CONTROLLER, controller);
    }
    public void setPlacer(Entity controller){
        this.placer = controller;
    }

    public float getAtkPower(Entity $$0){
        if (((StandUser)this).roundabout$getStandPowers().getReducedDamage($$0)){
            return getDamageMod((float) ((float) (this.getAttributeValue(Attributes.ATTACK_DAMAGE)/2)
                                * (ClientNetworking.getAppropriateConfig().justiceSettings.corpseDamageMultOnPlayers *0.01)));
        }
        return getDamageMod((float) ((float) this.getAttributeValue(Attributes.ATTACK_DAMAGE)
                        * (ClientNetworking.getAppropriateConfig().justiceSettings.corpseDamageMultOnMobs *0.01)) * modX());
    }

    public float modX(){
        return 1.5F;
    }

    /**If rpg leveling config is on*/
    public float getDamageMod(float pow){
        if (this.controller != null){
            return ((StandUser)this.controller).roundabout$getStandPowers().levelupDamageMod(pow);
        }
        return pow;
    }

    @Override
    public float getSpeed() {
        if (!this.getActivated()){
            return 0;
        } else {
            return super.getSpeed();
        }
    }
    public int hasPlaced = -1;
    @Override
    public boolean removeWhenFarAway(double $$0) {
        return false;
    }
    @Override
    public boolean doHurtTarget(Entity $$0) {

        if (((StandUser) this).roundabout$isDazed() ||
                (!((StandUser)this).roundabout$getStandDisc().isEmpty() &&
                        ((StandUser)this).roundabout$getStandPowers().disableMobAiAttack()) || ((StandUser) this).roundabout$isRestrained()) {
            return false;
        }



        float $$1 = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float $$2 = (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        if ($$0 instanceof LivingEntity) {
            $$2 += (float)EnchantmentHelper.getKnockbackBonus(this);
        }

        int $$3 = EnchantmentHelper.getFireAspect(this);
        if ($$3 > 0) {
            $$0.setSecondsOnFire($$3 * 4);
        }

        Entity ent2 = this;
        if (getController() > 0 && getController() != $$0.getId()){
            ent2 = controller;
        }


        if (!getMainHandItem().isEmpty()){
            /**If you give it an item, fights with it instead*/
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
                    ((IMob)this).roundabout$maybeDisableShield($$5, this.getMainHandItem(), $$5.isUsingItem() ? $$5.getUseItem() : ItemStack.EMPTY);
                }

                this.doEnchantDamageEffects(this, $$0);
                this.setLastHurtMob($$0);
            }

            getMainHandItem().hurtAndBreak(1, this, $$1x -> $$1x.broadcastBreakEvent(InteractionHand.MAIN_HAND));
            return $$4;
        } else {
            /**Otherwise it does stand damage of sorts*/
            boolean $$4 = DamageHandler.CorpseDamageEntity($$0, getAtkPower($$0),this);
            if (this instanceof FallenZombie){
                swing(InteractionHand.MAIN_HAND,true);
            }
            if ($$4) {
                if ($$2 > 0.0F && $$0 instanceof LivingEntity LE) {
                    LE
                            .knockback(
                                    (double)($$2 * 0.5F),
                                    (double) Mth.sin(this.getYRot() * (float) (Math.PI / 180.0)),
                                    (double)(-Mth.cos(this.getYRot() * (float) (Math.PI / 180.0)))
                            );
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));

                        LE.setLastHurtMob(this);
                        if (LE instanceof Mob mb){
                            mb.setLastHurtByMob(this);
                        }
                }

                this.doEnchantDamageEffects(this, $$0);
                this.setLastHurtMob($$0);
            }
            return $$4;
        }


    }


    @Override
    public void aiStep() {
        this.updateSwingTime();
        super.aiStep();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, true));
        this.addBehaviourGoals();
    }
    public void addBehaviourGoals() {
        this.goalSelector.addGoal(7, new CorpseStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new CorpseFollowCommanderGoal(this, 1.0, 10.0F, 1.5F, false));
        this.targetSelector.addGoal(1, new CorpseTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::canGetMadAt));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, 5, false, false, this::canGetMadAt));
    }

    public boolean canGetMadAt(LivingEntity $$0) {
        if (!this.canAttack($$0)) {
            return false;
        } else {
            return (
                    (this.getTargetTactic() == Tactics.HUNT_PLAYERS.id && $$0.getType() == EntityType.PLAYER && !(this.controller != null && $$0.is(this.controller))) ||
                            (this.getTargetTactic() == Tactics.HUNT_MONSTERS.id && $$0 instanceof Enemy && !($$0 instanceof Creeper) && !(this.controller != null && $$0.is(this.controller)))
            );
        }
    }

    @Override
    public boolean canAttack(LivingEntity $$0){
        if (this.getTargetTactic() == Tactics.PEACEFUL.id || !this.getActivated()){
            return false;
        }
        return super.canAttack($$0);
    }


    @Override
    public void addAdditionalSaveData(CompoundTag $$0){
        $$0.putBoolean("IsTurned",getTurned());
        $$0.putBoolean("IsActivated",getActivated());
        $$0.putBoolean("diesUnc",diesWhenUncontrolled);
        $$0.putByte("moveTactic",getMovementTactic());
        $$0.putByte("targetTactic",getTargetTactic());
        $$0.putInt("TicksThroughPhases",ticksThroughPhases);
        if (this.placer != null) {
            $$0.putUUID("Placer", this.placer.getUUID());
        }
        if (this.controller != null) {
            $$0.putUUID("Controller", this.controller.getUUID());
        }
        super.addAdditionalSaveData($$0);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0){
        this.setTurned($$0.getBoolean("IsTurned"));
        diesWhenUncontrolled = $$0.getBoolean("diesUnc");
        this.setActivated($$0.getBoolean("IsActivated"));
        this.ticksThroughPhases = $$0.getInt("TicksThroughPhases");
        this.setTargetTactic($$0.getByte("targetTactic"));
        this.setMovementTactic($$0.getByte("moveTactic"));

        if (ticksThroughPhases >= 10) {
            setPhasesFull(true);
        }
        UUID $$1;
        UUID $$2;
        if ($$0.hasUUID("Placer")) {
            $$1 = $$0.getUUID("Placer");
            if (this.level() instanceof ServerLevel SE){
                this.placer = SE.getEntity($$1);
            }
        }
        if ($$0.hasUUID("Controller")) {
            $$2 = $$0.getUUID("Controller");
            if (this.level() instanceof ServerLevel SE){
                this.setController(SE.getEntity($$2));
            }
        }
        super.readAdditionalSaveData($$0);
    }
    public void tickThroughPlacerStart(){
        this.entityData.set(TICKS_THROUGH_PLACER, true);
        this.ticksThroughPlacer = 50;
    }
    @Override
    public void tick(){
        if (!this.level().isClientSide()) {
            if (controller != null){
                controller = this.level().getEntity(getController());
            }

            if (this.getTarget() != null && !(this.getTarget().isAlive() || this.getTarget().isRemoved())){
                this.setTarget(null);
                this.setLastHurtByMob(null);
                this.setPersistentAngerTarget(null);
                this.setLastHurtByPlayer(null);
                this.setAggressive(false);
            }

            IPermaCasting icast = ((IPermaCasting) this.level());
            if (!getActivated()) {
                if (this.getSelected()){
                    this.setSelected(false);
                }
                if (ticksThroughPhases >= 10) {
                    LivingEntity pcaster = icast.roundabout$inPermaCastRangeEntityJustice(this, this.getOnPos());
                    if (pcaster != null && !pcaster.isRemoved() && pcaster.isAlive()) {
                        if (((StandUser) pcaster).roundabout$getStandPowers() instanceof PowersJustice PJ) {
                            setActivated(true);
                            this.setController(pcaster);
                            PJ.addJusticeEntities(this);
                        }
                    }
                }
                if (this.getTarget() != null){
                    setLastHurtByPlayer(null);
                    setLastHurtByMob(null);
                    setTarget(null);
                }

                this.moveControl.setWantedPosition(this.getX(),this.getY(),this.getZ(),0);
                if (this.getNavigation().getPath() != null){
                    this.getNavigation().stop();
                }
            } else {
                if (controller == null || controller.isRemoved() || !controller.isAlive() ||
                        !icast.roundabout$inPermaCastFogRange(this) ||  (controller instanceof LivingEntity LE &&
                        !icast.roundabout$isPermaCastingEntity(LE))){
                    setActivated(false);
                    this.setController(null);
                } else {
                    if (controller.getId() != this.getController()){
                        this.setController(controller.getId());
                    }
                    if (getTargetTactic() == Tactics.NONE.id || getTargetTactic() == Tactics.DEFEND.id) {
                        if (controller instanceof LivingEntity LE) {
                            autoTarget = LE.getLastHurtByMob();
                            autoTarget2 = LE.getLastHurtMob();
                            if (autoTarget instanceof FallenMob fm && fm.getController() == this.getController()){
                                autoTarget = null;
                            }
                            if (autoTarget2 instanceof FallenMob fm && fm.getController() == this.getController()){
                                autoTarget2 = null;
                            }
                            boolean check1 = (this.getTarget() != autoTarget) || autoTarget == null;
                            boolean check2 = (this.getTarget() != autoTarget2) || autoTarget2 == null;

                            if (check1 && check2) {
                                if (autoTarget2 != null && (LE.tickCount - LE.getLastHurtMobTimestamp()) < 200 &&
                                        !(autoTarget2 instanceof Player PE && PE.isCreative())) {
                                    if (autoTarget2 instanceof Player PL) {
                                        setLastHurtByPlayer(PL);
                                    }
                                    setLastHurtByMob(autoTarget2);
                                    setTarget(autoTarget2);
                                } else if (autoTarget != null && (LE.tickCount - LE.getLastHurtByMobTimestamp()) < 200 &&
                                        !(autoTarget instanceof Player PE && PE.isCreative())) {
                                    if (autoTarget instanceof Player PL) {
                                        setLastHurtByPlayer(PL);
                                    }
                                    setLastHurtByMob(autoTarget);
                                    setTarget(autoTarget);
                                }
                            }
                        }
                    }

                    if (this.getSelected() && !((StandUser) controller).roundabout$getStandPowers().isPiloting()){
                        this.setSelected(false);
                    }
                    if (((StandUser) controller).roundabout$getStandPowers() instanceof PowersJustice PJ) {
                        if (!PJ.isCastingFog()){
                            setActivated(false);
                            this.setController(null);
                        }
                    }
                }

                if (!this.getActivated()){
                    if (diesWhenUncontrolled){
                        ((ServerLevel) this.level()).sendParticles(ModParticles.FOG_CHAIN, this.getX(),
                                this.getY()+this.getEyeHeight(), this.getZ(),
                                20, 0.3, 0.3, 0.3, 0.3);
                        this.discard();
                    }
                }
            }
        }
        if (ticksThroughPlacer > 0){
            ticksThroughPlacer--;
            if (ticksThroughPlacer <= 0){
                this.entityData.set(TICKS_THROUGH_PLACER, false);
            }
        }



            if (!getActivated()){
                if (!getPhasesFull()) {
                    if (ticksThroughPhases < 10) {
                        ticksThroughPhases++;
                    } else {
                        if (!this.level().isClientSide()) {
                            setPhasesFull(true);
                        }
                    }
                } else {
                    if (!getTicksThroughPlacer()) {
                        if (this.level().isClientSide) {

                            float fr = this.getForcedRotation();
                            if (fr != 0 && spinTicks < 100) {
                                this.setYBodyRot(fr);
                                this.yBodyRotO = fr;
                                this.setYRot(fr);
                                this.yRotO = fr;
                                spinTicks++;
                            } else {
                                spinTicks=0;
                                this.setForcedRotation(0);
                            }

                            if ((!getTurned() || this.getHealth() >= this.getMaxHealth()) && ClientUtil.checkIfClientHoldingBag()) {
                                if (this.tickCount % 5 == 0) {
                                    for (int i = 0; i < ConfigManager.getClientConfig().particleSettings.bodyBagHoldingParticlesPerFiveTicks; i++) {
                                        this.level()
                                                .addParticle(
                                                        ParticleTypes.HAPPY_VILLAGER,
                                                        this.getRandomX(1.3),
                                                        this.getY() + this.getBbHeight() / 6,
                                                        this.getRandomZ(1.3),
                                                        0,
                                                        0.15,
                                                        0
                                                );
                                    }
                                }
                            }
                        } else {
                            float fr = this.getForcedRotation();
                            if (fr != 0 && spinTicks < 100) {
                                this.setYBodyRot(fr);
                                this.yBodyRotO = fr;
                                this.setYRot(fr);
                                this.yRotO = fr;
                                spinTicks++;
                            } else {
                                spinTicks=0;
                                this.setForcedRotation(0);
                            }
                        }
                    }
                }
            } else {
                if (ticksThroughPhases > 0){
                    if (ticksThroughPhases >= 10){
                        setPhasesFull(false);
                    }
                    ticksThroughPhases--;
                }
            }
        super.tick();
    }

    @Override
    public boolean killedEntity(ServerLevel $$0, LivingEntity $$1) {
        if (controller != null)
            return controller.killedEntity($$0,$$1);
        return true;
    }
    @Override
    protected Vec3 getLeashOffset() {
        if (!this.getActivated()){
            return new Vec3(0.0, 0, (double)(this.getBbWidth() * 0.4F));
        }
        return new Vec3(0.0, (double)this.getEyeHeight(), (double)(this.getBbWidth() * 0.4F));
    }

    public String getData(){
        return "zombie";
    }

    public void dropOnDeath(){
        if (this instanceof FallenZombie && !this.getMainHandItem().isEmpty()){
            double $$3 = this.getEyeY() - 0.3F;
            ItemEntity $$4 = new ItemEntity(this.level(), this.getX(), $$3, this.getZ(), this.getMainHandItem().copyAndClear());
            $$4.setPickUpDelay(40);
            $$4.setThrower(this.getUUID());
            this.level().addFreshEntity($$4);
            this.setItemInHand(InteractionHand.MAIN_HAND,ItemStack.EMPTY);
        }
    }

    @Override
    public void playerTouch(Player $$0) {
        if (!getActivated() && (!getTurned() || this.getHealth() >= this.getMaxHealth()) && this.isAlive() && !this.isRemoved() && !getTicksThroughPlacer()) {
            if (!this.level().isClientSide) {
                if ($$0.getMainHandItem().getItem() instanceof BodyBagItem BB){
                    if (BB.fillWithBody($$0.getMainHandItem(),this)){
                        this.level().playSound(null, this.blockPosition(), ModSounds.BODY_BAG_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.98 + (Math.random() * 0.04)));
                        dropOnDeath();
                        this.discard();
                    }
                } else if ($$0.getOffhandItem().getItem() instanceof BodyBagItem BB){
                    if (BB.fillWithBody($$0.getOffhandItem(),this)){
                        this.level().playSound(null, this.blockPosition(), ModSounds.BODY_BAG_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.98 + (Math.random() * 0.04)));
                        dropOnDeath();
                        this.discard();
                    }
                }
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(CONTROLLER)) {
            super.defineSynchedData();
            this.entityData.define(CONTROLLER, -1);
            this.entityData.define(TICKS_THROUGH_PLACER, false);
            this.entityData.define(PHASES_FULL, false);
            this.entityData.define(IS_ACTIVATED, false);
            this.entityData.define(IS_TURNED, false);
            this.entityData.define(FORCED_ROTATION, 0F);
            this.entityData.define(SELECTED, false);
            this.entityData.define(TARGET_TACTIC, (byte) 0);
            this.entityData.define(MOVEMENT_TACTIC, (byte) 0);
            this.entityData.define(TEAM_COLOR, (byte) 0);
        }
    }
    protected FallenMob(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
    }

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private int remainingPersistentAngerTime;
    @Nullable
    private UUID persistentAngerTarget;
    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public void setRemainingPersistentAngerTime(int $$0) {
        this.remainingPersistentAngerTime = $$0;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setPersistentAngerTarget(@javax.annotation.Nullable UUID $$0) {
        this.persistentAngerTarget = $$0;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }
    public int getUniqueFlapTickOffset() {
        return this.getId() * 3;

    }
}
