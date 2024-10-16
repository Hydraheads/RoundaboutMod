package net.hydra.jojomod.event.powers.stand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.event.powers.stand.presets.TWAndSPSharedPowers;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.hydra.jojomod.event.index.PacketDataIndex.FLOAT_STAR_FINGER_SIZE;

public class PowersStarPlatinum extends TWAndSPSharedPowers {
    public PowersStarPlatinum(LivingEntity self) {
        super(self);
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity){
        return new PowersStarPlatinum(entity);
    }

    @Override
    protected SoundEvent getSummonSound() {
        return ModSounds.STAR_SUMMON_SOUND_EVENT;
    }
    @Override
    public StandEntity getNewStandEntity(){
        return ModEntities.STAR_PLATINUM.create(this.getSelf().level());
    }
    @Override
    public SoundEvent getLastHitSound(){
        return ModSounds.STAR_PLATINUM_ORA_SOUND_EVENT;
    }
    @Override
    public SoundEvent getLastRejectionHitSound(){
        return ModSounds.STAR_PLATINUM_ORA_SOUND_EVENT;
    }

    @Override
    public boolean canScope(){
        return (this.isGuarding() || this.hasBlock() || this.hasEntity()
                || (this.getSelf().isUsingItem() && this.getSelf().getUseItem().is(Items.SPYGLASS)));
    }

    public int scopeTicks = -1;


    @Override
    public float getBarrageHitStrength(Entity entity){
        float str = super.getBarrageHitStrength(entity);
        if (forwardBarrage){
            str*=0.6F;
        }
        return str;
    }
    @Override
    public float getBarrageFinisherStrength(Entity entity){
        float str = super.getBarrageFinisherStrength(entity);
        if (forwardBarrage && !(entity instanceof Player)){
            str*=0.6F;
        } else if (forwardBarrage){
            str*=0.8F;
        }
        return str;
    }

    @Override
    public void playBarrageClashSound(){
        if (!this.self.level().isClientSide()) {
            playSoundsIfNearby(BARRAGE_NOISE, 32, false);
        }
    }
    @Override
    public void tickPower() {
        super.tickPower();
        if (this.self.isAlive() && !this.self.isRemoved()) {
            if (this.getActivePower() != PowerIndex.POWER_1){
                StandEntity stand = getStandEntity(this.self);
                if (Objects.nonNull(stand) && stand instanceof StarPlatinumEntity SE && SE.getFingerLength() > 1.01) {
                    if (this.getSelf() instanceof Player && isPacketPlayer()) {
                         ModPacketHandler.PACKET_ACCESS.floatToServerPacket(1F, FLOAT_STAR_FINGER_SIZE);
                    }
                        SE.setFingerLength(1F);
                }
            }

            if (scopeTicks > -1){
                scopeTicks--;
            }
            if (scopeLevel > 0){
                if (scopeTime < 10) {
                    scopeTime++;
                }
            } else {
                if (scopeTime > -1) {
                    scopeTime--;
                }
            }
        }
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot){
        return this.isDazed(this.getSelf()) || (activeP != PowerIndex.SKILL_4 && (((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()))
                || ((((this.getActivePower() == PowerIndex.POWER_2_SNEAK && this.getAttackTimeDuring() >= 0)) || hasBlock() || hasEntity()) && (slot != 1
        || (slot == 1 && this.getSelf().isShiftKeyDown()))));
    }
    public float inputSpeedModifiers(float basis){
            if (this.scopeLevel > -1){
                basis *= 0.85f;
            }
            if (this.activePower == PowerIndex.POWER_1 && this.getAttackTimeDuring() >= 0 && this.getAttackTimeDuring() <= 26){
                basis *= 0.74f;
            }
        return super.inputSpeedModifiers(basis);
    }


    public void buttonInput3(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (this.getActivePower() != PowerIndex.POWER_3_SNEAK) {
                if (this.getSelf().level().isClientSide && !this.isClashing() && this.getActivePower() != PowerIndex.POWER_2
                        && (this.getActivePower() != PowerIndex.POWER_2_EXTRA || this.getAttackTimeDuring() < 0) && !hasEntity()
                        && (this.getActivePower() != PowerIndex.POWER_2_SNEAK || this.getAttackTimeDuring() < 0) && !hasBlock()) {
                    if (this.isGuarding()){

                    } else {
                        super.buttonInput3(keyIsDown,options);
                    }
                }
            }
        } else {
            inputDash = false;
        }
    }

