package net.hydra.jojomod.mixin.fates;

import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.VampireData;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.access.AccessFateFoodData;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.fates.powers.VampiricFate;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Player.class, priority = 100)
public abstract class FatePlayerMixin extends LivingEntity implements IFatePlayer {

    @Shadow public abstract FoodData getFoodData();

    @Shadow public abstract void displayClientMessage(Component component, boolean bl);

    @Unique
    public FatePowers rdbt$fatePowers = null;

    @Unique
    public byte rdbt$lastFate = (byte) FateTypes.HUMAN.ordinal();

    @Unique
    @Override
    public FatePowers rdbt$getFatePowers(){
        if (rdbt$lastFate != ((IPlayerEntity)this).roundabout$getFate()){
            rdbt$lastFate = ((IPlayerEntity)this).roundabout$getFate();
            rdbt$fatePowers = null;
        }
        if (rdbt$fatePowers == null){
            rdbt$fatePowers = FateTypes.getFateFromByte(((IPlayerEntity)this).roundabout$getFate()).fatePowers.generateFatePowers(this);
        }
        return rdbt$fatePowers;
    }
    @Inject(method = "playStepSound", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$playStepSound(BlockPos $$0, BlockState $$1, CallbackInfo ci) {
        if (rdbt$getFatePowers() instanceof VampiricFate VP && VP.isPlantedInWall()){
            if (this.isInWater()) {
                this.waterSwimSound();
            } else {
                BlockPos $$2 = this.getPrimaryStepSoundBlockPos($$0);
                if (!$$0.equals($$2)) {
                    BlockState $$3 = this.level().getBlockState($$2);
                    if ($$3.is(BlockTags.COMBINATION_STEP_SOUND_BLOCKS)) {
                        SoundType st = $$1.getSoundType();
                        this.playSound(st.getBreakSound(), st.getVolume(), st.getPitch());
                    } else {
                        SoundType st = $$1.getSoundType();
                        this.playSound(st.getBreakSound(), st.getVolume(), st.getPitch());
                    }
                } else {
                    SoundType st = $$1.getSoundType();
                    BlockState $$3 = this.level().getBlockState($$2);
                    super.playStepSound($$2, $$3);
                    this.playSound(st.getBreakSound(), st.getVolume(), st.getPitch());
                }
            }
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    protected void roundabout$Tick(CallbackInfo ci) {
        if (this.isAlive() && !this.isRemoved()) {
            ((AccessFateFoodData) getFoodData()).rdbt$setPlayer((Player) (Object) this);
            if (!this.level().isClientSide()) {
                rdbt$tickThroughVampire();
                rdbt$tickThroughVampireChange();
            }
        }
    }
    /**tick through transformation sequences*/

    @Unique
    public int rdbt$vampireTransformation = -1;

    @Unique
    @Override
    public void rdbt$startVampireTransformation(boolean mask){
        //starts the animation
        rdbt$vampireTransformation = 0;
        if (mask){
            ((StandUser)this).roundabout$setDazed((byte) 120);
            level().playSound(null, this, ModSounds.STONE_MASK_ACTIVATE_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
        } else {
            ((StandUser)this).roundabout$setDazed((byte) 20);
            rdbt$vampireTransformation = 100;
            ((IPlayerEntity)this).roundabout$SetPoseEmote((byte)12);
            level().playSound(null, this, ModSounds.VAMPIRE_AWAKEN_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @Unique
    @Override
    public boolean rdbt$isTransforming(){
        return rdbt$vampireTransformation > -1 ||
                (FateTypes.isHuman(this) && MainUtil.isWearingBloodyStoneMask(this));
    }
    @Unique
    public void rdbt$tickThroughVampire(){
        if (FateTypes.takesSunlightDamage(this)){
            if (FateTypes.isInSunlight(this)){
                this.hurt(ModDamageTypes.of(this.level(), ModDamageTypes.SUNLIGHT), this.getMaxHealth()*ClientNetworking.getAppropriateConfig().vampireSettings.sunDamagePercentPerDamageTick);
            }
        } else if (FateTypes.isHuman(this)){
            if (MainUtil.isWearingBloodyStoneMask(this) && rdbt$vampireTransformation < 0){
                rdbt$startVampireTransformation(true);
            }
        }
        if (MainUtil.isWearingStoneMask(this) && hasEffect(ModEffects.BLEED)){
            MainUtil.activateStoneMask(this);
        }
        if (FateTypes.isVampire(this)){
            VampireData vdata = ((IPlayerEntity)this).rdbt$getVampireData();
            if (vdata.timeSinceNpc > 0){
                vdata.timeSinceNpc--;
                if (vdata.timeSinceNpc == 0){
                    vdata.npcExp = 0;
                    S2CPacketUtil.beamVampireData2((Player) (Object)this);
                } else {
                    rdbt$changed = true;
                }
            }
            if (vdata.timeSinceAnimal > 0){
                vdata.timeSinceAnimal--;
                if (vdata.timeSinceAnimal == 0){
                    vdata.animalExp = 0;
                    S2CPacketUtil.beamVampireData2((Player) (Object)this);
                } else {
                    rdbt$changed = true;
                }
            }
            if (vdata.timeSinceMonster > 0){
                vdata.timeSinceMonster--;
                if (vdata.timeSinceMonster == 0){
                    vdata.monsterEXP = 0;
                    S2CPacketUtil.beamVampireData2((Player) (Object)this);
                } else {
                    rdbt$changed = true;
                }
            }
            if (tickCount % 20 == 1){
                S2CPacketUtil.beamVampireTimings((Player) (Object)this);
            }
        }


        if (ClientNetworking.getAppropriateConfig().vampireSettings.vampireUsesPotionEffectForNightVision) {
            if (FateTypes.canSeeInTheDark(this)) {
                if (!hasEffect(MobEffects.NIGHT_VISION)) {
                    addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, -1, 20, false, false), null);
                }
            } else {
                MobEffectInstance ME = getEffect(MobEffects.NIGHT_VISION);
                if (ME != null && ME.isInfiniteDuration() && ME.getAmplifier() == 20) {
                    removeEffect(MobEffects.NIGHT_VISION);
                }
            }
        } else {
            MobEffectInstance ME = getEffect(MobEffects.NIGHT_VISION);
            if (ME != null && ME.isInfiniteDuration() && ME.getAmplifier() == 20) {
                removeEffect(MobEffects.NIGHT_VISION);
            }
        }

        //This can move into a dedicated fate class eventually
    }
    public boolean rdbt$changed = false;
    @Unique
    public void rdbt$tickThroughVampireChange(){
        if (rdbt$vampireTransformation >= 0){
            rdbt$vampireTransformation++;
            if (rdbt$vampireTransformation > 120){
                rdbt$vampireTransformation = -1;
                if (FateTypes.isHuman(this)){
                    MainUtil.popOffStoneMask(this);
                    FateTypes.setVampire(this);
                    displayClientMessage(Component.translatable("item.roundabout.stand_arrow.acquireVampire1").withStyle(ChatFormatting.WHITE).withStyle(ChatFormatting.BOLD), true);
                    (((IPlayerEntity)this)).roundabout$qmessage(2);
                    if (hasEffect(ModEffects.BLEED)){
                        removeEffect(ModEffects.BLEED);
                    }
                }
                //12 is the id of vampire stone mask transformation animation in Poses.java
                if (((IPlayerEntity)this).roundabout$GetPoseEmote() == 12){
                    ((IPlayerEntity)this).roundabout$SetPoseEmote((byte)0);
                }
            } else if (rdbt$vampireTransformation > 13){
                if (rdbt$vampireTransformation == 14){
                    //12 is the id of vampire stone mask transformation animation in Poses.java
                    ((IPlayerEntity)this).roundabout$SetPoseEmote((byte)12);
                }
                rdbt$scatterTransformationParticles();
            }
            markHurt();
            hasImpulse = true;
            Vec3 getTransformVec = RotationUtil.vecPlayerToWorld(new Vec3(0,-0.1,0),((IGravityEntity)this).roundabout$getGravityDirection());
            Vec3 dm = getDeltaMovement().add(getTransformVec.x,getTransformVec.y,getTransformVec.z);
            setDeltaMovement(dm);
        }
    }
    @Unique
    public void rdbt$scatterTransformationParticles(){
        for (int i = 0; i < 7; ++i) {
            double randomX = (Math.random() * 0.5) - 0.25;
            double randomY = (Math.random() * 0.5) - 0.25;
            double randomZ = (Math.random() * 0.5) - 0.25;

            Vec3 vecpos = getEyePosition();
            ((ServerLevel) this.level()).sendParticles(ModParticles.BLUE_SPARKLE,
                    vecpos.x,vecpos.y,vecpos.z,
                    0, randomX, randomY, randomZ, 3.5);
        }
    }
    //((IPlayerEntity) this.getSelf()).roundabout$SetPos(PlayerPosIndex.DODGE_BACKWARD);

    protected FatePlayerMixin(EntityType<? extends LivingEntity> $$0, Level $$1) {
        super($$0, $$1);
    }
}
