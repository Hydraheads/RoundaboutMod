package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IClientEntity;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.KeyboardPilotInput;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.client.WhitesnakeControlClient;
import net.hydra.jojomod.client.gui.WhitesnakeInventoryMenu;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.HallucinatoryAcidProjectile;
import net.hydra.jojomod.entity.projectile.ThrownObjectEntity;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.WhitesnakeEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModGamerules;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.event.powers.whitesnake.OldEffect;
import net.hydra.jojomod.event.powers.whitesnake.WhitesnakeControlInventory;
import net.hydra.jojomod.event.powers.whitesnake.disc.WhitesnakeDiscUtil;
import net.hydra.jojomod.event.powers.visagedata.voicedata.PucciVoice;
import net.hydra.jojomod.item.AbstractBodyDiscItem;
import net.hydra.jojomod.item.CommandDiscItem;
import net.hydra.jojomod.item.FirearmItem;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.MemoryDiscItem;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.BlockGrabPreset;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.RotationAnimation;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PowersWhitesnake extends BlockGrabPreset {
    public static final int PILOT_INTERACTION_RANGE = 5;
    private static final byte DISC_STEAL = 90;
    public static final byte DISC_SELECTION = 91;
    private static final byte WHITESNAKE_INVENTORY = 93;
    private static final byte ACID_TOSS = 94;
    private static final byte CONTROL_DASH = 95;
    private static final byte MELTING_MODE = 97;
    private static final byte MELTING_HOVER = 98;
    private static final byte MELTING_GRAVITY = 99;
    private static final byte AUTO_MODE = 100;
    private static final byte TIME_SPARK = 101;
    private static final byte ENTER_CONTROL_MODE = 102;
    private static final byte CONTROL_INTERACT = 104;
    private static final byte AUTO_MODE_MOVE = 105;
    private static final byte AUTO_MODE_ATTACK = 106;
    private static final byte CONTROL_MODE_FROM_AUTO = 107;
    private static final byte ROUNDABOUT_DODGE_NOISE = 59;
    private static final byte TIME_SPARK_COOLDOWN = PowerIndex.SKILL_EXTRA;
    private static final byte PHASE_GRAB_COOLDOWN = PowerIndex.SKILL_EXTRA_2;
    private static final byte ACID_CHARGE_NOISE = 123;
    private static final byte DISC_STEAL_CHARGE_NOISE = 124;
    private static final int ACID_TOSS_COOLDOWN_TICKS = 120;
    private static final int DISC_STEAL_COOLDOWN = 300;
    private static final int DISC_STEAL_MISS_COOLDOWN = 60;
    private static final float CONTROL_PUNCH_RANGE = 3.0F;
    private static final double FORWARD_BARRAGE_RANGE = 10.0D;
    private Vec3 phaseGrabOffset = Vec3.ZERO;
    private boolean meltingMode;
    private boolean meltingHoverExhausted;
    private int meltingCrawlGraceTicks;
    private int meltingCrawlTransitionTicks;
    private boolean autoMode;
    private int autoAttackCooldown;
    private Vec3 autoMoveTarget;
    private int manualAutoTargetId = -1;
    private int mobAbilityDecisionCooldown;
    private int mobGuardTicks;
    private int mobAutoModeTicks;
    private byte selectedDisc = WhitesnakeDiscUtil.STAND;
    private BlockPos timeSparkCropPos;
    private int timeSparkCropTicks;
    private boolean holdDownClick;
    private int chargedFinal;

    public PowersWhitesnake(LivingEntity self) {
        super(self);
    }

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
        return Component.literal("Olive").withStyle(ChatFormatting.BLUE);
    }

    @Override
    public float getPickMiningSpeed() {
        return 14.0F;
    }

    @Override
    public float getAxeMiningSpeed() {
        return 8.0F;
    }

    @Override
    public float getSwordMiningSpeed() {
        return 8.0F;
    }

    @Override
    public float getShovelMiningSpeed() {
        return 8.0F;
    }

    @Override
    public boolean isStandEnabled() {
        return ClientNetworking.getAppropriateConfig().whitesnakeSettings.enableWhitesnake;
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersWhitesnake(entity);
    }

    @Override
    public StandEntity getNewStandEntity() {
        return ModEntities.WHITESNAKE.create(this.getSelf().level());
    }

    @Override
    public List<Byte> getSkinList() {
        List<Byte> skins = Lists.newArrayList();
        skins.add(WhitesnakeEntity.ANIME_SKIN);
        skins.add(WhitesnakeEntity.MANGA_SKIN);
        if (self instanceof Player player) {
            byte level = ((IPlayerEntity) player).roundabout$getStandLevel();
            ItemStack disc = ((StandUser) player).roundabout$getStandDisc();
            boolean bypass = player.isCreative()
                    || !disc.isEmpty() && disc.getItem() instanceof MaxStandDiscItem;
            if (level > 1 || bypass) {
                skins.add(WhitesnakeEntity.ANIME_PURPLE_SKIN);
                skins.add(WhitesnakeEntity.ANIME_GREEN_SKIN);
                skins.add(WhitesnakeEntity.ANIME_YELLOW_SKIN);
                skins.add(WhitesnakeEntity.ANIME_AQUA_SKIN);
                skins.add(WhitesnakeEntity.MANGA_PURPLE_SKIN);
                skins.add(WhitesnakeEntity.MANGA_RED_SKIN);
            }
            if (level > 2 || bypass) {
                skins.add(WhitesnakeEntity.ASBR_SKIN);
                skins.add(WhitesnakeEntity.AGOGO_SKIN);
            }
            if (level > 3 || bypass) {
                skins.add(WhitesnakeEntity.COTTON_CANDY_SKIN);
                skins.add(WhitesnakeEntity.SOUR_CANDY_SKIN);
            }
            if (level > 4 || bypass) {
                skins.add(WhitesnakeEntity.DARK_SKIN);
            }
            if (level > 5 || bypass) {
                skins.add(WhitesnakeEntity.SILVER_SKIN);
                skins.add(WhitesnakeEntity.GOLD_SKIN);
            }
            if (level > 6 || bypass) {
                skins.add(WhitesnakeEntity.GOLD_TRIMMED_SKIN);
                skins.add(WhitesnakeEntity.EDGY_GOLD_SKIN);
                skins.add(WhitesnakeEntity.SANDSNAKE_SKIN);
            }
        }
        return skins;
    }

    @Override
    public Component getSkinName(byte skinId) {
        String key = switch (skinId) {
            case WhitesnakeEntity.MANGA_SKIN -> "skins.roundabout.whitesnake.manga";
            case WhitesnakeEntity.ANIME_PURPLE_SKIN -> "skins.roundabout.whitesnake.anime_purple";
            case WhitesnakeEntity.ANIME_GREEN_SKIN -> "skins.roundabout.whitesnake.anime_green";
            case WhitesnakeEntity.ANIME_YELLOW_SKIN -> "skins.roundabout.whitesnake.anime_yellow";
            case WhitesnakeEntity.ANIME_AQUA_SKIN -> "skins.roundabout.whitesnake.anime_aqua";
            case WhitesnakeEntity.MANGA_PURPLE_SKIN -> "skins.roundabout.whitesnake.manga_purple";
            case WhitesnakeEntity.MANGA_RED_SKIN -> "skins.roundabout.whitesnake.manga_red";
            case WhitesnakeEntity.GOLD_SKIN -> "skins.roundabout.whitesnake.gold";
            case WhitesnakeEntity.SILVER_SKIN -> "skins.roundabout.whitesnake.silver";
            case WhitesnakeEntity.COTTON_CANDY_SKIN -> "skins.roundabout.whitesnake.cotton_candy";
            case WhitesnakeEntity.ASBR_SKIN -> "skins.roundabout.whitesnake.asbr";
            case WhitesnakeEntity.AGOGO_SKIN -> "skins.roundabout.whitesnake.agogo";
            case WhitesnakeEntity.DARK_SKIN -> "skins.roundabout.whitesnake.dark";
            case WhitesnakeEntity.SOUR_CANDY_SKIN -> "skins.roundabout.whitesnake.sour_candy";
            case WhitesnakeEntity.EDGY_GOLD_SKIN -> "skins.roundabout.whitesnake.edgy_gold";
            case WhitesnakeEntity.GOLD_TRIMMED_SKIN -> "skins.roundabout.whitesnake.gold_trimmed";
            case WhitesnakeEntity.SANDSNAKE_SKIN -> "skins.roundabout.whitesnake.sandsnake";
            default -> "skins.roundabout.whitesnake.anime";
        };
        return Component.translatable(key);
    }

    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    @Override
    public void playSummonSound() {
        if (self.isCrouching()) return;
        if (self instanceof Player player
                && ((IPlayerEntity) player).roundabout$getVoiceData() instanceof PucciVoice voice) {
            voice.playSummon();
        }
        playStandUserOnlySoundsIfNearby(getSummonSound(), 10, false, false);
    }

    @Override
    public int getMaxGuardPoints() {
        return ClientNetworking.getAppropriateConfig().whitesnakeSettings.whitesnakeGuardPoints;
    }

    // Control Mode
    @Override
    public boolean isPiloting() {
        if (self instanceof Player player) {
            StandEntity stand = ((StandUser) player).roundabout$getStand();
            return stand != null && ((IPlayerEntity) player).roundabout$getControlling() == stand.getId();
        }
        return false;
    }

    @Override
    public int getMaxPilotRange() {
        return ClientNetworking.getAppropriateConfig().whitesnakeSettings.controlModeRange;
    }

    public int getMaxPilotVerticalRange() {
        return ClientNetworking.getAppropriateConfig().whitesnakeSettings.controlModeVerticalRange;
    }

    private boolean isWithinRemoteRange(Entity stand) {
        double horizontalDistance = MainUtil.cheapDistanceTo2(
                stand.getX(), stand.getZ(), self.getX(), self.getZ());
        double verticalDistance = Math.abs(stand.getY() - self.getY());
        return horizontalDistance <= getMaxPilotRange()
                && verticalDistance <= getMaxPilotVerticalRange();
    }

    // Auto Mode
    public boolean isAutoMode() {
        return autoMode;
    }

    private void toggleControlModeClient() {
        if (!(self instanceof Player)) return;
        if (autoMode) {
            enterControlFromAutoClient();
            return;
        }
        if (isPiloting()) {
            exitControlModeClient();
            return;
        }
        StandEntity stand = getStandEntity(self);
        if (isUsableStand(stand)) {
            setPiloting(stand.getId());
            WhitesnakeControlClient.enter();
            tryIntPowerPacket(ENTER_CONTROL_MODE, stand.getId());
        }
    }

    private void toggleAutoModeClient() {
        if (!(self instanceof Player)) return;
        if (autoMode) {
            setAutoMode(false);
            tryIntPowerPacket(AUTO_MODE, 0);
            return;
        }
        StandEntity stand = getStandEntity(self);
        if (!isUsableStand(stand)) return;
        boolean wasPiloting = isPiloting();
        setAutoMode(true);
        tryIntPowerPacket(AUTO_MODE, 1);
        if (wasPiloting) exitControlModeClient();
    }

    private void enterControlFromAutoClient() {
        if (!(self instanceof Player) || !autoMode) return;
        StandEntity stand = getStandEntity(self);
        if (!isUsableStand(stand)) return;
        tryIntPower(CONTROL_MODE_FROM_AUTO, true, stand.getId());
        tryIntPowerPacket(CONTROL_MODE_FROM_AUTO, stand.getId());
        WhitesnakeControlClient.enter();
    }

    private void exitControlModeClient() {
        setPiloting(0);
        WhitesnakeControlClient.exit();
        tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT, 0);
    }

    private void setAutoMode(boolean enabled) {
        StandEntity stand = getStandEntity(self);
        boolean wasAutoMode = autoMode;
        boolean nextAutoMode = enabled && isUsableStand(stand);
        boolean standModeChanged = stand instanceof WhitesnakeEntity whitesnake
                && whitesnake.isAutoModeActive() != nextAutoMode;
        if (autoMode == nextAutoMode && !standModeChanged) return;
        autoMode = nextAutoMode;
        if (autoMode) prepareStandForRemoteControl(stand);
        if (autoMode && self instanceof Player player && player.isUsingItem()
                && player.getUseItem().isEdible()) {
            player.stopUsingItem();
        }
        clearAutoModeTargets();
        setMeltingMode(false, false);
        if (stand instanceof FollowingStandEntity following) {
            following.setOffsetType(autoMode ? OffsetIndex.LOOSE : OffsetIndex.FOLLOW);
        }
        if (stand instanceof WhitesnakeEntity whitesnake) {
            whitesnake.setAutoMode(autoMode);
            whitesnake.getNavigation().stop();
            whitesnake.clearControlInput();
            whitesnake.clearDisguise();
            whitesnake.setTarget(null);
        }
        resetRemoteStandMovement(stand);
        if (wasAutoMode && !autoMode && !isPiloting()) transferRemoteStandEffects(stand);
        if (!autoMode && getActivePower() == PowerIndex.ATTACK) tryPower(PowerIndex.NONE, true);
    }

    private void clearAutoModeTargets() {
        autoAttackCooldown = 0;
        autoMoveTarget = null;
        manualAutoTargetId = -1;
    }

    @Override
    public void setPiloting(int id) {
        if (!(self instanceof Player player)) return;
        boolean wasPiloting = isPiloting();
        StandEntity stand = getStandEntity(self);
        Entity selected = self.level().getEntity(id);
        boolean entering = stand != null && selected != null && selected.is(stand);
        boolean leavingAutoMode = entering && autoMode;
        if (entering && getActivePower() == PowerIndex.POWER_2_BLOCK) {
            stopPhaseGrabAtCurrentPosition(stand);
        }
        if (entering) prepareStandForRemoteControl(stand);
        ((IPlayerEntity) player).roundabout$setIsControlling(entering ? id : 0);
        if (stand instanceof WhitesnakeEntity whitesnake) whitesnake.setControlMode(entering);
        if (leavingAutoMode) {
            setAutoMode(false);
            if (stand instanceof WhitesnakeEntity whitesnake) whitesnake.clearAutoModeMovement();
        }
        if (stand instanceof FollowingStandEntity following) {
            following.setOffsetType(entering || autoMode ? OffsetIndex.LOOSE : OffsetIndex.FOLLOW);
        }
        if (!entering) {
            clearForwardBarrageTravel();
            setMeltingMode(false, false);
            player.stopUsingItem();
            if (stand instanceof WhitesnakeEntity whitesnake) {
                whitesnake.clearControlInput();
                whitesnake.clearDisguise();
            }
            resetRemoteStandMovement(stand);
            if (wasPiloting && !autoMode) transferRemoteStandEffects(stand);
        } else if (entering && stand instanceof WhitesnakeEntity whitesnake) {
            whitesnake.getNavigation().stop();
            whitesnake.clearControlInput();
        }
        if (!self.level().isClientSide() && wasPiloting != entering) {
            LivingEntity soundSource = stand == null ? self : stand;
            playSoundIfPossible(self.level(),null, soundSource.blockPosition(), entering
                            ? ModSounds.WHITESNAKE_CONTROL_MODE_ENTER_EVENT
                            : ModSounds.WHITESNAKE_CONTROL_MODE_EXIT_EVENT,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    private void setMeltingMode(boolean enabled, boolean rotateCamera) {
        meltingMode = enabled && isPiloting();
        meltingHoverExhausted = false;
        meltingCrawlGraceTicks = 0;
        meltingCrawlTransitionTicks = 0;
        WhitesnakeEntity whitesnake = getStandEntity(self) instanceof WhitesnakeEntity entity ? entity : null;
        if (whitesnake != null) whitesnake.setMeltingModeActive(meltingMode);
        if (meltingMode) {
            if (getActivePower() == PowerIndex.ATTACK) tryPower(PowerIndex.NONE, true);
            return;
        }
        if (whitesnake == null) return;
        whitesnake.setMeltingHovering(false);
        Direction oldGravity = ((IGravityEntity) whitesnake).roundabout$getGravityDirection();
        ((IGravityEntity) whitesnake).roundabout$setGravityDirection(Direction.DOWN);
        if (rotateCamera && isClient()) {
            WhitesnakeControlClient.rotateLookForGravityChange(whitesnake, oldGravity, Direction.DOWN);
        }
    }

    private static void resetRemoteStandMovement(StandEntity stand) {
        if (stand == null) return;
        stand.setSprinting(false);
        stand.setPose(Pose.STANDING);
    }

    private void transferRemoteStandEffects(StandEntity stand) {
        if (self.level().isClientSide() || !(stand instanceof WhitesnakeEntity whitesnake)) return;
        List<MobEffectInstance> effects = List.copyOf(whitesnake.getActiveEffects());
        for (MobEffectInstance effect : effects) self.addEffect(new MobEffectInstance(effect));
        if (!effects.isEmpty()) whitesnake.removeAllEffects();
    }

    private static boolean isUsableStand(StandEntity stand) {
        return stand != null && stand.isAlive() && !stand.isRemoved();
    }

    private void stopPhaseGrabAtCurrentPosition(StandEntity stand) {
        stopPowerAtCurrentPosition(stand);
    }

    private void stopPowerAtCurrentPosition(StandEntity stand) {
        Vec3 position = stand.position();
        float yaw = stand.getYRot();
        float pitch = stand.getXRot();
        tryPower(PowerIndex.NONE, true);
        restoreStandTransform(stand, position, yaw, pitch);
    }

    private void prepareStandForRemoteControl(StandEntity stand) {
        if (stand == null) return;
        Vec3 position = stand.position();
        float yaw = stand.getYRot();
        float pitch = stand.getXRot();
        clearForwardBarrageTravel();
        if (stand instanceof FollowingStandEntity following) following.setOffsetType(OffsetIndex.LOOSE);
        normalizeRemoteGravity(stand);
        restoreStandTransform(stand, position, yaw, pitch);
    }

    private static void normalizeRemoteGravity(StandEntity stand) {
        IGravityEntity gravity = (IGravityEntity) stand;
        gravity.roundabout$setBaseGravityDirection(Direction.DOWN);
        gravity.roundabout$setGravityStrength(1.0D);
        gravity.roundabout$setGravityDirection(Direction.DOWN);
        gravity.roundabout$applyGravityChange();
        if (stand.level().isClientSide()) {
            ((IClientEntity) stand).roundabout$setGravityAnimation(new RotationAnimation());
        }
    }

    private void clearForwardBarrageTravel() {
        forwardBarrage = false;
        moveStarted = false;
    }

    private static void restoreStandTransform(StandEntity stand, Vec3 position, float yaw, float pitch) {
        stand.setPos(position);
        stand.setYRot(yaw);
        stand.setXRot(pitch);
        stand.setYHeadRot(yaw);
        stand.setDeltaMovement(Vec3.ZERO);
        stand.getNavigation().stop();
    }

    private boolean hasDetachedStand() {
        StandEntity stand = getStandEntity(self);
        return isPiloting() || autoMode
                || stand instanceof WhitesnakeEntity whitesnake && whitesnake.isRemoteControlled();
    }

    @Override
    public void poseStand(byte pose) {
        if (!hasDetachedStand()) super.poseStand(pose);
    }

    @Override
    public boolean setPowerNone() {
        boolean detached = hasDetachedStand();
        boolean result = super.setPowerNone();
        if (detached && getStandEntity(self) instanceof FollowingStandEntity following) {
            following.setOffsetType(OffsetIndex.LOOSE);
        }
        return result;
    }

    @Override
    public void animateStand(byte animation) {
        StandEntity stand = getStandEntity(self);
        if (stand instanceof WhitesnakeEntity whitesnake && whitesnake.isDisguised()
                && animation != StandEntity.IDLE && animation != StandEntity.BLOCK
                && getActivePower() != PowerIndex.MINING) {
            whitesnake.clearDisguise();
        }
        super.animateStand(animation);
    }

    @Override
    public void synchToCamera() {
        if (isPiloting()) {
            LivingEntity stand = getPilotingStand();
            if (stand != null) WhitesnakeControlClient.applyLook(stand);
        }
    }

    @Override
    public void pilotInputAttack() {
        if (!self.level().isClientSide()) return;
        if (isControlHovering()) return;
        Minecraft minecraft = Minecraft.getInstance();
        if (WhitesnakeControlClient.tryMining(minecraft)) return;
        if (!(self instanceof Player player)) return;
        ItemStack selected = self.getMainHandItem();
        if (selected.getItem() instanceof FirearmItem firearm
                && firearm.interceptAttack(selected, player)
                && !player.getCooldowns().isOnCooldown(selected.getItem())) {
            C2SPacketUtil.gunShot();
            StandEntity stand = getStandEntity(self);
            if (stand instanceof WhitesnakeEntity whitesnake) whitesnake.clearDisguise();
            return;
        }
        preCheckButtonInputAttack(true, minecraft.options);
    }

    @Override
    public void preCheckButtonInputAttack(boolean keyIsDown, Options options) {
        if (autoMode) return;
        if (keyIsDown && isControlHovering()) return;
        if (isPiloting() && keyIsDown
                && WhitesnakeControlClient.tryMining(Minecraft.getInstance())) return;
        super.preCheckButtonInputAttack(keyIsDown, options);
    }

    @Override
    public void preCheckButtonInputUse(boolean keyIsDown, Options options) {
        if (!autoMode) super.preCheckButtonInputUse(keyIsDown, options);
    }

    @Override
    public void preCheckButtonInputBarrage(boolean keyIsDown, Options options) {
        if (!autoMode) super.preCheckButtonInputBarrage(keyIsDown, options);
    }

    @Override
    public boolean preCheckButtonInputGuard(boolean keyIsDown, Options options) {
        return !autoMode && super.preCheckButtonInputGuard(keyIsDown, options);
    }

    @Override
    public boolean interceptAttack() {
        return !autoMode && super.interceptAttack();
    }

    @Override
    public boolean interceptGuard() {
        return !autoMode && super.interceptGuard();
    }

    @Override
    public void pilotStandControls(KeyboardPilotInput input, LivingEntity entity) {
        if (!(entity instanceof WhitesnakeEntity whitesnake)) return;
        whitesnake.setControlInput(input.leftImpulse, input.forwardImpulse);
        entity.setShiftKeyDown(input.shiftKeyDown);
        entity.setPose(input.shiftKeyDown ? Pose.CROUCHING : Pose.STANDING);
        entity.setMaxUpStep(0.6F);
        boolean sprinting = !meltingMode && !input.shiftKeyDown && input.forwardImpulse > 0.0F
                && Minecraft.getInstance().options.keySprint.isDown();
        entity.setSprinting(sprinting);
        boolean swimming = !meltingMode && entity.isInWater();
        boolean inLava = !meltingMode && entity.isInLava();
        entity.setSwimming(swimming && sprinting);
        if (entity.isSwimming()) entity.setPose(Pose.SWIMMING);
        if (swimming || inLava) whitesnake.controlSwim(input.jumping, input.shiftKeyDown);
        float movementSpeed = meltingMode ? 0.06F : input.shiftKeyDown ? 0.03F : sprinting ? 0.13F : 0.1F;
        entity.setSpeed(inputSpeedModifiers(movementSpeed));
        entity.setYHeadRot(entity.getYRot());
        if (whitesnake.getMeltingHoverCharge() <= 0) meltingHoverExhausted = true;
        if (!input.jumping) meltingHoverExhausted = false;
        Direction gravity = ((IGravityEntity) whitesnake).roundabout$getGravityDirection();
        boolean hovering = meltingMode && input.jumping && !meltingHoverExhausted
                && whitesnake.getMeltingHoverCharge() > 0
                && (whitesnake.isMeltingHovering() || gravity != Direction.DOWN
                || entity.getDeltaMovement().y <= 0.0D);
        if (hovering != whitesnake.isMeltingHovering()) {
            tryIntPower(MELTING_HOVER, true, hovering ? 1 : 0);
            tryIntPowerPacket(MELTING_HOVER, hovering ? 1 : 0);
        }
        if (hovering) {
            meltingCrawlGraceTicks = 0;
            meltingCrawlTransitionTicks = 0;
            setMeltingGravityClient(whitesnake, Direction.DOWN);
            Vec3 velocity = entity.getDeltaMovement();
            double vertical = input.shiftKeyDown ? 0.0001D : Math.min(velocity.y + 0.2D, 0.1D);
            entity.setDeltaMovement(velocity.x, vertical, velocity.z);
            entity.resetFallDistance();
        } else if (meltingMode) {
            updateMeltingCrawl(whitesnake, input);
        } else {
            meltingCrawlGraceTicks = 0;
            meltingCrawlTransitionTicks = 0;
        }
        if (!meltingMode && !swimming && !inLava && input.jumping && entity.onGround()) {
            whitesnake.controlJump();
        }
    }

    // Melting Mode
    private void updateMeltingCrawl(WhitesnakeEntity whitesnake, KeyboardPilotInput input) {
        IGravityEntity gravityEntity = (IGravityEntity) whitesnake;
        Direction current = gravityEntity.roundabout$getGravityDirection();
        if (meltingCrawlTransitionTicks > 0) {
            meltingCrawlTransitionTicks--;
            meltingCrawlGraceTicks = 4;
            return;
        }
        boolean moving = Math.abs(input.leftImpulse) > 0.01F || Math.abs(input.forwardImpulse) > 0.01F;

        if (moving) {
            Vec3 movement = getMeltingMovementVector(whitesnake, input, current);
            Direction movementDirection = Direction.getNearest(movement.x, movement.y, movement.z);
            if (movementDirection != current && movementDirection != current.getOpposite()
                    && touchesMeltingCrawlSurface(whitesnake, movementDirection)) {
                meltingCrawlGraceTicks = 4;
                beginMeltingCrawlTransition(whitesnake, movementDirection);
                return;
            }

            Vec3 lookAhead = movement.normalize().scale(0.42D);
            Direction outerSurface = movementDirection.getOpposite();
            if (outerSurface != current && outerSurface != current.getOpposite()
                    && touchesMeltingCrawlSurface(whitesnake, current)
                    && !touchesMeltingCrawlSurface(whitesnake, current, lookAhead)
                    && touchesMeltingCrawlSurface(whitesnake, outerSurface, lookAhead)) {
                meltingCrawlGraceTicks = 4;
                beginMeltingCrawlTransition(whitesnake, outerSurface);
                return;
            }
        }

        if (touchesMeltingCrawlSurface(whitesnake, current)) {
            meltingCrawlGraceTicks = 4;
            return;
        }

        for (Direction direction : Direction.values()) {
            if (direction == current || direction == current.getOpposite()) continue;
            if (touchesMeltingCrawlSurface(whitesnake, direction)) {
                meltingCrawlGraceTicks = 4;
                beginMeltingCrawlTransition(whitesnake, direction);
                return;
            }
        }

        if (meltingCrawlGraceTicks > 0) {
            meltingCrawlGraceTicks--;
        } else {
            setMeltingGravityClient(whitesnake, Direction.DOWN);
        }
    }

    private void beginMeltingCrawlTransition(WhitesnakeEntity whitesnake, Direction direction) {
        meltingCrawlTransitionTicks = 4;
        whitesnake.resetFallDistance();
        setMeltingGravityClient(whitesnake, direction);
    }

    private Vec3 getMeltingMovementVector(WhitesnakeEntity whitesnake, KeyboardPilotInput input,
                                         Direction gravity) {
        float radians = whitesnake.getYRot() * Mth.DEG_TO_RAD;
        float sin = Mth.sin(radians);
        float cos = Mth.cos(radians);
        Vec3 localMovement = new Vec3(
                input.leftImpulse * cos - input.forwardImpulse * sin,
                0.0D,
                input.forwardImpulse * cos + input.leftImpulse * sin);
        return RotationUtil.vecPlayerToWorld(localMovement, gravity);
    }

    private boolean touchesMeltingCrawlSurface(WhitesnakeEntity whitesnake, Direction direction) {
        return touchesMeltingCrawlSurface(whitesnake, direction, Vec3.ZERO);
    }

    private boolean touchesMeltingCrawlSurface(WhitesnakeEntity whitesnake, Direction direction, Vec3 offset) {
        Vec3 probe = new Vec3(direction.step()).scale(0.18D);
        return !whitesnake.level().noCollision(whitesnake,
                whitesnake.getBoundingBox().deflate(0.03D).move(offset).move(probe));
    }

    private void setMeltingGravityClient(WhitesnakeEntity whitesnake, Direction direction) {
        IGravityEntity gravityEntity = (IGravityEntity) whitesnake;
        Direction oldGravity = gravityEntity.roundabout$getGravityDirection();
        if (oldGravity == direction) return;
        gravityEntity.roundabout$setGravityDirection(direction);
        WhitesnakeControlClient.rotateLookForGravityChange(whitesnake, oldGravity, direction);
        int encoded = MainUtil.getIntFromDirection(direction);
        tryIntPower(MELTING_GRAVITY, true, encoded);
        tryIntPowerPacket(MELTING_GRAVITY, encoded);
    }

    @Override
    public int getPilotPlaceRange() {
        return PILOT_INTERACTION_RANGE;
    }

    @Override
    public boolean canUseMiningStand() {
        if (autoMode) return false;
        return isPiloting() ? isMiningStand() : super.canUseMiningStand();
    }

    @Override
    public boolean pilotInputInteract() {
        if (!self.level().isClientSide()) return true;
        if (self instanceof Player player && self.getMainHandItem().getItem() instanceof MemoryDiscItem
                && Minecraft.getInstance().options.keyShift.isDown()) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.gameMode != null) minecraft.gameMode.useItem(player, InteractionHand.MAIN_HAND);
            return true;
        }
        if (self instanceof Player player && self.getMainHandItem().getItem() instanceof CommandDiscItem
                && Minecraft.getInstance().options.keyShift.isDown()) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.gameMode != null) minecraft.gameMode.useItem(player, InteractionHand.MAIN_HAND);
            return true;
        }
        if (self instanceof Player player && self.getMainHandItem().getItem() instanceof FirearmItem) {
            if (Minecraft.getInstance().options.keyShift.isDown()) {
                C2SPacketUtil.whitesnakeGunReload();
                return true;
            }
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.gameMode != null) minecraft.gameMode.useItem(player, InteractionHand.MAIN_HAND);
            return true;
        }
        if (tryControlBlockInteractClient()) return true;
        return preCheckButtonInputGuard(true, Minecraft.getInstance().options);
    }

    private boolean tryControlBlockInteractClient() {
        if (!isPiloting()) return false;
        LivingEntity stand = getPilotingStand();
        if (!(stand instanceof WhitesnakeEntity)) return false;
        Vec3 eye = stand.getEyePosition(1.0F);
        BlockHitResult hit = self.level().clip(new ClipContext(eye,
                eye.add(stand.getViewVector(1.0F).scale(5.0D)),
                ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, stand));
        if (hit.getType() != HitResult.Type.BLOCK
                || !isControlInteractable(hit.getBlockPos())) return false;
        tryBlockPosPower(CONTROL_INTERACT, true, hit.getBlockPos(), hit);
        tryBlockPosPowerPacket(CONTROL_INTERACT, hit.getBlockPos(), hit);
        return true;
    }

    private boolean isControlInteractable(BlockPos pos) {
        BlockState state = self.level().getBlockState(pos);
        Object blockEntity = state.hasBlockEntity() ? self.level().getBlockEntity(pos) : null;
        return blockEntity instanceof MenuProvider || blockEntity instanceof Container
                || state.getBlock() instanceof LeverBlock || state.getBlock() instanceof ButtonBlock
                || state.getBlock() instanceof DoorBlock || state.getBlock() instanceof TrapDoorBlock
                || state.getBlock() instanceof BedBlock || state.getBlock() instanceof FenceGateBlock;
    }

    private LivingEntity actionOrigin() {
        StandEntity stand = getStandEntity(self);
        return hasDetachedStand() && isUsableStand(stand) ? stand : self;
    }

    public void tryToDashClient() {
        if (hasBlock()) {
            return;
        }
        if (!doVault()) {
            dash();
        }
    }

    private void impaleClient() {
        if (!canImpale()) {
            return;
        }
        if (hasBlock() || hasEntity()) {
            return;
        }
        if (!onCooldown(PowerIndex.SKILL_1_SNEAK) && canExecuteMoveWithLevel(getImpaleLevel())) {
            byte move = activePower == PowerIndex.POWER_1_SNEAK ? PowerIndex.NONE : PowerIndex.POWER_1_SNEAK;
            ((StandUser) self).roundabout$tryPower(move, true);
            tryPowerPacket(move);
        }
    }

    // Forward Barrage
    private boolean clientForwardBarrage() {
        if (!isBarrageAttacking()) {
            return false;
        }
        if (attackTimeDuring >= 0 && !forwardBarrage) {
            forwardBarrage = true;
            C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_BYTE_FORWARD_BARRAGE);
        }
        return true;
    }

    // Phase Grab
    private void phaseGrabClient() {
        if (hasBlock() || hasEntity() || activePower == PowerIndex.POWER_2_BLOCK) {
            return;
        }
        if (!onCooldown(PHASE_GRAB_COOLDOWN)) {
            ((StandUser) self).roundabout$tryPower(PowerIndex.POWER_2_BLOCK, true);
            tryPowerPacket(PowerIndex.POWER_2_BLOCK);
        }
    }

    @Override
    public void powerActivate(PowerContext context) {
        if (autoMode) {
            switch (context) {
                case SKILL_1_NORMAL, SKILL_1_CROUCH, SKILL_1_GUARD, SKILL_1_CROUCH_GUARD ->
                        autoModeMoveClient();
                case SKILL_2_NORMAL, SKILL_2_CROUCH, SKILL_2_GUARD, SKILL_2_CROUCH_GUARD ->
                        autoModeAttackClient();
                case SKILL_3_NORMAL -> tryToDashClient();
                case SKILL_4_NORMAL, SKILL_4_GUARD -> toggleAutoModeClient();
                case SKILL_4_CROUCH, SKILL_4_CROUCH_GUARD -> enterControlFromAutoClient();
            }
            return;
        }
        switch (context) {
            case SKILL_1_NORMAL -> {
                if (!meltingMode && !clientForwardBarrage()) discStealClient();
            }
            case SKILL_1_CROUCH -> {
                if (!meltingMode && canExecuteMoveWithLevel(getDiscStealLevel())) {
                    ClientUtil.openDiscStealScreen();
                }
            }
            case SKILL_1_GUARD, SKILL_1_CROUCH_GUARD -> {
                if (!isPiloting()) {
                    onReleaseGuard();
                    tryIntPower(WHITESNAKE_INVENTORY, true, 0);
                    tryIntPowerPacket(WHITESNAKE_INVENTORY, 0);
                }
            }
            case SKILL_2_NORMAL -> {
                if (isThrowableDisc(self.getMainHandItem())) grabHeldDiscClient();
                else acidTossClient();
            }
            case SKILL_2_CROUCH -> {
                if (!meltingMode && !this.isBarrageAttacking()) {
                    impaleClient();
                }
            }
            case SKILL_2_GUARD, SKILL_2_CROUCH_GUARD -> {
                if (isPiloting() && canExecuteMoveWithLevel(getMeltingModeLevel())) toggleMeltingModeClient();
                else if (!isPiloting()) phaseGrabClient();
            }
            case SKILL_3_NORMAL -> {
                if (isPiloting()) controlDashClient();
                else tryToDashClient();
            }
            case SKILL_3_CROUCH -> {
                if (!meltingMode) timeSparkClient();
            }
            case SKILL_4_GUARD, SKILL_4_CROUCH_GUARD -> {
                if (isPiloting() && canExecuteMoveWithLevel(getHallucinatoryDisguiseLevel())
                        && !onCooldown(PowerIndex.SKILL_4)) {
                    ClientUtil.openWhitesnakeDisguiseScreen();
                } else if (!isPiloting()) {
                    toggleControlModeClient();
                }
            }
            case SKILL_4_NORMAL -> toggleControlModeClient();
            case SKILL_4_CROUCH -> toggleAutoModeClient();
        }
    }

    // Auto Mode Commands
    private void autoModeMoveClient() {
        Vec3 eye = self.getEyePosition(1.0F);
        Vec3 end = eye.add(self.getViewVector(1.0F).scale(getMaxPilotRange()));
        BlockHitResult hit = self.level().clip(new ClipContext(eye, end,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, self));
        if (hit.getType() != HitResult.Type.BLOCK) return;
        BlockPos destination = hit.getBlockPos().relative(hit.getDirection());
        tryBlockPosPower(AUTO_MODE_MOVE, true, destination);
        tryBlockPosPowerPacket(AUTO_MODE_MOVE, destination);
        self.playSound(ModSounds.JUSTICE_SELECT_EVENT, 200.0F, 1.2F);
        self.level().addParticle(ModParticles.POINTER, destination.getX() + 0.5D,
                destination.getY() + 0.5D, destination.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
    }

    private void autoModeAttackClient() {
        Entity target = MainUtil.raytraceEntityStand(self.level(), self, getMaxPilotRange());
        StandEntity stand = getStandEntity(self);
        if (!(target instanceof LivingEntity) || target.is(self) || target.is(stand)) return;
        tryIntPower(AUTO_MODE_ATTACK, true, target.getId());
        tryIntPowerPacket(AUTO_MODE_ATTACK, target.getId());
        self.playSound(ModSounds.JUSTICE_SELECT_ATTACK_EVENT, 200.0F, 1.0F);
    }

    // Disc Steal Selection
    public byte getSelectedDisc() {
        return WhitesnakeDiscUtil.isDiscStealEnabled(selectedDisc)
                ? selectedDisc : WhitesnakeDiscUtil.firstEnabledDisc();
    }

    private void setSelectedDisc(int selectedDisc) {
        byte requested = (byte) Mth.clamp(selectedDisc, WhitesnakeDiscUtil.STAND, WhitesnakeDiscUtil.HEARING);
        this.selectedDisc = WhitesnakeDiscUtil.isDiscStealEnabled(requested)
                ? requested : WhitesnakeDiscUtil.firstEnabledDisc();
    }

    // Disc Toss
    private static boolean isThrowableDisc(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof StandDiscItem || item instanceof AbstractBodyDiscItem
                || item instanceof CommandDiscItem || item instanceof RecordItem;
    }

    @Override
    public boolean canImplantMusicDisc() {
        return true;
    }

    private void grabHeldDiscClient() {
        if (isThrowableDisc(self.getMainHandItem())) itemGrabClient();
    }

    @Override
    public boolean inventoryGrab() {
        if (!(self instanceof Player player) || !WhitesnakeControlInventory.isActive(player)) {
            return super.inventoryGrab();
        }
        if (self.level().isClientSide()) {
            setAttackTimeDuring(0);
            setActivePower(PowerIndex.POWER_2);
            return true;
        }

        StandEntity stand = getStandEntity(self);
        ItemStack stack = self.getMainHandItem();
        if (!isUsableStand(stand) || !isThrowableDisc(stack)) {
            setPowerNone();
            return false;
        }

        stand.canAcquireHeldItem = true;
        stand.setHeldItem(stack.copyWithCount(1));
        playSoundIfPossible(self.level(),null, self.blockPosition(), ModSounds.BLOCK_GRAB_EVENT,
                SoundSource.PLAYERS, 1.7F, 1.3F);
        setActivePower(PowerIndex.POWER_2_SNEAK);
        setAttackTimeDuring(0);
        poseStand(OffsetIndex.FOLLOW_NOLEAN);
        animateStand(StandEntity.ITEM_GRAB);
        stack.shrink(1);
        return true;
    }

    @Override
    public boolean throwObject(ItemStack item) {
        if (!isPiloting()) return super.throwObject(item);
        LivingEntity stand = getPilotingStand();
        if (!(stand instanceof WhitesnakeEntity)) return super.throwObject(item);

        int cooldown = ClientNetworking.getAppropriateConfig().generalStandSettings.objectThrowCooldown;
        setCooldown(PowerIndex.SKILL_2, cooldown);
        Vec3 origin = new Vec3(stand.getX(), stand.getEyeY() - 0.1D, stand.getZ());
        return ThrownObjectEntity.throwAnObject(self, canSnipe(), item, getShotAccuracy(),
                getBundleAccuracy(), getThrowAngle(), getThrowAngle2(), getThrowAngle3(),
                getCanPlace(), getThrowStyleType(), stand.getXRot(), stand.getYRot(), origin,
                true, 1.0F, true);
    }

    // Acid Toss
    private void acidTossClient() {
        if (getActivePower() == ACID_TOSS) {
            tryPower(PowerIndex.NONE, true);
            tryPowerPacket(PowerIndex.NONE);
            return;
        }
        if (!canExecuteMoveWithLevel(getAcidTossLevel())
                || onCooldown(PowerIndex.SKILL_2) || !canImpale() || hasBlock() || hasEntity()
                || isGuarding()) return;
        tryPower(ACID_TOSS, true);
        tryPowerPacket(ACID_TOSS);
    }

    public boolean isMeltingMode() {
        return meltingMode;
    }

    @Override
    public boolean shouldRenderPilotingHud() {
        return !usesControlHoverMeter();
    }

    @Override
    public boolean replaceHudActively() {
        return isPiloting() && usesControlHoverMeter() || super.replaceHudActively();
    }

    @Override
    public void getReplacementHUD(GuiGraphics context, Player cameraPlayer, int screenWidth, int screenHeight,
                                  int x, boolean removeNum) {
        if (!isPiloting() || !usesControlHoverMeter()) {
            super.getReplacementHUD(context, cameraPlayer, screenWidth, screenHeight, x, removeNum);
            return;
        }
        int charge = 0;
        int maximum = 1;
        StandEntity stand = getStandEntity(self);
        if (stand instanceof WhitesnakeEntity whitesnake) {
            charge = whitesnake.getMeltingHoverCharge();
            maximum = whitesnake.getMaxMeltingHoverCharge();
        }
        int width = Mth.clamp((int) Math.ceil(182.0D * charge
                / maximum), 0, 182);
        int y = screenHeight - 29;
        context.blit(StandIcons.JOJO_ICONS, x, y, 0, 131, 182, 5);
        if (width > 0) context.blit(StandIcons.JOJO_ICONS, x, y, 0, 136, width, 5);
    }

    private void toggleMeltingModeClient() {
        int value = meltingMode ? 0 : 1;
        tryIntPower(MELTING_MODE, true, value);
        tryIntPowerPacket(MELTING_MODE, value);
    }

    private boolean usesControlHoverMeter() {
        return meltingMode;
    }

    private boolean isControlHovering() {
        return isPiloting() && getStandEntity(self) instanceof WhitesnakeEntity whitesnake
                && whitesnake.isMeltingHovering();
    }

    private void controlDashClient() {
        LivingEntity stand = actionOrigin();
        if (!isPiloting() || meltingMode || !stand.onGround() || onCooldown(PowerIndex.GLOBAL_DASH)) return;
        Options options = Minecraft.getInstance().options;
        int forward = 0;
        int strafe = 0;
        if (options.keyUp.isDown()) forward++;
        if (options.keyDown.isDown()) forward--;
        if (options.keyLeft.isDown()) strafe++;
        if (options.keyRight.isDown()) strafe--;
        int direction = 0;
        if (strafe > 0 && forward == 0) direction = 1;
        else if (strafe > 0 && forward > 0) direction = 2;
        else if (strafe > 0) direction = -1;
        else if (strafe < 0 && forward == 0) direction = 3;
        else if (strafe < 0 && forward > 0) direction = 4;
        else if (strafe < 0) direction = -2;
        else if (forward < 0) direction = -3;
        if (options.keyJump.isDown()) direction += 1000;
        tryIntPower(CONTROL_DASH, true, direction);
        tryIntPowerPacket(CONTROL_DASH, direction);
    }

    private boolean controlDash(int encodedDirection) {
        if (!isPiloting() || meltingMode) return false;
        LivingEntity stand = actionOrigin();
        if (!stand.onGround() || onCooldown(PowerIndex.GLOBAL_DASH)) return false;
        boolean jumping = encodedDirection > 500;
        int direction = jumping ? encodedDirection - 1000 : encodedDirection;
        int offset = switch (direction) {
            case 1 -> -90;
            case 2 -> -45;
            case -1 -> -135;
            case 3 -> 90;
            case 4 -> 45;
            case -2 -> 135;
            case -3 -> 180;
            default -> 0;
        };
        int degrees = ((int) stand.getYRot() + offset) % 360;
        int cooldown = jumping
                ? ClientNetworking.getAppropriateConfig().generalStandSettings.jumpingDashCooldown
                : ClientNetworking.getAppropriateConfig().generalStandSettings.dashCooldown;
        setCooldown(PowerIndex.GLOBAL_DASH, cooldown);
        MainUtil.takeUnresistableKnockbackWithY(stand, 0.91F,
                Mth.sin(degrees * ((float) Math.PI / 180)),
                Mth.sin(-20 * ((float) Math.PI / 180)),
                -Mth.cos(degrees * ((float) Math.PI / 180)));
        if (!self.level().isClientSide()) {
            playSoundIfPossible(self.level(),null, stand.blockPosition(), ModSounds.DODGE_EVENT,
                    SoundSource.PLAYERS, 1.5F, (float) (0.98 + Math.random() * 0.04));
            if (self instanceof ServerPlayer player) {
                S2CPacketUtil.sendCooldownSyncPacket(player, PowerIndex.GLOBAL_DASH, cooldown);
            }
        }
        return true;
    }

    private boolean startAcidToss() {
        if (!canExecuteMoveWithLevel(getAcidTossLevel())) return false;
        StandEntity stand = getStandEntity(self);
        if (!isUsableStand(stand)) return false;
        airTriggered = false;
        setAttackTimeDuring(0);
        setActivePower(ACID_TOSS);
        playSoundsIfNearby(ACID_CHARGE_NOISE, 27, false);
        animateStand(WhitesnakeEntity.ACID_TOSS);
        poseStand(OffsetIndex.FOLLOW);
        return true;
    }

    public boolean isAcidTossActive() {
        return getActivePower() == ACID_TOSS;
    }

    private void updateAcidToss() {
        if (attackTimeDuring > 24) launchAcidToss();
    }

    private void launchAcidToss() {
        setAttackTimeDuring(-20);
        applyAcidTossCooldown();
        if (!self.level().isClientSide()) {
            LivingEntity origin = isPiloting() ? actionOrigin() : self;
            HallucinatoryAcidProjectile projectile = new HallucinatoryAcidProjectile(self, self.level());
            projectile.setPos(origin.getX(), origin.getEyeY() - 0.1D, origin.getZ());
            projectile.shootFromRotation(origin, origin.getXRot(), origin.getYRot(), -7.0F, 0.6F, 1.0F);
            self.level().addFreshEntity(projectile);
            playSoundIfPossible(self.level(),null, origin.blockPosition(), ModSounds.BLOCK_THROW_EVENT,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
            if (self instanceof ServerPlayer player) {
                S2CPacketUtil.sendGenericIntToClientPacket(player, PacketDataIndex.S2C_INT_ATD, -20);
            }
        }
    }

    private void applyAcidTossCooldown() {
        setCooldown(PowerIndex.SKILL_2, ACID_TOSS_COOLDOWN_TICKS);
        if (self instanceof ServerPlayer player) {
            S2CPacketUtil.sendCooldownSyncPacket(player, PowerIndex.SKILL_2, ACID_TOSS_COOLDOWN_TICKS);
        }
    }

    // Time Spark
    private void timeSparkClient() {
        if (!canExecuteMoveWithLevel(getTimeSparkLevel()) || !canAttack()
                || onCooldown(TIME_SPARK_COOLDOWN)) return;
        Entity target = rayCastEntity(actionOrigin(), 1.0F);
        if (target instanceof LivingEntity) {
            tryIntPower(TIME_SPARK, true, target.getId());
            tryIntPowerPacket(TIME_SPARK, target.getId());
            return;
        }
        LivingEntity origin = actionOrigin();
        Vec3 eye = origin.getEyePosition(1.0F);
        BlockHitResult hit = self.level().clip(new ClipContext(eye,
                eye.add(origin.getViewVector(1.0F).scale(2.0D)),
                ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, origin));
        if (hit.getType() != HitResult.Type.BLOCK) return;
        BlockPos pos = hit.getBlockPos();
        if (!(self.level().getBlockState(pos).getBlock() instanceof CropBlock)) return;
        tryBlockPosPower(TIME_SPARK, true, pos);
        tryBlockPosPowerPacket(TIME_SPARK, pos);
    }

    private boolean useTimeSpark(int targetId) {
        if (!canExecuteMoveWithLevel(getTimeSparkLevel())) return false;
        LivingEntity origin = actionOrigin();
        Entity target = self.level().getEntity(targetId);
        if (!(target instanceof LivingEntity living) || target.is(self) || target.is(origin)
                || !target.isAlive() || origin.distanceToSqr(target) > 4.0D
                || !origin.hasLineOfSight(target)) return false;
        if (!self.level().isClientSide()) {
            if (living instanceof Mob mob) OldEffect.makeAdult(mob);
            living.addEffect(new MobEffectInstance(ModEffects.OLD, 60, 0), self);
        }
        completeTimeSpark();
        return true;
    }

    private boolean useTimeSparkCrop(BlockPos pos) {
        if (!canExecuteMoveWithLevel(getTimeSparkLevel())) return false;
        LivingEntity origin = actionOrigin();
        if (origin.distanceToSqr(Vec3.atCenterOf(pos)) > 4.0D) return false;
        BlockState state = self.level().getBlockState(pos);
        if (!(state.getBlock() instanceof CropBlock growable)
                || !growable.isValidBonemealTarget(self.level(), pos, state, false)) return false;
        if (self.level() instanceof ServerLevel) {
            timeSparkCropPos = pos.immutable();
            timeSparkCropTicks = 60;
        }
        completeTimeSpark();
        return true;
    }

    private void tickTimeSparkCropGrowth() {
        if (timeSparkCropTicks <= 0 || timeSparkCropPos == null
                || !(self.level() instanceof ServerLevel serverLevel)) return;
        timeSparkCropTicks--;
        BlockState state = serverLevel.getBlockState(timeSparkCropPos);
        if (!(state.getBlock() instanceof CropBlock crop) || crop.isMaxAge(state)) {
            timeSparkCropTicks = 0;
            timeSparkCropPos = null;
            return;
        }
        if (timeSparkCropTicks % 10 == 0) {
            int age = Math.min(crop.getMaxAge(), crop.getAge(state) + 1);
            serverLevel.setBlock(timeSparkCropPos, crop.getStateForAge(age), 3);
            serverLevel.levelEvent(1505, timeSparkCropPos, 0);
        }
        if (timeSparkCropTicks == 0) timeSparkCropPos = null;
    }

    private void completeTimeSpark() {
        int cooldown = ClientNetworking.getAppropriateConfig().whitesnakeSettings.timeSparkCooldown;
        setCooldown(TIME_SPARK_COOLDOWN, cooldown);
        if (self instanceof ServerPlayer player) {
            S2CPacketUtil.sendCooldownSyncPacket(player, TIME_SPARK_COOLDOWN, cooldown);
            LivingEntity origin = actionOrigin();
            playSoundIfPossible(self.level(),null, origin.blockPosition(), ModSounds.WHITESNAKE_TIME_SPARK_EVENT,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    // Disc Steal
    private void discStealClient() {
        if (!meltingMode && canExecuteMoveWithLevel(getDiscStealLevel()) && !onCooldown(PowerIndex.SKILL_1)
                && canImpale() && !hasBlock() && !hasEntity()) {
            if (this.activePower == DISC_STEAL) {
                tryPower(PowerIndex.NONE, true);
                tryPowerPacket(PowerIndex.NONE);
            } else {
                tryPower(DISC_STEAL, true);
                tryPowerPacket(DISC_STEAL);
            }
        }
    }

    private boolean startDiscSteal() {
        if (meltingMode || getSelectedDisc() < 0 || !canExecuteMoveWithLevel(getDiscStealLevel())) return false;
        StandEntity stand = getStandEntity(this.self);
        if (stand == null) return false;
        this.airTriggered = false;
        this.setAttackTimeDuring(0);
        this.setActivePower(DISC_STEAL);
        playSoundsIfNearby(DISC_STEAL_CHARGE_NOISE, 27, false);
        animateStand(WhitesnakeEntity.DISC_STEAL_WINDUP);
        poseStand(OffsetIndex.GUARD);
        return true;
    }

    private void updateDiscSteal() {
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring == 20) {
                animateStand(WhitesnakeEntity.DISC_STEAL_RELEASE);
            } else if (this.attackTimeDuring > 23) {
                launchDiscSteal();
            }
        }
    }

    private void launchDiscSteal() {
        if (this.self instanceof Player) {
            if (isPacketPlayer()) {
                setAttackTimeDuring(-20);
                impaleTicks = 15;
                int targetId = getTargetEntityId2(impaleRange, actionOrigin(), 50);
                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK, targetId);
            }
        } else {
            discStealImpact(getTargetEntity(actionOrigin(), impaleRange));
        }
    }

    private void discStealImpact(Entity entity) {
        if (activePower != DISC_STEAL) return;
        setAttackTimeDuring(-20);
        LivingEntity origin = actionOrigin();
        boolean successfulHit = false;
        if (entity != null && entity.distanceTo(origin) > impaleRange + 0.75F) entity = null;
        if (entity != null) {
            if (!self.level().isClientSide()) {
                playSoundIfPossible(self.level(),null, entity.blockPosition(),
                    ModSounds.KING_CRIMSON_PUNCH_4_EVENT,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
            }
            hitParticles(entity);
            boolean dealsDamage = ClientNetworking.getAppropriateConfig().whitesnakeSettings.discStealDealsDamage;
            boolean hit = dealsDamage ? damageWithDiscSteal(entity) : canApplyDiscSteal(entity);
            if (hit) {
                successfulHit = true;
                if (entity instanceof LivingEntity living) {
                    addEXP(5, living);
                    WhitesnakeDiscUtil.ejectDisc(living, getSelectedDisc(), self);
                }
                if (dealsDamage) takeDeterminedKnockback(origin, entity, getImpaleKnockback());
            } else {
                knockShield2(entity, 100);
            }
        }
        applyDiscStealCooldown(successfulHit);
        if (entity == null && !self.level().isClientSide()) {
            playSoundIfPossible(self.level(),null, self.blockPosition(), ModSounds.PUNCH_2_SOUND_EVENT,
                    SoundSource.PLAYERS, 0.95F, 1.0F);
        }
    }

    @Override
    public SimpleParticleType getImpactParticle() {
        return getActivePower() == DISC_STEAL ? ModParticles.DISC_STEAL_HIT : super.getImpactParticle();
    }

    private boolean damageWithDiscSteal(Entity entity) {
        float power;
        if (getReducedDamage(entity)) {
            power = levelupDamageMod(multiplyPowerByStandConfigPlayers(1.5F));
        } else {
            power = levelupDamageMod(multiplyPowerByStandConfigMobs(8.5F));
        }
        return nonlethalStandAttack(entity, power);
    }

    private boolean nonlethalStandAttack(Entity entity, float power) {
        if (entity instanceof LivingEntity living) {
            power = Math.min(power, Math.max(0.0F, living.getHealth() - 1.0F));
        }
        boolean hit = StandDamageEntityAttack(entity, power, 0, self);
        if (hit && entity instanceof LivingEntity living && living.isAlive() && living.getHealth() < 1.0F) {
            living.setHealth(1.0F);
        }
        return hit;
    }

    private boolean canApplyDiscSteal(Entity entity) {
        if (!(entity instanceof LivingEntity living) || !living.isAlive()) return false;
        DamageSource source = ModDamageTypes.of(entity.level(), ModDamageTypes.STAND, self);
        return !living.isInvulnerableTo(source) && !living.isDamageSourceBlocked(source);
    }

    private void applyDiscStealCooldown(boolean successfulHit) {
        int cooldown = successfulHit ? DISC_STEAL_COOLDOWN : DISC_STEAL_MISS_COOLDOWN;
        if (self instanceof ServerPlayer player) {
            S2CPacketUtil.sendCooldownSyncPacket(player, PowerIndex.SKILL_1, cooldown);
        }
        setCooldown(PowerIndex.SKILL_1, cooldown);
    }

    private int getMeltingModeLevel() {
        return 2;
    }

    public int getHallucinatoryDisguiseLevel() {
        return 3;
    }

    private int getAcidTossLevel() {
        return 4;
    }

    public int getImpaleLevel() {
        return 6;
    }

    private int getDiscStealLevel() {
        return 5;
    }

    private int getTimeSparkLevel() {
        return 7;
    }

    @Override
    public byte getMaxLevel() {
        return 7;
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.VAULT) {
            return vault();
        } else if (move == DISC_STEAL) {
            return startDiscSteal();
        } else if (move == ACID_TOSS) {
            return startAcidToss();
        } else if (move == PowerIndex.POWER_1_SNEAK) {
            return impale();
        } else if (move == PowerIndex.POWER_2_BLOCK) {
            return phaseGrab();
        } else if (move == PowerIndex.SNEAK_ATTACK_CHARGE) {
            return setPowerFinalAttack();
        } else if (move == PowerIndex.SNEAK_ATTACK) {
            return setPowerSuperHit();
        }
        return super.setPowerOther(move, lastMove);
    }

    @Override
    public boolean tryIntPower(int move, boolean forced, int value) {
        if (move == PowerIndex.SNEAK_ATTACK) chargedFinal = value;
        if (meltingMode && (move == TIME_SPARK || move == DISC_SELECTION)) return false;
        if (move == TIME_SPARK) return useTimeSpark(value);
        if (move == ENTER_CONTROL_MODE) return enterControlModeAtCurrentPosition(value, false);
        if (move == CONTROL_MODE_FROM_AUTO) return enterControlModeAtCurrentPosition(value, true);
        if (move == AUTO_MODE_ATTACK) {
            if (!autoMode) return false;
            Entity target = self.level().getEntity(value);
            StandEntity stand = getStandEntity(self);
            double range = getMaxPilotRange() + 2.0D;
            if (!(target instanceof LivingEntity living) || !living.isAlive() || living.isRemoved()
                    || target.is(self) || target.is(stand) || self.distanceToSqr(target) > range * range) return false;
            autoMoveTarget = null;
            manualAutoTargetId = target.getId();
            if (!self.level().isClientSide()) self.setLastHurtMob(null);
            return true;
        }
        if (move == AUTO_MODE) {
            setAutoMode(value != 0);
            return value == 0 || autoMode;
        }
        if (move == MELTING_MODE) {
            if (value != 0 && !canExecuteMoveWithLevel(getMeltingModeLevel())) return false;
            setMeltingMode(value != 0, true);
            return true;
        }
        if (move == MELTING_HOVER) {
            if (!(getStandEntity(self) instanceof WhitesnakeEntity whitesnake)) return false;
            boolean hovering = value != 0 && meltingMode && isPiloting()
                    && whitesnake.getMeltingHoverCharge() > 0;
            whitesnake.setMeltingHovering(hovering);
            return true;
        }
        if (move == MELTING_GRAVITY) {
            if (!(getStandEntity(self) instanceof WhitesnakeEntity whitesnake)) return false;
            Direction direction = MainUtil.getDirectionFromInt(Mth.clamp(value, 0, 5));
            if (direction != Direction.DOWN && (!meltingMode || !isPiloting() || whitesnake.isMeltingHovering())) {
                return false;
            }
            ((IGravityEntity) whitesnake).roundabout$setGravityDirection(direction);
            return true;
        }
        if (move == CONTROL_DASH) return controlDash(value);
        if (move == WHITESNAKE_INVENTORY) {
            if (isPiloting()) return false;
            if (!isClient() && self instanceof ServerPlayer player) {
                if (isGuarding()) tryPower(PowerIndex.NONE, true);
                WhitesnakeInventoryMenu.open(player);
            }
            return true;
        }
        if (move == DISC_SELECTION) {
            setSelectedDisc(value);
            if (!isClient() && self instanceof Player) saveDiscAndSync();
        }
        return super.tryIntPower(move, forced, value);
    }

    private boolean enterControlModeAtCurrentPosition(int standId, boolean requireAutoMode) {
        StandEntity stand = getStandEntity(self);
        if ((requireAutoMode && !autoMode) || stand == null || stand.getId() != standId) return false;
        Vec3 position = stand.position();
        float yaw = stand.getYRot();
        float pitch = stand.getXRot();
        setPiloting(standId);
        restoreStandTransform(stand, position, yaw, pitch);
        return isPiloting();
    }

    @Override
    public void updatePowerInt(byte activePower, int data) {
        if (activePower == AUTO_MODE) {
            setAutoMode(data != 0);
            return;
        }
        if (activePower == ENTER_CONTROL_MODE && data == 0) {
            setPiloting(0);
            if (self.level().isClientSide()) WhitesnakeControlClient.exit();
            return;
        }
        super.updatePowerInt(activePower, data);
    }

    @Override
    public void updateUniqueMoves() {
        if (getActivePower() == DISC_STEAL) updateDiscSteal();
        else if (getActivePower() == ACID_TOSS) updateAcidToss();
        else if (getActivePower() == PowerIndex.POWER_1_SNEAK) updateImpale();
        else if (getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) updateFinalAttackCharge();
        else if (getActivePower() == PowerIndex.SNEAK_ATTACK) updateFinalAttack();
        super.updateUniqueMoves();
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        StandEntity stand = getStandEntity(self);
        if (move != PowerIndex.POWER_2_BLOCK && stand != null) stand.setFadePercent(100);
        if (moveStarted) moveStarted = false;
        if (move == PowerIndex.BARRAGE_CHARGE_2 || move == PowerIndex.BARRAGE_2) return false;
        if (isControlHovering() && isHoverRestrictedPower(move)) return false;
        if (meltingMode && isMeltingRestrictedPower(move)) return false;
        if (!self.level().isClientSide() && getActivePower() == ACID_TOSS && move != ACID_TOSS) {
            stopSoundsIfNearby(ACID_CHARGE_NOISE, 100, false);
        }
        return super.tryPower(move, forced);
    }

    private static boolean isHoverRestrictedPower(int move) {
        return switch (move) {
            case PowerIndex.ATTACK, PowerIndex.BARRAGE_CHARGE, PowerIndex.BARRAGE,
                    PowerIndex.SNEAK_ATTACK_CHARGE, PowerIndex.SNEAK_ATTACK,
                    PowerIndex.POWER_1_SNEAK, DISC_STEAL, ACID_TOSS -> true;
            default -> false;
        };
    }

    private static boolean isMeltingRestrictedPower(int move) {
        return switch (move) {
            case PowerIndex.ATTACK, DISC_STEAL,
                    PowerIndex.BARRAGE_CHARGE, PowerIndex.BARRAGE,
                    PowerIndex.SNEAK_ATTACK_CHARGE, PowerIndex.SNEAK_ATTACK,
                    PowerIndex.POWER_1_SNEAK -> true;
            default -> false;
        };
    }

    @Override
    public void tickPower() {
        if (self instanceof Player player && self.level().isClientSide() && isPacketPlayer()) {
            int controlledId = ((IPlayerEntity) player).roundabout$getControlling();
            Entity controlled = self.level().getEntity(controlledId);
            if (controlled instanceof LivingEntity living && living.isAlive() && !living.isRemoved()) {
                if (!isWithinRemoteRange(living)) {
                    exitControlModeClient();
                } else {
                    WhitesnakeControlClient.enforceCamera(living);
                }
            } else if (controlledId != 0) {
                exitControlModeClient();
            } else {
                WhitesnakeControlClient.exitIfInactive();
            }
        }
        if (!self.level().isClientSide()) {
            tickTimeSparkCropGrowth();
            tickControlModeServer();
            if (autoMode) tickAutoMode();
        }
        if (forwardBarrage && !isBarrageAttacking()) forwardBarrage = false;
        super.tickPower();
    }

    @Override
    public void tickPowerEnd() {
        super.tickPowerEnd();
        if (!self.isAlive() || self.isRemoved()) return;
        if (forwardBarrage && attackTimeDuring >= 0 && isBarrageAttacking()) {
            tickForwardBarrage();
        } else if (activePower == PowerIndex.POWER_2_BLOCK) {
            tickPhaseGrab();
        }
    }

    private void tickForwardBarrage() {
        if (self.level().isClientSide()) return;
        StandEntity stand = getStandEntity(self);
        if (stand == null) return;
        double speed = moveStarted ? 0.12D : 0.0075D;
        stand.setPos(stand.getPosition(1).add(stand.getForward().scale(speed)));
        if (stand.isTechnicallyInWall()
                || stand.position().distanceTo(self.position()) > FORWARD_BARRAGE_RANGE) {
            ((StandUser) self).roundabout$tryPower(PowerIndex.NONE, true);
        }
    }

    private void tickPhaseGrab() {
        if (self.level().isClientSide()) return;
        if (attackTimeDuring == 108) {
            ((StandUser) self).roundabout$tryPower(PowerIndex.NONE, true);
            return;
        }
        if (attackTimeDuring < 0) return;
        StandEntity stand = getStandEntity(self);
        if (stand == null) return;

        AABB oldBounds = stand.getBoundingBox();
        Vec3 destination = self.getEyePosition(0).add(self.getViewVector(0).scale(20.0D));
        phaseGrabOffset = phaseGrabOffset.add(
                destination.subtract(self.position().add(phaseGrabOffset)).normalize().scale(0.18D));
        Vec3 nextPosition = self.position().add(phaseGrabOffset);
        double distance = stand.position().distanceTo(destination);
        if (distance < 1.5D) {
            stand.setYRot(self.getYHeadRot() % 360.0F);
            stand.setXRot(self.getXRot());
        } else {
            Direction gravity = ((IGravityEntity) self).roundabout$getGravityDirection();
            Vec2 rotation = RotationUtil.rotWorldToPlayer(new Vec2(
                    getLookAtPlaceYaw(stand, destination), getLookAtPlacePitch(stand, destination)), gravity);
            stand.setYRot(rotation.x);
            stand.setXRot(rotation.y);
        }
        stand.setPos(distance < 0.4D ? destination : nextPosition);

        if (stand.isTechnicallyInImpassableWall() || stand.position().distanceTo(self.position()) > 15.0D) {
            if (self instanceof ServerPlayer player) {
                S2CPacketUtil.sendCooldownSyncPacket(player, PHASE_GRAB_COOLDOWN, 7);
            }
            setCooldown(PHASE_GRAB_COOLDOWN, 5);
            ((StandUser) self).roundabout$tryPower(PowerIndex.NONE, true);
            return;
        }

        usePhaseGrabRedstone(stand);
        if (attackTimeDuring > 2) tryPhaseItemGrab(stand, oldBounds, stand.getBoundingBox());
    }

    private void usePhaseGrabRedstone(StandEntity stand) {
        if (!self.level().getGameRules().getBoolean(ModGamerules.ROUNDABOUT_STAND_REDSTONE_INTERFERENCE)) return;
        Vec3 eye = stand.getEyePosition(0);
        BlockHitResult hit = stand.level().clip(new ClipContext(eye, eye.add(stand.getViewVector(0).scale(3.0D)),
                ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, stand));
        BlockPos pos = hit.getBlockPos();
        BlockState state = stand.level().getBlockState(pos);
        if (state.isAir() || !(self instanceof Player player)) return;
        if (state.getBlock() instanceof LeverBlock || state.getBlock() instanceof ButtonBlock
                || state.getBlock() instanceof DoorBlock || state.getBlock() instanceof TrapDoorBlock
                || state.getBlock() instanceof FenceGateBlock) {
            state.getBlock().use(state, self.level(), pos, player, player.getUsedItemHand(), hit);
            ((StandUser) self).roundabout$tryPower(PowerIndex.NONE, true);
        }
    }

    private void tryPhaseItemGrab(StandEntity stand, AABB oldBounds, AABB newBounds) {
        AABB searchBounds = oldBounds.inflate(1.6D).minmax(newBounds.inflate(1.6D));
        for (Entity entity : stand.level().getEntities(stand, searchBounds)) {
            if (!(entity instanceof ItemEntity itemEntity) || !stand.getSensing().hasLineOfSight(entity)) continue;
            ItemStack stack = itemEntity.getItem();
            if (!isThrowableDisc(stack)) continue;
            stand.canAcquireHeldItem = true;
            stand.setHeldItem(stack.copyWithCount(1));
            playSoundIfPossible(self.level(),null, self.blockPosition(), ModSounds.BLOCK_GRAB_EVENT,
                    SoundSource.PLAYERS, 1.7F, 1.3F);
            setActivePower(PowerIndex.POWER_2_SNEAK);
            setAttackTimeDuring(0);
            poseStand(OffsetIndex.FOLLOW_NOLEAN);
            animateStand(MainUtil.isThrownBlockItem(stack.getItem())
                    ? StandEntity.BLOCK_GRAB : StandEntity.ITEM_GRAB);
            stack.shrink(1);
            syncGrabbedItem(itemEntity.getId());
            return;
        }
    }

    private void syncGrabbedItem(int entityId) {
        if (!(self.level() instanceof ServerLevel level)) return;
        for (ServerPlayer player : level.players()) {
            if (player.blockPosition().closerToCenterThan(self.position(), 100.0D)) {
                S2CPacketUtil.sendGenericIntToClientPacket(player, PacketDataIndex.S2C_INT_GRAB_ITEM, entityId);
            }
        }
    }

    private boolean phaseGrab() {
        StandEntity stand = getStandEntity(self);
        if (stand == null) return false;
        animateStand(StandEntity.PHASE_GRAB);
        setAttackTimeDuring(0);
        stand.setFadePercent(50);
        setActivePower(PowerIndex.POWER_2_BLOCK);
        poseStand(OffsetIndex.LOOSE);

        Vec2 playerRotation = new Vec2(self.getYHeadRot() % 360.0F, self.getXRot());
        Direction gravity = ((IGravityEntity) self).roundabout$getGravityDirection();
        Vec2 worldRotation = RotationUtil.rotPlayerToWorld(playerRotation, gravity);
        Vec3 heightOffset = RotationUtil.vecPlayerToWorld(new Vec3(0.0D, 0.25D, 0.0D), gravity);
        stand.setYRot(playerRotation.x);
        stand.setXRot(playerRotation.y);
        phaseGrabOffset = DamageHandler.getRotationVector(worldRotation.y, worldRotation.x)
                .scale(1.8D).add(heightOffset);
        stand.setPos(self.position().add(phaseGrabOffset));
        return true;
    }

    private void tickControlModeServer() {
        if (!(self instanceof ServerPlayer player)) return;
        int controlledId = ((IPlayerEntity) player).roundabout$getControlling();
        if (controlledId == 0) return;
        StandEntity stand = getStandEntity(self);
        Entity controlled = self.level().getEntity(controlledId);
        boolean valid = stand != null && controlled != null && controlled.is(stand)
                && stand.isAlive() && !stand.isRemoved()
                && ((StandUser) self).roundabout$getActive()
                && isWithinRemoteRange(stand);
        if (valid) return;
        setPiloting(0);
        S2CPacketUtil.sendIntPowerDataPacket(player, ENTER_CONTROL_MODE, 0);
    }

    // Auto Mode AI
    private void tickAutoMode() {
        if (!(getStandEntity(self) instanceof WhitesnakeEntity stand)
                || !stand.isAlive() || stand.isRemoved()
                || !((StandUser) self).roundabout$getActive()) {
            stopAutoModeServer();
            return;
        }
        if (!isWithinRemoteRange(stand)) {
            stopAutoModeServer();
            return;
        }

        if (autoMoveTarget != null) {
            stand.setTarget(null);
            double distance = stand.position().distanceTo(autoMoveTarget);
            boolean sprinting = distance > 3.0D;
            stand.setSprinting(sprinting);
            stand.setSpeed(sprinting ? 0.3F : 0.2F);
            if (distance > 1.0D) {
                stand.getNavigation().moveTo(autoMoveTarget.x, autoMoveTarget.y, autoMoveTarget.z,
                        sprinting ? 1.5D : 1.0D);
                stand.getLookControl().setLookAt(autoMoveTarget.x, autoMoveTarget.y + 1.0D,
                        autoMoveTarget.z, 30.0F, 30.0F);
                rotateAutoStandToward(stand, autoMoveTarget);
            } else {
                stand.getNavigation().stop();
                stand.setSprinting(false);
            }
            return;
        }

        LivingEntity target = getAutoAttackTarget();

        if (target == null) {
            stand.setTarget(null);
            double distance = stand.distanceTo(self);
            boolean sprinting = distance > 3.0D;
            stand.setSprinting(sprinting);
            stand.setSpeed(sprinting ? 0.3F : 0.2F);
            if (distance > 3.0D) {
                stand.getNavigation().moveTo(self, sprinting ? 1.5D : 1.0D);
                stand.getLookControl().setLookAt(self, 30.0F, 30.0F);
                rotateAutoStandToward(stand, self);
            } else {
                stand.getNavigation().stop();
                rotateAutoStandToward(stand, self);
            }
            return;
        }

        stand.setTarget(target);
        stand.getLookControl().setLookAt(target, 30.0F, 30.0F);
        rotateAutoStandToward(stand, target);
        double distance = stand.distanceTo(target);
        boolean sprinting = distance > CONTROL_PUNCH_RANGE;
        stand.setSprinting(sprinting);
        stand.setSpeed(sprinting ? 0.3F : 0.2F);
        if (distance > CONTROL_PUNCH_RANGE) {
            stand.getNavigation().moveTo(target, sprinting ? 1.5D : 1.0D);
            return;
        }

        stand.getNavigation().stop();
        if (autoAttackCooldown > 0) {
            autoAttackCooldown--;
            return;
        }
        if (stand.hasLineOfSight(target) && getActivePower() == PowerIndex.NONE) {
            float specialRoll = self.getRandom().nextFloat();
            if (!onCooldown(PowerIndex.SKILL_1_SNEAK) && canImpale()
                    && specialRoll < 0.12F) {
                tryPower(PowerIndex.POWER_1_SNEAK, true);
                autoAttackCooldown = 10;
            } else {
                tryPower(PowerIndex.ATTACK, true);
                autoAttackCooldown = 4;
            }
        }
    }

    private LivingEntity getAutoAttackTarget() {
        LivingEntity target = null;
        if (manualAutoTargetId >= 0) {
            Entity manual = self.level().getEntity(manualAutoTargetId);
            if (manual instanceof LivingEntity living && living.isAlive() && !living.isRemoved()) {
                target = living;
            } else {
                manualAutoTargetId = -1;
            }
        }
        if (target == null && manualAutoTargetId < 0) target = self.getLastHurtMob();
        StandEntity stand = getStandEntity(self);
        if (target == null || stand == null || !target.isAlive() || target.isRemoved()
                || target.level() != stand.level() || target.is(self) || target.is(stand)) {
            return null;
        }
        return target;
    }

    private void rotateAutoStandToward(WhitesnakeEntity stand, Entity target) {
        applyAutoStandRotation(stand, getLookAtEntityYaw(stand, target));
    }

    private void rotateAutoStandToward(WhitesnakeEntity stand, Vec3 target) {
        applyAutoStandRotation(stand, getLookAtPlaceYaw(stand, target));
    }

    private static void applyAutoStandRotation(WhitesnakeEntity stand, float targetYaw) {
        float bodyYaw = Mth.rotLerp(0.3F, stand.yBodyRot, targetYaw);
        float headYaw = Mth.rotLerp(0.45F, stand.getYHeadRot(), targetYaw);
        stand.setYRot(bodyYaw);
        stand.setYBodyRot(bodyYaw);
        stand.setYHeadRot(headYaw);
    }

    @Override
    public boolean isAttackInept(byte activePower) {
        if (autoMode) return autoModeAttackDisabled();
        return super.isAttackInept(activePower);
    }

    @Override
    public boolean shouldReset(byte activePower) {
        if (autoMode) return autoModeAttackDisabled();
        return super.shouldReset(activePower);
    }

    private boolean autoModeAttackDisabled() {
        return isDazed(self) || ((TimeStop) self.level()).CanTimeStopEntity(self);
    }

    @Override
    public void tickMobAI(LivingEntity attackTarget) {
        if (mobAbilityDecisionCooldown > 0) mobAbilityDecisionCooldown--;
        if (getActivePower() == PowerIndex.GUARD) {
            if (--mobGuardTicks <= 0) ((StandUser) self).roundabout$tryPower(PowerIndex.NONE, true);
            return;
        }
        if (autoMode) {
            if (attackTarget != null && attackTarget.isAlive()) manualAutoTargetId = attackTarget.getId();
            if (--mobAutoModeTicks <= 0) setAutoMode(false);
            return;
        }
        if (attackTarget == null || !attackTarget.isAlive() || isDazed(self)) return;

        double distance = attackTarget.distanceTo(self);
        if (isBarrageAttacking() && distance > 2.0D && attackTimeDuring > 4) {
            forwardBarrage = true;
        }
        if (getActivePower() != PowerIndex.NONE || distance <= 12.0D) rotateMobHead(attackTarget);
        if (getActivePower() != PowerIndex.NONE || attackTimeDuring > -1 || mobAbilityDecisionCooldown > 0) {
            return;
        }

        Entity closeTarget = getTargetEntity(self, impaleRange);
        boolean closeAndAimed = closeTarget != null && closeTarget.is(attackTarget);
        double roll = self.getRandom().nextDouble();

        if (distance <= 5.0D && roll < 0.06D) {
            ((StandUser) self).roundabout$tryPower(PowerIndex.GUARD, true);
            if (getActivePower() == PowerIndex.GUARD) {
                mobGuardTicks = 12 + self.getRandom().nextInt(13);
                mobAbilityDecisionCooldown = mobGuardTicks;
                return;
            }
        }
        if (distance <= 12.0D && roll >= 0.06D && roll < 0.07D) {
            setAutoMode(true);
            if (autoMode) {
                manualAutoTargetId = attackTarget.getId();
                mobAutoModeTicks = 120 + self.getRandom().nextInt(121);
                return;
            }
        }
        if (distance > 4.0D && distance <= 14.0D && self.onGround()
                && !onCooldown(PowerIndex.GLOBAL_DASH) && roll < 0.20D) {
            mobDashToward(attackTarget);
            mobAbilityDecisionCooldown = 24;
            return;
        }
        if (distance <= 12.0D && self.hasLineOfSight(attackTarget)
                && !onCooldown(PowerIndex.SKILL_2) && roll < 0.34D) {
            int skillTwoMove = ACID_TOSS;
            ((StandUser) self).roundabout$tryPower(skillTwoMove, true);
            if (getActivePower() == skillTwoMove) {
                mobAbilityDecisionCooldown = 50;
                return;
            }
        }
        if (closeAndAimed && !onCooldown(PowerIndex.SKILL_1) && roll < 0.46D) {
            setSelectedDisc(WhitesnakeDiscUtil.randomEnabledDisc(self.getRandom()));
            ((StandUser) self).roundabout$tryPower(DISC_STEAL, true);
            if (getActivePower() == DISC_STEAL) {
                mobAbilityDecisionCooldown = 45;
                return;
            }
        }
        if (closeAndAimed && !onCooldown(PowerIndex.SKILL_1_SNEAK) && canImpale() && roll < 0.58D) {
            ((StandUser) self).roundabout$tryPower(PowerIndex.POWER_1_SNEAK, true);
            if (getActivePower() == PowerIndex.POWER_1_SNEAK) {
                mobAbilityDecisionCooldown = 40;
                return;
            }
        }
        if (closeAndAimed && roll < 0.70D) {
            ((StandUser) self).roundabout$tryPower(PowerIndex.SNEAK_ATTACK_CHARGE, true);
            if (getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
                mobAbilityDecisionCooldown = getMaxSuperHitTime() + 25;
                return;
            }
        }
        if (closeAndAimed && roll < 0.84D) {
            ((StandUser) self).roundabout$tryPower(PowerIndex.BARRAGE_CHARGE, true);
            if (getActivePower() == PowerIndex.BARRAGE_CHARGE) {
                mobAbilityDecisionCooldown = 35;
                return;
            }
        }
        if (closeAndAimed) {
            ((StandUser) self).roundabout$tryPower(PowerIndex.ATTACK, true);
            mobAbilityDecisionCooldown = 4;
        }
    }

    private void mobDashToward(LivingEntity target) {
        float yaw = getLookAtEntityYaw(self, target);
        self.setYRot(yaw);
        self.setYHeadRot(yaw);
        setCooldown(PowerIndex.GLOBAL_DASH, 40);
        MainUtil.takeUnresistableKnockbackWithY(self, 0.91F,
                Mth.sin(yaw * Mth.DEG_TO_RAD), Mth.sin(-20.0F * Mth.DEG_TO_RAD),
                -Mth.cos(yaw * Mth.DEG_TO_RAD));
        playSoundIfPossible(self.level(),null, self.blockPosition(), ModSounds.DODGE_EVENT,
                SoundSource.PLAYERS, 1.5F, 1.0F);
    }

    @Override
    public boolean tryBlockPosPower(int move, boolean forced, BlockPos blockPos) {
        if (meltingMode && move == TIME_SPARK) return false;
        if (move == AUTO_MODE_MOVE) {
            double range = getMaxPilotRange() + 2.0D;
            if (!autoMode || self.distanceToSqr(Vec3.atCenterOf(blockPos)) > range * range) return false;
            autoMoveTarget = Vec3.atBottomCenterOf(blockPos);
            manualAutoTargetId = -1;
            if (!self.level().isClientSide()) self.setLastHurtMob(null);
            StandEntity stand = getStandEntity(self);
            if (stand != null) {
                stand.setTarget(null);
                stand.getNavigation().stop();
            }
            return true;
        }
        if (move == TIME_SPARK) return useTimeSparkCrop(blockPos);
        return super.tryBlockPosPower(move, forced, blockPos);
    }

    @Override
    public boolean tryBlockPosPower(int move, boolean forced, BlockPos blockPos, BlockHitResult blockHit) {
        if (move != CONTROL_INTERACT) return super.tryBlockPosPower(move, forced, blockPos, blockHit);
        if (!isPiloting() || !(self instanceof Player player) || blockHit == null
                || !blockPos.equals(blockHit.getBlockPos()) || !isControlInteractable(blockPos)) return false;
        LivingEntity stand = getPilotingStand();
        if (!(stand instanceof WhitesnakeEntity) || stand.getEyePosition().distanceToSqr(blockHit.getLocation()) > 36.0D) {
            return false;
        }
        Vec3 eye = stand.getEyePosition();
        BlockHitResult verified = self.level().clip(new ClipContext(eye,
                blockHit.getLocation().subtract(blockHit.getDirection().getStepX() * 0.01D,
                        blockHit.getDirection().getStepY() * 0.01D,
                        blockHit.getDirection().getStepZ() * 0.01D),
                ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, stand));
        if (verified.getType() != HitResult.Type.BLOCK
                || !verified.getBlockPos().equals(blockPos)) return false;
        self.level().getBlockState(blockPos).use(self.level(), player, InteractionHand.MAIN_HAND, blockHit);
        return true;
    }

    private void stopAutoModeServer() {
        setAutoMode(false);
        if (self instanceof ServerPlayer player) {
            S2CPacketUtil.sendIntPowerDataPacket(player, AUTO_MODE, 0);
        }
    }

    @Override
    public void onStandSwitch() {
        StandEntity stand = getStandEntity(self);
        boolean hasControlId = self instanceof Player player
                && ((IPlayerEntity) player).roundabout$getControlling() != 0;
        if (hasControlId || stand instanceof WhitesnakeEntity whitesnake && whitesnake.isControlModeActive()) {
            setPiloting(0);
            if (self.level().isClientSide()) WhitesnakeControlClient.exit();
        }
        if (autoMode) setAutoMode(false);
        super.onStandSwitch();
    }

    @Override
    public void handleStandAttack(Player player, Entity target) {
        if (getActivePower() == DISC_STEAL) discStealImpact(target);
        else if (getActivePower() == PowerIndex.POWER_1_SNEAK) impaleImpact(target);
        else if (getActivePower() == PowerIndex.SNEAK_ATTACK) finalAttackImpact(target);
        else super.handleStandAttack(player, target);
    }

    @Override
    public boolean canInterruptPower(DamageSource source, Entity interrupter) {
        if (getActivePower() == ACID_TOSS) return false;
        if (getActivePower() == PowerIndex.POWER_1_SNEAK) {
            applyImpaleCooldown();
            return true;
        }
        if (getActivePower() == DISC_STEAL) {
            applyDiscStealCooldown(false);
            return true;
        }
        return super.canInterruptPower(source, interrupter);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putByte("WhitesnakeSelectedDisc", selectedDisc);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("WhitesnakeSelectedDisc")) setSelectedDisc(tag.getByte("WhitesnakeSelectedDisc"));
    }

    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (meltingMode) {
            holdDownClick = false;
            return;
        }
        if (keyIsDown && isControlHovering()) return;
        if (consumeClickInput) {
            if (!keyIsDown) consumeClickInput = false;
            return;
        }
        if (impaleTicks > 0) return;
        if (holdDownClick) {
            if (!keyIsDown) {
                if (activePower == PowerIndex.SNEAK_ATTACK_CHARGE) {
                    int chargeTime = attackTimeDuring;
                    tryIntPower(PowerIndex.SNEAK_ATTACK, true, chargeTime);
                    tryIntPowerPacket(PowerIndex.SNEAK_ATTACK, chargeTime);
                }
                holdDownClick = false;
            }
            return;
        }
        if (!keyIsDown) return;
        if (!isHoldingSneak()) {
            super.buttonInputAttack(true, options);
        } else if (canAttack()) {
            tryPower(PowerIndex.SNEAK_ATTACK_CHARGE, true);
            tryPowerPacket(PowerIndex.SNEAK_ATTACK_CHARGE);
            holdDownClick = true;
        } else {
            super.buttonInputAttack(true, options);
        }
    }

    @Override
    public boolean buttonInputGuard(boolean keyIsDown, Options options) {
        if (activePower == PowerIndex.POWER_2_BLOCK) return false;
        return super.buttonInputGuard(keyIsDown, options);
    }

    @Override
    public boolean clickRelease() {
        return activePower == PowerIndex.POWER_2_BLOCK || super.clickRelease();
    }

    @Override
    public boolean cancelSprintJump() {
        if (activePower == PowerIndex.POWER_1_SNEAK
                || activePower == PowerIndex.SNEAK_ATTACK_CHARGE) return true;
        return super.cancelSprintJump();
    }

    @Override
    public float inputSpeedModifiers(float basis) {
        if (activePower == PowerIndex.SNEAK_ATTACK_CHARGE) {
            if (self.isCrouching()) {
                float sneakSpeed = Mth.clamp(0.3F + EnchantmentHelper.getSneakingSpeedBonus(self), 0.0F, 1.0F);
                basis /= sneakSpeed;
            }
            basis *= 0.3F;
        } else if (activePower == PowerIndex.POWER_1_SNEAK && self.isCrouching()) {
            float sneakSpeed = Mth.clamp(0.3F + EnchantmentHelper.getSneakingSpeedBonus(self), 0.0F, 1.0F);
            basis /= sneakSpeed;
        }
        return super.inputSpeedModifiers(basis);
    }

    @Override
    public void buttonInputBarrage(boolean keyIsDown, Options options) {
        if (autoMode || meltingMode || isControlHovering()) return;
        if (keyIsDown && (getAttackTime() >= getAttackTimeMax()
                || getActivePowerPhase() != getActivePowerPhaseMax())) {
            tryPower(PowerIndex.BARRAGE_CHARGE, true);
            tryPowerPacket(PowerIndex.BARRAGE_CHARGE);
        }
    }

    @Override
    public void standPunch() {
        if (!isPiloting() && !autoMode) {
            super.standPunch();
            return;
        }
        if (autoMode) {
            if (!self.level().isClientSide()) {
                LivingEntity target = getAutoAttackTarget();
                if (target != null && target.isAlive() && !target.isRemoved()
                        && target.distanceTo(actionOrigin()) <= CONTROL_PUNCH_RANGE + 0.75F
                        && actionOrigin().hasLineOfSight(target)) {
                    punchImpact(target);
                } else {
                    punchImpact(null);
                }
            }
            return;
        }
        if (self instanceof Player && isPacketPlayer()) {
            attackTimeDuring = -10;
            Entity target = getTargetEntity(actionOrigin(), CONTROL_PUNCH_RANGE, getPunchAngle());
            C2SPacketUtil.standPunchPacket(target == null ? -1 : target.getId(), activePowerPhase);
        }
    }

    @Override
    public void punchImpact(Entity entity) {
        if (!isPiloting() && !autoMode) {
            super.punchImpact(entity);
            return;
        }
        setAttackTimeDuring(-10);
        LivingEntity origin = actionOrigin();
        if (entity != null && entity.distanceTo(origin) > CONTROL_PUNCH_RANGE + 0.75F) entity = null;
        boolean lastHit = activePowerPhase >= activePowerPhaseMax;
        if (entity != null) {
            float power = lastHit ? getHeavyPunchStrength(entity) : getPunchStrength(entity);
            float knockback = lastHit ? 1.0F : 0.2F;
            if (StandDamageEntityAttack(entity, power, 0, self)) {
                if (entity instanceof LivingEntity living) addEXP(lastHit ? 2 : 1, living);
                takeDeterminedKnockback(origin, entity, knockback);
            } else if (lastHit) {
                if (entity instanceof LivingEntity living && ((StandUser) living).roundabout$getStandPowers().interceptGuard()
                        && living.isBlocking() && !((StandUser) living).roundabout$isGuarding()) {
                    knockShield2(entity, 60);
                } else {
                    knockShield2(entity, 40);
                }
            }
        } else if (!self.level().isClientSide()) {
            Vec3 point = DamageHandler.getRayPoint(origin, CONTROL_PUNCH_RANGE * 0.5F);
            sendParticlesIfPossible(self.level(),ModParticles.PUNCH_MISS,
                    point.x, point.y, point.z, 1, 0.0, 0.0, 0.0, 1);
        }
        SoundEvent sound;
        float pitch = 1.0F;
        if (lastHit) {
            if (!self.level().isClientSide()) playTheLastHitSound();
            if (entity != null) {
                sound = getPunchLandLastSound();
                pitch = getPunchLandLastPitch();
            } else {
                sound = ModSounds.PUNCH_2_SOUND_EVENT;
            }
        } else if (entity != null) {
            sound = getPunchLandSound();
            pitch = getPunchLandPitch();
        } else {
            sound = ModSounds.PUNCH_2_SOUND_EVENT;
        }
        if (!self.level().isClientSide()) {
            if (entity != null) hitParticles(entity);
            playSoundIfPossible(self.level(),null, origin.blockPosition(), sound, SoundSource.PLAYERS, 0.95F, pitch);
        }
    }

    public void standFinalAttack() {
        setAttackTimeMax(ClientNetworking.getAppropriateConfig().generalStandSettings.finalPunchAndKickMinimumCooldown
                + chargedFinal);
        setAttackTime(0);
        setActivePowerPhase(getActivePowerPhaseMax());
        if (self instanceof Player && isPacketPlayer()) {
            attackTimeDuring = -10;
            if (isPiloting()) {
                Entity target = getTargetEntity(actionOrigin(), CONTROL_PUNCH_RANGE, getPunchAngle());
                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK, target == null ? -1 : target.getId());
            } else {
                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK, getTargetEntityId());
            }
        } else if (!(self instanceof Player)) {
            finalAttackImpact(getTargetEntity(self, -1));
        }
    }

    public void standImpale() {
        if (autoMode) {
            if (!self.level().isClientSide()) impaleImpact(getAutoAttackTarget());
            return;
        }
        if (self instanceof Player && isPacketPlayer()) {
            setAttackTimeDuring(-20);
            impaleTicks = 15;
            int targetId = isPiloting()
                    ? getTargetEntityId2(impaleRange, actionOrigin(), 50)
                    : getTargetEntityId2(impaleRange);
            tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK, targetId);
        } else if (!(self instanceof Player)) {
            impaleImpact(getTargetEntity(self, impaleRange));
        }
    }

    public void updateImpale() {
        if (!self.level().isClientSide() && attackTimeDuring == 20) {
            playSoundIfPossible(self.level(),null, actionOrigin().blockPosition(), ModSounds.WHITESNAKE_IMPALE_VOICE_EVENT,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        if (attackTimeDuring > 24) {
            standImpale();
        } else if (attackTimeDuring >= 0 && !self.level().isClientSide() && attackTimeDuring % 4 == 0) {
            LivingEntity origin = actionOrigin();
            sendParticlesIfPossible(self.level(),ModParticles.MENACING,
                    origin.getX(), origin.getY() + 0.3D, origin.getZ(),
                    1, 0.2D, 0.2D, 0.2D, 0.05D);
        }
    }

    @Override
    public void impaleImpact(Entity entity) {
        if (!isPiloting() && !autoMode) {
            super.impaleImpact(entity);
            applyImpaleCooldown();
            return;
        }
        if (activePower != PowerIndex.POWER_1_SNEAK) return;
        setAttackTimeDuring(-20);
        LivingEntity origin = actionOrigin();
        if (entity != null && entity.distanceTo(origin) > impaleRange + 0.75F) entity = null;
        if (entity != null) {
            hitParticlesCenter(entity);
            if (StandDamageEntityAttack(entity, getImpalePunchStrength(entity), 0, self)) {
                if (entity instanceof LivingEntity living) {
                    addEXP(5, living);
                    if (MainUtil.getMobBleed(entity) && !airTriggered) {
                        if (((TimeStop) self.level()).CanTimeStopEntity(entity)) {
                            MainUtil.makeBleed(entity, 0, 200, self);
                        } else {
                            MainUtil.makeBleed(entity, 2, 200, self);
                        }
                        MainUtil.makeMobBleed(entity);
                    }
                }
                takeDeterminedKnockback(origin, entity, getImpaleKnockback());
            } else {
                knockShield2(entity, 100);
            }
        }
        applyImpaleCooldown();
        SoundEvent sound = entity == null ? ModSounds.PUNCH_2_SOUND_EVENT
                : airTriggered ? ModSounds.WHITESNAKE_PUNCH_FINAL_HIT_EVENT : getImpaleSound();
        if (entity != null) playImpaleConnectSoundExtra();
        if (!self.level().isClientSide()) {
            playSoundIfPossible(self.level(),null, origin.blockPosition(), sound, SoundSource.PLAYERS, 0.95F,
                    entity == null ? 1.0F : 1.2F);
        }
    }

    private void applyImpaleCooldown() {
        int cooldown = ClientNetworking.getAppropriateConfig().generalStandSettings.impaleAttackCooldown;
        setCooldown(PowerIndex.SKILL_1_SNEAK, cooldown);
        if (self instanceof ServerPlayer player) {
            S2CPacketUtil.sendCooldownSyncPacket(player, PowerIndex.SKILL_1_SNEAK, cooldown);
        }
    }

    @Override
    public void standBarrageHit() {
        if (!isPiloting() && !autoMode) {
            StandEntity stand = getStandEntity(self);
            if (forwardBarrage && stand != null) {
                if (self instanceof Player && isPacketPlayer()) {
                    C2SPacketUtil.standBarrageHitPacket(
                            getTargetEntityId2(2.7F, stand, 50), attackTimeDuring);
                    if (isBarraging() && attackTimeDuring == getBarrageLength()) attackTimeDuring = -10;
                } else if (!(self instanceof Player)) {
                    barrageImpact(getTargetEntity(stand, 2.7F, 50), attackTimeDuring);
                }
                findDeflectables();
                return;
            }
            super.standBarrageHit();
            return;
        }
        if (autoMode) {
            if (!self.level().isClientSide()) {
                LivingEntity target = getAutoAttackTarget();
                if (target != null && target.distanceTo(actionOrigin()) <= CONTROL_PUNCH_RANGE + 0.75F
                        && actionOrigin().hasLineOfSight(target)) {
                    barrageImpact(target, attackTimeDuring);
                } else {
                    barrageImpact(null, attackTimeDuring);
                }
                if (attackTimeDuring == getBarrageLength()) attackTimeDuring = -10;
            }
            findDeflectables();
            return;
        }
        if (self instanceof Player && isPacketPlayer()) {
            C2SPacketUtil.standBarrageHitPacket(
                    getTargetEntityId2(CONTROL_PUNCH_RANGE, actionOrigin(), 50), attackTimeDuring);
            if (isBarraging() && attackTimeDuring == getBarrageLength()) {
                attackTimeDuring = -10;
            }
        }
        findDeflectables();
    }

    @Override
    public void barrageImpact(Entity entity, int hitNumber) {
        if (entity != null && moveStarted) {
            moveStarted = false;
            StandEntity stand = getStandEntity(self);
            if (stand != null) {
                stand.setXRot(getLookAtEntityPitch(stand, entity));
                stand.setYRot(getLookAtEntityYaw(stand, entity));
            }
        }
        if ((isPiloting() || autoMode) && entity != null
                && entity.distanceTo(actionOrigin()) > CONTROL_PUNCH_RANGE + 0.75F) {
            entity = null;
        }
        super.barrageImpact(entity, hitNumber);
    }

    @Override
    public boolean canActuallyHit(Entity entity) {
        if (entity == null) return false;
        if (!isPiloting() && !autoMode) return super.canActuallyHit(entity);
        if (ClientNetworking.getAppropriateConfig().generalStandSettings.standPunchesGoThroughDoorsAndCorners) {
            return true;
        }
        return actionOrigin().hasLineOfSight(entity);
    }

    @Override
    public float getRushDistance() {
        if (forwardBarrage) return 15.0F;
        if (!isPiloting() && !autoMode) return super.getRushDistance();
        return self.distanceTo(actionOrigin()) + CONTROL_PUNCH_RANGE;
    }

    @Override
    public void barrageImpact2(Entity entity, boolean lastHit, float knockbackStrength) {
        if (!isPiloting() && !autoMode) {
            super.barrageImpact2(entity, lastHit, knockbackStrength);
            return;
        }
        if (entity instanceof LivingEntity) {
            if (lastHit) takeDeterminedKnockbackWithY(actionOrigin(), entity, knockbackStrength);
            else takeKnockbackUp(entity, knockbackStrength);
        }
    }

    // Chop
    public boolean setPowerFinalAttack() {
        animateStand(StandEntity.FINAL_ATTACK_WINDUP);
        setAttackTimeDuring(0);
        setActivePower(PowerIndex.SNEAK_ATTACK_CHARGE);
        poseStand(OffsetIndex.GUARD);
        clashDone = false;
        return true;
    }

    public boolean setPowerSuperHit() {
        setAttackTimeDuring(0);
        setActivePower(PowerIndex.SNEAK_ATTACK);
        poseStand(OffsetIndex.ATTACK);
        chargedFinal = Math.min(chargedFinal, getMaxSuperHitTime());
        animateFinalAttackHit();
        return true;
    }

    public void updateFinalAttackCharge() {
        if (attackTimeDuring < 0) return;
        if (attackTimeDuring >= 60) {
            if (self instanceof Player && self.level().isClientSide() && isPacketPlayer()) {
                ((StandUser) self).roundabout$tryPower(PowerIndex.NONE, true);
                tryPowerPacket(PowerIndex.NONE);
            }
        } else if (attackTimeDuring >= getMaxSuperHitTime() && !(self instanceof Player)) {
            ((StandUser) self).roundabout$tryIntPower(PowerIndex.SNEAK_ATTACK, true, getMaxSuperHitTime());
        }
    }

    public void updateFinalAttack() {
        if (attackTimeDuring == 5) standFinalAttack();
    }

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        boolean standOn = PowerTypes.hasStandActive(playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;
        if (standOn && getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
            float charge = (float) attackTimeDuring / getMaxSuperHitTime();
            int barWidth = Math.min(15, Math.round(charge * 15));
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 111, 15, 6);
            if (charge >= 1.0F) {
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 132, barWidth, 6);
            } else if (charge >= 0.5F) {
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 118, barWidth, 6);
            } else {
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 125, barWidth, 6);
            }
        } else {
            super.renderAttackHud(context, playerEntity, scaledWidth, scaledHeight, ticks,
                    vehicleHeartCount, flashAlpha, otherFlashAlpha);
        }
    }

    public float getFinalAttackKnockback() {
        float charged = getChargedPercent();
        if (charged >= 1.0F) return charged * 3.0F;
        if (charged >= 0.5F) return 0.7F;
        return 0.1F;
    }

    public float getFinalPunchStrength(Entity entity) {
        float ret;
        float punchD = this.getPunchStrength(entity) * 2 + this.getHeavyPunchStrength(entity);
        if (this.getReducedDamage(entity)) {
            ret = getChargedPercent() * punchD;
            if (this.chargedFinal >= getMaxSuperHitTime()) {
                ret += 0.5F;
            }
        } else {
            ret = (getChargedPercent() * punchD) + 3;
            if (this.chargedFinal >= getMaxSuperHitTime()) {
                ret += 2;
            }
        }
        return ret;
    }

    public SoundEvent getFinalAttackSound() {
        float charged = getChargedPercent();
        if (charged >= 1.0F) return ModSounds.KING_CRIMSON_PUNCH_5_EVENT;
        if (charged >= 0.5F) return ModSounds.KING_CRIMSON_PUNCH_4_EVENT;
        return ModSounds.KING_CRIMSON_PUNCH_3_EVENT;
    }

    private float getFinalAttackPitch() {
        return getChargedPercent() < 0.5F ? 1.2F : 1.0F;
    }

    private float getChargedPercent() {
        return (float) this.chargedFinal / (float) getMaxSuperHitTime();
    }

    public int getMaxSuperHitTime() {
        return 30 + (getMeltLevel() * 2);
    }

    public void animateFinalAttackHit() {
        float charged = getChargedPercent();
        if (charged >= 1.0F) animateStand(WhitesnakeEntity.CHOP_CHARGED);
        else if (charged >= 0.5F) animateStand(WhitesnakeEntity.CHOP_ATTACK);
        else animateStand(StandEntity.FINAL_ATTACK);
    }

    public void finalAttackImpact(Entity entity) {
        this.setAttackTimeDuring(-20);
        LivingEntity origin = actionOrigin();
        float range = isPiloting() ? CONTROL_PUNCH_RANGE + 0.75F : 5.5F;
        if (entity != null && entity.distanceTo(origin) > range) entity = null;
        if (entity != null) {
            float charged = getChargedPercent();
            hitParticlesCenter(entity);
            if (StandDamageEntityAttack(entity, getFinalPunchStrength(entity), 0, this.self)) {
                if (entity instanceof LivingEntity living) {
                    if (charged >= 1.0F) {
                        addEXP(5, living);
                    } else if (charged > 0.5F) {
                        MainUtil.makeBleed(living, 0, 200, this.self);
                        addEXP(2, living);
                    }
                }
                takeDeterminedKnockbackWithY(origin, entity, getFinalAttackKnockback());
            } else if (chargedFinal >= getMaxSuperHitTime()) {
                if (charged >= 1.0F) knockShield2(entity, 70);
                else if (charged > 0.5F) knockShield2(entity, 50);
            }
        } else {
            float halfReach = isPiloting() ? CONTROL_PUNCH_RANGE * 0.5F
                    : this.getDistanceOut(this.self, this.getReach(), false) * 0.5F;
            Vec3 pointVec = DamageHandler.getRayPoint(origin, halfReach);
            if (!this.self.level().isClientSide) {
                sendParticlesIfPossible(self.level(),ModParticles.PUNCH_MISS,
                        pointVec.x, pointVec.y, pointVec.z, 1, 0.0, 0.0, 0.0, 1);
            }
        }
        SoundEvent sound = entity == null ? ModSounds.PUNCH_2_SOUND_EVENT : getFinalAttackSound();
        float pitch = entity == null ? 1.0F : getFinalAttackPitch();
        if (!self.level().isClientSide()) {
            playSoundIfPossible(self.level(),null, self.blockPosition(), sound,
                    SoundSource.PLAYERS, 0.95F, pitch);
        }
    }

    @Override
    public float multiplyPowerByStandConfigPlayers(float power) {
        return (float) (power * (ClientNetworking.getAppropriateConfig().
                whitesnakeSettings.whitesnakeAttackMultOnPlayers * 0.01));
    }

    @Override
    public float multiplyPowerByStandConfigMobs(float power) {
        return (float) (power * (ClientNetworking.getAppropriateConfig().
                whitesnakeSettings.whitesnakeAttackMultOnMobs * 0.01));
    }

    @Override
    public float getPunchStrength(Entity entity) {
        if (this.getReducedDamage(entity)) {
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(1.26F));
        }
        return levelupDamageMod(multiplyPowerByStandConfigMobs(4.0F));
    }

    @Override
    public float getHeavyPunchStrength(Entity entity) {
        if (this.getReducedDamage(entity)) {
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(1.87F));
        }
        return levelupDamageMod(multiplyPowerByStandConfigMobs(5.0F));
    }

    @Override
    public float getBarrageFinisherStrength(Entity entity) {
        return this.getReducedDamage(entity) ? 3.0F : 8.0F;
    }

    @Override
    public float getBarrageDamagePlayer() {
        return 8.0F;
    }

    @Override
    public float getBarrageDamageMob() {
        return 18.0F;
    }

    @Override
    public float getBarrageHitStrength(Entity entity) {
        float str = super.getBarrageHitStrength(entity);
        if (str > 0.005F) {
            if (getReducedDamage(entity)) {
                str *= levelupDamageMod((float) (ClientNetworking.getAppropriateConfig().
                        whitesnakeSettings.whitesnakeAttackMultOnPlayers * 0.01));
            } else {
                str *= levelupDamageMod((float) (ClientNetworking.getAppropriateConfig().
                        whitesnakeSettings.whitesnakeAttackMultOnMobs * 0.01));
            }
        }

        if (entity instanceof LivingEntity livingEntity
                && str >= livingEntity.getHealth()
                && ClientNetworking.getAppropriateConfig().generalStandSettings.barragesOnlyKillOnLastHit) {
            str = entity instanceof Player ? 0.00001F : 0F;
        }
        return str;
    }

    @Override
    public float getImpalePunchStrength(Entity entity) {
        if (this.getReducedDamage(entity)) {
            return levelupDamageMod(multiplyPowerByStandConfigPlayers((float) (3F
                    * (ClientNetworking.getAppropriateConfig().generalStandSettings.generalImpaleAttackMultiplier * 0.01))));
        }
        return levelupDamageMod(multiplyPowerByStandConfigMobs((float) (17F
                * (ClientNetworking.getAppropriateConfig().generalStandSettings.generalImpaleAttackMultiplier * 0.01))));
    }

    @Override
    public int getExpForLevelUp(int currentLevel) {
        int amount = currentLevel == 1 ? 100 : 100 + ((currentLevel - 1) * 100);
        return (int) (amount * getLevelMultiplier());
    }

    @Override
    public SoundEvent getPunchLandSound() {
        return getActivePowerPhase() <= 1
                ? ModSounds.WHITESNAKE_PUNCH_HIT_EVENT
                : ModSounds.WHITESNAKE_PUNCH_HIT_2_EVENT;
    }

    @Override
    public SoundEvent getPunchLandLastSound() {
        return ModSounds.WHITESNAKE_PUNCH_FINAL_HIT_EVENT;
    }

    @Override
    public SoundEvent getPunchMissSound() {
        return ModSounds.PUNCH_2_SOUND_EVENT;
    }

    @Override
    public SoundEvent getImpaleSound() {
        return ModSounds.IMPALE_HIT_EVENT;
    }

    @Override
    public SoundEvent getBarrageChargeSound() {
        return ModSounds.STAND_BARRAGE_WINDUP_EVENT;
    }

    @Override
    public void playBarrageMissNoise(int hitNumber) {
        if (!self.level().isClientSide() && hitNumber % 2 == 0) {
            playSoundIfPossible(self.level(),null, actionOrigin().blockPosition(), ModSounds.STAND_BARRAGE_MISS_EVENT,
                    SoundSource.PLAYERS, 0.95F, (float) (0.8 + Math.random() * 0.4));
        }
    }

    @Override
    public void playBarrageNoise(int hitNumber, Entity entity) {
        if (!self.level().isClientSide() && hitNumber % 2 == 0) {
            playSoundIfPossible(self.level(),null, actionOrigin().blockPosition(), ModSounds.WHITESNAKE_BARRAGE_HIT_EVENT,
                    SoundSource.PLAYERS, 0.9F, (float) (0.9 + Math.random() * 0.25));
        }
    }

    @Override
    public void playBarrageEndNoise(float mod, Entity entity) {
        if (!self.level().isClientSide()) {
            playSoundIfPossible(self.level(),null, actionOrigin().blockPosition(), ModSounds.STAND_BARRAGE_END_EVENT,
                    SoundSource.PLAYERS, 0.95F + mod, 1.0F);
        }
    }

    @Override
    public void playBarrageBlockNoise() {
        if (!self.level().isClientSide()) {
            playSoundIfPossible(self.level(),null, actionOrigin().blockPosition(), ModSounds.STAND_BARRAGE_BLOCK_EVENT,
                    SoundSource.PLAYERS, 0.95F, (float) (0.8 + Math.random() * 0.4));
        }
    }

    @Override
    public void playBarrageBlockEndNoise(float mod, Entity entity) {
        if (!self.level().isClientSide()) {
            playSoundIfPossible(self.level(),null, actionOrigin().blockPosition(), ModSounds.STAND_BARRAGE_END_BLOCK_EVENT,
                    SoundSource.PLAYERS, 0.88F + mod, 1.7F);
        }
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.WHITESNAKE_SUMMON_EVENT;
        } else if (soundChoice == IMPALE_NOISE) {
            return ModSounds.IMPALE_CHARGE_EVENT;
        } else if (soundChoice == ACID_CHARGE_NOISE) {
            return ModSounds.IMPALE_CHARGE_EVENT;
        } else if (soundChoice == DISC_STEAL_CHARGE_NOISE) {
            return ModSounds.WHITESNAKE_DISC_STEAL_CHARGE_EVENT;
        } else if (soundChoice == TIME_STOP_TICKING) {
            return ModSounds.TIME_STOP_TICKING_EVENT;
        } else if (soundChoice == ROUNDABOUT_DODGE_NOISE) {
            return ModSounds.DODGE_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    @Override
    public float getSoundVolumeFromByte(byte soundChoice) {
        return soundChoice == DISC_STEAL_CHARGE_NOISE ? 0.3F : super.getSoundVolumeFromByte(soundChoice);
    }

    @Override
    protected LivingEntity getSoundEmitter(byte soundNo, boolean onSelf) {
        if (!onSelf && isPiloting()
                && (soundNo == SoundIndex.REVOLVER_RELOAD || soundNo == SoundIndex.COLT_RELOAD)) {
            LivingEntity stand = getPilotingStand();
            if (stand != null && stand.isAlive() && !stand.isRemoved()) return stand;
        }
        return super.getSoundEmitter(soundNo, onSelf);
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY,
                                                   int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> icons = Lists.newArrayList();
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0,
                "ability.roundabout.punch", "instruction.roundabout.press_attack",
                StandIcons.WHITESNAKE_PUNCH, 0, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0,
                "ability.roundabout.guard", "instruction.roundabout.hold_block",
                StandIcons.WHITESNAKE_GUARD, 0, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0,
                "ability.roundabout.whitesnake_chop", "instruction.roundabout.hold_attack_crouch",
                StandIcons.WHITESNAKE_CHOP, 0, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0,
                "ability.roundabout.barrage", "instruction.roundabout.barrage",
                StandIcons.WHITESNAKE_BARRAGE, 0, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 118, 0,
                "ability.roundabout.forward_barrage", "instruction.roundabout.forward_barrage",
                StandIcons.WHITESNAKE_FORWARD_BARRAGE, 1, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, getMeltingModeLevel(),
                "ability.roundabout.whitesnake_melting_mode", "instruction.roundabout.press_skill_block",
                StandIcons.WHITESNAKE_MELTING_MODE, 2, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 77, topPos + 99, getImpaleLevel(),
                "ability.roundabout.impale", "instruction.roundabout.press_skill_crouch",
                StandIcons.WHITESNAKE_IMPALE, 2, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 96, topPos + 99, 0,
                "ability.roundabout.dodge", "instruction.roundabout.press_skill",
                StandIcons.WHITESNAKE_DASH, 3, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 80, getDiscStealLevel(),
                "ability.roundabout.whitesnake_disc_steal", "instruction.roundabout.press_skill",
                StandIcons.WHITESNAKE_DISC_STEAL, 1, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 118, 0,
                "ability.roundabout.whitesnake_inventory", "instruction.roundabout.whitesnake_press_skill_guard",
                StandIcons.WHITESNAKE_INVENTORY, 1, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 99, 0,
                "ability.roundabout.whitesnake_disc_throw", "instruction.roundabout.press_skill",
                StandIcons.WHITESNAKE_DISC_THROW, 2, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 77, topPos + 80, getAcidTossLevel(),
                "ability.roundabout.whitesnake_acid_toss", "instruction.roundabout.press_skill",
                StandIcons.WHITESNAKE_ACID_TOSS, 2, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 96, topPos + 80, 0,
                "ability.roundabout.whitesnake_control_mode", "instruction.roundabout.press_skill",
                StandIcons.WHITESNAKE_CONTROL_MODE, 4, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 96, topPos + 118, getHallucinatoryDisguiseLevel(),
                "ability.roundabout.whitesnake_disguise", "instruction.roundabout.whitesnake_press_skill_guard",
                StandIcons.WHITESNAKE_HALLUCINATORY_DISGUISE, 4, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 115, topPos + 80, 0,
                "ability.roundabout.phase_grab", "instruction.roundabout.press_skill_block",
                StandIcons.WHITESNAKE_PHASE_GRAB, 2, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 115, topPos + 99, 0,
                "ability.roundabout.whitesnake_auto_mode", "instruction.roundabout.press_skill_crouch",
                StandIcons.WHITESNAKE_AUTO_MODE, 4, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 115, topPos + 118, getTimeSparkLevel(),
                "ability.roundabout.whitesnake_time_spark", "instruction.roundabout.press_skill_crouch",
                StandIcons.WHITESNAKE_TIME_SPARK, 3, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 134, topPos + 80, 0,
                "ability.roundabout.whitesnake_auto_mode_move", "instruction.roundabout.whitesnake_auto_mode_move",
                StandIcons.WHITESNAKE_AUTO_MODE_MOVE, 1, level, bypass));
        icons.add(drawSingleGUIIcon(context, 18, leftPos + 134, topPos + 99, 0,
                "ability.roundabout.whitesnake_auto_mode_attack", "instruction.roundabout.whitesnake_auto_mode_attack",
                StandIcons.WHITESNAKE_AUTO_MODE_ATTACK, 2, level, bypass));
        return icons;
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (autoMode) {
            setSkillIcon(context, x, y, 1, StandIcons.WHITESNAKE_AUTO_MODE_MOVE, PowerIndex.NO_CD);
            setSkillIcon(context, x, y, 2, StandIcons.WHITESNAKE_AUTO_MODE_ATTACK, PowerIndex.NO_CD);
            setSkillIcon(context, x, y, 3, StandIcons.WHITESNAKE_DASH, PowerIndex.GLOBAL_DASH);
            setSkillIcon(context, x, y, 4, isHoldingSneak()
                    ? StandIcons.WHITESNAKE_CONTROL_MODE : StandIcons.WHITESNAKE_CONTROL_MODE_EXIT, PowerIndex.NO_CD);
            return;
        }
        if (isBarrageAttacking()) {
            setSkillIcon(context, x, y, 1, StandIcons.WHITESNAKE_FORWARD_BARRAGE, PowerIndex.NO_CD);
        } else if (meltingMode && !isGuarding()) {
            setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD, true);
        } else {
            boolean inventoryContext = isGuarding() && !isPiloting();
            byte discSelection = getSelectedDisc();
            if (inventoryContext || (canExecuteMoveWithLevel(getDiscStealLevel()) && discSelection >= 0)) {
                setSkillIcon(context, x, y, 1,
                        inventoryContext ? StandIcons.WHITESNAKE_INVENTORY
                                : isHoldingSneak() ? StandIcons.WHITESNAKE_DISC_TYPES[discSelection]
                                : StandIcons.WHITESNAKE_DISC_STEAL,
                        inventoryContext ? PowerIndex.NO_CD : PowerIndex.SKILL_1);
            } else {
                setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD, true);
            }
        }

        if (isGuarding()) {
            if (!isPiloting()) {
                setSkillIcon(context, x, y, 2, StandIcons.WHITESNAKE_PHASE_GRAB, PHASE_GRAB_COOLDOWN);
            } else if (canExecuteMoveWithLevel(getMeltingModeLevel())) {
                setSkillIcon(context, x, y, 2, StandIcons.WHITESNAKE_MELTING_MODE, PowerIndex.NO_CD);
            } else {
                setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.NO_CD, true);
            }
            setSkillIcon(context, x, y, 3, StandIcons.WHITESNAKE_DASH, PowerIndex.GLOBAL_DASH);
        } else if (isHoldingSneak()) {
            if (!meltingMode && canExecuteMoveWithLevel(getImpaleLevel())) {
                setSkillIcon(context, x, y, 2, StandIcons.WHITESNAKE_IMPALE, PowerIndex.SKILL_1_SNEAK);
            } else {
                setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.NO_CD, true);
            }
            if (!meltingMode && canExecuteMoveWithLevel(getTimeSparkLevel())) {
                setSkillIcon(context, x, y, 3, StandIcons.WHITESNAKE_TIME_SPARK, TIME_SPARK_COOLDOWN);
            } else {
                setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NO_CD, true);
            }
        } else {
            if (isThrowableDisc(self.getMainHandItem())) {
                setSkillIcon(context, x, y, 2, StandIcons.WHITESNAKE_DISC_THROW, PowerIndex.SKILL_2);
            } else {
                if (canExecuteMoveWithLevel(getAcidTossLevel())) {
                    setSkillIcon(context, x, y, 2, StandIcons.WHITESNAKE_ACID_TOSS, PowerIndex.SKILL_2);
                } else {
                    setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.NO_CD, true);
                }
            }
            if (meltingMode) {
                setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NO_CD, true);
            } else {
                setSkillIcon(context, x, y, 3, StandIcons.WHITESNAKE_DASH, PowerIndex.GLOBAL_DASH);
            }
        }

        if (isPiloting() && isGuarding() && !canExecuteMoveWithLevel(getHallucinatoryDisguiseLevel())) {
            setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD, true);
        } else {
            setSkillIcon(context, x, y, 4,
                    isPiloting() && isGuarding() ? StandIcons.WHITESNAKE_HALLUCINATORY_DISGUISE
                            : isPiloting() ? isHoldingSneak()
                                    ? StandIcons.WHITESNAKE_AUTO_MODE : StandIcons.WHITESNAKE_CONTROL_MODE_EXIT
                            : isHoldingSneak() ? StandIcons.WHITESNAKE_AUTO_MODE : StandIcons.WHITESNAKE_CONTROL_MODE,
                    isPiloting() && isGuarding() ? PowerIndex.SKILL_4 : PowerIndex.NO_CD);
        }
    }
}
