package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import com.ibm.icu.number.Precision;
import com.mojang.authlib.GameProfile;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.block.DiverLimbBlock;
import net.hydra.jojomod.block.DiverLimbBlockEntity;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.DiverDownControlsClient;
import net.hydra.jojomod.client.KeyboardPilotInput;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.BoneProjectileEntity;
import net.hydra.jojomod.entity.stand.DiverDownEntity;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModGamerules;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.*;
import net.hydra.jojomod.client.gui.diverdown.custom_workbench_code.*;
import net.hydra.jojomod.client.hud.StandHudRender;
import net.hydra.jojomod.fates.powers.AbilityScapeBasis;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class PowersDiverDown extends NewPunchingStand {

    // for move ids accessed here. update public bytes every time this is edited.
    private static final byte
            LIMB_SCAFFOLD = 53,
            LIMB_RECALL = 54,
    // Workbench move ids start here
    CRAFTING_TABLE = 55,
            LOOM = 56,
            STONECUTTER = 57,
            ANVIL = 58,
            SMITHING_TABLE = 59,
    //workbench ID end
    OPEN_CHEST = 60,
            GROUND_GET_ITEMS = 61,
            GROUND_DIVE_BARRAGE = 62,
            DIVER_ZIP = 63,
            STORE_KICK_TRAP = 64,
            MANUAL_TRAP_RELEASE = 65,
            TOGGLE_TRAP_MODE = 66,
            DIVER_SUBMERGE_START = 67,
            DIVER_EMERGE = 68,
            DISASSEMBLE_BLOCK = 69,
            DIVER_SELF_SUBMERGE = 70,
            DISGUISE = 71,
            EMBED_POTION = 72,
            DIVER_LEGS = 73,
            EFFECT_CURE = 74,
            TRANSFER = 75,
            RIBCAGE_TRAP = 76,
            BONE_BOMB = 77,
            SPRING_LEGS = 78,
            TOGGLE_DAMAGE_REDIRECT = 79;

    // for all the move ids accessed elsewhere.
    public static final byte
            ACCESS_WORKBENCH = 118,
            ACCESS_AFFLICTIONS = 119;

    // NOISES GO BELOW HERE, starting from 120. I think that should be more than
    // enough.
    public static final byte CHARGE_NOISE = 120;

    // used for limb scaffolds
    private int MAX_LIMB_DISTANCE = 3;
    // the next 2 variables are used for the charge phase punch later
    public boolean holdDownClick = false;
    public int chargedPhasePunch = 0;

    // used for ground dive
    public static final int MAX_DIVE_TICKS = 200; // 10 seconds
    public int diveTicksLeft = 0;
    private boolean wasPilotingClient = false;
    private boolean isBarrel = false;
    public volatile List<BlockPos> detectedOres = new ArrayList<>();
    private int oreScanCooldown = 0;
    public boolean isOpeningRemoteChest = false;
    public boolean oreDetectionEnabled = false;

    // used for ground barrage
    private int MAX_GROUND_BARRAGE_TICKS = 20; // will count down from 10 ticks AKA half a second + 1 for the final hit
    // + 9 for animation
    private int barrageTicksLeft = 0;

    // used for diver zip
    private Direction feetDirection = Direction.DOWN;
    public int justFlippedTicks = 0;
    private int mercyTicks = 0;
    private Vec3 lastGroundPosition = Vec3.ZERO;
    private Direction cutDirection;

    // used for traps
    public static class KickTrap {
        public int ticks;
        public Direction face;
        public Vec3 launchVector;
        public int playerContactTicks = 0;

        public KickTrap(int ticks, Direction face, Vec3 launchVector) {
            this.ticks = ticks;
            this.face = face;
            this.launchVector = launchVector;
        }
    }

    public final Map<BlockPos, KickTrap> storedKickTraps = new LinkedHashMap<>();
    private static final int MAX_TRAP_DURATION = 2400; // 2 minute lifetime
    private static final float TRAP_RANGE = 5.5f;
    private static final int MAX_NUMBER_OF_TRAPS = 10;
    public static final int KICK_TRAP_WINDUP_MAX = 10;
    public final Map<BlockPos, Integer> releasingLimbs = new HashMap<>();
    // water bucket

    // RELEASE
    // 🔥🔥🔥🔥🔥
    private boolean isAutoRelease = true; // true = automatic, false = manual

    // used for dive
    public Entity submergedTarget = null;
    public int submergedTargetId = -1; //for client/server desynchronization fix
    public static final int DIVE_WINDUP_MAX = 30; // 1.5 seconds uncancellable windup
    public static final float DIVE_REACH = 4.0f; // how far it goes
    public boolean isTransferringDamage = false; // recursion guard, prevents things like 2 DDs repeatedly protecting each other
    public boolean damageRedirectionEnabled = true;
    public boolean hasDiverLegs = false;
    public boolean showDisguiseArmor = true;
    public static final int TRANSFER_WINDUP_MAX = 40;
    private static final double TRANSFER_RANGE = 4.0;
    public LivingEntity ribcageTarget = null;
    public Vec3 ribcageWalkDirection = null;
    public int ribcageTrapTicks = 0;
    public static final int RIBCAGE_MAX_DURATION = 200; // 10 seconds max duration
    public static final double HOSTILE_DETECTION_RANGE = 16.0; // non-zombie hostile detection range
    public LivingEntity springTarget = null;
    public int springLegsTicks = 0;
    public static final int SPRING_LEGS_MAX_DURATION = 300; // 15 seconds max duration

    // for cooldowns
    private final int getAfflictionCooldown(){
        return 200;
    };
    /* TAKING NOTE OF GENERAL COOLDOWNS HERE:
        GENERAL_1 = AFFLICTION SELECTION
     */

    // move levels

    public int getGroundDiveLevel(){
        return 7;
    }
    public int getDiverZipLevel(){
        return 6;
    }
    public int getDiverLimbLevel(){
        return 5;
    }
    public int getBlockDisassemblyLevel(){
        return 4;
    }
    public int getWorkbenchLevel(){
        return 3;
    }
    public int getKickStorageLevel(){
        return 2;
    }
    //workbench levels
    public int getAnvilLevel(){
        return 7;
    }
    public int getSmithingTableLevel(){
        return 6;
    }
    public int getStonecutterLevel(){
        return 5;
    }
    public int getLoomLevel(){
        return 4;
    }


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

    // configs here

    public boolean canWallZipConfig() {
        return ClientNetworking.getAppropriateConfig().miscellaneousSettings.enableWallWalking;
    }

    // icons and ability list here

    /**
     * Renders the icons for the stand's abilities on the GUI. Comments will specify
     * if icon is for courching, blocking, or normal.
     */
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        // other modes
        if (areStandMovesDisabled() && !(this.getActivePower() == DIVER_SUBMERGE_START)) {
            // submerge
            if(isDiveActive()) {
                    if (damageRedirectionEnabled) {
                        setSkillIcon(context, x, y, 1, StandIcons.REDIRECTION_ENABLED, PowerIndex.NO_CD);
                    } else {
                        setSkillIcon(context, x, y, 1, StandIcons.REDIRECTION_DISABLED, PowerIndex.NO_CD);
                    }
                setSkillIcon(context, x, y, 2, StandIcons.DIVER_DOWN_AFFLICTION, PowerIndex.GENERAL_1);
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
                setSkillIcon(context, x, y, 4, StandIcons.DIVER_DOWN_RECALL, PowerIndex.NO_CD);
            }
            // diver zip
            else if(inZipMode()) {
                setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD, true);
                setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.NO_CD, true);
                setSkillIcon(context, x, y, 3, StandIcons.DIVER_DOWN_ZIP, PowerIndex.SKILL_3);
                setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD, true);
            }
            // diver limb scaffold
            else if(hasLimbsDeployed()) {
                setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD, true);
                setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.NO_CD, true);
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
                if (isHoldingSneak()) {
                    setSkillIcon(context, x, y, 4, StandIcons.DIVER_DOWN_PLATFORM, PowerIndex.SKILL_4);
                } else {
                    setSkillIcon(context, x, y, 4, StandIcons.DIVER_DOWN_RECALL, PowerIndex.NO_CD);
                }
            }
            // ground dive
            else if(isPiloting()) {
                if (oreDetectionEnabled) {
                    setSkillIcon(context, x, y, 1, StandIcons.ORE_DETECTION_ENABLED, PowerIndex.NO_CD);
                } else {
                    setSkillIcon(context, x, y, 1, StandIcons.ORE_DETECTION_DISABLED, PowerIndex.NO_CD);
                }
                setSkillIcon(context, x, y, 2, StandIcons.DIVER_DOWN_GRAB, PowerIndex.NO_CD);
                setSkillIcon(context, x, y, 3, StandIcons.DIVER_DOWN_CHEST, PowerIndex.NO_CD);
                setSkillIcon(context, x, y, 4, StandIcons.DIVER_DOWN_RECALL, PowerIndex.NO_CD);
            }
        }

        // Ability 1 (Z)
        else {
            if (isHoldingSneak()) {
                if (canExecuteMoveWithLevel(getBlockDisassemblyLevel())) {
                    setSkillIcon(context, x, y, 1, StandIcons.DIVER_DOWN_DISASSEMBLE, PowerIndex.SKILL_1_SNEAK);
                } else {
                    setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD, true);
                }
            } else if (isGuarding()) {
                setSkillIcon(context, x, y, 1, StandIcons.DIVER_DOWN_SELF_SUBMERGE, PowerIndex.SKILL_1_GUARD);
            } else {
                setSkillIcon(context, x, y, 1, StandIcons.DIVER_DOWN_SUBMERGE, PowerIndex.SKILL_1);
            }

            // Ability 2 (X)
            if (isGuarding()) {
                if (canExecuteMoveWithLevel(getKickStorageLevel())) {
                    // isAutoRelease will be true if auto, false if manual
                    if (this.isAutoRelease)
                        setSkillIcon(context, x, y, 2, StandIcons.DIVER_DOWN_TOGGLE_AUTO, PowerIndex.NO_CD);
                    else
                        setSkillIcon(context, x, y, 2, StandIcons.DIVER_DOWN_TOGGLE_MANUAL, PowerIndex.NO_CD);
                } else {
                    setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.NO_CD, true);
                }
            } else if (isHoldingSneak()) {
                if (canExecuteMoveWithLevel(getKickStorageLevel())) {
                    // changes the icons for deletion vs releation (real)
                    if (this.isAutoRelease)
                        setSkillIcon(context, x, y, 2, StandIcons.DIVER_DOWN_CANCEL_STORE, PowerIndex.SKILL_2_SNEAK);
                    else
                        setSkillIcon(context, x, y, 2, StandIcons.DIVER_DOWN_RELEASE_MANUAL, PowerIndex.SKILL_2_SNEAK);
                } else {
                    setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.NO_CD, true);
                }
            } else {
                if (canExecuteMoveWithLevel(getKickStorageLevel())) {
                    setSkillIcon(context, x, y, 2, StandIcons.DIVER_DOWN_STORE, PowerIndex.SKILL_2);
                } else {
                    setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.NO_CD, true);
                }
            }

            // Ability 3 (C)
            if (isHoldingSneak()) {
                if (canExecuteMoveWithLevel(getDiverZipLevel())) {
                    setSkillIcon(context, x, y, 3, StandIcons.DIVER_DOWN_ZIP, PowerIndex.SKILL_3);
                } else {
                    setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NO_CD, true);
                }
            } else if (canLatchOntoWall()) {
                if (canExecuteMoveWithLevel(getDiverZipLevel())) {
                    setSkillIcon(context, x, y, 3, StandIcons.DIVER_DOWN_VAULT, PowerIndex.SKILL_3);
                } else {
                    setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NO_CD, true);
                }
            } else {
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
            }

            // Ability 4 (V)
                if (isGuarding()) {
                    if (canExecuteMoveWithLevel(getWorkbenchLevel())) {
                        setSkillIcon(context, x, y, 4, StandIcons.DIVER_DOWN_WORKSTATION, PowerIndex.NO_CD);
                    } else {
                        setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD, true);
                    }
                } else if (isHoldingSneak()) {
                    if (canExecuteMoveWithLevel(getDiverLimbLevel())) {
                        setSkillIcon(context, x, y, 4, StandIcons.DIVER_DOWN_PLATFORM, PowerIndex.SKILL_4_SNEAK);
                    } else {
                        setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD, true);
                    }
                } else {
                    if (canExecuteMoveWithLevel(getGroundDiveLevel())) {
                        setSkillIcon(context, x, y, 4, StandIcons.DIVER_DOWN_GROUND_DIVE, PowerIndex.SKILL_4);
                    } else {
                        setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD, true);
                    }
                }
        }
    }

    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos,
                                                  int topPos, byte level, boolean bypas) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        int startPos = -8;
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20 + startPos, topPos + 80, 0, "ability.roundabout.punch",
                "instruction.roundabout.press_attack", StandIcons.DIVER_DOWN_PUNCH, 0, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20 + startPos, topPos + 99, 0, "ability.roundabout.guard",
                "instruction.roundabout.hold_block", StandIcons.DIVER_DOWN_GUARD, 0, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20 + startPos, topPos + 118, 0, "ability.roundabout.diver_phase_punch",
                "instruction.roundabout.hold_attack_crouch", StandIcons.DIVER_DOWN_PHASE_PUNCH, 0, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39 + startPos, topPos + 80, 0, "ability.roundabout.barrage",
                "instruction.roundabout.barrage", StandIcons.DIVER_DOWN_BARRAGE, 0, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39 + startPos, topPos + 99, 0, "ability.roundabout.diver_submerge",
                "instruction.roundabout.press_skill", StandIcons.DIVER_DOWN_SUBMERGE, 1, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39 + startPos, topPos + 118, 0, "ability.roundabout.diver_self_submerge",
                "instruction.roundabout.press_skill_block", StandIcons.DIVER_DOWN_SELF_SUBMERGE, 1, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 58 + startPos, topPos + 80, getBlockDisassemblyLevel(), "ability.roundabout.diver_disassemble",
                "instruction.roundabout.press_skill_crouch", StandIcons.DIVER_DOWN_DISASSEMBLE, 1, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 58 + startPos, topPos + 99, 0, "ability.roundabout.diver_redirection",
                "instruction.roundabout.press_skill_submerged", StandIcons.REDIRECTION_ENABLED, 1, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 58 + startPos, topPos + 118, 0, "ability.roundabout.diver_selection",
                "instruction.roundabout.press_skill_submerged", StandIcons.DIVER_DOWN_AFFLICTION, 2, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 77 + startPos, topPos + 80, getKickStorageLevel(), "ability.roundabout.diver_store",
                "instruction.roundabout.press_skill", StandIcons.DIVER_DOWN_STORE, 2, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 77 + startPos, topPos + 99, getKickStorageLevel(), "ability.roundabout.diver_release_toggle",
                "instruction.roundabout.press_skill_block", StandIcons.DIVER_DOWN_TOGGLE_AUTO, 2, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 77 + startPos, topPos + 118, getKickStorageLevel(), "ability.roundabout.diver_cancel_store",
                "instruction.roundabout.press_skill_crouch", StandIcons.DIVER_DOWN_CANCEL_STORE, 2, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 96 + startPos, topPos + 80, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE, 3, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 96 + startPos, topPos + 99, getDiverZipLevel(), "ability.roundabout.diver_wall_zip",
                "instruction.roundabout.press_skill_air", StandIcons.DIVER_DOWN_VAULT, 3, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 96 + startPos, topPos + 118, getDiverZipLevel(), "ability.roundabout.diver_zip",
                "instruction.roundabout.press_skill_crouch", StandIcons.DIVER_DOWN_ZIP, 3, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 115 + startPos, topPos + 80, getDiverLimbLevel(), "ability.roundabout.diver_limb_platform",
                "instruction.roundabout.press_skill_crouch", StandIcons.DIVER_DOWN_PLATFORM, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 115 + startPos, topPos + 99, getWorkbenchLevel(), "ability.roundabout.diver_workstation",
                "instruction.roundabout.press_skill_block", StandIcons.DIVER_DOWN_WORKSTATION, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 115 + startPos, topPos + 118, getGroundDiveLevel(), "ability.roundabout.diver_ground_dive",
                "instruction.roundabout.press_skill", StandIcons.DIVER_DOWN_GROUND_DIVE, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 134 + startPos, topPos + 80, getGroundDiveLevel(), "ability.roundabout.diver_detection",
                "instruction.roundabout.press_skill_submerged", StandIcons.ORE_DETECTION_ENABLED, 1, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 134 + startPos, topPos + 99, getGroundDiveLevel(), "ability.roundabout.diver_grab",
                "instruction.roundabout.press_skill_submerged", StandIcons.DIVER_DOWN_GRAB, 2, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 134 + startPos, topPos + 118, getGroundDiveLevel(), "ability.roundabout.diver_chest",
                "instruction.roundabout.press_skill_submerged", StandIcons.DIVER_DOWN_CHEST, 3, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 153 + startPos, topPos + 80, 0, "ability.roundabout.diver_recall",
                "instruction.roundabout.press_skill_submerged", StandIcons.DIVER_DOWN_RECALL, 4, level, bypas));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 153 + startPos, topPos + 99, 0, "ability.roundabout.mining",
                "instruction.roundabout.hold_attack", StandIcons.DIVER_DOWN_MINING, 0, level, bypas));
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

    @Override
    public float getPunchLandPitch(){
        return 1.1F + 0.05F * activePowerPhase;
    }

    @Override
    public float getPunchLandLastPitch(){
        return 1.2F;
    }

    @Override
    public SoundEvent getPunchLandSound(){
        return ModSounds.DIVER_DOWN_HIT_EVENT;
    }

    @Override
    public SoundEvent getPunchLandLastSound(){
        return ModSounds.DIVER_DOWN_HIT_HEAVY_EVENT;
    }

    @Override
    public void playBarrageEndNoise(float mod, Entity entity){
        if (!this.self.level().isClientSide()) {
            playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.DIVER_DOWN_HIT_HEAVY_EVENT, SoundSource.PLAYERS, 0.95F+mod, 1F);
        }
    }

    @Override
    public void playBarrageNoise(int hitNumber, Entity entity){
        if (!this.self.level().isClientSide()) {
            if (hitNumber % 3 == 0)
                playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.DIVER_DOWN_BARRAGE_EVENT, SoundSource.PLAYERS, 0.9F, (float) (0.9 + (Math.random() * 0.25)));
        }
    }

    // START OF ACTUAL MOVE METHODS

    //for cooldowns, like D4C
    @Override
    public boolean isServerControlledCooldown(byte num){
        if (num == PowerIndex.SKILL_1 || num == PowerIndex.SKILL_1_SNEAK
                || num == PowerIndex.SKILL_2 || num == PowerIndex.GENERAL_1
                || num == PowerIndex.SKILL_3) {
            return true;
        }
        return super.isServerControlledCooldown(num);
    }

    @Override
    public void tickPower() {
        super.tickPower();
        if (!this.self.level().isClientSide()) {
            // deletes limbs that are out of range
            if (!this.activeLimbs.isEmpty()) {
                Level level = this.self.level();
                double maxRangeSq = (double) getMaxPilotRange() * getMaxPilotRange();
                boolean changed = false;

                java.util.Iterator<BlockPos> iterator = this.activeLimbs.iterator();
                while (iterator.hasNext()) {
                    BlockPos limbPos = iterator.next();
                    boolean isLimbStillThere = level.getBlockState(limbPos).is(ModBlocks.DIVER_LIMB);
                    boolean isOutOfRange = this.self.distanceToSqr(Vec3.atCenterOf(limbPos)) > maxRangeSq;

                    if (!isLimbStillThere || isOutOfRange) {
                        if (isLimbStillThere) {
                            level.removeBlock(limbPos, false);
                        }
                        iterator.remove();
                        changed = true;
                    }
                }

                // If all limbs are gone, resummon Diver Down
                if (changed && this.activeLimbs.isEmpty()) {
                    this.currentLimbIndex = 0;
                    //sync with client
                    if (this.self instanceof Player player) {
                        S2CPacketUtil.sendIntPowerDataPacket(player, LIMB_RECALL, -1);
                    }
                    if (hasStandActive(this.self)) {
                        ((StandUser) this.self).roundabout$summonStand(level, true, false);
                        playSoundIfPossible(self.level(), null, this.self.blockPosition(),
                                ModSounds.SUMMON_DIVER_DOWN_EVENT,
                                SoundSource.PLAYERS, 0.85F, 1);
                    }
                }
            }
        }
        // force crawl mode in zippy time
        if (inZipMode()) {
            this.self.setSprinting(false);
            ((StandUser) this.self).rdbt$SetCrawlTicks(5);
            this.self.setPose(Pose.SWIMMING);
            this.self.setSwimming(true);
        }
        // timer for the pilot, kicks you out once it hits 0, all that good stuff.
        if (this.self.level().isClientSide()) {
            if (this.getActivePower() == GROUND_DIVE_BARRAGE) {
                if (this.barrageTicksLeft > 0) {
                    this.barrageTicksLeft--;
                } else {
                    this.setPowerNone();
                    exitGroundDive();
                }
            }
            boolean pilotingNow = isPiloting();
            if (pilotingNow) {
                wasPilotingClient = true;
                DiverDownControlsClient.handleChestAudio(this.isBarrel);

                if (this.isOpeningRemoteChest) {
                    // While opening or looking in chest, do not exit dive
                    if (DiverDownControlsClient.isScreenOpen()) {
                        // Screen is now opened!
                    } else if (DiverDownControlsClient.wasChestScreenClosed()) {
                        // Player closed chest screen, now exit!
                        this.isOpeningRemoteChest = false;
                        exitGroundDive();
                    }
                } else {
                    if (this.diveTicksLeft > 0) {
                        this.diveTicksLeft--;
                    } else if (this.diveTicksLeft <= 0) {
                        if (!DiverDownControlsClient.isScreenOpen()) {
                            exitGroundDive();
                        }
                    }
                }
                if (this.oreDetectionEnabled) {
                    if (this.oreScanCooldown <= 0) {
                        scoutForOresBeneathClient();
                        this.oreScanCooldown = 3;
                    } else {
                        this.oreScanCooldown--;
                    }
                } else {
                    if (!this.detectedOres.isEmpty()) {
                        this.detectedOres = Collections.emptyList();
                    }
                }
            } else if (wasPilotingClient) {
                // if this runs again, it ends early.
                wasPilotingClient = false;
                exitGroundDive();
            }
            if (isPacketPlayer()) {
                // pull down in air
                if (inZipMode() && !getStandUserSelf().rdbt$getJumping()) {
                    if (!self.onGround()) {
                        if (this.self.getDeltaMovement().y < 0) {
                            if (!(canCutCorners() && justFlippedTicks > 0)) {
                                this.self.setDeltaMovement(this.self.getDeltaMovement().add(0, -0.14, 0));
                            }
                        }
                    }
                }
                // disengage if swimming
                if (self.isInWater()) {
                    toggleZip(false);
                    C2SPacketUtil.trySingleBytePacket(PacketDataIndex.QUERY_STAND_UPDATE_2);
                }
                // Check for ground/ledge with mercyTicks(coyote time) grace period
                if (inZipMode()) {
                    if (justFlippedTicks > 0) {
                        justFlippedTicks--;
                    } else {
                        if (this.self.horizontalCollision && canCutCorners() && justFlippedTicks <= 0) {
                            Direction facing = RotationUtil.getRealFacingDirection2(this.self);
                            Direction grav = ((IGravityEntity) this.self).roundabout$getGravityDirection();
                            if (facing != grav && facing != grav.getOpposite()) {
                                Vec3 mpos = this.self.getPosition(1F);
                                // Check if there is a walkable block in front of the player
                                BlockPos wallPos = BlockPos.containing(mpos).relative(facing);
                                if (MainUtil.isBlockWalkable(this.self.level().getBlockState(wallPos))) {
                                    ((IGravityEntity) this.self).roundabout$setGravityDirection(facing);
                                    setHeelDirection(facing);
                                    justFlippedTicks = 7;
                                    C2SPacketUtil.intToServerPacket(
                                            PacketDataIndex.INT_GRAVITY_FLIP, MainUtil.getIntFromDirection(facing));
                                }
                            }
                        }
                        // Check block probe positions beneath player relative to gravity
                        Vec3 newVec = RotationUtil.vecPlayerToWorld(new Vec3(0, -0.2, 0),
                                ((IGravityEntity) self).roundabout$getGravityDirection());
                        BlockPos pos = BlockPos.containing(self.getPosition(1).add(newVec));
                        Vec3 newVec2 = RotationUtil.vecPlayerToWorld(new Vec3(0, -1.0, 0),
                                ((IGravityEntity) self).roundabout$getGravityDirection());
                        BlockPos pos2 = BlockPos.containing(self.getPosition(1).add(newVec2));
                        Vec3 newVec4 = RotationUtil.vecPlayerToWorld(new Vec3(0, -0.5, 0),
                                ((IGravityEntity) self).roundabout$getGravityDirection());
                        BlockPos pos4 = BlockPos.containing(self.getPosition(1).add(newVec4));
                        Vec3 newVec5 = RotationUtil.vecPlayerToWorld(new Vec3(0, -1.1, 0),
                                ((IGravityEntity) self).roundabout$getGravityDirection());
                        BlockPos pos5 = BlockPos.containing(self.getPosition(1).add(newVec5));
                        if (self.onGround() && MainUtil.isBlockWalkableSimplified(self.getBlockStateOn())) {
                            mercyTicks = 12; // needs lots of coyote time for sprinting
                            lastGroundPosition = self.position();
                        } else {
                            // If ANY block directly beneath your rotated feet is solid, you are still on a
                            // surface
                            if (MainUtil.isBlockWalkable(self.level().getBlockState(pos))
                                    || MainUtil.isBlockWalkable(self.level().getBlockState(pos2))
                                    || MainUtil.isBlockWalkable(self.level().getBlockState(pos4))
                                    || MainUtil.isBlockWalkable(self.level().getBlockState(pos5))) {
                                mercyTicks--;
                            } else {
                                // Only attempt to cut the corner when all probe blocks are AIR (stepped off
                                // edge)
                                if (self.onGround() && MainUtil.isBlockWalkableSimplified(self.getBlockStateOn())) {
                                    mercyTicks = 8;
                                    lastGroundPosition = self.position();
                                } else {
                                    mercyTicks--;
                                }
                            }
                        }
                        // cancel power if something bad happens
                        if (getStandUserSelf().rdbt$getJumping() || self.isSleeping()
                                || (!self.onGround() && !this.getStandUserSelf().roundabout$isPossessed()
                                && mercyTicks <= 0)
                                || self.getRootVehicle() != this.self) {
                            feetDirection = Direction.DOWN;
                            toggleZip(false);
                            C2SPacketUtil.trySingleBytePacket(PacketDataIndex.QUERY_STAND_UPDATE_2);

                            // Reset gravity direction to DOWN
                            ((IGravityEntity) this.self).roundabout$setGravityDirection(feetDirection);
                            justFlippedTicks = 5;
                            C2SPacketUtil.intToServerPacket(PacketDataIndex.INT_GRAVITY_FLIP,
                                    MainUtil.getIntFromDirection(feetDirection));
                        }
                    }
                } else {
                    feetDirection = Direction.DOWN;
                }
            }
        }
        // server side ticks
        else {
                tickRibcageTrap();
                tickSpringLegs();
                // recall stand if target dies, or if they go too far
                if (isDiveActive()) {
                    if (!this.submergedTarget.isAlive()
                            || this.submergedTarget.isRemoved()
                            || this.self.distanceTo(this.submergedTarget) > (getMaxPilotRange())) {
                        emergeServer();
                    }
                }
                // trap detection
                if (!this.storedKickTraps.isEmpty()) {
                    Iterator<Map.Entry<BlockPos, KickTrap>> it = this.storedKickTraps.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<BlockPos, KickTrap> entry = it.next();
                        BlockPos trapPos = entry.getKey();
                        KickTrap trap = entry.getValue();

                        // remove the traps if the timer runs out
                        if (--trap.ticks <= 0 || this.self.level().getBlockState(trapPos).isAir()) {
                            it.remove();
                            continue;
                        }

                        // particle effects
                        if (trap.ticks % 10 == 0) {
                            double px = trapPos.getX() + 0.5 + trap.face.getStepX() * 0.52;
                            double py = trapPos.getY() + 0.5 + trap.face.getStepY() * 0.52;
                            double pz = trapPos.getZ() + 0.5 + trap.face.getStepZ() * 0.52;
                            sendKickTrapParticles(px, py, pz);
                        }

                        // check if something touches the trap
                        if (this.isAutoRelease) {
                            List<LivingEntity> victims = detectTrapTrigger(trapPos, trap.face);
                            if (!victims.isEmpty()) {
                                boolean hasPlayer = false;
                                boolean hasMob = false;

                                for (LivingEntity v : victims) {
                                    if (v instanceof Player) {
                                        hasPlayer = true;
                                        // doesn't trigger if it's a pet
                                    } else if (!(v instanceof TamableAnimal TA && TA.getOwner() != null && TA.getOwner().is(this.getSelf()))){
                                        hasMob = true;
                                    }
                                }

                                if (hasPlayer) {
                                    trap.playerContactTicks++;
                                } else {
                                    trap.playerContactTicks = 0;
                                }

                                // trigger the traps auto on mobs, or after 1 second has passed for players
                                boolean shouldTrigger = hasMob || (trap.playerContactTicks >= 20);

                                if (shouldTrigger) {
                                    // remove players from the victim list, in case they were standing with mobs
                                    if (trap.playerContactTicks < 20) {
                                        victims.removeIf(v -> v instanceof Player);
                                    }
                                    if (!victims.isEmpty()) {
                                        triggerKickTrap(victims, trapPos, trap);
                                        it.remove(); // delete the triggered trap
                                    }
                                }
                            } else {
                                // reset timer if player leaves the trap block
                                trap.playerContactTicks = 0;
                            }
                        }
                    }
                }
                // cleanup the limbs if they expire
                if (!this.releasingLimbs.isEmpty()) {
                    Iterator<Map.Entry<BlockPos, Integer>> limbIt = this.releasingLimbs.entrySet().iterator();
                    while (limbIt.hasNext()) {
                        Map.Entry<BlockPos, Integer> entry = limbIt.next();
                        int remaining = entry.getValue() - 1;
                        if (remaining <= 0) {
                            BlockPos pos = entry.getKey();
                            if (this.self.level().getBlockState(pos).is(ModBlocks.DIVER_LIMB)) {
                                this.self.level().removeBlock(pos, false);
                            }
                            limbIt.remove();
                        } else {
                            entry.setValue(remaining);
                        }
                    }
                }
            }
            if (isPiloting()) {
                if (this.isOpeningRemoteChest) {
                    if (this.self instanceof ServerPlayer sp) {
                        // Wait until the chest menu is opened then closed
                        if (sp.containerMenu != sp.inventoryMenu) {
                            // Chest is actively open
                        } else {
                            // Player closed chest or hasn't opened yet; give 20 ticks grace
                            if (this.diveTicksLeft > 0) {
                                this.diveTicksLeft--;
                            } else {
                                this.isOpeningRemoteChest = false;
                                exitGroundDive();
                            }
                        }
                    }
                } else {
                    if (this.diveTicksLeft > 0) {
                        this.diveTicksLeft--;
                    } else if (this.diveTicksLeft <= 0) {
                        if (this.self instanceof ServerPlayer sp && sp.containerMenu == sp.inventoryMenu) {
                            exitGroundDive();
                        }
                    }
                }
            }
            LivingEntity stand = getPilotingStand();
            if (this.getActivePower() == GROUND_DIVE_BARRAGE && !this.self.level().isClientSide) {
                if (this.barrageTicksLeft > 0) {
                    this.barrageTicksLeft--;
                    // need to filter targets still to account for that pesky boss immunity
                    List<Entity> unfilteredTargets = getEntitiesBox();
                    // for boss filtering function, taken from walking heart. It took me a while to
                    // figure out that FE meant "Filtered Entities," so im leaving this variable as
                    // the whole thing for future reference.
                    List<Entity> filteredEntities = new ArrayList<>();
                    for (Entity target : unfilteredTargets) {
                        if (ClientNetworking.getAppropriateConfig().miscellaneousSettings.wallPassingHitboxesOnBosses) {
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
                    // filter end
                    for (Entity target : filteredEntities) {
                        if (target instanceof LivingEntity living) {
                            if (this.barrageTicksLeft > 11) {
                                // motion stores the knockback from the move then immediately deletes it
                                // this ensures that enemies can still run around while the move is hitting them
                                // whilst also making sure that the move doesn't send them flying away
                                Vec3 motion = living.getDeltaMovement();
                                DamageHandler.StandDamageEntity(living, 0.25F, this.self);
                                living.setDeltaMovement(motion);
                                hitParticles(living);
                                // animate the barrage and also sound here
                                playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.DIVER_DOWN_GROUND_BARRAGE_EVENT,
                                        SoundSource.PLAYERS, 0.55F, (float) (0.9 + (Math.random() * 0.30)));
                            } else if (this.barrageTicksLeft == 9) {
                                // BIG FINAL PUNCH!!! (does bleed)
                                DamageHandler.StandDamageEntity(living, 7.0F, this.self);
                                MainUtil.makeBleed(target, 0, 300, stand);
                                living.setDeltaMovement(living.getDeltaMovement().x * 0.3, 1.35D,
                                        living.getDeltaMovement().z * 0.3);
                                living.hurtMarked = true;
                                MainUtil.knockShieldPlusStand(living, 60); // 60 ticks = 3 seconds
                                // big final punch anim and sound here
                                playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.DIVER_DOWN_HIT_HEAVY_EVENT, SoundSource.PLAYERS, 0.95F, 1F);
                                hitParticlesCenter(living);
                                sendParticlesIfPossible(this.self.level(), ModParticles.AIR_CRACKLE,
                                        living.getX(), living.getY() + (living.getBbHeight() * 0.5), living.getZ(),
                                        1, 0.0, 0.0, 0.0, 0);
                                exitGroundDive();
                            } else {
                                // do nothing, wait for animation to finish
                                // revisit this with animated to adjust ticks based on animation
                            }
                        }
                    }
                } else {
                    this.setPowerNone();
                    exitGroundDive();
                }
            }
            if (!inZipMode()) {
                feetDirection = Direction.DOWN;
            } else if (justFlippedTicks <= 0) {
                Vec3 newVec = new Vec3(0, -0.2, 0);
                Vec3 newVec4 = new Vec3(0, -0.5, 0);
                newVec = RotationUtil.vecPlayerToWorld(newVec, ((IGravityEntity) self).roundabout$getGravityDirection());
                BlockPos pos = BlockPos.containing(self.getPosition(1).add(newVec));
                newVec4 = RotationUtil.vecPlayerToWorld(newVec4, ((IGravityEntity) self).roundabout$getGravityDirection());
                BlockPos pos4 = BlockPos.containing(self.getPosition(1).add(newVec4));
                BlockState state1 = self.level().getBlockState(pos);
                BlockState state4 = self.level().getBlockState(pos4);
                boolean isOnValidBlock = MainUtil.isBlockWalkableSimplified(state1)
                        && MainUtil.isBlockWalkableSimplified(state4);
                if (!isOnValidBlock && !this.self.onGround()) {
                    toggleZip(false);
                }
        }
    }

    @Override
    public boolean canVault() {
        return false; // Disable normal vaulting so zip takes over
    }

    /*
    like walking heart, this stand won't have a vault. instead, you will have a wall latch that lets you enter zip mode.
     */
    public boolean canLatchOntoWall() {
        if (self.isInWater())
            return false;
        if (MainUtil.isStandingInBlock(self))
            return false;
        if (forceBlock())
            return false;
        if ((this.self.onGround() && !inZipMode()) || (!this.self.onGround() && inZipMode()))
            return false;
        if (areStandMovesDisabled())
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

    /**
     * (non-Javadoc)
     * buttonInputAttack is overriden to add the heavy charged crouch punch.
     * Additional changes need to be made to give the charged punch AOE
     *
     * @see net.hydra.jojomod.fates.powers.AbilityScapeBasis#buttonInputAttack(boolean,
     * net.minecraft.client.Options)
     */
    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (areStandMovesDisabled()) {
            return;
        }
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
            if (inZipMode()) {
                // allows toggling the mode and that's it
                if (context == PowerContext.SKILL_3_CROUCH || context == PowerContext.SKILL_3_NORMAL) {
                    tryDiverZip();
                }
                return;
            } else if (hasLimbsDeployed()) {
                // pressing V recalls limbs if limb move is active
                if(context == PowerContext.SKILL_3_NORMAL)
                    tryToDashClient();
                else if (context == PowerContext.SKILL_4_NORMAL) {
                    tryRecallLimbs();
                    // note: still not sure about the heirarchy for sneak + guard moves. Replace
                    // this when known.
                } else if (context == PowerContext.SKILL_4_CROUCH || context == PowerContext.SKILL_4_CROUCH_GUARD) {
                    tryLimbClimb();
                }
                // stops everything else from working
                return;
            } else if (isPiloting()) {
                if (context == PowerContext.SKILL_1_NORMAL) {
                    toggleOreDetection();
                } else if (context == PowerContext.SKILL_4_NORMAL) {
                    setCooldown(PowerIndex.SKILL_4, 200);
                    exitGroundDive();
                } else if (context == PowerContext.SKILL_3_NORMAL) {
                    tryOpenChest();
                } else if (context == PowerContext.SKILL_2_NORMAL) {
                    tryDiveGetItems();
                }
                // stops everything else from working
                return;
            } else if (isDiveActive()) {
                if (context == PowerContext.SKILL_1_NORMAL || context == PowerContext.SKILL_1_CROUCH){
                    tryToggleDamageRedirect();
                }
                else if(context == PowerContext.SKILL_3_NORMAL)
                    tryToDashClient();
                else if (context == PowerContext.SKILL_4_NORMAL || context == PowerContext.SKILL_4_CROUCH) {
                    tryEmergeClient();
                }
                else if (context == PowerContext.SKILL_2_NORMAL || context == PowerContext.SKILL_2_CROUCH) {
                    tryAfflictionSelectionClient();
                }
                return;
            }
        }
        switch (context) {
            // submerge
            case SKILL_1_NORMAL -> {
                tryStartDiveClient();
            }
            // block disassembly
            case SKILL_1_CROUCH -> {
                tryDisassembleBlockClient();
            }
            // self dive (Guard + Z)
            case SKILL_1_GUARD -> {
                tryStartSelfDiveClient();
            }
            case SKILL_1_CROUCH_GUARD -> {
                tryStartSelfDiveClient();
            }
            // kick storage
            case SKILL_2_NORMAL -> {
                tryPlantKickTrap();
            }
            // trap release (deletes/activates traps, depends on mode)
            case SKILL_2_CROUCH -> {
                tryManualTrapRelease();
            }
            // toggle between auto and manual
            case SKILL_2_GUARD -> {
                tryToggleTrapMode();
            }
            case SKILL_2_CROUCH_GUARD -> {
                tryToggleTrapMode();
            }
            // dash, need to figure out how other moves will work.
            case SKILL_3_NORMAL -> {
                tryToDashClient();
            }
            // diver zip
            case SKILL_3_CROUCH -> {
                tryDiverZip();
            }
            // ground dive
            case SKILL_4_NORMAL -> {
                tryGroundDive();
            }
            // workbench
            case SKILL_4_GUARD -> {
                tryWorkbenchSelectionClient();
            }
            case SKILL_4_CROUCH_GUARD-> {
                tryWorkbenchSelectionClient();
            }
            // limb climbing move
            case SKILL_4_CROUCH -> {
                tryLimbClimb();
            }
        }
    }

    // for activating all the moves
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        // does the submerge
        if (move == DIVER_SUBMERGE_START) {
            return startDiveWindupServer();
        }
        // toggles damage redirection
        else if (move == TOGGLE_DAMAGE_REDIRECT) {
            return toggleDamageRedirectServer();
        }
        // recalls stand
        else if (move == DIVER_EMERGE) {
            return emergeServer();
        }
        // does the store kick thing
        else if (move == STORE_KICK_TRAP) {
            return startKickTrapWindup();
        }
        // releases/deletes traps
        else if (move == MANUAL_TRAP_RELEASE) {
            if (this.isAutoRelease) {
                return clearKickTraps();
            } else {
                return manualReleaseTraps();
            }
        }
        // toggles traps
        else if (move == TOGGLE_TRAP_MODE) {
            return toggleTrapMode();
        }
        // does the limb scaffold move
        else if (move == LIMB_SCAFFOLD) {
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
        return super.setPowerOther(move, lastMove);
    }

    @Override
    public void updatePowerInt(byte activePower, int data) {
        if (activePower == DIVER_SUBMERGE_START) {
            this.submergedTargetId = data;
            if (data == -1) {
                this.submergedTarget = null;
            } else {
                this.submergedTarget = this.self.level().getEntity(data);
            }
        } else if (activePower == DIVER_EMERGE) {
            this.submergedTargetId = -1;
            this.submergedTarget = null;
            this.hasDiverLegs = false;
        } else if (activePower == LIMB_RECALL) {
            this.activeLimbs.clear();
            this.currentLimbIndex = 0;
        }
        super.updatePowerInt(activePower, data);
    }

    // check if allowed to use a move.
    @Override
    public boolean tryPower(int move, boolean forced) {
        if (!this.getSelf().level().isClientSide && this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
            this.stopSoundsIfNearby(IMPALE_NOISE, 100, true);
        }
        switch (move) {
            case DIVER_ZIP -> {
                    activateZip();
            }
            case PowerIndex.POWER_3 -> {
                wallLatch();
            }
            case DIVER_SELF_SUBMERGE -> {
                startSelfDiveServer();
            }
        }
        return super.tryPower(move, forced);
    }

    @Override
    public boolean tryBlockPosPower(int move, boolean forced, BlockPos blockPos) {
        if (move == OPEN_CHEST) {
            openChest(blockPos);
        } else if (move == DISASSEMBLE_BLOCK) {
            disassembleBlock(blockPos);
        }
        return super.tryBlockPosPower(move, forced, blockPos);
    }

    @Override
    public boolean tryPosPower(int move, boolean forced, Vec3 pos) {
        if (move == GROUND_GET_ITEMS) {
            if (!this.self.level().isClientSide) {
                StandEntity stand = getStandEntity(this.self);
                if (stand != null) {
                    // Teleport the server stand to where the client actually is!
                    stand.setPos(pos.x, pos.y, pos.z);
                }
                diveGetItems();
            }
            return true;
        } else if (move == GROUND_DIVE_BARRAGE) {
            StandEntity stand = getStandEntity(this.self);
            if (stand != null) {
                stand.setPos(pos.x, pos.y, pos.z);
            }
            this.barrageTicksLeft = MAX_GROUND_BARRAGE_TICKS;
            this.setActivePower(GROUND_DIVE_BARRAGE);
            return true;
        }
        return super.tryPosPower(move, forced, pos);
    }

    public void tryToDashClient() {
        if (canLatchOntoWall() && canWallZipConfig()) {
            if(canExecuteMoveWithLevel(getDiverZipLevel())) {
                doWallLatchClient();
            }
        } else if (!inZipMode()) {
            dash();
        }
    }

    /**
     * (non-Javadoc)
     * tryLimbClimb is the client side activation for the limb move.
     */
    private void tryLimbClimb() {
        if (canExecuteMoveWithLevel(getDiverLimbLevel())) {
            if (this.self.level().isClientSide()) {
                if (!this.onCooldown(PowerIndex.SKILL_4_SNEAK)) {
                    // literally just to prevent the move from being spammed
                    this.setCooldown(PowerIndex.SKILL_4_SNEAK, 10);
                    ((StandUser) this.getSelf()).roundabout$tryPower(LIMB_SCAFFOLD, true);
                    tryPowerPacket(LIMB_SCAFFOLD);
                }
            }
        }
    }

    /**
     * (non-Javadoc)
     * tryIntPower
     *
     * @see net.hydra.jojomod.stand.powers.presets.NewDashPreset#tryIntPower(int,
     * boolean, int)
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
        }
        else if (move == ACCESS_AFFLICTIONS) {
            return openAfflictions(chargeTime);
        }
        else if (move == PowerIndex.SNEAK_ATTACK) {
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
        if (inZipMode()) {
            if (isHoldingSneak()) {
                basis *= 3.32F; // slow crawl when holding Shift (1.295 base crawl speed * 3.32 modifier = ~4.3
                // m/s)
            } else {
                basis *= 5.503F; // 1.295 base crawl speed * 5.303 modifier = ~7.127 m/s
            }
            if (this.self.level().isClientSide() && ClientUtil.isMovingDiagonally()) {
                basis *= 0.7071F;
            }
        }
        return super.inputSpeedModifiers(basis);
    }

    //used with attackTimeDuring to activate moves when it hits 0
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
        if (this.getActivePower() == DIVER_SUBMERGE_START) {
            completeDiveServer();
        }
        if (this.getActivePower() == TRANSFER) {
            triggerTransfer();
        }
        if (this.getActivePower() == STORE_KICK_TRAP) {
            completeKickTrap();
        }
        super.updateUniqueMoves();
    }

    @Override
    public boolean interceptIncomingHarm(DamageSource source, float amount) {
        // let transfer be cancellable
        if (this.getActivePower() == TRANSFER) {
            cancelTransfer();
        }
        // let store kick be cancellable
        if (this.getActivePower() == STORE_KICK_TRAP) {
            cancelStore();
        }
        return super.interceptIncomingHarm(source, amount);
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
                    SoundSource.PLAYERS, 0.9F, pitch);
        }
    }

    // wall latch start

    public void doWallLatchClient() {
        if (!this.onCooldown(PowerIndex.SKILL_3)) {
            if (canLatchOntoWall() && canWallZipConfig()) {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3, true);
            }
        }
    }

    public void wallLatch() {
        if(!(self.level().isClientSide())) {
            this.setCooldown(PowerIndex.SKILL_3, 10);
        }
        toggleZip(true);
        Direction gd = RotationUtil.getRealFacingDirection2(this.self);
        setHeelDirection(gd);
        ((IGravityEntity) this.self).roundabout$setGravityDirection(gd);
        this.justFlippedTicks = 7;
        this.mercyTicks = 10;
        if (self.level().isClientSide()) {
            C2SPacketUtil.intToServerPacket(
                    PacketDataIndex.INT_GRAVITY_FLIP_2, MainUtil.getIntFromDirection(gd)
            );
        }
    }

    // wall latch end

    // Workbench code start

    /**
     * Opens the selection menu for the player to choose what
     * workbench they want to access.
     */
    private void tryWorkbenchSelectionClient() {
        if(canExecuteMoveWithLevel(getWorkbenchLevel()) && !this.onCooldown(PowerIndex.SKILL_4_GUARD)) {
            ClientUtil.openWorkbenchSelect();
        }
    }

    /**
     * This functions runs the method based on the workbench found in
     * workbenchID. The next 5 functions that follow all open the corresponding
     * workbenches.
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
                (containerId, inventory, player) -> new DiverDownLoomMenu(containerId, inventory,
                        ContainerLevelAccess.create(serverPlayer.level(), serverPlayer.blockPosition())),
                Component.translatable("container.loom")));
    }

    public void openStonecutter(ServerPlayer serverPlayer) {
        serverPlayer.openMenu(new SimpleMenuProvider(
                (containerId, inventory, player) -> new DiverDownStonecutterMenu(containerId, inventory,
                        ContainerLevelAccess.create(serverPlayer.level(), serverPlayer.blockPosition())),
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

    // these variables list all the limbs so it can cycle through the 4
    public final List<BlockPos> activeLimbs = new ArrayList<>();
    public int currentLimbIndex = 0;

    /**
     * Checks to see if limbs are deployed to ensure moves can't be used while
     * active.
     */
    private boolean hasLimbsDeployed() {
        if (this.self.level() != null && !(this.self.level().isClientSide())) {
            this.activeLimbs.removeIf(pos -> !this.self.level().getBlockState(pos).is(ModBlocks.DIVER_LIMB));
        }
        return !this.activeLimbs.isEmpty();
    }

    //gets the next limb, to prevent scenarios where you need to cycle through 3 limbs just to use 1 that got destroyed
    private int getNextAvailableLimbIndex(Level level) {
        boolean[] used = new boolean[4];

        for (BlockPos pos : this.activeLimbs) {
            if (level.getBlockEntity(pos) instanceof DiverLimbBlockEntity be) {
                if (be.limbIndex >= 0 && be.limbIndex < 4) {
                    used[be.limbIndex] = true;
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            if (!used[i]) {
                return i;
            }
        }
        return 0;
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
                    // increases limb index so the next limb will be the next in queue (read
                    // DiverLimbBlockEntity to see the cycle order)
                    int nextLimb = getNextAvailableLimbIndex(level);
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
                                        SoundSource.PLAYERS, 0.7F, 1);
                                // finally despawns the stand once it's inside the block
                                stand.forceDespawn(true);
                                addEXP(1);
                            }
                        }
                        // subsequent usages
                        else {
                            // play the animations + effects
                            // play a sound
                            playSoundIfPossible(self.level(), null, this.self.blockPosition(),
                                    ModSounds.DIVER_DOWN_DIVE2_EVENT,
                                    SoundSource.PLAYERS, 0.7F, (float) (0.9 + (Math.random() * 0.25)));
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
            // blocks must be within MAX_LIMB_DISTANCE block in all 3 directions
            if (dx <= MAX_LIMB_DISTANCE && dy <= MAX_LIMB_DISTANCE && dz <= MAX_LIMB_DISTANCE) {
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
        this.setCooldown(PowerIndex.SKILL_4_SNEAK, 60);
        return true;
    }

    /**
     * This is a function that tries to recall all limb scaffolds
     */
    private void tryRecallLimbs() {
        ((StandUser) this.getSelf()).roundabout$tryPower(LIMB_RECALL, true);
        tryPowerPacket(LIMB_RECALL);
    }

    // Limb scaffold climb move end

    // Ground dive move here

    private void tryGroundDive() {
        if (canExecuteMoveWithLevel(getGroundDiveLevel()) && !this.onCooldown(PowerIndex.SKILL_4)) {
            if (this.self.level().isClientSide() && GravityAPI.getGravityDirection(this.self) == Direction.DOWN) {
                StandEntity stand = getStandEntity(this.self);
                if (stand != null && stand.isAlive()) {
                    DiverDownControlsClient.enter(stand);
                    setPiloting(stand.getId());
                    tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT, stand.getId());
                    // the 0.05 is there to fix the pilot enter bug, so it can enter pilot inside
                    // walls
                    stand.setPos(stand.getX(), stand.getY() + 0.05, stand.getZ());
                }
            }
        }
    }

    public void exitGroundDive() {
        if (this.self.level().isClientSide()) {
            DiverDownControlsClient.exit();
            this.wasPilotingClient = false;
            this.detectedOres = Collections.emptyList();
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
                //put animation here
                playSoundIfPossible(self.level(), null, getStandEntity(this.self).blockPosition(),
                        ModSounds.DIVER_DOWN_GROUND_DIVE_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
            } else {
                // move returns camera as a failsafe.
                this.diveTicksLeft = 0;
                if (this.self.level().isClientSide()) {
                    DiverDownControlsClient.exit();
                }
            }
        }
    }

    @Override
    public int getMaxPilotRange() {
        // (this is in blocks)
        return 13;
    }

    @Override
    public void synchToCamera() {
        if (isPiloting()) {
            LivingEntity stand = getPilotingStand();
        }
    }

    @Override
    public void pilotStandControls(KeyboardPilotInput kpi, LivingEntity entity) {
        if (entity instanceof DiverDownEntity diver) {
            // locks movement during barrage
            if (this.getActivePower() == GROUND_DIVE_BARRAGE) {
                diver.setDeltaMovement(0, diver.getDeltaMovement().y, 0);
                return;
            }
            // autostep can be found in DiverDownEntity
            // horizontal movement
            float speed = 0.4F;
            float yawRad = diver.getYRot() * ((float) Math.PI / 180F);
            double forward = kpi.forwardImpulse;
            double strafe = kpi.leftImpulse;
            if (forward != 0.0 && strafe != 0.0) {
                double length = Math.hypot(forward, strafe);
                forward /= length;
                strafe /= length;
            }
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
        // check for if entity is with the 2 block radius.
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
                        closestPos = pos.immutable();
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
            if (this.self.level().isClientSide()) {
                BlockState state = this.self.level().getBlockState(chestPos);
                this.isBarrel = state.getBlock() instanceof BarrelBlock;
                this.isOpeningRemoteChest = true; // Wait for screen to open
            }
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
        StandEntity stand = getStandEntity(this.self);
        if (stand != null && !isPiloting()) {
            setPiloting(stand.getId());
        }
        this.isOpeningRemoteChest = true;

        BlockState state = this.self.level().getBlockState(chestPos);
        MenuProvider menuProvider = state.getMenuProvider(this.self.level(), chestPos);
        if (menuProvider != null) {
            serverPlayer.openMenu(menuProvider);
        }
        return true;
    }

    // runs get items code on client and server
    private void tryDiveGetItems() {
        Vec3 pos = getStandEntity(this.self).position();
        tryPosPower(GROUND_GET_ITEMS, true, pos);
        tryPosPowerPacket(GROUND_GET_ITEMS, pos);
    }

    // gets the items when the move is pressed
    private boolean diveGetItems() {
        if (this.self.level().isClientSide || !(this.self instanceof Player player)) {
            return false;
        }
        StandEntity stand = getStandEntity(this.self);
        if (stand == null)
            return false;
        List<Entity> allTargets = getEntitiesBox();
        // then check for all the entities that are items
        if (!allTargets.isEmpty()) {
            // for boss filtering function, taken from walking heart. It took me a while to
            // figure out that FE meant "Filtered Entities," so im leaving this variable as
            // the whole thing for future reference
            // repurposed here to filter items
            for (Entity target : allTargets) {
                // the check below was taken from star platinum inhale, which is a much better
                // method to copy as that drags in ALL items, not just one
                // unlike phase grab
                boolean collected = false;
                if (target instanceof ItemEntity itemEntity) {
                    // fix is here in case, for some reason, you decide to drop an item only to immediately grab it with X
                    itemEntity.setPickUpDelay(0);
                    itemEntity.playerTouch(player);
                    collected = true;
                } else if (target instanceof ExperienceOrb xpOrb) {
                    xpOrb.playerTouch(player);
                    collected = true;
                }
                if (collected) {
                    playSoundIfPossible(this.self.level(), null, stand.blockPosition(),
                            SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.85F, 1.0F);
                    setCooldown(PowerIndex.SKILL_4,100);
                    exitGroundDive();
                }
            }
        }
        return true;
    }

    /*
     * Checks for clicks in pilot. if there is, trigger the dive barrage.
     * this can't be put in powerActivate because there's no check for clicks.
     */
    @Override
    public void pilotInputAttack() {
        // don't let the user barrage again if there's already an active one
        if (this.getActivePower() == GROUND_DIVE_BARRAGE) {
            return;
        }
        // test statement, comment out when done
        /*
         * if (this.self instanceof Player player) {
         * player.sendSystemMessage(Component.literal("it's chesting time"));
         * }
         */
        StandEntity stand = getStandEntity(this.self);
        if (stand != null) {
            Vec3 pos = stand.position();
            tryPosPower(GROUND_DIVE_BARRAGE, true, pos);
            tryPosPowerPacket(GROUND_DIVE_BARRAGE, pos);
            setCooldown(PowerIndex.SKILL_4,360);
        }
    }

    /*
     * helper method for ground dive and item grab that gets all entities in a
     * bounding box
     * if the hitbox ever becomes bigger, make sure to make this bigger too.
     */
    private List<Entity> getEntitiesBox() {
        StandEntity stand = getStandEntity(this.self);
        AABB box = stand.getBoundingBox().inflate(2.0, 2.0, 2.0);
        List<Entity> allTargets = this.self.level().getEntities(stand, box);
        // filter entities even more so it only counts those inside the ACTUAL hitbox
        List<Entity> filteredTargets = new ArrayList<>();
        for (Entity target : allTargets) {
            if (target != null && target.isAlive() && target != this.self && target != stand) {
                if (isInDiveHitbox(target.getX(), target.getY(), target.getZ())) {
                    filteredTargets.add(target);
                }
            }
        }
        return filteredTargets;
    }

    //method for looking for ores beneath diver down
    public void scoutForOresBeneathClient() {
        if (!this.self.level().isClientSide()) return;
        StandEntity stand = getStandEntity(this.self);
        BlockPos centerPos = (stand != null) ? stand.blockPosition() : this.self.blockPosition();
        List<BlockPos> found = new ArrayList<>();
        int hRange = 2;    // Horizontal radius (4 blocks each direction)
        int depth = 6;    // Depth beneath the stand (add 2 to start from the stand's feet)
        int maxOres = 32;   // Ore cap to prevent visual clutter
        //start y from -1 to start from feet
        for (int y = -1; y >= -depth; y--) {
            for (int x = -hRange; x <= hRange; x++) {
                for (int z = -hRange; z <= hRange; z++) {
                    BlockPos pos = centerPos.offset(x, y, z);
                    BlockState blk = this.self.level().getBlockState(pos);
                    if (MainUtil.confirmIsOre(blk)) {
                        found.add(pos);
                        if (found.size() >= maxOres) {
                            this.detectedOres = found;
                            return;
                        }
                    }
                }
            }
        }
        this.detectedOres = found;
    }

    public void toggleOreDetection() {
        this.oreDetectionEnabled = !this.oreDetectionEnabled;
        if (!this.oreDetectionEnabled) {
            this.detectedOres = Collections.emptyList();
        }
    }

    // walking heart autostep works on the player, not on the stand. i can't copy
    // that for this, unfortunately.

    // note to self: all of whitesnake's controls can be found in
    // WhitesnakeControlClient
    // i just need to use the camera third person thing though, so I'll just
    // transfer that here instead of making a new file.

    // Ground dive move end

    // heel plant 2.0 start

    public void tryDiverZip() {
        if(canExecuteMoveWithLevel(getDiverZipLevel()) && !this.onCooldown(PowerIndex.SKILL_3)) {
            if (!self.isInWater()) {
                if (forceBlock())
                    return;
                ((StandUser) this.getSelf()).roundabout$tryPower(DIVER_ZIP, true);
                tryPowerPacket(DIVER_ZIP);
            }
        }
    }

    public void activateZip() {
        if (self.isInWater())
            return;
        boolean isAnchored = inZipMode();
        if (isAnchored) {
            if (!this.self.level().isClientSide()) {
                toggleZip(false);
            }
        } else {
            if (self.onGround()) {
                this.setCooldown(PowerIndex.SKILL_3, 10);
                if (!this.self.level().isClientSide()) {
                    setHeelDirection(((IGravityEntity) this.self).roundabout$getGravityDirection());
                    toggleZip(true);
                }
            }
        }
    }

    public void toggleZip(boolean toggle) {
        if (!toggle) {
            ((StandUser) this.self).rdbt$SetCrawlTicks(0);
            this.self.setSwimming(false);
            setCooldown(PowerIndex.SKILL_3,180);
        } else {this.self.setSprinting(false);}
        if (!this.self.level().isClientSide()) {
            // test message, comment out when done
            /*
             * if (this.getSelf() instanceof ServerPlayer serverPlayer) {
             * serverPlayer.displayClientMessage(Component.literal("it's zipping time"),
             * false);
             * }
             */
            boolean getTog = getStandUserSelf().roundabout$getUniqueStandModeToggle();
            if (toggle != getTog) {
                if (toggle) {
                    // put sound here
                    playSoundIfPossible(self.level(), null, this.self.blockPosition(),
                            ModSounds.DIVER_DOWN_TRANSFER_EVENT,
                            SoundSource.PLAYERS, 0.9F, 0.9F);
                } else {
                    Direction gf = ((IGravityEntity) self).roundabout$getGravityDirection();
                    if (gf != getIntendedDirection()) {
                        Vec3 vec = new Vec3(0, 0.3f, 0);
                        vec = RotationUtil.vecPlayerToWorld(vec, gf);
                        self.teleportTo(
                                self.getX() + vec.x,
                                self.getY() + vec.y,
                                self.getZ() + vec.z);
                    }
                    // put cooldown here
                    // put sound here
                    playSoundIfPossible(self.level(), null, this.self.blockPosition(),
                            ModSounds.DIVER_DOWN_DIVE_EVENT,
                            SoundSource.PLAYERS, 0.5F, 1.3F);
                }
            }
        }
        getStandUserSelf().roundabout$setUniqueStandModeToggle(toggle);
    }

    @Override
    public void serverQueried2() {
        if (self instanceof ServerPlayer) {
            toggleZip(false);
        }
    }

    public void setHeelDirection(Direction dir) {
        feetDirection = dir;
    }

    public boolean forceBlock() {
        if (!MainUtil.isBlockWalkableSimplified(self.level().getBlockState(self.getOnPos())))
            return true;
        return false;
    }

    public boolean inZipMode() {
        return getStandUserSelf().roundabout$getUniqueStandModeToggle();
    }

    public void onActuallyHurt(DamageSource $$0, float $$1) {
        if ($$0.getEntity() != null && !$$0.is(DamageTypes.THORNS)) {
            if (!$$0.is(ModDamageTypes.KNIFE) && !$$0.is(ModDamageTypes.BULLET)) {
                if (inZipMode()) {
                    toggleZip(false);
                }
            }
        }
        //custom death message lol, needs to track the damage type, then send the custom death message if it's lethal
        if (this.isTransferringDamage && this.submergedTarget != null) {
            if (this.self.getHealth() - $$1 <= 0.0F) {
                // if the damage is lethal, time for the custom message lol
                DamageSource customSource = ModDamageTypes.of(
                        this.self.level(),
                        ModDamageTypes.DIVER_REDIRECTION,
                        this.submergedTarget
                );
                this.self.getCombatTracker().recordDamage(customSource, 0);
            }
        }
    }

    public Direction getIntendedDirection() {
        Direction rightAxis = Direction.DOWN;
        MobEffectInstance mi = self.getEffect(ModEffects.GRAVITY_FLIP);
        if (mi != null) {
            if (mi.getAmplifier() == 0) {
                rightAxis = Direction.NORTH;
            }
            if (mi.getAmplifier() == 1) {
                rightAxis = Direction.SOUTH;
            }
            if (mi.getAmplifier() == 2) {
                rightAxis = Direction.EAST;
            }
            if (mi.getAmplifier() == 3) {
                rightAxis = Direction.WEST;
            }
            if (mi.getAmplifier() == 4) {
                rightAxis = Direction.UP;
            }
        }
        return rightAxis;
    }

    @Override
    public boolean cancelSprint() {
        return super.cancelSprint() || inZipMode();
    }

    @Override
    public boolean cancelSprintParticles() {
        return super.cancelSprintParticles() || inZipMode();
    }

    @Override
    public float getStepHeightAddon() {
        if (inZipMode()) {
            if (canWallZipConfig())
                return 0.4F;
            else
                return 2.0F;
        } else if (!(self instanceof Player)) {
            return 3.0F;
        }
        return 0;
    }

    /**
     * Always enable auto corner-cutting while in zip mode if wall walking is
     * enabled in config
     */
    public boolean canCutCorners() {
        return false;
    }

    public boolean tryCut(Vec3 cutPos) {
        BlockPos pos1 = BlockPos.containing(cutPos);
        BlockState bs = this.self.level().getBlockState(pos1);
        return MainUtil.isBlockWalkable(bs);
    }

    public boolean tryCutEast(Vec3 mpos) {
        return (tryCut(mpos.add(new Vec3(0.1, 0, 0)))
                || tryCut(mpos.add(new Vec3(self.getBbWidth() * 1.1f, 0, 0)))
                || tryCut(mpos.add(new Vec3(self.getBbWidth() * 1.4f, 0, 0)))
                || tryCut(mpos.add(new Vec3(self.getBbWidth() * 1.6f, 0, 0)))
                || tryCut(mpos.add(new Vec3(self.getBbWidth() * 2.0f, 0, 0)))
                || tryCut(mpos.add(new Vec3(self.getBbWidth() * 2.5f, 0, 0))));
    }

    public boolean tryCutWest(Vec3 mpos) {
        return (tryCut(mpos.add(new Vec3(-0.1, 0, 0)))
                || tryCut(mpos.add(new Vec3(-self.getBbWidth() * 1.1f, 0, 0)))
                || tryCut(mpos.add(new Vec3(-self.getBbWidth() * 1.4f, 0, 0)))
                || tryCut(mpos.add(new Vec3(-self.getBbWidth() * 1.6f, 0, 0)))
                || tryCut(mpos.add(new Vec3(-self.getBbWidth() * 2.0f, 0, 0)))
                || tryCut(mpos.add(new Vec3(-self.getBbWidth() * 2.5f, 0, 0))));
    }

    public boolean tryCutNorth(Vec3 mpos) {
        return (tryCut(mpos.add(new Vec3(0, 0, -0.1)))
                || tryCut(mpos.add(new Vec3(0, 0, -self.getBbWidth() * 1.1f)))
                || tryCut(mpos.add(new Vec3(0, 0, -self.getBbWidth() * 1.4f)))
                || tryCut(mpos.add(new Vec3(0, 0, -self.getBbWidth() * 1.6f)))
                || tryCut(mpos.add(new Vec3(0, 0, -self.getBbWidth() * 2.0f)))
                || tryCut(mpos.add(new Vec3(0, 0, -self.getBbWidth() * 2.5f))));
    }

    public boolean tryCutSouth(Vec3 mpos) {
        return (tryCut(mpos.add(new Vec3(0, 0, 0.1)))
                || tryCut(mpos.add(new Vec3(0, 0, self.getBbWidth() * 1.1f)))
                || tryCut(mpos.add(new Vec3(0, 0, self.getBbWidth() * 1.4f)))
                || tryCut(mpos.add(new Vec3(0, 0, self.getBbWidth() * 1.6f)))
                || tryCut(mpos.add(new Vec3(self.getBbWidth() * 2.0f, 0, 0)))
                || tryCut(mpos.add(new Vec3(self.getBbWidth() * 2.5f, 0, 0)))
                || tryCut(mpos.add(new Vec3(0, 0, self.getBbWidth() * 2.0f)))
                || tryCut(mpos.add(new Vec3(0, 0, self.getBbWidth() * 2.5f))));
    }

    public boolean tryCutUp(Vec3 mpos) {
        return (tryCut(mpos.add(new Vec3(0, 0.1, 0)))
                || tryCut(mpos.add(new Vec3(0, self.getBbWidth() * 1.1f, 0)))
                || tryCut(mpos.add(new Vec3(0, self.getBbWidth() * 1.4f, 0)))
                || tryCut(mpos.add(new Vec3(0, self.getBbWidth() * 1.6f, 0)))
                || tryCut(mpos.add(new Vec3(0, self.getBbWidth() * 2.0f, 0)))
                || tryCut(mpos.add(new Vec3(0, self.getBbWidth() * 2.5f, 0))));
    }

    public boolean tryCutDown(Vec3 mpos) {
        return (tryCut(mpos.add(new Vec3(0, -0.1, 0)))
                || tryCut(mpos.add(new Vec3(0, -self.getBbWidth() * 1.1f, 0)))
                || tryCut(mpos.add(new Vec3(0, -self.getBbWidth() * 1.4f, 0)))
                || tryCut(mpos.add(new Vec3(0, -self.getBbWidth() * 1.6f, 0)))
                || tryCut(mpos.add(new Vec3(0, -self.getBbWidth() * 2.0f, 0)))
                || tryCut(mpos.add(new Vec3(0, -self.getBbWidth() * 2.5f, 0))));
    }

    record DirDist(Direction dir, float dist) {
    }

    public boolean canCut() {
        Vec3 mpos = this.self.getPosition(1F);
        float northTest = (float) (lastGroundPosition.z - mpos.z);
        float southTest = -1 * (float) (lastGroundPosition.z - mpos.z);
        float eastTest = -1 * (float) (lastGroundPosition.x - mpos.x);
        float westTest = (float) (lastGroundPosition.x - mpos.x);
        float upTest = -1 * (float) (lastGroundPosition.y - mpos.y);
        float downTest = (float) (lastGroundPosition.y - mpos.y);
        List<DirDist> tests = List.of(
                new DirDist(Direction.NORTH, northTest),
                new DirDist(Direction.SOUTH, southTest),
                new DirDist(Direction.EAST, eastTest),
                new DirDist(Direction.WEST, westTest),
                new DirDist(Direction.UP, upTest),
                new DirDist(Direction.DOWN, downTest));
        List<DirDist> ordered = new java.util.ArrayList<>(tests);
        ordered.sort(java.util.Comparator.comparing(DirDist::dist));
        for (DirDist test : ordered) {
            boolean success = switch (test.dir()) {
                case EAST -> tryCutEast(mpos);
                case WEST -> tryCutWest(mpos);
                case NORTH -> tryCutNorth(mpos);
                case SOUTH -> tryCutSouth(mpos);
                case UP -> tryCutUp(mpos);
                case DOWN -> tryCutDown(mpos);
            };
            if (success) {
                cutDirection = test.dir();
                // Opposing wall sanity checks
                if (cutDirection == Direction.EAST && tryCutWest(mpos))
                    return false;
                if (cutDirection == Direction.WEST && tryCutEast(mpos))
                    return false;
                if (cutDirection == Direction.NORTH && tryCutSouth(mpos))
                    return false;
                if (cutDirection == Direction.SOUTH && tryCutNorth(mpos))
                    return false;
                if (cutDirection == Direction.UP && tryCutDown(mpos))
                    return false;
                if (cutDirection == Direction.DOWN && tryCutUp(mpos))
                    return false;
                return true;
            }
        }
        return false;
    }

    // heel plant 2.0 end

    // kick storage start

    private void cancelStore () {
        this.setPowerNone();
    }

    public boolean startKickTrapWindup() {
        if (this.self.level().isClientSide()) {
            return true;
        }
        Vec3 eyePos = this.self.getEyePosition(0);
        Vec3 lookVec = this.self.getViewVector(0);
        Vec3 reachVec = eyePos.add(lookVec.scale(TRAP_RANGE));

        BlockHitResult blockHit = this.self.level().clip(
                new ClipContext(eyePos, reachVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.self));

        if (blockHit.getType() != HitResult.Type.BLOCK) {
            return false;
        }

        // startup timer
        this.setActivePower(STORE_KICK_TRAP);
        this.setAttackTimeDuring(0);

        // animations and sounds and stuff
        return true;
    }

    public void completeKickTrap() {
        if (this.getAttackTimeDuring() >= KICK_TRAP_WINDUP_MAX) {
            plantKickTrap();
            this.setPowerNone();
        }
    }

    //copied from sendParticlesIfPossible, but added a blacklist.
    private void sendKickTrapParticles(double px, double py, double pz) {
        Level level = this.self.level();
        if (level instanceof ServerLevel sl && level.getServer() != null) {
            if (PowerTypes.isErasingTime(this.self)) {
                return;
            }

            double range = 64.0D;
            double rangeSq = range * range;

            for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
                if (player.level() != sl) {
                    continue;
                }
                if (PowerTypes.isInADifferentExistenceNoTE(this.self, player)) {
                    continue;
                }
                if (player.distanceToSqr(px, py, pz) > rangeSq) {
                    continue;
                }
                boolean isUser = player.equals(this.self);

                // blacklist of interactions
                boolean canOtherSee = !player.isSprinting()
                        && !player.isUsingItem()
                        && !player.isSwimming()
                        && !player.isFallFlying()
                        && !player.isPassenger();

                if (isUser || canOtherSee) {
                    sl.sendParticles(
                            player,
                            //replace this with whatever new particles i get for the thing
                            ModParticles.ENERGY_DISTORTION,
                            false,
                            px, py, pz,
                            1,
                            0.02, 0.02, 0.02,
                            0.0
                    );
                }
            }
        }
    }

    public void tryPlantKickTrap() {
        if(canExecuteMoveWithLevel(getKickStorageLevel()) && !this.onCooldown(PowerIndex.SKILL_2)) {
            if (this.canAttack() && !this.areStandMovesDisabled()) {
                this.tryPower(STORE_KICK_TRAP, true);
                tryPowerPacket(STORE_KICK_TRAP);
            }
        }
    }

    public boolean plantKickTrap() {
        if (this.self.level().isClientSide()) {
            return true;
        }

        // find ground block
        Vec3 eyePos = this.self.getEyePosition(0);
        Vec3 lookVec = this.self.getViewVector(0);
        Vec3 reachVec = eyePos.add(lookVec.scale(TRAP_RANGE));

        BlockHitResult blockHit = this.self.level().clip(
                new ClipContext(eyePos, reachVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.self));

        // do nothing if nothing is found
        if (blockHit.getType() != HitResult.Type.BLOCK) {
            return false;
        }

        BlockPos hitPos = blockHit.getBlockPos();
        Direction face = blockHit.getDirection();

        if (!this.storedKickTraps.containsKey(hitPos)) {
            while (this.storedKickTraps.size() >= MAX_NUMBER_OF_TRAPS) {
                BlockPos oldest = this.storedKickTraps.keySet().iterator().next();
                this.storedKickTraps.remove(oldest);
            }
        }

        // mirror the angle at which the player looked at the block
        Vec3 normal = new Vec3(face.getStepX(), face.getStepY(), face.getStepZ());
        double dot = lookVec.dot(normal);
        Vec3 reflection = lookVec.subtract(normal.scale(2.0 * dot)).normalize();

        // store the trap
        this.storedKickTraps.put(hitPos, new KickTrap(MAX_TRAP_DURATION, face, reflection));
        setCooldown(PowerIndex.SKILL_2,100);

        // animation here
        // Sounds & ground impact particles here
        playSoundIfPossible(self.level(), null, this.self.blockPosition(),
                ModSounds.DIVER_DOWN_TRANSFER_EVENT,
                SoundSource.PLAYERS, 0.7F, 1);

        // cooldown here

        return true;
    }

    private List<LivingEntity> detectTrapTrigger(BlockPos pos, Direction face) {
        // Check area directly on top of the trapped block
        BlockPos triggerPos = pos.relative(face);
        AABB triggerBox = new AABB(triggerPos).inflate(-0.05);
        List<LivingEntity> victims = new ArrayList<>();
        for (LivingEntity target : this.self.level().getEntitiesOfClass(LivingEntity.class, triggerBox)) {
            if (target.equals(this.self)
                    || target instanceof StandEntity
                    || !target.isAlive()
                    || target.isDeadOrDying()) {
                continue;
            }
            victims.add(target);
        }
        return victims;
    }

    private void triggerKickTrap(List<LivingEntity> victims, BlockPos pos, KickTrap trap) {
        Direction face = trap.face;
        Level level = this.self.level();
        StandEntity stand = this.getStandEntity(this.self);
        BlockPos spawnPos = pos.relative(face);

        for (LivingEntity victim : victims) {
            if (victim instanceof TamableAnimal TA && TA.getOwner() != null && TA.getOwner().is(this.getSelf())) {
                continue;
            }
            DamageHandler.StandDamageEntity(victim, 8.0F, this.self);
            Vec3 dir = trap.launchVector != null ? trap.launchVector : new Vec3(face.getStepX(), face.getStepY(), face.getStepZ());

            double launchX = dir.x;
            double launchY = dir.y;
            double launchZ = dir.z;
            if (face == Direction.DOWN) {
                launchX *= 1.2D;
                launchZ *= 1.2D;
                launchY = Math.min(-0.8D, launchY * 1.5D);
            } else if (face == Direction.UP) {
                double horizScale = Math.max(1.1D,2D * (1.0D - Math.abs(launchY)));
                launchX *= horizScale;
                launchZ *= horizScale;
                launchY = Math.max(0.70D, launchY * 1D);
            } else {
                launchX *= 1.8D;
                launchZ *= 1.8D;
                launchY = Math.max(0.60D, launchY * 1.3D);
            }
            if (victim instanceof net.minecraft.world.entity.player.Player) {
                launchX *= 1.45D;
                launchZ *= 1.45D;
                // Give a tiny bit more air time to players so drag doesn't stop them
                launchY += 0.15D;
            }

            launchX = Math.min(2.9D, Math.max(-2.9D, launchX));
            launchZ = Math.min(2.9D, Math.max(-2.9D, launchZ));
            net.hydra.jojomod.util.MainUtil.takeLiteralUnresistableKnockbackWithY(victim, launchX, launchY, launchZ);

            victim.hurtMarked = true;
            victim.hasImpulse = true;

            // play effects sounds and stuff here
            playSoundIfPossible(self.level(), null, this.self.blockPosition(),
                    ModSounds.DIVER_DOWN_HIT_EVENT,
                    SoundSource.PLAYERS, 0.6F, 0.7F);
            playSoundIfPossible(self.level(), null, this.self.blockPosition(),
                    ModSounds.DIVER_DOWN_TRANSFER_EVENT,
                    SoundSource.PLAYERS, 0.8F, 1);
        }
    }

    private void tryToggleTrapMode() {
        this.isAutoRelease = !this.isAutoRelease;
        this.tryPower(TOGGLE_TRAP_MODE, true);
        tryPowerPacket(TOGGLE_TRAP_MODE);
    }

    private void tryManualTrapRelease() {
        this.tryPower(MANUAL_TRAP_RELEASE, true);
        tryPowerPacket(MANUAL_TRAP_RELEASE);
    }

    private boolean toggleTrapMode() {
        if (!this.self.level().isClientSide()) {
            this.isAutoRelease = !this.isAutoRelease;
        }
        return true;
    }

    private boolean manualReleaseTraps() {
        if (this.self.level().isClientSide() || this.storedKickTraps.isEmpty()) {
            return false;
        }
        boolean triggeredAny = false;
        Iterator<Map.Entry<BlockPos, KickTrap>> it = this.storedKickTraps.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<BlockPos, KickTrap> entry = it.next();
            BlockPos trapPos = entry.getKey();
            KickTrap trap = entry.getValue();
            // Detect any entity on the trap, used to instantly activate, even against
            // players
            List<LivingEntity> victims = detectTrapTrigger(trapPos, trap.face);
            if (!victims.isEmpty()) {
                triggerKickTrap(victims, trapPos, trap);
                it.remove(); // Consume trap
                triggeredAny = true;
            }
        }
        return triggeredAny;
    }

    private boolean clearKickTraps() {
        if (this.self.level().isClientSide() || this.storedKickTraps.isEmpty()) {
            return false;
        }

        // add particles here when deleting the traps, put it in a for loop like this
        /*
         * for (BlockPos pos : this.storedKickTraps.keySet()) {
         *
         * }
         */

        // Remove all active traps
        this.storedKickTraps.clear();

        // sound here
        playSoundIfPossible(self.level(), null, this.self.blockPosition(),
                ModSounds.DIVER_DOWN_TRANSFER_EVENT,
                SoundSource.PLAYERS, 0.5F, 1.5F);

        return true;
    }

    // kick storage end

    // dive start

    private void tryStartDiveClient() {
        if (!areStandMovesDisabled() && !isDiveActive() && !this.onCooldown(PowerIndex.SKILL_1)) {
            this.tryPower(DIVER_SUBMERGE_START, true);
            tryPowerPacket(DIVER_SUBMERGE_START);
        }
    }

    private void tryEmergeClient() {
        this.tryPower(DIVER_EMERGE, true);
        tryPowerPacket(DIVER_EMERGE);
    }

    //starts up the dive windup
    public boolean startDiveWindupServer() {
        if (this.self.level().isClientSide() || isDiveActive()) {
            return false;
        }
        // the windup
        this.setActivePower(DIVER_SUBMERGE_START);
        this.setAttackTimeDuring(0);
        // do animations and stuff here
        return true;
    }

    //actually does the dive
    public void completeDiveServer() {
        if(this.getAttackTimeDuring() >= DIVE_WINDUP_MAX) {
            // run the get target method to find a target
            Entity target = getTargetEntity(self, DIVE_REACH);
            if (target instanceof StandEntity stand && stand.getUser() != null) {
                target = stand.getUser();
            }
            if (!(target instanceof LivingEntity) || target instanceof StandEntity || target == null) {
                cancelDiveServer();
                this.setAttackTimeDuring(-5);
                return;
            }
            this.submergedTarget = target;
            if (this.submergedTarget == null || !this.submergedTarget.isAlive()) {
                cancelDiveServer(); //.25 second cooldown
                return;
            }
            this.setPowerNone();
            // Attach to target entity
            ((StandUser) this.submergedTarget).roundabout$SetDiverUser(this);
            // sync with the client for isDiveActive
            if (this.self instanceof Player player) {
                S2CPacketUtil.sendIntPowerDataPacket(player, DIVER_SUBMERGE_START, this.submergedTarget.getId());
            }
            // desummon stand
            if (hasStandEntity(this.self)) {
                StandEntity stand = this.getStandEntity(this.self);
                if (stand != null) {
                    stand.discard();
                }
            }
            addEXP(3);
            // sounds and particles here
            playSoundIfPossible(self.level(), null, this.self.blockPosition(),
                    ModSounds.DIVER_DOWN_DIVE_EVENT,
                    SoundSource.PLAYERS, 0.8F, 1F);
        }
    }

    public boolean emergeServer() {
        if (!isDiveActive())
            return false;
        removeDiverLegsFromTarget();
        if (this.submergedTarget != null) {
            ((StandUser) this.submergedTarget).roundabout$SetDiverUser(null);
            this.submergedTarget = null;
        }
        this.setPowerNone();
        //sync with the client
        if (this.self instanceof Player player) {
            S2CPacketUtil.sendIntPowerDataPacket(player, DIVER_EMERGE, -1);
        }
        // Resummon stand to user
        if (!this.self.level().isClientSide() && hasStandActive(this.self)) {
            ((StandUser) this.self).roundabout$summonStand(this.self.level(), true, false);
            playSoundIfPossible(self.level(), null, this.self.blockPosition(),
                    ModSounds.SUMMON_DIVER_DOWN_EVENT,
                    SoundSource.PLAYERS, 0.85F, 1);
        }
        setCooldown(PowerIndex.SKILL_1,300);
        return true;
    }

    public void cancelDiveServer() {
        removeDiverLegsFromTarget();
        this.submergedTarget = null;
        this.setPowerNone();
        // sync with client
        if (this.self instanceof Player player) {
            // S2C means server to client, for updating client/server desyncs
            S2CPacketUtil.sendIntPowerDataPacket(player, DIVER_EMERGE, -1);
        }
    }

    public boolean isDiveActive() {
        if (this.self.level().isClientSide()) {
            if (this.submergedTarget == null && this.submergedTargetId != -1) {
                this.submergedTarget = this.self.level().getEntity(this.submergedTargetId);
            }
            return this.submergedTargetId != -1;
        }
        return this.submergedTarget != null && this.submergedTarget.isAlive() && !this.submergedTarget.isRemoved();
    }

    /*
     * handles the damage transfer ability
     */
    public void onSubmergedTargetHurt(DamageSource source, float amount) {
        if (!this.damageRedirectionEnabled || this.isTransferringDamage || !isDiveActive() || isSelfDive())
            return;
        this.isTransferringDamage = true;
        try {
            float transferred = amount * 0.8F;
            // Deal 80% damage to the Diver Down user
            this.self.hurt(source, transferred);
            // sounds here if possible, to showcase the target absorbing damage
            // if user dies from the damage transfer, emerge immediately
            if (!this.self.isAlive()) {
                emergeServer();
            }
        } finally {
            this.isTransferringDamage = false;
        }
    }

    private void tryStartSelfDiveClient() {
        if (!areStandMovesDisabled() && !isDiveActive()) {
            this.tryPower(DIVER_SELF_SUBMERGE, true);
            tryPowerPacket(DIVER_SELF_SUBMERGE);
        }
    }

    public boolean startSelfDiveServer() {
        if (this.self.level().isClientSide() || isDiveActive()) {
            return false;
        }

        // Set target as self
        this.submergedTarget = this.self;
        ((StandUser) this.self).roundabout$SetDiverUser(this);

        // Desummon stand into player
        if (hasStandEntity(this.self)) {
            StandEntity stand = this.getStandEntity(this.self);
            if (stand != null) {
                stand.discard();
            }
        }

        // Sync with client so submergedTarget = self on client too
        if (this.self instanceof Player player) {
            S2CPacketUtil.sendIntPowerDataPacket(player, DIVER_SUBMERGE_START, this.self.getId());
        }

        //play sounds and animations here
        playSoundIfPossible(self.level(), null, this.self.blockPosition(),
                ModSounds.DIVER_DOWN_DIVE_EVENT,
                SoundSource.PLAYERS, 0.7F, 1);
        return true;
    }

    // simple check for self dive, will be used in afflictions wheel
    public boolean isSelfDive() {
        return isDiveActive() && this.submergedTarget == this.self;
    }

    private void tryToggleDamageRedirect() {
        if (!isDiveActive() || isSelfDive()) return;
        this.tryPower(TOGGLE_DAMAGE_REDIRECT, true);
        tryPowerPacket(TOGGLE_DAMAGE_REDIRECT);
    }

    public boolean toggleDamageRedirectServer() {
        this.damageRedirectionEnabled = !this.damageRedirectionEnabled;
        return true;
    }

    // dive end

    // dive afflictions start

    /**
     * Opens the selection menu for the player to choose what
     * affliction they want to access.
     */
    private void tryAfflictionSelectionClient() {
        if (!this.onCooldown(PowerIndex.GENERAL_1)) {
            ClientUtil.openAfflictionSelect();
        }
    }

    /**
     * This functions runs the method based on the affliction found in
     * afflictionID. The next 5 functions that follow all open the corresponding
     * affliction.
     */
    private boolean openAfflictions(int afflictionId) {
        if (submergedTarget == null) {
            return false;
        }

        switch (afflictionId) {
            case DISGUISE -> {
                if (this.submergedTarget instanceof StandUser su && su.roundabout$isDisguised()) {
                    su.roundabout$clearDisguise();
                    return true;
                }
                tryDisguiseClient();
                return true;
            }
            case EMBED_POTION -> {
                embedPotion();
                return true;
            }
            case DIVER_LEGS -> {
                diverLegs();
                return true;
            }
            case EFFECT_CURE -> {
                cureNegativeEffects();
                return true;
            }
            case TRANSFER -> {
                //comment out when tested
                //this.self.sendSystemMessage(Component.literal("it's transfering get update from affliciton screen time"));
                prepareTransfer();
                return true;
            }
            case BONE_BOMB -> {
                triggerBoneExplosion();
                return true;
            }
            case RIBCAGE_TRAP -> {
                applyRibcageTrap();
                return true;
            }
            case SPRING_LEGS -> {
                applySpringLegs();
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    // spring legs start

    public void applySpringLegs() {
        if (this.self.level().isClientSide()) return;
        if (!(this.submergedTarget instanceof LivingEntity host) || !host.isAlive()) return;

        this.springTarget = host;
        this.springLegsTicks = SPRING_LEGS_MAX_DURATION;

        // for disabling keys
        if (host instanceof StandUser su) {
            // just need this for the key disabling, nothing else
            su.roundabout$setSpringLegs(true);
        }
        emergeServer();
    }

    public void tickSpringLegs() {
        if (this.springTarget == null) return;

        LivingEntity host = this.springTarget;
        if (!host.isAlive() || host.isRemoved()) {
            clearSpringLegs();
            return;
        }

        boolean isPlayerOrBoss = host instanceof Player || MainUtil.isBossMob(host);
        if (isPlayerOrBoss) {
            if (--this.springLegsTicks <= 0) {
                clearSpringLegs();
                return;
            }
        } else {
            //extra 5 seconds for mobs
            if (--this.springLegsTicks <= -100) {
                clearSpringLegs();
                return;
            }
        }

        if(!host.onGround()) return;
        Level level = host.level();
        if (host.isSprinting()) {
            host.setSprinting(false);
        }
        double randomY = 0.7D + host.getRandom().nextDouble() * 0.3D;
        double randomX = (host.getRandom().nextDouble() - 0.5) * 2.5;
        double randomZ = (host.getRandom().nextDouble() - 0.5) * 2.5;
        host.setDeltaMovement(randomX, randomY, randomZ);
        host.hurtMarked = true;
        return;
    }

    public void clearSpringLegs() {
        if (this.springTarget instanceof StandUser su) {
            su.roundabout$setSpringLegs(false);
        }
        this.springTarget = null;
        this.springLegsTicks = 0;
    }

    // spring legs end

    // ribcage trap start

    public void applyRibcageTrap() {
        if (this.self.level().isClientSide()) return;
        if (!(this.submergedTarget instanceof LivingEntity host) || !host.isAlive()) return;

        this.ribcageTarget = host;
        this.ribcageTrapTicks = RIBCAGE_MAX_DURATION;

        // for disabling keys
        if (host instanceof StandUser su) {
            su.roundabout$setRibcageTrap(true);
        }

        // entity will start walking where the user is looking
        Vec3 look = host.getLookAngle();
        Vec3 horizontalLook = new Vec3(look.x, 0, look.z);
        if (horizontalLook.lengthSqr() < 1.0E-4) {
            this.ribcageWalkDirection = new Vec3(0, 0, 1);
        } else {
            this.ribcageWalkDirection = horizontalLook.normalize();
        }
        emergeServer();
    }

    public void tickRibcageTrap() {
        if (this.ribcageTarget == null) return;

        LivingEntity host = this.ribcageTarget;
        if (!host.isAlive() || host.isRemoved()) {
            clearRibcageTrap();
            return;
        }

        //only tick down if it's a player or a boss, otherwise the effect lasts forever
        boolean isPlayerOrBoss = host instanceof Player || MainUtil.isBossMob(host);
        if (isPlayerOrBoss) {
            if (--this.ribcageTrapTicks <= 0) {
                clearRibcageTrap();
                return;
            }
        }

        Level level = host.level();

        // forced forward walking for players and bosses
        if (host instanceof Player player) {
            if (player.isSprinting()) {
                player.setSprinting(false);
            }
            Vec3 forward = player.getForward();
            double walkSpeed = 0.4317D;
            player.setDeltaMovement(forward.x * walkSpeed, player.getDeltaMovement().y, forward.z * walkSpeed);
            player.hurtMarked = true;
            return;
        } else if (MainUtil.isBossMob(host)) {
            Vec3 forward = host.getForward();
            double walkSpeed = 0.4317D;
            host.setDeltaMovement(forward.x * walkSpeed, host.getDeltaMovement().y, forward.z * walkSpeed);
            host.hurtMarked = true;
            return;
        }

        // normal mobs lose all control
        if (host instanceof Mob mob) {
            // make them walk in a straight line. find the closest enemy
            List<LivingEntity> potentialVictims = level.getEntitiesOfClass(
                    LivingEntity.class,
                    host.getBoundingBox().inflate(HOSTILE_DETECTION_RANGE),
                    e -> e != host
                            && e != this.self
                            && !(e instanceof StandEntity)
                            && e.isAlive()
                            && !e.isSpectator()
                            && host.hasLineOfSight(e)
            );

            //find the closest enemy within detection range
            LivingEntity nearestTarget = null;
            double minDistanceSq = HOSTILE_DETECTION_RANGE * HOSTILE_DETECTION_RANGE;
            for (LivingEntity e : potentialVictims) {
                double distSq = host.distanceToSqr(e);
                if (distSq < minDistanceSq) {
                    minDistanceSq = distSq;
                    nearestTarget = e;
                }
            }

            if (nearestTarget != null) {
                // need this desiredSpeed thing because villagers zoom away when yuo use ribcage trap on them
                double desiredSpeed = 0.23D;
                double baseSpeed = mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
                double speedModifier = baseSpeed > 0 ? (desiredSpeed / baseSpeed) : 1.0D;

                mob.getNavigation().moveTo(nearestTarget, speedModifier);
                mob.getLookControl().setLookAt(nearestTarget, 30.0F, 30.0F);

                // collision check, stolen from d4c
                if (host.distanceTo(nearestTarget) < 1.5D) {
                    triggerRibcageSnap(host, nearestTarget);
                    return;
                }
            } else {
                // keep walking forward in straight line
                if (this.ribcageWalkDirection != null) {
                    double desiredSpeed = 0.23D;
                    double baseSpeed = mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
                    double speedModifier = baseSpeed > 0 ? (desiredSpeed / baseSpeed) : 1.0D;

                    Vec3 targetWalkPos = host.position().add(this.ribcageWalkDirection.scale(3.0D));
                    mob.getNavigation().moveTo(targetWalkPos.x, targetWalkPos.y, targetWalkPos.z, speedModifier);
                }
            }
        }
    }

    private void triggerRibcageSnap(LivingEntity host, LivingEntity victim) {
        Level level = host.level();
        Vec3 pos = host.position();

        // sounds and animation here

        DamageHandler.StandDamageEntity(victim, 12.0F, this.self);
        MainUtil.makeBleed(victim, 1, 1200, this.self);

        // The host mob with the ribcage trap dies
        DamageHandler.StandDamageEntity(host, Float.MAX_VALUE, this.self);

        clearRibcageTrap();
    }

    public void clearRibcageTrap() {
        if (this.ribcageTarget instanceof StandUser su) {
            su.roundabout$setRibcageTrap(false);
        }
        if (this.ribcageTarget instanceof Mob mob) {
            mob.getNavigation().stop();
        }
        this.ribcageTarget = null;
        this.ribcageWalkDirection = null;
        this.ribcageTrapTicks = 0;
    }

    // ribcage trap end

    // bone bomb start

    public void triggerBoneExplosion() {
        if (this.self.level().isClientSide()) return;
        if (!(this.submergedTarget instanceof LivingEntity host) || !host.isAlive()) return;

        // kill normal mobs; blindness to players & bosses
        if (host instanceof Player || MainUtil.isBossMob(host)) {
            host.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 300, 0, false, false, false));
            emergeServer();
            // 20 second blindness
        } else {
            Level level = host.level();
            Vec3 spawnPos = MainUtil.getMobCenter(host, 0.5F);
            // Find nearest entity within 25 blocks
            List<LivingEntity> nearby = level.getEntitiesOfClass(
                    LivingEntity.class,
                    host.getBoundingBox().inflate(25),
                    e -> e != host
                            && e != this.self
                            && !(e instanceof StandEntity)
                            && e.isAlive()
                            && !e.isSpectator()
            );

            LivingEntity closestTarget = null;
            double minDistanceSq = 25*25;
            for (LivingEntity entity : nearby) {
                double distSq = host.distanceToSqr(entity);
                if (distSq < minDistanceSq) {
                    minDistanceSq = distSq;
                    closestTarget = entity;
                }
            }
            // Shotgun blast towards nearest target
            if (closestTarget != null) {
                Vec3 targetCenter = MainUtil.getMobCenter(closestTarget, 0.5F);
                Vec3 toTarget = targetCenter.subtract(spawnPos);

                for (int i = 0; i < 8; i++) {
                    BoneProjectileEntity bone = new BoneProjectileEntity(level, host);
                    bone.setOwner(this.self);
                    bone.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
                    // sets the gravity to false so it goes in a straight line like star platinum throw until 25 blocks pass
                    bone.setNoGravity(true);
                    //last number represents the spread
                    bone.shoot(toTarget.x, toTarget.y, toTarget.z, 3F, 6F);
                    level.addFreshEntity(bone);
                }
            }

            // sounds and animations and stuff. probably no anims though, just gonna leave behind a blood splatter.
            LivingEntity hostStorage = host;
            emergeServer();
            //20F one shots normal mobs like villagers, zombies and stuff, but keeps bigger mobs alive.
            DamageHandler.StandDamageEntity(hostStorage, 20.0F, this.self);
        }
    }

    // bone bomb end

    // transfer start

    private void prepareTransfer(){
        if (this.self.level().isClientSide()) return;
        if (this.submergedTarget == null || !this.submergedTarget.isAlive()) return;

        // startup time
        this.setActivePower(TRANSFER);
        this.setAttackTime(0);
        this.setAttackTimeDuring(0);

        //test message
        //this.self.sendSystemMessage(Component.literal("it's transfering windup time"));

        //play the animation and sounds here
        playSoundIfPossible(self.level(), null, submergedTarget.blockPosition(),
                ModSounds.DIVER_DOWN_CHARGE_EVENT,
                SoundSource.PLAYERS, 0.8F, 0.8F);
    }

    public void triggerTransfer() {
        if(getAttackTimeDuring() >= TRANSFER_WINDUP_MAX) {
            //this.self.sendSystemMessage(Component.literal("it's transfering time"));
            LivingEntity host = (this.submergedTarget instanceof LivingEntity living) ? living : null;
            if (host == null || !host.isAlive()) {
                cancelTransfer();
                return;
            }

            //makes a bounding box to find entities withing it
            List<LivingEntity> nearby = host.level().getEntitiesOfClass(
                    LivingEntity.class,
                    host.getBoundingBox().inflate(TRANSFER_RANGE),
                    e -> e != host
                            && e != this.self
                            && !(e instanceof StandEntity)
                            && e.isAlive()
                            && !e.isSpectator()
            );

            //gets the closest one
            LivingEntity closestTarget = null;
            double minDistanceSq = TRANSFER_RANGE * TRANSFER_RANGE;
            for (LivingEntity entity : nearby) {
                double distSq = host.distanceToSqr(entity);
                if (distSq < minDistanceSq) {
                    minDistanceSq = distSq;
                    closestTarget = entity;
                }
            }

            if (closestTarget == null) {
                setCooldown(PowerIndex.GENERAL_1,100);
                cancelTransfer();
                return;
            }

            //transfer process
            removeDiverLegsFromTarget();
            if (this.submergedTarget != null) {
                ((StandUser) this.submergedTarget).roundabout$SetDiverUser(null);
            }
            this.submergedTarget = closestTarget;
            ((StandUser) this.submergedTarget).roundabout$SetDiverUser(this);

            // sync with client
            if (this.self instanceof Player player) {
                S2CPacketUtil.sendIntPowerDataPacket(player, DIVER_SUBMERGE_START, closestTarget.getId());
            }

            // sets activate power back to 0
            this.setPowerNone();

            // animation and sounds here
            playSoundIfPossible(self.level(), null, submergedTarget.blockPosition(),
                    ModSounds.DIVER_DOWN_DIVE_EVENT,
                    SoundSource.PLAYERS, 0.8F, 1);
            setCooldown(PowerIndex.GENERAL_1,100);
        }
    }

    private void cancelTransfer () {
        this.setPowerNone();
    }
    // transfer end

    // cleanse negative effects start

    private void cureNegativeEffects() {
        if (this.self.level().isClientSide()) return;
        if (!(this.submergedTarget instanceof LivingEntity targetLiving) || !targetLiving.isAlive()) return;
        boolean hadSlowness = false;

        // get all the harmful effects
        List<MobEffect> negativeEffects = new ArrayList<>();
        for (MobEffectInstance instance : targetLiving.getActiveEffects()) {
            MobEffect effect = instance.getEffect();
            if (effect.getCategory() == MobEffectCategory.HARMFUL && !MainUtil.isSpecialEffect(effect)) {
                negativeEffects.add(effect);
                if (effect == MobEffects.MOVEMENT_SLOWDOWN) {
                    hadSlowness = true;
                }
            }
        }
        if (negativeEffects.isEmpty()) return;

        // Remove all the effects
        for (MobEffect effect : negativeEffects) {
            targetLiving.removeEffect(effect);
        }

        // special turtle master cleanse like what is in the pearl jam docs
        if (hadSlowness) {
            targetLiving.removeEffect(MobEffects.DAMAGE_BOOST);
            targetLiving.removeEffect(MobEffects.DAMAGE_RESISTANCE);
        }

        // sounds here
        playSoundIfPossible(self.level(), null, targetLiving.blockPosition(),
                SoundEvents.ZOMBIE_VILLAGER_CURE,
                SoundSource.PLAYERS, 0.7F, 1.3F);

        setCooldown(PowerIndex.GENERAL_1,getAfflictionCooldown());
    }

    // cleanse negative effects end

    // diver legs start

    private void diverLegs() {
        if (this.self.level().isClientSide()) return;
        if (this.submergedTarget == null || !this.submergedTarget.isAlive()) return;

        this.hasDiverLegs = true;
        ((StandUser) this.submergedTarget).roundabout$setDiverLegs(true);

        //play sound here, replace entity legs with diver down legs
        playSoundIfPossible(self.level(), null, submergedTarget.blockPosition(),
                ModSounds.DIVER_DOWN_TRANSFER_EVENT,
                SoundSource.PLAYERS, 0.8F, 1.2F);
        setCooldown(PowerIndex.GENERAL_1,getAfflictionCooldown());
    }

    private void removeDiverLegsFromTarget() {
        if (!this.hasDiverLegs) return;
        if (this.self != null) {
            ((StandUser) this.self).roundabout$setDiverLegs(false);
        }
        if (this.submergedTarget != null) {
            ((StandUser) this.submergedTarget).roundabout$setDiverLegs(false);
        }
        this.hasDiverLegs = false;
    }

    //diver legs end

    // potion start

    private void embedPotion() {
        if (this.self.level().isClientSide()) return;
        if (!(this.submergedTarget instanceof LivingEntity targetLiving) || !targetLiving.isAlive()) return;
        if (!(this.self instanceof Player player)) return;

        // Check for potions or mob buckets in both main hand and off hand
        InteractionHand hand = InteractionHand.MAIN_HAND;
        ItemStack stack = player.getMainHandItem();
        boolean isMobBucket = stack.getItem() instanceof MobBucketItem;
        List<MobEffectInstance> effects = PotionUtils.getMobEffects(stack);

        if (effects.isEmpty() && !isMobBucket) {
            hand = InteractionHand.OFF_HAND;
            stack = player.getOffhandItem();
            isMobBucket = stack.getItem() instanceof MobBucketItem;
            effects = PotionUtils.getMobEffects(stack);
        }

        // mob bucket stuff
        if (isMobBucket) {
            // turn mobs passive
            if (targetLiving instanceof Mob mob && !MainUtil.isBossMob(targetLiving)) {
                mob.removeAllGoals(goal -> goal instanceof TargetGoal || goal instanceof MeleeAttackGoal || goal instanceof RangedAttackGoal);
                mob.setTarget(null);
                mob.setLastHurtByMob(null);
                mob.setAggressive(false);
            }

            // prevent players and bosses passive from attacking for 3 seconds
            if (targetLiving instanceof Player) {
                targetLiving.addEffect(new MobEffectInstance(ModEffects.IMPRINTING, 60, 0, false, true, true), this.self);
                targetLiving.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 4, false, false, false), this.self);
            } else if (MainUtil.isBossMob(targetLiving)) {
                targetLiving.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 2, false, false, false), this.self);
            }

            // return empty bucket to inventory
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
                if (stack.isEmpty()) {
                    player.setItemInHand(hand, new ItemStack(Items.WATER_BUCKET));
                } else {
                    if (!player.getInventory().add(new ItemStack(Items.WATER_BUCKET))) {
                        player.drop(new ItemStack(Items.WATER_BUCKET), false);
                    }
                }
            }

            // sound effects and stuff here
            playSoundIfPossible(self.level(), null, targetLiving.blockPosition(),
                    ModSounds.DIVER_DOWN_TRANSFER_EVENT,
                    SoundSource.PLAYERS, 0.8F, 1.1F);
            emergeServer();
            return;
        }

        // potion stuff
        if (effects.isEmpty()) return;

        for (MobEffectInstance instance : effects) {
            MobEffect effect = instance.getEffect();
            if (effect.getCategory() == MobEffectCategory.HARMFUL) {
                emergeServer();
            }
        }

        // apply the potion effect
        for (MobEffectInstance effect : effects) {
            int newDuration = effect.getDuration();
            // make potions with durations longer
            if (!effect.getEffect().isInstantenous()) {
                newDuration = (int) (effect.getDuration() + 300); // extra 15 seconds
            }

            MobEffectInstance boostedEffect = new MobEffectInstance(
                    effect.getEffect(),
                    newDuration,
                    effect.getAmplifier() + 1, // extra potion amplifier
                    effect.isAmbient(),
                    effect.isVisible(),
                    effect.showIcon()
            );

            targetLiving.addEffect(boostedEffect, this.self);
            setCooldown(PowerIndex.GENERAL_1,getAfflictionCooldown());
        }

        // Consume potion and return bottle
        if (!player.getAbilities().instabuild) {
            Item item = stack.getItem();
            stack.shrink(1);
            if (item instanceof PotionItem && !(item instanceof SplashPotionItem) && !(item instanceof LingeringPotionItem)) {
                if (stack.isEmpty()) {
                    player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
                } else {
                    if (!player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE))) {
                        player.drop(new ItemStack(Items.GLASS_BOTTLE), false);
                    }
                }
            }
        }
    }

    //potion end

    //disguise start

    public boolean shouldShowDisguiseArmor() {
        return this.showDisguiseArmor;
    }

    public void setShowDisguiseArmor(boolean show) {
        this.showDisguiseArmor = show;
    }

    private void tryDisguiseClient(){
        if(this.self.level().isClientSide())
            ClientUtil.openDisguiseScreen();
    }

    public void applyDisguiseToTarget(Entity target, GameProfile profile) {
        if (this.self.level().isClientSide()) return;
        setCooldown(PowerIndex.GENERAL_1,getAfflictionCooldown());
        // it's testing time
        /*if (this.self instanceof ServerPlayer serverPlayer) {
            serverPlayer.sendSystemMessage(Component.literal("it's disguising time as " + profile.getName()));
        }*/
    }

    //disguise end

    // dive afflictions end

    // disassembly start

    /**
     * Checks if a targeted block at the given position is eligible for disassembly.
     * Much like the block grab thing that blockgrab stands can do
     */
    public boolean canDisassembleBlock(BlockPos pos) {
        Level level = this.self.level();
        if (level == null || pos == null) return false;

        // air
        BlockState state = level.getBlockState(pos);
        if (state.isAir()) return false;

        // Unbreakable blocks check (bedrock, end portal frame, etc.)
        if (state.getBlock().defaultDestroyTime() < 0) return false;

        // Server config block blacklist check
        if (MainUtil.isBlockBlacklisted(state)) return false;

        // check for distance (like BlockGrabPreset.getGrabRange())
        if (this.self.distanceToSqr(Vec3.atCenterOf(pos)) > 30.0) return false; // 30/6 = 5 blocks max

        // check for stand griefing enabled
        if (this.self instanceof ServerPlayer PE) {
            if (!level.getGameRules().getBoolean(ModGamerules.ROUNDABOUT_STAND_GRIEFING)) return false;
            if (PE.blockActionRestricted(PE.serverLevel(), pos, PE.gameMode.getGameModeForPlayer())) return false;
            if (!level.mayInteract(PE, pos)) return false;
        }

        // must have an associated Item
        Item targetItem = state.getBlock().asItem();
        if (targetItem == net.minecraft.world.item.Items.AIR) return false;

        // check for recipes
        // On server side, check the RecipeManager
        if (!level.isClientSide() && this.self.getServer() != null) {
            RecipeManager recipeManager = this.self.getServer().getRecipeManager();

            java.util.List<CraftingRecipe> matchingRecipes = new java.util.ArrayList<>();
            for (CraftingRecipe recipe : recipeManager.getAllRecipesFor(RecipeType.CRAFTING)) {
                if (recipe.getResultItem(level.registryAccess()).getItem() == targetItem) {
                    matchingRecipes.add(recipe);
                }
            }

            // Must have exactly one crafting recipe
            if (matchingRecipes.size() != 1) return false;

            CraftingRecipe recipe = matchingRecipes.get(0);

            // Must only produce 1 item to prevent dupes (e.g. 6 blocks -> 4 stairs)
            if (recipe.getResultItem(level.registryAccess()).getCount() != 1) return false;

            // Must have no alternative ingredients (tags or multiple items)
            for (Ingredient ingredient : recipe.getIngredients()) {
                if (ingredient.isEmpty()) continue;
                if (ingredient.getItems().length != 1) {
                    return false;
                }
                // don't allow recipes that return items (lilke cakes or honey blocks)
                if (ingredient.getItems()[0].getItem().hasCraftingRemainingItem()) {
                    return false;
                }
            }
        }

        return true;
    }

    //gets the block drops
    private java.util.List<ItemStack> getDisassemblyDrops(BlockState state) {
        java.util.List<ItemStack> drops = new java.util.ArrayList<>();
        if (this.self.getServer() == null) return drops;

        Item targetItem = state.getBlock().asItem();
        RecipeManager recipeManager = this.self.getServer().getRecipeManager();

        for (CraftingRecipe recipe : recipeManager.getAllRecipesFor(RecipeType.CRAFTING)) {
            if (recipe.getResultItem(this.self.level().registryAccess()).getItem() == targetItem) {
                for (Ingredient ingredient : recipe.getIngredients()) {
                    if (!ingredient.isEmpty()) {
                        drops.add(ingredient.getItems()[0].copy());
                    }
                }
                break;
            }
        }
        return drops;
    }

    //checks for a block to disassemble
    private void tryDisassembleBlockClient() {
        if(canExecuteMoveWithLevel(getBlockDisassemblyLevel()) ) {
            if (!this.onCooldown(PowerIndex.SKILL_1_SNEAK)) {
                Vec3 eyePos = this.self.getEyePosition(0);
                Vec3 viewVec = this.self.getViewVector(0);
                Vec3 targetVec = eyePos.add(viewVec.x * 6.0, viewVec.y * 6.0, viewVec.z * 6.0);

                BlockHitResult hit = this.self.level().clip(new ClipContext(
                        eyePos, targetVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.self));

                if (hit.getType() == HitResult.Type.BLOCK) {
                    BlockPos pos = hit.getBlockPos();
                    if (canDisassembleBlock(pos)) {
                        ((StandUser) this.getSelf()).roundabout$tryBlockPosPower(DISASSEMBLE_BLOCK, true, pos);
                        tryBlockPosPowerPacket(DISASSEMBLE_BLOCK, pos);
                    }
                }
            }
        }
    }

    private void disassembleBlock(BlockPos pos) {
        if (this.self.level().isClientSide()) return;

        // check with canDisassembleBlock
        if (!canDisassembleBlock(pos)) return;

        BlockState state = this.self.level().getBlockState(pos);
        java.util.List<ItemStack> drops = getDisassemblyDrops(state);
        if (drops.isEmpty()) return;

        // DESTROY the block
        boolean removed = this.self.level().destroyBlock(pos, false, this.self);
        if (!removed) return;

        // animate stand and sounds and stuff here
        playSoundIfPossible(self.level(), null, pos,
                ModSounds.DIVER_DOWN_DIVE_EVENT,
                SoundSource.PLAYERS, 0.6F, 1.5F);

        if (this.getSelf() instanceof ServerPlayer pl) {
            S2CPacketUtil.sendCooldownSyncPacket(pl, PowerIndex.SKILL_1_SNEAK, 60);
        }
        this.setCooldown(PowerIndex.SKILL_1_SNEAK, 20);

        // run the drops list
        for (ItemStack drop : drops) {
            drop.setCount(1);
            ItemEntity itemEntity = new ItemEntity(
                    this.self.level(),
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    drop
            );
            itemEntity.setDefaultPickUpDelay();
            this.self.level().addFreshEntity(itemEntity);
        }
        addEXP(1);
    }

    // disassembly end

    /**
     * Used to check if stand able to be used or not.
     * Use this to render alternative icons for moves etc, depending on what move is
     * being used
     * <p>
     * Update this if there are more moves that disable stand
     *
     * @return true if the stand moves are disabled, false otherwise
     */
    public boolean areStandMovesDisabled() {
        return hasLimbsDeployed() || isDiveActive() || isPiloting() || inZipMode() || this.getActivePower() == DIVER_SUBMERGE_START;
    }

    //enables player attacks while DD is diving somewhere
    @Override
    public boolean interceptAttack() {
        if (inZipMode()) {
            return true;
        }
        if (areStandMovesDisabled()) {
            return false;
        }
        return super.interceptAttack();
    }

    // disables PLAYER MOVES for zip mode
    @Override
    public boolean interceptAllInteractions() {
        if (inZipMode()) {
            return true;
        }
        if (areStandMovesDisabled()) {
            return false;
        }
        return super.interceptAllInteractions();
    }

    // disables player mining in zip mode
    @Override
    public boolean cancelAllRandomMiningThatBreaksMoves() {
        if (inZipMode()) {
            return true;
        }
        if (areStandMovesDisabled()) {
            return false;
        }
        return super.cancelAllRandomMiningThatBreaksMoves();
    }

    // disables player mining progress in zip mode (As a failsafe)
    @Override
    public float getBonusPassiveMiningSpeed() {
        if (inZipMode()) {
            return 0.0F;
        }
        if (areStandMovesDisabled()) {
            return 1F;
        }
        return super.getBonusPassiveMiningSpeed();
    }

    // overrides below are for making sure the stand can't do a variety of stuff
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

    @Override
    public boolean canGuard() {
        if (areStandMovesDisabled()) {
            return false;
        }
        return super.canGuard();
    }

    @Override
    public void buttonInputBarrage(boolean keyIsDown, Options options) {
        if (areStandMovesDisabled()) {
            return;
        }
        super.buttonInputBarrage(keyIsDown, options);
    }

    // for levels

    @Override
    public byte getMaxLevel(){
        return 7;
    }

    @Override
    public void levelUp(){
        if (!this.getSelf().level().isClientSide() && this.getSelf() instanceof Player PE){
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

    /*
     * this override goes here cuz it goes with the rest of the overrides
     * This will be used for recalling "pilot" moves, such as the limb move,
     * or the submerge move when unsummoning stand.
     */
    @Override
    public void onStandSummon(boolean desummon) {
        if (desummon) {
            removeDiverLegsFromTarget();
            recallLimbs();
            toggleZip(false);
            if (isDiveActive()) {
                emergeServer();
            }
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
            // There was this light green bar with some pink in the center that's perfect
            // for
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

    public Direction getHeelDirection() {
        return feetDirection;
    }

    private boolean setPowerChargePhase() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.SNEAK_ATTACK_CHARGE);
        this.poseStand(OffsetIndex.GUARD);
        // uncomment the animation later when done
        // animateStand
        playStandUserOnlySoundsIfNearby(IMPALE_NOISE, 27, false, false);
        return true;
    }

    private boolean setPowerPhasePunch() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.SNEAK_ATTACK);
        this.poseStand(OffsetIndex.ATTACK);
        this.chargedPhasePunch = Math.min(this.chargedPhasePunch, getMaxPhasePunchTime());
        // uncomment the animation later after done.
        // animateStand
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
