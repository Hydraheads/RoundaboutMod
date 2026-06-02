package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.stand.MagiciansRedEntity;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.PWBigMeteorEntity;
import net.hydra.jojomod.entity.projectile.PWMeteorEntity;
import net.hydra.jojomod.entity.stand.PlanetWavesEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.index.StandFireType;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewDashPreset;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;

import static net.hydra.jojomod.entity.stand.PlanetWavesEntity.MANGA_SKIN;
import static net.hydra.jojomod.entity.stand.PlanetWavesEntity.PART_6_SKIN;
import static net.hydra.jojomod.entity.stand.PlanetWavesEntity.BLUE_SKIN;
import static net.hydra.jojomod.entity.stand.PlanetWavesEntity.PURPLE_SKIN;
import static net.hydra.jojomod.entity.stand.PlanetWavesEntity.GREEN_SKIN;

public class PowersPlanetWaves extends NewDashPreset {
    public PowersPlanetWaves(LivingEntity self) {super(self);}

    @Override
    public StandEntity getNewStandEntity(){
        return ModEntities.PLANET_WAVES.create(this.getSelf().level());
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersPlanetWaves(entity);
    }

    @Override
    public boolean isWip(){
        return true;
    }
    @Override
    public Component ifWipListDevStatus(){
        return Component.translatable(  "roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);
    }
    @Override
    public Component ifWipListDev(){
        return Component.literal(  "Lloyd10").withStyle(ChatFormatting.YELLOW);
    }
    @Override
    public List<Byte> getSkinList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(PlanetWavesEntity.PART_6_SKIN);
        $$1.add(PlanetWavesEntity.MANGA_SKIN);
        if (this.getSelf() instanceof Player PE){
            byte Level = ((IPlayerEntity)PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser)PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (Level > 1 || bypass){
                $$1.add(PlanetWavesEntity.BLUE_SKIN);
                $$1.add(PlanetWavesEntity.PURPLE_SKIN);
                $$1.add(PlanetWavesEntity.GREEN_SKIN);
            }/* if (Level > 2 || bypass){
                $$1.add(MagiciansRedEntity.OVA_SKIN);
                $$1.add(MagiciansRedEntity.SIDEKICK);
            } if (Level > 3 || bypass){
                $$1.add(MagiciansRedEntity.PURPLE_SKIN);
                $$1.add(MagiciansRedEntity.PURPLE_ABLAZE);
            } if (Level > 4 || bypass){
                $$1.add(MagiciansRedEntity.GREEN_SKIN);
                $$1.add(MagiciansRedEntity.GREEN_ABLAZE);
            } if (Level > 5 || bypass){
                $$1.add(MagiciansRedEntity.BLUE_SKIN);
                $$1.add(MagiciansRedEntity.BLUE_ABLAZE);
                $$1.add(MagiciansRedEntity.BLUE_ACE_SKIN);
                $$1.add(MagiciansRedEntity.SKELETAL);
            } if (Level > 6 || bypass){
                $$1.add(MagiciansRedEntity.JOJONIUM);
                $$1.add(MagiciansRedEntity.JOJONIUM_ABLAZE);
                $$1.add(MagiciansRedEntity.MAGMA_SKIN);
                $$1.add(MagiciansRedEntity.DEBUT_SKIN);
                $$1.add(MagiciansRedEntity.BETA);
            } if (((IPlayerEntity)PE).roundabout$getUnlockedBonusSkin() || bypass){
                $$1.add(MagiciansRedEntity.DREAD_BEAST_SKIN);
                $$1.add(MagiciansRedEntity.DREAD_SKIN);
                $$1.add(MagiciansRedEntity.DREAD_ABLAZE);
            }*/
        }
        return $$1;
    }

