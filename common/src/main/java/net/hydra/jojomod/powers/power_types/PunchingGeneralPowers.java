package net.hydra.jojomod.powers.power_types;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.powers.GeneralPowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

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
        if (keyIsDown) {
            if (activePowerPhase == 0){
                this.tryPower(PowerIndex.ATTACK);
            }
        }
    }

    @Override
    /**Stand related things that slow you down or speed you up, override and call super to make
     * any stand ability slow you down*/
    public float inputSpeedModifiers(float basis){
        StandUser standUser = ((StandUser) this.getSelf());
        if (isGuarding() && this.getSelf().getVehicle() == null) {
            basis*=0.2f;
        }
        return basis;
    }

    public boolean cancelSprintJump(){
        return this.isGuarding();
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
    @Override
    public void tickPower(){
        if (!self.level().isClientSide()) {
            if (getActivePower() != PowerIndex.GUARD && getPlayerPos2() == PlayerPosIndex.GUARD) {
                setPlayerPos2(PlayerPosIndex.NONE);
            }

            if (getComboExpireTicks() > 0){
                setComboExpireTicks(getComboExpireTicks()-1);
                if (getComboExpireTicks() <= 0){
                    setComboAmt(0);
                }
            }
        }
        super.tickPower();
    }

    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime) {
        if (!self.level().isClientSide()) {
            if (move == PowerIndex.ATTACK) {
                attackTargetId = chargeTime;
            }
        }
        return super.tryPower(move,forced);
    }


    @Override
    public boolean tryPower(int move, boolean forced) {
        if (!self.level().isClientSide()) {
            if (move != PowerIndex.GUARD && getPlayerPos2() == PlayerPosIndex.GUARD) {
                setPlayerPos2(PlayerPosIndex.NONE);
            }
        }
        return super.tryPower(move,forced);
    }

    public int attackTargetId = -1;

    @Override
    /**Override this to set the special move*/
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.ATTACK) {
            setAttack();
        }

        return super.setPowerOther(move,lastMove);
    }
    public void setAttack(){
        this.attackTimeMax= 7;
        this.attackTimeDuring = 0;
        this.setAttackTime(0);
        setActivePowerPhase((byte) 1);
        if (!self.level().isClientSide()) {
            Entity target = null;
            if (attackTargetId > 0) {
                target = self.level().getEntity(attackTargetId);
            }
            punchImpact(target);
        } else {
            Entity TE = getTargetEntity(self, 3, getPunchAngle());
            int id = 0;
            if (TE != null){
                id = TE.getId();
            }
            tryIntPowerPacket(PowerIndex.ATTACK,id);
        }
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

    public int getComboTier(){
        if (comboAmt <= 9){
            return 1;
        } else if (comboAmt <= 24){
            return 2;
        } else if (comboAmt <= 49){
            return 3;
        }
        return 4;
    }

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

    public void punchImpact(Entity entity) {
        if (!this.self.level().isClientSide()) {
            self.swing(InteractionHand.MAIN_HAND, true);
            if (entity != null) {
                float pow;
                float knockbackStrength;
                pow = getPunchStrength(entity);
                pow = applyComboDamage(pow);
                knockbackStrength = 0.10F;

                if (DamageHandler.VampireDamageEntity(entity, pow, this.self)) {
                    takeDeterminedKnockbackWithY2(this.self, entity, knockbackStrength);
                    this.self.level().playSound(null, this.self.blockPosition(), getPunchSound(), SoundSource.PLAYERS, 1F, (float) (0.95f + Math.random() * 0.1f));
                    int comboAmt = getComboAmt();
                    setComboAmt(comboAmt+1);
                    setComboExpireTicks(100);
                } else {
                    if (!this.self.level().isClientSide()) {
                        this.self.level().playSound(null, this.self.blockPosition(), ModSounds.MELEE_GUARD_SOUND_EVENT, SoundSource.PLAYERS, 1F, (float) (0.95f + Math.random() * 0.1f));
                    }
                }
            }
        }
    }
    public float getPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 0.75F;
        } else {
            return 2.0F;
        }
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

    public float getPunchAngle(){
        return 5;
    }

    public SoundEvent getPunchSound(){
        double rand = Math.random();
        if (rand < 0.25){
            return ModSounds.COMBAT_PUNCH_1_EVENT;
        } else if (rand < 0.5){
            return ModSounds.COMBAT_PUNCH_2_EVENT;
        } else if (rand < 0.75){
            return ModSounds.COMBAT_PUNCH_3_EVENT;
        }
        return ModSounds.COMBAT_PUNCH_4_EVENT;
    }

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        StandUser standUser = ((StandUser) playerEntity);
        boolean powerOn = PowerTypes.hasPowerActive(playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;

        float attackTimeDuring = standUser.roundabout$getAttackTimeDuring();
        if (powerOn && standUser.roundabout$getStandPowers().isBarrageAttacking() && attackTimeDuring > -1) {
            int ClashTime = 15 - Math.round((attackTimeDuring / standUser.roundabout$getStandPowers().getBarrageLength()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else if (powerOn && standUser.roundabout$getStandPowers().isBarrageCharging()) {
            int ClashTime = Math.round((attackTimeDuring / standUser.roundabout$getStandPowers().getBarrageWindup()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else {
            int barTexture = 0;
            Entity TE = getTargetEntity(playerEntity, 3, getPunchAngle());
            float attackTimeMax = getAttackTimeMax();
            if (attackTimeMax > 0) {
                float attackTime = getAttackTime();
                float finalATime = attackTime / attackTimeMax;
                if (finalATime <= 1) {
                    if (TE != null) {
                        barTexture = 12;
                    } else {
                        barTexture = 18;
                    }


                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
                    int finalATimeInt = Math.round(finalATime * 15);
                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, barTexture, finalATimeInt, 6);


                }
            }
            if (powerOn) {
                if (TE != null) {
                    if (barTexture == 0) {
                        context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
                    }
                }
            }
        }
    }
}
