package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.block.GEBushBlockEntity;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.*;
import net.hydra.jojomod.entity.projectile.ThrownObjectEntity;
import net.hydra.jojomod.entity.stand.GoldExperienceEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.mixin.StandUserEntity;
import net.hydra.jojomod.networking.ServerToClientPackets;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.BlockGrabPreset;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.zetalasis.networking.message.api.ModMessageEvents;
import org.joml.Vector3f;

import java.util.*;

import static com.mojang.text2speech.Narrator.LOGGER;

public class PowersGoldExperience extends BlockGrabPreset {
    // move bytes
    private static final byte
        LIFE_PUNCH = 53,
        DNA_CREATE = 54,
        BUSH_CREATION = 55,
        WALL_VINES = 56,
        HEAL = 57;

    // for life punch
    private LifePunchCloneEntity lifePunchClone = null;
    private LivingEntity lifePunchVictim = null;
    private int lifePunchTimer = 0;
    private float lastKnownHealth = 0;
    public final Map<Integer, Integer> disabledMobTimers = new HashMap<>();

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
                    //insert life wheel here
                }
            }
            // heal
            case SKILL_1_CROUCH -> {
                tryHeal();
            }
            // dash or vine climb
            case SKILL_3_NORMAL -> {
                tryToDashClient();
            }
            // life punch
            case SKILL_3_CROUCH -> {
                tryLifePunch();
            }
            // bush growth move
            case SKILL_4_GUARD -> {
                tryGrowBush();
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
        return super.setPowerOther(move, lastMove);
    }

    @Override
    public void updateUniqueMoves() {
        if (this.getActivePower() == LIFE_PUNCH) {
            updateLifePunch();
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
        super.updateUniqueMoves();
    }

    @Override
    public void handleStandAttack(Player player, Entity target) {
        if (this.getActivePower() == LIFE_PUNCH) {
            lifePunchImpact(target);
        }
    }

    private void tryToDashClient() {
        if (canLatchOntoWall()) {
                tryWallVines();
        } else {
            dash();
        }
    }

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
            default -> {
                return Component.translatable("skins.roundabout.gold_experience.base");
            }
        }
    }

    @Override
    public List<Byte> getSkinList() {
        List<Byte> l = Lists.newArrayList();
        l.add(GoldExperienceEntity.PART_5);
        /*if (this.getSelf() instanceof Player PE) {
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
            }
            if (((IPlayerEntity) PE).roundabout$getUnlockedBonusSkin() || bypass) {
            }
        }*/

        return l;
    }

    @Override
    public StandEntity getNewStandEntity() {
        byte skin = ((StandUser) this.getSelf()).roundabout$getStandSkin();
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
            setSkillIcon(context, x, y, 3, StandIcons.GOLD_EXPERIENCE_HEAL, PowerIndex.SKILL_1_SNEAK);
        } else if (canDNACreate()) {
            setSkillIcon(context, x, y, 1, StandIcons.GOLD_EXPERIENCE_DNA_CREATION, PowerIndex.SKILL_1);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, PowerIndex.SKILL_1);
        }
        // Ability 2 (X)
        setSkillIcon(context, x, y, 2, StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, PowerIndex.SKILL_2);
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
        if (isGuarding()){
            setSkillIcon(context, x, y, 4, StandIcons.GOLD_EXPERIENCE_BRAMBLE_TRAP, PowerIndex.SKILL_4_GUARD);
        } else {
            setSkillIcon(context, x, y, 4, StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, PowerIndex.SKILL_1);
        }
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos,
                                                  int topPos, byte level, boolean bypas) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.gold_experience_placeholder",
                "instruction.roundabout.press_skill", StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, 1, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.gold_experience_placeholder",
                "instruction.roundabout.passive", StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, 1, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.gold_experience_heal",
                "instruction.roundabout.press_skill_crouch", StandIcons.GOLD_EXPERIENCE_HEAL, 1, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.gold_experience_dna_creation",
                "instruction.roundabout.press_skill_item", StandIcons.GOLD_EXPERIENCE_DNA_CREATION, 1, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 0, "ability.roundabout.gold_experience_placeholder",
                "instruction.roundabout.passive", StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, 2, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 118, 0, "ability.roundabout.gold_experience_placeholder",
                "instruction.roundabout.press_skill", StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, 2, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 80, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE, 3, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 99, 0, "ability.roundabout.gold_experience_life_punch",
                "instruction.roundabout.press_skill_crouch", StandIcons.GOLD_EXPERIENCE_LIFE_PUNCH, 3, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 118, 0, "ability.roundabout.gold_experience_vine_climb",
                "instruction.roundabout.press_skill_air", StandIcons.GOLD_EXPERIENCE_VINE_CLIMB, 3, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 77, topPos + 80, 0, "ability.roundabout.gold_experience_vine_climb",
                "instruction.roundabout.press_skill", StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 77, topPos + 99, 0, "ability.roundabout.gold_experience_placeholder",
                "instruction.roundabout.passive", StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 77, topPos + 118, 0, "ability.roundabout.gold_experience_placeholder",
                "instruction.roundabout.press_skill", StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 96, topPos + 80, 0, "ability.roundabout.gold_experience_placeholder",
                "instruction.roundabout.press_skill", StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 96, topPos + 99, 0, "ability.roundabout.gold_experience_placeholder",
                "instruction.roundabout.passive", StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 96, topPos + 118, 0, "ability.roundabout.gold_experience_placeholder",
                "instruction.roundabout.press_skill", StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 115, topPos + 80, 0, "ability.roundabout.gold_experience_placeholder",
                "instruction.roundabout.press_skill", StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 115, topPos + 99, 0, "ability.roundabout.gold_experience_placeholder",
                "instruction.roundabout.passive", StandIcons.GOLD_EXPERIENCE_PLACEHOLDER, 4, level, bypas));
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
