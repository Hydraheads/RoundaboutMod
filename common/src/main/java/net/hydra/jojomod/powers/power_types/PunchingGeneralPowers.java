package net.hydra.jojomod.powers.power_types;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.powers.GeneralPowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PunchingGeneralPowers extends GeneralPowers {
    public PunchingGeneralPowers(LivingEntity self) {
        super(self);
    }
    public boolean isBrawling(){
        return true;
    }
    public PunchingGeneralPowers() {
        super(null);
    }
    public GeneralPowers generatePowers(LivingEntity entity){
        return new PunchingGeneralPowers(entity);
    }

    @Override
    public boolean isMining() {
        return true;
    }
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (self instanceof Player pl &&  ((IPlayerEntity)pl).roundabout$getAttackStrengthTicker() < 5) {
            return;
        }
        if (keyIsDown) {
            if (activePowerPhase == 0){
                this.tryPower(PowerIndex.ATTACK);
            }
        }
    }

    /**Override this to set the special move*/
    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.ATTACK) {
            setAttack();
        } else if (move == PowerIndex.BARRAGE_CHARGE) {
            this.setPowerBarrageCharge();
        } else if (move == PowerIndex.BARRAGE) {
            this.setPowerBarrage();
        }
        return false;
    }

    @Override
    public void onStandSummon(boolean desummon){
        if (self instanceof Player pl){
            pl.resetAttackStrengthTicker();
        }
    }
    @Override
    /**Stand related things that slow you down or speed you up, override and call super to make
     * any stand ability slow you down*/
    public float inputSpeedModifiers(float basis){
        return super.inputSpeedModifiers(basis);
    }

    public void preCheckButtonInputAttack(boolean keyIsDown, Options options) {
        if (PowerTypes.hasPowerActive(this.getSelf()) && !this.isGuarding()) {
            buttonInputAttack(keyIsDown, options);
        }
    }
    public void preCheckButtonInputUse(boolean keyIsDown, Options options) {
        if (PowerTypes.hasPowerActive(this.getSelf())) {
            buttonInputUse(keyIsDown, options);
        }
    }
    public void preCheckButtonInputBarrage(boolean keyIsDown, Options options) {

        if (PowerTypes.hasPowerActive(this.getSelf())) {
            buttonInputBarrage(keyIsDown, options);
        }
    }
    public boolean preCheckButtonInputGuard(boolean keyIsDown, Options options) {
        if (PowerTypes.hasPowerActive(this.getSelf())) {
            return buttonInputGuard(keyIsDown, options);
        }
        return false;
    }

    public boolean canGuard(){
        return !this.isBarraging();
    }
    @Override
    public boolean buttonInputGuard(boolean keyIsDown, Options options) {
        if (!this.isGuarding() && canGuard()) {
            ((StandUser)this.getSelf()).roundabout$tryPowerP(PowerIndex.GUARD,true);
            tryPowerPacket(PowerIndex.GUARD);
            return true;
        }
        return false;
    }

    public int comboAmt = 0;
    public int comboExpireTicks = 0;

    @Override
    public int getComboAmt(){
        return comboAmt;
    }
    public void setComboAmt(int comboAmt){
        this.comboAmt = comboAmt;
        if (!self.level().isClientSide() && self instanceof ServerPlayer sp){
            S2CPacketUtil.sendGenericIntToClientPacket(sp, PacketDataIndex.S2C_INT_COMBO_AMT,getComboAmt());
        }
    }
    public int getComboExpireTicks(){
        return comboExpireTicks;
    }
    public void setComboExpireTicks(int expireTicks){
        this.comboExpireTicks = expireTicks;
        if (!self.level().isClientSide() && self instanceof ServerPlayer sp){
            S2CPacketUtil.sendGenericIntToClientPacket(sp, PacketDataIndex.S2C_INT_COMBO_SEC_LEFT,getComboExpireTicks());
        }
    }



    //here

    @Override
    public void updateUniqueMoves(){
        if (this.isBarraging()) {
            if (bonusBarrageConditions()) {
                if (this.isBarrageCharging()) {
                    this.updateBarrageCharge();
                } else {
                    this.updateBarrage();
                }
            } else {
                ((StandUser) this.self).roundabout$tryPowerP(PowerIndex.NONE, true);
            }
        }
        super.updateUniqueMoves();
    }
    @Override
    public void tickPower(){
        if (!self.level().isClientSide()) {
            if (getComboExpireTicks() > 0){
                setComboExpireTicks(getComboExpireTicks()-1);
                if (getComboExpireTicks() <= 0){
                    setComboAmt(0);
                }
            }
        }
        if (this.getSelf().onGround()) {
            this.fallTime = 0;
            this.airTime = 0;
        } else {
            if (self.getDeltaMovement().y < 0) {
                this.fallTime += 1;
            }
            airTime+=1;
        }
        super.tickPower();
    }
    int fallTime = 0;
    int airTime = 0;
    public boolean canUseAirAttack() {
        if (self.level().isClientSide()) {
            return !this.getSelf().onGround()
                    && !self.isInWater()
                    && (this.fallTime > 0)
                    && (this.airTime > 6);
        }
        return false;
    }

    @Override
    public void playSummonSound() {
        if (!this.self.isCrouching()){
            this.self.level().playSound(null, self.getX(), self.getY(),
                    self.getZ(), ModSounds.HEEL_RAISE_EVENT, self.getSoundSource(), 0.9F, 1.02F);
        }
    }



    public boolean bigJumpBlocker(){
        return isBarraging() || super.bigJumpBlocker();
    }


    @Override
    public boolean setPowerGuard(){
        if (!self.level().isClientSide()) {
            if (getPlayerPos2() != PlayerPosIndex.GUARD) {
                setPlayerPos2(PlayerPosIndex.GUARD);
            }
        }
        return super.setPowerGuard();
    }



    @Override
    public ResourceKey<DamageType> getPunchDamageSource(){
        return ModDamageTypes.MARTIAL_ARTS;
    }
    @Override
    public float applyComboDamage(float dmg){
        //Every combo hit is 1% more damage
        float curve = 1 + ((float) comboAmt * 0.01F);
        //Every combo level is 20% more damage
        curve += ((float) (getComboTier()-1) * 0.2F);
        //max of 3x damage
        curve = Mth.clamp(curve,0,3F);

        dmg*=curve;

        return dmg;
    }


    @Override
    public void addToCombo(Entity targ){
        if (targ instanceof FallenMob fm && !fm.getActivated())
            return;
        int comboAmt = getComboAmt();
        setComboAmt(comboAmt+1);
        setComboExpireTicks(110);
    }


    @Override
    public boolean interceptAttack(){

        return true;
    }


    @Override
    /**If the standard right click input should usually be canceled while your stand is active*/
    public boolean interceptGuard(){
        return true;
    }

    public float getBrawlPunchAngle(){
        return 5;
    }

    public boolean hasRendered = false;
    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        hasRendered = false;
        boolean powerOn = PowerTypes.hasPowerActive(playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;

        float attackTimeDuring = getAttackTimeDuring();
        if (powerOn && isBarrageAttacking() && attackTimeDuring > -1) {
            int ClashTime = 15 - Math.round((attackTimeDuring / getBarrageLength()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);
            hasRendered = true;
        } else if (powerOn && isBarrageCharging()) {
            int ClashTime = Math.round((attackTimeDuring / getBarrageWindup()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);
            hasRendered = true;
        } else {
            int barTexture = 0;
            Entity TE = getTargetEntity(playerEntity, 3, getBrawlPunchAngle());
            float attackTimeMax = getAttackTimeMax();
            if (attackTimeMax > 0) {
                float attackTime = getAttackTime();
                float finalATime = attackTime / attackTimeMax;
                if (finalATime <= 1) {

                    if (getActivePowerPhase() == getActivePowerPhaseMax()) {
                        barTexture = 24;
                    } else if (TE != null) {
                        barTexture = 12;
                    } else {
                        barTexture = 18;
                    }


                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
                    int finalATimeInt = Math.round(finalATime * 15);
                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, barTexture, finalATimeInt, 6);
                    hasRendered = true;

                }
            }
            if (powerOn) {
                if (TE != null) {
                    if (barTexture == 0) {
                        if (this instanceof VampireGeneralPowers vgp){
                            if (isHoldingSneak()){
                                if (getTargetEntity(playerEntity, 1.5F, getBrawlPunchAngle()) != null){
                                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, 75, 15, 6);
                                    hasRendered = true;
                                }
                            } else {
                                if (getTargetEntity(playerEntity, 1.5F, getBrawlPunchAngle()) != null){
                                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, 75, 15, 6);
                                    hasRendered = true;
                                } else {
                                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
                                    hasRendered = true;
                                }
                            }
                        } else {
                            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
                            hasRendered = true;
                        }
                    }
                }
            }
        }
    }
}
