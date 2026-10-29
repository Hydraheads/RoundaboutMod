package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.PHCapsuleEntity;
import net.hydra.jojomod.entity.projectile.VirusSpitEntity;
import net.hydra.jojomod.entity.stand.PurpleHazeEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.substand.PurpleSmokeEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.*;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.particles.HazeColorParticleOptions;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import net.minecraft.server.level.ServerLevel;


import java.util.List;
import java.util.Objects;

public class PowersPurpleHaze extends NewPunchingStand {
    public PowersPurpleHaze(LivingEntity self) {
        super(self);
    }
    @Override
    public Component getSkinName(byte skinId) {
        switch (skinId) {
            case PurpleHazeEntity.ANIME -> {return Component.translatable("skins.roundabout.purple_haze.anime");}
            case PurpleHazeEntity.MANGA -> {return Component.translatable("skins.roundabout.purple_haze.manga");}
            case PurpleHazeEntity.BLAZING_HAZE -> {return Component.translatable("skins.roundabout.purple_haze.blazing_haze");}
            case PurpleHazeEntity.BLACK -> {return Component.translatable("skins.roundabout.purple_haze.black");}
            case PurpleHazeEntity.GREEN -> {return Component.translatable("skins.roundabout.purple_haze.green");}
            case PurpleHazeEntity.NETHERITE -> {return Component.translatable("skins.roundabout.purple_haze.netherite");}
            case PurpleHazeEntity.MIRROR_BATTLE -> {return Component.translatable("skins.roundabout.purple_haze.mirror_battle");}
            case PurpleHazeEntity.ROTT -> {return Component.translatable("skins.roundabout.purple_haze.rott_haze");}
            case PurpleHazeEntity.PEPPERMINT -> {return Component.translatable("skins.roundabout.purple_haze.peppermint");}
            case PurpleHazeEntity.GREENER -> {return Component.translatable("skins.roundabout.purple_haze.greener");}
            case PurpleHazeEntity.DISTORTION -> {return Component.translatable("skins.roundabout.purple_haze.distortion");}
            case PurpleHazeEntity.GREEN_DSITORTION -> {return Component.translatable("skins.roundabout.purple_haze.green_distortion");}
            default -> {
                return Component.translatable("skins.roundabout.purple_haze.anime");
            }
        }
    }


