package net.hydra.jojomod.mixin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.ILivingEntityAccess;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.entity.projectile.MatchEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.LocacacaCurseIndex;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.*;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
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
    @Shadow public abstract float getMaxHealth();

    @Shadow
    public abstract boolean hurt(DamageSource $$0, float $$1);

    @Shadow
    public abstract void indicateDamage(double $$0, double $$1);

    @Shadow
    public abstract void heal(float $$0);

    @Shadow
    public abstract boolean isAlive();

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
    private final int roundabout$maxLeapTicks = 60;

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
    @Nullable
    @Unique
    private LivingEntity roundabout$thrower;

    @Nullable
    @Unique
    private ImmutableList<StandEntity> roundabout$followers = ImmutableList.of();

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
    private static final EntityDataAccessor<Integer> ROUNDABOUT$BLEED_LEVEL = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Boolean> ROUNDABOUT$ONLY_BLEEDING = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<ItemStack> ROUNDABOUT$STAND_DISC = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.ITEM_STACK);
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
    public final float roundabout$maxGuardPoints = 15F;
    @Unique
    private float roundabout$GuardPoints = roundabout$maxGuardPoints;
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
    private int roundabout$maxGasTicks = 200;
    private int roundabout$maxBucketGasTicks = 600;

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
    public void roundabout$setBlip(Vector3f vec) {
        this.roundabout$blip = true;
        this.roundabout$blipVector = vec;
    }

    @Unique
    @Override
    public LivingEntity roundabout$getThrower() {
        return this.roundabout$thrower;
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
            if (this.activeEffects.size() > 1) {
                Iterator<MobEffect> $$0 = this.activeEffects.keySet().iterator();
                while ($$0.hasNext()) {
                    MobEffect $$1 = $$0.next();
                    MobEffectInstance $$2 = this.activeEffects.get($$1);
                    if ($$2.isVisible() && !$$2.getEffect().equals(ModEffects.BLEED)) {
                        onlyBleeding = false;
                    }
                }
            }
            if (this.roundabout$getOnlyBleeding() != onlyBleeding) {
                this.roundabout$setOnlyBleeding(onlyBleeding);
            }
        }
        if (this.roundabout$getBleedLevel() > -1) {
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
            if (this.roundabout$getOnlyBleeding()) {
                ci.cancel();
            }
        }
    }

    /**When mobs TS teleport, part of canceling visual interpolation between two points so it looks like they
     * just "blip" there*/
    @Unique
    @Override
    public void roundabout$tryBlip() {
        if (roundabout$blip && roundabout$blipVector !=null){
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
        }
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    public void roundabout$endTick(CallbackInfo ci) {
        if (!(((LivingEntity)(Object)this) instanceof Player)) {
            this.roundabout$getStandPowers().tickPowerEnd();
        }
    }
    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void roundabout$tick(CallbackInfo ci) {
        //if (StandID > -1) {
        if (!this.level().isClientSide) {
            if (this.roundabout$getActive() &&this.roundabout$getStandPowers().canSummonStand() && (this.roundabout$getStand() == null ||
                    (this.roundabout$getStand().level().dimensionTypeId() != this.level().dimensionTypeId() &&
                            OffsetIndex.OffsetStyle(this.roundabout$getStand().getOffsetType()) == OffsetIndex.FOLLOW_STYLE))){
                this.roundabout$summonStand(this.level(),true,false);
            }
        }

        this.roundabout$getStandPowers().tickPower();
        this.roundabout$tickGuard();
        this.roundabout$tickDaze();
        if (this.roundabout$leapTicks > -1){
            if (this.onGround() && roundabout$leapTicks < (roundabout$maxLeapTicks - 5)){
                roundabout$leapTicks = -1;
            }
            roundabout$cancelConsumableItem((LivingEntity)(Object)this);
            roundabout$leapTicks--;
            if (!this.level().isClientSide){
                ((ServerLevel) this.level()).sendParticles(new DustParticleOptions(new Vector3f(1f,0.65f,0), 1f), this.getX(), this.getY(), this.getZ(),
                        1, 0, 0, 0, 0.1);
            }
        }
        if (roundabout$sealedTicks > -1){
            roundabout$sealedTicks--;
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
    public void roundabout$addFollower(StandEntity $$0) {
        if (this.roundabout$followers.isEmpty()) {
            this.roundabout$followers = ImmutableList.of($$0);
        } else {
            List<StandEntity> $$1 = Lists.newArrayList(this.roundabout$followers);
            $$1.add($$0);
            this.roundabout$followers = ImmutableList.copyOf($$1);
        }
    }

    @Override
    public void roundabout$removeFollower(StandEntity $$0) {
        if (this.roundabout$followers.size() == 1 && this.roundabout$followers.get(0) == $$0) {
            this.roundabout$followers = ImmutableList.of();
        } else {
            this.roundabout$followers =
                    this.roundabout$followers.stream().filter($$1 -> $$1 != $$0).collect(ImmutableList.toImmutableList());
        }
    }

    @Override
    public final List<StandEntity> roundabout$getFollowers() {
        return this.roundabout$followers;
    }

    @Override
    public boolean roundabout$hasFollower(StandEntity $$0) {
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
    public byte roundabout$standSkin;
    @Unique
    @Override
    public byte roundabout$getStandSkin(){
        return this.roundabout$standSkin;
    }
    @Unique
    @Override
    public void roundabout$setStandSkin(byte skin){
        this.roundabout$standSkin = skin;
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
        return this.roundabout$maxGasTicks;
    }
    @Unique
    public int roundabout$getMaxBucketGasolineTime(){
        return this.roundabout$maxBucketGasTicks;
    }

    @Unique
    public int roundabout$getLeapTicks(){
        return this.roundabout$leapTicks;
    }
    @Unique
    public int roundabout$getMaxLeapTicks(){
        return this.roundabout$maxLeapTicks;
    }
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
        if (((LivingEntity) (Object) this) instanceof Player && !((LivingEntity) (Object) this).level().isClientSide){
            ModPacketHandler.PACKET_ACCESS.sendIntPacket(((ServerPlayer) (Object) this),(byte) 1, gasTicks);
        }
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
        this.roundabout$tsJump = roundaboutTSJump;
        if (((LivingEntity)(Object)this) instanceof Player){
            if (roundaboutTSJump && ((IPlayerEntity) this).roundabout$GetPos() == PlayerPosIndex.NONE) {
                ((IPlayerEntity) this).roundabout$SetPos(PlayerPosIndex.TS_FLOAT);
            } else if (!roundaboutTSJump && ((IPlayerEntity) this).roundabout$GetPos() == PlayerPosIndex.TS_FLOAT){
                ((IPlayerEntity) this).roundabout$SetPos(PlayerPosIndex.NONE);
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
    public void roundabout$setBleedLevel(int bleedLevel) {
        if (!(this.level().isClientSide)) {
            this.getEntityData().set(ROUNDABOUT$BLEED_LEVEL, bleedLevel);
        }
    }
    @Unique
    @Override
    public void roundabout$setOnlyBleeding(boolean only) {
        if (!(this.level().isClientSide)) {
            this.getEntityData().set(ROUNDABOUT$ONLY_BLEEDING, only);
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
            return (float) (living.getMaxHealth() * 0.3);
        } else if (living instanceof Illusioner) {
            return (float) (living.getMaxHealth() * 0.3);
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
        return this.roundabout$maxGuardPoints;
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
            this.roundabout$getStandPowers().animateStand((byte) 15);
        }
        this.roundabout$syncGuard();
    }
    @Unique
    public void roundabout$setGuardBroken(boolean guardBroken){
        this.roundabout$GuardBroken = guardBroken;
        if (!this.level().isClientSide) {
            if (guardBroken && this.roundabout$getStandPowers().isGuarding()){
                this.roundabout$getStandPowers().animateStand((byte) 15);
            } else if (!guardBroken && this.roundabout$getStandPowers().isGuarding()){
                this.roundabout$getStandPowers().animateStand((byte) 15);
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
        this.roundabout$GuardPoints = this.roundabout$maxGuardPoints;
        this.roundabout$GuardBroken = false;
        if (!this.level().isClientSide && this.roundabout$getStandPowers().isGuarding()) {
            this.roundabout$getStandPowers().animateStand((byte) 10);
        }
        this.roundabout$syncGuard();
    }
    @Unique
    public void roundabout$regenGuard(float regen){
        float finalGuard = this.roundabout$GuardPoints + regen;
        if (finalGuard >= this.roundabout$maxGuardPoints){
            this.roundabout$fixGuard();
        } else {
            this.roundabout$GuardPoints = finalGuard;
            this.roundabout$syncGuard();
        }
    }
    @Unique
    public void roundabout$tickGuard(){
        if (this.roundabout$GuardPoints < this.roundabout$maxGuardPoints) {
            if (this.roundabout$GuardBroken){
                float guardRegen = roundabout$maxGuardPoints / 100;
                this.roundabout$regenGuard(guardRegen);
            } else if (!this.roundabout$isGuarding() && this.roundabout$shieldNotDisabled()){
                float guardRegen = roundabout$maxGuardPoints / 200;
                this.roundabout$regenGuard(guardRegen);
            }
            if (this.roundabout$isGuarding() && !roundabout$shieldNotDisabled()){
                this.roundabout$setAttackTimeDuring(0);
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
                    this.roundabout$getStandPowers().animateStand((byte) 0);
                    this.roundabout$syncDaze();
                }
            }
        }
    }

    @Unique
    public void roundabout$syncGuard(){
        if (((LivingEntity) (Object) this) instanceof Player && !((LivingEntity) (Object) this).level().isClientSide){
            ModPacketHandler.PACKET_ACCESS.StandGuardPointPacket(((ServerPlayer) (Object) this),this.roundabout$getGuardPoints(),this.roundabout$getGuardBroken());
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
            ModPacketHandler.PACKET_ACCESS.DazeTimePacket(((ServerPlayer) (Object) this),this.roundabout$dazeTime);
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
            this.roundabout$getStandPowers().syncCooldowns();
            if (this.level().isClientSide) {
                this.roundabout$getStandPowers().kickStarted = false;
            }
        }
    }
    public void roundabout$tryChargedPower(int move, boolean forced, int chargeTime){
         this.roundabout$getStandPowers().tryChargedPower(move, forced, chargeTime);
        this.roundabout$getStandPowers().syncCooldowns();
        if (this.level().isClientSide) {
            this.roundabout$getStandPowers().kickStarted = false;
        }
    }
    public void roundabout$tryPosPower(int move, boolean forced, BlockPos blockPos){
        this.roundabout$getStandPowers().tryPosPower(move, forced, blockPos);
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
        return this.roundabout$getStandPowers().getStandReach();
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
        return (this.roundabout$shieldNotDisabled() && this.roundabout$getStandPowers().isGuarding() && this.roundabout$getStandPowers().getAttackTimeDuring() >= 3);
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
            if (!ItemStack.matches(roundabout$itemParityClient, this.roundabout$getStandDisc())){
                roundabout$itemParityClient = this.roundabout$getStandDisc();
                if (this.roundabout$getStandDisc().getItem() instanceof StandDiscItem SE){
                    SE.generateStandPowers((LivingEntity)(Object)this);
                } else {
                    this.roundabout$setStandPowers(null);
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
        roundabout$sealedTicks = ticks;
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
                    Vec3 spos = stand.getStandOffsetVector(roundabout$User);
                    stand.absMoveTo(spos.x(), spos.y(), spos.z());

                    stand.setSkin(roundabout$getStandSkin());

                    theWorld.addFreshEntity(stand);

                    if (sound && !((TimeStop)this.level()).CanTimeStopEntity(this)) {
                        thispowers.playSummonSound();
                    }

                    this.roundabout$standMount(stand);
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
     * @see StandEntity#setMoveForward */
    public void roundabout$setDI(byte forward, byte strafe){
        //RoundaboutMod.LOGGER.info("MF:"+ forward);
        if (roundabout$Stand != null){
            if (!roundabout$User.isShiftKeyDown() && roundabout$User.isSprinting()){
                forward*=2;}
            roundabout$Stand.setMoveForward(forward);
        }
    }

    /** Retooled vanilla riding code to update the location of a stand every tick relative to the entity it
     * is the user of.
     * @see StandEntity#getAnchorPlace */
    public void roundabout$updateStandOutPosition(StandEntity stand) {
        this.roundabout$updateStandOutPosition(stand, Entity::setPos);
    }

    public void roundabout$updateStandOutPosition(StandEntity stand, Entity.MoveFunction positionUpdater) {
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
                    } else if (OT == OffsetIndex.GUARD_AND_TRACE){
                        BlockHitResult dd = roundabout$getStandPowers().getAheadVec(30);
                        rot = (roundabout$User.getYHeadRot()) % 360;
                        stand.setXRot(roundabout$getStandPowers().getLookAtPlacePitch(stand,dd.getBlockPos().getCenter()));
                    } else {
                        rot = (float) ((roundabout$User.getYHeadRot() -(stand.getPunchYaw(stand.getAnchorPlace(),
                                0.36))) % 360);
                    }
                    stand.setYRot(rot);
                    stand.setYBodyRot(rot);
                }
            }
        } else {
            positionUpdater.accept(stand, stand.getX(), stand.getY(), stand.getZ());
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
        ((LivingEntity)(Object)this).getEntityData().define(ROUNDABOUT$STAND_ID, -1);
        ((LivingEntity)(Object)this).getEntityData().define(ROUNDABOUT_TS_DAMAGE, (byte) 0);
        ((LivingEntity)(Object)this).getEntityData().define(ROUNDABOUT$LOCACACA_CURSE, (byte) -1);
        ((LivingEntity)(Object)this).getEntityData().define(ROUNDABOUT$BLEED_LEVEL, -1);
        ((LivingEntity)(Object)this).getEntityData().define(ROUNDABOUT$ONLY_BLEEDING, true);
        ((LivingEntity)(Object)this).getEntityData().define(ROUNDABOUT$STAND_DISC, ItemStack.EMPTY);
        ((LivingEntity)(Object)this).getEntityData().define(ROUNDABOUT$STAND_ACTIVE, false);
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
        if ($$0.is(ModDamageTypes.STAND)) {
            ci.setReturnValue($$1);
        }
    }

    /**Here, we cancel barrage if it has not "wound up" and the user is hit*/
    @Inject(method = "hurt", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$RoundaboutDamage(DamageSource $$0, float $$1, CallbackInfoReturnable<Boolean> ci) {

        if ($$0.is(ModDamageTypes.GASOLINE_EXPLOSION)){
            if (roundabout$gasolineIFRAMES > 0){
                ci.setReturnValue(false);
                return;
            }
        }

        if (this.roundabout$gasTicks > -1) {
            if ($$0.is(DamageTypeTags.IS_FIRE) || ($$0.getDirectEntity() instanceof Projectile && $$0.getDirectEntity().isOnFire())) {
                float power = Roundabout.gasDamage*18;
                if ($$0.is(DamageTypeTags.IS_FIRE)) {
                    if ($$0.getDirectEntity() instanceof Projectile) {
                        if ($$0.getDirectEntity() instanceof MatchEntity){
                            if (((MatchEntity) $$0.getDirectEntity()).isBundle){
                                power = Roundabout.gasDamage*20;
                            }
                        }
                    } else {
                        power = Roundabout.gasDamage*14;
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
            }
        }
        if ($$0.getDirectEntity() != null) {
            if (this.roundabout$isBarraging()) {
                if (((LivingEntity)(Object)this) instanceof Player){
                    this.roundabout$tryPower(PowerIndex.GUARD, true);
                } else {
                    this.roundabout$tryPower(PowerIndex.NONE, true);
                }
            } else if (this.roundabout$getStandPowers().canInterruptPower()) {
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
        if (this.hasEffect(ModEffects.HEX)) {
            int hexLevel = this.getEffect(ModEffects.HEX).getAmplifier();
            if ((hexLevel >= 0 && $$0.is(Items.ENCHANTED_GOLDEN_APPLE)) || (hexLevel >= 1 && $$0.is(Items.GOLDEN_APPLE))
                    || hexLevel >= 2){
                ci.cancel();
            }
        }
    }

    /**Prevent you from hearing every hit in a rush*/
    @Inject(method = "playHurtSound", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$PlayHurtSound(DamageSource $$0, CallbackInfo ci) {
        if (this.roundabout$isDazed()) {
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

    @Shadow
    protected float getDamageAfterArmorAbsorb(DamageSource $$0, float $$1){
        return 0;
    }

    @Shadow
    protected float getDamageAfterMagicAbsorb(DamageSource $$0, float $$1) {
        return 0;
    }

    /**Part of Registering Stand Guarding as a form of Blocking*/
    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurtCurrentlyUsedShield(F)V", shift = At.Shift.BEFORE))
    private void roundabout$RoundaboutDamage2(DamageSource source, float amount, CallbackInfoReturnable<Boolean> ci) {
        if (this.roundabout$isGuarding()) {
            if (!source.is(DamageTypeTags.BYPASSES_COOLDOWN) && this.roundabout$getGuardCooldown() > 0) {
                return;
            }

            this.roundabout$damageGuard(amount);
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
        }
        return $$1;
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

        if (this.roundabout$leapTicks > -1){
            ((LivingEntity) (Object) this).resetFallDistance();
        }
        return $$1;
    }

    /**This code prevents you from swimming upwards while barrage clashing*/
    @Inject(method = "jumpInLiquid", at = @At(value = "HEAD"), cancellable = true)
    protected void rooundabout$swimUpward(TagKey<Fluid> $$0, CallbackInfo ci) {
        if (this.roundabout$isClashing()) {
            ci.cancel();
        }
    }

    /**Villager call to action*/
    @Inject(method = "actuallyHurt", at = @At(value = "HEAD"), cancellable = true)
    protected void rooundabout$actuallyHurt(DamageSource $$0, float $$1, CallbackInfo ci) {
        if (((LivingEntity)(Object)this) instanceof AbstractVillager AV && !($$0.getEntity()
                instanceof AbstractVillager) && $$0.getEntity() instanceof LivingEntity LE) {
            if (!this.isInvulnerableTo($$0)) {
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
        }
    }

    @Inject(method = "checkFallDamage", at = @At(value = "HEAD"), cancellable = true)
    protected void rooundabout$checkFallDamage(double $$0, boolean $$1, BlockState $$2, BlockPos $$3, CallbackInfo ci) {
        if (this.roundabout$leapTicks > -1) {
            this.level().gameEvent(GameEvent.HIT_GROUND, this.getPosition(0F),
                    GameEvent.Context.of(this, this.mainSupportingBlockPos.map(blockPos -> this.level().getBlockState((BlockPos)blockPos)).orElse(this.level().getBlockState($$3))));
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
    }
    @SuppressWarnings("deprecation")
    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    protected void roundabout$hurt(DamageSource $$0, float $$1, CallbackInfoReturnable<Boolean> ci){

        if (roundabout$gasolineIFRAMES > 0 && $$0.is(ModDamageTypes.GASOLINE_EXPLOSION)){
            ci.setReturnValue(false);
            return;
        } else {
            if ($$0.is(ModDamageTypes.GASOLINE_EXPLOSION)) {
                roundabout$gasolineIFRAMES = 10;
                roundabout$knifeIFrameTicks = 10;
                roundabout$stackedKnivesAndMatches = 12;
            }
        }

        if (this.getVehicle() != null && this.getVehicle() instanceof StandEntity SE){
            if (SE.dismountOnHit() && ($$0.getDirectEntity() != null || $$0.is(DamageTypes.IN_WALL))) {
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
                        qVec2 = new Vec3(vec3d3.x,vec3d3.y,vec3d3.z);
                        this.dismountTo(vec3d3.x,vec3d3.y,vec3d3.z);
                    } else {
                        qVec2 = new Vec3(this.getX(),this.getY(),this.getZ());
                        this.dismountTo(SE.getUser().getX(), SE.getUser().getY(), SE.getUser().getZ());
                    }

                    if (((Entity)(Object)this) instanceof Player){
                        ((IEntityAndData)this).roundabout$setQVec2Params(qVec2);
                    }

                }
                if ($$0.is(DamageTypes.IN_WALL)) {
                    ci.setReturnValue(false);
                }
            }
        }

        LivingEntity entity = ((LivingEntity)(Object) this);
        if (entity.level().isClientSide){
            ci.setReturnValue(false);
            return;
        }
        if (((TimeStop)entity.level()).CanTimeStopEntity(entity)){
            if (this.roundabout$TSHurtTime <= 0 || $$0.is(DamageTypeTags.BYPASSES_COOLDOWN)) {

                float dmg = roundabout$getStoredDamage();
                float max = roundaboutGetMaxStoredDamage();


                if (((LivingEntity)(Object) this).isInvulnerableTo($$0)) {
                    ci.setReturnValue(false);
                } else if (((LivingEntity)(Object) this).isDeadOrDying()) {
                    ci.setReturnValue(false);
                } else if ($$0.is(DamageTypeTags.IS_FIRE) && ((LivingEntity)(Object) this).hasEffect(MobEffects.FIRE_RESISTANCE)) {
                    ci.setReturnValue(false);
                }
                $$1 = getDamageAfterArmorAbsorb($$0, $$1);
                $$1 = getDamageAfterMagicAbsorb($$0, $$1);

                if (roundaboutTSHurtSound < 1){
                    roundaboutTSHurtSound = 1;
                } if (roundaboutTSHurtSound < 2 && ($$0.is(ModDamageTypes.STAND) || $$0.is(ModDamageTypes.PENETRATING_STAND))){
                    roundaboutTSHurtSound = 2;
                }
                if (MainUtil.isStandDamage($$0) && $$0.getEntity() instanceof LivingEntity LE && ((StandUser)LE).roundabout$getStandPowers().fullTSChargeBonus()){
                    if (!this.level().isClientSide()){
                        ((ServerLevel) this.level()).sendParticles(new DustParticleOptions(new Vector3f(0.74F,0.73F,0.98F), 1f), this.getX(), this.getY()+this.getEyeHeight(), this.getZ(),
                                1, 0.3, 0.3, 0.3, 0.3);
                    }
                } else {
                    $$1 *= 0.66F;
                }
                if ((dmg + $$1) > max) {
                    roundabout$setStoredDamage(max);
                } else {
                    roundabout$setStoredDamage((dmg + $$1));
                }
                if ($$0 != null && $$0.getEntity() != null) {
                    if ($$0.getEntity() instanceof LivingEntity){
                        ((StandUser)$$0.getEntity()).roundabout$getStandPowers().hasActedInTS = true;
                    }
                    roundaboutSetStoredAttacker($$0.getEntity());
                } else {
                    roundaboutSetStoredAttacker(null);
                }
                this.roundabout$TSHurtTime = 7;
                Entity $$8 = $$0.getEntity();
                if ($$8 != null && !$$0.is(DamageTypeTags.IS_EXPLOSION)) {
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
            /*This extra check ensures that extra damage will not be dealt if a projectile ticks before the TS damage catch-up*/
            if (roundabout$getStoredDamage() > 0 && !$$0.is(ModDamageTypes.TIME)) {
                ci.setReturnValue(false);
                return;
            } if ($$0.is(ModDamageTypes.TIME)){
                roundabout$postTSHurtTime = 17;
            } else {
                /*Knife and match code*/
                if (roundabout$postTSHurtTime > 0 || roundabout$extraIFrames > 0) {
                    ci.setReturnValue(false);
                    return;
                } else {
                    if ($$0.is(ModDamageTypes.KNIFE) || $$0.is(ModDamageTypes.MATCH)) {
                        if ($$0.is(ModDamageTypes.KNIFE)){
                            roundabout$gasolineIFRAMES = 10;
                        }
                        if (roundabout$stackedKnivesAndMatches < 12) {
                            if (roundabout$stackedKnivesAndMatches <= 0) {
                                roundabout$knifeIFrameTicks = 9;
                                roundabout$knifeDespawnTicks = 300;
                            }
                            roundabout$stackedKnivesAndMatches++;
                            if (roundabout$stackedKnivesAndMatches >= 12) {
                                roundabout$extraIFrames = 8;
                            }
                            if ($$0.is(ModDamageTypes.KNIFE) && entity instanceof Player) {
                                ((IPlayerEntity) entity).roundabout$addKnife();
                            }
                        } else {
                            roundabout$gasolineIFRAMES = 10;
                            ci.setReturnValue(false);
                            return;
                        }
                    }
                }
            }
        }

        if (!((TimeStop)entity.level()).getTimeStoppingEntities().isEmpty()
                && ((TimeStop)entity.level()).getTimeStoppingEntities().contains(entity) &&
                ($$0.is(DamageTypes.ON_FIRE) || $$0.is(DamageTypes.IN_FIRE))){
            ci.setReturnValue(false);
        }
    }


    @Inject(method = "tick", at = @At(value = "TAIL"), cancellable = true)
    protected void roundabout$tickTail(CallbackInfo ci) {
        ((IEntityAndData)this).roundabout$tickQVec();
    }

    @Shadow
    protected int decreaseAirSupply(int $$0) {
        return 0;
    }

    @Shadow protected abstract float getEyeHeight(Pose $$0, EntityDimensions $$1);

    @Inject(method = "baseTick", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$BreathingCancel(CallbackInfo ci){
        if (!Roundabout.canBreathInTS) {
            if (!((TimeStop) this.level()).getTimeStoppingEntities().isEmpty()
                    && ((TimeStop) this.level()).getTimeStoppingEntities().contains(((LivingEntity) (Object) this))) {
                ((IEntityAndData) this).roundabout$setRoundaboutJamBreath(true);
            }
        }
    }

    @Inject(method = "baseTick", at = @At(value = "TAIL"), cancellable = true)
    protected void roundabout$BreathingCancel2(CallbackInfo ci){
        if (!Roundabout.canBreathInTS) {
            if (((IEntityAndData) this).roundabout$getRoundaboutJamBreath()) {
                ((IEntityAndData) this).roundabout$setRoundaboutJamBreath(false);
            }
        }
    }

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
            byte curse = this.roundabout$getLocacacaCurse();
            if (curse > -1) {
                if (curse == LocacacaCurseIndex.CHEST) {
                    int $$1 = EnchantmentHelper.getRespiration(((LivingEntity) (Object) this));
                    $$1 = $$1 > 0 && this.random.nextInt($$1 + 1) > 0 ? $$0 : $$0 - 4;
                    if ($$1 < -20) {
                        $$1 = -20;
                    }
                    cir.setReturnValue($$1);
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

    /**Use this code to eliminate the sprint jump during certain actions*/
    @Inject(method = "jumpFromGround", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$jumpFromGround(CallbackInfo ci) {
        byte curse = this.roundabout$getLocacacaCurse();
        if (this.roundabout$getStandPowers().cancelSprintJump() || (curse > -1 && (curse == LocacacaCurseIndex.RIGHT_LEG || curse == LocacacaCurseIndex.LEFT_LEG))) {
            Vec3 $$0 = this.getDeltaMovement();
            this.setDeltaMovement($$0.x, (double) this.getJumpPower(), $$0.z);
            this.hasImpulse = true;
            ci.cancel();
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
        byte curse = this.roundabout$getLocacacaCurse();
        if (curse > -1) {
            if (curse == LocacacaCurseIndex.RIGHT_LEG || curse == LocacacaCurseIndex.LEFT_LEG) {
                basis = (basis * 0.82F);
            } else if (curse == LocacacaCurseIndex.CHEST) {
                basis = (basis * 0.85F);
            }
        }
        if (!((StandUser) this).roundabout$getStandDisc().isEmpty() &&
                ((LivingEntity)(Object)this) instanceof AbstractVillager AV &&
                AV.getTarget() != null && !(((IMob) this).roundabout$getFightOrFlight())){
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
}
