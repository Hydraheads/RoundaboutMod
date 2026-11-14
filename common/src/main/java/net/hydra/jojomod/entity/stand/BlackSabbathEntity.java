package net.hydra.jojomod.entity.stand;

import com.google.common.collect.ImmutableSet;
import net.hydra.jojomod.access.*;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.models.stand.MandomBlackSabbathModel;
import net.hydra.jojomod.client.models.stand.RattModel;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.badCompanyTroops.BCTroop;
import net.hydra.jojomod.entity.goals.BlackSabbathRangedAttackGoal;
import net.hydra.jojomod.entity.mobs.StrayCatEntity;
import net.hydra.jojomod.entity.navigation.BlackSabbathNavigation;
import net.hydra.jojomod.entity.objects.GrabbedMobSoul;
import net.hydra.jojomod.entity.objects.GrabbedMobSoulPlayerLarge;
import net.hydra.jojomod.entity.objects.GrabbedMobSoulPlayerSlim;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.*;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.*;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Position;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.*;

public class BlackSabbathEntity extends StandEntity implements HasCustomInventoryScreen, RangedAttackMob {

    private static final EntityDataAccessor<Boolean> HAS_TARGETS = SynchedEntityData.defineId(BlackSabbathEntity.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> ANIMATION = SynchedEntityData.defineId(BlackSabbathEntity.class,
            EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> SLIM_SOUL = SynchedEntityData.defineId(BlackSabbathEntity.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> NOT_RENDER_IT = SynchedEntityData.defineId(BlackSabbathEntity.class,
            EntityDataSerializers.BOOLEAN);
    /**Food that villagers would do a mukbang video about*/
    private static final Set<Item> WANTED_ITEMS = ImmutableSet.of(Items.BREAD, Items.POTATO, Items.CARROT, Items.WHEAT, Items.WHEAT_SEEDS, Items.BEETROOT, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD);
    private static final EntityDataAccessor<Integer> SLIDER =
            SynchedEntityData.defineId(BlackSabbathEntity.class, EntityDataSerializers.INT);
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
    public final AnimationState grabOne = new AnimationState();
    public final AnimationState grabJustice = new AnimationState();
    public final AnimationState grabHeyYa = new AnimationState();
 @Override
 public void setupAnimationStates() {
     super.setupAnimationStates();
     Byte bt = this.getBsAnimation();
     if(this.getUser() == null){
         if(this.getBsGrabbed() != 0){
             grabbedStandByBlackSabbath.startIfStopped(this.tickCount);
             burningCripple.stop();
             burningStart.stop();
             strafeEmerge.stop();
             strafeWalk.stop();
             catching.stop();
             this.emerge.stop();
             this.diving.stop();
             this.chest_open.stop();
             this.coat_open.stop();
             walk.stop();
             stando.stop();
             grabOne.stop();
             grabJustice.stop();
             grabHeyYa.stop();
             return;
         }
     }
     switch (bt) {
         case 0 -> {
             coat_open.startIfStopped(this.tickCount);
             this.emerge.start(this.tickCount);
         }
         /* 1 and 2: Black Sabbath chest */
         case 1 -> {
             this.emerge.stop();
             this.coat_open.stop();
             chest_close.stop();
             this.chest_open.startIfStopped(this.tickCount);
         }
         case 2 -> {
             this.emerge.stop();
             this.chest_open.stop();
             this.coat_open.stop();
             this.chest_close.startIfStopped(this.tickCount);
         }
         /*3: floating behind user*/
         case 3 -> {
             this.emerge.stop();
             this.chest_open.stop();
             this.chest_close.stop();
             this.floating.startIfStopped(this.tickCount);
             this.coat_open.startIfStopped(this.tickCount);
         }
         /*Hunting Mode*/
         case 4 -> {
             burningCripple.stop();
             burningStart.stop();
             catching.stop();
             walk.stop();
             stando.stop();
             this.coat_open.stop();
             strafeEmerge.stop();
             strafeWalk.stop();
             burningDive.stop();
             diving.startIfStopped(this.tickCount);
             emerge.stop();
         }
         case 5 -> {
             burningCripple.stop();
             burningStart.stop();
             catching.stop();
             walk.stop();
             stando.stop();
             this.coat_open.stop();
             strafeEmerge.stop();
             strafeWalk.stop();
             emerge.startIfStopped(this.tickCount);
             diving.stop();
             burningDive.stop();
         }
         case 6 -> {
             burningCripple.stop();
             burningStart.stop();
             walk.stop();
             stando.stop();
             this.coat_open.stop();
             strafeEmerge.stop();
             strafeWalk.stop();
             catching.startIfStopped(this.tickCount);
             emerge.stop();
         }
         case 7 -> {
             this.chest_close.stop();
             strafeEmerge.stop();
             strafeWalk.stop();
             coat_open.stop();
             emerge.stop();
             catching.stop();
             diving.stop();
             walk.stop();
             stando.stop();
             burningCripple.stop();
             burningStart.stop();
             grabOne.stop();
             grabJustice.stop();
             grabHeyYa.stop();
             burningDive.startIfStopped(this.tickCount);
         }
         case 8 -> {
             grabOne.stop();
             grabJustice.stop();
             grabHeyYa.stop();
             burningCripple.stop();
             burningStart.stop();
             this.chest_close.stop();
             strafeEmerge.stop();
             strafeWalk.stop();
             coat_open.stop();
             emerge.stop();
             catching.stop();
             burningDive.stop();
             walk.stop();
             stando.stop();
             diving.startIfStopped(this.tickCount);
         }
         case 9 -> {
             burningCripple.stop();
             burningStart.stop();
             strafeEmerge.stop();
             strafeWalk.stop();
             coat_open.stop();
             emerge.stop();
             catching.stop();
             stando.stop();
             walk.startIfStopped(this.tickCount);
         }
         case 10 -> {
             burningCripple.stop();
             burningStart.stop();
             strafeEmerge.stop();
             strafeWalk.stop();
             coat_open.stop();
             emerge.stop();
             catching.stop();
             walk.stop();
             stando.stop();
         }
         case 11 -> {
             this.coat_open.stop();
             burningDive.stop();
             burningCripple.stop();
             burningStart.stop();
             walk.stop();
             stando.stop();
             diving.stop();
             catching.stop();
             emerge.stop();
             grabJustice.stop();
             grabHeyYa.stop();
             grabOne.startIfStopped(this.tickCount);
         }
         case 12 -> {
             burningDive.stop();
             burningCripple.stop();
             burningStart.stop();
             walk.stop();
             stando.stop();
             diving.stop();
             catching.stop();
             emerge.stop();
         }
         case 13 -> {
             this.emerge.stop();
             diving.stop();
             burningDive.stop();
             burningCripple.stop();
             burningStart.stop();
             this.coat_open.stop();
             chest_close.stop();
             if (animationTick2 > 0) {
                 strafeWalk.stop();
                 strafeEmerge.startIfStopped(this.tickCount);
             } else {
                 strafeEmerge.stop();
                 strafeWalk.startIfStopped(this.tickCount);
             }
         }
         case 14 -> {
             diving.stop();
             burningDive.stop();
             this.emerge.stop();
             burningCripple.stop();
             burningStart.stop();
             this.coat_open.stop();
             strafeEmerge.stop();
             strafeWalk.stop();
             chest_close.startIfStopped(this.tickCount);
         }
         case 15 -> {
             this.coat_open.stop();
             this.chest_close.stop();
             grabOne.stop();
             strafeEmerge.stop();
             strafeWalk.stop();
             catching.stop();
             this.emerge.stop();
             burningDive.stop();
             this.diving.stop();
             walk.stop();
             stando.stop();
             this.chest_open.stop();
             grabJustice.stop();
             grabHeyYa.stop();
             burningStart.startIfStopped(this.tickCount);
             burningCripple.stop();
         }
         case 16 -> {
             this.coat_open.stop();
             this.chest_close.stop();
             grabOne.stop();
             grabJustice.stop();
             grabHeyYa.stop();
             strafeEmerge.stop();
             strafeWalk.stop();
             catching.stop();
             this.emerge.stop();
             burningDive.stop();
             this.diving.stop();
             walk.stop();
             stando.stop();
             this.chest_open.stop();
             burningStart.stop();
             burningCripple.startIfStopped(this.tickCount);
         }
         case 17 -> {
             burningCripple.stop();
             burningStart.stop();
             strafeEmerge.stop();
             strafeWalk.stop();
             catching.stop();
             this.emerge.stop();
             this.diving.stop();
             this.chest_open.stop();
             this.coat_open.stop();
             walk.stop();
             stando.stop();
             grabOne.stop();
             grabJustice.stop();
             grabHeyYa.stop();
             this.chest_close.startIfStopped(this.tickCount);
         }
         case 18 -> {
             this.coat_open.stop();
             burningDive.stop();
             burningCripple.stop();
             burningStart.stop();
             walk.stop();
             stando.stop();
             diving.stop();
             catching.stop();
             emerge.stop();
             grabJustice.startIfStopped(this.tickCount);
             grabHeyYa.stop();
             grabOne.stop();
         }
         case 19 -> {
             this.coat_open.stop();
             burningDive.stop();
             burningCripple.stop();
             burningStart.stop();
             walk.stop();
             stando.stop();
             diving.stop();
             catching.stop();
             emerge.stop();
             grabHeyYa.startIfStopped(this.tickCount);
             grabJustice.stop();
             grabOne.stop();
         }
     }
 }

    public final Byte getBsAnimation() {
        return this.entityData.get(ANIMATION);
    }
    public final void setBsAnimations(Byte bt) {
        this.entityData.set(ANIMATION, bt);
    }

 /**Changes animation states dynamically server sided*/
    public void animationStateTick(){
     if(!this.level().isClientSide()){
         if(this.getUser() != null){
             if (((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb){
                 switch (pb.moveMode) {
                     case 1 -> {
                         if (pb.active) {
                             setBsAnimations((byte)1);
                         } else {
                             setBsAnimations((byte)2);
                         }
                     }
                     case 2 -> {
                         setBsAnimations((byte)3);
                     }
                     case 3 -> {
                         if(getHasTargets()){
                             if (!isBlackSabbathUnderLight()) {
                                 if (!isOnFire()) {
                                     animationTick = 20;
                                 }
                                 if (!getThrowable() && !getTridentLoyalty()) {
                                     if(meleeInteractionTicks < 1) {
                                         this.chest_close.stop();
                                         this.grabOne.stop();
                                         this.coat_open.stop();
                                         if ((targetSabbath() != null && !isUnderSunlight(targetSabbath()))) {
                                             if (lungeTicks < 10) {
                                                 if(!getTridentLoyalty()) {
                                                     setBsAnimations((byte) 4);
                                                 }
                                             }
                                             if (lungeTicks < 220 && lungeTicks > 210) {
                                                 setBsAnimations((byte)5);
                                             } else {
                                                 emerge.stop();
                                             }
                                             if (lungeTicks <= 210 && lungeTicks > 180) {
                                                 setBsAnimations((byte)6);
                                             }
                                             if (lungeTicks <= 50 && lungeTicks > 35) {
                                                 if (burningCripple.isStarted() || burningStart.isStarted() || isOnFire()) {
                                                     if (!diving.isStarted()) {
                                                         setBsAnimations((byte)7);
                                                     }
                                                 } else {
                                                         if (!burningDive.isStarted()) {
                                                             if(!getTridentLoyalty()) {
                                                                 setBsAnimations((byte) 8);
                                                             }
                                                         }
                                                 }
                                             }
                                         } else {
                                             if (lungeTicks <= 50 && lungeTicks > 35) {
                                                 if (burningCripple.isStarted() || burningStart.isStarted() || isOnFire()) {
                                                     if (!diving.isStarted()) {
                                                         setBsAnimations((byte)7);
                                                     }
                                                 } else {
                                                         if (!burningDive.isStarted()) {
                                                             if(!getTridentLoyalty()) {
                                                                 setBsAnimations((byte) 8);
                                                             }
                                                         }
                                                 }
                                             }
                                             if (isWalking && lungeTicks > 50) {
                                                 if (this.getDeltaMovement().x() != 0 || this.getDeltaMovement().z() != 0) {
                                                     setBsAnimations((byte)9);
                                                 } else {
                                                     setBsAnimations((byte)10);
                                                 }
                                             } else {
                                                 setBsAnimations((byte)10);
                                             }
                                         }
                                     } else {
                                         if (this.level().getEntity(this.getGrabbing()) != null) {
                                             if(meleeInteractionTicks > 20) {
                                                 if(this.grabbedSoul instanceof JusticeEntity) {
                                                     if(this.grabbedSoul instanceof DarkMirageEntity){
                                                         setBsAnimations((byte) 11);
                                                     } else {
                                                         setBsAnimations((byte) 18);
                                                     }
                                                 } else if (this.grabbedSoul instanceof HeyYaEntity){
                                                     setBsAnimations((byte) 19);
                                                 }else {
                                                     setBsAnimations((byte) 11);
                                                 }
                                             } else {
                                                 setBsAnimations((byte)12);
                                             }
                                         }
                                     }
                                 }else {
                                     if (lungeTicks <= 51 && lungeTicks > 35) {
                                         strafeEmerge.stop();
                                         strafeWalk.stop();
                                         if (burningCripple.isStarted() || burningStart.isStarted() || isOnFire()) {
                                             if (!diving.isStarted()) {
                                                 setBsAnimations((byte) 7);
                                             }
                                         } else {
                                                 if (!burningDive.isStarted()) {
                                                     if(!getTridentLoyalty()) {
                                                         setBsAnimations((byte) 8);
                                                     }
                                                 }
                                         }
                                     }
                                     if (getStrafing()) {
                                             setBsAnimations((byte) 13);
                                     } else {
                                            setBsAnimations((byte) 14);
                                     }
                                 }
                             } else {
                                 if(animationTick >= 1){
                                     animationTick--;
                                 }
                                 if(animationTick > 1){
                                     setBsAnimations((byte)15);
                                 } else {
                                     setBsAnimations((byte)16);
                                 }
                             }
                         } else {
                             setBsAnimations((byte)17);
                         }
                     }
                 }
             }
         } else {
             setBsAnimations((byte)0);
         }
     }
    }

    int animationTick2 = 14;
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
        if(getGrabbing() == 0) {
            super.knockback($$0 * 1.65D, $$1, $$2 * 1.65D);
        } else {
            super.knockback($$0 * 1.25D, $$1, $$2 * 1.25D);
        }
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
    public boolean canBeAffected(MobEffectInstance $$0){
        return $$0.getEffect() == ModEffects.INFINITE_SPIN;
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
        return super.isNoGravity();
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
        if (!level().isClientSide) {
            if (this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb) {
                setHasTargets(!pb.blackSabbathTargets.isEmpty());
            }
        }
        if (this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb) {
            if(level().isClientSide()) {
                if (targetSabbath() instanceof Player P) {
                    setSlimSoul(isWideSkin(P));
                }
            }
        }
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
        if (!level().isClientSide) {
            if (this.getUser() != null && this.getUser() instanceof Player pl) {
                if (!level().isClientSide()) {
                    IPlayerEntity play = ((IPlayerEntity) pl);
                    ItemStack blackSabbathFirstSlot = play.roundabout$getBlckSabbathPlayerInventory().getItem(0);
                    if (blackSabbathFirstSlot != getHeldItemSabbath()) {
                        setHeldItemSabbath(blackSabbathFirstSlot);
                    }
                    if (!level().isClientSide() && !getTridentLoyalty()) {
                        if (blackSabbathFirstSlot.getItem() instanceof TridentItem tr && EnchantmentHelper.getLoyalty(blackSabbathFirstSlot) > 0) {
                            setTridentLoyalty(true);
                        } else if (blackSabbathFirstSlot.getItem() instanceof HarpoonItem) {
                            setTridentLoyalty(true);
                        }
                    }
                }
            }
        }

        if(getHunting()){
            huntingTick();
            hurtBlackSabbath();
            //System.out.println(this.level());
            if(meleeInteractionTicks > -1){
                meleeInteractionTicks--;
                soulGrabbingTick();
            } else {
                setMeleeInteractionTicks(-1);
            }
        }
        animationStateTick();
        super.tick();
        travelAhead(Entity::setPos);
        travelAheadSoul(Entity::setPos);
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
    private static final EntityDataAccessor<Integer> IS_GRABBING =
            SynchedEntityData.defineId(BlackSabbathEntity.class, EntityDataSerializers.INT);

    /**Which entity it's grabbing*/
    public final Integer getGrabbing() {
        return this.entityData.get(IS_GRABBING);
    }
    /**Set which entity it's grabbing*/
    public final void setGrabbing(Integer le) {
        this.entityData.set(IS_GRABBING, le);
    }

    /**item slider*/
    public final Integer slider() {
        return this.entityData.get(SLIDER);
    }
    public final void setSlider(Integer i) {
        this.entityData.set(SLIDER, i);
    }

    public final Boolean getNotRenderItem() {
        return this.entityData.get(NOT_RENDER_IT);
    }
    public final void setNotRenderItem(Boolean bool) {
        this.entityData.set(NOT_RENDER_IT, bool);
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
    /**0 = no items; 1 = ranged mode; 2 = other items; 3 = open chest (animations only!!!)*/
    public final void setMoveMode(Integer i) {
        this.entityData.set(MOVEMENT_MODE, i);
        this.entityData.set(MUST_UNRENDER, true);
    }
    public final Boolean getTridentLoyalty() {
        return this.entityData.get(IS_TRIDENT);
    }
    public final void setTridentLoyalty(Boolean bool) {
        this.entityData.set(IS_TRIDENT, bool);
    }
    public final Boolean getHasTargets() {
        return this.entityData.get(HAS_TARGETS);
    }
    public final void setHasTargets(Boolean bool) {
        this.entityData.set(HAS_TARGETS, bool);
    }
    public final Boolean getSlimSoul() {
        return this.entityData.get(SLIM_SOUL);
    }
    public final void setSlimSoul(Boolean bool) {
        this.entityData.set(SLIM_SOUL, bool);
    }

    private static final EntityDataAccessor<Boolean> IS_TRIDENT =
            SynchedEntityData.defineId(BlackSabbathEntity.class, EntityDataSerializers.BOOLEAN);
    public final Boolean getThrowable() {
        return (this.getHeldItemSabbath().getItem() instanceof ArrowItem && !(this.getHeldItemSabbath().getItem() instanceof RoundaboutArrowItem) || this.getHeldItemSabbath().getItem() instanceof KnifeItem
                || this.getHeldItemSabbath().is(ModItems.KNIFE_BUNDLE) || this.getHeldItemSabbath().getItem() instanceof TridentItem && EnchantmentHelper.getRiptide(getHeldItemSabbath()) <= 0 && !(getHeldItemSabbath().getDamageValue() >= getHeldItemSabbath().getMaxDamage() - 1)
                || this.getHeldItemSabbath().getItem() instanceof EggItem || this.getHeldItemSabbath().getItem() instanceof SnowballItem || this.getHeldItemSabbath().getItem() instanceof ThrowablePotionItem
                || this.getHeldItemSabbath().getItem() instanceof EnderpearlItem || this.getHeldItemSabbath().getItem() instanceof GasolineBucketItem || this.getHeldItemSabbath().getItem() instanceof GasolineCanItem
                || this.getHeldItemSabbath().getItem() instanceof MatchItem || this.getHeldItemSabbath().is(ModItems.MATCH_BUNDLE) || this.getHeldItemSabbath().getItem() instanceof FleshBucketItem
                ||this.getHeldItemSabbath().getItem() instanceof HarpoonItem  && !(getHeldItemSabbath().getDamageValue() >= getHeldItemSabbath().getMaxDamage() - 1)) && !(this.getHeldItemSabbath().is(ItemStack.EMPTY.getItem()));
    }
    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(IS_RIDING)) {
            super.defineSynchedData();
            this.entityData.define(ANIMATION, (byte) 0);
            this.entityData.define(CRIPPLED, false);
            this.entityData.define(IS_RIDING, false);
            this.entityData.define(IS_HUNTING, false);
            this.entityData.define(IS_GRABBING, 0);
            this.entityData.define(SLIDER, 0);
            this.entityData.define(NOT_RENDER_IT, false);
            this.entityData.define(IS_STRAFING, false);
            this.entityData.define(IS_TRIDENT, false);
            this.entityData.define(MOVEMENT_MODE, 0);
            this.entityData.define(HELD_ITEM_BLACK_SABBATH, ItemStack.EMPTY);
            this.entityData.define(HAS_TARGETS, true);
            this.entityData.define(MUST_UNRENDER, false);
            this.entityData.define(SLIM_SOUL, false);
        }
    }
    @Override
    public void die(@NotNull DamageSource source) {
        if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pbs){
            ((StandUser)this.getUser()).roundabout$setSealedTicks(300);
            Entity e = this.level().getEntity(this.getGrabbing());
            if(e != null && e instanceof LivingEntity l) {
                ((StandUser)l).roundabout$setGrabbedSoul(false);
                ((StandUser)l).roundabout$setGrabberSabbath(0);
                this.killSoul();
            }
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
            } else if (source.is(ModDamageTypes.INFINITE_SPIN)){
                setUnrender(true);
            }
        }
    }

    private int tickShootCooldown = 50;

    public LivingEntity targetSabbath(){
        if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pbs){
            if(!pbs.blackSabbathTargets.isEmpty()){
                List<LivingEntity> targent = new ArrayList<>(pbs.blackSabbathTargets);
                if(!getThrowable()) {
                    targent.removeIf(this::isUnderSunlight);
                    if (targent != null && !targent.isEmpty()) {
                        List<LivingEntity> targent2 = new ArrayList<>(targent);
                        for (LivingEntity value : targent) {
                            if(((StandUser)value).roundabout$getGrabberSabbath() != 0 && ((StandUser)value).roundabout$getGrabberSabbath() != this.getId()){
                                targent2.remove(value);
                            }
                            if(value.isRemoved() || value.isDeadOrDying() || !value.isAlive()){
                                targent2.remove(value);
                            }
                        }
                        targent = targent2;
                    }
                }

                LivingEntity lv = this.level().getNearestEntity(targent,
                        MainUtil.OFFER_TARGER_CONTEXT, null,
                        this.getX(), this.getY(), this.getZ());

                if(lv != null) {
                    return lv;
                }
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
                    } else if (source.is(ModDamageTypes.INFINITE_SPIN)){
                        return super.hurt(source, amount * 1.618f);
                    }
                }
            } else if (source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.IN_FIRE) || source.is(ModDamageTypes.STAND_FIRE) || source.is(DamageTypes.HOT_FLOOR) || source.is(DamageTypes.LAVA)) {
                return super.hurt(source, amount);
            } else if (source.is(ModDamageTypes.GO_BEYOND)) {
                return super.hurt(source, amount * 30);
            }else if (source.is(ModDamageTypes.INFINITE_SPIN)){
                return super.hurt(source, amount * 1.618f);
            } else if (MainUtil.isStandDamage(source) && (this.getRemainingFireTicks() > 0 || isBlackSabbathUnderLight())) {
                return super.hurt(source, amount * 3);
            }
            this.markHurt();
            return super.hurt(source, 0.0F);
        } else {
            if (source.is(ModDamageTypes.GO_BEYOND)) {
                return super.hurt(source, amount * 30);
            } else if (source.is(ModDamageTypes.INFINITE_SPIN)){
                return super.hurt(source, amount * 1.618f);
            }
            this.markHurt();
        }
        return false;
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource $$0) {
        return ModSounds.BLACK_SABBATH_HURT_EVENT;
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

    int meleeInteractionTicks = -1;
    public void setMeleeInteractionTicks(int i){
        meleeInteractionTicks = i;
    }

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
        AABB abba = this.getBoundingBox().expandTowards(0.15, 9, 0.15);
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
        if (securityTicks2 >= 1) {
            securityTicks2--;
            if (securityTicks2 == 1) {
                setUnrender(false);
            }
        }
        if(!isBlackSabbathUnderLight()) {
            if (this.targetSabbath() != null) {
                if(meleeInteractionTicks < 1) {
                    if (!this.level().isClientSide()) {
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
                } else {}
            } else {
                if(meleeInteractionTicks < 1) {
                    moveRandom();
                    this.getNavigation().setSpeedModifier(0.65);
                }
            }
        } else {
            moveToSafe();
        }
        if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pbs){
            if(pbs.blackSabbathTargets != null){
                if(securityTicks < 1 && lungeTicks < 1 && meleeInteractionTicks < 1) {
                    if(!getStrafing()){
                        if(getGrabbing() == 0){
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
                                            }
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
                if(!getThrowable() && getGrabbing() == 0) {
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
                if(getThrowable() || getTridentLoyalty() || getMoveMode() == 1){
                    shootingTick();
                }
                if(getUnrender() || (lungeTicks < 20 && lungeTicks > 1)){
                    if(!getCrippled() && !getRiding()) {
                        createShadowParticles();
                    }
                }
                if(getUnrender() || getRiding() || getCrippled()){
                    tickShootCooldown = 50;
                }
                if(getUnrender() && shouldReattemptSpawn < 0){
                    isStuck = true;
                } else {
                    isStuck = false;
                }
                if(targetSabbath() != null) {
                    if(getUnrender()){
                        setGrabbing(0);
                    }
                    if(getGrabbing() != 0 && meleeInteractionTicks == -1){
                        setMeleeInteractionTicks(220);
                    }
                    if (!isBlackSabbathUnderLight()) {
                        if (isWalking) {
                            isWalking = false;
                            lungeTicks = 51;
                        }
                        if(!getThrowable() && !getTridentLoyalty() && getMoveMode() != 1) {
                            if(meleeInteractionTicks < 1) {
                                if (lungeTicks < 1) {
                                    if(getGrabbing() == 0) {
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
                                                        this.level().playSound(null, this, ModSounds.BLACK_SABBATH_EMERGE_EVENT, SoundSource.HOSTILE, 0.95F, (float) (0.85F + Math.random() * 0.12f));
                                                    }
                                                    attemptGrab();
                                                }
                                            }
                                        }
                                    }
                                }
                                if (lungeTicks < 215 && this.emerge.isStarted() || lungeTicks > 207 && walk.isStarted()) {
                                    if (getUnrender()) {
                                        this.level().playSound(null, this, ModSounds.BLACK_SABBATH_EMERGE_EVENT, SoundSource.HOSTILE, 0.95F, (float) (0.85F + Math.random() * 0.12f));
                                    }
                                    setUnrender(false);
                                }
                                if (lungeTicks > 200) {
                                    this.getNavigation().setSpeedModifier(0.70);
                                } else if (lungeTicks > 185 && lungeTicks < 195) {
                                    setUnrender(false);
                                    this.getNavigation().setSpeedModifier(2.25);
                                    if (targetSabbath() != null && isTouchingTarget(targetSabbath())) {
                                        if (!isBlackSabbathUnderLight()) {
                                            if (!this.level().isClientSide()) {
                                                setGrabbing(targetSabbath().getId());
                                                if(level().getEntity(getGrabbing()) instanceof LivingEntity e){((StandUser)e).roundabout$setGrabbedSoul(true); ((StandUser)e).roundabout$setGrabberSabbath(this.getId());}
                                                this.level().playSound(null, this, ModSounds.BLACK_SABBATH_GRAB_EVENT, SoundSource.HOSTILE, 0.95F, (float) (0.85F + Math.random() * 0.12f));
                                            }
                                            lungeTicks = 51;
                                            if(!this.level().isClientSide) {
                                                ((StandUser) targetSabbath()).roundabout$setSummonCD(8);
                                                ((StandUser) targetSabbath()).roundabout$setActive(false);
                                                ((StandUser) targetSabbath()).roundabout$tryPower(PowerIndex.NONE, true);
                                                targetSabbath().knockback(0.3f, this.getX() - targetSabbath().getX(), this.getZ() - targetSabbath().getZ());
                                                createSoul();
                                            }
                                        }
                                    }
                                } else if (lungeTicks < 185 && lungeTicks > 40) {
                                    if (!isBlackSabbathUnderLight()) {
                                        this.getNavigation().setSpeedModifier((float) 0);
                                    }
                                }
                                    if (lungeTicks < 40 && lungeTicks >= 1) {
                                        if (!getUnrender()) {
                                            setUnrender(true);
                                        }
                                    }
                            } else {
                                this.getNavigation().setSpeedModifier((float) 0);
                                setUnrender(false);
                            }
                        }
                    } else {
                        if(level().getEntity(getGrabbing()) != null && level().getEntity(getGrabbing()) instanceof LivingEntity e){((StandUser)e).roundabout$setGrabbedSoul(false);killSoul();((StandUser)e).roundabout$setGrabberSabbath(0);}
                        setGrabbing(0);
                        setMeleeInteractionTicks(-1);
                        setUnrender(false);
                        setStrafing(false);
                        lungeTicks = 51;
                    }
                } else {
                    if(getHasTargets()) {
                        if (!isBlackSabbathUnderLight()) {
                            if(!this.level().isClientSide()) {
                                if (!isWalking) {
                                    isWalking = true;
                                }
                            }
                            if(!this.level().isClientSide()) {
                                if (lungeTicks < 15) {
                                    setUnrender(true);
                                    if (isWalking) {
                                        isWalking = false;
                                    }
                                }
                            }
                        } else {
                            if(level().getEntity(getGrabbing()) != null && level().getEntity(getGrabbing()) instanceof LivingEntity e){((StandUser)e).roundabout$setGrabbedSoul(false);killSoul();((StandUser)e).roundabout$setGrabberSabbath(0);}
                            setGrabbing(0);
                            setMeleeInteractionTicks(-1);
                            if (isWalking) {
                                isWalking = false;
                            }
                            setUnrender(false);
                            lungeTicks = 51;
                        }
                    } else {
                        //setUnrender(false);
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

    private void shootingTick(){
        if(getStrafing()){
            tickShootCooldown--;
            if(tickShootCooldown < 1){
                if(targetSabbath() != null && !getUnrender() && ((!this.level().isClientSide() && seeTime >= 59))) {
                        if (getHeldItemSabbath().is(Items.ENDER_PEARL)) {
                            if (hasLineOfSight(targetSabbath()) && MainUtil.cheapDistanceTo(this.getUser().getX(), this.getUser().getY(), this.getUser().getZ(), targetSabbath().getX(), targetSabbath().getY(), targetSabbath().getZ()) > 15) {
                                tickShootCooldown = 200;
                                performRangedAttackUnique(this, targetSabbath());
                            }
                        } else if (getHeldItemSabbath().getItem() instanceof TridentItem || getHeldItemSabbath().getItem() instanceof HarpoonItem) {
                            if(!(getHeldItemSabbath().getDamageValue() >= getHeldItemSabbath().getMaxDamage() - 1)) {
                                tickShootCooldown = 50;
                                performRangedAttackUnique(this, targetSabbath());
                            }
                        } else {
                            tickShootCooldown = 50;
                            performRangedAttackUnique(this, targetSabbath());
                        }
                }
            }
            if(getHeldItemSabbath().is(ItemStack.EMPTY.getItem()) && !getTridentLoyalty()){
                if(getStrafing()){
                    setStrafing(false);
                    tickShootCooldown = 50;
                }
            }
        }
        if(targetSabbath() != null){
            if(getStrafing()){
                if (this.hasLineOfSight(targetSabbath())) {
                    this.seeTime = 60;
                } else {
                    if(seeTime >= 0) {
                        this.seeTime--;
                    }
                }
                if (animationTick2 >= 0) {
                    animationTick2--;
                }
                setUnrender(false);
            } else {
                if (animationTick2 < 14) {
                    animationTick2++;
                }
                if(animationTick2 >= 14) {
                    setUnrender(true);
                } else {
                    if (this.getNavigation() != null) {
                        getNavigation().setSpeedModifier(0);
                    }
                }
            }
        }
    }

    private StandEntity grabbedSoul = null;

    private void createSoul(){
        StandEntity s = this.getNewSoulGrabbed();
        Entity grab = this.level().getEntity(getGrabbing());
        if(!this.level().isClientSide()) {
            if (s != null && grab != null) {
                s.setBsGrabbed(this.getId());
                if(s instanceof GrabbedMobSoul) {
                    if(grab instanceof LivingEntity le) {
                        s.setMaster(le);
                    }
                } else {
                    StandPowers pw = ((StandUser)grab).roundabout$getStandPowers();
                    s.setMaster(null);
                    if(pw != null){
                        s.setSkin(pw.standSkin);
                        if(s instanceof RattEntity r){
                            r.setSavedSkin(pw.standSkin);
                        }
                        if(pw instanceof PowersSurvivor ps){
                            ps.removeAllSurvivors();
                        }
                    }
                }
                s.setFadeOut((byte) 8);
                s.absMoveTo(grab.position().x, grab.position().y, grab.position().z);
                s.setYRot((getYRot() % 360) - 180);
                PowerTypes.copyPlaneOfExisting(this, s);
                grabbedSoul = s;
                this.level().addFreshEntity(s);
                if (this.getUser() != null && ((StandUser) this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pbs) {
                    pbs.setGrabbedSoul(s);
                }
            }
        }
    }

    private StandEntity getNewSoulGrabbed(){
        Entity grab = this.level().getEntity(getGrabbing());
        if(grab != null && grab instanceof LivingEntity l){
            StandPowers sp = ((StandUser)l).roundabout$getStandPowers();
            StandEntity s = sp.getNewStandEntity();
            boolean regSoul = s == null && (!(sp instanceof PowersBadCompany) && !(sp instanceof PowersBlackSabbath));
            if(!regSoul) {
                if (s != null) {
                    return s;
                }
            } else if (sp instanceof PowersHeyYa){
                return ModEntities.GRABBED_HEY_YA.create(this.level());
            } else if (sp instanceof PowersMandom){
                return ModEntities.GRABBED_MANDOM.create(this.level());
            }
            else {
                if(l instanceof Player){
                    if(getSlimSoul()) {
                        return ModEntities.GRABBED_PLAYER_SOUL.create(this.level());
                    } else {
                        return ModEntities.GRABBED_PLAYER_SOUL_SLIM.create(this.level());
                    }
                } else {
                    return ModEntities.GRABBED_MOB_SOUL.create(this.level());
                }
            }
        }
        return null;
    }

    public boolean isWideSkin(Player p) {
        return ((AbstractClientPlayer) p).getModelName().equals("default");
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
            if(getGrabbing() != 0) {
                if ((!getStrafing() && !getTridentLoyalty())) {
                    this.lookAt(EntityAnchorArgument.Anchor.EYES, new Vec3(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ()));
                }
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
        float rot = (float) (Math.atan2(z, x) - Math.PI / 1.9);

        double hy = (targetPos.y() - 0.35 - (this.getEyeP(0).y()));
        double hd = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));

        float hrot = (float) (Math.atan2(hd, hy) + Math.PI / 1.9);

        if (target != null) {
            return new Vec3(hrot, rot, 0);
        }

        return new Vec3(0, 0, 0);
    }

    private void swordHit(float damage, LivingEntity attacked, Entity attacker, ItemStack item){
        if(this.getUser() != null) {
            if(attacker instanceof Player){
                attacked.hurt(ModDamageTypes.of(level(), DamageTypes.PLAYER_ATTACK, this.getUser()), damage + 1);
            } else {
                attacked.hurt(ModDamageTypes.of(level(), DamageTypes.MOB_ATTACK, this.getUser()), damage + 1);
            }
            this.level().playSound((Player) null, this, ModSounds.BLACK_SABBATH_STAB_EVENT, SoundSource.HOSTILE, 2.0F, (float) (0.85F + Math.random() * 0.12f));
            attacked.invulnerableTime = 0;
            attacked.hurt(ModDamageTypes.of(level(), ModDamageTypes.STAND, this.getUser()), 2);
            if (item.getItem() instanceof GlaiveItem || item.getItem() instanceof SacrificialDaggerItem
                    || item.getItem() instanceof ScissorItem || item.getItem() instanceof ShearsItem) {
                if(item.getItem() instanceof GlaiveItem || item.getItem() instanceof SacrificialDaggerItem) {MainUtil.makeMobBleed(attacked);}
                MainUtil.makeBleed(attacked, 0, 300, this.getUser());
            }
            int ench = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, item);
            if (ench >= 1) {
                attacked.setSecondsOnFire((ench) * 4);
            }
        }
    }

    private static void play(Level p_220127_, Instrument p_220129_, BlackSabbathEntity bs) {
        SoundEvent soundevent = p_220129_.soundEvent().value();
        float f = p_220129_.range() / 16.0F;
        p_220127_.playSound(null, bs, soundevent, SoundSource.RECORDS, f, 1.0F);
    }

    public int slideTicks = 0;
    void setShouldSlide(boolean yes){shouldSlide = yes;}
    public boolean shouldSlide = false;
    void setShouldSlidePotionsAndFood(boolean yes){shouldSlidePotionsAndFood = yes;}
    public boolean shouldSlidePotionsAndFood = false;
    private void soulGrabbingTick(){
        ItemStack i = this.getHeldItemSabbath();
        Entity le = this.level().getEntity(getGrabbing());
        boolean flag1 = (i.getItem() instanceof ShearsItem || i.getItem() instanceof ScissorItem) && !(le instanceof Sheep) || i.getItem() instanceof SwordItem || i.getItem() instanceof SacrificialDaggerItem || i.getItem() instanceof DiggerItem || i.getItem() instanceof GlaiveItem || i.getItem() instanceof WarhammerItem;
        boolean flag2 = i.getItem() instanceof StandArrowItem && !(i.getDamageValue() >= i.getMaxDamage()) || i.getItem() instanceof WorthyArrowItem && this.getUser() instanceof Player p && p.isCreative();
        boolean flag2visual = i.getItem() instanceof StandArrowItem || i.getItem() instanceof WorthyArrowItem && this.getUser() instanceof Player p && p.isCreative();
        boolean flag3 = i.getItem() instanceof ArmorItem || i.getItem() instanceof ElytraItem;
        boolean flag4 = i.getItem() instanceof PotionItem || i.getItem().getFoodProperties() != null || i.getItem() instanceof MilkBucketItem;
        boolean flag5 = (i.getItem() instanceof ShearsItem || i.getItem() instanceof ScissorItem) && le instanceof Sheep sheep && !(sheep.isSheared());

        boolean allYouCanEat = le instanceof Player p && i.getItem().getFoodProperties() != null && p.canEat(i.getItem().getFoodProperties().canAlwaysEat()) || le instanceof Animal || le instanceof Villager || i.getItem() instanceof MilkBucketItem;
        if(!level().isClientSide && this.getUser() != null){
            setSlider(slideTicks);
        }

        int inte = slideTicks;
        if(!flag4 && !flag5) {
            if (shouldSlide) {
                if (slideTicks <= 20) {
                    slideTicks = inte + 10;
                }
            } else {
                if (slideTicks > 0) {
                    slideTicks = inte - 5;
                }
            }
        } else {
            if (shouldSlidePotionsAndFood) {
                if (slideTicks <= 20) {
                    slideTicks = inte + 2;
                }
            } else {
                if (slideTicks > 0) {
                    slideTicks = 0;
                }
            }
        }

        if(this.getUser() != null && ((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pbs){
        if(meleeInteractionTicks > 0) {
            if(!this.level().isClientSide()){
                setUnrender(false);
            }
            if (le != null && le instanceof LivingEntity e) {
                if (meleeInteractionTicks > 1) {
                    lungeTicks = 51;
                    ((StandUser) e).roundabout$setGrabbedSoul(true);
                    ((StandUser) e).roundabout$setGrabberSabbath(this.getId());
                    if (this.grabbedSoul == null) {
                        createSoul();
                    }
                    if (!e.isAlive() || e.isDeadOrDying() || e.isRemoved()) {
                        setMeleeInteractionTicks(5);
                    }
                    /*FUN STUFF starts >:D */
                    /*Initial ticks: 220 (or 12 seconds)*/
                    float itemDamage = 0;
                    if (flag1) {
                        if (meleeInteractionTicks == 135 || meleeInteractionTicks == 180 || meleeInteractionTicks == 90 && !(i.getItem() instanceof GlaiveItem || i.getItem() instanceof WarhammerItem)) {
                            setShouldSlide(true);
                        }
                        if (meleeInteractionTicks == 120 || meleeInteractionTicks == 165 || meleeInteractionTicks == 75) {
                            setShouldSlide(false);
                        }
                    }
                    if(flag5){
                        if(meleeInteractionTicks == 150){
                            setShouldSlide(true);
                        } else if (meleeInteractionTicks == 100){
                            setShouldSlide(false);
                        }
                    }
                    if(flag2visual){
                        if(meleeInteractionTicks == 110){
                            setShouldSlide(true);
                        } else if (meleeInteractionTicks == 70){
                            setShouldSlide(false);
                        }
                    }
                    if (flag3 && e instanceof Player p && !getNotRenderItem()) {
                        EquipmentSlot equipmentslot = Mob.getEquipmentSlotForItem(i);
                        ItemStack stack = p.getItemBySlot(equipmentslot);
                        if (!EnchantmentHelper.hasBindingCurse(i) && stack.isEmpty()) {
                            if (meleeInteractionTicks == 114) {
                                setShouldSlide(true);
                            }
                        }
                    }
                    if (flag4 && allYouCanEat && meleeInteractionTicks == 160) {
                        setShouldSlidePotionsAndFood(true);
                    }

                    if (!i.isEmpty()) {
                        if (flag1) {
                            if (!(i.getDamageValue() >= i.getMaxDamage() - 2)) {
                                if (meleeInteractionTicks == 130 || meleeInteractionTicks == 85 && !(i.getItem() instanceof GlaiveItem || i.getItem() instanceof WarhammerItem) || meleeInteractionTicks == 175) {
                                    if (i.getItem() instanceof SwordItem SI) {
                                        itemDamage = SI.getDamage();
                                        itemDamage += EnchantmentHelper.getDamageBonus(i, ((LivingEntity) e).getMobType());
                                        swordHit(itemDamage, e, this.getUser(), i);
                                    } else if (i.getItem() instanceof DiggerItem SI) {
                                        itemDamage = SI.getAttackDamage();
                                        itemDamage += EnchantmentHelper.getDamageBonus(i, ((LivingEntity) e).getMobType());
                                        swordHit(itemDamage, e, this.getUser(), i);
                                    } else if (i.getItem() instanceof GlaiveItem SI) {
                                        itemDamage = SI.getDamage();
                                        itemDamage += EnchantmentHelper.getDamageBonus(i, ((LivingEntity) e).getMobType());
                                        swordHit(itemDamage, e, this.getUser(), i);
                                    } else if (i.getItem() instanceof SacrificialDaggerItem SI){
                                        itemDamage = SI.getDamage();
                                        itemDamage += EnchantmentHelper.getDamageBonus(i, ((LivingEntity) e).getMobType());
                                        swordHit(itemDamage, e, this.getUser(), i);
                                    } else if (i.getItem() instanceof ScissorItem SI){
                                        itemDamage = SI.getDamage();
                                        itemDamage += EnchantmentHelper.getDamageBonus(i, ((LivingEntity) e).getMobType());
                                        swordHit(itemDamage, e, this.getUser(), i);
                                    } else if (i.getItem() instanceof ShearsItem){
                                        itemDamage = 3;
                                        itemDamage += EnchantmentHelper.getDamageBonus(i, ((LivingEntity) e).getMobType());
                                        swordHit(itemDamage, e, this.getUser(), i);
                                    }
                                    if (i.getItem() instanceof WarhammerItem) {
                                        i.hurtAndBreak(2, this, $$1x -> $$1x.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                                    } else {
                                        i.hurtAndBreak(1, this, $$1x -> $$1x.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                                    }
                                }
                            }
                        }
                        if (i.is(Items.BELL)) {
                            if (meleeInteractionTicks > 160 && this.tickCount % 20 == 0 || meleeInteractionTicks < 160 && meleeInteractionTicks > 100 && this.tickCount % 10 == 0 || meleeInteractionTicks < 100 && meleeInteractionTicks >= 5 && this.tickCount % 5 == 0) {
                                this.level().playSound((Player) null, this, SoundEvents.BELL_BLOCK, SoundSource.HOSTILE, 10.0F, (float) (0.95F + Math.random() * -0.05f));
                            }
                        }
                        if (meleeInteractionTicks == 150) {
                            if (i.getItem() instanceof InstrumentItem horn) {
                                if (this.level() instanceof ServerLevel sl) {
                                    Optional<? extends Holder<Instrument>> optional = ((IInstrumentItemAccess) horn).roundabout$GetInstrument(i);
                                    if (optional.isPresent()) {
                                        Instrument instrument = optional.get().value();
                                        play(sl, instrument, this);
                                    }
                                }
                            }
                        }
                        if (i.getItem() instanceof ArmorItem || i.getItem() instanceof ElytraItem) {
                            if (meleeInteractionTicks == 110) {
                                if (e instanceof Player p) {
                                    EquipmentSlot equipmentslot = Mob.getEquipmentSlotForItem(i);
                                    ItemStack stack = p.getItemBySlot(equipmentslot);
                                    if (!EnchantmentHelper.hasBindingCurse(i) && stack.isEmpty()) {
                                        ItemStack itemstackArmor = i.copyAndClear();
                                        e.setItemSlot(equipmentslot, itemstackArmor);
                                        if (!level().isClientSide) {
                                            setNotRenderItem(true);
                                        }
                                    }
                                }
                            }
                        }
                        if (i.getItem() instanceof PotionItem pi) {
                            if (meleeInteractionTicks == 140 || meleeInteractionTicks == 110 || meleeInteractionTicks == 85) {
                                this.level().playSound((Player) null, this, SoundEvents.GENERIC_DRINK, SoundSource.HOSTILE, 2.0F, 1F);
                            }
                            if (meleeInteractionTicks == 80) {
                                if (i.getItem() instanceof PotionItem) {
                                    pi.finishUsingItem(i, this.level(), e);
                                    if (this.getUser() instanceof Player pl) {
                                        IPlayerEntity play = ((IPlayerEntity) pl);
                                        play.roundabout$getBlckSabbathPlayerInventory().setItem(0, new ItemStack(Items.GLASS_BOTTLE));
                                    } else {
                                        this.setHeldItemSabbath(new ItemStack(Items.GLASS_BOTTLE));
                                    }
                                }
                            }
                        }
                        if (i.getItem().getFoodProperties() != null || i.getItem() instanceof MilkBucketItem) {
                            if (i.getItem() instanceof MilkBucketItem || (e instanceof Player && i.isEdible() || e instanceof Animal a && a.isFood(i) || e instanceof Villager)) {
                                if ((meleeInteractionTicks == 140 || meleeInteractionTicks == 110 || meleeInteractionTicks == 85 || meleeInteractionTicks == 70 && i.getItem() instanceof NewLocacacaItem) && allYouCanEat) {
                                    if(i.getItem() instanceof MilkBucketItem){
                                        this.level().playSound((Player) null, this, SoundEvents.GENERIC_DRINK, SoundSource.HOSTILE, 2.0F, 1F);
                                    } else {
                                        this.level().playSound((Player) null, this, SoundEvents.GENERIC_EAT, SoundSource.HOSTILE, 2.0F, 1F);
                                    }
                                }
                                if(i.getItem() instanceof NewLocacacaItem && meleeInteractionTicks < 140 && meleeInteractionTicks > 60){
                                    if(i.getItem() instanceof NewLocacacaItem){
                                        if(e instanceof Player) {
                                            locacacaTick(this.level(), e);
                                        }
                                    }
                                }
                                if (!(i.getItem() instanceof NewLocacacaItem) && meleeInteractionTicks == 80
                                        || i.getItem() instanceof NewLocacacaItem && meleeInteractionTicks == 60) {
                                    if((i.getItem() instanceof MilkBucketItem)){
                                        if (!this.level().isClientSide()) {
                                            i.finishUsingItem(this.level(), e);
                                            if (this.getUser() instanceof Player pl) {
                                                IPlayerEntity play = ((IPlayerEntity) pl);
                                                play.roundabout$getBlckSabbathPlayerInventory().setItem(0, new ItemStack(Items.BUCKET));
                                            } else {
                                                this.setHeldItemSabbath(new ItemStack(Items.BUCKET));
                                            }
                                            hasStabbed = true;
                                        }
                                    } else {
                                        if (e instanceof Player p) {
                                            if (p.canEat(i.getItem().getFoodProperties().canAlwaysEat())) {
                                                if (!this.level().isClientSide()) {
                                                    i.finishUsingItem(this.level(), p);
                                                    if(i.getItem() instanceof LocacacaItem || i.getItem() instanceof NewLocacacaItem){
                                                        hasStabbed = true;
                                                    }
                                                }
                                            }
                                        } else if (e instanceof Animal an) {
                                            if (an.getHealth() < an.getMaxHealth()) {
                                                an.heal((float) i.getItem().getFoodProperties().getNutrition());
                                            } else if (an.canFallInLove()) {
                                                if (this.getUser() instanceof Player P) {
                                                    an.setInLove(P);
                                                } else {
                                                    if(an.getInLoveTime() < 1) {
                                                        an.setInLoveTime(600);
                                                    }
                                                }
                                            }
                                        }
                                        if (this.getUser() instanceof Player pl) {
                                            IPlayerEntity play = ((IPlayerEntity) pl);
                                            play.roundabout$getBlckSabbathPlayerInventory().getItem(0).shrink(1);
                                            if (!level().isClientSide) {
                                                if (play.roundabout$getBlckSabbathPlayerInventory().getItem(0).getCount() == 0) {
                                                    setNotRenderItem(true);
                                                }
                                            }
                                        }
                                    }
                                    setShouldSlidePotionsAndFood(false);
                                }
                            } else if (e instanceof Villager v) {
                                if (WANTED_ITEMS.contains(i.getItem())) {
                                    if (meleeInteractionTicks == 80) {
                                            ItemEntity $$4 = new ItemEntity(this.level(), v.getX(), v.getEyeY(), v.getZ(), this.getHeldItemSabbath().copyWithCount(1));
                                            this.level().addFreshEntity($$4);
                                            InventoryCarrier.pickUpItem(v, v, $$4);
                                        if (this.getUser() instanceof Player pl) {
                                            IPlayerEntity play = ((IPlayerEntity) pl);
                                            play.roundabout$getBlckSabbathPlayerInventory().getItem(0).shrink(1);
                                            if(!level().isClientSide) {
                                                if (play.roundabout$getBlckSabbathPlayerInventory().getItem(0).getCount() == 0) {
                                                    setNotRenderItem(true);
                                                }
                                            }
                                        }
                                        setShouldSlidePotionsAndFood(false);
                                    }
                                }
                            }
                        }
                        if(flag5 && !(i.getDamageValue() >= i.getMaxDamage() - 1)){
                            if(e instanceof Sheep sheep && !(sheep.isSheared())) {
                                if (meleeInteractionTicks == 125) {
                                    if (this.getUser() != null && this.getUser() instanceof Player p) {
                                        sheep.shear(SoundSource.HOSTILE);
                                        sheep.gameEvent(GameEvent.SHEAR, p);
                                        i.hurtAndBreak(1, this, $$1x -> $$1x.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                                    }
                                }
                           }
                        }

                        /*Stand Arrow!*/
                        if(i.getItem() instanceof RoundaboutArrowItem sa){
                            boolean isWorthyArrow = sa instanceof WorthyArrowItem;
                            if(meleeInteractionTicks == 100){
                                if(isWorthyArrow){
                                    if(this.getUser() instanceof Player p && p.isCreative()) {
                                        worthyEntitySabbath(e, sa);
                                    }
                                } else if (i.getItem() instanceof StandArrowItem st) {
                                    if(!(i.getDamageValue() >= i.getMaxDamage())) {

                                        CompoundTag tag = i.isEmpty() ? null : i.getTagElement("StandDisc");
                                        CompoundTag tag2 = tag != null ? tag.getCompound("DiscItem") : null;
                                        if(tag2 == null){
                                            int in = random.nextInt(1, 3);
                                            StandArrowItem.rerollStand(i, in > 1);
                                        }
                                        requiemArrowHypeAndAuraTopTenMomentsInAllAnime(e, st);
                                    }
                                }
                            }
                        }
                    }
                    } else {
                        if(!this.level().isClientSide) {
                            ((StandUser) e).roundabout$updateSealedTicks(-1);
                        }
                        lungeTicks = 51;
                        setShouldSlidePotionsAndFood(false);
                        ((StandUser) e).roundabout$setGrabbedSoul(false);
                        ((StandUser)e).roundabout$setGrabberSabbath(0);
                        killSoul();
                        if(getNotRenderItem() || i.is(Items.BUCKET) && hasStabbed){
                            pbs.killTargetListServerToClient();
                        }
                        if((flag2 || i.getItem() instanceof RoundaboutArrowItem || i.getItem() instanceof NewLocacacaItem || i.getItem() instanceof LocacacaItem) && hasStabbed){
                            if(this.getUser() instanceof Player) {
                                pbs.removeTargetSingular(e);
                            }
                        }
                    }
            }
        }
        }
    }

    boolean hasStabbed = false;

    void requiemArrowHypeAndAuraTopTenMomentsInAllAnime(LivingEntity $$1, StandArrowItem sa){
        ItemStack i = this.getHeldItemSabbath();

        DamageSource $$6;
        $$6 = this.damageSources().mobAttack(this.getUser());
        boolean hadStandEffect = false;

        CompoundTag tag = i.isEmpty() ? null : i.getTagElement("StandDisc");
        CompoundTag tag2 = tag != null ? tag.getCompound("DiscItem") : null;
        int get = ClientNetworking.getAppropriateConfig().itemSettings.levelsToGetStand;
        boolean isMobWorthy = (!($$1 instanceof Player) && $$1 instanceof Mob mob && MainUtil.canGrantStand(mob));
        boolean isPlayerWorthy = $$1 instanceof Player;

        if(!isMobWorthy && !isPlayerWorthy) {
            if(!level().isClientSide) {
                if ($$1 instanceof Mob mb) {
                    hadStandEffect = mb.getEffect(ModEffects.STAND_VIRUS) != null;
                }
                if (this.getUser() instanceof LivingEntity) {
                    ((LivingEntity) this.getUser()).setLastHurtMob($$1);
                }
                $$1.hurt($$6, 8);
                ItemStack StandDisc = ((StandUser) $$1).roundabout$getStandDisc();
                if (StandDisc == ItemStack.EMPTY) {
                    if (MainUtil.canCauseRejection($$1)) {
                        $$1.addEffect(new MobEffectInstance(ModEffects.STAND_VIRUS, 200, 0), this.getUser());
                        StandArrowItem.grantMobRejection(i, this.level(), $$1);
                    }
                    if (!$$1.isAlive() && !hadStandEffect && $$1 instanceof Cat C && !C.isTame()) {
                        StrayCatEntity.tryToSpawnStrayCat(C);
                    }
                }
                this.level().playSound((Player) null, this, ModSounds.BLACK_SABBATH_STAB_EVENT, SoundSource.HOSTILE, 2.0F, (float) (0.85F + Math.random() * 0.12f));
                i.removeTagKey("StandDisc");
            }
        } else {
            if (!level().isClientSide) {
                if (isMobWorthy) {
                    if (sa.isWorthinessType(i, ((LivingEntity) $$1))) {
                        this.level().playSound((Player) null, this, ModSounds.BLACK_SABBATH_STAB_EVENT, SoundSource.HOSTILE, 2.0F, (float) (0.85F + Math.random() * 0.12f));
                        StandArrowItem.grantMobStand(i, this.level(), (LivingEntity) $$1);
                    }
                }
                if ($$1 instanceof Player PE) {
                    if (tag2 != null) {
                        ItemStack itemstack = ItemStack.of(tag2);
                        if (itemstack.getItem() instanceof StandDiscItem de) {
                            this.level().playSound((Player) null, this, ModSounds.BLACK_SABBATH_STAB_EVENT, SoundSource.HOSTILE, 2.0F, (float) (0.85F + Math.random() * 0.12f));
                            if (de.standPowers.isSecondaryStand()) {
                                get = 0;
                            }
                            ItemStack StandDisc = ((StandUser) $$1).roundabout$getStandDisc();
                            if (StandDisc == ItemStack.EMPTY) {
                                if (de.standPowers.isSecondaryStand() || PE.experienceLevel >= get) {
                                    if (sa.grantStand(itemstack, $$1)) {
                                        this.level().playSound((Player) null, this, ModSounds.BLACK_SABBATH_STAB_EVENT, SoundSource.HOSTILE, 2.0F, (float) (0.85F + Math.random() * 0.12f));
                                        this.level().playSound(null, $$1.blockPosition(), ModSounds.STAND_ARROW_USE_EVENT, SoundSource.PLAYERS, 1.5F, 1F);
                                        PE.displayClientMessage(Component.translatable("item.roundabout.stand_arrow.acquireStandBlackSabbath").withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.BOLD), true);
                                        ((IPlayerEntity) PE).roundabout$qmessage(1);
                                        ((ServerLevel) level()).sendParticles(ParticleTypes.FIREWORK, $$1.getX(),
                                                $$1.getY() + $$1.getEyeHeight(), $$1.getZ(),
                                                20, 0, 0, 0, 0.4);
                                        if (!PE.isCreative() && get > 0) {
                                            PE.giveExperienceLevels(-get);
                                        }
                                        i.removeTagKey("StandDisc");
                                    }

                                } else {
                                    $$1.hurt($$6, 8);
                                    this.level().playSound((Player) null, this, ModSounds.BLACK_SABBATH_STAB_EVENT, SoundSource.HOSTILE, 2.0F, (float) (0.85F + Math.random() * 0.12f));
                                    if (MainUtil.canCauseRejection($$1)) {
                                        $$1.addEffect(new MobEffectInstance(ModEffects.STAND_VIRUS, 200, 0), this.getUser());
                                        StandArrowItem.grantMobRejection(i, this.level(), $$1);
                                    }
                                    i.removeTagKey("StandDisc");
                                }
                            } else {
                                $$1.hurt($$6, 8);
                                this.level().playSound((Player) null, this, ModSounds.BLACK_SABBATH_STAB_EVENT, SoundSource.HOSTILE, 2.0F, (float) (0.85F + Math.random() * 0.12f));
                                i.removeTagKey("StandDisc");
                            }
                        }
                    }
                }
            }
        }
        i.hurt(1, this.level().getRandom(), null);
        hasStabbed = true;
    }

    void worthyEntitySabbath(LivingEntity $$1, RoundaboutArrowItem rai){
        if (rai instanceof WorthyArrowItem WI && ($$1 instanceof Mob || $$1 instanceof Player)) {
            this.level().playSound((Player) null, this, ModSounds.BLACK_SABBATH_STAB_EVENT, SoundSource.HOSTILE, 2.0F, (float) (0.45F + Math.random() * 0.12f));
            if (this.getUser() instanceof Player PE && PE.isCreative() && !((StandUser)$$1).roundabout$getStandDisc().isEmpty()){
                if (PowerTypes.hasStandActive($$1)){
                    ((StandUser)$$1).roundabout$setActive(false);
                }
                ((StandUser)$$1).roundabout$setStandDisc(ItemStack.EMPTY);
                if ($$1 instanceof Mob mb) {
                    ((IMob) $$1).roundabout$setWorthy(false);
                }
                particleStorm(this.level(),$$1);
                return;
            } else if ($$1 instanceof Mob mb){
                if (((IMob)mb).roundabout$isWorthy()){
                    if (this.getUser() instanceof Player PE && PE.isCreative()){
                        ((IMob)mb).roundabout$setWorthy(false);
                        particleStorm(this.level(),$$1);
                        return;
                    }
                } else {
                    ((IMob)mb).roundabout$setWorthy(true);
                    particleStorm(this.level(),$$1);
                    return;
                }
            }
            hasStabbed = true;
        }
    }

    private void particleStorm(Level level, Entity live){
        if (!level.isClientSide()) {
            level.playSound(null, live.blockPosition(), ModSounds.STAND_ARROW_USE_EVENT, SoundSource.PLAYERS, 1.5F, 1F);
            ((ServerLevel) level).sendParticles(ParticleTypes.FIREWORK, live.getX(),
                    live.getY() + live.getEyeHeight(), live.getZ(),
                    20, 0, 0, 0, 0.4);
        }
    }

    void locacacaTick(Level $$0, LivingEntity $$1){
        LivingEntity ent = MainUtil.getStoneTarget($$0, $$1);
        if (!($$1 instanceof Player && !((TimeStop) $$1.level()).getTimeStoppingEntities().isEmpty() &&
                ((TimeStop) $$1.level()).isTimeStoppingEntity($$1))) {
            if (ent != null) {
                if (!$$0.isClientSide()) {
                    double random = (Math.random() * 1.2) - 0.6;
                    double random2 = (Math.random() * 1.2) - 0.6;
                    double random3 = (Math.random() * 1.2) - 0.6;
                    ((ServerLevel) $$0).sendParticles(new ItemParticleOption(ParticleTypes.ITEM,
                                    Blocks.STONE.asItem().getDefaultInstance()), ent.getX() + random,
                            ent.getY() + ent.getEyeHeight() + random2, ent.getZ() + random3,
                            0,
                            ($$1.getX() - ent.getX()), ($$1.getY() - ent.getY() + ent.getEyeHeight()), ($$1.getZ() - ent.getZ()),
                            0.08);
                }
                Vec3 db = RotationUtil.distanceBetween(ent, $$1);
                ent.setDeltaMovement(ent.getDeltaMovement().add(
                        db.x * -0.018,
                        0,
                        db.z * -0.018
                ));
            }
        }
    }


    public final Vec3 getLookAngle(float $$0, Entity selfie) {
        return ((StandUser)selfie).roundabout$calculateViewVectorButICanUseIt(0, selfie.getYRot());
    }

    private float yVerticalOffset(StandEntity s){
        return s.blackSabbathGrabOffset();
    }
    private float horizontalOffset(){
        if (this.getUser() != null) {
            StandEntity s = grabbedSoul;
            if(s instanceof GrabbedMobSoulPlayerLarge || s instanceof GrabbedMobSoulPlayerSlim) {
                return 0.65f;
            } else {
                Entity grab = this.level().getEntity(getGrabbing());
                if (grab != null && grab instanceof LivingEntity l) {
                        if(s instanceof CaliforniaKingBedEntity){
                            return 0.55f;
                        } else if (s instanceof RattEntity){
                            return 0.63f;
                        } else if (s instanceof PearlJamEntity) {
                            return 0.50f;
                        } else if (s instanceof MandomEntity){
                            return 0.45f;
                        } else if (s instanceof HeyYaEntity){
                            return 0.0f;
                        } else {
                            return 0.65f;
                        }
                }
            }
        }
        return 0.55f;
    }

    public void travelAheadSoul(Entity.MoveFunction positionUpdater) {
        if (this.getUser() != null) {
            StandEntity s = grabbedSoul;
            if(s != null){
                if(((StandUser)this.getUser()).roundabout$getStandPowers() instanceof PowersBlackSabbath pb) {
                    Vec3 lvec = pb.getLookAngleChest(this.getYRot(), this);
                    Position pn = this.position().add(lvec.scale(horizontalOffset()));
                    positionUpdater.accept(s, pn.x(), this.getY() - yVerticalOffset(s), pn.z());
                }
            }
        }
    }

    public void travelAheadSoulRender(float render, StandEntity se){
        if (this.getUser() != null && se != null) {
            Vec3 junkPos = MainUtil.getAheadVecRender(this, (float) horizontalOffset(), render).getLocation();
            Vec3 junkPosY = MainUtil.getAheadVecRender(this, 0f, render).getLocation();
            if (!this.level().isClientSide()) {
                se.setOldPosAndRot();
            }
            se.xOld = junkPos.x;
            se.yOld = junkPosY.y - yVerticalOffset(se);
            se.zOld = junkPos.z;
            if (this.level().isClientSide()) {
                se.absMoveTo(junkPos.x, junkPosY.y - yVerticalOffset(se), junkPos.z);
            } else {
                se.setPos(junkPos.x, junkPosY.y - yVerticalOffset(se), junkPos.z);
            }

            if (this.level().isClientSide()) {
                se.setYRot(getYHeadRot() % 360 - 180);
                se.setXRot(getXRot());
                se.setYBodyRot(getYHeadRot() % 360 - 180);
                se.setYHeadRot(getYHeadRot() % 360 - 180);
            } else {
                se.setYRot(getYHeadRot() % 360 - 180);
                se.setXRot(getXRot());
                se.setYBodyRot(getYHeadRot() % 360 - 180);
                se.setYHeadRot(getYHeadRot() % 360 - 180);
            }
        }
    }

    public void killSoul(){
        if(this.grabbedSoul != null){
            grabbedSoul.forceDespawn(true);
            this.setGrabbing(0);
        }
    }

    @Override
    public void performRangedAttack(LivingEntity var1, float var2) {
    }
}
