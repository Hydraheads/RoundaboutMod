package net.hydra.jojomod.stand.powers.presets;

import com.google.common.collect.Lists;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.GlaiveItem;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.powers.power_types.VampireGeneralPowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersStarPlatinum;
import net.hydra.jojomod.stand.powers.PowersTheWorld;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Objects;

public class NewPunchingStand extends NewDashPreset {
    public NewPunchingStand(LivingEntity self) {
        super(self);
    }
    @Override
    public boolean canSummonStand(){
        return true;
    }


    @Override
    public boolean interceptAttack(){
        return true;
    }
    @Override
    public boolean interceptGuard(){
        return true;
    }

    @Override
    public boolean isMiningStand() {
        return true;
    }
    public boolean consumeClickInput = false;

    public SoundEvent getLastRejectionHitSound(){
        return null;
    }

    public boolean wentForCharge = false;

    public boolean canGuard(){
        return !this.isBarraging() && !this.isClashing();
    }
    @Override
    public boolean buttonInputGuard(boolean keyIsDown, Options options) {
        if (!this.isGuarding() && canGuard()) {
            ((StandUser)this.getSelf()).roundabout$tryPower(PowerIndex.GUARD,true);
            tryPowerPacket(PowerIndex.GUARD);
            return true;
        }
        return false;
    }

    @Override
    public boolean canClash(){
        return true;
    }

    /**If you override this for any reason, you should probably call the super(). Although SP and TW override
     * this, you can probably do better*/
    public void barrageImpact(Entity entity, int hitNumber){
        if (this.isBarrageAttacking()) {
            if (bonusBarrageConditions()) {
                boolean sideHit = false;
                if (hitNumber > 1000){
                    if (!(ClientNetworking.getAppropriateConfig().generalStandSettings.barrageHasAreaOfEffect)){
                        return;
                    }
                    hitNumber-=1000;
                    sideHit = true;
                }
                boolean lastHit = (hitNumber >= this.getBarrageLength());
                if (entity != null) {
                    if (entity instanceof LivingEntity && ((StandUser) entity).roundabout$isBarraging() && ((StandUser) entity).roundabout$getStandPowers().canClash()
                            && ((StandUser) entity).roundabout$getAttackTimeDuring() > -1 && !(((TimeStop)this.getSelf().level()).CanTimeStopEntity(entity))  && !this.getStandUserSelf().roundabout$isPossessed()   ) {
                        initiateClash(entity);
                    } else {
                        hitParticles(entity);

                        float pow;
                        float knockbackStrength = 0;
                        /**By saving the velocity before hitting, we can let people approach barraging foes
                         * through shields.*/
                        Vec3 prevVelocity = entity.getDeltaMovement();
                        if (lastHit) {
                            pow = this.getBarrageFinisherStrength(entity);
                            knockbackStrength = this.getBarrageFinisherKnockback();
                        } else {
                            pow = this.getBarrageHitStrength(entity);
                            float mn = this.getBarrageLength() - hitNumber;
                            if (mn == 0) {
                                mn = 0.015F;
                            } else {
                                mn = ((0.015F / (mn)));
                            }
                            knockbackStrength = 0.014F - mn;
                        }

                        if (sideHit){
                            pow/=4;
                            knockbackStrength/=6;
                        }

                        if (StandRushDamageEntityAttack(entity, pow, 0.0001F, this.self)) {
                            if (entity instanceof LivingEntity LE) {
                                if (lastHit) {
                                    setDazed((LivingEntity) entity, (byte) 0);

                                    if (!sideHit) {
                                        ((StandUser)LE).roundabout$setDestructionTrailTicks(80);
                                        addEXP(8,LE);
                                        playBarrageEndNoise(0, entity);
                                    }
                                } else {
                                    setDazed((LivingEntity) entity, (byte) 3);
                                    if (!sideHit) {
                                        playBarrageNoise(hitNumber, entity);
                                    }
                                }
                            }
                            barrageImpact2(entity, lastHit, knockbackStrength);
                        } else {
                            if (lastHit) {
                                knockShield2(entity, 200);
                                if (!sideHit) {
                                    playBarrageBlockEndNoise(0, entity);
                                }
                            } else {
                                entity.setDeltaMovement(prevVelocity);
                                playBarrageBlockNoise();
                            }
                        }
                    }
                } else {
                    if (!sideHit) {
                        playBarrageMissNoise(hitNumber);
                    }
                }

                if (lastHit) {
                    animateStand(StandEntity.BARRAGE_FINISHER);
                    this.attackTimeDuring = -10;
                }
            } else {
                ((StandUser) this.self).roundabout$tryPower(PowerIndex.NONE, true);
            }
        } else {
            ((StandUser) this.self).roundabout$tryPower(PowerIndex.NONE, true);
        }
    }