    @Override public Component getSkinName(byte skinId) {
        switch (skinId)
        {
            case PART_6_SKIN -> {return Component.translatable("skins.roundabout.planet_waves.base");}
            case MANGA_SKIN  -> {return Component.translatable("skins.roundabout.planet_waves.manga");}
            case BLUE_SKIN  -> {return Component.translatable("skins.roundabout.planet_waves.blue");}
            case PURPLE_SKIN  -> {return Component.translatable("skins.roundabout.planet_waves.purple");}
            case GREEN_SKIN  -> {return Component.translatable("skins.roundabout.planet_waves.green");}
        }
        return Component.translatable("skins.roundabout.planet_waves.base");

    }
    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {

        setSkillIcon(context, x, y, 1, StandIcons.PLANET_WAVES_METEOR_SHOWER, PowerIndex.SKILL_1);
        setSkillIcon(context, x, y, 2, StandIcons.PLANET_WAVES_BIG_METEOR, PowerIndex.SKILL_2);
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        if(canExecuteMoveWithLevel(StandTargetingLevel())){
            if (!instandtargeting()) {
                setSkillIcon(context, x, y, 4, StandIcons.PLANET_WAVES_STAND_TARGETING, PowerIndex.SKILL_4);
            } else setSkillIcon(context, x, y, 4, StandIcons.PLANET_WAVES_STAND_RETRIEVING, PowerIndex.SKILL_4);
        }else setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.SKILL_4);
        if(canExecuteMoveWithLevel(MeteorTrackingLevel())) {
            if (isHoldingSneak())

                setSkillIcon(context, x, y, 4, StandIcons.PLANET_WAVES_METEOR_TRACKING, PowerIndex.SKILL_4_SNEAK);
        }else setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.SKILL_4_SNEAK);
        if (isHoldingSneak())

            setSkillIcon(context, x, y, 2, StandIcons.PLANET_WAVES_DESINTEGRATION, PowerIndex.SKILL_2_SNEAK);
    }

    @Override
    public boolean isStandEnabled() {
        return ClientNetworking.getAppropriateConfig().PlanetWavesSettings.enablePlanetWaves;

    }

    public int StandTargetingLevel(){
        return 3;
    }
    public int MeteorTrackingLevel(){
        return 4;
    }
    @Override
    public byte getMaxLevel(){
        return 4;
    }

    @Override
    public int getExpForLevelUp(int currentLevel){
        int amt;
        if (currentLevel == 1) {
            amt = 100;
        } else if (currentLevel == 2){
            amt = 200;
        } else {
            amt = 400;
        }
        amt= (int) (amt*(getLevelMultiplier()));
        return amt;
    }

    @Override
    public void levelUp(){
        if (!this.getSelf().level().isClientSide() && this.getSelf() instanceof Player PE){
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            byte level = ipe.roundabout$getStandLevel();
            if (level == 4) {
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.max.both").
                        withStyle(ChatFormatting.AQUA), true);
            } else { if(level== 3){
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.both").
                        withStyle(ChatFormatting.AQUA), true);
                }else ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.skins").
                    withStyle(ChatFormatting.AQUA), true);
            }
        }
        super.levelUp();
    }

    private boolean targetingstand = false;
    private boolean tracking = false;
    private Vec3 standTargetPos = null;
    private Vec3 standTargetLook = null;
    public boolean instandtargeting(){
        return this.targetingstand;
    }
    public boolean inmeteortracking(){
        return this.tracking;
    }


    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move) {
            case PowerIndex.POWER_1 -> { // Meteor Shower
                meteorshower();
                return true;
            }
            case PowerIndex.POWER_2 -> { // Big Meteor
                bigmeteor();
                return true;
            }
            case PowerIndex.POWER_4 -> { // Stand Targeting or Stand Retrieving
                if (!instandtargeting()) {
                    attemptStandTargeting();
                } else {
                    attemptStandRetrieving();
                }
                return true;
            }
            case PowerIndex.POWER_4_SNEAK -> { // Meteor Tracking
                if (!inmeteortracking()) {
                    attemptMeteorTracking();
                } else {
                    attemptMeteorNotTracking();
                }
                return true;
            }
            case PowerIndex.POWER_2_SNEAK -> { // Meteor Dissapearence
                meteorDisappearance();
                return true;
            }
        }
        return super.setPowerOther(move, lastMove);
    }
    public void attemptStandTargeting(){
        if(canExecuteMoveWithLevel(StandTargetingLevel())) {
            standtargeting();
        }
    }
    public void attemptStandRetrieving(){
        if(canExecuteMoveWithLevel(StandTargetingLevel())) {
            usertargeting();
        }
    }
    public void attemptMeteorTracking(){
        if(canExecuteMoveWithLevel(MeteorTrackingLevel())) {
            meteortracking();
        }
    }
    public void attemptMeteorNotTracking(){
        if(canExecuteMoveWithLevel(MeteorTrackingLevel())) {
            meteornottracking();
        }
    }
    private void meteorshower() {
        if (this.onCooldown(PowerIndex.SKILL_1)) return;
        if (this.self.level().dimension() == Level.NETHER) return;

        Level level = this.self.level();
        if (level.isClientSide()) return;

        Vec3 eyePos = this.self.getEyePosition(1.0F);
        Vec3 lookVec = this.self.getViewVector(1.0F).normalize();

        double spawnDistance = ClientNetworking.getAppropriateConfig()
                .PlanetWavesSettings.meteorshowerDistance;

        Vec3 spawnPos = eyePos.add(lookVec.scale(spawnDistance));

        if (this.self.level().dimension() != Level.END) {
            if (spawnPos.y < this.self.getY() + 5.0) {
                spawnPos = new Vec3(
                        spawnPos.x,
                        this.self.getY() + 5.0,
                        spawnPos.z
                );
            }
        }
        Vec3 targetPos;

        if (instandtargeting() && standTargetPos != null) {

            targetPos = standTargetPos;

        } else if (this.self.isShiftKeyDown()) {


            targetPos = new Vec3(
                    this.self.getX(),
                    this.self.getY() - 0.5D,
                    this.self.getZ()
            );

        } else {


            targetPos = this.self.getEyePosition(1.0F);
        }

        Vec3 direction = targetPos.subtract(spawnPos).normalize();

        PWMeteorEntity meteor = new PWMeteorEntity(this.self, level);
        meteor.setTargetPos(targetPos);
        meteor.setUser(this.self);
        meteor.setOwner(this.self);
        meteor.setTrackingUser(inmeteortracking());

        meteor.absMoveTo(spawnPos.x, spawnPos.y, spawnPos.z);
        meteor.shoot(direction.x, direction.y, direction.z, 1.8F, 0.0F);

        meteor.setChain(0, true);

        level.addFreshEntity(meteor);

        level.playSound(null, this.self.blockPosition(),
                ModSounds.PLANET_WAVES_METEOR_SHOWER_EVENT,
                net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);

        int cooldown = ClientNetworking.getAppropriateConfig()
                .PlanetWavesSettings.meteorshowerCooldown;

        this.setCooldown(PowerIndex.SKILL_1, cooldown);
        S2CPacketUtil.sendCooldownSyncPacket(
                ((ServerPlayer)this.getSelf()),
                PowerIndex.SKILL_1,
                cooldown
        );
    }

    private static class ScheduledMeteor {
        int delay;
        Vec3 spawnPos;
        Vec3 targetPos;

        ScheduledMeteor(int delay, Vec3 spawnPos, Vec3 targetPos) {
            this.delay = delay;
            this.spawnPos = spawnPos;
            this.targetPos = targetPos;
        }
    }

    private final List<ScheduledMeteor> meteorQueue = new java.util.ArrayList<>();
    public void tick() {
        if (self.level().isClientSide()) return;

        if (meteorQueue.isEmpty()) return;

        for (int i = 0; i < meteorQueue.size(); i++) {
            ScheduledMeteor m = meteorQueue.get(i);
            m.delay--;

            if (m.delay <= 0) {
                spawnMeteor(m.spawnPos, m.targetPos);
                meteorQueue.remove(i);
                i--;
            }
        }
    }
    private void spawnMeteor(Vec3 spawnPos, Vec3 targetPos) {
        Level level = this.self.level();
        if (level.isClientSide()) return;

        Vec3 direction = targetPos.subtract(spawnPos).normalize();

        PWMeteorEntity meteor = new PWMeteorEntity(this.self, level);
        meteor.setTargetPos(targetPos);
        meteor.setUser(this.self);
        meteor.setOwner(this.self);

        meteor.absMoveTo(spawnPos.x, spawnPos.y, spawnPos.z);
        meteor.shoot(direction.x, direction.y, direction.z, 1.8F, 0.0F);

        level.addFreshEntity(meteor);
    }

    private void bigmeteor() {
        Level level = this.self.level();
        if (level.isClientSide()) return;
        if (this.self.level().dimension() == Level.NETHER) return;
        if (this.onCooldown(PowerIndex.SKILL_2)) return;

        Vec3 eyePos = this.self.getEyePosition(1.0F);
        Vec3 lookVec = this.self.getViewVector(1.0F).normalize();

        double spawnDistance = ClientNetworking.getAppropriateConfig()
                .PlanetWavesSettings.bigmeteorDistance;

        Vec3 spawnPos = eyePos.add(lookVec.scale(spawnDistance));


        if (this.self.level().dimension() != Level.END) {
            if (spawnPos.y < this.self.getY() + 5.0) {
                spawnPos = new Vec3(
                        spawnPos.x,
                        this.self.getY() + 5.0,
                        spawnPos.z
                );
            }
        }

        Vec3 targetPos;

        if (instandtargeting() && standTargetPos != null) {

            targetPos = standTargetPos;

        } else if (this.self.isShiftKeyDown()) {

            targetPos = new Vec3(
                    this.self.getX(),
                    this.self.getY() - 1.0D,
                    this.self.getZ()
            );

        } else {

            targetPos = this.self.getEyePosition(1.0F);
        }

        Vec3 direction = targetPos.subtract(spawnPos).normalize();

        PWBigMeteorEntity meteor = new PWBigMeteorEntity(this.self, level);

        meteor.absMoveTo(spawnPos.x, spawnPos.y, spawnPos.z);


        if (!level.noCollision(meteor)) {
            return;
        }

        meteor.setTargetPos(targetPos);
        meteor.setUser(this.self);
        meteor.setOwner(this.self);

        meteor.shoot(direction.x, direction.y, direction.z, 1.8F, 0.0F);

        level.addFreshEntity(meteor);

        int cooldown = ClientNetworking.getAppropriateConfig()
                .PlanetWavesSettings.bigmeteorCooldown;

        this.setCooldown(PowerIndex.SKILL_2, cooldown);

        S2CPacketUtil.sendCooldownSyncPacket(
                ((ServerPlayer) this.getSelf()),
                PowerIndex.SKILL_2,
                cooldown
        );

        level.playSound(
                null,
                this.self.blockPosition(),
                ModSounds.PLANET_WAVES_BIG_METEOR_EVENT,
                net.minecraft.sounds.SoundSource.PLAYERS,
                1.0F,
                1.0F
        );
    }


    private void standtargeting() {
        Level level = this.self.level();

        Vec3 eyePos = this.self.getEyePosition(1.0F);
        Vec3 lookVec = this.self.getViewVector(1.0F);

        Vec3 endPos = eyePos.add(lookVec.scale(128.0D));

        ClipContext clipContext = new ClipContext(
                eyePos,
                endPos,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                this.self
        );

        BlockHitResult hitResult = this.self.level().clip(clipContext);


        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = hitResult.getBlockPos();


            if (!this.self.level().getBlockState(pos).isAir()) {


                targetingstand = true;
                if (self instanceof ServerPlayer pl) {
                    S2CPacketUtil.sendGenericIntToClientPacket(
                            pl,
                            PacketDataIndex.S2C_INT_STAND_MODE,
                            1
                    );
                }

                this.standTargetPos = hitResult.getLocation();

                level.playSound(
                        null,
                        this.self.blockPosition(),
                        ModSounds.PLANET_WAVES_TARGET_EVENT,
                        net.minecraft.sounds.SoundSource.PLAYERS,
                        1.0F,
                        1.0F
                );
                if (!level.isClientSide()) {
                    this.setCooldown(
                            PowerIndex.SKILL_4,
                            ClientNetworking.getAppropriateConfig()
                                    .PlanetWavesSettings.standtargetingCooldown
                    );
                }
            }
        }
    }

    private void usertargeting() {
        Level level = this.self.level();

        targetingstand = false;
        if (self instanceof ServerPlayer pl) {
            S2CPacketUtil.sendGenericIntToClientPacket(
                    pl,
                    PacketDataIndex.S2C_INT_STAND_MODE,
                    0
            );
        }
        standTargetPos = null;

        if (!level.isClientSide()) {
            this.setCooldown(
                    PowerIndex.SKILL_4,
                    ClientNetworking.getAppropriateConfig()
                            .PlanetWavesSettings.usertargetingCooldown
            );
        }

        level.playSound(
                null,
                this.self.blockPosition(),
                ModSounds.PLANET_WAVES_TARGET_EVENT,
                net.minecraft.sounds.SoundSource.PLAYERS,
                1.0F,
                1.0F
        );
    }
    private void meteortracking() {
        tracking = true;
        for (PWMeteorEntity meteor : this.self.level().getEntitiesOfClass(
                PWMeteorEntity.class,
                this.self.getBoundingBox().inflate(256))) {

            if (meteor.getStandUser() == this.self) {
                meteor.setTrackingUser(true);
            }
        }
        if (!isClient() && this.self instanceof ServerPlayer PE) {
                PE.displayClientMessage(Component.translatable("text.roundabout.planet_waves.meteor_tracking_message").withStyle(ChatFormatting.RED), true);
        }
        if (self instanceof ServerPlayer pl) {
            S2CPacketUtil.sendGenericIntToClientPacket(
                    pl,
                    PacketDataIndex.S2C_INT_STAND_MODE,
                    2
            );
        }
    }

    private void meteornottracking() {
        tracking = false;

        for (PWMeteorEntity meteor : this.self.level().getEntitiesOfClass(
                PWMeteorEntity.class,
                this.self.getBoundingBox().inflate(256))) {

            if (meteor.getStandUser() == this.self) {
                meteor.setTrackingUser(false);
            }
        }

        if (!isClient() && this.self instanceof ServerPlayer PE) {
            PE.displayClientMessage(
                    Component.translatable(
                                    "text.roundabout.planet_waves.meteor_tracking_message_off")
                            .withStyle(ChatFormatting.RED),
                    true
            );
        }

        if (self instanceof ServerPlayer pl) {
            S2CPacketUtil.sendGenericIntToClientPacket(
                    pl,
                    PacketDataIndex.S2C_INT_STAND_MODE,
                    0
            );
        }
    }
    private void meteorDisappearance() {
        if (this.self.level().isClientSide()) return;

        Level level = this.self.level();

        boolean foundMeteor = false;

        List<PWBigMeteorEntity> meteors = level.getEntitiesOfClass(
                PWBigMeteorEntity.class,
                this.self.getBoundingBox().inflate(256)
        );

        for (PWBigMeteorEntity meteor : meteors) {

            if (meteor.getStandUser() == this.self) {

                meteor.forceDisintegrate();
                foundMeteor = true;

                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(
                            ParticleTypes.SMOKE,
                            meteor.getX(), meteor.getY(), meteor.getZ(),
                            10,
                            0.2, 0.2, 0.2,
                            0.01
                    );
                }
            }
        }

        if (foundMeteor) {
            level.playSound(
                    null,
                    this.self.blockPosition(),
                    ModSounds.PLANET_WAVES_DISINTEGRATION_EVENT,
                    SoundSource.PLAYERS,
                    1.5F,
                    1.0F
            );
        }
    }
    @Override
    public void powerActivate(PowerContext context) {
        switch (context) {

            // Skill 1 → Meteor Shower
            case SKILL_1_NORMAL, SKILL_1_CROUCH, SKILL_1_GUARD, SKILL_1_CROUCH_GUARD -> {
                this.tryPowerPacket(PowerIndex.POWER_1);
            }

            // Skill 2 → Big Meteor
            case SKILL_2_NORMAL, SKILL_2_GUARD, SKILL_2_CROUCH_GUARD -> {
                this.tryPowerPacket(PowerIndex.POWER_2);
            }

            // Skill 3 → Dash
            case SKILL_3_NORMAL, SKILL_3_GUARD, SKILL_3_CROUCH, SKILL_3_CROUCH_GUARD -> {
                dash();
            }

            // Skill 4 → Stand Targeting or Stand Retrieving
            case SKILL_4_NORMAL, SKILL_4_GUARD, SKILL_4_CROUCH_GUARD -> {
                this.tryPowerPacket(PowerIndex.POWER_4);

            }
            case SKILL_4_CROUCH-> {
                this.tryPowerPacket(PowerIndex.POWER_4_SNEAK);

            }
            case SKILL_2_CROUCH-> {
                this.tryPowerPacket(PowerIndex.POWER_2_SNEAK);

            }
        }
    }
    @Override
    public void tickMobAI(LivingEntity attackTarget) {

        if (attackTarget != null
                && attackTarget.isAlive()
                && !this.isDazed(this.getSelf())) {

            double dist = attackTarget.distanceTo(this.getSelf());

            if (this.getActivePower() != PowerIndex.NONE || dist <= 12) {
                rotateMobHead(attackTarget);
            }

            if (!this.onCooldown(PowerIndex.SKILL_1)
                    && activePower == PowerIndex.NONE
                    && !this.self.isUnderWater()) {

                ((StandUser) this.getSelf())
                        .roundabout$tryPower(PowerIndex.POWER_1, true);
            }

            else if (dist > 8
                    && dist <= 30
                    && !this.onCooldown(PowerIndex.SKILL_2)
                    && activePower == PowerIndex.NONE
                    && !this.self.isUnderWater()) {

                double RNG = Math.random();

                if (RNG < 0.2) {
                    ((StandUser) this.getSelf())
                            .roundabout$tryPower(PowerIndex.POWER_2, true);
                }
            }

            if (dist <= 7
                    && (activePower == PowerIndex.NONE
                    || activePower == PowerIndex.ATTACK)) {

                Entity targetEntity = getTargetEntity(this.self, -1);

                if (targetEntity != null && targetEntity.is(attackTarget)) {

                    if (this.attackTimeDuring <= -1) {

                        double RNG = Math.random();

                        if (RNG < 0.25
                                && !this.onCooldown(PowerIndex.SKILL_2)
                                && activePower == PowerIndex.NONE) {

                            ((StandUser) this.getSelf())
                                    .roundabout$tryPower(PowerIndex.POWER_2, true);

                        } else {

                            ((StandUser) this.getSelf())
                                    .roundabout$tryPower(PowerIndex.ATTACK, true);
                        }
                    }
                }
            }

        }
    }