    public float getHeavyPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod((float) multiplyPowerByStandConfigPlayers(1.75F));
        } else {
            return levelupDamageMod((float) (multiplyPowerByStandConfigMobs(6)));
        }
    }

    @Override
    public float getBarrageHitStrength(Entity entity){
        float str = super.getBarrageHitStrength(entity);
        if (str > 0.005F) {
            if (getReducedDamage(entity)) {
                str *= levelupDamageMod((float) ((ClientNetworking.getAppropriateConfig().
                        PurpleHazeSettings.PurpleHazeAttackMultOnPlayers * 0.01)));
            } else {
                str *= levelupDamageMod((float) ((ClientNetworking.getAppropriateConfig().
                        PurpleHazeSettings.PurpleHazeAttackMultOnMobs * 0.01)));
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
                    PurpleHazeSettings.PurpleHazeAttackMultOnPlayers*0.01)));
        } else {
            str *=levelupDamageMod((float) ((ClientNetworking.getAppropriateConfig().
                    PurpleHazeSettings.PurpleHazeAttackMultOnMobs*0.01)));
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
                        PurpleHazeSettings.PurpleHazeAttackMultOnPlayers * 0.01)));
            } else {
                str *= levelupDamageMod((float) ((ClientNetworking.getAppropriateConfig().
                        PurpleHazeSettings.PurpleHazeAttackMultOnMobs * 0.01)));
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
                    PurpleHazeSettings.PurpleHazeAttackMultOnPlayers*0.01)));
        } else {
            str *=levelupDamageMod((float) ((ClientNetworking.getAppropriateConfig().
                    PurpleHazeSettings.PurpleHazeAttackMultOnMobs*0.01)));
        }
        if (forwardBarrage && !(entity instanceof Player)){
            str*=0.6F;
        } else if (forwardBarrage){
            str*=0.8F;
        }
        return str;
    }

    @Override
    public void playKickBarrageCrySound(){
        if (!this.self.level().isClientSide()) {
            playStandUserOnlySoundsIfNearby(SoundIndex.BARRAGE_CRY_SOUND_2, 32, false, true);
        }
    }

    public int bonusLeapCount = -2;
    public int spacedJumpTime = -1;

    /*@Override
    public List<Byte> getSkinList() {
        return Arrays.asList(
                PurpleHazeEntity.ANIME,
                PurpleHazeEntity.BLAZING_HAZE,
                PurpleHazeEntity.BLACK,
                PurpleHazeEntity.GREEN,
                PurpleHazeEntity.NETHERITE
        );
    }*/
    @Override
    public List<Byte> getSkinList(){
        List<Byte> $$1 = Lists.newArrayList();
        $$1.add(PurpleHazeEntity.ANIME);
        $$1.add(PurpleHazeEntity.MANGA);
        if (this.getSelf() instanceof Player PE){
            byte Level = ((IPlayerEntity)PE).roundabout$getStandLevel();
            ItemStack goldDisc = ((StandUser)PE).roundabout$getStandDisc();
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);
            if (Level > 1 || bypass){
                $$1.add(PurpleHazeEntity.BLACK);
                $$1.add(PurpleHazeEntity.GREEN);
                $$1.add(PurpleHazeEntity.NETHERITE);
            } if (Level > 2 || bypass) {
                $$1.add(PurpleHazeEntity.BLAZING_HAZE);
                $$1.add(PurpleHazeEntity.MIRROR_BATTLE);
                $$1.add(PurpleHazeEntity.ROTT);
                $$1.add(PurpleHazeEntity.PEPPERMINT);
            } if (Level > 3 || bypass) {
                $$1.add(PurpleHazeEntity.DISTORTION);
                $$1.add(PurpleHazeEntity.GREEN_DSITORTION);
                $$1.add(PurpleHazeEntity.GREENER);
            }/*if (((IPlayerEntity) PE).roundabout$getUnlockedBonusSkin() || bypass) {
                $$1.add(PurpleHazeEntity.PLAGUE);}*/
        }
        return $$1;
    }
    private static final int SECRET_SKIN_TICKS = 40;
    private int secretSkinTicks = 0;
    private boolean secretSkinObtained = false;

    private boolean hasSecretSkinCondition() {
        return self.hasEffect(ModEffects.DISTORTION_VIRUS)
                && self.hasEffect(MobEffects.POISON)
                && self.hasEffect(MobEffects.WITHER);
    }
    private void tickSecretSkinObtaining() {
        if (self == null || self.level().isClientSide()) {
            return;
        }

        if (!(self instanceof Player player)) {
            return;
        }


        if (hasSecretSkinCondition()) {
            secretSkinTicks++;

            if (secretSkinTicks >= SECRET_SKIN_TICKS) {
                obtainSecretSkin();
            }
        } else {
            secretSkinTicks = 0;
        }
    }
    private void obtainSecretSkin() {
        if (this.getSelf() instanceof Player PE) {
            Level lv = this.getSelf().level();
            ItemStack goldDisc = ((StandUser) PE).roundabout$getStandDisc();
            StandUser user = (StandUser) PE;
            IPlayerEntity ipe = (IPlayerEntity) PE;
            boolean bypass = PE.isCreative() || (!goldDisc.isEmpty() && goldDisc.getItem() instanceof MaxStandDiscItem);

            if (!ipe.roundabout$getUnlockedBonusSkin() && !bypass) {
                ipe.roundabout$setUnlockedBonusSkin(true);
                playSoundIfPossible(self.level(),null, PE.getX(), PE.getY(),
                        PE.getZ(), ModSounds.UNLOCK_SKIN_EVENT, PE.getSoundSource(), 2.0F, 1.0F);
                sendParticlesIfPossible(self.level(),ParticleTypes.END_ROD, PE.getX(),
                        PE.getY() + PE.getEyeHeight(), PE.getZ(),
                        10, 0.5, 0.5, 0.5, 0.2);
                user.roundabout$setStandSkin(PurpleHazeEntity.GREEN);
                ((ServerPlayer) PE).displayClientMessage(
                        Component.translatable("unlock_skin.roundabout.purple_haze_plague"), true);
                user.roundabout$summonStand(lv, true, false);
            }
        }
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
    public float getMiningMultiplier() {
        return (float) (1F*(ClientNetworking.getAppropriateConfig().
                PurpleHazeSettings.miningSpeedMultiplierPH *0.01));
    }
    @Override
    public int getMiningLevel() {
        return ClientNetworking.getAppropriateConfig().PurpleHazeSettings.getMiningTierPH;
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersPurpleHaze(entity);
    }
    @Override public boolean isStandEnabled(){ return ClientNetworking.getAppropriateConfig().PurpleHazeSettings.enablePurpleHaze; }

    @Override
    public boolean canGuard(){
        if (isStrangling()||isStranglePinning()||activePower == PowerIndex.POWER_2){
            return false;
        }
        return super.canGuard();
    }
    public int getMaxGuardPoints(){

        return ClientNetworking.getAppropriateConfig().PurpleHazeSettings.PurpleHazeGuardPoints;
    }

    @Override public float getPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return levelupDamageMod(multiplyPowerByStandConfigPlayers(1.31F));
        } else {
            return levelupDamageMod(multiplyPowerByStandConfigMobs(4.5f));
        }
    }
    @Override
    public float multiplyPowerByStandConfigMobs(float power){
        return (float) (power*(ClientNetworking.getAppropriateConfig().
                PurpleHazeSettings.PurpleHazeAttackMultOnMobs*0.01));
    }
    @Override
    public float multiplyPowerByStandConfigPlayers(float power){
        return (float) (power*(ClientNetworking.getAppropriateConfig().
                PurpleHazeSettings.PurpleHazeAttackMultOnPlayers*0.01));
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
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        byte pods = ((IPlayerEntity) this.getSelf()).roundabout$getPurpleHazePods();
        if (activeP == PowerIndex.SKILL_1 || activeP == PowerIndex.SKILL_2 || activeP == PowerIndex.SKILL_2_SNEAK) {
            if (pods==0) {
                return true;
            }
        }
        return false;
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

    @Override
    public void buttonInputBarrage(boolean keyIsDown, Options options){
        if (hasHandsOut())
            return;
        if (keyIsDown) {
            if (isHoldingSneak() && (this.getAttackTime() >= this.getAttackTimeMax() ||
                    (this.getActivePowerPhase() != this.getActivePowerPhaseMax()))) {
                this.tryPower(PowerIndex.BARRAGE_CHARGE_2, true);
                tryPowerPacket(PowerIndex.BARRAGE_CHARGE_2);
            } else {
                super.buttonInputBarrage(keyIsDown, options);
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
                           // this.setCooldown(PowerIndex.SKILL_1_SNEAK, 10);
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

    @Override
    public boolean setPowerSneakMovement(int lastMove) {

        // leaping puts timestop on a cooldown
        applyLeapCooldowns(); // borrowed from TWAndSPSharedPowers, dunno if you gonna need to put something on cooldown

        this.setAttackTimeDuring(-1);
        this.setActivePower(PowerIndex.NONE);
        poseStand(OffsetIndex.FOLLOW);
        animateStand(StandEntity.LEAP);
        if (this.getSelf() instanceof Player) {
            cancelConsumableItem(this.getSelf());
        }
        if (!this.getSelf().level().isClientSide()) {
            if (Math.random() > 0.5){
                addEXP(1);
            }
            ((StandUser) this.getSelf()).roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
            ((StandUser) this.getSelf()).roundabout$setLeapIntentionally(true);
            playSoundIfPossible(self.level(),null, this.getSelf().blockPosition(), ModSounds.STAND_LEAP_EVENT, SoundSource.PLAYERS, 2.3F, (float) (0.98 + (Math.random() * 0.04)));
        }
        return true;
    }

    @Override
    public boolean canInterruptPower(DamageSource sauce, Entity interrupter){

        if (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2) {
            return true;
        }

        return super.canInterruptPower(sauce, interrupter);
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
    public Vector3f getLeapColor() {
        int color = PurpleSmokeEntity.SKIN_COLORS.getOrDefault(getStandSkin(), PurpleSmokeEntity.DEFAULT_HAZE_COLOR);
        return new Vector3f(
                ((color >> 16) & 0xFF) / 255f,
                ((color >> 8) & 0xFF) / 255f,
                (color & 0xFF) / 255f);
    }
    @Override
    public boolean cancelItemUse() {
        return (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2 || this.getActivePower() == PowerIndex.BARRAGE_2);
    }

    @Override
    public boolean clickRelease(){
        return (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2 || this.getActivePower() == PowerIndex.BARRAGE_2);
    }

    @Override
    public boolean cancelSprintJump(){
        if (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2 || this.getActivePower() == PowerIndex.BARRAGE_2) {
            return true;
        }
        return super.cancelSprintJump();
    }
    @Override
    public void updateUniqueMoves() {
         if (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2) {
             updateKickBarrageCharge();
         } else if (this.getActivePower() == PowerIndex.BARRAGE_2) {
             updateKickBarrage();
         }
         super.updateUniqueMoves();
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move) {
            case PowerIndex.POWER_1 -> { // Distortion
                attemptDistortion();
            }
            case PowerIndex.POWER_1_BONUS -> //Virus Spit
                attemptVirusSpit();
            case PowerIndex.POWER_1_SNEAK -> { // Distortion Mode Change
                attemptDistortionModeChange();
            }
            case PowerIndex.POWER_2 -> { // Strangle
                attemptStrangle();
            }
            case PowerIndex.SNEAK_ATTACK_CHARGE -> attemptThrowPod();
            case PowerIndex.EXTRA -> {
                return this.fallBraceInit();
            }
            case PowerIndex.FALL_BRACE_FINISH -> {
                return this.fallBrace();
            }
            case PowerIndex.BARRAGE_CHARGE_2 -> {
                return this.setPowerKickBarrageCharge();
            }
            case PowerIndex.BARRAGE_2 ->{
                return this.setPowerKickBarrage();
            }
        }
        return super.setPowerOther(move, lastMove);
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        if (!this.getSelf().level().isClientSide && move == PowerIndex.SNEAK_MOVEMENT && this.isClashing()){
            ((StandUser) this.getSelf()).roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
            ((StandUser) this.getSelf()).roundabout$setLeapIntentionally(true);
        }

        if (!this.self.level().isClientSide &&
                (this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2 || this.getActivePower() == PowerIndex.BARRAGE_2)
                && (move != PowerIndex.BARRAGE_2 && move != PowerIndex.BARRAGE_CHARGE_2)){
            this.stopSoundsIfNearby(SoundIndex.BARRAGE_SOUND_GROUP, 100,false);
        }

        return super.tryPower(move, forced);
    }

    @Override
    public void powerActivate(PowerContext context) {
        switch (context) {
            case SKILL_1_NORMAL, SKILL_1_GUARD -> {
                if(activePower == PowerIndex.BARRAGE || activePower == PowerIndex.BARRAGE_2){
                    clientForwardBarrage();
                }
                else if(isBarrageCharging()){

                } else
                    if(self.hasEffect(ModEffects.VIRUS_IMMUNITY)) {
                        this.tryPowerPacket(PowerIndex.POWER_1_BONUS);
                    }else this.tryPowerPacket(PowerIndex.POWER_1);
            }
            case SKILL_1_CROUCH,SKILL_1_CROUCH_GUARD -> {
                if(activePower == PowerIndex.BARRAGE || activePower == PowerIndex.BARRAGE_2){
                    clientForwardBarrage();
                }else {
                    this.tryPowerPacket(PowerIndex.POWER_1_SNEAK);
                }
            }
            case SKILL_2_NORMAL,SKILL_2_CROUCH,SKILL_2_CROUCH_GUARD, SKILL_2_GUARD -> {
                this.tryPowerPacket(PowerIndex.POWER_2);
            }
            case SKILL_3_NORMAL -> {
                if (!(activePower == PowerIndex.BARRAGE || activePower == PowerIndex.BARRAGE_2
                        || activePower == PowerIndex.BARRAGE_CHARGE || activePower == PowerIndex.BARRAGE_CHARGE_2)) {
                    tryToDashClient();
                }
            }
            case SKILL_3_CROUCH -> tryToStandLeapClient();
        }
    }


    @Override
    public void punchImpact(Entity entity) {
        boolean thirdPunch = this.getActivePowerPhase() == 3;
        super.punchImpact(entity);
        if(ClientNetworking.getAppropriateConfig().PurpleHazeSettings.ThirdPunchCapsuleBreak){
            if (thirdPunch
                    && entity != null
                    && self != null
                    && !self.level().isClientSide()) {

                if (getPods() > 0) {
                    if (!(self instanceof Player pl && pl.isCreative())) {
                        setPods(getPods() - 1);
                    }

                    activatePurpleHazeField(
                            entity.position(),
                            indistortionmode
                    );
            }
        }
        }
    }


    @Override
    public float getBarrageFinisherKnockback(){
        return 0.8F;
    }
    @Override
    public int getBarrageLength(){
        return 70;
    }

    private static final double FORWARD_BARRAGE_SPEED = 0.12;

    @Override
    public void barrageImpact(Entity entity, int hitNumber) {
        if (entity != null && moveStarted){
            moveStarted = false;
            StandEntity stand = getStandEntity(this.self);
            if (Objects.nonNull(stand)){
                stand.setXRot(getLookAtEntityPitch(stand, entity));
                stand.setYRot(getLookAtEntityYaw(stand, entity));
            }
        }

        super.barrageImpact(entity, hitNumber);

        int actualHitNumber = hitNumber;
        if (actualHitNumber > 1000) {
            actualHitNumber -= 1000;
        }
        boolean lastHit = actualHitNumber >= getBarrageLength();

        if (forwardBarrage && entity instanceof LivingEntity && entity.isAlive() && !entity.isRemoved() && !lastHit) {
            StandEntity stand = getStandEntity(this.self);
            if (Objects.nonNull(stand)) {
                Vec3 forward = stand.getForward();
                Vec3 current = entity.getDeltaMovement();
                entity.setDeltaMovement(
                        forward.x * FORWARD_BARRAGE_SPEED,
                        current.y * 0.5,
                        forward.z * FORWARD_BARRAGE_SPEED
                );
                entity.hurtMarked = true;
            }
        }

        if (!lastHit || entity == null || activePower == PowerIndex.BARRAGE_2) {
            return;
        }

        if (self == null || self.level().isClientSide()) {
            return;
        }

        if (getPods() > 0) {
            if (!(self instanceof Player pl && pl.isCreative())) {
                setPods(getPods() - 1);
            }
            activatePurpleHazeField(entity.position(), indistortionmode);
        }
    }
    private Vec2 forwardBarrageLockedRot = null;
    public boolean clientForwardBarrage(){
        if (activePower == PowerIndex.BARRAGE || activePower == PowerIndex.BARRAGE_2){
            if (attackTimeDuring < 0){
                return true;
            }
            if (!forwardBarrage) {
                forwardBarrage = true;
                captureForwardBarrageDirection();
                C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_BYTE_FORWARD_BARRAGE);
            }
            return true;
        }
        return false;
    }
    private void captureForwardBarrageDirection(){
        Vec2 twoVec = new Vec2((this.getSelf().getYHeadRot() % 360), this.getSelf().getXRot());
        Direction gdir = ((IGravityEntity) this.self).roundabout$getGravityDirection();
        forwardBarrageLockedRot = RotationUtil.rotPlayerToWorld(twoVec, gdir);
    }
    public float getRushDistance(){
        if (forwardBarrage){
            return 15;
        }
        return getReach();
    }

    public float getFloatOutRange(){
        return 7F;
    }
    @Override
    public void standBarrageHit(){
        if (forwardBarrage) {
            StandEntity stand = getStandEntity(this.self);
            if (this.self instanceof Player) {
                if (isPacketPlayer() && Objects.nonNull(stand)) {
                    C2SPacketUtil.standBarrageHitPacket(getTargetEntityId2(2.7F, stand, 50), this.attackTimeDuring);
                    if (this.isBarraging() && this.attackTimeDuring == this.getBarrageLength()) {
                        this.attackTimeDuring = -10;
                    }
                }
            } else if (Objects.nonNull(stand)) {
                Entity targetEntity = getTargetEntity(stand, 2.7F, 50);
                barrageImpact(targetEntity, this.attackTimeDuring);
            }
            findDeflectables();
            return;
        }
        super.standBarrageHit();
    }


    public void faceForwardBarrageDirection(StandEntity stand){
        Vec2 rot;
        if (ClientNetworking.getAppropriateConfig().PurpleHazeSettings.forwardBarrageCanSteer) {
            Vec2 twoVec = new Vec2((this.getSelf().getYHeadRot() % 360), this.getSelf().getXRot());
            Direction gdir = ((IGravityEntity) this.self).roundabout$getGravityDirection();
            rot = RotationUtil.rotPlayerToWorld(twoVec, gdir);
        } else {
            if (forwardBarrageLockedRot == null) {
                captureForwardBarrageDirection();
            }
            rot = forwardBarrageLockedRot;
        }
        stand.setYRot(rot.x);
        stand.setXRot(rot.y);
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
            sendParticlesIfPossible(self.level(),
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
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        StandUser standUser = ((StandUser) playerEntity);
        boolean standOn = PowerTypes.hasStandActive(playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;

        if (standOn && this.getActivePower() == PowerIndex.BARRAGE_2 && attackTimeDuring > -1) {
            int ClashTime = 15 - Math.round(((float) attackTimeDuring / this.getKickBarrageLength()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);
        } else if (standOn && this.getActivePower() == PowerIndex.BARRAGE_CHARGE_2) {
            int ClashTime = Math.round(((float) attackTimeDuring / this.getKickBarrageWindup()) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);
        } else {
            super.renderAttackHud(context,playerEntity,
                    scaledWidth,scaledHeight,ticks,vehicleHeartCount, flashAlpha, otherFlashAlpha);
        }

        if (onCooldown(PowerIndex.SNEAK_ATTACK)) {
            CooldownInstance cd = getCooldown(PowerIndex.SNEAK_ATTACK);
            if (cd != null) {
                int ClashTime = Math.round(((float) cd.time / cd.maxTime) * 15);
                context.blit(StandIcons.JOJO_ICONS, k, j+17, 213, 97, 15, 4);
                context.blit(StandIcons.JOJO_ICONS, k, j+17, 213, 102, ClashTime, 4);
            }
        }
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if ((activePower == PowerIndex.BARRAGE || activePower == PowerIndex.BARRAGE_2
                || activePower == PowerIndex.BARRAGE_CHARGE || activePower == PowerIndex.BARRAGE_CHARGE_2)) {
            setSkillIcon(context, x, y, 1, StandIcons.PH_FORWARD_BARRAGE, PowerIndex.NO_CD);
        } else if (isHoldingSneak()) {
            if (canExecuteMoveWithLevel(getDistortionAndPodBiteLevel())) {
                if(indistortionmode()){
                    setSkillIcon(context, x, y, 1, StandIcons.DISTORTION_MODE, PowerIndex.SKILL_1_SNEAK);

                }else {
                    setSkillIcon(context, x, y, 1, StandIcons.PURPLE_HAZE_MODE, PowerIndex.SKILL_1_SNEAK);

                }

            } else setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD);
        } else {
            if (canExecuteMoveWithLevel(getDistortionAndPodBiteLevel())) {
                if(self.hasEffect(ModEffects.VIRUS_IMMUNITY)){
                    setSkillIcon(context, x, y, 1, StandIcons.VIRUS_SPIT, PowerIndex.POWER_1_BONUS);
                }else setSkillIcon(context, x, y, 1, StandIcons.POD_BITE, PowerIndex.SKILL_1);
            } else setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD);
        }

        if (canExecuteMoveWithLevel(getStrangleLevel())) {
            setSkillIcon(context, x, y, 2, StandIcons.PURPLE_HAZE_STRANGLE, PowerIndex.SKILL_2);
        } else setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.NO_CD);

        if (isHoldingSneak()) {
            if (canExecuteMoveWithLevel(getLeapLevel())) {
                boolean jojoveinLikeKeys = !ClientNetworking.getAppropriateConfig().generalStandSettings.standJumpAndDashShareCooldown;
                if (jojoveinLikeKeys){
                    setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_PURPLE_HAZE, PowerIndex.SKILL_3);
                } else {
                    setSkillIcon(context, x, y, 3, StandIcons.STAND_LEAP_PURPLE_HAZE, PowerIndex.GLOBAL_DASH);
                }
            } else setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NO_CD);
        } else {
            if (canVault()) {
                setSkillIcon(context, x, y, 3, StandIcons.PURPLE_HAZE_LEDGE_GRAB, PowerIndex.GLOBAL_DASH);
            } else if (canFallBrace()) {
                setSkillIcon(context, x, y, 3, StandIcons.PURPLE_HAZE_FALL_CATCH, PowerIndex.GLOBAL_DASH);
            } else {
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
            }
        }

        renderPodStock(context, x, y, 4);
    }
    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypas){
        List<AbilityIconInstance> $$1 = Lists.newArrayList();
        int startPos = 0;
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20+startPos,topPos+80,0, "ability.roundabout.punch",
                "instruction.roundabout.press_attack", StandIcons.PH_PUNCH,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20+startPos, topPos+99,0, "ability.roundabout.guard",
                "instruction.roundabout.hold_block", StandIcons.STAR_PLATINUM_GUARD,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20+startPos,topPos+118,0, "ability.purple_haze.capsule_throw",
                "instruction.roundabout.press_attack_crouch", StandIcons.POD_THROW,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39+startPos,topPos+80,0, "ability.purple_haze.punch_barrage",
                "instruction.roundabout.barrage", StandIcons.PH_BARRAGE,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39+startPos,topPos+99,0, "ability.purple_haze.kick_barrage",
                "instruction.roundabout.kick_barrage", StandIcons.PH_KICK_BARRAGE,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39+startPos,topPos+118, 0, "ability.roundabout.forward_barrage",
               "instruction.roundabout.forward_barrage", StandIcons.PH_FORWARD_BARRAGE,1,level,bypas));
         $$1.add(drawSingleGUIIcon(context,18,leftPos+58+startPos,topPos+80,0, "ability.purple_haze.daily_capsule_recharge",
                "instruction.roundabout.passive", StandIcons.PODS_STOCKS,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58+startPos,topPos+99,4, "ability.purple_haze.distortion",
                "instruction.roundabout.press_skill", StandIcons.POD_BITE,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58+startPos,topPos+118,4, "ability.purple_haze.virus_spit",
                "instruction.roundabout.distortion_spit", StandIcons.VIRUS_SPIT,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77+startPos,topPos+80,4, "ability.purple_haze.haze_switch",
                "instruction.roundabout.press_skill_crouch", StandIcons.PH_SWITCH,1,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77+startPos,topPos+99,0, "ability.purple_haze.purple_smoke",
                "instruction.roundabout.passive", StandIcons.PURPLE_HAZE_MODE,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77+startPos,topPos+118,4, "ability.purple_haze.distortion_smoke",
                "instruction.roundabout.passive", StandIcons.DISTORTION_MODE,0,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96+startPos,topPos+80,2, "ability.purple_haze.strangle",
                "instruction.roundabout.press_skill", StandIcons.PURPLE_HAZE_STRANGLE,2,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96+startPos,topPos+99,0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96+startPos,topPos+118,0, "ability.purple_haze.falling_hit",
                "instruction.roundabout.press_skill_air", StandIcons.FALLING_ATTACK,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115+startPos,topPos+80,0, "ability.roundabout.vault",
                "instruction.roundabout.press_skill_air", StandIcons.PURPLE_HAZE_LEDGE_GRAB,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115+startPos,topPos+99,3, "ability.roundabout.stand_leap",
                "instruction.roundabout.press_skill_crouch", StandIcons.STAND_LEAP_PURPLE_HAZE,3,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115+startPos,topPos+118,0, "ability.purple_haze.capsule_count",
                "instruction.roundabout.passive", StandIcons.PODS6NOBORDER,4,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+134+startPos,topPos+80,5, "ability.purple_haze.capsule_advanced_recharge",
                "instruction.roundabout.passive", StandIcons.ADVANCED_RECHARGE,4,level,bypas));
        $$1.add(drawSingleGUIIcon(context,18,leftPos+134+startPos,topPos+99,0, "ability.roundabout.mining",
                "instruction.roundabout.hold_attack", StandIcons.MINING,0,level,bypas));
        return $$1;
    }

    private int getStrangleLevel() { return 2; }
    private int getLeapLevel() { return 3; }
    private int getDistortionAndPodBiteLevel() { return 4; }
    private int getPodRegenLevel() { return 4; }

    @Override  public byte getMaxLevel() { return 5; }


    @Override
    public int getExpForLevelUp(int currentLevel){
        int amt;
        if (currentLevel == 1){
            amt = 100;
        } else {
            amt = (100+((currentLevel-1)*60));
        }
        amt= (int) (amt*getLevelMultiplier());
        return amt;
    }

    @Override
    public void levelUp() {
        if (!this.getSelf().level().isClientSide() && this.getSelf() instanceof Player PE) {
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            byte level = ipe.roundabout$getStandLevel();
            if(getPods()< MAX_PODS){
                setPods(getPods()+1);
            }
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

        if (getPods() <= 0) {
            context.setColor(0.62f, 0.62f, 0.62f, 0.8f);
        }

        x += slot * 25;
        y -= 1;

        byte pods = ((IPlayerEntity) this.getSelf()).roundabout$getPurpleHazePods();

        if (pods == 6) {
            context.blit(StandIcons.PODS_6, x - 3, y - 3,
                    0, 0, squareWidth, squareHeight, squareWidth, squareHeight);
        } else if (pods == 5) {
            context.blit(StandIcons.PODS_5, x - 3, y - 3,
                    0, 0, squareWidth, squareHeight, squareWidth, squareHeight);
        } else if (pods == 4) {
            context.blit(StandIcons.PODS_4, x - 3, y - 3,
                    0, 0, squareWidth, squareHeight, squareWidth, squareHeight);
        } else if (pods == 3) {
            context.blit(StandIcons.PODS_3, x - 3, y - 3,
                    0, 0, squareWidth, squareHeight, squareWidth, squareHeight);
        } else if (pods == 2) {
            context.blit(StandIcons.PODS_2, x - 3, y - 3,
                    0, 0, squareWidth, squareHeight, squareWidth, squareHeight);
        } else if (pods == 1) {
            context.blit(StandIcons.PODS_1, x - 3, y - 3,
                    0, 0, squareWidth, squareHeight, squareWidth, squareHeight);
        } else {
            context.blit(StandIcons.PODS_0, x - 3, y - 3,
                    0, 0, squareWidth, squareHeight, squareWidth, squareHeight);
        }


    }

    private boolean indistortionmode = false;


    private static final int PURPLE_HAZE_FIELD_DURATION = 400;
    private static final int DISTORTION_FIELD_DURATION = 300;


    private PHCapsuleEntity purpleHazePod = null;
    private boolean purpleHazePodDistortionMode = false;

    //Pod count
    private static final int MAX_PODS = 6;
    private static final int POD_RECHARGE_TIME = 800;
    private int podRechargeTicks = 0;
    private boolean podsSyncedOnJoin = false;
    //private int podsRemaining = MAX_PODS;

    private int getPods() {
        if (self instanceof Player) {
            return ((IPlayerEntity) self).roundabout$getPurpleHazePods();
        }
        return MAX_PODS;
    }

    private void setPods(int pods) {
        if (self instanceof Player player) {
            ((IPlayerEntity) self).roundabout$setPurpleHazePods((byte) pods);
            S2CPacketUtil.syncPurpleHazePods(player, (byte) pods);
        }
    }
    //summon minecraft:husk ~ ~ ~ {roundabout.StandDisc:{id:"roundabout:max_purple_haze_disc",tag:{Memory:{Pose:0b,Skin:1b}},Count:1b}}
    private void tickPodReset() {
        if (self == null || self.level().isClientSide() || !(self instanceof Player)) {
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
        IPlayerEntity playerData = (IPlayerEntity) self;

        if (timeOfDay >= 50 &&
                playerData.roundabout$getPurpleHazePodResetDay() != day) {

            setPods(MAX_PODS);

            playerData.roundabout$setPurpleHazePodResetDay(day);
        }
        //System.out.println("PURPLE HAZE PODS RESET! DAY " + day);
    }
    private void tickPodRecharge() {
        if (self == null || self.level().isClientSide()) {
            return;
        }

        if (!(self instanceof Player player)) {
            return;
        }

        //IPlayerEntity playerData = (IPlayerEntity) player;

        if (canExecuteMoveWithLevel(getPodRegenLevel())) {
            podRechargeTicks = 0;
            return;
        }

        if (getPods() >= MAX_PODS) {
            podRechargeTicks = 0;
            return;
        }

        podRechargeTicks++;

        if (podRechargeTicks >= POD_RECHARGE_TIME) {
            setPods(Math.min(MAX_PODS, getPods() + 1));
            podRechargeTicks = 0;
        }
    }
    public void attemptDistortion() {
        if (canExecuteMoveWithLevel(getDistortionAndPodBiteLevel()) && !(activePower == PowerIndex.BARRAGE || activePower == PowerIndex.BARRAGE_2
                || activePower == PowerIndex.BARRAGE_CHARGE || activePower == PowerIndex.BARRAGE_CHARGE_2) && getPods()>0 ) {
            Distortion();
        }
    }

    public void Distortion() {
        if (!this.onCooldown(PowerIndex.SKILL_1)) {
            playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.PURPLE_HAZE_POD_BITE_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
            self.addEffect(new MobEffectInstance(
                    ModEffects.VIRUS_IMMUNITY, 100));
            if (!(self instanceof Player pl && pl.isCreative())) {
                this.eatCapsuleServer();
                self.hurt(ModDamageTypes.of(self.level(), DamageTypes.GENERIC_KILL), 2F);
            }
            if (!(self instanceof Player pl && pl.isCreative())) {
                setPods(getPods() - 1);
            }
            int cdr = ClientNetworking.getAppropriateConfig().PurpleHazeSettings.DistortionCooldown;
            this.setCooldown(PowerIndex.SKILL_1, cdr);
            if (this.getSelf() instanceof ServerPlayer sp) {
                S2CPacketUtil.sendCooldownSyncPacket(sp, PowerIndex.SKILL_1,
                        cdr);
            }
        }
    }
    public int capsuleEatingTick = 0;
    private  void setCapsuleEatingTick(int tick){capsuleEatingTick = tick;}
    public void eatCapsuleServer(){
        if (self instanceof ServerPlayer pl){
            setCapsuleEatingTick(16);
            ((IPlayerEntity)pl).roundabout$SetPoseEmote((byte) 37);
        }
    }
    public boolean isEatingCapsule() {
        return self instanceof Player pl
                && ((IPlayerEntity) pl).roundabout$GetPoseEmote() == 37;
    }

    @Override
    public float inputSpeedModifiers(float basis) {
        if (isEatingCapsule()) {
            basis *= 0.0f;
        }
        return super.inputSpeedModifiers(basis);
    }

    @Override
    public boolean cancelJump() {
        if (isEatingCapsule()) {
            return true;
        }
        return super.cancelJump();
    }

    public void attemptVirusSpit() {
        if (canExecuteMoveWithLevel(getDistortionAndPodBiteLevel()) && !(activePower == PowerIndex.BARRAGE || activePower == PowerIndex.BARRAGE_2
                || activePower == PowerIndex.BARRAGE_CHARGE || activePower == PowerIndex.BARRAGE_CHARGE_2)) {
            VirusSpit();
        }
    }

    public void VirusSpit() {
        if (!this.onCooldown(PowerIndex.POWER_1_BONUS)) {
            playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.PLANET_WAVES_METEOR_SHOWER_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
            self.removeEffect(ModEffects.VIRUS_IMMUNITY);

            VirusSpitEntity spit = new VirusSpitEntity(
                    ModEntities.VIRUS_SPIT,
                    self.level()
            );

            spit.setOwner(self);

            spit.setPos(
                    self.getX(),
                    self.getEyeY() - 0.1,
                    self.getZ()
            );

            spit.shootFromRotation(
                    self,
                    self.getXRot(),
                    self.getYRot(),
                    0.0F,
                    0.4F,
                    0.0F
            );

            purpleHazePodDistortionMode = indistortionmode;

            self.level().addFreshEntity(spit);

            int cdr = ClientNetworking.getAppropriateConfig().PurpleHazeSettings.VirusSpitCooldown;
            this.setCooldown(PowerIndex.POWER_1_BONUS, cdr);
            if (this.getSelf() instanceof ServerPlayer sp) {
                S2CPacketUtil.sendCooldownSyncPacket(sp, PowerIndex.POWER_1_BONUS,
                        cdr);
            }
        }
    }
    public void attemptDistortionModeChange() {
        if (canExecuteMoveWithLevel(getDistortionAndPodBiteLevel())) {
            DistortionModeChange();

        }
    }
    public void DistortionModeChange() {
        if (!this.onCooldown(PowerIndex.SKILL_1_SNEAK)) {
            playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.PURPLE_HAZE_SWITCH_POD_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
            indistortionmode = !indistortionmode;
            saveDiscAndSync();

            int cdr = ClientNetworking.getAppropriateConfig().PurpleHazeSettings.DistortionModeChangeCooldown;
            this.setCooldown(PowerIndex.SKILL_1_SNEAK, cdr);
            if (this.getSelf() instanceof ServerPlayer sp) {
                S2CPacketUtil.sendCooldownSyncPacket(sp, PowerIndex.SKILL_1_SNEAK, cdr);
            }
        }
    }
    private static final int STRANGLE_WINDUP_TICKS=22;
    private static final double STRANGLE_SPEED=1.4;
    private static final double STRANGLE_MAX_DISTANCE=16.0;
    private static final int STRANGLE_HOLD_DURATION= 80;
    private static final int STRANGLE_GRAB_TICKS = 5;
    private static final double STRANGLE_LIFT_HEIGHT = 0.25;

    private double strangleBaseY = 0;
    private int strangleGrabTicks = 0;
    private boolean strangleVictimHadNoGravity = false;

    private int strangleTicks = -1;
    private int strangleTravelTicks = 0;
    private Vec3 strangleOrigin = Vec3.ZERO;
    private Vec3 strangleDirection = Vec3.ZERO;
    private LivingEntity strangleVictim = null;
    private int strangleHoldTicks = 0;

    public void attemptStrangle() {
        if ((!(self instanceof Player) || canExecuteMoveWithLevel(getStrangleLevel())) && (activePower == NONE || activePower == GUARD)) {
            Strangle();
        }
    }
    public void Strangle() {
        if (this.onCooldown(PowerIndex.SKILL_2) || strangleVictim != null || strangleTicks != -1) {
            return;
        }

        StandEntity stand = getStandEntity(this.self);
        if (Objects.isNull(stand)) {
            return;
        }


        this.setActivePower(PowerIndex.POWER_2);
        this.setAttackTimeDuring(0);
        this.strangleTicks = 0;
        rotation = new Vec2((this.getSelf().getYHeadRot() % 360), this.getSelf().getXRot());

        animateStand(PurpleHazeEntity.STRANGLE_WINDUP);
        poseStand(OffsetIndex.LOOSE);
        Vec3 pos = self.getPosition(1).add(new Vec3(
                Mth.sin(rotation.x * ((float) Math.PI / 180)),
                0,
                -Mth.cos(rotation.x * ((float) Math.PI / 180)))
                .scale(-1.25f));

        stand.absMoveTo(pos.x, pos.y, pos.z, rotation.x, rotation.y);

        playSoundIfPossible(self.level(), null, this.self.blockPosition(),
                ModSounds.PURPLE_HAZE_STRANGLE_START_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);

    }

    @Override
    public void tickPowerEnd() {
        super.tickPowerEnd();
        if (this.getSelf().isAlive() && !this.getSelf().isRemoved()
                && isStrangling()
                && !this.getSelf().level().isClientSide()) {

            StandEntity stand = getStandEntity(this.self);
            if (Objects.isNull(stand)) {
                endStrangle();
                return;
            }

            if (strangleVictim != null) {
                tickStranglePin(stand);
                return;
            }

            if (strangleTicks < STRANGLE_WINDUP_TICKS) {
                strangleTicks++;
                return;
            }

            if (strangleTicks == STRANGLE_WINDUP_TICKS) {
                launchStrangle(stand);
                strangleTicks++;
                return;
            }

            tickStrangleTravel(stand);
        }
        if (this.forwardBarrage && !(this.isBarraging() || activePower == PowerIndex.BARRAGE_CHARGE_2 || activePower == PowerIndex.BARRAGE_2)){
            this.forwardBarrage = false;
            forwardBarrageLockedRot = null;
        }
        if (this.forwardBarrage && this.attackTimeDuring >= 0 && (this.isBarraging() || activePower == PowerIndex.BARRAGE_CHARGE_2 || activePower == PowerIndex.BARRAGE_2)) {
            if (!this.getSelf().level().isClientSide()) {
                StandEntity stand = getStandEntity(this.self);
                if (Objects.nonNull(stand)) {
                    faceForwardBarrageDirection(stand);

                    Entity target = getTargetEntity(stand, 2.7F, 50);
                    boolean blockedByWall = stand.isTechnicallyInWall();
                    boolean tooCloseToTarget = target != null
                            && stand.position().distanceTo(target.position()) < 1.0;

                    if (!blockedByWall && !tooCloseToTarget) {
                        stand.setPos(stand.getPosition(1).add(stand.getForward().scale(0.12)));
                    }

                    if (stand.position().distanceTo(this.getSelf().position()) > 15F) {
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                    }
                }
            }
        }
    }
    Vec2 rotation;
    private void launchStrangle(StandEntity stand) {

        Direction gdir = ((IGravityEntity) this.self).roundabout$getGravityDirection();
        Vec2 twoVecGrav = RotationUtil.rotPlayerToWorld(rotation, gdir);

        stand.setYRot(rotation.x);
        stand.setXRot(rotation.y);

        this.strangleOrigin = stand.position();
        this.strangleDirection = DamageHandler.getRotationVector(twoVecGrav.y, (float) twoVecGrav.x).normalize();
        this.strangleTravelTicks = 0;


        playSoundIfPossible(self.level(), null, this.self.blockPosition(),
                ModSounds.PURPLE_HAZE_STRANGLE_RUN_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
    private void tickStrangleTravel(StandEntity stand) {
        double traveled = strangleTravelTicks * STRANGLE_SPEED;
        if (traveled >= STRANGLE_MAX_DISTANCE) {
            endStrangle();
            return;
        }
        animateStand(PurpleHazeEntity.FLYLOOP);
        poseStand(OffsetIndex.LOOSE);
        Vec3 before = stand.position();
        Vec3 nextPos = strangleOrigin.add(
                strangleDirection.scale(Math.min(traveled + STRANGLE_SPEED, STRANGLE_MAX_DISTANCE)));

        BlockHitResult blockHit = this.getSelf().level().clip(new ClipContext(
                before, nextPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, stand));
        /*if (blockHit.getType() == BlockHitResult.Type.BLOCK) {
            endStrangle();
            return;
        }*/

        stand.setPos(nextPos);
        stand.setYRot(rotation.x);
        stand.setXRot(rotation.y);
        strangleTravelTicks++;

        AABB sweep = stand.getBoundingBox().inflate(0.6).minmax(
                new AABB(before, before).inflate(0.6));
        List<Entity> hits = stand.level().getEntities(stand, sweep);
        for (Entity e : hits) {
            if (e instanceof LivingEntity le
                    && !e.is(this.getSelf())
                    && le.isAlive()
                    && !le.isInvulnerable()
                    && stand.getSensing().hasLineOfSight(le)
                    && le.isAttackable() && le.isPickable() && !(le instanceof StandEntity)
            ) {
                beginStranglePin(stand, le);
                return;
            }
        }

        if (stand.position().distanceTo(this.getSelf().position()) > STRANGLE_MAX_DISTANCE + 4) {
            endStrangle();
        }
    }
    private static final double STRANGLE_STAND_GAP = 0.5;      // distancia entre mob y ph
    private static final double STRANGLE_STAND_Y_OFFSET = 0.1; // numero mas alto, sube un poco mas

    private Vec3 getStrangleStandPos(LivingEntity victim) {
        Vec3 horiz = new Vec3(strangleDirection.x, 0, strangleDirection.z);
        if (horiz.lengthSqr() < 1.0E-4) {

            horiz = new Vec3(victim.getX() - self.getX(), 0, victim.getZ() - self.getZ());
        }
        horiz = horiz.normalize();

        double dist = victim.getBbWidth() * 0.5 + STRANGLE_STAND_GAP;
        return new Vec3(
                victim.getX() - horiz.x * dist,
                strangleBaseY + STRANGLE_STAND_Y_OFFSET,
                victim.getZ() - horiz.z * dist
        );
    }
    private void beginStranglePin(StandEntity stand, LivingEntity victim) {
        this.strangleVictim = victim;
        this.strangleHoldTicks = STRANGLE_HOLD_DURATION;
        this.strangleBaseY = victim.getY();
        this.strangleGrabTicks = 0;

        ((StandUser) victim).roundabout$setRestrainedTicks(20);

        stand.setPos(getStrangleStandPos(victim));
        animateStand(PurpleHazeEntity.STRANGLE_GRAB);

        playSoundIfPossible(self.level(), null, this.self.blockPosition(),
                ModSounds.PURPLE_HAZE_STRANGLE_GRAB_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    private void tickStranglePin(StandEntity stand) {
        if (!strangleVictim.isAlive() || strangleVictim.isRemoved() || strangleHoldTicks <= 0) {
            endStrangle();
            return;
        }

        if (strangleVictim != null) {
            ((StandUser)strangleVictim).roundabout$setRestrainedTicks(20);
        }

        setDazed(strangleVictim, (byte)10);


        if (strangleGrabTicks < STRANGLE_GRAB_TICKS) {
            strangleGrabTicks++;
            if (strangleGrabTicks == STRANGLE_GRAB_TICKS) {
                animateStand(PurpleHazeEntity.STRANGLE_HOLD);
            }
        }
        double lift = STRANGLE_LIFT_HEIGHT * ((double) strangleGrabTicks / STRANGLE_GRAB_TICKS);

        strangleVictim.setPos(strangleVictim.getX(), strangleBaseY + lift, strangleVictim.getZ());
        strangleVictim.setDeltaMovement(Vec3.ZERO);
        strangleVictim.fallDistance = 0;
        strangleVictim.hurtMarked = true;

        stand.setPos(getStrangleStandPos(strangleVictim));

        if (strangleVictim instanceof Mob mob) {
            mob.getNavigation().stop();
            mob.setTarget(null);
        }

        if (strangleHoldTicks % 20 == 0) {
            this.StandDamageEntityAttack(strangleVictim, getStrangleTickDamage(), 0.0F, this.self);
        }
        strangleHoldTicks--;
    }

    private float getStrangleTickDamage() {
        if (this.getReducedDamage(strangleVictim)) {
            return levelupDamageMod(1.0F);
        }
        return levelupDamageMod(3.0F);
    }
    public boolean isStrangling() {
        return this.strangleTicks != -1;
    }
    public boolean isStranglePinning() {
        return this.strangleVictim != null;
    }
    private void endStrangle() {
        if (strangleVictim != null) {
            if (strangleVictim instanceof Mob mob) {
                //mob.setNoAi(false);
            }

            ((StandUser)strangleVictim).roundabout$setRestrainedTicks(0);

        }
        this.strangleGrabTicks = 0;
        this.strangleVictim = null;
        this.strangleTicks = -1;
        this.strangleTravelTicks = 0;
        int cdr = ClientNetworking.getAppropriateConfig().PurpleHazeSettings.StrangleCooldown;
        this.setCooldown(PowerIndex.SKILL_2, cdr);
        if (this.getSelf() instanceof ServerPlayer sp) {
            S2CPacketUtil.sendCooldownSyncPacket(sp, PowerIndex.SKILL_2, cdr);
        }
        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
    }

    private void tickFallingHitAnimation() {
        if (fallingHitAnimTicks < 0 || self.level().isClientSide()) {
            return;
        }

        if (this.getActivePower() != PowerIndex.EXTRA) {
            fallingHitAnimTicks = -1;
            return;
        }

        fallingHitAnimTicks++;

        if (fallingHitAnimTicks == FALLING_HIT_START_TICKS) {
            animateStand(PurpleHazeEntity.FALLINGHIT);
        } else if (fallingHitAnimTicks == FALLING_HIT_START_TICKS + FALLING_HIT_TRANSITION_TICKS) {
            animateStand(PurpleHazeEntity.FALLINGHITLONG);
        }
    }
    @Override
    public boolean canFallBrace() {
        return super.canFallBrace();
    }
    private static final float FALL_BRACE_SHOCKWAVE_RADIUS = 4.0F;
    private static final float FALL_BRACE_SHOCKWAVE_DAMAGE = 3.0F;

    @Override
    public boolean fallBrace() {
        boolean shouldResolve = this.getActivePower() == PowerIndex.EXTRA && this.attackTimeDuring >= 0;

        boolean result = super.fallBrace();

        if (shouldResolve && self != null && !self.level().isClientSide()) {
            float wouldBeDamage = (float) Math.max(fallBraceOriginalDistance - 3.0, 0.0);
            float halfDamage = wouldBeDamage * 0.5F;

            if (halfDamage > 0.05F) {
                self.hurt(ModDamageTypes.of(self.level(), DamageTypes.FALL), halfDamage);
            }

            if (getPods() > 0) {
                if (!(self instanceof Player pl && pl.isCreative())) {
                    setPods(getPods() - 1);
                }
                activatePurpleHazeField(self.position(), indistortionmode);
            }
            fallingHitAnimTicks = -1;
            animateStand(PurpleHazeEntity.FALLINGHITIMPACT);
            playFallBraceShockwave();

            fallBraceOriginalDistance = 0;
        }

        return result;
    }

    private void playFallBraceShockwave() {
        Vec3 pos = self.position();

        int color = PurpleSmokeEntity.SKIN_COLORS.getOrDefault(getStandSkin(), PurpleSmokeEntity.DEFAULT_HAZE_COLOR);

        sendParticlesIfPossible(self.level(),
                HazeColorParticleOptions.fromPackedColor(ModParticles.PURPLE_HAZE_BLASTWAVE, color),
                pos.x, pos.y + 0.05, pos.z,
                1, 0.005, 0.01, 0.005, 0.02);
        /*sendParticlesIfPossible(self.level(),
                ModParticles.PW_BLASTWAVE_EXPLOSION,
                pos.x, pos.y + 0.05, pos.z,
                1, 0.005, 0.01, 0.005, 0.02);*/

        AABB sweep = new AABB(pos, pos).inflate(FALL_BRACE_SHOCKWAVE_RADIUS, 1.5, FALL_BRACE_SHOCKWAVE_RADIUS);
        List<Entity> nearby = self.level().getEntities(self, sweep);
        for (Entity e : nearby) {
            if (e instanceof LivingEntity le && le.isAlive() && !le.isInvulnerable()) {
                StandDamageEntityAttack(le, FALL_BRACE_SHOCKWAVE_DAMAGE, 0.6F, self);
            }
        }
    }
    private double fallBraceOriginalDistance = 0;
    private static final int FALLING_HIT_START_TICKS = 5;  //
    private static final int FALLING_HIT_TRANSITION_TICKS = 40; //
    private int fallingHitAnimTicks = -1;

    @Override
    public boolean fallBraceInit() {
        fallBraceOriginalDistance = this.getSelf().fallDistance;
        boolean result = super.fallBraceInit();
        if (result) {
            poseStand(OffsetIndex.FOLLOW);
            animateStand(PurpleHazeEntity.FALLINGHITSTART);
            if (!this.getSelf().level().isClientSide()) {
                fallingHitAnimTicks = 0;
            }
        }
        return result;
    }


    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("indistortionmode", indistortionmode);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("indistortionmode")) {
            indistortionmode = tag.getBoolean("indistortionmode");
        }
    }
    public boolean indistortionmode(){
        return this.indistortionmode;
    }
    public void activatePurpleHazeField(Vec3 position, boolean distortionMode) {
        if (isClient()) return;

        StandEntity stand = getStandEntity(this.self);
        if (Objects.nonNull(stand)) {
            PurpleSmokeEntity field = ModEntities.PURPLE_SMOKE.create(this.self.level());
            if (field != null) {
                field.setUser(this.self);
                field.setXRot(this.self.getXRot());
                field.setYRot(this.self.getYRot());
                field.setPos(position);
                PowerTypes.copyPlaneOfExisting(self, field);
                field.setDistortionMode(distortionMode);
                field.setStandSkin(getStandSkin());
                field.totalDuration = distortionMode ? DISTORTION_FIELD_DURATION : PURPLE_HAZE_FIELD_DURATION;
                field.lifetime = field.totalDuration;
                PowerTypes.copyPlaneOfExisting(self, field);
                this.self.level().addFreshEntity(field);
            }
        }

        playSoundIfPossible(self.level(), null, BlockPos.containing(position),
                distortionMode ? ModSounds.PURPLE_HAZE_DISTORTION_SMOKE_EVENT : ModSounds.PURPLE_HAZE_SMOKE_EVENT,
                distortionMode ? SoundSource.NEUTRAL : SoundSource.PLAYERS, 4.0F, 1.0F);
    }



    public void attemptThrowPod() {

        ThrowPod();
    }

    public void ThrowPod() {
        if (getPods() <= 0) {
            return;
        }

        if (!this.onCooldown(PowerIndex.SNEAK_ATTACK)) {
            /*playSoundIfPossible(self.level(),
                    null,
                    this.self.blockPosition(),
                    ModSounds.STAR_FINGER_EVENT,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );*/

            PHCapsuleEntity capsule = new PHCapsuleEntity(
                    ModEntities.PH_CAPSULE,
                    self.level()
            );

            capsule.setOwner(self);
            capsule.setUser(self);

            capsule.setPos(
                    self.getX(),
                    self.getEyeY() - 0.1,
                    self.getZ()
            );

            capsule.shootFromRotation(
                    self,
                    self.getXRot(),
                    self.getYRot(),
                    0.0F,
                    0.4F,
                    0.0F
            );

            purpleHazePodDistortionMode = indistortionmode;

            self.level().addFreshEntity(capsule);

            this.purpleHazePod = capsule;
            if (!(self instanceof Player pl && pl.isCreative())) {
                setPods(getPods() - 1);
            }


            PowerTypes.copyPlaneOfExisting(self, capsule);

            int cdr = ClientNetworking.getAppropriateConfig().PurpleHazeSettings.PodThrowCooldown;
            this.setCooldown(PowerIndex.SNEAK_ATTACK, cdr);
            playSoundIfPossible(self.level(), null, this.self.blockPosition(),
                    ModSounds.PURPLE_HAZE_POD_LAUNCH_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    public void onPodLanded(Vec3 landingPosition) {
        if (purpleHazePod == null) return;
        boolean mode = purpleHazePodDistortionMode;
        purpleHazePod = null;
        activatePurpleHazeField(landingPosition, mode);
    }


    @Override
    public void TieClash(LivingEntity user1, LivingEntity user2) {
        super.TieClash(user1, user2);
        if (getPods() > 0) {
            if (!(self instanceof Player pl && pl.isCreative())) {
                setPods(getPods() - 1);
            }
            activatePurpleHazeField(user2.position(), indistortionmode);
        }
    }

    @Override
    public void breakClash(LivingEntity winner, LivingEntity loser){
        super.breakClash(winner, loser);
        if (getPods() > 0) {
            if (!(self instanceof Player pl && pl.isCreative())) {
                setPods(getPods() - 1);
            }
            activatePurpleHazeField(loser.position(), indistortionmode);
        }
    }
    @Override
    public void tickMobAI(LivingEntity attackTarget) {
        if (tickGenericFallBraceAI()) {
            return;
        }
        if (attackTarget != null && attackTarget.isAlive() && !this.isDazed(this.getSelf())) {
            boolean isCreeper = this.getSelf() instanceof Creeper;
            if (isCreeper) {
                double dist = attackTarget.distanceTo(this.getSelf());
                if (!isStrangling()
                        && this.getActivePower() == PowerIndex.NONE
                        && !onCooldown(PowerIndex.SKILL_2)
                        && dist <= 10
                        && this.getSelf().hasLineOfSight(attackTarget)) {
                    float yrot = getLookAtEntityYaw(this.getSelf(), attackTarget);
                    this.getSelf().setXRot(getLookAtEntityPitch(this.getSelf(), attackTarget));
                    this.getSelf().setYRot(yrot);
                    this.getSelf().setYHeadRot(yrot);
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2, true);
                }
            } else {
                super.tickMobAI(attackTarget);
            }
        }
    }
    @Override
    public void tickPower() {
        if (purpleHazeFieldGone) {
            purpleHazeFieldGone = false;
        }
        if (self == null) {
            return;
        }
        if (!self.level().isClientSide()
                && isStrangling()
                && this.getActivePower() != PowerIndex.POWER_2) {
            endStrangle();
        }
        super.tickPower();
        if (this.forwardBarrage && !(this.isBarraging() || activePower == PowerIndex.BARRAGE_CHARGE_2 || activePower == PowerIndex.BARRAGE_2)){
            this.forwardBarrage = false;
            forwardBarrageLockedRot = null;
        }
        if (!self.level().isClientSide) {
            tickPodReset();
            tickPodRecharge();
            tickSecretSkinObtaining();
            tickFallingHitAnimation();

            if (!podsSyncedOnJoin && self instanceof ServerPlayer sp) {
                podsSyncedOnJoin = true;
                S2CPacketUtil.syncPurpleHazePods(sp, (byte) getPods());
            }
        }

        if (capsuleEatingTick > 0) {
            capsuleEatingTick--;

            if (capsuleEatingTick == 0) {
                if (self instanceof ServerPlayer pl) {
                    this.setAttackTimeDuring(0);
                    ((IPlayerEntity) pl).roundabout$SetPoseEmote((byte) 0);
                }
            }
        }

        if (this.getSelf().onGround()){
            if (((StandUser)this.getSelf()).roundabout$getLeapTicks() <= -1) {
                if (this.getAnimation() == StandEntity.LEAP) {
                    animateStand(StandEntity.LEAP_END);
                }
            }
        }
    }
    public boolean purpleHazeFieldGone = false;

    public void serverEndPurpleHazeField(){
        purpleHazeFieldGone = true;
    }

    public byte getStandSkin(){
        return standSkin;
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
    public byte chooseBarrageSound(){ return SoundIndex.BARRAGE_CRY_SOUND;}
    protected SoundEvent getBarrageSound() {

        return ModSounds.PURPLE_HAZE_BARRAGE_CRY_EVENT;
    }

    protected SoundEvent getKickBarrageSound() {

        return ModSounds.PURPLE_HAZE_KICK_BARRAGE_CRY_EVENT;
    }


    @Override
    public void playBarrageEndNoise(float mod, Entity entity) {
        if (!this.self.level().isClientSide()) {
            playSoundIfPossible(self.level(), null, this.self.blockPosition(), ModSounds.PURPLE_HAZE_BARRAGE_FINISHER_EVENT, SoundSource.PLAYERS, 0.85F + mod, 1F);
        }
    }

    @Override
    public void playBarrageNoise(int hitNumber, Entity entity) {
        if (!this.self.level().isClientSide()) {
            if (hitNumber % 2 == 0)
                playSoundIfPossible(self.level(), null, this.self.blockPosition(), ModSounds.PURPLE_HAZE_BARRAGE_HIT_EVENT, SoundSource.PLAYERS, 0.7F, 1/*(float) (0.9 + (Math.random() * 0.25))*/);
        }
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.PURPLE_HAZE_SUMMON_EVENT;
        } else if (soundChoice == SoundIndex.BARRAGE_CRY_SOUND) {
            return getBarrageSound();
        } else if (soundChoice == SoundIndex.ALT_CHARGE_SOUND_1) {
            return ModSounds.STAND_BARRAGE_WINDUP_EVENT;
        } else if (soundChoice == SoundIndex.BARRAGE_CRY_SOUND_2) {
            return getKickBarrageSound();
        }
        return super.getSoundFromByte(soundChoice);
    }
    @Override
    public byte getSoundCancelingGroupByte(byte soundChoice) {
        if (soundChoice == SoundIndex.BARRAGE_CRY_SOUND || soundChoice == SoundIndex.BARRAGE_CRY_SOUND_2) { return SoundIndex.BARRAGE_SOUND_GROUP; }
        return super.getSoundCancelingGroupByte(soundChoice);
    }


    //@Override public SoundEvent getBarrageChargeSound() { return ModSounds.PURPLE_HAZE_BARRAGE_WINDUP_EVENT; }

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
