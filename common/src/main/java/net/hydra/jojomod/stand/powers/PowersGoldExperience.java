package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.block.*;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.client.hud.StandHudRender;
import net.hydra.jojomod.entity.*;
import net.hydra.jojomod.entity.gold_experience.*;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.GoldExperienceEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.*;
import net.hydra.jojomod.access.IGEProjectile;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.BlockGrabPreset;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.Config;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.*;

public class PowersGoldExperience extends BlockGrabPreset {
    // move bytes
    private static final byte
            LIFE_PUNCH = 53,
            DNA_CREATE = 54,
            BUSH_CREATION = 55,
            WALL_VINES = 56,
            HEAL = 57,
            BRAMBLES = 58,
            LIFEFORM_CREATION = 59,
            ITEM_THROW = 60,
            TREE = 61,
            LIFE_SENSOR = 63,
            HEAL_CHARGE = 64,
            REQUIEM = 90;

    // move bytes found in other moves
    public static final byte ITEM_IMBUE = 62;



    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("resolvePoints", resolvePoints);
        if (standArrow != null && !standArrow.isEmpty()) {
            CompoundTag tag2 = new CompoundTag();
            tag.put("StandArrow", standArrow.save(tag2));
        }else {
            tag.remove("StandArrow"); // dupe prevention
        }

