package net.hydra.jojomod.mixin.fates;

import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.access.AccessFateFoodData;
import net.hydra.jojomod.fates.FatePowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.http.client.utils.DateUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class FatePlayerMixin extends LivingEntity implements IFatePlayer {

    @Shadow public abstract FoodData getFoodData();

    @Unique
    public FatePowers rdbt$fatePowers = null;

    @Unique
    public FatePowers rdbt$getFatePowers(){
        if (rdbt$fatePowers == null){
            FateTypes.getFateFromByte(((IPlayerEntity)this).roundabout$getFate()).fatePowers.generateFatePowers(this);
        }
        return rdbt$fatePowers;
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    protected void roundabout$Tick(CallbackInfo ci) {
        ((AccessFateFoodData)getFoodData()).rdbt$setPlayer((Player) (Object) this);
        if (!this.level().isClientSide()) {
            rdbt$tickThroughVampire();
            rdbt$tickThroughVampireChange();
        }
    }
    /**tick through transformation sequences*/

    @Unique
    public int rdbt$vampireTransformation = -1;

    @Unique
    @Override
    public void rdbt$startVampireTransformation(){
        //starts the animation
        rdbt$vampireTransformation = 0;
        ((StandUser)this).roundabout$setDazed((byte) 120);
        level().playSound(null, this, ModSounds.STONE_MASK_ACTIVATE_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Unique
    @Override
    public boolean rdbt$isTransforming(){
        return rdbt$vampireTransformation > -1 ||
                (FateTypes.isHuman(this) && MainUtil.isWearingBloodyStoneMask(this));
    }
    @Unique
    public void rdbt$tickThroughVampire(){
        if (FateTypes.hasBloodHunger(this)){
            if (FateTypes.isInSunlight(this)
            ){
                this.hurt(ModDamageTypes.of(this.level(), ModDamageTypes.SUNLIGHT), this.getMaxHealth()*ClientNetworking.getAppropriateConfig().vampireSettings.sunDamagePercentPerDamageTick);
            }
        } else if (FateTypes.isHuman(this)){
            if (MainUtil.isWearingBloodyStoneMask(this) && rdbt$vampireTransformation < 0){
                rdbt$startVampireTransformation();
            }
        }
        if (MainUtil.isWearingStoneMask(this) && hasEffect(ModEffects.BLEED)){
            MainUtil.activateStoneMask(this);
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
    @Unique
    public void rdbt$tickThroughVampireChange(){
        if (rdbt$vampireTransformation >= 0){
            rdbt$vampireTransformation++;
            if (rdbt$vampireTransformation > 120){
                rdbt$vampireTransformation = -1;
                if (FateTypes.isHuman(this)){
                    MainUtil.popOffStoneMask(this);
                    FateTypes.setVampire(this);
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

    /**You cannot get hurt while transformed*/
    @Inject(method = "hurt", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$hurt(DamageSource $$0, float $$1, CallbackInfoReturnable<Boolean> cir) {
        if (!this.level().isClientSide()) {
            if (rdbt$vampireTransformation >= 0){
                cir.setReturnValue(false);
                return;
            }
        }
    }

    protected FatePlayerMixin(EntityType<? extends LivingEntity> $$0, Level $$1) {
        super($$0, $$1);
    }
}
