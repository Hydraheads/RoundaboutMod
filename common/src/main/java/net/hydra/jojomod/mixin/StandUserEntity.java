package net.hydra.jojomod.mixin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.*;
import net.hydra.jojomod.block.BarbedWireBlock;
import net.hydra.jojomod.block.FogBlock;
import net.hydra.jojomod.block.GoddessStatueBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.projectile.MatchEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.PermanentZoneCastInstance;
import net.hydra.jojomod.event.SoftExplosion;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.*;
import net.hydra.jojomod.stand.powers.*;
import net.hydra.jojomod.stand.powers.PowersJustice;
import net.hydra.jojomod.stand.powers.PowersMagiciansRed;
import net.hydra.jojomod.item.*;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Mixin(LivingEntity.class)
public abstract class StandUserEntity extends Entity implements StandUser {
    @Shadow public abstract ItemStack getItemInHand(InteractionHand $$0);

    @Shadow public float zza;
    @Shadow public float xxa;

    @Shadow public abstract void setLastHurtMob(Entity $$0);

    @Shadow public abstract void setLastHurtByPlayer(@Nullable Player $$0);

    @Shadow public abstract void setLastHurtByMob(@Nullable LivingEntity $$0);

    @Shadow public abstract boolean onClimbable();

    @Shadow protected abstract Vec3 handleOnClimbable(Vec3 $$0);

    @Shadow protected abstract float getFrictionInfluencedSpeed(float $$0);

    @Shadow public abstract Map<MobEffect, MobEffectInstance> getActiveEffectsMap();

    @Shadow public abstract boolean removeEffect(MobEffect $$0);

    @Shadow public abstract boolean addEffect(MobEffectInstance $$0);

    @Shadow public abstract boolean addEffect(MobEffectInstance $$0, @Nullable Entity $$1);

    @Shadow protected abstract void tickDeath();

    @Shadow protected boolean dead;

    @Shadow public abstract float getYHeadRot();

    @Shadow public float yHeadRot;
    @Shadow public float yBodyRot;
    @Shadow protected double lerpY;
    @Shadow protected double lerpZ;
    @Shadow protected double lerpX;

    @Shadow public abstract float getHealth();

    @Shadow public abstract float getMaxHealth();

    @Shadow
    public abstract boolean hurt(DamageSource $$0, float $$1);

    @Shadow
    public abstract void indicateDamage(double $$0, double $$1);

    @Shadow
    public abstract void heal(float $$0);

    @Shadow
    public abstract boolean isAlive();

    @Unique
    public int roundabout$remainingFireTicks = -1;
    @Unique
    public LivingEntity roundabout$fireStarter;
    @Unique
    public SoftAndWetPlunderBubbleEntity roundabout$eyeSightTaken;
    @Unique
    public int roundabout$fireStarterID;

    @Shadow
    @javax.annotation.Nullable
    public abstract MobEffectInstance getEffect(MobEffect $$0);

    @Shadow
    protected boolean jumping;

    @Shadow
    private float speed;
    @Unique
    private int roundabout$leapTicks = -1;
    @Unique
    private int roundabout$destructionModeTrailTicks = -1;
    @Unique
    private int roundabout$detectTicks = -1;

    public StandUserEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Shadow
    protected abstract int increaseAirSupply(int $$0);

    @Shadow
    @Final
    private Map<MobEffect, MobEffectInstance> activeEffects;

    /**
     * If you are stand guarding, this controls you blocking enemy atttacks.
     * For the damage against stand guard, and sfx, see PlayerEntity mixin
     * damageShield
     */
    @Shadow
    public boolean hasEffect(MobEffect $$0) {
        return this.activeEffects.containsKey($$0);
    }

    @Unique
    private final LivingEntity roundabout$User = ((LivingEntity) (Object) this);
    @Nullable
    @Unique
    private StandEntity roundabout$Stand;

    /*Mob throw keeps track of the thrower to deal damage in their name*/
    @Nullable
    @Unique
    private LivingEntity roundabout$thrower;

    /*Emulator is specifically for ZPlayerRenderer justice disguise spoofing and other mob spoofing, for rendering purposes of reading data from the user*/
    @Nullable
    @Unique
    private LivingEntity roundabout$emulator;
    @Nullable
    @Override
    @Unique
    public LivingEntity roundabout$getEmulator(){
        return roundabout$emulator;
    }

    @Override
    @Unique
    public void roundabout$setEmulator(LivingEntity le){
        roundabout$emulator = le;
    }
    @Nullable
    @Override
    @Unique
    public LivingEntity roundabout$getFireStarter(){
        return roundabout$fireStarter;
    }

    @Override
    @Unique
    public void roundabout$setFireStarter(LivingEntity le){
        roundabout$fireStarter = le;
    }
    @Override
    @Unique
    public int roundabout$getFireStarterID(){
        return roundabout$fireStarterID;
    }

    @Override
    @Unique
    public void roundabout$setFireStarterID(int le){
        roundabout$fireStarterID = le;
    }

    @Nullable
    @Unique
    private ImmutableList<FollowingStandEntity> roundabout$followers = ImmutableList.of();

    /**
     * StandID is used clientside only
     */

