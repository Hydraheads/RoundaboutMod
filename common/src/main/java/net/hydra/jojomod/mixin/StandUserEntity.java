package net.hydra.jojomod.mixin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.*;
import net.hydra.jojomod.block.*;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.pathfinding.AnubisPossessorEntity;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.RattEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.*;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.*;
import net.hydra.jojomod.powers.power_types.PunchingGeneralPowers;
import net.hydra.jojomod.stand.powers.*;
import net.hydra.jojomod.stand.powers.PowersJustice;
import net.hydra.jojomod.stand.powers.PowersMagiciansRed;
import net.hydra.jojomod.item.*;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
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

import java.util.*;
import java.util.function.Predicate;

@Mixin(value = LivingEntity.class, priority = 103)
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
    private boolean roundabout$leapIntentionally = false;
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
    private static final EntityDataAccessor<Integer> ROUNDABOUT$POSSESSOR = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Boolean> ROUNDABOUT$ONLY_BLEEDING = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.BOOLEAN);

    @Unique
    private static final EntityDataAccessor<ItemStack> ROUNDABOUT$STAND_DISC = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.ITEM_STACK);

    @Unique
    private static final EntityDataAccessor<Boolean> ROUNDABOUT$COMBAT_MODE = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Integer> ROUNDABOUT$HEAT = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.INT);


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
                Vec3 $$1 = new Vec3(this.getEyePosition().x, this.getEyePosition().y, this.getEyePosition().z);
                Vec3 $$2 = new Vec3($$0.getEyePosition().x, $$0.getEyePosition().y, $$0.getEyePosition().z);
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

    @Inject(method = "knockback",at = @At(value = "HEAD"))
    public void roundabout$transferKnockback(double $$0, double $$1, double $$2, CallbackInfo ci) {
        if (this.roundabout$isPossessed()) {
            LivingEntity poss = (LivingEntity) this.roundabout$getPossessor();
            if (poss != null) {
                poss.knockback($$0,$$1,$$2);
            }


        }
    }

    @Override
    @Unique
    public boolean rdbt$tickEffectsBleedEdition(boolean grav){

        Vec3 vec3d;
        Vec3 vec3d2;
        if (grav){
            Direction dir = ((IGravityEntity)this).roundabout$getGravityDirection();
            vec3d = this.position().subtract(RotationUtil.vecPlayerToWorld(this.position().subtract(this.getRandomX(0.5),
                    this.getRandomY()+this.getBbHeight(),
                    this.getRandomZ(0.5)), dir));
            vec3d2 = this.position().subtract(RotationUtil.vecPlayerToWorld(this.position().subtract(this.getRandomX(0.5),
                    this.getRandomY(),
                    this.getRandomZ(0.5)), dir));
        } else {
            vec3d = new Vec3(this.getRandomX(0.5),
                    this.getRandomY()+this.getBbHeight(),
                    this.getRandomZ(0.5));
            vec3d2 = new Vec3(this.getRandomX(0.5),
                    this.getRandomY(),
                    this.getRandomZ(0.5));
        }

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
                                vec3d.x,
                                vec3d.y,
                                vec3d.z,
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
        if (this.getEffect(ModEffects.MELTING) != null) {
            int stacks = this.getEffect(ModEffects.MELTING).getAmplifier();
            int bloodticks = 8;
            if (stacks == 3) {
                bloodticks = 6;
            } else if (stacks > 5) {
                bloodticks = 4;
            }
            if (this.tickCount % bloodticks == 0) {
                this.level()
                        .addParticle(
                                ModParticles.MELTING,
                                vec3d2.x,
                                vec3d2.y,
                                vec3d2.z,
                                0,
                                0,
                                0
                        );
            }
        }
        if (this.roundabout$getBleedLevel() > -1) {
            if (((IPermaCasting)this.level()).roundabout$inPermaCastFogRange(this)){
                this.level()
                        .addParticle(
                                ModParticles.FOG_CHAIN,
                                vec3d.x,
                                vec3d.y,
                                vec3d.z,
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
                                vec3d2.x,
                                vec3d2.y,
                                vec3d2.z,
                                0,
                                0,
                                0
                        );
            }
        }
        if (this.roundabout$getOnlyBleeding() || this.getEffect(ModEffects.MELTING) != null) {
            return true;
        }
        return false;
    }

    /**
     * Tick thru effects for bleed to not show potion swirls
     */
    @Inject(method = "tickEffects", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/syncher/SynchedEntityData;get(Lnet/minecraft/network/syncher/EntityDataAccessor;)Ljava/lang/Object;",
            shift = At.Shift.AFTER, ordinal = 0), cancellable = true)
    public void roundabout$tickEffects(CallbackInfo ci) {
        if (rdbt$tickEffectsBleedEdition(false)){
            ci.cancel();
            ((StandUser)rdbt$this()).rdbt$setRemoveLoveSafety(true);
        }
    }


    // Vampire swing speed in vampire power, how fast the arms move
    @Inject(method = "getCurrentSwingDuration()I", at = @At(value = "HEAD"), cancellable = true)
    public void rdbt$getCurrentSwingDurationBrawl(CallbackInfoReturnable<Integer> cir) {
        if (PowerTypes.isBrawling(rdbt$this())){
            int amt = 0;
            if (MobEffectUtil.hasDigSpeed(rdbt$this())) {
                amt = 6 - (1 + MobEffectUtil.getDigSpeedAmplification(rdbt$this()));
            } else {
                amt = this.hasEffect(MobEffects.DIG_SLOWDOWN) ? 6 + (1 + this.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier()) * 2 : 6;
            }
            //While barraging their arms move faster
            if (rdbt$this() instanceof Player pl && ((IPlayerEntity)pl).roundabout$GetPos2() == PlayerPosIndex.BARRAGE){
                cir.setReturnValue((int) (amt*0.8));
                return;
            }
            cir.setReturnValue((int) (amt*1.4));
        }
    }

    @Inject(method = "swing(Lnet/minecraft/world/InteractionHand;Z)V", at = @At(value = "HEAD"), cancellable = true)
    public void rdbt$swingBrawl (InteractionHand hand, boolean $$1,CallbackInfo ci) {
        if (PowerTypes.isBrawling(rdbt$this()) && level().isClientSide()){
            ci.cancel();
            if (!this.swinging || this.swingTime >= this.getCurrentSwingDuration() / 2 || this.swingTime < 0) {
                if (swingingArm != null) {
                    if (swingingArm == InteractionHand.MAIN_HAND) {
                        hand = InteractionHand.OFF_HAND;
                    } else {
                        hand = InteractionHand.MAIN_HAND;
                    }
                }



                this.swingTime = -1;
                this.swinging = true;
                this.swingingArm = hand;
                if (this.level() instanceof ServerLevel) {
                    ClientboundAnimatePacket $$2 = new ClientboundAnimatePacket(this, hand == InteractionHand.MAIN_HAND ? 0 : 3);
                    ServerChunkCache $$3 = ((ServerLevel)this.level()).getChunkSource();
                    if ($$1) {
                        $$3.broadcastAndSend(this, $$2);
                    } else {
                        $$3.broadcast(this, $$2);
                    }
                }
            }
        }
    }

    @Unique
    @Override
    public void rdbt$setRemoveLoveSafety(boolean yup){
        roundabout$safeToRemoveLove = yup;
    }

    @Inject(method = "tickEffects", at = @At(value = "HEAD"))
    public void roundabout$tickEffectsPre(CallbackInfo ci) {
        if (!this.level().isClientSide) {
            rdbt$setRemoveLoveSafety(false);
        }
    }
    @Inject(method = "tickEffects", at = @At(value = "TAIL"))
    public void roundabout$tickEffectsPost(CallbackInfo ci) {
        if (!this.level().isClientSide) {
            rdbt$setRemoveLoveSafety(true);
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
        if (isAlive())
        {
            if (rdbt$hideDeath){
                rdbt$hideDeath = false;
            }
        }
        if (level().isClientSide()){
            ClientUtil.tickHeartbeat(this);
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
        if (roundabout$getStandPowers().hasShootingModeVisually() && PowerTypes.hasStandActivelyEquipped(rdbt$this())){
            return true;
        }
        return false;
    }
    @Unique
    @Override
    public SoundEvent roundabout$getHurtSound(DamageSource sauce){
        return getHurtSound(sauce);
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




    @Override
    public PathfinderMob roundabout$getPossessor() {
        if (this.getEntityData().get(ROUNDABOUT$POSSESSOR) != -1) {
            return (PathfinderMob) this.level().getEntity(this.entityData.get(ROUNDABOUT$POSSESSOR));
        }
        return null;
    }
    @Override
    public void roundabout$setPossessor(PathfinderMob e) {
        this.getEntityData().set(ROUNDABOUT$POSSESSOR,e != null ? e.getId() : -1);
    }
    @Unique
    @Override
    public boolean roundabout$isPossessed() {
        return this.level().getEntity(this.getEntityData().get(ROUNDABOUT$POSSESSOR)) != null;
    }
    @Unique
    @Override
    public void roundabout$setHeat(int e) {
        this.getEntityData().set(ROUNDABOUT$HEAT,e);
    }
    @Unique
    @Override
    public int roundabout$getHeat() {
        return this.getEntityData().get(ROUNDABOUT$HEAT);
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
            if (!onGround()){
                roundabout$setIdleTime(0);
            }

            HeatUtil.tickHeat(rdbt$this());
            if (roundabout$getZappedToID() > -1) {
                roundabout$zappedTicks++;
                if (roundabout$zappedTicks >= ClientNetworking.getAppropriateConfig().survivorSettings.durationOfAggressiveAngerSetting) {
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

            if (this.roundabout$getPossessor() instanceof AnubisPossessorEntity APE) {
                if (rdbt$this() instanceof Player P && P.isCreative()) {
                    APE.discard();
                }

                if (this.roundabout$getActive()) {
                    this.roundabout$setActive(false);
                }

                if (rdbt$this() instanceof Player P) {

                    if (APE.getLifeSpan() == 1) {
                        if (this.roundabout$getPossessor() != null) {
                            this.roundabout$getPossessor().discard();
                            this.roundabout$setPossessor(null);
                        }
                     //   P.getCooldowns().addCooldown(ModItems.ANUBIS_ITEM,10);
                        P.displayClientMessage(Component.translatable("item.roundabout.anubis_item.message1").withStyle(ChatFormatting.RED), true);
                    }
                }
            }



            //**Stone Mask Clearing*/
            if (isInWater())
                MainUtil.clearStoneMask(rdbt$this());

            if (PowerTypes.hasStandActive(rdbt$this()) &&this.roundabout$getStandPowers().canSummonStand()&&this.roundabout$getStandPowers().canSummonStandAsEntity()  && (this.roundabout$getStand() == null ||
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
        if (rdbt$this() instanceof Player PL){
            ((IFatePlayer)PL).rdbt$getFatePowers().tickPower();
            ((IPowersPlayer)PL).rdbt$getPowers().tickPower();
        }
        this.rdbt$tickCooldowns();
        this.roundabout$tickGuard();
        this.roundabout$tickDaze();
        if (this.roundabout$leapTicks > -1) {
            if (this.onGround() && roundabout$leapTicks < (MainUtil.maxLeapTicks() - 5)) {
                roundabout$leapTicks = -1;
            }
            roundabout$cancelConsumableItem((LivingEntity) (Object) this);
            roundabout$leapTicks--;
            if (!this.level().isClientSide && roundabout$leapIntentionally) {
                Vector3f color = new Vector3f(1f, 0.65f, 0);
                if (this.roundabout$getStandPowers() instanceof PowersAnubis) {
                    color = new Vector3f(171F/255F,141F/255F,230F/255F   );
                }
                ((ServerLevel) this.level()).sendParticles(new DustParticleOptions(color, 1f), this.getX(), this.getY(), this.getZ(),
                        1, 0, 0, 0, 0.1);
            }
        }
        if (roundabout$leapTicks <= -1){
            roundabout$leapIntentionally = false;
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

    @Unique
    private static final EntityDataAccessor<Integer> ROUNDABOUT$METALLICA_INVISIBILITY = SynchedEntityData.defineId(LivingEntity.class,
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
    @Override
    @Unique
    public void roundabout$setLeapIntentionally(boolean intentional){
        this.roundabout$leapIntentionally = intentional;
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

        if (roundabout$isDazed())
            return TOT;


        if (roundabout$getBubbleEncased() == 1){
            TOT+=4;
            }
        TOT+=FateTypes.getJumpHeightAddon((LivingEntity) (Object)this);
        StandUser SU = (StandUser) rdbt$this();
        if (SU.roundabout$getStandPowers() != null) {
            TOT += SU.roundabout$getStandPowers().getJumpHeightAddon();
            if (SU.roundabout$getStandPowers().cancelJump())
                return 0;
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
        if (PowerTypes.isBrawling(rdbt$this())){
            return true;
        }
        if (PowerTypes.hasStandActive(rdbt$this()) && roundabout$getStandPowers().hasPassiveCombatMode()){
            return true;
        }
        if (getEntityData().hasItem(ROUNDABOUT$COMBAT_MODE)) {
            return this.getEntityData().get(ROUNDABOUT$COMBAT_MODE);
        } else {
            return false;
        }
    }
    @Unique
    @Override
    public boolean roundabout$getEffectiveCombatMode() {
        if (PowerTypes.isBrawling(rdbt$this())){
            return true;
        }
        if (PowerTypes.hasStandActive(rdbt$this()) && roundabout$getStandPowers().hasPassiveCombatMode()){
            return true;
        }
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
        if (getEntityData().hasItem(ROUNDABOUT$STAND_DISC)) {
            return this.getEntityData().get(ROUNDABOUT$STAND_DISC);
        } else {
            return ItemStack.EMPTY;
        }
    }
    @Unique
    @Override
    public void roundabout$setStandDisc(ItemStack stack) {
        if (!(this.level().isClientSide)) {
            this.getEntityData().set(ROUNDABOUT$STAND_DISC, stack);
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


    @Unique
    public boolean roundabout$hasInteractedWithDisc = false;
    @Unique
    @Override
    public boolean roundabout$getInteractedWithDisc(){
        return roundabout$hasInteractedWithDisc;
    };
    @Unique
    @Override
    public void roundabout$setInteractedWithDisc(boolean discInteract){
        roundabout$hasInteractedWithDisc = discInteract;
    };


    @Inject(method = "onSyncedDataUpdated", at = @At(value = "TAIL"), cancellable = true)
    public void roundabout$onSyncedDataUpdated(EntityDataAccessor<?> $$0, CallbackInfo ci){
        /**
        if ($$0.equals(ROUNDABOUT$STAND_DISC)){
            String updateString = this.entityData.get(ROUNDABOUT$STAND_DISC);
            if (!updateString.isEmpty()){
                if (ResourceLocation.isValidResourceLocation(updateString)) {
                    ResourceLocation rl = new ResourceLocation(updateString);
                    if (rl != null) {
                        Item tem = BuiltInRegistries.ITEM.get(rl);
                        if (tem != null) {
                            roundabout$standDisc = tem.getDefaultInstance();
                            if (this.roundabout$getStandPowers() != null && this.level().isClientSide()
                            && roundabout$getInteractedWithDisc()){
                                this.roundabout$getStandPowers().onStandSwitchInto();
                                roundabout$setInteractedWithDisc(false);
                            }
                        }
                    }
                }
            }
        }
         **/
    }

    /// does what getItemInHand does
    @Inject(method = "getMainHandItem",at = @At(value = "HEAD"),cancellable = true)
    public void roundabout$getMainHandItem(CallbackInfoReturnable<ItemStack> cir) {
        ItemStack ret = roundabout$XHandCancelItem(EquipmentSlot.MAINHAND);
        if (ret.equals(ItemStack.EMPTY)) {
            cir.setReturnValue(ret);
            cir.cancel();
        }
    }
    @Inject(method = "getOffhandItem",at = @At(value = "HEAD"),cancellable = true)
    public void roundabout$getOffHandItem(CallbackInfoReturnable<ItemStack> cir) {
        ItemStack ret = roundabout$XHandCancelItem(EquipmentSlot.OFFHAND);
        if (ret.equals(ItemStack.EMPTY)) {
            cir.setReturnValue(ret);
            cir.cancel();
        }
    }

    @Unique
    public ItemStack roundabout$XHandCancelItem(EquipmentSlot ES) {
        if (this.roundabout$isPossessed()) {return ItemStack.EMPTY;}
        if (roundabout$getEffectiveCombatMode()) {
            StandPowers SP = roundabout$getStandPowers();
            if (SP != null) {
                if (!SP.canCombatModeUse(getItemBySlot(ES))) {
                    return ItemStack.EMPTY;
                }
            }
        }
        return getItemBySlot(ES);
    }

    /**The items that shoot and brawl mode are allowed to use*/
    @Inject(method = "getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$getItemInHand(InteractionHand $$0, CallbackInfoReturnable<ItemStack> cir){
        if (this.roundabout$isPossessed()) {
            cir.setReturnValue(ItemStack.EMPTY);
            return;
        }
        if (roundabout$getEffectiveCombatMode()){
            ItemStack stack = ItemStack.EMPTY;
            if ($$0 == InteractionHand.MAIN_HAND) {
                stack = this.getItemBySlot(EquipmentSlot.MAINHAND);
            } else if ($$0 == InteractionHand.OFF_HAND) {
                stack = this.getItemBySlot(EquipmentSlot.OFFHAND);
            }
            StandPowers SP = this.roundabout$getStandPowers();
            if (SP != null) {
                if (SP.canCombatModeUse(stack)) {
                    cir.setReturnValue(stack);
                    return;
                }
            }
            cir.setReturnValue(ItemStack.EMPTY);
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
        compoundtag.putInt("heat",roundabout$getHeat());

        if (rdbt$fleshBudPlanted !=null){
            compoundtag.putUUID("fleshBud", rdbt$fleshBudPlanted);
        } else {
            if (compoundtag.contains("fleshBud")){
                compoundtag.remove("fleshBud");
            }
        }

        StandPowers powers = roundabout$getStandPowers();
        List<CooldownInstance> CDCopy = new ArrayList<>(rdbt$PowerCooldowns) {
        };
        if (!CDCopy.isEmpty()) {
            for (byte i = 0; i < CDCopy.size(); i++) {
                CooldownInstance ci = CDCopy.get(i);
                compoundtag.putInt("cooldown_" + i, ci.time);
                compoundtag.putInt("cooldown_" + i + "_max", ci.maxTime);
            }
        }


        $$0.put("roundabout",compoundtag);

        return $$0;
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
        roundabout$setHeat(compoundtag.getByte("heat"));
        if (compoundtag.contains("fleshBud")) {
            rdbt$fleshBudPlanted = compoundtag.getUUID("fleshBud");
        }

        StandPowers powers = roundabout$getStandPowers();
        List<CooldownInstance> CDCopy = new ArrayList<>(rdbt$PowerCooldowns) {
        };
        if (!CDCopy.isEmpty()) {
            for (byte i = 0; i < CDCopy.size(); i++) {
                CooldownInstance instance = CDCopy.get(i);
                if (compoundtag.contains("cooldown_" + i)) {
                    instance.time = compoundtag.getInt("cooldown_" + i);
                    instance.maxTime = compoundtag.getInt("cooldown_" + i + "_max");
                }
            }
        }
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
        if (PowerTypes.hasStandActivelyEquipped(rdbt$this())){
            return (float) (roundabout$getStandPowers().getMaxGuardPoints()*(ClientNetworking.getAppropriateConfig().generalStandSettings.standGuardMultiplier*0.01));
        } else if (PowerTypes.hasPowerActivelyEquipped(rdbt$this()) && rdbt$this() instanceof Player pl){
            return ((IPowersPlayer)pl).rdbt$getPowers().getMaxGuardPoints();
        }
        return 9F;
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
    public int roundabout$anubisVanishTicks = 0;

    @Unique
    @Override
    public int roundabout$getAnubisVanishTicks(){
        return roundabout$anubisVanishTicks;
    }
    @Unique
    @Override
    public void roundabout$setAnubisVanishTicks(int set){
        roundabout$anubisVanishTicks = Mth.clamp(set,0,10);
    }



    @Unique
    public AnimationState roundabout$wornStandAnimation = new AnimationState();
    @Unique
    @Override
    public AnimationState roundabout$getWornStandAnimation(){
        return roundabout$wornStandAnimation;
    }


    @Unique
    public AnimationState roundabout$wornStandIdleAnimation = new AnimationState();
    @Unique
    @Override
    public AnimationState roundabout$getWornStandIdleAnimation(){
        return roundabout$wornStandIdleAnimation;
    }
    @Unique
    @Override
    public void roundabout$setWornStandIdleAnimation(AnimationState layer){
        roundabout$wornStandIdleAnimation = layer;
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
        if (level().isClientSide() && rdbt$this() instanceof Player pl &&
                !((IPlayerEntity)pl).rdbt$getCooldownQuery()){
            return roundabout$getMaxGuardPoints();
        }
        if (roundabout$GuardPoints > roundabout$getMaxGuardPoints() && !level().isClientSide()){
            roundabout$setGuardPoints(roundabout$getMaxGuardPoints());
        }
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
    public int rdbt$ticksUntilGuardRegen = 0;
    @Unique
    public void roundabout$tickGuard(){
            if (!this.roundabout$GuardBroken && this.roundabout$isGuarding() && this.roundabout$shieldNotDisabled()) {
                rdbt$ticksUntilGuardRegen = 14;
            } else {
                if (rdbt$ticksUntilGuardRegen > 0) {
                    rdbt$ticksUntilGuardRegen--;
                }
            }

        if (this.roundabout$GuardPoints < this.roundabout$getMaxGuardPoints()) {
            if (this.roundabout$GuardBroken){
                float guardRegen = this.roundabout$getMaxGuardPoints() / 100;
                this.roundabout$regenGuard(guardRegen);
            } else if (!this.roundabout$isGuarding() && this.roundabout$shieldNotDisabled()){
                if (rdbt$ticksUntilGuardRegen <= 0) {
                    float guardRegen = this.roundabout$getMaxGuardPoints() / 220;
                    this.roundabout$regenGuard(guardRegen);
                }
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
    @Override
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

    public int rdbt$checkContext(){
        if (roundabout$getActive()){
            return 0;
        } else {
            return 1;
        }
    }

    @Unique
    public void roundabout$tryPower(int move, boolean forced){

            if (!this.roundabout$isClashing() || move == PowerIndex.CLASH_CANCEL) {
                this.roundabout$getStandPowers().tryPower(move, forced);
                rdbt$tryPowerStuff();
            }
    }
    @Unique
    @Override
    public void roundabout$tryIntPower(int move, boolean forced, int chargeTime){

            if (!this.roundabout$isClashing() || move == PowerIndex.CLASH_CANCEL) {
                this.roundabout$getStandPowers().tryIntPower(move, forced, chargeTime);
                rdbt$tryPowerStuff();
            }
    }
    @Unique
    @Override
    public void roundabout$tryIntPower(int move,  boolean forced, int chargeTime,int move2, int move3){

            if (!this.roundabout$isClashing() || move == PowerIndex.CLASH_CANCEL) {
                this.roundabout$getStandPowers().tryTripleIntPower(move, forced, chargeTime, move2, move3);
                rdbt$tryPowerStuff();
            }
    }
    @Unique
    @Override
    public void roundabout$tryBlockPosPower(int move, boolean forced, BlockPos blockPos){

            if (!this.roundabout$isClashing() || move == PowerIndex.CLASH_CANCEL) {
                this.roundabout$getStandPowers().tryBlockPosPower(move, forced, blockPos);
                rdbt$tryPowerStuff();
            }

    }
    @Unique
    @Override
    public void roundabout$tryPosPower(int move, boolean forced, Vec3 pos){

            if (!this.roundabout$isClashing() || move == PowerIndex.CLASH_CANCEL) {
                this.roundabout$getStandPowers().tryPosPower(move, forced, pos);
                rdbt$tryPowerStuff();
            }
    }


    @Unique
    @Override
    public void roundabout$tryPowerF(int move, boolean forced){
            if (rdbt$this() instanceof Player pl){
                ((IFatePlayer)pl).rdbt$getFatePowers().tryPower(move, forced);
                rdbt$tryPowerFateStuff();
            }
    }
    @Unique
    @Override
    public void roundabout$tryIntPowerF(int move, boolean forced, int chargeTime){
            if (rdbt$this() instanceof Player pl){
                ((IFatePlayer)pl).rdbt$getFatePowers().tryIntPower(move, forced, chargeTime);
                rdbt$tryPowerFateStuff();
            }
    }
    @Unique
    @Override
    public void roundabout$tryIntPowerF(int move,  boolean forced, int chargeTime,int move2, int move3){

            if (rdbt$this() instanceof Player pl){
                ((IFatePlayer)pl).rdbt$getFatePowers().tryTripleIntPower(move, forced, chargeTime, move2, move3);
                rdbt$tryPowerFateStuff();
            }
    }
    @Unique
    @Override
    public void roundabout$tryBlockPosPowerF(int move, boolean forced, BlockPos blockPos){
            if (rdbt$this() instanceof Player pl){
                ((IFatePlayer)pl).rdbt$getFatePowers().tryBlockPosPower(move, forced, blockPos);
                rdbt$tryPowerFateStuff();
            }
    }
    @Unique
    @Override
    public void roundabout$tryPosPowerF(int move, boolean forced, Vec3 pos){
            if (rdbt$this() instanceof Player pl){
                ((IFatePlayer)pl).rdbt$getFatePowers().tryPosPower(move, forced, pos);
                rdbt$tryPowerFateStuff();
            }
    }


    @Unique
    @Override
    public void roundabout$tryPowerP(int move, boolean forced){
        if (rdbt$this() instanceof Player pl){
            ((IPowersPlayer)pl).rdbt$getPowers().tryPower(move, forced);
            rdbt$tryPowerPowerStuff();
        }
    }
    @Unique
    @Override
    public void roundabout$tryIntPowerP(int move, boolean forced, int chargeTime){
        if (rdbt$this() instanceof Player pl){
            ((IPowersPlayer)pl).rdbt$getPowers().tryIntPower(move, forced, chargeTime);
            rdbt$tryPowerPowerStuff();
        }
    }
    @Unique
    @Override
    public void roundabout$tryIntPowerP(int move,  boolean forced, int chargeTime,int move2, int move3){

        if (rdbt$this() instanceof Player pl){
            ((IPowersPlayer)pl).rdbt$getPowers().tryTripleIntPower(move, forced, chargeTime, move2, move3);
            rdbt$tryPowerPowerStuff();
        }
    }
    @Unique
    @Override
    public void roundabout$tryBlockPosPowerP(int move, boolean forced, BlockPos blockPos){
        if (rdbt$this() instanceof Player pl){
            ((IPowersPlayer)pl).rdbt$getPowers().tryBlockPosPower(move, forced, blockPos);
            rdbt$tryPowerPowerStuff();
        }
    }
    @Unique
    @Override
    public void roundabout$tryPosPowerP(int move, boolean forced, Vec3 pos){
        if (rdbt$this() instanceof Player pl){
            ((IPowersPlayer)pl).rdbt$getPowers().tryPosPower(move, forced, pos);
            rdbt$tryPowerPowerStuff();
        }
    }




    public void rdbt$tryPowerStuff(){
        this.roundabout$getStandPowers().syncActivePower();
        if (this.level().isClientSide) {
            this.roundabout$getStandPowers().kickStarted = false;
        }
    }
    public void rdbt$tryPowerFateStuff(){
        if (rdbt$this() instanceof Player pl){
            IFatePlayer ifp = ((IFatePlayer) pl);
            ifp.rdbt$getFatePowers().syncActivePower();
            if (this.level().isClientSide) {
                ifp.rdbt$getFatePowers().kickStarted = false;
            }
        }
    }
    public void rdbt$tryPowerPowerStuff(){
        if (rdbt$this() instanceof Player pl){
            IPowersPlayer ifp = ((IPowersPlayer) pl);
            ifp.rdbt$getPowers().syncActivePower();
            if (this.level().isClientSide) {
                ifp.rdbt$getPowers().kickStarted = false;
            }
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
        return this.roundabout$getStandPowers().isGuarding() ||
                (rdbt$this() instanceof Player pl && ((IPowersPlayer)pl).rdbt$getPowers().isGuarding());
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
        return (this.roundabout$shieldNotDisabled() && roundabout$isGuarding() &&
                (
                        (PowerTypes.hasStandActive(rdbt$this()) &&
                                this.roundabout$getStandPowers().getAttackTimeDuring() >= ClientNetworking.getAppropriateConfig().generalStandSettings.standGuardDelayTicks)
                ||
                        (PowerTypes.hasPowerActive(rdbt$this()) && rdbt$this() instanceof Player pl &&
                        ((IPowersPlayer)pl).rdbt$getPowers().getAttackTimeDuring() >= ClientNetworking.getAppropriateConfig().vampireSettings.powerGuardDelayTicks)
                )

        );
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

            ItemStack StandDisc = this.roundabout$getStandDisc();
            if (!StandDisc.isEmpty() && StandDisc.getItem() instanceof StandDiscItem SD){
                if (this.roundabout$Powers == null || !SD.standPowers.getClass().equals(this.roundabout$Powers.getClass())) {
                    SD.generateStandPowers((LivingEntity) (Object) this);
                }
            } else {
                if (this.roundabout$Powers == null) {
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
            roundabout$tryPowerP(PowerIndex.NONE, true);
        }
        roundabout$getStandPowers().onStandSummon(!active);

        ((LivingEntity) (Object) this).getEntityData().set(ROUNDABOUT$STAND_ACTIVE, active);
    }

    @Unique
    public int roundabout$sealedTicks = -1;
    @Unique
    public int roundabout$maxSealedTicks = -1;

    @Override
    @Unique
    public void roundabout$setSealedTicks(int ticks){
        if (PowerTypes.hasStandActive(rdbt$this())){
            roundabout$setSummonCD(8);
            roundabout$setActive(false);
            roundabout$tryPower(PowerIndex.NONE, true);
            if (this.level().isClientSide()) {
                C2SPacketUtil.trySingleBytePacket(PacketDataIndex.SINGLE_BYTE_DESUMMON);
            }
        }
        roundabout$sealedTicks = ticks;
    }

    @Override
    public void roundabout$tryBlockPosPowerF(int move, boolean forced, BlockPos blockPos, BlockHitResult blockHit){
        if (!this.roundabout$isClashing() || move == PowerIndex.CLASH_CANCEL) {
            if (rdbt$this() instanceof Player pl){
                ((IFatePlayer)pl).rdbt$getFatePowers().tryBlockPosPower(move, forced, blockPos, blockHit);
                rdbt$tryPowerFateStuff();
            }
        }
    }
    @Override
    public void roundabout$tryBlockPosPowerP(int move, boolean forced, BlockPos blockPos, BlockHitResult blockHit){
        if (!this.roundabout$isClashing() || move == PowerIndex.CLASH_CANCEL) {
            if (rdbt$this() instanceof Player pl){
                ((IPowersPlayer)pl).rdbt$getPowers().tryBlockPosPower(move, forced, blockPos, blockHit);
                rdbt$tryPowerPowerStuff();
            }
        }
    }
    @Override
    public void roundabout$tryBlockPosPower(int move, boolean forced, BlockPos blockPos, BlockHitResult blockHit){
        if (!this.roundabout$isClashing() || move == PowerIndex.CLASH_CANCEL) {
            this.roundabout$getStandPowers().tryBlockPosPower(move, forced, blockPos, blockHit);
            rdbt$tryPowerStuff();
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
        return roundabout$sealedTicks > -1 || (rdbt$this() instanceof Player PE && PE.isSpectator());
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
        if (!roundabout$getActive() || forced) {
            //world.getEntity

            if (PowerTypes.hasStandActivelyEquipped(rdbt$this())){

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
                active = true;
            }

        } else {
            this.roundabout$tryPower(PowerIndex.NONE,true);
            this.roundabout$tryPowerP(PowerIndex.NONE,true);
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
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$POSSESSOR, -1);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$HEAT, 0);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$IS_BOUND_TO, -1);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$IS_ZAPPED_TO_ATTACK, -1);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$TRUE_INVISIBILITY, -1);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$METALLICA_INVISIBILITY, -1);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$ADJUSTED_GRAVITY, -1);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$ONLY_BLEEDING, true);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$COMBAT_MODE, false);
            ((LivingEntity) (Object) this).getEntityData().define(ROUNDABOUT$STAND_DISC, ItemStack.EMPTY);
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
        if (MainUtil.isArmorBypassingButNotShieldBypassing($$0,rdbt$this())) {
            float yeah = rdbt$mutuallyGetDamageAfterArmorAbsorb($$0,$$1);
            if (yeah != 1){
                ci.setReturnValue(yeah);
                return;
            }
            ci.setReturnValue($$1);
        }
    }

    /**Here, we cancel barrage if it has not "wound up" and the user is hit*/
    @Inject(method = "hurt", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$RoundaboutDamage(DamageSource $$0, float $$1, CallbackInfoReturnable<Boolean> ci) {

        //Vampire transformation immunity
        if (FateTypes.isTransforming(rdbt$this()) && !$$0.is(DamageTypes.GENERIC_KILL)){
            ci.setReturnValue(false);
            return;
        }

        //Right after a gravity flip you are briefly immune to suffocation
        if ($$0.is(DamageTypes.IN_WALL)){
            if (((IGravityEntity)rdbt$this()).roundabout$getSuffocationTicks() > 0){
                ci.setReturnValue(false);
                return;
            }
        }


        if ($$0.getEntity() instanceof Player pe && !$$0.isIndirect()
        && !$$0.is(DamageTypes.THORNS)&& !$$0.is(ModDamageTypes.CORPSE) &&
                !$$0.is(ModDamageTypes.CORPSE_EXPLOSION) &&
                !$$0.is(ModDamageTypes.CORPSE_ARROW)){
            if (((StandUser)pe).roundabout$getStandPowers().interceptDamageDealtEvent($$0,$$1, ((LivingEntity)(Object)this))){
                ci.setReturnValue(false);
                return;
            }
        }

        //Coprse damage is converted and multiplied for Justice Army
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

        //Gasoline invincibility frames
        if ($$0.is(ModDamageTypes.GASOLINE_EXPLOSION)){
            if (roundabout$gasolineIFRAMES > 0){
                ci.setReturnValue(false);
                return;
            }
        }

        if (this.roundabout$gasTicks > -1) {
            if ($$0.is(DamageTypeTags.IS_FIRE) || ($$0.getDirectEntity() instanceof Projectile && $$0.getDirectEntity().isOnFire()) || $$0.is(ModDamageTypes.MATCH)) {
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
            if (PowerTypes.hasStandActivelyEquipped(rdbt$this())){
                if (this.roundabout$getStandPowers().preCanInterruptPower($$0,$$0.getDirectEntity(),MainUtil.isStandDamage($$0))) {
                    this.roundabout$tryPower(PowerIndex.NONE, true);
                    if (!(((LivingEntity)(Object)this) instanceof Player)){
                        this.roundabout$setAttackTimeDuring(this.roundabout$getStandPowers().getMobRecoilTime());
                    }
                }
            } else if (PowerTypes.hasPowerActivelyEquipped(rdbt$this()) &&  rdbt$this() instanceof Player pl){
                if (((IPowersPlayer)pl).rdbt$getPowers().preCanInterruptPower($$0,$$0.getDirectEntity(),MainUtil.isStandDamage($$0))) {
                    this.roundabout$tryPowerP(PowerIndex.NONE, true);
                    if (!(((LivingEntity)(Object)this) instanceof Player)){
                        ((IPowersPlayer)pl).rdbt$getPowers().setAttackTimeDuring(((IPowersPlayer)pl).rdbt$getPowers().getMobRecoilTime());
                    }
                }
            }
            this.roundabout$setIdleTime(-1);
        }
    }

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z", at = @At(value = "HEAD"))
    public void roundabout$carryEffects(MobEffectInstance $$0, CallbackInfoReturnable<Boolean> cir) {
        if (this.roundabout$isPossessed()) {
            LivingEntity poss = this.roundabout$getPossessor();
            if (poss != null) {
                poss.addEffect($$0);
            }
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


    @Unique
    @Override
    public void roundabout$setMetallicaInvis(int invis) {
        if (this.entityData.hasItem(ROUNDABOUT$METALLICA_INVISIBILITY)) {
            this.getEntityData().set(ROUNDABOUT$METALLICA_INVISIBILITY, invis);
        }
    }

    @Unique
    @Override
    public int roundabout$getMetallicaInvis() {
        if (this.entityData.hasItem(ROUNDABOUT$METALLICA_INVISIBILITY)) {
            return this.getEntityData().get(ROUNDABOUT$METALLICA_INVISIBILITY);
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
                    Direction dir = ((IGravityEntity)this).roundabout$getGravityDirection();
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
;
                        if (dir == Direction.NORTH || dir == Direction.SOUTH){
                            yesVec = this.getPosition(1).add(RotationUtil.vecPlayerToWorld(this.getDeltaMovement(),dir));
                            yesVec2 = new BlockPos((int) yesVec.x, (int) yesVec.y, (int) (this.position().z));
                        }
                        if (dir == Direction.EAST || dir == Direction.WEST){
                            yesVec = this.getPosition(1).add(RotationUtil.vecPlayerToWorld(this.getDeltaMovement(),dir));
                            yesVec2 = new BlockPos((int) (this.position().x), (int) yesVec.y, (int) yesVec.z);
                        }
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
            gravityConstant *= 0.3F;
            modified = true;
        }

        if (modified){
            int gravityConstandAdjusted = (int) gravityConstant;
            roundabout$setAdjustedGravity(gravityConstandAdjusted);
        } else {
            roundabout$setAdjustedGravity(-1);
        }
    }


    // Cheat Death and negate the death event
    @Inject(method = "checkTotemDeathProtection(Lnet/minecraft/world/damagesource/DamageSource;)Z", at = @At(value = "HEAD"), cancellable = true)
    public void rdbt$checkTotemDeathProtection(DamageSource dsource, CallbackInfoReturnable<Boolean> cir) {

        if ( (rdbt$this() instanceof Player pl && ((IFatePlayer)pl).rdbt$getFatePowers().cheatDeath(dsource))
                || roundabout$getStandPowers().cheatDeath(dsource)){
            cir.setReturnValue(true);
        } else if (hasEffect(ModEffects.VAMPIRE_BLOOD)){
            removeEffect(ModEffects.VAMPIRE_BLOOD);
            if (rdbt$this() instanceof Player pl){
                if (FateTypes.isHuman(pl)) {
                    ((IFatePlayer) pl).rdbt$startVampireTransformation(false);
                    pl.setHealth(1);
                    cir.setReturnValue(true);
                }
            } else {
                if (rdbt$this() instanceof Mob mb && !((IMob)mb).roundabout$isVampire()){
                    setHealth(getMaxHealth());
                    this.level().playSound(null, blockPosition(), ModSounds.VAMPIRE_AWAKEN_EVENT,
                            SoundSource.PLAYERS, 1F, 1F);
                    cir.setReturnValue(true);
                    ((IMob)mb).roundabout$setVampire(true);
                    if (level() instanceof ServerLevel SL) {
                        SL.sendParticles(ModParticles.BLUE_SPARKLE,
                                this.getX(), this.getY() + this.getBbHeight() * 0.5, this.getZ(),
                                50, 0, 0, 0, 0.2);
                        SL.sendParticles(ModParticles.BLOOD_MIST,
                                this.getX(), this.getY() + this.getBbHeight() * 0.5, this.getZ(),
                                10, 0.4, 0.4, 0.4, 0.025);
                        roundabout$deeplyRemoveAttackTarget();
                    }
                }
            }
        }
    }
    @Inject(method = "setSprinting", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$canSprintPlayer(boolean $$0, CallbackInfo ci) {
        if (roundabout$getStandPowers().cancelSprint() || FateTypes.isTransforming(rdbt$this()) ||
                (FateTypes.takesSunlightDamage(rdbt$this()) && FateTypes.isInSunlight(rdbt$this()))
        || (rdbt$this() instanceof Player pl && ((IFatePlayer)pl).rdbt$getFatePowers().cancelSprint())){
            ci.cancel();
        }
    }

    @Inject(
            method = "maxUpStep()F",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void roundabout$maxUpStep(CallbackInfoReturnable<Float> cir) {
        float stepAddon = roundabout$getStandPowers().getStepHeightAddon();
        if (rdbt$this() instanceof Player pl){
            stepAddon += ((IFatePlayer)pl).rdbt$getFatePowers().getStepHeightAddon();
        }
        if (stepAddon > 0){
            cir.setReturnValue(((IEntityAndData)this).roundabout$getStepHeight() + stepAddon);
        }
    }

    @Unique
    @Override
    public boolean rdbt$getJumping(){
        return jumping;
    }

    @Override
    public double roundabout$getGravity(double ogGrav){

        if (this.getEntityData().hasItem(ROUNDABOUT$ADJUSTED_GRAVITY) && this.getDeltaMovement().y <= 0){
            double basegrav = roundabout$getAdjustedGravity();
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
    @Unique
    @Override
    public void rdbt$adjGravTrav(){
        roundabout$adjustGravity();

        if (this.isControlledByLocalInstance()) {
            if (MainUtil.isPlayerBonkingHead(((LivingEntity)(Object)this)) || isUsingItem()){
                roundabout$setBigJumpCurrentProgress(0);
                roundabout$setBigJump(false);
            }
            float curr = roundabout$getBigJumpCurrentProgress();
            float max = roundabout$getBonusJumpHeight();

            if (roundabout$getBigJump() || (curr < 1 && getDeltaMovement().y >= 0)) {
                if (curr < max+1) {
                    if (roundabout$isBubbleEncased()) {
                        roundabout$setBigJumpCurrentProgress(curr + 0.495F);
                    } else {
                        roundabout$setBigJumpCurrentProgress(curr + 0.68F);
                    }
                    Vec3 $$0 = this.getDeltaMovement();
                    Float sped = roundabout$mutualGetSpeed(1F);
                    if (sped < 1){
                        $$0 = $$0.scale(sped);
                    }
                    if (roundabout$cancelsprintJump()){
                        $$0 = $$0.scale(0.6F);
                    }


                    if (!onGround()){
                        if (roundabout$getBigJump()){
                            if (roundabout$isBubbleEncased()){
                                this.setDeltaMovement($$0.x*0.91, (double) this.getJumpPower(), $$0.z*0.91);
                            } else {
                                float curve = getSpeed();

                                curr = roundabout$getBigJumpCurrentProgress();
                                this.setDeltaMovement($$0.x,
                                        (double) this.getJumpPower()*FateTypes.getJumpHeightPower(
                                                ((LivingEntity)(Object)this),
                                                (curr > 2)
                                        ),
                                        $$0.z);
                            }
                        }
                    }

                }
            } else {
                /**Air Drag*/
                Vec3 $$0 = this.getDeltaMovement();
                if (roundabout$isBubbleEncased() && !onGround()){
                    this.setDeltaMovement($$0.x*0.92, (double)$$0.y, $$0.z*0.92);

                }
            }
        }
    }

    @Inject(method = "travel", at = @At(value = "HEAD"))
    public void roundabout$travelHead(CallbackInfo ci) {

        Direction gravityDirection = GravityAPI.getGravityDirection(rdbt$this());
        if (gravityDirection != Direction.DOWN)
            return;
        rdbt$adjGravTrav();

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
        if (this.roundabout$isDazed() && !FateTypes.isTransforming(rdbt$this())) {
            return 0;
        } else {
            return $$1;
        }
    }

    /**Modifies the gravity influence*/
    @ModifyVariable(method = "travel(Lnet/minecraft/world/phys/Vec3;)V", at = @At(value = "STORE"),ordinal = 0)
    private double roundabout$TravelGravity(double $$1) {
        if (this.roundabout$isDazed() && !FateTypes.isTransforming(rdbt$this())) {
            return 0;
        } else {
            return roundabout$getGravity($$1);
        }
    }

    @Unique
    @Override
    public double rdbt$modelTravel(double $$1){
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

    @ModifyVariable(method = "travel(Lnet/minecraft/world/phys/Vec3;)V", at = @At("STORE"),ordinal = 0)
    private double rdbt$Travel3(double $$1) {
        return rdbt$modelTravel($$1);
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
        } else if (rdbt$this() instanceof Player pl && ((IPowersPlayer)pl).rdbt$getPowers().isFaded()){
            cir.setReturnValue(0f);
        }
    }
    /**This code prevents you from swimming upwards while barrage clashing*/
    @Inject(method = "jumpInLiquid", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$swimUpward(TagKey<Fluid> $$0, CallbackInfo ci) {
        if (this.roundabout$isClashing() || (FateTypes.isTransforming(rdbt$this()))) {
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

    public LivingEntity rdbt$this(){
        return ((LivingEntity) (Object)this);
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
        }
        if (roundabout$mutualActuallyHurt($$0,$$1)){
            ci.cancel();
        }
    }

    @Override
    @Unique
    public float roundabout$mutualGetSpeed(float basis){
        byte curse = this.roundabout$getLocacacaCurse();
        LivingEntity livingEntity = ((LivingEntity)(Object)this);
        ItemStack hand = livingEntity.getMainHandItem();
        ItemStack offHand = livingEntity.getOffhandItem();
        if (curse > -1) {
            if (curse == LocacacaCurseIndex.RIGHT_LEG || curse == LocacacaCurseIndex.LEFT_LEG) {
                basis = (basis * 0.82F);
            } else if (curse == LocacacaCurseIndex.CHEST) {
                basis = (basis * 0.85F);
            }
        }


        if (hand.getItem() instanceof RoadRollerItem || offHand.getItem() instanceof RoadRollerItem) {
            if (!FateTypes.isVampireStrong(rdbt$this())) {
                basis = (basis * 0.50F);
            }
        }

        float sd = HeatUtil.getSlowdown(rdbt$this());
        if (sd > 0){
            basis = basis * (1f-sd);
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

        MobEffectInstance melting = this.rdbt$this().getEffect(ModEffects.MELTING);
        if (melting != null) {
            if (melting.getAmplifier() >= 8) {
                basis *= 0.7F;
            }
        }
        if (FateTypes.takesSunlightDamage(rdbt$this()) && FateTypes.isInSunlight(rdbt$this())){
            basis *= 0.15F;
        }

        if (FateTypes.hasBloodHunger((LivingEntity) (Object) this)) {
            if (roundabout$isDrown || getAirSupply() <= 0) {
                if (!isUnderWater()) {
                    basis *= ClientNetworking.getAppropriateConfig().vampireSettings.drownSpeedModifier;
                }
            }
        }

        List<AnubisSlipstreamEntity> slipstreams = rdbt$this().level().getEntitiesOfClass(AnubisSlipstreamEntity.class,this.getBoundingBox().inflate(3));
        if (!slipstreams.isEmpty() && rdbt$this() instanceof Player &&
                ( (this.roundabout$getStandPowers() instanceof PowersAnubis && !PowerTypes.hasStandActive(this) || !(this.roundabout$getStandPowers() instanceof PowersAnubis)  )  )
        ) {
            basis *= 1.6F;
        }


        if (!((StandUser)this).roundabout$getStandDisc().isEmpty()){
            basis = ((StandUser)this).roundabout$getStandPowers().inputSpeedModifiers(basis);
        }
        if (rdbt$this() instanceof Player pl) {
            basis = ((IFatePlayer) this).rdbt$getFatePowers().inputSpeedModifiers(basis);
            basis = ((IPowersPlayer) this).rdbt$getPowers().inputSpeedModifiers(basis);
        }

        return basis;
    }

    @Override
    @Unique
    public boolean roundabout$mutualActuallyHurt(DamageSource $$0, float $$1){
        if (!this.isInvulnerableTo($$0) && $$1 > 0) {

            Entity zent = $$0.getEntity();
            if (zent != null && zent instanceof LivingEntity LE){
                ItemStack stack = LE.getMainHandItem();
                if (stack != null && !stack.isEmpty()) {
                    if (stack.is(ModItems.SCISSORS) && ($$0.is(DamageTypes.PLAYER_ATTACK) || $$0.is(DamageTypes.MOB_ATTACK))) {
                        if (MainUtil.getMobBleed(this)) {
                            roundabout$setBleedLevel(0);
                            addEffect(new MobEffectInstance(ModEffects.BLEED, 300, 0), LE);
                        }
                        stack.hurtAndBreak(1, LE, $$0x -> $$0x.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                    }

                    if (stack.is(ModItems.SACRIFICIAL_DAGGER) && ($$0.is(DamageTypes.PLAYER_ATTACK) || $$0.is(DamageTypes.MOB_ATTACK))) {
                        if (MainUtil.getMobBleed(this)) {
                            roundabout$setBleedLevel(0);
                            addEffect(new MobEffectInstance(ModEffects.BLEED, 300, 0), LE);
                        }
                        stack.hurtAndBreak(1, LE, $$0x -> $$0x.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                    }

                    if (stack.getItem() instanceof SignBlockItem && ($$0.is(DamageTypes.PLAYER_ATTACK) || $$0.is(DamageTypes.MOB_ATTACK))) {
                        if (!level().isClientSide()) {
                            if (MainUtil.getMobBleed(rdbt$this())) {
                                MainUtil.makeBleed(rdbt$this(), 1, 100, zent);
                            }
                            CompoundTag ct = stack.getOrCreateTagElement("BlockStateTag");
                            int ctd = ct.getInt("damaged");
                            ctd++;
                            if (ctd > 2) {
                                level().playSound(null, blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.PLAYERS,
                                        1F, 1);
                                stack.shrink(1);
                            } else {
                                level().playSound(null, blockPosition(), ModSounds.SIGN_HIT_EVENT, SoundSource.PLAYERS,
                                        1F, 1);
                                ct.putInt("damaged", ctd);
                            }
                        }
                    }
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

            if (($$0.getEntity() != null || $$0.is(DamageTypes.THROWN)) && !$$0.is(DamageTypes.THORNS)){
                if (((IEntityAndData)this).roundabout$getTrueInvisibility() > -1 &&
                        ClientNetworking.getAppropriateConfig().achtungSettings.revealLocationWhenHurt){
                    ((IEntityAndData)this).roundabout$setTrueInvisibility(-1);
                }
                if ($$0.getEntity() != null){
                    if (((IEntityAndData)$$0.getEntity()).roundabout$getTrueInvisibility() > -1 &&
                            ClientNetworking.getAppropriateConfig().achtungSettings.revealLocationWhenDamaging){
                        ((IEntityAndData)$$0.getEntity()).roundabout$setTrueInvisibility(-1);
                    }
                }
                if ($$0.getDirectEntity() != null){
                    if (((IEntityAndData)$$0.getDirectEntity()).roundabout$getTrueInvisibility() > -1 &&
                            ClientNetworking.getAppropriateConfig().achtungSettings.revealLocationWhenDamaging){
                        ((IEntityAndData)$$0.getDirectEntity()).roundabout$setTrueInvisibility(-1);
                    }
                }
            }

            roundabout$getStandPowers().onActuallyHurt($$0,$$1);

            Entity bound = roundabout$getBoundTo();
            if (bound != null && ($$0.getEntity() != null || $$0.is(DamageTypes.MAGIC) || $$0.is(DamageTypes.EXPLOSION)) && !$$0.is(ModDamageTypes.STAND_FIRE)){
                roundabout$dropString();
            }



            //Frozen deaths from vampire freeze / ice sculptures / white album
            if ($$0.getEntity() != null && !$$0.is(DamageTypes.THORNS) && !$$0.is(ModDamageTypes.STAND_FIRE)){
                if (HeatUtil.isBodyFrozen(rdbt$this()) && !level().isClientSide()){
                    ((ServerLevel) this.level()).sendParticles(
                            new BlockParticleOption(ParticleTypes.BLOCK, Blocks.ICE.defaultBlockState()),
                            this.getEyePosition().x,
                            this.getEyePosition().y,
                            this.getEyePosition().z,
                            110, 0.0, 0, 0.0, 0.5);

                    level().playSound(
                            null,
                            blockPosition(),
                            Blocks.ICE.defaultBlockState().getSoundType().getBreakSound(),
                            SoundSource.PLAYERS,
                            1.0F,
                            (float) ( 1.0F+Math.random()*0.01F));
                    HeatUtil.resetHeat(rdbt$this());

                    if (rdbt$this() instanceof Player) {
                        hurt(ModDamageTypes.of(level(), ModDamageTypes.ICE_SHATTER, this), 15F);
                    } else {
                        hurt(ModDamageTypes.of(level(), ModDamageTypes.ICE_SHATTER, this), 30F);
                    }
                    S2CPacketUtil.shatterIce(getId());
                    return true;
                }
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
                        || stack.getItem() instanceof SacrificialDaggerItem
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

    //For Model to dissapear during death animation like vampire ice shatter
    public boolean rdbt$hideDeath = false;
    public void rdbt$setHideDeath(Boolean hide){
        rdbt$hideDeath = hide;
    }
    public boolean rdbt$getHideDeath(){
        return rdbt$hideDeath;
    }

    /**Reduced gravity changes fall damage calcs*/
    @Inject(method = "calculateFallDamage", at = @At(value = "HEAD"), cancellable = true)
    protected void rooundabout$calculateFallDamage(float $$0, float $$1, CallbackInfoReturnable<Integer> cir) {

        if (this.roundabout$leapTicks > -1 || roundabout$isBubbleEncased()) {
            cir.setReturnValue(0);
            return;
        }
        boolean adj = false;
        if (roundabout$getStandPowers() instanceof PowersWalkingHeart PW){
            $$1/=2;
            adj = true;
        }
        if ((LivingEntity)(Object)this instanceof Player pl){
            StandUser SU = (StandUser) pl;
            float fd = ((IFatePlayer)pl).rdbt$getFatePowers().getJumpHeightAddonMax();
            if (SU.roundabout$getStandPowers() != null) {
                fd += SU.roundabout$getStandPowers().getJumpHeightAddon();
            }
            if (fd > 0){
                adj = true;
                $$0 = Math.max(0,$$0-fd);
            }

            float jd = ((IFatePlayer)pl).rdbt$getFatePowers().getJumpDamageMult();
            if (jd != 1){
                adj = true;
                $$1*= jd;
            }
        }

        if (rdbt$this() instanceof Mob mb && ((IMob)mb).roundabout$isVampire()){
            $$1/=2;
            adj = true;
        }
        int yesInt = roundabout$getAdjustedGravity();
        if (yesInt > 0 || adj){
            cir.setReturnValue(roundabout$calculateFallDamage($$0,$$1,yesInt));
            return;
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


    public float rdbt$mutuallyGetDamageAfterArmorAbsorb(DamageSource source, float damageAmount){

        boolean modified = false;
        if (((LivingEntity)(Object)this) instanceof Mob){
            if (!((StandUser)this).roundabout$getStandDisc().isEmpty()){
                if (this.getMaxHealth() > 1) {
                    if (this.getMaxHealth() <= 3) {
                        damageAmount *= 0.5F;
                    } else if (this.getMaxHealth() <= 6) {
                        damageAmount *= 0.75F;
                    }
                }
                if ((source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.MOB_PROJECTILE))
                        || source.is(DamageTypes.MOB_ATTACK_NO_AGGRO)){
                    damageAmount*=0.5F;
                }
                modified = true;
            }
        }
        if (this.hasEffect(ModEffects.FACELESS)) {
            float amt = (float) (0.15* this.getEffect(ModEffects.FACELESS).getAmplifier()+0.15F);
            damageAmount = (damageAmount+(damageAmount*amt));
            modified = true;
        }
        float changeDamage = FateTypes.getDamageResist(rdbt$this(),source,damageAmount);
        if (changeDamage > 0){
            damageAmount = (damageAmount-(damageAmount*changeDamage));
            modified = true;
        }

        float addDamage = FateTypes.getDamageAdd(rdbt$this(),source,damageAmount);
        if (addDamage > 0){
            damageAmount = (damageAmount+(damageAmount*addDamage));
            modified = true;
        }
        if (roundabout$getZappedToID() > -1){
            if (!MainUtil.isMeleeDamage(source)){
                damageAmount = damageAmount*ClientNetworking.getAppropriateConfig().survivorSettings.resilienceToNonMeleeAttacksWhenZapped;
                modified = true;
            }
        }

        if (((LivingEntity)(Object)this) instanceof Player &&
                !(((TimeStop)this.level()).CanTimeStopEntity(this))){
            if (ClientNetworking.getAppropriateConfig().timeStopSettings.postTSSoften){
                if (roundabout$getStandPowers().softenTicks > 20){
                    if (source.is(ModDamageTypes.GASOLINE_EXPLOSION)
                            || MainUtil.isStandDamage(source)
                            || source.is(DamageTypes.PLAYER_EXPLOSION)
                            || source.is(DamageTypes.EXPLOSION)){
                        damageAmount*=0.5F;
                        modified = true;
                    }
                }
            }
        }
        if (source.getEntity() instanceof LivingEntity LE){
            if (((StandUser)LE).roundabout$getZappedToID() > -1){
                if (MainUtil.isMeleeDamage(source)){
                    damageAmount = damageAmount*ClientNetworking.getAppropriateConfig().survivorSettings.buffToMeleeAttacksWhenZapped;
                    modified = true;

                    if (LE.getMainHandItem() != null && LE.getMainHandItem().isEmpty()){
                        float power = ClientNetworking.getAppropriateConfig().survivorSettings.bonusDamageWhenPunching;
                        if (power > 0){
                            damageAmount += (CombatRules.getDamageAfterAbsorb(power, (float)this.getArmorValue(), (float)this.getAttributeValue(Attributes.ARMOR_TOUGHNESS)));
                        }
                        if (MainUtil.getMobBleed(LE)){
                            MainUtil.makeBleed(LE,0,200,LE);
                        }
                    }
                }
            }
        }

        if (modified){
            return damageAmount;
        }
        return 1;
    }

    /**This code makes stand user mobs resist attacks from other mobs*/
    @Inject(method = "getDamageAfterArmorAbsorb", at = @At(value = "RETURN"), cancellable = true)
    protected void rooundabout$armorAbsorb(DamageSource $$0, float $$1, CallbackInfoReturnable<Float> cir) {

        float yeah = rdbt$mutuallyGetDamageAfterArmorAbsorb($$0,$$1);
        if (yeah != 1){
            cir.setReturnValue(yeah);
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
            instanceof PowersJustice))) && ($$0.is(DamageTypes.PLAYER_ATTACK) || $$0.is(DamageTypes.MOB_ATTACK))) {
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


        //Players have their own die event
        if (FateTypes.takesSunlightDamage(rdbt$this())) {
            if ($$0.is(ModDamageTypes.SUNLIGHT)){
                if (this.level() instanceof ServerLevel SL){
                    Vec3 position = this.getPosition(1);
                    Vec3 position2 = this.getEyePosition();
                    Vec3 position3 = this.getEyePosition().subtract(this.getPosition(1)).multiply(new Vec3(0.5F,
                            0.5F,0.5F));
                    position3 = position3.add(this.getPosition(1));
                    SL.sendParticles(ModParticles.FIRE_CRUMBLE,
                            position.x, position.y, position.z,
                            0, 0.2, 0.2, 0.2, 0.1);
                    SL.sendParticles(ModParticles.FIRE_CRUMBLE,
                            position2.x, position2.y, position2.z,
                            0, 0.2, 0.2, 0.2, 0.1);
                    SL.sendParticles(ModParticles.FIRE_CRUMBLE,
                            position3.x, position3.y, position3.z,
                            0, 0.2, 0.2, 0.2, 0.1);


                    SL.sendParticles(ModParticles.DUST_CRUMBLE,
                            position.x, position.y, position.z,
                            0, 0.2, 0.5, 0.2, 0.5);
                    SL.sendParticles(ModParticles.DUST_CRUMBLE,
                            position2.x, position2.y, position2.z,
                            0, 0.2, 0.5, 0.2, 0.2);
                    SL.sendParticles(ModParticles.DUST_CRUMBLE,
                            position3.x, position3.y, position3.z,
                            0, 0.2, 0.5, 0.2, 0.2);

                    this.level().playSound(null, BlockPos.containing(this.position()), ModSounds.VAMPIRE_CRUMBLE_EVENT, SoundSource.PLAYERS, 1.0F, 1F);
                }
            } else {
                if (FateTypes.isVampire(rdbt$this())){
                    if (this.level() instanceof ServerLevel SL){
                        if (MainUtil.isStandDamage($$0)) {
                            Vec3 position = this.getPosition(1);
                            Vec3 position2 = this.getEyePosition();
                            Vec3 position3 = this.getEyePosition().subtract(this.getPosition(1)).multiply(new Vec3(0.5F,
                                    0.5F, 0.5F));
                            position3 = position3.add(this.getPosition(1));
                            SL.sendParticles(ModParticles.SOUL_FIRE_CRUMBLE,
                                    position.x, position.y, position.z,
                                    0, 0.2, 0.2, 0.2, 0.1);
                            SL.sendParticles(ModParticles.SOUL_FIRE_CRUMBLE,
                                    position2.x, position2.y, position2.z,
                                    0, 0.2, 0.2, 0.2, 0.1);
                            SL.sendParticles(ModParticles.SOUL_FIRE_CRUMBLE,
                                    position3.x, position3.y, position3.z,
                                    0, 0.2, 0.2, 0.2, 0.1);

                            SL.sendParticles(ModParticles.DUST_CRUMBLE,
                                    position.x, position.y, position.z,
                                    0, 0.2, 0.5, 0.2, 0.5);
                            SL.sendParticles(ModParticles.DUST_CRUMBLE,
                                    position2.x, position2.y, position2.z,
                                    0, 0.2, 0.5, 0.2, 0.2);
                            SL.sendParticles(ModParticles.DUST_CRUMBLE,
                                    position3.x, position3.y, position3.z,
                                    0, 0.2, 0.5, 0.2, 0.2);
                        }
                    }
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

                if (((LivingEntity)(Object) this) instanceof RoadRollerEntity RRE) {
                    if (damageSource.is(ModDamageTypes.STAND_RUSH)) {
                        RRE.hasBeenBaraged = true;
                    } else {
                        RRE.hasBeenBaraged = false;
                    }
                }

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
                if (living instanceof Player pl && ((IPowersPlayer)pl).rdbt$getPowers().interceptDamageEvent(damageSource, $$1)) {
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


    @Unique
    private int roundabout$anubisAttackDelay = 0;
    @Inject(method = "tick", at = @At(value = "TAIL"), cancellable = true)
    protected void roundabout$tickTail(CallbackInfo ci) {
        ((IEntityAndData)this).roundabout$tickQVec();
        roundabout$tickString();

        if (rdbt$this() instanceof Player && this.roundabout$isPossessed()) {

            PathfinderMob poss = roundabout$getPossessor();
           // Roundabout.LOGGER.info(level.isClientSide() + " | " + this.entityData.get(ROUNDABOUT$POSSESSOR) + " | " + poss);
            if (poss != null) {
             //   Roundabout.LOGGER.info("HO");
                if (poss.getTarget() != null) {
                    float f = (float)Mth.length(poss.getX() - poss.xo, 0.0, poss.getZ() - poss.zo);
                    float g = Math.min(f * 4.0f, 1.0f);
                    this.walkAnimation.update(g, 0.4f);
                }
            }
        }


        if (!level().isClientSide && this.roundabout$getPossessor() instanceof AnubisPossessorEntity APE && APE.getLifeSpan() < PowersAnubis.MaxPossessionTime -2) {
            AnubisPossessorEntity poss = (AnubisPossessorEntity) this.roundabout$getPossessor();
            if (poss != null) {
                if (this.roundabout$getPossessor().isRemoved()) {
                    this.roundabout$setPossessor(null);
                }
            }
            if (this.roundabout$isPossessed()) {
                if (rdbt$this() instanceof Player P) {
                    if (poss != null) {

                        rdbt$this().setDeltaMovement(Vec3.ZERO);
                        Vec3 pos = P.getPosition(0.5F);

                        pos = pos.lerp(poss.getPosition(0.5F),0.5).add(0,0.05,0);
                        rdbt$this().teleportTo(pos.x,pos.y,pos.z);

                        LivingEntity target = poss.getTarget();
                        if (target!= null) {

                            float $$1 = (float)Mth.length(this.getX() - this.xo, this.getY() - this.yo, this.getZ() - this.zo);

                            if (target.hurtTime == 0 && !this.roundabout$isDazed() && roundabout$anubisAttackDelay >= 0) {
                                if (P.getPosition(0.5F).distanceTo(target.getPosition(1)) < 2) {
                                    P.swing(InteractionHand.MAIN_HAND,true);
                                    if (target.hurt(ModDamageTypes.of(P.level(), ModDamageTypes.ANUBIS_POSSESS,this), 7.5F)) {
                                        roundabout$anubisAttackDelay = 12;
                                    } else {
                                        if (target.isBlocking()) {
                                            roundabout$anubisAttackDelay = 40;
                                        }
                                    }
                                    P.crit(target);
                                    Vec3 delta = target.getPosition(1).subtract(P.getPosition(1)).multiply(0.3,0.3,0.3);
                                    target.teleportRelative(0,0.4,0);
                                    target.setDeltaMovement(delta.x*0.4,0.2,delta.z*0.4);
                                }
                            } else if (roundabout$anubisAttackDelay >= 0) {
                                roundabout$anubisAttackDelay--;
                            }
                        }
                    }
                }
            }
        }

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
            if ((((LivingEntity)(Object)this) instanceof Player PE && PE.isCreative())
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

                    if (roundabout$remainingFireTicks % 20 == 0 && !this.isInLava()) {
                        float fireDamage = 1;
                        if (this.roundabout$getStandPowers().getReducedDamage((LivingEntity) (Object) this)) {
                            fireDamage = (float) (fireDamage * (ClientNetworking.getAppropriateConfig().
                                    magiciansRedSettings.standFireOnPlayersMult * 0.01))*0.334F;
                        } else {
                            fireDamage = (float) (fireDamage * (ClientNetworking.getAppropriateConfig().
                                    magiciansRedSettings.standFireOnMobsMult * 0.01))*0.8F;
                        }
                        this.hurt(ModDamageTypes.of(this.level(), ModDamageTypes.STAND_FIRE), fireDamage);
                    }

                    int amtdown = -1;
                    if (this.isInWaterRainOrBubble())
                        amtdown = -10;
                    roundabout$setRemainingStandFireTicks(roundabout$remainingFireTicks + amtdown);

                }
                if (roundabout$remainingFireTicks <= 0) {
                    if (roundabout$getOnStandFire() > 0) {
                        roundabout$setOnStandFire(StandFireType.FIRELESS.id);
                    }
                    if (roundabout$remainingFireTicks == 0 || roundabout$remainingFireTicks < -1) {
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

        /**Makes bleed work for vamps against their hunger*/
        if (!this.level().isClientSide() && rdbt$this() instanceof  Player PE) {
            if (FateTypes.hasBloodHunger(PE)) {
                if (hasEffect(ModEffects.BLEED)) {
                    MobEffectInstance ei = getEffect(ModEffects.BLEED);
                    if (ei != null){
                        PE.causeFoodExhaustion(0.005F * (float) (ei.getAmplifier() + 1));
                    }
                }
            }
        }

        byte curse = this.roundabout$getLocacacaCurse();
        if (curse > -1) {
            if (curse == LocacacaCurseIndex.HEART) {
                if (this.tickCount % 20 == 0) {
                    this.hurt(ModDamageTypes.of(this.level(), ModDamageTypes.HEART), 1.0F);
                }
            }
        }
        /**hey ya fade ticks*/
        boolean active = PowerTypes.hasStandActive(rdbt$this());
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
        if (PowerTypes.hasStandActive(rdbt$this()) && roundabout$getStandPowers() instanceof PowersRatt &&
                !((PowersRatt)roundabout$getStandPowers()).isPlaced()
        ){
            roundabout$setRattShoulderVanishTicks(roundabout$getRattShoulderVanishTicks()+1);
        } else {
            roundabout$setRattShoulderVanishTicks(0);
        }
        if (roundabout$getStandPowers() instanceof PowersAnubis && active){
            roundabout$setAnubisVanishTicks(roundabout$getAnubisVanishTicks()+1);
        } else {
            roundabout$setAnubisVanishTicks(roundabout$getAnubisVanishTicks()-1);
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
            if (air > 0 && PowerTypes.hasStandActive(rdbt$this())) {
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


    @Unique
    public UUID rdbt$fleshBudPlanted = null;
    //Flesh bud stuff

    public void rdbt$setFleshBud(UUID bud){
        rdbt$fleshBudPlanted = bud;
    }

    public UUID rdbt$getFleshBud(){
        return rdbt$fleshBudPlanted;
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
        if (HeatUtil.isLegsFrozen(rdbt$this()))
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
        if (this.roundabout$getStandPowers().cancelSprintJump() || roundabout$cancelsprintJump()
        || (rdbt$this() instanceof Player pl && (((IFatePlayer)pl).rdbt$getFatePowers().cancelSprintJump() ||
                ((IPowersPlayer)pl).rdbt$getPowers().cancelSprintJump()))) {
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
        }

        basis = roundabout$mutualGetSpeed(basis);
        if (!((StandUser) this).roundabout$getStandDisc().isEmpty() &&

                (((LivingEntity)(Object)this) instanceof AbstractVillager AV &&
                AV.getTarget() != null && !(((IMob) this).roundabout$getFightOrFlight()))

        ){
            basis *= 0.5F;
        }

        // Vampire mobs (not players) are faster
        if (FateTypes.isVampire(rdbt$this())){
            basis *= 1.3F;
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

    @Shadow public abstract Collection<MobEffectInstance> getActiveEffects();

    @Shadow public abstract boolean shouldDiscardFriction();

    @Shadow public abstract void calculateEntityAnimation(boolean bl);

    @Shadow public abstract Vec3 handleRelativeFrictionAndCalculateMovement(Vec3 vec3, float f);

    @Shadow public abstract boolean isFallFlying();

    @Shadow protected abstract SoundEvent getFallDamageSound(int i);

    @Shadow protected abstract boolean isAffectedByFluids();

    @Shadow public abstract boolean canStandOnFluid(FluidState fluidState);

    @Shadow public abstract Vec3 getFluidFallingAdjustedMovement(double d, boolean bl, Vec3 vec3);

    @Shadow public abstract float getSpeed();

    @Shadow protected abstract float getWaterSlowDown();

    @Shadow @Nullable protected abstract SoundEvent getHurtSound(DamageSource damageSource);

    @Shadow
    public abstract boolean isDamageSourceBlocked(DamageSource $$0);

    @Shadow
    @Final
    public WalkAnimationState walkAnimation;

    @Shadow public abstract boolean isUsingItem();

    @Shadow public boolean swinging;
    @Shadow public int swingTime;

    @Shadow protected abstract int getCurrentSwingDuration();

    @Shadow public InteractionHand swingingArm;
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
    public void DropExtra(DamageSource $$0, int $$1, boolean $$2,CallbackInfo info){
        Entity cause = $$0.getEntity();
        DamageType type = $$0.type();
        DamageSource uh = ModDamageTypes.of(this.level(), ModDamageTypes.DISINTEGRATION);
        LivingEntity me = (LivingEntity) (Object) this;
        if(type == uh.type() && (Roundabout.RANDOM.nextDouble()>0.8 ||me instanceof ServerPlayer)){
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


        ///  ratt skin unlocking
        if (cause != null) {
            if (this.getEffect(ModEffects.MELTING) != null) {
                if (((StandUserEntity) cause).roundabout$getStandPowers() instanceof PowersRatt PR) {
                    if (PR.isPlaced()) {
                        RattEntity RE = (RattEntity) PR.getStandEntity((LivingEntity) cause);
                        Vec3 vec3 = RE.getPosition(1);
                        for (int x=-1;x<2;x++) {
                            for(int z=-1;z<2;z++) {
                                BlockPos bp = new BlockPos(
                                        (int) vec3.x+x,
                                        (int) vec3.y,
                                        (int) vec3.z+z
                                );
                                BlockState state = cause.level().getBlockState(bp);
                                if (cause.level().getBlockState(bp).is(ModBlocks.WOODEN_MANOR_CHAIR)) {
                                    PR.unlockSkin();
                                }
                            }
                        }
                    }

                }
            }
        }


        /// ratt flesh dropping
        MobEffectInstance melting = this.getEffect(ModEffects.MELTING);
        if (melting != null && MainUtil.getMobBleed(rdbt$this())) {
            int amount =  Mth.clamp(melting.getAmplifier()/2,1,6) + (int) (Math.random() * 1 + 0.5);
            if ( !(this.roundabout$getStandPowers() instanceof PowersRatt) ) {
                if (this.level().getGameRules().getBoolean(ModGamerules.ROUNDABOUT_STAND_GRIEFING)) {
                    FleshPileEntity r = new FleshPileEntity((LivingEntity) ((Object) this), this.level(), amount);
                    r.setPos(this.getPosition(0));
                    this.level().addFreshEntity(r);
                } else {
                    spawnAtLocation(new ItemStack(ModBlocks.FLESH_BLOCK, amount));
                }
            }
        }


    }


    @Unique
    @Override
    public void rdbt$doMoldDetection(Vec3 movement){
        if(((IPermaCasting)this.level()).roundabout$inPermaCastRange(this.getOnPos(), PermanentZoneCastInstance.MOLD_FIELD)) {
            LivingEntity MoldFieldCaster = ((IPermaCasting)this.level()).roundabout$inPermaCastRangeEntity(this.getOnPos(),PermanentZoneCastInstance.MOLD_FIELD);
            if (MoldFieldCaster != null && !(((PowersGreenDay)((StandUser)MoldFieldCaster).roundabout$getStandPowers()).allies.contains(this.getStringUUID()))) {
                boolean isUser = this.equals(MoldFieldCaster);
                boolean down = previousYpos > this.getY();
                boolean isStand = (((LivingEntity) (Object) this) instanceof StandEntity);
                if (!roundabout$getStandPowers().isStoppingTime() && !this.roundabout$isBubbleEncased() && !isUser && !isStand && down && (MoldFieldCaster.getY() > this.getY()) && !isUser && jumpImmunityTicks < 1) {
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


        }
        if (previousYpos < this.getY()){
            jumpImmunityTicks = 6;
        }
        else{
            jumpImmunityTicks = jumpImmunityTicks -1;
        }
        previousYpos = this.getY();
    }
    public int CrawlTicks = 0;

    @Unique
    @Override
    public boolean rdbt$isForceCrawl() {
        return CrawlTicks > 0;
    }

    @Unique
    @Override
    public int rdbt$getCrawlTicks() {
        return CrawlTicks;
    }



    @Inject(method = "travel", at = @At(value = "HEAD"),cancellable = true)
    public void rdbt$crawltick(Vec3 movement, CallbackInfo ci) {
        if (this.rdbt$isForceCrawl()) {
            this.setPose(Pose.SWIMMING);
            this.setSwimming(true);
            CrawlTicks--;
        }

    }


    /**Your stand's generalized cooldowns*/
    @Unique
    public List<CooldownInstance> rdbt$PowerCooldowns = rdbt$initPowerCooldowns();
    public List<CooldownInstance> rdbt$initPowerCooldowns(){
        List<CooldownInstance> Cooldowns = new ArrayList<>();
        if (rdbt$this() instanceof Player){
            for (byte i = 0; i < 30; i++) {
                Cooldowns.add(new CooldownInstance(-1, -1));
            }
        } else {
            for (byte i = 0; i < 10; i++) {
                Cooldowns.add(new CooldownInstance(-1, -1));
            }
        }
        return Cooldowns;
    }

    public List<CooldownInstance> rdbt$getPowerCooldowns(){
        return rdbt$PowerCooldowns;
    }
    public void rdbt$setPowerCooldowns(List<CooldownInstance> cooldownInstances){
        rdbt$PowerCooldowns = cooldownInstances;
    }


    /**If you stand still enough, abilities recharge faster. But this could be overpowered for some abilties, so
     * use discretion and override this to return false on abilities where this might be op.*/
    @Unique
    public boolean rdbt$canUseStillStandingRecharge(byte bt){
        if (!roundabout$getStandPowers().canUseStillStandingRecharge(bt)){
            return false;
        }
        return true;
    }



    /**If the cooldown slot is to be controlled by the server, return true. Consider using this if
     * bad TPS makes a stand ability actually overpowered for the client to handle the recharging of.*/
    @Unique
    @Override
    public boolean rdbt$isServerControlledCooldown(CooldownInstance ci, byte num){
        if (roundabout$getStandPowers().isServerControlledCooldown(ci,num)){
            return true;
        }
        if (rdbt$this() instanceof Player pl && ((IPowersPlayer)pl).rdbt$getPowers().isServerControlledCooldown(ci,num)){
            return true;
        }
        return false;
    }

    @Unique
    public void rdbt$tickCooldowns(){
        try {
            int amt = 1;
            boolean isDrowning = false;

            // Changes how fast the cooldowns should recharge
            if (rdbt$this() instanceof Player) {
                isDrowning = (this.getAirSupply() <= 0);

                int idle = this.roundabout$getIdleTime();
                if (idle > 300) {
                    amt *= 4;
                } else if (idle > 200) {
                    amt *= 3;
                } else if (idle > 40) {
                    amt *= 2;
                }

                if (isDrowning && !ClientNetworking.getAppropriateConfig().generalStandSettings.canRechargeCooldownsWhileDrowning)
                { amt = 0; }
            }

            byte cin = -1;
            for (CooldownInstance ci : rdbt$PowerCooldowns){
                cin++;
                if (ci.time >= 0){
                    if (!rdbt$canUseStillStandingRecharge(cin)){
                        amt = 1;
                    }
                    ci.setFrozen(isDrowning && !ClientNetworking.getAppropriateConfig().generalStandSettings.canRechargeCooldownsWhileDrowning);

                    boolean serverControlledCooldwon = rdbt$isServerControlledCooldown(ci, cin);
                    if (!(this.level().isClientSide() && serverControlledCooldwon)) {

                        if (!ci.isFrozen()) {
                            ci.time -= amt;
                        }

                        if (ci.time < -1) {
                            ci.time = -1;
                        }

                        if (rdbt$this() instanceof Player) {
                            if ((((Player) rdbt$this()).isCreative() &&
                                    ClientNetworking.getAppropriateConfig().generalStandSettings.creativeModeRefreshesCooldowns) && ci.time > 2) {
                                ci.time = 2;
                            }
                        }

                        if (serverControlledCooldwon && !this.level().isClientSide() && rdbt$this() instanceof Player) {
                            List<CooldownInstance> CDCopy = new ArrayList<>(rdbt$PowerCooldowns) {
                            };

                            S2CPacketUtil.sendMaxCooldownSyncPacket(((ServerPlayer) rdbt$this()), cin, ci.time, ci.maxTime);
                        }
                    }
                }
            }
        } catch (Exception e){
            //I very much doubt this will error
            Roundabout.LOGGER.info("???");
        }
    }

    @Unique
    @Override
    public void rdbt$SetCrawlTicks(int ticks) {
        CrawlTicks = ticks;
    }



    @Inject(method = "travel", at = @At(value = "TAIL"),cancellable = true)
    public void   MoldDetection(Vec3 movement,CallbackInfo info) {
        rdbt$doMoldDetection(movement);
    }
}