//summon minecraft:creeper ~ ~ ~ {roundabout.StandDisc:{id:"roundabout:max_planet_waves_disc",tag:{Memory:{Pose:0b,Skin:1b}},Count:1b}}
    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.PLANET_WAVES_SUMMON_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }
    @Override
    public void serverQueried() {
        if (self instanceof ServerPlayer pl) {
            S2CPacketUtil.sendGenericIntToClientPacket(
                    pl,
                    PacketDataIndex.S2C_INT_STAND_MODE,
                    targetingstand ? 1 : 0
            );
        }
    }

    @Override
    public void clientIntUpdated(int integer) {
        targetingstand = integer == 1;
    }
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypass) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 80, 0, "ability.roundabout.meteor_shower",
                "instruction.roundabout.press_skill", StandIcons.PLANET_WAVES_METEOR_SHOWER, 1, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 99, 0, "ability.roundabout.big_meteor",
                "instruction.roundabout.press_skill", StandIcons.PLANET_WAVES_BIG_METEOR,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 20, topPos + 118, 0, "ability.roundabout.forced_dissintegration",
                "instruction.roundabout.press_skill_crouch", StandIcons.PLANET_WAVES_DESINTEGRATION,2,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 80, 0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 99, 3, "ability.roundabout.stand_targeting",
                "instruction.roundabout.press_skill", StandIcons.PLANET_WAVES_STAND_TARGETING, 4, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 39, topPos + 118, 3, "ability.roundabout.stand_retrieving",
                "instruction.roundabout.press_skill", StandIcons.PLANET_WAVES_STAND_RETRIEVING, 4, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 80, 4, "ability.roundabout.meteor_tracking",
                "instruction.roundabout.press_skill_crouch", StandIcons.PLANET_WAVES_METEOR_TRACKING, 4, level, bypass));
        $$1.add(drawSingleGUIIcon(context, 18, leftPos + 58, topPos + 99, 0, "ability.roundabout.desintegration",
                "instruction.roundabout.passive", StandIcons.PLANET_WAVES_DESINTEGRATION, 4, level, bypass));

        return $$1;


    }
    public byte getFireColor(){
        byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();

        return switch (skn) {
            /*case PlanetWavesEntity.BLUE_SKIN, PlanetWavesEntity.BLUE_ACE_SKIN, PlanetWavesEntity.BLUE_ABLAZE, PlanetWavesEntity.SKELETAL -> StandFireType.BLUE.id;
            case PlanetWavesEntity.PURPLE_SKIN, PlanetWavesEntity.PURPLE_ABLAZE -> StandFireType.PURPLE.id;
            case PlanetWavesEntity.GREEN_SKIN, PlanetWavesEntity.GREEN_ABLAZE -> StandFireType.GREEN.id;
            case PlanetWavesEntity.DREAD_SKIN, PlanetWavesEntity.DREAD_ABLAZE, PlanetWavesEntity.DREAD_BEAST_SKIN -> StandFireType.DREAD.id;
            case PlanetWavesEntity.JOJONIUM, PlanetWavesEntity.JOJONIUM_ABLAZE -> StandFireType.CREAM.id;*/
            case PlanetWavesEntity.PURPLE_SKIN -> StandFireType.PURPLE.id;
            case PlanetWavesEntity.BLUE_SKIN -> StandFireType.BLUE.id;
            case PlanetWavesEntity.MANGA_SKIN -> StandFireType.CREAM.id;
            default -> StandFireType.ORANGE.id;
        };
    }
    public void createStandFire(BlockPos pos){
        if (pos != null && tryPlaceBlock(pos)){
            createStandFire(pos);
        }
    }

    public float getFireballDamage(Entity entity){
        return 4F;
    }

    public float getBigMeteorDamage(Entity entity){
        return 8F;
    }

    public SimpleParticleType getFlameParticle(){
        byte skn = ((StandUser)this.getSelf()).roundabout$getStandSkin();

        return switch (skn) {
            //case PlanetWavesEntity.BLUE_SKIN, PlanetWavesEntity.BLUE_ACE_SKIN, PlanetWavesEntity.BLUE_ABLAZE, PlanetWavesEntity.SKELETAL -> ModParticles.BLUE_FLAME;
            // case PlanetWavesEntity.PURPLE_SKIN, PlanetWavesEntity.PURPLE_ABLAZE -> ModParticles.PURPLE_FLAME;
            // case PlanetWavesEntity.GREEN_SKIN, PlanetWavesEntity.GREEN_ABLAZE -> ModParticles.GREEN_FLAME;
            // case PlanetWavesEntity.DREAD_SKIN, PlanetWavesEntity.DREAD_ABLAZE, PlanetWavesEntity.DREAD_BEAST_SKIN -> ModParticles.DREAD_FLAME;
            //case PlanetWavesEntity.JOJONIUM, PlanetWavesEntity.JOJONIUM_ABLAZE -> ModParticles.CREAM_FLAME;
            case PlanetWavesEntity.GREEN_SKIN-> ModParticles.GREEN_FLAME;
            case PlanetWavesEntity.PURPLE_SKIN -> ModParticles.PURPLE_FLAME;
            case PlanetWavesEntity.BLUE_SKIN -> ModParticles.BLUE_FLAME;
            case PlanetWavesEntity.MANGA_SKIN -> ModParticles.CREAM_FLAME;
            default -> ModParticles.ORANGE_FLAME;
        };
    }
    }


