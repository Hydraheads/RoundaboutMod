package net.hydra.jojomod.fates.powers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.AccessFateFoodData;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersWalkingHeart;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
        if (canLatchOntoWall() && canWallWalkConfig()){
            this.setCooldown(PowerIndex.FATE_3, 10);
            if (!this.self.level().isClientSide()) {
                //if (!isOnWrongAxis())
                if (saveState != null){
                    this.self.level().playSound(
                            null,
                            this.self.blockPosition(),
                            saveState.getSoundType().getBreakSound(),
                            SoundSource.PLAYERS,
                            1.0F,
                            0.9F);
                    blockBreakParticles(saveState.getBlock(),
                            new Vec3(self.getX(),
                                    self.getY(),
                                    self.getZ()));
                }
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.VAMPIRE_WALL_GRIP_EVENT, SoundSource.PLAYERS, 2F, 1f);
                //toggleSpikes(true);
                Direction gd = RotationUtil.getRealFacingDirection2(this.self);
                setWallWalkDirection(gd);
                ((IGravityEntity) this.self).roundabout$setGravityDirection(gd);
                justFlippedTicks = 7;
            }
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

            if (isVisionOn()){
                if (dimTickEye > 0) {
                    dimTickEye--;
                }
            } else {
                if (dimTickEye < 10) {
                    dimTickEye++;
                }
            }

            if (!isHearing()){
                if (dimTickHearing > 0) {
                    dimTickHearing--;
                }
            } else {
                if (dimTickHearing < 10) {
                    dimTickHearing++;
                }
            }

            if (isPlantedInWall() && !getStandUserSelf().rdbt$getJumping()){
                if (!self.onGround()) {
                    if (this.self.getDeltaMovement().y < 0){
                        this.self.setDeltaMovement(this.self.getDeltaMovement().add(0,-0.14,0));
                    }
                }
            }
        } else {

            if (self.isSwimming()) {
                setWallWalkDirection(getIntendedDirection());
            }

            if (isPlantedInWall()){
                if (justFlippedTicks > 0){
                    justFlippedTicks--;
                } else {
                    Vec3 newVec = new Vec3(0,-0.2,0);
                    Vec3 newVec2 = new Vec3(0,-1.0,0);
                    Vec3 newVec4 = new Vec3(0,-0.5,0);
                    Vec3 newVec5 = new Vec3(0,-1.1,0);

                    newVec = RotationUtil.vecPlayerToWorld(newVec,((IGravityEntity)self).roundabout$getGravityDirection());
                    BlockPos pos = BlockPos.containing(self.getPosition(1).add(newVec));
                    newVec2 = RotationUtil.vecPlayerToWorld(newVec2,((IGravityEntity)self).roundabout$getGravityDirection());
                    BlockPos pos2 = BlockPos.containing(self.getPosition(1).add(newVec2));
                    newVec4 = RotationUtil.vecPlayerToWorld(newVec4,((IGravityEntity)self).roundabout$getGravityDirection());
                    BlockPos pos4 = BlockPos.containing(self.getPosition(1).add(newVec4));
                    newVec5 = RotationUtil.vecPlayerToWorld(newVec5,((IGravityEntity)self).roundabout$getGravityDirection());
                    BlockPos pos5 = BlockPos.containing(self.getPosition(1).add(newVec5));

                    BlockState state1 = self.level().getBlockState(pos);
                    BlockState state2 = self.level().getBlockState(pos2);
                    BlockState state4 = self.level().getBlockState(pos4);
                    BlockState state5 = self.level().getBlockState(pos5);
                    boolean isOnValidBlock =  MainUtil.isBlockWalkableSimplified(state1)
                            && MainUtil.isBlockWalkableSimplified(state4);

                    if (self.onGround() && MainUtil.isBlockWalkableSimplified(self.getBlockStateOn())
                            && isOnValidBlock){
                        mercyTicks = 5;
                    } else {
                        if (
                                (
                                        MainUtil.isBlockWalkable(self.level().getBlockState(pos))
                                                || MainUtil.isBlockWalkable(self.level().getBlockState(pos2))
                                                || MainUtil.isBlockWalkable(self.level().getBlockState(pos4))
                                                || MainUtil.isBlockWalkable(self.level().getBlockState(pos5))
                                )){
                            mercyTicks--;
                        } else {
                            mercyTicks = 0;
                        }
                    }
                    if (self.isSleeping() || ((!self.onGround() || !isOnValidBlock) && mercyTicks <= 0) || self.getRootVehicle() != this.self) {
                        wallWalkDirection = getIntendedDirection();
                        ((IGravityEntity) this.self).roundabout$setGravityDirection(wallWalkDirection);
                        setWallWalkDirection(wallWalkDirection);
                    }
                }

            } else {
                setWallWalkDirection(getIntendedDirection());
            }
        }
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
                Entity TE = getTargetEntity(self, 3, 15);
                if (TE != null && MainUtil.canDrinkBloodFair(TE, self)
                        && self.hurtTime <= 0 && bloodSuckingTarget.is(TE)) {
                    if (TE instanceof LivingEntity LE) {
                        ((StandUser) LE).roundabout$setDazed((byte) 3);
                        LE.setDeltaMovement(0,-0.1F,0);
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
                && getActivePower() != BLOOD_REGEN;
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
                tryPowerPacket(BLOOD_SPEED);
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
            //test
            if (isHearing()){
                stopHearingClient();
            }
            tryPower(WALL_WALK, true);
            tryPowerPacket(WALL_WALK);
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

        }
    }
    public void bloodSpeed(){
        if (canUseBloodSpeed()) {
            if (self instanceof Player PE && !PE.isCreative()){
                int foodLevel = PE.getFoodData().getFoodLevel();
                PE.getFoodData().setFoodLevel(foodLevel-10);
            }
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
        if (bloodSuckingTarget != null && self instanceof Player pl) {

            boolean canDrainGood = MainUtil.canDrinkBloodCrit(bloodSuckingTarget,self);
            DamageSource sauce = ModDamageTypes.of(self.level(),
                    ModDamageTypes.BLOOD_DRAIN);
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
        if (activePower == BLOOD_SUCK && move != BLOOD_SUCK && !self.level().isClientSide()) {
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
                super.setPlayerPos2(PlayerPosIndex.BLOOD_SUCK);
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
        } else if (getActivePower() == SUPER_HEARING){
            basis*=0.2F;
        } else if (isFast()){
            basis*=2F;
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
        return getActivePower() == BLOOD_SUCK || getActivePower() == BLOOD_REGEN
                || getActivePower() == SUPER_HEARING;
    }
    @Override
    /**Cancel all sprinting*/
    public boolean cancelSprint(){
        return getActivePower() == BLOOD_SUCK || getActivePower() == BLOOD_REGEN
                || getActivePower() == SUPER_HEARING;
    }
    @Override
    public boolean cancelSprintParticles(){
        return getActivePower() == BLOOD_SUCK || getActivePower() == BLOOD_REGEN
                || isPlantedInWall() || getActivePower() == SUPER_HEARING;
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
}