    public void updateBarrage(){
        if (this.attackTimeDuring == -2 && this.getSelf() instanceof Player) {
            ((StandUser) this.self).roundabout$tryPower(PowerIndex.GUARD, true);
        } else {
            if (this.attackTimeDuring > this.getBarrageLength()) {
                this.attackTimeDuring = -20;
            } else {
                if (this.attackTimeDuring > 0) {
                    this.setAttackTime((getBarrageRecoilTime() - 1) -
                            Math.round(((float) this.attackTimeDuring / this.getBarrageLength())
                                    * (getBarrageRecoilTime() - 1)));

                    standBarrageHit();
                }
            }
        }
    }

    @Override
    public int getBarrageRecoilTime(){
        return ClientNetworking.getAppropriateConfig().
                generalStandSettings.barrageRecoilCooldown;
    }
    /**Punching stands only go for barrages when facing players, because barrages will be interrupted 100% of the time
     * otherwise.*/
    @Override
    public void tickMobAI(LivingEntity attackTarget){
        if (attackTarget != null && attackTarget.isAlive()){
            if ((this.getActivePower() == PowerIndex.ATTACK || this.getActivePower() == PowerIndex.BARRAGE)
                    || attackTarget.distanceTo(this.getSelf()) <= 5){
                rotateMobHead(attackTarget);
            }

            Entity targetEntity = getTargetEntity(this.self, -1);
            if (targetEntity != null && targetEntity.is(attackTarget)) {
                if (this.attackTimeDuring <= -1) {
                    double RNG = Math.random();
                    if (RNG < 0.35 && targetEntity instanceof Player && this.activePowerPhase <= 0 && !wentForCharge){
                        wentForCharge = true;
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.BARRAGE_CHARGE, true);
                    } else if (this.activePowerPhase < this.activePowerPhaseMax || this.attackTime >= this.attackTimeMax) {
                        wentForCharge = false;
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.ATTACK, true);
                    }
                }
            }
        }
    }

    @Override
    public int getBarrageLength(){
        return 60;
    }

    @Override
    public float getRushDistance(){
        return getReach();
    }

    public int getMeltLevel(){
        if (self.hasEffect(ModEffects.STAND_MELTING)) {
            MobEffectInstance melt = self.getEffect(ModEffects.STAND_MELTING);
            if (melt != null) {
                return melt.getAmplifier() + 1;
            }
        }
        return 0;
    }

    @Override
    public boolean setPowerAttack(){
        if (this.activePowerPhase >= 3){
            this.activePowerPhase = 1;
        } else {
            this.activePowerPhase++;
            if (this.activePowerPhase == 3) {
                this.attackTimeMax= ClientNetworking.getAppropriateConfig().generalStandSettings.finalStandPunchInStringCooldown
                + getMeltLevel()*3;
            } else {
                this.attackTimeMax= ClientNetworking.getAppropriateConfig().generalStandSettings.standPunchCooldown
                        + getMeltLevel()*3;
            }

        }

        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.ATTACK);
        this.setAttackTime(0);

        animateStand(this.activePowerPhase);
        poseStand(OffsetIndex.ATTACK);
        return true;
    }

    @Override
    public void updateAttack(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > this.attackTimeMax) {
                this.attackTime = -1;
                this.attackTimeMax = 0;
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE,true);
            } else {
                int meltLevel = getMeltLevel();
                if ((this.attackTimeDuring == (5+meltLevel) && this.activePowerPhase == 1)
                        || this.attackTimeDuring == (6+meltLevel)) {
                    this.standPunch();
                }
            }
        }
    }

    public void playTheLastHitSound(){
        Byte LastHitSound = this.getLastHitSound();
        this.playStandUserOnlySoundsIfNearby(LastHitSound, 15, false,
                true);
    }
    @Override
    public void punchImpact(Entity entity){
        this.setAttackTimeDuring(-10);

        if (entity != null && entity.distanceTo(self) > getReach()+1) {
            entity = null;
        }
        if (entity != null) {
            float pow;
            float knockbackStrength;
            boolean lasthit = false;
            if (this.getActivePowerPhase() >= this.getActivePowerPhaseMax()) {
                /*The last hit in a string has more power and knockback if you commit to it*/
                pow = getHeavyPunchStrength(entity);
                knockbackStrength = 1F;
                lasthit = true;
            } else {
                pow = getPunchStrength(entity);
                knockbackStrength = 0.2F;
            }
            if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                if (entity instanceof LivingEntity LE){

                    if (lasthit){addEXP(2,LE);} else {addEXP(1,LE);}
                }

                takeDeterminedKnockback(this.self, entity, knockbackStrength);
            } else {
                if (this.activePowerPhase >= this.activePowerPhaseMax) {
                    if (entity instanceof LivingEntity LE && ((StandUser)LE).roundabout$getStandPowers().interceptGuard()
                    && LE.isBlocking() && !((StandUser) LE).roundabout$isGuarding()){
                        knockShield2(entity, 60);
                    } else {
                        knockShield2(entity, 40);
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
        if (this.activePowerPhase >= this.activePowerPhaseMax) {

            if (!this.self.level().isClientSide()) {
                playTheLastHitSound();
            }

            if (entity != null) {
                SE = getPunchLandLastSound();
                pitch = getPunchLandLastPitch();
            } else {
                SE = ModSounds.PUNCH_2_SOUND_EVENT;
            }
        } else {
            if (entity != null) {
                SE = getPunchLandSound();
                pitch = getPunchLandPitch();
            } else {
                SE = ModSounds.PUNCH_1_SOUND_EVENT;
            }
        }

        if (!this.self.level().isClientSide()) {
            if (entity != null) {
                hitParticles(entity);
            } else {
            }
            this.self.level().playSound(null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
        }
    }



    @Override
    public boolean setPowerBarrageCharge() {
        animateStand(StandEntity.BARRAGE_CHARGE);
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.BARRAGE_CHARGE);
        this.poseStand(OffsetIndex.ATTACK);
        this.clashDone = false;
        playBarrageChargeSound();
        return true;
    }

    public float getPunchLandPitch(){
        return 1.1F + 0.07F * activePowerPhase;
    }
    public float getPunchLandLastPitch(){
        return 1.2F;
    }

    public SoundEvent getPunchLandSound(){
        return ModSounds.PUNCH_3_SOUND_EVENT;
    }
    public SoundEvent getPunchLandLastSound(){
        return ModSounds.PUNCH_4_SOUND_EVENT;
    }

    @Override
    public List<Byte> getPosList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add((byte) 0);
        $$1.add((byte) 1);
        $$1.add((byte) 2);
        $$1.add((byte) 3);
        $$1.add((byte) 4);
        return $$1;
    }

    @Override
    public void setPowerBarrage() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.BARRAGE);
        this.poseStand(OffsetIndex.ATTACK);
        this.setAttackTimeMax(this.getBarrageRecoilTime());
        this.setActivePowerPhase(this.getActivePowerPhaseMax());
        animateStand(StandEntity.BARRAGE);
        playBarrageCrySound();
    }

    @Override
    /**Override this to set the special move*/
    public boolean setPowerOther(int move, int lastMove) {
        return false;
    }

    @Override
    public void tickStandRejection(MobEffectInstance effect){
        if (!this.getSelf().level().isClientSide()) {
            float kbs = 0;
            float pow = 0;
            boolean throwPunch = false;
            SoundEvent SE = null;
            float pitch = 0;
            if (effect.getDuration() == 13 || effect.getDuration() == 7) {
                kbs = 0.2F;
                pow = getPunchStrength(this.getSelf());
                throwPunch = true;
                SE = ModSounds.PUNCH_3_SOUND_EVENT;
                if (effect.getDuration() == 7) {
                    pitch = 1.24F;
                } else {
                    pitch = 1.17F;
                }
            } else if (effect.getDuration() == 1) {
                kbs = 1F;
                pow = getHeavyPunchStrength(this.getSelf());
                throwPunch = true;
                SE = ModSounds.PUNCH_4_SOUND_EVENT;
                pitch = 1.2F;
                if (!this.self.level().isClientSide()) {
                    Byte LastHitSound = this.getLastHitSound();
                    this.playStandUserOnlySoundsIfNearby(LastHitSound, 15, false,
                            true);
                }
            }

            if (throwPunch) {
                this.self.level().playSound(null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
                if (StandDamageEntityAttack(this.getSelf(), pow, 0, this.self)) {
                    takeDeterminedKnockback(this.self, this.getSelf(), kbs);
                    if ((kbs *= (float) (1.0 - ((LivingEntity)this.getSelf()).getAttributeValue(Attributes.KNOCKBACK_RESISTANCE))) <= 0.0) {
                        return;
                    }
                    if (MainUtil.isKnockbackImmune(this.getSelf())){
                        return;
                    }
                    this.getSelf().hurtMarked = true;
                    Vec3 vec3d2 = new Vec3(Mth.sin(
                            this.getSelf().getYRot() * ((float) Math.PI / 180)),
                            0,
                            -Mth.cos(this.getSelf().getYRot() * ((float) Math.PI / 180))).normalize().scale(kbs).reverse();
                    this.getSelf().setDeltaMovement(- vec3d2.x,
                            this.getSelf().onGround() ? 0.28 : 0,
                            - vec3d2.z);
                    this.getSelf().hasImpulse = true;
                }
            }
        }
    }

    public void standPunch(){
        /*By setting this to -10, there is a delay between the stand retracting*/

        if (this.self instanceof Player pl){
            if (isPacketPlayer()){
                //Roundabout.LOGGER.info("Time: "+this.self.getWorld().getTime()+" ATD: "+this.attackTimeDuring+" APP"+this.activePowerPhase);
                this.attackTimeDuring = -10;
                C2SPacketUtil.standPunchPacket(getTargetEntityId(getPunchAngle()), this.activePowerPhase);
                if (this.activePowerPhase >= this.activePowerPhaseMax){
                    if (self.getMainHandItem().getItem() instanceof TieredItem
                    ){
                        pl.resetAttackStrengthTicker();
                    }
                }
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,-1,getPunchAngle());
            punchImpact(targetEntity);
        }

    }

    public float getPunchAngle(){
        return ClientNetworking.getAppropriateConfig().generalStandSettings.basePunchAngle;
    }
    public float getImpalePunchStrength(Entity entity){
        return 0;
    }
    public float getImpaleKnockback(){
        return 1.3F;
    }

    public static final float impaleRange = 3.5F;
    public boolean airTriggered = false;
    public void impaleImpact(Entity entity){
        if (activePower == PowerIndex.POWER_1_SNEAK){
            this.setAttackTimeDuring(-20);
            if (entity != null && entity.distanceTo(self) > impaleRange+0.75F) {
                entity = null;
            }
            if (entity != null) {
                hitParticlesCenter(entity);

                float pow;
                float knockbackStrength;
                pow = getImpalePunchStrength(entity);
                knockbackStrength = getImpaleKnockback();
                if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                    if (entity instanceof LivingEntity LE) {
                        addEXP(5, LE);
                        if (MainUtil.getMobBleed(entity)) {
                            if (!airTriggered) {
                                if ((((TimeStop) this.getSelf().level()).CanTimeStopEntity(entity))) {
                                    MainUtil.makeBleed(entity, 0, 200, this.getSelf());
                                } else {
                                    MainUtil.makeBleed(entity, 2, 200, this.getSelf());
                                }
                                MainUtil.makeMobBleed(entity);
                            }
                        }
                    }
                    takeDeterminedKnockback(this.self, entity, knockbackStrength);
                } else {
                    knockShield2(entity, 100);
                }
            }

            if (this.getSelf() instanceof Player) {
                S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_1_SNEAK, ClientNetworking.getAppropriateConfig().generalStandSettings.impaleAttackCooldown);
            }
            this.setCooldown(PowerIndex.SKILL_1_SNEAK, ClientNetworking.getAppropriateConfig().generalStandSettings.impaleAttackCooldown);
            SoundEvent SE;
            float pitch = 1F;
            if (entity != null) {
                playImpaleConnectSoundExtra();
                if (airTriggered){
                    SE = ModSounds.PUNCH_4_SOUND_EVENT;
                } else {
                    SE = getImpaleSound();
                }
                pitch = 1.2F;
            } else {
                SE = ModSounds.PUNCH_2_SOUND_EVENT;
            }

            if (!this.self.level().isClientSide()) {
                this.self.level().playSound(null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
            }
        }
    }
    public void playImpaleConnectSoundExtra(){

    }

    public static final byte IMPALE_NOISE = 105;
    public static final byte EPITAPH_NOISE = 106;
    public static final byte EPITAPH_FADE_NOISE = 107;
    public boolean impale(){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){

            airTriggered = (((StandUser) this.getSelf()).roundabout$getLeapTicks() > 0);
            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_1_SNEAK);
            playSoundsIfNearby(IMPALE_NOISE, 27, false);
            this.animateStand(StandEntity.IMPALE);
            this.poseStand(OffsetIndex.GUARD);

            return true;
        }
        return false;
    }
    public SoundEvent getImpaleSound(){
        return ModSounds.IMPALE_HIT_EVENT;

    }
    public int impaleTicks = 0;
    public int ticksUntilCanImpale = 0;
    public boolean canImpale(){
        return ticksUntilCanImpale <= 0;
    }
    public int meltIFrames = 0;
    public void tickPower(){
        if (ticksUntilCanImpale > 0){
            ticksUntilCanImpale--;
        }
        if (impaleTicks > 0){
            impaleTicks--;
        }
        if (!self.level().isClientSide()){
            if (meltIFrames > 0){
                meltIFrames--;
            }
        }
        super.tickPower();
    }

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        StandUser standUser = ((StandUser) playerEntity);
        boolean standOn = PowerTypes.hasStandActive(playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;

        float attackTimeDuring = standUser.roundabout$getAttackTimeDuring();
        if (standOn && standUser.roundabout$isClashing()) {
            int ClashTime = 15 - Math.round((attackTimeDuring / 60) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else if (standOn && standUser.roundabout$getStandPowers().isBarrageAttacking() && attackTimeDuring > -1) {
            int ClashTime = 15 - Math.round((attackTimeDuring / standUser.roundabout$getStandPowers().getBarrageLength()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else if (standOn && standUser.roundabout$getStandPowers().isBarrageCharging()) {
            int ClashTime = Math.round((attackTimeDuring / standUser.roundabout$getStandPowers().getBarrageWindup()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else {
            int barTexture = 0;
            Entity TE = standUser.roundabout$getStandPowers().getTargetEntity(playerEntity, -1, getPunchAngle());
            float attackTimeMax = standUser.roundabout$getAttackTimeMax();
            if (attackTimeMax > 0) {
                float attackTime = standUser.roundabout$getAttackTime();
                float finalATime = attackTime / attackTimeMax;
                if (finalATime <= 1) {


                    if (standUser.roundabout$getActivePowerPhase() == standUser.roundabout$getActivePowerPhaseMax()) {
                        barTexture = 24;
                    } else {
                        if (TE != null) {
                            barTexture = 12;
                        } else {
                            barTexture = 18;
                        }
                    }


                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
                    int finalATimeInt = Math.round(finalATime * 15);
                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, barTexture, finalATimeInt, 6);


                }
            }
            if (standOn) {
                if (TE != null) {
                    if (barTexture == 0) {
                        boolean converted = false;
                        // Mob Grab range shows green
                        if (this instanceof BlockGrabPreset bgp && (this instanceof PowersStarPlatinum || this instanceof PowersTheWorld)){
                            Entity targetEntity = MainUtil.getTargetEntity(this.getSelf(), 2.1F);
                            Entity targetEntity2 = MainUtil.getTargetEntity(this.getSelf(), 5F);
                            if (targetEntity2 != null && bgp.canGrab(targetEntity2)) {
                                if (targetEntity != null && bgp.canGrab(targetEntity)) {
                                    converted = true;
                                }
                            }
                        }
                        if (!converted) {
                            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
                        } else {
                            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 82, 15, 6);
                        }
                    }
                }
            }
        }
    }

}