    /**Star Finger Ability*/
    @Override
    public void buttonInput1(boolean keyIsDown, Options options) {
        if ((!this.isBarrageAttacking() && this.getActivePower() != PowerIndex.BARRAGE_2) || this.getAttackTimeDuring() < 0) {
            if (this.getSelf().level().isClientSide && !this.isClashing() && !((TimeStop) this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                if (keyIsDown) {
                    if (this.canScope()) {
                        if (scopeTicks == -1) {
                            scopeTicks = 6;
                            int newLevel = scopeLevel + 1;
                            if (newLevel > 3) {
                                this.setScopeLevel(0);
                            } else {
                                this.getSelf().playSound(ModSounds.STAR_PLATINUM_SCOPE_EVENT, 1.0F, (float) (0.98F + (Math.random() * 0.04F)));
                                this.setScopeLevel(newLevel);
                            }
                        }
                    } else {
                        if (!this.isGuarding()) {
                            if (!hold1) {
                                if (options.keyShift.isDown()) {
                                    super.buttonInput1(keyIsDown, options);
                                } else {
                                    //Star Finger here
                                    hold1 = true;
                                    if (!this.onCooldown(PowerIndex.SKILL_1)) {
                                        if (this.activePower != PowerIndex.POWER_1) {
                                            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1, true);
                                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.POWER_1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    hold1 = false;
                }
            }
        } else {
            super.buttonInput1(keyIsDown, options);
        }
    }

    @Override
    public void updateUniqueMoves() {
        /*Tick through Time Stop Charge*/
        if (this.getActivePower() == PowerIndex.POWER_1) {
            this.updateStarFinger();
        }
        super.updateUniqueMoves();
    }

    public List<Entity> doFinger(float distance){
        float halfReach = (float) (distance*0.5);
        Vec3 pointVec = DamageHandler.getRayPoint(self, halfReach);
        return FingerGrabHitbox(DamageHandler.genHitbox(self, pointVec.x, pointVec.y,
                pointVec.z, halfReach, halfReach, halfReach), distance);
    }
    @Override
    public void handleStandAttack(Player player, Entity target){
        if (this.getActivePower() == PowerIndex.POWER_1){
            fingerDamage(target);
        } else {
            super.handleStandAttack(player,target);
        }
    }

    public void doFingerHit(List<Entity> entities){
        List<Entity> hitEntities = new ArrayList<>(entities) {
        };
        for (Entity value : hitEntities) {
            if (this.isPacketPlayer()){
                ModPacketHandler.PACKET_ACCESS.intToServerPacket(value.getId(), PacketDataIndex.INT_STAND_ATTACK);
            } else {
                fingerDamage(value);
            }
        }
    }

    @Override
    public int getFinalAttackKnockShieldTime(){
        return 80;
    }

    @Override
    public SoundEvent getFinalAttackSound(){
        return ModSounds.EXPLOSIVE_PUNCH_EVENT;
    }
    public void fingerDamage(Entity entity){
        float pow = getFingerDamage(entity);
        float knockbackStrength = 0.3F;
        if (StarFingerDamageEntityAttack(entity, pow, 0, this.self)) {
            this.takeDeterminedKnockback(this.self, entity, knockbackStrength);
            if (entity instanceof LivingEntity LE){
                MainUtil.makeBleed(LE,0,200,this.self);
            }
        } else {
            knockShield2(entity, 40);
        }
    }

    public boolean StarFingerDamageEntityAttack(Entity target, float pow, float knockbackStrength, Entity attacker){
        if (DamageHandler.StarFingerStandDamageEntity(target,pow, attacker)){
            if (attacker instanceof LivingEntity LE){
                LE.setLastHurtMob(target);
            }
            if (target instanceof LivingEntity && knockbackStrength > 0) {
                ((LivingEntity) target).knockback(knockbackStrength * 0.5f, Mth.sin(this.self.getYRot() * ((float) Math.PI / 180)), -Mth.cos(this.self.getYRot() * ((float) Math.PI / 180)));
            }
            return true;
        }
        return false;
    }
    public float getFingerDamage(Entity entity){
        if (this.getReducedDamage(entity)){
            return 2F;
        } else {
            return 7;
        }
    }
    public List<Entity> FingerGrabHitbox(List<Entity> entities, float maxDistance){
        List<Entity> hitEntities = new ArrayList<>(entities) {
        };
        for (Entity value : entities) {
            if (value.isInvulnerable() || !value.isAlive() || (this.self.isPassenger() && this.self.getVehicle().getUUID() == value.getUUID())
            || (value instanceof StandEntity SE && SE.getUser().getUUID() == this.self.getUUID())){
                hitEntities.remove(value);
            } else {
                int angle = 10;
                if (!(angleDistance(getLookAtEntityYaw(this.self, value), (this.self.getYHeadRot()%360f)) <= angle && angleDistance(getLookAtEntityPitch(this.self, value), this.self.getXRot()) <= angle)){
                    hitEntities.remove(value);
                }
            }
        }
        List<Entity> hitEntities2 = new ArrayList<>(hitEntities) {
        };
        for (Entity value : hitEntities) {
            if (value instanceof StandEntity SE && SE.getUser() != null){
                for (Entity value2 : hitEntities) {
                    if (value2.is(SE.getUser())) {
                        hitEntities2.remove(value);
                    }
                }
            }
        }
        return hitEntities2;
    }

    public boolean glowingEye = false;
    @Override
    public boolean glowingEyes(){
        return glowingEye;
    }
    public void updateStarFinger(){
        if (this.attackTimeDuring > -1) {
            StandEntity stand = getStandEntity(this.self);
            if (this.attackTimeDuring > 40) {
                if (Objects.nonNull(stand) && stand instanceof StarPlatinumEntity SE){
                    if (this.self instanceof Player) {
                        if (isPacketPlayer()) {
                            ModPacketHandler.PACKET_ACCESS.floatToServerPacket(1F, FLOAT_STAR_FINGER_SIZE);
                        }
                    }
                }
                this.setAttackTimeDuring(-10);
            } else if (this.attackTimeDuring>=24){
                float distanceOut = 0;
                if (this.attackTimeDuring <= 35){
                    distanceOut = Math.min(2.5F*(this.attackTimeDuring-23),8);
                } else {
                    distanceOut = Math.max(2.5F*(40-this.attackTimeDuring),1);
                }
                if (this.self instanceof Player){
                    if (isPacketPlayer()){
                        BlockHitResult dd = getAheadVec(distanceOut);
                        ModPacketHandler.PACKET_ACCESS.floatToServerPacket((float)
                                Math.max(Math.sqrt(dd.distanceTo(this.getSelf()))*16-32,1), FLOAT_STAR_FINGER_SIZE);
                        if (this.attackTimeDuring == 27){
                            this.setCooldown(PowerIndex.SKILL_1, 80);
                            List<Entity> fingerTargets = doFinger(8);
                            if (!fingerTargets.isEmpty()){
                                doFingerHit(fingerTargets);
                            }
                        }
                    }
                } else {
                    BlockHitResult dd = getAheadVec(distanceOut);
                    if (Objects.nonNull(stand) && stand instanceof StarPlatinumEntity SE){
                        SE.setFingerLength((float) Math.max(Math.sqrt(dd.distanceTo(this.getSelf()))*16-32,1));
                        if (this.attackTimeDuring == 27){
                            this.setCooldown(PowerIndex.SKILL_1, 80);

                            List<Entity> fingerTargets = doFinger(8);
                            if (!fingerTargets.isEmpty()){
                                doFingerHit(fingerTargets);
                            }
                        }
                    }
                }
            }
        }
    }


    public Vec3 getAheadVec2(float distOut){
        Vec3 vec3d = this.self.getEyePosition(0);
        Vec3 vec3d2 = this.self.getViewVector(0);
        return vec3d.add(vec3d2.x * distOut,
                vec3d2.y * distOut, vec3d2.z * distOut);
    }
    @Override
    public boolean cancelSprintJump(){
        if (this.getActivePower() == PowerIndex.POWER_1  && this.getAttackTimeDuring() >= 0 && this.getAttackTimeDuring() <= 26){
            return true;
        }
        return super.cancelSprintJump();
    }
    /**Makes*/
    public boolean fullTSChargeBonus(){
        return this.maxChargedTSTicks >= 100;
    }
    @Override
    public boolean canInterruptPower(){
        if (this.getActivePower() == PowerIndex.POWER_1 && this.getAttackTimeDuring() >= 0 && this.getAttackTimeDuring() <= 26){
            if (this.getSelf() instanceof Player) {
                ModPacketHandler.PACKET_ACCESS.syncSkillCooldownPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_1, 80);
            }
            this.setCooldown(PowerIndex.SKILL_1, 100);
            return true;
        } else {
            return super.canInterruptPower();
        }
    }

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        if (this.getActivePower() == PowerIndex.POWER_1){
            float distanceOut = 8;
            BlockHitResult dd = getAheadVec(distanceOut);
            List<Entity> fingerTargets = doFinger((float) Math.sqrt(dd.distanceTo(this.getSelf())));
            if (!fingerTargets.isEmpty()){
                int j = scaledHeight / 2 - 7 - 4;
                int k = scaledWidth / 2 - 8;
                context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
            }
        } else {
            super.renderAttackHud(context,playerEntity,
                    scaledWidth,scaledHeight,ticks,vehicleHeartCount, flashAlpha, otherFlashAlpha);
        }
    }


    @Override
    public boolean tryPower(int move, boolean forced) {

        if (this.getActivePower() == PowerIndex.POWER_1){
            stopSoundsIfNearby(STAR_FINGER, 32, false);
            StandEntity stand = getStandEntity(this.self);
            if (Objects.nonNull(stand) && stand instanceof StarPlatinumEntity SE && SE.getFingerLength() > 1.01) {
                if (this.getSelf() instanceof Player && isPacketPlayer()) {
                    ModPacketHandler.PACKET_ACCESS.floatToServerPacket(1F, FLOAT_STAR_FINGER_SIZE);
                }
                SE.setFingerLength(1F);
            }
        }
        return super.tryPower(move,forced);
    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y){

        boolean rendered1 = false;
        if (canScope()){
            rendered1 = true;
            if (scopeLevel == 1){
                setSkillIcon(context, x, y, 1, StandIcons.STAR_PLATINUM_SCOPE_1, PowerIndex.NO_CD);
            } else if (scopeLevel == 2) {
                setSkillIcon(context, x, y, 1, StandIcons.STAR_PLATINUM_SCOPE_2, PowerIndex.NO_CD);
            } else if (scopeLevel == 3) {
                setSkillIcon(context, x, y, 1, StandIcons.STAR_PLATINUM_SCOPE_3, PowerIndex.NO_CD);
            } else {
                setSkillIcon(context, x, y, 1, StandIcons.STAR_PLATINUM_SCOPE, PowerIndex.NO_CD);
            }
        } else {
            if (this.isBarrageAttacking() || this.getActivePower() == PowerIndex.BARRAGE_2) {
                setSkillIcon(context, x, y, 1, StandIcons.STAR_PLATINUM_TRAVEL_BARRAGE, PowerIndex.NO_CD);
            } else {
                if (this.getSelf().isShiftKeyDown()) {
                    setSkillIcon(context, x, y, 1, StandIcons.STAR_PLATINUM_IMPALE, PowerIndex.SKILL_1_SNEAK);
                } else {
                    setSkillIcon(context, x, y, 1, StandIcons.STAR_PLATINUM_FINGER, PowerIndex.SKILL_1);
                }
            }
        }

        if (this.getSelf().isShiftKeyDown()){

            setSkillIcon(context, x, y, 2, StandIcons.STAR_PLATINUM_GRAB_ITEM, PowerIndex.SKILL_2);

            if (this.isGuarding()){
                setSkillIcon(context, x, y, 3, StandIcons.STAR_PLATINUM_INHALE, PowerIndex.NONE);
            } else {
                boolean done = false;
                if (((StandUser) this.getSelf()).roundabout$getLeapTicks() > -1) {
                    if (!this.getSelf().onGround() && canStandRebound()) {
                        done = true;
                        setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_REBOUND_STAR_PLATINUM, PowerIndex.SKILL_3_SNEAK);
                    }
                } else {
                    if (!this.getSelf().onGround()) {
                        if (canVault()) {
                            done = true;
                            setSkillIcon(context, x, y, 3, StandIcons.STAR_PLATINUM_LEDGE_GRAB, PowerIndex.SKILL_3_SNEAK);
                        } else if (this.getSelf().fallDistance > 3) {
                            done = true;
                            setSkillIcon(context, x, y, 3, StandIcons.STAR_PLATINUM_FALL_CATCH, PowerIndex.SKILL_EXTRA);
                        }
                    }
                }
                if (!done) {
                    setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_STAR_PLATINUM, PowerIndex.SKILL_3_SNEAK);
                }
            }
        } else {


            //setSkillIcon(context, x, y, 1, StandIcons.THE_WORLD_ASSAULT, PowerIndex.SKILL_1);

            /*If it can find a mob to grab, it will*/
            Entity targetEntity = MainUtil.getTargetEntity(this.getSelf(),2.1F);
            if (targetEntity != null && canGrab(targetEntity)) {
                setSkillIcon(context, x, y, 2, StandIcons.STAR_PLATINUM_GRAB_MOB, PowerIndex.SKILL_2);
            } else {
                setSkillIcon(context, x, y, 2, StandIcons.STAR_PLATINUM_GRAB_BLOCK, PowerIndex.SKILL_2);
            }

            if (this.isGuarding()){
                setSkillIcon(context, x, y, 3, StandIcons.STAR_PLATINUM_INHALE, PowerIndex.NONE);
            } else {
               if (((StandUser) this.getSelf()).roundabout$getLeapTicks() > -1 && !this.getSelf().onGround() && canStandRebound()) {
                   setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_REBOUND_STAR_PLATINUM, PowerIndex.SKILL_3_SNEAK);
               } else {
                   if (!(((StandUser) this.getSelf()).roundabout$getLeapTicks() > -1) && !this.getSelf().onGround() && canVault()) {
                       setSkillIcon(context, x, y, 3, StandIcons.STAR_PLATINUM_LEDGE_GRAB, PowerIndex.SKILL_3_SNEAK);
                   } else if (!this.getSelf().onGround() && this.getSelf().fallDistance > 3) {
                       setSkillIcon(context, x, y, 3, StandIcons.STAR_PLATINUM_FALL_CATCH, PowerIndex.SKILL_EXTRA);
                   } else {
                       setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.SKILL_3_SNEAK);
                   }
               }
           }
        }

        if (((TimeStop)this.getSelf().level()).isTimeStoppingEntity(this.getSelf())) {
            setSkillIcon(context, x, y, 4, StandIcons.STAR_PLATINUM_TIME_STOP_RESUME, PowerIndex.NO_CD);
        } else if (this.getSelf().isShiftKeyDown()){
            setSkillIcon(context, x, y, 4, StandIcons.STAR_PLATINUM_TIME_STOP_IMPULSE, PowerIndex.SKILL_4);
        } else {
            setSkillIcon(context, x, y, 4, StandIcons.STAR_PLATINUM_TIME_STOP, PowerIndex.SKILL_4);
        }
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.POWER_1) {
            return this.starFinger();
        }
        return super.setPowerOther(move,lastMove);
    }
    public static final byte STAR_FINGER = 80;

    public boolean starFinger(){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)){
            this.setAttackTimeDuring(0);
            this.setActivePower(PowerIndex.POWER_1);
            playSoundsIfNearby(STAR_FINGER, 32, false);
            this.animateStand((byte)82);
            this.poseStand(OffsetIndex.GUARD_AND_TRACE);
            //stand.setYRot(this.getSelf().getYHeadRot() % 360);
            //stand.setXRot(this.getSelf().getXRot());
            return true;
        }
        return false;
    }

    @Override
    public int setCurrentMaxTSTime(int chargedTSSeconds){
        if (chargedTSSeconds >= 100){
            this.maxChargeTSTime = 100;
            this.setChargedTSTicks(100);
        } else if (chargedTSSeconds == 20) {
            this.maxChargeTSTime = 20;
        } else {
            this.maxChargeTSTime = 100;
        }
        return 0;
    }

    @Override
    public boolean canSnipe(){
        return true;
    }
    @Override
    public float getShotAccuracy(){
        return 0.0F;
    }
    @Override
    public float getBundleAccuracy(){
        return 0.3F;
    }
    @Override
    public float getThrowAngle2(){
        return 0.0F;
    }
    @Override
    public float getThrowAngle3(){
        return 0.0F;
    }
    @Override
    public float getThrowAngle(){
        return 0F;
    }

    @Override
    public byte chooseBarrageSound(){
        double rand = Math.random();
        if (rand > 0.5) {
            return BARRAGE_NOISE;
        } else {
            return BARRAGE_NOISE_2;
        }
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        if (soundChoice == BARRAGE_NOISE) {
            return ModSounds.STAR_PLATINUM_ORA_RUSH_2_SOUND_EVENT;
        } else if (soundChoice == SoundIndex.ALT_CHARGE_SOUND_1) {
            return ModSounds.STAND_BARRAGE_WINDUP_EVENT;
        } else if (soundChoice == BARRAGE_NOISE_2){
            return ModSounds.STAR_PLATINUM_ORA_RUSH_SOUND_EVENT;
        } else if (soundChoice == STAR_FINGER){
            return ModSounds.STAR_FINGER_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE) {
            return ModSounds.TIME_STOP_STAR_PLATINUM_EVENT;
        } else if (soundChoice == IMPALE_NOISE) {
            return ModSounds.IMPALE_CHARGE_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_2) {
            return ModSounds.TIME_STOP_THE_WORLD2_EVENT;
        } else if (soundChoice == TIME_STOP_NOISE_3) {
            return ModSounds.TIME_STOP_THE_WORLD3_EVENT;
        } else if (soundChoice == SoundIndex.SPECIAL_MOVE_SOUND_2) {
            return ModSounds.TIME_RESUME_EVENT;
        } else if (soundChoice == TIME_STOP_CHARGE){
            return ModSounds.TIME_STOP_CHARGE_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_VOICE){
            return ModSounds.STAR_PLATINUM_TIMESTOP_SOUND_EVENT;
        } else if (soundChoice == TIME_STOP_VOICE_2){
            return ModSounds.STAR_PLATINUM_TIMESTOP_2_SOUND_EVENT;
        } else if (soundChoice == TIME_STOP_VOICE_3){
            return ModSounds.TIME_STOP_VOICE_THE_WORLD3_EVENT;
        } else if (soundChoice == TIME_STOP_ENDING_NOISE){
            return ModSounds.TIME_STOP_RESUME_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_ENDING_NOISE_2){
            return ModSounds.TIME_STOP_RESUME_THE_WORLD2_EVENT;
        } else if (soundChoice == TIME_RESUME_NOISE){
            return ModSounds.TIME_RESUME_EVENT;
        } else if (soundChoice == TIME_STOP_TICKING){
            return ModSounds.TIME_STOP_TICKING_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }
}
