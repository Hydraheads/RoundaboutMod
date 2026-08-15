package net.hydra.jojomod.stand.powers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPermaCasting;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.stand.PurpleHazeEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.hydra.jojomod.event.PermanentZoneCastInstance;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.CooldownInstance;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.DustParticleOptions;
import org.joml.Vector3f;
import net.minecraft.server.level.ServerLevel;


import java.util.Arrays;
import java.util.List;

public class PowersPurpleHaze extends NewPunchingStand {
    public PowersPurpleHaze(LivingEntity self) {
        super(self);
    }

    @Override
    public Component getSkinName(byte skinId) {
        switch (skinId) {
            case PurpleHazeEntity.ANIME -> {
                return Component.translatable("skins.roundabout.purple_haze.anime");
            }
            //case PurpleHazeEntity.MIG_PLAGUE -> {return Component.translatable("skins.roundabout.purple_haze.mig_plague");}
            default -> {
                return Component.translatable("skins.roundabout.purple_haze.anime");
            }
        }
    }

    public int getLeapLevel() {
        return 3;
    }

    public int bonusLeapCount = -2;
    public int spacedJumpTime = -1;

    @Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                PurpleHazeEntity.ANIME,
                PurpleHazeEntity.MIG_PLAGUE
        );
    }

    @Override
    public boolean canSummonStand() {
        return true;
    }

    @Override
    public boolean isMiningStand() {
        return true;
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersPurpleHaze(entity);
    }

    @Override
    public int getMaxGuardPoints() {
        return 15;
    }

    @Override
    public StandEntity getNewStandEntity() {
        return ModEntities.PURPLE_HAZE.create(this.getSelf().level());
    }

    public void tryToDashClient() {
        if (vaultOrFallBraceFails()) {
            dash();
        }
    }
    @Override
    public boolean isServerControlledCooldown(byte num){
        if (num == PowerIndex.SKILL_1 || num == PowerIndex.SKILL_1_SNEAK || num == PowerIndex.SKILL_2 ){
            return true;
        }
        return super.isServerControlledCooldown(num);
    }
    public boolean holdDownClick = false;
    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (!consumeClickInput) {

            if (holdDownClick) {
                if (!keyIsDown) {
                    if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
                        int atd = this.getAttackTimeDuring();
                        this.tryIntPower(PowerIndex.SNEAK_ATTACK, true, atd);
                        tryIntPowerPacket(PowerIndex.SNEAK_ATTACK, atd);
                    }
                    holdDownClick = false;
                }
            } else {
                if (keyIsDown) {
                    if (!isHoldingSneak()) {
                        super.buttonInputAttack(keyIsDown, options);
                    } else {
                        if (this.canAttack()) {
                            this.tryPower(PowerIndex.SNEAK_ATTACK_CHARGE, true);
                            holdDownClick = true;
                            tryPowerPacket(PowerIndex.SNEAK_ATTACK_CHARGE);
                        } else {
                            super.buttonInputAttack(keyIsDown, options);
                        }
                    }
                }
            }
        } else {
            if (!keyIsDown) {
                consumeClickInput = false;
            }
        }
    }

    public void tryToStandLeapClient() {
        if (vaultOrFallBraceFails()) {
            if (this.getSelf().onGround()) {
                boolean jojoveinLikeKeys = !ClientNetworking.getAppropriateConfig().generalStandSettings.standJumpAndDashShareCooldown;
                if ((jojoveinLikeKeys && !this.onCooldown(PowerIndex.SKILL_3)) ||
                        (!jojoveinLikeKeys && !this.onCooldown(PowerIndex.GLOBAL_DASH))) {
                    if (canExecuteMoveWithLevel(getLeapLevel())) {
                        if (jojoveinLikeKeys) {
                            this.setCooldown(PowerIndex.SKILL_3, ClientNetworking.getAppropriateConfig().generalStandSettings.standJumpCooldown);
                        } else {
                            this.setCooldown(PowerIndex.GLOBAL_DASH, ClientNetworking.getAppropriateConfig().generalStandSettings.standJumpCooldown);
                            this.setCooldown(PowerIndex.SKILL_1_SNEAK, 10);
                        }
                        bonusLeapCount = 3;
                        bigLeap(this.getSelf(), 20, 1);
                        ((StandUser) this.getSelf()).roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
                        ((StandUser) this.getSelf()).roundabout$setLeapIntentionally(true);
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.SNEAK_MOVEMENT, true);
                        tryPowerPacket(PowerIndex.SNEAK_MOVEMENT);
                    }
                }
            }
        }
    }

    public void applyLeapCooldowns() {

    }

    public void bigLeap(LivingEntity entity, float range, float mult) {
        Vec3 vec3d = entity.getEyePosition(1);
        Vec3 vec3d2 = entity.getViewVector(1);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        BlockHitResult blockHit = entity.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));

        double mag = this.getSelf().getPosition(1).distanceTo(
                new Vec3(blockHit.getLocation().x, blockHit.getLocation().y, blockHit.getLocation().z)) * 0.75 + 1;
        Vec3 vec3 = new Vec3(
                (blockHit.getLocation().x - this.getSelf().getX()) / mag,
                (blockHit.getLocation().y - this.getSelf().getY()) / mag,
                (blockHit.getLocation().z - this.getSelf().getZ()) / mag
        );
        Direction gravD = ((IGravityEntity) this.self).roundabout$getGravityDirection();
        if (gravD != Direction.DOWN) {
            vec3 = RotationUtil.vecWorldToPlayer(vec3, gravD);
        }
        vec3 = new Vec3(
                vec3.x * mult,
                0.6 + Math.max(vec3.y, 0) * mult,
                vec3.z * mult
        );

        MainUtil.takeUnresistableKnockbackWithY2(this.getSelf(),
                vec3.x,
                vec3.y,
                vec3.z
        );

    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move) {
            case PowerIndex.POWER_1 -> { // Distortion
                attemptDistortion();
            }
            case PowerIndex.POWER_1_SNEAK -> { // Distortion Mode Change
                attemptDistortionModeChange();
            }

            case PowerIndex.SNEAK_ATTACK_CHARGE -> attemptThrowPod();
        }
        return super.setPowerOther(move, lastMove);
    }

    @Override
    public void powerActivate(PowerContext context) {
        switch (context) {
            case SKILL_1_NORMAL, SKILL_1_GUARD -> {
                this.tryPowerPacket(PowerIndex.POWER_1);
            }
            case SKILL_1_CROUCH,SKILL_1_CROUCH_GUARD -> {
                this.tryPowerPacket(PowerIndex.POWER_1_SNEAK);
            }
            case SKILL_3_NORMAL -> tryToDashClient();
            case SKILL_3_CROUCH -> tryToStandLeapClient();
        }
    }
    @Override
    public void tickPermaCast() {
        if (self == null) {
            return;
        }

        if (self.level().isClientSide) {
            return;
        }

        if (!purpleHazeFieldActive || purpleHazeFieldPosition == null) {
            return;
        }

        IPermaCasting permaCasting =
                (IPermaCasting) self.level();

        if (!permaCasting.roundabout$isPermaCastingEntity(self)) {
            return;
        }

        Level level = self.level();
        ServerLevel serverLevel = (ServerLevel) level;

        purpleHazeFieldTicks--;

        if (purpleHazeFieldTicks <= 0) {
            deactivatePurpleHazeField();
            return;
        }


        double x = purpleHazeFieldPosition.x;
        double y = purpleHazeFieldPosition.y;
        double z = purpleHazeFieldPosition.z;

        if (purpleHazeFieldDistortionMode) {

            serverLevel.sendParticles(
                    ModParticles.PURPLE_HAZE_SMOKE,
                    x,
                    y + 1.0,
                    z,
                    30,
                    PURPLE_HAZE_RANGE / 2,
                    1.5,
                    PURPLE_HAZE_RANGE / 2,
                    0.01
            );

            serverLevel.sendParticles(
                    new DustParticleOptions(
                            new Vector3f(0.0F, 0.0F, 0.0F),
                            1.5F
                    ),
                    x,
                    y + 1.0,
                    z,
                    45,
                    PURPLE_HAZE_RANGE / 2,
                    1.5,
                    PURPLE_HAZE_RANGE / 2,
                    0.02
            );

        } else {

            serverLevel.sendParticles(
                    ModParticles.PURPLE_HAZE_SMOKE,
                    x,
                    y + 1.0,
                    z,
                    30,
                    PURPLE_HAZE_RANGE / 2,
                    1.5,
                    PURPLE_HAZE_RANGE / 2,
                    0.01
            );
        }

        int startupDelay;

        if (purpleHazeFieldDistortionMode) {
            startupDelay = DISTORTION_FIELD_DURATION - 40;
        } else {
            startupDelay = PURPLE_HAZE_FIELD_DURATION - 40;
        }

        if (purpleHazeFieldTicks > startupDelay) {
            return;
        }

        List<Entity> entities = MainUtil.genHitbox(
                level,
                x,
                y,
                z,
                PURPLE_HAZE_RANGE,
                PURPLE_HAZE_RANGE,
                PURPLE_HAZE_RANGE
        );

        for (Entity entity : entities) {

            if (!(entity instanceof LivingEntity living)) {
                continue;
            }
            if(purpleHazeFieldDistortionMode){
                living.addEffect(
                        new MobEffectInstance(
                                ModEffects.DISTORTION_VIRUS,
                                300
                        )
                );
            }else if(!purpleHazeFieldDistortionMode) {
                living.addEffect(
                        new MobEffectInstance(
                                ModEffects.HAZE_VIRUS,
                                300
                        )
                );
            }
        }
    }


    @Override
    public void punchImpact(Entity entity) {
        boolean thirdPunch = this.getActivePowerPhase() == 3;

        super.punchImpact(entity);

        if (thirdPunch
                && entity != null
                && self != null
                && !self.level().isClientSide()) {

            if (podsRemaining > 0) {
                podsRemaining--;

                if (self instanceof Player player) {
                    ((IPlayerEntity) player).roundabout$setPurpleHazePods(
                            (byte) podsRemaining
                    );
                }


                activatePurpleHazeField(
                        entity.position(),
                        indistortionmode
                );
            }
        }
    }




    @Override
    public void barrageImpact(Entity entity, int hitNumber) {

        super.barrageImpact(entity, hitNumber);

        int actualHitNumber = hitNumber;
        if (actualHitNumber > 1000) {
            actualHitNumber -= 1000;
        }

        boolean lastHit = actualHitNumber >= getBarrageLength();

        if (!lastHit || entity == null) {
            return;
        }

        if (self == null || self.level().isClientSide()) {
            return;
        }

        if (podsRemaining > 0) {
            podsRemaining--;

            if (self instanceof Player player) {
                ((IPlayerEntity) player).roundabout$setPurpleHazePods(
                        (byte) podsRemaining
                );
            }


            activatePurpleHazeField(
                    entity.position(),
                    indistortionmode
            );
        }
    }



    private void breakPurpleHazePod(Vec3 position) {
        if (self.level().isClientSide()) {
            return;
        }


        if (purpleHazePod != null && !purpleHazePod.isRemoved()) {
            purpleHazePod.discard();
        }

        purpleHazePod = null;


        activatePurpleHazeField(
                position,
                purpleHazePodDistortionMode
        );


        if (self.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.LARGE_SMOKE,
                    position.x,
                    position.y + 1.0,
                    position.z,
                    35,
                    1.0,
                    0.8,
                    1.0,
                    0.02
            );
        }
    }


    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (isHoldingSneak()) {
            if (canExecuteMoveWithLevel(4)) {
                if(indistortionmode){
                    setSkillIcon(context, x, y, 1, StandIcons.PLANET_WAVES_STAND_TARGETING, PowerIndex.SKILL_1_SNEAK);
                }else setSkillIcon(context, x, y, 1, StandIcons.PLANET_WAVES_STAND_RETRIEVING, PowerIndex.SKILL_1_SNEAK);

            } else setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.SKILL_1_SNEAK);
        } else {
            if (canExecuteMoveWithLevel(4)) {
                setSkillIcon(context, x, y, 1, StandIcons.PLANET_WAVES_BIG_METEOR, PowerIndex.SKILL_1);
            } else setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.SKILL_1);
        }

        if (canExecuteMoveWithLevel(2)) {
            setSkillIcon(context, x, y, 2, StandIcons.PURPLE_HAZE_STRANGLE, PowerIndex.SKILL_2);
        } else setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.SKILL_2);

        if (isHoldingSneak()) {
            setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_PURPLE_HAZE, PowerIndex.SKILL_3);
        } else {
            if (canVault()) {
                setSkillIcon(context, x, y, 3, StandIcons.PURPLE_HAZE_LEDGE_GRAB, PowerIndex.GLOBAL_DASH);
            } else if (canFallBrace()) {
                setSkillIcon(context, x, y, 3, StandIcons.PURPLE_HAZE_FALL_CATCH, PowerIndex.GLOBAL_DASH);
            } else {
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
            }
        }
        //if(podsRemaining==6){
          //  setSkillIcon(context, x, y, 4, StandIcons.DODGE, PowerIndex.SKILL_4);
        //} else setSkillIcon(context, x, y, 4, StandIcons.ANUBIS_ALLURING_LIGHT, PowerIndex.SKILL_4);
        renderPodStock(context, x, y, 4);
    }

    @Override
    public byte getMaxLevel() {
        return 5;
    }

    @Override
    public int getExpForLevelUp(int currentLevel) {
        int amt;
        if (currentLevel == 1) {
            amt = 100;
        } else if (currentLevel == 2) {
            amt = 200;
        } else if (currentLevel == 3) {
            amt = 400;
        } else amt = 500;
        amt = (int) (amt * (getLevelMultiplier()));
        return amt;
    }

    @Override
    public void levelUp() {
        if (!this.getSelf().level().isClientSide() && this.getSelf() instanceof Player PE) {
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            byte level = ipe.roundabout$getStandLevel();
            if (level == 5) {
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.max.both").
                        withStyle(ChatFormatting.AQUA), true);
            }
            else { if (level == 4) {
                ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.both").
                        withStyle(ChatFormatting.AQUA), true);
                }
                else if (level == 3) {
                    ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.both").
                            withStyle(ChatFormatting.AQUA), true);
                }
                else {if (level == 2) {
                        ((ServerPlayer) this.self).displayClientMessage(Component.translatable("leveling.roundabout.levelup.both").
                                withStyle(ChatFormatting.AQUA), true);
                    }
                }
                super.levelUp();
            }
        }
    }

    public void renderPodStock(GuiGraphics context, int x, int y, int slot) {
        RenderSystem.enableBlend();
        context.setColor(1f, 1f, 1f, 1f);
        CooldownInstance cd = null;
        x += slot * 25;
        y -= 1;
        RenderSystem.enableBlend();
        context.blit(StandIcons.PODS_STOCKS, x - 3, y - 3, 0, 0, squareWidth, squareHeight, squareWidth, squareHeight);
    }
    private static final float PURPLE_HAZE_RANGE = 8.0F;

    private boolean purpleHazeFieldActive = false;
    private boolean indistortionmode = false;

    private int purpleHazeFieldTicks = 0;

    private static final int PURPLE_HAZE_FIELD_DURATION = 400;
    private static final int DISTORTION_FIELD_DURATION = 200;


    private Snowball purpleHazePod = null;
    private boolean purpleHazePodDistortionMode = false;
    private Vec3 purpleHazeFieldPosition = null;


    private boolean purpleHazeFieldDistortionMode = false;

    //Pod count
    private static final int MAX_PODS = 6;
    private int podsRemaining = MAX_PODS;

    private long lastPodResetDay = -1;

    private void tickPodReset() {
        if (self == null || self.level().isClientSide()) {
            return;
        }

        long dayTime = self.level().getDayTime();

        long day = Math.floorDiv(dayTime, 24000L);
        long timeOfDay = Math.floorMod(dayTime, 24000L);

        /*System.out.println(
                "PODS: " + podsRemaining +
                        " DAY: " + day +
                        " TIME: " + timeOfDay +
                        " LAST RESET: " + lastPodResetDay
        );*/

        if (timeOfDay >= 200 && lastPodResetDay != day) {
            podsRemaining = MAX_PODS;
            lastPodResetDay = day;

            //System.out.println("PURPLE HAZE PODS RESET! DAY " + day);
        }
    }




    public void attemptDistortion() {
        if (canExecuteMoveWithLevel(4) && !this.isBarraging()) {
            Distortion();
        }


    }

    public void Distortion() {
        if (!this.onCooldown(PowerIndex.SKILL_1)) {
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.PLANET_WAVES_DISINTEGRATION_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
            self.addEffect(new MobEffectInstance(
                    ModEffects.VIRUS_IMMUNITY, 100));
            if (!(self instanceof Player pl && pl.isCreative())) {
                self.hurt(ModDamageTypes.of(self.level(), DamageTypes.GENERIC_KILL), 2F);
            }
            this.setCooldown(PowerIndex.SKILL_1, 400);
            if (this.getSelf() instanceof ServerPlayer sp) {
                S2CPacketUtil.sendCooldownSyncPacket(sp, PowerIndex.SKILL_1,
                        400);
            }
        }
    }
    public void attemptDistortionModeChange() {
        if (canExecuteMoveWithLevel(4)) {
            DistortionModeChange();

        }
    }
    public void DistortionModeChange() {
        if (!this.onCooldown(PowerIndex.SKILL_1_SNEAK)) {
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.THE_WORLD_ASSAULT_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
            if(indistortionmode){
               indistortionmode=false;
            } else indistortionmode=true;

            this.setCooldown(PowerIndex.SKILL_1_SNEAK, 400);
            if (this.getSelf() instanceof ServerPlayer sp) {
                S2CPacketUtil.sendCooldownSyncPacket(sp, PowerIndex.SKILL_1_SNEAK,
                        400);
            }
        }
    }
    public void activatePurpleHazeField(
            Vec3 position,
            boolean distortionMode
    ) {
        if (this.self == null) {
            return;
        }

        if (this.self.level().isClientSide) {
            return;
        }

        IPermaCasting permaCasting =
                (IPermaCasting) this.self.level();

        purpleHazeFieldPosition = position;


        purpleHazeFieldDistortionMode = distortionMode;

        purpleHazeFieldTicks = distortionMode
                ? DISTORTION_FIELD_DURATION
                : PURPLE_HAZE_FIELD_DURATION;

        if (!permaCasting.roundabout$isPermaCastingEntity(this.self)) {
            permaCasting.roundabout$addPermaCaster(this.self);
        }

        purpleHazeFieldActive = true;
    }





    public void deactivatePurpleHazeField() {
        if (this.self == null) {
            return;
        }

        if (this.self.level().isClientSide) {
            return;
        }

        IPermaCasting permaCasting =
                (IPermaCasting) this.self.level();

        if (permaCasting.roundabout$isPermaCastingEntity(this.self)) {
            permaCasting.roundabout$removePermaCastingEntity(this.self);
        }

        purpleHazeFieldActive = false;
        purpleHazeFieldTicks = 0;
        purpleHazeFieldPosition = null;
    }


    public void attemptThrowPod() {

        ThrowPod();
    }

    public void ThrowPod() {
        if (podsRemaining <= 0) {
            return;
        }

        if (!this.onCooldown(PowerIndex.SNEAK_ATTACK)) {
            this.self.level().playSound(
                    null,
                    this.self.blockPosition(),
                    ModSounds.STAR_FINGER_EVENT,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );

            Snowball snowball = new Snowball(this.self.level(), self);

            snowball.setPos(
                    self.getX(),
                    self.getEyeY() - 0.1,
                    self.getZ()
            );

            snowball.shootFromRotation(
                    self,
                    self.getXRot(),
                    self.getYRot(),
                    0.0F,
                    0.4F,
                    0.0F
            );

            purpleHazePodDistortionMode = indistortionmode;

            this.self.level().addFreshEntity(snowball);

            this.purpleHazePod = snowball;

            podsRemaining--;

            if (self instanceof Player player) {
                ((IPlayerEntity) player).roundabout$setPurpleHazePods(
                        (byte) podsRemaining
                );
            }


            this.setCooldown(PowerIndex.SNEAK_ATTACK, 200);
        }
    }



    public void tickPurpleHazePod() {
        if (self == null) {
            return;
        }

        if (self.level().isClientSide) {
            return;
        }

        if (purpleHazePod == null) {
            return;
        }

        if (purpleHazePod.onGround()
                || purpleHazePod.horizontalCollision
                || purpleHazePod.verticalCollision
                || purpleHazePod.isRemoved()) {

            Vec3 landingPosition = purpleHazePod.position();


            activatePurpleHazeField(
                    landingPosition,
                    purpleHazePodDistortionMode
            );

            purpleHazePod = null;
        }
    }


    @Override
    public void tickPower() {
        if (self == null) {
            return;
        }

        super.tickPower();

        if (!self.level().isClientSide) {
            tickPurpleHazePod();
            tickPodReset();
        }
    }




    @Override
    public byte getPermaCastContext() {
        if(indistortionmode) {
            return PermanentZoneCastInstance.DISTORTION_SMOKE;
        } else return PermanentZoneCastInstance.PURPLE_SMOKE;
    }
    @Override
    public float getPermaCastRange() {
        return PURPLE_HAZE_RANGE;
    }

    @Override
    public void tickStandRejection(MobEffectInstance effect) {
        if (!this.getSelf().level().isClientSide()) {
            if (effect.getDuration() == 15) {
                if (!(self instanceof Player pl && pl.isCreative())) {
                    self.addEffect(new MobEffectInstance(
                            ModEffects.HAZE_VIRUS, 200));
                    }
                }
        }
    }

    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }
    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.PURPLE_HAZE_SUMMON_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
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
        return Component.literal(  "Feu_Ghost&Lloyd10").withStyle(ChatFormatting.YELLOW);
    }


}
