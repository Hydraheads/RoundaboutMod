package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.block.DiverLimbBlock;
import net.hydra.jojomod.block.DiverLimbBlockEntity;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.KeyboardPilotInput;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.DiverDownEntity;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.client.gui.diverdown.custom_workbench_code.*;
import net.hydra.jojomod.client.hud.StandHudRender;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class PowersDiverDown extends NewPunchingStand {

    // for move ids accessed here. update public bytes every time this is edited.
    private static final byte LIMB_SCAFFOLD = 53,
            LIMB_RECALL = 54,
            // Workbench move ids start here
            CRAFTING_TABLE = 55,
            LOOM = 56,
            STONECUTTER = 57,
            ANVIL = 58,
            SMITHING_TABLE = 59,
            OPEN_CHEST = 60,
            GROUND_GET_ITEMS = 61,
            GROUND_DIVE_BARRAGE = 62;

    // for all the move ids accessed elsewhere.
    public static final byte ACCESS_WORKBENCH = 119;

    // NOISES GO BELOW HERE, starting from 120. I think that should be more than
    // enough.
    public static final byte CHARGE_NOISE = 120;

    // the next 2 variables are used for the charge phase punch later
    public boolean holdDownClick = false;
    public int chargedPhasePunch = 0;

    // used for ground dive
    public static final int MAX_DIVE_TICKS = 200; // 10 seconds
    public int diveTicksLeft = 0;
    private CameraType previousCameraType = null;
    private boolean wasPilotingClient = false;

    // stand creation model floaty creation whatever thingy.
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersDiverDown(entity);
    }

    @Override
    public StandEntity getNewStandEntity() {
        return ModEntities.DIVER_DOWN.create(this.getSelf().level());
    }

    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    public PowersDiverDown(LivingEntity self) {
        super(self);
    }

    // icons and ability list here

    /**
     * Renders the icons for the stand's abilities on the GUI. Comments will specify
     * if icon is for courching, blocking, or normal.
     */
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        // Ability 1 (Z)
        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 1, StandIcons.DIVER_DOWN_DISASSEMBLE, PowerIndex.SKILL_1_SNEAK);
        } else if (isGuarding()) {
            setSkillIcon(context, x, y, 1, StandIcons.DIVER_DOWN_SELF, PowerIndex.SKILL_1_GUARD);
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.DIVER_DOWN_SELECTION, PowerIndex.SKILL_1);
        }

        // Ability 2 (X)
        if (isDiveActive()) {
            setSkillIcon(context, x, y, 2, StandIcons.DIVER_DOWN_AFFLICTION, PowerIndex.SKILL_2);
        } else if (isGuarding()) {
            // releaseMode will be true if auto, false if manual
            if (releaseMode())
                setSkillIcon(context, x, y, 2, StandIcons.DIVER_DOWN_RELEASE_AUTO, PowerIndex.SKILL_2_GUARD);
            else
                setSkillIcon(context, x, y, 2, StandIcons.DIVER_DOWN_RELEASE_MANUAL, PowerIndex.SKILL_2_GUARD);
        } else {
            setSkillIcon(context, x, y, 2, StandIcons.DIVER_DOWN_STORE, PowerIndex.SKILL_2);
        }

        // Ability 3 (C)
        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 3, StandIcons.DIVER_DOWN_ZIP, PowerIndex.SKILL_3);
        } else if (canVault()) {
            setSkillIcon(context, x, y, 3, StandIcons.DIVER_DOWN_VAULT, PowerIndex.GLOBAL_DASH);
        } else {
            setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        }

        // Ability 4 (V)
        if (areStandMovesDisabled()) {
            if (isGuarding()) {
                setSkillIcon(context, x, y, 4, StandIcons.DIVER_DOWN_PLATFORM, PowerIndex.SKILL_4_GUARD);
            } else if (isHoldingSneak()) {
                setSkillIcon(context, x, y, 4, StandIcons.DIVER_DOWN_WORKSTATION, PowerIndex.SKILL_4_SNEAK);
            } else {
                setSkillIcon(context, x, y, 4, StandIcons.DIVER_DOWN_RECALL, PowerIndex.SKILL_4);
            }
        } else {
            if (isGuarding()) {
                setSkillIcon(context, x, y, 4, StandIcons.DIVER_DOWN_PLATFORM, PowerIndex.SKILL_4_GUARD);
            } else if (isHoldingSneak()) {
                setSkillIcon(context, x, y, 4, StandIcons.DIVER_DOWN_WORKSTATION, PowerIndex.SKILL_4_SNEAK);
            } else {
                setSkillIcon(context, x, y, 4, StandIcons.DIVER_DOWN_GROUND_DIVE, PowerIndex.SKILL_4);
            }
        }
    }

    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos,
            int topPos, byte level, boolean bypas) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 96, topPos + 99, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE, 3, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 115, topPos + 99, 0, "ability.roundabout.vault",
                "instruction.roundabout.press_skill_air", StandIcons.DIVER_DOWN_VAULT, 3, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 134, topPos + 99, 0, "ability.roundabout.diver_zip",
                "instruction.roundabout.press_skill_crouch", StandIcons.DIVER_DOWN_ZIP, 3, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.diver_selection",
                "instruction.roundabout.press_skill", StandIcons.DIVER_DOWN_SELECTION, 4, level, bypas));
        return $$1;
    }

    // icons and ability list end

    @Override
    public int getMaxGuardPoints() {
        // change later to replace with the config.
        return 15;
    }

    // th...thank you soundman...
    // *sound*
    // soundman????
    // be-because you played sounds
    // oh yeah
    // sounds and such

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.SUMMON_DIVER_DOWN_EVENT;
        } else if (soundChoice == IMPALE_NOISE) {
            return ModSounds.DIVER_DOWN_CHARGE_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    // START OF ACTUAL MOVE METHODS

    /**
     * (non-Javadoc)
     * buttonInputAttack is overriden to add the heavy charged crouch punch.
     * Additional changes need to be made to give the charged punch AOE
     * 
     * @see net.hydra.jojomod.fates.powers.AbilityScapeBasis#buttonInputAttack(boolean,
     *      net.minecraft.client.Options)
     */
    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (!consumeClickInput) {
            if (holdDownClick) {
                if (keyIsDown) {
                } else {
                    if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
                        int atd = this.getAttackTimeDuring();
                        this.tryIntPower(PowerIndex.SNEAK_ATTACK, true, atd);
                        tryIntPowerPacket(PowerIndex.SNEAK_ATTACK, atd);
                    }
                    holdDownClick = false;
                }
            } else {
                if (keyIsDown) {
                    if (!isHoldingSneak()) {
                        super.buttonInputAttack(keyIsDown, options);
                    } else {
                        if (this.canAttack()) {
                            this.tryPower(PowerIndex.SNEAK_ATTACK_CHARGE, true);
                            holdDownClick = true;
                            tryPowerPacket(PowerIndex.SNEAK_ATTACK_CHARGE);
                        } else {
                            super.buttonInputAttack(keyIsDown, options);
                        }
                    }
                }
            }
        } else {
            if (!keyIsDown) {
                consumeClickInput = false;
            }
        }
    }

    /**
     * Gets the input for the power based on the buttons pressed.
     * Note, this is client side only. DO NOT FORGET!!!!
     *
     * @param context What button combination was pressed.
     */
    @Override
    public void powerActivate(PowerContext context) {
        if (areStandMovesDisabled()) {
            if (hasLimbsDeployed()) {
                // pressing V recalls limbs if limb move is active
                if (context == PowerContext.SKILL_4_NORMAL) {
                    tryRecallLimbs();
                    // note: still not sure about the heirarchy for sneak + guard moves. Replace
                    // this when known.
                } else if (context == PowerContext.SKILL_4_GUARD || context == PowerContext.SKILL_4_CROUCH_GUARD) {
                    tryLimbClimb();
                }
                // stops everything else from working
                return;
            }
            if (isPiloting()) {
                if (context == PowerContext.SKILL_4_NORMAL) {
                    exitGroundDive();
                    // note: still not sure about the heirarchy for sneak + guard moves. Replace
                    // this when known.
                } else if (context == PowerContext.SKILL_3_NORMAL) {
                    tryOpenChest();
                } else if (context == PowerContext.SKILL_2_NORMAL) {
                    // insert item pickup here
                }
                // stops everything else from working
                return;
            }
        }
        switch (context) {
            // dash, need to figure out how other moves will work.
            case SKILL_3_NORMAL -> {
                tryToDashClient();
            }
            // ground dive
            case SKILL_4_NORMAL -> {
                tryGroundDive();
            }
            // 3x3 crafting grid.
            case SKILL_4_CROUCH -> {
                tryWorkbenchSelectionClient();
            }
            // limb climbing move
            case SKILL_4_GUARD -> {
                tryLimbClimb();
            }
        }
    }

    // for activating all the moves
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        // does the limb scaffold move
        if (move == LIMB_SCAFFOLD) {
            return placeLimb();
        }
        // recalls limb scaffolds
        else if (move == LIMB_RECALL) {
            return recallLimbs();
        }
        // charges the phase punch
        else if (move == PowerIndex.SNEAK_ATTACK_CHARGE) {
            return setPowerChargePhase();
        }
        // does the phase punch
        else if (move == PowerIndex.SNEAK_ATTACK) {
            return setPowerPhasePunch();
        }
        // barrages in ground dive
        else if (move == GROUND_DIVE_BARRAGE) {
            return groundDiveBarrage();
        }
        return super.setPowerOther(move, lastMove);
    }

    @Override
    public boolean tryBlockPosPower(int move, boolean forced, BlockPos blockPos) {
        if (move == OPEN_CHEST) {
            openChest(blockPos);
        }
        return super.tryBlockPosPower(move, forced, blockPos);
    }

    public void tryToDashClient() {
        if (vaultOrFallBraceFails()) {
            dash();
        }
    }

    /**
     * Opens the selection menu for the player to choose what
     * workbench they want to access.
     */
    private void tryWorkbenchSelectionClient() {
        ClientUtil.openWorkbenchSelect();
    }

    /**
     * (non-Javadoc)
     * tryLimbClimb is the client side activation for the limb move.
     */
    private void tryLimbClimb() {
        if (this.self.level().isClientSide()) {
            if (!this.onCooldown(PowerIndex.SKILL_4_GUARD)) {
                // literally just to prevent the move from being spammed
                this.setCooldown(PowerIndex.SKILL_4_GUARD, 30);
                ((StandUser) this.getSelf()).roundabout$tryPower(LIMB_SCAFFOLD, true);
                tryPowerPacket(LIMB_SCAFFOLD);
            }
        }
    }

    /**
     * (non-Javadoc)
     * tryIntPower
     * 
     * @see net.hydra.jojomod.stand.powers.presets.NewDashPreset#tryIntPower(int,
     *      boolean, int)
     */
    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime) {
        if (move == ACCESS_WORKBENCH) {
            // test message, once done comment everything out...
            /*
             * if (this.getSelf() instanceof ServerPlayer serverPlayer) {
             * serverPlayer.displayClientMessage(Component.literal("Server received ID: " +
             * workbenchId), false);
             * }
             */
            // up to here

            // NOTE: for the purposes of the workbench: chargeTime serves as the workbench
            // ID
            // AKA what workbench is being accessed
            return openWorkbench(chargeTime);
        } else if (move == PowerIndex.SNEAK_ATTACK) {
            this.chargedPhasePunch = chargeTime;
        }
        return super.tryIntPower(move, forced, chargeTime);
    }

    /**
     * Stand related things that slow you down or speed you up.
     * Will be used for the charge punch and phase move.
     */
    @Override
    public float inputSpeedModifiers(float basis) {
        if (this.activePower == PowerIndex.SNEAK_ATTACK_CHARGE) {
            if (this.getSelf().isCrouching()) {
                // gets the sneak speed i think
                float f = Mth.clamp(0.3F + EnchantmentHelper.getSneakingSpeedBonus(this.getSelf()), 0.0F, 1.0F);
                // turns f into it's reciprocal.
                // (ngl, i don't understand why the guy that made this code didn't just do that
                // in the f thing itself, but it works so i also don't care.)
                float g = 1 / f;
                // multiplies basis by g, instead of just slowing you down by 70%, that way
                // movement speed is the same for both crouching and standing up.
                basis *= g;
            }
            // slows you down by 70%
            basis *= 0.3f;
            // right now the dive move isn't implemented, so this is just a placeholder.
        } else if (this.getActivePower() == PowerIndex.POWER_1) {
            // slows you down by 70%
            basis *= 0.3f;
        }
        return super.inputSpeedModifiers(basis);
    }

    @Override
    public void updateUniqueMoves() {
        // this is specifically to destroy all limbs if the user dies while still having
        // limbs active.
        if (!this.self.isAlive() && !this.activeLimbs.isEmpty()) {
            this.recallLimbs();
        }
        if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
            updatePhasePunchCharge();
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK) {
            updatePhasePunch();
        }

        super.updateUniqueMoves();
    }

    public void standPhasePunch() {
        this.setAttackTimeMax(
                ClientNetworking.getAppropriateConfig().generalStandSettings.finalPunchAndKickMinimumCooldown
                        + chargedPhasePunch);
        this.setAttackTime(0);
        this.setActivePowerPhase(this.getActivePowerPhaseMax());

        boolean isFullyCharged = this.chargedPhasePunch >= getMaxPhasePunchTime();
        float reach = 5.5F;
        float defaultAngle = 25F;

        if (this.self instanceof Player) {
            if (isPacketPlayer()) {
                this.attackTimeDuring = -10;
                if (!isFullyCharged) {
                    // punches that aren't fully charged won't phase through walls nor multiple
                    // targets.
                    tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK, getTargetEntityId());
                } else {
                    List<Entity> piercedTargets = getTargetEntityListThroughWalls(this.self, reach, defaultAngle);
                    // this if statement is for the config thing that allows people to enable or
                    // disable moves that go through walls.
                    if (!ClientNetworking.getAppropriateConfig().miscellaneousSettings.wallPassingHitboxes) {
                        // changes piercedTargets to only count targets NOT hit through walls.
                        piercedTargets = getTargetEntityList(this.self, reach, defaultAngle);
                    } else {
                        if (!piercedTargets.isEmpty()) {
                            // for boss filtering function, taken from walking heart. It took me a while to
                            // figure out that FE meant "Filtered Entities," so im leaving this variable as
                            // the whole thing for future reference.
                            List<Entity> filteredEntities = new ArrayList<>();
                            for (Entity target : piercedTargets) {
                                if (ClientNetworking
                                        .getAppropriateConfig().miscellaneousSettings.wallPassingHitboxesOnBosses) {
                                    filteredEntities.add(target);
                                } else if (MainUtil.isBossMob(target)) {
                                    // Bosses require direct line of sight
                                    if (MainUtil.canActuallyHitInvolved(target, this.self)) {
                                        filteredEntities.add(target);
                                    }
                                } else {
                                    // Regular mobs can be hit through walls
                                    filteredEntities.add(target);
                                }
                            }
                            piercedTargets = filteredEntities;
                        }
                    }
                    if (!piercedTargets.isEmpty()) {
                        for (Entity target : piercedTargets) {
                            if (target != this.self && target.isAlive() && !(target instanceof StandEntity)) {
                                // send packet for EVERY target caught in the line
                                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK, target.getId());
                            }
                        }
                    } else {
                        // whiffed. moron.
                        tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK, -1);
                    }
                }
            }
        } else {
            /* Caps how far out the punch goes */
            Entity targetEntity = getTargetEntity(this.self, -1);
            phasePunchImpact(targetEntity);
        }
    }

    @Override
    public void handleStandAttack(Player player, Entity target) {
        if (this.getActivePower() == PowerIndex.SNEAK_ATTACK) {
            phasePunchImpact(target);
        }
    }

    public void phasePunchImpact(Entity entity) {
        this.setAttackTimeDuring(-20);
        boolean isFullyCharged = this.chargedPhasePunch >= getMaxPhasePunchTime();

        // note, 5.5 here is the reach for the move.
        if (entity != null && entity.distanceTo(self) > 5.5F) {
            entity = null;
        }
        if (entity != null) {
            hitParticlesCenter(entity);
            float pow;
            float knockbackStrength;
            pow = getPhasePunchStrength(entity);
            knockbackStrength = getPhasePunchKnockback();
            boolean hitSuccess;
            if (isFullyCharged) {
                hitSuccess = DamageHandler.PenetratingStandDamageEntity(entity, pow, this.self);
            } else {
                hitSuccess = StandDamageEntityAttack(entity, pow, 0, this.self);
            }

            if (hitSuccess) {
                if (entity instanceof LivingEntity LE) {
                    if (isFullyCharged) {
                        addEXP(5, LE);
                    } else {
                        addEXP(2, LE);
                    }
                }
                takeDeterminedKnockbackWithY(this.self, entity, knockbackStrength);
            } else {
                if (!isFullyCharged) {
                    knockShield2(entity, 20);
                }
            }
        } else {
            // This is less accurate raycasting as it is server sided but it is important
            // for particle effects
            float distMax = this.getDistanceOut(this.self, this.getReach(), false);
            float halfReach = (float) (distMax * 0.5);
            Vec3 pointVec = DamageHandler.getRayPoint(self, halfReach);
            if (!this.self.level().isClientSide) {
                sendParticlesIfPossible(self.level(), ModParticles.PUNCH_MISS, pointVec.x, pointVec.y, pointVec.z,
                        1, 0.0, 0.0, 0.0, 1);
            }
        }

        SoundEvent SE;
        float pitch = 1F;
        if (entity != null) {
            SE = getPhasePunchSound();
            pitch = getPhasePunchPitch();
        } else {
            SE = ModSounds.PUNCH_2_SOUND_EVENT;
        }

        if (!this.self.level().isClientSide()) {
            playSoundIfPossible(self.level(), null, this.self.blockPosition(), SE,
                    SoundSource.PLAYERS, 0.85F, pitch);
        }
    }

    // Workbench code start

    /**
     * This functions runs the method based on the workbench found in
     * workbenchID. The next 5 functions that follow all open the corresponding
     * workbenches.
     * 
     * @param workbenchID ID of the workbench being accessed
     */
    private boolean openWorkbench(int workbenchId) {
        if (!(this.getSelf() instanceof ServerPlayer serverPlayer)) {
            return false;
        }

        switch (workbenchId) {
            case CRAFTING_TABLE -> {
                // test message, comment this out once done
                // serverPlayer.displayClientMessage(Component.literal("SERVER: calling
                // crafting"), false);
                openCraftingTable(serverPlayer);
                return true;
            }
            case LOOM -> {
                openLoom(serverPlayer);
                return true;
            }
            case STONECUTTER -> {
                openStonecutter(serverPlayer);
                return true;
            }
            case ANVIL -> {
                openAnvil(serverPlayer);
                return true;
            }
            case SMITHING_TABLE -> {
                openSmithingTable(serverPlayer);
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public void openCraftingTable(ServerPlayer serverPlayer) {
        serverPlayer.openMenu(new SimpleMenuProvider(
                (containerId, inventory, player) -> new DiverDownCraftingMenu(containerId, inventory,
                        ContainerLevelAccess.create(serverPlayer.level(), serverPlayer.blockPosition())) {
                    @Override
                    public boolean stillValid(Player player) {
                        return true;
                    }
                },
                Component.translatable("container.crafting")));/**
                                                                * test to see if the selection even works in the first
                                                                * place.
                                                                * comment this out when unneeded anymore :thumbsup:
                                                                */
        // serverPlayer.displayClientMessage(Component.literal("Selected: Crafting
        // Table"), false);
    }

    public void openLoom(ServerPlayer serverPlayer) {
        serverPlayer.openMenu(new SimpleMenuProvider(
                (containerId, inventory, player) -> new LoomMenu(containerId, inventory,
                        ContainerLevelAccess.create(serverPlayer.level(), serverPlayer.blockPosition())) {
                    @Override
                    public boolean stillValid(Player player) {
                        return true;
                    }
                },
                Component.translatable("container.loom")));
    }

    public void openStonecutter(ServerPlayer serverPlayer) {
        serverPlayer.openMenu(
                new SimpleMenuProvider(
                        (containerId, inventory, player) -> new StonecutterMenu(containerId, inventory,
                                ContainerLevelAccess.create(serverPlayer.level(), serverPlayer.blockPosition())) {
                            @Override
                            public boolean stillValid(Player player) {
                                return true;
                            }
                        },
                        Component.translatable("container.stonecutter")));
    }

    public void openAnvil(ServerPlayer serverPlayer) {
        serverPlayer.openMenu(
                new SimpleMenuProvider(
                        (containerId, inventory, player) -> new DiverDownAnvilMenu(containerId, inventory,
                                ContainerLevelAccess.create(serverPlayer.level(), serverPlayer.blockPosition())) {
                            @Override
                            public boolean stillValid(Player player) {
                                return true;
                            }
                        },
                        Component.translatable("container.repair")));
    }

    public void openSmithingTable(ServerPlayer serverPlayer) {
        serverPlayer.openMenu(
                new SimpleMenuProvider(
                        (containerId, inventory, player) -> new DiverDownSmithingMenu(containerId, inventory,
                                ContainerLevelAccess.create(serverPlayer.level(), serverPlayer.blockPosition())) {
                            @Override
                            public boolean stillValid(Player player) {
                                return true;
                            }
                        },
                        Component.translatable("container.upgrade")));
    }

    // Workbench code end

    // Limb scaffold climb move start

    // these variables lists all the limbs so it can cycle through the 4
    public final List<BlockPos> activeLimbs = new ArrayList<>();
    public int currentLimbIndex = 0;

    /**
     * Checks to see if limbs are deployed to ensure moves can't be used while
     * active.
     */
    private boolean hasLimbsDeployed() {
        if (this.self.level() != null && !this.self.level().isClientSide()) {
            this.activeLimbs.removeIf(pos -> !this.self.level().getBlockState(pos).is(ModBlocks.DIVER_LIMB));
        }
        return !this.activeLimbs.isEmpty();
    }

    private boolean placeLimb() {
        // checks to see if there is a block within 6... blocks.
        HitResult hit = this.self.pick(6.0D, 0.0F, false);

        if (hit.getType() == HitResult.Type.BLOCK && hit instanceof BlockHitResult blockHit) {
            // don't let the move be placed on the ceiling cuz scaffolds don't work like
            // that
            if (blockHit.getDirection() == Direction.DOWN) {
                return false;
            }

            BlockPos targetPos = blockHit.getBlockPos().relative(blockHit.getDirection());
            // makes sure that the limbs are close enough
            if (!isConnectedToExistingLimbs(targetPos)) {
                return false;
            }
            if (this.self.distanceToSqr(Vec3.atCenterOf(targetPos)) <= 25.0) {
                Level level = this.self.level();
                if (level.getBlockState(targetPos).canBeReplaced()
                        && !level.getBlockState(targetPos).is(ModBlocks.DIVER_LIMB)) {
                    // cycle limb code here
                    while (activeLimbs.size() >= 4) {
                        BlockPos oldest = activeLimbs.remove(0);
                        if (level.getBlockState(oldest).is(ModBlocks.DIVER_LIMB)) {
                            level.removeBlock(oldest, false);
                        }
                    }
                    // increases limb index so the next limb will be th enext in queue (read
                    // DiverLimbBlockEntity to see the cycle order)
                    int nextLimb = this.currentLimbIndex % 4;
                    this.currentLimbIndex++;
                    Direction facing = blockHit.getDirection().getOpposite();
                    // variable for ModBlocks.DIVER_LIMB.defaultBlockState() since it's used 3 times
                    BlockState state = ModBlocks.DIVER_LIMB.defaultBlockState();
                    activeLimbs.add(targetPos);
                    // place the block on the servers
                    if (!level.isClientSide) {
                        level.setBlockAndUpdate(targetPos, state);
                        if (level.getBlockEntity(targetPos) instanceof DiverLimbBlockEntity be) {
                            be.ownerUUID = this.self.getUUID();
                            be.limbIndex = nextLimb;
                            be.facing = facing;
                            be.standSkin = ((StandUser) this.self).roundabout$getStandSkin();
                            be.setChanged();
                            level.sendBlockUpdated(targetPos, state, state, 3);
                        }
                        // desummons stand
                        if (hasStandEntity(this.self)) {
                            StandEntity stand = this.getStandEntity(this.self);
                            if (stand != null) {
                                // disconnects the stand from the user so it can move freely
                                // note to self: see if i can use this agian later for the dive into people and
                                // dive underground move.
                                if (stand instanceof FollowingStandEntity fse) {
                                    fse.setOffsetType(OffsetIndex.LOOSE);
                                }
                                // check where the block is
                                Vec3 blockCenter = Vec3.atCenterOf(targetPos);
                                float yaw;
                                float pitch = 0.0F;
                                if (facing == Direction.DOWN) {
                                    // diving into ground
                                    yaw = this.self.getYRot();
                                    pitch = 90.0F;
                                } else if (facing == Direction.UP) {
                                    // ceiling dive is kinda useless, delete this later don't forget pls ty
                                    yaw = this.self.getYRot();
                                    pitch = -90.0F;
                                } else {
                                    // diving into wall
                                    yaw = facing.toYRot();
                                }
                                // send DD to the block
                                stand.teleportTo(blockCenter.x, targetPos.getY() - 0.5, blockCenter.z);
                                stand.setYRot(yaw);
                                stand.setYHeadRot(yaw);
                                stand.setYBodyRot(yaw);
                                stand.setXRot(pitch);

                                // add animations and effects here
                                // sound effect here, currentLimbIndex says if it should play the first limb
                                // phase sound, or the rephase sound
                                playSoundIfPossible(self.level(), null, this.self.blockPosition(),
                                        ModSounds.DIVER_DOWN_DIVE_EVENT,
                                        SoundSource.PLAYERS, 0.85F, 1);
                                // finally despawns the stand once it's inside the block
                                stand.forceDespawn(true);
                            }
                        }
                        // subsequent usages
                        else {
                            // play the animations + effects
                            // play a sound
                            playSoundIfPossible(self.level(), null, this.self.blockPosition(),
                                    ModSounds.DIVER_DOWN_DIVE2_EVENT,
                                    SoundSource.PLAYERS, 0.85F, 1);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if targetPos is directly connected to any already-placed limb.
     * Allows 1st limb anywhere, but subsequent limbs must touch an existing limb.
     */
    private boolean isConnectedToExistingLimbs(BlockPos targetPos) {
        if (this.activeLimbs.isEmpty()) {
            // 1st limb can be placed anywhere valid
            return true;
        }
        for (BlockPos limbPos : this.activeLimbs) {
            // checks where the target location is in relation to the already existing limbs
            int dx = Math.abs(targetPos.getX() - limbPos.getX());
            int dy = Math.abs(targetPos.getY() - limbPos.getY());
            int dz = Math.abs(targetPos.getZ() - limbPos.getZ());
            // blocks must be within 4 block in all 3 directions
            if (dx <= 4 && dy <= 4 && dz <= 4) {
                return true;
            }
        }
        return false;
    }

    private boolean recallLimbs() {
        if (this.activeLimbs.isEmpty()) {
            return false;
        }
        Level level = this.self.level();
        if (!level.isClientSide()) {
            for (BlockPos pos : this.activeLimbs) {
                if (level.getBlockState(pos).is(ModBlocks.DIVER_LIMB)) {
                    level.removeBlock(pos, false);
                }
            }
        }
        // reset current limbs
        this.activeLimbs.clear();
        this.currentLimbIndex = 0;
        // resummons stand
        if (!level.isClientSide() && hasStandActive(this.self)) {
            ((StandUser) this.self).roundabout$summonStand(level, true, false);
            playSoundIfPossible(self.level(), null, this.self.blockPosition(),
                    ModSounds.SUMMON_DIVER_DOWN_EVENT,
                    SoundSource.PLAYERS, 0.85F, 1);
        }
        return true;
    }

    /**
     * This is a client side function that tries to recall all limb scaffolds
     */
    private void tryRecallLimbs() {
        ((StandUser) this.getSelf()).roundabout$tryPower(LIMB_RECALL, true);
        tryPowerPacket(LIMB_RECALL);
    }

    // Limb scaffold climb move end

    // Ground dive move here

    private void tryGroundDive() {
        StandEntity stand = getStandEntity(this.self);
        if (stand != null && stand.isAlive()) {
            Minecraft mc = Minecraft.getInstance();
            // saves camera
            if (this.previousCameraType == null) {
                this.previousCameraType = mc.options.getCameraType();
            }
            // forces third person camera
            mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
            if (mc.player != null && mc.getCameraEntity() != mc.player) {
                mc.setCameraEntity(mc.player);
            }
            setPiloting(stand.getId());
            tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT, stand.getId());
            if (mc.player != null) {
                ClientUtil.setCameraEntity(stand);
            }
            // the 0.05 is there to fix the pilot enter bug, so it can enter pilot inside
            // walls
            stand.setPos(stand.getX(), stand.getY() + 0.05, stand.getZ());
            // note to self: get the last survivor ult sound effect for this. this is a
            // placeholder for now
            playSoundIfPossible(self.level(), null, stand.blockPosition(),
                    ModSounds.DIVER_DOWN_DIVE_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    public void exitGroundDive() {
        if (this.self.level().isClientSide()) {
            Minecraft mc = Minecraft.getInstance();
            // return camera to player
            ClientUtil.setCameraEntity(null);
            if (mc.player != null) {
                mc.setCameraEntity(mc.player);
            }
            // go back to first person/whatever the person was using when they used this
            // move.
            CameraType restore = (this.previousCameraType != null) ? this.previousCameraType : CameraType.FIRST_PERSON;
            mc.options.setCameraType(restore);
            this.previousCameraType = null;
            this.wasPilotingClient = false;
        }
        setPiloting(0);
        tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT, 0);
        StandEntity stand = getStandEntity(this.self);
        if (stand != null) {
            // Bring stand back to user
            stand.setPos(this.self.getX(), this.self.getY(), this.self.getZ());
        }
    }

    // checks if diver down is in pilot
    @Override
    public boolean isPiloting() {
        // wow diver down is stealing 2 moves from whitesnake now
        if (self instanceof Player player) {
            StandEntity stand = getStandEntity(player);
            return stand != null && ((IPlayerEntity) player).roundabout$getControlling() == stand.getId();
        }
        return false;
    }

    // starts/stops stand piloting. also initializes the timer.
    @Override
    public void setPiloting(int id) {
        if (this.self instanceof Player player) {
            StandEntity stand = getStandEntity(this.self);
            boolean entering = stand != null && id == stand.getId();

            ((IPlayerEntity) player).roundabout$setIsControlling(entering ? id : 0);
            if (stand instanceof FollowingStandEntity following) {
                // detach the stand entity from the player
                following.setOffsetType(entering ? OffsetIndex.LOOSE : OffsetIndex.FOLLOW);
            }
            if (entering) {
                this.diveTicksLeft = MAX_DIVE_TICKS;
            } else {
                // move returns camera as a failsafe.
                // NOTE TO SELF: MOVE CAMERA STILL DOESN'T WORK, IT'S NOT RETURNING PROPERLY.
                this.diveTicksLeft = 0;
                if (this.self.level().isClientSide()) {
                    Minecraft mc = Minecraft.getInstance();
                    ClientUtil.setCameraEntity(null);
                    if (mc.player != null) {
                        mc.setCameraEntity(mc.player);
                    }
                    CameraType restore = (this.previousCameraType != null) ? this.previousCameraType
                            : CameraType.FIRST_PERSON;
                    mc.options.setCameraType(restore);
                    this.previousCameraType = null;
                }
            }
        }
    }

    @Override
    public void tickPower() {
        super.tickPower();
        // timer for the pilot, kicks you out once it hits 0, all that good stuff.
        if (this.self.level().isClientSide()) {
            boolean pilotingNow = isPiloting();
            if (pilotingNow) {
                wasPilotingClient = true;
                if (this.diveTicksLeft > 0) {
                    this.diveTicksLeft--;
                    if (this.diveTicksLeft <= 0) {
                        exitGroundDive();
                    }
                }
            } else if (wasPilotingClient) {
                // if this runs again, it ends early.
                wasPilotingClient = false;
                exitGroundDive();
            }
        } else {
            if (isPiloting()) {
                if (this.diveTicksLeft > 0) {
                    this.diveTicksLeft--;
                    if (this.diveTicksLeft <= 0) {
                        setPiloting(0);
                    }
                }
            }
        }
    }

    @Override
    public int getMaxPilotRange() {
        // (this is in blocks)
        return 20;
    }

    @Override
    public void synchToCamera() {
        if (isPiloting()) {
            LivingEntity stand = getPilotingStand();
            if (stand != null) {
                ClientUtil.synchToCamera(stand);
                Minecraft mc = Minecraft.getInstance();
                if (mc.options.getCameraType() != CameraType.THIRD_PERSON_BACK) {
                    mc.options.setCameraType(CameraType.THIRD_PERSON_BACK);
                }
            }
        }
    }

    @Override
    public void pilotStandControls(KeyboardPilotInput kpi, LivingEntity entity) {
        if (entity instanceof DiverDownEntity diver) {
            // autostep
            // need to think about if this should be 1.0, or 1.4F so it can go up things
            // like carpeted fences and stuff.
            // will first try out this move with friends, and balance accordingly.
            diver.setMaxUpStep(1.0F);
            // horizontal movement
            float speed = 0.4F;
            float yawRad = diver.getYRot() * ((float) Math.PI / 180F);
            double forward = kpi.forwardImpulse;
            double strafe = kpi.leftImpulse;
            double motionX = (-Math.sin(yawRad) * forward + Math.cos(yawRad) * strafe) * speed;
            double motionZ = (Math.cos(yawRad) * forward + Math.sin(yawRad) * strafe) * speed;
            // stops movement when reaching max range
            double nextX = diver.getX() + motionX;
            double nextZ = diver.getZ() + motionZ;
            double distFromPlayer = Math.hypot(nextX - this.self.getX(), nextZ - this.self.getZ());
            int maxRange = getMaxPilotRange();
            if (distFromPlayer > maxRange) {
                double angle = Math.atan2(nextZ - this.self.getZ(), nextX - this.self.getX());
                double stopX = this.self.getX() + Math.cos(angle) * maxRange;
                double stopZ = this.self.getZ() + Math.sin(angle) * maxRange;
                motionX = stopX - diver.getX();
                motionZ = stopZ - diver.getZ();
            }
            diver.setDeltaMovement(motionX, diver.getDeltaMovement().y, motionZ);
        }
    }

    // diver down has it's own "you can't leave this range" circle, so this is
    // unnecessary
    @Override
    public boolean shouldRenderPilotingHud() {
        return false;
    }

    // replaces hud when piloting
    @Override
    public boolean replaceHudActively() {
        return isPiloting();
    }

    // replaces the exp bar with the timer
    @Override
    public void getReplacementHUD(GuiGraphics context, Player cameraPlayer, int screenWidth, int screenHeight, int x,
            boolean removeNum) {
        if (isPiloting()) {
            // shows the timer for how long diver down pilot is active for
            StandHudRender.renderGroundDiveHud(context, cameraPlayer, screenWidth, screenHeight, x, this);
            return;
        }
        super.getReplacementHUD(context, cameraPlayer, screenWidth, screenHeight, x, removeNum);
    }

    /**
     * Checks if a position is within a 4x4 horizontal area around Diver Down,
     * with the 4 outer 1x1 corners removed to simulate a circle.
     *
     * @param targetX Target X coordinate
     * @param targetY Target Y coordinate
     * @param targetZ Target Z coordinate
     */
    private boolean isInDiveHitbox(double targetX, double targetY, double targetZ) {
        StandEntity stand = getStandEntity(this.self);
        if (stand == null)
            return false;
        double dx = Math.abs(targetX - stand.getX());
        double dy = Math.abs(targetY - stand.getY());
        double dz = Math.abs(targetZ - stand.getZ());
        // Vertical check. 3 represents the height of the hitbox. edit this number to
        // change it.
        if (dy > 3)
            return false;
        // check for if entity is with the 2 block radius
        if (dx > 2.0 || dz > 2.0)
            return false;
        // cut out the 4 corners
        if (dx > 1.0 && dz > 1.0)
            return false;
        return true;
    }

    // Gets chest to open.
    private BlockPos getClosestChest() {
        StandEntity stand = getStandEntity(this.self);
        if (stand == null)
            return null;

        BlockPos standPos = stand.blockPosition();
        BlockPos closestPos = null;
        double closestDistSq = Double.MAX_VALUE;
        // does the math. BlockPos is used to check the positions of blocks.
        for (BlockPos pos : BlockPos.betweenClosed(standPos.offset(-2, 0, -2), standPos.offset(2, 0, 2))) {
            if (isInDiveHitbox(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)) {
                BlockState state = stand.level().getBlockState(pos);
                if (state.getBlock() instanceof ChestBlock || state.getBlock() instanceof BarrelBlock) {
                    // distanceToSqr calculates 3D distance squared to stand's position
                    double distSq = stand.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                    if (distSq < closestDistSq) {
                        closestDistSq = distSq;
                        closestPos = pos.immutable(); // .immutable() is important with betweenClosed!
                    }
                }
            }
        }
        return closestPos;
    }

    // sends the open chest move to the server to process it
    private void tryOpenChest() {
        BlockPos chestPos = this.getClosestChest();
        if (chestPos != null) {
            tryBlockPosPower(OPEN_CHEST, true, chestPos);
            tryBlockPosPowerPacket(OPEN_CHEST, chestPos);
        }
    }

    // Brings up the chest UI on the player's screen
    private boolean openChest(BlockPos chestPos) {
        if (!(this.self instanceof ServerPlayer serverPlayer)) {
            return false;
        }
        if (chestPos == null) {
            return false;
        }
        BlockState state = this.self.level().getBlockState(chestPos);
        MenuProvider menuProvider = state.getMenuProvider(this.self.level(), chestPos);
        if (menuProvider != null) {
            serverPlayer.openMenu(menuProvider);
            this.self.level().blockEvent(chestPos, state.getBlock(), 1, 1);
            // test message, comment out once done
            /*
             * if (this.self instanceof Player player) {
             * player.sendSystemMessage(Component.literal("it's chesting time4"));
             * }
             */
        }
        this.self.level().blockEvent(chestPos, state.getBlock(), 1, 1);
        return true;
    }

    // runs get items code on client and server
    private void tryDiveGetItems() {
        ((StandUser) this.getSelf()).roundabout$tryPower(GROUND_GET_ITEMS, true);
        tryPowerPacket(GROUND_GET_ITEMS);
    }

    // gets the items when the move is pressed
    private boolean diveGetItems() {
        return true;
    }

    /*
     * Checks for clicks in pilot. if there is, trigger the dive barrage.
     * this can't be put in powerActivate because there's no check for clicks.
     */
    @Override
    public void pilotInputAttack() {
        tryDiveBarrage();
    }

    // runs dive barrage on client and server
    private void tryDiveBarrage() {
        ((StandUser) this.getSelf()).roundabout$tryPower(GROUND_DIVE_BARRAGE, true);
        tryPowerPacket(GROUND_DIVE_BARRAGE);
    }

    // dive barrage code
    private boolean groundDiveBarrage() {
        // insert mini barrage code here
        // wait a bit to account for how long the move takes
        // recall stand
        return true;
    }

    // walking heart autostep works on the player, not on the stand. i can't copy
    // that for this, unfortunately.

    // note to self: all of whitesnake's controls can be found in
    // WhitesnakeControlClient
    // i just need to use the camera third person thing though, so I'll just
    // transfer that here instead of making a new file.

    // Ground dive move end

    /**
     * Placeholder function, right now returns false (because dive hasn't even been
     * implemented yet. duh.)
     * 
     * Will be given code to see if diver down is inside someone or not
     */
    private boolean isDiveActive() {
        return false;
    }

    /**
     * Placeholder function, right now returns true
     * 
     * Will be given code to see if X release is manual or auto. true for auto,
     * false for manual
     */
    private boolean releaseMode() {
        return true;
    }

    // Used to stop sounds early
    @Override
    public boolean tryPower(int move, boolean forced) {
        if (!this.getSelf().level().isClientSide && this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
            this.stopSoundsIfNearby(IMPALE_NOISE, 100, true);
        }
        return super.tryPower(move, forced);
    }

    /**
     * Used to check if stand able to be used or not.
     * Use this to render alternative icons for moves etc, depending on what move is
     * being used
     * 
     * Update this if there are more moves that disable stand
     * 
     * @return true if the stand moves are disabled, false otherwise
     */
    public boolean areStandMovesDisabled() {
        return hasLimbsDeployed() || isDiveActive() || isPiloting();
    }

    // 4 overrides below are for making sure the stand can't do a variety of stuff
    // while the stand moves are disabled
    @Override
    public boolean canAttack() {
        if (areStandMovesDisabled())
            return false;
        return super.canAttack();
    }

    @Override
    public boolean canAttackHeavy() {
        if (areStandMovesDisabled())
            return false;
        return super.canAttackHeavy();
    }

    @Override
    public boolean canSummonStandAsEntity() {
        if (areStandMovesDisabled()) {
            return false;
        }
        return super.canSummonStandAsEntity();
    }

    @Override
    public boolean canUseMiningStand() {
        return !areStandMovesDisabled() && super.canUseMiningStand();
    }

    /*
     * this override goes here cuz it goes with the rest of the overrides
     * This will be used for recalling "pilot" moves, such as the limb move,
     * or the dive move when unsummoning stand.
     */
    @Override
    public void onStandSummon(boolean desummon) {
        if (desummon) {
            recallLimbs();
        }
        super.onStandSummon(desummon);
    }

    // animations and attack stuff go here

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
            int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
            float flashAlpha, float otherFlashAlpha) {
        StandUser standUser = ((StandUser) playerEntity);
        boolean standOn = PowerTypes.hasStandActive(playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;

        if (standOn && this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
            float charge = (float) this.attackTimeDuring / this.getMaxPhasePunchTime();
            int barWidth = Math.min(15, Math.round(charge * 15));
            // Background frame
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            // There was the light green with a pink in the center thing that's perfect for
            // a DD charge punch. I'm using that.
            context.blit(StandIcons.JOJO_ICONS, k, j, 213, 89, barWidth, 6);
        } else {
            super.renderAttackHud(context, playerEntity, scaledWidth, scaledHeight, ticks,
                    vehicleHeartCount, flashAlpha, otherFlashAlpha);
        }
    }

    // skins go here

    @Override
    public Component getSkinName(byte skinId) {
        switch (skinId) {
            case DiverDownEntity.PART_6 -> {
                return Component.translatable("skins.roundabout.diver_down.base");
            }
            case DiverDownEntity.LAVA_DIVER -> {
                return Component.translatable("skins.roundabout.diver_down.lavadiver");
            }
            case DiverDownEntity.RED_DIVER -> {
                return Component.translatable("skins.roundabout.diver_down.reddiver");
            }
            case DiverDownEntity.ORANGE_DIVER -> {
                return Component.translatable("skins.roundabout.diver_down.orangediver");
            }
            case DiverDownEntity.TREASURE_DIVER -> {
                return Component.translatable("skins.roundabout.diver_down.treasurediver");
            }
            case DiverDownEntity.BIRTHDAY_DIVER -> {
                return Component.translatable("skins.roundabout.diver_down.birthdaydiver");
            }
            case DiverDownEntity.FIRE_DIVER -> {
                return Component.translatable("skins.roundabout.diver_down.firediver");
            }
            default -> {
                return Component.translatable("skins.roundabout.diver_down.base");
            }
        }
    }

    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                DiverDownEntity.PART_6,
                DiverDownEntity.LAVA_DIVER,
                DiverDownEntity.RED_DIVER,
                DiverDownEntity.ORANGE_DIVER,
                DiverDownEntity.TREASURE_DIVER,
                DiverDownEntity.BIRTHDAY_DIVER,
                DiverDownEntity.FIRE_DIVER);
    }

    // skins end

    // setter, getter, and updater functions below here

    private int getMaxPhasePunchTime() {
        return 25 + (getMeltLevel() * 2);
    }

    private float getChargedPercent() {
        return (((float) this.chargedPhasePunch / (float) getMaxPhasePunchTime()));
    }

    private float getPhasePunchStrength(Entity entity) {
        // lowering the heavy punch damage due to the ability's damage to phase through
        // everything at max charge.
        float punchD = this.getPunchStrength(entity) * 2 + (this.getHeavyPunchStrength(entity) - 2);
        if (this.getReducedDamage(entity)) {
            float ret = (getChargedPercent() * punchD);
            if (this.chargedPhasePunch >= getMaxPhasePunchTime()) {
                ret += 0.5F;
            }
            return ret;
        } else {
            float ret = (getChargedPercent() * punchD) + 3;
            if (this.chargedPhasePunch >= getMaxPhasePunchTime()) {
                ret += 2;
            }
            return ret;
        }
    }

    private float getPhasePunchKnockback() {
        float charge = getChargedPercent();
        if (charge >= 1) {
            return (((float) this.chargedPhasePunch / (float) getMaxPhasePunchTime()) * 3);
        } else if (charge >= 0.5F) {
            return 0.7F;
        }
        return 0.1F;
    }

    private SoundEvent getPhasePunchSound() {
        float charged = getChargedPercent();
        if (charged < 1F) {
            return ModSounds.DIVER_DOWN_HIT_EVENT;
        } else {
            return ModSounds.DIVER_DOWN_HIT_HEAVY_EVENT;
        }
    }

    private float getPhasePunchPitch() {
        float charged = getChargedPercent();
        if (charged < 1F) {
            return 1.5F;
        } else {
            return 1;
        }
    }

    private boolean setPowerChargePhase() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.SNEAK_ATTACK_CHARGE);
        this.poseStand(OffsetIndex.GUARD);
        // uncomment the animation later when done
        // animateStand((byte) 42);
        playStandUserOnlySoundsIfNearby(IMPALE_NOISE, 27, false, false);
        return true;
    }

    private boolean setPowerPhasePunch() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.SNEAK_ATTACK);
        this.poseStand(OffsetIndex.ATTACK);
        this.chargedPhasePunch = Math.min(this.chargedPhasePunch, getMaxPhasePunchTime());
        // uncomment the animation later after done.
        // animateStand((byte) 43);
        return true;
    }

    private void updatePhasePunchCharge() {
        // 20 ticks = 1 second, rn it's at 80 ticks so you can hold the move for 4
        // seconds after max charge.
        if (this.attackTimeDuring >= 80) {
            this.stopSoundsIfNearby(IMPALE_NOISE, 100, true);
            if (this.getSelf() instanceof Player && this.getSelf().level().isClientSide() && isPacketPlayer()) {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                tryPowerPacket(PowerIndex.NONE);
            }
        }
    }

    private void updatePhasePunch() {
        if (this.attackTimeDuring > -1 && this.attackTimeDuring == 5) {
            this.standPhasePunch();
        }
    }

    // WIP FEATURES BELOW HERE, DELETE WHEN DONE
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
