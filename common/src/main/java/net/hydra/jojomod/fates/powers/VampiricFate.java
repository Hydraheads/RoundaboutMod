package net.hydra.jojomod.fates.powers;

import net.hydra.jojomod.access.AccessFateFoodData;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.ILevelAccess;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.VampireData;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersWalkingHeart;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class VampiricFate extends FatePowers {
    public VampiricFate(LivingEntity self) {
        super(self);
    }
    public VampiricFate() {
        super();
    }
    public static final byte BLOOD_SUCK = 27;
    public static final byte BLOOD_SPEED = 28;
    public static final byte BLOOD_REGEN = 29;
    public static final byte WALL_WALK = 30;
    public static final byte SUPER_HEARING = 31;

    public Direction wallWalkDirection = Direction.DOWN;

    public Direction getWallWalkDirection(){
        return wallWalkDirection;
    }
    public void setWallWalkDirection(Direction dir){
        wallWalkDirection = dir;
    }

    public float walkDistLast = 0;
    public void wallLatch(){
        this.setCooldown(PowerIndex.FATE_3, 10);
                //if (!isOnWrongAxis())
                //toggleSpikes(true);
                Direction gd = RotationUtil.getRealFacingDirection2(this.self);
                setWallWalkDirection(gd);
                ((IGravityEntity) this.self).roundabout$setGravityDirection(gd);
                justFlippedTicks = 7;
        jumpedOffWall = true;
        if (this.self.level().isClientSide()) {
            C2SPacketUtil.intToServerPacket(
                    PacketDataIndex.INT_GRAVITY_FLIP_3,MainUtil.getIntFromDirection(gd)
            );
        }
    }

    public void setSuperHearingClient(){
        if (isHearing()){
            stopHearingClient();
            return;
        }
        tryPower(SUPER_HEARING, true);
        tryPowerPacket(SUPER_HEARING);
    }
    public void setSuperHearing(){
        setAttackTimeDuring(0);
        setActivePower(SUPER_HEARING);
    }


    public void blockBreakParticles(Block block, Vec3 pos){
        if (!this.self.level().isClientSide()) {
            ((ServerLevel) this.self.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK,
                            block.defaultBlockState()),
                    pos.x, pos.y, pos.z,
                    100, 0.2, 0.2, 0.2, 0.5);
        }
    }

    public boolean jumpedOffWall = false;