    @Unique
    private static final EntityDataAccessor<Integer> ROUNDABOUT$STAND_ID = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Boolean> ROUNDABOUT$STAND_ACTIVE = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Byte> ROUNDABOUT_TS_DAMAGE = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.BYTE);
    @Unique
    private static final EntityDataAccessor<Byte> ROUNDABOUT$LOCACACA_CURSE = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.BYTE);
    @Unique
    private static final EntityDataAccessor<Byte> ROUNDABOUT$ON_STAND_FIRE = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.BYTE);
    @Unique
    private static final EntityDataAccessor<Byte> ROUNDABOUT$GLOW = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.BYTE);
    @Unique
    private static final EntityDataAccessor<Integer> ROUNDABOUT$BLEED_LEVEL = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> ROUNDABOUT$IS_BOUND_TO = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> ROUNDABOUT$IS_ZAPPED_TO_ATTACK = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> ROUNDABOUT$ADJUSTED_GRAVITY = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Byte> ROUNDABOUT$IS_BUBBLE_ENCASED = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.BYTE);
    @Unique
    private static final EntityDataAccessor<Boolean> ROUNDABOUT$ONLY_BLEEDING = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.BOOLEAN);

    @Unique
    private static final EntityDataAccessor<String> ROUNDABOUT$STAND_DISC = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.STRING);

    public ItemStack roundabout$standDisc = ItemStack.EMPTY;
    @Unique
    private static final EntityDataAccessor<Boolean> ROUNDABOUT$COMBAT_MODE = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.BOOLEAN);
    @Unique
    private StandPowers roundabout$Powers;
    @Unique
    private StandPowers roundabout$RejectionStandPowers = null;
    @Unique
    private ItemStack roundabout$RejectionStandDisc = null;

    /**
     * Guard variables for stand blocking
     **/
    @Unique
    private float roundabout$GuardPoints = 10;
    @Unique
    private boolean roundabout$GuardBroken = false;
    @Unique
    private int roundabout$GuardCooldown = 0;

    @Unique
    public boolean roundabout$blip = false;

    @Unique
    public Vector3f roundabout$blipVector;

    @Unique
    private int roundabout$gasTicks = -1;
    @Unique
    private int roundabout$gasRenderTicks = -1;

    /**
     * Idle time is how long you are standing still without using skills, eating, or
     */
    @Unique
    private int roundabout$IdleTime = -1;

    @Unique
    @Override
    public int roundabout$getIdleTime() {
        return this.roundabout$IdleTime;
    }

    @Unique
    @Override
    public void roundabout$setIdleTime(int roundaboutIdleTime) {
        this.roundabout$IdleTime = roundaboutIdleTime;
    }

    @Unique
    @Override
    public void roundabout$setThrower(LivingEntity thrower) {
        this.roundabout$thrower = thrower;
    }
    @Unique
    @Override
    public void roundabout$setEyeSightTaken(SoftAndWetPlunderBubbleEntity bubble) {
        this.roundabout$eyeSightTaken = bubble;
        if (((LivingEntity)(Object)this) instanceof Player PL){
            if (bubble == null){
                ((IPlayerEntity) PL).roundabout$setBlinded(false);
            } else {
                ((IPlayerEntity) PL).roundabout$setBlinded(true);
            }
        }
    }
    @Unique
    @Override
    public SoftAndWetPlunderBubbleEntity roundabout$getEyeSightTaken() {
        return this.roundabout$eyeSightTaken;
    }

    @Unique
    @Override
    public void roundabout$setBlip(Vector3f vec) {
        this.roundabout$blip = true;
        this.roundabout$blipVector = vec;
    }

    @Unique
    @Override
    public LivingEntity roundabout$getThrower() {
        return this.roundabout$thrower;
    }
    @Unique
    @Override
    public void roundabout$setSecondsOnStandFire(int $$0) {
        int $$1 = $$0 * 20;

        if (roundabout$remainingFireTicks < $$1) {
            roundabout$setRemainingStandFireTicks($$1);
        }
    }
    @Unique
    @Override
    public void roundabout$setRemainingStandFireTicks(int $$0) {
        roundabout$remainingFireTicks = $$0;
    }

    @Unique
    @Override
    public int roundabout$getRemainingFireTicks() {
        return roundabout$remainingFireTicks;
    }
    /**
     * These variables control if someone is dazed, stunned, frozen, or controlled.
     **/

    /* dazeTime: how many ticks left of daze. Inflicted by stand barrage,
     * daze lets you scroll items and look around, but it takes away
     * your movement, item usage, and stand ability usage. You also
     * have no gravity while dazed**/

    @Unique
    private byte roundabout$dazeTime = 0;

    @Unique
    private int roundabout$TSHurtTime = 0;
    @Unique
    private int roundabout$postTSHurtTime = 0;
    @Unique
    private int roundabout$extraIFrames = 0;
    @Unique
    private int roundabout$gasolineIFRAMES = 0;


    @Unique
    public boolean roundabout$toggleFightOrFlight = false;

    @Inject(method = "hasLineOfSight(Lnet/minecraft/world/entity/Entity;)Z", at = @At(value = "HEAD",
            shift = At.Shift.AFTER, ordinal = 0), cancellable = true)
    public void roundabout$hasLineOfSight(Entity $$0, CallbackInfoReturnable<Boolean> cir) {
        if (((IPermaCasting)this.level()).roundabout$inPermaCastFogRange($$0)){

            if ($$0.level() != this.level()) {
                cir.setReturnValue(false);
                return;
            } else {
                Vec3 $$1 = new Vec3(this.getX(), this.getEyeY(), this.getZ());
                Vec3 $$2 = new Vec3($$0.getX(), $$0.getEyeY(), $$0.getZ());
                if ($$2.distanceTo($$1) > 128.0){
                    cir.setReturnValue(false);
                    return;
                }
                BlockHitResult clipX = this.level().clip(new ClipContext($$1, $$2, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this));
                if (clipX.getType() != HitResult.Type.MISS){
                    if (this.level().getBlockState(clipX.getBlockPos()).getBlock() instanceof FogBlock){
                        cir.setReturnValue(false);
                        return;
                    }
                }

                cir.setReturnValue(this.level().clip(new ClipContext($$1, $$2, ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS);
            }

        }
    }

    /**
     * Tick thru effects for bleed to not show potion swirls
     */
    @Inject(method = "tickEffects", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/syncher/SynchedEntityData;get(Lnet/minecraft/network/syncher/EntityDataAccessor;)Ljava/lang/Object;",
            shift = At.Shift.AFTER, ordinal = 0), cancellable = true)
    public void roundabout$tickEffects(CallbackInfo ci) {
        if (!this.level().isClientSide) {
            int bleedlvl = -1;
            if (this.hasEffect(ModEffects.BLEED) && this.getEffect(ModEffects.BLEED).isVisible()) {
                bleedlvl = this.getEffect(ModEffects.BLEED).getAmplifier();
            }
            if (this.roundabout$getBleedLevel() != bleedlvl) {
                this.roundabout$setBleedLevel(bleedlvl);
            }

            boolean onlyBleeding = true;
            if (!this.activeEffects.isEmpty()) {
                Iterator<MobEffect> $$0 = this.activeEffects.keySet().iterator();
                while ($$0.hasNext()) {
                    MobEffect $$1 = $$0.next();
                    MobEffectInstance $$2 = this.activeEffects.get($$1);
                    if ($$2.isVisible() && !$$2.getEffect().equals(ModEffects.BLEED) && !$$2.getEffect().equals(ModEffects.CAPTURING_LOVE)
                            && !$$2.getEffect().equals(ModEffects.FACELESS)) {
                        onlyBleeding = false;
                    }
                }
            }

            if (this.roundabout$getOnlyBleeding() != onlyBleeding) {
                this.roundabout$setOnlyBleeding(onlyBleeding);
            }

            byte glow = 0;
            if (this.hasEffect(ModEffects.FACELESS)) {
                glow = 1;
            } else if (this.hasEffect(ModEffects.CAPTURING_LOVE)) {
                glow = 2;
            }
            if (this.roundabout$getGlow() != glow) {
                this.roundabout$setGlow(glow);
            }

            if (this.getHealth() > this.getMaxHealth()) {
                this.setHealth(this.getMaxHealth());
            }
            if (this.getEffect(ModEffects.MELTING) != null) {
                if (this.getEffect(ModEffects.MELTING).getAmplifier() >= 6) {
                    this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 1));
                }
                if (Math.abs(1.0-this.getMaxHealth()) <=0.2) {
                    this.hurt(ModDamageTypes.of(this.level(), ModDamageTypes.MELTING), 200);
                }
            }

        } else {
            if (ClientNetworking.getAppropriateConfig().miscellaneousSettings.disableBleedingAndBloodSplatters &&
                    (((IPermaCasting)this.level()).roundabout$inPermaCastFogRange(this)
                    && this.getHealth() < this.getMaxHealth())){

                this.level()
                        .addParticle(
                                ModParticles.FOG_CHAIN,
                                this.getRandomX(0.5),
                                this.getRandomY()+this.getBbHeight(),
                                this.getRandomZ(0.5),
                                0,
                                0.2,
                                0
                        );
            }
        }
        if (this.roundabout$getGlow() == 2){
            if (this.tickCount %2 == 0) {
                this.level().addParticle(ModParticles.CINDERELLA_GLOW, this.getRandomX(0.6D), this.getRandomY(), this.getRandomZ(0.6D), 0.0D, 0.0D, 0.0D);
            }
        }
        if (this.roundabout$getBleedLevel() > -1) {
            if (((IPermaCasting)this.level()).roundabout$inPermaCastFogRange(this)){
                this.level()
                        .addParticle(
                                ModParticles.FOG_CHAIN,
                                this.getRandomX(0.5),
                                this.getRandomY()+this.getBbHeight(),
                                this.getRandomZ(0.5),
                                0,
                                0.2,
                                0
                        );
            }
            int bleedlvl = this.roundabout$getBleedLevel();
            int bloodticks = 8;
            if (bleedlvl == 1) {
                bloodticks = 6;
            } else if (bleedlvl > 1) {
                bloodticks = 4;
            }
            if (this.tickCount % bloodticks == 0 && this.isAlive()) {
                SimpleParticleType bloodType = ModParticles.BLOOD;
                if (MainUtil.hasEnderBlood(this)) {
                    bloodType = ModParticles.ENDER_BLOOD;
                } else if (MainUtil.hasBlueBlood(this)) {
                    bloodType = ModParticles.BLUE_BLOOD;
                }
                this.level()
                        .addParticle(
                                bloodType,
                                this.getRandomX(0.5),
                                this.getRandomY(),
                                this.getRandomZ(0.5),
                                0,
                                0,
                                0
                        );
            }
        }
        if (this.roundabout$getOnlyBleeding()) {
            ci.cancel();
        }
    }


    @Inject(method = "tickEffects", at = @At(value = "HEAD"))
    public void roundabout$tickEffectsPre(CallbackInfo ci) {
        if (!this.level().isClientSide) {
            roundabout$safeToRemoveLove = false;
        }
    }
    @Inject(method = "tickEffects", at = @At(value = "TAIL"))
    public void roundabout$tickEffectsPost(CallbackInfo ci) {
        if (!this.level().isClientSide) {
            roundabout$safeToRemoveLove = true;
        }
    }

    @Unique
    public boolean roundabout$safeToRemoveLove = true;
    @Unique
    public boolean roundabout$prepUglyFace = false;

    @Unique
    @Override
    public void roundabout$setSafeToRemoveLove(boolean safe){
        roundabout$safeToRemoveLove = safe;
    }

    @Inject(method = "removeAllEffects", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$removeAllEffects(CallbackInfoReturnable<Boolean> cir) {
        roundabout$safeToRemoveLove = true;
    }
    @Inject(method = "onEffectRemoved", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$onEffectRemoved(MobEffectInstance $$0, CallbackInfo ci) {
        if ($$0.getEffect().equals(ModEffects.CAPTURING_LOVE) && !roundabout$safeToRemoveLove) {
            roundabout$prepUglyFace = true;
        }
    }



    /**When mobs TS teleport, part of canceling visual interpolation between two points so it looks like they
     * just "blip" there*/
    @Unique
    @Override
    public void roundabout$tryBlip() {
        if (ClientUtil.skipInterpolation) {
            if (ClientUtil.isPlayerOrCamera(this)) {
            ((ILivingEntityAccess) this).roundabout$setLerpSteps(0);
            double lerpx = ((ILivingEntityAccess) this).roundabout$getLerpX();
            double lerpy = ((ILivingEntityAccess) this).roundabout$getLerpY();
            double lerpz = ((ILivingEntityAccess) this).roundabout$getLerpZ();

            this.xo = lerpx;
            this.yo = lerpy;
            this.zo = lerpz;
            this.xOld = lerpx;
            this.yOld = lerpy;
            this.zOld = lerpz;
            this.setPos(lerpx, lerpy, lerpz);
            }
        } if (roundabout$blip && roundabout$blipVector !=null){
            ((ILivingEntityAccess) this).roundabout$setLerpSteps(0);
            this.xo = roundabout$blipVector.x;
            this.yo = roundabout$blipVector.y;
            this.zo = roundabout$blipVector.z;
            this.xOld = roundabout$blipVector.x;
            this.yOld = roundabout$blipVector.y;
            this.zOld = roundabout$blipVector.z;
            ((ILivingEntityAccess) this).roundabout$setLerp(roundabout$blipVector);
            this.setPos(roundabout$blipVector.x, roundabout$blipVector.y, roundabout$blipVector.z);
            this.roundabout$blip = false;
        } else {
            if (ClientUtil.getWasFrozen() && !ClientUtil.getScreenFreeze() && !ClientUtil.isPlayer(this)){
                if (((Entity)(Object)this) instanceof StandEntity SE){
                    if (SE.getUser() != null) {
                        StandUser user =  ((StandUser) SE.getUser());
                        StandPowers powers = user.roundabout$getStandPowers();
                        if (powers.isPiloting()){
                            LivingEntity pilot = powers.getPilotingStand();
                            if (pilot != null && pilot.is(SE)){
                                return;
                            }
                        }
                    }
                }
                ((ILivingEntityAccess) this).roundabout$setLerpSteps(0);
                this.xo = this.lerpX;
                this.yo = this.lerpY;
                this.zo = this.lerpZ;
                this.xOld = this.lerpX;
                this.yOld = this.lerpY;
                this.zOld = this.lerpZ;
                ((ILivingEntityAccess) this).roundabout$setLerp(new Vec3(this.lerpX,this.lerpY,this.lerpZ).toVector3f());
                this.setPos(this.lerpX, this.lerpY, this.lerpZ);
            }
        }
    }

    /**Break free from stand grab*/
    @Inject(method = "setLastHurtByMob(Lnet/minecraft/world/entity/LivingEntity;)V", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$setLastHurtByMob(LivingEntity $$0, CallbackInfo ci) {
        LivingEntity liv = ((LivingEntity) (Object) this);
        if (liv instanceof AbstractSkeleton){
            if ($$0 instanceof Player PE){
                IPlayerEntity ipe = ((IPlayerEntity) PE);
                ShapeShifts shift = ShapeShifts.getShiftFromByte(ipe.roundabout$getShapeShift());
                if (shift != ShapeShifts.PLAYER) {
                    if (ShapeShifts.isSkeleton(shift)) {
                        ipe.roundabout$shapeShiftSilent();
                        ipe.roundabout$setShapeShift(ShapeShifts.PLAYER.id);
                    }
                }
            }
        } else if (liv instanceof Zombie){
            if ($$0 instanceof Player PE){
                IPlayerEntity ipe = ((IPlayerEntity) PE);
                ShapeShifts shift = ShapeShifts.getShiftFromByte(ipe.roundabout$getShapeShift());
                if (shift != ShapeShifts.PLAYER) {
                    if (ShapeShifts.isZombie(shift)) {
                        ipe.roundabout$shapeShiftSilent();
                        ipe.roundabout$setShapeShift(ShapeShifts.PLAYER.id);
                    }
                }
            }
        } else if (liv instanceof Villager){
            if ($$0 instanceof Player PE){
                IPlayerEntity ipe = ((IPlayerEntity) PE);
                ShapeShifts shift = ShapeShifts.getShiftFromByte(ipe.roundabout$getShapeShift());
                if (shift != ShapeShifts.PLAYER) {
                    if (ShapeShifts.isVillager(shift)) {
                        ipe.roundabout$shapeShiftSilent();
                        ipe.roundabout$setShapeShift(ShapeShifts.PLAYER.id);
                    }
                }
            }
        }
    }
    @Inject(method = "tick", at = @At(value = "TAIL"))
    public void roundabout$endTick(CallbackInfo ci) {
        if (!(((LivingEntity)(Object)this) instanceof Player)) {
            this.roundabout$getStandPowers().tickPowerEnd();
        }

        if (roundabout$prepUglyFace) {
            roundabout$prepUglyFace = false;
            roundabout$setGlow((byte) 2);
            this.removeEffect(ModEffects.CAPTURING_LOVE);
            this.addEffect(new MobEffectInstance(ModEffects.FACELESS, 3600, 0, false, true));
        }
    }
    @Unique
    public boolean roundabout$isDrown = false;
    @Unique
    @Override
    public void roundabout$setDrowning(boolean drown){
        roundabout$isDrown = drown;
    }
    @Unique
    @Override
    public boolean roundabout$rotateArmToShoot(){
        if (roundabout$getStandPowers().hasShootingModeVisually()){
            return true;
        }
        return false;
    }
    @Unique
    @Override
    public boolean roundabout$getDrowning(){
        return roundabout$isDrown;
    }

    @Unique
    @javax.annotation.Nullable
    private Entity roundabout$stringHolder;

    @Unique
    @Override
    public int roundabout$getDetectTicks(){
        return roundabout$detectTicks;
    }
    @Unique
    @Override
    public void roundabout$setDetectTicks(int life){
        roundabout$detectTicks = life;
    }

    @Unique
    @Override
    public boolean roundabout$canBeBound(Player $$0) {
        return !this.roundabout$isStringBound() && !(this instanceof Enemy);
    }
    @Unique
    @Override
    public void roundabout$tickString() {
        if (!this.level().isClientSide) {
            if (this.roundabout$stringHolder != null) {
                if (!this.isAlive() || !roundabout$stringHolder.isAlive()|| roundabout$stringHolder.level() != this.level()) {
                    roundabout$dropString();
                } else {
                    LivingEntity live = ((LivingEntity)(Object)this);
                    if (live instanceof Mob mb){
                        mb.restrictTo(roundabout$stringHolder.blockPosition(), 5);
                    }
                    float f = this.distanceTo(roundabout$stringHolder);
                    if (live instanceof TamableAnimal tm && tm.isInSittingPose()) {
                        if (f > 10.0F) {
                            this.roundabout$dropString();
                        }

                        return;
                    }

                    if (f > 10.0F) {
                        this.roundabout$dropString();
                        if (live instanceof Mob mb) {
                            ((IMob)mb).roundabout$getGoalSelector().disableControlFlag(Goal.Flag.MOVE);
                        }
                    } else if (f > 6.0F) {
                        double d0 = (roundabout$stringHolder.getX() - this.getX()) / (double)f;
                        double d1 = (roundabout$stringHolder.getY() - this.getY()) / (double)f;
                        double d2 = (roundabout$stringHolder.getZ() - this.getZ()) / (double)f;
                        this.setDeltaMovement(this.getDeltaMovement().add(Math.copySign(d0 * d0 * 0.4D, d0), Math.copySign(d1 * d1 * 0.4D, d1), Math.copySign(d2 * d2 * 0.4D, d2)));
                        this.checkSlowFallDistance();
                    } else {
                        if (live instanceof Mob mb) {
                            ((IMob)mb).roundabout$getGoalSelector().enableControlFlag(Goal.Flag.MOVE);
                            float f1 = 2.0F;
                            Vec3 vec3 = (new Vec3(roundabout$stringHolder.getX() - this.getX(),
                                    roundabout$stringHolder.getY() - this.getY(),
                                    roundabout$stringHolder.getZ() - this.getZ())).normalize().scale((double) Math.max(f - 2.0F, 0.0F));
                            float speed = 1;
                            mb.getNavigation().moveTo(this.getX() + vec3.x, this.getY() + vec3.y, this.getZ() + vec3.z, speed);
                        }
                    }
                }
            }
        }
    }
    @Unique
    @Override
    public void roundabout$dropString() {
        roundabout$setBoundTo(null);
        roundabout$setRedBound(false);
    }

    @Unique
    @Override
    public boolean roundabout$isStringBound() {
        return roundabout$getBoundTo() != null;
    }

    @Unique
    @Override
    @javax.annotation.Nullable
    public Entity roundabout$getBoundTo() {
        if (this.roundabout$getBoundToID() != 0 && this.level().isClientSide) {
            this.roundabout$stringHolder = this.level().getEntity(this.roundabout$getBoundToID());
        }

        return this.roundabout$stringHolder;
    }

    @Unique
    @Override
    public void roundabout$setBoundToID(int bound) {
        if (this.entityData.hasItem(ROUNDABOUT$IS_BOUND_TO)) {
            this.getEntityData().set(ROUNDABOUT$IS_BOUND_TO, bound);
        }
    }
    @Unique
    @Override
    public int roundabout$getBoundToID() {
        if (this.entityData.hasItem(ROUNDABOUT$IS_BOUND_TO)) {
            return this.getEntityData().get(ROUNDABOUT$IS_BOUND_TO);
        }
        return -1;
    }
    @Unique
    @Override
    public void roundabout$setZappedToID(int bound) {
        if (this.entityData.hasItem(ROUNDABOUT$IS_ZAPPED_TO_ATTACK)) {
            roundabout$zappedTicks = 0;
            this.getEntityData().set(ROUNDABOUT$IS_ZAPPED_TO_ATTACK, bound);
        }
    }
    @Unique
    @Override
    public int roundabout$getZappedToID() {
        if (this.entityData.hasItem(ROUNDABOUT$IS_ZAPPED_TO_ATTACK)) {
            return this.getEntityData().get(ROUNDABOUT$IS_ZAPPED_TO_ATTACK);
        }
        return -1;
    }

    public int roundabout$getZappedTicks(){
        return roundabout$zappedTicks;
    }

    @Unique
    @Override
    public void roundabout$aggressivelyEnforceZapAggro(){

        Entity theory = level().getEntity(roundabout$getZappedToID());
        if (theory != null && !theory.isRemoved() && theory.isAlive()) {
            if (theory instanceof Mob mb){
                this.setLastHurtByMob(mb);
            } else {
                this.setLastHurtByMob(null);
            }
            if (theory instanceof Player pl){
                this.setLastHurtByPlayer(pl);
            } else {
                this.setLastHurtByPlayer(null);
            }
            this.setLastHurtMob(theory);
            if (((LivingEntity) (Object) this) instanceof Mob mb) {
                ((IMob) mb).roundabout$deeplyEnforceTarget(theory);
            }
        }
    }

    @Unique
    @Override
    public void roundabout$aggressivelyEnforceAggro(Entity theory){

        if (theory == null || (!theory.isRemoved() && theory.isAlive())) {
            if (theory instanceof Mob mb){
                this.setLastHurtByMob(mb);
            } else {
                this.setLastHurtByMob(null);
            }
            if (theory instanceof Player pl){
                this.setLastHurtByPlayer(pl);
            } else {
                this.setLastHurtByPlayer(null);
            }
            this.setLastHurtMob(theory);
            if (((LivingEntity) (Object) this) instanceof Mob mb) {
                ((IMob) mb).roundabout$deeplyEnforceTarget(theory);
            }
        }
    }
    /**-1 gravity is no change, 0 is suspending gravity, 1000 is the base amount*/
    @Unique
    @Override
    public void roundabout$setAdjustedGravity(int adj) {
        if (this.entityData.hasItem(ROUNDABOUT$ADJUSTED_GRAVITY)) {
            this.getEntityData().set(ROUNDABOUT$ADJUSTED_GRAVITY, adj);
        }
    }
    @Unique
    @Override
    public int roundabout$getAdjustedGravity() {
        if (this.entityData.hasItem(ROUNDABOUT$ADJUSTED_GRAVITY)) {
            return this.getEntityData().get(ROUNDABOUT$ADJUSTED_GRAVITY);
        }
        return -1;
    }
    @Unique
    @Override
    public void roundabout$setBubbleEncased(byte adj) {
        if (this.entityData.hasItem(ROUNDABOUT$IS_BUBBLE_ENCASED)) {
            this.getEntityData().set(ROUNDABOUT$IS_BUBBLE_ENCASED, adj);
        }
    }
    @Unique
    @Override
    public void roundabout$setBubbleLaunchEncased() {
        this.roundabout$encasedTimer = 140;
        if (this.entityData.hasItem(ROUNDABOUT$IS_BUBBLE_ENCASED)) {
            this.getEntityData().set(ROUNDABOUT$IS_BUBBLE_ENCASED, (byte)2);
        }
    }
    @Unique
    @Override
    public byte roundabout$getBubbleEncased() {
        if (this.entityData.hasItem(ROUNDABOUT$IS_BUBBLE_ENCASED)) {
            return this.getEntityData().get(ROUNDABOUT$IS_BUBBLE_ENCASED);
        }
        return 0;
    }

    @Unique
    @Override
    public boolean roundabout$isBubbleEncased() {
        if (this.entityData.hasItem(ROUNDABOUT$IS_BUBBLE_ENCASED)) {
            return this.getEntityData().get(ROUNDABOUT$IS_BUBBLE_ENCASED) > 0;
        }
        return false;
    }
    @Unique
    @Override
    public boolean roundabout$isLaunchBubbleEncased() {
        if (this.entityData.hasItem(ROUNDABOUT$IS_BUBBLE_ENCASED)) {
            return this.getEntityData().get(ROUNDABOUT$IS_BUBBLE_ENCASED) == 2;
        }
        return false;
    }
    @Unique
    @Override
    public void roundabout$setBoundTo(Entity $$0) {
        if ($$0 != null){
            this.roundabout$stringHolder = $$0;
            roundabout$setBoundToID($$0.getId());

            if (this.isPassenger()) {
                this.stopRiding();
            }
        } else {

            this.roundabout$stringHolder = null;
            roundabout$setBoundToID(-1);
        }
    }

    @Override
    @Unique
    public void roundabout$explode(double $$0) {
        this.roundabout$explode((DamageSource)null, $$0);
    }

    @Unique
    protected void roundabout$explode(@javax.annotation.Nullable DamageSource $$0, double $$1) {
        if (!this.level().isClientSide) {
            double $$2 = Math.sqrt($$1);
            if ($$2 > 5.0) {
                $$2 = 5.0;
            }

            roundabout$explode(this, $$0, (ExplosionDamageCalculator)null, this.getX(), this.getY(), this.getZ(), (float)(4.0 + this.random.nextDouble() * 1.5 * $$2), false, Level.ExplosionInteraction.TNT);

        }

    }
    @Unique
    public void roundabout$explodePublic(double $$1, double x, double y, double z) {
        if (!this.level().isClientSide) {
            double $$2 = Math.sqrt($$1);
            if ($$2 > 5.0) {
                $$2 = 5.0;
            }

            roundabout$explode(this, null, null, x, y, z, (float)(1.5 * $$2), false, Level.ExplosionInteraction.TNT);

        }

    }

    public Explosion roundabout$explode(@javax.annotation.Nullable Entity $$0, @javax.annotation.Nullable DamageSource $$1, @javax.annotation.Nullable ExplosionDamageCalculator $$2, double $$3, double $$4, double $$5, float $$6, boolean $$7, Level.ExplosionInteraction $$8) {
        return this.roundabout$explode($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7, $$8, true);
    }

    private Explosion.BlockInteraction roundabout$getDestroyType(GameRules.Key<GameRules.BooleanValue> $$0) {
        return this.level().getGameRules().getBoolean($$0) ? Explosion.BlockInteraction.DESTROY_WITH_DECAY : Explosion.BlockInteraction.DESTROY;
    }
    public Explosion roundabout$explode(@javax.annotation.Nullable Entity $$0, @javax.annotation.Nullable DamageSource $$1, @javax.annotation.Nullable ExplosionDamageCalculator $$2, double $$3, double $$4, double $$5, float $$6, boolean $$7, Level.ExplosionInteraction $$8, boolean $$9) {
        Explosion.BlockInteraction var10000;
        switch ($$8) {
            case NONE:
                var10000 = Explosion.BlockInteraction.KEEP;
                break;
            case BLOCK:
                var10000 = this.roundabout$getDestroyType(GameRules.RULE_BLOCK_EXPLOSION_DROP_DECAY);
                break;
            case MOB:
                var10000 = this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? this.roundabout$getDestroyType(GameRules.RULE_MOB_EXPLOSION_DROP_DECAY) : Explosion.BlockInteraction.KEEP;
                break;
            case TNT:
                var10000 = this.roundabout$getDestroyType(GameRules.RULE_TNT_EXPLOSION_DROP_DECAY);
                break;
            default:
                throw new IncompatibleClassChangeError();
        }

        Explosion.BlockInteraction $$10 = var10000;
        SoftExplosion $$11 = new SoftExplosion(this.level(), $$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7, $$10);
        $$11.explode();
        $$11.finalizeExplosion($$9);
        return $$11;
    }

    @Unique
    public boolean roundabout$queForTargetDeletion = false;
    @Unique
    @Override
    public void roundabout$deeplyRemoveAttackTarget(){
        this.setLastHurtByMob(null);
        this.setLastHurtByPlayer(null);
        this.setLastHurtMob(null);
        if (((LivingEntity)(Object)this) instanceof Mob mb){
            mb.setTarget(null);
            ((IMob)mb).roundabout$deeplyRemoveTargets();
            ((IMob)mb).roundabout$setSightProtectionTicks(ClientNetworking.getAppropriateConfig().softAndWetSettings.ticksBetweenSightStealsOnSameMob);
        }

    }
    @Unique
    @Override
    public void roundabout$removeQueForTargetDeletion(){
        if (roundabout$queForTargetDeletion){
            roundabout$queForTargetDeletion = false;
        }
    }
    @Unique
    @Override
    public boolean roundabout$getQueForTargetDeletion(){
        if (roundabout$queForTargetDeletion){
            return true;
        }
        return false;
    }
    @Unique
    public int roundabout$zappedTicks = -1;
    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void roundabout$tick(CallbackInfo ci) {

        roundabout$tickStandOrStandless();
        //if (StandID > -1) {
        if (!this.level().isClientSide()) {
            if (roundabout$getZappedToID() > -1){
                roundabout$zappedTicks++;
                if (roundabout$zappedTicks >= ClientNetworking.getAppropriateConfig().survivorSettings.durationOfAggressiveAngerSetting){
                    roundabout$setZappedToID(-1);
                } else {
                    Entity ent = this.level().getEntity(roundabout$getZappedToID());
                    if (!(ent != null && !ent.isRemoved() && ent.isAlive() && ent.distanceTo(this) < 50)) {
                        roundabout$setZappedToID(-1);
                    }
                }
            } else {
                if (roundabout$zappedTicks > -1) {
                    if (roundabout$zappedTicks > 10) {
                        roundabout$zappedTicks = 10;
                    }
                }
                roundabout$zappedTicks--;
            }


            if (this.roundabout$getActive() &&this.roundabout$getStandPowers().canSummonStand()&&this.roundabout$getStandPowers().canSummonStandAsEntity()  && (this.roundabout$getStand() == null ||
                    (this.roundabout$getStand().level().dimensionTypeId() != this.level().dimensionTypeId() &&
                            this.roundabout$getStand() instanceof FollowingStandEntity FE && OffsetIndex.OffsetStyle(FE.getOffsetType()) == OffsetIndex.FOLLOW_STYLE))){
                this.roundabout$summonStand(this.level(),true,false);
            }

            if (roundabout$getEyeSightTaken() != null){
                MobEffectInstance mobInstance = this.getEffect(MobEffects.BLINDNESS);
                if (roundabout$getEyeSightTaken().isRemoved() || !roundabout$getEyeSightTaken().isAlive()){
                    roundabout$setEyeSightTaken(null);
                    if (mobInstance != null){
                        if (mobInstance.isInfiniteDuration()){
                            this.removeEffect(MobEffects.BLINDNESS);
                        }
                    }
                } else {
                    if (mobInstance == null && ((LivingEntity)(Object)this) instanceof Player){
                        this.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, -1, 1));
                    }
                }
            }
        } else {
            int dt = roundabout$detectTicks;
            if (dt > -1){
                dt--;
                roundabout$detectTicks = dt;
            }

            if (roundabout$getZappedToID() > -1){
                roundabout$zappedTicks++;
            } else {
                if (roundabout$zappedTicks > -1) {
                    if (roundabout$zappedTicks > 10) {
                        roundabout$zappedTicks = 10;
                    }
                }
                roundabout$zappedTicks--;
            }
            roundabout$zappedTicks = Mth.clamp(roundabout$zappedTicks,-1,10);
        }
        this.roundabout$getStandPowers().tickPower();
        this.roundabout$tickGuard();
        this.roundabout$tickDaze();
        if (this.roundabout$leapTicks > -1) {
            if (this.onGround() && roundabout$leapTicks < (MainUtil.maxLeapTicks() - 5)) {
                roundabout$leapTicks = -1;
            }
            roundabout$cancelConsumableItem((LivingEntity) (Object) this);
            roundabout$leapTicks--;
            if (!this.level().isClientSide) {
                ((ServerLevel) this.level()).sendParticles(new DustParticleOptions(new Vector3f(1f, 0.65f, 0), 1f), this.getX(), this.getY(), this.getZ(),
                        1, 0, 0, 0, 0.1);
            }
        }
        if (this.roundabout$destructionModeTrailTicks > -1){
            if (this.horizontalCollision || this.verticalCollision) {
                /*The stupid setting which puts launched mobs in boom boom mode*/
                if (this.horizontalCollision) {
                    double $$0 = this.getDeltaMovement().horizontalDistanceSqr();
                    if ($$0 >= 0.009999999776482582) {
                        this.roundabout$explode($$0);
                    }
                } else  {
                    double $$0 = this.getDeltaMovement().horizontalDistanceSqr();
                    if ($$0 >= 0.009999999776482582) {
                        this.roundabout$explode($$0);
                    }
                }
                roundabout$destructionModeTrailTicks = -1;
            } else if (this.isInWater() || this.isInLava()){
                roundabout$destructionModeTrailTicks = -1;
            } else {
                roundabout$cancelConsumableItem((LivingEntity) (Object) this);
                roundabout$destructionModeTrailTicks--;
                if (!this.level().isClientSide) {
                    ((ServerLevel) this.level()).sendParticles(ModParticles.AIR_CRACKLE, this.getX(), this.getY(), this.getZ(),
                            1, 0, 0, 0, 0.1);
                }
            }
        }
        if (roundabout$isSealed()){
            roundabout$setSealedTicks(roundabout$sealedTicks - 1);
            if (((LivingEntity)(Object)this) instanceof Player PE && PE.isCreative()){
                roundabout$setSealedTicks(-1);
            }
            if (!roundabout$isSealed()){
                this.roundabout$setDrowning(false);
            }
        }
        if (roundabout$gasolineIFRAMES > 0){
            roundabout$gasolineIFRAMES--;
        }
        if (roundabout$knifeIFrameTicks > 0){
            roundabout$knifeIFrameTicks--;
            if (roundabout$knifeIFrameTicks==0){
                roundabout$stackedKnivesAndMatches = 0;
            }
        } if (roundabout$knifeDespawnTicks > 0){
            roundabout$knifeDespawnTicks--;
            if (roundabout$knifeDespawnTicks==0){
                if (((LivingEntity)(Object) this) instanceof Player){
                    ((IPlayerEntity)this).roundabout$setKnife((byte)0);
                }
            }
        } if (roundabout$postTSHurtTime > 0){
            roundabout$postTSHurtTime--;
        } if (roundabout$extraIFrames > 0){
            roundabout$extraIFrames--;
        } if (roundabout$gasTicks > -1){
            if (!this.level().isClientSide){
                if (this.isInWaterOrRain()) {
                    roundabout$setGasolineTime(-1);
                } else {
                    roundabout$setGasolineTime(roundabout$gasTicks-1);
                    if (this.tickCount%2 == 0) {
                        float width = this.getBbWidth() / 2;
                        float height = this.getBbHeight() / 4;
                        float height2 = this.getBbHeight()/2;
                        ((ServerLevel) this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.GASOLINE_SPLATTER.defaultBlockState()), this.getX(), this.getY() + height2, this.getZ(),
                                1, width, height, width, 0.1);
                    }
                }
            }
        }
        //}
    }

    @Override
    public void roundabout$addFollower(FollowingStandEntity $$0) {
        if (this.roundabout$followers.isEmpty()) {
            this.roundabout$followers = ImmutableList.of($$0);
        } else {
            List<FollowingStandEntity> $$1 = Lists.newArrayList(this.roundabout$followers);
            $$1.add($$0);
            this.roundabout$followers = ImmutableList.copyOf($$1);
        }
    }

    @Override
    public void roundabout$removeFollower(FollowingStandEntity $$0) {
        if (this.roundabout$followers.size() == 1 && this.roundabout$followers.get(0) == $$0) {
            this.roundabout$followers = ImmutableList.of();
        } else {
            this.roundabout$followers =
                    this.roundabout$followers.stream().filter($$1 -> $$1 != $$0).collect(ImmutableList.toImmutableList());
        }
    }

    @Override
    public final List<FollowingStandEntity> roundabout$getFollowers() {
        return this.roundabout$followers;
    }

    @Override
    public boolean roundabout$hasFollower(FollowingStandEntity $$0) {
        return this.roundabout$followers.contains($$0);
    }

    @Override
    public boolean roundabout$hasFollower(Predicate<Entity> $$0) {
        for (Entity $$1 : this.roundabout$followers) {
            if ($$0.test($$1)) {
                return true;
            }
        }

        return false;
    }

    public void roundabout$cancelConsumableItem(LivingEntity entity){
        ItemStack itemStack = entity.getUseItem();
        Item item = itemStack.getItem();
        if (item.isEdible() || item instanceof PotionItem) {
            entity.releaseUsingItem();
            if (entity instanceof Player) {
                ((Player) entity).stopUsingItem();
            }
        }
    }


    @Unique
    private static final EntityDataAccessor<Byte> ROUNDABOUT$STAND_SKIN = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.BYTE);
    @Unique
    private static final EntityDataAccessor<Byte> ROUNDABOUT$STAND_ANIMATION = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.BYTE);
    @Unique
    private static final EntityDataAccessor<Boolean> ROUNDABOUT$UNIQUE_STAND_MODE_TOGGLE = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Integer> ROUNDABOUT$TRUE_INVISIBILITY = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.INT);

    private byte roundabout$lastSkin = 0;
    @Override
    @Unique
    public byte roundabout$getStandSkin(){
        if (this.getEntityData().hasItem(ROUNDABOUT$STAND_SKIN)) {
            return this.getEntityData().get(ROUNDABOUT$STAND_SKIN);
        }
        return 0;
    }
    @Override
    @Unique
    public byte roundabout$getStandAnimation(){
        if (this.getEntityData().hasItem(ROUNDABOUT$STAND_ANIMATION)) {
            return this.getEntityData().get(ROUNDABOUT$STAND_ANIMATION);
        }
        return 0;
    }

    @Override
    @Unique
    public void roundabout$setStandAnimation(byte anim){
        this.getEntityData().set(ROUNDABOUT$STAND_ANIMATION, anim);
    }
    @Override
    @Unique
    public boolean roundabout$getUniqueStandModeToggle(){
        if (this.getEntityData().hasItem(ROUNDABOUT$UNIQUE_STAND_MODE_TOGGLE)) {
            return this.getEntityData().get(ROUNDABOUT$UNIQUE_STAND_MODE_TOGGLE);
        }
        return false;
    }

    @Override
    @Unique
    public void roundabout$setUniqueStandModeToggle(boolean mode){
        this.getEntityData().set(ROUNDABOUT$UNIQUE_STAND_MODE_TOGGLE, mode);
    }


    @Override
    @Unique
    public byte roundabout$getLastStandSkin(){
        return roundabout$lastSkin;
    }
    @Override
    @Unique
    public void roundabout$setLastStandSkin(byte skn){
        roundabout$lastSkin = skn;
    }


    @Override
    @Unique
    public void roundabout$setStandSkin(byte skin){
        this.getEntityData().set(ROUNDABOUT$STAND_SKIN, skin);
        StandEntity stand = roundabout$getStand();
        if (stand != null){
            stand.setSkin(skin);
        }
    }

    @Unique
    private static final EntityDataAccessor<Byte> ROUNDABOUT$IDLE_POS = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.BYTE);
    @Override
    @Unique
    public byte roundabout$getIdlePos(){
        if (this.getEntityData().hasItem(ROUNDABOUT$IDLE_POS)) {
            return this.getEntityData().get(ROUNDABOUT$IDLE_POS);
        }
        return 0;
    }
    @Override
    @Unique
    public void roundabout$setIdlePosX(byte pos){
        this.getEntityData().set(ROUNDABOUT$IDLE_POS, pos);
        StandEntity stand = roundabout$getStand();
        if (stand != null){
            stand.setIdleAnimation(pos);
        }
    }
    @Unique
    public int roundabout$getGasolineTime(){
        return this.roundabout$gasTicks;
    }
    @Unique
    public int roundabout$getGasolineRenderTime(){
        return this.roundabout$gasRenderTicks;
    }
    @Unique
    public int roundabout$getMaxGasolineTime(){
        return MainUtil.maxGasTicks();
    }
    @Unique
    public int roundabout$getMaxBucketGasolineTime(){
        return MainUtil.maxBucketGasTicks();
    }

    @Override
    @Unique
    public int roundabout$getDestructionTrailTicks(){
        return this.roundabout$destructionModeTrailTicks;
    }
    @Override
    @Unique
    public void roundabout$setDestructionTrailTicks(int destructTicks){
        if (ClientNetworking.getAppropriateConfig().griefSettings.SuperBlockDestructionBarrageLaunching) {
            this.roundabout$destructionModeTrailTicks = destructTicks;
        }
    }
    @Override
    @Unique
    public int roundabout$getLeapTicks(){
        return this.roundabout$leapTicks;
    }
    @Override
    @Unique
    public int roundabout$getMaxLeapTicks(){
        return MainUtil.maxLeapTicks();
    }
    @Override
    @Unique
    public void roundabout$setLeapTicks(int leapTicks){
        this.roundabout$leapTicks = leapTicks;
    }
    @Unique
    public void roundabout$setGasolineTime(int gasTicks){
        this.roundabout$gasTicks = gasTicks;
        if (gasTicks == -1){
            roundabout$gasRenderTicks = -1;
        } else {
            roundabout$gasRenderTicks++;
        }
        if (((LivingEntity) (Object) this) instanceof Player && !((LivingEntity) (Object) this).level().isClientSide()){
            S2CPacketUtil.sendGenericIntToClientPacket(((ServerPlayer) (Object) this),(byte) 1, gasTicks);
        }
    }

    /**Tall Jumping Code*/

    @Unique
    boolean roundabout$bigJump = false;
    @Unique
    public boolean roundabout$getBigJump(){
        return this.roundabout$bigJump;
    }

    @Unique
    public void roundabout$setBigJump(boolean bigJump){
        this.roundabout$bigJump = bigJump;
    }
    @Unique
    public float roundabout$getBonusJumpHeight(){
        float TOT = 0;
        if (roundabout$getBubbleEncased() == 1){
            TOT+=4;
        }
        return TOT;
    }
    @Unique
    float roundabout$currentBigJump = 0;
    @Unique
    public float roundabout$getBigJumpCurrentProgress(){
        return this.roundabout$currentBigJump;
    }

    @Unique
    public void roundabout$setBigJumpCurrentProgress(float bigJump){
        this.roundabout$currentBigJump = bigJump;
    }

    /**TS Floating Code*/
    @Unique
    boolean roundabout$tsJump = false;
    @Unique
    public boolean roundabout$getTSJump(){
        return this.roundabout$tsJump;
    }
    @Unique
    public void roundabout$setTSJump(boolean roundaboutTSJump){
        if (ClientNetworking.getAppropriateConfig().timeStopSettings.enableHovering) {
            this.roundabout$tsJump = roundaboutTSJump;
            if (((LivingEntity) (Object) this) instanceof Player) {
                if (roundaboutTSJump && ((IPlayerEntity) this).roundabout$GetPos() == PlayerPosIndex.NONE) {
                    ((IPlayerEntity) this).roundabout$SetPos(PlayerPosIndex.TS_FLOAT);
                } else if (!roundaboutTSJump && ((IPlayerEntity) this).roundabout$GetPos() == PlayerPosIndex.TS_FLOAT) {
                    ((IPlayerEntity) this).roundabout$SetPos(PlayerPosIndex.NONE);
                }
            }
        }
    }

    /**TS Stored Damage Code*/
    @Unique
    float roundabout$storedDamage = 0;
    @Unique
    Entity roundabout$storedAttacker;
    @Unique
    public float roundabout$getStoredDamage(){
        return this.roundabout$storedDamage;
    }
    @Unique
    public byte roundabout$getStoredDamageByte(){
        if (getEntityData().hasItem(ROUNDABOUT_TS_DAMAGE)) {
            return this.getEntityData().get(ROUNDABOUT_TS_DAMAGE);
        } else {
            return 0;
        }
    }

    @Unique
    @Override
    public void roundabout$setLocacacaCurse(byte locacacaCurse) {
        if (!(this.level().isClientSide)) {
            this.getEntityData().set(ROUNDABOUT$LOCACACA_CURSE, locacacaCurse);
        }
    }
    @Unique
    @Override
    public void roundabout$setOnStandFire(byte onStandFire) {
        if (!(this.level().isClientSide)) {
            this.getEntityData().set(ROUNDABOUT$ON_STAND_FIRE, onStandFire);
            if (onStandFire == StandFireType.FIRELESS.id){
                this.roundabout$fireStarter = null;
            }
        }
    }
    @Unique
    @Override
    public void roundabout$setOnStandFire(byte onStandFire, LivingEntity LE) {
        if (!(this.level().isClientSide)) {
            this.getEntityData().set(ROUNDABOUT$ON_STAND_FIRE, onStandFire);
            if (onStandFire == StandFireType.FIRELESS.id){
                this.roundabout$fireStarter = null;
            } else {
                if (LE != null && ((StandUser)LE).roundabout$getStandPowers() instanceof PowersMagiciansRed pm){

                    this.roundabout$fireStarter = LE;
                    this.roundabout$fireStarterID = pm.snapNumber;
                }
            }
        }
    }
    @Unique
    @Override
    public void roundabout$setOnlyBleeding(boolean only) {
        if (!(this.level().isClientSide)) {
            this.getEntityData().set(ROUNDABOUT$ONLY_BLEEDING, only);
        }
    }

    /**Combat mode goes down when you don't have a stand or power active NO MATTER WHAT*/
    @Unique
    public void roundabout$tickStandOrStandless(){
        if (!this.roundabout$getStandPowers().hasStandActive(((LivingEntity) (Object)this))){
            roundabout$setCombatMode(false);
        }
    }
    @Unique
    @Override
    public void roundabout$setCombatMode(boolean only) {
        if (!(this.level().isClientSide)) {
            this.getEntityData().set(ROUNDABOUT$COMBAT_MODE, only);
        }
    }

    @Unique
    @Override
    public byte roundabout$getLocacacaCurse() {
        if (getEntityData().hasItem(ROUNDABOUT$LOCACACA_CURSE)) {
            return this.getEntityData().get(ROUNDABOUT$LOCACACA_CURSE);
        } else {
            return 0;
        }
    }
    @Unique
    @Override
    public byte roundabout$getOnStandFire() {
        if (getEntityData().hasItem(ROUNDABOUT$ON_STAND_FIRE)) {
            return this.getEntityData().get(ROUNDABOUT$ON_STAND_FIRE);
        } else {
            return 0;
        }
    }
    @Unique
    @Override
    public void roundabout$setBleedLevel(int bleedLevel) {
        if (!(this.level().isClientSide)) {
            this.getEntityData().set(ROUNDABOUT$BLEED_LEVEL, bleedLevel);
        }
    }
    @Unique
    @Override
    public int roundabout$getBleedLevel() {

        if (getEntityData().hasItem(ROUNDABOUT$BLEED_LEVEL)) {
            return this.getEntityData().get(ROUNDABOUT$BLEED_LEVEL);
        } else {
            return 0;
        }
    }

    /**Not to be confused with glowing, this is for the cinderella lipstick and lets
     * entities render with different brightness, 1= darker and 2 = brighter
     * This data can later be reused if something else needs to render and can
     * overtake this*/
    @Unique
    @Override
    public void roundabout$setGlow(byte glow) {
        if (!(this.level().isClientSide)) {
            this.getEntityData().set(ROUNDABOUT$GLOW, glow);
        }
    }
    @Unique
    @Override
    public byte roundabout$getGlow() {
        if (getEntityData().hasItem(ROUNDABOUT$GLOW)) {
            return this.getEntityData().get(ROUNDABOUT$GLOW);
        } else {
            return 0;
        }
    }
    @Unique
    @Override
    public boolean roundabout$getOnlyBleeding() {
        if (getEntityData().hasItem(ROUNDABOUT$ONLY_BLEEDING)) {
            return this.getEntityData().get(ROUNDABOUT$ONLY_BLEEDING);
        } else {
            return false;
        }
    }
    @Unique
    @Override
    public boolean roundabout$getCombatMode() {
        if (getEntityData().hasItem(ROUNDABOUT$COMBAT_MODE)) {
            return this.getEntityData().get(ROUNDABOUT$COMBAT_MODE);
        } else {
            return false;
        }
    }
    @Unique
    @Override
    public boolean roundabout$getEffectiveCombatMode() {
        if (getEntityData().hasItem(ROUNDABOUT$COMBAT_MODE)) {
            return this.getEntityData().get(ROUNDABOUT$COMBAT_MODE) && (((StandUser)this).roundabout$hasAStand() &&
                    this.roundabout$getStandPowers().hasStandActive(((LivingEntity) (Object)this)));
        } else {
            return false;
        }
    }
    @Unique
    @Override
    public boolean roundabout$hasAStand(){
        return !this.roundabout$getStandDisc().isEmpty() && !this.roundabout$getStandDisc().is(ModItems.STAND_DISC);
    }
    @Unique
    @Override
    public StandPowers roundabout$getRejectionStandPowers() {
        return this.roundabout$RejectionStandPowers;
    }
    @Unique
    @Override
    public ItemStack roundabout$getRejectionStandDisc() {
        return this.roundabout$RejectionStandDisc;
    }
    @Unique
    @Override
    public void roundabout$setRejectionStandPowers(StandPowers powers) {
        if (!(this.level().isClientSide)) {
            this.roundabout$RejectionStandPowers = powers;
        }
    }
    @Unique
    @Override
    public void roundabout$setRejectionStandDisc(ItemStack disc) {
        if (!(this.level().isClientSide)) {
            this.roundabout$RejectionStandDisc = disc;
        }
    }
    @Unique
    @Override
    public ItemStack roundabout$getStandDisc() {
        return roundabout$standDisc;
    }
    @Unique
    @Override
    public void roundabout$setStandDisc(ItemStack stack) {
        if (!(this.level().isClientSide)) {
            roundabout$standDisc = stack;
            this.getEntityData().set(ROUNDABOUT$STAND_DISC, BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
            if (stack.getItem() instanceof StandDiscItem SD){
                MainUtil.extractDiscData(((LivingEntity)(Object)this), SD, stack);
            }
        }
    }
    @Unique
    public void roundabout$setStoredDamage(float roundaboutStoredDamage){
        if (!((LivingEntity) (Object) this).level().isClientSide) {
            this.roundabout$storedDamage = roundaboutStoredDamage;
            if (roundaboutStoredDamage <= 0) {
                ((LivingEntity) (Object) this).getEntityData().set(ROUNDABOUT_TS_DAMAGE, (byte) 0);
            } else {
                ((LivingEntity) (Object) this).getEntityData().set(ROUNDABOUT_TS_DAMAGE, (byte) (2 + Math.round(Math.min(
                        (roundaboutStoredDamage / (this.roundaboutGetMaxStoredDamage())) * 6,
                        6))));
            }
        }
    }

    @Unique
    private int roundabout$restrainTicks = -1;
    @Unique
    private int roundabout$knifeIFrameTicks = 0;
    @Unique
    private int roundabout$stackedKnivesAndMatches = 0;
    @Unique
    private int roundabout$knifeDespawnTicks = 0;

    @Unique
    private int roundaboutTSHurtSound = 0;

    public int roundaboutGetTSHurtSound(){
        return this.roundaboutTSHurtSound;
    }
    public void roundaboutSetTSHurtSound(int roundaboutTSHurtSound){
        this.roundaboutTSHurtSound = roundaboutTSHurtSound;
    }


    @Unique
    public Entity roundaboutGetStoredAttacker(){
        return this.roundabout$storedAttacker;
    }
    @Unique
    public void roundaboutSetStoredAttacker(Entity roundaboutStoredAttacker){
        this.roundabout$storedAttacker = roundaboutStoredAttacker;
    }

    @Unique
    public float roundaboutGetMaxStoredDamage(){
        LivingEntity living = ((LivingEntity)(Object)this);
        if (living instanceof Player) {
            return (float) (living.getMaxHealth() * (ClientNetworking.getAppropriateConfig().timeStopSettings.playerDamageCapHealthPercent *0.01));
        } else {
            return living.getMaxHealth();
        }

    }


    @ModifyVariable(method = "addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
    public CompoundTag roundabout$addAdditionalSaveData(CompoundTag $$0){
        if (!this.roundabout$getStandDisc().isEmpty() || $$0.contains("roundabout.StandDisc", 10)) {
            ItemStack discy = this.roundabout$getStandDisc();
            CompoundTag compoundtag = new CompoundTag();
            $$0.put("roundabout.StandDisc",MainUtil.saveToDiscData(((LivingEntity)(Object)this), discy).save(compoundtag));
        }
        if ((this.roundabout$getRejectionStandDisc() != null && !this.roundabout$getRejectionStandDisc().isEmpty()) || $$0.contains("roundabout.StandRejectionDisc", 10)) {
            CompoundTag compoundtag = new CompoundTag();
            if (roundabout$getRejectionStandDisc() == null){
                roundabout$setRejectionStandDisc(ItemStack.EMPTY);
            }
            $$0.put("roundabout.StandRejectionDisc",this.roundabout$getRejectionStandDisc().save(compoundtag));
        }

        CompoundTag compoundtag = $$0.getCompound("roundabout");
        compoundtag.putByte("bubbleEncased",roundabout$getBubbleEncased());
        $$0.put("roundabout",compoundtag);

        return $$0;
    }

    @Inject(method = "onSyncedDataUpdated", at = @At(value = "TAIL"), cancellable = true)
    public void roundabout$onSyncedDataUpdated(EntityDataAccessor<?> $$0, CallbackInfo ci){
        if ($$0.equals(ROUNDABOUT$STAND_DISC)){
            String updateString = this.entityData.get(ROUNDABOUT$STAND_DISC);
            if (!updateString.isEmpty()){
                if (ResourceLocation.isValidResourceLocation(updateString)) {
                    ResourceLocation rl = new ResourceLocation(updateString);
                    if (rl != null) {
                        Item tem = BuiltInRegistries.ITEM.get(rl);
                        if (tem != null) {
                            roundabout$standDisc = tem.getDefaultInstance();
                        }
                    }
                }
            }
        }
    }

    /**The items that shoot and brawl mode are allowed to use*/
    @Inject(method = "getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$getItemInHand(InteractionHand $$0, CallbackInfoReturnable<ItemStack> cir){
        if (roundabout$getEffectiveCombatMode()){
            ItemStack stack = ItemStack.EMPTY;
            if ($$0 == InteractionHand.MAIN_HAND) {
                stack = this.getItemBySlot(EquipmentSlot.MAINHAND);
            } else if ($$0 == InteractionHand.OFF_HAND) {
                stack = this.getItemBySlot(EquipmentSlot.OFFHAND);
            }
            if (stack.isEdible() || stack.getItem() instanceof HarpoonItem || stack.getItem() instanceof TridentItem
                    || stack.getItem() instanceof KnifeItem){
                cir.setReturnValue(stack);
                return;
            }
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }

    @Inject(method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", at = @At(value = "HEAD"))
    public void roundabout$readAdditionalSaveData(CompoundTag $$0, CallbackInfo ci){
        if ($$0.contains("roundabout.StandDisc", 10)) {
            CompoundTag compoundtag = $$0.getCompound("roundabout.StandDisc");
            ItemStack itemstack = ItemStack.of(compoundtag);
            if (!itemstack.isEmpty() && itemstack.getItem() instanceof StandDiscItem SD){
                this.roundabout$setStandDisc(itemstack);
                SD.generateStandPowers((LivingEntity)(Object)this);
                MainUtil.extractDiscData(((LivingEntity)(Object)this),SD,itemstack);
            }
        }if ($$0.contains("roundabout.StandRejectionDisc", 10)) {
            CompoundTag compoundtag = $$0.getCompound("roundabout.StandRejectionDisc");
            ItemStack itemstack = ItemStack.of(compoundtag);
            if (!itemstack.isEmpty() && itemstack.getItem() instanceof StandDiscItem SD){
                this.roundabout$setRejectionStandDisc(itemstack);
                SD.generateStandPowerRejection((LivingEntity)(Object)this);
            }
        }

        CompoundTag compoundtag = $$0.getCompound("roundabout");
        roundabout$setBubbleEncased(compoundtag.getByte("bubbleEncased"));
    }
    @ModifyVariable(method = "checkAutoSpinAttack(Lnet/minecraft/world/phys/AABB;Lnet/minecraft/world/phys/AABB;)V", at = @At("STORE"), ordinal = 0)
    public List<Entity> roundabout$checkAutoSpin(List<Entity> list){
        List<Entity> listE= new ArrayList<>();
        for (Entity entity : list) {
            if (!(entity instanceof StandEntity se && se.ignoreTridentSpin()) && !(entity.is(this.roundabout$getThrower()))) {
                listE.add(entity);
            }
        }
        return listE;
    }

    /**returns if the mob has a stand. For now, returns if stand is active, but in the future will be more
     * complicated**/
    @Unique
    public boolean roundabout$isStandUser(){
        return this.roundabout$getActive();
    }



    @Override
    public boolean roundabout$isRestrained(){
        return (this.getVehicle() instanceof StandEntity SE && SE.canRestrainWhileMounted());
    }

    @Override
    public int roundabout$getRestrainedTicks(){
        return this.roundabout$restrainTicks;
    }

    @Override
    public void roundabout$setRestrainedTicks(int restrain){
        this.roundabout$restrainTicks = restrain;
    }

    @Unique
    public boolean roundabout$isDazed(){
        return this.roundabout$dazeTime > 0;
    }
    @Unique
    public void roundabout$setDazeTime(byte dazeTime){
        this.roundabout$dazeTime = dazeTime;
    }
    @Unique
    public void roundabout$setDazed(byte dazeTime){
        this.roundabout$dazeTime = dazeTime;
        this.roundabout$syncDaze();
    }
    @Unique
    @Override
    public void roundabout$setRedBound(boolean roundabout$isRedBound){
        this.roundabout$isRedBound = roundabout$isRedBound;
    }
    public boolean roundabout$isRedBound = false;
    @Unique
    public boolean roundabout$getActive() {
        if (((LivingEntity) (Object) this).getEntityData().hasItem(ROUNDABOUT$STAND_ACTIVE) &&
                ((LivingEntity) (Object) this).getEntityData().get(ROUNDABOUT$STAND_ACTIVE) instanceof Boolean) {
            return ((LivingEntity) (Object) this).getEntityData().get(ROUNDABOUT$STAND_ACTIVE);
        } else {
            return false;
        }
    }
    /**If the player currently is stand attacking vs using items*/
    public boolean roundabout$getMainhandOverride() {
        return this.roundabout$getActive();
    }
    @Unique
    public float roundabout$getMaxGuardPoints(){
        return (float) (roundabout$getStandPowers().getMaxGuardPoints()*(ClientNetworking.getAppropriateConfig().generalStandSettings.standGuardMultiplier*0.01));
    }

    @Unique
    public int roundabout$heyYaVanishTicks = 0;

    @Unique
    @Override
    public int roundabout$getHeyYaVanishTicks(){
        return roundabout$heyYaVanishTicks;
    }
    @Unique
    @Override
    public void roundabout$setHeyYaVanishTicks(int set){
        roundabout$heyYaVanishTicks = Mth.clamp(set,0,10);
    }

    @Unique
    public int roundabout$RattShoulderVanishTicks = 0;

    @Unique
    @Override
    public int roundabout$getRattShoulderVanishTicks(){
        return roundabout$RattShoulderVanishTicks;
    }
    @Unique
    @Override
    public void roundabout$setRattShoulderVanishTicks(int set){
        roundabout$RattShoulderVanishTicks = Mth.clamp(set,0,10);
    }

    @Unique
    public int roundabout$mandomVanishTicks = 0;

    @Unique
    @Override
    public int roundabout$getMandomVanishTicks(){
        return roundabout$mandomVanishTicks;
    }
    @Unique
    @Override
    public void roundabout$setMandomVanishTicks(int set){
        roundabout$mandomVanishTicks = Mth.clamp(set,0,10);
    }

    @Unique
    public AnimationState roundabout$heyYaAnimation2 = new AnimationState();

    @Unique
    @Override
    public AnimationState roundabout$getHeyYaAnimation2(){
        return roundabout$heyYaAnimation2;
    }
    @Unique
    public AnimationState roundabout$heyYaAnimation = new AnimationState();

    @Unique
    @Override
    public AnimationState roundabout$getWornStandIdleAnimation(){
        return roundabout$heyYaAnimation;
    }

    @Unique
    @Override
    public void roundabout$setHeyYaAnimation(AnimationState layer){
        roundabout$heyYaAnimation = layer;
    }
    @Unique
    public AnimationState roundabout$handLayerAnimation = new AnimationState();

    @Unique
    @Override
    public AnimationState roundabout$getHandLayerAnimation(){
        return roundabout$handLayerAnimation;
    }

    @Unique
    @Override
    public void roundabout$setHandLayerAnimation(AnimationState layer){
        roundabout$handLayerAnimation = layer;
    }
    @Unique
    public float roundabout$getGuardCooldown(){
        return this.roundabout$GuardCooldown;
    }
    @Unique
    public float roundabout$getGuardPoints(){
        return this.roundabout$GuardPoints;
    }
    @Unique
    public void roundabout$setGuardPoints(float GuardPoints){
        this.roundabout$GuardPoints = GuardPoints;
    }
    @Unique
    public boolean roundabout$getGuardBroken(){
        return this.roundabout$GuardBroken;
    }
    @Unique
    public void roundabout$breakGuard() {
        this.roundabout$GuardBroken = true;
        if (!this.level().isClientSide && this.roundabout$getStandPowers().isGuarding()) {
            this.roundabout$getStandPowers().animateStand(StandEntity.BROKEN_GUARD);
        }
        this.roundabout$syncGuard();
    }
    @Unique
    public void roundabout$setGuardBroken(boolean guardBroken){
        this.roundabout$GuardBroken = guardBroken;
        if (!this.level().isClientSide) {
            if (guardBroken && this.roundabout$getStandPowers().isGuarding()){
                this.roundabout$getStandPowers().animateStand(StandEntity.BROKEN_GUARD);
            } else if (!guardBroken && this.roundabout$getStandPowers().isGuarding()){
                this.roundabout$getStandPowers().animateStand(StandEntity.BROKEN_GUARD);
            }
        }
    }
    @Unique
    public void roundabout$damageGuard(float damage){
        float finalGuard = this.roundabout$GuardPoints - damage;
        this.roundabout$GuardCooldown = 10;
        if (finalGuard <= 0){
            this.roundabout$GuardPoints = 0;
            this.roundabout$breakGuard();
            this.roundabout$syncGuard();
        } else {
            this.roundabout$GuardPoints = finalGuard;
            this.roundabout$syncGuard();
        }
    }

    @Unique
    public void roundabout$fixGuard() {
        this.roundabout$GuardPoints = this.roundabout$getMaxGuardPoints();
        this.roundabout$GuardBroken = false;
        if (!this.level().isClientSide && this.roundabout$getStandPowers().isGuarding()) {
            this.roundabout$getStandPowers().animateStand(StandEntity.BLOCK);
        }
        this.roundabout$syncGuard();
    }
    @Unique
    public void roundabout$regenGuard(float regen){
        float finalGuard = this.roundabout$GuardPoints + regen;
        if (finalGuard >= this.roundabout$getMaxGuardPoints()){
            this.roundabout$fixGuard();
        } else {
            this.roundabout$GuardPoints = finalGuard;
            this.roundabout$syncGuard();
        }
    }
    @Unique
    public void roundabout$tickGuard(){
        if (this.roundabout$GuardPoints < this.roundabout$getMaxGuardPoints()) {
            if (this.roundabout$GuardBroken){
                float guardRegen = this.roundabout$getMaxGuardPoints() / 100;
                this.roundabout$regenGuard(guardRegen);
            } else if (!this.roundabout$isGuarding() && this.roundabout$shieldNotDisabled()){
                float guardRegen = this.roundabout$getMaxGuardPoints() / 200;
                this.roundabout$regenGuard(guardRegen);
            }
            if (this.roundabout$isGuarding() && !roundabout$shieldNotDisabled()){
                this.roundabout$setAttackTimeDuring(0);
            }
        } else {
            if (this.roundabout$GuardBroken){
                this.roundabout$regenGuard(1);
            }
        }
        if (this.roundabout$GuardCooldown > 0){this.roundabout$GuardCooldown--;}
    }
    @Unique
    public void roundabout$tickDaze(){
        if (!this.roundabout$User.level().isClientSide) {
            if (this.roundabout$dazeTime > 0) {
                ((LivingEntity)(Object)this).stopUsingItem();
                roundabout$dazeTime--;
                if (roundabout$dazeTime <= 0){
                    this.roundabout$getStandPowers().animateStand(StandEntity.IDLE);
                    this.roundabout$syncDaze();
                }
            }
        }
    }

    @Unique
    public void roundabout$syncGuard(){
        if (((LivingEntity) (Object) this) instanceof Player pl && !((LivingEntity) (Object) this).level().isClientSide){
            S2CPacketUtil.synchGuard(pl,this.roundabout$getGuardPoints(),this.roundabout$getGuardBroken());
        }
    }

    @Shadow
    protected int autoSpinAttackTicks;
    @Shadow
    protected void setLivingEntityFlag(int $$0, boolean $$1){
    }


    @Override
    public void roundabout$startAutoSpinAttack(int p_204080_) {
        this.autoSpinAttackTicks = p_204080_;
        if (!this.level().isClientSide) {
            this.setLivingEntityFlag(4, true);
        }
    }

    @Unique
    public void roundabout$syncDaze(){
        if (((LivingEntity) (Object) this) instanceof Player && !((LivingEntity) (Object) this).level().isClientSide){
            S2CPacketUtil.synchDaze((ServerPlayer) (Object) this,this.roundabout$dazeTime);
        }
    }

    @Unique
    public float roundabout$getRayDistance(Entity entity, float range){
        return this.roundabout$getStandPowers().getRayDistance(entity,range);
    }

    @Unique
    public void roundabout$tryPower(int move, boolean forced){
        if (!this.roundabout$isClashing() || move == PowerIndex.CLASH_CANCEL) {
            this.roundabout$getStandPowers().tryPower(move, forced);
            tryPowerStuff();
        }
    }
    @Unique
    @Override
    public void roundabout$tryIntPower(int move, boolean forced, int chargeTime){
        if (!this.roundabout$isClashing() || move == PowerIndex.CLASH_CANCEL) {
            this.roundabout$getStandPowers().tryIntPower(move, forced, chargeTime);
            tryPowerStuff();
        }
    }
    @Unique
    @Override
    public void roundabout$tryIntPower(int move,  boolean forced, int chargeTime,int move2, int move3){
        if (!this.roundabout$isClashing() || move == PowerIndex.CLASH_CANCEL) {
            this.roundabout$getStandPowers().tryTripleIntPower(move, forced, chargeTime, move2, move3);
            tryPowerStuff();
        }
    }
    @Unique
    @Override
    public void roundabout$tryBlockPosPower(int move, boolean forced, BlockPos blockPos){
        if (!this.roundabout$isClashing() || move == PowerIndex.CLASH_CANCEL) {
            this.roundabout$getStandPowers().tryBlockPosPower(move, forced, blockPos);
            tryPowerStuff();
        }
    }
    @Unique
    @Override
    public void roundabout$tryPosPower(int move, boolean forced, Vec3 pos){
        if (!this.roundabout$isClashing() || move == PowerIndex.CLASH_CANCEL) {
            this.roundabout$getStandPowers().tryPosPower(move, forced, pos);
            tryPowerStuff();
        }
    }
    public void tryPowerStuff(){
        this.roundabout$getStandPowers().syncCooldowns();
        if (this.level().isClientSide) {
            this.roundabout$getStandPowers().kickStarted = false;
        }
    }



    @Unique
    public boolean roundabout$canAttack(){
        return this.roundabout$getStandPowers().canAttack();
    }
    public int roundabout$getSummonCD2(){
        return this.roundabout$getStandPowers().getSummonCD2();
    }
    public Entity roundabout$getTargetEntity(LivingEntity User, float distMax){
        return this.roundabout$getStandPowers().getTargetEntity(User, distMax);
    }
    public boolean roundabout$getSummonCD(){
        return this.roundabout$getStandPowers().getSummonCD();
    }
    public void roundabout$setSummonCD(int summonCD){
        this.roundabout$getStandPowers().setSummonCD(summonCD);
    }
    public byte roundabout$getActivePower(){
        return this.roundabout$getStandPowers().getActivePower();
    }
    public LivingEntity roundabout$getPowerUser(){
        return this.roundabout$getStandPowers().getSelf();
    }
    public int roundabout$getAttackTimeMax(){
        return this.roundabout$getStandPowers().getAttackTimeMax();
    }
    public int roundabout$getAttackTime(){
        return this.roundabout$getStandPowers().getAttackTime();
    }
    public int roundabout$getAttackTimeDuring(){
        return this.roundabout$getStandPowers().getAttackTimeDuring();
    }
    public boolean roundabout$getInterruptCD(){
        return this.roundabout$getStandPowers().getInterruptCD();
    }
    public byte roundabout$getActivePowerPhase(){
        return this.roundabout$getStandPowers().getActivePowerPhase();
    }public byte roundabout$getActivePowerPhaseMax(){
        return this.roundabout$getStandPowers().getActivePowerPhaseMax();
    }
    public float roundabout$getStandReach(){
        return this.roundabout$getStandPowers().getReach();
    }
    public boolean roundabout$isGuarding(){
        return this.roundabout$getStandPowers().isGuarding();
    }
    public boolean roundabout$isBarraging(){
        return this.roundabout$getStandPowers().isBarraging();
    }
    public boolean roundabout$isClashing(){
        return this.roundabout$getStandPowers().isClashing();
    }
    public boolean roundabout$isGuardingEffectively(){
        if (this.roundabout$GuardBroken){return false;}
        return this.roundabout$isGuardingEffectively2();
    }
    public boolean roundabout$isGuardingEffectively2(){
        return (this.roundabout$shieldNotDisabled() && this.roundabout$getStandPowers().isGuarding() && this.roundabout$getStandPowers().getAttackTimeDuring() >= ClientNetworking.getAppropriateConfig().generalStandSettings.standGuardDelayTicks);
    }

    public boolean roundabout$shieldNotDisabled(){
        return !(this.roundabout$User instanceof Player) || !(((Player) this.roundabout$User).getCooldowns().getCooldownPercent(Items.SHIELD, 0f) > 0);

    }
    public float roundabout$getDistanceOut(LivingEntity entity, float range, boolean offset){
        return this.roundabout$getStandPowers().getDistanceOut(entity,range,offset);
    }
    public void roundabout$setAttackTimeDuring(int attackTimeDuring){
        this.roundabout$getStandPowers().setAttackTimeDuring(attackTimeDuring);
    } public void roundabout$setInterruptCD(int interruptCD){
        this.roundabout$getStandPowers().setInterruptCD(interruptCD);
    }

    @Unique
    public ItemStack roundabout$itemParityClient = ItemStack.EMPTY;

    public StandPowers roundabout$getStandPowers() {
        if (this.level().isClientSide){
            ItemStack standDisc = this.roundabout$getStandDisc();
            if (!ItemStack.matches(roundabout$itemParityClient, standDisc)){
                if (standDisc.getItem() instanceof StandDiscItem SE){
                    SE.generateStandPowers((LivingEntity)(Object)this);
                    roundabout$itemParityClient = standDisc;
                } else {
                    this.roundabout$setStandPowers(null);
                    roundabout$itemParityClient = ItemStack.EMPTY;
                }
            }
        }

        if (this.roundabout$Powers == null) {
            ItemStack StandDisc = this.roundabout$getStandDisc();
            if (!StandDisc.isEmpty() && StandDisc.getItem() instanceof StandDiscItem SD){
                SD.generateStandPowers((LivingEntity)(Object)this);
            } else {

                this.roundabout$Powers = new StandPowers(roundabout$User);
            }
        }
        return this.roundabout$Powers;
    }

    public void roundabout$setStandPowers(StandPowers standPowers){
        this.roundabout$Powers = standPowers;
    }


    /** Turns your stand "on". This updates the HUD, and is necessary in case the stand doesn't have a body.*/
    public void roundabout$setActive(boolean active){
        if (!active){
            roundabout$tryPower(PowerIndex.NONE, true);
        }
        ((LivingEntity) (Object) this).getEntityData().set(ROUNDABOUT$STAND_ACTIVE, active);
    }

    @Unique
    public int roundabout$sealedTicks = -1;
    @Unique
    public int roundabout$maxSealedTicks = -1;

    @Override
    @Unique
    public void roundabout$setSealedTicks(int ticks){
        if (roundabout$getActive()){
            roundabout$setSummonCD(8);
            roundabout$setActive(false);
            roundabout$tryPower(PowerIndex.NONE, true);
            C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_BYTE_DESUMMON);
        }
        roundabout$sealedTicks = ticks;
    }

    @Override
    public void roundabout$tryBlockPosPower(int move, boolean forced, BlockPos blockPos, BlockHitResult blockHit){
        if (!this.roundabout$isClashing() || move == PowerIndex.CLASH_CANCEL) {
            this.roundabout$getStandPowers().tryBlockPosPower(move, forced, blockPos, blockHit);
            tryPowerStuff();
        }
    }
    @Override
    @Unique
    public void roundabout$setMaxSealedTicks(int ticks){
        roundabout$maxSealedTicks = ticks;
    }
    @Override
    @Unique
    public int roundabout$getSealedTicks(){
        return roundabout$sealedTicks;
    }
    @Override
    @Unique
    public boolean roundabout$isSealed(){
        return roundabout$sealedTicks > -1;
    }
    @Override
    @Unique
    public int roundabout$getMaxSealedTicks(){
        return roundabout$maxSealedTicks;
    }

    /** Sets a stand to a user, and a user to a stand.*/
    public void roundabout$standMount(StandEntity StandSet){
        this.roundabout$setStand(StandSet);
        StandSet.setMaster(roundabout$User);
    }

    /**Only sets a user's stand. Distinction may be important depending on when it is called.*/
    public void roundabout$setStand(StandEntity StandSet){
        this.roundabout$Stand = StandSet;
        if (StandSet==null){
            ((LivingEntity) (Object) this).getEntityData().set(ROUNDABOUT$STAND_ID, -1);
        } else {
            ((LivingEntity) (Object) this).getEntityData().set(ROUNDABOUT$STAND_ID, StandSet.getId());
        }
    }


    /** Code that brings out a user's stand, based on the stand's summon sounds and conditions. */
    public void roundabout$summonStand(Level theWorld, boolean forced, boolean sound){
        if (((LivingEntity)(Object)this) instanceof Player PE && PE.isSpectator()){
            return;
        }
        boolean active;
        if (!this.roundabout$getActive() || forced) {
            //world.getEntity
            StandPowers thispowers = this.roundabout$getStandPowers();
            if (thispowers.canSummonStand()) {
                thispowers.playSummonEffects(forced);

                if (thispowers.canSummonStandAsEntity()) {

                    StandEntity stand = thispowers.getNewStandEntity();
                    if (stand != null) {
                        InteractionHand hand = roundabout$User.getUsedItemHand();
                        if (hand == InteractionHand.OFF_HAND) {
                            ItemStack itemStack = roundabout$User.getUseItem();
                            Item item = itemStack.getItem();
                            if (item.getUseAnimation(itemStack) == UseAnim.BLOCK) {
                                roundabout$User.releaseUsingItem();
                            }
                        }
                        if (stand instanceof FollowingStandEntity FE) {
                            Vec3 spos = FE.getStandOffsetVector(roundabout$User);
                        stand.absMoveTo(spos.x(), spos.y(), spos.z());
                        } else {
                            Vec3 yes = this.getPosition(1F).add(stand.getBonusOffset());
                            stand.absMoveTo(yes.x,yes.y,yes.z);
                        }

                        stand.setSkin(roundabout$getStandSkin());
                        stand.setIdleAnimation(roundabout$getIdlePos());

                        if (((LivingEntity) (Object) this) instanceof Player PE) {
                            stand.playerSetProperties(PE);
                            if (stand instanceof FollowingStandEntity FE) {
                                FE.setDistanceOut(((IPlayerEntity) PE).roundabout$getDistanceOut());
                                FE.setAnchorPlace(((IPlayerEntity) PE).roundabout$getAnchorPlace());
                                FE.setAnchorPlaceAttack(((IPlayerEntity) PE).roundabout$getAnchorPlaceAttack());
                                FE.setSizePercent(((IPlayerEntity) PE).roundabout$getSizePercent());
                                FE.setIdleRotation(((IPlayerEntity) PE).roundabout$getIdleRotation());
                                FE.setIdleYOffset(((IPlayerEntity) PE).roundabout$getIdleYOffset());
                            }
                            if (!this.level().isClientSide()) {
                                IPlayerEntity ipe = ((IPlayerEntity) this);
                                S2CPacketUtil.sendPowerInventorySettings(
                                        ((ServerPlayer) ((Player) (Object) this)), ipe.roundabout$getAnchorPlace(),
                                        ipe.roundabout$getDistanceOut(),
                                        ipe.roundabout$getSizePercent(),
                                        ipe.roundabout$getIdleRotation(),
                                        ipe.roundabout$getIdleYOffset(),
                                        ipe.roundabout$getAnchorPlaceAttack());
                            }
                        }

                        theWorld.addFreshEntity(stand);

                        if (sound && !((TimeStop) this.level()).CanTimeStopEntity(this)) {
                            thispowers.playSummonSound();
                        }

                        this.roundabout$standMount(stand);
                    }
                } else {

                    if (sound && !((TimeStop) this.level()).CanTimeStopEntity(this)) {
                        thispowers.playSummonSound();
                    }
                }
                active=true;
            } else {
                active=false;
            }

        } else {
            this.roundabout$tryPower(PowerIndex.NONE,true);
            active=false;
        }
        this.roundabout$setActive(active);
    }

    /** Returns the stand of a User, and makes necessary checks to reload the stand on a client
     * if the client does not have the stand loaded*/

    @Nullable
    public StandEntity roundabout$getStand() {
        if (this.level().isClientSide && (((LivingEntity) (Object) this).getEntityData().hasItem(ROUNDABOUT$STAND_ID))) {
            return (StandEntity) this.level().getEntity(((LivingEntity) (Object) this).getEntityData().get(ROUNDABOUT$STAND_ID));
        } else {
            if (this.roundabout$Stand != null && this.roundabout$Stand.isRemoved()){
                this.roundabout$setStand(null);
            }
            return this.roundabout$Stand;
        }
    }
    public boolean roundabout$hasStandOut() {
        StandEntity standOut = this.roundabout$getStand();
        return (standOut != null && standOut.isAlive() && !standOut.isRemoved());
    }


    /** Set Direction input. This is part of stand rendering as leaning.
     * @see FollowingStandEntity#setMoveForward(Byte)  */
    public void roundabout$setDI(byte forward, byte strafe){
        //RoundaboutMod.LOGGER.info("MF:"+ forward);
        if (roundabout$Stand instanceof FollowingStandEntity FE){
            if (!roundabout$User.isShiftKeyDown() && roundabout$User.isSprinting()){
                forward*=2;}
            FE.setMoveForward(forward);
        }
    }

    /** Retooled vanilla riding code to update the location of a stand every tick relative to the entity it
     * is the user of.
     * @see FollowingStandEntity#setMoveForward */
    @Unique
    @Override
    public void roundabout$updateStandOutPosition(FollowingStandEntity stand) {
        this.roundabout$updateStandOutPosition(stand, Entity::setPos);
    }

    @Unique
    public void roundabout$updateStandOutPosition(FollowingStandEntity stand, Entity.MoveFunction positionUpdater) {
        if (!(this.roundabout$hasStandOut())) {
            return;
        }

            byte OT = stand.getOffsetType();
            if (OffsetIndex.OffsetStyle(OT) != OffsetIndex.LOOSE_STYLE) {

                Vec3 grabPos = stand.getStandOffsetVector(roundabout$User);
                positionUpdater.accept(stand, grabPos.x, grabPos.y, grabPos.z);

                if (!this.level().isClientSide() || ((LivingEntity) (Object) this) instanceof Player) {
                    stand.setYRot(roundabout$User.getYHeadRot() % 360);
                    stand.setXRot(roundabout$User.getXRot());
                    stand.setYBodyRot(roundabout$User.getYHeadRot() % 360);
                    stand.setYHeadRot(roundabout$User.getYHeadRot() % 360);
                    if (OffsetIndex.OffsetStyle(OT) == OffsetIndex.FIXED_STYLE) {
                        float rot;
                        if (OT == OffsetIndex.BENEATH) {
                            rot = (roundabout$User.getYRot()) % 360;
                        } else if (OT == OffsetIndex.GUARD_AND_TRACE) {
                            BlockHitResult dd = roundabout$getStandPowers().getAheadVec(30);
                            rot = (roundabout$User.getYHeadRot()) % 360;
                            stand.setXRot(roundabout$getStandPowers().getLookAtPlacePitch(stand, dd.getBlockPos().getCenter()));
                        } else {
                            rot = (float) ((roundabout$User.getYHeadRot() - (stand.getPunchYaw(stand.getAnchorPlaceAttack(),
                                    0.36))) % 360);
                        }
                        stand.setYRot(rot);
                        stand.setYBodyRot(rot);
                    }
                }
            } else {
                if (stand.lockPos()) {
                    positionUpdater.accept(stand, stand.getX(), stand.getY(), stand.getZ());
                }
            }
    }

    public void roundabout$onStandOutLookAround(StandEntity passenger) {
    }

    public void roundabout$removeStandOut() {
        this.roundabout$Stand = null;
        ((LivingEntity) (Object) this).getEntityData().set(ROUNDABOUT$STAND_ID, -1);
        //this.emitGameEvent(GameEvent.ENTITY_DISMOUNT, passenger);
    }

    @Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
    private void roundabout$initDataTrackerRoundabout(CallbackInfo ci) {
        if (!((LivingEntity)(Object)this).getEntityData().hasItem(ROUNDABOUT$STAND_ID)) {
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$STAND_ID, -1);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT_TS_DAMAGE, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$LOCACACA_CURSE, (byte) -1);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$ON_STAND_FIRE, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$BLEED_LEVEL, -1);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$GLOW, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$IS_BUBBLE_ENCASED, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$IS_BOUND_TO, -1);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$IS_ZAPPED_TO_ATTACK, -1);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$TRUE_INVISIBILITY, -1);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$ADJUSTED_GRAVITY, -1);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$ONLY_BLEEDING, true);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$COMBAT_MODE, false);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$STAND_DISC, "");
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$STAND_ACTIVE, false);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$IDLE_POS, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$STAND_SKIN, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$STAND_ANIMATION, (byte) 0);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$UNIQUE_STAND_MODE_TOGGLE, false);
        }
    }

    @Inject(method = "handleEntityEvent", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$HandleStatus(byte $$0, CallbackInfo ci) {
        if ($$0 == 29){
            if (this.roundabout$isGuarding()) {
                if (!this.roundabout$getGuardBroken()) {
                    ((Entity) (Object) this).level().playLocalSound(((Entity) (Object) this).getX(), ((Entity) (Object) this).getY(), ((Entity) (Object) this).getZ(),
                            ModSounds.STAND_GUARD_SOUND_EVENT, ((Entity) (Object) this).getSoundSource(),
                            0.8F, 0.9F + ((Entity) (Object) this).level().random.nextFloat() * 0.3f, false);
                    //((Entity) (Object) this).playSound(ModSounds.STAND_GUARD_SOUND_EVENT, 0.8f, 0.9f + ((Entity) (Object) this).level().random.nextFloat() * 0.3f);
                } else {
                    ((Entity) (Object) this).level().playLocalSound(((Entity) (Object) this).getX(), ((Entity) (Object) this).getY(), ((Entity) (Object) this).getZ(),
                            SoundEvents.SHIELD_BREAK, ((Entity) (Object) this).getSoundSource(),
                            1f, 1.5f, false);
                    //((Entity) (Object) this).playSound(SoundEvents.SHIELD_BREAK, 1f, 1.5f);
                }
                ci.cancel();
            }
        }
    }
    @Inject(method = "isBlocking", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$isBlockingRoundabout(CallbackInfoReturnable<Boolean> ci) {
        if (this.roundabout$isGuarding()){
            ci.setReturnValue(this.roundabout$isGuardingEffectively());
        }
    }
    @Inject(method = "doAutoAttackOnTouch", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$doAttackOnTouch(LivingEntity $$0, CallbackInfo ci) {
        if (!$$0.is(this.roundabout$getThrower())){
            DamageSource $$5 = ModDamageTypes.of($$0.level(), ModDamageTypes.THROWN_OBJECT, this.roundabout$getThrower());
            $$0.hurt($$5, 8);
        }
    }

    @Inject(method = "getDamageAfterArmorAbsorb", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$ApplyArmorToDamage(DamageSource $$0, float $$1, CallbackInfoReturnable<Float> ci){
        if (MainUtil.isArmorBypassingButNotShieldBypassing($$0)) {
            ci.setReturnValue($$1);
        }
    }

    /**Here, we cancel barrage if it has not "wound up" and the user is hit*/
    @Inject(method = "hurt", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$RoundaboutDamage(DamageSource $$0, float $$1, CallbackInfoReturnable<Boolean> ci) {

        if ($$0.getEntity() instanceof Player pe && !$$0.isIndirect()
        && !$$0.is(DamageTypes.THORNS)&& !$$0.is(ModDamageTypes.CORPSE) &&
                !$$0.is(ModDamageTypes.CORPSE_EXPLOSION) &&
                !$$0.is(ModDamageTypes.CORPSE_ARROW)){
            if (((StandUser)pe).roundabout$getStandPowers().interceptDamageDealtEvent($$0,$$1, ((LivingEntity)(Object)this))){
                ci.setReturnValue(false);
                return;
            }
        }

        if ($$0.is(DamageTypes.ARROW) && $$0.getEntity() instanceof FallenMob FM){
            if (!(((LivingEntity)(Object)this) instanceof Player) && FM.getController() == this.getId()){
                ci.setReturnValue(false);
                return;
            }
            if (this.roundabout$getStandPowers().getReducedDamage(this)){
                $$1/=3.2f;
                $$1*= (float) (ClientNetworking.getAppropriateConfig().justiceSettings.corpseDamageMultOnPlayers *0.01);
                $$1 = FM.getDamageMod($$1);
            } else {
                $$1 *= (float) (ClientNetworking.getAppropriateConfig().justiceSettings.corpseDamageMultOnMobs *0.01);
                $$1 = FM.getDamageMod($$1);
            }
            ci.setReturnValue(hurt(ModDamageTypes.of(this.level(), ModDamageTypes.CORPSE_ARROW, FM),
                    $$1));
            return;
        } else if ($$0.is(DamageTypes.PLAYER_EXPLOSION) && $$0.getEntity() instanceof FallenMob FM){
            if (this.roundabout$getStandPowers().getReducedDamage(this)){
                $$1/=2;
                $$1*= (float) (ClientNetworking.getAppropriateConfig().justiceSettings.corpseDamageMultOnPlayers *0.01);
                $$1 = FM.getDamageMod($$1);
            } else {
                $$1 *= (float) (ClientNetworking.getAppropriateConfig().justiceSettings.corpseDamageMultOnMobs *0.01);
                $$1 = FM.getDamageMod($$1);
            }
            Entity ent2 = FM;
            if (FM.getController() > 0 && FM.getController() != this.getId()){
                ent2 = FM.controller;
            }
            ci.setReturnValue(hurt(ModDamageTypes.of(this.level(), ModDamageTypes.CORPSE_EXPLOSION, ent2, $$0.getDirectEntity()),
                    $$1));
            return;
        }

        if ($$0.is(ModDamageTypes.GASOLINE_EXPLOSION)){
            if (roundabout$gasolineIFRAMES > 0){
                ci.setReturnValue(false);
                return;
            }
        }

        if (this.roundabout$gasTicks > -1) {
            if ($$0.is(DamageTypeTags.IS_FIRE) || ($$0.getDirectEntity() instanceof Projectile && $$0.getDirectEntity().isOnFire())) {
                float power = MainUtil.gasDamageMultiplier()*17;
                if ($$0.is(DamageTypeTags.IS_FIRE) || $$0.is(ModDamageTypes.MATCH)) {
                    if ($$0.getDirectEntity() instanceof Projectile) {
                        if ($$0.getDirectEntity() instanceof MatchEntity){
                            if (((MatchEntity) $$0.getDirectEntity()).isBundle){
                                power = MainUtil.gasDamageMultiplier()*23;
                            } else {
                                power = MainUtil.gasDamageMultiplier()*18;
                            }
                        }
                    } else {
                        power = MainUtil.gasDamageMultiplier()*14;
                    }
                }
                this.roundabout$setGasolineTime(-1);
                if (!this.level().isClientSide) {
                    ((ServerLevel) this.level()).sendParticles(ParticleTypes.FLAME, this.getX(), this.getY()+this.getEyeHeight(), this.getZ(),
                            40, 0.0, 0.2, 0.0, 0.2);
                    ((ServerLevel) this.level()).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY()+this.getEyeHeight(), this.getZ(),
                            1, 0.5, 0.5, 0.5, 0.2);
                    MainUtil.gasExplode(null, (ServerLevel) this.level(), this.getOnPos(), 0, 2, 4, power);
                }

                ci.setReturnValue(true);
                return;
            } else if ($$0.is(ModDamageTypes.STAND_FIRE)) {
                float power = MainUtil.gasDamageMultiplier()*10;
                this.roundabout$setGasolineTime(-1);
                if (!this.level().isClientSide) {
                    ((ServerLevel) this.level()).sendParticles(ParticleTypes.FLAME, this.getX(), this.getY()+this.getEyeHeight(), this.getZ(),
                            40, 0.0, 0.2, 0.0, 0.2);
                    ((ServerLevel) this.level()).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY()+this.getEyeHeight(), this.getZ(),
                            1, 0.5, 0.5, 0.5, 0.2);
                    MainUtil.gasExplode(null, (ServerLevel) this.level(), this.getOnPos(), 0, 2, 4, power);
                }

                ci.setReturnValue(true);
                return;
            }
        }
        if ($$0.getDirectEntity() != null && !$$0.is(ModDamageTypes.STAND_FIRE)) {
           if (this.roundabout$getStandPowers().preCanInterruptPower($$0.getDirectEntity(),MainUtil.isStandDamage($$0))) {
                this.roundabout$tryPower(PowerIndex.NONE, true);
                if (!(((LivingEntity)(Object)this) instanceof Player)){
                    this.roundabout$setAttackTimeDuring(this.roundabout$getStandPowers().getMobRecoilTime());
                }
            }
            this.roundabout$setIdleTime(-1);
        }
    }

    /**Hex prevents eating effects from golden apples. Once you reach level 3 (commands only), all foods lose them*/
    @Inject(method = "addEatEffect", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$addEatEffect(ItemStack $$0, Level $$1, LivingEntity $$2, CallbackInfo ci) {

        if (((IEntityAndData)this).roundabout$getTrueInvisibility() > -1 &&
                ClientNetworking.getAppropriateConfig().achtungSettings.revealLocationWhenFinishedEating){
            ((IEntityAndData)this).roundabout$setTrueInvisibility(-1);
        }
        if (this.hasEffect(ModEffects.HEX)) {
            int hexLevel = this.getEffect(ModEffects.HEX).getAmplifier();
            if ((hexLevel >= 0 && $$0.is(Items.ENCHANTED_GOLDEN_APPLE)) || (hexLevel >= 1 && $$0.is(Items.GOLDEN_APPLE))
                    || hexLevel >= 2){
                ci.cancel();
                return;
            }
        }
        roundabout$getStandPowers().eatEffectIntercept($$0,$$1,$$2);
    }



    @Unique
    @Override
    public boolean roundabout$isOnStandFire() {
        boolean $$0 = this.level() != null && this.level().isClientSide;
        return  (roundabout$remainingFireTicks > 0 || $$0 && roundabout$getOnStandFire() > 0);
    }

    /**Prevent you from hearing every hit in a rush*/
    @Inject(method = "playHurtSound", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$PlayHurtSound(DamageSource $$0, CallbackInfo ci) {
        if (this.roundabout$isDazed() || $$0.is(ModDamageTypes.STAND_RUSH)) {
            ci.cancel();
        }
    }

    /**This Should prevent repeated crossbow charging on barrage*/
    @Inject(method = "updatingUsingItem", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$TickActiveItemStack(CallbackInfo ci) {
        if (this.roundabout$isDazed()) {
            ci.cancel();
        }
    }


    /***
     * Invisiblity functions for Achtung Baby. Note that only Living Entities use tracked/synched entitydata,
     * so regular entities use a function in IEntityAndData instead.
     */
    @Unique
    @Override
    public void roundabout$setTrueInvis(int bound) {
        if (this.entityData.hasItem(ROUNDABOUT$TRUE_INVISIBILITY)) {
            roundabout$zappedTicks = 0;
            this.getEntityData().set(ROUNDABOUT$TRUE_INVISIBILITY, bound);
        }
    }
    @Unique
    @Override
    public int roundabout$getTrueInvis() {
        if (this.entityData.hasItem(ROUNDABOUT$TRUE_INVISIBILITY)) {
            return this.getEntityData().get(ROUNDABOUT$TRUE_INVISIBILITY);
        }
        return -1;
    }

    @Shadow
    protected float getDamageAfterArmorAbsorb(DamageSource $$0, float $$1){
        return 0;
    }

    @Shadow
    protected float getDamageAfterMagicAbsorb(DamageSource $$0, float $$1) {
        return 0;
    }

    @Shadow protected float lastHurt;

    /**Part of Registering Stand Guarding as a form of Blocking*/
    @Inject(method = "hurt", at = @At(value = "HEAD"))
    private void roundabout$RoundaboutDamage2(DamageSource source, float amount, CallbackInfoReturnable<Boolean> ci) {
        roundabout$logSource = source;
    }

    @Unique
    DamageSource roundabout$logSource = null;

    public DamageSource roundabout$getLogSource(){
        return roundabout$logSource;
    }

    /**For things like bubble encasement delta*/
    @Unique
    public Vec3 roundabout$storedVelocity = Vec3.ZERO;
    @Unique
    @Override
    public void roundabout$setStoredVelocity(Vec3 store){
        roundabout$storedVelocity = store;
    }
    @Unique
    @Override
    public Vec3 roundabout$getStoredVelocity(){
        return roundabout$storedVelocity;
    }

    @Unique
    public Vec3 roundabout$frictionSave = Vec3.ZERO;
    @Unique
    public boolean roundabout$skipFriction = false;

    /**Soft and Wet slipperiness friction plunder*/

    @Inject(method = "aiStep", at = @At(value = "HEAD"))
    public void roundabout$aiStep(CallbackInfo ci) {
        if (MainUtil.canHaveFrictionTaken(((LivingEntity) (Object)this))) {
            if (((ILevelAccess) this.level()).roundabout$isFrictionPlundered(this.blockPosition()) ||
                    ((ILevelAccess) this.level()).roundabout$isFrictionPlunderedEntity(this)
            ) {
                if (this.onGround()) {
                    if (roundabout$frictionSave.equals(Vec3.ZERO)) {
                        if (this.getDeltaMovement().x != 0 || this.getDeltaMovement().z != 0) {
                            roundabout$frictionSave = this.getDeltaMovement();
                            roundabout$frictionSave = new Vec3(roundabout$frictionSave.x, 0, roundabout$frictionSave.z);
                            double scale = 0.36;
                            if (!this.isSprinting()) {
                                scale *= 1.3;
                            }
                            this.setDeltaMovement((roundabout$frictionSave.normalize()).scale(scale));
                        }
                        roundabout$skipFriction = false;
                    } else {
                        this.jumping = false;
                        this.xxa = 0.0F;
                        this.zza = 0.0F;
                        double scale = 0.36;
                        if (!this.isSprinting()) {
                            scale *= 1.3;
                        }
                        Vec3 yesVec = this.getPosition(1).add(this.getDeltaMovement());
                        BlockPos yesVec2 = new BlockPos((int) yesVec.x, (int) (this.position().y), (int) yesVec.z);
                        if (this.level().getBlockState(yesVec2).isSolid()) {
                            roundabout$frictionSave = new Vec3(Math.random() - 0.5, 0, Math.random() - 0.5);
                        }
                        this.setDeltaMovement((roundabout$frictionSave.normalize()).scale(scale));
                        roundabout$skipFriction = true;
                    }

                    if (!this.level().isClientSide()) {
                        ((ServerLevel) this.level()).sendParticles(ModParticles.FRICTIONLESS,
                                this.getX(), this.getY() + 0.2, this.getZ(),
                                1, 0, 0, 0, 0.015);
                    }
                } else {
                    roundabout$frictionSave = Vec3.ZERO;
                }
            } else {
                roundabout$frictionSave = Vec3.ZERO;
            }
        }
    }

    /**Update gravitational pull*/
    @Unique
    @Override
    public void roundabout$adjustGravity(){
        float gravityConstant = 1000;
        boolean modified = false;

        if (roundabout$isBubbleEncased()){
            gravityConstant *= 0.9F;
            modified = true;
        }

        if (modified){
            int gravityConstandAdjusted = (int) gravityConstant;
            roundabout$setAdjustedGravity(gravityConstandAdjusted);
        } else {
            roundabout$setAdjustedGravity(-1);
        }
    }

    @Override
    public double roundabout$getGravity(double ogGrav){
        if (this.getEntityData().hasItem(ROUNDABOUT$ADJUSTED_GRAVITY) && this.getDeltaMovement().y <= 0){
            double basegrav = (double) this.getEntityData().get(ROUNDABOUT$ADJUSTED_GRAVITY);
            if (basegrav >= 0) {
                ogGrav *= (basegrav / 1000);
                return ogGrav;
            }
        } else if (roundabout$getBigJump() && this.getDeltaMovement().y >= 0){
            return ogGrav;
        }
        return ogGrav;
    }

    @SuppressWarnings("deprecation")
    @Inject(method = "travel", at = @At(value = "HEAD"))
    public void roundabout$travelHead(CallbackInfo ci) {
        roundabout$adjustGravity();

        if (this.isControlledByLocalInstance()) {
            float curr = roundabout$getBigJumpCurrentProgress();
            float max = roundabout$getBonusJumpHeight();
            if (roundabout$getBigJump() || (curr < 1 && getDeltaMovement().y >= 0)) {
                if (curr < max+1) {
                    roundabout$setBigJumpCurrentProgress(curr+0.495F);
                    Vec3 $$0 = this.getDeltaMovement();


                    if (!onGround()){
                        if (roundabout$getBigJump()){
                            this.setDeltaMovement($$0.x*0.91, (double) this.getJumpPower(), $$0.z*0.91);
                        }
                    }

                }
            } else {
                /**Air Drag*/
                Vec3 $$0 = this.getDeltaMovement();
                if (roundabout$isBubbleEncased() && !onGround()){
                    this.setDeltaMovement($$0.x*0.95, (double)$$0.y, $$0.z*0.95);

                }
            }
        }

    }


    /**Entities who are caught in a barrage stop moving from their own volition in the x and z directions.*/
    @ModifyVariable(method = "travel(Lnet/minecraft/world/phys/Vec3;)V", at = @At(value = "HEAD"))
    private Vec3 roundabout$Travel(Vec3 $$0) {
        if (this.roundabout$isDazed()) {
            return new Vec3(0,0,0);
        } else {
            return $$0;
        }
    }

    /**This code stops a barrage target from losing velocity, preventing lag spikes from causing them to drop.*/
    @ModifyVariable(method = "travel(Lnet/minecraft/world/phys/Vec3;)V", at = @At("STORE"))
    private double roundabout$Travel2(double $$1) {
        if (this.roundabout$isDazed()) {
            return 0;
        } else {
            return $$1;
        }
    }

    /**Modifies the gravity influence*/
    @ModifyVariable(method = "travel(Lnet/minecraft/world/phys/Vec3;)V", at = @At(value = "STORE"),ordinal = 0)
    private double roundabout$TravelGravity(double $$1) {
        if (this.roundabout$isDazed()) {
            return 0;
        } else {
            return roundabout$getGravity($$1);
        }
    }
    @ModifyVariable(method = "travel(Lnet/minecraft/world/phys/Vec3;)V", at = @At(value = "STORE"),ordinal = 1)
    private double roundabout$TravelGravitySlowFall(double $$1) {
        if (this.roundabout$isDazed()) {
            return 0;
        } else {
            return roundabout$getGravity($$1);
        }
    }

    @ModifyVariable(method = "travel(Lnet/minecraft/world/phys/Vec3;)V", at = @At("STORE"),ordinal = 0)
    private double roundabout$Travel3(double $$1) {
        float cooking = 0.2F;
        if (((LivingEntity)(Object)this) instanceof Player && ((TimeStop)((LivingEntity)(Object)this).level()).isTimeStoppingEntity((LivingEntity)(Object)this)) {

            boolean TSJumping = ((IPlayerEntity)this).roundabout$GetPos() == PlayerPosIndex.TS_FLOAT;
            if (TSJumping) {
                    float cooking2 = (float) (((LivingEntity)(Object)this).getDeltaMovement().y + 0.2);
                    if (((LivingEntity)(Object)this) instanceof Player && ((Player)(Object)this).isCrouching()) {
                        if (cooking2 >= 0.0001) {
                            cooking = 0.0001F;
                        }
                    } else {
                        if (cooking2 >= 0.1) {
                            cooking = 0.1F;
                        }
                    }
                    ((LivingEntity)(Object)this).setDeltaMovement(
                            ((LivingEntity)(Object)this).getDeltaMovement().x,
                            cooking,
                            ((LivingEntity)(Object)this).getDeltaMovement().z
                    );
            }

            boolean $$2 = ((LivingEntity)(Object)this).getDeltaMovement().y <= 0.0;
            ((LivingEntity) (Object) this).resetFallDistance();
            if ($$2) {
                if (this.roundabout$getTSJump()){
                    return 0;
                } else {
                    return $$1;
                }
            }
        }

        if (this.roundabout$leapTicks > -1 || roundabout$isBubbleEncased()){
            ((LivingEntity) (Object) this).resetFallDistance();
        }
        return $$1;
    }
    @Inject(method = "getVisibilityPercent", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$getVisibilityPercent(CallbackInfoReturnable<Double> cir) {
        if (roundabout$getStandPowers() instanceof PowersAchtungBaby PB && PB.inBurstState() && ClientNetworking.getAppropriateConfig().achtungSettings.invisiBurstAlertsMobs){
            cir.setReturnValue(0.33);
        }
    }
    /**Hide from mobs with armor on*/
    @Inject(method = "getArmorCoverPercentage", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$getArmorCoverPercentage(CallbackInfoReturnable<Float> cir) {
        if (roundabout$getTrueInvis() > -1 && ClientNetworking.getAppropriateConfig().achtungSettings.hidesArmor) {
            cir.setReturnValue(0f);
        }
    }
    /**This code prevents you from swimming upwards while barrage clashing*/
    @Inject(method = "jumpInLiquid", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$swimUpward(TagKey<Fluid> $$0, CallbackInfo ci) {
        if (this.roundabout$isClashing()) {
            ci.cancel();
        }
    }

    @Inject(method = "checkFallDamage", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$fallOn(double $$0, boolean $$1, BlockState $$2, BlockPos $$3, CallbackInfo ci) {
        if (roundabout$isBubbleEncased()){
            if (!this.level().isClientSide() && $$2.getBlock() instanceof PointedDripstoneBlock){
                roundabout$setBubbleEncased((byte) 0);
                this.level().playSound(null, $$3, ModSounds.BUBBLE_POP_EVENT,
                        SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
                ((ServerLevel) this.level()).sendParticles(ModParticles.BUBBLE_POP,
                        this.getX(), this.getY() + this.getBbHeight() * 0.5, this.getZ(),
                        5, 0.25, 0.25, 0.25, 0.025);
            }
        }
    }

    /**Villager call to action*/
    @Inject(method = "actuallyHurt", at = @At(value = "HEAD"), cancellable = true)
    protected void rooundabout$actuallyHurt(DamageSource $$0, float $$1, CallbackInfo ci) {
        if (!this.isInvulnerableTo($$0)) {
            if (((LivingEntity)(Object)this) instanceof AbstractVillager AV && !($$0.getEntity()
                    instanceof AbstractVillager) && $$0.getEntity() instanceof LivingEntity LE) {
                    AABB AAB = this.getBoundingBox().inflate(10.0, 8.0, 10.0);
                    List<? extends AbstractVillager> ENT = this.level().getNearbyEntities(AbstractVillager.class, MainUtil.attackTargeting, ((LivingEntity)(Object)this), AAB);

                    for (AbstractVillager $$3 : ENT) {
                        if (!((StandUser)$$3).roundabout$getStandDisc().isEmpty()){
                            if($$3.getTarget() == null && !(LE instanceof Player PE && PE.isCreative())){
                                $$3.setTarget(LE);
                            }
                        }
                    }
            }




            if ($$0.getEntity() != null
                    && !$$0.is(DamageTypes.THORNS)){
                if ($$0.getEntity() instanceof Player pe){
                    if (((StandUser)pe).roundabout$getStandPowers().interceptSuccessfulDamageDealtEvent($$0,$$1, ((LivingEntity)(Object)this))){
                        ci.cancel();
                        return;
                    }
                }
            }

            if (($$0.getEntity() != null || $$0.is(DamageTypes.THROWN)) && !$$0.is(DamageTypes.THORNS)){
                if (((IEntityAndData)this).roundabout$getTrueInvisibility() > -1 &&
                        ClientNetworking.getAppropriateConfig().achtungSettings.revealLocationWhenDamagingOrHurt){
                    ((IEntityAndData)this).roundabout$setTrueInvisibility(-1);
                }
                if ($$0.getEntity() != null){
                    if (((IEntityAndData)$$0.getEntity()).roundabout$getTrueInvisibility() > -1 &&
                            ClientNetworking.getAppropriateConfig().achtungSettings.revealLocationWhenDamagingOrHurt){
                        ((IEntityAndData)$$0.getEntity()).roundabout$setTrueInvisibility(-1);
                    }
                }
                if ($$0.getDirectEntity() != null){
                    if (((IEntityAndData)$$0.getDirectEntity()).roundabout$getTrueInvisibility() > -1 &&
                            ClientNetworking.getAppropriateConfig().achtungSettings.revealLocationWhenDamagingOrHurt){
                        ((IEntityAndData)$$0.getDirectEntity()).roundabout$setTrueInvisibility(-1);
                    }
                }
            }
        }
        if (roundabout$mutualActuallyHurt($$0,$$1)){
            ci.cancel();
        }
    }

    @Override
    @Unique
    public float roundabout$mutualGetSpeed(float basis){
        byte curse = this.roundabout$getLocacacaCurse();
        if (curse > -1) {
            if (curse == LocacacaCurseIndex.RIGHT_LEG || curse == LocacacaCurseIndex.LEFT_LEG) {
                basis = (basis * 0.82F);
            } else if (curse == LocacacaCurseIndex.CHEST) {
                basis = (basis * 0.85F);
            }
        }

        int zapped = roundabout$getZappedToID();
        if (zapped > -1 && !(((LivingEntity)(Object)this) instanceof Drowned)){
            Entity ent = level().getEntity(zapped);
            if (ent != null){
                float dist1 = distanceTo(ent);
                float dist2 = (float) position().add(getDeltaMovement()).distanceTo(ent.position());
                if (dist1 >= dist2){
                    basis *= ClientNetworking.getAppropriateConfig().survivorSettings.speedMultiplierTowardsEnemy;
                } else {
                    basis *= ClientNetworking.getAppropriateConfig().survivorSettings.speedMultiplierAwayFromEnemy;
                }
            }
        }

        return basis;
    }

    @Override
    @Unique
    public boolean roundabout$mutualActuallyHurt(DamageSource $$0, float $$1){
        if (!this.isInvulnerableTo($$0)) {

            Entity zent = $$0.getEntity();
            if (zent != null && zent instanceof LivingEntity LE){
                ItemStack stack = LE.getMainHandItem();

                if (stack != null && !stack.isEmpty() && stack.is(ModItems.SCISSORS)) {
                    if (MainUtil.getMobBleed(this)) {
                        roundabout$setBleedLevel(0);
                        addEffect(new MobEffectInstance(ModEffects.BLEED, 300, 0), LE);
                    }
                    stack.hurtAndBreak(1, LE, $$0x -> $$0x.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                }
            }
            /**Big bubble pops*/
            if (roundabout$isBubbleEncased()){
                if ($$0.getEntity() != null || $$0.is(DamageTypes.THORNS) || $$0.is(DamageTypes.ARROW)
                        || $$0.is(DamageTypes.STALAGMITE) || $$0.is(DamageTypes.FALLING_STALACTITE)
                        || $$0.is(DamageTypes.SWEET_BERRY_BUSH)
                        || $$0.is(DamageTypes.THROWN) || $$0.is(ModDamageTypes.KNIFE)
                        || $$0.is(DamageTypes.CACTUS) || $$0.is(ModDamageTypes.THROWN_OBJECT)
                        || $$0.is(ModDamageTypes.BARBED_WIRE) || $$0.is(ModDamageTypes.STATUE)){
                    roundabout$setBubbleEncased((byte) 0);
                    if (!this.level().isClientSide()){
                        this.level().playSound(null, this.blockPosition(), ModSounds.BUBBLE_POP_EVENT,
                                SoundSource.PLAYERS, 2F, (float)(0.98+(Math.random()*0.04)));
                        ((ServerLevel) this.level()).sendParticles(ModParticles.BUBBLE_POP,
                                this.getX(), this.getY() + this.getBbHeight()*0.5, this.getZ(),
                                5, 0.25, 0.25,0.25, 0.025);
                    }
                }
            }

            roundabout$getStandPowers().onActuallyHurt($$0,$$1);

            Entity bound = roundabout$getBoundTo();
            if (bound != null && ($$0.getEntity() != null || $$0.is(DamageTypes.MAGIC) || $$0.is(DamageTypes.EXPLOSION)) && !$$0.is(ModDamageTypes.STAND_FIRE)){
                roundabout$dropString();
            }
        }
        return false;
    }

    /**swing sharp item to pop bubble */
    @Inject(method = "swing(Lnet/minecraft/world/InteractionHand;Z)V", at = @At(value = "HEAD"), cancellable = true)
    protected void rooundabout$swing(InteractionHand $$0, boolean $$1, CallbackInfo ci) {
        if (!this.level().isClientSide()){
            if (roundabout$isBubbleEncased()) {
                ItemStack stack = this.getItemInHand($$0);
                if (stack.getItem() instanceof ScissorItem || stack.getItem() instanceof SwordItem
                        || stack.getItem() instanceof KnifeItem || stack.getItem() instanceof AxeItem
                        || stack.getItem() instanceof GlaiveItem
                        || stack.getItem() instanceof HarpoonItem
                        || stack.getItem() instanceof TridentItem
                        || stack.getItem() instanceof ShearsItem
                        || stack.getItem() instanceof PickaxeItem
                        || stack.getItem() instanceof ArrowItem
                        || (stack.getItem() instanceof BlockItem BI && (BI.getBlock() instanceof CactusBlock
                || BI.getBlock() instanceof GoddessStatueBlock || BI.getBlock() instanceof SweetBerryBushBlock || BI.getBlock() instanceof BarbedWireBlock))) {
                    roundabout$setBubbleEncased((byte) 0);
                    this.level().playSound(null, this.blockPosition(), ModSounds.BUBBLE_POP_EVENT,
                            SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
                    ((ServerLevel) this.level()).sendParticles(ModParticles.BUBBLE_POP,
                            this.getX(), this.getY() + this.getBbHeight() * 0.5, this.getZ(),
                            5, 0.25, 0.25, 0.25, 0.025);
                }
            }
        }
    }


    /**reduce or nullify fall damage */
    @Inject(method = "checkFallDamage", at = @At(value = "HEAD"), cancellable = true)
    protected void rooundabout$checkFallDamage(double $$0, boolean $$1, BlockState $$2, BlockPos $$3, CallbackInfo ci) {

        if (this.roundabout$leapTicks > -1) {
            this.level().gameEvent(GameEvent.HIT_GROUND, this.getPosition(0F),
                    GameEvent.Context.of(this, this.mainSupportingBlockPos.map(blockPos -> this.level().getBlockState((BlockPos)blockPos)).orElse(this.level().getBlockState($$3))));
        }
    }

    /**Reduced gravity changes fall damage calcs*/
    @Inject(method = "calculateFallDamage", at = @At(value = "HEAD"), cancellable = true)
    protected void rooundabout$calculateFallDamage(float $$0, float $$1, CallbackInfoReturnable<Integer> cir) {

        if (this.roundabout$leapTicks > -1 || roundabout$isBubbleEncased()) {
            cir.setReturnValue(0);
            return;
        }
        int yesInt = roundabout$getAdjustedGravity();
        if (yesInt > 0){
            cir.setReturnValue(roundabout$calculateFallDamage($$0,$$1,yesInt));
        }
    }


    /**gravity calcs into fall damage*/
    @Unique
    protected int roundabout$calculateFallDamage(float blockmultiplier, float fallLength,int yesInt) {
        if (this.getType().is(EntityTypeTags.FALL_DAMAGE_IMMUNE) || roundabout$isBubbleEncased()) {
            return 0;
        } else {
            MobEffectInstance jumpEffect = this.getEffect(MobEffects.JUMP);
            float jumpLevel = jumpEffect == null ? 0.0F : (float)(jumpEffect.getAmplifier() + 1);
            return Mth.ceil(((roundabout$getGravity(blockmultiplier)) - 3.0F - jumpLevel) * fallLength);
        }
    }

    /**This code makes stand user mobs resist attacks from other mobs*/
    @Inject(method = "getDamageAfterArmorAbsorb", at = @At(value = "RETURN"), cancellable = true)
    protected void rooundabout$armorAbsorb(DamageSource $$0, float $$1, CallbackInfoReturnable<Float> cir) {
        if (((LivingEntity)(Object)this) instanceof Mob){
            if (!((StandUser)this).roundabout$getStandDisc().isEmpty()){
                if (this.getMaxHealth() > 1) {
                    if (this.getMaxHealth() <= 3) {
                        $$1 *= 0.5F;
                    } else if (this.getMaxHealth() <= 6) {
                        $$1 *= 0.75F;
                    }
                }
                if (($$0.is(DamageTypes.MOB_ATTACK) || $$0.is(DamageTypes.MOB_PROJECTILE))
                        || $$0.is(DamageTypes.MOB_ATTACK_NO_AGGRO)){
                    $$1*=0.5F;
                }
                cir.setReturnValue($$1);
            }
        }
        boolean modified = false;
        if (this.hasEffect(ModEffects.FACELESS)) {
            float amt = (float) (0.15* this.getEffect(ModEffects.FACELESS).getAmplifier()+0.15F);
            $$1 = ($$1+($$1*amt));
            modified = true;
        }
        if (roundabout$getZappedToID() > -1){
            if (!MainUtil.isMeleeDamage($$0)){
                $$1 = $$1*ClientNetworking.getAppropriateConfig().survivorSettings.resilienceToNonMeleeAttacksWhenZapped;
                modified = true;
            }
        }

        if ($$0.getEntity() instanceof LivingEntity LE){
            if (((StandUser)LE).roundabout$getZappedToID() > -1){
                if (MainUtil.isMeleeDamage($$0)){
                    $$1 = $$1*ClientNetworking.getAppropriateConfig().survivorSettings.buffToMeleeAttacksWhenZapped;
                    modified = true;

                    if (LE.getMainHandItem() != null && LE.getMainHandItem().isEmpty()){
                        float power = ClientNetworking.getAppropriateConfig().survivorSettings.bonusDamageWhenPunching;
                        if (power > 0){
                            $$1 += (CombatRules.getDamageAfterAbsorb(power, (float)this.getArmorValue(), (float)this.getAttributeValue(Attributes.ARMOR_TOUGHNESS)));
                        }
                        if (MainUtil.getMobBleed(LE)){
                            MainUtil.makeBleed(LE,0,200,LE);
                        }
                    }
                }
            }
        }

        if (modified){
            cir.setReturnValue($$1);
        }
    }

    @Inject(method = "die", at = @At("HEAD"))
    protected void roundabout$die(DamageSource $$0, CallbackInfo ci){
        if ($$0.getEntity() instanceof FallenMob fm){
            Entity ent2 = fm;
            if (fm.getController() > 0  && fm.getController() != fm.getId()){
                ent2 = fm.controller;
            }
            DamageSource corpseCorrect = new DamageSource($$0.typeHolder(), ent2, ent2);
            if (ent2 instanceof Player PE){
                this.setLastHurtByPlayer(PE);
                this.getCombatTracker().recordDamage(corpseCorrect, 0);
            }
        }
        StandEntity stnd = roundabout$getStand();
        if (stnd != null){
            stnd.setMaster(null);
            stnd.setFollowing(null);
            roundabout$setStand(null);
        }
        if (!this.isRemoved() && !this.dead) {
            if ($$0.is(ModDamageTypes.STAND_FIRE) || $$0.is(ModDamageTypes.CROSSFIRE) || roundabout$isOnStandFire()){
                this.setRemainingFireTicks(1);
            }
        }
    }

    @Inject(method = "die(Lnet/minecraft/world/damagesource/DamageSource;)V", at = @At(value = "INVOKE",
    target="Lnet/minecraft/world/entity/LivingEntity;setPose(Lnet/minecraft/world/entity/Pose;)V",
            shift= At.Shift.BEFORE), cancellable = true)
    public void roundabout$die2(DamageSource $$0, CallbackInfo ci){
        /**Corspe dropping, for Justice*/
        if ($$0.getDirectEntity() != null) {
            if (($$0.getDirectEntity() instanceof Player PE && (PE.getMainHandItem().is(ModItems.EXECUTIONER_AXE)
            || (PE.getMainHandItem().is(ModItems.SCISSORS) && ((StandUser)PE).roundabout$getStandPowers()
            instanceof PowersJustice))) && $$0.is(DamageTypes.PLAYER_ATTACK)) {
                LivingEntity ths = ((LivingEntity)(Object)this);
                boolean marked = false;
                FallenMob mb = null;
                if (!ths.isBaby()) {
                    if (ths instanceof Zombie) {
                        marked = true;
                        mb = ModEntities.FALLEN_ZOMBIE.create(this.level());

                    } else if (ths instanceof Skeleton || ths instanceof Stray) {
                        marked = true;
                        mb = ModEntities.FALLEN_SKELETON.create(this.level());
                    } else if (ths instanceof Villager || ths instanceof Witch || ths instanceof AbstractIllager
                            || ths instanceof WanderingTrader) {
                        marked = true;
                        mb = ModEntities.FALLEN_VILLAGER.create(this.level());
                    } else if (ths instanceof Spider && !(ths instanceof CaveSpider)) {
                        marked = true;
                        mb = ModEntities.FALLEN_SPIDER.create(this.level());
                    } else if (ths instanceof Creeper) {
                        marked = true;
                        mb = ModEntities.FALLEN_CREEPER.create(this.level());
                    } else if (ths instanceof Phantom){
                        marked = true;
                        mb = ModEntities.FALLEN_PHANTOM.create(this.level());
                    }
                }
                if (mb != null){
                    mb.setPos(this.position());
                    mb.setXRot(this.getXRot());
                    mb.setYRot(this.getYRot());
                    mb.setYBodyRot(this.yBodyRot);
                    mb.setYHeadRot(this.yHeadRot);
                    mb.yHeadRotO = this.getYHeadRot();
                    mb.setOldPosAndRot();
                    this.level().addFreshEntity(mb);
                }
                if (marked){
                    discard();
                    ci.cancel();
                }
            }

        }
    }
        @SuppressWarnings("deprecation")
    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
        private void roundabout$roundabouthurt(DamageSource damageSource, float $$1, CallbackInfoReturnable<Boolean> ci) {
        if (((StandUser)this).roundabout$getStandPowers() instanceof PowersD4C powers)
        {
            if (((StandUser)this).roundabout$isParallelRunning())
            {
                if (damageSource.is(DamageTypes.IN_FIRE) ||
                        damageSource.is(DamageTypes.ON_FIRE) ||
                        damageSource.is(DamageTypes.LAVA) ||
                        damageSource.is(DamageTypes.MAGIC) ||
                        damageSource.is(DamageTypes.STARVE) ||
                        damageSource.is(DamageTypes.WITHER) ||
                        damageSource.is(DamageTypes.DROWN) ||
                        damageSource.is(DamageTypes.SWEET_BERRY_BUSH) ||
                        damageSource.is(DamageTypes.CACTUS) ||
                        damageSource.is(DamageTypes.FALL)
                )
                {
                    if (damageSource.is(DamageTypes.MAGIC))
                    {
                        if (hasEffect(MobEffects.POISON))
                            return;
                    }
                    else
                        return;
                }

                ci.setReturnValue(false);
                ci.cancel();
            }

            if (powers.meltDodgeTicks >= 0)
            {
                ci.setReturnValue(false);
                ci.cancel();
                return;
            }
        }

        if (roundabout$gasolineIFRAMES > 0 && damageSource.is(ModDamageTypes.GASOLINE_EXPLOSION)){
            ci.setReturnValue(false);
            return;
        } else {
            if (damageSource.is(ModDamageTypes.GASOLINE_EXPLOSION)) {
                roundabout$gasolineIFRAMES = 10;
                roundabout$knifeIFrameTicks = 10;
                roundabout$stackedKnivesAndMatches = ClientNetworking.getAppropriateConfig().itemSettings.maxKnivesInOneHit;
            }
        }

        if (this.getVehicle() != null && this.getVehicle() instanceof StandEntity SE && !this.level().isClientSide()){
            if (SE.dismountOnHit() && (damageSource.getDirectEntity() != null || damageSource.is(DamageTypes.IN_WALL))) {
                Vec3 sanityCheckCoordinates = this.getPosition(1);
                SE.ejectPassengers();
                if (SE.getUser() != null) {
                    //((StandUser)SE.getUser())
                    boolean candoit = true;
                    Vec3 vec3d3 = SE.getUser().getForward();
                    for (var i = 0; i< this.getBbHeight(); i++){
                        if (this.level().getBlockState(new BlockPos(
                                (int) vec3d3.x(), (int) (vec3d3.y+i),
                                (int) vec3d3.z)).isSolid()){
                            candoit = false;
                            break;
                        }
                    }
                    Vec3 qVec2 = Vec3.ZERO;
                    if (candoit){
                        if (!vec3d3.equals(Vec3.ZERO) && vec3d3.distanceTo(SE.getUser().position()) < 100 &&
                        vec3d3.distanceTo(Vec3.ZERO) > 5) {
                            qVec2 = new Vec3(vec3d3.x,vec3d3.y,vec3d3.z);
                            this.dismountTo(vec3d3.x, vec3d3.y, vec3d3.z);
                        }
                    } else {
                        qVec2 = new Vec3(this.getX(),this.getY(),this.getZ());
                        Vec3 dVec = new Vec3(SE.getUser().getX(), SE.getUser().getY(), SE.getUser().getZ());
                        if (!dVec.equals(Vec3.ZERO) && dVec.distanceTo(SE.getUser().position()) < 100 &&
                                dVec.distanceTo(Vec3.ZERO) > 5) {
                            this.dismountTo(dVec.x(), dVec.y(), dVec.z());
                        }
                    }

                    if (((Entity)(Object)this) instanceof Player){
                        ((IEntityAndData)this).roundabout$setQVec2Params(qVec2);
                    }

                    if (this.getPosition(1).distanceTo(Vec3.ZERO) < 5){
                        this.teleportTo(sanityCheckCoordinates.x,sanityCheckCoordinates.y,sanityCheckCoordinates.z);
                    }
                }
                if (damageSource.is(DamageTypes.IN_WALL)) {
                    ci.setReturnValue(false);
                }
            }
        }

        LivingEntity entity = ((LivingEntity)(Object) this);
        if (entity.level().isClientSide){
            ci.setReturnValue(false);
            return;
        }
        if (((TimeStop)entity.level()).CanTimeStopEntity(entity) && !(damageSource.is(DamageTypes.GENERIC_KILL))){
            if (this.roundabout$TSHurtTime <= 0 || damageSource.is(DamageTypeTags.BYPASSES_COOLDOWN)) {
                float dmg = roundabout$getStoredDamage();
                float max = roundaboutGetMaxStoredDamage();

                if (((LivingEntity)(Object) this).isInvulnerableTo(damageSource)) {
                    ci.setReturnValue(false);
                } else if (((LivingEntity)(Object) this).isDeadOrDying()) {
                    ci.setReturnValue(false);
                } else if (damageSource.is(DamageTypeTags.IS_FIRE) && ((LivingEntity)(Object) this).hasEffect(MobEffects.FIRE_RESISTANCE)) {
                    ci.setReturnValue(false);
                }
                $$1 = getDamageAfterArmorAbsorb(damageSource, $$1);
                $$1 = getDamageAfterMagicAbsorb(damageSource, $$1);

                if (roundaboutTSHurtSound < 1){
                    roundaboutTSHurtSound = 1;
                } if (roundaboutTSHurtSound < 2 && (damageSource.is(ModDamageTypes.STAND) || damageSource.is(ModDamageTypes.PENETRATING_STAND)
                        || damageSource.is(ModDamageTypes.STAND_RUSH))){
                    roundaboutTSHurtSound = 2;
                }
                if (MainUtil.isStandDamage(damageSource) && damageSource.getEntity() instanceof LivingEntity LE && ((StandUser)LE).roundabout$getStandPowers().fullTSChargeBonus()){
                    if (!this.level().isClientSide()){
                        ((ServerLevel) this.level()).sendParticles(new DustParticleOptions(new Vector3f(0.74F,0.73F,0.98F), 1f), this.getX(), this.getY()+this.getEyeHeight(), this.getZ(),
                                1, 0.3, 0.3, 0.3, 0.3);
                    }
                } else {
                    $$1 *= (((float)(ClientNetworking.getAppropriateConfig().timeStopSettings.reducedTSDamageDealt))*0.01F);
                }
                if ((dmg + $$1) > max) {
                    roundabout$setStoredDamage(max);
                } else {
                    roundabout$setStoredDamage((dmg + $$1));
                }
                if (damageSource != null && damageSource.getEntity() != null) {
                    if (damageSource.getEntity() instanceof LivingEntity){
                        ((StandUser)damageSource.getEntity()).roundabout$getStandPowers().hasActedInTS = true;
                    }
                    roundaboutSetStoredAttacker(damageSource.getEntity());
                } else {
                    roundaboutSetStoredAttacker(null);
                }
                this.roundabout$TSHurtTime = 7;
                Entity $$8 = damageSource.getEntity();
                if ($$8 != null && !damageSource.is(DamageTypeTags.IS_EXPLOSION)) {
                    double $$13 = $$8.getX() - entity.getX();
                    double $$14;
                    for ($$14 = $$8.getZ() - entity.getZ(); $$13 * $$13 + $$14 * $$14 < 1.0E-4; $$14 = (Math.random() - Math.random()) * 0.01) {
                        $$13 = (Math.random() - Math.random()) * 0.01;
                    }
                    entity.knockback(0.4F, $$13, $$14);
                }
                ci.setReturnValue(true);

                return;
            }
            ci.setReturnValue(false);
            return;
        } else {

            boolean dothis = false;
            if ((float)this.invulnerableTime > 10.0F && !damageSource.is(DamageTypeTags.BYPASSES_COOLDOWN)) {
                if (!($$1 <= this.lastHurt)) {
                    dothis = true;
                }
            } else {
                dothis = true;
            }
            if (dothis) {
                LivingEntity living = ((LivingEntity) (Object) this);
                if (((StandUser) living).roundabout$getStandPowers().interceptDamageEvent(damageSource, $$1)) {
                    ci.setReturnValue(false);
                    return;
                }
            }


            /*This extra check ensures that extra damage will not be dealt if a projectile ticks before the TS damage catch-up*/
            if (roundabout$getStoredDamage() > 0 && !damageSource.is(ModDamageTypes.TIME)) {
                ci.setReturnValue(false);
                return;
            } if (damageSource.is(ModDamageTypes.TIME)){
                roundabout$postTSHurtTime = ClientNetworking.getAppropriateConfig().timeStopSettings.postTSiframes;
            } else {
                /*Knife and match code*/
                if (roundabout$postTSHurtTime > 0 || roundabout$extraIFrames > 0) {
                    ci.setReturnValue(false);
                    return;
                } else {
                    if (damageSource.is(ModDamageTypes.KNIFE) || damageSource.is(ModDamageTypes.MATCH)) {
                        if (damageSource.is(ModDamageTypes.KNIFE)){
                            roundabout$gasolineIFRAMES = 10;
                        }
                        if (damageSource.is(ModDamageTypes.GASOLINE_EXPLOSION) && roundabout$knifeIFrameTicks <= 0){
                            roundabout$gasolineIFRAMES = 10;
                        }
                        int knifeCap = ClientNetworking.getAppropriateConfig().itemSettings.maxKnivesInOneHit;
                        if (roundabout$stackedKnivesAndMatches < knifeCap) {
                            if (roundabout$stackedKnivesAndMatches <= 0) {
                                roundabout$knifeIFrameTicks = 9;
                                roundabout$knifeDespawnTicks = 300;
                            }
                            roundabout$stackedKnivesAndMatches++;
                            if (roundabout$stackedKnivesAndMatches >= knifeCap) {
                                roundabout$extraIFrames = 8;
                            }
                            if (damageSource.is(ModDamageTypes.KNIFE) && entity instanceof Player) {
                                ((IPlayerEntity) entity).roundabout$addKnife();
                            }
                        } else {
                            roundabout$gasolineIFRAMES = 10;
                            ci.setReturnValue(false);
                            return;
                        }
                    } else if (roundabout$knifeIFrameTicks > 0 && MainUtil.isStandDamage(damageSource)){
                        ci.setReturnValue(false);
                        return;
                    }
                }
            }
        }

        if (!((TimeStop)entity.level()).getTimeStoppingEntities().isEmpty()
                && ((TimeStop)entity.level()).getTimeStoppingEntities().contains(entity) &&
                (damageSource.is(DamageTypes.ON_FIRE) || damageSource.is(DamageTypes.IN_FIRE))){
            ci.setReturnValue(false);
        }
    }


    @Inject(method = "tick", at = @At(value = "TAIL"), cancellable = true)
    protected void roundabout$tickTail(CallbackInfo ci) {
        ((IEntityAndData)this).roundabout$tickQVec();
        roundabout$tickString();
    }

    @Shadow
    protected int decreaseAirSupply(int $$0) {
        return 0;
    }

    @Shadow protected abstract float getEyeHeight(Pose $$0, EntityDimensions $$1);

    @Inject(method = "baseTick", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$BreathingCancel(CallbackInfo ci){
        if (roundabout$isDrown) {
            this.hurt(this.damageSources().drown(), 2.0f);
        }
        boolean cannotBreathInTs = ClientNetworking.getAppropriateConfig().timeStopSettings.preventsBreathing;
        if (cannotBreathInTs) {
            if (!((TimeStop) this.level()).getTimeStoppingEntities().isEmpty()
                    && ((TimeStop) this.level()).getTimeStoppingEntities().contains(((LivingEntity) (Object) this))) {
                ((IEntityAndData) this).roundabout$setRoundaboutJamBreath(true);
            }
        }
    }
    @Inject(method = "baseTick", at = @At(value = "TAIL"), cancellable = true)
    protected void roundabout$BreathingCancel2(CallbackInfo ci){
        boolean cannotBreathInTs = ClientNetworking.getAppropriateConfig().timeStopSettings.preventsBreathing;
        if (cannotBreathInTs) {
            if (((IEntityAndData) this).roundabout$getRoundaboutJamBreath()) {
                ((IEntityAndData) this).roundabout$setRoundaboutJamBreath(false);
            }
        }
        if (!this.level().isClientSide()) {
            if (this.isInWaterRainOrBubble() || (((LivingEntity)(Object)this) instanceof Player PE && PE.isCreative())
            || (roundabout$fireStarter != null && ((StandUser)roundabout$fireStarter).roundabout$getStandPowers() instanceof
                    PowersMagiciansRed PM && (PM.snapNumber != roundabout$fireStarterID))){
                if (roundabout$remainingFireTicks >= 0) {
                    roundabout$remainingFireTicks = -1;
                    roundabout$setOnStandFire(StandFireType.FIRELESS.id);
                }
            }

            if (!(!((TimeStop)this.level()).getTimeStoppingEntities().isEmpty()
                    && ((TimeStop)this.level()).getTimeStoppingEntities().contains(((LivingEntity) (Object)this)))){
                if (roundabout$remainingFireTicks > 0) {

                    //Roundabout.LOGGER.info(""+roundabout$remainingFireTicks);
                    if (roundabout$remainingFireTicks % 20 == 0 && !this.isInLava()) {
                        float fireDamage = 1;
                        if (this.roundabout$getStandPowers().getReducedDamage((LivingEntity) (Object) this)) {
                            fireDamage = (float) (fireDamage * (ClientNetworking.getAppropriateConfig().
                                    magiciansRedSettings.standFireOnPlayersMult * 0.01))*0.8F;
                        } else {
                            fireDamage = (float) (fireDamage * (ClientNetworking.getAppropriateConfig().
                                    magiciansRedSettings.standFireOnMobsMult * 0.01))*0.8F;
                        }
                        this.hurt(ModDamageTypes.of(this.level(), ModDamageTypes.STAND_FIRE), fireDamage);
                    }

                    roundabout$setRemainingStandFireTicks(roundabout$remainingFireTicks - 1);

                }
                if (roundabout$remainingFireTicks <= 0) {
                    if (roundabout$getOnStandFire() > 0) {
                        roundabout$setOnStandFire(StandFireType.FIRELESS.id);
                    }
                    if (roundabout$remainingFireTicks == 0) {
                        roundabout$remainingFireTicks = -1;
                    }
                }
            }
        }
    }

    @Unique
    public int roundabout$encasedTimer = 0;

    /**Stone Heart and Potion Ticks*/
    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;tickEffects()V", shift = At.Shift.BEFORE))
    protected void roundabout$baseTick(CallbackInfo ci) {

        byte curse = this.roundabout$getLocacacaCurse();
        if (curse > -1) {
            if (curse == LocacacaCurseIndex.HEART) {
                if (this.tickCount % 20 == 0) {
                    this.hurt(ModDamageTypes.of(this.level(), ModDamageTypes.HEART), 1.0F);
                }
            }
        }
        /**hey ya fade ticks*/
        boolean active = roundabout$getActive();
        if (roundabout$getStandPowers() instanceof PowersHeyYa && active){
            roundabout$setHeyYaVanishTicks(roundabout$getHeyYaVanishTicks()+1);
        } else {
            roundabout$setHeyYaVanishTicks(roundabout$getHeyYaVanishTicks()-1);
        }

        if (roundabout$getStandPowers() instanceof PowersMandom && active){
            roundabout$setMandomVanishTicks(roundabout$getMandomVanishTicks()+1);
        } else {
            roundabout$setMandomVanishTicks(roundabout$getMandomVanishTicks()-1);
        }
        /** RattShoulder fade ticks*/
        if (roundabout$getActive() && roundabout$getStandPowers() instanceof PowersRatt &&
                !((PowersRatt)roundabout$getStandPowers()).isPlaced()
        ){
            roundabout$setRattShoulderVanishTicks(roundabout$getRattShoulderVanishTicks()+1);
        } else {
            roundabout$setRattShoulderVanishTicks(0);
        }


        /**Soft and Wet Bubble Encase launch*/
        if (roundabout$isLaunchBubbleEncased() && !this.level().isClientSide()){
            if (roundabout$encasedTimer > 0){
                roundabout$encasedTimer--;
                Vec3 storedVec = roundabout$getStoredVelocity();
                MainUtil.takeLiteralUnresistableKnockbackWithY(((LivingEntity)(Object)this),storedVec.x,storedVec.y,storedVec.z);
            }
            if (roundabout$encasedTimer <= 0) {
                roundabout$setBubbleEncased((byte) 0);
                this.level().playSound(null, this.blockPosition(), ModSounds.BUBBLE_POP_EVENT,
                        SoundSource.PLAYERS, 2F, (float) (0.98 + (Math.random() * 0.04)));
                ((ServerLevel) this.level()).sendParticles(ModParticles.BUBBLE_POP,
                        this.getX(), this.getY() + this.getBbHeight() * 0.5, this.getZ(),
                        5, 0.25, 0.25, 0.25, 0.025);
                roundabout$setBubbleEncased((byte)0);
            }
        }

        if (this.hasEffect(ModEffects.MELTING)) {
            if (!this.level().isClientSide() && this.isAlive()) {
                // put code here?
            }
        }

        if (this.hasEffect(ModEffects.STAND_VIRUS)) {
            if (this.tickCount % 20 == 0) {
                if (!this.level().isClientSide() && this.isAlive()){
                    this.hurt(ModDamageTypes.of(this.level(), ModDamageTypes.STAND_VIRUS), this.getEffect(ModEffects.STAND_VIRUS).getAmplifier()+1);
                }
            }
            if (this.roundabout$RejectionStandPowers != null){
                this.roundabout$RejectionStandPowers.tickStandRejection(this.getEffect(ModEffects.STAND_VIRUS));
            }
        } else {
            if (!this.level().isClientSide()){
                if (this.roundabout$RejectionStandPowers != null){
                    roundabout$RejectionStandPowers = null;
                    roundabout$RejectionStandDisc = null;
                }
            }
        }
    }
    @Override
    @Unique
    public Vec3 roundabout$frictionSave() {
        return roundabout$frictionSave;
    }
    @Override
    @Unique
    public boolean roundabout$skipFriction() {
        return roundabout$skipFriction;
    }
    @Override
    @Unique
    public void roundabout$clearFire() {
        this.setRemainingFireTicks(0);
    }
    @Shadow
    public boolean canBreatheUnderwater() {
        return false;
    }

    /**If you have a chest turned to stone, decreases breath faster*/
    @Inject(method = "decreaseAirSupply", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$decreaseAirSupply(int $$0, CallbackInfoReturnable<Integer> cir) {
        boolean $$0P = ((LivingEntity)(Object)this) instanceof Player;
            int air = roundabout$getStandPowers().getAirAmount();
            if (air > 0 && roundabout$getActive()) {
                if (this.isEyeInFluid(FluidTags.WATER)
                        && !this.level().getBlockState(BlockPos.containing(this.getX(), this.getEyeY(), this.getZ())).is(Blocks.BUBBLE_COLUMN)) {
                    boolean $$3 = !this.canBreatheUnderwater() && !MobEffectUtil.hasWaterBreathing((LivingEntity) (Object)this) &&
                            (!$$0P || !((Player)((LivingEntity)(Object)this)).getAbilities().invulnerable);
                    if ($$3) {

                        int $$1 = EnchantmentHelper.getRespiration(((LivingEntity) (Object) this));
                        air = $$1 > 0 && this.random.nextInt($$1 + 1) > 0 ? air : air - 1;
                        if (air <= 0) {
                            air = 0;
                        }
                        roundabout$getStandPowers().setAirAmount(air);
                        cir.setReturnValue($$0);
                        return;
                    }
                }
            }


        if (roundabout$isBubbleEncased()){
            if (this.tickCount % 2 == 0){
                cir.setReturnValue($$0);
                return;
            }
        }
            byte curse = this.roundabout$getLocacacaCurse();
            if (curse > -1) {
                if (curse == LocacacaCurseIndex.CHEST) {
                    int $$1 = EnchantmentHelper.getRespiration(((LivingEntity) (Object) this));
                    $$1 = $$1 > 0 && this.random.nextInt($$1 + 1) > 0 ? $$0 : $$0 - 4;
                    if ($$1 < -20) {
                        $$1 = -20;
                    }
                    cir.setReturnValue($$1);
                    return;
                }
            }
    }
    @Inject(method = "increaseAirSupply", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$increaseAirSupply(int $$0, CallbackInfoReturnable<Integer> cir) {
        /**
        int air = roundabout$getStandPowers().getAirAmount();
        if (air > -1 && air < roundabout$getStandPowers().getMaxAirAmount()) {
            cir.setReturnValue($$0);
        }
         */
    }

    @Unique
    @Override
    public int roundabout$increaseAirSupply(int $$0){
        return this.increaseAirSupply($$0);
    }

    /**When time is stopped by the user, incurs an action penalty if food is eaten*/
    @Inject(method = "completeUsingItem", at = @At(value = "TAIL"), cancellable = true)
    protected void roundabout$completeUsingItem(CallbackInfo ci){
        if (!level().isClientSide) {
            LivingEntity entity = ((LivingEntity)(Object) this);
            if (((TimeStop) entity.level()).isTimeStoppingEntity(entity)) {
                this.roundabout$getStandPowers().hasActedInTS = true;
            }
        }
    }

    /**While using item, leave idle state*/
    @Inject(method = "updateUsingItem", at = @At(value = "HEAD"))
    protected void roundabout$UpdateUsingItem(ItemStack $$0, CallbackInfo ci){
        this.roundabout$IdleTime = -1;
    }


    @Unique
    public void roundabout$UniversalTick(){
        if (this.roundabout$TSHurtTime > 0){this.roundabout$TSHurtTime--;}

        if (roundabout$getStandPowers().summonCD > 0) {
            roundabout$getStandPowers().summonCD--;
        }
    }

    @Unique
    boolean roundabout$cancelsprintJump(){
        byte curse = this.roundabout$getLocacacaCurse();
        if (curse > -1 && (curse == LocacacaCurseIndex.RIGHT_LEG || curse == LocacacaCurseIndex.LEFT_LEG))
            return true;

        int zapped = roundabout$getZappedToID();
        if (zapped > -1){
            Entity ent = level().getEntity(zapped);
            if (ent != null){
                float dist1 = distanceTo(ent);
                float dist2 = (float) position().add(getDeltaMovement()).distanceTo(ent.position());
                if (dist1 < dist2){
                    return true;
                }
            }
        }
        return false;
    }

    /**Use this code to eliminate the sprint jump during certain actions*/
    @Inject(method = "jumpFromGround", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$jumpFromGround(CallbackInfo ci) {
        if (this.roundabout$getStandPowers().cancelSprintJump() || roundabout$cancelsprintJump()) {
            Vec3 $$0 = this.getDeltaMovement();
            this.setDeltaMovement($$0.x, (double) this.getJumpPower(), $$0.z);
            this.hasImpulse = true;
            ci.cancel();
            return;
        }

        if (ClientNetworking.getAppropriateConfig().softAndWetSettings.frictionStopsJumping) {
            if (MainUtil.canHaveFrictionTaken(((LivingEntity) (Object) this))) {
                if (((ILevelAccess) this.level()).roundabout$isFrictionPlundered(this.blockPosition()) ||
                        ((ILevelAccess) this.level()).roundabout$isFrictionPlunderedEntity(this)
                ) {
                    ci.cancel();
                    return;
                }
            }
        }
    }

    @Inject(method = "getSpeed", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$getSpeed(CallbackInfoReturnable<Float> cir) {
        float basis = this.speed;
        if (!roundabout$getStandDisc().isEmpty()){
            if (roundabout$isClashing()) {
                cir.setReturnValue((float) 0);
                return;
            }
            basis = roundabout$getStandPowers().inputSpeedModifiers(basis);
        }

        basis = roundabout$mutualGetSpeed(basis);
        if (!((StandUser) this).roundabout$getStandDisc().isEmpty() &&

                (((LivingEntity)(Object)this) instanceof AbstractVillager AV &&
                AV.getTarget() != null && !(((IMob) this).roundabout$getFightOrFlight()))

        ){
            basis *= 0.5F;
        }

        if (basis != this.speed){
            cir.setReturnValue(basis);
        }
    }


    @Shadow
    protected float getJumpPower() {
        return 0;
    }

    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot var1);

    @Shadow public abstract void die(DamageSource $$0);

    @Shadow public abstract CombatTracker getCombatTracker();

    @Shadow public abstract int getArmorValue();

    @Shadow public abstract double getAttributeValue(Attribute $$0);

    @Shadow @Nullable public abstract AttributeInstance getAttribute(Attribute attribute);

    @Shadow public abstract void kill();

    @Shadow public abstract void setHealth(float $$0);

    @Unique private boolean roundabout$isPRunning = false;

    @Override
    public void roundabout$setParallelRunning(boolean value) {
        this.roundabout$isPRunning = value;
    }

    @Override
    public boolean roundabout$isParallelRunning() {
        if (roundabout$getStandPowers() instanceof PowersD4C)
        {
            return this.roundabout$isPRunning;
        }
        else
        {
            roundabout$isPRunning = false;
            return false;
        }
    }


    public double previousYpos = 0.0;
    public float MoldLevel = 0.0f;
    public int jumpImmunityTicks = 0;

    @Override
    public void DoMoldTick() {
        MoldLevel = MoldLevel + 1f;

            if (MoldLevel % 3 == 0) {

                if(MoldLevel > 60){
                    MoldLevel = 0;
                    if (true){
                        this.hurt(ModDamageTypes.of(this.level(), ModDamageTypes.DISINTEGRATION),326);
                    }
                }
                else {
                    this.hurt(ModDamageTypes.of(this.level(), ModDamageTypes.DISINTEGRATION), (MoldLevel/3f) + 2.0f);
                }

            }


    }

    @Override
    public void MoldFieldExit() {
        MoldLevel = 0;
        this.tick();

    }
    @Inject(method = "dropCustomDeathLoot", at = @At(value = "TAIL"), cancellable = true)
    public void DropHeads(DamageSource $$0, int $$1, boolean $$2,CallbackInfo info){
        Entity cause = $$0.getEntity();
        DamageType type = $$0.type();
        DamageSource uh = ModDamageTypes.of(this.level(), ModDamageTypes.DISINTEGRATION);
        LivingEntity me = (LivingEntity) (Object) this;
        if(type == uh.type() && Roundabout.RANDOM.nextDouble()>0.0){
            if((LivingEntity) (Object) this instanceof Zombie){
                spawnAtLocation(new ItemStack(Items.ZOMBIE_HEAD));
            } else if (me instanceof Creeper) {
                spawnAtLocation(new ItemStack(Items.CREEPER_HEAD));
            } else if (me instanceof Skeleton) {
                spawnAtLocation(new ItemStack(Items.SKELETON_SKULL));
            } else if (me instanceof WitherSkeleton) {
                spawnAtLocation(new ItemStack(Items.WITHER_SKELETON_SKULL));



            } else if (me instanceof ServerPlayer){
                ItemStack skull = new ItemStack(Items.PLAYER_HEAD);
                skull.setTag(new CompoundTag());
                //PlayerHeadItem
                CompoundTag tag = skull.getTag();
                tag.putString("SkullOwner",this.getName().getString());
                skull.setTag(tag);
                spawnAtLocation(skull);
            } else if (me instanceof Piglin){
                spawnAtLocation(new ItemStack(Items.PIGLIN_HEAD));
            }

        }

    }



    @Inject(method = "travel", at = @At(value = "TAIL"))
    public void   MoldDetection(Vec3 movement,CallbackInfo info) {

        if(((IPermaCasting)this.level()).roundabout$inPermaCastRange(this.getOnPos(), PermanentZoneCastInstance.MOLD_FIELD)) {
            LivingEntity glumbo = ((IPermaCasting)this.level()).roundabout$inPermaCastRangeEntity(this.getOnPos(),PermanentZoneCastInstance.MOLD_FIELD);
            boolean isUser = this.equals(glumbo);
            boolean down = previousYpos > this.getY();
            boolean isStand = (((LivingEntity)(Object) this) instanceof StandEntity);
            if (!roundabout$getStandPowers().isStoppingTime() &&!this.roundabout$isBubbleEncased() && !isUser && !isStand && down && (glumbo.getY() > this.getY()) && !isUser && jumpImmunityTicks < 1){
                for (int i = 0; i < 3; i = i + 1) {

                    double width = this.getBbWidth();
                    double height = this.getBbHeight();
                    double randomX = Roundabout.RANDOM.nextDouble(0 - (width / 2), width / 2);
                    double randomY = Roundabout.RANDOM.nextDouble(0 - (height / 2), height / 2);
                    double randomZ = Roundabout.RANDOM.nextDouble(0 - (width / 2), width / 2);
                    (this.level()).addParticle(ModParticles.MOLD,
                            this.getX() + randomX, (this.getY() + height / 2) + randomY, this.getZ() + randomZ,
                            this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z
                    );

                }
                DoMoldTick();
            }


        }
        if (previousYpos < this.getY()){
            jumpImmunityTicks = 6;
        }
        else{
            jumpImmunityTicks = jumpImmunityTicks -1;
        }
        previousYpos = this.getY();


    }
}
