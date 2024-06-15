package net.hydra.jojomod.event.powers;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.mixin.PlayerEntity;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StandPowers {
    /**StandPowers is a class that every stand has a variation of, override it
     * to define and tick through stand abilities and cooldowns.
     * Note that most generic STAND USER code is in a mixin to the livingentity class.*/

    /**Note that self refers to the stand user, and not the stand itself.*/
    private final LivingEntity self;

    /**The time that passed since using the last attack. It counts up, so that a visual meter can display cooldowns.
    * It is also used to */
    private int attackTime = -1;

    /**The time within an attack. This matters, because if you desummon a stand the attack time doesnt reset */
    private int attackTimeDuring = -1;

    /**The time until the generic ability cooldown passes.
    This exists so you have downtime that non-stand users can get it and attack you during.*/
    private int attackTimeMax = -1;

    /**The id of the move being used. Ex: 1 = punch*/
    private byte activePower = 0;

    /**The phase of the move being used, primarily to keep track of which punch you are on in a punch string.*/
    private byte activePowerPhase = 0;

    /**This is when the punch combo goes on cooldown. Default is 3 hit combo.*/
    private final byte activePowerPhaseMax = 3;

    /**This variable exists so that a client can begin displaying your attack hud info without ticking through it.
     * Basically, stand attacks are clientside, but they need the server's confirmation to kickstart so you
     * can't hit targets in frozen tps*/
    private boolean kickStarted = true;

    public StandPowers(LivingEntity self) {
        this.self = self;
    }
    public LivingEntity getSelf(){
        return this.self;
    }
    public int getAttackTime(){
        return this.attackTime;
    }
    public int getAttackTimeDuring(){
        return this.attackTimeDuring;
    }
    public byte getActivePower(){
        return this.activePower;
    }
    public byte getActivePowerPhase(){
        return this.activePowerPhase;
    }
    public byte getActivePowerPhaseMax(){
        return this.activePowerPhaseMax;
    }

    public void setAttackTime(int attackTime){
        this.attackTime = attackTime;
    }
    public void setAttackTimeDuring(int attackTimeDuring){
        this.attackTimeDuring = attackTimeDuring;
    }
    public void setAttackTimeMax(int attackTimeMax){
        this.attackTimeMax = attackTimeMax;
    }
    public int getAttackTimeMax(){
        return this.attackTimeMax;
    }
    public float getStandReach(){
        return this.standReach;
    }
    public void setMaxAttackTime(int attackTimeMax){
        this.attackTimeMax = attackTimeMax;
    }
    public void setActivePower(byte activeMove){
        this.activePower = activeMove;
    }
    public void setActivePowerPhase(byte activePowerPhase){
        this.activePowerPhase = activePowerPhase;
    }
    public float getTimestopRange(){
        return 100;
    }

    /**The cooldown for summoning. It is mostly clientside and doesn't have to be synced*/
    private int summonCD = 0;

    /**This updates when a punch is thrown, to stop the stand from throwing the same punch twice if the game lags*/
    private byte activePowerPhaseCheck = -1;

    public boolean getSummonCD(){
        return this.summonCD <= 0;
    } public void setSummonCD(int summonCD){
        this.summonCD = summonCD;
    } public int getSummonCD2(){
        return this.summonCD;
    }

    /**This value prevents you from resummoning/blocking to cheese the 3 hit combo's last hit faster*/
    public int interruptCD = 0;
    public boolean getInterruptCD(){
        return this.interruptCD <= 0;
    }
    public void setInterruptCD(int interruptCD){
        this.interruptCD = interruptCD;
    }

    public boolean canBeTimeStopped(){
        return ((TimeStop) this.self.level()).inTimeStopRange(this.self);
    }

    /**Override this to set the special move key press conditions*/
    public void buttonInputSpecial(){

    }

    /**Barrage sound playing and canceling involve sending a byte in a packet, then reading it from here on
     * the client level. */
    public SoundEvent getSoundFromByte(byte soundChoice){
        if (soundChoice >= SoundIndex.BARRAGE_CRY_SOUND && soundChoice <= SoundIndex.BARRAGE_CRY_SOUND_7) {
            return this.getBarrageSound(SoundIndex.BARRAGE_CRY_SOUND);
        } else if (soundChoice == SoundIndex.BARRAGE_CHARGE_SOUND) {
            return this.getBarrageChargeSound();
        } else {
            return this.getOtherSounds(soundChoice);
        }
    }
    public float getSoundPitchFromByte(byte soundChoice){
        if (soundChoice >= SoundIndex.BARRAGE_CRY_SOUND && soundChoice <= SoundIndex.BARRAGE_CRY_SOUND_7) {
            return this.getBarragePitch(SoundIndex.BARRAGE_CRY_SOUND);
        } else if (soundChoice == SoundIndex.BARRAGE_CHARGE_SOUND){
            return this.getBarrageChargePitch();
        } else {
            return 1F;
        }
    }

    public float getSoundVolumeFromByte(byte soundChoice){
        return 1F;
    }
    protected SoundEvent getSummonSound() {
        return ModSounds.SUMMON_SOUND_EVENT;
    }

    public void playSummonSound() {
        if (this.self.isCrouching()){
            return;
        }
        this.self.level().playSound(null, this.self.blockPosition(), getSummonSound(), SoundSource.PLAYERS, 1F, 1F);
    } //Plays the Summon sound. Happens when stand is summoned with summon key.



    /**Override this function for alternate rush noises*/
    private byte chooseBarrageSound(){
        return SoundIndex.BARRAGE_CRY_SOUND;
    }
    private float getBarrageChargePitch(){
        return 1/((float) this.getBarrageWindup() /20);
    }
    /**Override this if you want to use the basic barrage sound and use that implementation*/
    public SoundEvent getBarrageSound(byte soundChoice){
            return null;
    }
    /**Realistically, you only need to override this if you're canceling sounds*/
    public SoundEvent getOtherSounds(byte soundChoice){
        return null;
    }
    private float getBarragePitch(byte soundChoice){
        if (soundChoice == SoundIndex.BARRAGE_CRY_SOUND) {
            return 1F;
        } else {
            return 1F;
        }
    }

    public ResourceLocation getBarrageCryID(){
        return ModSounds.STAND_THEWORLD_MUDA1_SOUND_ID;
    }
    private SoundEvent getBarrageChargeSound(){
        return ModSounds.STAND_BARRAGE_WINDUP_EVENT;
    }
    public ResourceLocation getBarrageChargeID(){
        return ModSounds.STAND_BARRAGE_WINDUP_ID;
    }
    private SoundEvent getLastHitSound(){
        return ModSounds.STAND_THEWORLD_MUDA3_SOUND_EVENT;
    }

    public ResourceLocation getLastHitID(){
        return ModSounds.STAND_THEWORLD_MUDA3_SOUND_ID;
    }

    public ResourceLocation getSoundID(byte soundNumber){
        if (soundNumber == SoundIndex.BARRAGE_CRY_SOUND) {
            return getBarrageCryID();
        } else if (soundNumber == SoundIndex.BARRAGE_CHARGE_SOUND) {
            return getBarrageChargeID();
        }
        return null;
    }

    public void tickPower(){
        if (this.self.isAlive() && !this.self.isRemoved()) {
            if (this.isClashing()) {
                if (this.attackTimeDuring != -1) {
                    this.attackTimeDuring++;
                    this.updateClashing();
                }
            } else if (!this.self.level().isClientSide || kickStarted) {
                if (this.attackTimeDuring != -1) {
                    this.attackTimeDuring++;
                    if (this.attackTimeDuring == -1) {
                        animateStand((byte) 0);
                        poseStand(OffsetIndex.FOLLOW);
                    } else {
                        if (this.hasStandActive(this.self) && !this.self.isUsingItem() && !this.isDazed(this.self)) {
                            if (this.activePower == PowerIndex.ATTACK) {
                                this.updateAttack();
                            } else if (this.isBarraging()) {

                                if (bonusBarrageConditions()) {
                                    if (this.isBarrageCharging()) {
                                        this.updateBarrageCharge();
                                    } else {
                                        this.updateBarrage();
                                    }
                                } else {
                                    ((StandUser) this.self).tryPower(PowerIndex.NONE, true);
                                }
                            } else {
                                this.updateUniqueMoves();
                            }
                        } else {
                            resetAttackState();
                        }
                    }
                }
                this.attackTime++;
                if (this.attackTime > this.attackTimeMax) {
                    this.setActivePowerPhase((byte) 0);
                }
                if (this.interruptCD > 0) {
                    this.interruptCD--;
                }
            }
            if (this.summonCD > 0) {
                this.summonCD--;
            }
        }
        if (this.self.level().isClientSide) {
            tickSounds();
        }
    }

    /**The manner in which your powers tick when you are being timestopped. Override this if the stand acts differently.
     * By technicality, you should still tick sounds.*/
    public void timeTick(){
    }

    /**Ticks through your own timestop. This value exists in the general stand powers in case you switch stands.*/
    public void timeTickStopPower(){
    }


    protected void tickSounds(){
        if (this.self.level().isClientSide) {
            if (((StandUserClient) this.self).getSoundPlay() || ((StandUserClient) this.self).getSoundCancel()) {
                this.runExtraSoundCode(((StandUserClient) this.self).getRoundaboutSoundByte());
            }
            if (((StandUserClient) this.self).getSoundPlay()) {
                ((StandUserClient) this.self).clientPlaySound();
            }
            if (((StandUserClient) this.self).getSoundCancel()) {
                ((StandUserClient) this.self).clientSoundCancel();
            }
        }
    }

    private int clashIncrement =0;
    private int clashMod =0;

    private void RoundaboutEnemyClash(){
        if (this.isClashing()) {
            if (this.clashIncrement < 0) {
                ++this.clashIncrement;
                if (this.clashIncrement == 0) {
                    this.setClashProgress(0.0f);
                }
            }
            ++this.clashIncrement;
            if (this.clashIncrement < (6 + this.clashMod)){
                this.setClashProgress(this.clashIncrement < 10 ?
                        (float) this.clashIncrement * 0.1f : 0.8f + 2.0f / (float) (this.clashIncrement - 9) * 0.1f);
            } else {
                this.setClashDone(true);
            }

        }
    }

    public void breakClash(LivingEntity winner, LivingEntity loser){
        if (StandDamageEntityAttack(loser, this.getClashBreakStrength(loser), 0.0001F, winner)) {
            ((StandUser)winner).getStandPowers().playBarrageEndNoise(0, loser);
            this.takeDeterminedKnockbackWithY(winner, loser, this.getBarrageFinisherKnockback());
            ((StandUser)winner).getStandPowers().animateStand((byte) 13);
        }
    }
    public void TieClash(LivingEntity user1, LivingEntity user2){
        ((StandUser)user1).getStandPowers().playBarrageEndNoise(0F,user2);
        ((StandUser)user2).getStandPowers().playBarrageEndNoise(-0.05F,user1);

        user1.hurtMarked = true;
        user2.hurtMarked = true;
        user1.knockback(0.55f,user2.getX()-user1.getX(), user2.getZ()-user1.getZ());
        user2.knockback(0.55f,user1.getX()-user2.getX(), user1.getZ()-user2.getZ());
    }


    public void updateClashing(){
        if (this.getStandEntity(this.self) != null) {
            //Roundabout.LOGGER.info("3 " + this.getStandEntity(this.self).getPitch() + " " + this.getStandEntity(this.self).getYaw());
        }
        if (this.getClashOp() != null) {
            if (this.attackTimeDuring <= 60) {
                LivingEntity entity = this.getClashOp();

                /*Rotation has to be set actively by both client and server,
                 * because serverPitch and serverYaw are inconsistent, client overwrites stand stuff sometimes*/
                LivingEntity standEntity = ((StandUser) entity).getStand();
                LivingEntity standSelf = ((StandUser) self).getStand();
                if (standSelf != null && standEntity != null) {
                    standSelf.setXRot(getLookAtEntityPitch(standSelf, standEntity));
                    standSelf.setYRot(getLookAtEntityYaw(standSelf, standEntity));
                    standEntity.setXRot(getLookAtEntityPitch(standEntity, standSelf));
                    standEntity.setYRot(getLookAtEntityYaw(standEntity, standSelf));
                }


                if (!(this.self instanceof Player)) {
                    this.RoundaboutEnemyClash();
                }
                if (!this.self.level().isClientSide) {

                    if ((this.getClashDone() && ((StandUser) entity).getStandPowers().getClashDone())
                    || !((StandUser) this.self).getActive() || !((StandUser) entity).getActive()) {
                        this.updateClashing2();
                    } else {
                        playBarrageNoise(this.attackTimeDuring+ clashStarter, entity);
                    }
                }
            } else {
                if (!this.self.level().isClientSide) {
                    this.updateClashing2();
                }
            }
        } else {
            if (!this.self.level().isClientSide) {
                ((StandUser) this.self).tryPower(PowerIndex.NONE, true);
            }
        }
    }
    private void updateClashing2(){
        if (this.getClashOp() != null) {
            boolean thisActive = ((StandUser) this.self).getActive();
            boolean opActive = ((StandUser) this.getClashOp()).getActive();
            if (thisActive && !opActive){
                breakClash(this.self, this.getClashOp());
            } else if (!thisActive && opActive){
                breakClash(this.getClashOp(), this.self);
            } else if (thisActive && opActive){
                if ((this.getClashProgress() == ((StandUser) this.getClashOp()).getStandPowers().getClashProgress())) {
                    TieClash(this.self, this.getClashOp());
                } else if (this.getClashProgress() > ((StandUser) this.getClashOp()).getStandPowers().getClashProgress()) {
                    breakClash(this.self, this.getClashOp());
                } else {
                    breakClash(this.getClashOp(), this.self);
                }
            }
            ((StandUser) this.self).setAttackTimeDuring(-10);
            ((StandUser) this.getClashOp()).setAttackTimeDuring(-10);
            ((StandUser) this.self).getStandPowers().syncCooldowns();
            ((StandUser) this.getClashOp()).getStandPowers().syncCooldowns();
        }
    }
    public void updateBarrageCharge(){
        if (this.attackTimeDuring >= this.getBarrageWindup()) {
            ((StandUser) this.self).tryPower(PowerIndex.BARRAGE, true);
        }
    }
    public void updateBarrage(){
        if (this.attackTimeDuring == -2) {
            ((StandUser) this.self).tryPower(PowerIndex.GUARD, true);
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
    public void updateAttack(){
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > this.attackTimeMax) {
                this.attackTimeMax = 0;
                this.setPowerNone();
            } else {
                if (this.attackTimeDuring == 5 && this.activePowerPhase == 1
                || this.attackTimeDuring == 6) {
                    this.standPunch();
                }
            }
        }
    }

    public void resetAttackState(){
        this.interruptCD = 3;
        this.setAttackTimeDuring(-1);
        poseStand(OffsetIndex.FOLLOW);
    }

    public void poseStand(byte r){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){
            stand.setOffsetType(r);
        }
    }
    public void animateStand(byte r){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){
            stand.setAnimation(r);
        }
    }

    public StandUser getUserData(LivingEntity User){
        return ((StandUser) User);
    }
    public StandEntity getStandEntity(LivingEntity User){
        return this.getUserData(User).getStand();
    } public boolean hasStandEntity(LivingEntity User){
        return this.getUserData(User).hasStandOut();
    } public boolean hasStandActive(LivingEntity User){
        return this.getUserData(User).getActive();
    }

    float standReach = 5;

    public int getTargetEntityId(){
        Entity targetEntity = getTargetEntity(this.self, -1);
        int id;
        if (targetEntity != null) {
            id = targetEntity.getId();
        } else {
            id = -1;
        }
        return id;
    }
    public void standBarrageHit(){
        if (this.self instanceof Player){
            if (isPacketPlayer()){
                ModPacketHandler.PACKET_ACCESS.StandBarrageHitPacket(getTargetEntityId(), this.attackTimeDuring);

                if (this.attackTimeDuring == this.getBarrageLength()){
                    this.attackTimeDuring = -10;
                }
            }
        } else {
            /*Caps how far out the barrage hit goes*/
            Entity targetEntity = getTargetEntity(this.self,-1);
            barrageImpact(targetEntity, this.attackTimeDuring);
        }
    }

    public void standPunch(){
        /*By setting this to -10, there is a delay between the stand retracting*/

        if (this.self instanceof Player){
            if (isPacketPlayer()){
                //Roundabout.LOGGER.info("Time: "+this.self.getWorld().getTime()+" ATD: "+this.attackTimeDuring+" APP"+this.activePowerPhase);
                this.attackTimeDuring = -10;
                ModPacketHandler.PACKET_ACCESS.StandPunchPacket(getTargetEntityId(), this.activePowerPhase);
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,-1);
            punchImpact(targetEntity);
        }

    }

    /**This function ensures the client sending attack packets is ONLY the player using the attack, prevents double attacking*/
    public boolean isPacketPlayer(){
        if (this.self.level().isClientSide) {
            Minecraft mc = Minecraft.getInstance();
            return mc.player != null && mc.player.getId() == this.self.getId();
        }
        return false;
    }
    //((ServerWorld) this.self.getWorld()).spawnParticles(ParticleTypes.EXPLOSION,pointVec.x, pointVec.y, pointVec.z,
    //        1,0.0, 0.0, 0.0,1);

    private boolean isDazed(LivingEntity entity){
        return this.getUserData(entity).isDazed();
    }
    private void setDazed(LivingEntity entity, byte dazeTime){
        if ((1.0 - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)) <= 0.0) {
            /*Warden, iron golems, and anything else knockback immmune can't be dazed**/
            return;
        } else if (entity instanceof EnderDragon || entity instanceof WitherBoss){
            /*Bosses can't be dazed**/
            return;
        }
        if (dazeTime > 0){
            ((StandUser) entity).tryPower(PowerIndex.NONE,true);
            ((StandUser) entity).getStandPowers().animateStand((byte) 14);
        } else {
            ((StandUser) entity).getStandPowers().animateStand((byte) 0);
        }
        this.getUserData(entity).setDazed(dazeTime);
    }

    public boolean knockShield(Entity entity, int duration){

        if (entity != null && entity.isAlive() && !entity.isRemoved()) {
            if (entity instanceof LivingEntity) {
                if (((LivingEntity) entity).isBlocking()) {

                    StandUser standUser= this.getUserData((LivingEntity) entity);
                    if (standUser.isGuarding()) {
                        if (!standUser.getGuardBroken()){
                            standUser.breakGuard();
                        }
                    }
                    if (entity instanceof Player){
                         ItemStack itemStack = ((LivingEntity) entity).getUseItem();
                         Item item = itemStack.getItem();
                         if (item.getUseAnimation(itemStack) == UseAnim.BLOCK) {
                             ((LivingEntity) entity).releaseUsingItem();
                             ((Player) entity).stopUsingItem();
                         }
                        ((Player) entity).getCooldowns().addCooldown(Items.SHIELD, duration);
                        entity.level().broadcastEntityEvent(entity, EntityEvent.SHIELD_DISABLED);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean knockShield2(Entity entity, int duration){

        if (entity != null && entity.isAlive() && !entity.isRemoved()) {
            if (entity instanceof LivingEntity) {
                if (((LivingEntity) entity).isBlocking()) {

                    if (entity instanceof Player){
                        ItemStack itemStack = ((LivingEntity) entity).getUseItem();
                        Item item = itemStack.getItem();
                        if (item.getUseAnimation(itemStack) == UseAnim.BLOCK) {
                            ((LivingEntity) entity).releaseUsingItem();
                            ((Player) entity).stopUsingItem();
                        }
                        ((Player) entity).getCooldowns().addCooldown(Items.SHIELD, duration);
                        entity.level().broadcastEntityEvent(entity, EntityEvent.SHIELD_DISABLED);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**Override these methods to fine tune the attack strength of the stand*/
    private float getPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 2;
        } else {
            return 5;
        }
    } private float getHeavyPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 3;
        } else {
            return 7;
        }
    } private float getBarrageFinisherStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 3;
        } else {
            return 8;
        }
    }
    private float getBarrageFinisherKnockback(){
        return 2.8F;
    }

    private float getClashBreakStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 4;
        } else {
            return 10;
        }
    }

    private float getBarrageHitStrength(Entity entity){
        float barrageLength = this.getBarrageLength();
        float power;
        if (this.getReducedDamage(entity)){
            power = 9/barrageLength;
        } else {
            power = 20/barrageLength;
        }
        /*Barrage hits are incapable of killing their target until the last hit.*/
        if (entity instanceof LivingEntity){
            if (power >= ((LivingEntity) entity).getHealth()){
                if (entity instanceof Player) {
                    power = 0.00001F;
                } else {
                    power = 0F;
                }
            }
        }
        return power;
    }
    public boolean getReducedDamage(Entity entity){
        return entity instanceof Player;
    }

    /**Initiates a stand barrage clash. This code should probably not be overridden, it is a very mutual event*/
    public void initiateClash(Entity entity){
        ((StandUser) entity).getStandPowers().setClashOp(this.self);
        ((StandUser) this.self).getStandPowers().setClashOp((LivingEntity) entity);
        this.clashStarter = 0;
        ((StandUser) entity).getStandPowers().clashStarter = 1;

        ((StandUser) entity).tryPower(PowerIndex.BARRAGE_CLASH, true);
        ((StandUser) self).tryPower(PowerIndex.BARRAGE_CLASH, true);

        LivingEntity standEntity = ((StandUser) entity).getStand();
        LivingEntity standSelf = ((StandUser) self).getStand();

        if (standEntity != null && standSelf != null){
            Vec3 CenterPoint = entity.position().add(self.position()).scale(0.5);

            Vec3 entityPoint = offsetBarrageVector(
                    CenterPoint.subtract(((CenterPoint.subtract(entity.position())).normalize()).scale(0.4)),
                    getLookAtEntityYaw(entity,self));


            Vec3 selfPoint = offsetBarrageVector(
                    CenterPoint.subtract(((CenterPoint.subtract(self.position())).normalize()).scale(0.4)),
                    getLookAtEntityYaw(self,entity));

            standEntity.setPosRaw(entityPoint.x(),entityPoint.y()+getYOffSet(standEntity),entityPoint.z());
            standEntity.setXRot(getLookAtEntityPitch(standEntity,standSelf));
            standEntity.setYRot(getLookAtEntityYaw(standEntity,standSelf));

            standSelf.setPosRaw(selfPoint.x(),selfPoint.y()+getYOffSet(standSelf),selfPoint.z());
            standSelf.setXRot(getLookAtEntityPitch(standSelf,standEntity));
            standSelf.setYRot(getLookAtEntityYaw(standSelf,standEntity));

        }
    }

    private Vec3 offsetBarrageVector(Vec3 vec3d, float yaw){
        Vec3 vec3d2 = DamageHandler.getRotationVector(0, yaw+ 90);
        return vec3d.add(vec3d2.x*0.3, 0, vec3d2.z*0.3);
    }

    private float getYOffSet(LivingEntity stand){
        float yy = 0.1F;
        if (stand.isSwimming() || stand.isVisuallyCrawling() || stand.isFallFlying()) {
            yy -= 1;
        }
        return yy;
    }

    public void barrageImpact(Entity entity, int hitNumber){
        if (this.isBarrageAttacking()) {
            if (bonusBarrageConditions()) {
                boolean lastHit = (hitNumber >= this.getBarrageLength());
                if (entity != null) {
                    if (entity instanceof LivingEntity && ((StandUser) entity).isBarraging()
                            && ((StandUser) entity).getAttackTimeDuring() > -1) {
                        initiateClash(entity);
                    } else {
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
                        if (StandDamageEntityAttack(entity, pow, 0.0001F, this.self)) {
                            if (entity instanceof LivingEntity) {
                                if (lastHit) {
                                    setDazed((LivingEntity) entity, (byte) 0);
                                    playBarrageEndNoise(0, entity);
                                } else {
                                    setDazed((LivingEntity) entity, (byte) 3);
                                        playBarrageNoise(hitNumber, entity);
                                }
                            }
                            barrageImpact2(entity, lastHit, knockbackStrength);
                        } else {
                            if (lastHit) {
                                knockShield2(entity, 200);
                                playBarrageBlockEndNoise(0, entity);
                            } else {
                                entity.setDeltaMovement(prevVelocity);
                            }
                        }
                    }
                } else {
                    playBarrageMissNoise(hitNumber);
                }

                if (lastHit) {
                    animateStand((byte) 13);
                    this.attackTimeDuring = -10;
                }
            } else {
                ((StandUser) this.self).tryPower(PowerIndex.NONE, true);
            }
        } else {
            ((StandUser) this.self).tryPower(PowerIndex.NONE, true);
        }
    }

    public void barrageImpact2(Entity entity, boolean lastHit, float knockbackStrength){
        if (entity instanceof LivingEntity){
            if (lastHit) {
                this.takeDeterminedKnockbackWithY(this.self, entity, knockbackStrength);
            } else {
                this.takeKnockbackUp(entity,knockbackStrength);
            }
        }
    }


    public boolean bonusBarrageConditions(){
        return true;
    }

    public void takeDeterminedKnockbackWithY(LivingEntity user, Entity target, float knockbackStrength){
        float xRot; if (!target.onGround()){xRot=user.getXRot();} else {xRot = -15;}
        this.takeKnockbackWithY(target, knockbackStrength,
                Mth.sin(user.getYRot() * ((float) Math.PI / 180)),
                Mth.sin(xRot * ((float) Math.PI / 180)),
                -Mth.cos(user.getYRot() * ((float) Math.PI / 180)));

    }
    public void takeDeterminedKnockback(LivingEntity user, Entity target, float knockbackStrength){

        if (target instanceof LivingEntity && (knockbackStrength *= (float) (1.0 - ((LivingEntity)target).getAttributeValue(Attributes.KNOCKBACK_RESISTANCE))) <= 0.0) {
            return;
        }
        target.hurtMarked = true;
        Vec3 vec3d2 = new Vec3(Mth.sin(
                user.getYRot() * ((float) Math.PI / 180)),
                0,
                -Mth.cos(user.getYRot() * ((float) Math.PI / 180))).normalize().scale(knockbackStrength);
        target.setDeltaMovement(- vec3d2.x,
                target.onGround() ? 0.28 : 0,
                - vec3d2.z);
        target.hasImpulse = true;
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
    } public void playBarrageNoise2(int hitNumber, Entity entity){
        if (!this.self.level().isClientSide()) {
            if (hitNumber%2==0) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_BARRAGE_HIT2_EVENT, SoundSource.PLAYERS, 0.95F, (float) (0.9 + (Math.random() * 0.25)));
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
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_BARRAGE_END_BLOCK_EVENT, SoundSource.PLAYERS, 0.97F+mod, 1f);
        }
    }
    public void playBarrageBlockNoise(){
        if (!this.self.level().isClientSide()) {
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.STAND_BARRAGE_BLOCK_EVENT, SoundSource.PLAYERS, 0.95F, (float) (0.8 + (Math.random() * 0.4)));
        }
    }

    /**ClashDone is a value that makes you lock in your barrage when you are done barraging**/
    private boolean clashDone = false;
    public boolean getClashDone(){
        return this.clashDone;
    } public void setClashDone(boolean clashDone){
        this.clashDone = clashDone;
    }
    public float clashProgress = 0.0f;
    private float clashOpProgress = 0.0f;

    /**Clash Op is the opponent you are clashing with*/
    @Nullable
    private LivingEntity clashOp;
    public @Nullable LivingEntity getClashOp() {
        return this.clashOp;
    }
    public void setClashOp(@Nullable LivingEntity clashOp) {
        this.clashOp = clashOp;
    }
    public float getClashOpProgress(){
        return this.clashOpProgress;
    }
    public void setClashOpProgress(float clashOpProgress1) {
        this.clashOpProgress = clashOpProgress1;
    }
    public float getClashProgress(){
        return this.clashProgress;
    }
    public void setClashProgress(float clashProgress1){
        this.clashProgress = clashProgress1;
        if (!this.self.level().isClientSide && this.clashOp != null && this.clashOp instanceof ServerPlayer){
            ModPacketHandler.PACKET_ACCESS.updateClashPacket((ServerPlayer) this.clashOp,
                    this.self.getId(), this.clashProgress);
        }
    }

    public void punchImpact(Entity entity){
        this.attackTimeDuring = -10;
            if (entity != null) {
                float pow;
                float knockbackStrength;
                if (this.activePowerPhase >= this.activePowerPhaseMax) {
                    /*The last hit in a string has more power and knockback if you commit to it*/
                    pow = getHeavyPunchStrength(entity);
                    knockbackStrength = 1F;
                } else {
                    pow = getPunchStrength(entity);
                    knockbackStrength = 0.2F;
                }
                if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                    this.takeDeterminedKnockback(this.self, entity, knockbackStrength);
                } else {
                    if (this.activePowerPhase >= this.activePowerPhaseMax) {
                        knockShield2(entity, 40);
                    }
                }
            } else {
                // This is less accurate raycasting as it is server sided but it is important for particle effects
                float distMax = this.getDistanceOut(this.self, this.standReach, false);
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
                    SoundEvent LastHitSound = this.getLastHitSound();
                    if (LastHitSound != null) {
                        this.self.level().playSound(null, this.self.blockPosition(), LastHitSound,
                                SoundSource.PLAYERS, 1F, 1);
                    }
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

    public void damage(Entity entity){

    }

    public Entity getTargetEntity(Entity User, float distMax){
        /*First, attempts to hit what you are looking at*/
        if (!(distMax >= 0)) {
            distMax = this.getDistanceOut(this.self, this.standReach, false);
        }
        Entity targetEntity = this.rayCastEntity(this.self,distMax);
        /*If that fails, attempts to hit the nearest entity in a spherical radius in front of you*/
        if (targetEntity == null) {
            float halfReach = (float) (distMax*0.5);
            Vec3 pointVec = DamageHandler.getRayPoint(self, halfReach);
            targetEntity = StandAttackHitboxNear(StandGrabHitbox(DamageHandler.genHitbox(self, pointVec.x, pointVec.y,
                    pointVec.z, halfReach, halfReach, halfReach), distMax));
        }
        return targetEntity;
    }


    public float getDistanceOut(Entity entity, float range, boolean offset){
        float distanceFront = this.getRayDistance(entity, range);
        if (offset) {
            Entity targetEntity = this.rayCastEntity(this.self,this.standReach);
            if (targetEntity != null && targetEntity.distanceTo(entity) < distanceFront) {
                distanceFront = targetEntity.distanceTo(entity);
            }
            distanceFront -= 1;
            distanceFront = Math.max(Math.min(distanceFront, 1.7F), 0.4F);
        }
        return distanceFront;
    }

    public float getRayDistance(Entity entity, float range){
        Vec3 vec3d = entity.getEyePosition(0);
        Vec3 vec3d2 = entity.getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        HitResult blockHit = entity.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
        if (blockHit.getType() != HitResult.Type.MISS){
            return Mth.sqrt((float) entity.distanceToSqr(blockHit.getLocation()));
        }
        return range;
    } public Vec3 getRayBlock(Entity entity, float range){
        Vec3 vec3d = entity.getEyePosition(0);
        Vec3 vec3d2 = entity.getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        HitResult blockHit = entity.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
        return blockHit.getLocation();
    }

    public float getPivotPoint(Vector3d pointToRotate, Vector3d axisStart, Vector3d axisEnd) {
        Vector3d d = new Vector3d(axisEnd.x-axisStart.x,axisEnd.y-axisStart.y,axisEnd.z-axisStart.z).normalize();
        Vector3d v = new Vector3d(pointToRotate.x-axisStart.x,pointToRotate.y-axisStart.y,pointToRotate.z-axisStart.z).normalize();
        double t = v.dot(d);
        return (float) pointToRotate.distance(axisStart.add(d.mul(t)));
    }

    public static float angleDistance(float alpha, float beta) {
        float phi = Math.abs(beta - alpha) % 360;       // This is either the distance or 360 - distance
        float distance = phi > 180 ? 360 - phi : phi;
        return distance;
    }

    /**Returns the vertical angle between two mobs*/
    public float getLookAtEntityPitch(Entity user, Entity targetEntity) {
        double f;
        double d = targetEntity.getX() - user.getX();
        double e = targetEntity.getZ() - user.getZ();
        if (targetEntity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)targetEntity;
            f = livingEntity.getEyeY() - user.getEyeY();
        } else {
            f = (targetEntity.getBoundingBox().minY + targetEntity.getBoundingBox().maxY) / 2.0 - user.getEyeY();
        }
        double g = Math.sqrt(d * d + e * e);
        return (float)(-(Mth.atan2(f, g) * 57.2957763671875));
    }
    /**Returns the horizontal angle between two mobs*/
    public float getLookAtEntityYaw(Entity user, Entity targetEntity) {
        double d = targetEntity.getX() - user.getX();
        double e = targetEntity.getZ() - user.getZ();
        return (float)(Mth.atan2(e, d) * 57.2957763671875) - 90.0f;
    }

    /** This code grabs an entity in front of you at the specified range, raycasting is used*/
    public Entity rayCastEntity(LivingEntity User, float reach){
        float tickDelta = 0;
        if (this.self.level().isClientSide()) {
            Minecraft mc = Minecraft.getInstance();
            tickDelta = mc.getDeltaFrameTime();
        }
        Vec3 vec3d = User.getEyePosition(tickDelta);

        Vec3 vec3d2 = User.getViewVector(1.0f);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * reach, vec3d2.y * reach, vec3d2.z * reach);
        float f = 1.0f;
        AABB box = new AABB(vec3d.x+reach, vec3d.y+reach, vec3d.z+reach, vec3d.x-reach, vec3d.y-reach, vec3d.z-reach);

        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(User, vec3d, vec3d3, box, entity -> !entity.isSpectator() && entity.isPickable() && !entity.isInvulnerable(), reach*reach);
        if (entityHitResult != null){
            Entity hitResult = entityHitResult.getEntity();
            if (hitResult.isAlive() && !hitResult.isRemoved()) {
                return hitResult;
            }
        }
        return null;
    }

    public List<Entity> StandGrabHitbox(List<Entity> entities, float maxDistance){
        List<Entity> hitEntities = new ArrayList<>(entities) {
        };
            for (Entity value : entities) {
                if (!value.showVehicleHealth() || value.isInvulnerable() || !value.isAlive() || (this.self.isPassenger() && this.self.getVehicle().getUUID() == value.getUUID())){
                    hitEntities.remove(value);
                } else {
                    int angle = 25;
                    if (!(angleDistance(getLookAtEntityYaw(this.self, value), (this.self.getYHeadRot()%360f)) <= angle && angleDistance(getLookAtEntityPitch(this.self, value), this.self.getXRot()) <= angle)){
                        hitEntities.remove(value);
                    }
                }
            }
        return hitEntities;
    }
    public boolean StandAttackHitbox(List<Entity> entities, float pow, float knockbackStrength){
        boolean hitSomething = false;
        float nearestDistance = -1;
        Entity nearestMob;
        if (entities != null){
            for (Entity value : entities) {
                if (this.StandDamageEntityAttack(value,pow, knockbackStrength, this.self)){
                    hitSomething = true;
                }
            }
        }
        return hitSomething;
    }

    public boolean StandDamageEntityAttack(Entity target, float pow, float knockbackStrength, Entity attacker){
        if (DamageHandler.StandDamageEntity(target,pow, attacker)){
            if (target instanceof LivingEntity && knockbackStrength > 0) {
                ((LivingEntity) target).knockback(knockbackStrength * 0.5f, Mth.sin(this.self.getYRot() * ((float) Math.PI / 180)), -Mth.cos(this.self.getYRot() * ((float) Math.PI / 180)));
            }
            return true;
        }
        return false;
    }

    public void takeKnockbackWithY(Entity entity, double strength, double x, double y, double z) {

        if (entity instanceof LivingEntity && (strength *= (float) (1.0 - ((LivingEntity)entity).getAttributeValue(Attributes.KNOCKBACK_RESISTANCE))) <= 0.0) {
            return;
        }
        entity.hurtMarked = true;
        Vec3 vec3d2 = new Vec3(x, y, z).normalize().scale(strength);
        entity.setDeltaMovement(- vec3d2.x,
                -vec3d2.y,
                - vec3d2.z);
        entity.hasImpulse = true;
    }


    public void takeKnockbackUp(Entity entity, double strength) {
        if (entity instanceof LivingEntity && (strength *= (float) (1.0 - ((LivingEntity)entity).getAttributeValue(Attributes.KNOCKBACK_RESISTANCE))) <= 0.0) {
            return;
        }
        entity.hasImpulse = true;

        Vec3 vec3d2 = new Vec3(0, strength, 0).normalize().scale(strength);
        entity.setDeltaMovement(vec3d2.x,
                vec3d2.y,
                vec3d2.z);
    }

    public Entity StandAttackHitboxNear(List<Entity> entities){
        float nearestDistance = -1;
        Entity nearestMob = null;

        if (entities != null){
            for (Entity value : entities) {
                if (!value.isInvulnerable() && value.isAlive() && value.getUUID() != this.self.getUUID()){
                    float distanceTo = value.distanceTo(this.self);
                    if ((nearestDistance < 0 || distanceTo < nearestDistance) && distanceTo <= this.standReach){
                        nearestDistance = distanceTo;
                        nearestMob = value;
                    }
                }
            }
        }

        return nearestMob;
    }

    public void updateUniqueMoves(){
    }

    public void kickStartClient(){
        this.kickStarted = true;
    }

    /** Tries to use an ability of your stand. If forced is true, the ability comes out no matter what.**/
    public void tryPower(int move, boolean forced){
        if (!this.self.level().isClientSide && this.isBarraging() && move != PowerIndex.BARRAGE && this.attackTimeDuring  > -1){
            this.stopSoundsIfNearby();
        }

        if (!this.isClashing() || move == PowerIndex.NONE) {
            if ((this.activePower == PowerIndex.NONE || forced) &&
                    (!this.isDazed(this.self) || move == PowerIndex.BARRAGE_CLASH)) {

                if (move == PowerIndex.NONE) {
                    this.setPowerNone();
                } else if (move == PowerIndex.ATTACK) {
                    this.setPowerAttack();
                } else if (move == PowerIndex.GUARD) {
                    this.setPowerGuard();
                } else if (move == PowerIndex.BARRAGE_CHARGE) {
                    this.setPowerBarrageCharge();
                } else if (move == PowerIndex.BARRAGE) {
                    this.setPowerBarrage();
                } else if (move == PowerIndex.BARRAGE_CLASH) {
                    this.setPowerClash();
                } else if (move == PowerIndex.SPECIAL) {
                    this.setPowerSpecial(move);
                }
            }
            if (this.self.level().isClientSide) {
                kickStarted = false;
            }
        }
    }

    /**The Sound Event to cancel when your barrage is canceled*/



    public final void playSoundsIfNearby(byte soundNo) {
        if (!this.self.level().isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) this.self.level());
            Vec3 userLocation = new Vec3(this.self.getX(),  this.self.getY(), this.self.getZ());
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) this.self.level()).players().get(j);

                if (((ServerLevel) serverPlayerEntity.level()) != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(userLocation, 32.0)) {
                    ModPacketHandler.PACKET_ACCESS.startSoundPacket(serverPlayerEntity,this.self.getId(),soundNo);
                }
            }
        }
    }
    /**This is called first by the server, it chooses the sfx and sends packets to nearby players*/
    public void playBarrageCrySound(){
        if (!this.self.level().isClientSide()) {
            byte barrageCrySound = this.chooseBarrageSound();
            if (barrageCrySound != SoundIndex.NO_SOUND) {
                playSoundsIfNearby(barrageCrySound);
            }
        }
    }
    public void playBarrageChargeSound(){
        if (!this.self.level().isClientSide()) {
            SoundEvent barrageChargeSound = this.getBarrageChargeSound();
            if (barrageChargeSound != null) {
                playSoundsIfNearby(SoundIndex.BARRAGE_CHARGE_SOUND);
            }
        }
    }



    /**This is called fourth by the server, it sends a packet to cancel the sound.*/
    public final void stopSoundsIfNearby() {
        if (!this.self.level().isClientSide) {
            ServerLevel serverWorld = ((ServerLevel) this.self.level());
            Vec3 userLocation = new Vec3(this.self.getX(),  this.self.getY(), this.self.getZ());
            for (int j = 0; j < serverWorld.players().size(); ++j) {
                ServerPlayer serverPlayerEntity = ((ServerLevel) this.self.level()).players().get(j);

                if (((ServerLevel) serverPlayerEntity.level()) != serverWorld) {
                    continue;
                }

                BlockPos blockPos = serverPlayerEntity.blockPosition();
                if (blockPos.closerToCenterThan(userLocation, 32.0)) {
                    ModPacketHandler.PACKET_ACCESS.stopSoundPacket(serverPlayerEntity,this.self.getId());
                }
            }
        }
    }


    public void setPowerNone(){
        this.attackTimeDuring = -1;
        this.setActivePower(PowerIndex.NONE);
        poseStand(OffsetIndex.FOLLOW);
        animateStand((byte) 0);
    }

    public boolean canAttack(){
        if (this.attackTimeDuring <= -1) {
            return this.activePowerPhase < this.activePowerPhaseMax || this.attackTime >= this.attackTimeMax;
        }
        return false;
    }
    public void setPowerGuard() {
        animateStand((byte) 10);
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.GUARD);
        this.poseStand(OffsetIndex.GUARD);
    }


    public void setPowerBarrageCharge() {
        animateStand((byte) 11);
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.BARRAGE_CHARGE);
        this.poseStand(OffsetIndex.ATTACK);
        this.clashDone = false;
        playBarrageChargeSound();
    }

    public void setPowerBarrage() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.BARRAGE);
        this.poseStand(OffsetIndex.ATTACK);
        this.setAttackTimeMax(this.getBarrageRecoilTime());
        this.setActivePowerPhase(this.getActivePowerPhaseMax());
        this.setAttackTime(19);
        animateStand((byte) 12);
        playBarrageCrySound();
    }

    public int clashStarter = 0;

    /**Override this to set the special move*/
    public void setPowerSpecial(int lastMove) {
    }

    public void setPowerClash() {
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.BARRAGE_CLASH);
        this.poseStand(OffsetIndex.LOOSE);
        this.setClashProgress(0f);
        this.clashIncrement = 0;
        this.clashMod = (int) (Math.round(Math.random()*8));
        animateStand((byte) 12);

        if (this.self instanceof Player && !this.self.level().isClientSide) {
            ((ServerPlayer) this.self).displayClientMessage(Component.translatable("text.roundabout.barrage_clash"), true);
        }
        //playBarrageGuardSound();
    }

    public int getBarrageRecoilTime(){
        return 35;
    }

    public boolean isGuarding(){
        return this.activePower == PowerIndex.GUARD;
    }

    public boolean isBarrageCharging(){
        return (this.activePower == PowerIndex.BARRAGE_CHARGE);
    }
    public boolean isBarraging(){
        return (this.activePower == PowerIndex.BARRAGE || this.activePower == PowerIndex.BARRAGE_CHARGE);
    }
    public boolean isBarrageAttacking(){
        return this.activePower == PowerIndex.BARRAGE;
    }
    public boolean isClashing(){
        return this.activePower == PowerIndex.BARRAGE_CLASH && this.attackTimeDuring > -1;
    }

    public int getBarrageWindup(){
        return 29;
    }
    public int getBarrageLength(){
        return 60;
    }

    public void setPowerAttack(){
        if (this.attackTimeDuring <= -1) {
            if (this.activePowerPhase < this.activePowerPhaseMax || this.attackTime >= this.attackTimeMax) {
                if (this.activePowerPhase >= this.activePowerPhaseMax){
                    this.activePowerPhase = 1;
                } else {
                    this.activePowerPhase++;
                    if (this.activePowerPhase == this.activePowerPhaseMax) {
                        this.attackTimeMax= 40;
                    } else {
                        this.attackTimeMax= 30;
                    }

                }

                this.attackTimeDuring = 0;
                this.setActivePower(PowerIndex.ATTACK);
                this.setAttackTime(0);

                animateStand(this.activePowerPhase);
                poseStand(OffsetIndex.ATTACK);
            }
        }
    }

    public void syncCooldowns(){
        if (!this.self.level().isClientSide && this.self instanceof ServerPlayer){
            ModPacketHandler.PACKET_ACCESS.syncCooldownPacket((ServerPlayer) this.self,
                    attackTime,attackTimeMax,attackTimeDuring,
                    activePower,activePowerPhase);
        }
    }

    public boolean isStoppingTime(){
        return false;
    }

    public void runExtraSoundCode(byte soundChoice) {
    }
}
