package net.hydra.jojomod.entity;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IItemEntityAccess;
import net.hydra.jojomod.access.IPlayerEntityServer;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.goals.AvoidLearnedAnnihilatorGoal;
import net.hydra.jojomod.entity.goals.D4CCloneLookAtMaster;
import net.hydra.jojomod.entity.goals.D4CCloneObeyMasterGoal;
import net.hydra.jojomod.entity.goals.D4CMeleeAttackGoal;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersD4C;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.Util;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class D4CCloneEntity extends CloneEntity implements ContainerListener,HasCustomInventoryScreen {
    @Unique
    private static final EntityDataAccessor<Boolean> SAFE_COPY = SynchedEntityData.defineId(D4CCloneEntity.class,
            EntityDataSerializers.BOOLEAN);
    public int timer = 0;
    public D4CCloneEntity(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
        this.goalSelector.addGoal(1, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(3, new FloatGoal(this));
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        ((GroundPathNavigation)this.getNavigation()).setCanPassDoors(true);
        this.createInventory();
    }

    public int getInventorySize() {
        return 15;
    }
    protected void createInventory() {
        SimpleContainer simplecontainer = this.inventory;
        this.inventory = new SimpleContainer(this.getInventorySize());
        if (simplecontainer != null) {
            simplecontainer.removeListener(this);
            int i = Math.min(simplecontainer.getContainerSize(), this.inventory.getContainerSize());

            for(int j = 0; j < i; ++j) {
                ItemStack itemstack = simplecontainer.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.inventory.setItem(j, itemstack.copy());
                }
            }
        }

        this.inventory.addListener(this);
        this.updateContainerEquipment();
    }

    public boolean letsNotAttackOurMasterOrPeers(Entity isMaster){
        if (getSafeCopy() && PowerTypes.isNativeToOurWorld(this)){
            if (isMaster != null && getPlayerUUID().isPresent()){
                if (isMaster.getUUID().equals(getPlayerUUID().get())) {
                    return true;
                }
                if (isMaster instanceof CloneEntity CE && CE.getPlayerUUID().isPresent()){
                    if (CE.getPlayerUUID().get().equals(getPlayerUUID().get())) {
                        return true;
                    }
                }
            }

        }
        return false;
    }
    protected static final EntityDataAccessor<Integer> STRATEGY = SynchedEntityData.defineId(D4CCloneEntity.class,
            EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> SPAWNED = SynchedEntityData.defineId(D4CCloneEntity.class,
            EntityDataSerializers.BOOLEAN);
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(
                1,
                new AvoidLearnedAnnihilatorGoal(this)
        );
        this.goalSelector.addGoal(1, new D4CCloneObeyMasterGoal(this));
        this.goalSelector.addGoal(1, new D4CCloneLookAtMaster(this));

        this.goalSelector.addGoal(2, new D4CMeleeAttackGoal(this, 1.0D, false));

        this.goalSelector.addGoal(3, new FloatGoal(this));

        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
        addBehaviourGoals();
    }

    public void addBehaviourGoals() {
        this.targetSelector.addGoal(4, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(
                this,
                LivingEntity.class,
                5,
                false,
                false,
                entity -> (
                        isValidTarget(entity)
                        && canFocusOnFighting(entity)
                )
        ));
    }

    public void peaceOut(){
        MainUtil.playSoundIfPossible(this,this.level(),null, this.blockPosition(), ModSounds.D4C_CLONE_EVENT,
                SoundSource.PLAYERS, 0.95F, 1F);
        Vector3f color = new Vector3f(0.97F, 1F, 0.3F);
        MainUtil.sendParticlesIfPossible(this,this.level(),
                new DustParticleOptions(
                        color,
                        1.0F
                ), this.getEyePosition().x,
                this.getEyePosition().y, this.getEyePosition().z,
                20, 0.3, 0.3, 0.3, 0.3);
        dropAll();
        this.discard();
    }

    @Override
    public void setTarget(@Nullable LivingEntity $$0) {
        if (letsNotAttackOurMasterOrPeers($$0)){
            return;
        } else {
            super.setTarget($$0);
        }
    }
    public void setLastHurtByPlayer(@Nullable Player $$0) {
        if (letsNotAttackOurMasterOrPeers($$0)){
            return;
        } else {
            super.setLastHurtByPlayer($$0);
        }
    }

    public void setLastHurtByMob(@Nullable LivingEntity $$0) {
        if (letsNotAttackOurMasterOrPeers($$0)){
            return;
        } else {
            super.setLastHurtMob($$0);
        }
    }

    public void setLastHurtMob(Entity $$0) {
        if (letsNotAttackOurMasterOrPeers($$0)){
            return;
        } else {
            super.setLastHurtMob($$0);
        }
    }
    public SimpleContainer inventory;
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("safeCopy",getSafeCopy());

        ListTag listTag = new ListTag();

        for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemStack = this.inventory.getItem(i);
            if (!itemStack.isEmpty()) {
                CompoundTag compoundTag2 = new CompoundTag();
                compoundTag2.putByte("Slot", (byte)i);
                itemStack.save(compoundTag2);
                listTag.add(compoundTag2);
            }
        }

        tag.put("Items", listTag);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("safeCopy", 10)) {
            setSafeCopy(tag.getBoolean("safeCopy"));
        }
        ListTag listTag = tag.getList("Items", 10);

        for(int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundTag2 = listTag.getCompound(i);
            int j = compoundTag2.getByte("Slot") & 255;
            if (j < this.inventory.getContainerSize()) {
                this.inventory.setItem(j, ItemStack.of(compoundTag2));
            }
        }
    }
    public SlotAccess getSlot(int i) {
        int k = i - 500;
        return k < this.inventory.getContainerSize() ? SlotAccess.forContainer(this.inventory, k) : super.getSlot(i);
    }

    public boolean hasInventoryChanged(Container p_149512_) {
        return this.inventory != p_149512_;
    }

    public int getInventoryColumns() {
        return 5;
    }
    private boolean isValidTarget(LivingEntity entity) {
        if (entity == null || entity == this) {
            return false;
        }
        if (letsNotAttackOurMasterOrPeers(entity)){
            return false;
        }

        if (!canFocusOnFighting(entity)) {
            return false;
        }

        // Normal enemies
        if (entity instanceof Enemy && !(entity instanceof Creeper)
                && !(entity instanceof EnderMan)) {
            return true;
        }

        if (learnedAnnihilator != null) {

            // Something that is attacking the learned player.
            if (entity.getLastHurtByMob() != null &&
                    entity.getLastHurtByMob().getUUID().equals(learnedAnnihilator)) {
                return true;
            }

            // A clone whose current target is the learned player.
            if (entity instanceof CloneEntity clone &&
                    clone.getTarget() != null &&
                    clone.getTarget().getUUID().equals(learnedAnnihilator)) {
                return true;
            }
        }

        return false;
    }


    public boolean canFocusOnFighting(@Nullable Entity entity) {

        if (entity == null) {
            return false;
        }

        if (isBeingAnnihilated()) {
            return false;
        }

        return true;
    }

    public static final int STRATEGY_NORMAL = 0;
    public static final int STRATEGY_BACKING_AWAY = 1;
    public static final int STRATEGY_FLEEING = 2;
    public static final int STRATEGY_ACKNOWLEDGING = 3;
    public static final int STRATEGY_AVOIDING = 4;

    public int acknowledgeTicks = 0;
    public int sneakCooldown = 0;
    public int sneakTicks = 0;
    public int annihilationTicks = 0;
    public int jumpCooldown = 20;

    @Nullable
    private UUID learnedAnnihilator;

    public int ticksSinceSwitch = 0;

    public static int getCloneLimit(){
        return ClientNetworking.getAppropriateConfig().d4cSettings.cloneLimit;
    }
    @Override
    public void tick() {
        if (this.level().isClientSide) {
            if (getSpawned()){
                ticksSinceSwitch++;
            }
        } else {
            if (tickCount % 15 == 0){
                AABB $$0 = this.getBoundingBox().inflate(40, 40, 40);
                List<? extends D4CCloneEntity> $$1 = this.level().getNearbyEntities(D4CCloneEntity.class, MainUtil.plsWorkTargetting2, this, $$0);
                int iterator = 0;
                for (D4CCloneEntity $$3 : $$1) {
                    if (getPlayerUUID().isPresent() && $$3.getPlayerUUID().isPresent()) {
                        if (getPlayerUUID().equals($$3.getPlayerUUID())){
                            iterator++;
                        }
                    }
                }
                if (iterator > getCloneLimit() && tickCount > 100){
                    peaceOut();
                }
            }

            if (getSpawned()){
                ticksSinceSwitch++;
            }
            Player pl = getPlayer();
            if (pl != null){
                if (letsNotAttackOurMasterOrPeers(pl)){
                    if (pl.distanceTo(this) < 30){
                        LivingEntity hurt = pl.getLastHurtMob();
                        LivingEntity hurtBy = pl.getLastHurtByMob();
                        if (hurt != null && hurt.isAlive()){
                            setTarget(hurt);
                        } else if (hurtBy != null && hurtBy.isAlive()){
                            setTarget(hurtBy);
                        }
                    }
                }
            }
        }

        super.tick();

        if (this.level().isClientSide) {
            return;
        }

        Entity annihilator = getAnnihilating();

        // =========================================================
        // CURRENTLY BEING ANNIHILATED
        // =========================================================

        if (annihilator != null && annihilator.isAlive()) {

            // Remember who did this to us.
            learnedAnnihilator = annihilator.getUUID();

            annihilationTicks++;


            if (annihilationTicks < 35) {
                setStrategy(STRATEGY_NORMAL);
                return;
            }

            if (annihilationTicks < 75) {
                setStrategy(STRATEGY_BACKING_AWAY);
                backAwayFrom(annihilator);
                return;
            }

            setStrategy(STRATEGY_FLEEING);
            fleeFrom(annihilator);

            return;
        }


        if (getStrategy() == STRATEGY_FLEEING && learnedAnnihilator != null) {
            startAcknowledging();
            return;
        }


        if (getStrategy() == STRATEGY_ACKNOWLEDGING) {

            Entity learned = getLearnedAnnihilator();

            if (learned != null && learned.isAlive()) {
                acknowledgeAnnihilator(learned);
            } else {
                setStrategy(STRATEGY_NORMAL);
            }

            return;
        }


        if (learnedAnnihilator != null) {

            Entity learned = getLearnedAnnihilator();

            if (learned != null && learned.isAlive()) {
                setStrategy(STRATEGY_AVOIDING);
                avoidAnnihilator(learned);
                return;
            }
        }


        annihilationTicks = 0;
        setStrategy(STRATEGY_NORMAL);
    }

    public boolean figuredItOut(){
        return annihilationTicks >= 75;
    }
    private void startAcknowledging() {
        setStrategy(STRATEGY_ACKNOWLEDGING);

        acknowledgeTicks = 40 + this.getRandom().nextInt(41);

        sneaksRemaining = 0;
        float sneakChance = this.getRandom().nextFloat();

        if (sneakChance < 0.2F) {
            sneaksRemaining = 0; // no greeting
        } else if (sneakChance < 0.4F) {
            sneaksRemaining = 1;
        } else {
            sneaksRemaining = 2;
        }

        sneakCooldown = 20 + this.getRandom().nextInt(10);
        sneakTicks = 0;

        this.getNavigation().stop();

        this.setDeltaMovement(
                0,
                this.getDeltaMovement().y,
                0
        );

        stopSneaking();
    }
    private void stopSneaking() {
        this.setShiftKeyDown(false);

        if (this.getPose() == Pose.CROUCHING) {
            this.setPose(Pose.STANDING);
        }

        sneakTicks = 0;
    }
    private int sneaksRemaining = 0;
    @Nullable
    public Entity getLearnedAnnihilator() {
        if (learnedAnnihilator == null) {
            return null;
        }

        return ((ServerLevel) this.level()).getEntity(learnedAnnihilator);
    }

    private void faceEntity(Entity entity) {
        Vec3 look = entity.getEyePosition().subtract(this.getEyePosition());

        if (look.lengthSqr() < 0.001D) {
            return;
        }

        float yaw = (float)(Mth.atan2(
                look.z,
                look.x
        ) * (180F / Math.PI)) - 90F;

        float pitch = (float)-(Mth.atan2(
                look.y,
                Math.sqrt(look.x * look.x + look.z * look.z)
        ) * (180F / Math.PI));

        this.setYRot(yaw);
        this.setYHeadRot(yaw);
        this.setYBodyRot(yaw);

        this.yRotO = yaw;

        this.setXRot(pitch);
    }

    @Nullable
    private Player interactedPlayer;

    public void setInteractedPlayer(@Nullable Player $$0) {
        boolean $$1 = this.getInteractedPlayer() != null && $$0 == null;
        this.interactedPlayer = $$0;
        if ($$1) {
            this.stopInteraction();
        }

    }
    @Nullable
    public Player getInteractedPlayer() {
        return this.interactedPlayer;
    }
    protected void stopInteraction() {
        this.setInteractedPlayer((Player)null);
    }

    private void acknowledgeAnnihilator(Entity entity) {

        if (entity == null || !entity.isAlive()) {
            stopSneaking();
            setStrategy(STRATEGY_NORMAL);
            return;
        }

        this.getNavigation().stop();

        this.setDeltaMovement(
                0,
                this.getDeltaMovement().y-0.1,
                0
        );

        faceEntity(entity);

        acknowledgeTicks--;

        if (sneakTicks > 0) {

            sneakTicks--;

            this.setShiftKeyDown(true);
            this.setPose(Pose.CROUCHING);

            return;
        }

        this.setShiftKeyDown(false);

        if (this.getPose() == Pose.CROUCHING) {
            this.setPose(Pose.STANDING);
        }

        if (sneaksRemaining > 0) {

            sneakCooldown--;

            if (sneakCooldown <= 0) {

                sneakTicks = 2 + this.getRandom().nextInt(3);

                sneaksRemaining--;

                sneakCooldown = 8 + this.getRandom().nextInt(14);
            }
        }

        if (acknowledgeTicks <= 0) {

            stopSneaking();

            setStrategy(STRATEGY_AVOIDING);
        }
    }

    private Vec3 fleeDirection = Vec3.ZERO;
    private int fleeDirectionCooldown = 0;
    private void backAwayFrom(Entity entity) {
        Vec3 away = this.position().subtract(entity.position());

        if (away.lengthSqr() < 0.0001D) {
            return;
        }

        away = away.normalize();

        // Don't go absolutely full speed yet.
        double speed = 0.08D;

        Vec3 movement = new Vec3(
                away.x * speed,
                this.getDeltaMovement().y-0.1,
                away.z * speed
        );

        this.setDeltaMovement(movement);
    }

    private void fleeFrom(Entity entity) {

        if (fleeDirectionCooldown <= 0 || fleeDirection.lengthSqr() < 0.001D) {

            Vec3 away = this.position().subtract(entity.position());

            if (away.lengthSqr() < 0.0001D) {
                return;
            }

            away = away.normalize();

            double side = (this.getRandom().nextDouble() - 0.5D) * 1.2D;

            fleeDirection = new Vec3(
                    away.x - away.z * side,
                    -0.1,
                    away.z + away.x * side
            ).normalize();

            fleeDirectionCooldown = 10 + this.getRandom().nextInt(21);

            // Only set a new navigation target when we
            // actually choose a new escape direction.
            double distance = 12.0D + this.getRandom().nextDouble() * 8.0D;

            this.getNavigation().moveTo(
                    this.getX() + fleeDirection.x * distance,
                    this.getY(),
                    this.getZ() + fleeDirection.z * distance,
                    1.5D
            );
        } else {
            fleeDirectionCooldown--;
        }

        if (jumpCooldown > 0) {
            jumpCooldown--;
        }

        if (this.onGround() && jumpCooldown <= 0) {
            this.jumpFromGround();

            jumpCooldown = 10 + this.getRandom().nextInt(200);
        }
    }


    public void aiStep() {
        this.updateSwingTime();
        if (this.getHealth() > 0.0F && !this.isSpectator()) {
            AABB aabb;
            if (this.isPassenger() && !this.getVehicle().isRemoved()) {
                aabb = this.getBoundingBox().minmax(this.getVehicle().getBoundingBox()).inflate(1.0D, 0.0D, 1.0D);
            } else {
                aabb = this.getBoundingBox().inflate(1.0D, 0.5D, 1.0D);
            }

            List<Entity> list = this.level().getEntities(this, aabb);
            List<Entity> list1 = Lists.newArrayList();

            for(int i = 0; i < list.size(); ++i) {
                Entity entity = list.get(i);
                if (entity.getType() == EntityType.EXPERIENCE_ORB) {
                    list1.add(entity);
                } else if (!entity.isRemoved()) {
                    this.touch(entity);
                }
            }

            if (!list1.isEmpty()) {
                this.touch(Util.getRandom(list1, this.random));
            }
        }
        super.aiStep();
    }
    public void die(DamageSource sauce) {
        if (!this.isRemoved() && !this.dead) {
            dropAll();
        }
        super.die(sauce);
    }
    public void dropAll() {
        for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemStack = this.inventory.getItem(i);
            if (!itemStack.isEmpty()) {
                double d = this.getEyeY() - (double)0.3F;
                ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), d, this.getZ(), itemStack);
                itemEntity.setPickUpDelay(40);
                float f = this.random.nextFloat() * 0.5F;
                float g = this.random.nextFloat() * ((float)Math.PI * 2F);
                itemEntity.setDeltaMovement((double)(-Mth.sin(g) * f), (double)0.2F, (double)(Mth.cos(g) * f));
                level().addFreshEntity(itemEntity);
            }
        }
        this.inventory.clearContent();
    }

    private void touch(Entity entity) {
        if (entity instanceof ItemEntity item){
            if (!this.level().isClientSide) {
                ItemStack $$1 = item.getItem();
                Item $$2 = $$1.getItem();
                int $$3 = $$1.getCount();
                if (((IItemEntityAccess)item).roundabout$getPickupDelay() == 0 &&
                        (((IItemEntityAccess)item).roundabout$getTarget() == null ||
                                ((IItemEntityAccess)item).roundabout$getTarget().equals(this.getUUID()))) {
                    if (inventory.canAddItem($$1)) {
                        inventory.addItem($$1);
                        this.take(item, $$3);
                        item.discard();

                        this.onItemPickup(item);
                    }
                }

            }
        }
    }
    private void avoidAnnihilator(Entity entity) {
        double distance = this.distanceTo(entity);

        // We're comfortably outside the danger zone.
        // Do nothing.
        if (distance > 8.0D) {
            return;
        }

        Vec3 away = this.position().subtract(entity.position());

        if (away.lengthSqr() < 0.001D) {
            return;
        }

        away = away.normalize();

        // Only move far enough to get back outside the boundary.
        double distanceToMove = Math.max(1.5D, 8.0D - distance);

        double targetX = this.getX() + away.x * distanceToMove;
        double targetZ = this.getZ() + away.z * distanceToMove;

        this.getNavigation().moveTo(
                targetX,
                this.getY(),
                targetZ,
                1.0D
        );
    }


    public boolean isBeingAnnihilated(){
        return ((IEntityAndData)this).rdbt$getNearAlt()  != null;
    }
    public Entity getAnnihilating(){
        return ((IEntityAndData)this).rdbt$getNearAlt();
    }
    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(STRATEGY)) {
            super.defineSynchedData();
            this.entityData.define(STRATEGY, 0);
            this.entityData.define(DATA_ID_FLAGS, (byte)0);
            this.entityData.define(SAFE_COPY, false);
            this.entityData.define(SPAWNED, false);
        }
    }
    public boolean getSafeCopy(){
        return entityData.get(SAFE_COPY);
    }
    public void setSafeCopy(boolean spawned){
        entityData.set(SAFE_COPY,spawned);
    }
    public boolean getSpawned(){
        return entityData.get(SPAWNED);
    }
    public void setSpawned(boolean spawned){
        entityData.set(SPAWNED,spawned);
    }
    public int getStrategy(){
        return entityData.get(STRATEGY);
    }
    public void setStrategy(int strategy){
        if (strategy != STRATEGY_ACKNOWLEDGING){
            stopSneaking();
        }
        entityData.set(STRATEGY,strategy);
    }



    @Override
    public void remove(RemovalReason $$0) {
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

    @Override
    public void containerChanged(Container p_30548_) {
        this.updateContainerEquipment();
    }
    protected void updateContainerEquipment() {
        if (!this.level().isClientSide) {
            this.setFlag(4, !this.inventory.getItem(0).isEmpty());
        }
    }
    private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(D4CCloneEntity.class, EntityDataSerializers.BYTE);


    protected boolean getFlag(int p_30648_) {
        return (this.entityData.get(DATA_ID_FLAGS) & p_30648_) != 0;
    }

    protected void setFlag(int p_30598_, boolean p_30599_) {
        byte b0 = this.entityData.get(DATA_ID_FLAGS);
        if (p_30599_) {
            this.entityData.set(DATA_ID_FLAGS, (byte)(b0 | p_30598_));
        } else {
            this.entityData.set(DATA_ID_FLAGS, (byte)(b0 & ~p_30598_));
        }

    }

    public void openCustomInventoryScreen(Player player) {
        if (!this.level().isClientSide) {
            ((IPlayerEntityServer)player).roundabout$openD4CCloneInventory(this, this.inventory);
        }
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (getPlayerUUID().isPresent()) {
            if (player.getUUID().equals(getPlayerUUID().get())) {
                if (((StandUser)player).roundabout$getStandPowers() instanceof PowersD4C pd4c && pd4c.isInSelectionMode
                    || !((StandUser)player).roundabout$getActive()
                ) {
                    setInteractedPlayer(player);
                    this.openCustomInventoryScreen(player);
                    return InteractionResult.sidedSuccess(this.level().isClientSide);
                }
            }
        }


        return super.mobInteract(player, hand);
    }

    @Nullable
    public Entity changeDimension(ServerLevel p_35295_) {
        stopInteraction();
        return super.changeDimension(p_35295_);
    }
}
