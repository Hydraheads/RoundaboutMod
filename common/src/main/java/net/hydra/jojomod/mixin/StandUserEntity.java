package net.hydra.jojomod.mixin;

import com.google.common.collect.Maps;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.MatchEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.index.LocacacaCurseIndex;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.event.powers.stand.PowersTheWorld;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
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

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class StandUserEntity extends Entity implements StandUser {
    @Shadow @javax.annotation.Nullable public abstract MobEffectInstance getEffect(MobEffect $$0);

    @Shadow protected boolean jumping;

    @Shadow private float speed;
    @Unique
    private int roundabout$leapTicks = -1;
    @Unique
    private final int roundabout$maxLeapTicks = 60;

    public StandUserEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Shadow protected abstract int increaseAirSupply(int $$0);
    @Shadow
    @Final
    private Map<MobEffect, MobEffectInstance> activeEffects;

    /**If you are stand guarding, this controls you blocking enemy atttacks.
     * For the damage against stand guard, and sfx, see PlayerEntity mixin
     * damageShield
     */
    @Shadow
    public boolean hasEffect(MobEffect $$0) {
        return this.activeEffects.containsKey($$0);
    }

    @Unique
    private final LivingEntity User = ((LivingEntity)(Object) this);
    @Nullable
    @Unique
    private StandEntity Stand;

    /** StandID is used clientside only*/

    @Unique
    private static final EntityDataAccessor<Integer> STAND_ID = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Boolean> STAND_ACTIVE = SynchedEntityData.defineId(LivingEntity.class,
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
    private StandPowers roundabout$Powers;

    /** Guard variables for stand blocking**/
    @Unique
    public final float maxGuardPoints = 15F;
    @Unique
    private float GuardPoints = maxGuardPoints;
    @Unique
    private boolean GuardBroken = false;
    @Unique
    private int GuardCooldown = 0;


    @Unique
    private int roundabout$gasTicks = -1;
    @Unique
    private int roundabout$gasRenderTicks = -1;
    private int roundabout$maxGasTicks = 200;
    private int roundabout$maxBucketGasTicks = 600;

    /**Idle time is how long you are standing still without using skills, eating, or */
    @Unique
    private int roundaboutIdleTime = -1;

    @Unique
    @Override
    public int getRoundaboutIdleTime(){
        return this.roundaboutIdleTime;
    }
    @Unique
    @Override
    public void setRoundaboutIdleTime(int roundaboutIdleTime){
        this.roundaboutIdleTime = roundaboutIdleTime;
    }


    /** These variables control if someone is dazed, stunned, frozen, or controlled.**/

    /* dazeTime: how many ticks left of daze. Inflicted by stand barrage,
     * daze lets you scroll items and look around, but it takes away
     * your movement, item usage, and stand ability usage. You also
     * have no gravity while dazed**/

    @Unique
    private byte dazeTime = 0;

    @Unique
    private int roundaboutTSHurtTime = 0;
    @Unique
    private int roundabout$postTSHurtTime = 0;
    @Unique
    private int roundabout$gasolineIFRAMES = 0;

    /**Tick thru effects for bleed to not show potion swirls*/
    @Inject(method = "tickEffects", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/syncher/SynchedEntityData;get(Lnet/minecraft/network/syncher/EntityDataAccessor;)Ljava/lang/Object;",
    shift= At.Shift.AFTER,ordinal = 0),  cancellable = true)
    public void roundabout$tickEffects(CallbackInfo ci) {
        if (!this.level().isClientSide){
            int bleedlvl = -1;
            if (this.hasEffect(ModEffects.BLEED)){
                bleedlvl = this.getEffect(ModEffects.BLEED).getAmplifier();
            }
            if (this.roundabout$getBleedLevel() != bleedlvl){
                this.roundabout$setBleedLevel(bleedlvl);
            }

            boolean onlyBleeding = true;
            if (this.activeEffects.size() > 1){
                onlyBleeding = false;
            }
            if (this.roundabout$getOnlyBleeding() != onlyBleeding){
                this.roundabout$setOnlyBleeding(onlyBleeding);
            }
        }
        if (this.roundabout$getBleedLevel() > -1){
            int bleedlvl = this.roundabout$getBleedLevel();
            int bloodticks = 8;
            if (bleedlvl == 1){
                bloodticks = 6;
            } else if (bleedlvl > 1){
                bloodticks = 4;
            }
            if (this.tickCount % bloodticks == 0) {
                this.level()
                        .addParticle(
                                new BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState()),
                                this.getRandomX(0.5),
                                this.getRandomY(),
                                this.getRandomZ(0.5),
                                0,
                                0,
                                0
                        );
            }
            if (this.roundabout$getOnlyBleeding()){
                ci.cancel();
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void tickRoundabout(CallbackInfo ci) {
        //if (StandID > -1) {
        this.getStandPowers().tickPower();
        this.tickGuard();
        this.tickDaze();
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
        if (roundabout$gasolineIFRAMES > 0){
            roundabout$gasolineIFRAMES--;
            if (roundabout$gasolineIFRAMES==0){
                roundabout$gasolineIFRAMES = 0;
            }
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
    boolean roundaboutTSJump = false;
    @Unique
    public boolean roundaboutGetTSJump(){
        return this.roundaboutTSJump;
    }
    @Unique
    public void roundaboutSetTSJump(boolean roundaboutTSJump){
        this.roundaboutTSJump = roundaboutTSJump;
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
    float roundaboutStoredDamage = 0;
    @Unique
    Entity roundaboutStoredAttacker;
    @Unique
    public float roundaboutGetStoredDamage(){
        return this.roundaboutStoredDamage;
    }
    @Unique
    public byte roundaboutGetStoredDamageByte(){
        return this.getEntityData().get(ROUNDABOUT_TS_DAMAGE);
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
        return this.getEntityData().get(ROUNDABOUT$LOCACACA_CURSE);
    }
    @Unique
    @Override
    public int roundabout$getBleedLevel() {
        return this.getEntityData().get(ROUNDABOUT$BLEED_LEVEL);
    }
    @Unique
    @Override
    public boolean roundabout$getOnlyBleeding() {
        return this.getEntityData().get(ROUNDABOUT$ONLY_BLEEDING);
    }
    @Unique
    public void roundaboutSetStoredDamage(float roundaboutStoredDamage){
        if (!((LivingEntity) (Object) this).level().isClientSide) {
            this.roundaboutStoredDamage = roundaboutStoredDamage;
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
        return this.roundaboutStoredAttacker;
    }
    @Unique
    public void roundaboutSetStoredAttacker(Entity roundaboutStoredAttacker){
        this.roundaboutStoredAttacker = roundaboutStoredAttacker;
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


    @ModifyVariable(method = "addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", at = @At(value = "HEAD"), ordinal = 0)
    public CompoundTag roundabout$addAdditionalSaveData(CompoundTag $$0){
        return $$0;
    }
    @Inject(method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", at = @At(value = "HEAD"))
    public void roundabout$readAdditionalSaveData(CompoundTag $$0, CallbackInfo ci){
    }

    /**returns if the mob has a stand. For now, returns if stand is active, but in the future will be more
     * complicated**/
    public boolean isStandUser(){
        return this.getActive();
    }

    public boolean isDazed(){
        return this.dazeTime > 0;
    }
    public void setDazeTime(byte dazeTime){
        this.dazeTime = dazeTime;
    }
    public void setDazed(byte dazeTime){
        this.dazeTime = dazeTime;
        this.syncDaze();
    }

    public boolean getActive() {
        return ((LivingEntity) (Object) this).getEntityData().get(STAND_ACTIVE);
    }
    /**If the player currently is stand attacking vs using items*/
    public boolean getMainhandOverride() {
        return this.getActive();
    }
    public float getMaxGuardPoints(){
        return this.maxGuardPoints;
    }
    public float getGuardCooldown(){
        return this.GuardCooldown;
    }
    public float getGuardPoints(){
        return this.GuardPoints;
    } public void setGuardPoints(float GuardPoints){
        this.GuardPoints = GuardPoints;
    }
    public boolean getGuardBroken(){
        return this.GuardBroken;
    }
    public void breakGuard() {
        this.GuardBroken = true;
        if (!this.level().isClientSide && this.getStandPowers().isGuarding()) {
            this.getStandPowers().animateStand((byte) 15);
        }
        this.syncGuard();
    }
    public void setGuardBroken(boolean guardBroken){
        this.GuardBroken = guardBroken;
        if (!this.level().isClientSide) {
            if (guardBroken && this.getStandPowers().isGuarding()){
                this.getStandPowers().animateStand((byte) 15);
            } else if (!guardBroken && this.getStandPowers().isGuarding()){
                this.getStandPowers().animateStand((byte) 15);
            }
        }
    }
    public void damageGuard(float damage){
        float finalGuard = this.GuardPoints - damage;
        this.GuardCooldown = 10;
        if (finalGuard <= 0){
            this.GuardPoints = 0;
            this.breakGuard();
            this.syncGuard();
        } else {
            this.GuardPoints = finalGuard;
            this.syncGuard();
        }
    } public void fixGuard() {
        this.GuardPoints = this.maxGuardPoints;
        this.GuardBroken = false;
        if (!this.level().isClientSide && this.getStandPowers().isGuarding()) {
            this.getStandPowers().animateStand((byte) 10);
        }
        this.syncGuard();
    } public void regenGuard(float regen){
        float finalGuard = this.GuardPoints + regen;
        if (finalGuard >= this.maxGuardPoints){
            this.fixGuard();
        } else {
            this.GuardPoints = finalGuard;
            this.syncGuard();
        }
    } public void tickGuard(){
        if (this.GuardPoints < this.maxGuardPoints) {
            if (this.GuardBroken){
                float guardRegen = maxGuardPoints / 100;
                this.regenGuard(guardRegen);
            } else if (!this.isGuarding() && this.shieldNotDisabled()){
                float guardRegen = maxGuardPoints / 200;
                this.regenGuard(guardRegen);
            }
            if (this.isGuarding() && !shieldNotDisabled()){
                this.setAttackTimeDuring(0);
            }
        }
        if (this.GuardCooldown > 0){this.GuardCooldown--;}
    } public void tickDaze(){
        if (!this.User.level().isClientSide) {
            if (this.dazeTime > 0) {
                ((LivingEntity)(Object)this).stopUsingItem();
                dazeTime--;
                if (dazeTime <= 0){
                    this.getStandPowers().animateStand((byte) 0);
                    this.syncDaze();
                }
            }
        }
    }

    public void syncGuard(){
        if (((LivingEntity) (Object) this) instanceof Player && !((LivingEntity) (Object) this).level().isClientSide){
            ModPacketHandler.PACKET_ACCESS.StandGuardPointPacket(((ServerPlayer) (Object) this),this.getGuardPoints(),this.getGuardBroken());
        }
    }

    public void syncDaze(){
        if (((LivingEntity) (Object) this) instanceof Player && !((LivingEntity) (Object) this).level().isClientSide){
            ModPacketHandler.PACKET_ACCESS.DazeTimePacket(((ServerPlayer) (Object) this),this.dazeTime);
        }
    }

    public float getRayDistance(Entity entity, float range){
        return this.getStandPowers().getRayDistance(entity,range);
    }

    public void tryPower(int move, boolean forced){
        if (!this.isClashing() || move == PowerIndex.CLASH_CANCEL) {
            this.getStandPowers().tryPower(move, forced);
            this.getStandPowers().syncCooldowns();
        }
    }
    public void tryChargedPower(int move, boolean forced, int chargeTime){
            if (this.getStandPowers().tryChargedPower(move, forced, chargeTime)) {
                this.getStandPowers().syncCooldowns();
            }
    }



    public boolean canAttack(){
        return this.getStandPowers().canAttack();
    }
    public int getSummonCD2(){
        return this.getStandPowers().getSummonCD2();
    }
    public Entity getTargetEntity(Entity User, float distMax){
        return this.getStandPowers().getTargetEntity(User, distMax);
    }
    public boolean getSummonCD(){
        return this.getStandPowers().getSummonCD();
    } public void setSummonCD(int summonCD){
        this.getStandPowers().setSummonCD(summonCD);
    }
    public byte getActivePower(){
        return this.getStandPowers().getActivePower();
    }
    public LivingEntity getPowerUser(){
        return this.getStandPowers().getSelf();
    }
    public int getAttackTimeMax(){
        return this.getStandPowers().getAttackTimeMax();
    }
    public int getAttackTime(){
        return this.getStandPowers().getAttackTime();
    }
    public int getAttackTimeDuring(){
        return this.getStandPowers().getAttackTimeDuring();
    }
    public boolean getInterruptCD(){
        return this.getStandPowers().getInterruptCD();
    }
    public byte getActivePowerPhase(){
        return this.getStandPowers().getActivePowerPhase();
    }public byte getActivePowerPhaseMax(){
        return this.getStandPowers().getActivePowerPhaseMax();
    }
    public float getStandReach(){
        return this.getStandPowers().getStandReach();
    }
    public boolean isGuarding(){
        return this.getStandPowers().isGuarding();
    }
    public boolean isBarraging(){
        return this.getStandPowers().isBarraging();
    }
    public boolean isClashing(){
        return this.getStandPowers().isClashing();
    }
    public boolean isGuardingEffectively(){
        if (this.GuardBroken){return false;}
        return this.isGuardingEffectively2();
    }
    public boolean isGuardingEffectively2(){
        return (this.shieldNotDisabled() && this.getStandPowers().isGuarding() && this.getStandPowers().getAttackTimeDuring() >= 3);
    }

    public boolean shieldNotDisabled(){
        return !(this.User instanceof Player) || !(((Player) this.User).getCooldowns().getCooldownPercent(Items.SHIELD, 0f) > 0);

    }
    public float getDistanceOut(Entity entity, float range, boolean offset){
        return this.getStandPowers().getDistanceOut(entity,range,offset);
    }
    public void setAttackTimeDuring(int attackTimeDuring){
        this.getStandPowers().setAttackTimeDuring(attackTimeDuring);
    } public void setInterruptCD(int interruptCD){
        this.getStandPowers().setInterruptCD(interruptCD);
    }

    public StandPowers getStandPowers() {
        if (this.roundabout$Powers == null) {
            this.roundabout$Powers = new PowersTheWorld(User);
        }
        return this.roundabout$Powers;
    }

    public void setStandPowers(StandPowers standPowers){
        this.roundabout$Powers = standPowers;
    }


    /** Turns your stand "on". This updates the HUD, and is necessary in case the stand doesn't have a body.*/
    public void setActive(boolean active){
        ((LivingEntity) (Object) this).getEntityData().set(STAND_ACTIVE, active);
    }

    /** Sets a stand to a user, and a user to a stand.*/
    public void standMount(StandEntity StandSet){
        this.setStand(StandSet);
        StandSet.setMaster(User);
    }

    /**Only sets a user's stand. Distinction may be important depending on when it is called.*/
    public void setStand(StandEntity StandSet){
        this.Stand = StandSet;
        ((LivingEntity) (Object) this).getEntityData().set(STAND_ID, StandSet.getId());
    }


    /** Code that brings out a user's stand, based on the stand's summon sounds and conditions. */
    public void summonStand(Level theWorld, boolean forced, boolean sound){
        boolean active;
        if (!this.getActive() || forced) {
            //world.getEntity
            StandEntity stand = ModEntities.THE_WORLD.create(User.level());
            if (stand != null) {
                InteractionHand hand = User.getUsedItemHand();
                if (hand == InteractionHand.OFF_HAND) {
                    ItemStack itemStack = User.getUseItem();
                    Item item = itemStack.getItem();
                    if (item.getUseAnimation(itemStack) == UseAnim.BLOCK) {
                        User.releaseUsingItem();
                    }
                }
                Vec3 spos = stand.getStandOffsetVector(User);
                stand.absMoveTo(spos.x(), spos.y(), spos.z());

                theWorld.addFreshEntity(stand);

                if (sound) {
                    this.getStandPowers().playSummonSound();
                }

                this.standMount(stand);
            }

            active=true;
        } else {
            this.tryPower(PowerIndex.NONE,true);
            active=false;
        }
        this.setActive(active);
    }

    /** Returns the stand of a User, and makes necessary checks to reload the stand on a client
     * if the client does not have the stand loaded*/

    @Nullable
    public StandEntity getStand() {
        if (((LivingEntity) (Object) this).level().isClientSide) {
            return (StandEntity) ((LivingEntity) (Object) this).level().getEntity(((LivingEntity) (Object) this).getEntityData().get(STAND_ID));
        } else {
            return this.Stand;
        }
    }
    public boolean hasStandOut() {
        StandEntity standOut = this.getStand();
        return (standOut != null && standOut.isAlive() && !standOut.isRemoved());
    }


    /** Set Direction input. This is part of stand rendering as leaning.
     * @see StandEntity#setMoveForward */
    public void setDI(byte forward, byte strafe){
        //RoundaboutMod.LOGGER.info("MF:"+ forward);
        if (Stand != null){
            if (!User.isShiftKeyDown() && User.isSprinting()){
                forward*=2;}
            Stand.setMoveForward(forward);
        }
    }

    /** Retooled vanilla riding code to update the location of a stand every tick relative to the entity it
     * is the user of.
     * @see StandEntity#getAnchorPlace */
    public void updateStandOutPosition(StandEntity stand) {
        this.updateStandOutPosition(stand, Entity::setPos);
    }

    public void updateStandOutPosition(StandEntity stand, Entity.MoveFunction positionUpdater) {
        if (!(this.hasStandOut())) {
            return;
        }
        byte OT = stand.getOffsetType();
        if (OffsetIndex.OffsetStyle(OT) != OffsetIndex.LOOSE_STYLE) {

            Vec3 grabPos = stand.getStandOffsetVector(User);
            positionUpdater.accept(stand, grabPos.x, grabPos.y, grabPos.z);

                stand.setYRot(User.getYHeadRot() % 360);
                stand.setXRot(User.getXRot());
                stand.setYBodyRot(User.getYHeadRot() % 360);
                stand.setYHeadRot(User.getYHeadRot() % 360);
                if (OffsetIndex.OffsetStyle(OT) == OffsetIndex.FIXED_STYLE) {
                    float rot;
                    if (OT == OffsetIndex.BENEATH){
                        rot = (User.getYRot()) % 360;
                    } else {
                        rot = (float) ((User.getYHeadRot() -(stand.getPunchYaw(stand.getAnchorPlace(),
                                0.36))) % 360);
                    }
                    stand.setYRot(rot);
                    stand.setYBodyRot(rot);
                }
        } else {
            positionUpdater.accept(stand, stand.getX(), stand.getY(), stand.getZ());
        }
    }

    public void onStandOutLookAround(StandEntity passenger) {
    }

    public void removeStandOut() {
        this.Stand = null;
        ((LivingEntity) (Object) this).getEntityData().set(STAND_ID, -1);
        //this.emitGameEvent(GameEvent.ENTITY_DISMOUNT, passenger);
    }

    @Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
    private void initDataTrackerRoundabout(CallbackInfo ci) {
        ((LivingEntity)(Object)this).getEntityData().define(STAND_ID, -1);
        ((LivingEntity)(Object)this).getEntityData().define(ROUNDABOUT_TS_DAMAGE, (byte) 0);
        ((LivingEntity)(Object)this).getEntityData().define(ROUNDABOUT$LOCACACA_CURSE, (byte) -1);
        ((LivingEntity)(Object)this).getEntityData().define(ROUNDABOUT$BLEED_LEVEL, -1);
        ((LivingEntity)(Object)this).getEntityData().define(ROUNDABOUT$ONLY_BLEEDING, true);
        ((LivingEntity)(Object)this).getEntityData().define(STAND_ACTIVE, false);
    }

    @Inject(method = "handleEntityEvent", at = @At(value = "HEAD"), cancellable = true)
    public void roundaboutHandleStatus(byte $$0, CallbackInfo ci) {
        if ($$0 == 29){
            if (this.isGuarding()) {
                if (!this.getGuardBroken()) {
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
    private void isBlockingRoundabout(CallbackInfoReturnable<Boolean> ci) {
        if (this.isGuarding()){
            ci.setReturnValue(this.isGuardingEffectively());
        }
    }

    @Inject(method = "getDamageAfterArmorAbsorb", at = @At(value = "HEAD"), cancellable = true)
    private void RoundaboutApplyArmorToDamage(DamageSource $$0, float $$1,CallbackInfoReturnable<Float> ci){
        if ($$0.is(ModDamageTypes.STAND)) {
            ci.setReturnValue($$1);
        }
    }

    /**Here, we cancel barrage if it has not "wound up" and the user is hit*/
    @Inject(method = "hurt", at = @At(value = "HEAD"), cancellable = true)
    private void RoundaboutDamage(DamageSource $$0, float $$1, CallbackInfoReturnable<Boolean> ci) {

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
            }
        }
        if ($$0.getDirectEntity() != null) {
            if (this.isBarraging()) {
                this.tryPower(PowerIndex.GUARD, true);
            } else if (this.getStandPowers().canInterruptPower()) {
                this.tryPower(PowerIndex.NONE, true);
            }
            this.setRoundaboutIdleTime(-1);
        }
    }

    /**Prevent you from hearing every hit in a rush*/
    @Inject(method = "playHurtSound", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutPlayHurtSound(DamageSource $$0, CallbackInfo ci) {
        if (this.isDazed()) {
            ci.cancel();
        }
    }

    /**This Should prevent repeated crossbow charging on barrage*/
    @Inject(method = "updatingUsingItem", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutTickActiveItemStack(CallbackInfo ci) {
        if (this.isDazed()) {
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
    private void RoundaboutDamage2(DamageSource source, float amount, CallbackInfoReturnable<Boolean> ci) {
        if (this.isGuarding()) {
            if (!source.is(DamageTypeTags.BYPASSES_COOLDOWN) && this.getGuardCooldown() > 0) {
                return;
            }

            this.damageGuard(amount);
        }
    }

    /**Entities who are caught in a barrage stop moving from their own volition in the x and z directions.*/
    @ModifyVariable(method = "travel(Lnet/minecraft/world/phys/Vec3;)V", at = @At(value = "HEAD"))
    private Vec3 RoundaboutTravel(Vec3 $$0) {
        if (this.isDazed()) {
            return new Vec3(0,0,0);
        } else {
            return $$0;
        }
    }

    /**This code stops a barrage target from losing velocity, preventing lag spikes from causing them to drop.*/
    @ModifyVariable(method = "travel(Lnet/minecraft/world/phys/Vec3;)V", at = @At("STORE"))
    private double RoundaboutTravel2(double $$1) {
        if (this.isDazed()) {
            return 0;
        }
        return $$1;
    }

    @ModifyVariable(method = "travel(Lnet/minecraft/world/phys/Vec3;)V", at = @At("STORE"),ordinal = 0)
    private double RoundaboutTravel3(double $$1) {
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
                if (this.roundaboutGetTSJump()){
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
    protected void swimUpward(TagKey<Fluid> $$0, CallbackInfo ci) {
        if (this.isClashing()) {
            ci.cancel();
        }
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    protected void roundaboutHurt(DamageSource $$0, float $$1, CallbackInfoReturnable<Boolean> ci){
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

        LivingEntity entity = ((LivingEntity)(Object) this);
        if (entity.level().isClientSide){
            ci.setReturnValue(false);
            return;
        }
        if (((TimeStop)entity.level()).CanTimeStopEntity(entity)){
            if (this.roundaboutTSHurtTime <= 0 || $$0.is(DamageTypeTags.BYPASSES_COOLDOWN)) {

                float dmg = roundaboutGetStoredDamage();
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
                } if (roundaboutTSHurtSound < 2 && $$0.is(ModDamageTypes.STAND)){
                    roundaboutTSHurtSound = 2;
                }
                $$1*=0.66F;
                if ((dmg + $$1) > max) {
                    roundaboutSetStoredDamage(max);
                } else {
                    roundaboutSetStoredDamage((dmg + $$1));
                }
                if ($$0 != null && $$0.getEntity() != null) {
                    if ($$0.getEntity() instanceof LivingEntity){
                        ((StandUser)$$0.getEntity()).getStandPowers().hasActedInTS = true;
                    }
                    roundaboutSetStoredAttacker($$0.getEntity());
                } else {
                    roundaboutSetStoredAttacker(null);
                }
                this.roundaboutTSHurtTime = 7;
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
            if (roundaboutGetStoredDamage() > 0 && !$$0.is(ModDamageTypes.TIME)) {
                ci.setReturnValue(false);
                return;
            } if ($$0.is(ModDamageTypes.TIME)){
                roundabout$postTSHurtTime = 8;
            } else {
                /*Knife and match code*/
                if (roundabout$postTSHurtTime > 0) {
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

    @Shadow
    protected int decreaseAirSupply(int $$0) {
        return 0;
    }

    @Shadow protected abstract float getEyeHeight(Pose $$0, EntityDimensions $$1);

    @Inject(method = "baseTick", at = @At(value = "HEAD"), cancellable = true)
    protected void roundaboutBreathingCancel(CallbackInfo ci){
        if (!Roundabout.canBreathInTS) {
            if (!((TimeStop) this.level()).getTimeStoppingEntities().isEmpty()
                    && ((TimeStop) this.level()).getTimeStoppingEntities().contains(((LivingEntity) (Object) this))) {
                ((IEntityAndData) this).setRoundaboutJamBreath(true);
            }
        }
    }

    @Inject(method = "baseTick", at = @At(value = "TAIL"), cancellable = true)
    protected void roundaboutBreathingCancel2(CallbackInfo ci){
        if (!Roundabout.canBreathInTS) {
            if (((IEntityAndData) this).getRoundaboutJamBreath()) {
                ((IEntityAndData) this).setRoundaboutJamBreath(false);
            }
        }
    }

    /**If you have a chest turned to stone, decreases breath faster*/
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
    }

    /**If you have a chest turned to stone, decreases breath faster*/
    @Inject(method = "decreaseAirSupply", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$decreaseAirSupply(int $$0, CallbackInfoReturnable<Integer> cir) {
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

    /**When time is stopped by the user, incurs an action penalty if food is eaten*/
    @Inject(method = "completeUsingItem", at = @At(value = "TAIL"), cancellable = true)
    protected void roundaboutCompleteUsingItem(CallbackInfo ci){
        if (!level().isClientSide) {
            LivingEntity entity = ((LivingEntity)(Object) this);
            if (((TimeStop) entity.level()).isTimeStoppingEntity(entity)) {
                this.getStandPowers().hasActedInTS = true;
            }
        }
    }

    /**While using item, leave idle state*/
    @Inject(method = "updateUsingItem", at = @At(value = "HEAD"))
    protected void roundaboutUpdateUsingItem(ItemStack $$0, CallbackInfo ci){
        this.roundaboutIdleTime = -1;
    }


    @Unique
    public void roundaboutUniversalTick(){
        if (this.roundaboutTSHurtTime > 0){this.roundaboutTSHurtTime--;}
    }

    /**Use this code to eliminate the sprint jump during certain actions*/
    @Inject(method = "jumpFromGround", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$jumpFromGround(CallbackInfo ci) {
        byte curse = this.roundabout$getLocacacaCurse();
        if (this.getStandPowers().isBarraging() || (curse > -1 && (curse == LocacacaCurseIndex.RIGHT_LEG || curse == LocacacaCurseIndex.LEFT_LEG))) {
            Vec3 $$0 = this.getDeltaMovement();
            this.setDeltaMovement($$0.x, (double) this.getJumpPower(), $$0.z);
            this.hasImpulse = true;
            ci.cancel();
        }
    }

    @Inject(method = "getSpeed", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$getSpeed(CallbackInfoReturnable<Float> cir) {
        byte curse = this.roundabout$getLocacacaCurse();
        if (curse > -1) {
            if (curse == LocacacaCurseIndex.RIGHT_LEG || curse == LocacacaCurseIndex.LEFT_LEG) {
                cir.setReturnValue((float) (this.speed * 0.82));
            } else if (curse == LocacacaCurseIndex.CHEST) {
                cir.setReturnValue((float) (this.speed * 0.85));
            }
        }
    }

    @Shadow
    protected float getJumpPower() {
        return 0;
    }
}
