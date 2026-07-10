package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.PWBigMeteorEntity;
import net.hydra.jojomod.entity.projectile.PWMeteorEntity;
import net.hydra.jojomod.entity.stand.PlanetWavesEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;

import static net.hydra.jojomod.entity.stand.PlanetWavesEntity.*;

public class PowersPlanetWaves extends NewDashPreset {
    public PowersPlanetWaves(LivingEntity self) {super(self);}

    @Override
    public StandEntity getNewStandEntity(){
        return ModEntities.PLANET_WAVES.create(this.getSelf().level());
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersPlanetWaves(entity);
    }

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
        return Component.literal(  "Lloyd10").withStyle(ChatFormatting.YELLOW);
    }
    @Override
    public List<Byte> getSkinList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(PlanetWavesEntity.PART_6_SKIN);
        $$1.add(PlanetWavesEntity.MANGA_SKIN);
        if (this.getSelf() instanceof Player PE){
            byte Level = ((IPlayerEntity)PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser)PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (Level > 1 || bypass){
                $$1.add(PlanetWavesEntity.BLUE_SKIN);
                $$1.add(PlanetWavesEntity.PURPLE_SKIN);
                $$1.add(PlanetWavesEntity.GREEN_SKIN);
            } if (Level > 2 || bypass){
                $$1.add(PlanetWavesEntity.OCEAN_WAVES);
                $$1.add(PlanetWavesEntity.SYMPHONY_WAVES);
            }if (Level > 3 || bypass){
                $$1.add(PlanetWavesEntity.SPARTA);
                $$1.add(PlanetWavesEntity.SPARTA2);
            }/* if (Level > 4 || bypass){
                $$1.add(MagiciansRedEntity.GREEN_SKIN);
                $$1.add(MagiciansRedEntity.GREEN_ABLAZE);
            } if (Level > 5 || bypass){
                $$1.add(MagiciansRedEntity.BLUE_SKIN);
                $$1.add(MagiciansRedEntity.BLUE_ABLAZE);
                $$1.add(MagiciansRedEntity.BLUE_ACE_SKIN);
                $$1.add(MagiciansRedEntity.SKELETAL);
            } if (Level > 6 || bypass){
                $$1.add(MagiciansRedEntity.JOJONIUM);
                $$1.add(MagiciansRedEntity.JOJONIUM_ABLAZE);
                $$1.add(MagiciansRedEntity.MAGMA_SKIN);
                $$1.add(MagiciansRedEntity.DEBUT_SKIN);
                $$1.add(MagiciansRedEntity.BETA);
            } if (((IPlayerEntity)PE).roundabout$getUnlockedBonusSkin() || bypass){
                $$1.add(MagiciansRedEntity.DREAD_BEAST_SKIN);
                $$1.add(MagiciansRedEntity.DREAD_SKIN);
                $$1.add(MagiciansRedEntity.DREAD_ABLAZE);
            }*/
        }
        return $$1;
    }

    @Override public Component getSkinName(byte skinId) {
        switch (skinId)
        {
            case PART_6_SKIN -> {return Component.translatable("skins.roundabout.planet_waves.base");}
            case MANGA_SKIN  -> {return Component.translatable("skins.roundabout.planet_waves.manga");}
            case BLUE_SKIN  -> {return Component.translatable("skins.roundabout.planet_waves.blue");}
            case PURPLE_SKIN  -> {return Component.translatable("skins.roundabout.planet_waves.purple");}
            case GREEN_SKIN  -> {return Component.translatable("skins.roundabout.planet_waves.green");}
            case OCEAN_WAVES  -> {return Component.translatable("skins.roundabout.planet_waves.ocean_waves");}
            case SYMPHONY_WAVES  -> {return Component.translatable("skins.roundabout.planet_waves.symphony_waves");}
            case SPARTA  -> {return Component.translatable("skins.roundabout.planet_waves.sparta");}
            case SPARTA2  -> {return Component.translatable("skins.roundabout.planet_waves.sparta2");}

        }
        return Component.translatable("skins.roundabout.planet_waves.base");

    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        setSkillIcon(context, x, y, 1, StandIcons.PLANET_WAVES_METEOR_SHOWER, PowerIndex.SKILL_1);
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);

        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 2, StandIcons.PLANET_WAVES_FORCED_DISINTEGRATION, PowerIndex.SKILL_2_SNEAK);
            if (canExecuteMoveWithLevel(MeteorTrackingLevel())) {
                setSkillIcon(context, x, y, 4, StandIcons.PLANET_WAVES_METEOR_TRACKING, PowerIndex.SKILL_4_SNEAK);
            } else {
                setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.SKILL_4_SNEAK);
            }
        } else {
            setSkillIcon(context, x, y, 2, StandIcons.PLANET_WAVES_BIG_METEOR, PowerIndex.SKILL_2);
            if (canExecuteMoveWithLevel(StandTargetingLevel())) {
                if (!instandtargeting()) {
                    setSkillIcon(context, x, y, 4, StandIcons.PLANET_WAVES_STAND_TARGETING, PowerIndex.SKILL_4);
                } else {
                    setSkillIcon(context, x, y, 4, StandIcons.PLANET_WAVES_STAND_RETRIEVING, PowerIndex.SKILL_4);
                }
            } else {
                setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.SKILL_4);
            }
        }
    }
    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        if (activeP == PowerIndex.SKILL_1 || activeP == PowerIndex.SKILL_2 || activeP == PowerIndex.SKILL_2_SNEAK) {
            if (isTravelling) {
                return true;
            }
        }

        if (activeP == PowerIndex.SKILL_4) {
            if (isTravelling || isBlockInvalidForTargeting()) {
                return true;
            }
        }

        if (activeP == PowerIndex.SKILL_2_SNEAK) {

            List<PWBigMeteorEntity> meteors = this.self.level().getEntitiesOfClass(
                    PWBigMeteorEntity.class,
                    this.self.getBoundingBox().inflate(256)
            );

            for (PWBigMeteorEntity meteor : meteors) {
                if (meteor.getStandUser() == this.self) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean isStandEnabled() {
        return ClientNetworking.getAppropriateConfig().PlanetWavesSettings.enablePlanetWaves;

    }

    public int StandTargetingLevel(){
        return 3;
    }
    public int MeteorTrackingLevel(){
        return 4;
    }
    @Override
    public byte getMaxLevel(){
        return 4;
    }

    @Override
    public int getExpForLevelUp(int currentLevel){
        int amt;
        if (currentLevel == 1) {
            amt = 100;
        } else if (currentLevel == 2){
            amt = 200;
        } else {
            amt = 400;
        }
        amt= (int) (amt*(getLevelMultiplier()));
        return amt;
    }

    @Override
    public void levelUp(){
        if (!this.getSelf().level().isClientSide() && this.getSelf() instanceof Player PE){
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            byte level = ipe.roundabout$getStandLevel();
            if (level == 4) {
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.max.both").
                        withStyle(ChatFormatting.AQUA), true);
            } else { if(level== 3){
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.both").
                        withStyle(ChatFormatting.AQUA), true);
                }else ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.skins").
                    withStyle(ChatFormatting.AQUA), true);
            }
        }
        super.levelUp();
    }

    private boolean targetingstand = false;
    private boolean tracking = false;
    private Vec3 standTargetPos = null;
    private Vec3 standSurfacePos = null;
    private Vec3 standTargetLook = null;
    private Vec3 standApproachDir = null;
    public boolean instandtargeting(){
        return this.targetingstand;
    }
    public boolean inmeteortracking(){
        return this.tracking;
    }


    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move) {
            case PowerIndex.POWER_1 -> { // Meteor Shower
                meteorshower();
                return true;
            }
            case PowerIndex.POWER_2 -> { // Big Meteor
                bigmeteor();
                return true;
            }
            case PowerIndex.POWER_4 -> { // Stand Targeting or Stand Retrieving
                if (!instandtargeting()) {
                    attemptStandTargeting();
                } else {
                    attemptStandRetrieving();
                }
                return true;
            }
            case PowerIndex.POWER_4_SNEAK -> { // Meteor Tracking
                if (!inmeteortracking()) {
                    attemptMeteorTracking();
                } else {
                    attemptMeteorNotTracking();
                }
                return true;
            }
            case PowerIndex.POWER_2_SNEAK -> { // Meteor Dissapearence
                meteorDisappearance();
                return true;
            }
        }
        return super.setPowerOther(move, lastMove);
    }
    public void attemptStandTargeting(){
        if(canExecuteMoveWithLevel(StandTargetingLevel())) {
            standtargeting();
        }
    }
    public void attemptStandRetrieving(){
        if(canExecuteMoveWithLevel(StandTargetingLevel())) {
            usertargeting();
        }
    }
    public void attemptMeteorTracking(){
        if(canExecuteMoveWithLevel(MeteorTrackingLevel())) {
            meteortracking();
        }
    }
    public void attemptMeteorNotTracking(){
        if(canExecuteMoveWithLevel(MeteorTrackingLevel())) {
            meteornottracking();
        }
    }
    private void meteorshower() {
        if(isTravelling)return;
        if (this.onCooldown(PowerIndex.SKILL_1)) return;
        if (this.self.level().dimension() == Level.NETHER) return;

        Level level = this.self.level();
        if (level.isClientSide()) return;

        Vec3 eyePos = this.self.getEyePosition(1.0F);
        Vec3 lookVec = this.self.getViewVector(1.0F).normalize();

        double spawnDistance = ClientNetworking.getAppropriateConfig()
                .PlanetWavesSettings.meteorshowerDistance;

        Vec3 spawnPos = eyePos.add(lookVec.scale(spawnDistance));

        if (this.self.level().dimension() != Level.END) {
            double minSpawnY;
            if (instandtargeting() && standTargetPos != null) {
                minSpawnY = standTargetPos.y + 5.0; // no nacer debajo del nivel del objetivo
            } else {
                minSpawnY = this.self.getY() + 5.0;
            }
            if (spawnPos.y < minSpawnY) {
                spawnPos = new Vec3(
                        spawnPos.x,
                        minSpawnY,
                        spawnPos.z
                );
            }
        }
        Vec3 targetPos;

        if (instandtargeting() && standTargetPos != null) {

            targetPos = standTargetPos;

        } else if (this.self.isShiftKeyDown()) {


            targetPos = new Vec3(
                    this.self.getX(),
                    this.self.getY() - 0.5D,
                    this.self.getZ()
            );

        } else {


            targetPos = this.self.getEyePosition(1.0F);
        }

        Vec3 direction = targetPos.subtract(spawnPos).normalize();

        PWMeteorEntity meteor = new PWMeteorEntity(this.self, level);
        meteor.setTargetPos(targetPos);
        meteor.setUser(this.self);
        meteor.setOwner(this.self);
        meteor.setTrackingUser(inmeteortracking());

        meteor.absMoveTo(spawnPos.x, spawnPos.y, spawnPos.z);
        meteor.shoot(direction.x, direction.y, direction.z, 1.8F, 0.0F);

        meteor.setChain(0, true);

        level.addFreshEntity(meteor);

        level.playSound(null, this.self.blockPosition(),
                ModSounds.PLANET_WAVES_METEOR_SHOWER_EVENT,
                net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);

        this.setCooldown(PowerIndex.SKILL_1, ClientNetworking.getAppropriateConfig()
                .PlanetWavesSettings.meteorshowerCooldown);
        if (this.getSelf() instanceof ServerPlayer sp) {
            S2CPacketUtil.sendCooldownSyncPacket(sp, PowerIndex.SKILL_1,
                    ClientNetworking.getAppropriateConfig().PlanetWavesSettings.meteorshowerCooldown);
        }
        syncStandMode();
    }

    private static class ScheduledMeteor {
        int delay;
        Vec3 spawnPos;
        Vec3 targetPos;

        ScheduledMeteor(int delay, Vec3 spawnPos, Vec3 targetPos) {
            this.delay = delay;
            this.spawnPos = spawnPos;
            this.targetPos = targetPos;
        }
    }

    private final List<ScheduledMeteor> meteorQueue = new java.util.ArrayList<>();
    public void tick() {
        if (self.level().isClientSide()) {
            clientTickVisualYaw();
            return;
        }

        for (int i = 0; i < meteorQueue.size(); i++) {
            ScheduledMeteor m = meteorQueue.get(i);
            m.delay--;
            if (m.delay <= 0) {
                spawnMeteor(m.spawnPos, m.targetPos);
                meteorQueue.remove(i);
                i--;
            }
        }
    }
    private void spawnMeteor(Vec3 spawnPos, Vec3 targetPos) {
        Level level = this.self.level();
        if (level.isClientSide()) return;

        Vec3 direction = targetPos.subtract(spawnPos).normalize();

        PWMeteorEntity meteor = new PWMeteorEntity(this.self, level);
        meteor.setTargetPos(targetPos);
        meteor.setUser(this.self);
        meteor.setOwner(this.self);

        meteor.absMoveTo(spawnPos.x, spawnPos.y, spawnPos.z);
        meteor.shoot(direction.x, direction.y, direction.z, 1.8F, 0.0F);

        level.addFreshEntity(meteor);
    }

    private void bigmeteor() {
        if(isTravelling)return;
        Level level = this.self.level();
        if (level.isClientSide()) return;
        if (this.self.level().dimension() == Level.NETHER) return;
        if (this.onCooldown(PowerIndex.SKILL_2)) return;

        Vec3 eyePos = this.self.getEyePosition(1.0F);
        Vec3 lookVec = this.self.getViewVector(1.0F).normalize();

        double spawnDistance = ClientNetworking.getAppropriateConfig()
                .PlanetWavesSettings.bigmeteorDistance;

        Vec3 spawnPos = eyePos.add(lookVec.scale(spawnDistance));


        if (this.self.level().dimension() != Level.END) {
            double minSpawnY;
            if (instandtargeting() && standTargetPos != null) {
                minSpawnY = standTargetPos.y + 5.0; // no nacer debajo del nivel del objetivo
            } else {
                minSpawnY = this.self.getY() + 5.0;
            }
            if (spawnPos.y < minSpawnY) {
                spawnPos = new Vec3(
                        spawnPos.x,
                        minSpawnY,
                        spawnPos.z
                );
            }
        }

        Vec3 targetPos;

        if (instandtargeting() && standTargetPos != null) {

            targetPos = standTargetPos;

        } else if (this.self.isShiftKeyDown()) {

            targetPos = new Vec3(
                    this.self.getX(),
                    this.self.getY() - 1.0D,
                    this.self.getZ()
            );

        } else {

            targetPos = this.self.getEyePosition(1.0F);
        }

        Vec3 direction = targetPos.subtract(spawnPos).normalize();

        PWBigMeteorEntity meteor = new PWBigMeteorEntity(this.self, level);

        meteor.absMoveTo(spawnPos.x, spawnPos.y, spawnPos.z);


        if (!level.noCollision(meteor)) {
            return;
        }

        meteor.setTargetPos(targetPos);
        meteor.setUser(this.self);
        meteor.setOwner(this.self);

        meteor.shoot(direction.x, direction.y, direction.z, 1.8F, 0.0F);

        level.addFreshEntity(meteor);


        this.setCooldown(PowerIndex.SKILL_2, ClientNetworking.getAppropriateConfig()
                .PlanetWavesSettings.bigmeteorCooldown);
        if (this.getSelf() instanceof ServerPlayer sp) {
            S2CPacketUtil.sendCooldownSyncPacket(sp, PowerIndex.SKILL_2,
                    ClientNetworking.getAppropriateConfig().PlanetWavesSettings.bigmeteorCooldown);
        }

        level.playSound(
                null,
                this.self.blockPosition(),
                ModSounds.PLANET_WAVES_BIG_METEOR_EVENT,
                net.minecraft.sounds.SoundSource.PLAYERS,
                1.0F,
                1.0F
        );
    }
    private boolean isBlockInvalidForTargeting() {
        Vec3 eyePos = this.self.getEyePosition(1.0F);
        Vec3 lookVec = this.self.getViewVector(1.0F);
        Vec3 endPos = eyePos.add(lookVec.scale(128.0D));

        ClipContext clipContext = new ClipContext(
                eyePos, endPos,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                this.self
        );

        BlockHitResult hitResult = this.self.level().clip(clipContext);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = hitResult.getBlockPos();
            var state = this.self.level().getBlockState(pos);
            boolean isFullBlock = state.isCollisionShapeFullBlock(this.self.level(), pos);
            boolean isOpaque = state.canOcclude();
            if (!isFullBlock || !isOpaque) return true;

            if (eyePos.distanceTo(Vec3.atCenterOf(pos)) > 30.0) return true;

            return false;
        }

        return true;
    }
    private net.minecraft.core.Direction standHitDirection = null;
    private boolean isTravelling = false;
    private Vec3 standTravelTarget = null;

    public double buryDepthHorizontal = 0.5; //0 superficie, 1 adentro
    public double buryDepthUp = 0.5;
    public double buryDepthDown = 0.5;

    private float standTargetYaw = 0f;
    private float standTargetYawAligned = 0f;
    private float currentStandYawDeg = 0f;
    private float preSinkStartYawDeg = 0f;

    private float syncedTargetYaw = 0f;
    private int clientPreSinkTicks = 0;
    private float clientPreSinkStartYaw = 0f;

    private void standtargeting() {
        Level level = this.self.level();
        if (this.onCooldown(PowerIndex.SKILL_4)) return;
        Vec3 eyePos  = this.self.getEyePosition(1.0F);
        Vec3 lookVec = this.self.getViewVector(1.0F);
        Vec3 endPos  = eyePos.add(lookVec.scale(128.0D));

        ClipContext clipContext = new ClipContext(
                eyePos, endPos,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                this.self
        );
        BlockHitResult hitResult = this.self.level().clip(clipContext);
        if (hitResult.getType() != HitResult.Type.BLOCK) return;

        BlockPos pos = hitResult.getBlockPos();

        var state = this.self.level().getBlockState(pos);
        if (!state.isCollisionShapeFullBlock(this.self.level(), pos) || !state.canOcclude()) return;
        if (state.isAir()) return;
        if (eyePos.distanceTo(Vec3.atCenterOf(pos)) > 30.0) return; //  límite de 30 bloques de stand targeting
        Vec3 horizontalLook = new Vec3(lookVec.x, 0, lookVec.z);
        this.standApproachDir = (horizontalLook.lengthSqr() > 1.0E-4)
                ? horizontalLook.normalize()
                : new Vec3(0, 0, 1);
        this.standTargetYaw = (float) (Mth.atan2(-this.standApproachDir.x, this.standApproachDir.z)
                * (180.0 / Math.PI));

        Vec3 blockCenter = Vec3.atLowerCornerOf(pos).add(0.5, 0.5, 0.5);
        Vec3 faceNormal  = Vec3.atLowerCornerOf(hitResult.getDirection().getNormal());
        Vec3 faceCenter  = blockCenter.add(faceNormal.scale(0.5));

        this.standTargetPos = faceCenter;

        boolean isHorizontal = hitResult.getDirection() != net.minecraft.core.Direction.UP
                && hitResult.getDirection() != net.minecraft.core.Direction.DOWN;

        this.standTargetYawAligned = isHorizontal
                ? (float) (Mth.atan2(faceNormal.x, -faceNormal.z) * (180.0 / Math.PI))
                : this.standTargetYaw;

        Vec3 visualFaceCenter = faceCenter;
        if (isHorizontal) {
            StandEntity standForHeight = this.getStandEntity(this.self);
            float standHeight = (standForHeight != null) ? standForHeight.getBbHeight() : 1.95f;
            visualFaceCenter = faceCenter.subtract(0, standHeight / 2.0, 0);
        }
        this.standSurfacePos = visualFaceCenter;

        double sinkDepth;
        switch (hitResult.getDirection()) {
            case UP -> {
                sinkDepth = 0.85;
                this.standTravelTarget = visualFaceCenter;
            }
            case DOWN -> {
                sinkDepth = -0.85;
                this.standTravelTarget = visualFaceCenter;
            }
            default -> {
                sinkDepth = 0.0;
                this.standTravelTarget = visualFaceCenter.add(faceNormal.scale(0.85));
            }
        }

        this.sinkTarget = visualFaceCenter.subtract(faceNormal.scale(sinkDepth));
        this.standHitDirection = hitResult.getDirection();
        restrainAnimationType  = 0;

        targetingstand = true;
        isTravelling   = true;
        isSinking      = false;
        buryEffectTick = 0;

        StandEntity stand = this.getStandEntity(this.self);
        if (stand != null) {
            stand.setStandRotationX(-90.0F * Mth.DEG_TO_RAD);
            setStandYaw(stand, this.standTargetYaw);
        }
        if (stand instanceof FollowingStandEntity FSE) {
            FSE.setOffsetType(OffsetIndex.LOOSE);
        }

        syncStandMode();

        level.playSound(null, this.self.blockPosition(),
                ModSounds.PLANET_WAVES_TARGET_EVENT,
                SoundSource.PLAYERS, 1.0F, 1.0F);

        if (!level.isClientSide()) {
            this.setCooldown(PowerIndex.SKILL_4,
                    ClientNetworking.getAppropriateConfig()
                            .PlanetWavesSettings.standtargetingCooldown);
        }
    }

    private LivingEntity restrainedEntity = null;
    private byte restrainAnimationType = 0; // 0 = side, 1 = above, 2 = below
    private int grabCooldownTicks = 0;
    private static final int GRAB_COOLDOWN = 60;
    private boolean isSinking = false;
    private Vec3 sinkTarget = null;
    private boolean wasSinking = false;
    private boolean wasTravelling = false;
    private int restrainHoldTicks = 0;
    private int preSinkTicks = 0;
    private static final int PRE_SINK_DURATION = 10; // 0.5 segundos = 10 ticks
    private boolean isPreSinking = false;

    private void setStandYaw(StandEntity stand, float yawDeg) {
        this.currentStandYawDeg = yawDeg;
        if (stand != null) {
            stand.setStandRotationY(yawDeg * Mth.DEG_TO_RAD);
        }
    }
    private void clientTickVisualYaw() {
        if (!isPreSinking) return;
        StandEntity stand = this.getStandEntity(this.self);
        if (stand == null) return;

        if (clientPreSinkTicks > 0) {
            clientPreSinkTicks--;
            float progress = 1.0f - (clientPreSinkTicks / (float) PRE_SINK_DURATION);
            float delta = Mth.wrapDegrees(syncedTargetYaw - clientPreSinkStartYaw);
            setStandYaw(stand, clientPreSinkStartYaw + delta * progress);
        } else {
            setStandYaw(stand, syncedTargetYaw);
        }
    }
    private BlockPos getBuriedBlockPos() {
        if (standTargetPos == null || standHitDirection == null) return null;

        Vec3 normal = Vec3.atLowerCornerOf(standHitDirection.getNormal());
        Vec3 inside = standTargetPos.subtract(normal.scale(0.5));

        return BlockPos.containing(inside);
    }
    private int buryEffectTick = 0;

    private void playBuriedEffects() {
        if (!(self.level() instanceof ServerLevel level)) return;
        if (standSurfacePos == null) return;

        BlockPos pos = getBuriedBlockPos();
        if (pos == null) return;

        BlockState state = level.getBlockState(pos);
        if (state.isAir()) return;

        level.sendParticles(
                new BlockParticleOption(ParticleTypes.BLOCK, state),
                standSurfacePos.x,
                standSurfacePos.y,
                standSurfacePos.z,
                8,
                0.15, 0.15, 0.15,
                0.02
        );
    }
    private void playBuriedSound() {
        if (!(self.level() instanceof ServerLevel level)) return;

        BlockPos pos = getBuriedBlockPos();
        if (pos == null) return;

        BlockState state = level.getBlockState(pos);

        if (state.isAir()) return;

        SoundType sound = state.getSoundType();

        level.playSound(
                null,
                pos,
                sound.getHitSound(), // o getBreakSound()
                SoundSource.BLOCKS,
                sound.getVolume(),
                sound.getPitch()
        );
    }
    @Override
    public void tickPowerEnd() {
        super.tickPowerEnd();
        if (self.level().isClientSide()) return;
        if (grabCooldownTicks > 0) grabCooldownTicks--;
        StandEntity stand = this.getStandEntity(this.self);
        // ── 0. CHECK IF BLOCK WAS DESTROYED ────────────────────────────────────
        if (targetingstand && !isTravelling && !isSinking && standTargetPos != null) {
            BlockPos checkPos = BlockPos.containing(standTargetPos.x, standTargetPos.y, standTargetPos.z);
            if (standHitDirection != null) {
                Vec3 normal = Vec3.atLowerCornerOf(standHitDirection.getNormal());
                Vec3 insideBlock = standTargetPos.subtract(normal.scale(0.5));
                checkPos = BlockPos.containing(insideBlock.x, insideBlock.y, insideBlock.z);
            }
            var blockState = this.self.level().getBlockState(checkPos);
            if (!blockState.isCollisionShapeFullBlock(this.self.level(), checkPos) || !blockState.canOcclude() || blockState.isAir()) {
                usertargeting();
            }
        }
        // ── 1a. PHASE 1 — travel to block face ───────────────────────────────
        if (isTravelling && standTravelTarget != null && stand != null) {

            Vec3   current = stand.position();
            Vec3   dir     = standTravelTarget.subtract(current);
            double dist    = dir.length();

            if (dist < 0.3) {
                stand.setPos(standTravelTarget.x, standTravelTarget.y, standTravelTarget.z);
                isTravelling  = false;
                isPreSinking  = true;
                preSinkTicks  = PRE_SINK_DURATION;
                preSinkStartYawDeg = currentStandYawDeg;
                if (standHitDirection != null) {
                    switch (standHitDirection) {
                        case UP   -> animateStand(PlanetWavesEntity.BURY_UPWARDS);
                        case DOWN -> animateStand(PlanetWavesEntity.BURY_DOWNWARDS);
                        default   -> animateStand(PlanetWavesEntity.BURY_HORIZONTAL);
                    }
                    applyBurialPitch(stand);
                }
                syncStandMode();
            } else {
                animateStand(PlanetWavesEntity.FLOATING);

                double horizLen = Math.sqrt(dir.x * dir.x + dir.z * dir.z);
                float travelYaw   = (float) (Mth.atan2(-dir.x, dir.z) * (180.0 / Math.PI));
                float travelPitch = (float) (-Mth.atan2(dir.y, horizLen) * (180.0 / Math.PI));

                stand.setStandRotationX(travelPitch * Mth.DEG_TO_RAD);
                setStandYaw(stand, travelYaw);

                double speed = Math.min(dist, 0.5);
                Vec3   step  = dir.normalize().scale(speed);
                stand.setPos(
                        current.x + step.x,
                        current.y + step.y,
                        current.z + step.z
                );
            }
        }

        // ── 1b. PHASE 2 — sink into block ────────────────────────────────────
        if (isSinking && sinkTarget != null && stand != null) {

            Vec3   current = stand.position();
            Vec3   dir     = sinkTarget.subtract(current);
            double dist    = dir.length();

            buryEffectTick++;
            if (buryEffectTick % 4 == 0) {
                playBuriedEffects();
            }
            if (buryEffectTick % 4 == 0) {
                playBuriedSound();
            }

            if (dist < 0.15) {
                stand.setPos(sinkTarget.x, sinkTarget.y, sinkTarget.z);
                isSinking = false;
                buryEffectTick = 0;
                applyBurialRotation(stand);
                syncStandMode();
            } else {
                double speed = Math.min(dist, 0.25);
                Vec3   step  = dir.normalize().scale(speed);
                stand.setPos(
                        current.x + step.x,
                        current.y + step.y,
                        current.z + step.z
                );
            }
        }
        // ── 1.5. PRE-SINK — wait for bury animation before moving ────────────
        if (isPreSinking && stand != null) {
            if (preSinkTicks > 0) {
                preSinkTicks--;
                float progress = 1.0f - (preSinkTicks / (float) PRE_SINK_DURATION);
                float delta = Mth.wrapDegrees(standTargetYawAligned - preSinkStartYawDeg);
                setStandYaw(stand, preSinkStartYawDeg + delta * progress);
            } else {
                setStandYaw(stand, standTargetYawAligned);
                isPreSinking = false;
                isSinking    = true;
                syncStandMode();
            }
        }
        // ── 2. BURIED — GRAB DETECTION ───────────────────────────────────────
        if (!isTravelling && !isSinking && targetingstand && stand != null) {

            if (restrainedEntity == null && grabCooldownTicks <= 0 && standTargetPos != null && standHitDirection != null) {

                AABB grabBox;
                double reach = 0.8;
                double width = 0.6;

                switch (standHitDirection) {
                    case UP -> grabBox = new AABB(
                            standTargetPos.x - width, standTargetPos.y, standTargetPos.z - width,
                            standTargetPos.x + width, standTargetPos.y + reach, standTargetPos.z + width
                    );
                    case DOWN -> grabBox = new AABB(
                            standTargetPos.x - width, standTargetPos.y - reach, standTargetPos.z - width,
                            standTargetPos.x + width, standTargetPos.y, standTargetPos.z + width
                    );
                    default -> {
                        Vec3 normal = Vec3.atLowerCornerOf(standHitDirection.getNormal());
                        grabBox = new AABB(
                                standTargetPos.x - width, standTargetPos.y - width, standTargetPos.z - width,
                                standTargetPos.x + width, standTargetPos.y + width, standTargetPos.z + width
                        ).expandTowards(normal.scale(reach));
                    }
                }

                List<LivingEntity> nearby = self.level().getEntitiesOfClass(
                        LivingEntity.class,
                        grabBox
                );

                for (LivingEntity entity : nearby) {
                    if (entity.is(this.self))         continue;
                    if (!entity.isAlive())             continue;
                    if (entity instanceof StandEntity) continue;

                    restrainedEntity = entity;
                    restrainHoldTicks = 60; // 3 seconds

                    if (restrainedEntity instanceof Mob mob) mob.setNoAi(true);
                    if (entity instanceof StandUser SU)      SU.roundabout$setRestrainedTicks(60); // 3 seconds

                    byte grabAnim;
                    byte animType;
                    switch (standHitDirection) {
                        case UP -> { grabAnim = PlanetWavesEntity.GRAB_UPWARDS; animType = 1; }
                        case DOWN -> { grabAnim = PlanetWavesEntity.GRAB_DOWNWARDS; animType = 2; }
                        default -> { grabAnim = PlanetWavesEntity.GRAB_HORIZONTAL; animType = 0; }
                    }
                    restrainAnimationType = animType;
                    animateStand(grabAnim);
                    syncStandMode();
                    break;
                }
            }
        }

        // ── 3. HOLD RESTRAINED ENTITY ────────────────────────────────────────
        if (restrainedEntity != null && stand != null) {

            if (restrainedEntity instanceof StandUser SU) {
                SU.roundabout$setRestrainedTicks(20);
            }

            boolean dead = !restrainedEntity.isAlive();
            boolean lost = dead || restrainedEntity.distanceTo(stand) > 4.0;

            if (restrainHoldTicks > 0 && !dead) {
                restrainHoldTicks--;
            }

            if (lost || restrainHoldTicks <= 0) {
                releaseRestrainedEntity(stand);
            } else {
                restrainedEntity.setDeltaMovement(Vec3.ZERO);
                restrainedEntity.hurtMarked = true;
                if (restrainedEntity instanceof Mob mob) {
                    mob.getNavigation().stop();
                    mob.setTarget(null);
                }
                double tpX = standTargetPos.x;
                double tpY = standTargetPos.y;
                double tpZ = standTargetPos.z;

                if (standHitDirection == net.minecraft.core.Direction.UP) {
                    tpY += 0.5;
                    if (standApproachDir != null) {
                        double offsetDist = 0.75; // 1.0 = mob más alejado del stand
                        tpX += standApproachDir.x * offsetDist;
                        tpZ += standApproachDir.z * offsetDist;
                    }
                } else if (standHitDirection == net.minecraft.core.Direction.DOWN) {
                    tpY -= 2.0;

                    if (standApproachDir != null) {
                        double offsetDist = 0.75;
                        tpX += standApproachDir.x * offsetDist;
                        tpZ += standApproachDir.z * offsetDist;
                    }
                }

                restrainedEntity.teleportTo(tpX, tpY, tpZ);
            }
        }

// ── 4. RELEASE if stand recalled ─────────────────────────────────────
        if (!targetingstand && restrainedEntity != null) {
            releaseRestrainedEntity(stand);
        }
    }

    /** Shared cleanup when the grab ends. */
    private void releaseRestrainedEntity(StandEntity stand) {
        if (restrainedEntity instanceof Mob mob) mob.setNoAi(false);
        restrainedEntity = null;
        grabCooldownTicks = GRAB_COOLDOWN;
        if (stand != null && targetingstand && standHitDirection != null) {
            byte burialAnim = switch (standHitDirection) {
                case UP -> PlanetWavesEntity.BURY_UPWARDS;
                case DOWN -> PlanetWavesEntity.BURY_DOWNWARDS;
                default -> PlanetWavesEntity.BURY_HORIZONTAL;
            };
            this.animateStand(burialAnim);
        } else if (stand != null) {
            this.animateStand(StandEntity.IDLE);
        }
        syncStandMode();
    }

    private void applyBurialRotation(StandEntity stand) {
        if (standHitDirection == null || stand == null) return;
        applyBurialPitch(stand);
        setStandYaw(stand, this.standTargetYawAligned);
    }
    private void applyBurialPitch(StandEntity stand) {
        if (standHitDirection == null || stand == null) return;
        float extraX;
        switch (standHitDirection) {
            case NORTH, SOUTH, EAST, WEST -> extraX = -90.0F * Mth.DEG_TO_RAD;
            case DOWN  -> extraX = (float) Math.PI;
            default    -> extraX = 0.0F; // UP
        }
        stand.setStandRotationX(extraX);
    }
    private void applyTravelRotation(StandEntity stand) {
        if (stand == null) return;
        stand.setStandRotationX(0.0F);
        setStandYaw(stand, this.standTargetYaw);
    }

    private boolean isBuried= false;
    private Vec3 buriedStandPos= null;
    private void usertargeting() {
        Level level = this.self.level();
        if (this.onCooldown(PowerIndex.SKILL_4)) return;
        targetingstand    = false;
        isTravelling      = false;
        isSinking      = false;
        standTravelTarget = null;
        standHitDirection = null;
        standApproachDir  = null;
        buryEffectTick = 0;

        StandEntity stand = this.getStandEntity(this.self);
        releaseRestrainedEntity(stand);

        if (stand instanceof FollowingStandEntity FSE) {
            FSE.setOffsetType(OffsetIndex.FOLLOW);
        }

        standTargetPos = null;
        syncStandMode();

        if (!level.isClientSide()) {
            this.setCooldown(PowerIndex.SKILL_4,
                    ClientNetworking.getAppropriateConfig()
                            .PlanetWavesSettings.usertargetingCooldown);
        }

        level.playSound(null, this.self.blockPosition(),
                ModSounds.PLANET_WAVES_TARGET_EVENT,
                SoundSource.PLAYERS, 1.0F, 1.0F);
    }
    private void meteortracking() {
        tracking = true;
        for (PWMeteorEntity meteor : this.self.level().getEntitiesOfClass(
                PWMeteorEntity.class,
                this.self.getBoundingBox().inflate(256))) {

            if (meteor.getStandUser() == this.self) {
                meteor.setTrackingUser(true);
            }
        }
        if (!isClient() && this.self instanceof ServerPlayer PE) {
                PE.displayClientMessage(Component.translatable("text.roundabout.planet_waves.meteor_tracking_message").withStyle(ChatFormatting.RED), true);
        }
        syncStandMode();
    }
    @Override
    public boolean canInterruptPower(DamageSource sauce, Entity interrupter) {
        if (isTravelling) {
            isTravelling = false;
            isSinking     = false;
            targetingstand = false;
            standTravelTarget = null;
            standTargetPos = null;
            buryEffectTick = 0;

            StandEntity stand = this.getStandEntity(this.self);
            if (stand instanceof FollowingStandEntity FSE) {
                FSE.setOffsetType(OffsetIndex.FOLLOW);
            }

            this.animateStand(StandEntity.IDLE);
            syncStandMode();
            return true;
        }
        return super.canInterruptPower(sauce,interrupter);
    }
    private void meteornottracking() {
        tracking = false;

        for (PWMeteorEntity meteor : this.self.level().getEntitiesOfClass(
                PWMeteorEntity.class,
                this.self.getBoundingBox().inflate(256))) {

            if (meteor.getStandUser() == this.self) {
                meteor.setTrackingUser(false);
            }
        }

        if (!isClient() && this.self instanceof ServerPlayer PE) {
            PE.displayClientMessage(
                    Component.translatable(
                                    "text.roundabout.planet_waves.meteor_tracking_message_off")
                            .withStyle(ChatFormatting.RED),
                    true
            );
        }

        syncStandMode();
    }
    private void meteorDisappearance() {
        if (this.onCooldown(PowerIndex.SKILL_2_SNEAK)) return;
        if (this.self.level().isClientSide()) return;

        Level level = this.self.level();

        boolean foundMeteor = false;

        List<PWBigMeteorEntity> meteors = level.getEntitiesOfClass(
                PWBigMeteorEntity.class,
                this.self.getBoundingBox().inflate(256)
        );

        for (PWBigMeteorEntity meteor : meteors) {

            if (meteor.getStandUser() == this.self) {

                meteor.forceDisintegrate();
                foundMeteor = true;

                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(
                            ParticleTypes.SMOKE,
                            meteor.getX(), meteor.getY(), meteor.getZ(),
                            10,
                            0.2, 0.2, 0.2,
                            0.01
                    );
                }
            }
        }
        if (foundMeteor) {
            level.playSound(
                    null,
                    this.self.blockPosition(),
                    ModSounds.PLANET_WAVES_DISINTEGRATION_EVENT,
                    SoundSource.PLAYERS,
                    1.5F,
                    1.0F
            );

            this.setCooldown(PowerIndex.SKILL_2_SNEAK, 60);
            if (this.getSelf() instanceof ServerPlayer sp) {
                S2CPacketUtil.sendCooldownSyncPacket(sp, PowerIndex.SKILL_2_SNEAK, 60);
            }
        }
    }
    @Override
    public void powerActivate(PowerContext context) {
        switch (context) {

            // Skill 1 → Meteor Shower
            case SKILL_1_NORMAL, SKILL_1_CROUCH, SKILL_1_GUARD, SKILL_1_CROUCH_GUARD -> {
                this.tryPowerPacket(PowerIndex.POWER_1);
            }

            // Skill 2 → Big Meteor
            case SKILL_2_NORMAL, SKILL_2_GUARD, SKILL_2_CROUCH_GUARD -> {
                this.tryPowerPacket(PowerIndex.POWER_2);
            }

            // Skill 3 → Dash
            case SKILL_3_NORMAL, SKILL_3_GUARD, SKILL_3_CROUCH, SKILL_3_CROUCH_GUARD -> {
                dash();
            }

            // Skill 4 → Stand Targeting or Stand Retrieving
            case SKILL_4_NORMAL, SKILL_4_GUARD, SKILL_4_CROUCH_GUARD -> {
                this.tryPowerPacket(PowerIndex.POWER_4);

            }
            case SKILL_4_CROUCH-> {
                this.tryPowerPacket(PowerIndex.POWER_4_SNEAK);

            }
            case SKILL_2_CROUCH-> {
                this.tryPowerPacket(PowerIndex.POWER_2_SNEAK);

            }
        }
    }
    @Override
    public void tickMobAI(LivingEntity attackTarget) {

        if (attackTarget != null
                && attackTarget.isAlive()
                && !this.isDazed(this.getSelf())) {

            double dist = attackTarget.distanceTo(this.getSelf());

            if (this.getActivePower() != PowerIndex.NONE || dist <= 12) {
                rotateMobHead(attackTarget);
            }

            if (!this.onCooldown(PowerIndex.SKILL_1)
                    && activePower == PowerIndex.NONE
                    && !this.self.isUnderWater()) {

                ((StandUser) this.getSelf())
                        .roundabout$tryPower(PowerIndex.POWER_1, true);
            }

            else if (dist > 8
                    && dist <= 30
                    && !this.onCooldown(PowerIndex.SKILL_2)
                    && activePower == PowerIndex.NONE
                    && !this.self.isUnderWater()) {

                double RNG = Math.random();

                if (RNG < 0.2) {
                    ((StandUser) this.getSelf())
                            .roundabout$tryPower(PowerIndex.POWER_2, true);
                }
            }

            if (dist <= 7
                    && (activePower == PowerIndex.NONE
                    || activePower == PowerIndex.ATTACK)) {

                Entity targetEntity = getTargetEntity(this.self, -1);

                if (targetEntity != null && targetEntity.is(attackTarget)) {

                    if (this.attackTimeDuring <= -1) {

                        double RNG = Math.random();

                        if (RNG < 0.25
                                && !this.onCooldown(PowerIndex.SKILL_2)
                                && activePower == PowerIndex.NONE) {

                            ((StandUser) this.getSelf())
                                    .roundabout$tryPower(PowerIndex.POWER_2, true);

                        } else {

                            ((StandUser) this.getSelf())
                                    .roundabout$tryPower(PowerIndex.ATTACK, true);
                        }
                    }
                }
            }

        }
    }
