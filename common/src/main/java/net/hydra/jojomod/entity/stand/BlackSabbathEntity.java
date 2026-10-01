package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.access.AccessThrownTrident;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPlayerEntityServer;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.goals.BlackSabbathRangedAttackGoal;
import net.hydra.jojomod.entity.navigation.BlackSabbathNavigation;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.*;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersBlackSabbath;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlackSabbathEntity extends StandEntity implements HasCustomInventoryScreen, RangedAttackMob {


    public BlackSabbathEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
        this.setPathfindingMalus(BlockPathTypes.BLOCKED, -1.0F);
    }

    public static final byte
            PART_5_ANIME = 1,
            PART_5_MANGA = 2,
            BURNING = 3,
            GIO_GIO = 4,
            VERDANT = 5,
            NIGHT = 6,
            DEPARTURE = 7,
            CHERRY = 8,
            GRAPE = 9,
            MINT = 10,
            TACO = 11,
            WOOL = 12,
            FUNGUS = 13,
            DAPPER = 14,
            COPPER = 15,
            PHANTOM = 16,
            SWEET = 17,
            MAGMA = 18,
            OCULUS = 19,
            CRIMSON = 20,
            SACTHOTH = 21,
            COWBOY = 22,
            BEACH = 23,
            SANTA = 24;

    public final AnimationState coat_open = new AnimationState();
    public final AnimationState chest_open = new AnimationState();
    public final AnimationState chest_close = new AnimationState();
    public final AnimationState floating = new AnimationState();
    public final AnimationState diving = new AnimationState();
    public final AnimationState emerge = new AnimationState();
    public final AnimationState catching = new AnimationState();
    public final AnimationState burningStart = new AnimationState();
    public final AnimationState burningCripple = new AnimationState();
    public final AnimationState burningDive = new AnimationState();
    public final AnimationState walk = new AnimationState();
    public final AnimationState stando = new AnimationState();
    public final AnimationState strafeWalk = new AnimationState();
    public final AnimationState strafeEmerge = new AnimationState();
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if(this.getUser() != null){
            if (((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb){
                switch (pb.moveMode) {
                    case 1 -> {
                        if (pb.active) {
                            this.coat_open.stop();
                            chest_close.stop();
                            this.chest_open.startIfStopped(this.tickCount);
                        } else {
                            this.chest_open.stop();
                            this.coat_open.stop();
                            this.chest_close.startIfStopped(this.tickCount);
                        }
                    }
                    case 2 -> {
                        this.chest_open.stop();
                        this.chest_close.stop();
                        this.floating.startIfStopped(this.tickCount);
                        this.coat_open.startIfStopped(this.tickCount);
                    }
                    case 3 -> {
                        if(!pb.blackSabbathTargets.isEmpty() || !(this.getUser() instanceof Player)){
                            if (!isBlackSabbathUnderLight()) {
                                if(!isOnFire()) {
                                    animationTick = 20;
                                }
                                if(!getThrowable()) {
                                    this.coat_open.stop();
                                    if ((targetSabbath() != null && !isUnderSunlight(targetSabbath()))) {
                                        if (lungeTicks < 10) {
                                            burningDive.stop();
                                            diving.startIfStopped(this.tickCount);
                                        }
                                        if (lungeTicks < 220 && lungeTicks > 210) {
                                            emerge.startIfStopped(this.tickCount);
                                            diving.stop();
                                            burningDive.stop();
                                        } else {
                                            emerge.stop();
                                        }
                                        if (lungeTicks <= 210 && lungeTicks > 180) {
                                            catching.startIfStopped(this.tickCount);
                                            emerge.stop();
                                        }
                                        if (lungeTicks <= 50 && lungeTicks > 35) {
                                            if (burningCripple.isStarted() || burningStart.isStarted() || isOnFire()) {
                                                if (!diving.isStarted()) {
                                                    diving.stop();
                                                    walk.stop();
                                                    stando.stop();
                                                    burningCripple.stop();
                                                    burningStart.stop();
                                                    burningDive.startIfStopped(this.tickCount);
                                                }
                                            } else {
                                                if (!burningDive.isStarted()) {
                                                    burningDive.stop();
                                                    walk.stop();
                                                    stando.stop();
                                                    diving.startIfStopped(this.tickCount);
                                                }
                                            }
                                            emerge.stop();
                                            catching.stop();
                                        }
                                    } else {
                                        catching.stop();
                                        this.emerge.stop();
                                        coat_open.stop();
                                        emerge.stop();
                                        catching.stop();
                                        if (lungeTicks <= 50 && lungeTicks > 35) {
                                            if (burningCripple.isStarted() || burningStart.isStarted() || isOnFire()) {
                                                if (!diving.isStarted()) {
                                                    burningCripple.stop();
                                                    burningStart.stop();
                                                    diving.stop();
                                                    walk.stop();
                                                    stando.stop();
                                                    burningDive.startIfStopped(this.tickCount);
                                                }
                                            } else {
                                                if (!burningDive.isStarted()) {
                                                    walk.stop();
                                                    stando.stop();
                                                    diving.startIfStopped(this.tickCount);
                                                }
                                            }
                                        }
                                        if (isWalking && lungeTicks > 50) {
                                            if (this.getDeltaMovement().x() != 0 || this.getDeltaMovement().z() != 0) {
                                                stando.stop();
                                                walk.startIfStopped(this.tickCount);
                                            } else {
                                                walk.stop();
                                                stando.stop();
                                            }
                                        } else {
                                            walk.stop();
                                            stando.stop();
                                        }
                                    }
                                    strafeEmerge.stop();
                                    strafeWalk.stop();
                                } else {
                                    if (lungeTicks <= 50 && lungeTicks > 35) {
                                        strafeEmerge.stop();
                                        strafeWalk.stop();
                                        if (burningCripple.isStarted() || burningStart.isStarted() || isOnFire()) {
                                            if (!diving.isStarted()) {
                                                diving.stop();
                                                walk.stop();
                                                stando.stop();
                                                burningCripple.stop();
                                                burningStart.stop();
                                                burningDive.startIfStopped(this.tickCount);
                                            }
                                        } else {
                                            if (!burningDive.isStarted()) {
                                                burningDive.stop();
                                                walk.stop();
                                                stando.stop();
                                                diving.startIfStopped(this.tickCount);
                                            }
                                        }
                                        emerge.stop();
                                        catching.stop();
                                    } else {
                                        burningDive.stop();
                                        diving.stop();
                                        emerge.stop();
                                    }
                                        if (getStrafing()) {
                                            chest_close.stop();
                                            if (animationTick2 > 0) {
                                                animationTick2--;
                                                strafeWalk.stop();
                                                strafeEmerge.startIfStopped(this.tickCount);
                                            } else {
                                                strafeEmerge.stop();
                                                strafeWalk.startIfStopped(this.tickCount);
                                            }
                                        } else {
                                            if (!getTridentLoyalty()) {
                                                if (animationTick2 < 14) {
                                                    animationTick2++;
                                                }
                                                strafeEmerge.stop();
                                                strafeWalk.stop();
                                                chest_close.startIfStopped(this.tickCount);
                                            }
                                        }
                                    burningCripple.stop();
                                    burningStart.stop();
                                    emerge.stop();
                                    catching.stop();
                                    walk.stop();
                                    stando.stop();
                                }
                            } else {
                                strafeEmerge.stop();
                                strafeWalk.stop();
                                catching.stop();
                                this.emerge.stop();
                                burningDive.stop();
                                this.diving.stop();
                                walk.stop();
                                stando.stop();
                                this.chest_open.stop();
                                if(animationTick >= 1){
                                    animationTick--;
                                }
                                if(animationTick > 1){
                                    burningStart.startIfStopped(this.tickCount);
                                    burningCripple.stop();
                                } else {
                                    burningStart.stop();
                                    burningCripple.startIfStopped(this.tickCount);
                                }
                            }
                        } else {
                            strafeEmerge.stop();
                            strafeWalk.stop();
                            catching.stop();
                            this.emerge.stop();
                            this.diving.stop();
                            this.chest_open.stop();
                            this.coat_open.stop();
                            walk.stop();
                            stando.stop();
                            this.chest_close.startIfStopped(this.tickCount);
                        }
                        this.chest_open.stop();
                    }
                }
            }
        } else {
            this.chest_open.stop();
            this.chest_close.stop();
            this.coat_open.startIfStopped(this.tickCount);
        }
    }
    int animationTick2 = 20;
    @Override
    public boolean canStandBeHurt(){
        return getHunting() && !getUnrender();
    }
    @Override
    public boolean canBeHitByProjectile() {
        return getHunting() && !getRiding() && !getUnrender();
    }
    @Override
    public boolean isAttackable() {
        return getHunting() && !getRiding() && !getUnrender();
    }
    @Override
    public boolean isPickable() {
        return getHunting() && !getRiding() && !getUnrender();
    }
    @Override
    public boolean skipAttackInteraction(Entity $$0) {
        return false;
    }
    @Override
    public boolean isInvulnerable() {
        return getHunting() && (getRiding() || getUnrender());
    }
    protected boolean isAffectedByFluids() {
        FluidState $$3 = this.level().getFluidState(this.blockPosition());
        return horizontalCollision && !($$3.is(Fluids.LAVA) || $$3.is(Fluids.FLOWING_LAVA));
    }
    @Override
    public boolean fireImmune() {
        return false;
    }
    @Override
    public void knockback(double $$0, double $$1, double $$2){
        super.knockback($$0 * 1.65D, $$1, $$2 * 1.65D);
    }
    public boolean shouldFloat = false;
    public void setShouldFloat(boolean bool){shouldFloat = bool;}
    public boolean shouldSelect = false;
    public void setShouldSelect(boolean bool){shouldSelect = bool;}
    public int tickDownSecond = 0;
    public void setTickDownSecond(int td){tickDownSecond = td;}
    @Override
    public boolean forceVisualRotation(){
        return true;
    }
    @Override
    public boolean lockPos(){
        if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb){
            return pb.moveMode == 2 || getRiding();
        }
        return false;
    }
    @Override
    public boolean isPushable() {
        return getHunting() && !getRiding() || !getUnrender();
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return super.canCollideWith(entity);
    }
    @Override
    public boolean hasNoPhysics(){
        if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb){
            return pb.moveMode == 2 || this.is(pb.blackSelect) || getRiding();
        }
        return false;
    }
    @Override
    public boolean isNoGravity() {
        if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb){
            return pb.moveMode == 2 || this.is(pb.blackSelect) || getRiding();
        }
        return false;
    }
    @Override
    public boolean standHasGravity() {
        if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb){
            return pb.moveMode != 2 || !this.is(pb.blackSelect);
        }
        return true;
    }
    public boolean isUnderSunlight(LivingEntity lent){
        if(lent != null) {
            BlockPos pos = lent.blockPosition();
            long timeOfDay = lent.level().getDayTime() % 24000L;
            Vec3 yes = lent.getEyePosition();
            BlockPos atVec = BlockPos.containing(yes);
            boolean isDay = timeOfDay < 12555L || timeOfDay > 23470;
            if (lent.level().getBrightness(LightLayer.BLOCK, pos) < 12) {
                if (isDay) {
                    if (lent.level().isRaining() || lent.level().isThundering()) {
                        return false;
                    } else if (lent.level().getBrightness(LightLayer.SKY, atVec) < 14) {
                        return false;
                    } else {
                        return true;
                    }
                } else if (!isDay) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return true;
    }
    public boolean isBlackSabbathUnderLight(){
        BlockPos pos = this.blockPosition();
        long timeOfDay = this.level().getDayTime() % 24000L;
        Vec3 yes = this.getEyePosition();
        BlockPos atVec = BlockPos.containing(yes);
        boolean isDay = timeOfDay < 12555L || timeOfDay > 23470;
        if (this.level().getBrightness(LightLayer.BLOCK, pos) < 13) {
            if (isDay) {
                if (this.level().isRaining() || this.level().isThundering()) {
                    return false;
                } else if (this.level().getBrightness(LightLayer.SKY, atVec) < 15) {
                    return false;
                } else {
                    return true;
                }
            } else if (!isDay) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }
    private int damageImmunityTicks = 10;
    private void setDamageImmunityTicks(int immun){damageImmunityTicks = immun;}
    @Override
    public void tick(){
        validateUUID();
        float pitch = this.getXRot();
        float yaw = this.getYRot();
        if(!getHunting()) {
            if (shouldFloat && this.getUser() != null) {
                if (!this.level().isClientSide()) {
                    this.setXRot(pitch);
                    this.setYRot(yaw);
                    this.setYBodyRot(yaw);
                    this.xRotO = pitch;
                    this.yRotO = yaw;
                }
                if (((StandUser) this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb) {
                    if (tickDownSecond > 1) {
                        tickDownSecond--;

                        if (tickDownSecond == 4) {
                            this.forceDespawnSet = true;
                        }
                    }
                }
            }
        } if(getHunting()) {
            if (this.getUser() != null && ((StandUser) this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb) {
                    if(pb.tickDown2 > -10){
                        if(this.getDeltaMovement() != null) {
                            setDeltaMovement(0, this.getDeltaMovement().y, 0);
                        }
                    }
            }
        }
        if(getRiding()){
            if(this.getY() < this.level().getMinBuildHeight()) {
                setDeltaMovement(this.getDeltaMovement().x, 0, this.getDeltaMovement().z);
                absMoveTo(this.getX(), this.getY() , this.getZ());
            } else {
                absMoveTo(this.getX(), this.level().getMinBuildHeight() , this.getZ());
            }
        }
        if(isBlackSabbathUnderLight()){
            this.getNavigation().setSpeedModifier(0.05);
        }
        if(this.getUser() != null && this.getUser() instanceof Player pl){
            IPlayerEntity play = ((IPlayerEntity)pl);
            ItemStack blackSabbathFirstSlot = play.roundabout$getBlckSabbathPlayerInventory().getItem(0);
                if(blackSabbathFirstSlot != getHeldItemSabbath()){
                    if(!this.level().isClientSide()) {
                        setHeldItemSabbath(blackSabbathFirstSlot);
                        /*Yo, see this as an example to put a message into chat*/
                        /*MutableComponent message = Component.literal("Your held item is " + getHeldItemSabbath().getCount() + " ");
                        MutableComponent message2 = Component.translatable(getHeldItemSabbath().getItem().getDescription().getString());
                        if(getHunting()) {
                        pl.sendSystemMessage(message.append(message2).append(".")); }*/
                    }
                }
                if(!level().isClientSide() && !getTridentLoyalty()) {
                    if (blackSabbathFirstSlot.getItem() instanceof TridentItem tr && EnchantmentHelper.getLoyalty(blackSabbathFirstSlot) > 0) {
                        setTridentLoyalty(true);
                    } else if (blackSabbathFirstSlot.getItem() instanceof HarpoonItem) {
                        setTridentLoyalty(true);
                    }
                }
           // System.out.println(getHeldItemSabbath() + " " + this.level());
            //System.out.println(blackSabbathFirstSlot + " " + this.level());
        }


        if(getHunting()){
            huntingTick();
            hurtBlackSabbath();
        }
        super.tick();
        travelAhead(Entity::setPos);
    }
    public void hurtBlackSabbath(){
        if(getHunting()) {
            if (this.getUser() != null && ((StandUser) this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb) {
                if (isBlackSabbathUnderLight()) {
                    damageImmunityTicks--;
                    if (damageImmunityTicks < 1) {
                        if(pb.moveMode == 3) {
                            if(this.isInWater() || isInPowderSnow){
                                DamageSource damageSource = ModDamageTypes.of(this.level(), DamageTypes.HOT_FLOOR);
                                setDamageImmunityTicks(10);
                                super.hurt(damageSource, 1);
                            } else {
                                this.setSecondsOnFire(1);
                                setDamageImmunityTicks(10);
                            }
                        }
                    }
                } else {
                    damageImmunityTicks--;
                    if (damageImmunityTicks < 1) {
                        if(pb.moveMode == 3 && this.getHealth() < this.getMaxHealth()) {
                            setDamageImmunityTicks(10);
                            setSecondsOnFire(0);
                            if(!this.level().isClientSide){
                                heal(1);
                            }
                        }
                    }
                }
                if(getRiding()){
                    damageImmunityTicks--;
                    if (damageImmunityTicks < 1) {
                        if(pb.moveMode == 3 && this.getHealth() < this.getMaxHealth()) {
                            setDamageImmunityTicks(10);
                            if(!this.level().isClientSide){
                                setSecondsOnFire(0);
                                heal(1);
                            }
                        }
                    }
                }
            }
        }
    }
    /*Sync Data slop*/

    protected static final EntityDataAccessor<ItemStack> HELD_ITEM_BLACK_SABBATH = SynchedEntityData.defineId(BlackSabbathEntity.class,
            EntityDataSerializers.ITEM_STACK);
    public final ItemStack getHeldItemSabbath() {
        return this.entityData.get(HELD_ITEM_BLACK_SABBATH);
    }
    public final void setHeldItemSabbath(ItemStack stack) {
        this.entityData.set(HELD_ITEM_BLACK_SABBATH, stack);
    }
    private static final EntityDataAccessor<Boolean> MUST_UNRENDER =
            SynchedEntityData.defineId(BlackSabbathEntity.class, EntityDataSerializers.BOOLEAN);
    public final Boolean getUnrender() {
        return this.entityData.get(MUST_UNRENDER);
    }
    public final void setUnrender(Boolean bool) {
        this.entityData.set(MUST_UNRENDER, bool);
    }
    private static final EntityDataAccessor<Boolean> IS_RIDING =
            SynchedEntityData.defineId(BlackSabbathEntity.class, EntityDataSerializers.BOOLEAN);
    public final Boolean getRiding() {
        return this.entityData.get(IS_RIDING);
    }
    public final void setRiding(Boolean bool) {
        this.entityData.set(IS_RIDING, bool);
    }
    private static final EntityDataAccessor<Boolean> CRIPPLED =
            SynchedEntityData.defineId(BlackSabbathEntity.class, EntityDataSerializers.BOOLEAN);
    public final Boolean getCrippled() {
        return this.entityData.get(CRIPPLED);
    }
    public final void setCrippled(Boolean bool) {
        this.entityData.set(CRIPPLED, bool);
    }
    private static final EntityDataAccessor<Boolean> IS_HUNTING =
            SynchedEntityData.defineId(BlackSabbathEntity.class, EntityDataSerializers.BOOLEAN);
    public final Boolean getHunting() {
        return this.entityData.get(IS_HUNTING);
    }
    public final void setHunting(Boolean bool) {
        this.entityData.set(IS_HUNTING, bool);
    }
    private static final EntityDataAccessor<Boolean> IS_GRABBING =
            SynchedEntityData.defineId(BlackSabbathEntity.class, EntityDataSerializers.BOOLEAN);

    public final Boolean getGrabbing() {
        return this.entityData.get(IS_GRABBING);
    }
    public final void setGrabbing(Boolean bool) {
        this.entityData.set(IS_GRABBING, bool);
    }
    private static final EntityDataAccessor<Boolean> IS_STRAFING =
            SynchedEntityData.defineId(BlackSabbathEntity.class, EntityDataSerializers.BOOLEAN);
    public final Boolean getStrafing() {
        return this.entityData.get(IS_STRAFING);
    }
    public final void setStrafing(Boolean bool) {
        this.entityData.set(IS_STRAFING, bool);
    }
    private static final EntityDataAccessor<Integer> MOVEMENT_MODE =
            SynchedEntityData.defineId(BlackSabbathEntity.class, EntityDataSerializers.INT);
    /**0 = no items; 1 = ranged mode; 2 = other items used for melee mode*/
    public final Integer getMoveMode() {
        return this.entityData.get(MOVEMENT_MODE);
    }
    /**0 = no items; 1 = ranged mode; 2 = other items*/
    public final void setMoveMode(Integer i) {
        this.entityData.set(MOVEMENT_MODE, i);
    }
    public final Boolean getTridentLoyalty() {
        return this.entityData.get(IS_TRIDENT);
    }
    public final void setTridentLoyalty(Boolean bool) {
        this.entityData.set(IS_TRIDENT, bool);
    }
    private static final EntityDataAccessor<Boolean> IS_TRIDENT =
            SynchedEntityData.defineId(BlackSabbathEntity.class, EntityDataSerializers.BOOLEAN);
    public final Boolean getThrowable() {
        return (this.getHeldItemSabbath().getItem() instanceof ArrowItem || this.getHeldItemSabbath().getItem() instanceof KnifeItem
                || this.getHeldItemSabbath().is(ModItems.KNIFE_BUNDLE) || this.getHeldItemSabbath().getItem() instanceof TridentItem && EnchantmentHelper.getRiptide(getHeldItemSabbath()) <= 0
                || this.getHeldItemSabbath().getItem() instanceof EggItem || this.getHeldItemSabbath().getItem() instanceof SnowballItem || this.getHeldItemSabbath().getItem() instanceof ThrowablePotionItem
                || this.getHeldItemSabbath().getItem() instanceof EnderpearlItem || this.getHeldItemSabbath().getItem() instanceof GasolineBucketItem || this.getHeldItemSabbath().getItem() instanceof GasolineCanItem
                || this.getHeldItemSabbath().getItem() instanceof MatchItem || this.getHeldItemSabbath().is(ModItems.MATCH_BUNDLE) || this.getHeldItemSabbath().getItem() instanceof FleshBucketItem
                ||this.getHeldItemSabbath().getItem() instanceof HarpoonItem) && !(this.getHeldItemSabbath().is(ItemStack.EMPTY.getItem()));
    }
    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(IS_RIDING)) {
            super.defineSynchedData();
            this.entityData.define(CRIPPLED, false);
            this.entityData.define(IS_RIDING, false);
            this.entityData.define(IS_HUNTING, false);
            this.entityData.define(MUST_UNRENDER, false);
            this.entityData.define(IS_GRABBING, false);
            this.entityData.define(IS_STRAFING, false);
            this.entityData.define(IS_TRIDENT, false);
            this.entityData.define(MOVEMENT_MODE, 0);
            this.entityData.define(HELD_ITEM_BLACK_SABBATH, ItemStack.EMPTY);
        }
    }
    @Override
    public void die(@NotNull DamageSource source) {
        if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pbs){
            ((StandUser)this.getUser()).roundabout$setSealedTicks(300);
            pbs.setTickDown2(20);
            if(MainUtil.isStandDamage(source) && this.getRemainingFireTicks() > 0 || source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.IN_FIRE) || source.is(ModDamageTypes.STAND_FIRE) || source.is(ModDamageTypes.STAND_FIRE) || source.is(DamageTypes.LAVA) || source.is(DamageTypes.HOT_FLOOR)) {
                setCrippled(true);
                if (this.level() instanceof ServerLevel SL) {
                    Vec3 position = this.getPosition(1);
                    Vec3 position2 = this.getEyePosition();
                    Vec3 position3 = this.getEyePosition().subtract(this.getPosition(1)).multiply(new Vec3(0.5F,
                            0.5F, 0.5F));
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
            } else if(source.is(ModDamageTypes.GO_BEYOND)){
                setUnrender(false);
                if (this.level() instanceof ServerLevel SL) {
                    Vec3 position = this.getPosition(1);
                    Vec3 position2 = this.getEyePosition();
                    Vec3 position3 = this.getEyePosition().subtract(this.getPosition(1)).multiply(new Vec3(0.5F,
                            0.5F, 0.5F));
                    position3 = position3.add(this.getPosition(1));

                    SL.sendParticles(ModParticles.SOUL_FIRE_CRUMBLE,
                            position.x, position.y, position.z,
                            0, 0.2, 0.5, 0.2, 0.5);
                    SL.sendParticles(ModParticles.SOUL_FIRE_CRUMBLE,
                            position2.x, position2.y, position2.z,
                            0, 0.2, 0.5, 0.2, 0.2);
                    SL.sendParticles(ModParticles.SOUL_FIRE_CRUMBLE,
                            position3.x, position3.y, position3.z,
                            0, 0.2, 0.5, 0.2, 0.2);

                    SL.sendParticles(ModParticles.STAR,
                            position.x, position.y, position.z,
                            0, 0.2, 0.5, 0.2, 0.5);
                    SL.sendParticles(ModParticles.STAR,
                            position2.x, position2.y, position2.z,
                            0, 0.2, 0.5, 0.2, 0.2);
                    SL.sendParticles(ModParticles.STAR,
                            position3.x, position3.y, position3.z,
                            0, 0.2, 0.5, 0.2, 0.2);
                }
            }
        }
    }

    private int tickShootCooldown = 10;

    public LivingEntity targetSabbath(){
        if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pbs){
            if(!pbs.blackSabbathTargets.isEmpty()){
                List<LivingEntity> targent = new ArrayList<>(pbs.blackSabbathTargets);
                if(!getThrowable()) {
                    targent.removeIf(this::isUnderSunlight);
                }

                LivingEntity lv = this.level().getNearestEntity(targent,
                        MainUtil.OFFER_TARGER_CONTEXT, null,
                        this.getX(), this.getY(), this.getZ());

                return lv;
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(!getRiding()) {
            if (source.is(DamageTypes.GENERIC_KILL) || source.is(DamageTypes.FELL_OUT_OF_WORLD)) {
                discard();
                return false;
            }
            if (source.getEntity() != null && source.getEntity() != this.getUser()) {
                if (this.getUser() != null) {
                    if (source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.IN_FIRE) || source.is(ModDamageTypes.STAND_FIRE) || source.is(DamageTypes.HOT_FLOOR) || source.is(DamageTypes.LAVA)) {
                        return super.hurt(source, amount + 2);
                    } else if (source.is(ModDamageTypes.GO_BEYOND)) {
                        return super.hurt(source, amount * 30);
                    }
                }
            } else if (source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.IN_FIRE) || source.is(ModDamageTypes.STAND_FIRE) || source.is(DamageTypes.HOT_FLOOR) || source.is(DamageTypes.LAVA)) {
                return super.hurt(source, amount);
            } else if (MainUtil.isStandDamage(source) && this.getRemainingFireTicks() > 0) {
                return super.hurt(source, amount * 0.85F);
            }
            this.markHurt();
            return super.hurt(source, 0.0F);
        }
        return false;
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource $$0) {
        if($$0.is(ModDamageTypes.GO_BEYOND)){
            return SoundEvents.BEACON_DEACTIVATE;
        }
        if($$0.is(DamageTypes.ON_FIRE) || $$0.is(DamageTypes.IN_FIRE) || $$0.is(ModDamageTypes.STAND_FIRE) || $$0.is(ModDamageTypes.STAND_FIRE) || $$0.is(DamageTypes.LAVA) || $$0.is(DamageTypes.HOT_FLOOR)) {
            return SoundEvents.PLAYER_HURT_ON_FIRE;
        }
        return SoundEvents.PLAYER_HURT;
    }

    public void travelAhead(Entity.MoveFunction positionUpdater) {
        if (this.getUser() != null) {
            if(((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb && (pb.moveMode == 2) && !getHunting()) {
                Vec3 lvec = pb.getLookAngleChest(this.getUser().getYRot(), this.getUser());
                Position pn = this.getUser().getEyePosition().add(lvec.scale(-0.90F));
                positionUpdater.accept(this, pn.x(), this.getUser().getY() + (this.getUser().getBbHeight() / 2.45), pn.z());
            }
        }
    }

    public void openCustomInventoryScreen(Player player) {
        if (!this.level().isClientSide) {
            ((IPlayerEntityServer)player).roundabout$openBlackSabbathInventory(this, player.getInventory());
        }
    }

    /*Mob AI*/
    public LivingEntity shadowHidTarget() {
        if (this.level() != null) {
            List<LivingEntity> lvent = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(3.5, 9, 3.5), (livingEntity) -> {
                return true;
            });
            if (lvent != null && !lvent.isEmpty()) {
                List<LivingEntity> targent = new ArrayList<>(lvent);
                for (LivingEntity value : lvent) {
                    if (value instanceof StandEntity || !this.hasLineOfSight(value)) {
                        targent.remove(value);
                    }
                    if(!isUnderSunlight(value)){
                        targent.remove(value);
                    }
                    if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pbs){
                        if(pbs.blackSabbathTargets.contains(value)){
                            targent.remove(value);
                        }
                    }
                }

                lvent = targent;
            }
            LivingEntity lv = this.level().getNearestEntity(lvent,
                    MainUtil.OFFER_TARGER_CONTEXT, null,
                    this.getX(), this.getY(), this.getZ());

            return lv;
        }
        return null;
    }

    private LivingEntity ridingEntity = null;
    void setRidingEntity(LivingEntity ent){ridingEntity = ent;}
    int securityTicks = 0;
    int securityTicks2 = 0;

    @Override
    protected PathNavigation createNavigation(Level $$0) {
        BlackSabbathNavigation nav = new BlackSabbathNavigation(this, $$0);
        nav.setAvoidLight(true);
        return nav;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new BlackSabbathRangedAttackGoal<>(this,  1.0, 20, 15.0F));
    }

    public void bsStopMove() {
        this.getMoveControl().setWantedPosition(this.getX(), this.getY(), this.getZ(), 0.0);
        this.getNavigation().setSpeedModifier(0.0);
        this.getNavigation().stop();
    }

    public Vec3 getTargetPosition() {
        Vec3 targetPos;
        if(targetSabbath() != null){
            targetPos = targetSabbath().position();
            return targetPos;
        }
        return null;
    }

    public boolean isNearTarget(LivingEntity lent){
        AABB abba = this.getBoundingBox().inflate(2, 0, 2);
        List<LivingEntity> lvent = this.level().getEntitiesOfClass(LivingEntity.class, abba, (livingEntity) -> {
            return true;
        });

        if(lvent.contains(lent)){
            return true;
        }

        return false;
    }
    public boolean isTouchingTarget(LivingEntity lent){
        AABB abba = this.getBoundingBox().inflate(1.15, 9, 1.15);
        List<LivingEntity> lvent = this.level().getEntitiesOfClass(LivingEntity.class, abba, (livingEntity) -> {
            return true;
        });

        if(lvent.contains(lent)){
            if(this.getY() <= lent.getY() && hasLineOfSight(lent)) {
                return true;
            }
        }

        return false;
    }

    @Nullable
    public Vec3 findBlackSabbathRandomPosition(
            ServerLevel level,
            LivingEntity lent,
            double radius
    ) {
        if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pbs) {
            int attempts = 100;
            double minDistance = 1;
            for (int i = 0; i < attempts; i++) {
                double angle = Math.random() * Math.PI * 2.0D;
                double distance = minDistance
                        + Math.sqrt(Math.random()) * (radius - minDistance);
                double x = lent.getX() + Math.cos(angle) * distance;
                double z = lent.getZ() + Math.sin(angle) * distance;
                int baseY = Mth.floor(lent.getY());
                for (int yOffset = 0; yOffset <= 10; yOffset++) {
                    double y = baseY + yOffset;
                    Vec3 candidate = new Vec3(x, y, z);
                    BlockPos bpos = BlockPos.containing(x, y - 0.1, z);
                    var blockState = this.level().getBlockState(bpos);
                    AABB yesbox = ModEntities.BLACK_SABBATH.getAABB(lent.getX(), lent.getY(), lent.getZ());
                    AABB testBox = yesbox.move(
                            candidate.x - lent.getX(),
                            candidate.y - lent.getY(),
                            candidate.z - lent.getZ()
                    );
                    if (level.noCollision(lent, testBox) && !blockState.isAir() && pbs.checkIfBposIsInDark(candidate)) {
                        return candidate;
                    } else {
                        for (int yOffset2 = -1; yOffset2 >= -8; yOffset2--) {
                            double y2 = baseY + yOffset2;
                            Vec3 candidate2 = new Vec3(x, y2, z);
                            BlockPos bpos2 = BlockPos.containing(x, y2 - 0.1, z);
                            var blockState2 = this.level().getBlockState(bpos2);
                            AABB yesbox2 = ModEntities.BLACK_SABBATH.getAABB(lent.getX(), lent.getY(), lent.getZ());
                            AABB testBox2 = yesbox2.move(
                                    candidate2.x - lent.getX(),
                                    candidate2.y - lent.getY(),
                                    candidate2.z - lent.getZ()
                            );
                            if (level.noCollision(lent, testBox2) && !blockState2.isAir() && pbs.checkIfBposIsInDark(candidate)) {
                                return candidate2;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private int pause = 0;
    private int animationTick = 20;
    public void huntingTick(){
        if(!this.level().isClientSide()) {
            if (securityTicks2 >= 1) {
                securityTicks2--;
                if (securityTicks2 == 1) {
                    setUnrender(false);
                }
            }
        }
        if(!isBlackSabbathUnderLight()) {
            if (this.targetSabbath() != null) {
                if (!this.level().isClientSide) {
                    if (pause >= 1) {
                        pause--;
                    }
                    if (!isBlackSabbathUnderLight()) {
                        if (isUnderSunlight(targetSabbath())) {
                            if (pause < 20) {
                                if (pause < 1) {
                                    pause = 100;
                                }
                                moveRandom();
                                this.getNavigation().setSpeedModifier(0.65);
                            }
                        } else {
                            this.moveToTarget();
                        }
                    }
                }
            } else {
                moveRandom();
                this.getNavigation().setSpeedModifier(0.65);
            }
        } else {
            moveToSafe();
        }
        if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pbs){
            if(pbs.blackSabbathTargets != null){
                if(!this.level().isClientSide) {
                    if(securityTicks < 1 && lungeTicks < 1) {
                        if(!getStrafing()){
                        if (this.getNavigation().getPath() != null && this.getNavigation().getPath().isDone() && targetSabbath() != null && !isNearTarget(targetSabbath()) || isUnderSunlight(targetSabbath()) && this.getNavigation().getPath() != null && this.getNavigation().getPath().isDone()) {
                            if (shadowHidTarget() != null) {
                                if (ridingEntity == null) {
                                    if (pbs.blackSabbathTargets != null && !pbs.blackSabbathTargets.isEmpty()) {
                                        if (!pbs.blackSabbathTargets.contains(shadowHidTarget())) {
                                            if (!isBlackSabbathUnderLight()) {
                                                setRidingEntity(shadowHidTarget());
                                                setRiding(true);
                                                setSecondsOnFire(0);
                                                setUnrender(true);
                                                System.out.println(1);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        }
                        if (ridingEntity != null && (!isUnderSunlight(ridingEntity) || ridingEntity.isDeadOrDying() || ridingEntity.isRemoved())) {
                            absMoveTo(ridingEntity.getX(), ridingEntity.getY(), ridingEntity.getZ());
                            setRidingEntity(null);
                            setRiding(false);
                            setDamageImmunityTicks(10);
                            securityTicks2 = 15;
                            securityTicks = 80;
                        }
                    } else {
                        securityTicks--;
                    }
                }
                if(!getThrowable()) {
                    if (lungeTicks >= 1) {
                        lungeTicks--;
                    }
                }
                String test = "";
                if(this.level().isClientSide()){
                    test = "Level is Clientside";
                } else {
                    test = "Level is Serverside";
                }
               // System.out.println(getThrowable() + ". " + test);

                if(getStrafing()){
                    tickShootCooldown--;
                    if(tickShootCooldown < 1){
                        if(targetSabbath() != null && !getUnrender() && ((!this.level().isClientSide() && seeTime >= 59))) {
                            if(animationTick2 < 1) {
                                if (getHeldItemSabbath().is(Items.ENDER_PEARL)) {
                                    if (hasLineOfSight(targetSabbath()) && MainUtil.cheapDistanceTo(this.getUser().getX(), this.getUser().getY(), this.getUser().getZ(), targetSabbath().getX(), targetSabbath().getY(), targetSabbath().getZ()) > 15) {
                                        tickShootCooldown = 200;
                                        performRangedAttackUnique(this, targetSabbath());
                                    }
                                } else if (getHeldItemSabbath().getItem() instanceof TridentItem || getHeldItemSabbath().getItem() instanceof HarpoonItem) {
                                    tickShootCooldown = 50;
                                    performRangedAttackUnique(this, targetSabbath());
                                } else {
                                    tickShootCooldown = 50;
                                    performRangedAttackUnique(this, targetSabbath());
                                }
                            }
                        }
                    }
                    if(getHeldItemSabbath().is(ItemStack.EMPTY.getItem())){
                        if(getStrafing()){
                            setStrafing(false);
                            tickShootCooldown = 50;
                        }
                    }
                }
               // System.out.println(lungeTicks + " " + this.level().isClientSide());
                if(getUnrender() || (lungeTicks < 20 && lungeTicks > 1)){
                    if(!getCrippled() && !getRiding()) {
                        createShadowParticles();
                    }
                }
                if(getUnrender() && shouldReattemptSpawn < 0){
                    isStuck = true;
                } else {
                    isStuck = false;
                }
                if(targetSabbath() != null) {
                    if (!isBlackSabbathUnderLight()) {
                        if (isWalking) {
                            isWalking = false;
                            lungeTicks = 51;
                        }
                        if(!getThrowable() && !getTridentLoyalty() && getMoveMode() != 1) {
                                if (lungeTicks < 1) {
                                    if (MainUtil.cheapDistanceTo2(this.getX(), this.getZ(), targetSabbath().getX(), targetSabbath().getZ()) > 2.5 || !hasLineOfSight(targetSabbath())) {
                                        setUnrender(true);
                                        setSecondsOnFire(0);
                                    } else {
                                        if (((this.getY() > targetSabbath().getY() && !hasLineOfSight(targetSabbath())) || targetSabbath().getY() - this.getY() > 9) && MainUtil.cheapDistanceTo2(this.getX(), this.getZ(), targetSabbath().getX(), targetSabbath().getZ()) > 1.5) {
                                            setUnrender(true);
                                            setSecondsOnFire(0);
                                        } else {
                                            if (lungeTicks < 1) {
                                                if (getUnrender()) {
                                                    setUnrender(false);
                                                    this.level().playSound(null, this, ModSounds.BLACK_SABBATH_EMERGE_EVENT, SoundSource.HOSTILE, 0.95F, 1.0F);
                                                }
                                                attemptGrab();
                                            }
                                        }
                                    }
                                }
                                if (lungeTicks < 215 && this.emerge.isStarted() || lungeTicks > 207 && walk.isStarted()) {
                                    if (getUnrender()) {
                                        this.level().playSound(null, this, ModSounds.BLACK_SABBATH_EMERGE_EVENT, SoundSource.HOSTILE, 0.95F, 1.0F);
                                    }
                                    setUnrender(false);
                                }
                                if (lungeTicks > 200) {
                                    this.getNavigation().setSpeedModifier(0.60);
                                } else if (lungeTicks > 190 && lungeTicks < 195) {
                                    this.getNavigation().setSpeedModifier(2.25);
                                    if (targetSabbath() != null && isTouchingTarget(targetSabbath())) {
                                        //    targetSabbath().kill();
                                    }
                                } else if (lungeTicks < 185 && lungeTicks > 40) {
                                    if (!isBlackSabbathUnderLight()) {
                                        this.getNavigation().setSpeedModifier((float) 0);
                                    }
                                }
                                if (lungeTicks < 40 && lungeTicks > 1) {
                                    if (!getUnrender()) {
                                        setUnrender(true);
                                    }
                                }
                        } else {
                            if(!this.level().isClientSide()) {
                                if(targetSabbath() != null) {
                                    if (!(getTridentLoyalty() && this.getHeldItemSabbath().is(ItemStack.EMPTY.getItem()))) {
                                        if (((!this.level().isClientSide() && seeTime > 0) && (MainUtil.cheapDistanceTo2(this.getX(), this.getZ(), targetSabbath().getX(), targetSabbath().getZ()) < 8.5 && this.getY() - targetSabbath().getY() < 12))) {
                                            if(getUnrender()) {
                                                setUnrender(false);
                                                this.level().playSound(null, this, ModSounds.BLACK_SABBATH_EMERGE_EVENT, SoundSource.HOSTILE, 0.95F, 1.0F);
                                            }
                                            if(animationTick2 > 0) {
                                                animationTick2--;
                                            }
                                        } else {
                                            if (MainUtil.cheapDistanceTo2(this.getX(), this.getZ(), targetSabbath().getX(), targetSabbath().getZ()) > 12.5 || this.getY() - targetSabbath().getY() > 12 || (!this.level().isClientSide() && seeTime < 0)) {
                                                if(animationTick2 >= 0 && animationTick2 < 14){
                                                    animationTick2++;
                                                    getNavigation().setSpeedModifier(0.25);
                                                }else if(animationTick2 >= 14) {
                                                    if(!strafeEmerge.isStarted()) {
                                                        setUnrender(true);
                                                        setSecondsOnFire(0);
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        setUnrender(false);
                                        this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
                                        if (this.getNavigation() != null) {
                                            getNavigation().setSpeedModifier(1);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        setUnrender(false);
                        setStrafing(false);
                        lungeTicks = 51;
                    }
                } else {
                    if(!pbs.blackSabbathTargets.isEmpty()) {
                        if (!isBlackSabbathUnderLight()) {
                            if (!isWalking) {
                                isWalking = true;
                            }
                            if (lungeTicks < 15) {
                                setUnrender(true);
                                if (isWalking) {
                                    isWalking = false;
                                }
                            }
                        } else {
                            if (isWalking) {
                                isWalking = false;
                            }
                            setUnrender(false);
                            lungeTicks = 51;
                        }
                    } else {
                        setUnrender(false);
                        if(this.getNavigation() != null) {
                            this.getNavigation().setSpeedModifier(0);
                        }
                    }
                }
            }
            if(getUnrender()) {
                isPathfindingStuckBlackSabbath();
            }
        }
    }
    public boolean isStuck = false;
    private Vec3 lastPos = Vec3.ZERO;
    public int shouldReattemptSpawn = 100;
    private void isPathfindingStuckBlackSabbath(){
        Vec3 position =  getPosition(1);
        if(getUnrender()) {
            if (lastPos != null && lastPos.distanceToSqr(position) > 0.01F) {
                shouldReattemptSpawn = 100;
            } else {
                    shouldReattemptSpawn--;
            }
        }
        lastPos = position;
    }
    boolean isWalking = false;
    protected void createShadowParticles() {
        if(this.level() instanceof ServerLevel SL){
            Random random = new Random();
            Float flute5 = random.nextFloat(-0F, 0.15F);
            if(this.onGround()) {
                ((ServerLevel) this.level()).sendParticles((new DustParticleOptions(new Vector3f(flute5, flute5, flute5), 1.75f)), this.getX(),
                        this.getY() - 0.175, this.getZ(),
                        200,
                        0.01, 0.01, 0.01,
                        0.1);
            }
        }
    }
    int lungeTicks = 0;
    protected void attemptGrab(){
        lungeTicks = 220;
    }
    protected void moveToTarget() {
        Vec3 pos = this.getTargetPosition();
        bsMove(pos);
    }
    protected void moveRandom() {
        if(this.level() instanceof ServerLevel sl) {
            if(findBlackSabbathRandomPosition(sl, this, 3) != null){
                bsMove(findBlackSabbathRandomPosition(sl, this, 3));
            }
        }
    }
    protected void moveToSafe() {
        if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pbs){
            if(this.level() instanceof ServerLevel sl) {
                if(findBlackSabbathRandomPosition(sl, this, 7) != null){
                        bsMove(findBlackSabbathRandomPosition(sl, this, 7));
                }
            }
        }
    }

    int ticksUntilNextPathRecalculation = 5;
    public int seeTime = -1;

    public void bsMove(Vec3 targetPos) {
        ticksUntilNextPathRecalculation--;
        if (ticksUntilNextPathRecalculation <= 0) {
            if(!isBlackSabbathUnderLight()) {
                ticksUntilNextPathRecalculation = 5;
            } else {
                ticksUntilNextPathRecalculation = 20;
            }
            Path newPath;
            if(targetPos != null) {
                newPath = this.getNavigation().createPath(targetPos.x, targetPos.y, targetPos.z, 0);
            } else {
                newPath = null;
            }
            if (newPath == null) { return; }
            if(!getThrowable() && targetSabbath() != null && hasLineOfSight(targetSabbath())) {
                this.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ()));
            }
            if (!this.getNavigation().moveTo(newPath, 1.6f))
                ticksUntilNextPathRecalculation += 5;
        }
    }

    private static boolean performRangedAttackUnique(BlackSabbathEntity bs, LivingEntity target){
        if(!bs.level().isClientSide && target != null && bs.getUser() != null) {
            if (bs.getUserData(bs.getUser()) != null && bs.getUserData(bs.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath PBS) {
                Vec3 rots = bs.getRotations(target);
                float X = (float) rots.x() * 180 / (float) Math.PI + 180;
                float Y = (float) rots.y * 180 / (float) Math.PI;
                ItemStack item = bs.getHeldItemSabbath();
                if (item.getItem() instanceof ArrowItem) {
                    ArrowItem $$10 = (ArrowItem) item.getItem();
                    AbstractArrow $$11 = $$10.createArrow(bs.getUser().level(), item, bs.getUser());
                    $$11.setPos(bs.position().x, bs.getEyeY() - 0.1, bs.position().z);
                    $$11.shootFromRotation(bs, X, Y, 0.0F, 1.5F, 0.5F);
                    $$11.pickup = AbstractArrow.Pickup.ALLOWED;
                    bs.level().addFreshEntity($$11);
                } else if (item.getItem() instanceof EnderpearlItem) {
                    ThrownEnderpearl $$7 = new ThrownEnderpearl(bs.getUser().level(), bs.getUser());
                    $$7.setPos(bs.position().x, bs.getEyeY() - 0.1, bs.position().z);
                    $$7.setItem(item);
                    $$7.shootFromRotation(bs, X, Y,  0.0F, 1.5F, 1.0F);
                    bs.level().addFreshEntity($$7);
                    bs.level().playSound(null, bs.getX(), bs.getY(), bs.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (bs.getRandom().nextFloat() * 0.4F + 0.8F));
                } else if (item.getItem() instanceof SnowballItem) {
                    Snowball $$7 = new Snowball(bs.getUser().level(), bs.getUser());
                    $$7.setPos(bs.position().x, bs.getEyeY() - 0.1, bs.position().z);
                    $$7.setItem(item);
                    $$7.shootFromRotation(bs, X, Y,  0.0F, 1.5F, 1.0F);
                    bs.level().addFreshEntity($$7);
                    bs.level().playSound(null, bs.getX(), bs.getY(), bs.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (bs.getRandom().nextFloat() * 0.4F + 0.8F));
                } else if (item.getItem() instanceof EggItem) {
                    ThrownEgg $$7 = new ThrownEgg(bs.getUser().level(), bs.getUser());
                    $$7.setPos(bs.position().x, bs.getEyeY() - 0.1, bs.position().z);
                    $$7.setItem(item);
                    $$7.shootFromRotation(bs, X, Y,  0.0F, 1.5F, 1.0F);
                    bs.level().addFreshEntity($$7);
                    bs.level().playSound(null, bs.getX(), bs.getY(), bs.getZ(), SoundEvents.EGG_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (bs.getRandom().nextFloat() * 0.4F + 0.8F));
                } else if (item.getItem() instanceof TridentItem || item.getItem() instanceof HarpoonItem) {
                    item.hurtAndBreak(1, bs, $$1x -> $$1x.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                        if (item.getItem() instanceof TridentItem) {
                            ThrownTrident $$7 = new ThrownTrident(bs.getUser().level(), bs.getUser(), item);
                            $$7.setPos(bs.position().x, bs.getEyeY() - 0.1, bs.position().z);
                            $$7.shootFromRotation(bs, X, Y, 0.0F, 2.5F, 1.0F);
                            if (EnchantmentHelper.getLoyalty(item) > 0) {
                                bs.setTridentReturn($$7);
                            }
                            bs.level().addFreshEntity($$7);
                            bs.level().playSound(null, $$7, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                            $$7.pickup = AbstractArrow.Pickup.ALLOWED;
                        } else {
                            HarpoonEntity $$7 = new HarpoonEntity(bs.getUser().level(), bs.getUser(), item);
                            $$7.setPos(bs.position().x, bs.getEyeY() - 0.1, bs.position().z);
                            $$7.shootFromRotation(bs, X, Y, 0.0F, 2.5F, 1.0F);
                            $$7.isBlackSabbathShot = true;
                            bs.level().addFreshEntity($$7);
                            bs.level().playSound(null, $$7, ModSounds.HARPOON_THROW_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
                            $$7.pickup = AbstractArrow.Pickup.ALLOWED;
                        }
                } else if (item.getItem() instanceof KnifeItem) {
                    int knifeCount = 1;
                    boolean bundle = item.is(ModItems.KNIFE_BUNDLE);
                    if (bundle){knifeCount=4;}
                    for (int i = 0; i< knifeCount; i++) {
                        KnifeEntity $$7 = new KnifeEntity(bs.getUser().level(), bs.getUser(), item);
                        if (bundle){
                            $$7.shootFromRotationWithVariance(bs, X, Y, -0.5F, 1.5F, 1.0F);
                        } else {
                            $$7.shootFromRotation(bs, X, Y, -0.5F, 1.5F, 1.0F);
                        }
                        $$7.setPos(bs.position().x, bs.getEyeY() - 0.1, bs.position().z);
                        bs.level().addFreshEntity($$7);
                        if (i == 0) {
                            if (bundle) {
                                bs.level().playSound(null, $$7, ModSounds.KNIFE_BUNDLE_THROW_SOUND_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
                            } else {
                                bs.level().playSound(null, $$7, ModSounds.KNIFE_THROW_SOUND_EVENT, SoundSource.PLAYERS, 1.0F, 1.0F);
                            }
                        }
                    }
                } else if (item.getItem() instanceof MatchItem) {
                    int knifeCount = 1;
                    boolean bundle = item.is(ModItems.MATCH_BUNDLE);
                    if (bundle){knifeCount=4;}
                    for (int i = 0; i< knifeCount; i++) {
                        MatchEntity $$7 = new MatchEntity(bs, bs.level());
                        $$7.setPos(bs.position().x, bs.getEyeY() - 0.1, bs.position().z);
                        if (bundle) {
                            $$7.isBundle = true;
                            $$7.shootFromRotationWithVariance(bs, X, Y, -3F, 0.9F, 1.0F);
                        } else {
                            $$7.shootFromRotation(bs, X, Y, -3F, 0.9F, 1.0F);
                        }

                        bs.level().addFreshEntity($$7);
                        if (i == 0) {
                            bs.level().playSound(null, $$7, ModSounds.MATCH_THROW_EVENT, SoundSource.PLAYERS, 0.9F, 1.0F);
                        }
                    }
                } else if(item.getItem() instanceof GasolineBucketItem){
                    GasolineSplatterEntity $$7 = new GasolineSplatterEntity(bs, bs.level());
                    $$7.shootFromRotation(bs, X, Y, -7, 0.6F, 1.0F);
                    bs.level().addFreshEntity($$7);
                    bs.level().playSound(null, $$7, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 0.85F, 1.0F);
                } else if(item.getItem() instanceof FleshBucketItem fb){
                    FleshPileEntity $$7 = new FleshPileEntity(bs, bs.level(),fb.getMaxDamage()-item.getDamageValue());
                    $$7.shootFromRotation(bs, X, Y, -7, 0.6F, 1.0F);
                    bs.level().addFreshEntity($$7);
                    bs.level().playSound(null, $$7, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 0.85F, 1.0F);
                }else if(item.getItem() instanceof GasolineCanItem){
                    GasolineCanEntity $$7 = new GasolineCanEntity(bs, bs.level());
                    $$7.shootFromRotation(bs, X, Y, -25F, 0.48F, 1.0F);
                    bs.level().addFreshEntity($$7);
                    bs.level().playSound(null, $$7, ModSounds.GAS_CAN_THROW_EVENT, SoundSource.PLAYERS, 0.85F, 1.0F);
                } else if (item.getItem() instanceof ThrowablePotionItem){
                    ThrownPotion $$4 = new ThrownPotion(bs.getUser().level(), bs.getUser());
                    $$4.setItem(item);
                    $$4.setPos(bs.position().x, bs.getEyeY() - 0.1, bs.position().z);
                    $$4.shootFromRotation(bs, X, Y, -20.0F, 0.5F, 1.0F);
                    bs.level().addFreshEntity($$4);
                    if(item.getItem() instanceof LingeringPotionItem){
                        bs.level().playSound(null, bs.getX(), bs.getY(), bs.getZ(), SoundEvents.LINGERING_POTION_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (bs.getRandom().nextFloat() * 0.4F + 0.8F));
                    } else {
                        bs.level().playSound(null, bs.getX(), bs.getY(), bs.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (bs.getRandom().nextFloat() * 0.4F + 0.8F));
                    }
                }
            }
        }
        ItemStack item = bs.getHeldItemSabbath();
            if (!(item.getItem() instanceof GasolineBucketItem || item.getItem() instanceof FleshBucketItem)) {
                if (bs.getUser() instanceof Player pl) {
                    IPlayerEntity play = ((IPlayerEntity) pl);
                    play.roundabout$getBlckSabbathPlayerInventory().getItem(0).shrink(1);
                }
            } else {
                if (bs.getUser() instanceof Player pl) {
                    IPlayerEntity play = ((IPlayerEntity) pl);
                    play.roundabout$getBlckSabbathPlayerInventory().setItem(0, new ItemStack(Items.BUCKET));
                }
            }
        return true;
    }

    void setTridentReturn(AbstractArrow ab){
        if(ab instanceof ThrownTrident tr){
            ((AccessThrownTrident) tr).roundabout$setReturnToBlackSabbath(true);
        }
    }

    public static final float[] ShotPowerFloats = {3.55F, 3.5F, 4F};

    public BlockHitResult getTargetPos() {
        Vec3 vec3d = this.getEyePosition(0);
        Vec3 vec3d2 = this.getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * 60, vec3d2.y * 60, vec3d2.z * 60);
        return this.level().clip(new ClipContext(vec3d, vec3d3,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

    }

    public Vec3 getEyeP(float d) {
        return this.getPosition(d).add(0, 0.15, 0);
    }

    public Vec3 getRotations(Entity target) {
        Vec3 targetPos = getTargetPos().getLocation();
        if (target != null) {
            targetPos = target.getEyePosition(1);

            double dist = targetPos.distanceTo(this.getPosition(1));
            double time = dist / ShotPowerFloats[1];
            time *= 1.4;
            Vec3 vec = target.getDeltaMovement();
            if (target instanceof Player) {
                if (Math.abs(vec.y) < 3) {
                    vec = new Vec3(vec.x, 0, vec.z);
                }
            }
            targetPos = targetPos.add(vec.multiply(time, time, time));

        }
        double x = (targetPos.x() - this.getPosition(0).x());
        double z = (targetPos.z() - this.getPosition(0).z());
        float rot = (float) (Math.atan2(z, x) - Math.PI / 2);

        double hy = (targetPos.y() - 0.25 - (this.getEyeP(0).y()));
        double hd = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));

        float hrot = (float) (Math.atan2(hd, hy) + Math.PI / 2);

        if (target != null) {
            return new Vec3(hrot, rot, 0);
        }

        return new Vec3(0, 0, 0);
    }

    @Override
    public void performRangedAttack(LivingEntity var1, float var2) {

    }
}