        super.addAdditionalSaveData(tag);
    }
    protected boolean giveArrowOnLoad() {
        return activePower != REQUIEM; // prevent from the requiem transformation giving this arrow back before it is time
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("resolvePoints")) {
            resolvePoints = tag.getFloat("resolvePoints");
        }
        if (tag.contains("StandArrow")) {
            CompoundTag compoundtag = tag.getCompound("StandArrow");
            standArrow = ItemStack.of(compoundtag);
            if (standArrow != null && !standArrow.isEmpty() && giveArrowOnLoad()) {
                giveStandArrowBack();
            }
        }

        super.readAdditionalSaveData(tag);
    }

    protected void giveStandArrowBack() {
        if (canAddItem(standArrow, ((Player) this.getSelf()).getInventory())) {
            ((Player) this.getSelf()).addItem(standArrow);
        } else {
            ItemEntity $$4 = new ItemEntity(this.getSelf().level(), this.getSelf().getX(),
                    this.getSelf().getY() + this.getSelf().getEyeHeight(), this.getSelf().getZ(),
                    standArrow);
            $$4.setPickUpDelay(40);
            $$4.setThrower(this.getSelf().getUUID());
            PowerTypes.copyPlaneOfExisting(self,$$4);
            this.getSelf().level().addFreshEntity($$4);
        }
        standArrow = null;
    }

    @Override
    public boolean interceptIncomingHarm(DamageSource source, float amount) {
        if (activePower == REQUIEM) {
            return !source.is(DamageTypes.GENERIC_KILL);
        }

        return super.interceptIncomingHarm(source, amount);
    }

    // for cooldowns

    /*
    NOTE:
    GENERAL_EXTRA is for DNA creation
    GENERAL_1, GENERAL_2, GENERAL_4 are all for guard versions of moves
     */

    private final int getDNACooldown() {
        return geConfig().goldExperienceCooldownDNA;
    };
    private final int getAnimalCreationCooldown() {
        return geConfig().goldExperienceCooldownAnimal;
    };
    private final int getHealCooldown() {
        return geConfig().goldExperienceCooldownHeal;
    };
    private final int getLifeSensorCooldown() {
        return geConfig().goldExperienceCooldownLifeSensor;
    };
    private final int getItemImbueCooldown() {
        return geConfig().goldExperienceCooldownItemImbue;
    };
    private final int getLifePunchCooldown() {
        return geConfig().goldExperienceCooldownLifePunch;
    };
    private final int getVineCooldown() {
        return geConfig().goldExperienceCooldownVine;
    };
    private final int getTreeCooldown() {
        return geConfig().goldExperienceCooldownTree;
    };
    private final int getBrambleCooldown() {
        return geConfig().goldExperienceCooldownBramble;
    };
    private final int getBushCooldown() {
        return geConfig().goldExperienceCooldownBush;
    };

    // for damage

    @Override
    public float getPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(1.15F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(4F));
        }
    }
    @Override
    public float getHeavyPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(1.6F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(5F));
        }
    }
    @Override
    public float getBarrageFinisherStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(1.8F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(6F));
        }
    }
    @Override
    public float getBarrageDamagePlayer(){
        return levelupDamageMod(multiplyPowerByStandConfigPlayers(7));
    }
    @Override
    public float getBarrageDamageMob(){
        return levelupDamageMod(multiplyPowerByStandConfigMobs(14));
    }
    public float getBrambleDamage(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(1.5F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(6F));
        }
    }
    public float getThornDamage(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(0.6F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(4F));
        }
    }

    @Override
    public float multiplyPowerByStandConfigPlayers(float power){
        return (float) (power*(geConfig().goldExperienceAttackMultOnPlayers *0.01));
    }

    @Override
    public float multiplyPowerByStandConfigMobs(float power){
        return (float) (power*(geConfig().goldExperienceAttackMultOnMobs *0.01));
    }

    // for levels
    private final int getLifePunchLevel() {
        return 2;
    };
    private final int getTreeLevel() {
        return 3;
    };
    private final int getHealLevel() {
        return 4;
    };
    private final int getItemImbueLevel() {
        return 5;
    };
    private final int getBushLevel() {
        return 6;
    };
    private final int getBrambleLevel() {
        return 7;
    };

    // for configs
    private Config.GoldExperienceSettings geConfig() {
        return ClientNetworking.getAppropriateConfig().goldExperienceSettings;
    }

    // for healing
    public boolean holdDownHeal = false;
    private boolean hasHealed = false;
    private int healTargetId = -1;
    private boolean healOthers = false;
    private static final int MAX_HEAL_CHARGE_TICKS = 15;

    // for life punch
    private LifePunchCloneEntity lifePunchClone = null;
    private LivingEntity lifePunchVictim = null;
    private int lifePunchTimer = 0;
    private float lastKnownHealth = 0;
    private final Map<Integer, Integer> disabledMobTimers = new HashMap<>();

    // for brambles
    private final List<BrambleSpawnData> pendingBrambles = new ArrayList<>();
    private int brambleSpawnTimer = 0;
    private record BrambleSpawnData(Vec3 pos, float yRot) {}

    // for bush
    private final List<BushSpawnData> pendingBushes = new ArrayList<>();
    private Vec3 bushWaveOrigin = null;
    private int bushWaveTick = 0;
    private record BushSpawnData(BlockPos pos, double distSq) {}

    // for lifeforms
    /**For mob ai, change the animalType before trypower to set what kind of plunder it has*/
    private byte animalType = AnimalTypes.FROG.id;
    @Nullable
    public static EntityType<?> getAnimalEntityType(byte animalId) {
        if (animalId == AnimalTypes.BAT.id) return ModEntities.GE_BAT;
        if (animalId == AnimalTypes.FROG.id) return ModEntities.GE_FROG;
        if (animalId == AnimalTypes.BEE.id) return ModEntities.GE_BEE;
        if (animalId == AnimalTypes.FISH.id) return ModEntities.GE_FISH;
        if (animalId == AnimalTypes.FOX.id) return ModEntities.GE_FOX;
        if (animalId == AnimalTypes.SNAKE.id) return ModEntities.GE_SNAKE;
        if (animalId == AnimalTypes.PIRAHNA.id) return ModEntities.GE_PIRANHA;
        if (animalId == AnimalTypes.BUTTERFLY.id) return ModEntities.GE_BUTTERFLY;
        return null;
    }

    // for tree growth
    private final List<TreeBlockSpawnData> pendingTreeBlocks = new ArrayList<>();
    private int treeSpawnTimer = 0;
    private BlockPos treeBasePos = null;
    private Direction treeGrowthDir = Direction.UP;
    private record TreeBlockSpawnData(
            Vector3f startPos,
            Vector3f finalPos,
            BlockState state,
            int delayTicks,
            boolean isTrunk
    ) {}
    private double treeRoofY = -500.0;

    // for ground barrage thorns
    private int groundBarrageTicks = 0;
    private boolean isGroundBarraging = false;
    public boolean isGroundBarraging() {
        return this.isGroundBarraging && this.getActivePower() == PowerIndex.BARRAGE && this.getAttackTimeDuring() >= 0;
    }

    // for item imbue
    public static final String IMBUE_TAG = "GEImbue";
    public static final byte IMBUE_ANIMAL = 1;
    public static final byte IMBUE_TREE = 2;
    public static final byte IMBUE_DISGUISE = 3;
    @Nullable
    private UUID activeImbuedItemId = null;
    @Nullable
    public UUID getActiveImbuedItemId() {
        return this.activeImbuedItemId;
    }
    public void setActiveImbuedItemId(@Nullable UUID id) {
        this.activeImbuedItemId = id;
    }
    public static List<Item> getDisguiseOptions() {
        return MainUtil.GOLD_EXPERIENCE_DISGUISE_ITEMS;
    }
    private record KnifeSpawnData(KnifeEntity knife, int delayTicks) {}
    private final List<KnifeSpawnData> pendingKnives = new ArrayList<>();

    // for life sensor
    public boolean lifeSensorActive = false;
    private int sensedImbueEntityId = -1;

    // for sounds
    protected static final byte BARRAGE_NOISE = 20;
    private static final byte LAST_HIT_NOISE = 21;

    // basic stand registry stuff
    public PowersGoldExperience(LivingEntity self) {super(self);}
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {return new PowersGoldExperience(entity);}
    @Override
    public boolean isStandEnabled(){return geConfig().enableGoldExperience;}

    // configs start

    @Override
    public float getMiningMultiplier() {
        return (float) (1F*(geConfig().miningSpeedMultiplierGoldExperience *0.01));
    }

    @Override
    public int getMiningLevel() {
        return geConfig().getMiningTierGoldExperience;
    }

    @Override
    public int getMaxGuardPoints(){
        return geConfig().goldExperienceGuardPoints;
    }

    // configs end

    // stand rejection start

    @Override
    public void tickStandRejection(MobEffectInstance effect){
        if (!this.getSelf().level().isClientSide()) {
            if (effect.getDuration() == 200) {
                playSoundIfPossible(self.level(), null, this.self.blockPosition(), ModSounds.GOLD_EXPERIENCE_LIFE_PUNCH_EVENT, SoundSource.PLAYERS, 0.95F, 1.1F);
                LivingEntity LE = this.getSelf();
                if (LE instanceof Player) {
                    // IMPORTANT FAILSAFE: If someone else gets Life Punched, restore previous victim first
                    if (lifePunchVictim != null && lifePunchVictim != LE) {
                        restoreLifePunchVictim();
                    }
                    // punch the SOUL outta them
                    PowerTypes.setPlaneOfExisting(LE, (byte) 12);
                    // spawn a Life Punch Clone in their place
                    LifePunchCloneEntity clone = ModEntities.LIFE_PUNCH_CLONE.create(this.self.level());
                    if (clone != null) {
                        clone.setPlayer((Player) LE);
                        clone.copyPosition(LE);
                        clone.setHealth(LE.getHealth());
                        clone.getAttribute(Attributes.MAX_HEALTH).setBaseValue(LE.getMaxHealth());
                        // copy inventory
                        for (EquipmentSlot slot : EquipmentSlot.values()) {
                            clone.setItemSlot(slot, LE.getItemBySlot(slot).copy());
                        }
                        clone.setTarget((LivingEntity) this.self);
                        this.self.level().addFreshEntity(clone);
                        // track the victim
                        lifePunchVictim = LE;
                        lifePunchClone = clone;
                        lifePunchTimer = 180; // 9 seconds
                        lastKnownHealth = clone.getHealth();
                    }
                } else if (LE instanceof Mob mob) {
                    ((StandUser) mob).roundabout$deeplyRemoveAttackTarget();
                    mob.getNavigation().stop();
                    disabledMobTimers.put(LE.getId(), 180);
                }
            }
            tickLifePunch();
            // failsafe just in case it doesn't tick properly
            if (effect.getDuration() <= 1 && lifePunchVictim != null) {
                if (lifePunchClone != null) {
                    lifePunchVictim.teleportTo(lifePunchClone.getX(), lifePunchClone.getY(), lifePunchClone.getZ());
                }
                restoreLifePunchVictim();
            }
        }
    }

    // stand rejection end

    // mob ai start

    private int attackCD = 0;

    @Override
    public void tickMobAI(LivingEntity attackTarget) {
        if (this.self.level().isClientSide()) return;

        if (this.attackCD > 0) {
            this.attackCD--;
        }

        if ((this.getActivePower() == HEAL_CHARGE || this.getActivePower() == HEAL)
                && this.healOthers
                && this.healTargetId != -1
                && this.self.level().getEntity(this.healTargetId) instanceof LivingEntity healTarget
                && healTarget.isAlive()) {
            rotateMobHead(healTarget);
            return;
        }

        if (this.getActivePower() != PowerIndex.NONE && this.getActivePower() != PowerIndex.ATTACK) {
            if (attackTarget != null && attackTarget.isAlive()) {
                rotateMobHead(attackTarget);
            }
            return;
        }

        boolean canHeal = !this.onCooldown(PowerIndex.SKILL_1_SNEAK);

        // selfheal below half
        if (canHeal && this.self.getHealth() < this.self.getMaxHealth() * 0.5F) {
            this.healOthers = false;
            this.healTargetId = -1;
            ((StandUser) this.getSelf()).roundabout$tryPower(HEAL_CHARGE, true);
            return;
        }

        // villagers heal others
        if (this.self instanceof AbstractVillager villager) {
            boolean isFleeing = ((IMob) villager).roundabout$getFightOrFlight();

            if (canHeal) {
                List<LivingEntity> nearbyAllies = this.self.level().getEntitiesOfClass(
                        LivingEntity.class,
                        this.self.getBoundingBox().inflate(6.0),
                        e -> e != this.self
                                && e.isAlive()
                                && e.getHealth() < e.getMaxHealth() * 0.75F
                                && (e instanceof AbstractVillager || e instanceof IronGolem)
                );

                if (!nearbyAllies.isEmpty()) {
                    LivingEntity allyToHeal = nearbyAllies.get(0);
                    this.healOthers = true;
                    this.healTargetId = allyToHeal.getId();
                    rotateMobHead(allyToHeal);
                    ((StandUser) this.getSelf()).roundabout$tryPower(HEAL_CHARGE, true);
                    return;
                }
            }
        }

        if (attackTarget != null && attackTarget.isAlive() && !this.isDazed(this.getSelf())) {
            double dist = attackTarget.distanceTo(this.getSelf());

            if (dist <= 9.0) {
                rotateMobHead(attackTarget);
            }

            // just make something lol
            if (this.self instanceof Creeper) {
                animalType = AnimalTypes.FROG.id;
            } else if (this.self instanceof AbstractSkeleton) {
                animalType = AnimalTypes.BAT.id;
            } else if (this.self instanceof Drowned) {
                animalType = AnimalTypes.PIRAHNA.id;
            }
            // everything else makes one of the good "pvp" mobs
            else {
                double rng = Math.random();
                if (rng < 1.0 / 3.0) {
                    animalType = AnimalTypes.FROG.id;
                } else if (rng < 2.0 / 3.0) {
                    animalType = AnimalTypes.BAT.id;
                } else {
                    animalType = AnimalTypes.SNAKE.id;
                }
            }

            // close range
            if (dist <= 4.0 && this.canAttack() && !this.onCooldown(PowerIndex.GENERAL_3) && Math.random() < 0.3 && this.attackCD <= 0) {
                ((StandUser) this.getSelf()).roundabout$tryPower(LIFE_PUNCH, true);
                this.attackCD = 100;
                return;
            }

            // mid range
            else if (dist <= 8.0 && !this.onCooldown(PowerIndex.SKILL_4) && Math.random() < 0.25 && this.attackCD <= 0) {
                ((StandUser) this.getSelf()).roundabout$tryPower(BRAMBLES, true);
                this.attackCD = 100;
                return;
            }

            // util
            else if (dist <= 9.0 && !this.onCooldown(PowerIndex.SKILL_2) && this.attackCD <= 0) {
                ((StandUser) this.getSelf()).roundabout$tryIntPower(LIFEFORM_CREATION, true, animalType);
                this.attackCD = 100;
                return;
            }

            // barrage and whatever
            else {super.tickMobAI(attackTarget);}
        }
    }

    // mob ai end

    // start of move methods

    // tick power start

    @Override
    public void tickPower() {
        if (resolveRenderTicks > 0) {
            resolveRenderTicks--;
        }
        if (!this.self.level().isClientSide()) {
            // 1 second knife animal fire delay
            if (!this.pendingKnives.isEmpty()) {
                Level level = this.getSelf().level();
                Iterator<KnifeSpawnData> it = this.pendingKnives.iterator();
                while (it.hasNext()) {
                    KnifeSpawnData data = it.next();
                    if (data.delayTicks() <= 0) {
                        level.addFreshEntity(data.knife());
                        it.remove();
                    }
                }
                this.pendingKnives.replaceAll(d -> new KnifeSpawnData(d.knife(), d.delayTicks() - 1));
            }
            // bramble bush expanding ring
            if (this.bushWaveOrigin != null && this.self.level() instanceof ServerLevel serverLevel) {
                this.bushWaveTick++;

                double maxRadius = 7.0;
                int durationTicks = 10;
                double currentRadius = Math.min(maxRadius, (this.bushWaveTick / (double) durationTicks) * maxRadius);
                double currentRadiusSq = currentRadius * currentRadius;

                BlockState bushState = ModBlocks.GE_BUSH.defaultBlockState();
                Iterator<BushSpawnData> it = this.pendingBushes.iterator();
                while (it.hasNext()) {
                    BushSpawnData data = it.next();
                    if (data.distSq() <= currentRadiusSq) {
                        BlockPos pos = data.pos();
                        if (serverLevel.getBlockState(pos).canBeReplaced()
                                && !serverLevel.getBlockState(pos).is(ModBlocks.GE_BUSH)
                                && bushState.canSurvive(serverLevel, pos)) {
                            serverLevel.setBlock(pos, bushState, 3);
                            if (serverLevel.getBlockEntity(pos) instanceof GEBushBlockEntity bushBE) {
                                bushBE.setOwner(this.getSelf().getUUID());
                            }
                        }
                        it.remove();
                    }
                }

                if (this.bushWaveTick >= durationTicks) {
                    this.pendingBushes.clear();
                    this.bushWaveOrigin = null;
                    this.bushWaveTick = 0;
                }
            }
            // life sensor scanning for imbued item
            if (this.lifeSensorActive) {
                if (!this.isGuarding() || !PowerTypes.hasStandActive(this.self)) {
                    this.lifeSensorActive = false;
                    this.sensedImbueEntityId = -1;
                } else if (this.self.tickCount % 5 == 0 && this.self instanceof ServerPlayer sp) {
                    int foundId = -1;
                    if (this.activeImbuedItemId != null) {
                        AABB searchBox = this.self.getBoundingBox().inflate(geConfig().lifeSenseImbueRange);
                        for (Entity target : this.self.level().getEntities(this.self, searchBox,
                                e -> e != this.self && !PowerTypes.isInADifferentExistence(e, this.self))) {
                            if (target.distanceToSqr(this.self) <= (geConfig().lifeSenseImbueRange * geConfig().lifeSenseImbueRange)
                                    && isHoldingImbuedItem(target)) {
                                foundId = target.getId();
                                break;
                            }
                        }
                    }
                    if (foundId != this.sensedImbueEntityId) {
                        this.sensedImbueEntityId = foundId;
                        S2CPacketUtil.sendIntPowerDataPacket(sp, LIFE_SENSOR, foundId);
                    }
                }
            }
            //tree spawning
            if (this.treeRoofY > -100.0) {
                AABB checkArea = this.self.getBoundingBox().inflate(0.5, 1.0, 0.5);
                for (BlockWallEntity wall : this.self.level().getEntitiesOfClass(BlockWallEntity.class, checkArea)) {
                    if (wall.isGoldExperienceTree) {
                        this.self.fallDistance = 0.0F;
                        if (this.self.getY() >= this.treeRoofY - 0.5 && this.self.getDeltaMovement().y > 0.0) {
                            this.self.setDeltaMovement(this.self.getDeltaMovement().x, 0.0, this.self.getDeltaMovement().z);
                        }
                    }
                }
            }
            if (!pendingTreeBlocks.isEmpty()) {
                treeSpawnTimer++;

                if (treeSpawnTimer < 0 && this.treeBasePos != null && this.self.level() instanceof ServerLevel serverLevel) {
                    if (treeSpawnTimer % 2 == 0) {
                        BlockPos supportPos = this.treeBasePos.relative(this.treeGrowthDir.getOpposite());
                        BlockState groundState = serverLevel.getBlockState(supportPos);

                        double x = this.treeBasePos.getX() + 0.5 - (this.treeGrowthDir.getStepX() * 0.45);
                        double y = this.treeBasePos.getY() + 0.5 - (this.treeGrowthDir.getStepY() * 0.45);
                        double z = this.treeBasePos.getZ() + 0.5 - (this.treeGrowthDir.getStepZ() * 0.45);

                        double spreadX = this.treeGrowthDir.getAxis() == Direction.Axis.X ? 0.05 : 0.3;
                        double spreadY = this.treeGrowthDir.getAxis() == Direction.Axis.Y ? 0.05 : 0.3;
                        double spreadZ = this.treeGrowthDir.getAxis() == Direction.Axis.Z ? 0.05 : 0.3;

                        sendParticlesIfPossible(serverLevel,
                                ModParticles.VITAL_ESSENCE,
                                x + (this.treeGrowthDir.getStepX() * 0.1),
                                y + (this.treeGrowthDir.getStepY() * 0.1),
                                z + (this.treeGrowthDir.getStepZ() * 0.1),
                                3, spreadX, spreadY, spreadZ, 0.02);

                        if (!groundState.isAir()) {
                            sendParticlesIfPossible(serverLevel,
                                    new BlockParticleOption(ParticleTypes.BLOCK, groundState),
                                    x, y, z,
                                    4, spreadX, spreadY, spreadZ, 0.05);
                        }
                    }
                }

                if (treeSpawnTimer >= 0) {
                    Iterator<TreeBlockSpawnData> iterator = pendingTreeBlocks.iterator();
                    while (iterator.hasNext()) {
                        TreeBlockSpawnData data = iterator.next();

                        if (treeSpawnTimer >= data.delayTicks()) {
                            BlockWallEntity treePart = new BlockWallEntity(
                                    this.self.level(),
                                    data.startPos().x(),
                                    data.startPos().y(),
                                    data.startPos().z(),
                                    data.state()
                            );

                            treePart.setDataFinalPos(data.finalPos());
                            //despawn timer of 20 seconds
                            treePart.timing = Math.max(1, 400 - data.delayTicks());
                            treePart.isWhiteAlbumWall = false;
                            if (data.isTrunk()) {
                                treePart.tsmove = true;
                            }
                            treePart.isGoldExperienceTree = true;
                            treePart.creator = this.getSelf();
                            PowerTypes.copyPlaneOfExisting(this.self, treePart);

                            this.self.level().addFreshEntity(treePart);
                            iterator.remove();
                        }
                    }
                    if (pendingTreeBlocks.isEmpty()) {
                        this.treeBasePos = null;
                    }
                }
            }
            // bramble staggered deploy time
            if (!pendingBrambles.isEmpty()) {
                brambleSpawnTimer++;
                if (brambleSpawnTimer >= 2) { // 2 ticks = 0.1 seconds
                    brambleSpawnTimer = 0;
                    BrambleSpawnData data = pendingBrambles.remove(0);

                    GEBrambleEntity bramble = new GEBrambleEntity(ModEntities.GE_BRAMBLE, this.self.level());
                    bramble.setOwner(this.getSelf());
                    bramble.setPos(data.pos().x, data.pos().y, data.pos().z);
                    bramble.setYRot(data.yRot());

                    this.self.level().addFreshEntity(bramble);
                }
            }
            // life punch stuff
            tickLifePunch();
        }
        // client side stuff
        else {
            if (this.lifeSensorActive && (!this.isGuarding() || !PowerTypes.hasStandActive(this.self))) {
                this.lifeSensorActive = false;
                this.sensedImbueEntityId = -1;
                this.setCooldown(PowerIndex.GENERAL_1, getLifeSensorCooldown());
            }
        }
        super.tickPower();
    }

    // tick power end

    /**
     * Gets the input for the power based on the buttons pressed.
     * Note, this is client side only. DO NOT FORGET!!!!
     *
     * @param context What button combination was pressed.
     */
    @Override
    public void powerActivate(PowerContext context) {
        switch (context) {
            case SKILL_1_NORMAL -> {
                if(canDNACreate()) {
                    tryDNACreate();
                } else {
                    tryOpenAnimalSelect();
                }
            }
            // heal
            case SKILL_1_CROUCH -> {
                tryHeal();
            }
            case SKILL_1_GUARD, SKILL_1_CROUCH_GUARD -> {
                tryLifeSensor();
            }
            // lifeform creation
            case SKILL_2_NORMAL -> {
                tryMakeAnimalClient();
            }
            // tree creation
            case SKILL_2_GUARD, SKILL_2_CROUCH_GUARD -> {
                tryGrowTree();
            }
            // item throw
            case SKILL_2_CROUCH -> {
                itemGrabClient();
            }
            // dash or vine climb
            case SKILL_3_NORMAL -> {
                tryToDashClient();
            }
            // life punch
            case SKILL_3_CROUCH -> {
                tryLifePunch();
            }
            // bramble spike thing
            case SKILL_4_NORMAL -> {
                tryGrowBramble();
            }
            // bush growth move
            case SKILL_4_GUARD, SKILL_4_CROUCH_GUARD -> {
                tryGrowBush();
            }
            // item imbue
            case SKILL_4_CROUCH -> {
                tryOpenItemImbue();
            }
        }
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == REQUIEM) {
            //inventoryGrab();
            return setRequiem();
        }

        if (move == LIFE_PUNCH) {
            return startLifePunch();
        }
        else if (move == DNA_CREATE) {
            return executeDNACreate();
        }
        else if (move == PowerIndex.BARRAGE_CHARGE_2) {
            return this.setPowerKickBarrageCharge();
        } else if (move == PowerIndex.BARRAGE_2) {
            return this.setPowerKickBarrage();
        }
        else if (move == BUSH_CREATION) {
            return startBushWindup();
        }
        else if (move == WALL_VINES) {
            return executeWallVines();
        }
        else if (move == HEAL_CHARGE) {
            return startHealCharge();
        }
        else if (move == HEAL) {
            return startHealRelease();
        }
        else if (move == BRAMBLES) {
            return startBrambleWindup();
        }
        else if (move == LIFEFORM_CREATION) {
            return executeLifeformCreation();
        }
        else if (move == PowerIndex.SNEAK_ATTACK) {
            return executeDualPunch();
        }
        else if (move == TREE) {
            return startTreeWindup();
        }
        else if (move == LIFE_SENSOR) {
            if (!this.self.level().isClientSide() && this.isGuarding()) {
                this.lifeSensorActive = true;
            }
            return false;
        }
        return super.setPowerOther(move, lastMove);
    }

    /* note: passedInt could be anything, it could be charge time, etc. for the purposes of life punch, for example
    passedInt could be what animal you're making
    */
    @Override
    public boolean tryIntPower(int move, boolean forced, int passedInt){
        if (move == LIFEFORM_CREATION || move == ITEM_THROW) {
            this.animalType = (byte) passedInt;
            if (move == ITEM_THROW) {
                return false;
            }
        }
        else if (move == ITEM_IMBUE) {
            byte imbueType = (byte) ((passedInt >> 8) & 0xFF);
            byte animalId = (byte) (passedInt & 0xFF);
            return executeItemImbue(imbueType, animalId);
        }
        else if (move == HEAL_CHARGE) {
            this.healOthers = (passedInt == 1);
            return false;
        } else if (move == REQUIEM) {
            grabInventorySlot = passedInt;
        }


        return super.tryIntPower(move, forced, passedInt);
    }

    @Override
    public void updatePowerInt(byte activePower, int data) {
        if (activePower == LIFE_SENSOR) {
            this.sensedImbueEntityId = data;
            return;
        }
        if (activePower == PowerIndex.SPECIAL_CHARGED) {
            resolveRenderTicks = 50;
            return;
        }
        super.updatePowerInt(activePower, data);
    }

    @Override
    public void updateUniqueMoves() {
        if (this.getActivePower() == LIFE_PUNCH) {
            updateLifePunch();
        }
        // dual punch
        if (this.getActivePower() == PowerIndex.SNEAK_ATTACK) {
            updateDualPunch();
        }
        //kick barrage
        if (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2) {
            updateKickBarrageCharge();
        } else if (this.getActivePower() == PowerIndex.BARRAGE_2) {
            updateKickBarrage();
        }
        // heal
        if (this.getActivePower() == HEAL_CHARGE) {
            updateHealCharge();
        } else if (this.getActivePower() == HEAL) {
            updateHeal();
        }
        // bramble
        if (this.getActivePower() == BRAMBLES) {
            updateBramble();
        }
        // bush
        if (this.getActivePower() == BUSH_CREATION) {
            updateBush();
        }
        // tree
        if (this.getActivePower() == TREE) {
            updateTree();
        }

        // this is requiem...
        if (this.getActivePower() == REQUIEM) {
            updateRequiem();
        }
        super.updateUniqueMoves();
    }

    public boolean isAttackInept(byte activeP){
        if (activeP == REQUIEM) {

            return this.isDazed(this.self) || (((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()));
        }
        return super.isAttackInept(activeP);
    }

    @Override
    public void updateMovesFromPacket(byte activePower){
        if (activePower == PowerIndex.BARRAGE_2){
            this.setActivePowerPhase(this.activePowerPhaseMax);
        }
        super.updateMovesFromPacket(activePower);
    }

    @Override
    public void handleStandAttack(Player player, Entity target) {
        if (this.getActivePower() == LIFE_PUNCH) {
            lifePunchImpact(target);
        }
        else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK) {
            boolean isSecondHit = this.attackTimeDuring >= 10;
            dualPunchImpact(target, isSecondHit);
        }
    }

    private void tryToDashClient() {
        if (canLatchOntoWall()) {
                tryWallVines();
        } else {
            dash();
        }
    }

    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (!consumeClickInput) {
            if (activePower == REQUIEM) {return; }
            if (impaleTicks > 0){
                return;
            }
            if (keyIsDown) {
                if (!isHoldingSneak()) {
                    super.buttonInputAttack(keyIsDown, options);
                } else {
                    if (this.canAttack()) {
                        this.tryPower(PowerIndex.SNEAK_ATTACK, true);
                        tryPowerPacket(PowerIndex.SNEAK_ATTACK);
                    } else {
                        super.buttonInputAttack(keyIsDown, options);
                    }
                }
            }
        }
        else {
            if (!keyIsDown) {
                consumeClickInput = false;
            }
        }
    }

    @Override
    public boolean setPowerNone() {
        this.groundBarrageTicks = 0;
        this.isGroundBarraging = false;
        this.holdDownHeal = false;
        return super.setPowerNone();
    }

    //server controller cooldowns
    @Override
    public boolean isServerControlledCooldown(byte num) {
        if (num == PowerIndex.GENERAL_EXTRA || num == PowerIndex.SKILL_1_SNEAK || num == PowerIndex.SKILL_4_SNEAK
                || num == PowerIndex.SKILL_3 || num == PowerIndex.GENERAL_2 || num == PowerIndex.SKILL_4 || num == PowerIndex.GENERAL_4) {
            return true;
        }
        return super.isServerControlledCooldown(num);
    }

    // lets animations happen while blocking
    @Override
    public boolean buttonInputGuard(boolean keyIsDown, Options options) {
        if (impaleTicks > 0 || this.activePower == REQUIEM) {
            return false;
        }
        if (this.activePower == TREE || this.activePower == BUSH_CREATION
                || this.activePower == PowerIndex.BARRAGE_CHARGE_2 || this.activePower == PowerIndex.BARRAGE_2) {
            return false;
        }
        return super.buttonInputGuard(keyIsDown, options);
    }

    // item interactions (lifeform creation) start

    private static Vec3 getRevertBoost(Level level, Vec3 pos, LivingEntity entity, LivingEntity owner, double speed) {
        double range = 10.0;
        AABB box = new AABB(pos, pos).inflate(range);
        LivingEntity closest = null;
        double closestDistSq = range * range;

        for (LivingEntity candidate : level.getEntitiesOfClass(LivingEntity.class, box)) {
            if (candidate == entity || candidate == owner || !candidate.isAlive()
                    || candidate instanceof StandEntity || candidate instanceof ArmorStand
                    || PowerTypes.isInADifferentExistence(candidate, owner)) {
                continue;
            }
            double distSq = candidate.distanceToSqr(pos);
            if (distSq < closestDistSq) {
                closestDistSq = distSq;
                closest = candidate;
            }
        }

        if (closest != null) {
            Vec3 targetPos = closest.position().add(0, closest.getBbHeight() * 0.5, 0);
            Vec3 dir = targetPos.subtract(pos);
            if (dir.lengthSqr() > 1.0E-4) {
                return dir.normalize().scale(speed).add(0, 0.08, 0);
            }
        }
        return new Vec3(0, -0.1, 0);
    }

    public static void handleLifeformRevertItem(LivingEntity entity, GELifeform lifeform, ItemStack sourceStack) {
        Level level = entity.level();
        Vec3 pos = entity.position().add(0, 0.25, 0);
        LivingEntity creator = lifeform.getCreator();
        LivingEntity owner = creator != null ? creator : entity;
        BlockPos blockPos = entity.blockPosition();

        // copied from ThrownObjectEntity, this is basically just one long "if else" chain that defines what an object should do when reverting
        if (sourceStack.is(Items.TNT)) {
            PrimedTnt tnt = new PrimedTnt(
                    level, pos.x, pos.y, pos.z, creator
            );
            tnt.setFuse(80);
            level.addFreshEntity(tnt);
            level.playSound(null, tnt.getX(), tnt.getY(), tnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            return;
        } else if (sourceStack.getItem() instanceof ThrowablePotionItem) {
            ThrownPotion potion = new ThrownPotion(level, owner);
            potion.setPos(pos);
            potion.setItem(sourceStack.copy());
            potion.setDeltaMovement(getRevertBoost(level, pos, entity, owner, 0.55));
            level.addFreshEntity(potion);
            return;
        } else if (sourceStack.getItem() instanceof SnowballItem) {
            Snowball snowball = new Snowball(level, owner);
            snowball.setPos(pos);
            snowball.setItem(sourceStack.copy());
            snowball.setDeltaMovement(getRevertBoost(level, pos, entity, owner, 0.65));
            level.addFreshEntity(snowball);
            return;
        } else if (sourceStack.is(ModItems.KNIFE)) {
            KnifeEntity knife = new KnifeEntity(level, owner, sourceStack.copy());
            knife.setPos(pos);
            knife.setDeltaMovement(getRevertBoost(level, pos, entity, owner, 0.75));
            level.addFreshEntity(knife);
            return;
        } else if (sourceStack.is(ModItems.KNIFE_BUNDLE)) {
            Vec3 baseBoost = getRevertBoost(level, pos, entity, owner, 0.75);
            PowersGoldExperience gePowers = ((StandUser) owner).roundabout$getStandPowers() instanceof PowersGoldExperience ge ? ge : null;

            for (int i = 0; i < 4; i++) {
                KnifeEntity knife = new KnifeEntity(level, owner, sourceStack.copy());
                knife.setPos(pos);
                knife.setDeltaMovement(baseBoost.add(
                        (level.random.nextDouble() - 0.5) * 0.14,
                        (level.random.nextDouble() - 0.5) * 0.08,
                        (level.random.nextDouble() - 0.5) * 0.14
                ));
                if (gePowers != null) {
                    // 1 second delay
                    gePowers.pendingKnives.add(new KnifeSpawnData(knife, 20));
                } else {
                    // fallback
                    level.addFreshEntity(knife);
                }
            }
            return;
        } else if (sourceStack.is(ModItems.GASOLINE_CAN)) {
            GasolineCanEntity gasCan = new GasolineCanEntity(owner, level);
            gasCan.setPos(pos);
            gasCan.setDeltaMovement(getRevertBoost(level, pos, entity, owner, 0.45));
            level.addFreshEntity(gasCan);
            return;
        } else if (sourceStack.is(ModItems.MATCH)) {
            MatchEntity match = new MatchEntity(owner, level);
            match.setPos(pos);
            match.setDeltaMovement(getRevertBoost(level, pos, entity, owner, 0.55));
            level.addFreshEntity(match);
            return;
        } else if (sourceStack.is(ModItems.MATCH_BUNDLE)) {
            Vec3 baseBoost = getRevertBoost(level, pos, entity, owner, 0.55);
            for (int i = 0; i < 4; i++) {
                MatchEntity match = new MatchEntity(owner, level);
                match.setPos(pos);
                match.setDeltaMovement(baseBoost.add(
                        (level.random.nextDouble() - 0.5) * 0.14,
                        (level.random.nextDouble() - 0.5) * 0.08,
                        (level.random.nextDouble() - 0.5) * 0.14
                ));
                level.addFreshEntity(match);
            }
            return;
        } else if ((sourceStack.getItem() instanceof StandArrowItem && !(sourceStack.getDamageValue() >= sourceStack.getMaxDamage()))
                || sourceStack.getItem() instanceof WorthyArrowItem) {
            StandArrowEntity arrow = new StandArrowEntity(level, owner, sourceStack.copy());
            arrow.setPos(pos);
            arrow.setDeltaMovement(getRevertBoost(level, pos, entity, owner, 0.75));
            level.addFreshEntity(arrow);
            return;
        } else if (sourceStack.getItem() instanceof EggItem) {
            ThrownEgg egg = new ThrownEgg(level, owner);
            egg.setPos(pos);
            egg.setItem(sourceStack.copy());
            egg.setDeltaMovement(getRevertBoost(level, pos, entity, owner, 0.65));
            level.addFreshEntity(egg);
            return;
        } else if (sourceStack.getItem() instanceof ExperienceBottleItem) {
            ThrownExperienceBottle xpBottle = new ThrownExperienceBottle(level, owner);
            xpBottle.setPos(pos);
            xpBottle.setItem(sourceStack.copy());
            xpBottle.setDeltaMovement(0, -0.1, 0);
            level.addFreshEntity(xpBottle);
            return;
        } else if (sourceStack.getItem() instanceof EnderpearlItem) {
            ThrownEnderpearl pearl = new ThrownEnderpearl(level, owner);
            pearl.setPos(pos);
            pearl.setDeltaMovement(0, -0.1, 0);
            level.addFreshEntity(pearl);
            return;
        } else if (sourceStack.getItem() instanceof ArrowItem arrowItem && !(sourceStack.getItem() instanceof RoundaboutArrowItem)) {
            AbstractArrow arrow = arrowItem.createArrow(level, sourceStack.copy(), owner);
            arrow.setPos(pos);
            arrow.setDeltaMovement(getRevertBoost(level, pos, entity, owner, 0.75));
            level.addFreshEntity(arrow);
            return;
        } else if (sourceStack.is(Items.TRIDENT)) {
            if (!sourceStack.hurt(1, level.getRandom(), null)) {
                ThrownTrident trident = new ThrownTrident(level, owner, sourceStack.copy());
                trident.setPos(pos);
                trident.setDeltaMovement(getRevertBoost(level, pos, entity, owner, 0.75));
                level.addFreshEntity(trident);
            }
            return;
        } else if (sourceStack.getItem() instanceof HarpoonItem) {
            if (!sourceStack.hurt(1, level.getRandom(), null)) {
                HarpoonEntity harpoon = new HarpoonEntity(level, owner, sourceStack.copy());
                harpoon.setPos(pos);
                harpoon.setDeltaMovement(getRevertBoost(level, pos, entity, owner, 0.75));
                level.addFreshEntity(harpoon);
            }
            return;
        } else if (sourceStack.getItem() instanceof BlockItem blockItem && !(blockItem instanceof SolidBucketItem)
                && !(blockItem.getBlock() instanceof SnowLayerBlock)) {
            Vec3 center = entity.getBoundingBox().getCenter();
            BlockPos placePos = BlockPos.containing(center);

            if (!level.getBlockState(placePos).canBeReplaced()) {
                placePos = blockPos;
            }
            if (!level.getBlockState(placePos).canBeReplaced()) {
                placePos = placePos.above();
            }

            boolean hasSupport = false;
            for (Direction dir : Direction.values()) {
                BlockPos neighborPos = placePos.relative(dir);
                if (level.getBlockState(neighborPos).isFaceSturdy(level, neighborPos, dir.getOpposite())) {
                    hasSupport = true;
                    break;
                }
            }

            if (hasSupport) {
                // orientation fix maybe
                Vec3 vel = entity.getDeltaMovement();
                Direction hitFace = Direction.UP;
                if (entity.verticalCollision) {
                    hitFace = vel.y > 0 ? Direction.DOWN : Direction.UP;
                } else if (entity.horizontalCollision) {
                    hitFace = entity.getDirection().getOpposite();
                }

                Direction placeDir = entity.getDirection();
                if (placeDir.getAxis() == Direction.Axis.X) {
                    placeDir = placeDir.getOpposite();
                }
                if (blockItem.getBlock() instanceof RotatedPillarBlock) {
                    placeDir = hitFace;
                }

                // prevents blocks from not being placed due to entity hitbox
                Vec3 originalPos = entity.position();
                entity.setPos(originalPos.x, originalPos.y + 100.0D, originalPos.z);

                BlockState candidateState = level.getBlockState(placePos);
                if ((candidateState.isAir() || candidateState.canBeReplaced())
                        && MainUtil.tryPlaceBlock(owner, placePos)) {
                    ItemStack placeStack = sourceStack.copy();
                    if (blockItem.place(new DirectionalPlaceContext(level, placePos, placeDir, placeStack, placeDir)) != InteractionResult.FAIL) {
                        entity.setPos(originalPos.x, originalPos.y, originalPos.z);
                        return;
                    }
                }

                entity.setPos(originalPos.x, originalPos.y, originalPos.z);
            }
        } else if (sourceStack.getItem() instanceof BoneMealItem) {
            ItemStack mealStack = sourceStack.copy();
            BlockPos targetPos = !level.getBlockState(blockPos).isAir() ? blockPos : blockPos.below();
            if (BoneMealItem.growCrop(mealStack, level, targetPos)) {
                level.levelEvent(1505, targetPos, 0);
                return;
            }
            BlockState state = level.getBlockState(targetPos);
            if (state.isFaceSturdy(level, targetPos, Direction.UP)
                    && BoneMealItem.growWaterPlant(mealStack, level, targetPos.above(), Direction.UP)) {
                level.levelEvent(1505, targetPos.above(), 0);
                return;
            }
        } else if (sourceStack.getItem() instanceof HangingEntityItem hangingItem) {
            Direction facing = entity.getDirection();
            BlockPos wallPos = blockPos.relative(facing.getOpposite());
            ItemStack hangStack = sourceStack.copy();
            if (hangingItem.useOn(new DirectionalPlaceContext(level, wallPos, facing, hangStack, facing)) != InteractionResult.FAIL) {
                return;
            }
        } else if (sourceStack.getItem() instanceof SpawnEggItem spawnEgg) {
            BlockPos spawnPos = level.getBlockState(blockPos).canBeReplaced() ? blockPos.below() : blockPos;
            ItemStack eggStack = sourceStack.copy();
            if (spawnEgg.useOn(new DirectionalPlaceContext(level, spawnPos, Direction.UP, eggStack, Direction.UP)) != InteractionResult.FAIL) {
                return;
            }
        } else if (sourceStack.getItem() instanceof RecordItem) {
            BlockPos[] checks = new BlockPos[]{blockPos, blockPos.below()};
            for (BlockPos checkPos : checks) {
                BlockEntity be = level.getBlockEntity(checkPos);
                if (be instanceof JukeboxBlockEntity je && je.getFirstItem().isEmpty()) {
                    je.setItem(0, sourceStack.copy());
                    return;
                } else if (be instanceof StereoBlockEntity se && se.getFirstItem().isEmpty()) {
                    se.setItem(0, sourceStack.copy());
                    return;
                }
            }
        } else if (sourceStack.getItem() instanceof SwordItem || sourceStack.getItem() instanceof DiggerItem) {
            //weapon damage
            float weaponDamage = 1.0F;
            if (sourceStack.getItem() instanceof SwordItem sword) {
                weaponDamage = sword.getDamage() + 1.0F;
            } else if (sourceStack.getItem() instanceof DiggerItem digger) {
                weaponDamage = digger.getAttackDamage() + 1.0F;
            }

            //hits entities if close enough
            boolean hitAny = false;
            AABB hitBox = entity.getBoundingBox().inflate(0.8);
            for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, hitBox)) {
                if (target == entity || target == owner || !target.isAlive()
                        || target instanceof StandEntity || target instanceof ArmorStand
                        || PowerTypes.isInADifferentExistence(target, owner)) {
                    continue;
                }
                DamageSource dmgSource = owner instanceof Player player
                        ? level.damageSources().playerAttack(player)
                        : level.damageSources().mobAttack(owner);
                if (target.hurt(dmgSource, weaponDamage)) {
                    hitAny = true;
                }
            }

            //for hits
            if (hitAny) {
                level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.PLAYERS, 0.85F, 1.0F);
                sourceStack.hurt(1, level.getRandom(), null);
            }
            // mining stuff if it's a mining tool
            else if (sourceStack.getItem() instanceof DiggerItem) {
                BlockPos[] checks = new BlockPos[]{
                        blockPos,
                        blockPos.below(),
                        blockPos.relative(entity.getDirection()),
                        blockPos.above()
                };
                for (BlockPos checkPos : checks) {
                    BlockState state = level.getBlockState(checkPos);
                    if (!state.isAir()
                            && state.getDestroySpeed(level, checkPos) >= 0
                            && sourceStack.isCorrectToolForDrops(state)) {
                        // thank you king nothing dev guy
                        BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(checkPos) : null;
                        if (level.destroyBlock(checkPos, false, owner)) {
                            boolean isCreative = owner instanceof Player player && player.getAbilities().instabuild;
                            if (!isCreative) {
                                Block.dropResources(state, level, checkPos, blockEntity, owner, sourceStack);
                            }
                            sourceStack.hurt(1, level.getRandom(), null);
                            break;
                        }
                    }
                }
            }
        } else if (sourceStack.getItem() instanceof FireChargeItem) {
            level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (level.random.nextFloat() - level.random.nextFloat()) * 0.2F + 1.0F);
            int radius = 2;
            for (int x = -radius; x <= radius; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        if (x * x + z * z > radius * radius) continue;
                        BlockPos firePos = blockPos.offset(x, y, z);
                        BlockState state = level.getBlockState(firePos);
                        if ((state.isAir() || state.canBeReplaced())
                                && !state.liquid()
                                && MainUtil.tryPlaceBlock(owner, firePos)
                                && MainUtil.getIsGamemodeApproriateForGrief(owner)) {
                            BlockState fireState = BaseFireBlock.getState(level, firePos);
                            if (fireState.canSurvive(level, firePos)) {
                                level.setBlockAndUpdate(firePos, fireState);
                            }
                        }
                    }
                }
            }

            AABB fireBox = entity.getBoundingBox().inflate(2.0);
            for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, fireBox)) {
                if (target == entity || target == owner || !target.isAlive()
                        || target instanceof StandEntity || target instanceof ArmorStand
                        || PowerTypes.isInADifferentExistence(target, owner)) {
                    continue;
                }
                target.setSecondsOnFire(5);
            }
            return;
        }
        // look at SoftAndWetPlunderBubbleEntity for more info on splashing buckets
        else if (sourceStack.is(Items.WATER_BUCKET)) {
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        new BlockParticleOption(ParticleTypes.BLOCK, Blocks.WATER.defaultBlockState()),
                        pos.x, pos.y + 0.25, pos.z,
                        80, 1.0, 1.0, 1.0, 0.4
                );
            }
            level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 1.0F, 1.5F);

            AABB splashBox = entity.getBoundingBox().inflate(4.0);
            for (Entity target : level.getEntities(entity, splashBox)) {
                if (target instanceof LivingEntity living) {
                    if (((StandUser) living).roundabout$isOnStandFire()) {
                        ((StandUser) living).roundabout$setRemainingStandFireTicks(0);
                    }
                }
                if (target.isOnFire()) {
                    target.clearFire();
                }
            }
            int radius = 3;
            for (int x = -radius; x <= radius; x++) {
                for (int y = -1; y <= 2; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        if (x * x + z * z > radius * radius) continue;
                        BlockPos firePos = blockPos.offset(x, y, z);
                        BlockState state = level.getBlockState(firePos);
                        if (state.getBlock() instanceof BaseFireBlock || state.getBlock() instanceof StandFireBlock) {
                            level.removeBlock(firePos, false);
                        }
                    }
                }
            }
            entity.spawnAtLocation(new ItemStack(Items.BUCKET));
            return;
        } else if (sourceStack.is(Items.LAVA_BUCKET)) {
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        new BlockParticleOption(ParticleTypes.BLOCK, Blocks.LAVA.defaultBlockState()),
                        pos.x, pos.y + 0.25, pos.z,
                        80, 1.0, 1.0, 1.0, 0.4
                );
            }
            level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 1.0F, 1.5F);

            AABB splashBox = entity.getBoundingBox().inflate(4.0);
            for (Entity target : level.getEntities(entity, splashBox)) {
                if (target == owner || target instanceof StandEntity || target instanceof ArmorStand
                        || PowerTypes.isInADifferentExistence(target, owner)) {
                    continue;
                }
                target.setRemainingFireTicks(180 + target.getRemainingFireTicks());
            }
            entity.spawnAtLocation(new ItemStack(Items.BUCKET));
            return;
        } else if (sourceStack.is(Items.MILK_BUCKET)) {
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        new BlockParticleOption(ParticleTypes.BLOCK, Blocks.WHITE_CONCRETE_POWDER.defaultBlockState()),
                        pos.x, pos.y + 0.25, pos.z,
                        80, 1.0, 1.0, 1.0, 0.4
                );
            }
            level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 1.0F, 1.5F);

            AABB splashBox = entity.getBoundingBox().inflate(3.0);
            for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, splashBox)) {
                if (target == entity || target instanceof StandEntity || target instanceof ArmorStand
                        || PowerTypes.isInADifferentExistence(target, owner)) {
                    continue;
                }
                List<MobEffectInstance> preserved = new ArrayList<>();
                for (MobEffectInstance effect : target.getActiveEffects()) {
                    if (MainUtil.isSpecialEffect(effect)) {
                        preserved.add(effect);
                    }
                }
                target.removeAllEffects();
                for (MobEffectInstance effect : preserved) {
                    target.addEffect(effect);
                }
            }
            entity.spawnAtLocation(new ItemStack(Items.BUCKET));
            return;
        }

        // default revert to item
        entity.spawnAtLocation(sourceStack);
    }

    // item interactions (lifeform creation) end

    // item imbue tree interactions start

    public static boolean isProjectileImbueItem(ItemStack stack) {
        if (stack.isEmpty()) return false;
        Item item = stack.getItem();
        return item instanceof ArrowItem
                || item instanceof SnowballItem
                || item instanceof EggItem
                || item instanceof AmmoItem
                || stack.is(ModItems.KNIFE)
                || stack.is(ModItems.KNIFE_BUNDLE)
                || stack.is(ModItems.MATCH)
                || stack.is(ModItems.MATCH_BUNDLE);
    }

    public static boolean isTreeImbuedProjectileItem(ItemStack stack) {
        CompoundTag imbue = getImbueData(stack);
        return imbue != null && imbue.getByte("Type") == IMBUE_TREE && isProjectileImbueItem(stack);
    }

    public static void transferTreeImbueToProjectile(Level level, ItemStack sourceStack, Projectile projectile) {
        CompoundTag imbue = getImbueData(sourceStack);
        if (imbue == null || imbue.getByte("Type") != IMBUE_TREE) return;

        if (!level.isClientSide() && level instanceof ServerLevel serverLevel
                && imbue.hasUUID("Id") && imbue.hasUUID("Owner")) {
            UUID imbueId = imbue.getUUID("Id");
            UUID ownerId = imbue.getUUID("Owner");
            Entity ownerEntity = serverLevel.getEntity(ownerId);
            if (ownerEntity instanceof LivingEntity ownerLiving
                    && ((StandUser) ownerLiving).roundabout$getStandPowers() instanceof PowersGoldExperience gePowers
                    && imbueId.equals(gePowers.getActiveImbuedItemId())) {
                ((IGEProjectile) projectile).roundabout$setGETreeImbue(true);
                ((IGEProjectile) projectile).roundabout$setGETreeImbueOwner(ownerId);
                gePowers.setActiveImbuedItemId(null);
            }
        }
        clearImbue(sourceStack);
    }

    public static void applyTreeImbueOnProjectileHit(Projectile projectile, Entity target) {
        if (projectile.level().isClientSide() || !(target instanceof LivingEntity victim)) return;
        if (!((IGEProjectile) projectile).roundabout$hasGETreeImbue()) return;

        ((IGEProjectile) projectile).roundabout$setGETreeImbue(false);

        if (victim instanceof StandUser su) {
            if (!su.roundabout$getGERootedItem().isEmpty()) {
                victim.spawnAtLocation(su.roundabout$getGERootedItem());
            }
            su.roundabout$setGERootedItem(ItemStack.EMPTY);
            su.roundabout$setGERooted(true);
            victim.level().playSound(null, victim.blockPosition(),
                    ModSounds.GOLD_EXPERIENCE_LIFE_CREATION_EVENT, SoundSource.PLAYERS, 0.85F, 1.0F);
        }
    }

    public static void applyTreeImbueOnBlockHit(Projectile projectile, BlockHitResult result) {
        if (projectile.level().isClientSide() || !(projectile.level() instanceof ServerLevel serverLevel)) return;
        if (!(projectile instanceof IGEProjectile geProj) || !geProj.roundabout$hasGETreeImbue()) return;

        geProj.roundabout$setGETreeImbue(false);

        PowersGoldExperience gePowers = null;
        UUID ownerId = geProj.roundabout$getGETreeImbueOwner();
        if (ownerId != null && serverLevel.getEntity(ownerId) instanceof LivingEntity imbuer
                && ((StandUser) imbuer).roundabout$getStandPowers() instanceof PowersGoldExperience powers) {
            gePowers = powers;
        } else if (projectile.getOwner() instanceof LivingEntity shooter
                && ((StandUser) shooter).roundabout$getStandPowers() instanceof PowersGoldExperience powers) {
            gePowers = powers;
        }

        if (gePowers == null) return;

        BlockPos hitPos = result.getBlockPos();
        Direction face = result.getDirection();
        BlockPos placePos = hitPos.relative(face);

        if (face == Direction.UP) {
            gePowers.growGETreeAt(placePos, false);
        } else if (face.getAxis().isHorizontal()) {
            gePowers.growGEWallBranchAt(placePos, face);
        }
    }

    // item imbue tree interactions end

    // item imbue disguise interactions start

    public static void handleDisguiseRevertItem(LivingEntity holder, ItemStack stack, PowersGoldExperience gePowers) {
        Level level = holder.level();
        Vec3 eyePos = holder.getEyePosition();
        Vec3 bodyPos = holder.position().add(0, holder.getBbHeight() * 0.5, 0);
        boolean isCreative = holder instanceof Player p && p.isCreative();

        DamageSource dmgSource = gePowers.getSelf() instanceof Player gePlayer ? level.damageSources().playerAttack(gePlayer) : level.damageSources().mobAttack(gePowers.getSelf());

        if (stack.getItem() instanceof FirearmItem firearm && firearm.getAmmo(stack) > 0) {
            //bro shot himself :laughing:
            if (!isCreative) {
                firearm.setAmmo(stack, firearm.getAmmo(stack) - 1);
            }

            level.playSound(null, holder.blockPosition(), firearm.getFireSound(), SoundSource.PLAYERS,
                    firearm.getFireSoundVolume(), firearm.getFireSoundPitch());

            if (level instanceof ServerLevel serverLevel) {
                Vec3 flashPos = eyePos.add(holder.getLookAngle().scale(0.45));
                serverLevel.sendParticles(ModParticles.FLASH, flashPos.x, flashPos.y, flashPos.z,
                        0, 255, 210, 17, 1.0);
                serverLevel.sendParticles(ParticleTypes.SMOKE, flashPos.x, flashPos.y, flashPos.z,
                        8, 0.1, 0.1, 0.1, 0.03);
            }

            holder.hurt(dmgSource, 8.0F);
            return;
        }
        else if (stack.getItem() instanceof SwordItem || stack.getItem() instanceof DiggerItem) {
            float weaponDamage = 1.0F;
            if (stack.getItem() instanceof SwordItem sword) {
                weaponDamage = sword.getDamage() + 1.0F;
            } else if (stack.getItem() instanceof DiggerItem digger) {
                weaponDamage = digger.getAttackDamage() + 1.0F;
            }

            level.playSound(null, holder.blockPosition(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.PLAYERS, 0.9F, 1.0F);
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, bodyPos.x, bodyPos.y, bodyPos.z,
                        1, 0.0, 0.0, 0.0, 0.0);
            }

            if (holder.hurt(dmgSource, weaponDamage) && !isCreative) {
                stack.hurt(1, level.getRandom(), null);
            }
            return;
        }
        else if (stack.is(Items.TNT)) {
            if (!isCreative) {
                stack.shrink(1);
            }
            PrimedTnt tnt = new PrimedTnt(level, holder.getX(), holder.getY(), holder.getZ(), gePowers.getSelf());
            tnt.setFuse(40);
            level.addFreshEntity(tnt);
            level.playSound(null, tnt.getX(), tnt.getY(), tnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            return;
        }
        else if (stack.getItem() instanceof FireChargeItem) {
            if (!isCreative) {
                stack.shrink(1);
            }
            level.playSound(null, holder.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS,
                    1.0F, (level.random.nextFloat() - level.random.nextFloat()) * 0.2F + 1.0F);
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.FLAME, bodyPos.x, bodyPos.y, bodyPos.z,
                        20, 0.3, 0.4, 0.3, 0.05);
            }
            holder.setSecondsOnFire(5);
            holder.hurt(level.damageSources().onFire(), 3.0F);
            return;
        }
        else if (stack.is(Items.WATER_BUCKET)) {
            if (!isCreative) {
                replaceWithEmptyBucket(holder, stack);
            }
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        new BlockParticleOption(ParticleTypes.BLOCK, Blocks.WATER.defaultBlockState()),
                        bodyPos.x, bodyPos.y, bodyPos.z,
                        50, 0.4, 0.6, 0.4, 0.25);
            }
            level.playSound(null, holder.blockPosition(), SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 1.0F, 1.3F);

            if (((StandUser) holder).roundabout$isOnStandFire()) {
                ((StandUser) holder).roundabout$setRemainingStandFireTicks(0);
            }
            if (holder.isOnFire()) {
                holder.clearFire();
            }
            return;
        }
        else if (stack.is(Items.LAVA_BUCKET)) {
            if (!isCreative) {
                replaceWithEmptyBucket(holder, stack);
            }
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        new BlockParticleOption(ParticleTypes.BLOCK, Blocks.LAVA.defaultBlockState()),
                        bodyPos.x, bodyPos.y, bodyPos.z,
                        50, 0.4, 0.6, 0.4, 0.25);
            }
            level.playSound(null, holder.blockPosition(), SoundEvents.BUCKET_EMPTY_LAVA, SoundSource.PLAYERS, 1.0F, 1.1F);
            holder.setRemainingFireTicks(180 + holder.getRemainingFireTicks());
            holder.hurt(level.damageSources().lava(), 4.0F);
            return;
        }
        else if (stack.is(Items.MILK_BUCKET)) {
            if (!isCreative) {
                replaceWithEmptyBucket(holder, stack);
            }
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        new BlockParticleOption(ParticleTypes.BLOCK, Blocks.WHITE_CONCRETE_POWDER.defaultBlockState()),
                        bodyPos.x, bodyPos.y, bodyPos.z,
                        50, 0.4, 0.6, 0.4, 0.25);
            }
            level.playSound(null, holder.blockPosition(), SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 1.3F, 1.3F);

            List<MobEffectInstance> preserved = new ArrayList<>();
            for (MobEffectInstance effect : holder.getActiveEffects()) {
                if (MainUtil.isSpecialEffect(effect)) {
                    preserved.add(effect);
                }
            }
            holder.removeAllEffects();
            for (MobEffectInstance effect : preserved) {
                holder.addEffect(effect);
            }
            return;
        }
        else if (stack.is(ModItems.GASOLINE_CAN)) {
            if (!isCreative) {
                stack.shrink(1);
            }
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.GASOLINE_SPLATTER.defaultBlockState()),
                        bodyPos.x, bodyPos.y, bodyPos.z,
                        35, 0.4, 0.5, 0.4, 0.2);
            }
            level.playSound(null, holder.blockPosition(), SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 1.0F, 1F);
            ((StandUser) holder).roundabout$setGasolineTime(((StandUser) holder).roundabout$getMaxGasolineTime());
            return;
        }

        // default revert to item
        gePowers.playSoundIfPossible(level, null, holder.blockPosition(),
                ModSounds.GOLD_EXPERIENCE_LIFE_CREATION_EVENT, SoundSource.PLAYERS, 0.85F, 1.0F);
    }

    private static void replaceWithEmptyBucket(LivingEntity holder, ItemStack bucketStack) {
        ItemStack emptyBucket = new ItemStack(Items.BUCKET);
        if (bucketStack.getCount() == 1) {
            if (holder.getMainHandItem() == bucketStack) {
                holder.setItemSlot(EquipmentSlot.MAINHAND, emptyBucket);
                return;
            } else if (holder.getOffhandItem() == bucketStack) {
                holder.setItemSlot(EquipmentSlot.OFFHAND, emptyBucket);
                return;
            }
        }
        bucketStack.shrink(1);
        if (holder instanceof Player player) {
            if (!player.getInventory().add(emptyBucket)) {
                player.drop(emptyBucket, false);
            }
        } else {
            holder.spawnAtLocation(emptyBucket);
        }
    }

    // item imbue disguise interactions end

    // life sensor start

    private void tryLifeSensor() {
        if (!this.lifeSensorActive && !this.onCooldown(PowerIndex.GENERAL_1)) {
            this.sensedImbueEntityId = -1;
            this.lifeSensorActive = true;
            this.self.playSound(ModSounds.GOLD_EXPERIENCE_LIFE_CREATION_EVENT, 0.85F, 1.3F);
            tryPowerPacket(LIFE_SENSOR);
        }
    }

    private boolean isHoldingImbuedItem(Entity ent) {
        if (this.activeImbuedItemId == null) {
            return false;
        }
        if (ent instanceof Player player) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                if (hasMatchingImbueId(player.getInventory().getItem(i), this.activeImbuedItemId)) {
                    return true;
                }
            }
            ItemStack carried = player.containerMenu.getCarried();
            if (hasMatchingImbueId(carried, this.activeImbuedItemId)) {
                return true;
            }
        } else if (ent instanceof LivingEntity living) {
            for (ItemStack slotStack : living.getAllSlots()) {
                if (hasMatchingImbueId(slotStack, this.activeImbuedItemId)) {
                    return true;
                }
            }
        } else if (ent instanceof net.minecraft.world.entity.item.ItemEntity itemEntity) {
            return hasMatchingImbueId(itemEntity.getItem(), this.activeImbuedItemId);
        }
        return false;
    }

    @Override
    public boolean highlightsEntity(Entity ent, Player player) {
        if (this.lifeSensorActive && this.isGuarding() && PowerTypes.hasStandActive(this.self)) {
            if (ent != this.self && !PowerTypes.isInADifferentExistence(ent, this.self)) {
                double distSq = ent.distanceToSqr(this.self);

                if (ent.getId() == this.sensedImbueEntityId) {
                    return distSq <= (geConfig().lifeSenseImbueRange * geConfig().lifeSenseImbueRange);
                }

                if (ent instanceof LivingEntity living
                        && !(ent instanceof StandEntity)
                        && !(ent instanceof ArmorStand)
                        && living.isAlive()) {
                    return distSq <= (geConfig().lifeSenseRange * geConfig().lifeSenseRange);
                }
            }
        }
        return super.highlightsEntity(ent, player);
    }

    @Override
    public int highlightsEntityColor(Entity ent, Player player) {
        if (this.lifeSensorActive) {
            return 16766720;
        }
        return super.highlightsEntityColor(ent, player);
    }

    // life sensor end

    // item imbue start

    private void tryOpenItemImbue() {
        if(!this.onCooldown(PowerIndex.SKILL_4_SNEAK) && canExecuteMoveWithLevel(getItemImbueLevel()) && isHoldingItem()) {
            ClientUtil.openItemImbue();
        }
    }

    private static CompoundTag createBaseImbueTag(UUID imbueId, UUID ownerId, byte type) {
        CompoundTag data = new CompoundTag();
        data.putUUID("Id", imbueId);
        data.putUUID("Owner", ownerId);
        data.putByte("Type", type);
        return data;
    }

    @Nullable
    public static CompoundTag getImbueData(ItemStack stack) {
        if (!stack.isEmpty() && stack.hasTag() && stack.getTag().contains(IMBUE_TAG, Tag.TAG_COMPOUND)) {
            return stack.getTag().getCompound(IMBUE_TAG);
        }
        return null;
    }

    public static void clearImbue(ItemStack stack) {
        if (!stack.isEmpty() && stack.hasTag()) {
            stack.removeTagKey(IMBUE_TAG);
        }
    }

    public static void clearImbueFromConsumedStack(Level level, ItemStack stack) {
        CompoundTag imbue = getImbueData(stack);
        if (imbue == null) {
            return;
        }
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel
                && imbue.hasUUID("Id") && imbue.hasUUID("Owner")) {
            UUID imbueId = imbue.getUUID("Id");
            UUID ownerId = imbue.getUUID("Owner");
            Entity ownerEntity = serverLevel.getEntity(ownerId);
            if (ownerEntity instanceof LivingEntity ownerLiving
                    && ((StandUser) ownerLiving).roundabout$getStandPowers() instanceof PowersGoldExperience gePowers
                    && imbueId.equals(gePowers.getActiveImbuedItemId())) {
                gePowers.setActiveImbuedItemId(null);
            }
        }
        clearImbue(stack);
    }

    // animal imbue start

    public static void applyAnimalImbue(ItemStack stack, UUID imbueId, UUID ownerId, byte animalId) {
        CompoundTag data = createBaseImbueTag(imbueId, ownerId, IMBUE_ANIMAL);
        data.putByte("AnimalId", animalId);
        stack.getOrCreateTag().put(IMBUE_TAG, data);
    }

    // tree imbue start

    public static void applyTreeImbue(ItemStack stack, UUID imbueId, UUID ownerId) {
        CompoundTag data = createBaseImbueTag(imbueId, ownerId, IMBUE_TREE);
        stack.getOrCreateTag().put(IMBUE_TAG, data);
    }

    // diisguise imbue start

    public static ItemStack getDisguisedDisplayStack(ItemStack realStack) {
        CompoundTag imbue = getImbueData(realStack);
        if (imbue != null && imbue.getByte("Type") == IMBUE_DISGUISE && imbue.contains("DisguiseItem")) {
            ResourceLocation id = ResourceLocation.tryParse(imbue.getString("DisguiseItem"));
            if (id != null && BuiltInRegistries.ITEM.containsKey(id)) {
                return new ItemStack(BuiltInRegistries.ITEM.get(id), realStack.getCount());
            }
        }
        return ItemStack.EMPTY;
    }

    // note: look at GEItemDisguiseMixin for how it actually gets disguised
    public static void applyDisguiseImbue(ItemStack stack, UUID imbueId, UUID ownerId, Item disguiseItem) {
        CompoundTag data = createBaseImbueTag(imbueId, ownerId, IMBUE_DISGUISE);
        data.putString("DisguiseItem", BuiltInRegistries.ITEM.getKey(disguiseItem).toString());
        stack.getOrCreateTag().put(IMBUE_TAG, data);
    }

    public static int packImbueData(byte imbueType, byte animalId) {
        return ((imbueType & 0xFF) << 8) | (animalId & 0xFF);
    }

    public boolean executeItemImbue(byte imbueType, byte subId) {
        if (this.self.level().isClientSide()) return false;

        ItemStack heldItem = this.self.getMainHandItem();
        if (heldItem.isEmpty()) return false;

        // Clear any previously imbued item immediately
        if (this.activeImbuedItemId != null) {
            clearPreviousImbuedItem(this.activeImbuedItemId);
        }
        clearImbue(heldItem);

        // Separate 1 item from the stack so only 1 gets imbued
        ItemStack targetStack = heldItem.getCount() > 1 ? heldItem.split(1) : heldItem;

        UUID imbueId = UUID.randomUUID();
        this.activeImbuedItemId = imbueId;

        if (imbueType == IMBUE_TREE) {
            applyTreeImbue(targetStack, imbueId, this.self.getUUID());
        } else if (imbueType == IMBUE_DISGUISE) {
            int disguiseIdx = Byte.toUnsignedInt(subId);
            if (disguiseIdx >= getDisguiseOptions().size()) {
                this.activeImbuedItemId = null;
                return false;
            }
            applyDisguiseImbue(targetStack, imbueId, this.self.getUUID(), getDisguiseOptions().get(disguiseIdx));
        } else {
            applyAnimalImbue(targetStack, imbueId, this.self.getUUID(), subId);
        }

        if (targetStack != heldItem && this.self instanceof Player player) {
            if (!player.getInventory().add(targetStack)) {
                player.drop(targetStack, false);
            }
        }

        setCooldown(PowerIndex.SKILL_4_SNEAK, getItemImbueCooldown());
        playSoundIfPossible(this.self.level(), null, this.self.blockPosition(),
                ModSounds.GOLD_EXPERIENCE_LIFE_CREATION_EVENT, SoundSource.PLAYERS, 0.85F, 1F);
        return true;
    }

    private void clearPreviousImbuedItem(UUID oldId) {
        if (this.self.level() instanceof ServerLevel serverLevel && serverLevel.getServer() != null) {
            // lots of checks here to ensure that any item gets cleared
            for (ServerPlayer serverPlayer : serverLevel.getServer().getPlayerList().getPlayers()) {
                for (int i = 0; i < serverPlayer.getInventory().getContainerSize(); i++) {
                    ItemStack invStack = serverPlayer.getInventory().getItem(i);
                    if (hasMatchingImbueId(invStack, oldId)) {
                        clearImbue(invStack);
                    }
                }
                ItemStack carried = serverPlayer.containerMenu.getCarried();
                if (hasMatchingImbueId(carried, oldId)) {
                    clearImbue(carried);
                }
            }
            for (ServerLevel level : serverLevel.getServer().getAllLevels()) {
                for (Entity entity : level.getAllEntities()) {
                    if (entity instanceof net.minecraft.world.entity.item.ItemEntity itemEntity) {
                        ItemStack entityStack = itemEntity.getItem();
                        if (hasMatchingImbueId(entityStack, oldId)) {
                            clearImbue(entityStack);
                            itemEntity.setItem(entityStack);
                        }
                    }
                }
            }
        } else {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack slotStack = this.self.getItemBySlot(slot);
                if (hasMatchingImbueId(slotStack, oldId)) {
                    clearImbue(slotStack);
                }
            }
        }
    }

    private static boolean hasMatchingImbueId(ItemStack stack, UUID targetId) {
        CompoundTag imbue = getImbueData(stack);
        return imbue != null && imbue.hasUUID("Id") && targetId.equals(imbue.getUUID("Id"));
    }

    // This is where the magic happens. This method transforms the item into an animal
    public static boolean tryActivateImbuedItem(LivingEntity holder, ItemStack stack) {
        if (holder.level().isClientSide() || !(holder.level() instanceof ServerLevel serverLevel)) {
            return false;
        }

        CompoundTag imbue = getImbueData(stack);
        if (imbue == null || !imbue.hasUUID("Id") || !imbue.hasUUID("Owner")) {
            return false;
        }

        UUID imbueId = imbue.getUUID("Id");
        UUID ownerId = imbue.getUUID("Owner");

        Entity ownerEntity = serverLevel.getEntity(ownerId);
        if (!(ownerEntity instanceof LivingEntity ownerLiving)
                || !(((StandUser) ownerLiving).roundabout$getStandPowers() instanceof PowersGoldExperience gePowers)) {
            return false;
        }

        // 1 item limit, clears the old tags to make way for the new one
        if (!imbueId.equals(gePowers.getActiveImbuedItemId())) {
            clearImbue(stack);
            return false;
        }

        byte imbueType = imbue.getByte("Type");

        // note: look at PowersItemStack for activation rules
        if (imbueType == IMBUE_ANIMAL) {
            byte animalId = imbue.getByte("AnimalId");
            EntityType<?> typeToSpawn = getAnimalEntityType(animalId);
            if (typeToSpawn == null) return false;

            ItemStack cleanSource = stack.copyWithCount(1);
            clearImbue(cleanSource);

            Vec3 spawnPos = holder.position().add(holder.getLookAngle().scale(1.2)).add(0, 1.0, 0);
            if (gePowers.spawnLifeform(typeToSpawn, spawnPos, cleanSource)) {
                clearImbue(stack);
                stack.shrink(1);
                gePowers.setActiveImbuedItemId(null);
                return true;
            }
        } else if (imbueType == IMBUE_TREE) {
            ItemStack cleanSource = stack.copyWithCount(1);
            clearImbue(cleanSource);
            if (holder instanceof StandUser su) {
                if (!su.roundabout$getGERootedItem().isEmpty()) {
                    holder.spawnAtLocation(su.roundabout$getGERootedItem());
                }
                su.roundabout$setGERootedItem(cleanSource);
                // just need this for the key disabling, nothing else
                su.roundabout$setGERooted(true);
                gePowers.playSoundIfPossible(serverLevel, null, holder.blockPosition(),
                        ModSounds.GOLD_EXPERIENCE_LIFE_CREATION_EVENT, SoundSource.PLAYERS, 0.85F, 1.0F);
                clearImbue(stack);
                stack.shrink(1);
                gePowers.setActiveImbuedItemId(null);
                return true;
            }
        } else if (imbueType == IMBUE_DISGUISE) {
            clearImbue(stack);
            gePowers.setActiveImbuedItemId(null);
            handleDisguiseRevertItem(holder, stack, gePowers);
            return true;
        }

        return false;
    }

    // animal imbue end

    // item imbue end

    // unique root barrage start

    @Override
    public void setPowerBarrage() {
        this.groundBarrageTicks = 0;
        this.isGroundBarraging = false;
        super.setPowerBarrage();
    }

    @Override
    public void updateBarrage() {
        if (this.attackTimeDuring == -2 && this.getSelf() instanceof Player) {
            this.groundBarrageTicks = 0;
            ((StandUser) this.self).roundabout$tryPower(PowerIndex.GUARD, true);
        } else {
            if (this.attackTimeDuring > this.getBarrageLength()) {
                this.attackTimeDuring = -20;
                this.groundBarrageTicks = 0;
            } else if (this.attackTimeDuring > 0) {
                this.setAttackTime((getBarrageRecoilTime() - 1) -
                        Math.round(((float) this.attackTimeDuring / this.getBarrageLength())
                                * (getBarrageRecoilTime() - 1)));
                standBarrageHit();
            } else {
                this.groundBarrageTicks = 0;
            }
        }
    }

    @Override
    public void standBarrageHit() {
        float punchRange = 4.5F;

        Entity targetInCrosshair = getTargetEntity(this.self, punchRange);
        BlockHitResult blockHit = this.getRayBlockOutline(this.self, punchRange);

        // entity vs block check
        if (this.activePower != PowerIndex.BARRAGE_2 && (targetInCrosshair == null || targetInCrosshair instanceof GEThornEntity) && blockHit.getType() == HitResult.Type.BLOCK) {
            this.isGroundBarraging = true;

            if (this.self instanceof Player) {
                if (isPacketPlayer()) {
                    C2SPacketUtil.standBarrageHitPacket(-1, this.attackTimeDuring);

                    if (this.activePower == PowerIndex.BARRAGE_2 && this.attackTimeDuring == this.getKickBarrageLength()){
                        this.attackTimeDuring = -10;
                    } else if (this.isBarraging() && this.attackTimeDuring == this.getBarrageLength()){
                        this.attackTimeDuring = -10;
                    }
                }
            } else {
                barrageImpact(null, this.attackTimeDuring);
            }

            if (!this.self.level().isClientSide()) {
                // sounds
                if (this.attackTimeDuring >= this.getBarrageLength()) {
                    playBarrageEndNoise(0, null);
                } else {
                    playBarrageNoise(this.attackTimeDuring, null);
                }

                this.groundBarrageTicks++;

                Level level = this.self.level();
                Vec3 lookVec = this.self.getLookAngle();
                Vec3 dir = new Vec3(lookVec.x, 0, lookVec.z).normalize();
                Vec3 right = new Vec3(-dir.z, 0, dir.x);

                //spawns the thorns
                float progress = Math.min(1.0F, (float) this.groundBarrageTicks / (this.getBarrageLength() * 0.75F));
                double startDist = 1.2;
                double maxReach = 12.0;
                double currentDist = startDist + (progress * (maxReach - startDist));
                double spreadWidth = 0.8 + (currentDist * 0.4);

                int entitiesPerHit = 2;

                for (int i = 0; i < entitiesPerHit; i++) {
                    double sideBias = (i == 0 ? -0.5 : 0.5) + (level.random.nextDouble() - 0.5D) * 0.8D;
                    double offset = sideBias * spreadWidth;
                    double lengthJitter = (level.random.nextDouble() - 0.5D) * 1.2D;

                    double targetX = this.self.getX() + dir.x * (currentDist + lengthJitter) + right.x * offset;
                    double targetZ = this.self.getZ() + dir.z * (currentDist + lengthJitter) + right.z * offset;

                    BlockPos groundPos = findGroundPosition(level, BlockPos.containing(targetX, this.self.getY(), targetZ));

                    if (groundPos != null) {
                        Vec3 spawnPos = new Vec3(targetX, groundPos.getY() + 1.0, targetZ);
                        boolean tooClose = !level.getEntitiesOfClass(
                                GEThornEntity.class, new AABB(spawnPos, spawnPos).inflate(0.35D)).isEmpty();

                        // z fighting fix. i couldn't find any other way to fix it, so i'm just gonna prevent them from overlapping in the first place.
                        if (!tooClose) {
                            GEThornEntity rootEntity = new GEThornEntity(ModEntities.GE_THORN, level);
                            rootEntity.setOwner(this.getSelf());
                            rootEntity.setPos(spawnPos.x, spawnPos.y, spawnPos.z);

                            level.addFreshEntity(rootEntity);
                        }
                    }
                }
            }
        } else {
            this.isGroundBarraging = false;
            super.standBarrageHit();
        }
    }

    // unique root barrage end

    // requiem system start

    @Override
    public boolean canUseStandArrow() { return canUseRequiem() && ((StandUser)self).roundabout$getActive(); }

    @Override public boolean shouldStandArrowShake() { return isPossibleToRequiem() && canUseRequiem(); }

    public boolean canUseRequiem() {
        return canExecuteMoveWithLevel(getMaxLevel());
    }
    public boolean isPossibleToRequiem() {
        return resolvePoints >= getNeededResolvePoints() && ((StandUser)self).roundabout$getActive();
    }

    protected float resolvePoints = 0F;

    public float getResolvePoints() { return resolvePoints; }
    public float getNeededResolvePoints() { return 300.0F; }

    @Override
    public boolean onStandArrowUse() {
        if (this.self instanceof Player PE && activePower == PowerIndex.NONE) {
            if (isPossibleToRequiem()) {
                //((StandUser) self).roundabout$summonStand(self.level(), true, true);
                sendParticlesIfPossible(self.level(),ParticleTypes.FIREWORK, PE.getX(),
                        PE.getY() + PE.getEyeHeight(), PE.getZ(),
                        20, 0, 0, 0, 0.4);

                playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.GE_REQUIEM_ARROW_EVENT, SoundSource.PLAYERS, 1.05F, 1.0f);

                if (!isClient()) {
                    ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.requiem_activated").
                            withStyle(ChatFormatting.YELLOW), true);
                }

                return false;
            }else {
                if (!isClient()) {
                    ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.requiem_failed").
                            withStyle(ChatFormatting.RED), true);
                }
            }
        }
        return false;
    }

    @Override
    public boolean onStandArrowUseClient() {
        if (this.self instanceof Player && activePower == PowerIndex.NONE) {
            if (isPossibleToRequiem()) {

                if (!isAppropriateToGrab()) {
                    putDownClient();
                }

                StandEntity standEntity = getStandEntity(self);

                if (standEntity != null && standEntity.canAcquireHeldItem) {
                    if ((this.getActivePower() == PowerIndex.POWER_2 || this.getActivePower() == PowerIndex.POWER_2_SNEAK
                            || this.getActivePower() == PowerIndex.POWER_2_SNEAK_EXTRA)){
                        this.addItem(standEntity);
                        standEntity.setHeldItem(ItemStack.EMPTY);
                    }
                }

                ((StandUser) this.getSelf()).roundabout$tryIntPower(REQUIEM, true,
                        ((Player) this.getSelf()).getInventory().selected);
                tryIntPowerPacket(REQUIEM,
                        ((Player) this.getSelf()).getInventory().selected);
                /*((StandUser) this.getSelf()).roundabout$tryPower(REQUIEM, true);
                tryPowerPacket(REQUIEM);*/

                return true;
            }
        }
        return false;
    }

    private float getResolvePointsObtainingCap() { return 30.0F; }

    public boolean onKilledEntity(ServerLevel $$0, LivingEntity victim){
        if (!(canUseRequiem())) {
            return super.onKilledEntity($$0, victim);
        }

        if (resolvePoints < getNeededResolvePoints()) {
            if (victim instanceof Mob) {
                float amount = victim.getMaxHealth();

                if (!((StandUser)victim).roundabout$getStandDisc().isEmpty() && (victim instanceof Monster ||
                        victim instanceof NeutralMob)){

                    amount = amount * 1.25f;
                }

                addResolvePoints(amount);
            }
        }

        return super.onKilledEntity($$0,victim); // DO NOT RETURN 'true' OR ELSE IT WILL STOP ENTITIES FROM DYIENG
    }

    public void onPlayerHurt(Player PL, DamageSource src, float damage) {
        if (!(canUseRequiem())) {
            return;
        }
        addResolvePoints(damage*1.75f);
    }

    private void addResolvePoints(float amount) {
        resolvePoints += Math.min(amount, getResolvePointsObtainingCap());
        if (resolvePoints > getNeededResolvePoints()) {
            resolvePoints = getNeededResolvePoints();
        }
        saveDiscAndSync();
        if (!isClient() && self instanceof Player PL) {
            S2CPacketUtil.sendIntPowerDataPacket(PL, PowerIndex.SPECIAL_CHARGED, 0);
        }
    }

    // requiem system end

    // requiem animation start

    // to make sure the arrow isn't lost
    protected ItemStack standArrow = null;

    public ItemStack getStandArrow() {
        return standArrow;
    }

    private boolean setRequiem() {
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand) && !stand.isRemoved()) {
            if (!this.getSelf().level().isClientSide()) {
                ItemStack stack = ((Player)this.getSelf()).getInventory().getItem(this.grabInventorySlot);
                if (stack.is(ModItems.STAND_ARROW) || stack.is(ModItems.STAND_BEETLE_ARROW)) {
                    standArrow = stack.copyWithCount(1);
                    stack.shrink(1);
                    saveDiscAndSync();
                }else {
                    return false; // how isn't your main hand a stand arrow?
                }
            }
            this.setAttackTimeDuring(0);
            this.setActivePower(REQUIEM);
            //playSoundsIfNearby(IMPALE_NOISE, 27, false);
            this.animateStand(GoldExperienceEntity.REQUIEM);
            this.poseStand(OffsetIndex.GUARD_FURTHER_RIGHT);
            return true;
        }
        return false;
    }

    private void updateRequiem() {

        if (attackTimeDuring >= 240) {
            //setPowerNone();

            ItemStack discy = ((StandUser)self).roundabout$getStandDisc();
            ItemStack savedDisc = MainUtil.saveToDiscData(self, discy);

            //onStandSwitch();

            ((StandUser)self).roundabout$setStandDisc(new ItemStack(ModItems.CREATIVE_STAND_DISC_GER));
            ((StandUser)self).roundabout$getStandPowers().onStandSwitchInto();
            StandEntity GERequiem = null;

            if (((StandUser)self).roundabout$getStandPowers() instanceof PowersGER GER) {
                GER.idlePos = idlePos;
                GER.standSkin = standSkin;
                GER.resolvePoints = resolvePoints;

                GER.setOriginalDisc(savedDisc);

                GERequiem = GER.getNewStandEntity();
            }

            if (((StandUser)self).roundabout$getStand() != null) {
                StandEntity oldMe = ((StandUser)self).roundabout$getStand();
                if (GERequiem != null) {
                    GERequiem.absMoveTo(oldMe.getX(), oldMe.getY(), oldMe.getZ(), oldMe.getYRot(), oldMe.getXRot());

                    GERequiem.setSkin(((StandUser)self).roundabout$getStandSkin());
                    GERequiem.setIdleAnimation(((StandUser)self).roundabout$getIdlePos());

                    if (self instanceof Player PE) {
                        GERequiem.playerSetProperties(PE);
                        if (GERequiem instanceof FollowingStandEntity FE) {
                            FE.setDistanceOut(((IPlayerEntity) PE).roundabout$getDistanceOut());
                            FE.setAnchorPlace(((IPlayerEntity) PE).roundabout$getAnchorPlace());
                            FE.setAnchorPlaceAttack(((IPlayerEntity) PE).roundabout$getAnchorPlaceAttack());
                            FE.setSizePercent(((IPlayerEntity) PE).roundabout$getSizePercent());
                            FE.setIdleRotation(((IPlayerEntity) PE).roundabout$getIdleRotation());
                            FE.setIdleYOffset(((IPlayerEntity) PE).roundabout$getIdleYOffset());
                        }
                        if (!self.level().isClientSide()) {
                            IPlayerEntity ipe = ((IPlayerEntity) self);
                            S2CPacketUtil.sendPowerInventorySettings(
                                    PE, ipe.roundabout$getAnchorPlace(),
                                    ipe.roundabout$getDistanceOut(),
                                    ipe.roundabout$getSizePercent(),
                                    ipe.roundabout$getIdleRotation(),
                                    ipe.roundabout$getIdleYOffset(),
                                    ipe.roundabout$getAnchorPlaceAttack());
                        }
                    }
                    oldMe.discard();
                    self.level().addFreshEntity(GERequiem);
                    ((StandUser)self).roundabout$standMount(GERequiem);
                }
            }else {
                ((StandUser)self).roundabout$summonStand(self.level(), true, false);
            }

            ((StandUser)self).roundabout$setStand(GERequiem);

        }
    }


    // requiem animation end

    // tree creation start

    private void tryGrowTree() {
        if(!this.onCooldown(PowerIndex.GENERAL_2) && canExecuteMoveWithLevel(getTreeLevel())) {
            ((StandUser) this.getSelf()).roundabout$tryPower(TREE, true);
            tryPowerPacket(TREE);
        }
    }

    private boolean startTreeWindup() {
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)) {
            this.setAttackTimeDuring(0);
            this.setActivePower(TREE);
            this.animateStand(GoldExperienceEntity.GROUND_PUNCH);
            this.poseStand(OffsetIndex.ATTACK);
            return true;
        }
        return false;
    }

    private void updateTree() {
        if (this.attackTimeDuring >= 10) {
            finishTree();
        }
    }

    private boolean finishTree() {
        BlockPos centerPos;

        if (this.isHoldingSneak()) {
            centerPos = this.self.blockPosition();
        } else {
            HitResult hit = pick(3, 0.0F, false);

            if (hit.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHit = (BlockHitResult) hit;
                centerPos = blockHit.getBlockPos().relative(blockHit.getDirection());
            } else {
                Vec3 eyePos = this.self.getEyePosition(1.0F);
                Vec3 lookVec = this.self.getViewVector(1.0F);
                Vec3 reachVec = eyePos.add(lookVec.scale(3.0D));
                centerPos = BlockPos.containing(reachVec);
            }
        }

        return growGETreeAt(centerPos, true);
    }

    public boolean growGETreeAt(BlockPos centerPos, boolean fromSkill) {
        Level level = this.self.level();

        boolean isSafe = false;
        for (int i = 0; i < 5; i++) {
            if (level.getBlockState(centerPos.below(i)).isSolid()) {
                centerPos = centerPos.below(i).above();
                isSafe = true;
                break;
            }
        }

        if (!isSafe) {
            return false;
        }

        BlockPos base = fromSkill
                ? new BlockPos(centerPos.getX(), Math.min(centerPos.getY(), this.self.blockPosition().getY()), centerPos.getZ())
                : centerPos;
        Direction growthDirection = Direction.UP;

        int maxHeight = geConfig().treeMaxHeight;
        int actualHeight = 0;

        for (int y = 0; y < maxHeight; y++) {
            BlockPos trunkPos = base.relative(growthDirection, y);

            if (!level.getBlockState(trunkPos).canBeReplaced() ||
                    !level.getBlockState(trunkPos.above(1)).canBeReplaced() ||
                    !level.getBlockState(trunkPos.above(2)).canBeReplaced()) {
                break;
            }
            actualHeight++;
        }

        if (actualHeight == 0) {
            return false;
        }

        this.treeRoofY = base.getY() + actualHeight;

        if (!level.isClientSide()) {
            pendingTreeBlocks.clear();
            treeSpawnTimer = -30;
            this.treeBasePos = base;
            this.treeGrowthDir = Direction.UP;

            float yOffset = -actualHeight;

            for (int y = 0; y < actualHeight; y++) {
                BlockPos trunkPos = base.relative(growthDirection, y);

                Vector3f trunkStart = new Vector3f((float) (trunkPos.getX() + 0.5), (float) trunkPos.getY() + yOffset, (float) (trunkPos.getZ() + 0.5));
                Vector3f trunkFinal = new Vector3f(trunkStart).add(0, actualHeight, 0);

                pendingTreeBlocks.add(new TreeBlockSpawnData(
                        trunkStart,
                        trunkFinal,
                        Blocks.OAK_WOOD.defaultBlockState(),
                        0,
                        true
                ));

                // all branches ecept for last and first with leaves
                if (y != actualHeight - 1 && y != 0) {
                    Direction branchDir = Direction.from2DDataValue(y % 4);
                    BlockPos branchPos = trunkPos.relative(branchDir);

                    Vector3f branchStart = new Vector3f(trunkPos.getX() + 0.5f, trunkPos.getY() - 0.5f, trunkPos.getZ() + 0.5f);

                    BlockState branchState = Blocks.OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, branchDir.getAxis());
                    BlockState leafState = Blocks.OAK_LEAVES.defaultBlockState().setValue(BlockStateProperties.PERSISTENT, true);

                    Vector3f branchFinal = new Vector3f(branchPos.getX() + 0.5f, branchPos.getY(), branchPos.getZ() + 0.5f);
                    pendingTreeBlocks.add(new TreeBlockSpawnData(branchStart, branchFinal, branchState, 12 + (y * 2), false));

                    BlockPos centerWoodPos = branchPos.relative(branchDir).above();
                    Vector3f centerFinal = new Vector3f(centerWoodPos.getX() + 0.5f, centerWoodPos.getY(), centerWoodPos.getZ() + 0.5f);
                    pendingTreeBlocks.add(new TreeBlockSpawnData(branchStart, centerFinal, branchState, 14 + (y * 2), false));

                    BlockPos[] leafPositions = new BlockPos[]{
                            centerWoodPos.above(),
                            centerWoodPos.below(),
                            centerWoodPos.relative(branchDir.getClockWise()),
                            centerWoodPos.relative(branchDir.getCounterClockWise()),
                            centerWoodPos.relative(branchDir)
                    };

                    for (BlockPos leafPos : leafPositions) {
                        Vector3f leafFinal = new Vector3f(leafPos.getX() + 0.5f, leafPos.getY(), leafPos.getZ() + 0.5f);
                        pendingTreeBlocks.add(new TreeBlockSpawnData(branchStart, leafFinal, leafState, 16 + (y * 2), false));
                    }
                }
                // first branch root thing for jumping
                else if (y == 0) {
                    Direction branchDir = Direction.from2DDataValue(y % 4);
                    BlockPos branchPos = trunkPos.relative(branchDir);

                    Vector3f branchStart = new Vector3f(trunkPos.getX() + 0.5f, trunkPos.getY() - 0.5f, trunkPos.getZ() + 0.5f);
                    Vector3f branchFinal = new Vector3f(branchPos.getX() + 0.5f, branchPos.getY(), branchPos.getZ() + 0.5f);

                    pendingTreeBlocks.add(new TreeBlockSpawnData(branchStart, branchFinal, Blocks.OAK_WOOD.defaultBlockState(), 12 + (y * 2), false));
                }
            }

            playSoundIfPossible(level, null, base, ModSounds.GOLD_EXPERIENCE_TREE_EVENT, SoundSource.PLAYERS, 0.9F, 1F);
            if (fromSkill) {
                addEXP(5);
                this.setCooldown(PowerIndex.GENERAL_2, getTreeCooldown());
                this.setAttackTimeDuring(-5);
            }
        }
        if (fromSkill) {
            this.setAttackTimeDuring(-10);
        }
        return true;
    }

    public boolean growGEWallBranchAt(BlockPos placePos, Direction outwardDir) {
        Level level = this.self.level();
        if (level.isClientSide() || !outwardDir.getAxis().isHorizontal()) return false;

        BlockPos log1 = placePos;
        BlockPos log2 = placePos.relative(outwardDir);
        BlockPos topLog = log2.relative(outwardDir).above();

        if (!level.getBlockState(log1).canBeReplaced() || !level.getBlockState(log2).canBeReplaced()) {
            return false;
        }

        BlockState branchState = Blocks.OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, outwardDir.getAxis());
        BlockState leafState = Blocks.OAK_LEAVES.defaultBlockState().setValue(BlockStateProperties.PERSISTENT, true);

        BlockPos wallAnchor = placePos.relative(outwardDir.getOpposite());
        Vector3f branchStart = new Vector3f(wallAnchor.getX() + 0.5f, wallAnchor.getY(), wallAnchor.getZ() + 0.5f);

        pendingTreeBlocks.clear();
        treeSpawnTimer = -30;
        this.treeBasePos = placePos;
        this.treeGrowthDir = outwardDir;

        pendingTreeBlocks.add(new TreeBlockSpawnData(
                branchStart,
                new Vector3f(log1.getX() + 0.5f, log1.getY(), log1.getZ() + 0.5f),
                branchState,
                0,
                true
        ));
        pendingTreeBlocks.add(new TreeBlockSpawnData(
                branchStart,
                new Vector3f(log2.getX() + 0.5f, log2.getY(), log2.getZ() + 0.5f),
                branchState,
                2,
                true
        ));

        if (level.getBlockState(topLog).canBeReplaced()) {
            pendingTreeBlocks.add(new TreeBlockSpawnData(
                    branchStart,
                    new Vector3f(topLog.getX() + 0.5f, topLog.getY(), topLog.getZ() + 0.5f),
                    branchState,
                    4,
                    true
            ));

            BlockPos[] leafPositions = new BlockPos[]{
                    topLog.above(),
                    topLog.below(),
                    topLog.relative(outwardDir),
                    topLog.relative(outwardDir.getClockWise()),
                    topLog.relative(outwardDir.getCounterClockWise())
            };

            for (BlockPos leafPos : leafPositions) {
                if (level.getBlockState(leafPos).canBeReplaced()) {
                    pendingTreeBlocks.add(new TreeBlockSpawnData(
                            branchStart,
                            new Vector3f(leafPos.getX() + 0.5f, leafPos.getY(), leafPos.getZ() + 0.5f),
                            leafState,
                            6,
                            false
                    ));
                }
            }
        }

        playSoundIfPossible(level, null, placePos, ModSounds.GOLD_EXPERIENCE_LIFE_CREATION_EVENT, SoundSource.PLAYERS, 0.9F, 1.0F);
        return true;
    }

    // taken directly from white album
    private HitResult pick(double $$0, float $$1, boolean $$2) {
        Vec3 $$3 = this.self.getEyePosition($$1);
        Vec3 $$4 = this.self.getViewVector($$1);
        Vec3 $$5 = $$3.add($$4.x * $$0, $$4.y * $$0, $$4.z * $$0);
        return this.self.level().clip(new ClipContext($$3, $$5, ClipContext.Block.COLLIDER, $$2 ? net.minecraft.world.level.ClipContext.Fluid.ANY : net.minecraft.world.level.ClipContext.Fluid.NONE, self));
    }

    // tree creation end

    // item throw start

    @Override
    public void itemGrabClient() {
        if (!this.onCooldown(PowerIndex.SKILL_2)) {
            // GE animal sync
            int animalClientConfig = 1;
            ClientConfig clientConfig = ConfigManager.getClientConfig();
            if (clientConfig != null && clientConfig.dynamicSettings != null) {
                animalClientConfig = clientConfig.dynamicSettings.goldExperienceCurrentAnimal;
            }
            this.animalType = (byte) animalClientConfig;
            tryIntPowerPacket(ITEM_THROW, animalClientConfig);
            super.itemGrabClient();
        }
    }

    @Override
    public boolean throwObject(ItemStack item){
        this.setCooldown(PowerIndex.SKILL_2, getAnimalCreationCooldown());
        /***/

        Vec3 pos = new Vec3(this.self.getX(), this.self.getEyeY() - 0.1F, this.self.getZ());
        Direction gravD = ((IGravityEntity)this.self).roundabout$getGravityDirection();
        if (gravD != Direction.DOWN){
            pos = RotationUtil.vecPlayerToWorld(
                    new Vec3(0, this.self.getEyeHeight() - 0.1F, 0
                    ),gravD);
            pos = new Vec3(this.self.getX()+pos.x,this.self.getY()+pos.y,this.self.getZ()+pos.z);
        }

        return ThrownObjectEntity.throwAnObject(this.self,canSnipe(),item,getShotAccuracy(),getBundleAccuracy(),getThrowAngle(),
                getThrowAngle2(),getThrowAngle3(),getCanPlace(),getThrowStyleType(),this.self.getXRot(),this.self.getYRot(),
                new Vec3(pos.x,pos.y,pos.z),true,1, true);
    }

    @Override
    public byte getThrowStyleType(){
        return ThrownObjectEntity.GETHROW;
    }

    public byte getSelectedAnimalType() {
        return this.animalType;
    }

    // item throw end

    // 1-2 sneak attack punch start

    // god i hate doing attacks
    private boolean executeDualPunch() {
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)) {
            this.activePowerPhase = this.getActivePowerPhaseMax();
            this.attackTimeDuring = 0;
            this.attackTimeMax = ClientNetworking.getAppropriateConfig().generalStandSettings.finalStandPunchInStringCooldown;
            this.setActivePower(PowerIndex.SNEAK_ATTACK);
            this.setAttackTime(0);
            this.animateStand(GoldExperienceEntity.DOUBLE_PUNCH);
            poseStand(OffsetIndex.ATTACK);
            return true;
        }
        return false;
    }

    private void updateDualPunch() {
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > this.attackTimeMax) {
                this.attackTime = -1;
                this.attackTimeMax = 0;
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
            } else {
                if (this.attackTimeDuring == 5 || this.attackTimeDuring == 11) {
                    boolean isSecondHit = (this.attackTimeDuring == 11);
                    this.standDualPunch(isSecondHit);
                }
            }
        }
    }

    private void standDualPunch(boolean isSecondHit) {
        if (this.self instanceof Player pl) {
            if (isPacketPlayer()) {
                if (isSecondHit) {
                    this.attackTimeDuring = -10;
                }
                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK, getTargetEntityId(getPunchAngle()));

                if (self.getMainHandItem().getItem() instanceof TieredItem) {
                    pl.resetAttackStrengthTicker();
                }
            }
        } else {
            Entity targetEntity = getTargetEntity(this.self, -1, getPunchAngle());
            dualPunchImpact(targetEntity, isSecondHit);
        }
    }

    private void dualPunchImpact(Entity entity, boolean isSecondHit) {
        if (entity != null && entity.distanceTo(self) > getReach() + 1) {
            entity = null;
        }

        if (entity != null) {
            // for some reason halfing the damage made it stronger thaan the normal punches so.. i need to make it slightly more than a half reduction
            float pow = getHeavyPunchStrength(entity) * 0.42F;

            float knockbackStrength = isSecondHit ? 1.4F : 0.3F;

            if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                if (entity instanceof LivingEntity LE) {
                    addEXP(isSecondHit ? 2 : 1, LE);
                }
                takeDeterminedKnockback(this.self, entity, knockbackStrength);
            } else {
                if (entity instanceof LivingEntity LE) {
                    knockShield2(entity, isSecondHit ? 60 : 20);
                }
            }
            hitParticles(entity);
        } else {
            float distMax = this.getDistanceOut(this.self, this.getReach(), false);
            float halfReach = (float) (distMax * 0.5);
            Vec3 pointVec = DamageHandler.getRayPoint(self, halfReach);
            if (!this.self.level().isClientSide) {
                sendParticlesIfPossible(self.level(), ModParticles.PUNCH_MISS, pointVec.x, pointVec.y, pointVec.z,
                        1, 0.0, 0.0, 0.0, 1);
            }
        }

        SoundEvent SE;
        float pitch = 1.0F;
        float volume = 0.95F;
        if (isSecondHit) {
            if (!this.self.level().isClientSide()) {
                playTheLastHitSound();
            }

            if (entity != null) {
                SE = getPunchLandLastSound();
                pitch = getPunchLandLastPitch();
                volume = getPunchLandLastVolume();
            } else {
                SE = ModSounds.PUNCH_2_SOUND_EVENT;
            }
        } else {
            if (entity != null) {
                SE = getPunchLandSound();
                pitch = getPunchLandPitch();
                volume = getPunchLandVolume();
            } else {
                SE = getPunchMissSound();
            }
        }

        if (!this.self.level().isClientSide()) {
            playSoundIfPossible(self.level(), null, this.self.blockPosition(), SE, SoundSource.PLAYERS, volume, pitch);
        }
    }

    // 1 -2 sneak attack punch end

    // animal creation (X) start

    private void tryMakeAnimalClient() {
        if (!this.onCooldown(PowerIndex.SKILL_2)) {
            int animalClientConfig = 1;
            ClientConfig clientConfig = ConfigManager.getClientConfig();
            if (clientConfig != null && clientConfig.dynamicSettings != null) {
                animalClientConfig = clientConfig.dynamicSettings.goldExperienceCurrentAnimal;
            }
            this.tryIntPower(LIFEFORM_CREATION, true, animalClientConfig);
            tryIntPowerPacket(LIFEFORM_CREATION, animalClientConfig);
        }
    }

    private boolean executeLifeformCreation() {
        if (!this.self.level().isClientSide()) {
            boolean isMob = !(this.self instanceof Player);

            if (!isMob && this.self.isCrouching()) {
                return false;
            }

            if (!isMob && !isHoldingItem()) {
                return false;
            }

            EntityType<?> typeToSpawn = getAnimalEntityType(this.animalType);

            int cooldown = getAnimalCreationCooldown();

            if(animalType == AnimalTypes.SNAKE.id || animalType == AnimalTypes.PIRAHNA.id || animalType == AnimalTypes.BAT.id) {
                cooldown += 200;
            }
            if(animalType == AnimalTypes.FROG.id || animalType == AnimalTypes.BEE.id) {
                cooldown += 100;
            }

            if (typeToSpawn != null) {
                Vec3 spawnPos = this.self.position().add(this.self.getLookAngle().scale(1.2));
                spawnPos = new Vec3(spawnPos.x, spawnPos.y + 1.0, spawnPos.z);

                ItemStack handItem = this.self.getMainHandItem();
                ItemStack sourceStack = (isMob) ? ItemStack.EMPTY : handItem;

                if (spawnLifeform(typeToSpawn, spawnPos, sourceStack)) {
                    setCooldown(PowerIndex.SKILL_2, cooldown);
                    if (!isMob || !handItem.isEmpty()) {
                        handItem.shrink(1);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean spawnLifeform(EntityType<?> entityType, Vec3 spawnPos, ItemStack sourceItem) {
        Entity entity = entityType.create(this.self.level());

        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.moveTo(spawnPos.x, spawnPos.y, spawnPos.z, this.self.getYRot(), 0.0F);

            if (livingEntity instanceof GELifeform lifeform) {
                lifeform.setCreator(this.getSelf());
                ItemStack consumedItem = sourceItem.copy();
                consumedItem.setCount(1);
                clearImbueFromConsumedStack(this.self.level(), consumedItem);
                lifeform.setSourceItem(consumedItem);
            }

            if (livingEntity instanceof Mob mob && !(mob instanceof GEFoxEntity) && this.self.level() instanceof ServerLevel serverLevel) {
                mob.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
            }

            this.self.level().addFreshEntity(livingEntity);
            playSoundIfPossible(this.self.level(), null, BlockPos.containing(spawnPos),
                    ModSounds.GOLD_EXPERIENCE_LIFE_CREATION_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
            return true;
        }
        return false;
    }

    private boolean isHoldingItem() {
        ItemStack heldItem = this.self.getMainHandItem();
        if (heldItem.isEmpty()) {
            return false;
        }
        return true;
    }

    // animal creation (X) end

    // animal selection start

    private void tryOpenAnimalSelect() {
        ClientUtil.openAnimalSelect();
    }

    // animal selection end

    // bramble spikes start

    private void tryGrowBramble() {
        if (!this.onCooldown(PowerIndex.SKILL_4) && canExecuteMoveWithLevel(getBrambleLevel())) {
            ((StandUser) this.getSelf()).roundabout$tryPower(BRAMBLES, true);
            tryPowerPacket(BRAMBLES);
        }
    }

    private boolean startBrambleWindup() {
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)) {
            this.setAttackTimeDuring(0);
            this.setActivePower(BRAMBLES);
            this.animateStand(GoldExperienceEntity.GROUND_PUNCH);
            this.poseStand(OffsetIndex.ATTACK);
            return true;
        }
        return false;
    }

    private void updateBramble() {
        if (this.attackTimeDuring > 9) {
            finishBramble();
        }
    }

    private void finishBramble() {
        if (!this.self.level().isClientSide()) {
            Level level = this.self.level();
            Vec3 lookVec = this.self.getLookAngle();
            Vec3 dir = new Vec3(lookVec.x, 0, lookVec.z).normalize();

            double startX = this.self.getX();
            double startY = this.self.getY();
            double startZ = this.self.getZ();

            int spikeCount = geConfig().brambleAmount;
            double spacing = 1.3D;

            // Clear any old queued spikes before adding new ones
            pendingBrambles.clear();
            brambleSpawnTimer = 0;

            for (int i = 1; i <= spikeCount; i++) {
                double targetX = startX + dir.x * (i * spacing);
                double targetZ = startZ + dir.z * (i * spacing);

                BlockPos groundPos = findGroundPosition(level, BlockPos.containing(targetX, startY, targetZ));
                if (groundPos != null) {
                    Vec3 spawnPos = new Vec3(targetX, groundPos.getY() + 1.0, targetZ);
                    pendingBrambles.add(new BrambleSpawnData(spawnPos, this.self.getYRot()));
                }
            }

            playSoundIfPossible(level, null, this.self.blockPosition(),
                    ModSounds.GOLD_EXPERIENCE_LIFE_CREATION_EVENT, SoundSource.PLAYERS, 1.0F, 1F);
            addEXP(7);
            this.setCooldown(PowerIndex.SKILL_4, getBrambleCooldown());
            this.setAttackTimeDuring(-5);
        }
    }

    private BlockPos findGroundPosition(Level level, BlockPos start) {
        // Search up to 2 blocks up or 3 blocks down for solid ground
        for (int yOffset = 2; yOffset >= -3; yOffset--) {
            BlockPos checkPos = start.above(yOffset);
            BlockState state = level.getBlockState(checkPos);
            BlockPos abovePos = checkPos.above();

            if (state.isSolid() && level.getBlockState(abovePos).canBeReplaced()) {
                return checkPos;
            }
        }
        return null;
    }

    // bramble spikes end

    // heal start

    @Override
    public void buttonInput1(boolean keyIsDown, Options options) {
        if (holdDownHeal) {
            if (!keyIsDown) {
                holdDownHeal = false;
                if (this.getActivePower() == HEAL_CHARGE && this.attackTimeDuring < MAX_HEAL_CHARGE_TICKS) {
                    this.healOthers = false;
                    tryIntPowerPacket(HEAL_CHARGE, 0);
                }
            }
            return;
        }
        super.buttonInput1(keyIsDown, options);
    }

    private void tryHeal() {
        if (!this.onCooldown(PowerIndex.SKILL_1_SNEAK)
                && this.getActivePower() != HEAL_CHARGE
                && this.getActivePower() != HEAL
                && canExecuteMoveWithLevel(getHealLevel())) {
            this.holdDownHeal = true;
            this.healOthers = true;
            this.tryPower(HEAL_CHARGE, true);
            tryPowerPacket(HEAL_CHARGE);
        }
    }

    private boolean startHealCharge() {
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)) {
            this.hasHealed = false;
            this.healTargetId = -1;
            if (!this.self.level().isClientSide()) {
                this.healOthers = true;
            }
            this.setAttackTimeDuring(0);
            this.setActivePower(HEAL_CHARGE);
            this.animateStand(GoldExperienceEntity.HEAL_WINDUP);
            this.poseStand(OffsetIndex.GUARD);
            return true;
        }
        return false;
    }

    private void updateHealCharge() {
        if (this.attackTimeDuring >= MAX_HEAL_CHARGE_TICKS) {
            if (this.self.level().isClientSide() && isPacketPlayer()) {
                this.healOthers = this.holdDownHeal;
                this.holdDownHeal = false;
            }
            startHealRelease();
        }
    }

    private boolean startHealRelease() {
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)) {
            this.hasHealed = false;
            this.setAttackTimeDuring(0);
            this.setActivePower(HEAL);
            if (!this.self.level().isClientSide()) {
                if (this.healOthers) {
                    Entity target = MainUtil.getTargetEntity(this.getSelf(), 5f);
                    this.healTargetId = (target instanceof LivingEntity livingHeal) ? livingHeal.getId() : -1;
                    this.animateStand(GoldExperienceEntity.HEAL_OTHERS);
                } else {
                    this.healTargetId = -1;
                    this.animateStand(GoldExperienceEntity.HEAL_SELF);
                }
            }
            return true;
        }
        return false;
    }

    private void updateHeal() {
        if (this.attackTimeDuring >= 7 && !this.hasHealed) {
            finishHeal();
        }
    }

    private void finishHeal() {
        if (!this.self.level().isClientSide()) {
            this.hasHealed = true;
            if (this.healOthers) {
                Entity lockedTarget = this.healTargetId != -1
                        ? this.self.level().getEntity(this.healTargetId)
                        : MainUtil.getTargetEntity(this.getSelf(), 5f);
                if (lockedTarget instanceof LivingEntity livingHeal && livingHeal.isAlive()) {
                    livingHeal.heal(geConfig().initialHealBurst);
                    livingHeal.addEffect(new MobEffectInstance(ModEffects.RECONSTRUCTION, 400, 0, false, true, true));
                    playSoundIfPossible(this.self.level(), livingHeal, ModSounds.GOLD_EXPERIENCE_LIFE_CREATION_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            } else {
                this.self.heal(geConfig().initialHealBurst);
                this.self.addEffect(new MobEffectInstance(ModEffects.RECONSTRUCTION, 400, 0, false, true, true));
                playSoundIfPossible(this.self.level(), this.self, ModSounds.GOLD_EXPERIENCE_LIFE_CREATION_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
            this.setAttackTimeDuring(-20);
            setCooldown(PowerIndex.SKILL_1_SNEAK, getHealCooldown());
        }
    }

    // heal end

    // wall vines start

    private void tryWallVines() {
        if (!this.onCooldown(PowerIndex.SKILL_3)) {
            ((StandUser) this.getSelf()).roundabout$tryPower(WALL_VINES, true);
            tryPowerPacket(WALL_VINES);
        }
    }

    // this move was actually stupidly easy to make. it's like if diver zip didn't have any of the convuluted gravity nonsense.
    private boolean executeWallVines() {
        if (this.self.level().isClientSide()) return false;
        Level level = this.self.level();
        Direction facing = RotationUtil.getRealFacingDirection2(this.self);
        Direction wallFace = facing.getOpposite();

        BlockPos startPos = this.self.blockPosition().below();
        boolean createdAny = false;

        // loop for 30 blocks, or until you hit a non-valid block. whichever happens first.
        for (int i = 0; i < 30; i++) {
            BlockPos currentAirPos = startPos.above(i);
            BlockPos wallPos = currentAirPos.relative(facing);

            BlockState airState = level.getBlockState(currentAirPos);
            BlockState wallState = level.getBlockState(wallPos);

            if (airState.canBeReplaced() && VineBlock.isAcceptableNeighbour(level, wallPos, facing)) {
                BlockState vineState = ModBlocks.GE_VINE.defaultBlockState()
                        .setValue(VineBlock.getPropertyForFace(facing), true);

                if (level.setBlock(currentAirPos, vineState, 3)) {
                    // Schedule despawn tick on the custom GE_VINE block
                    level.scheduleTick(currentAirPos, ModBlocks.GE_VINE, 600);
                    createdAny = true;
                }
            } else if (i > 2) {
                break;
            }
        }

        if (createdAny) {
            this.setCooldown(PowerIndex.SKILL_3, getVineCooldown());
            playSoundIfPossible(level, null, this.self.blockPosition(),
                    ModSounds.GOLD_EXPERIENCE_VINE_EVENT, SoundSource.PLAYERS, 0.9F, 1.0F);
            addEXP(5);
        }

        return createdAny;
    }

    /*
    taken from DD and WH. checks to see if you can wall latch for vines
     */
    private boolean canLatchOntoWall() {
        if (self.isInWater())
            return false;
        if (MainUtil.isStandingInBlock(self))
            return false;
        if (this.self.onGround())
            return false;

        Vec3 mpos = this.self.getPosition(1F);
        Direction gravdir = ((IGravityEntity) this.self).roundabout$getGravityDirection();
        switch (gravdir) {
            case DOWN -> mpos = mpos.add(0, 0.1F, 0);
            case UP -> mpos = mpos.add(0, -0.1F, 0);
            case NORTH -> mpos = mpos.add(0, 0, 0.1F);
            case SOUTH -> mpos = mpos.add(0, 0, -0.1F);
            case WEST -> mpos = mpos.add(0.1F, 0, 0);
            case EAST -> mpos = mpos.add(-0.1F, 0, 0);
        }
        Direction rd = RotationUtil.getRealFacingDirection2(this.self);
        if (rd == gravdir || rd == gravdir.getOpposite())
            return false;

        BlockPos feetWall = BlockPos.containing(mpos).relative(rd);
        BlockPos eyeWall = BlockPos.containing(this.self.getEyePosition()).relative(rd);

        return MainUtil.isBlockWalkable(this.self.level().getBlockState(feetWall))
                || MainUtil.isBlockWalkable(this.self.level().getBlockState(eyeWall));
    }

    // wall vines end

    // bramble bush start

    private void tryGrowBush() {
        if(!this.onCooldown(PowerIndex.GENERAL_4) && canExecuteMoveWithLevel(getBushLevel())) {
            ((StandUser) this.getSelf()).roundabout$tryPower(BUSH_CREATION, true);
            tryPowerPacket(BUSH_CREATION);
        }
    }

    private boolean startBushWindup() {
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)) {
            this.setAttackTimeDuring(0);
            this.setActivePower(BUSH_CREATION);
            this.animateStand(GoldExperienceEntity.GROUND_PUNCH);
            this.poseStand(OffsetIndex.GUARD);
            return true;
        }
        return false;
    }

    private void updateBush() {
        if (this.attackTimeDuring >= 10) {
            finishBushCreation();
        }
    }

    private void finishBushCreation() {
        if (!this.self.level().isClientSide()) {
            Level level = this.self.level();
            BlockPos origin = this.self.blockPosition();
            BlockState bushState = ModBlocks.GE_BUSH.defaultBlockState();

            this.pendingBushes.clear();
            this.bushWaveOrigin = new Vec3(origin.getX() + 0.5, origin.getY(), origin.getZ() + 0.5);
            this.bushWaveTick = 0;

            int radius = 4;
            double radiusSq = radius * radius;
            for (BlockPos columnPos : BlockPos.betweenClosed(
                    origin.offset(-radius, 0, -radius),
                    origin.offset(radius, 0, radius))) {

                double distSq = columnPos.distSqr(origin);
                if (distSq > radiusSq) continue;
                if (level.random.nextFloat() > 0.70F) continue;

                for (int yOffset : new int[]{0, 1, -1}) {
                    BlockPos checkPos = columnPos.above(yOffset);
                    BlockState checkState = level.getBlockState(checkPos);

                    if (checkState.canBeReplaced()
                            && !checkState.is(ModBlocks.GE_BUSH)
                            && bushState.canSurvive(level, checkPos)) {
                        this.pendingBushes.add(new BushSpawnData(checkPos.immutable(), distSq));
                        break;
                    }
                }
            }

            this.pendingBushes.sort(Comparator.comparingDouble(BushSpawnData::distSq));

            if (!this.pendingBushes.isEmpty()) {
                playSoundIfPossible(level, null, origin,
                        ModSounds.GOLD_EXPERIENCE_LIFE_CREATION_EVENT, SoundSource.PLAYERS, 0.85F, 1.0F);
                if (level instanceof ServerLevel serverLevel) {
                    sendParticlesIfPossible(serverLevel, ModParticles.GE_WAVE,
                            this.bushWaveOrigin.x, this.bushWaveOrigin.y, this.bushWaveOrigin.z,
                            1, 0.0, 0.0, 0.0, 0.0);
                }
            } else {
                this.bushWaveOrigin = null;
            }

            addEXP(5);
            this.setCooldown(PowerIndex.GENERAL_4, getBushCooldown());
            this.setAttackTimeDuring(-8);
        }
    }

    // bramble bush end

    // kick barrage start

    @Override
    public void buttonInputBarrage(boolean keyIsDown, Options options) {
        if (hasHandsOut())
            return;
        if (keyIsDown) {
            if (isHoldingSneak() && (this.getAttackTime() >= this.getAttackTimeMax() ||
                    (this.getActivePowerPhase() != this.getActivePowerPhaseMax()))) {
                this.tryPower(PowerIndex.BARRAGE_CHARGE_2, true);
                tryPowerPacket(PowerIndex.BARRAGE_CHARGE_2);
            } else {
                super.buttonInputBarrage(keyIsDown, options);
            }
        }
    }

    //all of ge's moves are uncancellable, so this can go here
    @Override
    public boolean canInterruptPower(DamageSource sauce, Entity interrupter){
        if (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2) {
            return true;
        }
        return super.canInterruptPower(sauce, interrupter);
    }

    // kick barrage end

    // DNA creation start

    private boolean executeDNACreate() {
        if (this.self.level().isClientSide()) return false;
        ItemStack heldItem = this.self.getMainHandItem();
        if (heldItem.isEmpty()) return false;
        Item dnaItem = heldItem.getItem();
        if (!MainUtil.GOLD_EXPERIENCE_DNA_DROPS.containsKey(dnaItem)) return false;

        // Spawn the mob
        DNACreationEntity spawner = ModEntities.DNA_CREATION_SPAWNER.create(this.self.level());
        if (spawner != null) {
            Vec3 chestPos = new Vec3(this.self.getX(), this.self.getY() + 1.2D, this.self.getZ());
            Vec3 spawnPos = chestPos.add(this.self.getLookAngle().scale(1.3D));
            spawner.moveTo(spawnPos.x, spawnPos.y, spawnPos.z, this.self.level().random.nextFloat() * 360.0F, 0.0F);

            // need to pass the item for the spinny thingy
            spawner.setDNAItem(heldItem);

            this.self.level().addFreshEntity(spawner);
            heldItem.shrink(1);

            playSoundIfPossible(self.level(), null, this.self.blockPosition(),
                    ModSounds.GOLD_EXPERIENCE_LIFE_CREATION_EVENT, SoundSource.PLAYERS, 0.85F, 1F);

            setCooldown(PowerIndex.GENERAL_EXTRA, getDNACooldown());
            return true;
        }

        return false;
    }

    private void tryDNACreate() {
        if (!this.onCooldown(PowerIndex.GENERAL_EXTRA)) {
            ((StandUser) this.getSelf()).roundabout$tryPower(DNA_CREATE, true);
            tryPowerPacket(DNA_CREATE);
        }
    }

    //checks to see if the player is holding a valid item
    private boolean canDNACreate() {
        ItemStack heldItem = this.self.getMainHandItem();
        if (!heldItem.isEmpty() && MainUtil.GOLD_EXPERIENCE_DNA_DROPS.containsKey(heldItem.getItem())) {
            return true;
        }
        return false;
    }

    // DNA creation end

    // life punch start

    private void tryLifePunch() {
        if (!this.onCooldown(PowerIndex.GENERAL_3) && canExecuteMoveWithLevel(getLifePunchLevel())) {
            if (this.activePower == LIFE_PUNCH) {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                tryPowerPacket(PowerIndex.NONE);
            } else {
                ((StandUser) this.getSelf()).roundabout$tryPower(LIFE_PUNCH, true);
                tryPowerPacket(LIFE_PUNCH);
            }
        }
    }

    private boolean startLifePunch() {
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)) {
            this.setAttackTimeDuring(0);
            this.setActivePower(LIFE_PUNCH);
            playSoundsIfNearby(IMPALE_NOISE, 27, false);
            this.animateStand(GoldExperienceEntity.LIFE_PUNCH);
            this.poseStand(OffsetIndex.GUARD);
            return true;
        }
        return false;
    }

    private void updateLifePunch() {
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > 24) {
                this.standLifePunch();
            } else {
                if (!this.getSelf().level().isClientSide()) {
                    // oooooh i'm sooo menacing ooooh
                    if (this.attackTimeDuring % 4 == 0) {
                        sendParticlesIfPossible(self.level(), ModParticles.MENACING,
                                this.getSelf().getX(), this.getSelf().getY() + 0.3, this.getSelf().getZ(),
                                1, 0.2, 0.2, 0.2, 0.05);
                    }
                }
            }
        }
    }

    private void standLifePunch() {
        if (this.self instanceof Player) {
            if (isPacketPlayer()) {
                this.setAttackTimeDuring(-25);
                impaleTicks = 20;
                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK, getTargetEntityId2(impaleRange));
            }
        } else {
            // mob ai stuff i guess? it makes sense because this is outside the play check.
            Entity targetEntity = getTargetEntity(this.self, impaleRange);
            //this is basically a reskinned impale
            lifePunchImpact(targetEntity);
        }
    }

    private void lifePunchImpact(Entity entity) {
        if (this.activePower == LIFE_PUNCH) {
            this.setAttackTimeDuring(-25);
            float range = 3.5F + 0.75F;

            if (entity != null && entity.distanceTo(self) > range) {
                entity = null;
            }

            if (entity != null) {
                hitParticlesCenter(entity);
                float pow = getHeavyPunchStrength(entity);
                float knockbackStrength = 0.5F;

                if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                    if (entity instanceof LivingEntity LE) {
                        addEXP(5, LE);
                        if (!this.self.level().isClientSide()) {
                            if(LE instanceof Player) {
                                // IMPORTANT FAILSAFE: If someone else gets Life Punched, restore previous victim first
                                if (lifePunchVictim != null && lifePunchVictim != LE) {
                                    restoreLifePunchVictim();
                                }
                                // punch the SOUL outta them
                                PowerTypes.setPlaneOfExisting(LE, (byte) 12);
                                // spawn a Life Punch Clone in their place
                                LifePunchCloneEntity clone = ModEntities.LIFE_PUNCH_CLONE.create(this.self.level());
                                if (clone != null) {
                                    clone.setPlayer((Player) LE);
                                    clone.copyPosition(LE);
                                    clone.setHealth(LE.getHealth());
                                    clone.getAttribute(Attributes.MAX_HEALTH).setBaseValue(LE.getMaxHealth());
                                    // copy inventory
                                    for (EquipmentSlot slot : EquipmentSlot.values()) {
                                        clone.setItemSlot(slot, LE.getItemBySlot(slot).copy());
                                    }
                                    clone.setTarget((LivingEntity) this.self);
                                    this.self.level().addFreshEntity(clone);
                                    ((StandUser) LE).roundabout$setLifePunchClone(clone);
                                    // track the victim
                                    lifePunchVictim = LE;
                                    lifePunchClone = clone;
                                    lifePunchTimer = 40; // 2 seconds
                                    lastKnownHealth = clone.getHealth();
                                }
                            } else if (LE instanceof Mob mob) {
                                ((StandUser) mob).roundabout$deeplyRemoveAttackTarget();
                                mob.getNavigation().stop();
                                disabledMobTimers.put(LE.getId(), 40);
                            }
                        }
                    }
                    takeDeterminedKnockback(this.self, entity, knockbackStrength);
                } else {
                    knockShield2(entity, 100);
                }
                SoundEvent sound = entity != null ? ModSounds.GOLD_EXPERIENCE_LIFE_PUNCH_EVENT : ModSounds.PUNCH_2_SOUND_EVENT;
                if (!this.self.level().isClientSide()) {
                    playSoundIfPossible(self.level(), null, this.self.blockPosition(), sound, SoundSource.PLAYERS, 0.95F, 1.1F);
                }
                if (this.getSelf() instanceof Player serverPlayer && !this.getSelf().level().isClientSide()) {
                    S2CPacketUtil.sendCooldownSyncPacket((ServerPlayer) serverPlayer, PowerIndex.GENERAL_3, getLifePunchCooldown() + 200);
                }
                this.setCooldown(PowerIndex.GENERAL_3, getLifePunchCooldown() + 200);
                return;
            }
            // all of this is for the block version of life punch
            else {
                BlockHitResult blockHit = this.getRayBlockOutline(this.self, range);

                if (blockHit.getType() == HitResult.Type.BLOCK) {
                    BlockPos centerPos = blockHit.getBlockPos();
                    BlockState centerState = this.self.level().getBlockState(centerPos);
                    ItemStack boneMeal = new ItemStack(Items.BONE_MEAL);

                    if (!this.self.level().isClientSide) {
                        if (centerState.is(BlockTags.LOGS)) {
                            Set<BlockPos> logsFound = new HashSet<>();
                            Set<BlockPos> leavesFound = new HashSet<>();
                            Queue<BlockPos> queue = new LinkedList<>();
                            boolean foundNaturalLeaves = false;

                            queue.add(centerPos);
                            logsFound.add(centerPos);

                            // need to limit these, otherwise some guy's pc might genuinely explode
                            int logLimit = 50;
                            int leafLimit = 100;

                            // algorithm (breadth first search) for looking for logs and leaves
                            while (!queue.isEmpty() && logsFound.size() < logLimit) {
                                BlockPos currentPos = queue.poll();
                                for (int x = -1; x <= 1; x++) {
                                    for (int y = -1; y <= 1; y++) {
                                        for (int z = -1; z <= 1; z++) {
                                            if (x == 0 && y == 0 && z == 0) continue;

                                            BlockPos checkPos = currentPos.offset(x, y, z);
                                            BlockState checkState = this.self.level().getBlockState(checkPos);

                                            if (checkState.is(BlockTags.LEAVES) && !leavesFound.contains(checkPos)) {
                                                if (leavesFound.size() < leafLimit) {
                                                    leavesFound.add(checkPos);
                                                }
                                                // check to make sure that the leaves haven't been sheared
                                                if (checkState.hasProperty(BlockStateProperties.PERSISTENT)) {
                                                    if (!checkState.getValue(BlockStateProperties.PERSISTENT)) {
                                                        foundNaturalLeaves = true;
                                                    }
                                                } else {
                                                    foundNaturalLeaves = true;
                                                }
                                            } else if (checkState.is(BlockTags.LOGS) && !logsFound.contains(checkPos)) {
                                                logsFound.add(checkPos);
                                                queue.add(checkPos);
                                            }
                                        }
                                    }
                                }
                            }

                            if (foundNaturalLeaves) {
                                // need to throw the logs like anubis block slice, so they go where the player is looking.
                                Vec3 viewVector = this.getSelf().getViewVector(0);

                                // leaves needs to be cleared, otherwise they will block logs
                                Set<BlockPos> clearedLeaves = new HashSet<>();
                                for (BlockPos logPos : logsFound) {
                                    for (int x = -2; x <= 2; x++) {
                                        for (int y = -2; y <= 2; y++) {
                                            for (int z = -2; z <= 2; z++) {
                                                BlockPos checkPos = logPos.offset(x, y, z);
                                                BlockState checkState = this.self.level().getBlockState(checkPos);
                                                if (checkState.is(BlockTags.LEAVES) && !clearedLeaves.contains(checkPos)) {
                                                    this.self.level().destroyBlock(checkPos, false);
                                                    clearedLeaves.add(checkPos);
                                                }
                                            }
                                        }
                                    }
                                }

                                // launch the logs with respect to where the player is looking
                                for (BlockPos pos : logsFound) {
                                    BlockState logState = this.self.level().getBlockState(pos);
                                    ItemStack logItem = new ItemStack(logState.getBlock().asItem());
                                    ThrownObjectEntity projectile = new ThrownObjectEntity(this.self, this.self.level(), logItem, false);
                                    projectile.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                                    projectile.setDeltaMovement(
                                            // note: randomness is always * x - (0.5x)
                                            viewVector.x * 1.2 + (Math.random() * 1.0 - 0.5),
                                            viewVector.y * 1.2 + (Math.random() * 0.5 - 0.25),
                                            viewVector.z * 1.2 + (Math.random() * 1.0 - 0.5)
                                    );
                                    projectile.setStyle(ThrownObjectEntity.SPTHROW);
                                    this.self.level().addFreshEntity(projectile);
                                    this.self.level().destroyBlock(pos, false);
                                }
                                this.self.level().playSound(null, centerPos, SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, SoundSource.PLAYERS, 1.2F, 0.8F);
                            }
                        }
                        // special logic for saplings that way they insta grow
                        else if (centerState.is(BlockTags.SAPLINGS)) {
                            // bonemeal spalings up to 10 times, breaking the loop early if the block is no longer a sapling
                            for (int i = 0; i < 10; i++) {
                                BoneMealItem.growCrop(boneMeal, this.self.level(), centerPos);
                                if (!this.self.level().getBlockState(centerPos).is(BlockTags.SAPLINGS)) {
                                    break;
                                }
                            }
                            this.self.level().levelEvent(1505, centerPos, 0);
                        }
                        // if the punch was on grass, it should only apply once because vanilla bonemeal is already super big
                        else if (centerState.is(Blocks.GRASS_BLOCK)) {
                            if (BoneMealItem.growCrop(boneMeal, this.self.level(), centerPos)) {
                                this.self.level().levelEvent(1505, centerPos, 0);
                            }
                        } else {
                            // anything else gets the radius, outside of grass blocks
                            int radius = 3;
                            for (int x = -radius; x <= radius; x++) {
                                for (int y = -2; y <= 2; y++) {
                                    for (int z = -radius; z <= radius; z++) {
                                        BlockPos targetPos = centerPos.offset(x, y, z);
                                        BlockState targetState = this.self.level().getBlockState(targetPos);

                                        //ignore grass because it spawns a LOT of grass if it goes on them lol
                                        if (targetState.is(Blocks.GRASS_BLOCK)) {
                                            continue;
                                        }

                                        if (BoneMealItem.growCrop(boneMeal, this.self.level(), targetPos) ||
                                                BoneMealItem.growWaterPlant(boneMeal, this.self.level(), targetPos, null)) {

                                            this.self.level().levelEvent(1505, targetPos, 0);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    addEXP(5);
                    if (!this.self.level().isClientSide()) {
                        playSoundIfPossible(self.level(), null, this.self.blockPosition(), ModSounds.GOLD_EXPERIENCE_LIFE_CREATION_EVENT, SoundSource.PLAYERS, 0.95F, 1F);
                    }
                }
            }

            if (this.getSelf() instanceof Player serverPlayer && !this.getSelf().level().isClientSide()) {
                S2CPacketUtil.sendCooldownSyncPacket((ServerPlayer) serverPlayer, PowerIndex.GENERAL_3, getLifePunchCooldown());
            }
            this.setCooldown(PowerIndex.GENERAL_3, getLifePunchCooldown());
        }
    }

    private void restoreLifePunchVictim() {
        if (lifePunchVictim != null) {
            PowerTypes.setPlaneOfExisting(lifePunchVictim, (byte)0);
            ((StandUser) lifePunchVictim).roundabout$setLifePunchClone(null);
        }
        if (lifePunchClone != null) {
            lifePunchClone.discard();
        }
        lifePunchVictim = null;
        lifePunchClone = null;
        lifePunchTimer = 0;
        lastKnownHealth = 0;
    }

    // no lerp when teleporting
    private final void packetNearby(Vector3f blip, int entId) {
        if (!this.self.level().isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) this.self.level());
            Vec3 userLocation = new Vec3(this.self.getX(),  this.self.getY(), this.self.getZ());
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) this.self.level()).players().get(j);

                if (((ServerLevel) serverPlayerEntity.level()) != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(userLocation, 100)) {
                    S2CPacketUtil.sendBlipPacket(serverPlayerEntity, (byte) 2, entId,blip);
                }
            }
        }
    }

    // life punch is used for stand rejection as wlel, so i need to have a method for both rejection and normal tick
    private void tickLifePunch() {
        // tick down Life Punch timer
        if (lifePunchTimer > 0 && lifePunchVictim != null && lifePunchClone != null) {
            if (!this.self.isAlive()) {
                restoreLifePunchVictim();
            }
            // check if either entity is gone
            if (!lifePunchVictim.isAlive() || !lifePunchClone.isAlive()) {
                if (lifePunchVictim.isAlive()) {
                    // clone died, victim takes fatal damage
                    lifePunchVictim.hurt(lifePunchVictim.damageSources().generic(), 9999);
                }
                restoreLifePunchVictim();
            } else {
                // transfer damage from clone to victim
                float currentHealth = lifePunchClone.getHealth();
                if (currentHealth < lastKnownHealth) {
                    float damage = lastKnownHealth - currentHealth;
                    lifePunchVictim.hurt(lifePunchVictim.damageSources().generic(), damage);
                    lifePunchClone.setHealth(lastKnownHealth); // reset clone health
                }
                lastKnownHealth = lifePunchClone.getHealth();

                // transfer effects from clone to victim
                for (MobEffectInstance effect : lifePunchClone.getActiveEffects()) {
                    if (!lifePunchVictim.hasEffect(effect.getEffect())) {
                        lifePunchVictim.addEffect(new MobEffectInstance(effect));
                    }
                }

                // tick down timer
                lifePunchTimer--;
                if (lifePunchTimer <= 0) {
                    // restore the victim
                    Vector3f blipVec = new Vector3f(
                            (float) lifePunchClone.getX(),
                            (float) lifePunchClone.getY(),
                            (float) lifePunchClone.getZ()
                    );
                    packetNearby(blipVec, lifePunchVictim.getId());
                    lifePunchVictim.teleportTo(lifePunchClone.getX(), lifePunchClone.getY(), lifePunchClone.getZ());
                    lifePunchVictim.setYRot(lifePunchClone.getYRot());
                    lifePunchVictim.setXRot(lifePunchClone.getXRot());
                    lifePunchVictim.setDeltaMovement(Vec3.ZERO);
                    restoreLifePunchVictim();
                }
            }
        }

        // for mob life punch stuff
        Iterator<Map.Entry<Integer, Integer>> it = disabledMobTimers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> entry = it.next();
            int mobId = entry.getKey();
            int ticksLeft = entry.getValue();
            Entity mobEntity = this.self.level().getEntity(mobId);

            if (mobEntity == null || !mobEntity.isAlive() || ticksLeft <= 0) {
                if (mobEntity instanceof Mob mob) {
                    mob.getNavigation().recomputePath();
                }
                it.remove();
            } else {
                if (mobEntity instanceof Mob mob) {
                    ((StandUser) mob).roundabout$deeplyRemoveAttackTarget();
                    mob.getNavigation().stop();
                }
                entry.setValue(ticksLeft - 1);
            }
        }
    }

    // life punch end

    // move speed modifiers and the sort

    @Override
    public float inputSpeedModifiers(float basis) {
        if (this.activePower == REQUIEM) { return 0f; }
        if (this.activePower == PowerIndex.BARRAGE_CHARGE_2) {
            basis*=0.5f;
        } else if (this.activePower == PowerIndex.BARRAGE_2) {
            basis *= 0.3f;
        }

        if (this.getActivePower() == HEAL || this.getActivePower() == HEAL_CHARGE){
            // slows you down by 70% while healing
            basis *= 0.7f;
        }
        return super.inputSpeedModifiers(basis);
    }

    @Override
    public boolean cancelItemUse() {
        return (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2 || this.getActivePower() == PowerIndex.BARRAGE_2 || this.getActivePower() == REQUIEM);
    }

    @Override
    public boolean clickRelease(){
        return (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2 || this.getActivePower() == PowerIndex.BARRAGE_2);
    }

    @Override
    public boolean cancelSprintJump(){
        if (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2 || this.getActivePower() == PowerIndex.BARRAGE_2
            || this.getActivePower() == HEAL || this.getActivePower() == HEAL_CHARGE || this.getActivePower() == REQUIEM) {
            return true;
        }
        return super.cancelSprintJump();
    }

    @Override
    public boolean cancelJump(){
        return getActivePower() == REQUIEM;
    }

    public boolean cancelSprintParticles(){
        return getActivePower() == REQUIEM;
    }
    /**Cancel all sprinting*/
    public boolean cancelSprint(){
        return getActivePower() == REQUIEM;
    }

    // end of move methods

    // levels start

    @Override
    public byte getMaxLevel() {
        return 7;
    }

    @Override
    public void levelUp() {
        if (!this.getSelf().level().isClientSide() && this.getSelf() instanceof Player PE) {
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            byte level = ipe.roundabout$getStandLevel();
            if (level == 7) {
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.max.both").
                        withStyle(ChatFormatting.AQUA), true);
            } else {
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.both").
                        withStyle(ChatFormatting.AQUA), true);
            }
        }
        super.levelUp();
    }

    @Override
    public int getExpForLevelUp(int currentLevel) {
        int amt;
        if (currentLevel == 1) {
            amt = 100;
        } else {
            amt = (100 + ((currentLevel - 1) * 65));
        }
        amt = (int) (amt * getLevelMultiplier());
        return amt;
    }

    // levels end

    // sounds start

    @Override
    protected Byte getSummonSound() {return SoundIndex.SUMMON_SOUND;}

    @Override
    public Byte getLastHitSound(){

        double rand = Math.random();
        byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        return LAST_HIT_NOISE;
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.SUMMON_GOLD_EXPERIENCE_EVENT;
        } else if (soundChoice == IMPALE_NOISE || soundChoice == SoundIndex.ALT_CHARGE_SOUND_1) {
            return ModSounds.GOLD_EXPERIENCE_LIFE_PUNCH_WINDUP_EVENT;
        } else if (soundChoice == BARRAGE_NOISE) {
            return ModSounds.GOLD_EXPERIENCE_MUDA_RUSH_EVENT;
        } else if (soundChoice == LAST_HIT_NOISE) {
            return ModSounds.GOLD_EXPERIENCE_MUDA_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    @Override
    public SoundEvent getBarrageChargeSound() {
        if (!PowerTypes.hasStandActive(self)) {
            return super.getBarrageChargeSound();
        }
        return ModSounds.GOLD_EXPERIENCE_LIFE_PUNCH_WINDUP_EVENT;
    }

    @Override
    public void playBarrageEndNoise(float mod, Entity entity) {
        if (!this.self.level().isClientSide()) {
            playSoundIfPossible(self.level(), null, this.self.blockPosition(), ModSounds.GOLD_EXPERIENCE_LIFE_PUNCH_EVENT, SoundSource.PLAYERS, 0.75F + mod, 1F);
        }
    }

    @Override
    public void playBarrageNoise(int hitNumber, Entity entity) {
        if (!this.self.level().isClientSide()) {
            if (hitNumber % 3 == 0)
                playSoundIfPossible(self.level(), null, this.self.blockPosition(), ModSounds.GOLD_EXPERIENCE_HIT_EVENT, SoundSource.PLAYERS, 0.7F, 1F);
        }
    }

    @Override
    public void playKickBarrageNoise(int hitNumber, Entity entity){
        if (!this.getSelf().level().isClientSide()) {
            if (!this.getSelf().level().isClientSide() && (((TimeStop)this.getSelf().level()).CanTimeStopEntity(entity))) {
                playBarrageBlockNoise();
            } else {
                if (hitNumber % 3 == 0) {
                    playSoundIfPossible(self.level(),null, this.getSelf().blockPosition(), ModSounds.GOLD_EXPERIENCE_HIT_EVENT, SoundSource.PLAYERS, 0.7F, 1F);
                }
            }
        }
    }

    @Override
    public byte chooseBarrageSound(){
        double rand = Math.random();
        byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        return BARRAGE_NOISE;
    }

    @Override
    public float getPunchLandPitch() {
        return 1F + 0.05F * activePowerPhase;
    }

    @Override
    public float getPunchLandLastPitch() {
        return 1F;
    }

    @Override
    public SoundEvent getPunchLandSound() {
        return ModSounds.GOLD_EXPERIENCE_HIT_EVENT;
    }

    @Override
    public SoundEvent getPunchLandLastSound() {
        return ModSounds.GOLD_EXPERIENCE_LIFE_PUNCH_EVENT;
    }

    @Override
    public SoundEvent getImpaleSound() {
        return ModSounds.GOLD_EXPERIENCE_LIFE_PUNCH_EVENT;
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        if (!this.self.level().isClientSide &&
                (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2 || this.getActivePower() == PowerIndex.BARRAGE_2)
                && (move != PowerIndex.BARRAGE_2 && move != PowerIndex.BARRAGE_CHARGE_2)){
            this.stopSoundsIfNearby(SoundIndex.BARRAGE_SOUND_GROUP, 100,false);
        }

        return super.tryPower(move, forced);
    }

    @Override
    public byte getSoundCancelingGroupByte(byte soundChoice) {
        if (soundChoice == BARRAGE_NOISE) {
            return SoundIndex.BARRAGE_SOUND_GROUP;
        } else if (soundChoice == SoundIndex.ALT_CHARGE_SOUND_1) {
            return SoundIndex.BARRAGE_SOUND_GROUP;
        }
        return super.getSoundCancelingGroupByte(soundChoice);
    }

    // sounds end

    // skins and entity start

    @Override
    public Component getSkinName(byte skinId) {
        switch (skinId) {
            case GoldExperienceEntity.BETA -> {
                return Component.translatable("skins.roundabout.gold_experience.beta");
            }
            case GoldExperienceEntity.DIAMOND -> {
                return Component.translatable("skins.roundabout.gold_experience.diamond");
            }
            case GoldExperienceEntity.GUARD -> {
                return Component.translatable("skins.roundabout.gold_experience.guard");
            }
            case GoldExperienceEntity.PS2 -> {
                return Component.translatable("skins.roundabout.gold_experience.ps2");
            }
            default -> {
                return Component.translatable("skins.roundabout.gold_experience.base");
            }
        }
    }

    @Override
    public List<Byte> getSkinList() {
        List<Byte> l = Lists.newArrayList();
        l.add(GoldExperienceEntity.PART_5);
        if (this.getSelf() instanceof Player PE) {
            byte Level = ((IPlayerEntity) PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser) PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (Level > 1 || bypass) {
            }
            if (Level > 2 || bypass) {
            }
            if (Level > 3 || bypass) {
                l.add(GoldExperienceEntity.PS2);
            }
            if (Level > 4 || bypass) {
                l.add(GoldExperienceEntity.DIAMOND);
            }
            if (Level > 5 || bypass) {
                l.add(GoldExperienceEntity.GUARD);
            }
            if (Level > 6 || bypass) {
                l.add(GoldExperienceEntity.BETA);
            }
            if (((IPlayerEntity) PE).roundabout$getUnlockedBonusSkin() || bypass) {
                // deimos GE goes here
            }
        }

        return l;
    }

    @Override
    public StandEntity getNewStandEntity() {
        byte skin = ((StandUser) this.getSelf()).roundabout$getStandSkin();
        if (skin == GoldExperienceEntity.BETA) {
            return ModEntities.GOLD_EXPERIENCE_BETA.create(this.getSelf().level());
        }
        return ModEntities.GOLD_EXPERIENCE.create(this.getSelf().level());
    }

    // skins and entity end

    // stand icons and info screen start

    /*
     * Renders the icons for the stand's abilities on the GUI. Comments will specify
     * if icon is for courching, blocking, or normal.
     */
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        // Ability 1 (Z)
        if (isGuarding()){
            setSkillIcon(context, x, y, 1, StandIcons.GOLD_EXPERIENCE_LIFE_SENSOR, PowerIndex.GENERAL_1);
        } else if (isHoldingSneak()) {
            if (canExecuteMoveWithLevel(getHealLevel())) {
                setSkillIcon(context, x, y, 1, StandIcons.GOLD_EXPERIENCE_HEAL, PowerIndex.SKILL_1_SNEAK);
            } else {
                setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD, true);
            }
        } else if (canDNACreate()) {
            setSkillIcon(context, x, y, 1, StandIcons.GOLD_EXPERIENCE_DNA_CREATION, PowerIndex.GENERAL_EXTRA);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.GOLD_EXPERIENCE_LIFEFORM_SELECT, PowerIndex.SKILL_1);
        }
        // Ability 2 (X)
        if (isGuarding()){
            if (canExecuteMoveWithLevel(getTreeLevel())) {
                setSkillIcon(context, x, y, 2, StandIcons.GOLD_EXPERIENCE_TREE_GROWTH, PowerIndex.GENERAL_2);
            } else {
                setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.NO_CD, true);
            }
        } else if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 2, StandIcons.GOLD_EXPERIENCE_ITEM_THROW, PowerIndex.SKILL_2);
        } else {
            setSkillIcon(context, x, y, 2, StandIcons.GOLD_EXPERIENCE_LIFEFORM_CREATION, PowerIndex.SKILL_2);
        }
        // Ability 3 (C)
        if (isGuarding()){
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        } else if (isHoldingSneak()) {
            if (canExecuteMoveWithLevel(getLifePunchLevel())) {
                setSkillIcon(context, x, y, 3, StandIcons.GOLD_EXPERIENCE_LIFE_PUNCH, PowerIndex.GENERAL_3);
            } else {
                setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NO_CD, true);
            }
        } else if (canLatchOntoWall()) {
            setSkillIcon(context, x, y, 3, StandIcons.GOLD_EXPERIENCE_VINE_CLIMB, PowerIndex.SKILL_3);
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        }
        // Ability 4 (V)
        if (isHoldingSneak()) {
            if (canExecuteMoveWithLevel(getItemImbueLevel())) {
                setSkillIcon(context, x, y, 4, StandIcons.GOLD_EXPERIENCE_ITEM_IMBUE, PowerIndex.SKILL_4_SNEAK);
            } else {
                setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD, true);
            }
        } else if (isGuarding()){
            if (canExecuteMoveWithLevel(getBrambleLevel())) {
                setSkillIcon(context, x, y, 4, StandIcons.GOLD_EXPERIENCE_BRAMBLE_TRAP, PowerIndex.GENERAL_4);
            } else {
                setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD, true);
            }
        } else {
            if (canExecuteMoveWithLevel(getBushLevel())) {
                setSkillIcon(context, x, y, 4, StandIcons.GOLD_EXPERIENCE_BRAMBLES, PowerIndex.SKILL_4);
            } else {
                setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD, true);
            }
        }
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        // lifeform creation
        if (slot == 2 && !isHoldingSneak() && !isGuarding() && !isHoldingItem()) {
            return true;
        }
        // item imbue
        if (slot == 4 && isHoldingSneak() && !isHoldingItem()) {
            return true;
        }
        return super.isAttackIneptVisually(activeP, slot);
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypas) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.punch",
                "instruction.roundabout.press_attack", StandIcons.GOLD_EXPERIENCE_PUNCH, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.guard",
                "instruction.roundabout.hold_block", StandIcons.GOLD_EXPERIENCE_GUARD, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.gold_experience_double_punch",
                "instruction.roundabout.press_skill", StandIcons.GOLD_EXPERIENCE_DOUBLE_PUNCH, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.barrage",
                "instruction.roundabout.barrage", StandIcons.GOLD_EXPERIENCE_BARRAGE, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 0, "ability.roundabout.kick_barrage",
                "instruction.roundabout.kick_barrage", StandIcons.GOLD_EXPERIENCE_KICK_BARRAGE, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 118, 0, "ability.roundabout.gold_experience_lifeform_select",
                "instruction.roundabout.press_skill", StandIcons.GOLD_EXPERIENCE_LIFEFORM_SELECT, 1, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 80, getHealLevel(), "ability.roundabout.gold_experience_heal",
                "instruction.roundabout.press_skill_crouch", StandIcons.GOLD_EXPERIENCE_HEAL, 1, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 99, 0, "ability.roundabout.gold_experience_life_sensor",
                "instruction.roundabout.press_skill_block", StandIcons.GOLD_EXPERIENCE_LIFE_SENSOR, 1, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 118, 0, "ability.roundabout.gold_experience_dna_creation",
                "instruction.roundabout.press_skill_item", StandIcons.GOLD_EXPERIENCE_DNA_CREATION, 1, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 77, topPos + 80, 0, "ability.roundabout.gold_experience_lifeform_creation",
                "instruction.roundabout.press_skill", StandIcons.GOLD_EXPERIENCE_LIFEFORM_CREATION, 2, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 77, topPos + 99, 0, "ability.roundabout.gold_experience_item_throw",
                "instruction.roundabout.press_skill_crouch", StandIcons.GOLD_EXPERIENCE_ITEM_THROW, 2, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 77, topPos + 118, getTreeLevel(), "ability.roundabout.gold_experience_tree_growth",
                "instruction.roundabout.press_skill_block", StandIcons.GOLD_EXPERIENCE_TREE_GROWTH, 2, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 96, topPos + 80, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE, 3, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 96, topPos + 99, getLifePunchLevel(), "ability.roundabout.gold_experience_life_punch",
                "instruction.roundabout.press_skill_crouch", StandIcons.GOLD_EXPERIENCE_LIFE_PUNCH, 3, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 96, topPos + 118, 0, "ability.roundabout.gold_experience_vine_climb",
                "instruction.roundabout.press_skill_air", StandIcons.GOLD_EXPERIENCE_VINE_CLIMB, 3, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 115, topPos + 80, getBrambleLevel(), "ability.roundabout.gold_experience_violent_brambles",
                "instruction.roundabout.press_skill", StandIcons.GOLD_EXPERIENCE_BRAMBLES, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 115, topPos + 99, getItemImbueLevel(), "ability.roundabout.gold_experience_item_imbue",
                "instruction.roundabout.press_skill_crouch", StandIcons.GOLD_EXPERIENCE_ITEM_IMBUE, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 115, topPos + 118, getBushLevel(), "ability.roundabout.gold_experience_bramble_trap",
                "instruction.roundabout.press_skill_block", StandIcons.GOLD_EXPERIENCE_BRAMBLE_TRAP, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 134, topPos + 80, getBrambleLevel(), "ability.roundabout.gold_experience_requiem",
                "instruction.roundabout.stand_arrow", StandIcons.GOLD_EXPERIENCE_REQUIEM, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 134, topPos + 99, getBrambleLevel(), "ability.roundabout.mining",
                "instruction.roundabout.hold_attack", StandIcons.GOLD_EXPERIENCE_MINING, 4, level, bypas));
        return $$1;
    }

    // used for kick barrage
    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        boolean standOn = PowerTypes.hasStandActive(playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;
        if (standOn && this.getActivePower() == PowerIndex.BARRAGE_2 && attackTimeDuring > -1) {
            int ClashTime = 15 - Math.round(((float) attackTimeDuring / this.getKickBarrageLength()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);
        } else if (standOn && this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2) {
            int ClashTime = Math.round(((float) attackTimeDuring / this.getKickBarrageWindup()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);
        } else {
            super.renderAttackHud(context,playerEntity,
                    scaledWidth,scaledHeight,ticks,vehicleHeartCount, flashAlpha, otherFlashAlpha);
        }
    }

    private int resolveRenderTicks = 0;

    /** hud **/
    @Override
    public boolean replaceHudActively() {
        return resolveRenderTicks > 0;
    }
    public void getReplacementHUD(GuiGraphics context, Player cameraPlayer, int screenWidth, int screenHeight, int x,
                                  boolean removeNum){
        StandHudRender.renderRequiemBarr(context,cameraPlayer,screenWidth,screenHeight,x, (int)resolvePoints, (int)getNeededResolvePoints());
    }

    // stand icons and info screen end

    // WIP tags, comment out when done

    @Override
    public boolean isWip() {
        return true;
    }

    @Override
    public Component ifWipListDevStatus() {
        return Component.translatable("roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);
    }

    @Override
    public Component ifWipListDev() {
        return Component.literal("88superguy").withStyle(ChatFormatting.GOLD);
    }
}