//summon minecraft:zombie ~ ~ ~ {roundabout.StandDisc:{id:"roundabout:max_planet_waves_disc",tag:{Memory:{Pose:0b,Skin:1b}},Count:1b}}
    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.PLANET_WAVES_SUMMON_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }
    @Override
    public boolean isServerControlledCooldown(byte num){
        if (num == PowerIndex.SKILL_1 || num == PowerIndex.SKILL_2 || num == PowerIndex.SKILL_4 || num == PowerIndex.SKILL_2_SNEAK || num == PowerIndex.SKILL_4_SNEAK){
            return true;
        }
        return super.isServerControlledCooldown(num);
    }
    @Override
    public boolean setPowerNone() {
        boolean result = super.setPowerNone();
        if (targetingstand) {
            StandEntity stand = this.getStandEntity(this.self);
            if (stand instanceof FollowingStandEntity FSE) {
                FSE.setOffsetType(OffsetIndex.LOOSE);
            }
        }
        return result;
    }
    @Override
    public void onStandSummon(boolean desummon) {
        super.onStandSummon(desummon);
        if (desummon) {
            if (!targetingstand) {
                StandEntity stand = this.getStandEntity(this.self);
                releaseRestrainedEntity(stand);
                isTravelling      = false;
                isSinking         = false;
                standTargetPos    = null;
                standTravelTarget = null;
                standHitDirection = null;
            }
        } else if (targetingstand) {
            StandEntity stand = this.getStandEntity(this.self);
            if (stand instanceof FollowingStandEntity FSE) {
                FSE.setOffsetType(OffsetIndex.LOOSE);
            }
            if (stand != null && sinkTarget != null) {
                stand.setPos(sinkTarget.x, sinkTarget.y, sinkTarget.z);
                applyBurialRotation(stand);

                if (standHitDirection != null) {
                    byte burialAnim = switch (standHitDirection) {
                        case UP   -> PlanetWavesEntity.BURY_UPWARDS;
                        case DOWN -> PlanetWavesEntity.BURY_DOWNWARDS;
                        default   -> PlanetWavesEntity.BURY_HORIZONTAL;
                    };
                    animateStand(burialAnim);
                }
                isTravelling = false;
                isSinking    = false;
            }
            syncStandMode();
        }
    }
    private Vec3 standApproachOrigin = null;
    @Override
    public void serverQueried() {
        if (self instanceof ServerPlayer pl) {
            S2CPacketUtil.sendGenericIntToClientPacket(
                    pl,
                    PacketDataIndex.S2C_INT_STAND_MODE,
                    targetingstand ? 1 : 0
            );
        }
    }
    private void syncStandMode() {
        if (self instanceof ServerPlayer pl) {
            int mode = 0;
            if (targetingstand) mode |= 1;
            if (tracking)       mode |= 2;
            if (isTravelling)   mode |= 4;
            if (isSinking)      mode |= 8;
            mode |= (restrainAnimationType & 0x3) << 4;   // bits 4-5
            int dirOrdinal = (standHitDirection != null) ? standHitDirection.ordinal() : 0;
            mode |= (dirOrdinal & 0x7) << 6;               // bits 6-8
            if (isPreSinking) mode |= 512;                 // bit 9

            float wrappedYaw = Mth.wrapDegrees(standTargetYawAligned);
            if (wrappedYaw < 0) wrappedYaw += 360f;
            int yawEncoded = Math.round(wrappedYaw * 100f) % 36000;
            mode |= (yawEncoded & 0xFFFF) << 10;

            S2CPacketUtil.sendGenericIntToClientPacket(
                    pl, PacketDataIndex.S2C_INT_STAND_MODE, mode);
        }
    }

    public void clientIntUpdated(int integer) {
        boolean wasTravel   = isTravelling;
        boolean prevPreSink = isPreSinking;

        targetingstand        = (integer & 1) != 0;
        tracking              = (integer & 2) != 0;
        isTravelling          = (integer & 4) != 0;
        isSinking             = (integer & 8) != 0;
        restrainAnimationType = (byte)((integer >> 4) & 0x3);
        int dirOrdinal        = (integer >> 6) & 0x7;
        standHitDirection     = net.minecraft.core.Direction.values()[dirOrdinal];
        isPreSinking          = (integer & 512) != 0;

        int yawEncoded = (integer >> 10) & 0xFFFF;
        this.syncedTargetYaw = yawEncoded / 100f;

        if (isTravelling) {
            StandEntity stand = this.getStandEntity(this.self);
            applyTravelRotation(stand);
        }

        if (wasTravel && !isTravelling) {
            StandEntity stand = this.getStandEntity(this.self);
            byte burialAnim = switch (standHitDirection) {
                case UP   -> PlanetWavesEntity.BURY_UPWARDS;
                case DOWN -> PlanetWavesEntity.BURY_DOWNWARDS;
                default   -> PlanetWavesEntity.BURY_HORIZONTAL;
            };
            animateStand(burialAnim);
            applyBurialPitch(stand);
        }

        if (!prevPreSink && isPreSinking) {
            clientPreSinkStartYaw = currentStandYawDeg;
            clientPreSinkTicks = PRE_SINK_DURATION;
        }

        if (targetingstand && restrainAnimationType != 0) {
            byte grabAnim = switch (restrainAnimationType) {
                case 1 -> PlanetWavesEntity.GRAB_UPWARDS;
                case 2 -> PlanetWavesEntity.GRAB_DOWNWARDS;
                default -> PlanetWavesEntity.GRAB_HORIZONTAL;
            };
            animateStand(grabAnim);
        }
    }

    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.meteor_shower",
                "instruction.roundabout.press_skill", StandIcons.PLANET_WAVES_METEOR_SHOWER, 1, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.big_meteor",
                "instruction.roundabout.press_skill", StandIcons.PLANET_WAVES_BIG_METEOR,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.forced_dissintegration",
                "instruction.roundabout.press_skill_crouch", StandIcons.PLANET_WAVES_FORCED_DISINTEGRATION,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 3, "ability.roundabout.stand_targeting",
                "instruction.roundabout.press_skill", StandIcons.PLANET_WAVES_STAND_TARGETING, 4, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 118, 3, "ability.roundabout.stand_retrieving",
                "instruction.roundabout.press_skill", StandIcons.PLANET_WAVES_STAND_RETRIEVING, 4, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 80, 4, "ability.roundabout.meteor_tracking",
                "instruction.roundabout.press_skill_crouch", StandIcons.PLANET_WAVES_METEOR_TRACKING, 4, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 99, 0, "ability.roundabout.desintegration",
                "instruction.roundabout.passive", StandIcons.PLANET_WAVES_DESINTEGRATION, 4, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 118, 0, "ability.roundabout.grab",
                "instruction.roundabout.passive", StandIcons.PLANET_WAVES_GRAB, 4, level, bypass));

        return $$1;


    }
    public byte getFireColor(){
        byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();

        return switch (skn) {
            /*case PlanetWavesEntity.BLUE_SKIN, PlanetWavesEntity.BLUE_ACE_SKIN, PlanetWavesEntity.BLUE_ABLAZE, PlanetWavesEntity.SKELETAL -> StandFireType.BLUE.id;
            case PlanetWavesEntity.PURPLE_SKIN, PlanetWavesEntity.PURPLE_ABLAZE -> StandFireType.PURPLE.id;
            case PlanetWavesEntity.GREEN_SKIN, PlanetWavesEntity.GREEN_ABLAZE -> StandFireType.GREEN.id;
            case PlanetWavesEntity.DREAD_SKIN, PlanetWavesEntity.DREAD_ABLAZE, PlanetWavesEntity.DREAD_BEAST_SKIN -> StandFireType.DREAD.id;
            case PlanetWavesEntity.JOJONIUM, PlanetWavesEntity.JOJONIUM_ABLAZE -> StandFireType.CREAM.id;*/
            case PlanetWavesEntity.PURPLE_SKIN -> StandFireType.PURPLE.id;
            case PlanetWavesEntity.BLUE_SKIN -> StandFireType.BLUE.id;
            case PlanetWavesEntity.MANGA_SKIN -> StandFireType.CREAM.id;
            default -> StandFireType.ORANGE.id;
        };
    }
    public void createStandFire(BlockPos pos){
        if (pos != null && tryPlaceBlock(pos)){
            createStandFire(pos);
        }
    }

    public float getFireballDamage(Entity entity){
        return 4F;
    }

    public float getBigMeteorDamage(Entity entity){
        return 8F;
    }

    public SimpleParticleType getFlameParticle(){
        byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();

        return switch (skn) {
            case PlanetWavesEntity.OCEAN_WAVES,PlanetWavesEntity.SYMPHONY_WAVES -> ParticleTypes.SPLASH;
            case PlanetWavesEntity.GREEN_SKIN-> ModParticles.GREEN_FLAME;
            case PlanetWavesEntity.PURPLE_SKIN -> ModParticles.PURPLE_FLAME;
            case PlanetWavesEntity.BLUE_SKIN -> ModParticles.BLUE_FLAME;
            case PlanetWavesEntity.MANGA_SKIN -> ModParticles.CREAM_FLAME;
            default -> ModParticles.ORANGE_FLAME;
        };
    }
    }