public int speedActivated = 0;
    public boolean isFast(){
        return speedActivated > 0;
    }
    public int getSpeedActivated(){
        return speedActivated;
    }
    public void setSpeedActivated(int sped){
        speedActivated = sped;
    }

    @Override
    public void tickPower() {
        tickBloodSuck();
        tickSpeed();
        tickBloodRegen();
        super.tickPower();


        if (this.self.level().isClientSide()) {

            if (isPacketPlayer()) {
                if (!isPlantedInWall() && self.onGround()) {
                    jumpedOffWall = false;
                }

                Vec3 newVec = new Vec3(0, -0.2, 0);
                Vec3 newVec2 = new Vec3(0, -1.0, 0);
                Vec3 newVec4 = new Vec3(0, -0.5, 0);
                Vec3 newVec5 = new Vec3(0, -1.1, 0);

                newVec = RotationUtil.vecPlayerToWorld(newVec, ((IGravityEntity) self).roundabout$getGravityDirection());
                BlockPos pos = BlockPos.containing(self.getPosition(1).add(newVec));
                newVec2 = RotationUtil.vecPlayerToWorld(newVec2, ((IGravityEntity) self).roundabout$getGravityDirection());
                BlockPos pos2 = BlockPos.containing(self.getPosition(1).add(newVec2));
                newVec4 = RotationUtil.vecPlayerToWorld(newVec4, ((IGravityEntity) self).roundabout$getGravityDirection());
                BlockPos pos4 = BlockPos.containing(self.getPosition(1).add(newVec4));
                newVec5 = RotationUtil.vecPlayerToWorld(newVec5, ((IGravityEntity) self).roundabout$getGravityDirection());
                BlockPos pos5 = BlockPos.containing(self.getPosition(1).add(newVec5));

                BlockState state1 = self.level().getBlockState(pos);
                BlockState state4 = self.level().getBlockState(pos4);
                boolean isOnValidBlock = MainUtil.isBlockWalkableSimplified(state1)
                        && MainUtil.isBlockWalkableSimplified(state4);

                if (isVisionOn()) {
                    if (dimTickEye > 0) {
                        dimTickEye--;
                    }
                } else {
                    if (dimTickEye < 10) {
                        dimTickEye++;
                    }
                }

                if (!isHearing()) {
                    if (dimTickHearing > 0) {
                        dimTickHearing--;
                    }
                } else {
                    if (dimTickHearing < 10) {
                        dimTickHearing++;
                    }
                }

                if (isPlantedInWall() && !getStandUserSelf().rdbt$getJumping()) {
                    if (!self.onGround()) {
                        if (this.self.getDeltaMovement().y < 0) {
                            this.self.setDeltaMovement(this.self.getDeltaMovement().add(0, -0.14, 0));
                        }
                    }
                }

                if (self.isSwimming()) {
                    setWallWalkDirection(getIntendedDirection());
                    ((IGravityEntity) this.self).roundabout$setGravityDirection(getIntendedDirection());
                    C2SPacketUtil.intToServerPacket(
                            PacketDataIndex.INT_GRAVITY_FLIP_4, MainUtil.getIntFromDirection(getIntendedDirection())
                    );
                }

                if (isPlantedInWall()) {
                    if (justFlippedTicks > 0) {
                        justFlippedTicks--;
                    } else {

                        if (self.onGround()) {
                            mercyTicks = 5;
                        } else {
                            if (
                                    (
                                            MainUtil.isBlockWalkable(self.level().getBlockState(pos))
                                                    || MainUtil.isBlockWalkable(self.level().getBlockState(pos2))
                                                    || MainUtil.isBlockWalkable(self.level().getBlockState(pos4))
                                                    || MainUtil.isBlockWalkable(self.level().getBlockState(pos5))
                                    )) {
                                mercyTicks--;
                            } else {
                                mercyTicks = 0;
                            }
                        }
                        if (self.isSleeping() || ((!self.onGround()) && mercyTicks <= 0) || self.getRootVehicle() != this.self) {
                            wallWalkDirection = getIntendedDirection();
                            ((IGravityEntity) this.self).roundabout$setGravityDirection(wallWalkDirection);
                            setWallWalkDirection(wallWalkDirection);
                            C2SPacketUtil.intToServerPacket(
                                    PacketDataIndex.INT_GRAVITY_FLIP_4, MainUtil.getIntFromDirection(wallWalkDirection)
                            );
                        }
                    }

                } else {
                    setWallWalkDirection(getIntendedDirection());
                }
            }
        } else {

            if (hasStandActive(self) && getActivePower() == SUPER_HEARING){
                xTryPower(PowerIndex.NONE,true);
            }

            Vec3 newVec = new Vec3(0,-0.2,0);
            Vec3 newVec4 = new Vec3(0,-0.5,0);

            newVec = RotationUtil.vecPlayerToWorld(newVec,((IGravityEntity)self).roundabout$getGravityDirection());
            BlockPos pos = BlockPos.containing(self.getPosition(1).add(newVec));
            newVec4 = RotationUtil.vecPlayerToWorld(newVec4,((IGravityEntity)self).roundabout$getGravityDirection());
            BlockPos pos4 = BlockPos.containing(self.getPosition(1).add(newVec4));
            BlockState state1 = self.level().getBlockState(pos);
            BlockState state4 = self.level().getBlockState(pos4);
            boolean isOnValidBlock =  MainUtil.isBlockWalkableSimplified(state1)
                    && MainUtil.isBlockWalkableSimplified(state4);
            if (!isOnValidBlock){
                setWallWalkDirection(getIntendedDirection());
            }
        }
    }




    public float getStepHeightAddon(){
        if (isFast()) {
            return 0.4F;
        }
        return 0;
    }

    public Direction getIntendedDirection(){
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

    public int justFlippedTicks = 0;
    public boolean shouldReset(byte activeP){
        if (activeP == BLOOD_REGEN)
            return false;
        return super.shouldReset(activeP);
    }

    public final float bloodSpread = 3;
    public final int duration = 100;
    public void tickBloodRegen(){
        if (!this.self.level().isClientSide()) {
            if (getActivePower() == BLOOD_REGEN && self.isAlive() && !self.isRemoved()){
                if (self instanceof Player PE && !PE.isCreative()){
                    PE.getFoodData().setFoodLevel(0);
                }
                //Particle
                float spreadX = (float) (Math.random()*bloodSpread - (bloodSpread/2));
                float spreadY = (float) (Math.random()*bloodSpread - (bloodSpread/2));
                float spreadZ = (float) (Math.random()*bloodSpread - (bloodSpread/2));

                Vec3 shotPos = new Vec3(spreadX,spreadY,spreadZ);
                Vec3 spawnPos = shotPos.add(self.getEyePosition(1f));
                shotPos = shotPos.multiply(new Vec3(-1,-1,-1));

                ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.BLOOD_MIST,
                        spawnPos.x, spawnPos.y, spawnPos.z,
                        0, shotPos.x, shotPos.y,shotPos.z, 0.03);


                //heal
                float healthBack = sunkRegen/duration * 0.9F;
                float health = self.getHealth();
                float maxHealth = self.getMaxHealth();

                if (health < maxHealth){
                    health+=healthBack;
                    if (health < maxHealth){
                        self.setHealth(health);
                    } else {
                        self.setHealth(maxHealth);
                    }
                }
                if (attackTimeDuring > duration || self.getHealth() >= maxHealth){
                    xTryPower(PowerIndex.NONE, true);
                    this.stopSoundsIfNearby(SoundIndex.BLOOD_REGEN, 100,false);
                    self.level().playSound(null, self.getX(), self.getY(), self.getZ(), ModSounds.BLOOD_REGEN_FINISH_EVENT, SoundSource.PLAYERS, 1F, 1F);
                }
            }
        }
    }
    public int mercyTicks = 0;
    public void tickSpeed(){
        if (isFast()){
            setSpeedActivated(getSpeedActivated()-1);
            if (!isFast()){
                if (self instanceof Player pl) {
                    if (self.level().isClientSide()) {
                        C2SPacketUtil.trySingleBytePacket(PacketDataIndex.END_BLOOD_SPEED);
                    } else {
                        S2CPacketUtil.sendGenericIntToClientPacket(pl, PacketDataIndex.S2C_INT_VAMPIRE_SPEED,
                                0);
                    }
                }
            }
        }
    }



    public boolean isOnWrongAxis(){
        if (self.level().isClientSide()) {
            return ClientUtil.getDirectionRight(self);
        } else {

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
            return ((IGravityEntity)self).roundabout$getGravityDirection() != rightAxis;
        }
    }

    public Entity bloodSuckingTarget = null;

    public void tickBloodSuck(){
        if (!this.self.level().isClientSide()) {


            if (self.isUsingItem()) {
                if (bloodSuckingTarget != null || this.getActivePower() == BLOOD_SUCK) {
                    bloodSuckingTarget = null;
                    xTryPower(PowerIndex.NONE, true);
                }
            }


            if (bloodSuckingTarget != null) {

                if (bloodSuckingTarget instanceof LivingEntity LE && FateTypes.isVampire(LE)){
                    endSuckingVamp();
                } else {
                    Entity TE = getTargetEntity(self, 3, 15);
                    if (TE != null && MainUtil.canDrinkBloodFair(TE, self)
                            && self.hurtTime <= 0 && bloodSuckingTarget.is(TE)) {
                        if (TE instanceof LivingEntity LE) {
                            ((StandUser) LE).roundabout$setDazed((byte) 3);
                            LE.setDeltaMovement(0, -0.1F, 0);
                        }

                        if (self.tickCount % 2 == 0) {
                            double random = (Math.random() * 0.8) - 0.4;
                            double random2 = (Math.random() * 0.8) - 0.4;
                            double random3 = (Math.random() * 0.8) - 0.4;
                            SimpleParticleType particle = ModParticles.BLOOD;
                            if (MainUtil.hasBlueBlood(TE)) {
                                particle = ModParticles.BLUE_BLOOD;
                            }
                            ((ServerLevel) this.self.level()).sendParticles(particle, TE.getX() + random,
                                    TE.getY() + TE.getEyeHeight() + random2, TE.getZ() + random3,
                                    0,
                                    (this.self.getX() - TE.getX()), (this.self.getY() - TE.getY() + TE.getEyeHeight()), (this.self.getZ() - TE.getZ()),
                                    0.08);
                        }
                    }
                }
            }


            if (this.getActivePower() == BLOOD_SUCK){
                if (attackTimeDuring == 0 || attackTimeDuring == 5){
                    self.level().playSound(null, self.getX(), self.getY(), self.getZ(), ModSounds.BLOOD_SUCK_EVENT, SoundSource.PLAYERS, 1F, 0.95F+(float)(Math.random()*0.1));

                }
                if (attackTimeDuring >= 20){
                     finishSucking();
                    bloodSuckingTarget = null;
                }
            }
        } else {
            if (bloodSuckingTarget != null) {
                Entity TE = getTargetEntity(self, 3, 15);
                if (TE != null && MainUtil.canDrinkBloodFair(TE, self)
                        && self.hurtTime <= 0 && bloodSuckingTarget.is(TE)) {
                    //safe
                } else {
                    xTryPower(PowerIndex.NONE,true);
                    C2SPacketUtil.cancelSuckingPacket();
                    bloodSuckingTarget = null;
                }
            }
            if (this.getActivePower() == BLOOD_SUCK) {
                if (attackTimeDuring >= 20) {
                    if (this.isPacketPlayer() && attackTimeDuring == 20) {
                        C2SPacketUtil.finishSuckingPacket();
                    }
                }
            }
        }
    }

    public void endSuckingVamp(){
        bloodSuckingTarget = null;
        xTryPower(PowerIndex.NONE, true);
    }

    public boolean canUseBloodSpeedUnlock(){
        return true;
    }

    public void packetFinish(){
        if (this.getActivePower() == BLOOD_SUCK){
            finishSucking();
        }
    }
    public void packetCancel(){
        if (this.getActivePower() == BLOOD_SUCK){
            xTryPower(PowerIndex.NONE,true);
        }
        bloodSuckingTarget = null;
    }

    public boolean canUseBloodSpeed(){
        return self instanceof Player PE && PE.getFoodData().getFoodLevel() >= 10 && !isFast()
                && getActivePower() != BLOOD_REGEN;
    }
    public boolean canUseRegen(){
        return self instanceof Player PE && PE.getFoodData().getFoodLevel() >= 1 && !isFast()
                && getActivePower() != BLOOD_REGEN && self.getHealth() < self.getMaxHealth();
    }
    public void regenClient(){
        if (canUseRegen() && !onCooldown(PowerIndex.FATE_2_SNEAK)){
            if (isHearing()){
                stopHearingClient();
            }
            tryPowerPacket(BLOOD_REGEN);
        }
    }
    public void bloodSpeedClient() {

        if (canLatchOntoWall() && canWallWalkConfig()) {
            if (isHearing()){
                stopHearingClient();
            }
            doWallLatchClient();
        } else if (canUseBloodSpeed() && !onCooldown(PowerIndex.FATE_3_SNEAK)) {
            if (self.onGround()) {
                if (isHearing()){
                    stopHearingClient();
                }
                if (canUseBloodSpeedUnlock()) {
                    tryPowerPacket(BLOOD_SPEED);
                }
            }
        }
    }
    public void dashOrWallWalk(){
        if (canLatchOntoWall() && canWallWalkConfig())
            doWallLatchClient();
        else if (!isPlantedInWall())
            dash();
    }
    public void doWallLatchClient(){
        if (!this.onCooldown(PowerIndex.FATE_3)) {
            if (canLatchOntoWall() && canWallWalkConfig()){
            //test
                if (isHearing()){
                    stopHearingClient();
                }
                tryPower(WALL_WALK, true);
            }
        }
    }

    public float sunkRegen = 0;
    public void bloodRegen(){
        if (canUseRegen()) {
            if (self instanceof Player PE && !PE.isCreative()){
                int foodLevel = PE.getFoodData().getFoodLevel();
                sunkRegen = foodLevel;
                PE.getFoodData().setFoodLevel(0);
            }
            setAttackTimeDuring(0);
            setActivePower(BLOOD_REGEN);
            playSoundsIfNearby(SoundIndex.BLOOD_REGEN, 100, true);
            this.setCooldown(PowerIndex.FATE_2_SNEAK, 1200);
            S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.FATE_2_SNEAK,
                    1200
            );

        }
    }
    public void bloodSpeed(){
        if (canUseBloodSpeed()) {
            if (self instanceof Player PE && !PE.isCreative()){
                int foodLevel = PE.getFoodData().getFoodLevel();
                PE.getFoodData().setFoodLevel(foodLevel-10);
            }

            this.setCooldown(PowerIndex.GLOBAL_DASH, 600);
            S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.GLOBAL_DASH,
                    600
            );
            this.setCooldown(PowerIndex.FATE_3_SNEAK, 600);
            S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.FATE_3_SNEAK,
                    600
            );
            setFast();
            self.level().playSound(null, self.getX(), self.getY(), self.getZ(), ModSounds.BLOOD_SPEED_EVENT, SoundSource.PLAYERS, 1F, 0.95F+(float)(Math.random()*0.1));

        }
    }

    public boolean isVisionOn(){
        ClientConfig clientConfig = ConfigManager.getClientConfig();
        if (clientConfig != null && clientConfig.dynamicSettings != null) {
            return clientConfig.dynamicSettings.vampireVisionMode;
        }
        return true;
    }

    @Override
    public boolean interceptAttack(){
        return this.getActivePower() == BLOOD_SUCK;
    }

    public void clientChangeVision(){
        if (isHearing()){
            stopHearingClient();
            return;
        }
        ClientConfig clientConfig = ConfigManager.getClientConfig();
        if (clientConfig != null && clientConfig.dynamicSettings != null) {
            clientConfig.dynamicSettings.vampireVisionMode = !clientConfig.dynamicSettings.vampireVisionMode;
            ConfigManager.saveClientConfig();
        }
    }

    public boolean isPlantedInWall(){
        return isOnWrongAxis() && !(((StandUser)self).roundabout$getStandPowers() instanceof PowersWalkingHeart PW && PW.hasExtendedHeelsForWalking());
    }


    public boolean forceBlock(){
        if (!MainUtil.isBlockWalkableSimplified(self.level().getBlockState(self.getOnPos())))
            return true;
        return false;
    }


    public boolean canLatchOntoWall(){
        if (onCooldown(PowerIndex.SKILL_2) || self.isSwimming())
            return false;

        if (MainUtil.isStandingInBlock(self))
            return false;

        if (forceBlock())
            return false;

        if ((this.self.onGround() && !isPlantedInWall()) || (!this.self.onGround() && isPlantedInWall()))
            return false;

        Vec3 mpos = this.self.getPosition(1F);
        Direction gravdir = ((IGravityEntity)this.self).roundabout$getGravityDirection();
        switch (gravdir) {
            case DOWN -> {
                mpos = mpos.add(0,0.1F,0);
            }
            case UP -> {
                mpos = mpos.add(0,-0.1F,0);
            }
            case NORTH -> {
                mpos = mpos.add(0,0,0.1F);
            }
            case SOUTH -> {
                mpos = mpos.add(0,0,-0.1F);
            }
            case WEST -> {
                mpos = mpos.add(0.1F,0,0);
            }
            case EAST -> {
                mpos = mpos.add(-0.1F,0,0);
            }
        }
        BlockPos pos1 = BlockPos.containing(mpos);

        Direction rd = RotationUtil.getRealFacingDirection2(this.self);
        if (rd == gravdir)
            return false;
        pos1 = pos1.relative(RotationUtil.getRealFacingDirection2(this.self));
        BlockState bs = this.self.level().getBlockState(pos1);
        saveState = bs;
        return MainUtil.isBlockWalkable(bs);
    }
    public BlockState saveState = null;


    public void finishSucking(){
        if (bloodSuckingTarget instanceof LivingEntity LE && FateTypes.isVampire(LE)){
            endSuckingVamp();
            return;
        }

        if (bloodSuckingTarget != null && self instanceof Player pl) {

            boolean canDrainGood = MainUtil.canDrinkBloodCrit(bloodSuckingTarget,self);
            DamageSource sauce = ModDamageTypes.of(self.level(),
                    ModDamageTypes.BLOOD_DRAIN,pl);
            if (bloodSuckingTarget.hurt(sauce, getSuckDamage()) && bloodSuckingTarget instanceof LivingEntity LE) {//this.setCooldown(PowerIndex.FATE_2, 30);
                //if (!self.level().isClientSide()){
                //    S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()),
                //            PowerIndex.FATE_2, 30);
                //}
                if (canDrainGood) {
                    if (pl.canEat(false)) {
                        pl.getFoodData().eat(6, 1.0F);
                    } else {
                        if (((AccessFateFoodData)pl.getFoodData()).rdbt$getRealSaturation() < 7){
                            pl.getFoodData().eat(6, 0.5F);
                        } else {
                            pl.getFoodData().eat(6, 0);
                        }
                    }
                    self.level().playSound(null, self.getX(), self.getY(), self.getZ(), ModSounds.BLOOD_SUCK_DRAIN_EVENT, SoundSource.PLAYERS, 1F, 1.4F+(float)(Math.random()*0.1));
                    self.level().playSound(null, self.getX(), self.getY(), self.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1F, 1F+(float)(Math.random()*0.1));
                    int $$23 = (int)((double)2 * 0.5);
                    ((ServerLevel)this.self.level()).sendParticles(ParticleTypes.CRIT,
                            bloodSuckingTarget.getEyePosition().x,
                            bloodSuckingTarget.getEyePosition().y,
                            bloodSuckingTarget.getEyePosition().z,
                             15, 0.2, 0.2, 0.2, 0.0);

                } else {
                    self.level().playSound(null, self.getX(), self.getY(), self.getZ(), ModSounds.BLOOD_SUCK_DRAIN_EVENT, SoundSource.PLAYERS, 1F, 1.4F+(float)(Math.random()*0.1));
                    pl.getFoodData().eat(2, 0.0F);
                }
                MainUtil.makeBleed(bloodSuckingTarget, 0, 200, null);
                MainUtil.takeNoKnockback(bloodSuckingTarget);
            }
            bloodSuckingTarget = null;
            xTryPower(PowerIndex.NONE, true);
        }
    }

    public void stopHearingClient(){
        tryPower(PowerIndex.NONE,true);
        tryPowerPacket(PowerIndex.NONE);
    }
    public void suckBlood(){
        if (!onCooldown(PowerIndex.FATE_2)) {
            Entity TE = getTargetEntity(self, 3, 15);
            if (TE != null && MainUtil.canDrinkBloodFair(TE, self) && getActivePower() != BLOOD_REGEN) {
                if (isHearing()){
                    stopHearingClient();
                }
                setActivePower(BLOOD_SUCK);
                self.setSprinting(false);
                tryIntPowerPacket(BLOOD_SUCK, TE.getId());
                bloodSuckingTarget = TE;
                this.attackTimeDuring = 0;
                this.setCooldown(PowerIndex.FATE_2, 44);
            }
        }
    }
    @Override
    public boolean tryPower(int move, boolean forced){
        if (move != BLOOD_SUCK && !self.level().isClientSide()
        &&  getPlayerPos2() == PlayerPosIndex.BLOOD_SUCK) {
            super.setPlayerPos2(PlayerPosIndex.NONE_2);
        }
        return super.tryPower(move, forced);
    }

    @Override
    public float getJumpDamageMult(){
        return 0.5F;
    }

    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime){
        if (move == BLOOD_SUCK) {
            bloodSuckingTarget = this.self.level().getEntity(chargeTime);
            setActivePower(BLOOD_SUCK);
            self.setSprinting(false);
            if (!self.level().isClientSide()) {
                setPlayerPos2(PlayerPosIndex.BLOOD_SUCK);
            }
            this.attackTimeDuring = 0;
            if (bloodSuckingTarget != null) {
                bloodSuckingTarget.setDeltaMovement(Vec3.ZERO);
            }
        }
        return super.tryIntPower(move, forced, chargeTime);
    }

    @Override
    /**Stand related things that slow you down or speed you up, override and call super to make
     * any stand ability slow you down*/
    public float inputSpeedModifiers(float basis){

        if (isPlantedInWall()){
            basis *= 0.5F;
        }

        if (getActivePower() == BLOOD_SUCK){
            basis*=0.2F;
        } else if (getActivePower() == BLOOD_REGEN){
            basis*=0.1F;
        } else if (isFast()){
            basis*=getSpeedMod();
        }

        return basis;
    }

    public void setFast(){
        speedActivated = 120;
        if (!self.level().isClientSide()){
            if (self instanceof Player pl) {
                S2CPacketUtil.sendGenericIntToClientPacket(pl,PacketDataIndex.S2C_INT_VAMPIRE_SPEED,
                        speedActivated);
            }
            speedActivated+=60;
        }
    }
    @Override
    public float zoomMod(){
        if (getActivePower() == BLOOD_SUCK) {
            return 0.6f;
        } else if (isFast()){
            return 1.1F;
        }
        return 1;
    }
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == BLOOD_SPEED) {
            bloodSpeed();
        } else if (move == BLOOD_REGEN) {
            bloodRegen();
        } else if (move == SUPER_HEARING) {
            setSuperHearing();
        }
        return super.setPowerOther(move,lastMove);
    }

    public int dimTickEye = 0;
    public int dimTickHearing = 0;


    public SoundEvent randomHeart(){
        double rand = Math.random();
        if (rand < 0.33){
            return ModSounds.HEARTBEAT_EVENT;
        } else if (rand < 0.66){
            return ModSounds.HEARTBEAT2_EVENT;
        } else {
            return ModSounds.HEARTBEAT3_EVENT;
        }
    }

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        StandUser standUser = ((StandUser) playerEntity);
        boolean standOn = standUser.roundabout$getActive();
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8 - 1;
        if (!standOn){
            Entity TE = getTargetEntity(playerEntity, 3, 15);
            if (TE != null && MainUtil.canDrinkBloodFair(TE, self)){

                if (getActivePower() == BLOOD_SUCK){
                    int test = (int) ((17F/20F) * Mth.clamp(this.attackTimeDuring,0,20));
                    context.blit(StandIcons.JOJO_ICONS, k, j, 192, 36, 17, 8);
                    context.blit(StandIcons.JOJO_ICONS, k, j, 192, 44, 17-test, 8);
                } else {
                    if (TE instanceof LivingEntity LE && LE.getHealth()-getSuckDamage() <= 0){
                        context.blit(StandIcons.JOJO_ICONS, k, j, 192, 52, 17, 8);
                    } else {
                        context.blit(StandIcons.JOJO_ICONS, k, j, 192, 44, 17, 8);
                    }
                }

            }
        }
    }

    public float getSuckDamage(){
        return 4;
    }
    @Override
    public boolean cancelSprintJump(){
        return getActivePower() == BLOOD_SUCK || getActivePower() == BLOOD_REGEN;
    }
    @Override
    /**Cancel all sprinting*/
    public boolean cancelSprint(){
        return getActivePower() == BLOOD_SUCK || getActivePower() == BLOOD_REGEN;
    }
    @Override
    public boolean cancelSprintParticles(){
        return getActivePower() == BLOOD_SUCK || getActivePower() == BLOOD_REGEN
                || isPlantedInWall();
    }
    @Override
    public boolean cancelJump(){
        return getActivePower() == BLOOD_REGEN;
    }

    /**This function grays out icons for moves you can't currently use. Slot is the icon slot from 1-4,
     * activeP is your currently active power*/
    public boolean isAttackIneptVisually(byte activeP, int slot){
        Entity TE = getUserData(self).roundabout$getStandPowers().getTargetEntity(this.self, 3, 15);
        if (slot == 2 && !MainUtil.canDrinkBloodFair(TE, self) && !isHoldingSneak())
            return true;
        return super.isAttackIneptVisually(activeP,slot);
    }

    public boolean isHearing(){
        return getActivePower() == SUPER_HEARING;
    }


    public boolean canWallWalkConfig(){
        return ClientNetworking.getAppropriateConfig().walkingHeartSettings.enableWallWalking;
    }

    @Override
    public ResourceLocation getIconYes(int slot){
        if (slot == 2 && isHoldingSneak()){
            return StandIcons.SQUARE_ICON_BLOOD;
        } else if (slot == 3 && isHoldingSneak() && !canLatchOntoWall()){
            return StandIcons.SQUARE_ICON_BLOOD;
        }
        return StandIcons.SQUARE_ICON;
    }

    public float hearingDistance(){
        return 10;
    }
    public float getSpeedMod(){
        return 1.5F;
    }
    /**every entity the client renders is checked against this, overrride and use it to see if they can be highlighted
     * for detection or attack highlighting related skills*/
    @Override
    public boolean highlightsEntity(Entity ent,Player player){
        return isHearing() && MainUtil.getMobBleed(ent) && (
       !isSoundStolen(ent)
                ) &&ent.distanceTo(player) <= hearingDistance();
    }

    public boolean isSoundStolen(Entity ent){
        if ((((ILevelAccess)ent.level()).roundabout$isSoundPlunderedEntity(ent)) ||
        (((ILevelAccess)ent.level()).roundabout$isSoundPlundered(ent.blockPosition()))){
            return true;
        }
        if (ent instanceof LivingEntity LE && ((StandUser)LE).roundabout$getLocacacaCurse() == LocacacaCurseIndex.HEART)
            return true;
        return false;
    }
    /**The color id for this entity to be displayed as if the above returns true, it is in decimal rather than
     * hexadecimal*/
    @Override
    public int highlightsEntityColor(Entity ent, Player player){
        if (MainUtil.hasEnderBlood(ent))
            return 15139071;
        if (MainUtil.hasBlueBlood(ent))
            return 52991;
        return 16711680;
    }

    public void tickHeartbeat(Entity entity){
        if (entity != null && !self.is(entity)){
        if (MainUtil.getMobBleed(entity) && entity.distanceTo(self) <= hearingDistance()) {

            ILevelAccess access = ((ILevelAccess) entity.level());
            if (!isSoundStolen(entity)) {
                Vec3 center = MainUtil.getMobCenter(entity, 0.58f);
                int heartRate = 20;
                float heartpitch = 1;
                if (entity instanceof LivingEntity lv) {
                    float percent = lv.getHealth() / lv.getMaxHealth();
                    if (percent <= 0.2) {
                        heartRate = 8;
                        heartpitch = 1.6F;
                    } else if (percent < 0.4) {
                        heartRate = 11;
                        heartpitch = 1.45F;
                    } else if (percent < 0.6) {
                        heartRate = 14;
                        heartpitch = 1.3F;
                    } else if (percent < 0.8) {
                        heartRate = 17;
                        heartpitch = 1.15F;
                    }

                    if (entity.tickCount % heartRate == 0) {
                        entity.level()
                                .addParticle(
                                        ModParticles.HEARTBEAT,
                                        center.x,
                                        center.y,
                                        center.z,
                                        0,
                                        0.1,
                                        0
                                );
                        ClientUtil.playSound(randomHeart(), entity, 0.5F, heartpitch);
                    }
                }
            }
        }
        }
    }
}
