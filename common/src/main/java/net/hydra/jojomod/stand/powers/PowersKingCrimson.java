package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.*;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.client.hud.StandHudRender;
import net.hydra.jojomod.entity.KingCrimsonCloneEntity;
import net.hydra.jojomod.entity.KingCrimsonProjectionEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.TimeSkipSnapshot;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.projectile.BloodSplatterEntity;
import net.hydra.jojomod.entity.projectile.GasolineCanEntity;
import net.hydra.jojomod.entity.projectile.ThrownObjectEntity;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.KingCrimsonEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.BlockGrabPreset;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;

import java.util.*;

public class PowersKingCrimson extends BlockGrabPreset {

    public PowersKingCrimson(LivingEntity self) {
        super(self);
    }

    public final Map<Integer, TimeSkipSnapshot> epitaph = new HashMap<>();
    public final Map<Integer, TimeSkipSnapshot> skip_dump = new HashMap<>();

    @Override
    /**Override to add disable config*/
    public boolean isStandEnabled() {
        return ClientNetworking.getAppropriateConfig().kingCrimsonSettings.enableKingCrimson;
    }

    public KingCrimsonCloneEntity activeClone = null;

    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }


    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersKingCrimson(entity);
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.SUMMON_KING_CRIMSON_EVENT;
        } else if (soundChoice == IMPALE_NOISE) {
            return ModSounds.IMPALE_CHARGE_EVENT;
        } else if (soundChoice == EPITAPH_NOISE) {
            return ModSounds.EPITAPH_ACTIVATE_EVENT;
        } else if (soundChoice == EPITAPH_FADE_NOISE) {
            return ModSounds.EPITAPH_FADE_EVENT;
        } else if (soundChoice == TIME_ERASE) {
            return ModSounds.TIME_ERASE_FULL_EVENT;
        } else if (soundChoice == EPITAPH_PROJECTION) {
            return ModSounds.HOLOGRAM_START_EVENT;
        }else if (soundChoice == EPITAPH_PROJECTION_2) {
            return ModSounds.HOLOGRAM_END_EVENT;
        }else if (soundChoice == DING_NOISE) {
            return ModSounds.DING_EVENT;
        }else if (soundChoice == DRAIN_NOISE) {
            return ModSounds.VAMPIRE_DRAIN_EVENT;
        }else if (soundChoice == SUMMON_ARMS) {
            return ModSounds.SUMMON_SOUND_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    @Override
    public float getSoundPitchFromByte(byte soundChoice){
        if (soundChoice == SUMMON_ARMS) {
            return 1.6F;
        } else {
            return super.getSoundPitchFromByte(soundChoice);
        }
    }

    public static final byte EPITAPH_NOISE = 106;
    public static final byte EPITAPH_FADE_NOISE = 107;
    public static final byte EPITAPH_PROJECTION = 108;
    public static final byte EPITAPH_PROJECTION_2 = 109;
    public static final byte TIME_ERASE = 110;
    public static final byte DING_NOISE = 111;
    public static final byte DRAIN_NOISE = 112;
    public static final byte SUMMON_ARMS = 113;

    @Override
    public SoundEvent getImpaleSound() {
        return ModSounds.KING_CRIMSON_IMPALE_EVENT;
    }
    public final Set<LivingEntity> bloodSplatterHits = new HashSet<>();
    public int ticksOfEraseLeft = 0;
    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);
        $$0.putBoolean("timeEraseActive",timeEraseActive);
        $$0.putBoolean("hasArmsOut",hasArmsOut);
        $$0.putBoolean("isRenderingArms",isRenderingArms);
        $$0.putInt("ticksOfEraseLeft",ticksOfEraseLeft);
        if (onCooldown(PowerIndex.SKILL_4)){
            $$0.putInt("timeEraseCooldown",getCooldown(PowerIndex.SKILL_4).time);
        } else {
            $$0.putInt("timeEraseCooldown",0);
        }
    }
    @Override
    public void retractHands(){
        hasArmsOut = false;
        flipArmRendering();
    }
    public boolean hasArmsOut = false;
    //hands code for hiding stand
    public boolean canSummonStandAsEntity(){
        if (hasArmsOut){
            return false;
        }
        return super.canSummonStandAsEntity();
    }

    @Override
    public boolean rendersPlayer(){
        return hasHandsOut();
    }
    @Override
    public boolean canUseMiningStand() {
        return super.canUseMiningStand();
    }
    public boolean isRenderingArms = false;
    @Override
    public boolean hasHandsOut(){
        return hasArmsOut;
    }
    @Override
    public boolean hasHandsOutRendering(){
        return isRenderingArms && self instanceof Player;
    }
    @Override
    public void flipArmRendering(){
        handTicks = 0;
        isRenderingArms = false;
        saveDiscAndSync();
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);
        if ($$0.contains("timeEraseActive")) {
            timeEraseActive = $$0.getBoolean("timeEraseActive");
            if (self.level().isClientSide()){
                if (timeEraseActive){
                    ClientUtil.bootTimeErase();
                }
            }
        }
        if ($$0.contains("hasArmsOut")) {
            hasArmsOut = $$0.getBoolean("hasArmsOut");
        }
        if ($$0.contains("isRenderingArms")) {
            isRenderingArms = $$0.getBoolean("isRenderingArms");
        }
        if ($$0.contains("ticksOfEraseLeft")) {
            ticksOfEraseLeft = $$0.getInt("ticksOfEraseLeft");
        }
        if ($$0.contains("timeEraseCooldown")) {
            if (!self.level().isClientSide()){
                int jint = $$0.getInt("timeEraseCooldown");
                if (jint > 0){
                    setCooldown(PowerIndex.SKILL_4,jint);
                }
            }
        }
    }
    public void applyBloodSplatterEffects() {
        if (bloodSplatterHits.isEmpty()){
            return;
        }
        for (Iterator<LivingEntity> it = bloodSplatterHits.iterator(); it.hasNext();) {
            LivingEntity entity = it.next();

            if (entity.isRemoved() || !entity.isAlive()) {
                it.remove();
                continue;
            }

            ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.BLOOD,
                    entity.getEyePosition().x(), entity.getEyePosition().y(), entity.getEyePosition().z(),
                    30, 0, 0, 0, 0.1);
            entity.addEffect(new MobEffectInstance(
                    MobEffects.BLINDNESS,
                    80,
                    1,
                    false,
                    true,
                    true
            ));
            if (entity instanceof Mob mb && !MainUtil.isBossMob(mb)){
                ((IMob)mb).roundabout$setConfusionTicks(60);
            }
        }

        bloodSplatterHits.clear();
    }

    @Override
    public void onStandSwitchInto(){
        super.onStandSwitchInto();
        if (!self.level().isClientSide()) {
            int minCo = ClientNetworking.getAppropriateConfig().
                    kingCrimsonSettings.timeEraseMinimumCooldown;
            if (!onCooldown(PowerIndex.SKILL_4) || getCooldown(PowerIndex.SKILL_4).time < minCo) {
                setCooldown(PowerIndex.SKILL_4, minCo);
            }
        }
    }
    public boolean isUsingEpitaph() {
        return !epitaph.isEmpty();
    }
    public boolean timeEraseActive = false;
    public boolean isUsingTimeErase() {
        return timeEraseActive;
    }

    public float getSped(Entity entity) {
        if (entity instanceof LivingEntity LE) {
            if (LE.getSpeed() <= 0) {
                if (LE.getAttributes().hasAttribute(Attributes.MOVEMENT_SPEED)) {
                    return (float) LE.getAttributeValue(Attributes.MOVEMENT_SPEED);
                }
            }
            return LE.getSpeed();
        }
        return 0;
    }

    public void skipBlockEntities(int ticks) {
        ((ILevelAccess)self.level()).rdbt$skipTime(self,ticks,getSkipRange());
    }
    private void skipDayTime(int ticks) {
        if (ClientNetworking.getAppropriateConfig().kingCrimsonSettings.enableDaySkip){
            if (self.level() instanceof ServerLevel sl){
                if (sl.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)) {
                    sl.setDayTime(sl.getDayTime() + ticks);
                }
            }
        }
    }

    public int timeEraseMaxTicks(){
        return ClientNetworking.getAppropriateConfig().kingCrimsonSettings.timeEraseDuration;
    }
    public void getReplacementHUD(GuiGraphics context, Player cameraPlayer, int screenWidth, int screenHeight, int x,
                                  boolean removeNum){
        if (isUsingTimeErase()){
            StandHudRender.renderTimeErase(context,cameraPlayer,screenWidth,screenHeight,x,this);
            return;
        }
        StandHudRender.renderEpitaph(context,cameraPlayer,screenWidth,screenHeight,x,this);
    }


    public boolean replaceHudActively(){
        return isUsingEpitaph() || isErasingTime();
    }
    public int getEpitphDuration(){
        return ClientNetworking.getAppropriateConfig().kingCrimsonSettings.epitaphDuration;
    }
    public int getTicksIntoEpitaph(){
        return ticksIntoEpitaph;
    }
    public boolean canUseEpitaphWithoutSkip(){
        return ClientNetworking.getAppropriateConfig().kingCrimsonSettings.enableEpitaphPreSkip;
    }
    public boolean canPredictIdles(){
        return ClientNetworking.getAppropriateConfig().kingCrimsonSettings.predictIdles;
    }
    public int ticksIntoEpitaph = 0;
    public boolean vibeCheck = false;
    @Override
    public void tickPower() {
        if (isUsingTimeErase()) {
            if (ticksOfEraseLeft > 0) {
                if (!(self instanceof Player pl && pl.isCreative())) {
                    ticksOfEraseLeft--;
                    if (ticksOfEraseLeft == 0) {
                        if (self.level().isClientSide()) {
                            C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_STAND_TRIGGER_2);
                        } else {
                            timeErase();
                        }
                    }
                }
            }
        } else {
            if (disengageTime > 0){
                disengageTime--;
                if (disengageTime <= 0){
                    setDisengageTarget(null);
                }
            }
        }
        if (self.level().isClientSide()){
            if (isUsingEpitaph()){
                ticksIntoEpitaph++;
                if (ticksIntoEpitaph > getEpitphDuration()){
                    C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_STAND_TRIGGER);
                    epitaph.clear();
                    ticksIntoEpitaph = 0;
                }
            } else {
                ticksIntoEpitaph = 0;
            }
        } else {
            //Cancel erase on relog
            if (isErasingTime() && activeClone == null){
                timeErase();
            }
        }

        skipRange = ClientNetworking.getAppropriateConfig().kingCrimsonSettings.timeSkipRange;
        super.tickPower();
    }
    public static Vec3 getPredictedDirection() {
        return new Vec3(Math.random()*1-0.5F,0,Math.random()*1-0.5F);
    }
    public Vec3 predictIdle(LivingEntity liv, int ticks) {
        if (!canPredictIdles() || !isGravityNormal(liv)){
            return liv.position();
        }
        //Mobs and Players that are still still need to move when idle
        Level level = liv.level();

        Vec3 predicted = liv.position();
        AABB box = liv.getBoundingBox();

        if (liv.getPose() == Pose.SITTING){
            return predicted;
        }
        if (liv instanceof Creeper creeper && creeper.getSwelling(1) > 0){
            return predicted;
        }
        if (liv instanceof FlyingMob){
            return predicted;
        }
        if (liv instanceof WanderingTrader){
            return predicted;
        }

        float speed = (float) (Math.random()*0.9F);
        float sped = getSped(liv);
        Vec3 basevelocity = getPredictedDirection()
                .normalize()
                .scale(sped * speed);
        if (basevelocity.y > 0)
            basevelocity = basevelocity.multiply(1, 0, 1);
        for (int i = 0; i < ticks; i++) {

            Vec3 velocity = basevelocity;
            BlockPos ft = BlockPos.containing(predicted);
            if (!liv.isInWater() && !MainUtil.inWater(level.getBlockState(ft))) {
                velocity = velocity.add(0, -1, 0);
            } else {
                velocity.multiply(1,0,1);
            }

            // ----- Normal collision -----
            Vec3 collided = Entity.collideBoundingBox(
                    liv,
                    velocity,
                    box,
                    level,
                    List.of()
            );
            Vec3 nextPos = predicted.add(collided);
            BlockPos feet = BlockPos.containing(nextPos);
            BlockPos below = feet.below();
            BlockState ground = level.getBlockState(below);
            BlockState ground2 = level.getBlockState(feet);

            if (!ground.blocksMotion()) {
                // Don't move there
                break;
            }

            if (isSunlightDanger(liv,nextPos)){
                return predicted;
            }
            AABB checkBox = box.inflate(-0.05);

            for (BlockPos pos : BlockPos.betweenClosed(
                    Mth.floor(checkBox.minX), Mth.floor(checkBox.minY), Mth.floor(checkBox.minZ),
                    Mth.floor(checkBox.maxX), Mth.floor(checkBox.maxY), Mth.floor(checkBox.maxZ))) {

                boolean isStrider = liv instanceof Strider;
                if (level.getFluidState(pos).is(FluidTags.LAVA) && !isStrider) {
                    return predicted;
                }


                BlockState state = level.getBlockState(pos);
                if (MainUtil.isDangerous(level, pos, state, liv instanceof Strider)) {
                    return predicted;
                }
            }

            predicted = predicted.add(collided);
            box = box.move(collided);
        }

        return predicted;
    }

    public void releaseTimeSkip(){

    }

    public boolean spawnClone(){
        if (!this.getSelf().level().isClientSide() && this.getSelf() instanceof Player PE) {
            KingCrimsonCloneEntity fclone = ModEntities.KING_CRIMSON_CLONE.create(this.getSelf().level());
            activeClone = fclone;
            Entity mount = PE.getVehicle();
            if (mount != null && mount.getId() != self.getId()){
                PE.stopRiding();
            }
            fclone.setVisage(((IPlayerEntity)PE).roundabout$getMaskSlot());
            fclone.setPlayer(PE);
            fclone.copyPosition(PE);
            // Position
            fclone.setPos(PE.getX(), PE.getY(), PE.getZ());
            fclone.setYRot(PE.getYRot());
            fclone.yRotO = PE.yRotO;

            fclone.setXRot(PE.getXRot());
            fclone.xRotO = PE.xRotO;

            fclone.yBodyRot = PE.yBodyRot;
            fclone.yBodyRotO = PE.yBodyRotO;

            fclone.yHeadRot = PE.yHeadRot;
            fclone.yHeadRotO = PE.yHeadRotO;
            fclone.getNavigation().stop();
            this.getSelf().level().addFreshEntity(fclone);

            if (mount != null && mount.getId() != self.getId()){
                fclone.startRiding(mount);
            }
            fclone.setDeltaMovement(delta);
            fclone.isBackingUp = isBackingUp;
            fclone.isMovingForward = isMovingForward;
            fclone.isSneaking = isSneaking;
            fclone.isSprinting = isSprinting;
            runaway = hasHandsOut() || isTargetBehindPlayer(PE);
            fclone.runaway = runaway;
            if (hasHandsOut()){
                fclone.runaway = true;
                fclone.runawayTrue = true;
            }

            fclone.setIsJumping(isJumping);
            ((StandUser)fclone).roundabout$setStandDisc(((StandUser)self).roundabout$getStandDisc().copy());

            LivingEntity last = self.getLastHurtMob();
            LivingEntity last2 = self.getLastHurtByMob();
            if (last != null && last.getUUID() != self.getUUID() && last.isAlive() &&
                    last.distanceTo(self) < 30){
                fclone.setLastHurtMob(last);
                fclone.setTarget(last);
            } else {
                if (last2 != null && last2.getUUID() != self.getUUID() && last2.isAlive() &&
                last2.distanceTo(self) < 30) {
                    fclone.setTarget(last2);
                }
            } if (last2 != null && last2.getUUID() != self.getUUID() && last2.isAlive() &&
                    last2.distanceTo(self) < 30){
                fclone.setLastHurtByMob(last);
            }
            activeClone.setHealth(self.getHealth());
            activeClone.getAttribute(Attributes.MAX_HEALTH).setBaseValue(
                    self.getMaxHealth()
            );
            for (MobEffectInstance effect : self.getActiveEffects()) {
                activeClone.addEffect(new MobEffectInstance(effect));
            }
            activeClone.setInvulnerable(self.isInvulnerable());
            activeClone.setNoGravity(self.isNoGravity());
            activeClone.setSilent(self.isSilent());
            activeClone.setArrowCount(self.getArrowCount());
            activeClone.setStingerCount(self.getStingerCount());
            activeClone.setSharedFlagOnFire(self.isOnFire());
            activeClone.setRemainingFireTicks(self.getRemainingFireTicks());
            activeClone.setAirSupply(self.getAirSupply());
            activeClone.setItemSlot(EquipmentSlot.HEAD, self.getItemBySlot(EquipmentSlot.HEAD).copy());
            activeClone.setItemSlot(EquipmentSlot.CHEST, self.getItemBySlot(EquipmentSlot.CHEST).copy());
            activeClone.setItemSlot(EquipmentSlot.LEGS, self.getItemBySlot(EquipmentSlot.LEGS).copy());
            activeClone.setItemSlot(EquipmentSlot.FEET, self.getItemBySlot(EquipmentSlot.FEET).copy());
            activeClone.setItemSlot(EquipmentSlot.MAINHAND, self.getMainHandItem().copy());
            activeClone.setItemSlot(EquipmentSlot.OFFHAND, self.getOffhandItem().copy());
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                activeClone.setDropChance(slot, 0.0F);
            }
            activeClone.hurtTime = self.hurtTime;
            activeClone.fallDistance = self.fallDistance;
            StandUser activeCloneUser = ((StandUser) activeClone);
            ((IMob)activeClone).roundabout$setFate(((IPlayerEntity) PE).roundabout$getFate());
            if (FateTypes.takesSunlightDamage(activeClone)) {
                ((IMob) activeClone).roundabout$getGoalSelector().addGoal(2, new RestrictSunGoal(activeClone));
            }
            if (!runaway){
                ((IMob) activeClone).roundabout$getGoalSelector().addGoal(8, new LookAtPlayerGoal(activeClone, Player.class, 8.0F));
                activeClone.addBehaviourGoals();
            }
            StandUser thisUser = getStandUserSelf();
            activeCloneUser.roundabout$setStandSkin(thisUser.roundabout$getStandSkin());
            activeCloneUser.roundabout$setDazeTime(thisUser.roundabout$getDazeTime());
            activeCloneUser.roundabout$setOnStandFire(thisUser.roundabout$getOnStandFire());
            activeCloneUser.roundabout$setRemainingStandFireTicks(
                    thisUser.roundabout$getRemainingFireTicks());
            activeCloneUser.roundabout$setBubbleEncased(thisUser.roundabout$getBubbleEncased());
            HeatUtil.setHeat(activeClone,HeatUtil.getHeat(self));
            activeCloneUser.roundabout$setLocacacaCurse(thisUser.roundabout$getLocacacaCurse());
            activeCloneUser.roundabout$setGasolineTime(thisUser.roundabout$getGasolineTime());
            activeCloneUser.roundabout$setLeapTicks(thisUser.roundabout$getLeapTicks());

            StandPowers powers = activeCloneUser.roundabout$getStandPowers();
            powers.attackTime = attackTimeMax;
            powers.attackTimeMax = attackTimeMax;
            powers.activePowerPhase = activePowerPhase;

            StandEntity st = getStandEntity(self);
            ((StandUser)activeClone).roundabout$setActive(true);
            if (st != null && !st.isRemoved()) {
                StandEntity stand = getNewStandEntity();
                if (stand instanceof FollowingStandEntity fse && st instanceof FollowingStandEntity ste) {
                    ((StandUser)activeClone).roundabout$setStand(stand);
                    stand.setFollowing(activeClone);
                    stand.setUser(activeClone);

                    stand.setPos(st.getX(), st.getY(), st.getZ());
                    stand.xOld = st.xOld;
                    stand.yOld = st.yOld;
                    stand.zOld = st.zOld;

                    // Body rotation
                    stand.setYRot(st.getYRot());
                    stand.yRotO = st.yRotO;

                    // Pitch
                    stand.setXRot(st.getXRot());
                    stand.xRotO = st.xRotO;

                    // Body/head rotations
                    stand.yBodyRot = st.yBodyRot;
                    stand.yBodyRotO = st.yBodyRotO;
                    stand.yHeadRot = st.yHeadRot;
                    stand.yHeadRotO = st.yHeadRotO;

                    // Animation
                    stand.walkAnimation.setSpeed(st.walkAnimation.speed());
                    stand.walkAnimation.position(st.walkAnimation.position());
                    ILivingEntityAccess entityAndData = ((ILivingEntityAccess) stand);
                    ILivingEntityAccess playerAndData = ((ILivingEntityAccess) st);

                    entityAndData.roundabout$setLerpXRot(playerAndData.roundabout$getLerpXRot());
                    entityAndData.roundabout$setLerpYRot(playerAndData.roundabout$getLerpYRot());
                    entityAndData.roundabout$setLerp(new Vector3f(
                            (float) playerAndData.roundabout$getLerpX(),
                            (float) playerAndData.roundabout$getLerpY(),
                            (float) playerAndData.roundabout$getLerpZ()
                    ));


                    stand.setFadePercent(st.getFadePercent());
                    stand.setFadeOut((byte) st.getFadeOut());
                    stand.copyPosition(st);
                    stand.setSkin(st.getSkin());
                    stand.setIdleAnimation(st.getIdleAnimation());
                    fse.setDistanceOut(ste.getDistanceOut());
                    fse.setAnchorPlace(ste.getAnchorPlace());
                    fse.setAnchorPlaceAttack(ste.getAnchorPlaceAttack());
                    fse.setSizePercent(ste.getSizePercent());
                    fse.setIdleRotation(ste.getIdleRotation());
                    fse.setIdleYOffset(ste.getIdleYOffset());
                    self.level().addFreshEntity(stand);
                }
            }
        }
        return true;
    }


    public static boolean isTargetBehindPlayer(Player player) {
        LivingEntity target = null;

        // Prefer the entity the player last attacked
        LivingEntity lastHurt = player.getLastHurtMob();
        if (lastHurt != null && lastHurt.isAlive() && player.distanceToSqr(lastHurt) <= 50 * 50) {
            target = lastHurt;
        }

        // Otherwise use the last entity that hurt the player
        if (target == null) {
            LivingEntity lastAttacker = player.getLastHurtByMob();
            if (lastAttacker != null && lastAttacker.isAlive()
                    && player.distanceToSqr(lastAttacker) <= 50 * 50) {
                target = lastAttacker;
            }
        }

        if (target == null) {
            return false;
        }

        // Horizontal look vector
        Vec3 look = player.getLookAngle();
        look = new Vec3(look.x, 0.0, look.z);

        if (look.lengthSqr() < 1.0E-6) {
            return false;
        }

        look = look.normalize();

        // Horizontal vector to target
        Vec3 toTarget = target.position().subtract(player.position());
        toTarget = new Vec3(toTarget.x, 0.0, toTarget.z);

        if (toTarget.lengthSqr() < 1.0E-6) {
            return false;
        }

        toTarget = toTarget.normalize();

        return look.dot(toTarget) < 0.0;
    }

    public boolean isBackingUp = false;
    public boolean isMovingForward = false;
    public boolean isSneaking = false;
    public boolean isJumping = false;
    public boolean isSprinting = false;
    public boolean runaway = false;
    public Vec3 delta = Vec3.ZERO;


    @Override
    public void tickMobAI(LivingEntity attackTarget){
        if (self instanceof KingCrimsonCloneEntity kce){
            tickCloneAi(attackTarget, kce);
        } else {
            super.tickMobAI(attackTarget);
        }
    }

    public void tickCloneAi(LivingEntity attackTarget, KingCrimsonCloneEntity kce){
        if (attackTarget != null && attackTarget.isAlive()){
            if (kce.runaway){
                return;
            }
            float distanceTo = attackTarget.distanceTo(this.getSelf());
            if ((this.getActivePower() == PowerIndex.ATTACK || this.getActivePower() == PowerIndex.BARRAGE)
                    || distanceTo <= 5){
                rotateMobHead(attackTarget);
            }

            Entity targetEntity = getTargetEntity(this.self, -1);
            if (targetEntity != null && targetEntity.is(attackTarget)) {
                if (this.attackTimeDuring <= -1) {
                    double RNG = Math.random();
                    if (RNG < 0.35 && targetEntity instanceof Player && this.activePowerPhase <= 0 && !wentForCharge) {
                        wentForCharge = true;
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.BARRAGE_CHARGE, true);
                    } else if (RNG < 0.6 && targetEntity instanceof Player && this.activePowerPhase <= 0 && !wentForCharge
                    && distanceTo <= 3){
                        wentForCharge = true;
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1_SNEAK, true);
                    } else if (this.activePowerPhase < this.activePowerPhaseMax || this.attackTime >= this.attackTimeMax) {
                        wentForCharge = false;
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.ATTACK, true);
                    }
                }
            }
        }
    }

    @Override
    public boolean isServerControlledCooldown(byte num){
        if (num == PowerIndex.SKILL_1 || num == PowerIndex.SKILL_2_SNEAK
                || num == PowerIndex.SKILL_4|| num == PowerIndex.SKILL_4_SNEAK) {
            return true;
        }
        return super.isServerControlledCooldown(num);
    }
    public Vec3 predictPlayer(LivingEntity player, int ticks) {

        if (!isGravityNormal(player)){
            return player.position();
        }
        boolean inTimeLockBlock = false;

        AABB checkBoxOG = player.getBoundingBox().inflate(-0.05);

        for (BlockPos pos : BlockPos.betweenClosed(
                Mth.floor(checkBoxOG.minX), Mth.floor(checkBoxOG.minY), Mth.floor(checkBoxOG.minZ),
                Mth.floor(checkBoxOG.maxX), Mth.floor(checkBoxOG.maxY), Mth.floor(checkBoxOG.maxZ))) {

            BlockState state = player.level().getBlockState(pos);

            if (state.is(ModBlocks.STICKY_ICE) || state.is(ModBlocks.COLD_AIR)
                    || state.is(ModBlocks.BARBED_WIRE_BUNDLE) || state.is(Blocks.COBWEB)) {
                inTimeLockBlock = true;
                break;
            }
        }

        if (inTimeLockBlock) {
            return player.position();
        }
        Level level = player.level();

        Vec3 predicted = player.position();
        Vec3 previousSafe = predicted;
        Vec3 previousPreviousSafe = predicted;

        AABB box = player.getBoundingBox();

        Deque<Vec3> history = null;
        if (player instanceof Player ye){
            history = ((IPlayerEntity) ye).rdbt$getMovementHistory();
        } else if (player.getControllingPassenger() instanceof Player ye){
            history = ((IPlayerEntity) ye).rdbt$getMovementHistory();
        }

        Vec3 oldPos = player.position();

        if (history != null && history.size() >= 2) {
            Iterator<Vec3> it = history.descendingIterator();

            Vec3 newest = it.next();
            Vec3 previous = it.hasNext() ? it.next() : newest;
            Vec3 third = it.hasNext() ? it.next() : previous;

            oldPos = third;
        }
        if (player.position().distanceTo(oldPos) < 0.1 && player.getId() != self.getId()){
            return predictIdle(player,ticks);
        }
        Vec3 baseVelocity = player.position()
                .subtract(oldPos)
                .normalize()
                .scale(getSped(player) * (2.5+(Math.random()*0.5)));
        if (baseVelocity.y > 0)
            baseVelocity = baseVelocity.multiply(1, 0, 1);

        for (int i = 0; i < ticks; i++) {
            hitWall2 = false;

            // ----- Estimate movement direction -----



            Vec3 velocity = baseVelocity;

            BlockPos ft = BlockPos.containing(predicted);
            if (!player.isInWater() && !player.isFallFlying() && !MainUtil.inWater(level.getBlockState(ft))
            && !(player instanceof Player pl2 && pl2.getAbilities().flying) && !(player instanceof FlyingMob)) {
                velocity = velocity.add(0, -1, 0);
            }  else {
                velocity = velocity.multiply(1,0,1);
            }

            // ----- Normal collision -----
            Vec3 collided = Entity.collideBoundingBox(
                    player,
                    velocity,
                    box,
                    level,
                    List.of()
            );

            // ----- Try stepping up -----
            boolean hitWall =
                    collided.x != velocity.x ||
                            collided.z != velocity.z;

            if (hitWall) {
                i+=3;
                double stepHeight = 1.0;

                // Move upward first
                Vec3 up = Entity.collideBoundingBox(
                        player,
                        new Vec3(0, stepHeight, 0),
                        box,
                        level,
                        List.of()
                );

                AABB steppedBox = box.move(up);

                // Move horizontally while elevated
                Vec3 forward = Entity.collideBoundingBox(
                        player,
                        new Vec3(velocity.x, 0, velocity.z),
                        steppedBox,
                        level,
                        List.of()
                );

                steppedBox = steppedBox.move(forward);

                // Move back down
                Vec3 down = Entity.collideBoundingBox(
                        player,
                        new Vec3(0, -stepHeight, 0),
                        steppedBox,
                        level,
                        List.of()
                );

                Vec3 steppedMove = up.add(forward).add(down);

                // Prefer whichever gives more horizontal travel
                if (forward.horizontalDistanceSqr() > collided.horizontalDistanceSqr()) {
                    collided = steppedMove;
                }
                if (collided.y == 0){
                    hitWall2 = true;
                }
            }
            previousPreviousSafe = previousSafe;
            previousSafe = predicted;

            predicted = predicted.add(collided);
            box = box.move(collided);
            if (player.getId() != self.getId()) {
                AABB checkBox = box.inflate(-0.05);

                if (isSunlightDanger(player,predicted)){
                    predicted = previousPreviousSafe;
                    break;
                }
                for (BlockPos pos : BlockPos.betweenClosed(
                        Mth.floor(checkBox.minX), Mth.floor(checkBox.minY), Mth.floor(checkBox.minZ),
                        Mth.floor(checkBox.maxX), Mth.floor(checkBox.maxY), Mth.floor(checkBox.maxZ))) {

                    if (level.getFluidState(pos).is(FluidTags.LAVA)) {
                        if (!hasGroundWithin3Blocks(level, player, previousPreviousSafe)) {
                            return player.position();
                        }
                        return previousPreviousSafe;
                    }

                    BlockState state = level.getBlockState(pos);
                    if (MainUtil.isDangerous(level, pos, state, false)) {
                        predicted = previousPreviousSafe;
                        break;
                    }
                }
            }
        }



        boolean deviousStratBlocker = ClientNetworking.getAppropriateConfig().mandomSettings.timeRewindStopsDeviousStrategies;

        if (deviousStratBlocker) {
            // 2. Check for dangerous blocks inside target box
            boolean cancel = false;
            double width = player.getBbWidth();
            double height = player.getBbHeight();
            AABB targetBox = new AABB(
                    predicted.x - width / 2.0, predicted.y, predicted.z - width / 2.0,
                    predicted.x + width / 2.0, predicted.y + height, predicted.z + width / 2.0
            );
            //
            targetBox = RotationUtil.boxPlayerToWorld(targetBox,((IGravityEntity)player).roundabout$getGravityDirection());

            for (BlockPos pos : BlockPos.betweenClosed(
                    Mth.floor(targetBox.minX), Mth.floor(targetBox.minY), Mth.floor(targetBox.minZ),
                    Mth.floor(targetBox.maxX), Mth.floor(targetBox.maxY), Mth.floor(targetBox.maxZ))) {

                BlockState state = level.getBlockState(pos);
                Block block = state.getBlock();

                // List of bad blocks to avoid
                if (block == Blocks.COBWEB || block == Blocks.LAVA ||
                block == ModBlocks.BARBED_WIRE_BUNDLE) {
                    cancel = true;
                    break;
                }

                // Optional: also avoid fire or cactus
                if (block == Blocks.FIRE || block == Blocks.CACTUS) {
                    cancel = true;
                    break;
                }
            }

            if (isSunlightDanger(player,predicted)){
                cancel = true;
            }
            if (cancel){
                return player.position();
            }
        }
        if (!hasGroundWithin3Blocks(level, player, predicted)) {
            return player.position();
        }
        return predicted;
    }

    private boolean hasGroundWithin3Blocks(Level level, LivingEntity player, Vec3 predicted) {
        if (player instanceof Player pl2 && pl2.getAbilities().flying){
            return true;
        }

        double halfWidth = player.getBbWidth() * 0.5 - 0.05;

        // Check each corner of the player's feet
        double[] xs = {
                predicted.x - halfWidth,
                predicted.x + halfWidth
        };
        double[] zs = {
                predicted.z - halfWidth,
                predicted.z + halfWidth
        };

        int startY = Mth.floor(predicted.y - 0.01);

        for (double x : xs) {
            for (double z : zs) {

                boolean supported = false;

                for (int dy = 1; dy <= 3; dy++) {
                    BlockPos pos = BlockPos.containing(x, startY - dy, z);

                    BlockState state = level.getBlockState(pos);

                    if (!state.isAir()
                            && state.blocksMotion()
                            && state.getCollisionShape(level, pos).isEmpty() == false) {
                        supported = true;
                        break;
                    }
                }

                // One corner has no support within 3 blocks
                if (!supported) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean canUseTimeSkip(){
        if (ClientNetworking.getAppropriateConfig().kingCrimsonSettings.freeTimeSkip){
            return true;
        }
        return (canAttackLight() || isGuarding()) && !self.isUsingItem() && !isClashing();
    }

    @Override
    public void eatEffectIntercept(ItemStack $$0, Level $$1, LivingEntity $$2){
        if (isUsingTimeErase()){
            timeErase();
        }
    }
    @Override
    public void onPlaceBlock(ServerPlayer $$0, BlockPos $$1, ItemStack $$2){
        /**This will be a denial of epitaph + block place*/
        if (ClientNetworking.getAppropriateConfig().kingCrimsonSettings.blocksCancelEpitaph2) {
            if (isUsingEpitaph()) {
                epitaph();
            }
        }
        if (isUsingTimeErase()){
            timeErase();
        }
        super.onPlaceBlock($$0,$$1,$$2);
    }
    public Vec3 predictStrider(Strider strider, int ticks) {
        Level level = strider.level();

        Entity rider = strider.getControllingPassenger();
        if (!(rider instanceof Player player)) {
            return strider.position();
        }

        Deque<Vec3> history = ((IPlayerEntity) player).rdbt$getMovementHistory();

        Vec3 oldPos = player.position();

        if (history != null && history.size() >= 2) {
            Iterator<Vec3> it = history.descendingIterator();

            it.next();
            Vec3 previous = it.next();
            oldPos = previous;
        }

        if (player.position().distanceTo(oldPos) < 0.1) {
            return strider.position();
        }

        Vec3 predicted = strider.position();
        AABB box = strider.getBoundingBox();

        Vec3 velocity = player.position()
                .subtract(oldPos)
                .normalize()
                .scale(getSped(player) * 2.5);

        // Striders don't need normal gravity prediction as aggressively
        velocity = velocity.multiply(1, 0, 1);

        Vec3 previousSafe = predicted;

        for (int i = 0; i < ticks; i++) {

            Vec3 move = velocity;

            // Only apply gravity if NOT over lava
            BlockPos below = BlockPos.containing(
                    predicted.x,
                    predicted.y - 0.5,
                    predicted.z
            );

            boolean overLava = level.getFluidState(below).is(FluidTags.LAVA);

            if (!overLava) {
                move = move.add(0, -0.08, 0);
            }

            Vec3 collided = Entity.collideBoundingBox(
                    strider,
                    move,
                    box,
                    level,
                    List.of()
            );

            previousSafe = predicted;
            predicted = predicted.add(collided);
            box = box.move(collided);


            // Don't let striders walk onto solid ground if desired
            AABB check = box.inflate(-0.05);

            boolean touchingInvalid = false;

            for (BlockPos pos : BlockPos.betweenClosed(
                    Mth.floor(check.minX),
                    Mth.floor(check.minY),
                    Mth.floor(check.minZ),
                    Mth.floor(check.maxX),
                    Mth.floor(check.maxY),
                    Mth.floor(check.maxZ))) {

                BlockState state = level.getBlockState(pos);

                // Striders like lava
                if (state.isSolid() && !level.getFluidState(pos).is(FluidTags.LAVA)) {
                    touchingInvalid = true;
                    break;
                }
            }

            if (touchingInvalid) {
                return previousSafe;
            }
        }

        return predicted;
    }


    public Vec3 predictBoat(Boat boat, int ticks) {
        Level level = boat.level();

        if (!(boat.getControllingPassenger() instanceof Player player)) {
            return boat.position();
        }

        Deque<Vec3> history = ((IPlayerEntity) player).rdbt$getMovementHistory();

        Vec3 oldPos = player.position();

        if (history != null && history.size() >= 3) {
            Iterator<Vec3> it = history.descendingIterator();
            it.next(); // newest
            it.next(); // previous
            oldPos = it.next(); // third newest
        }

        if (player.position().distanceTo(oldPos) < 0.1) {
            return boat.position();
        }

        Vec3 predicted = boat.position();
        Vec3 previousSafe = predicted;
        Vec3 previousPreviousSafe = predicted;

        Vec3 velocity = player.position()
                .subtract(oldPos)
                .normalize()
                .scale(0.4);

        AABB box = boat.getBoundingBox();

        for (int i = 0; i < ticks; i++) {

            previousPreviousSafe = previousSafe;
            previousSafe = predicted;

            Vec3 collided = Entity.collideBoundingBox(
                    boat,
                    velocity,
                    box,
                    level,
                    List.of()
            );

            // Couldn't move fully -> hit shore.
            if (collided.horizontalDistanceSqr() + 1.0E-6 < velocity.horizontalDistanceSqr()) {
                return previousPreviousSafe;
            }

            predicted = predicted.add(collided);
            box = box.move(collided);

            // Make sure the boat is still floating.
            if (!boatHasWaterBelow(level, box)) {
                return previousPreviousSafe;
            }
        }

        return predicted;
    }
    public Vec3 predictTNT(PrimedTnt tnt, int ticks) {
        Level level = tnt.level();

        Vec3 predicted = tnt.position();
        Vec3 velocity = tnt.getDeltaMovement();

        AABB box = tnt.getBoundingBox();

        for (int i = 0; i < ticks; i++) {

            // vanilla TNT gravity
            velocity = velocity.add(0, -0.04, 0);

            // vanilla air drag
            velocity = velocity.scale(0.98);

            Vec3 movement = Entity.collideBoundingBox(
                    tnt,
                    velocity,
                    box,
                    level,
                    List.of()
            );

            predicted = predicted.add(movement);
            box = box.move(movement);

            // Hit ground, stop falling
            if (movement.y != velocity.y) {
                velocity = new Vec3(
                        velocity.x * 0.7,
                        0,
                        velocity.z * 0.7
                );
            }
        }

        return predicted;
    }
    public Vec3 predictMinecart(AbstractMinecart cart, int ticks) {
        Level level = cart.level();

        Entity rider = cart.getFirstPassenger();
        if (!(rider instanceof Player player)) {
            if (!cart.onGround()) {
                return predictFallingMinecart(cart, ticks);
            }
            return cart.position();
        }

        // Figure out initial movement direction from player history.
        Deque<Vec3> history = ((IPlayerEntity) player).rdbt$getMovementHistory();
        Vec3 oldPos = player.position();

        if (history != null && history.size() >= 3) {
            Iterator<Vec3> it = history.descendingIterator();
            it.next();
            it.next();
            oldPos = it.next();
        }

        Vec3 movement = player.position().subtract(oldPos);

        Direction dir;
        if (Math.abs(movement.x) > Math.abs(movement.z)) {
            dir = movement.x > 0 ? Direction.EAST : Direction.WEST;
        } else {
            dir = movement.z > 0 ? Direction.SOUTH : Direction.NORTH;
        }

        BlockPos railPos = BlockPos.containing(cart.position());

        if (!BaseRailBlock.isRail(level.getBlockState(railPos))) {
            railPos = railPos.below();
            if (!BaseRailBlock.isRail(level.getBlockState(railPos))) {
                return cart.position();
            }
        }
        double remaining = cart.getDeltaMovement().horizontalDistance() * ticks;

        while (remaining >= 1.0) {
            // move to next rail
            remaining -= 1.0;

            BlockState state = level.getBlockState(railPos);
            RailShape shape = state.getValue(((BaseRailBlock) state.getBlock()).getShapeProperty());

            // Curves
            switch (shape) {
                case NORTH_EAST -> {
                    if (dir == Direction.NORTH) dir = Direction.EAST;
                    else if (dir == Direction.EAST) dir = Direction.NORTH;
                }
                case NORTH_WEST -> {
                    if (dir == Direction.NORTH) dir = Direction.WEST;
                    else if (dir == Direction.WEST) dir = Direction.NORTH;
                }
                case SOUTH_EAST -> {
                    if (dir == Direction.SOUTH) dir = Direction.EAST;
                    else if (dir == Direction.EAST) dir = Direction.SOUTH;
                }
                case SOUTH_WEST -> {
                    if (dir == Direction.SOUTH) dir = Direction.WEST;
                    else if (dir == Direction.WEST) dir = Direction.SOUTH;
                }
                default -> {}
            }

            BlockPos next = railPos.relative(dir);

            // descending rail
            if (!BaseRailBlock.isRail(level.getBlockState(next))) {
                next = next.below();
            }

            // ascending rail
            if (!BaseRailBlock.isRail(level.getBlockState(next))) {
                BlockPos up = railPos.relative(dir).above();
                if (BaseRailBlock.isRail(level.getBlockState(up))) {
                    next = up;
                }
            }

            if (!BaseRailBlock.isRail(level.getBlockState(next))) {
                break;
            }

            BlockState nextState = level.getBlockState(next);
            RailShape nextShape = nextState.getValue(
                    ((BaseRailBlock) nextState.getBlock()).getShapeProperty()
            );

            if (nextShape == RailShape.NORTH_EAST ||
                    nextShape == RailShape.NORTH_WEST ||
                    nextShape == RailShape.SOUTH_EAST ||
                    nextShape == RailShape.SOUTH_WEST) {

                break;
            }

            railPos = next;
        }

        BlockState endState = level.getBlockState(railPos);
        RailShape endShape = endState.getValue(((BaseRailBlock) endState.getBlock()).getShapeProperty());

        return new Vec3(
                railPos.getX() + 0.5,
                railPos.getY() + railYOffset(endShape),
                railPos.getZ() + 0.5
        );
    }
    private boolean isConnectedRail(Level level, BlockPos from, BlockPos to) {
        BlockState fromState = level.getBlockState(from);
        BlockState toState = level.getBlockState(to);

        if (!BaseRailBlock.isRail(fromState) || !BaseRailBlock.isRail(toState)) {
            return false;
        }

        RailShape fromShape = fromState.getValue(
                ((BaseRailBlock) fromState.getBlock()).getShapeProperty()
        );

        RailShape toShape = toState.getValue(
                ((BaseRailBlock) toState.getBlock()).getShapeProperty()
        );

        Direction travel = Direction.fromDelta(
                to.getX() - from.getX(),
                to.getY() - from.getY(),
                to.getZ() - from.getZ()
        );

        if (travel == null) {
            return false;
        }

        // The next rail must have an exit back toward the rail we came from
        return switch (toShape) {
            case NORTH_SOUTH -> travel == Direction.NORTH || travel == Direction.SOUTH;
            case EAST_WEST -> travel == Direction.EAST || travel == Direction.WEST;

            case ASCENDING_NORTH -> travel == Direction.NORTH || travel == Direction.SOUTH;
            case ASCENDING_SOUTH -> travel == Direction.NORTH || travel == Direction.SOUTH;
            case ASCENDING_EAST -> travel == Direction.EAST || travel == Direction.WEST;
            case ASCENDING_WEST -> travel == Direction.EAST || travel == Direction.WEST;

            case SOUTH_EAST -> travel == Direction.SOUTH || travel == Direction.EAST;
            case SOUTH_WEST -> travel == Direction.SOUTH || travel == Direction.WEST;
            case NORTH_EAST -> travel == Direction.NORTH || travel == Direction.EAST;
            case NORTH_WEST -> travel == Direction.NORTH || travel == Direction.WEST;
        };
    }
    public Vec3 predictFallingMinecart(AbstractMinecart cart, int ticks) {
        Level level = cart.level();

        Entity rider = cart.getFirstPassenger();
        if (!(rider instanceof Player player)) {

            Vec3 predicted = cart.position();
            AABB box = cart.getBoundingBox();

            Vec3 velocity = Vec3.ZERO;

            for (int i = 0; i < ticks; i++) {
                velocity = velocity.add(0, -0.08, 0); // vanilla gravity

                Vec3 move = Entity.collideBoundingBox(
                        cart,
                        velocity,
                        box,
                        level,
                        List.of()
                );

                predicted = predicted.add(move);
                box = box.move(move);

                // Hit the ground
                if (move.y != velocity.y) {
                    break;
                }
            }

            return predicted;
        }
        return cart.position();
    }
    private static double railYOffset(RailShape shape) {
        return switch (shape) {
            case ASCENDING_EAST,
                 ASCENDING_WEST,
                 ASCENDING_NORTH,
                 ASCENDING_SOUTH -> 0.5;
            default -> 0.0625;
        };
    }
    private static boolean boatHasWaterBelow(Level level, AABB box) {

        double y = box.minY - 0.1;

        int minX = Mth.floor(box.minX + 0.1);
        int maxX = Mth.floor(box.maxX - 0.1);

        int minZ = Mth.floor(box.minZ + 0.1);
        int maxZ = Mth.floor(box.maxZ - 0.1);

        int water = 0;
        int total = 0;

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                total++;

                if (level.getFluidState(BlockPos.containing(x, y, z)).is(FluidTags.WATER)) {
                    water++;
                }
            }
        }

        return water * 2 >= total;
    }
    public Vec3 predictPosition(Mob mob, int ticks) {
        if (!isGravityNormal(mob)){
            return mob.position();
        }
        if (mob.getControllingPassenger() instanceof Player pl){
            if (mob instanceof Strider str){
                Vec3 pred = predictStrider(str,40);
                return pred;
            } else {
                Vec3 pred = predictPlayer(mob,40);
                return pred;
            }
        }


        Path path = mob.getNavigation().getPath();

        if (path == null) {
            return predictIdle(mob,ticks);
        }

        double remaining = getSped(mob) * ticks;
        Vec3 current = mob.position();

        int index = path.getNextNodeIndex();

        while (index < path.getNodeCount()) {
            Node node = path.getNode(index);

            Vec3 next = new Vec3(
                    node.x + 0.5,
                    node.y,
                    node.z + 0.5
            );

            double segment = current.distanceTo(next);

            if (remaining <= segment) {
                return current.lerp(next, remaining / segment);
            }

            remaining -= segment;
            current = next;
            index++;
        }

        if (current.distanceTo(mob.position()) < 0.01 && !MainUtil.isBossMob(mob)
        && !(mob instanceof FlyingMob)){
            return predictIdle(mob,ticks);
        }

        return current;
    }

    public void debugPlayer(){
        if (self instanceof Player pl) {
            int id = self.getId();
            float xRot = self.getXRot();
            float yRot = self.getYRot();
            Vec3 predicted = self.position();

            hitWall2 = false;
            predicted = predictPlayer(pl, 40);
            if (hitWall2){
                yRot = Mth.wrapDegrees(yRot + 180.0F);
            }
            epitaph.put(self.getId(), new TimeSkipSnapshot(
                    id,
                    predicted,
                    xRot,
                    yRot
            ));
            S2CPacketUtil.addEpitaph(pl, id, predicted, xRot, yRot);
        }
    }

    //This variable makes a player turn around when they hit a wall to sell a believable reaction
    public boolean hitWall2 = false;

    public void basicSkip(boolean skipSelf){
        hitWall2 = false;
        AABB area = self.getBoundingBox().inflate(getSkipRange());
        List<FallingBlockEntity> fallingBlocks = new ArrayList<>();
        for (Entity entity : self.level().getEntitiesOfClass(Entity.class, area)) {
            if (entity instanceof KingCrimsonProjectionEntity kcpj){
                kcpj.spawnDeathParticles();
                kcpj.discard();
                continue;
            }
            if (!canSkip(entity))
                continue;
            hitWall2 = false;
            if (entity instanceof Projectile proj) {
                if (proj instanceof FireworkRocketEntity){
                    proj.discard();
                } else {
                    skip_dump.put(proj.getId(), new TimeSkipSnapshot(
                            proj.getId(),
                            predictProjectile(proj, 40),
                            proj.getXRot(),
                            proj.getYRot()
                    ));
                }
            } else if (entity instanceof ItemEntity it) {
                Vec3 predicted = predictItem(it, 100);
                skip_dump.put(it.getId(), new TimeSkipSnapshot(
                        it.getId(),
                        predicted,
                        it.getXRot(),
                        it.getYRot()
                ));
            } else if (entity instanceof FallingBlockEntity fbe){
                fallingBlocks.add(fbe);
            } else if (entity instanceof Boat bt && bt.getControllingPassenger() instanceof Player) {
                Vec3 boat = predictBoat(bt, 40);

                skip_dump.put(bt.getId(), new TimeSkipSnapshot(
                        bt.getId(),
                        boat,
                        bt.getXRot(),
                        bt.getYRot()
                ));
            } else if (entity instanceof PrimedTnt tnt) {
                Vec3 predicted = predictTNT(tnt, 100);

                skip_dump.put(
                        entity.getId(),
                        new TimeSkipSnapshot(
                                entity.getId(),
                                predicted,
                                entity.getXRot(),
                                entity.getYRot()
                        )
                );
            } else if (entity instanceof AbstractMinecart bt ){
                Vec3 minecart = predictMinecart(bt,40);

                skip_dump.put(bt.getId(), new TimeSkipSnapshot(
                        bt.getId(),
                        minecart,
                        bt.getXRot(),
                        bt.getYRot()
                ));
            } if (entity instanceof LivingEntity living) {
                if (!skipSelf && living.getId() == self.getId()) {
                    continue;
                } else if (living instanceof StandEntity) {
                    continue;
                }
                StandEntity stand = getStandEntity(self);
                int id = living.getId();
                if (!(stand != null && stand.getId() == id)) {
                    if (!(living instanceof StandEntity) &&
                            !(living instanceof Player pk && pk.isCreative()
                                    && pk.getId() != self.getId())
                    ) {
                        Vec3 predicted = living.position();
                        float xRot = living.getXRot();
                        float yRot = living.getYRot();
                        if (!living.isSleeping()) {
                            if (living instanceof Mob mob) {
                                if (!mob.isLeashed() && !(mob instanceof WanderingTrader)) {
                                    predicted = predictPosition(mob, 100);
                                }
                            } else if (living instanceof Player player) {
                                // Fallback for players, armor stands, etc.
                                predicted = predictPlayer(player, 40);
                                if (player.getId() == self.getId()) {
                                    if (predicted.distanceTo(self.getPosition(1)) < 0.1) {
                                        continue;
                                    }
                                }
                                if (hitWall2 && player.getId() != self.getId()) {
                                    yRot = Mth.wrapDegrees(yRot + 180.0F);
                                }
                            }
                        }


                        skip_dump.put(living.getId(), new TimeSkipSnapshot(
                                id,
                                predicted,
                                xRot,
                                yRot
                        ));
                    }
                }
            }
        }

        playStandUserOnlySoundsIfNearby(TIME_SKIP_1, getSkipBonusRange(), true, false);
        scatterPackets();
        if (skip_dump.isEmpty()){
            return;
        }
        for (TimeSkipSnapshot snapshot : skip_dump.values()) {
            skipSingle(snapshot);
        }
        skip_dump.clear();
        if (!fallingBlocks.isEmpty()) {
            fallingBlocks.sort(
                    Comparator.comparingDouble(entity -> entity.position().y)
            );


            for (FallingBlockEntity falling : fallingBlocks) {
                skipFallingBlock(falling, 100);
            }
        }

    }

    public boolean isSunlightDanger(Entity entity, Vec3 pos){
        if (isSunlightDanger2(entity.getControllingPassenger(),pos)){
            return true;
        }
        if (entity instanceof LivingEntity LE && (FateTypes.takesSunlightDamage(LE) || LE instanceof Zombie ||
                LE instanceof Skeleton || LE instanceof Phantom)){
            if (!FateTypes.canCurrentlyAvoidSunlight(LE)){
                if (!FateTypes.isInSunlight(LE)) {
                    return FateTypes.isInSunlight(LE, pos);
                }

            }
        }
        return false;
    }
    public boolean isSunlightDanger2(Entity entity, Vec3 pos){
        if (entity instanceof LivingEntity LE && (FateTypes.takesSunlightDamage(LE) || LE instanceof Zombie ||
                LE instanceof Skeleton || LE instanceof Phantom)){
            if (!FateTypes.canCurrentlyAvoidSunlight(LE)){
                if (!FateTypes.isInSunlight(LE)) {
                    return FateTypes.isInSunlight(LE, pos);
                }

            }
        }
        return false;
    }

    private static final int SKIP_TICKS = 100;

    public static void skipEffects(LivingEntity entity) {
        if (entity.getActiveEffects().isEmpty()) {
            return;
        }

        List<MobEffectInstance> effects = new ArrayList<>(entity.getActiveEffects());

        for (MobEffectInstance effect : effects) {
            // Don't touch your custom effect
            int duration = effect.getDuration();
            if (effect.getEffect() == ModEffects.STAND_VIRUS ||
                    effect.getEffect() == MobEffects.ABSORPTION ||
                    effect.getEffect() == MobEffects.HEALTH_BOOST ||
                    effect.getEffect() == ModEffects.MELTING ||
                    duration == MobEffectInstance.INFINITE_DURATION) {
                continue;
            }


        // Preserve infinite effects
            duration -= SKIP_TICKS;

            // Keep it alive for one tick so vanilla can remove it naturally
            if (duration <= 0) {
                duration = 1;
            }

            MobEffectInstance replacement = new MobEffectInstance(
                    effect.getEffect(),
                    duration,
                    effect.getAmplifier(),
                    effect.isAmbient(),
                    effect.isVisible(),
                    effect.showIcon()
            );

            entity.removeEffect(effect.getEffect());
            entity.addEffect(replacement);
        }

        // Fire uses its own timer
        if (entity.getRemainingFireTicks() > 0) {
            entity.setRemainingFireTicks(Math.max(
                    0,
                    entity.getRemainingFireTicks() - SKIP_TICKS
            ));
        }
    }

    public void skipSingle(TimeSkipSnapshot snapshot){
        if (snapshot.getEntityId() == -1) {
            return;
        }
        Level level = self.level();

        Entity entity = level.getEntity(snapshot.getEntityId());

        if (entity == null || !entity.isAlive()) {
            return;
        }
        if (PowerTypes.isExistentiallyElsewhere(entity)){
            return;
        }
        if (entity instanceof KingCrimsonProjectionEntity kcpj){
            return;
        }
        if (entity instanceof StandEntity) {
            return;
        }
        if (entity.isPassenger()){
            return;
        }
        if (!isGravityNormal(entity))
            return;
        double distance = entity.position().distanceTo(snapshot.position);
        if (distance > getSkipBonusRange()) {
            return;
        }
        if ((entity instanceof ThrowableProjectile && !(entity instanceof GasolineCanEntity))|| entity instanceof ItemEntity) {
            entity.setDeltaMovement(entity.getDeltaMovement().scale(0));
        } else if (entity instanceof Projectile pj) {
            if (!(pj instanceof AbstractArrow aa && ((ISuperThrownAbstractArrow)aa).roundabout$getSuperThrow())) {
                if (pj instanceof ThrowableProjectile || pj instanceof AbstractArrow) {
                    Vec3 motion = pj.getDeltaMovement();

                    boolean aboutToHit = false;

                    if (!motion.equals(Vec3.ZERO)) {
                        AABB box = pj.getBoundingBox().move(motion);

                        for (VoxelShape shape : entity.level().getBlockCollisions(entity, box)) {
                            if (!shape.isEmpty()) {
                                aboutToHit = true;
                                break;
                            }
                        }
                    }

                    if (!aboutToHit) {
                        pj.setDeltaMovement(pj.getDeltaMovement().x, Math.min(0, pj.getDeltaMovement().y),
                                pj.getDeltaMovement().z);
                        pj.setDeltaMovement(motion.scale(0.4));
                    }
                }
            }
        }

        if (entity instanceof LivingEntity LE) {
            if (LE instanceof Creeper creeper && creeper.getSwelling(1) > 0){
                creeper.setSwellDir(30);
            }
            double width = entity.getBbWidth();
            double height = entity.getBbHeight();
            // Construct bounding box at the target position
            AABB targetBox = new AABB(
                    snapshot.position.x - width / 2.0, snapshot.position.y, snapshot.position.z - width / 2.0,
                    snapshot.position.x + width / 2.0, snapshot.position.y + height, snapshot.position.z + width / 2.0
            );
            targetBox = RotationUtil.boxPlayerToWorld(targetBox, ((IGravityEntity) entity).roundabout$getGravityDirection());

        for(VoxelShape $$2 : level.getBlockCollisions(entity, targetBox)) {
            if (!$$2.isEmpty()) {
                return;
            }
        }

            boolean deviousStratBlocker = ClientNetworking.getAppropriateConfig().mandomSettings.timeRewindStopsDeviousStrategies;

        boolean isStrider = entity instanceof Strider;
            if (deviousStratBlocker && (entity instanceof Player || entity.getControllingPassenger() instanceof Player)) {
                // 2. Check for dangerous blocks inside target box
                boolean cancel = false;
                for (BlockPos pos : BlockPos.betweenClosed(
                        Mth.floor(targetBox.minX), Mth.floor(targetBox.minY), Mth.floor(targetBox.minZ),
                        Mth.floor(targetBox.maxX), Mth.floor(targetBox.maxY), Mth.floor(targetBox.maxZ))) {

                    BlockState state = level.getBlockState(pos);
                    Block block = state.getBlock();

                    // List of bad blocks to avoid
                    if (block == Blocks.COBWEB || (block == Blocks.LAVA && !isStrider)
                            || block == ModBlocks.BARBED_WIRE_BUNDLE) {
                        cancel = true;
                        break;
                    }

                    // Optional: also avoid fire or cactus
                    if (block == Blocks.FIRE || block == Blocks.CACTUS
                            ) {
                        cancel = true;
                        break;
                    }
                }

                if (isSunlightDanger(entity,snapshot.position)){
                    cancel = true;
                }
                if (cancel) {
                    return;
                }
            }
        }


        if (entity instanceof AbstractMinecart am){
            MinecraftServer server = entity.level().getServer();

            am.setPos(snapshot.position.x,
                    snapshot.position.y,
                    snapshot.position.z);
            ((AccessMinecart)am).rodbt$cleardata();
        } else {
            skipFire(entity);
            if (entity instanceof LivingEntity living && entity.getId() != snapshot.entityId) {
                skipEffects(living);
            }
            if (entity instanceof PrimedTnt pt){
                pt.setFuse(1);
            }
            packetNearby(new Vector3f((float) snapshot.position.x,
                            (float) snapshot.position.y,
                            (float) snapshot.position.z),
                    entity.getId());
            entity.teleportTo(
                    snapshot.position.x,
                    snapshot.position.y,
                    snapshot.position.z
            );
            entity.setYRot(snapshot.yRot);
            entity.setYHeadRot(snapshot.yRot);
            entity.teleportTo(((ServerLevel) entity.level()), snapshot.position.x,
                    snapshot.position.y,
                    snapshot.position.z,
                    EnumSet.noneOf(RelativeMovement.class),
                    snapshot.yRot, entity.getXRot());
        }
        if (entity instanceof Mob mb && !MainUtil.isBossMob(mb)){
                mb.getNavigation().stop();
            if (!MainUtil.blockConfusionTicks(mb)) {
                ((IMob) mb).roundabout$setConfusionTicks(7);
            }
        }
    }

    private static void skipItemUse(Entity entity) {
        if (entity instanceof LivingEntity player) {
            if (!player.isUsingItem()) {
                return;
            }

            ItemStack stack = player.getUseItem();

            if (stack.isEmpty()) {
                player.stopUsingItem();
                return;
            }

            Item item = stack.getItem();

            if (item.getFoodProperties() != null || item instanceof BowlFoodItem || item instanceof PotionItem
                    || item instanceof MilkBucketItem || item instanceof SpyglassItem || item instanceof ChorusFruitItem) {
                // Force the normal vanilla completion logic
                ((StandUser) player).rdbt$completeUsingItem();
            } else if (item instanceof CrossbowItem ci){
                ci.releaseUsing(stack,entity.level(),player,0);
            } else {
                // Bow, crossbow, shield, spyglass, trident, etc.
                player.stopUsingItem();
            }
        }
    }
    public static void skipFire(Entity entity) {
        if (entity.getRemainingFireTicks() > 0) {
            entity.setRemainingFireTicks(
                    Math.max(1, entity.getRemainingFireTicks() - 100)
            );
        }
        if (entity instanceof  LivingEntity LE){
            StandUser user = ((StandUser) LE);
            if (user.roundabout$getRemainingFireTicks() > 0){
                entity.setRemainingFireTicks(
                        Math.max(1, user.roundabout$getRemainingFireTicks() - 100)
                );
            }
        }
        skipItemUse(entity);
        if (entity instanceof Player player) {
            FishingHook hook = player.fishing;
            if (hook != null) {
                hook.discard();
                player.fishing = null;
            }
            if (ClientNetworking.getAppropriateConfig().kingCrimsonSettings.enableSkippingCooldowns) {
                ItemCooldowns cds = player.getCooldowns();
                if (cds != null) {
                    ((IItemCooldowns) cds).rdbt$skipItemCooldowns(100);
                }
            }
        }
    }

    public Vec3 predictItem(ItemEntity item, int ticks) {
        Level level = item.level();

        Vec3 predicted = item.position();
        Vec3 velocity = item.getDeltaMovement();

        AABB box = item.getBoundingBox();

        for (int i = 0; i < ticks; i++) {

            // gravity
            velocity = velocity.add(0, -0.04, 0);

            Vec3 move = Entity.collideBoundingBox(
                    item,
                    velocity,
                    box,
                    level,
                    List.of()
            );

            predicted = predicted.add(move);
            box = box.move(move);

            // Hit ground
            if (move.y != velocity.y && velocity.y < 0) {
                // Item landed, stop completely
                return predicted;
            }

            // vanilla drag while airborne
            velocity = velocity.scale(0.98);

            if (predicted.y < level.getMinBuildHeight()) {
                break;
            }
        }

        return predicted;
    }

    public  void onEggHit(HitResult $$0) {
        if (!self.level().isClientSide) {
            if (self.getRandom().nextInt(8) == 0) {
                int $$1 = 1;
                if (self.getRandom().nextInt(32) == 0) {
                    $$1 = 4;
                }

                for(int $$2 = 0; $$2 < $$1; ++$$2) {
                    Chicken $$3 = (Chicken)EntityType.CHICKEN.create(self.level());
                    if ($$3 != null) {
                        $$3.setAge(-24000);
                        $$3.moveTo($$0.getLocation().x, $$0.getLocation().y, $$0.getLocation().z, 0, 0.0F);
                        self.level().addFreshEntity($$3);
                    }
                }
            }
        }
    }
    public  Vec3 predictProjectile(Projectile projectile, int ticks) {
        Level level = projectile.level();

        Vec3 pos = projectile.position();
        Vec3 velocity = projectile.getDeltaMovement();

        for (int i = 0; i < ticks; i++) {

            Vec3 nextPos = pos.add(velocity);

            // Ignore entities, collide only with blocks.
            BlockHitResult hit = level.clip(new ClipContext(
                    pos,
                    nextPos,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    projectile
            ));

            if (hit.getType() == HitResult.Type.BLOCK) {
                if (projectile instanceof Snowball || projectile instanceof ThrownEgg){
                    if (projectile instanceof ThrownEgg){
                        onEggHit(hit);
                    }
                    projectile.discard();
                }
                return pos;
                //return hit.getLocation();
            }

            pos = nextPos;

            // Vanilla-style drag
            velocity = velocity.scale(0.99);

            // Vanilla gravity
            if (!projectile.isNoGravity()) {
                if (!(projectile instanceof AbstractArrow aa && ((ISuperThrownAbstractArrow)aa).roundabout$getSuperThrow())){
                    if (projectile instanceof AbstractArrow || projectile instanceof ThrowableProjectile) {
                        float gravity = -0.05F;
                        if (projectile instanceof ThrowableProjectile aa) {
                            gravity = -1 * ((AccessThrowableProjectile) aa).rdbt$getGravity();
                        }

                        velocity = velocity.add(0.0, gravity, 0.0);
                    }
                }
            }
        }

        return pos;
    }

    private void skipFallingBlock(FallingBlockEntity falling, int ticks) {
        if (falling.isRemoved())
            return;

        for (int i = 0; i < ticks; i++) {
            if (falling.isRemoved())
                break;

            falling.tick();
        }
    }

    public void timeSkip(boolean skipSelf) {
        if (!(self instanceof ServerPlayer pl)) {
            return;
        }
        if (isUsingTimeErase()){
            return;
        }
        if (!canUseTimeSkip()){
            return;
        }
        if (onCooldown(PowerIndex.SKILL_2_SNEAK)){
            return;
        }
        setCooldown(PowerIndex.SKILL_2_SNEAK,
                ClientNetworking.getAppropriateConfig().kingCrimsonSettings.timeSkipCooldown);
        if (ClientNetworking.getAppropriateConfig().kingCrimsonSettings.cooldownSplit){
            setCooldown(PowerIndex.SKILL_4,
                    ClientNetworking.getAppropriateConfig().kingCrimsonSettings.timeSkipCooldown);
        }

        self.fallDistance = 0;

        skipBlockEntities(100);
        skipDayTime(100);
        skipFire(self);
        skipEffects(self);
        if (epitaph.isEmpty()) {
            basicSkip(skipSelf);
            return;
        }
        List<FallingBlockEntity> fallingBlocks = new ArrayList<>();
        AABB area = self.getBoundingBox().inflate(getSkipRange());
        for (Entity entity : self.level().getEntitiesOfClass(Entity.class, area)) {
            if (entity instanceof Projectile proj){
                if (proj instanceof FireworkRocketEntity) {
                    proj.discard();
                } else {
                    epitaph.put(proj.getId(), new TimeSkipSnapshot(
                            proj.getId(),
                            predictProjectile(proj, 40),
                            proj.getXRot(),
                            proj.getYRot()
                    ));
                }
            } else if (entity instanceof ItemEntity it) {
                Vec3 predicted = predictItem(it, 100);
                skip_dump.put(it.getId(), new TimeSkipSnapshot(
                        it.getId(),
                        predicted,
                        it.getXRot(),
                        it.getYRot()
                ));
            } else if (entity instanceof FallingBlockEntity fbe){
                fallingBlocks.add(fbe);
            }
        }

        for (TimeSkipSnapshot snapshot : epitaph.values()) {
            if (!skipSelf && snapshot.getEntityId() == self.getId()){
                continue;
            }
            skipSingle(snapshot);
        }

        if (!fallingBlocks.isEmpty()) {
            fallingBlocks.sort(
                    Comparator.comparingDouble(entity -> entity.position().y)
            );


            for (FallingBlockEntity falling : fallingBlocks) {
                skipFallingBlock(falling, 100);
            }
        }

        S2CPacketUtil.sendCancelSoundPacket(pl,this.self.getId(),EPITAPH_NOISE);
        playStandUserOnlySoundsIfNearby(TIME_SKIP_2, getSkipBonusRange(), true, false);
        scatterPackets();
        epitaph.clear();
        S2CPacketUtil.clearEpitaph(pl);
    }

    public void scatterPackets(){
        packetNearby2();
    }
    int skipRange = 50;
    public int getSkipRange(){
        return 50;
    }
    public int getSkipBonusRange(){
        return getSkipRange()+25;
    }
    public final void packetNearby2() {
        if (!this.self.level().isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) this.self.level());
            Vec3 userLocation = new Vec3(this.self.getX(),  this.self.getY(), this.self.getZ());
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) this.self.level()).players().get(j);

                if (((ServerLevel) serverPlayerEntity.level()) != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(userLocation, getSkipBonusRange())) {
                    S2CPacketUtil.sendSimpleByteToClientPacket(serverPlayerEntity,PacketDataIndex.TIME_SKIP);
                }
            }
        }
    }
    public final void packetNearby(Vector3f blip, int entId) {
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

    public boolean canSkip(Entity entity){
        if (entity instanceof FallenMob fm && !fm.getActivated()){
            return false;
        }
        return true;
    }
    public void epitaph() {
        if (self instanceof ServerPlayer pl) {
            if (isUsingTimeErase()){
                return;
            }
            if (epitaph.isEmpty()) {
                if (onCooldown(PowerIndex.SKILL_2_SNEAK) && !canUseEpitaphWithoutSkip()){
                    return;
                }
                //debugPlayer();
                AABB area = self.getBoundingBox().inflate(getSkipRange());

                for (Entity entity : self.level().getEntitiesOfClass(Entity.class, area)) {
                    if (!isGravityNormal(entity))
                        continue;
                    if (!canSkip(entity))
                        continue;

                    if (entity instanceof KingCrimsonProjectionEntity kcpj){
                        continue;
                    }
                    if (entity instanceof LivingEntity lv && !(PowerTypes.isExistentiallyElsewhere(lv))) {
                        StandEntity stand = getStandEntity(self);
                        int id = entity.getId();
                        if (!(stand != null && stand.getId() == id)) {
                            if (!(entity instanceof StandEntity) &&
                                    !(entity instanceof Player pk && pk.isCreative()
                                            && pk.getId() != self.getId())
                            ) {
                                Vec3 predicted = entity.position();
                                float xRot = entity.getXRot();
                                float yRot = entity.getYRot();
                                if (!lv.isSleeping()) {
                                    if (entity instanceof Mob mob) {
                                        if (!mob.isLeashed() && !(mob instanceof WanderingTrader)) {
                                            predicted = predictPosition(mob, 100);
                                        }
                                    } else if (entity instanceof Player player) {
                                        // Fallback for players, armor stands, etc.
                                        hitWall2 = false;
                                        predicted = predictPlayer(player, 40);
                                        if (hitWall2) {
                                            yRot = Mth.wrapDegrees(yRot + 180.0F);
                                        }
                                    }
                                }


                                epitaph.put(entity.getId(), new TimeSkipSnapshot(
                                        id,
                                        predicted,
                                        xRot,
                                        yRot
                                ));
                                S2CPacketUtil.addEpitaph(pl, id, predicted, xRot, yRot);
                            }
                        }
                    } else if (entity instanceof Boat bt){
                        Vec3 predicted = entity.position();
                        float xRot = entity.getXRot();
                        float yRot = entity.getYRot();
                        if (bt.getControllingPassenger() instanceof Player) {
                            predicted = predictBoat(bt, 40);
                        }
                        epitaph.put(entity.getId(), new TimeSkipSnapshot(
                                entity.getId(),
                                predicted,
                                xRot,
                                yRot
                        ));
                        S2CPacketUtil.addEpitaph(pl, entity.getId(), predicted, xRot, yRot);
                    } else if (entity instanceof PrimedTnt tnt) {
                        Vec3 predicted = predictTNT(tnt, 100);

                        epitaph.put(
                                entity.getId(),
                                new TimeSkipSnapshot(
                                        entity.getId(),
                                        predicted,
                                        entity.getXRot(),
                                        entity.getYRot()
                                )
                        );
                        S2CPacketUtil.addEpitaph(pl, entity.getId(), predicted, entity.getXRot(),
                                entity.getYRot());
                    } else if (entity instanceof AbstractMinecart bt){
                        Vec3 predicted = entity.position();
                        float xRot = entity.getXRot();
                        float yRot = entity.getYRot();
                        predicted = predictMinecart(bt,40);
                        epitaph.put(entity.getId(), new TimeSkipSnapshot(
                                entity.getId(),
                                predicted,
                                xRot,
                                yRot
                        ));
                        S2CPacketUtil.addEpitaph(pl, entity.getId(), predicted, xRot, yRot);
                    }

                }
                epitaph.put(-1, new TimeSkipSnapshot(
                        -1,
                        Vec3.ZERO,
                        0,
                        0
                ));
                S2CPacketUtil.addEpitaph(pl, -1,  Vec3.ZERO, 0, 0);
                S2CPacketUtil.sendPlaySoundPacket(pl,this.self.getId(),EPITAPH_NOISE);
                S2CPacketUtil.sendCancelSoundPacket(pl,this.self.getId(),EPITAPH_FADE_NOISE);
            } else {

                setCooldown(PowerIndex.SKILL_1,
                        ClientNetworking.getAppropriateConfig().kingCrimsonSettings.epitaphCooldown);
                S2CPacketUtil.sendPlaySoundPacket(pl,this.self.getId(),EPITAPH_FADE_NOISE);
                S2CPacketUtil.sendCancelSoundPacket(pl,this.self.getId(),EPITAPH_NOISE);
                epitaph.clear();
                S2CPacketUtil.clearEpitaph(pl);
            }

        }
    }

    @Override
    public float multiplyPowerByStandConfigPlayers(float power){
        return (float) (power*(ClientNetworking.getAppropriateConfig().
                theWorldSettings.theWorldAttackMultOnPlayers *0.01));
    }
    @Override
    public float getImpalePunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers((float) (4F * (ClientNetworking.getAppropriateConfig().
                    generalStandSettings.generalImpaleAttackMultiplier *0.01))));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs((float) (20.1F * (ClientNetworking.getAppropriateConfig().
                    generalStandSettings.generalImpaleAttackMultiplier *0.01))));
        }
    }

    public float getBloodSplashStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers((float) (1.5F)));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs((float) (6F * (ClientNetworking.getAppropriateConfig().
                    generalStandSettings.generalImpaleAttackMultiplier *0.01))));
        }
    }
        @Override
    public StandEntity getNewStandEntity() {
        byte sk = ((StandUser) this.getSelf()).roundabout$getStandSkin();
        return ModEntities.KING_CRIMSON.create(this.getSelf().level());
    }
    @Override
    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }
    public static Component getSkinNameT(byte skinId){
        if (skinId == KingCrimsonEntity.MANGA_SKIN){
            return Component.translatable(  "skins.roundabout.king_crimson.manga");
        } if (skinId == KingCrimsonEntity.END){
            return Component.translatable(  "skins.roundabout.king_crimson.end");
        } if (skinId == KingCrimsonEntity.END_2){
            return Component.translatable(  "skins.roundabout.king_crimson.end_2");
        } if (skinId == KingCrimsonEntity.STARLESS){
            return Component.translatable(  "skins.roundabout.king_crimson.starless");
        } if (skinId == KingCrimsonEntity.HEAVEN){
            return Component.translatable(  "skins.roundabout.king_crimson.heaven");
        }if (skinId == KingCrimsonEntity.AGOGO){
            return Component.translatable(  "skins.roundabout.king_crimson.agogo");
        }if (skinId == KingCrimsonEntity.SPINE_ART){
            return Component.translatable(  "skins.roundabout.king_crimson.spine_art");
        }if (skinId == KingCrimsonEntity.GREEN){
            return Component.translatable(  "skins.roundabout.king_crimson.green");
        }if (skinId == KingCrimsonEntity.YELLOW){
            return Component.translatable(  "skins.roundabout.king_crimson.yellow");
        }if (skinId == KingCrimsonEntity.AQUA){
            return Component.translatable(  "skins.roundabout.king_crimson.aqua");
        }if (skinId == KingCrimsonEntity.BLACK){
            return Component.translatable(  "skins.roundabout.king_crimson.black");
        }if (skinId == KingCrimsonEntity.DARK){
            return Component.translatable(  "skins.roundabout.king_crimson.dark");
        }if (skinId == KingCrimsonEntity.BETA){
            return Component.translatable(  "skins.roundabout.king_crimson.beta");
        }if (skinId == KingCrimsonEntity.CONCEPT){
            return Component.translatable(  "skins.roundabout.king_crimson.concept");
        }if (skinId == KingCrimsonEntity.PART_5_SKIN){
            return Component.translatable(  "skins.roundabout.king_crimson.base");
        }if (skinId == KingCrimsonEntity.BLUE){
            return Component.translatable(  "skins.roundabout.king_crimson.blue");
        }if (skinId == KingCrimsonEntity.VISION){
            return Component.translatable(  "skins.roundabout.king_crimson.vision");
        }
        return Component.translatable(  "skins.roundabout.king_crimson.red");
    }
    @Override
    public boolean cancelSprintJump(){
        if (this.getActivePower() == PowerIndex.POWER_1_SNEAK
                || this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE ||
        isChargingBloodSplash()){
            return true;
        }
        return super.cancelSprintJump();
    }
    @Override
    public boolean canInterruptPower(DamageSource sauce, Entity interrupter) {
        if (isUsingEpitaph() && ClientNetworking.getAppropriateConfig().kingCrimsonSettings.epitaphInterrupt) {
            epitaph();
        }
        if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            int cdr = ClientNetworking.getAppropriateConfig().generalStandSettings.impaleAttackCooldown;
            if (this.getSelf() instanceof Player) {
                S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_1_SNEAK, cdr);
            }
            this.setCooldown(PowerIndex.SKILL_1_SNEAK, cdr);
            return true;
        }
        return super.canInterruptPower(sauce,interrupter);
    }

        @Override
    public List<Byte> getSkinList() {
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(KingCrimsonEntity.RED);
        if (this.getSelf() instanceof Player PE) {
            byte Level = ((IPlayerEntity) PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser) PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);

            $$1.add(KingCrimsonEntity.PART_5_SKIN);
            $$1.add(KingCrimsonEntity.MANGA_SKIN);
            if (Level > 1 || bypass) {
                $$1.add(KingCrimsonEntity.VISION);
                $$1.add(KingCrimsonEntity.SPINE_ART);
                $$1.add(KingCrimsonEntity.AGOGO);
            } if (Level > 2 || bypass) {
                $$1.add(KingCrimsonEntity.BLUE);
                $$1.add(KingCrimsonEntity.BLACK);
                $$1.add(KingCrimsonEntity.DARK);
            } if (Level > 3 || bypass) {
                $$1.add(KingCrimsonEntity.HEAVEN);
                $$1.add(KingCrimsonEntity.AQUA);
                $$1.add(KingCrimsonEntity.YELLOW);
                $$1.add(KingCrimsonEntity.GREEN);
            } if (Level > 4 || bypass) {
                $$1.add(KingCrimsonEntity.STARLESS);
                $$1.add(KingCrimsonEntity.CONCEPT);
                $$1.add(KingCrimsonEntity.BETA);
            } if (((IPlayerEntity)PE).roundabout$getUnlockedBonusSkin() || bypass){
                $$1.add(KingCrimsonEntity.END);
                $$1.add(KingCrimsonEntity.END_2);
            }
        }
        return $$1;
    }
    @Override
    public void powerActivate(PowerContext context) {
        switch (context)
        {
            case SKILL_1_NORMAL,SKILL_1_GUARD-> {
                epitaphClient();
            }
            case SKILL_1_CROUCH -> {
                impaleClient();
            }

            case SKILL_2_NORMAL -> {
                timeSkipClient();
            }
            case SKILL_2_GUARD -> {
                timeSkipSelfClient();
            }
            case SKILL_2_CROUCH -> {
                itemGrabClient();
            }
            case SKILL_3_GUARD, SKILL_3_CROUCH_GUARD -> {
                handsActiveClient();
            }
            case SKILL_3_NORMAL -> {
                tryToDashClient();
            }
            case SKILL_3_CROUCH -> {
                tryBloodClient();
            }
            case SKILL_4_NORMAL,SKILL_4_CROUCH -> {
                timeEraseClient();
            }
            case SKILL_4_GUARD,SKILL_4_CROUCH_GUARD -> {
                projectionClient();
            }
        }
    }

    public void itemGrabClient(){
        if (hasHandsOut())
            return;
        super.itemGrabClient();
    }

    public Vec3 getEpitaphColors(){
        byte sk = ((StandUser) this.getSelf()).roundabout$getStandSkin();
        if (sk == KingCrimsonEntity.MANGA_SKIN){
            return new Vec3(0.5,0.5,0.5);
        } if (sk == KingCrimsonEntity.STARLESS){
            return new Vec3(1,0,0.5);
        } if (sk == KingCrimsonEntity.BETA){
            return new Vec3(1.5,0,0);
        } if (sk == KingCrimsonEntity.DARK){
            return new Vec3(0,0,0);
        } if (sk == KingCrimsonEntity.GREEN){
            return new Vec3(0,1,0);
        } if (sk == KingCrimsonEntity.YELLOW){
            return new Vec3(1,1,0);
        } if (sk == KingCrimsonEntity.AQUA){
            return new Vec3(0.2,0.5,1);
        } if (sk == KingCrimsonEntity.END || sk == KingCrimsonEntity.END_2){
            return new Vec3(0.75,0,1.5);
        }
        return new Vec3(1,0,1);
    }


    public void handsActiveClient(){
        if (!onCooldown(PowerIndex.SKILL_EXTRA_2)) {
            if (!hasBlock() && canAttackHeavy()) {
                tryPowerPacket(PowerIndex.POWER_3_BLOCK);
                setCooldown(PowerIndex.SKILL_EXTRA_2, 7);
            }
        }
    }

    public void tryBloodClient(){

        if (!hasBlock()) {
            if (!doVault()) {
                if (!onCooldown(PowerIndex.SKILL_3)) {
                        if (hasHandsOut())
                            return;
                        tryPower(PowerIndex.POWER_3,true);
                        tryPowerPacket(PowerIndex.POWER_3);
                }
            }
        }
    }
    public boolean cancelSprintParticles(){
        return super.cancelSprintParticles() || isChargingBloodSplash();
    }
    /**Cancel all sprinting*/
    public boolean cancelSprint(){
        return super.cancelSprint() || isChargingBloodSplash();
    }
    public boolean isChargingBloodSplash(){
        return activePower == PowerIndex.SKILL_3;
    }

    public void standBloodShot(){
        if (this.self instanceof Player){
            if (isPacketPlayer()){
                this.setAttackTimeDuring(-13);
                impaleTicks = 15;
                tryPowerPacket(PowerIndex.POWER_3_SNEAK_EXTRA);
            }
        } else {
            shootBloodServer();
        }
    }

    public void shootBloodServer(){
        animateStand(KingCrimsonEntity.BLOOD_SPLASH_THROW);
        setAttackTimeDuring(-13);
        BloodSplatterEntity bloodsplash = new BloodSplatterEntity(self, self.level());
        bloodsplash.healthAmt = 1;
        float SHOOT_POWER = 1.5F;
        if (!isUsingTimeErase()) {
            bloodsplash.setSplatterType((byte) 1);

            this.self.level().playSound(null, this.self.blockPosition(),
                    ModSounds.KING_BLOOD_SPLASH_EVENT,
                    SoundSource.PLAYERS, 1F, (float) (0.99F + Math.random() * 0.02));
        } else {
            bloodsplash.setSplatterType((byte) 2);
        }
        bloodsplash.shootFromRotation(self, self.getXRot(), self.getYRot(), -7, SHOOT_POWER, 1.5F);
        bloodsplash.setPos(self.getPosition(1).add((self.getEyePosition().subtract(self.getPosition(1))).scale(0.5f)));
        self.level().addFreshEntity(bloodsplash);
    }
    public void updateBloodShot(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > 13) {
                this.standBloodShot();
            }
        }
    }
    public void bloodSplash() {
        if (hasHandsOut())
            return;
        setActivePower(PowerIndex.POWER_3);
        setAttackTimeDuring(0);
        setCooldown(PowerIndex.SKILL_3, 140);
        animateStand(KingCrimsonEntity.BLOOD_SPLASH_WINDUP);
        if (self.level() instanceof ServerLevel sl) {
            int bloodTime = 240;
            MobEffectInstance instance = self.getEffect(ModEffects.BLEED);
            boolean isBigOuchie = false;
            if (instance != null){
                if (instance.getDuration() > 0) {
                    isBigOuchie = instance.getAmplifier() > 0;
                    bloodTime += instance.getDuration();
                }
            }
            if (!isUsingTimeErase()) {
                if (!isBigOuchie) {
                    if (!(self instanceof Player pl && pl.isCreative())) {
                        MainUtil.makeBleed(self, 0, bloodTime, self);
                        this.self.level().playSound(null, this.self.blockPosition(),
                                ModSounds.KING_CRIMSON_PUNCH_EVENT,
                                SoundSource.PLAYERS, 1F, (float) (1.2F + Math.random() * 0.05));
                    }
                    ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.BLOOD,
                            self.getEyePosition().x(), self.getEyePosition().y(), self.getEyePosition().z(),
                            30, 0, 0, 0, 0.1);
                } else {
                    ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.BLOOD,
                            self.getEyePosition().x(), self.getEyePosition().y() + 0.3F, self.getEyePosition().z(),
                            30, 0, 0, 0, 0.3);
                }
                this.self.level().playSound(null, this.self.blockPosition(),
                        ModSounds.VAMPIRE_DRAIN_EVENT,
                        SoundSource.PLAYERS, 1F, (float) (0.9F + Math.random() * 0.2));
            } else {
                if (self instanceof ServerPlayer sp){
                    S2CPacketUtil.sendPlaySoundPacket(sp, sp.getId(), PowersKingCrimson.DRAIN_NOISE);
                }
            }
        }
    }
    public void projectionClient(){
        if (!onCooldown(PowerIndex.SKILL_4_SNEAK) && !isUsingTimeErase()) {
            if (!hasBlock()) {
                if (!isUsingEpitaph()) {
                    ClientUtil.sendControlData();
                    tryPowerPacket(PowerIndex.POWER_4_SNEAK);
                }
            }
        }
    }
    public void hologram() {
        if (onCooldown(PowerIndex.SKILL_4_SNEAK)) {
            return;
        }
        if (isUsingTimeErase()){
            return;
        }
        if (isUsingEpitaph()) {
            return;
        }

        setCooldown(PowerIndex.SKILL_4_SNEAK, 240);

        if (!(getSelf() instanceof Player player) || player.level().isClientSide()) {
            return;
        }

        KingCrimsonProjectionEntity clone =
                ModEntities.KING_CRIMSON_PROJECTION.create(player.level());

        if (clone == null) {
            return;
        }

        Level level = player.level();

        // Where the player is looking
        Vec3 start = player.getEyePosition();
        Vec3 end = start.add(player.getLookAngle().scale(20.0));

        // Hit a block if possible
        BlockHitResult hit = level.clip(new ClipContext(
                start,
                end,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        Vec3 target = hit.getType() == HitResult.Type.MISS
                ? end
                : hit.getLocation();

        BlockPos pos = BlockPos.containing(target);

        // Search downward for ground
        while (pos.getY() > level.getMinBuildHeight()
                && !level.getBlockState(pos).isSolid()) {
            pos = pos.below();
        }

        // Stand on top of the first solid block
        pos = pos.above();

        // Make sure there's room for a player-sized entity
        while (pos.getY() < level.getMaxBuildHeight() - 2) {
            if (!level.getBlockState(pos).isSolid()
                    && !level.getBlockState(pos.above()).isSolid()) {
                break;
            }
            pos = pos.above();
        }

        clone.moveTo(
                pos.getX() + 0.5,
                pos.getY(),
                pos.getZ() + 0.5,
                player.getYRot(),
                player.getXRot()
        );

        clone.user = player;
        clone.setYRot(self.getYRot());
        clone.setXRot(self.getXRot());
        clone.setYBodyRot(self.yBodyRot);
        clone.setYHeadRot(self.getYHeadRot());
        clone.lifespan = 160;
        clone.pkc = this;
        playStandUserOnlySoundsIfNearby(EPITAPH_PROJECTION, 40, false, false);
        level.addFreshEntity(clone);
        AABB search = clone.getBoundingBox().inflate(40);

        for (Mob mob : self.level().getEntitiesOfClass(Mob.class, search)) {

            LivingEntity targetT = mob.getTarget();

            if (targetT != player) {
                continue;
            }
            if (MainUtil.isBossMob(targetT)){
                continue;
            }

            if (!mob.hasLineOfSight(clone)) {
                continue;
            }
            if (((StandUser)mob).roundabout$hasAStand() || (mob instanceof NeutralMob)){
                continue;
            }

            float yaw = mob.yHeadRot * Mth.DEG_TO_RAD;
            Vec3 forward = new Vec3(-Mth.sin(yaw), 0.0, Mth.cos(yaw));
            Vec3 toClone = clone.position().subtract(mob.position()).normalize();

            if (forward.dot(toClone) <= 0.0) {
                continue; // Clone is behind the mob
            }

            double distToPlayer = mob.distanceToSqr(player);
            double distToProjection = mob.distanceToSqr(clone);

            if (distToProjection < distToPlayer) {
                ((StandUser) mob).roundabout$aggressivelyEnforceAggro(clone);
            }
        }
    }

    public void timeEraseClient(){
        if (!onCooldown(PowerIndex.SKILL_4)) {
            if (!hasBlock() && canAttackLight()) {
                ClientUtil.sendControlData();
                tryPowerPacket(PowerIndex.POWER_4);
            }
        }
    }

    // Code for additional cooldown penalty for running in a fight
    // Only applies in pvp
    @Override
    public void onActuallyHurt(DamageSource $$0, float $$1){
        if (!self.level().isClientSide() && $$0.getEntity() instanceof Player pl &&
        pl.getId() != self.getId()) {
            setDisengageTarget($$0.getEntity());
            disengageTime = 600;
        }
    }
    public void setDisengageTarget(Entity target) {
        if (self instanceof ServerPlayer sp) {
            if (target instanceof Player pl &&
                    pl.getId() != self.getId()){
                disengageTarget = target;
            } else {
                if (target == null){
                    disengageTarget = target;
                } else {
                    return;
                }
            }
            if (target != null) {
                S2CPacketUtil.sendGenericIntToClientPacket(sp,
                        PacketDataIndex.S2C_STAND_SPECIAL_INT,
                        disengageTarget.getId());
            } else {
                S2CPacketUtil.sendGenericIntToClientPacket(sp,
                        PacketDataIndex.S2C_STAND_SPECIAL_INT,
                        -1);
            }
        }
    }
    @Override
    public boolean interceptDamageDealtEvent(DamageSource $$0, float $$1, LivingEntity target){
        if (!self.level().isClientSide() && target instanceof Player pl &&  pl.getId() != self.getId()) {
            setDisengageTarget(target);
        disengageTime = 600;

        }
        return false;
    }


    public int getDisengageDistance(){
        return 25;
    }
    public int disengageTime = 0;
    public boolean isBeyondRange(){
        if (self.level().isClientSide()){
            disengageTarget = self.level().getEntity(disengageTargetInt);
        }

        if (disengageTarget != null && disengageTarget.isAlive() &&
                disengageTarget.distanceTo(self) > getDisengageDistance()){
            return true;
        }
        return false;
    }
    public Entity disengageTarget = null;
    public int disengageTargetInt = -1;

    public int getTimeEraseCooldown(){
        int maxTicks = timeEraseMaxTicks();
        int ticksEaten = maxTicks - ticksOfEraseLeft;
        ticksEaten = Math.max(ticksEaten,0);

        int cooldownOverall = ClientNetworking.getAppropriateConfig().
                kingCrimsonSettings.timeEraseMinimumCooldown;
        cooldownOverall += (int)(((float)ticksEaten)
                *((ClientNetworking.getAppropriateConfig().kingCrimsonSettings.
                additionalCooldownPerSecondsUsed2 *0.05)));

        if (isBeyondRange()) {
            cooldownOverall+=ClientNetworking.getAppropriateConfig().
                    kingCrimsonSettings.additionalCooldownFromPlayerRunning;
        }

        return cooldownOverall;
    }

    public int getTimeEraseCooldownMax(){
        int maxTicks = timeEraseMaxTicks();
        int ticksEaten = maxTicks;
        ticksEaten = Math.max(ticksEaten,0);

        int cooldownOverall = ClientNetworking.getAppropriateConfig().
                kingCrimsonSettings.timeEraseMinimumCooldown;
        cooldownOverall += (int)(((float)ticksEaten)
                *((ClientNetworking.getAppropriateConfig().kingCrimsonSettings.
                additionalCooldownPerSecondsUsed2 *0.05)));

        if (isBeyondRange()) {
            cooldownOverall+=ClientNetworking.getAppropriateConfig().
                    kingCrimsonSettings.additionalCooldownFromPlayerRunning;
        }

        return cooldownOverall;
    }

    public void timeErase() {
        if (!self.level().isClientSide() && self instanceof ServerPlayer sp) {
            if (onCooldown(PowerIndex.SKILL_4)) {
                return;
            }
            if (isUsingEpitaph())
                return;
            if (timeEraseActive){
                timeEraseActive = false;
                setCooldown(PowerIndex.SKILL_4,getTimeEraseCooldown());
                if (ClientNetworking.getAppropriateConfig().kingCrimsonSettings.cooldownSplit) {
                    setCooldown(PowerIndex.SKILL_2_SNEAK,
                            ClientNetworking.getAppropriateConfig().kingCrimsonSettings.timeSkipCooldown);
                }
                S2CPacketUtil.sendCancelSoundPacket(sp,this.self.getId(),TIME_ERASE);

                packetNearby2();
                playStandUserOnlySoundsIfNearby(TIME_ERASE_END, getSkipBonusRange(), true, false);
                saveDiscAndSync();

                if (activeClone != null){
                    activeClone.discardStand();
                    activeClone.discard();
                }
                applyBloodSplatterEffects();
                if (fakedDeath){
                    if (!self.level().isClientSide && self.level().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES)) {

                        double range = getSkipBonusRange();
                        double rangeSqr = range * range;

                        Component message = Component.translatable("text.roundabout.time_erase",
                                self.getDisplayName()).withStyle(ChatFormatting.BOLD).
                                withStyle(ChatFormatting.WHITE);

                        for (ServerPlayer player : ((ServerLevel) self.level()).players()) {
                            if (player.distanceToSqr(self) <= rangeSqr) {
                                player.sendSystemMessage(message);
                            }
                        }
                    }
                    fakedDeath = false;
                }
            } else {
                spawnClone();
                timeEraseActive = true;
                self.stopUsingItem();
                if (hasBlock()){
                    resetItem();
                }
                ticksOfEraseLeft = timeEraseMaxTicks()-1;
                S2CPacketUtil.sendSimpleByteToClientPacket(sp,PacketDataIndex.TIME_SKIP);
                S2CPacketUtil.sendPlaySoundPacket(sp, this.self.getId(), TIME_ERASE);
                S2CPacketUtil.sendCancelSoundPacket(sp,this.self.getId(),TIME_ERASE_END);
                saveDiscAndSync();
                ticksOfEraseLeft++;
            }
        }
    }
    @Override
    public boolean isAppropriateToGrab(){
        if (!hasBlock()) {
            return true;
        }
        return false;
    }
    public void timeSkipSelfClient() {

        if (isUsingTimeErase()){
            //blood
            return;
        }
        if (onCooldown(PowerIndex.SKILL_2_SNEAK)){
            return;
        }
        if (hasBlock()){
            return;
        }
        if (!canUseTimeSkip()){
            return;
        }
        if (isUsingEpitaph()){
            tryPowerPacket(PowerIndex.EXTRA);
        }
    }

    @Override
    public void onItemGrab(){
        if (isErasingTime()){
            timeErase();
        }
    }
    public void timeSkipClient() {

        if (isUsingTimeErase()){
            itemGrabClient();
            return;
        }
        if (onCooldown(PowerIndex.SKILL_2_SNEAK)){
            return;
        }
        if (!canUseTimeSkip()){
            return;
        }
        if (hasBlock()){
            itemGrabClient();
            return;
        }

        boolean isMoving = (Math.abs(self.getDeltaMovement().x) > 0.01 ||
                Math.abs(self.getDeltaMovement().z) > 0.01 ||
                !self.onGround());
        if (isMoving && !isUsingEpitaph()){
            tryPowerPacket(PowerIndex.EXTRA);
        } else {
            tryPowerPacket(PowerIndex.POWER_2);
        }
    }


    public void epitaphClient(){
        if (isUsingTimeErase()){
            impaleClient();
            return;
        }
        if (onCooldown(PowerIndex.SKILL_2_SNEAK) && !canUseEpitaphWithoutSkip()){
            return;
        }
        if (this.onCooldown(PowerIndex.SKILL_1)) {
            return;
        }
        if (hasBlock())
            return;
        tryPowerPacket(PowerIndex.POWER_1);
    }

    public void tryToDashClient(){
        if (hasBlock())
            return;
        if (!doVault()) {
            dash();
        }
    }


    public int getImpaleLevel(){
        return 1;
    }
    public void impaleClient(){
        if (!canImpale()){
            return;
        }
        if (hasHandsOut())
            return;

        if (hasBlock())
            return;
        if (!this.onCooldown(PowerIndex.SKILL_1_SNEAK)) {
            if (canExecuteMoveWithLevel(getImpaleLevel())) {
                if (this.activePower == PowerIndex.POWER_1_SNEAK) {
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                    tryPowerPacket(PowerIndex.NONE);
                } else {
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1_SNEAK, true);
                    tryPowerPacket(PowerIndex.POWER_1_SNEAK);
                }
            }
        }
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (!isHoldingSneak() && !isUsingTimeErase()){
            LockedOrNot(context, x, y, 1, StandIcons.KING_CRIMSON_EPITAPH, PowerIndex.SKILL_1, 0);
        } else {
            LockedOrNot(context, x, y, 1, StandIcons.KING_CRIMSON_IMAPLE, PowerIndex.SKILL_1_SNEAK,getImpaleLevel());
        }

        if (!isHoldingSneak() && !isUsingTimeErase()){
            if (hasBlock()){
                LockedOrNot(context, x, y, 2, StandIcons.KING_CRIMSON_ITEM_GRAB, PowerIndex.SKILL_2,getImpaleLevel());

            } else if (isUsingEpitaph()){
                if (isGuarding()){
                    LockedOrNot(context, x, y, 2, StandIcons.TIME_SKIP_3, PowerIndex.SKILL_2_SNEAK, 0);
                } else {
                    LockedOrNot(context, x, y, 2, StandIcons.TIME_SKIP_2, PowerIndex.SKILL_2_SNEAK, 0);
                }
            } else {
                LockedOrNot(context, x, y, 2, StandIcons.TIME_SKIP, PowerIndex.SKILL_2_SNEAK, 0);
            }
        } else {
            LockedOrNot(context, x, y, 2, StandIcons.KING_CRIMSON_ITEM_GRAB, PowerIndex.SKILL_2,getImpaleLevel());
        }

        if (isGuarding()) {
            setSkillIcon(context, x, y, 3, StandIcons.KING_CRIMSON_HANDS_ACTIVE,
                    PowerIndex.SKILL_EXTRA_2);
        } else if (canVault()){
            setSkillIcon(context, x, y, 3, StandIcons.KING_CRIMSON_LEDGE_GRAB,
                    PowerIndex.GLOBAL_DASH);
        } else {
            if (!isHoldingSneak()){
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
            } else {
                setSkillIcon(context, x, y, 3, StandIcons.KING_CRIMSON_BLOOD_SPLASH,
                        PowerIndex.SKILL_3);
            }
        }
        if (isGuarding() && !isUsingTimeErase() && !isUsingEpitaph()) {
            LockedOrNot(context, x, y, 4, StandIcons.HOLOGRAM, PowerIndex.SKILL_4_SNEAK, 0);
        } else if (!isHoldingSneak()){
            LockedOrNot(context, x, y, 4, StandIcons.TIME_ERASE, PowerIndex.SKILL_4, 0);
        } else {
            LockedOrNot(context, x, y, 4, StandIcons.TIME_ERASE, PowerIndex.SKILL_4,0);
        }
    }

    public boolean fakedDeath = false;

    @Override
    public boolean isWip(){
        return true;
    }
    @Override
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);
    }
    @Override
    public Component ifWipListDev(){
        return Component.literal(  "Hydra").withStyle(ChatFormatting.GOLD);
    }
    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        StandUser standUser = ((StandUser) playerEntity);
        boolean standOn = PowerTypes.hasStandActive(playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;
        if (hasArmsOut){
            int barTexture = 0;
            Entity TE = getTargetEntity(playerEntity, 3, getBrawlPunchAngle());
            float attackTimeMax = getAttackTimeMax();
            if (attackTimeMax > 0) {
                float attackTime = getAttackTime();
                float finalATime = attackTime / attackTimeMax;
                if (finalATime <= 1) {

                    if (getActivePowerPhase() == getActivePowerPhaseMax()) {
                        barTexture = 24;
                    } else if (TE != null && isBrawling()) {
                        barTexture = 12;
                    } else {
                        barTexture = 18;
                    }


                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
                    int finalATimeInt = Math.round(finalATime * 15);
                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, barTexture, finalATimeInt, 6);

                }
            }
            if (standOn) {
                if (TE != null) {
                    if (barTexture == 0) {
                        context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
                    }
                }
            }
        } else if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            Entity TE = this.getTargetEntity(playerEntity, impaleRange);
            if (TE != null) {
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
            }
        } else if (standOn && this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE){
            float zamn = ((float) attackTimeDuring / getMaxSuperHitTime());
            int ClashTime = Math.min(15,Math.round(zamn * 15));
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 111, 15, 6);
            if (zamn >= 1){
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 132, ClashTime, 6);
            } else if (crossedThreshold2(zamn)){
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 118, ClashTime, 6);
            } else {
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 125, ClashTime, 6);
            }
        } else {
            super.renderAttackHud(context,playerEntity,
                    scaledWidth,scaledHeight,ticks,vehicleHeartCount, flashAlpha, otherFlashAlpha);
        }
    }

    @Override
    public float getBrawlPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 0.75F;
        } else {
            return 3.4F;
        }
    }
    public boolean crossedThreshold(){
        float zamn = ((float) attackTimeDuring / getMaxSuperHitTime());
        return crossedThreshold2(zamn);
    }
    public boolean crossedThreshold2(float zamn){
        return zamn >= 0.5F;
    }

    public boolean isErasingTime(){
        return timeEraseActive;
    }

    @Override
    public boolean interceptIncomingHarm(DamageSource $$0, float $$1){
        if (timeEraseActive){
            if (!MainUtil.isSpecialDamage($$0)){
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean interceptDamageDealtEventTrue(DamageSource $$0, float $$1, LivingEntity target){
        if (timeEraseActive){
            timeErase();
        }
        return false;
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        if (hasArmsOut && (move == PowerIndex.BARRAGE || move == PowerIndex.BARRAGE_CHARGE
        || move == PowerIndex.SNEAK_ATTACK_CHARGE || move == PowerIndex.SNEAK_ATTACK))
            return false;
        if (!this.getSelf().level().isClientSide && this.getActivePower() == PowerIndex.POWER_1_SNEAK) {
            this.stopSoundsIfNearby(IMPALE_NOISE, 100,true);
        }
        return super.tryPower(move,forced);
    }

    @Override
    public void refreshArms(){
        if (!self.level().isClientSide()) {
            isRenderingArms = true;
            handTicks = getMaxHandTicks();
        }
        super.refreshArms();
    }

    @Override
    public boolean setPowerAttack(){
        if (hasArmsOut) {
            setAttack();
            return false;
        }
        return super.setPowerAttack();
    }

    @Override
    public void setAttack(){
        if (!self.level().isClientSide()){
            if (isUsingTimeErase()){
                timeErase();
            }
        }
        super.setAttack();
    }
    //hold input
    public boolean holdDownClick = false;
    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (!consumeClickInput) {
            if (impaleTicks > 0){
                return;
            }
            if (hasArmsOut){
                if (keyIsDown) {
                    if (activePowerPhase == 0) {
                        this.tryPower(PowerIndex.ATTACK);
                    }
                }
                holdDownClick = false;
            } else {
                if (holdDownClick) {
                    if (keyIsDown) {

                    } else {
                        if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
                            C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_STAND_TRIGGER_2);
                            int atd = this.getAttackTimeDuring();
                            this.tryIntPower(PowerIndex.SNEAK_ATTACK, true, atd);
                            tryIntPowerPacket(PowerIndex.SNEAK_ATTACK, atd);
                        }
                        holdDownClick = false;
                    }
                } else {
                    if (keyIsDown) {
                        if (!isHoldingSneak()) {
                            if (isErasingTime()) {
                                C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_STAND_TRIGGER_2);
                            }
                            super.buttonInputAttack(keyIsDown, options);
                        } else {
                            if (this.canAttack()) {
                                this.tryPower(PowerIndex.SNEAK_ATTACK_CHARGE, true);
                                holdDownClick = true;
                                tryPowerPacket(PowerIndex.SNEAK_ATTACK_CHARGE);
                            } else {
                                if (isErasingTime()) {
                                    C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_STAND_TRIGGER_2);
                                }
                                super.buttonInputAttack(keyIsDown, options);
                            }
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

    public void switchHands(){
        if (!self.level().isClientSide()){
            this.poseStand(OffsetIndex.FOLLOW);
            animateStand(StandEntity.IDLE);
            xTryPower(PowerIndex.NONE,true);
            if (!hasArmsOut){
                StandEntity stand = getStandUserSelf().roundabout$getStand();
                if (stand != null){
                    stand.forceDespawn(true);
                }
                isRenderingArms = true;

                if (!self.isCrouching()) {
                    playStandUserOnlySoundsIfNearby(SUMMON_ARMS, 10, true, false);
                }
            }
            hasArmsOut = !hasArmsOut;
            saveDiscAndSync();
        }
    }

    @Override
    /**Stand related things that slow you down or speed you up*/
    public float inputSpeedModifiers(float basis){
        if (this.activePower == PowerIndex.SNEAK_ATTACK_CHARGE) {
            if (this.getSelf().isCrouching()) {
                float f = Mth.clamp(0.3F + EnchantmentHelper.getSneakingSpeedBonus(this.getSelf()), 0.0F, 1.0F);
                float g = 1 / f;
                basis *= g;
            }
            basis *= 0.3f;
        } else if (this.getActivePower()==PowerIndex.POWER_3){
            basis *= 0.3f;
        } else if (this.getActivePower()==PowerIndex.POWER_1_SNEAK){
            if (this.getSelf().isCrouching()){
                float f = Mth.clamp(0.3F + EnchantmentHelper.getSneakingSpeedBonus(this.getSelf()), 0.0F, 1.0F);
                float g = 1/f;
                basis *= g;
            }
        }
        return super.inputSpeedModifiers(basis);
    }
    @Override
    public void updateUniqueMoves() {
        /*Tick through Time Stop Charge*/
        if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            updateImpale();
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK){
            updateFinalAttack();
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE){
            updateFinalAttackCharge();
        } else if (this.getActivePower() == PowerIndex.POWER_3) {
            this.updateBloodShot();
        }
        super.updateUniqueMoves();
    }

    public int chargedFinal;

    public void updateImpale(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring == 7 && isPacketPlayer() && isErasingTime() && self.level().isClientSide()) {
                C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_STAND_TRIGGER_2);
            }
            if (this.attackTimeDuring > 24) {
                this.standImpale();
            } else {
                if (!this.getSelf().level().isClientSide()) {
                    if(this.attackTimeDuring%4==0) {
                        ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.MENACING,
                                this.getSelf().getX(), this.getSelf().getY() + 0.3, this.getSelf().getZ(),
                                1, 0.2, 0.2, 0.2, 0.05);
                    }
                }
            }
        }
    }

    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime){
        if (move == PowerIndex.SNEAK_ATTACK) {
                this.chargedFinal = chargeTime;
        }
        return super.tryIntPower(move, forced, chargeTime);
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.VAULT){
            return this.vault();
        } else if (move == PowerIndex.POWER_1_SNEAK){
            return this.impale();
        } else if (move == PowerIndex.POWER_1){
            this.epitaph();
        } else if (move == PowerIndex.POWER_2){
            this.timeSkip(false);
            return true;
        } else if (move == PowerIndex.EXTRA){
            this.timeSkip(true);
            return true;
        } else if (move == PowerIndex.SNEAK_ATTACK_CHARGE){
            return this.setPowerFinalAttack();
        } else if (move == PowerIndex.SNEAK_ATTACK){
            return this.setPowerSuperHit();
        } else if (move == PowerIndex.POWER_4){
           this.timeErase();
           return true;
        } else if (move == PowerIndex.POWER_3){
            this.bloodSplash();
            return true;
        } else if (move == PowerIndex.POWER_3_BLOCK){
            this.switchHands();
            return true;
        } else if (move == PowerIndex.POWER_3_SNEAK_EXTRA){
            this.shootBloodServer();
            return true;
        } else if (move == PowerIndex.POWER_4_SNEAK){
            this.hologram();
            return true;
        }
        return super.setPowerOther(move,lastMove);
    }
    public boolean setPowerSuperHit() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.SNEAK_ATTACK);
        this.poseStand(OffsetIndex.ATTACK);
        chargedFinal = Math.min(this.chargedFinal,getMaxSuperHitTime());
        animateFinalAttackHit();
        //playBarrageCrySound();
        return true;
    }
    @Override
    public boolean setPowerBarrageCharge() {
        if (hasArmsOut)
            return false;
        if (!self.level().isClientSide()){
            if (isUsingTimeErase()){
                timeErase();
            }
        }
        return super.setPowerBarrageCharge();
    }
    @Override
    public void handleStandAttack(Player player, Entity target){
        if (this.getActivePower() == PowerIndex.POWER_1_SNEAK){
            impaleImpact(target);
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK){
            finalAttackImpact(target);
        }
    }
    public void animateFinalAttack(){
        animateStand(StandEntity.FINAL_ATTACK_WINDUP);
    }

    public void animateFinalAttackHit(){
        float charged = getChargedPercent();
        if (charged >= 1F){
            animateStand(KingCrimsonEntity.FINAL_2);
            return;
        } else if (charged >= 0.5F){
            animateStand(KingCrimsonEntity.FINAL_1);
            return;
        }
        animateStand((byte) 86);
    }

    public boolean setPowerFinalAttack() {
        animateFinalAttack();
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.SNEAK_ATTACK_CHARGE);
        this.poseStand(OffsetIndex.GUARD);
        this.clashDone = false;
        return true;
    }
    public static final float impaleRange = 3.5F;
    public void standImpale(){
        /*By setting this to -10, there is a delay between the stand retracting*/

        if (this.self instanceof Player){
            if (isPacketPlayer()){
                this.setAttackTimeDuring(-20);
                impaleTicks = 15;
                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK,getTargetEntityId2(impaleRange));
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,impaleRange);
            impaleImpact(targetEntity);
        }

    }

    public void updateFinalAttack(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring == 5) {
                this.standFinalAttack();
            }
        }
    }

    public void standFinalAttack(){

        this.setAttackTimeMax(ClientNetworking.getAppropriateConfig().generalStandSettings.finalPunchAndKickMinimumCooldown + chargedFinal);
        this.setAttackTime(0);
        this.setActivePowerPhase(this.getActivePowerPhaseMax());

        if (this.self instanceof Player){
            if (isPacketPlayer()){
                this.attackTimeDuring = -10;
                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK,getTargetEntityId());
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,-1);
            finalAttackImpact(targetEntity);
        }
    }

    public void finalAttackImpact(Entity entity){
        this.setAttackTimeDuring(-20);

        if (entity != null && entity.distanceTo(self) > 5.5F) {
            entity = null;
        }
        if (entity != null) {
            float charged = getChargedPercent();
            hitParticlesCenter(entity);
            float pow;
            float knockbackStrength;
            pow = getFinalPunchStrength(entity);
            knockbackStrength = getFinalAttackKnockback();
            if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                if (entity instanceof LivingEntity LE) {
                    if (charged >= 1) {
                        addEXP(5, LE);
                    } else if (charged > 0.5F){
                        MainUtil.makeBleed(LE, 0, 200, this.self);
                        addEXP(2, LE);
                    }
                }
                takeDeterminedKnockbackWithY(this.self, entity, knockbackStrength);
            } else {
                if (chargedFinal >= getMaxSuperHitTime()) {
                    if (charged >= 1) {
                        knockShield2(entity, 70);
                    } else if (charged > 0.5F){
                        knockShield2(entity, 50);
                    }
                }
            }
        } else {
            // This is less accurate raycasting as it is server sided but it is important for particle effects
            float distMax = this.getDistanceOut(this.self, this.getReach(), false);
            float halfReach = (float) (distMax * 0.5);
            Vec3 pointVec = DamageHandler.getRayPoint(self, halfReach);
            if (!this.self.level().isClientSide) {
                ((ServerLevel) this.self.level()).sendParticles(ModParticles.PUNCH_MISS, pointVec.x, pointVec.y, pointVec.z,
                        1, 0.0, 0.0, 0.0, 1);
            }
        }

        SoundEvent SE;
        float pitch = 1F;
        if (entity != null) {
            SE = getFinalAttackSound();
            pitch = getFinalAttackPitch();
        } else {
            SE = ModSounds.PUNCH_2_SOUND_EVENT;
        }

        if (!this.self.level().isClientSide()) {
            this.self.level().playSound(null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
        }
    }
    public SoundEvent getFinalAttackSound(){
        float charged = getChargedPercent();
        if (charged >= 1F){
            return ModSounds.KING_CRIMSON_PUNCH_5_EVENT;
        } else if (charged >= 0.5F){
            return ModSounds.KING_CRIMSON_PUNCH_4_EVENT;
        }
        return ModSounds.KING_CRIMSON_PUNCH_3_EVENT;
    }
    public float getFinalAttackPitch(){
        float charged = getChargedPercent();
        if (charged >= 1F){
            return 1;
        } else if (charged >= 0.5F){
            return 1;
        }
        return 1.2F;
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot){
        if (hasBlock()){
            return true;
        }

        if (hasArmsOut){
            if (slot ==1){
                if (isHoldingSneak() || isErasingTime()){
                    return true;
                }
            } if (slot ==2){
                if (isHoldingSneak() || isErasingTime()){
                    return true;
                }
            }  if (slot ==3){
                if (isHoldingSneak() && !canVault() && !isGuarding()){
                    return true;
                }
            }
        }

        if (slot == 1 && !isHoldingSneak() && onCooldown(PowerIndex.SKILL_2_SNEAK)){
            if (!canUseEpitaphWithoutSkip()) {
                return true;
            }
        }
        return super.isAttackIneptVisually(activeP,slot);
    }

    @Override
    public byte getThrowStyleType(){
        return ThrownObjectEntity.TWTHROW;
    }

    public float getFinalAttackKnockback(){
        float charge = getChargedPercent();
        if (charge >= 1){
            return (((float)this.chargedFinal /(float)getMaxSuperHitTime())*3);
        } else if (charge >= 0.5F){
            return 0.7F;
        }
        return 0.1F;
    }
    public float getFinalPunchStrength(Entity entity){
        float punchD = this.getPunchStrength(entity)*2+this.getHeavyPunchStrength(entity);
        if (this.getReducedDamage(entity)){
            float ret = (getChargedPercent()*punchD);
            if (this.chargedFinal >= getMaxSuperHitTime()){
                ret +=0.5F;
            }
            return ret;
        } else {
            float ret = (getChargedPercent()*punchD)+3;
            if (this.chargedFinal >= getMaxSuperHitTime()){
                ret +=2;
            }
            return ret;
        }
    }


    @Override
    public float getPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(1.35F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(5));
        }
    }
    @Override
    public float getHeavyPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(1.89F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(6F));
        }
    }

    public float getChargedPercent(){
        return (((float)this.chargedFinal/(float)getMaxSuperHitTime()));
    }

    public int getMaxSuperHitTime(){
        return 30+(getMeltLevel()*2);
    }

    public void updateFinalAttackCharge(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring == 14 && isErasingTime() && self.level().isClientSide()) {
                C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_STAND_TRIGGER_2);
            }
            if (this.attackTimeDuring >= 60) {
                if (this.getSelf() instanceof Player && this.getSelf().level().isClientSide && this.isPacketPlayer()){
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                    tryPowerPacket(PowerIndex.NONE);
                }
            } else if (this.attackTimeDuring >= getMaxSuperHitTime() && !(this.getSelf() instanceof Player)){
                ((StandUser) this.getSelf()).roundabout$tryIntPower(PowerIndex.SNEAK_ATTACK, true,getMaxSuperHitTime());
            }
        }
    }

    @Override
    public float getPunchLandPitch(){
        return 1.3F + 0.07F * activePowerPhase;
    }
    @Override
    public float getPunchLandLastPitch(){
        return 1F;
    }

    @Override
    public SoundEvent getPunchLandSound(){
        return ModSounds.KING_CRIMSON_PUNCH_EVENT;
    }
    @Override
    public SoundEvent getPunchLandLastSound(){
        return ModSounds.KING_CRIMSON_PUNCH_2_EVENT;
    }

}