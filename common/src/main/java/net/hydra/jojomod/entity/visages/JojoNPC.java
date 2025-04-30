package net.hydra.jojomod.entity.visages;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import net.hydra.jojomod.event.index.Poses;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.SpawnUtil;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.sensing.GolemSensor;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class JojoNPC extends AgeableMob implements InventoryCarrier, Npc, ReputationEventHandler {
    private static final EntityDataAccessor<Integer> DATA_UNHAPPY_COUNTER = SynchedEntityData.defineId(JojoNPC.class, EntityDataSerializers.INT);
    private static final Logger LOGGER = LogUtils.getLogger();
    private final SimpleContainer inventory = new SimpleContainer(8);
    private static final int MAX_GOSSIP_TOPICS = 10;
    private static final int GOSSIP_COOLDOWN = 1200;
    private static final int GOSSIP_DECAY_INTERVAL = 24000;
    private static final int REPUTATION_CHANGE_PER_EVENT = 25;
    private static final int HOW_FAR_AWAY_TO_TALK_TO_OTHER_VILLAGERS_ABOUT_GOLEMS = 10;
    private static final int HOW_MANY_VILLAGERS_NEED_TO_AGREE_TO_SPAWN_A_GOLEM = 5;
    private static final long TIME_SINCE_SLEEPING_FOR_GOLEM_SPAWNING = 24000L;
    private static final Set<Item> WANTED_ITEMS = ImmutableSet.of(Items.BREAD, Items.POTATO, Items.CARROT, Items.WHEAT, Items.WHEAT_SEEDS, Items.BEETROOT, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD);
    @VisibleForTesting
    public static final float SPEED_MODIFIER = 0.5F;
    private boolean chasing;
    private int foodLevel;
    private final GossipContainer gossips = new GossipContainer();
    private long lastGossipTime;
    private long lastGossipDecayTime;
    private int villagerXp;
    private long lastRestockGameTime;
    private int numberOfRestocksToday;
    private long lastRestockCheckDayTime;
    private boolean assignProfessionWhenSpawned;
    protected Vec3 deltaMovementOnPreviousTick = Vec3.ZERO;

    public Player host = null;

    public boolean isDisplay = false;

    public Poses standPos = null;
    public final AnimationState WRYYY = new AnimationState();
    public final AnimationState GIORNO = new AnimationState();
    public final AnimationState JOSEPH = new AnimationState();
    public final AnimationState KOICHI = new AnimationState();
    public final AnimationState OH_NO = new AnimationState();
    public final AnimationState TORTURE_DANCE = new AnimationState();
    public final AnimationState WAMUU = new AnimationState();

    public final AnimationState JOTARO = new AnimationState();
    public final AnimationState JONATHAN = new AnimationState();

    public boolean isSimple(){
        return false;
    }

    public void setupAnimationStates() {
        if (standPos == Poses.JONATHAN) {
            this.JONATHAN.startIfStopped(this.tickCount);
        } else {
            this.JONATHAN.stop();
        }
        if (standPos == Poses.JOTARO) {
            this.JOTARO.startIfStopped(this.tickCount);
        } else {
            this.JOTARO.stop();
        }
        if (standPos == Poses.WAMUU) {
            this.WAMUU.startIfStopped(this.tickCount);
        } else {
            this.WAMUU.stop();
        }
        if (standPos == Poses.TORTURE_DANCE) {
            this.TORTURE_DANCE.startIfStopped(this.tickCount);
        } else {
            this.TORTURE_DANCE.stop();
        }
        if (standPos == Poses.OH_NO) {
            this.OH_NO.startIfStopped(this.tickCount);
        } else {
            this.OH_NO.stop();
        }
        if (standPos == Poses.WRY) {
            this.WRYYY.startIfStopped(this.tickCount);
        } else {
            this.WRYYY.stop();
        }
        if (standPos == Poses.GIORNO) {
            this.GIORNO.startIfStopped(this.tickCount);
        } else {
            this.GIORNO.stop();
        }
        if (standPos == Poses.KOICHI) {
            this.KOICHI.startIfStopped(this.tickCount);
        } else {
            this.KOICHI.stop();
        }
        if (standPos == Poses.JOSEPH) {
            this.JOSEPH.startIfStopped(this.tickCount);
        } else {
            this.JOSEPH.stop();
        }
    }
    private static final EntityDataAccessor<Byte> ROUNDABOUT$DATA_KNIFE_COUNT_ID = SynchedEntityData.defineId(JojoNPC.class,
            EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Integer> ROUNDABOUT$DODGE_TIME = SynchedEntityData.defineId(JojoNPC.class,
            EntityDataSerializers.INT);

    private static final EntityDataAccessor<Byte> ROUNDABOUT$POS = SynchedEntityData.defineId(JojoNPC.class,
            EntityDataSerializers.BYTE);
    public void roundabout$SetPos(byte Pos){
        this.getEntityData().set(ROUNDABOUT$POS, Pos);
    }
    public byte roundabout$GetPos(){
        return this.getEntityData().get(ROUNDABOUT$POS);
    }
    public final int roundabout$getKnifeCount() {
        return this.entityData.get(ROUNDABOUT$DATA_KNIFE_COUNT_ID);
    }

    public void roundabout$addKnife() {
        byte knifeCount = this.entityData.get(ROUNDABOUT$DATA_KNIFE_COUNT_ID);

        knifeCount++;
        if (knifeCount <= 12){
            ((LivingEntity) (Object) this).getEntityData().set(ROUNDABOUT$DATA_KNIFE_COUNT_ID, knifeCount);
        }
    }

    private int roundabout$clientDodgeTime = 0;
    public int roundabout$getDodgeTime(){
        return this.getEntityData().get(ROUNDABOUT$DODGE_TIME);
    }
    public int roundabout$getClientDodgeTime(){
        return roundabout$clientDodgeTime;
    }
    public void roundabout$setClientDodgeTime(int dodgeTime){
        roundabout$clientDodgeTime = dodgeTime;
    }
    public void roundabout$setDodgeTime(int dodgeTime){
        this.getEntityData().set(ROUNDABOUT$DODGE_TIME, dodgeTime);
    }
    public void roundabout$setKnife(byte knives) {
        this.getEntityData().set(ROUNDABOUT$DATA_KNIFE_COUNT_ID, knives);
    }

    public CompoundTag getShoulderEntityLeft() {
        return this.entityData.get(DATA_SHOULDER_LEFT);
    }

    protected void setShoulderEntityLeft(CompoundTag $$0) {
        this.entityData.set(DATA_SHOULDER_LEFT, $$0);
    }

    public CompoundTag getShoulderEntityRight() {
        return this.entityData.get(DATA_SHOULDER_RIGHT);
    }

    protected void setShoulderEntityRight(CompoundTag $$0) {
        this.entityData.set(DATA_SHOULDER_RIGHT, $$0);
    }
    public static final Map<Item, Integer> FOOD_POINTS = ImmutableMap.of(Items.BREAD, 4, Items.POTATO, 1, Items.CARROT, 1, Items.BEETROOT, 1);

    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.HOME, MemoryModuleType.MEETING_POINT, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.BREED_TARGET, MemoryModuleType.PATH, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_BED, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.NEAREST_HOSTILE, MemoryModuleType.HIDING_PLACE, MemoryModuleType.HEARD_BELL_TIME, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.LAST_SLEPT, MemoryModuleType.LAST_WOKEN, MemoryModuleType.LAST_WORKED_AT_POI, MemoryModuleType.GOLEM_DETECTED_RECENTLY);
    private static final ImmutableList<SensorType<? extends Sensor<? super JojoNPC>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.NEAREST_BED, SensorType.HURT_BY, SensorType.VILLAGER_HOSTILES, SensorType.VILLAGER_BABIES, SensorType.GOLEM_DETECTED);
    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<JojoNPC, Holder<PoiType>>> POI_MEMORIES = ImmutableMap.of(MemoryModuleType.HOME, (p_219625_, p_219626_) -> {
        return p_219626_.is(PoiTypes.HOME);
    }, MemoryModuleType.MEETING_POINT, (p_219616_, p_219617_) -> {
        return p_219617_.is(PoiTypes.MEETING);
    });

    public JojoNPC(EntityType<? extends JojoNPC> p_35384_, Level p_35385_) {
        super(p_35384_, p_35385_);
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        this.getNavigation().setCanFloat(true);
        this.setCanPickUpLoot(true);
    }


    public Brain<JojoNPC> getBrain() {
        return (Brain<JojoNPC>)super.getBrain();
    }

    protected Brain.Provider<JojoNPC> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }


    public Vec3 getDeltaMovementLerped(float $$0) {
        return this.deltaMovementOnPreviousTick.lerp(this.getDeltaMovement(), (double)$$0);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.26).add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.ATTACK_DAMAGE, 5).
                add(Attributes.FOLLOW_RANGE, 48.0D);
    }


    public boolean isUsingBrain(){
        return true;
    }
    public void useNotBrain(){
    }

    protected void customServerAiStep() {
        if (isUsingBrain()) {
            this.getBrain().tick((ServerLevel) this.level(), this);
        } else {
            useNotBrain();
        }
        if (this.assignProfessionWhenSpawned) {
            this.assignProfessionWhenSpawned = false;
        }

        if (!this.isNoAi() && this.random.nextInt(100) == 0) {
            Raid raid = ((ServerLevel)this.level()).getRaidAt(this.blockPosition());
            if (raid != null && raid.isActive() && !raid.isOver()) {
                this.level().broadcastEntityEvent(this, (byte)42);
            }
        }

        super.customServerAiStep();
    }
    public void tick() {
        this.deltaMovementOnPreviousTick = this.getDeltaMovement();
        super.tick();
        if (this.getUnhappyCounter() > 0) {
            this.setUnhappyCounter(this.getUnhappyCounter() - 1);
        }

        this.maybeDecayGossip();
    }

    public int getUnhappyCounter() {
        return this.entityData.get(DATA_UNHAPPY_COUNTER);
    }

    private void setUnhappy() {
        this.setUnhappyCounter(40);
        if (!this.level().isClientSide()) {
            this.playSound(SoundEvents.VILLAGER_NO, this.getSoundVolume(), this.getVoicePitch());
        }

    }
    public void setUnhappyCounter(int p_35320_) {
        this.entityData.set(DATA_UNHAPPY_COUNTER, p_35320_);
    }
    protected Brain<?> makeBrain(Dynamic<?> p_35445_) {
        Brain<JojoNPC> brain = this.brainProvider().makeBrain(p_35445_);
        this.registerBrainGoals(brain);
        return brain;
    }

    public void refreshBrain(ServerLevel p_35484_) {
        Brain<JojoNPC> brain = this.getBrain();
        brain.stopAll(p_35484_, this);
        this.brain = brain.copyWithoutBehaviors();
        this.registerBrainGoals(this.getBrain());
    }

    public void registerBrainGoals(Brain<JojoNPC> p_35425_) {
        if (this.isBaby()) {
            p_35425_.setSchedule(Schedule.VILLAGER_BABY);
            p_35425_.addActivity(Activity.PLAY, JojoNPCGoalPackages.getPlayPackage(1F));
        } else {
            p_35425_.setSchedule(Schedule.VILLAGER_DEFAULT);
            p_35425_.addActivityWithConditions(Activity.WORK, JojoNPCGoalPackages.getMeetPackage(1F), ImmutableSet.of(Pair.of(MemoryModuleType.MEETING_POINT, MemoryStatus.VALUE_PRESENT)));
        }

        p_35425_.addActivity(Activity.CORE, JojoNPCGoalPackages.getCorePackage(1F));
        p_35425_.addActivityWithConditions(Activity.MEET, JojoNPCGoalPackages.getMeetPackage(1F), ImmutableSet.of(Pair.of(MemoryModuleType.MEETING_POINT, MemoryStatus.VALUE_PRESENT)));
        p_35425_.addActivity(Activity.REST, JojoNPCGoalPackages.getRestPackage(1F));
        p_35425_.addActivity(Activity.IDLE, JojoNPCGoalPackages.getIdlePackage(1F));
        p_35425_.addActivity(Activity.PANIC, JojoNPCGoalPackages.getPanicPackage(1F));
        p_35425_.addActivity(Activity.PRE_RAID, JojoNPCGoalPackages.getPreRaidPackage(1F));
        p_35425_.addActivity(Activity.RAID, JojoNPCGoalPackages.getRaidPackage(1F));
        p_35425_.addActivity(Activity.HIDE, JojoNPCGoalPackages.getHidePackage(1F));
        p_35425_.setCoreActivities(ImmutableSet.of(Activity.CORE));
        p_35425_.setDefaultActivity(Activity.IDLE);
        p_35425_.setActiveActivityIfPossible(Activity.IDLE);
        p_35425_.updateActivityFromSchedule(this.level().getDayTime(), this.level().getGameTime());
    }

    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        if (this.level() instanceof ServerLevel) {
            this.refreshBrain((ServerLevel)this.level());
        }
    }
    @javax.annotation.Nullable
    protected SoundEvent getAmbientSound() {
        if (this.isSleeping()) {
            return null;
        } else {
            return null;
        }
    }

    private static final EntityDataAccessor<Float> DATA_PLAYER_ABSORPTION_ID = SynchedEntityData.defineId(JojoNPC.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_SCORE_ID = SynchedEntityData.defineId(JojoNPC.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Byte> DATA_PLAYER_MODE_CUSTOMISATION = SynchedEntityData.defineId(JojoNPC.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Byte> DATA_PLAYER_MAIN_HAND = SynchedEntityData.defineId(JojoNPC.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<CompoundTag> DATA_SHOULDER_LEFT = SynchedEntityData.defineId(JojoNPC.class, EntityDataSerializers.COMPOUND_TAG);
    protected static final EntityDataAccessor<CompoundTag> DATA_SHOULDER_RIGHT = SynchedEntityData.defineId(JojoNPC.class, EntityDataSerializers.COMPOUND_TAG);
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_UNHAPPY_COUNTER, 0);
        this.entityData.define(ROUNDABOUT$POS, (byte)0);
        this.entityData.define(DATA_PLAYER_ABSORPTION_ID, 0.0F);
        this.entityData.define(DATA_SCORE_ID, 0);
        this.entityData.define(DATA_PLAYER_MODE_CUSTOMISATION, (byte)0);
        this.entityData.define(ROUNDABOUT$DODGE_TIME, -1);
        this.entityData.define(ROUNDABOUT$DATA_KNIFE_COUNT_ID, (byte)0);
        this.entityData.define(DATA_PLAYER_MAIN_HAND, (byte)1);
        this.entityData.define(DATA_SHOULDER_LEFT, new CompoundTag());
        this.entityData.define(DATA_SHOULDER_RIGHT, new CompoundTag());
    }

    public void addAdditionalSaveData(CompoundTag p_35481_) {
        super.addAdditionalSaveData(p_35481_);
        p_35481_.putByte("FoodLevel", (byte)this.foodLevel);
        p_35481_.put("Gossips", this.gossips.store(NbtOps.INSTANCE));
        p_35481_.putInt("Xp", this.villagerXp);
        p_35481_.putLong("LastGossipDecay", this.lastGossipDecayTime);
        this.writeInventoryToTag(p_35481_);

    }

    public void readAdditionalSaveData(CompoundTag p_35451_) {
        super.readAdditionalSaveData(p_35451_);

        if (p_35451_.contains("FoodLevel", 1)) {
            this.foodLevel = p_35451_.getByte("FoodLevel");
        }

        ListTag listtag = p_35451_.getList("Gossips", 10);
        this.gossips.update(new Dynamic<>(NbtOps.INSTANCE, listtag));
        if (p_35451_.contains("Xp", 3)) {
            this.villagerXp = p_35451_.getInt("Xp");
        }

        this.lastGossipDecayTime = p_35451_.getLong("LastGossipDecay");
        this.setCanPickUpLoot(true);
        if (this.level() instanceof ServerLevel) {
            this.refreshBrain((ServerLevel)this.level());
        }


    }


    private boolean hungry() {
        return this.foodLevel < 12;
    }

    public boolean hasExcessFood() {
        return this.countFoodPointsInInventory() >= 24;
    }

    public boolean wantsMoreFood() {
        return this.countFoodPointsInInventory() < 12;
    }

    private int countFoodPointsInInventory() {
        SimpleContainer simplecontainer = this.getInventory();
        return FOOD_POINTS.entrySet().stream().mapToInt((p_186300_) -> {
            return simplecontainer.countItem(p_186300_.getKey()) * p_186300_.getValue();
        }).sum();
    }
    private void eatUntilFull() {
        if (this.hungry() && this.countFoodPointsInInventory() != 0) {
            for(int i = 0; i < this.getInventory().getContainerSize(); ++i) {
                ItemStack itemstack = this.getInventory().getItem(i);
                if (!itemstack.isEmpty()) {
                    Integer integer = FOOD_POINTS.get(itemstack.getItem());
                    if (integer != null) {
                        int j = itemstack.getCount();

                        for(int k = j; k > 0; --k) {
                            this.foodLevel += integer;
                            this.getInventory().removeItem(i, 1);
                            if (!this.hungry()) {
                                return;
                            }
                        }
                    }
                }
            }

        }
    }

    public int getPlayerReputation(Player p_35533_) {
        return this.gossips.getReputation(p_35533_.getUUID(), (p_186302_) -> {
            return true;
        });
    }
    private void digestFood(int p_35549_) {
        this.foodLevel -= p_35549_;
    }

    public void eatAndDigestFood() {
        this.eatUntilFull();
        this.digestFood(12);
    }
    public void handleEntityEvent(byte p_35391_) {
        if (p_35391_ == 12) {
            this.addParticlesAroundSelf(ParticleTypes.HEART);
        } else if (p_35391_ == 13) {
            this.addParticlesAroundSelf(ParticleTypes.ANGRY_VILLAGER);
        } else if (p_35391_ == 14) {
            this.addParticlesAroundSelf(ParticleTypes.HAPPY_VILLAGER);
        } else if (p_35391_ == 42) {
            this.addParticlesAroundSelf(ParticleTypes.SPLASH);
        } else {
            super.handleEntityEvent(p_35391_);
        }

    }

    public boolean removeWhenFarAway(double p_35535_) {
        return false;
    }
    protected SoundEvent getHurtSound(DamageSource p_35498_) {
        return SoundEvents.PLAYER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PLAYER_DEATH;
    }

    public void setChasing(boolean p_150016_) {
        this.chasing = p_150016_;
    }

    public boolean isChasing() {
        return this.chasing;
    }

    public void setLastHurtByMob(@javax.annotation.Nullable LivingEntity p_35423_) {
        if (p_35423_ != null && this.level() instanceof ServerLevel) {
            ((ServerLevel)this.level()).onReputationEvent(ReputationEventType.VILLAGER_HURT, p_35423_, this);
            if (this.isAlive() && p_35423_ instanceof Player) {
                this.level().broadcastEntityEvent(this, (byte)13);
            }
        }

        super.setLastHurtByMob(p_35423_);
    }

    public void die(DamageSource p_35419_) {
        LOGGER.info("JojoNPC {} died, message: '{}'", this, p_35419_.getLocalizedDeathMessage(this).getString());
        Entity entity = p_35419_.getEntity();
        if (entity != null) {
            this.tellWitnessesThatIWasMurdered(entity);
        }

        this.releaseAllPois();
        super.die(p_35419_);
    }
    private void releaseAllPois() {
        this.releasePoi(MemoryModuleType.HOME);
        this.releasePoi(MemoryModuleType.MEETING_POINT);
    }

    public void spawnGolemIfNeeded(ServerLevel p_35398_, long p_35399_, int p_35400_) {
        if (this.wantsToSpawnGolem(p_35399_)) {
            AABB aabb = this.getBoundingBox().inflate(10.0D, 10.0D, 10.0D);
            List<Villager> list = p_35398_.getEntitiesOfClass(Villager.class, aabb);
            List<Villager> list1 = list.stream().filter((p_186293_) -> {
                return p_186293_.wantsToSpawnGolem(p_35399_);
            }).limit(5L).collect(Collectors.toList());
            if (list1.size() >= p_35400_) {
                if (SpawnUtil.trySpawnMob(EntityType.IRON_GOLEM, MobSpawnType.MOB_SUMMONED, p_35398_, this.blockPosition(), 10, 8, 6, SpawnUtil.Strategy.LEGACY_IRON_GOLEM).isPresent()) {
                    list.forEach(GolemSensor::golemDetected);
                }
            }
        }
    }
    public boolean wantsToSpawnGolem(long p_35393_) {
        if (!this.golemSpawnConditionsMet(this.level().getGameTime())) {
            return false;
        } else {
            return !this.brain.hasMemoryValue(MemoryModuleType.GOLEM_DETECTED_RECENTLY);
        }
    }



    protected void pickUpItem(ItemEntity p_35467_) {
        InventoryCarrier.pickUpItem(this, this, p_35467_);
    }

    public boolean wantsToPickUp(ItemStack p_35543_) {
        Item item = p_35543_.getItem();
        return (WANTED_ITEMS.contains(item) && this.getInventory().canAddItem(p_35543_));
    }
    public GossipContainer getGossips() {
        return this.gossips;
    }

    public void setGossips(Tag p_35456_) {
        this.gossips.update(new Dynamic<>(NbtOps.INSTANCE, p_35456_));
    }

    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }
    public void startSleeping(BlockPos p_35479_) {
        super.startSleeping(p_35479_);
        this.brain.setMemory(MemoryModuleType.LAST_SLEPT, this.level().getGameTime());
        this.brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        this.brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
    }

    public void stopSleeping() {
        super.stopSleeping();
        this.brain.setMemory(MemoryModuleType.LAST_WOKEN, this.level().getGameTime());
    }

    private boolean golemSpawnConditionsMet(long p_35462_) {
        Optional<Long> optional = this.brain.getMemory(MemoryModuleType.LAST_SLEPT);
        if (optional.isPresent()) {
            return p_35462_ - optional.get() < 24000L;
        } else {
            return false;
        }
    }

    public void gossip(ServerLevel p_35412_, JojoNPC p_35413_, long p_35414_) {
        if ((p_35414_ < this.lastGossipTime || p_35414_ >= this.lastGossipTime + 1200L) && (p_35414_ < p_35413_.lastGossipTime || p_35414_ >= p_35413_.lastGossipTime + 1200L)) {
            this.gossips.transferFrom(p_35413_.gossips, this.random, 10);
            this.lastGossipTime = p_35414_;
            p_35413_.lastGossipTime = p_35414_;
            this.spawnGolemIfNeeded(p_35412_, p_35414_, 5);
        }
    }
    private void maybeDecayGossip() {
        long i = this.level().getGameTime();
        if (this.lastGossipDecayTime == 0L) {
            this.lastGossipDecayTime = i;
        } else if (i >= this.lastGossipDecayTime + 24000L) {
            this.gossips.decay();
            this.lastGossipDecayTime = i;
        }
    }
    private void tellWitnessesThatIWasMurdered(Entity p_35421_) {
        Level $$3 = this.level();
        if ($$3 instanceof ServerLevel serverlevel) {
            Optional<NearestVisibleLivingEntities> optional = this.brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
            if (!optional.isEmpty()) {
                optional.get().findAll(ReputationEventHandler.class::isInstance).forEach((p_186297_) -> {
                    serverlevel.onReputationEvent(ReputationEventType.VILLAGER_KILLED, p_35421_, (ReputationEventHandler)p_186297_);
                });
            }
        }
    }
    public void releasePoi(MemoryModuleType<GlobalPos> p_35429_) {
        if (this.level() instanceof ServerLevel) {
            MinecraftServer minecraftserver = ((ServerLevel)this.level()).getServer();
            this.brain.getMemory(p_35429_).ifPresent((p_186306_) -> {
                ServerLevel serverlevel = minecraftserver.getLevel(p_186306_.dimension());
                if (serverlevel != null) {
                    PoiManager poimanager = serverlevel.getPoiManager();
                    Optional<Holder<PoiType>> optional = poimanager.getType(p_186306_.pos());
                    BiPredicate<JojoNPC, Holder<PoiType>> bipredicate = POI_MEMORIES.get(p_35429_);
                    if (optional.isPresent() && bipredicate.test(this, optional.get())) {
                        poimanager.release(p_186306_.pos());
                        DebugPackets.sendPoiTicketCountPacket(serverlevel, p_186306_.pos());
                    }

                }
            });
        }
    }

    public void playCelebrateSound() {
        //this.playSound(SoundEvents.VILLAGER_CELEBRATE, this.getSoundVolume(), this.getVoicePitch());
    }
    @Nullable
    @Override
    public JojoNPC getBreedOffspring(ServerLevel p_150012_, AgeableMob p_150013_) {
        return null;
    }


    public void onReputationEventFrom(ReputationEventType p_35431_, Entity p_35432_) {
        if (p_35431_ == ReputationEventType.ZOMBIE_VILLAGER_CURED) {
            this.gossips.add(p_35432_.getUUID(), GossipType.MAJOR_POSITIVE, 20);
            this.gossips.add(p_35432_.getUUID(), GossipType.MINOR_POSITIVE, 25);
        } else if (p_35431_ == ReputationEventType.TRADE) {
            this.gossips.add(p_35432_.getUUID(), GossipType.TRADING, 2);
        } else if (p_35431_ == ReputationEventType.VILLAGER_HURT) {
            this.gossips.add(p_35432_.getUUID(), GossipType.MINOR_NEGATIVE, 25);
        } else if (p_35431_ == ReputationEventType.VILLAGER_KILLED) {
            this.gossips.add(p_35432_.getUUID(), GossipType.MAJOR_NEGATIVE, 25);
        }

    }



    public Vec3 getRopeHoldPosition(float p_35318_) {
        float f = Mth.lerp(p_35318_, this.yBodyRotO, this.yBodyRot) * ((float)Math.PI / 180F);
        Vec3 vec3 = new Vec3(0.0D, this.getBoundingBox().getYsize() - 1.0D, 0.2D);
        return this.getPosition(p_35318_).add(vec3.yRot(-f));
    }

    public boolean isClientSide() {
        return this.level().isClientSide;
    }


    protected void addParticlesAroundSelf(ParticleOptions p_35288_) {
        for(int i = 0; i < 5; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level().addParticle(p_35288_, this.getRandomX(1.0D), this.getRandomY() + 1.0D, this.getRandomZ(1.0D), d0, d1, d2);
        }

    }

    public boolean canBeLeashed(Player p_35272_) {
        return false;
    }

    public SimpleContainer getInventory() {
        return this.inventory;
    }

    public SlotAccess getSlot(int p_149995_) {
        int i = p_149995_ - 300;
        return i >= 0 && i < this.inventory.getContainerSize() ? SlotAccess.forContainer(this.inventory, i) : super.getSlot(p_149995_);
    }

    private Vector3f sizeOffset = new Vector3f(0.f, 0.f, 0.f);
    public void setSizeOffset(Vector3f value) { this.sizeOffset = value; }
    public Vector3f getSizeOffset() { return this.sizeOffset; }
}
