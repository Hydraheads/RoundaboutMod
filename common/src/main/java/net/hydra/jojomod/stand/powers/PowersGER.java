package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.ThrownObjectEntity;
import net.hydra.jojomod.entity.stand.GoldExperienceEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.SavedSecond;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class PowersGER extends PowersGoldExperience {
    public PowersGER(LivingEntity self) {
        super(self);
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersGER(entity);
    }

    @Override
    public StandEntity getNewStandEntity() {
        return ModEntities.GOLD_EXPERIENCE_REQUIEM.create(this.getSelf().level());
    }

    public boolean canUseRequiem() {
        return false;
    }

    public boolean isRequiemStand() {
        return true;
    }

    ItemStack originalDisc;

    public boolean hasNormalGEDisc() {
        return originalDisc != null && !originalDisc.isEmpty();
    }

    public void setOriginalDisc(ItemStack disc) {
        originalDisc = disc;
        saveDiscAndSync();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        if (originalDisc != null) {
            CompoundTag compoundtag = new CompoundTag();

            if (self instanceof Player PE && !(originalDisc.getItem() instanceof MaxStandDiscItem)) {
                IPlayerEntity IPE = ((IPlayerEntity) PE);
                if (originalDisc.getItem() instanceof StandDiscItem SD && !SD.standPowers.isSecondaryStand()) {
                    originalDisc.getOrCreateTagElement("Memory").putByte("Level",
                            (byte) Math.max(IPE.roundabout$getStandLevel() - 1, 0));
                    originalDisc.getOrCreateTagElement("Memory").putInt("Experience", IPE.roundabout$getStandExp());
                    originalDisc.getOrCreateTagElement("Memory").putBoolean("BonusSkin", IPE.roundabout$getUnlockedBonusSkin());
                }
            }

            tag.put("roundabout.originalDisc", originalDisc.save(compoundtag));
        }
        super.addAdditionalSaveData(tag);
    }

    protected boolean giveArrowOnLoad() {
        return false; // prevent from the requiem transformation giving this arrow back before it is time
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("roundabout.originalDisc", 10)) {
            CompoundTag compoundtag = tag.getCompound("roundabout.originalDisc");
            originalDisc = ItemStack.of(compoundtag);
        }

        super.readAdditionalSaveData(tag);
    }

    @Override
    public boolean replaceHudActively() {
        return hasNormalGEDisc();
    }

    public void goBackToGE(){
        setPowerNone();

        ((StandUser) self).roundabout$setStandDisc(originalDisc);
        ((StandUser) self).roundabout$getStandPowers().onStandSwitchInto();
        if (((StandUser) self).roundabout$getStandPowers() instanceof PowersGoldExperience GER) {
            GER.idlePos = idlePos;
            GER.standSkin = standSkin;
            GER.resolvePoints = resolvePoints;

            GER.saveDiscAndSync();
        }

        if (((StandUser) self).roundabout$getStand() != null) {
            ((StandUser) self).roundabout$getStand().discard();
            ((StandUser) self).roundabout$setStand(null);
        }
        if (standArrow != null && !standArrow.isEmpty()) {
            giveStandArrowBack();
        }
    }

    public void tickPower() {
        super.tickPower();
        if (hasNormalGEDisc()) {
            resolvePoints -= 0.25f;
            if (resolvePoints <= 0f) {
                goBackToGE();
                ((StandUser) self).roundabout$summonStand(self.level(), true, true);
            }
        }
    }

    @Override
    public boolean interceptIncomingHarm(DamageSource source, float amount) {
        Entity direct = source.getDirectEntity();
        Entity shooter = source.getEntity();
        Entity culprit = shooter != null ? shooter : direct;

        if (culprit != null) {
            if (direct != null && direct != shooter && direct instanceof IEntityAndData directData) {
                SavedSecond projectileSecond = directData.roundabout$getLastSavedSecond();
                if (projectileSecond != null) {
                    projectileSecond.loadTime(direct);
                }
            }

            if (culprit instanceof LivingEntity livingCulprit) {
                IEntityAndData iData = (IEntityAndData) livingCulprit;
                SavedSecond lastSecond = iData.roundabout$getLastSavedSecond();
                if (lastSecond != null) {
                    Vec3 startPos = livingCulprit.position();
                    float startYaw = livingCulprit.getYRot();
                    float startPitch = livingCulprit.getXRot();

                    lastSecond.resetHealth = false;
                    lastSecond.loadTime(livingCulprit);
                    setDazed(livingCulprit, (byte) 60);
                    ((StandUser)livingCulprit).roundabout$sealStand(60);

                    if (livingCulprit instanceof ServerPlayer victim) {
                        Vec3 endPos = victim.position();
                        float endYaw = victim.getYRot();
                        float endPitch = victim.getXRot();
                        S2CPacketUtil.sendStartRTZPacket(
                                victim,
                                startPos,
                                endPos,
                                startYaw,
                                startPitch,
                                endYaw,
                                endPitch
                        );
                    }
                }
                if (livingCulprit.isUsingItem()) {
                    livingCulprit.stopUsingItem();
                }
                if (livingCulprit instanceof Mob mb && !MainUtil.isBossMob(mb)) {
                    mb.getNavigation().stop();
                    if (!MainUtil.blockConfusionTicks(mb)) {
                        ((IMob) mb).roundabout$setConfusionTicks(60);
                    }
                }
            }

            return true;
        }

        return super.interceptIncomingHarm(source, amount);
    }

    // infinite death start

    public boolean onKilledEntity(ServerLevel $$0, LivingEntity victim) {
        if ( victim.getLastDamageSource() != null &&  victim.getLastDamageSource().is(ModDamageTypes.GER_STAND)) {
            if (victim instanceof ServerPlayer serverPlayer) {
                ((IPlayerEntity) serverPlayer).rdbt$setPendingGerCutscene(true);
            }
        }
        return super.onKilledEntity($$0, victim);
    }

    // infinite death end

    // Buffs

    // for item throw (raw spaghetti laser beam) start

    @Override
    public boolean canSnipe() {
        return true;
    }

    @Override
    public float getShotAccuracy() {
        return 0.0F;
    }

    @Override
    public float getThrowAngle() {
        return 0.0F;
    }

    @Override
    public float getThrowAngle2() {
        return 0.0F;
    }

    @Override
    public byte getThrowStyleType(){
        return ThrownObjectEntity.GERTHROW;
    }

    // for item throw (raw spaghetti laser beam) start

    // for damage start

    @Override
    public float getPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 2.55F;
        } else {
            return 6;
        }
    }

    @Override
    public float getHeavyPunchStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 2.6F;
        } else {
            return 8;
        }
    }
    @Override
    public float getBarrageFinisherStrength(Entity entity){
        if (this.getReducedDamage(entity)){
            return 3.8F;
        } else {
            return 8;
        }
    }
    @Override
    public float getBarrageDamagePlayer(){
        return 8;
    }
    @Override
    public float getBarrageDamageMob(){
        return 19;
    }

    private float attackSpeedModifier() {return 0.65f; } /// faster than Silver Chariot

    @Override
    public boolean setPowerAttack() {
        //dogael forgot to add the item throw check
        StandEntity standEntity = ((StandUser) this.getSelf()).roundabout$getStand();
        if (standEntity != null && standEntity.isAlive() && !standEntity.isRemoved()
                && (!standEntity.getHeldItem().isEmpty() || standEntity.getFirstPassenger() != null)) {
            return super.setPowerAttack();
        }

        if (this.activePowerPhase >= 3) {
            this.activePowerPhase = 1;
        } else {
            this.activePowerPhase++;
            if (this.activePowerPhase == 3) {
                this.attackTimeMax= Math.round(ClientNetworking.getAppropriateConfig().generalStandSettings.finalStandPunchInStringCooldown*attackSpeedModifier())
                        + (getMeltLevel()*3) + getExtraPunchTime();
            } else {
                this.attackTimeMax= Math.round(ClientNetworking.getAppropriateConfig().generalStandSettings.standPunchCooldown*attackSpeedModifier())
                        + (getMeltLevel()*3) + getExtraPunchTime();
            }

        }

        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.ATTACK);
        this.setAttackTime(0);

        animateStand(this.activePowerPhase);
        poseStand(OffsetIndex.ATTACK);
        return true;
    }

    // for damage end

    // for sounds start

    @Override
    public SoundEvent getBarrageChargeSound() {
        if (!PowerTypes.hasStandActive(self)) {
            return super.getBarrageChargeSound();
        }
        return ModSounds.GER_BARRAGE_WINDUP_EVENT;
    }

    @Override
    public void playBarrageNoise(int hitNumber, Entity entity) {
        if (!this.self.level().isClientSide()) {
            if (hitNumber % 3 == 0)
                playSoundIfPossible(self.level(), null, this.self.blockPosition(), ModSounds.GER_BARRAGE_HIT_EVENT, SoundSource.PLAYERS, 1.3F, 1F);
        }
    }

    @Override
    public float getBarrageChargePitch() {
        if (!PowerTypes.hasStandActive(self)) {
            return super.getBarrageChargePitch();
        }
        return 1.0F;
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        if (soundChoice == BARRAGE_NOISE) {
            return ModSounds.GER_MUDA_RUSH_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    // for sounds end

    // for skins start

    @Override
    /*The stand is named on the disc so we just use that*/
    public Component getStandName(){
        ItemStack disc = ((StandUser)this.getSelf()).roundabout$getStandDisc();
        if (!disc.isEmpty() && disc.getItem() instanceof StandDiscItem SDI){
            return Component.translatable(SDI.getDescriptionId() + ".desc.short");
        }
        return Component.empty();
    }

    // for skins end

    // for icons start

    @Override
    public ResourceLocation getIconYes(int slot) {
        return StandIcons.SQUARE_REQUIEM;
    }

    // for icons end

    // for particles start

    @Override
    public SimpleParticleType getImpactParticle() {
        SimpleParticleType punchpart;
        float random = (float) (Math.random()*3);
        if (random > 2){
            punchpart = ModParticles.REQUIEM_PUNCH_IMPACT_A;
        } else if (random > 1){
            punchpart = ModParticles.REQUIEM_PUNCH_IMPACT_B;
        } else {
            punchpart = ModParticles.REQUIEM_PUNCH_IMPACT_C;
        }
        return punchpart;
    }

    // for particles end
}