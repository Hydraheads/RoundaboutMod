package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.*;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.RattDartEntity;
import net.hydra.jojomod.entity.projectile.ThrownObjectEntity;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.StarPlatinumBaseballEntity;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.hydra.jojomod.entity.visages.mobs.JotaroNPC;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.TWAndSPSharedPowers;
import net.hydra.jojomod.event.powers.visagedata.voicedata.JotaroVoice;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
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
    /**Override to add disable config*/
    public boolean isStandEnabled(){
        return ClientNetworking.getAppropriateConfig().starPlatinumSettings.enableStarPlatinum;
    }

    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    @Override
    public StandEntity getNewStandEntity(){
            if (((StandUser)this.getSelf()).roundabout$getStandSkin() == StarPlatinumEntity.BASEBALL_SKIN){
                return ModEntities.STAR_PLATINUM_BASEBALL.create(this.getSelf().level());
            }
        return ModEntities.STAR_PLATINUM.create(this.getSelf().level());
    }

    @Override
    public int getMaxGuardPoints(){
        return ClientNetworking.getAppropriateConfig().starPlatinumSettings.starPlatinumGuardPoints;
    }
    @Override
    public void playSummonEffects(boolean forced){
        if (!forced) {
            if (((StandUser) this.getSelf()).roundabout$getStandSkin() == StarPlatinumEntity.ATOMIC_SKIN) {
                if (!this.getSelf().level().isClientSide()) {
                    ((ServerLevel) this.self.level()).sendParticles(ParticleTypes.EXPLOSION,
                            this.getSelf().getX(), this.getSelf().getY() + this.getSelf().getEyeHeight(), this.getSelf().getZ(),
                            5, 0.4, 0.4, 0.4, 0.4);
                }
            }
        }
    }

    @Override
    public float getMiningMultiplier() {
        return (float) (1F*(ClientNetworking.getAppropriateConfig().
                starPlatinumSettings.miningSpeedMultiplierStarPlatinum *0.01));
    }

    @Override
    public int getMiningLevel() {
        return ClientNetworking.getAppropriateConfig().starPlatinumSettings.getMiningTierStarPlatinum;
    }
    @Override
    public void levelUp(){
        if (!this.getSelf().level().isClientSide() && this.getSelf() instanceof Player PE){
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            byte level = ipe.roundabout$getStandLevel();
            if (level == 7){
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.max.skins").
                        withStyle(ChatFormatting.AQUA), true);
            } else if (level == 2 || level == 3 || level == 4 || level == 6 || level == 5){
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.both").
                        withStyle(ChatFormatting.AQUA), true);
            }
        }
        super.levelUp();
    }


    @Override
    public Byte getLastHitSound(){

        double rand = Math.random();
        byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (skn == StarPlatinumEntity.OVA_SKIN) {
            if (rand > 0.66) {
                return LAST_HIT_4_NOISE;
            } else if (rand > 0.33) {
                return LAST_HIT_5_NOISE;
            } else {
                return LAST_HIT_6_NOISE;
            }

        } else if (isArcade(skn)){
            if (rand > 0.66) {
                return LAST_HIT_10_NOISE;
            } else if (rand > 0.33) {
                return LAST_HIT_11_NOISE;
            } else {
                return LAST_HIT_12_NOISE;
            }
        } else {
            if (rand > 0.66) {
                return LAST_HIT_1_NOISE;
            } else if (rand > 0.33) {
                return LAST_HIT_2_NOISE;
            } else {
                return LAST_HIT_3_NOISE;
            }
        }
    }

    @Override
    public SoundEvent getLastRejectionHitSound(){
        return ModSounds.STAR_PLATINUM_ORA_SOUND_EVENT;
    }
    public void rollSkin(){
        StandUser user = getUserData(this.self);
        if (this.self instanceof Zombie){
            user.roundabout$setStandSkin(StarPlatinumEntity.MANGA_SKIN);
        } else if (this.self instanceof Creeper){
            user.roundabout$setStandSkin(StarPlatinumEntity.GREEN_SKIN);
        } else if (this.self instanceof WanderingTrader){
            user.roundabout$setStandSkin(StarPlatinumEntity.BASEBALL_SKIN);
        } else if (this.self instanceof Warden){
            user.roundabout$setStandSkin(StarPlatinumEntity.ATOMIC_SKIN);
        } else if (this.self instanceof Raider){
            user.roundabout$setStandSkin(StarPlatinumEntity.OVA_SKIN);
        } else if (this.self instanceof Piglin ||
                this.self instanceof Rabbit){
            user.roundabout$setStandSkin(StarPlatinumEntity.PART_4_SKIN);
        } else if (this.self instanceof IronGolem){
            user.roundabout$setStandSkin(StarPlatinumEntity.PART_6_SKIN);
        } else if (this.self instanceof EnderMan || this.self instanceof EnderDragon){
            user.roundabout$setStandSkin(StarPlatinumEntity.MANGA_PURPLE_SKIN);
        }
    }
    @Override
    public boolean canScope(){
        return (this.isGuarding() || this.hasBlock() || this.hasEntity()
                || (this.getSelf().isUsingItem() && this.getSelf().getUseItem().is(Items.SPYGLASS)))
                || (this.getSelf().isUsingItem() && this.getSelf().getUseItem().getItem() instanceof ProjectileWeaponItem);
    }

    public float getGrabThrowStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod((float) ((float) 1.1F* (ClientNetworking.getAppropriateConfig().
                    starPlatinumSettings.starPlatinumAttackMultOnPlayers*0.01)));
        } else {
            return levelupDamageMod((float) ((float) 6F* (ClientNetworking.getAppropriateConfig().
                    starPlatinumSettings.starPlatinumAttackMultOnMobs*0.01)));
        }
    }
    public int scopeTicks = -1;

    @Override
    public float getFinalPunchStrength(Entity entity){
        float punchD = this.getPunchStrength(entity)*2+this.getHeavyPunchStrength(entity);
        if (this.getReducedDamage(entity)){
            return (((float)this.chargedFinal/(float)maxSuperHitTime)*punchD);
        } else {
            return ((((float)this.chargedFinal/(float)maxSuperHitTime)*punchD)+3);
        }
    }

    @Override
    public byte getThrowStyleType(){
        return ThrownObjectEntity.SPTHROW;
    }

    @Override
    public float getBarrageHitStrength(Entity entity){
        float str = super.getBarrageHitStrength(entity);
        if (str > 0.005F) {
            if (getReducedDamage(entity)) {
                str *= levelupDamageMod((float) ((ClientNetworking.getAppropriateConfig().
                        starPlatinumSettings.starPlatinumAttackMultOnPlayers * 0.01)));
            } else {
                str *= levelupDamageMod((float) ((ClientNetworking.getAppropriateConfig().
                        starPlatinumSettings.starPlatinumAttackMultOnMobs * 0.01)));
            }
            if (forwardBarrage) {
                str *= 0.6F;
            }
        }
        if (entity instanceof LivingEntity){
            if (str >= ((LivingEntity) entity).getHealth() && ClientNetworking.getAppropriateConfig().generalStandSettings.barragesOnlyKillOnLastHit){
                if (entity instanceof Player) {
                    str = 0.00001F;
                } else {
                    str = 0F;
                }
            }
        }
        return str;
    }
    @Override
    public float getBarrageFinisherStrength(Entity entity){
        float str = super.getBarrageFinisherStrength(entity);
        if (this.getReducedDamage(entity)){
            str *=levelupDamageMod((float) ((ClientNetworking.getAppropriateConfig().
                    starPlatinumSettings.starPlatinumAttackMultOnPlayers*0.01)));
        } else {
            str *=levelupDamageMod((float) ((ClientNetworking.getAppropriateConfig().
                    starPlatinumSettings.starPlatinumAttackMultOnMobs*0.01)));
        }
        if (forwardBarrage && !(entity instanceof Player)){
            str*=0.6F;
        } else if (forwardBarrage){
            str*=0.8F;
        }
        return str;
    }
    @Override
    public float getKickBarrageHitStrength(Entity entity){
        float str = super.getKickBarrageHitStrength(entity);
        if (str > 0.005F) {
            if (getReducedDamage(entity)) {
                str *= levelupDamageMod((float) ((ClientNetworking.getAppropriateConfig().
                        starPlatinumSettings.starPlatinumAttackMultOnPlayers * 0.01)));
            } else {
                str *= levelupDamageMod((float) ((ClientNetworking.getAppropriateConfig().
                        starPlatinumSettings.starPlatinumAttackMultOnMobs * 0.01)));
            }
            if (forwardBarrage) {
                str *= 0.6F;
            }
        }
        return str;
    }
    @Override
    public float getKickBarrageFinisherStrength(Entity entity){
        float str = super.getKickBarrageFinisherStrength(entity);
        if (this.getReducedDamage(entity)){
            str *=levelupDamageMod((float) ((ClientNetworking.getAppropriateConfig().
                    starPlatinumSettings.starPlatinumAttackMultOnPlayers*0.01)));
        } else {
            str *=levelupDamageMod((float) ((ClientNetworking.getAppropriateConfig().
                    starPlatinumSettings.starPlatinumAttackMultOnMobs*0.01)));
        }
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
            byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();
            if (skn == StarPlatinumEntity.OVA_SKIN) {
                playStandUserOnlySoundsIfNearby(BARRAGE_NOISE_3, 27, false,true);
                return;
            } if (isArcade(skn)) {
                playStandUserOnlySoundsIfNearby(BARRAGE_NOISE_8, 27, false,true);
                return;
            }
            playStandUserOnlySoundsIfNearby(BARRAGE_NOISE, 27, false,true);
        }
    }


    public boolean isArcade(byte bt){
        return (bt == StarPlatinumEntity.ARCADE || bt == StarPlatinumEntity.ARCADE_2);
    }
    @Override
    public void playSPandTWTSSounds(){

        byte bt = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (bt == StarPlatinumEntity.OVA_SKIN) {
            playSoundsIfNearby(TIME_STOP_NOISE_8, 100, true);
        } else if (isArcade(bt)) {
            playSoundsIfNearby(TIME_STOP_NOISE_11, 100, true);
        } else {
            playSoundsIfNearby(TIME_STOP_NOISE, 100, true);
        }
    }

    public static final byte POWER_STAR_FINGER = PowerIndex.POWER_1;
    public static final byte POWER_EARLY_STAR_FINGER = PowerIndex.POWER_1_SNEAK;
    public static final byte POWER_INHALE = PowerIndex.POWER_3;

    boolean letServerKnowScopeCatchIsReady = false;
    @Override
    public void tickPower() {
        /**This little excerpt lets the server know you're ready to catch another projectile*/
        if (this.getSelf().level().isClientSide()) {
            if (this.onCooldown(PowerIndex.SKILL_EXTRA_2)) {
                if (!letServerKnowScopeCatchIsReady) {
                    letServerKnowScopeCatchIsReady = true;
                }
            } else {
                if (letServerKnowScopeCatchIsReady) {
                    C2SPacketUtil.byteToServerPacket(PacketDataIndex.BYTE_UPDATE_COOLDOWN,PowerIndex.SKILL_EXTRA_2);
                    letServerKnowScopeCatchIsReady = false;
                }
            }
        } else {
            StandEntity stand = getStandEntity(this.self);
            if (!(Objects.nonNull(stand) && stand instanceof StarPlatinumEntity SE && this.self instanceof ServerPlayer PE && SE.getScoping() &&
                    SE.level().dimensionTypeId() == this.self.level().dimensionTypeId())) {
                if (ClientNetworking.getAppropriateConfig().starPlatinumSettings.starPlatinumScopeUsesPotionEffectForNightVision) {
                    MobEffectInstance ME = this.getSelf().getEffect(MobEffects.NIGHT_VISION);
                    if (ME != null && ME.getDuration() >= 100000 && ME.getAmplifier() > 20) {
                        this.getSelf().removeEffect(MobEffects.NIGHT_VISION);
                    }
                }
            }
        }
        super.tickPower();
        if (this.self.isAlive() && !this.self.isRemoved()) {
            if (this.getActivePower() != POWER_STAR_FINGER){
                StandEntity stand = getStandEntity(this.self);
                if (Objects.nonNull(stand) && stand instanceof StarPlatinumEntity SE && SE.getFingerLength() > 1.01) {
                    if (this.getSelf() instanceof Player && isPacketPlayer()) {
                        C2SPacketUtil.floatToServerPacket(PacketDataIndex.FLOAT_STAR_FINGER_SIZE,1F);
                    }
                    if (!this.self.level().isClientSide()){
                        SE.setFingerLength(1F);
                    }
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
        return this.isDazed(this.getSelf()) || (slot == 3 && this.isGuarding() && ((TimeStop)this.getSelf().level()).isTimeStoppingEntity(this.getSelf())) || (activeP != PowerIndex.SKILL_4 && (((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()))
                || ((((this.getActivePower() == PowerIndex.POWER_2_SNEAK && this.getAttackTimeDuring() >= 0) && slot != 1) || ((hasBlock() || hasEntity()) && slot != 1
       ))));
    }
    public float inputSpeedModifiers(float basis){
        //Scope Slows You down
        if (this.scopeLevel > -1){
            basis *= 0.85f;
        }
        //Star Finger Slows You down
        if (this.activePower == POWER_STAR_FINGER && this.getAttackTimeDuring() >= 0 && this.getAttackTimeDuring() <= 26){
            basis *= 0.54f;
        }
        //Inhale Slows You down
        if (this.activePower == POWER_INHALE && !(this.getSelf() instanceof Creeper)){
            basis *= 0.5f;
        }
        return super.inputSpeedModifiers(basis);
    }

    @Override
    public boolean cancelJump(){
        if (this.activePower == POWER_STAR_FINGER && this.getAttackTimeDuring() >= 0 && this.getAttackTimeDuring() <= 26)
            return true;
        return false;
    }

    @Override
    public void updateIntMove(int in){
        if (activePower == POWER_STAR_FINGER) {
            ticksForFinger = in;
            stopSoundsIfNearby(STAR_FINGER, 100, false);
            stopSoundsIfNearby(STAR_FINGER_2, 100, false);
            stopSoundsIfNearby(STAR_FINGER_3, 100, false);
            stopSoundsIfNearby(STAR_FINGER_SILENT, 100, false);
            if (this.getActivePower() == POWER_STAR_FINGER) {
                this.animateStand(StarPlatinumEntity.STAR_FINGER_2);
            }
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.DSP_SUMMON_EVENT, SoundSource.PLAYERS,
                    0.5F, (float) (1.5 + (Math.random() * 0.04)));
        }
    }

    public int ticksForFinger = 0;
    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (keyIsDown && this.getActivePower() == POWER_EARLY_STAR_FINGER && this.attackTimeDuring < 24 && attackTimeDuring > 2) {
            holdDownClick = true;
            animateStand(StarPlatinumEntity.IMPALE_2);
            this.attackTimeDuring = -8;
            tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK_2,getTargetEntityId2(impaleRange));
        } else if ((this.getActivePower() != POWER_STAR_FINGER || this.attackTimeDuring >= 26)) {
            super.buttonInputAttack(keyIsDown,options);
        } else {
            if (keyIsDown && ticksForFinger == 100) {
                holdDownClick = true;
                ticksForFinger = 101;
                animateStand(StarPlatinumEntity.STAR_FINGER_2);
                tryIntToServerPacket(PacketDataIndex.INT_UPDATE_MOVE,attackTimeDuring);
                this.attackTimeDuring = 26;
            }
        }
    }


    @Override
    public void powerActivate(PowerContext context) {
        switch (context) {

            case SKILL_1_NORMAL -> {
                starFingerOrFBarrageClient();
            }
            case SKILL_1_GUARD, SKILL_1_CROUCH_GUARD -> {
                tryScopeOrFBarrageClient();
            }
            case SKILL_1_CROUCH -> {
                impaleOrFBarrageClient();
            }

            case SKILL_2_NORMAL -> {
                blockAndEntityGrabClient();
            }
            case SKILL_2_GUARD, SKILL_2_CROUCH_GUARD -> {
                phaseGrabClient();
            }
            case SKILL_2_CROUCH -> {
                itemGrabClient();
            }

            case SKILL_3_NORMAL -> {
                tryToDashClient();
            }
            case SKILL_3_CROUCH -> {
                tryToStandLeapClient();
            }
            case SKILL_3_GUARD, SKILL_3_CROUCH_GUARD -> {
                tryInhaleClient();
            }

            case SKILL_4_NORMAL, SKILL_4_CROUCH, SKILL_4_GUARD, SKILL_4_CROUCH_GUARD -> {
                doTSClient();
            }
        }
    }

    public void tryInhaleClient(){
        if (this.activePower != POWER_INHALE && !this.getSelf().isUnderWater()) {
            if (canExecuteMoveWithLevel(getInhaleLevel())) {
                ((StandUser) this.getSelf()).roundabout$tryPower(POWER_INHALE, true);
                tryPowerPacket(POWER_INHALE);
            }
        }
    }


    @Override

    public void impaleOrFBarrageClient() {
        if (clientForwardBarrage())
            return;
        if (this.canScope()){
            doScope();
            return;
        }
        super.impaleOrFBarrageClient();
    }
    public void starFingerOrFBarrageClient(){
        if (clientForwardBarrage())
            return;
        if (this.canScope()){
            doScope();
            return;
        }
        if (hasBlock() || hasEntity())
            return;
        if (!this.onCooldown(PowerIndex.SKILL_1)) {
            if (canExecuteMoveWithLevel(getFingerLevel())) {
                if (this.activePower != POWER_STAR_FINGER) {
                    ticksForFinger = 0;
                    ((StandUser) this.getSelf()).roundabout$tryPower(POWER_STAR_FINGER, true);
                    tryPowerPacket(POWER_STAR_FINGER);
                }
            }
        }
    }

    public void tryScopeOrFBarrageClient(){
        if (clientForwardBarrage()) {
            return;
        }
        if (this.canScope()){
            doScope();
        }
    }

    public void doScope(){
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
    }

    @Override
    public int getExpForLevelUp(int currentLevel){
        int amt;
        if (currentLevel == 1){
            amt = 25;
        } else {
            amt = (100+((currentLevel-1)*50));
        }
        amt= (int) (amt*(getLevelMultiplier()));
        return amt;
    }
    @Override
    public void updateUniqueMoves() {
        /*Tick through Time Stop Charge*/
        if (this.getActivePower() == POWER_STAR_FINGER) {
            this.updateStarFinger();
        } else if (this.getActivePower() == POWER_INHALE) {
            this.updateInhale();
        }
        super.updateUniqueMoves();
    }

    public void updateInhale(){
        float dist = 8F;
        Vec3 pointVec = DamageHandler.getRayPoint(self, dist);
        Vec3 pointVec2 = DamageHandler.getRayPoint(self, Math.max(0.6,dist-1.5));
        if (!this.self.level().isClientSide) {
            if (this.getSelf().isUnderWater()){
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                return;
            }

            if (this.attackTimeDuring % 7 == 0){
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.INHALE_EVENT, SoundSource.PLAYERS, 0.5F, (float) (0.98 + (Math.random() * 0.04)));
            }

            double random = (Math.random() * 1.8) - 0.9;
            double random2 = (Math.random() * 1.8) - 0.9;
            double random3 = (Math.random() * 1.8) - 0.9;
            ((ServerLevel) this.self.level()).sendParticles(ModParticles.VACUUM, pointVec2.x + random,
                    pointVec2.y+ random2, pointVec2.z + random3,
                    0,
                    (this.getSelf().getEyePosition().x - pointVec2.x)*1.4F, (this.getSelf().getEyePosition().y - pointVec2.y)*1.4F, (this.getSelf().getEyePosition().z - pointVec2.z)*1.4F,
                    0.08);
            float dst = (float) pointVec.distanceTo(this.getSelf().position());
            float halfReach = (float) (dst*0.5);
            Vec3 pointVec3 = DamageHandler.getRayPoint(self, halfReach);
            List<Entity> listEnt = DamageHandler.genHitbox(self, pointVec3.x, pointVec3.y,
                    pointVec3.z, halfReach, halfReach, halfReach);

            Direction gravD = ((IGravityEntity)self).roundabout$getGravityDirection();

            for (Entity value : listEnt) {
                Vec2 lookVec = new Vec2(getLookAtEntityYaw(self, value), getLookAtEntityPitch(self, value));
                if (gravD != Direction.DOWN) {
                    lookVec = RotationUtil.rotPlayerToWorld(lookVec.x, lookVec.y, gravD);
                }
                if (value instanceof JusticeEntity JE) {
                    if ((angleDistance(lookVec.x, (self.getYHeadRot()% 360f)) <= 60 && angleDistance(lookVec.y, self.getXRot()) <= 60)) {
                        JE.inhaleTick();
                    }
                } else if (!(value instanceof StarPlatinumEntity) && !value.isInvulnerable()) {
                    if ((angleDistance(lookVec.x, (self.getYHeadRot()% 360f)) <= 60 && angleDistance(lookVec.y, self.getXRot()) <= 60)) {
                        double strength = 0.05;
                        if (value instanceof ItemEntity || value instanceof ExperienceOrb) {
                            ((IEntityAndData) value).roundabout$setNoGravTicks(2);
                            strength = 0.7;

                            float degrees = Mth.wrapDegrees(getLookAtEntityYaw(this.getSelf(), value) - 180);
                            float degreesY = -1 * getLookAtEntityPitch(this.getSelf(), value);
                            float ybias = (90F - Math.abs(degreesY)) / 90F;

                            Vec3 vec = new Vec3(Mth.sin(((degrees * ((float) Math.PI / 180)))),
                                    Mth.sin(degreesY * ((float) Math.PI / 180)),
                                    -Mth.cos((degrees * ((float) Math.PI / 180))));



                            MainUtil.takeUnresistableKnockbackWithYBias(value, strength * (0.5 + (ybias / 2)),
                                    vec.x,vec.y,vec.z,
                                    ybias);
                        } else if (!(value instanceof Projectile)) {
                            if (value instanceof LivingEntity LE && (MainUtil.isKnockbackImmune(LE) || (strength *= (float) (1.0 - LE.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE))) <= 0.0)) {
                                strength = 0;
                            } else {
                                if (value instanceof PrimedTnt && this.getSelf() instanceof Player PE){
                                    StandUser user = ((StandUser)PE);
                                    ItemStack stack = user.roundabout$getStandDisc();
                                    if (!stack.isEmpty() && stack.is(ModItems.STAND_DISC_STAR_PLATINUM)) {
                                        IPlayerEntity ipe = ((IPlayerEntity) PE);
                                        if (!ipe.roundabout$getUnlockedBonusSkin()) {
                                            if (value.distanceTo(this.getSelf()) < 1){
                                                if (!this.getSelf().level().isClientSide()) {
                                                    ipe.roundabout$setUnlockedBonusSkin(true);
                                                    this.getSelf().level().playSound(null, this.getSelf().getX(), this.getSelf().getY(),
                                                            this.getSelf().getZ(), ModSounds.UNLOCK_SKIN_EVENT, this.getSelf().getSoundSource(), 2.0F, 1.0F);
                                                    this.getSelf().level().playSound(null, this.getSelf().getX(), this.getSelf().getY(),
                                                            this.getSelf().getZ(), SoundEvents.GENERIC_EXPLODE, this.getSelf().getSoundSource(), 2.0F, 1.0F);
                                                    ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.EXPLOSION, this.getSelf().getX(),
                                                            this.getSelf().getY()+this.getSelf().getEyeHeight(), this.getSelf().getZ(),
                                                            20, 0.7, 0.7, 0.7, 0.2);
                                                    user.roundabout$setStandSkin(StarPlatinumEntity.ATOMIC_SKIN);
                                                    ((ServerPlayer) ipe).displayClientMessage(
                                                            Component.translatable("unlock_skin.roundabout.star_platinum.atomic"), true);
                                                    user.roundabout$summonStand(this.getSelf().level(), true, false);
                                                    value.discard();
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                }
                                float degrees = Mth.wrapDegrees(getLookAtEntityYaw(this.getSelf(), value) - 180);

                                MainUtil.knockbackWithoutBumpUp(value, strength,
                                        Mth.sin(((degrees * ((float) Math.PI / 180)))),
                                        -Mth.cos((degrees * ((float) Math.PI / 180))));
                            }
                        }
                    }
                }
            }
        }

    }

    public List<Entity> doFinger(float distance){
        float halfReach = (float) (distance*0.5);
        Vec3 pointVec = DamageHandler.getRayPoint(self, halfReach);
        return FingerGrabHitbox(DamageHandler.genHitbox(self, pointVec.x, pointVec.y,
                pointVec.z, halfReach, halfReach, halfReach), distance);
    }


    public float getBlitzKnockback(){
        return 0.42F;
    }
    public void impaleImpact2(Entity entity){
        this.setAttackTimeDuring(-7);
        if (entity != null) {
            hitParticles(entity);
            float pow;
            float knockbackStrength;
            pow = getStarBlitzStrength(entity);
            knockbackStrength = getBlitzKnockback();
            if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                if (entity instanceof LivingEntity LE) {
                    addEXP(1, LE);
                }
                this.takeDeterminedKnockbackWithY(this.self, entity, knockbackStrength);
                entity.setDeltaMovement(entity.getDeltaMovement().add(0,0.3,0));
            } else {
                knockShield2(entity, 30);
            }
        }

        if (this.getSelf() instanceof Player) {
            S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_1_SNEAK, ClientNetworking.getAppropriateConfig().starPlatinumSettings.blitzAttackCooldown);
        }
        this.setCooldown(PowerIndex.SKILL_1_SNEAK, ClientNetworking.getAppropriateConfig().generalStandSettings.impaleAttackCooldown);
        SoundEvent SE;
        float pitch = 1F;
        if (entity != null) {
            SE = ModSounds.PUNCH_3_SOUND_EVENT;
            pitch = 0.9F;
        } else {
            SE = ModSounds.PUNCH_1_SOUND_EVENT;
        }

        if (!this.self.level().isClientSide()) {
            animateStand(StarPlatinumEntity.IMPALE_2);
            this.self.level().playSound(null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
        }
    }

    @Override
    public void handleStandAttack(Player player, Entity target){
            super.handleStandAttack(player,target);
    }
    @Override
    public void handleStandAttack2(Player player, Entity target){
        if (activePower == POWER_EARLY_STAR_FINGER) {
            impaleImpact2(target);
        }
        if (this.getActivePower() == POWER_STAR_FINGER){
            fingerDamage(target);
        }
    }
    @Override
    public boolean dealWithProjectile(Entity ent, HitResult res){
        if (!ent.level().isClientSide()) {
            StandEntity stand = getStandEntity(this.self);
            if (Objects.nonNull(stand) && stand instanceof StarPlatinumEntity SE && this.self instanceof ServerPlayer PE) {
                if (SE.getScoping() && !onCooldown(PowerIndex.SKILL_EXTRA_2)) {
                    if (!hasBlock() && !hasEntity() &&
                            ((StandUser) this.getSelf()).roundabout$getActivePower() == PowerIndex.GUARD) {
                        boolean success = false;
                        if (ent instanceof AbstractArrow AA) {
                            ItemStack ii = ((IAbstractArrowAccess)ent).roundabout$GetPickupItem();
                            if (!ii.isEmpty() && !ii.isDamageableItem()) {
                                success = true;
                                S2CPacketUtil.sendSimpleByteToClientPacket(PE,
                                        PacketDataIndex.S2C_SIMPLE_SUSPEND_RIGHT_CLICK);
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                                if (AA.pickup.equals(AbstractArrow.Pickup.ALLOWED)) {
                                    SE.canAcquireHeldItem = true;
                                } else {
                                    SE.canAcquireHeldItem = false;
                                }
                                SE.setHeldItem(ii.copyAndClear());
                            } else if (ent instanceof RattDartEntity RD) {
                                success = true;
                                RD.applyEffect(this.getSelf());
                            }
                        } else if (ent instanceof ThrownObjectEntity TO) {
                            ItemStack ii = TO.getItem();
                            if (!ii.isEmpty()) {
                                success = true;
                                S2CPacketUtil.sendSimpleByteToClientPacket(PE,
                                        PacketDataIndex.S2C_SIMPLE_SUSPEND_RIGHT_CLICK);
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                                if (TO.places) {
                                    SE.canAcquireHeldItem = true;
                                } else {
                                    SE.canAcquireHeldItem = false;
                                }
                                SE.setHeldItem(ii.copyAndClear());
                            }
                        } else if (ent instanceof ThrownPotion TP) {
                            ItemStack ii = TP.getItem();
                            if (!ii.isEmpty()) {
                                success = true;
                                S2CPacketUtil.sendSimpleByteToClientPacket(PE,
                                        PacketDataIndex.S2C_SIMPLE_SUSPEND_RIGHT_CLICK);
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                                if (TP.getOwner() == null || TP.getOwner() instanceof Player) {
                                    SE.canAcquireHeldItem = true;
                                } else {
                                    SE.canAcquireHeldItem = false;
                                }
                                SE.setHeldItem(ii.copyAndClear());
                            }
                        }

                        if (success){
                            int cdr = ClientNetworking.getAppropriateConfig().starPlatinumSettings.guardianCooldown;
                            S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()),
                                    PowerIndex.SKILL_EXTRA_2, cdr);
                            ((StarPlatinumEntity) stand).setScoping(false);
                            if (ClientNetworking.getAppropriateConfig().starPlatinumSettings.starPlatinumScopeUsesPotionEffectForNightVision) {
                                MobEffectInstance ME = this.getSelf().getEffect(MobEffects.NIGHT_VISION);
                                if (ME != null && ME.getDuration() >= 100000 && ME.getAmplifier() > 20) {
                                    this.getSelf().removeEffect(MobEffects.NIGHT_VISION);
                                }
                            }
                            this.setCooldown(PowerIndex.SKILL_EXTRA_2, cdr);
                            this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.ITEM_CATCH_EVENT, SoundSource.PLAYERS, 1.7F, 1.2F);
                            this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.BLOCK_GRAB_EVENT, SoundSource.PLAYERS, 1.7F, 0.5F);
                            poseStand(OffsetIndex.FOLLOW_NOLEAN);
                            if (MainUtil.isThrownBlockItem(SE.getHeldItem().getItem())) {
                                animateStand(StandEntity.BLOCK_GRAB);
                            } else {
                                animateStand(StandEntity.ITEM_GRAB);
                            }

                            ((ServerLevel) this.self.level()).sendParticles(ModParticles.AIR_CRACKLE,
                                    ent.getX(), ent.getY(), ent.getZ(),
                                    0, 0, 0, 0, 0);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void doFingerHit(List<Entity> entities){
        List<Entity> hitEntities = new ArrayList<>(entities) {
        };
        for (Entity value : hitEntities) {
            if (this.isPacketPlayer()){
                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK_2,value.getId());
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
    public void dspStuff(Entity ent){
        if (((StandUser)this.getSelf()).roundabout$getStandSkin() == StarPlatinumEntity.ATOMIC_SKIN) {
            ((ServerLevel) this.self.level()).sendParticles(ParticleTypes.EXPLOSION,
                    ent.getX(), ent.getY() + ent.getEyeHeight(), ent.getZ(),
                    5, 0.6, 0.6, 0.6, 0.4);
        }
    }
    @Override
    public SoundEvent getFinalAttackSound(){
        if (((StandUser)this.getSelf()).roundabout$getStandSkin() == StarPlatinumEntity.BASEBALL_SKIN){
            return ModSounds.EXPLOSIVE_BAT_EVENT;
        }
        return ModSounds.EXPLOSIVE_PUNCH_EVENT;
    }
    public void fingerDamage(Entity entity){
        float pow = getFingerDamage(entity);
        float knockbackStrength = 0.3F;
        if(ticksForFinger < 26){
            pow*=(1 - ((float) (26-ticksForFinger) /26));
        }

        hitParticlesCenter(entity);
        if(ticksForFinger < 26){
            if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                this.takeDeterminedKnockback(this.self, entity, knockbackStrength);
                if (entity instanceof LivingEntity LE){
                    addEXP(1, LE);
                    if (ticksForFinger >13){
                        MainUtil.makeBleed(LE,0,200,this.self);
                    }
                }
            }
        } else {
            if (StarFingerDamageEntityAttack(entity, pow, 0, this.self)) {
                this.takeDeterminedKnockback(this.self, entity, knockbackStrength);
                if (entity instanceof LivingEntity LE){
                    addEXP(2, LE);
                    MainUtil.makeBleed(LE,1,200,this.self);
                }
            } else {
                knockShield2(entity, 40);
            }
        }
    }

    public boolean StarFingerDamageEntityAttack(Entity target, float pow, float knockbackStrength, Entity attacker){
        if (attacker instanceof TamableAnimal TA){
            if (target instanceof TamableAnimal TT && TT.getOwner() != null
                    && TA.getOwner() != null && TT.getOwner().is(TA.getOwner())){
                return false;
            }
        } else if (attacker instanceof AbstractVillager){
            if (target instanceof AbstractVillager){
                return false;
            }
        }
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

    public float getPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod((float) ((float) 1.75* (ClientNetworking.getAppropriateConfig().
                    starPlatinumSettings.starPlatinumAttackMultOnPlayers*0.01)));
        } else {
            return levelupDamageMod((float) ((float) 5* (ClientNetworking.getAppropriateConfig().
                    starPlatinumSettings.starPlatinumAttackMultOnMobs*0.01)));
        }
    } public float getHeavyPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod((float) ((float) 2.5* (ClientNetworking.getAppropriateConfig().
                    starPlatinumSettings.starPlatinumAttackMultOnPlayers*0.01)));
        } else {
            return levelupDamageMod((float) ((float) 6* (ClientNetworking.getAppropriateConfig().
                    starPlatinumSettings.starPlatinumAttackMultOnMobs*0.01)));
        }
    }
    public float getFingerDamage(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod((float) ((float) 2.7* (ClientNetworking.getAppropriateConfig().
                    starPlatinumSettings.starPlatinumAttackMultOnPlayers*0.01)));
        } else {
            return levelupDamageMod((float) ((float) 10* (ClientNetworking.getAppropriateConfig().
                    starPlatinumSettings.starPlatinumAttackMultOnMobs*0.01)));
        }
    }
    public List<Entity> FingerGrabHitbox(List<Entity> entities, float maxDistance){
        List<Entity> hitEntities = new ArrayList<>(entities) {
        };
        Direction gravD = ((IGravityEntity)this.self).roundabout$getGravityDirection();

        for (Entity value : entities) {
            if (value.isInvulnerable() || ((!value.isAttackable() || !MainUtil.isStandPickable(value)) && !(value instanceof StandEntity)) || !value.isAlive() || (this.self.isPassenger() && this.self.getVehicle().getUUID() == value.getUUID())
            || (value instanceof StandEntity SE && SE.getUser() !=null && SE.getUser().getUUID() == this.self.getUUID())){
                hitEntities.remove(value);
            } else {
                int angle = 18;
                Vec2 lookVec = new Vec2(getLookAtEntityYaw(self, value), getLookAtEntityPitch(self, value));
                if (gravD != Direction.DOWN) {
                    lookVec = RotationUtil.rotPlayerToWorld(lookVec.x, lookVec.y, gravD);
                }
                if (!(angleDistance(lookVec.x, self.getYHeadRot()%360f) <= angle && angleDistance(lookVec.y, self.getXRot()) <= angle)){
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

    public void updateStarFinger(){
        if (this.attackTimeDuring > -1) {
            StandEntity stand = getStandEntity(this.self);
            if (this.attackTimeDuring > 40) {
                if (Objects.nonNull(stand) && stand instanceof StarPlatinumEntity SE){
                    if (this.self instanceof Player) {
                        if (isPacketPlayer()) {
                            C2SPacketUtil.floatToServerPacket(PacketDataIndex.FLOAT_STAR_FINGER_SIZE,1F);
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
                        double maxDist = Math.max(Math.sqrt(dd.distanceTo(this.getSelf())),1);
                        double maxDist2 = Math.max(Math.sqrt(dd.distanceTo(this.getSelf()))*16-32,1);
                        C2SPacketUtil.floatToServerPacket(PacketDataIndex.FLOAT_STAR_FINGER_SIZE,(float)
                                maxDist2);
                        if (this.attackTimeDuring == 27){
                            int cdr = ClientNetworking.getAppropriateConfig().starPlatinumSettings.starFingerCooldown;
                            this.setCooldown(PowerIndex.SKILL_1, cdr);
                            ticksForFinger = 101;
                            List<Entity> fingerTargets = doFinger((float) maxDist);
                            if (!fingerTargets.isEmpty()){
                                doFingerHit(fingerTargets);
                            }
                        }
                    }
                } else {
                    BlockHitResult dd = getAheadVec(distanceOut);
                    if (Objects.nonNull(stand) && stand instanceof StarPlatinumEntity SE){
                        double maxDist = Math.max(Math.sqrt(dd.distanceTo(this.getSelf())),1);
                        double maxDist2 = Math.max(Math.sqrt(dd.distanceTo(this.getSelf()))*16-32,1);
                        if (!this.getSelf().level().isClientSide) {
                            SE.setFingerLength((float) maxDist2);
                        }
                        if (this.attackTimeDuring == 27){
                            int cdr = ClientNetworking.getAppropriateConfig().starPlatinumSettings.starFingerCooldown;
                            this.setCooldown(PowerIndex.SKILL_1, cdr);

                            List<Entity> fingerTargets = doFinger((float) maxDist);
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
        if (this.getActivePower() == POWER_STAR_FINGER  && this.getAttackTimeDuring() >= 0 && this.getAttackTimeDuring() <= 26){
            return true;
        } else if (this.getActivePower() == POWER_INHALE){
            return true;
        }
        return super.cancelSprintJump();
    }
    /**Makes*/
    public boolean fullTSChargeBonus(){
        if (canExecuteMoveWithLevel(getMaxTSFactorLevel()) && ClientNetworking.getAppropriateConfig().timeStopSettings.maxSPBypassesReduction){
            return this.maxChargedTSTicks >= ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksStarPlatinum;
        } else {
            return false;
        }
    }

    @Override
    public boolean canInterruptPower(){
        if (this.getActivePower() == POWER_STAR_FINGER && this.getAttackTimeDuring() >= 0 && this.getAttackTimeDuring() <= 26){
            int cdr = ClientNetworking.getAppropriateConfig().starPlatinumSettings.starFingerInterruptCooldown;
            if (this.getSelf() instanceof Player) {
                S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_1, cdr);
            }
            this.setCooldown(PowerIndex.SKILL_1, cdr);
            return true;
        } else {
            return super.canInterruptPower();
        }
    }

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        if (this.getActivePower() == POWER_STAR_FINGER){
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
    public List<Byte> getSkinList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(StarPlatinumEntity.PART_3_SKIN);
        if (this.getSelf() instanceof Player PE){
            byte Level = ((IPlayerEntity)PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser)PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (Level > 1 || bypass){
                $$1.add(StarPlatinumEntity.MANGA_SKIN);
                $$1.add(StarPlatinumEntity.FIRST_SKIN);
                $$1.add(StarPlatinumEntity.OVA_SKIN);
                $$1.add(StarPlatinumEntity.FOUR_DEE);
            } if (Level > 2 || bypass){
                $$1.add(StarPlatinumEntity.JOJOVELLER);
                $$1.add(StarPlatinumEntity.VOLUME_39);
                $$1.add(StarPlatinumEntity.GREEN_SKIN);
                $$1.add(StarPlatinumEntity.GREEN_2);
            } if (Level > 3 || bypass){
                $$1.add(StarPlatinumEntity.ARCADE);
                $$1.add(StarPlatinumEntity.ARCADE_2);
                $$1.add(StarPlatinumEntity.BASEBALL_SKIN);
            } if (Level > 4 || bypass){
                $$1.add(StarPlatinumEntity.LIGHT);
                $$1.add(StarPlatinumEntity.TREE);
                $$1.add(StarPlatinumEntity.NETHER);
                $$1.add(StarPlatinumEntity.JUMP_13);
            } if (Level > 5 || bypass){
                $$1.add(StarPlatinumEntity.JOJONIUM_SKIN);
                $$1.add(StarPlatinumEntity.PART_4_SKIN);
                $$1.add(StarPlatinumEntity.CROP);
            } if (Level > 6 || bypass){
                $$1.add(StarPlatinumEntity.PART_6_SKIN);
                $$1.add(StarPlatinumEntity.MANGA_PURPLE_SKIN);
                $$1.add(StarPlatinumEntity.PHANTOM);
                $$1.add(StarPlatinumEntity.BETA);
            } if (((IPlayerEntity)PE).roundabout$getUnlockedBonusSkin() || bypass){
                $$1.add(StarPlatinumEntity.ATOMIC_SKIN);
            }
        }
        return $$1;
    }

    @Override
    public void tickMobAI(LivingEntity attackTarget){
        if (this.attackTimeDuring <= -1) {
            if (this.getSelf().fallDistance > 4 && !(this.self instanceof FlyingMob) && !this.getSelf().isNoGravity()
                    && !(this.getSelf().noPhysics) && !(this.self instanceof EnderDragon) && !(this.self instanceof WitherBoss)) {
                /**Fall Brace AI*/
                ((StandUser) this.getSelf()).roundabout$summonStand(this.getSelf().level(),true,false);
                if (this.getSelf() instanceof Mob MB){
                    ((IMob)MB).roundabout$setRetractTicks(140);
                }
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.EXTRA, true);
                return;
            }
        }
        if (attackTarget != null && attackTarget.isAlive() && !this.isDazed(this.getSelf())) {
            double dist = attackTarget.distanceTo(this.getSelf());
            boolean isCreeper = this.getSelf() instanceof Creeper;
            if (isCreeper) {
                boolean inhaling = this.getActivePower() == POWER_INHALE;
                if (dist <= 8) {
                    if (!inhaling){
                        ((StandUser) this.getSelf()).roundabout$tryPower(POWER_INHALE, true);
                    }
                } else {
                    if (inhaling){
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                    }
                }
            } else {

                if ((this.getActivePower() != PowerIndex.NONE)
                        || dist <= 5){
                    this.getSelf().setXRot(getLookAtEntityPitch(this.getSelf(), attackTarget));
                    float yrot = getLookAtEntityYaw(this.getSelf(), attackTarget);
                    this.getSelf().setYRot(yrot);
                    this.getSelf().setYHeadRot(yrot);
                }

                if (this.attackTimeDuring == -1 || (this.attackTimeDuring < -1 && this.activePower == PowerIndex.ATTACK)) {
                    Entity targetEntity = getTargetEntity(this.self, -1);
                    if (targetEntity != null && targetEntity.is(attackTarget)) {
                        double RNG = Math.random();
                        if (RNG < 0.4 && targetEntity instanceof Player && this.activePowerPhase <= 0 && !wentForCharge) {
                            wentForCharge = true;
                            if (RNG < 0.1) {
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.BARRAGE_CHARGE_2, true);
                            } else {
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.BARRAGE_CHARGE, true);
                            }
                        } else if (this.activePowerPhase < this.activePowerPhaseMax || this.attackTime >= this.attackTimeMax) {
                            if ((RNG < 0.85 && (this.getSelf() instanceof Hoglin || this.getSelf() instanceof Ravager)) ||
                                    (this.self instanceof JotaroNPC && RNG < 0.47)) {
                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.SNEAK_ATTACK_CHARGE, true);
                                wentForCharge = false;
                            } else {
                                if (!onCooldown(PowerIndex.SKILL_1_SNEAK) && RNG >= 0.85 && dist <= 3 && !wentForCharge) {
                                    ((StandUser) this.getSelf()).roundabout$tryPower(POWER_EARLY_STAR_FINGER, true);
                                    wentForCharge = true;
                                } else {
                                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.ATTACK, true);
                                    wentForCharge = false;
                                }
                            }
                        }
                    } else if ((this.getSelf().getHealth() > 20 || this.getSelf() instanceof Piglin
                            || this.getSelf() instanceof JotaroNPC
                            || this.getSelf() instanceof AbstractVillager) && dist <= 8 && dist >= 5) {
                        if (!onCooldown(PowerIndex.SKILL_1)) {
                            ((StandUser) this.getSelf()).roundabout$tryPower(POWER_STAR_FINGER, true);
                        }
                    } else if ((this.getSelf() instanceof Spider || this.getSelf() instanceof Slime
                            || this.getSelf() instanceof JotaroNPC
                            || this.getSelf() instanceof Rabbit || this.getSelf() instanceof AbstractVillager
                            || this.getSelf() instanceof Piglin || this.getSelf() instanceof Vindicator) &&
                            this.getSelf().onGround() && dist <= 19 && dist >= 5) {
                        if (!onCooldown(PowerIndex.GLOBAL_DASH)){
                            if (!this.onCooldown(PowerIndex.GLOBAL_DASH)) {
                                    this.setCooldown(PowerIndex.GLOBAL_DASH, 300);
                                    bonusLeapCount = 3;
                                    this.getSelf().setXRot(getLookAtEntityPitch(this.getSelf(), attackTarget));
                                    float yrot = getLookAtEntityYaw(this.getSelf(), attackTarget);
                                    this.getSelf().setYRot(yrot);
                                    this.getSelf().setYRot(yrot);
                                    this.getSelf().setYHeadRot(yrot);
                                    bigLeap(this.getSelf(), 20, 1);
                                    ((StandUser) this.getSelf()).roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
                                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.SNEAK_MOVEMENT, true);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean tryPower(int move, boolean forced) {

        if (this.getActivePower() == POWER_STAR_FINGER){
            stopSoundsIfNearby(STAR_FINGER, 100, false);
            stopSoundsIfNearby(STAR_FINGER_2, 100, false);
            stopSoundsIfNearby(STAR_FINGER_3, 100, false);
            stopSoundsIfNearby(STAR_FINGER_SILENT, 100, false);
            StandEntity stand = getStandEntity(this.self);
            if (Objects.nonNull(stand) && stand instanceof StarPlatinumEntity SE && SE.getFingerLength() > 1.01) {
                if (this.getSelf() instanceof Player && isPacketPlayer()) {
                    C2SPacketUtil.floatToServerPacket(PacketDataIndex.FLOAT_STAR_FINGER_SIZE,1F);
                }
                SE.setFingerLength(1F);
            }
        }
        return super.tryPower(move,forced);
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level,boolean bypas){
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+80,0, "ability.roundabout.punch",
                "instruction.roundabout.press_attack", StandIcons.STAR_PLATINUM_PUNCH,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20, topPos+99,0, "ability.roundabout.guard",
                "instruction.roundabout.hold_block", StandIcons.STAR_PLATINUM_GUARD,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20,topPos+118,0, "ability.roundabout.final_punch",
                "instruction.roundabout.hold_attack_crouch", StandIcons.STAR_PLATINUM_FINAL_PUNCH,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+80,0, "ability.roundabout.barrage",
                "instruction.roundabout.barrage", StandIcons.STAR_PLATINUM_BARRAGE,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+99,0, "ability.roundabout.kick_barrage",
                "instruction.roundabout.kick_barrage", StandIcons.STAR_PLATINUM_KICK_BARRAGE,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39,topPos+118,0, "ability.roundabout.forward_barrage",
                "instruction.roundabout.forward_barrage", StandIcons.STAR_PLATINUM_TRAVEL_BARRAGE,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+80, getFingerLevel(), "ability.roundabout.star_finger",
                "instruction.roundabout.press_skill", StandIcons.STAR_PLATINUM_FINGER,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+99, getImpaleLevel(), "ability.roundabout.impale_blitz",
                "instruction.roundabout.press_skill_crouch", StandIcons.STAR_PLATINUM_IMPALE,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58,topPos+118,0, "ability.roundabout.scope",
                "instruction.roundabout.press_skill_block", StandIcons.STAR_PLATINUM_SCOPE,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+80,0, "ability.roundabout.block_grab",
                "instruction.roundabout.press_skill", StandIcons.STAR_PLATINUM_GRAB_BLOCK,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+99,0, "ability.roundabout.item_grab",
                "instruction.roundabout.press_skill_crouch", StandIcons.STAR_PLATINUM_GRAB_ITEM,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77,topPos+118,0, "ability.roundabout.mob_grab",
                "instruction.roundabout.press_skill_near_mob", StandIcons.STAR_PLATINUM_GRAB_MOB,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+80,0, "ability.roundabout.phase_grab",
                "instruction.roundabout.press_skill_block", StandIcons.STAR_PLATINUM_PHASE_GRAB,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+99,0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96,topPos+118,0, "ability.roundabout.fall_brace",
                "instruction.roundabout.press_skill_falling", StandIcons.STAR_PLATINUM_FALL_CATCH,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+80,0, "ability.roundabout.vault",
                "instruction.roundabout.press_skill_air", StandIcons.STAR_PLATINUM_LEDGE_GRAB,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+99,getLeapLevel(), "ability.roundabout.stand_leap",
                "instruction.roundabout.press_skill_crouch", StandIcons.STAND_LEAP_STAR_PLATINUM,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115,topPos+118,getLeapLevel(), "ability.roundabout.stand_leap_rebound",
                "instruction.roundabout.press_skill_rebound", StandIcons.STAND_LEAP_REBOUND_STAR_PLATINUM,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+134,topPos+80,getInhaleLevel(), "ability.roundabout.inhale",
                "instruction.roundabout.press_skill_block", StandIcons.STAR_PLATINUM_INHALE,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+134,topPos+99,6, "ability.roundabout.time_stop",
                "instruction.roundabout.press_skill", StandIcons.STAR_PLATINUM_TIME_STOP,4,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+134,topPos+118,6, "ability.roundabout.time_stop_impulse",
                "instruction.roundabout.press_skill_crouch", StandIcons.STAR_PLATINUM_TIME_STOP_IMPULSE,4,level,bypas));
        return $$1;
    }
    public int getFingerLevel(){
        return 2;
    }
    public int getInhaleLevel(){
        return 4;
    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y){

        boolean rendered1 = false;
        if (canScope()){
            rendered1 = true;
            if (scopeLevel == 1){
                setSkillIcon(context, x, y, 1, StandIcons.STAR_PLATINUM_SCOPE_1, PowerIndex.SKILL_EXTRA_2);
            } else if (scopeLevel == 2) {
                setSkillIcon(context, x, y, 1, StandIcons.STAR_PLATINUM_SCOPE_2, PowerIndex.SKILL_EXTRA_2);
            } else if (scopeLevel == 3) {
                setSkillIcon(context, x, y, 1, StandIcons.STAR_PLATINUM_SCOPE_3, PowerIndex.SKILL_EXTRA_2);
            } else {
                setSkillIcon(context, x, y, 1, StandIcons.STAR_PLATINUM_SCOPE, PowerIndex.SKILL_EXTRA_2);
            }
        } else {
            if (this.isBarrageAttacking() || this.getActivePower() == PowerIndex.BARRAGE_2) {
                setSkillIcon(context, x, y, 1, StandIcons.STAR_PLATINUM_TRAVEL_BARRAGE, PowerIndex.NO_CD);
            } else {
                if (isHoldingSneak()) {
                    if (canExecuteMoveWithLevel(getImpaleLevel())) {
                        setSkillIcon(context, x, y, 1, StandIcons.STAR_PLATINUM_IMPALE, PowerIndex.SKILL_1_SNEAK);
                    } else {
                        setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                    }
                } else {
                    if (canExecuteMoveWithLevel(getFingerLevel())) {
                        setSkillIcon(context, x, y, 1, StandIcons.STAR_PLATINUM_FINGER, PowerIndex.SKILL_1);
                    } else {
                        setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                    }
                }
            }
        }

        if (isHoldingSneak()){

            if (this.isGuarding()) {
                setSkillIcon(context, x, y, 2, StandIcons.STAR_PLATINUM_PHASE_GRAB, PowerIndex.SKILL_2);
            } else {
                setSkillIcon(context, x, y, 2, StandIcons.STAR_PLATINUM_GRAB_ITEM, PowerIndex.SKILL_2);
            }

            if (this.isGuarding()){
                    if (canExecuteMoveWithLevel(getInhaleLevel())) {
                        setSkillIcon(context, x, y, 3, StandIcons.STAR_PLATINUM_INHALE, PowerIndex.NO_CD);
                    } else {
                        setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                    }
            } else {
                boolean done = false;
                if (((StandUser) this.getSelf()).roundabout$getLeapTicks() > -1) {
                    if (!this.getSelf().onGround() && canStandRebound()) {
                        done = true;
                            if (canExecuteMoveWithLevel(getLeapLevel())) {
                                setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_REBOUND_STAR_PLATINUM, PowerIndex.NO_CD);
                            } else {
                                setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                            }
                    }
                } else {
                    if (!this.getSelf().onGround()) {
                        if (canVault()) {
                            done = true;
                            setSkillIcon(context, x, y, 3, StandIcons.STAR_PLATINUM_LEDGE_GRAB, PowerIndex.GLOBAL_DASH);
                        } else if (this.getSelf().fallDistance > 3) {
                            done = true;
                            setSkillIcon(context, x, y, 3, StandIcons.STAR_PLATINUM_FALL_CATCH, PowerIndex.SKILL_EXTRA);
                        }
                    }
                }
                if (!done) {
                    if (canExecuteMoveWithLevel(getLeapLevel())) {
                        boolean jojoveinLikeKeys = !ClientNetworking.getAppropriateConfig().generalStandSettings.standJumpAndDashShareCooldown;
                        if (jojoveinLikeKeys){
                            setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_STAR_PLATINUM, PowerIndex.SKILL_3);
                        } else {
                            setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_STAR_PLATINUM, PowerIndex.GLOBAL_DASH);
                        }
                    } else {
                        setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                    }
                }
            }
        } else {


            //setSkillIcon(context, x, y, 1, StandIcons.THE_WORLD_ASSAULT, PowerIndex.SKILL_1);

            /*If it can find a mob to grab, it will*/
            if (this.isGuarding()) {
                setSkillIcon(context, x, y, 2, StandIcons.STAR_PLATINUM_PHASE_GRAB, PowerIndex.SKILL_2);
            } else {
                Entity targetEntity = MainUtil.getTargetEntity(this.getSelf(), 2.1F);
                if (targetEntity != null && canGrab(targetEntity)) {
                    setSkillIcon(context, x, y, 2, StandIcons.STAR_PLATINUM_GRAB_MOB, PowerIndex.SKILL_2);
                } else {
                    setSkillIcon(context, x, y, 2, StandIcons.STAR_PLATINUM_GRAB_BLOCK, PowerIndex.SKILL_2);
                }
            }

            if (this.isGuarding()){
                if (canExecuteMoveWithLevel(getInhaleLevel())) {
                    setSkillIcon(context, x, y, 3, StandIcons.STAR_PLATINUM_INHALE, PowerIndex.NO_CD);
                } else {
                    setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                }
            } else {
               if (((StandUser) this.getSelf()).roundabout$getLeapTicks() > -1 && !this.getSelf().onGround() && canStandRebound()) {

                   setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_REBOUND_STAR_PLATINUM, PowerIndex.NO_CD);
               } else {
                   if (!(((StandUser) this.getSelf()).roundabout$getLeapTicks() > -1) && !this.getSelf().onGround() && canVault()) {
                       setSkillIcon(context, x, y, 3, StandIcons.STAR_PLATINUM_LEDGE_GRAB, PowerIndex.GLOBAL_DASH);
                   } else if (!this.getSelf().onGround() && this.getSelf().fallDistance > 3) {
                       setSkillIcon(context, x, y, 3, StandIcons.STAR_PLATINUM_FALL_CATCH, PowerIndex.SKILL_EXTRA);
                   } else {
                       setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
                   }
               }
           }
        }
        boolean exTS = canExecuteMoveWithLevel(getTSLevel());
        boolean exImpTS = canExecuteMoveWithLevel(getImpulseTSLevel());
        if (((TimeStop)this.getSelf().level()).isTimeStoppingEntity(this.getSelf())) {
            setSkillIcon(context, x, y, 4, StandIcons.STAR_PLATINUM_TIME_STOP_RESUME, PowerIndex.NO_CD);
        } else if (isHoldingSneak()){
            if (exImpTS) {
                setSkillIcon(context, x, y, 4, StandIcons.STAR_PLATINUM_TIME_STOP_IMPULSE, PowerIndex.SKILL_4);
            } else {
                setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD,true);
            }
        } else {
            if (exTS) {
                setSkillIcon(context, x, y, 4, StandIcons.STAR_PLATINUM_TIME_STOP, PowerIndex.SKILL_4);
            } else {
                setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD,true);
            }
        }
    }

    @Override
    public boolean clickRelease(){
        if (this.getActivePower() == POWER_INHALE){
            return true;
        }
        return super.clickRelease();
    }

    @Override
    public boolean buttonInputGuard(boolean keyIsDown, Options options) {
        if (suspendGuard) {
            return false;
        }
        if (this.activePower == POWER_INHALE) {
            return false;
        }
        return super.buttonInputGuard(keyIsDown,options);
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == POWER_STAR_FINGER) {
            return this.starFinger();
        } else if (move == POWER_INHALE) {
            return this.inhale();
        }
        return super.setPowerOther(move,lastMove);
    }
    public static final byte STAR_FINGER = 80;
    public static final byte STAR_FINGER_2 = 81;
    public static final byte STAR_FINGER_3 = 82;
    public static final byte STAR_FINGER_SILENT = 83;

    public boolean inhale(){
        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand) && !((TimeStop)this.getSelf().level()).inTimeStopRange(this.self))
        {
            this.setAttackTimeDuring(0);
            this.setActivePower(POWER_INHALE);
            this.animateStand((byte)15);
            this.poseStand(OffsetIndex.GUARD_FURTHER_RIGHT);
            return true;
        }
        return false;
    }
    @Override
    public void playTSVoiceSound(){
        if (this.self instanceof Player pe && ((IPlayerEntity)pe).roundabout$getVoiceData() instanceof JotaroVoice JV) {
            if (!JV.inTheMiddleOfTalking()) {
                JV.forceTalkingTicks(40);
                playSoundsIfNearby(getTSVoice(), 100, false, true);
            }
        } else {

            playStandUserOnlySoundsIfNearby(getTSVoice(), 100, false, true);
        }
    }
    public boolean starFinger(){
        StandEntity stand = getStandEntity(this.self);
        ticksForFinger = 100;
        if (Objects.nonNull(stand)){
            this.setAttackTimeDuring(0);
            this.setActivePower(POWER_STAR_FINGER);
            double rand = Math.random();
            byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();
            if (skn == StarPlatinumEntity.OVA_SKIN ||
                    this.getSelf() instanceof Player PE &&
                    ((IPlayerEntity)PE).roundabout$getMaskInventory().getItem(1).is(ModItems.BLANK_MASK)) {
                playStandUserOnlySoundsIfNearby(STAR_FINGER_SILENT, 32, false, false);
            } else if (isArcade(skn)){
                playStandUserOnlySoundsIfNearby(STAR_FINGER_3, 32, false, true);
            } else{

                if (this.self instanceof Player pe && ((IPlayerEntity)pe).roundabout$getVoiceData() instanceof JotaroVoice JV) {
                    if (!JV.inTheMiddleOfTalking()) {
                        JV.forceTalkingTicks(56);
                        if (rand > 0.5) {
                            playSoundsIfNearby(STAR_FINGER, 32, false, true);
                        } else {
                            playSoundsIfNearby(STAR_FINGER_2, 32, false, true);
                        }
                    }
                } else {
                    if (rand > 0.5) {
                        playStandUserOnlySoundsIfNearby(STAR_FINGER, 32, false, true);
                    } else {
                        playStandUserOnlySoundsIfNearby(STAR_FINGER_2, 32, false, true);
                    }
                }

            }
            this.animateStand(StarPlatinumEntity.STAR_FINGER);
            this.poseStand(OffsetIndex.GUARD_AND_TRACE);
            //stand.setYRot(this.getSelf().getYHeadRot() % 360);
            //stand.setXRot(this.getSelf().getXRot());
            return true;
        }
        return false;
    }

    @Override
    public int getMobTSTime(){
        return ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksStarPlatinum;
    }
    @Override
    public int getMaxTSTime (){
        return ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksStarPlatinum;
    }


    @Override
    public void playSummonSound() {
        if (this.self.isCrouching()){
            return;
        }

        if (this.self instanceof Player pe && ((IPlayerEntity)pe).roundabout$getVoiceData() instanceof JotaroVoice JV){
            JV.playSummon();
        }
        playStandUserOnlySoundsIfNearby(this.getSummonSound(), 10, false,false);
    }

    @Override

    public void setChargeTicksMult(){
        this.setChargedTSTicks(this.getChargedTSTicks()*(1+
                (ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksStarPlatinum -100)/100));
    }
    @Override
    public int setCurrentMaxTSTime(int chargedTSSeconds){
        if (chargedTSSeconds >= (ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksStarPlatinum)){
            if (canExecuteMoveWithLevel(getMaxTSFactorLevel()) && this.getSelf() instanceof Player) {
                this.maxChargeTSTime = ClientNetworking.getAppropriateConfig().timeStopSettings.maxSPChargeBonusTicks +
                        ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksStarPlatinum;
                this.setChargedTSTicks(this.maxChargeTSTime);
                return ClientNetworking.getAppropriateConfig().timeStopSettings.maxSPChargeBonusTicks;
            } else {
                this.maxChargeTSTime = (int) (ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksStarPlatinum);
                this.setChargedTSTicks(this.maxChargeTSTime);
            }
        } else if (chargedTSSeconds == (Math.min(ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksStarPlatinum,ClientNetworking.getAppropriateConfig().timeStopSettings.impulseTimeStopLength))) {
            this.maxChargeTSTime = (int) (Math.min(ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksStarPlatinum,ClientNetworking.getAppropriateConfig().timeStopSettings.impulseTimeStopLength));
        } else {
            this.maxChargeTSTime = (int) (ClientNetworking.getAppropriateConfig().timeStopSettings.maxTimeStopTicksStarPlatinum);
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
        byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (skn == StarPlatinumEntity.OVA_SKIN) {
            return BARRAGE_NOISE_3;
        } else if (isArcade(skn)) {
                return BARRAGE_NOISE_8;
        } else {
            if (rand > 0.5) {
                return BARRAGE_NOISE;
            } else {
                return BARRAGE_NOISE_2;
            }
        }
    }

    @Override
    public void playBarrageCrySound(){
        if (!this.self.level().isClientSide()) {
            if (this.self instanceof Player pe && ((IPlayerEntity)pe).roundabout$getVoiceData() instanceof JotaroVoice JV) {
                if (Math.random() > 0.7) {
                    JV.playSoundIfPossible(ModSounds.JOTARO_JUDGEMENT_EVENT, 46, 1, 2);
                }
            }
        }
        super.playBarrageCrySound();
    }

    @Override
    public void animateFinalAttack(){
        if (((StandUser)this.getSelf()).roundabout$getStandSkin() == StarPlatinumEntity.BASEBALL_SKIN){
            animateStand(StarPlatinumBaseballEntity.BASEBALL_CHARGE);
        } else {
            super.animateFinalAttack();
        }
    }
    @Override
    public void animateFinalAttackHit(){
        if (((StandUser)this.getSelf()).roundabout$getStandSkin() == StarPlatinumEntity.BASEBALL_SKIN){
            animateStand(StarPlatinumBaseballEntity.BASEBALL_SWING);
        } else {
            super.animateFinalAttackHit();
        }
    }
    @Override
    public byte getTimeResumeNoise(){
        byte bt = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (bt == StarPlatinumEntity.OVA_SKIN) {
            return TIME_RESUME_NOISE_2;
        }
        return TIME_RESUME_NOISE;
    }

    @Override
    public byte getTimeStopShortNoise(){
        byte bt = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (bt == StarPlatinumEntity.OVA_SKIN ) {
            return TIME_STOP_NOISE_9;
        } if (isArcade(bt)) {
            return TIME_STOP_NOISE_12;
        }
        return TIME_STOP_NOISE_2;
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        if (soundChoice == BARRAGE_NOISE) {
            return ModSounds.STAR_PLATINUM_ORA_RUSH_2_SOUND_EVENT;
        } else if (soundChoice == SoundIndex.SUMMON_SOUND) {
            if (((StandUser)this.getSelf()).roundabout$getStandSkin() == StarPlatinumEntity.OVA_SKIN) {
                return ModSounds.OVA_SUMMON_EVENT;
            } else if (((StandUser)this.getSelf()).roundabout$getStandSkin() == StarPlatinumEntity.ATOMIC_SKIN){
                    return ModSounds.DSP_SUMMON_EVENT;
            } else {
                return ModSounds.STAR_SUMMON_SOUND_EVENT;
            }
        } else if (soundChoice == LAST_HIT_1_NOISE) {
            return ModSounds.STAR_PLATINUM_ORA_SOUND_EVENT;
        } else if (soundChoice == LAST_HIT_2_NOISE) {
            return ModSounds.STAR_PLATINUM_ORA_2_SOUND_EVENT;
        } else if (soundChoice == LAST_HIT_3_NOISE) {
            return ModSounds.STAR_PLATINUM_ORA_3_SOUND_EVENT;
        } else if (soundChoice == LAST_HIT_4_NOISE) {
            return ModSounds.OVA_PLATINUM_ORA_3_EVENT;
        } else if (soundChoice == LAST_HIT_5_NOISE) {
            return ModSounds.OVA_PLATINUM_ORA_2_EVENT;
        } else if (soundChoice == LAST_HIT_6_NOISE) {
            return ModSounds.OVA_PLATINUM_ORA_EVENT;
        } else if (soundChoice == LAST_HIT_10_NOISE) {
            return ModSounds.ARCADE_ORA_EVENT;
        } else if (soundChoice == LAST_HIT_11_NOISE) {
            return ModSounds.ARCADE_ORA_2_EVENT;
        } else if (soundChoice == LAST_HIT_12_NOISE) {
            return ModSounds.ARCADE_ORA_3_EVENT;
        } else if (soundChoice == SoundIndex.ALT_CHARGE_SOUND_1) {
            return ModSounds.STAND_BARRAGE_WINDUP_EVENT;
        } else if (soundChoice == BARRAGE_NOISE_2){
            return ModSounds.STAR_PLATINUM_ORA_RUSH_SOUND_EVENT;
        } else if (soundChoice == BARRAGE_NOISE_3){
            return ModSounds.OVA_PLATINUM_BARRAGE_EVENT;
        } else if (soundChoice == BARRAGE_NOISE_8) {
            return ModSounds.ARCADE_STAR_PLATINUM_BARRAGE_EVENT;
        } else if (soundChoice == STAR_FINGER){
            return ModSounds.STAR_FINGER_EVENT;
        } else if (soundChoice == STAR_FINGER_2){
            return ModSounds.STAR_FINGER_2_EVENT;
        } else if (soundChoice == STAR_FINGER_3){
            return ModSounds.ARCADE_STAR_FINGER_EVENT;
        } else if (soundChoice == STAR_FINGER_SILENT){
            return ModSounds.STAR_FINGER_SILENT_EVENT;
        } else if (soundChoice == IMPALE_NOISE) {
            return ModSounds.IMPALE_CHARGE_EVENT;
        } else if (soundChoice == TIME_STOP_CHARGE){
            return ModSounds.TIME_STOP_CHARGE_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_VOICE){
            if (((StandUser)this.getSelf()).roundabout$getStandSkin() == StarPlatinumEntity.OVA_SKIN) {
                return ModSounds.OVA_PLATINUM_ORA_4_EVENT;
            } else if (isArcade(((StandUser)this.getSelf()).roundabout$getStandSkin())){
                    return ModSounds.ARCADE_STAR_PLATINUM_TIME_STOP_EVENT;
            } else {
                return ModSounds.STAR_PLATINUM_TIMESTOP_SOUND_EVENT;
            }
        } else if (soundChoice == TIME_STOP_VOICE_2){
            if (((StandUser)this.getSelf()).roundabout$getStandSkin() == StarPlatinumEntity.OVA_SKIN){
                return ModSounds.OVA_PLATINUM_ORA_4_EVENT;
            } else if (isArcade(((StandUser)this.getSelf()).roundabout$getStandSkin())){
                return ModSounds.ARCADE_STAR_PLATINUM_TIME_STOP_EVENT;
            } else {
                return ModSounds.STAR_PLATINUM_TIMESTOP_2_SOUND_EVENT;
            }
        } else if (soundChoice == TIME_STOP_VOICE_3){
            return ModSounds.TIME_STOP_VOICE_THE_WORLD3_EVENT;
        } else if (soundChoice == TIME_STOP_ENDING_NOISE){
            return ModSounds.TIME_STOP_RESUME_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_ENDING_NOISE_2){
            return ModSounds.TIME_STOP_RESUME_THE_WORLD2_EVENT;
        } else if (soundChoice == TIME_STOP_TICKING){
            return ModSounds.TIME_STOP_TICKING_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }


    public float getStarBlitzStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(((float) ((float) 1* (ClientNetworking.getAppropriateConfig().
                    starPlatinumSettings.starPlatinumAttackMultOnPlayers*0.01))));
        } else {
            return levelupDamageMod(((float) ((float) 5* (ClientNetworking.getAppropriateConfig().
                    starPlatinumSettings.starPlatinumAttackMultOnMobs*0.01))));
        }
    }

    @Override
    public float getImpalePunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(((float) ((float) 3* (ClientNetworking.getAppropriateConfig().
                    starPlatinumSettings.starPlatinumAttackMultOnPlayers*0.01) * (ClientNetworking.getAppropriateConfig().
                    generalStandSettings.generalImpaleAttackMultiplier *0.01))));
        } else {
            return levelupDamageMod(((float) ((float) 17* (ClientNetworking.getAppropriateConfig().
                    starPlatinumSettings.starPlatinumAttackMultOnMobs*0.01) * (ClientNetworking.getAppropriateConfig().
                    generalStandSettings.generalImpaleAttackMultiplier *0.01))));
        }
    }
    @Override
    public SoundEvent getImpaleSound(){

        byte bt = ((StandUser)this.getSelf()).roundabout$getStandSkin();
        if (isArcade(bt)){
            return ModSounds.ARCADE_IMPALE_EVENT;
        }
        return super.getImpaleSound();
    }
    public static final byte LAST_HIT_1_NOISE = 120;
    public static final  byte LAST_HIT_2_NOISE = 121;
    public static final  byte LAST_HIT_3_NOISE = 122;
    public static final  byte LAST_HIT_4_NOISE = 123;
    public static final  byte LAST_HIT_5_NOISE = 124;
    public static final  byte LAST_HIT_6_NOISE = 125;

    public static final byte LAST_HIT_10_NOISE = 119;
    public static final byte LAST_HIT_11_NOISE = 118;
    public static final byte LAST_HIT_12_NOISE = 117;

    @Override
    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }
    public static Component getSkinNameT(byte skinId){
        if (skinId == StarPlatinumEntity.PART_3_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.base");
        } else if (skinId == StarPlatinumEntity.MANGA_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.manga");
        } else if (skinId == StarPlatinumEntity.MANGA_PURPLE_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.manga_purple");
        } else if (skinId == StarPlatinumEntity.FIRST_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.first_summon");
        } else if (skinId == StarPlatinumEntity.OVA_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.ova");
        } else if (skinId == StarPlatinumEntity.GREEN_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.green");
        } else if (skinId == StarPlatinumEntity.GREEN_2){
            return Component.translatable(  "skins.roundabout.star_platinum.green_2");
        } else if (skinId == StarPlatinumEntity.BASEBALL_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.baseball");
        } else if (skinId == StarPlatinumEntity.PART_4_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.part_4");
        } else if (skinId == StarPlatinumEntity.PART_6_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.part_6");
        } else if (skinId == StarPlatinumEntity.ATOMIC_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.atomic");
        } else if (skinId == StarPlatinumEntity.JOJONIUM_SKIN){
            return Component.translatable(  "skins.roundabout.star_platinum.jojonium");
        } else if (skinId == StarPlatinumEntity.BETA){
            return Component.translatable(  "skins.roundabout.star_platinum.beta");
        } else if (skinId == StarPlatinumEntity.ARCADE){
            return Component.translatable(  "skins.roundabout.star_platinum.arcade");
        } else if (skinId == StarPlatinumEntity.FOUR_DEE){
            return Component.translatable(  "skins.roundabout.star_platinum.four_dee");
        } else if (skinId == StarPlatinumEntity.JOJOVELLER){
            return Component.translatable(  "skins.roundabout.star_platinum.jojoveller");
        } else if (skinId == StarPlatinumEntity.CROP){
            return Component.translatable(  "skins.roundabout.star_platinum.crop");
        } else if (skinId == StarPlatinumEntity.VOLUME_39){
            return Component.translatable(  "skins.roundabout.star_platinum.volume_39");
        } else if (skinId == StarPlatinumEntity.JUMP_13){
            return Component.translatable(  "skins.roundabout.star_platinum.jump_13");
        } else if (skinId == StarPlatinumEntity.ARCADE_2){
            return Component.translatable(  "skins.roundabout.star_platinum.arcade_2");
        } else if (skinId == StarPlatinumEntity.TREE){
            return Component.translatable(  "skins.roundabout.star_platinum.tree");
        } else if (skinId == StarPlatinumEntity.NETHER){
            return Component.translatable(  "skins.roundabout.star_platinum.nether");
        } else if (skinId == StarPlatinumEntity.LIGHT){
            return Component.translatable(  "skins.roundabout.star_platinum.light");
        } else if (skinId == StarPlatinumEntity.PHANTOM){
            return Component.translatable(  "skins.roundabout.star_platinum.phantom");
        }
        return Component.translatable(  "skins.roundabout.star_platinum.base");
    }

}
