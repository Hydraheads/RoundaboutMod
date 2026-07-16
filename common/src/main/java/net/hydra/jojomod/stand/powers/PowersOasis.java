package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.platform.services.IPlatformHelper;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PowersOasis extends NewDashPreset {
    public PowersOasis(LivingEntity self) {
        super(self);
    }

    @Override
    public boolean isStandEnabled(){
        return ClientNetworking.getAppropriateConfig().oasisSettings.enableOasis;
    }
    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersOasis(entity);
    }
    @Override
    public boolean canSummonStandAsEntity(){
        return false;
    }
    @Override
    public boolean rendersPlayer(){
        return true;
    }
    @Override
    public boolean isBrawling(){
        return fistsOut;
    }
    @Override
    public boolean interceptAttack(){
        return fistsOut;
    }
    @Override
    public boolean interceptGuard(){
        return fistsOut;
    }

    public boolean fistsOut = false;


    public boolean renderSuit(){
        return (self instanceof Player pl || MainUtil.isHumanoid2(self)) && PowerTypes.hasStandActive(self);
    }

    // stand fading for first person
    public static float getOasisAmt(Entity entity, float partialTicks){
        float heyFull = 0;
        if (entity instanceof LivingEntity LE) {
            StandUser user = ((StandUser) LE);
            boolean hasOasisOut = user.roundabout$getStandPowers() instanceof PowersOasis po && po.renderSuit();
            int oasisTicks = user.roundabout$getOasisVanishTicks();
            if (hasOasisOut || oasisTicks > 0) {
                byte skin = user.roundabout$getStandSkin();
                if (user.roundabout$getLastStandSkin() != skin) {
                    user.roundabout$setLastStandSkin(skin);
                    oasisTicks = 0;
                    user.roundabout$setOasisVanishTicks(0);
                }

                float partialTicks2 = partialTicks % 1;
                if (hasOasisOut) {
                    heyFull = oasisTicks + partialTicks2;
                    heyFull = Math.min(heyFull / 10, 1f);
                } else {
                    heyFull = oasisTicks - partialTicks2;
                    heyFull = Math.max(heyFull / 10, 0);
                }
            }
        }
        return heyFull;
    }

    @Override
    public void onStandSummon(boolean desummon){
        if (self instanceof Player pl && fistsOut){
            pl.resetAttackStrengthTicker();
        }
    }






    public void toggleFistsClient() {
        if (!onCooldown(PowerIndex.POWER_1)) {
            if (self instanceof Player pl){
                pl.resetAttackStrengthTicker();
            }
            this.setCooldown(PowerIndex.SKILL_1, 9);
            tryPowerPacket(PowerIndex.POWER_1);
        }
    }

    public void toggleFists() {
        this.setCooldown(PowerIndex.SKILL_4, 9);

        if (!this.self.level().isClientSide()){
            fistsOut = !fistsOut;
            if (fistsOut){
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.HEEL_RAISE_EVENT, SoundSource.PLAYERS, 0.9F, (float) (1.02 + (Math.random() * 0.06)));
            }
            saveDiscAndSync();
        }
    }



    @Override
    public void powerActivate(PowerContext context) {
        switch (context) {

            case SKILL_1_NORMAL -> {
                toggleFistsClient();
            }

            case SKILL_1_CROUCH -> {
                mudHitClient();
            }
        }
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move) {

            case PowerIndex.POWER_1 -> {
                toggleFists();
            }
            case PowerIndex.SNEAK_ATTACK -> {
                kickAttack();
            }
            case PowerIndex.POWER_1_SNEAK -> {
                mudHitCharge();
            }


        }

        return super.setPowerOther(move,lastMove);
    }



    @Override
    public void updateUniqueMoves() {
        if (this.activePower == PowerIndex.POWER_1_SNEAK) {
            updateMudHit();
        }

        super.updateUniqueMoves();
    }



    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime) {
        if (!self.level().isClientSide()) {
            if (move == PowerIndex.SNEAK_ATTACK) {
                attackTargetId = chargeTime;
            }
        }
        return super.tryIntPower(move,forced,chargeTime);
    }

    @Override
    public void tickPower() {
        super.tickPower();

        if (this.self.level().isClientSide()) {
            if (this.getActivePower() == PowerIndex.SNEAK_ATTACK) {
                Roundabout.LOGGER.info("1");
                if (attackTimeDuring > 4) {
                    Roundabout.LOGGER.info("2");
                    tryPowerPacket(NONE);

                    if (getPlayerPos2() != PlayerPosIndex.OASIS_KICK) {
                        setPlayerPos2(PlayerPosIndex.OASIS_KICK);
                    }
                }
            }
        } else {
            byte pos2 = getPlayerPos2();
            if (getActivePower() != PowerIndex.SNEAK_ATTACK && pos2 == PlayerPosIndex.OASIS_KICK) {
                setPlayerPos2(PlayerPosIndex.NONE);
            }
            // possibly place this logic in the method where the tryPowerPacket(NONE) is received?
            // probably copy over tryPower method
        }

    }







    /** mud hit stuff */
    public void mudHitClient() {
        if (!onCooldown(PowerIndex.SKILL_1_SNEAK)) {
            this.setCooldown(PowerIndex.SKILL_1_SNEAK, 5);
            Roundabout.LOGGER.info("mud hit client");

            //((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1_SNEAK, true);
            tryPowerPacket(PowerIndex.POWER_1_SNEAK);
        }
    }

    public void mudHitCharge() {
        this.setAttackTimeDuring(0);
        this.setActivePower(PowerIndex.POWER_1_SNEAK);


        this.self.level().playSound(null, this.self.blockPosition(), ModSounds.IMPALE_CHARGE_EVENT, SoundSource.PLAYERS, 1F, (float) (0.97 + (Math.random() * 0.06)));
    }

    public void updateMudHit() {
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > 24) {
                doMudHit();
            }
        }
    }


    public void doMudHit() {
        int blockReach = 3;
        Roundabout.LOGGER.info("mudhit!");

        BlockHitResult hitBlock = this.getLookedBlock(blockReach);
        if (hitBlock.getType() == HitResult.Type.BLOCK) {
            Roundabout.LOGGER.info("hit block");
        } else {
            Roundabout.LOGGER.info("combat variant");
        }

        this.setActivePower(PowerIndex.NONE);
        this.setAttackTimeDuring(-10);
    }


















    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);
        $$0.putBoolean("fistsOut",fistsOut);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);
        if ($$0.contains("fistsOut")) {
            fistsOut = $$0.getBoolean("fistsOut");
        }
    }

    private BlockHitResult getLookedBlock(int reach) {
        Vec3 vec3d = this.getSelf().getEyePosition(0);
        Vec3 vec3d2 = this.getSelf().getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * reach, vec3d2.y * reach, vec3d2.z * reach);
        return this.getSelf().level().clip(new ClipContext(vec3d, vec3d3,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.getSelf()));
    }


    @Override
    public boolean setPowerAttack() {
        setAttack();
        return false;
    }

    public void spawnWallPunchParticles(Entity entity) {
        if (!this.self.level().isClientSide()) {
            if (!this.self.hasLineOfSight(entity)) {
                BlockHitResult hitBlock = this.getLookedBlock(3);
                Vec3 pos = hitBlock.getLocation();

                ((ServerLevel) this.self.level()).sendParticles(ParticleTypes.SPLASH, pos.x, pos.y, pos.z, 4, .01, .01, .01, .05);
                float pitch = (float) ((Math.random() * 0.1 - 0.5) + 1.0);
                this.self.level().playSound(null, hitBlock.getBlockPos(), SoundEvents.PLAYER_SPLASH, SoundSource.PLAYERS, 0.9f, pitch);
            }
        }
    }

    @Override
    public void setAttack(){
        if (HeatUtil.isArmsFrozen(self)){
            this.attackTimeMax= 12;
        } else {
            this.attackTimeMax= 7;
        }
        this.attackTimeDuring = 0;
        this.setAttackTime(0);
        setActivePower(PowerIndex.NONE);
        setActivePowerPhase((byte) 1);
        if (!self.level().isClientSide()) {
            Entity target = null;
            if (attackTargetId > 0) {
                target = self.level().getEntity(attackTargetId);
            }
            brawlPunchImpact(target);

            if (target != null) {
                spawnWallPunchParticles(target);
            }
        } else {
            Entity TE = getTargetEntityThroughWalls(self, 3, getBrawlPunchAngle());
            int id = 0;
            if (TE != null){
                id = TE.getId();
                HitResult hit = Minecraft.getInstance().hitResult;
                if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
                    Roundabout.LOGGER.info("block detected");
                }
            }
            tryIntPowerPacket(PowerIndex.ATTACK,id);
        }
    }

    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (self instanceof Player pl &&  ((IPlayerEntity)pl).roundabout$getAttackStrengthTicker() < 5) {
            return;
        }
        if (keyIsDown) {
            if (activePowerPhase == 0 && isBrawling() && !isBarraging()) {
                if (!isHoldingSneak()) {
                    this.tryPower(PowerIndex.ATTACK);
                } else if (self.onGround()) {
                    this.kickAttackClient();
                }
            }
        }
    }







    public void kickImpact(Entity entity) {
        if (!this.self.level().isClientSide) {
            if (entity != null) {
                if (entity.distanceTo(self) > 3) {
                    return;
                }
                Roundabout.LOGGER.info("reached kickImpact method");
                float pow;
                float knockbackStrength;
                pow = 1;

                knockbackStrength = 1.0f;
                if (entity instanceof LivingEntity LE && LE.isBlocking()) {
                    knockShield2(LE, 120);
                    knockbackStrength = 0.10f;
                }

                if (DamageHandler.StandDamageEntity(entity, pow, this.self)) {
                    //takeDeterminedKnockbackWithY2(this.self, entity, knockbackStrength);
                    takeKnockbackUp(entity, knockbackStrength);

                    this.self.level().playSound(null, this.self.blockPosition(), getBrawlPunchSound(), SoundSource.PLAYERS, 1F, (float) (1.15f + Math.random() * 0.1f));
                    hitParticles(entity);
                } else {
                    if (!this.self.level().isClientSide()) {
                        this.self.level().playSound(null, this.self.blockPosition(), ModSounds.MELEE_GUARD_SOUND_EVENT, SoundSource.PLAYERS, 1F, (float) (0.95f + Math.random() * 0.1f));
                    }
                }
            }
        }
    }
    public void doKickHit(){
        if (!self.level().isClientSide()) {
            Entity target = null;
            if (attackTargetId > 0) {
                target = self.level().getEntity(attackTargetId);
            }
            kickImpact(target);
        }
    }

    public void kickAttackClient() {
        if (!onCooldown(PowerIndex.SKILL_EXTRA)) {
            this.setCooldown(PowerIndex.SKILL_EXTRA, 20);


            this.tryPower(PowerIndex.SNEAK_ATTACK);
        }
    }

    public void kickAttack() {
        Roundabout.LOGGER.info("kick attack");

        this.attackTimeMax= 5;
        this.attackTimeDuring = 0;
        this.setAttackTime(0);
        setActivePowerPhase((byte) 1);
        setActivePower(PowerIndex.SNEAK_ATTACK);



        if (!self.level().isClientSide) {

            if (this.onCooldown(PowerIndex.SKILL_EXTRA)) {
                return;
            }

            Roundabout.LOGGER.info("server received");
            this.setCooldown(PowerIndex.SKILL_EXTRA, 20);

            if (getPlayerPos2() != PlayerPosIndex.OASIS_KICK) {
                setPlayerPos2(PlayerPosIndex.OASIS_KICK);
            }

            doKickHit();
        } else {
            Entity TE = getTargetEntity(self, 2.0F, getBrawlPunchAngle());
            int id = 0;
            if (TE != null){
                id = TE.getId();
            }
           tryIntPowerPacket(PowerIndex.SNEAK_ATTACK,id);
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

    @Override
    public boolean buttonInputGuard(boolean keyIsDown, Options options) {
        if (!this.isGuarding() && this.isBrawling()) {
            this.tryPower(PowerIndex.GUARD, true);
            tryPowerPacket(PowerIndex.GUARD);
            return true;
        }
        return false;
    }






/*
if (keyIsDown) {
    if (this.getAttackTime() >= this.getAttackTimeMax() ||
            (this.getActivePowerPhase() != this.getActivePowerPhaseMax())) {
        if (isBrawling() && !isBarraging()) {
            this.tryPower(PowerIndex.BARRAGE_CHARGE, true);
            tryPowerPacket(PowerIndex.BARRAGE_CHARGE);
        }
    }
}

 */


    @Override
    public void buttonInputBarrage(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (this.getAttackTime() >= this.getAttackTimeMax() ||
                    (this.getActivePowerPhase() != this.getActivePowerPhaseMax())) {
                if (isBrawling() && !isBarraging()) {
                    this.tryPower(PowerIndex.BARRAGE_CHARGE, true);
                    tryPowerPacket(PowerIndex.BARRAGE_CHARGE);
                }
            }
        }
    }

    public void spawnBarrageParticles() {
        if (!this.self.level().isClientSide) {

            BlockHitResult hitBlock = this.getLookedBlock(3);
            Vec3 pos = hitBlock.getLocation();
            ((ServerLevel) this.self.level()).sendParticles(ParticleTypes.SPLASH, pos.x, pos.y, pos.z, 4, .1, .1, .1, .05);
            float pitch = (float) ((Math.random() * 0.1 - 0.5) + 1.0);
            this.self.level().playSound(null, hitBlock.getBlockPos(), SoundEvents.PLAYER_SPLASH, SoundSource.PLAYERS, 0.9f, pitch);

        }
    }

    @Override
    public void barrageImpact(Entity entity, int hitNumber) {
        super.barrageImpact(entity, hitNumber);

        if (entity != null && !this.self.level().isClientSide && !this.self.hasLineOfSight(entity)) {
            spawnBarrageParticles();
        }

    }

    @Override
    public void standBarrageHit(){
        if (this.self instanceof Player){
            if (isPacketPlayer()){
                List<Entity> listE = getTargetEntityListThroughWalls(this.self,-1, 25);
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

            List<Entity> listE = getTargetEntityListThroughWalls(this.self,-1, 25);
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

        findDeflectables();
    }





    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        boolean powerOn = PowerTypes.hasStandActive(playerEntity);
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
            Entity TE = getTargetEntityThroughWalls(playerEntity, 3, getBrawlPunchAngle());
            float attackTimeMax = getAttackTimeMax();
            if (attackTimeMax > 0) {
                float attackTime = getAttackTime();
                float finalATime = attackTime / attackTimeMax;
                if (finalATime <= 1) {

                    if (getActivePowerPhase() == getActivePowerPhaseMax()) {
                        barTexture = 24;
                    } else if (TE != null && isBrawling()) {
                        barTexture = 12;
                    } else {
                        barTexture = 18;
                    }


                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
                    int finalATimeInt = Math.round(finalATime * 15);
                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, barTexture, finalATimeInt, 6);

                }
            }
            if (powerOn && isBrawling()) {
                if (TE != null) {
                    if (barTexture == 0) {
                        context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
                    }
                }
            }
        }
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

        if (!isHoldingSneak()) {
            if (fistsOut) {
                setSkillIcon(context, x, y, 1, StandIcons.SUIT_COMBAT_2, PowerIndex.SKILL_1);
            } else {
                setSkillIcon(context, x, y, 1, StandIcons.SUIT_COMBAT, PowerIndex.SKILL_1);
            }
        } else {
            setSkillIcon(context, x, y, 1, StandIcons.OASIS_MUD_HIT, PowerIndex.SKILL_1_SNEAK);
        }

    }

    public static final byte
            BASE = 1,
            AGOGO =2,
            BLANK =3,
            COUNTDOWN =4,
            INVERTED =5,
            MANGA =6,
            OPEN =7,
            TESTAMENTI =8,
            PS2 =9;

    @Override
    public List<Byte> getSkinList() {
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(BASE);
        $$1.add(OPEN);
        $$1.add(MANGA);
        $$1.add(AGOGO);
        $$1.add(BLANK);
        $$1.add(COUNTDOWN);
        $$1.add(INVERTED);
        $$1.add(TESTAMENTI);
        $$1.add(PS2);

        return $$1;
    }
    @Override
    public Component getSkinName(byte skinId) {
        return Component.translatable("skins.roundabout.oasis."+getSkinString(skinId));
    }
    public static String getSkinString(byte skinId) {
        return switch (skinId)
        {
            case AGOGO -> "agogo";
            case BLANK -> "blank";
            case COUNTDOWN -> "countdown";
            case INVERTED -> "inverted";
            case MANGA -> "manga";
            case OPEN -> "open";
            case TESTAMENTI -> "testamenti";
            case PS2 -> "ps2";
            default -> "base";
        };
    }

    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        switch(soundChoice) {
            case SoundIndex.SUMMON_SOUND -> {
                return ModSounds.SUMMON_OASIS_EVENT;
            }
        }
        return super.getSoundFromByte(soundChoice);
    }

    @Override
    public boolean isWip() {
        return true;
    }
    @Override
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.WHITE);
    }
    @Override
    public Component ifWipListDev(){
        return Component.literal(  "kepich").withStyle(ChatFormatting.WHITE);
    }
}
