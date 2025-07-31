package net.hydra.jojomod.stand.powers.presets;

import com.google.common.collect.Lists;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.List;

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
            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.GUARD);
            return true;
        }
        return false;
    }


    /**Punching stands only go for barrages when facing players, because barrages will be interrupted 100% of the time
     * otherwise.*/
    @Override
    public void tickMobAI(LivingEntity attackTarget){
        if (attackTarget != null && attackTarget.isAlive()){
            if ((this.getActivePower() == PowerIndex.ATTACK || this.getActivePower() == PowerIndex.BARRAGE)
                    || attackTarget.distanceTo(this.getSelf()) <= 5){
                this.getSelf().setXRot(getLookAtEntityPitch(this.getSelf(), attackTarget));
                float yrot = getLookAtEntityYaw(this.getSelf(), attackTarget);
                this.getSelf().setYRot(yrot);
                this.getSelf().setYHeadRot(yrot);
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
    public boolean setPowerAttack(){
        if (((StandUser)this.getSelf()).roundabout$isParallelRunning())
        {
            return false;
        }

        if (this.activePowerPhase >= 3){
            this.activePowerPhase = 1;
        } else {
            this.activePowerPhase++;
            if (this.activePowerPhase == 3) {
                this.attackTimeMax= ClientNetworking.getAppropriateConfig().generalStandSettings.finalStandPunchInStringCooldown;
            } else {
                this.attackTimeMax= ClientNetworking.getAppropriateConfig().generalStandSettings.standPunchCooldown;
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
                if ((this.attackTimeDuring == 5 && this.activePowerPhase == 1)
                        || this.attackTimeDuring == 6) {
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

                this.takeDeterminedKnockback(this.self, entity, knockbackStrength);
            } else {
                if (this.activePowerPhase >= this.activePowerPhaseMax) {
                    knockShield2(entity, 40);
                }
            }
        } else {
            // This is less accurate raycasting as it is server sided but it is important for particle effects
            float distMax = this.getDistanceOut(this.self, this.getReach(), false);
            float halfReach = (float) (distMax * 0.5);
            Vec3 pointVec = DamageHandler.getRayPoint(self, halfReach);
            if (!this.self.level().isClientSide) {
                ((ServerLevel) this.self.level()).sendParticles(ParticleTypes.EXPLOSION, pointVec.x, pointVec.y, pointVec.z,
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
                SE = ModSounds.PUNCH_4_SOUND_EVENT;
                pitch = 1.2F;
            } else {
                SE = ModSounds.PUNCH_2_SOUND_EVENT;
            }
        } else {
            if (entity != null) {
                SE = ModSounds.PUNCH_3_SOUND_EVENT;
                pitch = 1.1F + 0.07F * activePowerPhase;
            } else {
                SE = ModSounds.PUNCH_1_SOUND_EVENT;
            }
        }

        if (!this.self.level().isClientSide()) {
            this.self.level().playSound(null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
        }
    }

    @Override
    public boolean setPowerBarrageCharge() {
        if (((StandUser)this.getSelf()).roundabout$isParallelRunning())
        {
            return false;
        }

        animateStand(StandEntity.BARRAGE_CHARGE);
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.BARRAGE_CHARGE);
        this.poseStand(OffsetIndex.ATTACK);
        this.clashDone = false;
        playBarrageChargeSound();
        return true;
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
    public boolean setPowerBarrage() {
        if (((StandUser)this.getSelf()).roundabout$isParallelRunning())
        {
            return false;
        }

        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.BARRAGE);
        this.poseStand(OffsetIndex.ATTACK);
        this.setAttackTimeMax(this.getBarrageRecoilTime());
        this.setActivePowerPhase(this.getActivePowerPhaseMax());
        animateStand(StandEntity.BARRAGE);
        playBarrageCrySound();
        return true;
    }

    @Override
    public void updateMovesFromPacket(byte activePower){
        if (activePower == PowerIndex.BARRAGE){
            this.setActivePowerPhase(this.activePowerPhaseMax);
        }
        super.updateMovesFromPacket(activePower);
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
                    this.takeDeterminedKnockback(this.self, this.getSelf(), kbs);
                    if ((kbs *= (float) (1.0 - ((LivingEntity)this.getSelf()).getAttributeValue(Attributes.KNOCKBACK_RESISTANCE))) <= 0.0) {
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

        if (this.self instanceof Player){
            if (isPacketPlayer()){
                //Roundabout.LOGGER.info("Time: "+this.self.getWorld().getTime()+" ATD: "+this.attackTimeDuring+" APP"+this.activePowerPhase);
                this.attackTimeDuring = -10;
                C2SPacketUtil.standPunchPacket(getTargetEntityId(getPunchAngle()), this.activePowerPhase);
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

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        StandUser standUser = ((StandUser) playerEntity);
        boolean standOn = standUser.roundabout$getActive();
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
                        context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
                    }
                }
            }
        }
    }

}
