package net.hydra.jojomod.powers.power_types;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.powers.GeneralPowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
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
        } else if (this.isBarrageAttacking() || standUser.roundabout$isClashing()) {
            basis*=0.2f;
        } else if (this.isBarrageCharging()) {
            basis*=0.3f;
        }
        return basis;
    }

    public boolean cancelSprintJump(){
        return this.isGuarding() || getActivePower() == PowerIndex.BARRAGE_CHARGE ||
                getActivePower() == PowerIndex.BARRAGE;
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

        if (!this.self.level().isClientSide && this.isBarraging()  && (move != PowerIndex.BARRAGE && move != PowerIndex.BARRAGE_CLASH
                && move != PowerIndex.BARRAGE_CHARGE && move != PowerIndex.GUARD) && this.attackTimeDuring  > -1){
            this.stopSoundsIfNearby(SoundIndex.BARRAGE_SOUND_GROUP, 100,false);

        }
        return super.tryPower(move,forced);
    }

    public int attackTargetId = -1;
    public void setPowerBarrage() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.BARRAGE);
        this.setAttackTimeMax(this.getBarrageRecoilTime());
        this.setActivePowerPhase(this.getActivePowerPhaseMax());
        playBarrageCrySound();
    }
    public void setPowerBarrageCharge() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.BARRAGE_CHARGE);
        playBarrageChargeSound();
    }
    @Override
    /**Override this to set the special move*/
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.ATTACK) {
            setAttack();
        } else if (move == PowerIndex.BARRAGE_CHARGE) {
            this.setPowerBarrageCharge();
        } else if (move == PowerIndex.BARRAGE) {
            this.setPowerBarrage();
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
    public int getBarrageLength(){
        return 30;
    }
    public int getBarrageWindup(){
        return 21;
    }
    public void updateBarrageCharge(){
        if (this.attackTimeDuring >= this.getBarrageWindup()) {
            ((StandUser) this.self).roundabout$tryPowerP(PowerIndex.BARRAGE, true);
        }
    }
    public void updateBarrage(){
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

    /**This happens every time a stand barrage hits, generally you dont want to override this unless
     * your stand's barrage operates very differently*/
    public void standBarrageHit(){
        if (this.self instanceof Player){
            if (isPacketPlayer()){
                List<Entity> listE = getTargetEntityList(this.self,-1);
                int id = -1;
                if (storeEnt != null){
                    id = storeEnt.getId();
                }
                C2SPacketUtil.standBarrageHitPacket(id, this.attackTimeDuring);
                if (!listE.isEmpty() && ClientNetworking.getAppropriateConfig().generalStandSettings.barrageHasAreaOfEffect){
                    for (int i = 0; i< listE.size(); i++){
                        if (!(storeEnt != null && listE.get(i).is(storeEnt))) {
                            if (!(listE.get(i) instanceof StandEntity) && listE.get(i).distanceTo(this.self) < 3.5) {
                                C2SPacketUtil.standBarrageHitPacket(listE.get(i).getId(), this.attackTimeDuring + 1000);
                            }
                        }
                    }
                }

                if (this.attackTimeDuring == this.getBarrageLength()){
                    this.attackTimeDuring = -10;
                }
            }
        } else {
            /*Caps how far out the barrage hit goes*/
            Entity targetEntity = getTargetEntity(this.self,-1);

            List<Entity> listE = getTargetEntityList(this.self,-1);
            barrageImpact(storeEnt, this.attackTimeDuring);
            if (!listE.isEmpty()){
                for (int i = 0; i< listE.size(); i++){
                    if (!(storeEnt != null && listE.get(i).is(storeEnt))) {
                        if (!(listE.get(i) instanceof StandEntity) && listE.get(i).distanceTo(this.self) < 3.5) {
                            barrageImpact(listE.get(i), this.attackTimeDuring + 1000);
                        }
                    }
                }
            }

        }
    }

    public float getBarrageFinisherKnockback(){
        return 1.0F;
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
                } else {
                    if (!sideHit) {
                        playBarrageMissNoise(hitNumber);
                    }
                }

                if (lastHit) {
                    this.attackTimeDuring = -10;
                }
            } else {
                ((StandUser) this.self).roundabout$tryPower(PowerIndex.NONE, true);
            }
        } else {
            ((StandUser) this.self).roundabout$tryPower(PowerIndex.NONE, true);
        }
    }
    public float getBarrageDamagePlayer(){
        return applyComboDamage(6);
    }
    public float getBarrageDamageMob(){
        return applyComboDamage(10);
    }
    public float getBarrageFinisherStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return applyComboDamage(2);
        } else {
            return applyComboDamage(4);
        }
    }
    public float getBarrageHitStrength(Entity entity){
        float barrageLength = this.getBarrageLength();
        float power;
        if (this.getReducedDamage(entity)){
            power = getBarrageDamagePlayer()/barrageLength;
        } else {
            power = getBarrageDamageMob()/barrageLength;
        }
        /*Barrage hits are incapable of killing their target until the last hit.*/
        if (entity instanceof LivingEntity){
            if (power >= ((LivingEntity) entity).getHealth() && ClientNetworking.getAppropriateConfig().generalStandSettings.barragesOnlyKillOnLastHit){
                if (entity instanceof Player) {
                    power = 0.00001F;
                } else {
                    power = 0F;
                }
            }
        }
        return power;
    }


    public void playBarrageMissNoise(int hitNumber){
        if (!this.self.level().isClientSide()) {
            if (hitNumber%2==0) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_BARRAGE_MISS_EVENT, SoundSource.PLAYERS, 0.95F, (float) (0.8 + (Math.random() * 0.4)));
            }
        }
    }
    public void playBarrageNoise(int hitNumber, Entity entity){
        if (!this.self.level().isClientSide()) {
            if (hitNumber % 2 == 0) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_BARRAGE_HIT_EVENT, SoundSource.PLAYERS, 0.9F, (float) (0.9 + (Math.random() * 0.25)));
            }
        }
    }

    public void playBarrageEndNoise(float mod, Entity entity){
        if (!this.self.level().isClientSide()) {
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_BARRAGE_END_EVENT, SoundSource.PLAYERS, 0.95F+mod, 1f);
        }
    }
    public void playBarrageBlockEndNoise(float mod, Entity entity){
        if (!this.self.level().isClientSide()) {
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_BARRAGE_END_BLOCK_EVENT, SoundSource.PLAYERS, 0.88F+mod, 1.7f);
        }
    }
    public void playBarrageBlockNoise(){
        if (!this.self.level().isClientSide()) {
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_BARRAGE_BLOCK_EVENT, SoundSource.PLAYERS, 0.95F, (float) (0.8 + (Math.random() * 0.4)));
        }
    }
    public void barrageImpact2(Entity entity, boolean lastHit, float knockbackStrength){
        if (entity instanceof LivingEntity){
            if (lastHit) {
                takeDeterminedKnockbackWithY(this.self, entity, knockbackStrength);
            } else {
                takeKnockbackUp(entity,knockbackStrength);
            }
        }
    }

    public int getBarrageRecoilTime(){
        return 30;
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
                    addToCombo();
                } else {
                    if (!this.self.level().isClientSide()) {
                        this.self.level().playSound(null, this.self.blockPosition(), ModSounds.MELEE_GUARD_SOUND_EVENT, SoundSource.PLAYERS, 1F, (float) (0.95f + Math.random() * 0.1f));
                    }
                }
            }
        }
    }

    public void addToCombo(){
        int comboAmt = getComboAmt();
        setComboAmt(comboAmt+1);
        setComboExpireTicks(70);
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
        boolean powerOn = PowerTypes.hasPowerActive(playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;

        float attackTimeDuring = getAttackTimeDuring();
        if (powerOn && isBarrageAttacking() && attackTimeDuring > -1) {
            int ClashTime = 15 - Math.round((attackTimeDuring / getBarrageLength()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else if (powerOn && isBarrageCharging()) {
            int ClashTime = Math.round((attackTimeDuring / getBarrageWindup()) * 15);
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
