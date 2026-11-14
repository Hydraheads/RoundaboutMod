package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.block.GEBushBlockEntity;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.*;
import net.hydra.jojomod.entity.gold_experience.*;
import net.hydra.jojomod.entity.projectile.ThrownObjectEntity;
import net.hydra.jojomod.entity.stand.GoldExperienceEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.BlockGrabPreset;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
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
        TREE = 61;

    // move bytes found in other moves
    public static final byte ITEM_IMBUE = 62;

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
    private static List<Item> disguiseOptions = null;
    public static List<Item> getDisguiseOptions() {
        if (disguiseOptions == null) {
            disguiseOptions = List.of(
                    Items.APPLE,
                    Items.MELON_SLICE,
                    Items.SWEET_BERRIES,
                    Items.GLOW_BERRIES,
                    Items.CHORUS_FRUIT,
                    Items.CARROT,
                    Items.POTATO,
                    Items.BEETROOT,
                    Items.SUGAR_CANE,
                    ModItems.CHERRIES,
                    Items.BEEF,
                    Items.PORKCHOP,
                    Items.MUTTON,
                    Items.CHICKEN,
                    Items.EGG,
                    Items.RABBIT,
                    Items.COD,
                    Items.SALMON,
                    Items.TROPICAL_FISH,
                    Items.PUFFERFISH,
                    Items.ACACIA_SAPLING,
                    Items.BIRCH_SAPLING,
                    Items.CHERRY_SAPLING,
                    Items.DARK_OAK_SAPLING,
                    Items.JUNGLE_SAPLING,
                    Items.OAK_SAPLING,
                    Items.SPRUCE_SAPLING,
                    Items.MANGROVE_PROPAGULE,
                    Items.BROWN_MUSHROOM,
                    Items.RED_MUSHROOM,
                    Items.CRIMSON_FUNGUS,
                    Items.WARPED_FUNGUS,
                    Items.GRASS,
                    Items.TALL_GRASS,
                    Items.FERN,
                    Items.LARGE_FERN,
                    Items.DANDELION,
                    Items.POPPY,
                    Items.BLUE_ORCHID,
                    Items.ALLIUM,
                    Items.AZURE_BLUET,
                    Items.RED_TULIP,
                    Items.ORANGE_TULIP,
                    Items.WHITE_TULIP,
                    Items.PINK_TULIP,
                    Items.OXEYE_DAISY,
                    Items.CORNFLOWER,
                    Items.LILY_OF_THE_VALLEY,
                    Items.TORCHFLOWER,
                    Items.PINK_PETALS,
                    Items.SUNFLOWER,
                    Items.LILAC,
                    Items.ROSE_BUSH,
                    Items.PEONY,
                    Items.CRIMSON_ROOTS,
                    Items.WARPED_ROOTS,
                    Items.NETHER_SPROUTS,
                    Items.WEEPING_VINES,
                    Items.TWISTING_VINES,
                    Items.VINE,
                    Items.WHEAT_SEEDS,
                    Items.COCOA_BEANS,
                    Items.PUMPKIN_SEEDS,
                    Items.MELON_SEEDS,
                    Items.BEETROOT_SEEDS,
                    Items.TORCHFLOWER_SEEDS,
                    Items.LILY_PAD,
                    Items.SEAGRASS,
                    Items.SEA_PICKLE,
                    Items.KELP,
                    Items.TUBE_CORAL,
                    Items.BRAIN_CORAL,
                    Items.BUBBLE_CORAL,
                    Items.FIRE_CORAL,
                    Items.HORN_CORAL,
                    Items.TUBE_CORAL_FAN,
                    Items.BRAIN_CORAL_FAN,
                    Items.BUBBLE_CORAL_FAN,
                    Items.FIRE_CORAL_FAN,
                    Items.HORN_CORAL_FAN
            );
        }
        return disguiseOptions;
    }

    // basic stand registry stuff
    public PowersGoldExperience(LivingEntity self) {super(self);}
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {return new PowersGoldExperience(entity);}
    //TODO: replace this with the GE enable config
    @Override
    public boolean isStandEnabled(){return true;}

    // start of move methods

    // tick power start

    @Override
    public void tickPower() {
        if (!this.self.level().isClientSide()) {
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
            //tree spawning
            if (!pendingTreeBlocks.isEmpty()) {
                treeSpawnTimer++;

                if (treeSpawnTimer < 0 && this.treeBasePos != null && this.self.level() instanceof ServerLevel serverLevel) {
                    if (treeSpawnTimer % 2 == 0) {
                        BlockState groundState = serverLevel.getBlockState(this.treeBasePos.below());
                        double x = this.treeBasePos.getX() + 0.5;
                        double y = this.treeBasePos.getY() + 0.05;
                        double z = this.treeBasePos.getZ() + 0.5;

                        sendParticlesIfPossible(serverLevel,
                                ModParticles.VITAL_ESSENCE,
                                x, y + 0.1, z,
                                3, 0.3, 0.05, 0.3, 0.02);

                        if (!groundState.isAir()) {
                            sendParticlesIfPossible(serverLevel,
                                    new BlockParticleOption(ParticleTypes.BLOCK, groundState),
                                    x, y, z,
                                    4, 0.25, 0.02, 0.25, 0.05);
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
            // lifeform creation
            case SKILL_2_NORMAL -> {
                tryMakeAnimalClient();
            }
            // tree creation
            case SKILL_2_GUARD -> {
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
            case SKILL_4_GUARD -> {
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
            return executeBushCreation();
        }
        else if (move == WALL_VINES) {
            return executeWallVines();
        }
        else if (move == HEAL) {
            return startHealWindup();
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
            return finishTree();
        }
        return super.setPowerOther(move, lastMove);
    }

    /* note: passedInt could be anything, it could be charge time, etc. for the purposes of life punch, for example
    passedInt could be what animal you're making
    */
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
        return super.tryIntPower(move, forced, passedInt);
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
        if (this.getActivePower() == HEAL) {
            updateHeal();
        }
        // bramble
        if (this.getActivePower() == BRAMBLES) {
            updateBramble();
        }
        super.updateUniqueMoves();
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
        return super.setPowerNone();
    }

    // item imbue start

    private void tryOpenItemImbue() {
        ClientUtil.openItemImbue();
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
                        ModSounds.GOLD_EXPERIENCE_LIFE_CREATION_EVENT, SoundSource.PLAYERS, 0.7F, 1.0F);
                clearImbue(stack);
                stack.shrink(1);
                gePowers.setActiveImbuedItemId(null);
                return true;
            }
        } else if (imbueType == IMBUE_DISGUISE) {
            clearImbue(stack);
            gePowers.setActiveImbuedItemId(null);
            gePowers.playSoundIfPossible(serverLevel, null, holder.blockPosition(),
                    ModSounds.GOLD_EXPERIENCE_LIFE_CREATION_EVENT, SoundSource.PLAYERS, 0.85F, 1.0F);
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
        if ((targetInCrosshair == null || targetInCrosshair instanceof GEThornEntity) && blockHit.getType() == HitResult.Type.BLOCK) {
            this.isGroundBarraging = true;

            if (this.self instanceof Player) {
                if (isPacketPlayer()) {
                    C2SPacketUtil.standBarrageHitPacket(-1, this.attackTimeDuring);
                    if (this.attackTimeDuring == this.getBarrageLength()) {
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
                        GEThornEntity rootEntity = new GEThornEntity(ModEntities.GE_THORN, level);
                        rootEntity.setOwner(this.getSelf());
                        rootEntity.setPos(targetX, groundPos.getY() + 1.0, targetZ);

                        level.addFreshEntity(rootEntity);
                    }
                }
            }
        } else {
            this.isGroundBarraging = false;
            super.standBarrageHit();
        }
    }

    // unique root barrage end

    // tree creation start

    private void tryGrowTree() {
        ((StandUser) this.getSelf()).roundabout$tryPower(TREE, true);
        tryPowerPacket(TREE);
    }

    private boolean finishTree() {
        Level level = this.self.level();
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

        BlockPos base = new BlockPos(centerPos.getX(), Math.min(centerPos.getY(), this.self.blockPosition().getY()), centerPos.getZ());
        Direction growthDirection = Direction.UP;

        int maxHeight = 7;
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

            playSoundIfPossible(level, null, base, ModSounds.GOLD_EXPERIENCE_LIFE_CREATION_EVENT, SoundSource.PLAYERS, 0.9F, 1F);
            addEXP(5);
            this.setAttackTimeDuring(-5);
        }
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
        // GE animal sync
        int animalClientConfig = 1;
        ClientConfig clientConfig = ConfigManager.getClientConfig();
        if (clientConfig != null && clientConfig.dynamicSettings != null) {
            animalClientConfig = clientConfig.dynamicSettings.goldExperienceCurrentAnimal;
        }
        this.animalType = (byte) animalClientConfig;
        tryIntPowerPacket(ITEM_THROW, animalClientConfig);

        // TODO: level lock here
        //if (canExecuteMoveWithLevel(getItemThrowLevel())) {
            super.itemGrabClient();
        //}
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

            // TODO: animations lol
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
        int animalClientConfig = 1;
        ClientConfig clientConfig = ConfigManager.getClientConfig();
        if (clientConfig != null && clientConfig.dynamicSettings != null) {
            animalClientConfig = clientConfig.dynamicSettings.goldExperienceCurrentAnimal;
        }
        this.tryIntPower(LIFEFORM_CREATION, true, animalClientConfig);
        tryIntPowerPacket(LIFEFORM_CREATION, animalClientConfig);
    }

    private boolean executeLifeformCreation() {
        if (!this.self.level().isClientSide()) {

            if (this.self.isCrouching()) {
                return false;
            }

            if(!isHoldingItem()){
                return false;
            }

            EntityType<?> typeToSpawn = getAnimalEntityType(this.animalType);

            if (typeToSpawn != null) {
                Vec3 spawnPos = this.self.position().add(this.self.getLookAngle().scale(1.2));
                spawnPos = new Vec3(spawnPos.x, spawnPos.y + 1.0, spawnPos.z);

                ItemStack handItem = this.self.getMainHandItem();

                if (spawnLifeform(typeToSpawn, spawnPos, handItem)) {
                    handItem.shrink(1);
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
        ((StandUser) this.getSelf()).roundabout$tryPower(BRAMBLES, true);
        tryPowerPacket(BRAMBLES);
    }

    private boolean startBrambleWindup() {
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)) {
            this.setAttackTimeDuring(0);
            this.setActivePower(BRAMBLES);
            //animate stand here
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

            int spikeCount = 8;
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

    private void tryHeal() {
        ((StandUser) this.getSelf()).roundabout$tryPower(HEAL, true);
        tryPowerPacket(HEAL);
    }

    private boolean startHealWindup() {
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)) {
            this.setAttackTimeDuring(0);
            this.setActivePower(HEAL);
            //animate stand here
            this.poseStand(OffsetIndex.GUARD);
            return true;
        }
        return false;
    }

    private void updateHeal() {
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > 9) {
                finishHeal();
            }
        }
    }

    private void finishHeal(){
        if (!this.self.level().isClientSide()) {
            Entity target = MainUtil.getTargetEntity(this.getSelf(), 5f);
            if (target instanceof LivingEntity livingHeal && livingHeal != null) {
                // for healing others
                livingHeal.heal(1f);
                livingHeal.addEffect(new MobEffectInstance(ModEffects.RECONSTRUCTION, 400, 0, false, true, true));
                playSoundIfPossible(this.self.level(), livingHeal, ModSounds.GOLD_EXPERIENCE_LIFE_CREATION_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
            } else {
                // for healing yourself
                this.self.heal(1f);
                this.self.addEffect(new MobEffectInstance(ModEffects.RECONSTRUCTION, 400, 0, false, true, true));
                playSoundIfPossible(this.self.level(), this.self, ModSounds.GOLD_EXPERIENCE_LIFE_CREATION_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
            this.setAttackTimeDuring(-5);
        }
    }

    // heal end

    // wall vines start

    private void tryWallVines() {
        ((StandUser) this.getSelf()).roundabout$tryPower(WALL_VINES, true);
        tryPowerPacket(WALL_VINES);
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

            if (airState.canBeReplaced() && wallState.isFaceSturdy(level, wallPos, wallFace)) {
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
            playSoundIfPossible(level, null, this.self.blockPosition(),
                    ModSounds.GOLD_EXPERIENCE_LIFE_CREATION_EVENT, SoundSource.PLAYERS, 0.85F, 1.0F);
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
        ((StandUser) this.getSelf()).roundabout$tryPower(BUSH_CREATION, true);
        tryPowerPacket(BUSH_CREATION);
    }

    private boolean executeBushCreation() {
        if (this.self.level().isClientSide()) return false;
        Level level = this.self.level();
        BlockPos origin = this.self.blockPosition();
        BlockState bushState = ModBlocks.GE_BUSH.defaultBlockState();

        boolean createdAny = false;
        int radius = 7;
        double radiusSq = radius * radius;
        for (BlockPos columnPos : BlockPos.betweenClosed(
                origin.offset(-radius, 0, -radius),
                origin.offset(radius, 0, radius))) {

            // limit to the radius
            if (columnPos.distSqr(origin) > radiusSq) continue;

            // 70% chance of creation
            if (level.random.nextFloat() > 0.70F) continue;

            // using the move on ONLY flat surfaces would feel bad
            // so i fix this by letting bushes be placed 1 up or down
            BlockPos targetPos = null;
            for (int yOffset : new int[]{0, 1, -1}) {
                BlockPos checkPos = columnPos.above(yOffset);
                BlockState checkState = level.getBlockState(checkPos);

                if (checkState.canBeReplaced()
                        && !checkState.is(ModBlocks.GE_BUSH)
                        && bushState.canSurvive(level, checkPos)) {
                    targetPos = checkPos;
                    break;
                }
            }

            // places the bush
            if (targetPos != null) {
                level.setBlock(targetPos, bushState, 3);

                if (level.getBlockEntity(targetPos) instanceof GEBushBlockEntity bushBE) {
                    bushBE.setOwner(this.getSelf().getUUID());
                }

                createdAny = true;
            }
        }

        if (createdAny) {
            playSoundIfPossible(level, null, this.self.blockPosition(),
                    ModSounds.GOLD_EXPERIENCE_LIFE_CREATION_EVENT, SoundSource.PLAYERS, 0.85F, 1.0F);
            addEXP(5);
        }

        return createdAny;
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

            // eventually, add the cd here
            return true;
        }

        return false;
    }

    private void tryDNACreate() {
        ((StandUser) this.getSelf()).roundabout$tryPower(DNA_CREATE, true);
        tryPowerPacket(DNA_CREATE);
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
        if (!this.onCooldown(LIFE_PUNCH)) {
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
                this.setAttackTimeDuring(-20);
                impaleTicks = 15;
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
            this.setAttackTimeDuring(-20);
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
                            // bonemeal spalings up to 5 times, breaking the loop early if the block is no longer a sapling
                            for (int i = 0; i < 5; i++) {
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

            int cooldownTicks = 100;
            if (this.getSelf() instanceof Player serverPlayer && !this.getSelf().level().isClientSide()) {
                S2CPacketUtil.sendCooldownSyncPacket((net.minecraft.server.level.ServerPlayer) serverPlayer, LIFE_PUNCH, cooldownTicks);
            }
            this.setCooldown(LIFE_PUNCH, cooldownTicks);
        }
    }

    private void restoreLifePunchVictim() {
        if (lifePunchVictim != null) {
            PowerTypes.setPlaneOfExisting(lifePunchVictim, (byte)0);
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

    // life punch end

    // move speed modifiers and the sort

    @Override
    public float inputSpeedModifiers(float basis) {
        if (this.activePower == PowerIndex.BARRAGE_CHARGE_2) {
            basis*=0.5f;
        } else if (this.activePower == PowerIndex.BARRAGE_2) {
            basis *= 0.3f;
        }

        if (this.getActivePower() == HEAL){
            // slows you down by 70% while healing
            basis *= 0.7f;
        }
        return super.inputSpeedModifiers(basis);
    }

    @Override
    public boolean cancelItemUse() {
        return (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2 || this.getActivePower() == PowerIndex.BARRAGE_2);
    }

    @Override
    public boolean clickRelease(){
        return (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2 || this.getActivePower() == PowerIndex.BARRAGE_2);
    }

    @Override
    public boolean cancelSprintJump(){
        if (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2 || this.getActivePower() == PowerIndex.BARRAGE_2
            || this.getActivePower() == HEAL) {
            return true;
        }
        return super.cancelSprintJump();
    }

    // end of move methods

    // sounds start

    @Override
    protected Byte getSummonSound() {return SoundIndex.SUMMON_SOUND;}

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.SUMMON_GOLD_EXPERIENCE_EVENT;
        } else if (soundChoice == IMPALE_NOISE || soundChoice == SoundIndex.ALT_CHARGE_SOUND_1) {
            return ModSounds.GOLD_EXPERIENCE_LIFE_PUNCH_WINDUP_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    @Override
    public SoundEvent getBarrageChargeSound() {
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

    // sounds end

    // skins and entity start

    @Override
    public Component getSkinName(byte skinId) {
        switch (skinId) {
            case GoldExperienceEntity.BETA -> {
                return Component.translatable("skins.roundabout.gold_experience.beta");
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
            }
            if (Level > 4 || bypass) {
            }
            if (Level > 5 || bypass) {
            }
            if (Level > 6 || bypass) {
                l.add(GoldExperienceEntity.BETA);
            }
            if (((IPlayerEntity) PE).roundabout$getUnlockedBonusSkin() || bypass) {
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

    /**
     * Renders the icons for the stand's abilities on the GUI. Comments will specify
     * if icon is for courching, blocking, or normal.
     */
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        // Ability 1 (Z)
        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 1, StandIcons.GOLD_EXPERIENCE_HEAL, PowerIndex.SKILL_1_SNEAK);
        } else if (canDNACreate()) {
            setSkillIcon(context, x, y, 1, StandIcons.GOLD_EXPERIENCE_DNA_CREATION, PowerIndex.SKILL_1);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.GOLD_EXPERIENCE_LIFEFORM_SELECT, PowerIndex.SKILL_1);
        }
        // Ability 2 (X)
        if (isGuarding()){
            setSkillIcon(context, x, y, 2, StandIcons.GOLD_EXPERIENCE_TREE_GROWTH, PowerIndex.SKILL_2_GUARD);
        } else if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 2, StandIcons.GOLD_EXPERIENCE_ITEM_THROW, PowerIndex.SKILL_2_SNEAK);
        } else {
            setSkillIcon(context, x, y, 2, StandIcons.GOLD_EXPERIENCE_LIFEFORM_CREATION, PowerIndex.SKILL_2);
        }
        // Ability 3 (C)
        if (isGuarding()){
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        } else if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 3, StandIcons.GOLD_EXPERIENCE_LIFE_PUNCH, PowerIndex.SKILL_3_CROUCH_GUARD);
        } else if (canLatchOntoWall()) {
            setSkillIcon(context, x, y, 3, StandIcons.GOLD_EXPERIENCE_VINE_CLIMB, PowerIndex.SKILL_3);
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        }
        // Ability 4 (V)
        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 4, StandIcons.GOLD_EXPERIENCE_ITEM_IMBUE, PowerIndex.SKILL_4_SNEAK);
        } else if (isGuarding()){
            setSkillIcon(context, x, y, 4, StandIcons.GOLD_EXPERIENCE_BRAMBLE_TRAP, PowerIndex.SKILL_4_GUARD);
        } else {
            setSkillIcon(context, x, y, 4, StandIcons.GOLD_EXPERIENCE_BRAMBLES, PowerIndex.SKILL_4);
        }
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        // lifeform creation
        if (slot == 2 && !isHoldingSneak() && !isGuarding() && !isHoldingItem()) {
            return true;
        }
        return super.isAttackIneptVisually(activeP, slot);
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos,
                                                  int topPos, byte level, boolean bypas) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.gold_experience_lifeform_select",
                "instruction.roundabout.press_skill", StandIcons.GOLD_EXPERIENCE_LIFEFORM_SELECT, 1, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.gold_experience_heal",
                "instruction.roundabout.press_skill_crouch", StandIcons.GOLD_EXPERIENCE_HEAL, 1, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.gold_experience_placeholder",
                "instruction.roundabout.passive", StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, 1, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.gold_experience_dna_creation",
                "instruction.roundabout.press_skill_item", StandIcons.GOLD_EXPERIENCE_DNA_CREATION, 1, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 0, "ability.roundabout.gold_experience_lifeform_creation",
                "instruction.roundabout.press_skill", StandIcons.GOLD_EXPERIENCE_LIFEFORM_CREATION, 2, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 118, 0, "ability.roundabout.gold_experience_item_throw",
                "instruction.roundabout.press_skill_crouch", StandIcons.GOLD_EXPERIENCE_ITEM_THROW, 2, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 80, 0, "ability.roundabout.gold_experience_tree_growth",
                "instruction.roundabout.press_skill_block", StandIcons.GOLD_EXPERIENCE_TREE_GROWTH, 2, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 99, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE, 3, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 118, 0, "ability.roundabout.gold_experience_life_punch",
                "instruction.roundabout.press_skill_crouch", StandIcons.GOLD_EXPERIENCE_LIFE_PUNCH, 3, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 77, topPos + 80, 0, "ability.roundabout.gold_experience_vine_climb",
                "instruction.roundabout.press_skill_air", StandIcons.GOLD_EXPERIENCE_VINE_CLIMB, 3, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 77, topPos + 99, 0, "ability.roundabout.gold_experience_vine_climb",
                "instruction.roundabout.press_skill", StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 77, topPos + 118, 0, "ability.roundabout.gold_experience_placeholder",
                "instruction.roundabout.passive", StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 96, topPos + 80, 0, "ability.roundabout.gold_experience_placeholder",
                "instruction.roundabout.press_skill", StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 96, topPos + 99, 0, "ability.roundabout.gold_experience_placeholder",
                "instruction.roundabout.passive", StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 96, topPos + 118, 0, "ability.roundabout.gold_experience_placeholder",
                "instruction.roundabout.press_skill", StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 115, topPos + 80, 0, "ability.roundabout.gold_experience_violent_brambles",
                "instruction.roundabout.press_skill", StandIcons.GOLD_EXPERIENCE_BRAMBLES, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 115, topPos + 99, 0, "ability.roundabout.gold_experience_item_imbue",
                "instruction.roundabout.press_skill_crouch", StandIcons.GOLD_EXPERIENCE_ITEM_IMBUE, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 115, topPos + 118, 0, "ability.roundabout.gold_experience_bramble_trap",
                "instruction.roundabout.press_skill_block", StandIcons.GOLD_EXPERIENCE_BRAMBLE_TRAP, 4, level, bypas));
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
