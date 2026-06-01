package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.access.*;
import net.hydra.jojomod.block.MirrorBlock;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.*;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.hydra.jojomod.stand.powers.PowersManhattanTransfer;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.ArrayList;
import java.util.List;

public class ManhattanTransferEntity extends StandEntity {

    public ManhattanTransferEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    public static final byte
            ANIME_SKIN = 1,
            MANGA_SKIN = 2,
            AERO_TRANSFER_SKIN = 3,
            JOLLY_SKIN = 4,
            BRAZIL_SKIN = 5,
            RADIOACTIVE_SKIN = 6,
            POLLINATION_SKIN = 7,
            UFO_TRANSFER_SKIN = 8;

    @Override
    public boolean isNoGravity() {
        return true;
    }
    @Override
    public boolean lockPos() {
        return false;
    }
    @Override
    public boolean forceVisualRotation() {
        return true;
    }
    @Override
    public boolean canBeHitByProjectile() {return true;}
    @Override
    public boolean hasNoPhysics() {
        return false;
    }
    @Override
    public boolean isAttackable() {
        return true;
    }
    @Override
    public boolean skipAttackInteraction(Entity $$0) {
        return false;
    }
    @Override
    protected float getFlyingSpeed() {
        if(this.getUserData(this.getUser()) != null && this.getUser() != null) {
            if (this.getUserData(this.getUser()).roundabout$getStandPowers() instanceof PowersManhattanTransfer PM) {
                if(PM.XtraSpdTick > 7) {
                    return 0.30F;
                }else if(PM.XtraSpdTick > 4) {
                    return 0.25F;
                } else if(PM.XtraSpdTick > 1) {
                    return 0.20F;
                }
            }
        }
        return 0.10F;
    }
    @Override
    public boolean isControlledByLocalInstance() {
        LivingEntity user = this.getUser();
        if (user != null) {
            Entity ent = this.getUserData(user).roundabout$getStandPowers().getPilotingStand();
            if (ent != null && ent.is(this)) {
                return (user instanceof Player $$0 ? $$0.isLocalPlayer() : this.isEffectiveAi());
            }
        }
        return super.isControlledByLocalInstance();
    }
    @Override
    public void travel(Vec3 vec3) {
        super.travel(vec3);
        if (this.isControlledByLocalInstance()) {
            if (this.getUser() instanceof Player PE && this.level().isClientSide()) {
                C2SPacketUtil.updatePilot(this);
            }
        }

    }
    @Override
    public void addAdditionalSaveData(CompoundTag $$0){
        $$0.putBoolean("roundabout.AcquireHeldItem",this.canAcquireHeldItem);
        CompoundTag compoundtag = new CompoundTag();
        $$0.put("roundabout.HeldItem",this.getHeldItemManhattan().save(compoundtag));
        $$0.put("roundabout.HeldItem",this.getHeldItemManhattanFull().save(compoundtag));
        super.addAdditionalSaveData($$0);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag $$0){
        this.canAcquireHeldItem = $$0.getBoolean("roundabout.AcquireHeldItem");
        CompoundTag compoundtag = $$0.getCompound("roundabout.HeldItem");
        ItemStack itemstack = ItemStack.of(compoundtag);
        this.setHeldItemManhattan(itemstack);
        this.setHeldItemManhattanFull(itemstack);
        super.readAdditionalSaveData($$0);
    }
    protected static final EntityDataAccessor<ItemStack> HELD_ITEM_MANHATTAN = SynchedEntityData.defineId(ManhattanTransferEntity.class,
            EntityDataSerializers.ITEM_STACK);
    public final ItemStack getHeldItemManhattan() {
        return this.entityData.get(HELD_ITEM_MANHATTAN);
    }
    public final void setHeldItemManhattan(ItemStack stack) {
        this.entityData.set(HELD_ITEM_MANHATTAN, stack);
    }
    @Override
    protected void defineSynchedData() {
        if (!this.entityData.hasItem(HELD_ITEM_MANHATTAN)){
            super.defineSynchedData();
            this.entityData.define(HELD_ITEM_MANHATTAN, ItemStack.EMPTY);
            this.entityData.define(HELD_ITEM_MANHATTAN_FULL, ItemStack.EMPTY);
        }
    }
    protected static final EntityDataAccessor<ItemStack> HELD_ITEM_MANHATTAN_FULL = SynchedEntityData.defineId(ManhattanTransferEntity.class,
            EntityDataSerializers.ITEM_STACK);
    public final ItemStack getHeldItemManhattanFull() {
        return this.entityData.get(HELD_ITEM_MANHATTAN_FULL);
    }
    public final void setHeldItemManhattanFull(ItemStack stack) {
        this.entityData.set(HELD_ITEM_MANHATTAN_FULL, stack);
    }
    public boolean hasItem = false;
    public boolean hasItemTwo = false;
    public boolean success = false;
    public boolean isSnubnose = false;
    public float manhattanDamageIncipit = 0;
    public boolean canAcquireHeldItem = false;
    public boolean getCanPlace() {
        return  false;
    }
    public boolean canSnipe(){
        return false;
    }
    public float getShotAccuracy(){
        return 0.0F;
    }
    public float getBundleAccuracy(){
        return 0.0F;
    }
    public float getThrowAngle(){
        return 0.0F;
    }
    public float getThrowAngle2(){
        return 0.0F;
    }
    public float getThrowAngle3(){
        return 0.0F;
    }

    public boolean canOthersLoadMT = ClientNetworking.getAppropriateConfig().manhattanTransferSettings.canOtherMobsLoadManhattanTransfer;
    public int fireTicksPrj = 0;

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity direct = source.getDirectEntity();
        Entity directEntityWho = source.getEntity();
        if(User != null) {
            if (directEntityWho != null && direct != null) {
                if (direct instanceof Projectile PR && !source.is(ModDamageTypes.STAND)) {
                    if (directEntityWho != this) {
                        if (((directEntityWho.is(User) && !canOthersLoadMT) || canOthersLoadMT) && !hasItem) {
                            hasItemTwo = false;
                            if (direct instanceof AbstractArrow AA) {
                                ItemStack ii = ((IAbstractArrowAccess) direct).roundabout$GetPickupItem();
                                if (!ii.isEmpty()) {
                                    hasItem = true;
                                    success = true;
                                    if (AA.pickup.equals(AbstractArrow.Pickup.ALLOWED)) {
                                        this.canAcquireHeldItem = true;
                                    } else {
                                        this.canAcquireHeldItem = false;
                                    }
                                    this.fireTicksPrj = AA.getRemainingFireTicks();
                                    this.setHeldItemManhattan(ii.copyAndClear());
                                    AA.discard();
                                } else if (AA instanceof RoundaboutBulletEntity BE) {
                                    hasItem = true;
                                    success = true;
                                    this.canAcquireHeldItem = true;
                                    ItemStack bulletItem = BE.getBulletItemStack();
                                    this.setHeldItemManhattan(bulletItem);
                                    if (BE.getAmmoType() == RoundaboutBulletEntity.SNUBNOSE) {
                                        isSnubnose = true;
                                    } else if (BE.getAmmoType() == RoundaboutBulletEntity.COLT) {
                                        isSnubnose = false;
                                    }
                                    this.fireTicksPrj = BE.getRemainingFireTicks();
                                    AA.discard();
                                }
                            } else if (direct instanceof ThrowableItemProjectile TO) {
                                ItemStack ii = TO.getItem();
                                if (!ii.isEmpty()) {
                                    hasItem = true;
                                    success = true;
                                    this.canAcquireHeldItem = true;
                                    this.fireTicksPrj = TO.getRemainingFireTicks();
                                    this.setHeldItemManhattan(ii.copyAndClear());
                                    TO.discard();
                                }
                            }
                            this.changeMovementState();
                        } else {
                            success = false;
                            if (direct instanceof AbstractArrow AA) {
                                if (AA instanceof IronBallEntity) {
                                    return this.getUser().hurt(source, amount / 2);
                                }
                                ItemStack ii = ((IAbstractArrowAccess) AA).roundabout$GetPickupItem();
                                if (!ii.isEmpty()) {
                                    if (AA.pickup.equals(AbstractArrow.Pickup.ALLOWED)) {
                                        this.setHeldItemManhattanFull(ii.copyAndClear());
                                        hasItemTwo = true;
                                        AA.discard();
                                    } else {
                                        AA.discard();
                                    }
                                } else if (AA instanceof RoundaboutBulletEntity BE) {
                                    ItemStack bulletItem = BE.getBulletItemStack();
                                    this.setHeldItemManhattanFull(bulletItem);
                                    hasItemTwo = true;
                                    AA.discard();
                                }
                            } else if (direct instanceof ThrownObjectEntity TO) {
                                ItemStack ii = TO.getItem();
                                if (!ii.isEmpty()) {
                                    this.setHeldItemManhattanFull(ii.copyAndClear());
                                    hasItemTwo = true;
                                    TO.discard();
                                }
                            } else if (direct instanceof ThrowableItemProjectile TH) {
                                ItemStack ii = TH.getItem();
                                if (!ii.isEmpty()) {
                                    this.setHeldItemManhattanFull(ii.copyAndClear());
                                    hasItemTwo = true;
                                    TH.discard();
                                }
                            }
                        }
                    }
                }
            this.itemEject();
                if (success) {
                    if (direct instanceof AbstractArrow AA) {
                        manhattanDamageIncipit = amount;
                    }
                }
            }
        }
        this.markHurt();
        return super.hurt(source, amount);
    }

public void itemEject(){
    if (hasItem && this.canAcquireHeldItem) {
        if (!this.getHeldItemManhattanFull().isEmpty() && !this.getHeldItemManhattanFull().isEmpty()) {
            double $$3 = this.getEyeY() - 0.3F;
            ItemEntity $$4 = new ItemEntity(this.level(), this.getX(), $$3, this.getZ(), this.getHeldItemManhattanFull());
            $$4.setThrower(this.getUUID());
            this.level().addFreshEntity($$4);
            this.setHeldItemManhattanFull(ItemStack.EMPTY);
        }
    } else if (!this.canAcquireHeldItem) {}
    if (hasItemTwo) {
        double $$3 = this.getEyeY() - 0.3F;
        ItemEntity $$4 = new ItemEntity(this.level(), this.getX(), $$3, this.getZ(), this.getHeldItemManhattanFull());
        $$4.setThrower(this.getUUID());
        this.level().addFreshEntity($$4);
        this.setHeldItemManhattanFull(ItemStack.EMPTY);
        hasItemTwo = false;
    }
}

    public boolean shootHattan(/*ItemStack item*/){
        /***/
        if(!getHeldItemManhattan().isEmpty()) {
            Vec3 pos = new Vec3(this.getX(), this.getEyeY() - 0.1F, this.getZ());
            Direction gravD = ((IGravityEntity) this).roundabout$getGravityDirection();
            this.getUser();

            if (gravD != Direction.DOWN) {
                pos = RotationUtil.vecPlayerToWorld(
                        new Vec3(0, this.getEyeHeight() - 0.1F, 0
                        ), gravD);
                pos = new Vec3(this.getX() + pos.x, this.getY() + pos.y, this.getZ() + pos.z);
            }

            return manhattanShoot(this, canSnipe(), getHeldItemManhattan(), getShotAccuracy(), getBundleAccuracy(), getThrowAngle(),
                    getThrowAngle2(), getThrowAngle3(), getCanPlace(), this.rotationXHattan, this.rotationYHattan,
                    new Vec3(pos.x, pos.y, pos.z), true, 1, true);
        }
        return false;
    }

    public static boolean manhattanShoot(ManhattanTransferEntity thrower, boolean canSnipe, ItemStack item, float getShotAccuracy,
                                         float getBundleAccuracy,
                                         float getThrowAngle1, float getThrowAngle2, float getThrowAngle3,
                                         boolean getCanPlace, float xRot, float yRot,Vec3 pos,
                                         boolean playSounds, float mult, boolean canGiveYouItem){
        thrower.playSound(ModSounds.BULLET_RICOCHET_EVENT, 1.0F, (thrower.random.nextFloat() * 0.2F + 0.7F));
     if(!thrower.level().isClientSide) {
         if(thrower.getUserData(thrower.getUser()) != null && thrower.getUserData(thrower.getUser()).roundabout$getStandPowers() instanceof PowersManhattanTransfer PM){
             PM.isNotLoaded();
         }
         if (item.getItem() instanceof ArrowItem) {
             ArrowItem $$10 = (ArrowItem) item.getItem();
             AbstractArrow $$11 = $$10.createArrow(thrower.level(), item, thrower);
             $$11.setPos(pos);
             $$11.shootFromRotation(thrower, xRot, yRot, 0.0F, 3, getShotAccuracy);
             $$11.setCritArrow(false);
             ((IAbstractArrowAccess)$$11).roundabout$SetIsManhattan(true);
             ((IProjectileAccess)$$11).roundabout$setManhattanProjectile(true);
             ((IAbstractArrowAccess) $$11).roundabout$setHattanDamage(thrower.manhattanDamageIncipit);

             if (thrower != null) {
                 if (!thrower.canAcquireHeldItem ) {
                     $$11.pickup = AbstractArrow.Pickup.DISALLOWED;
                 } else{
                     $$11.pickup = AbstractArrow.Pickup.ALLOWED;
                 }
             }
             $$11.setRemainingFireTicks(thrower.fireTicksPrj);
             thrower.level().addFreshEntity($$11);
             $$11.setOwner(thrower.getUser());
         }
         else if (item.getItem() instanceof AmmoItem) {
             RoundaboutBulletEntity $$7 = new RoundaboutBulletEntity(thrower.level(), thrower);
             $$7.shootFromRotation(thrower, xRot, yRot, 0.0F,3.5F, 1.3F);

             if(item.getItem() instanceof SnubnoseAmmoItem){
                 if(thrower.isSnubnose){
                     $$7.setAmmoType(RoundaboutBulletEntity.SNUBNOSE);
                 } else{
                     $$7.setAmmoType(RoundaboutBulletEntity.COLT);
                 }
             } else if(item.getItem() instanceof SniperAmmoItem){
                 $$7.setAmmoType(RoundaboutBulletEntity.SNIPER);
             } else if(item.getItem() instanceof TommyAmmoItem) {
                 $$7.setAmmoType(RoundaboutBulletEntity.TOMMY_GUN);
             }
             $$7.setRemainingFireTicks(thrower.fireTicksPrj);
             $$7.isHattan = true;
             $$7.manhattanDamage = thrower.manhattanDamageIncipit;
             thrower.level().addFreshEntity($$7);
             $$7.setOwner(thrower.getUser());
         } else if (item.getItem() instanceof EnderpearlItem){
             ThrownEnderpearl $$7 = new ThrownEnderpearl(thrower.level(), thrower);
             $$7.setPos(pos);
             $$7.setItem(item);
             $$7.shootFromRotation(thrower, xRot, yRot, -3.0F, 2F*mult, getShotAccuracy);
             $$7.setRemainingFireTicks(thrower.fireTicksPrj);
             $$7.setOwner(thrower.getUser());
             thrower.level().addFreshEntity($$7);
         } else if (item.getItem() instanceof SnowballItem){
             Snowball $$7 = new Snowball(thrower.level(), thrower);
             $$7.setPos(pos);
             $$7.setItem(item);
             $$7.shootFromRotation(thrower, xRot, yRot, -3.0F, 2F*mult, getShotAccuracy);
             $$7.setRemainingFireTicks(thrower.fireTicksPrj);
             $$7.setOwner(thrower.getUser());
             thrower.level().addFreshEntity($$7);
         }else if(item.getItem() instanceof TridentItem || item.getItem() instanceof HarpoonItem){
             if(item.getItem() instanceof TridentItem){
                 ThrownTrident $$7 = new ThrownTrident(thrower.level(), thrower, item);
                 $$7.setPos(pos);
                 $$7.setRemainingFireTicks(thrower.fireTicksPrj);
                 $$7.shootFromRotation(thrower, xRot, yRot, -3.0F, 2F*mult, getShotAccuracy);
                 $$7.setOwner(thrower.getUser());
                 thrower.level().addFreshEntity($$7);
             } else{
                 HarpoonEntity $$7 = new HarpoonEntity(thrower.level(), thrower, item);
                 $$7.setPos(pos);
                 $$7.shootFromRotation(thrower, xRot, yRot, -3.0F, 2F*mult, getShotAccuracy);
                 $$7.setOwner(thrower.getUser());
                 $$7.setRemainingFireTicks(thrower.fireTicksPrj);
                 thrower.level().addFreshEntity($$7);
             }
         }else if (item.is(Items.IRON_INGOT)){
             IronBallEntity $$7 = new IronBallEntity(thrower.level(), thrower, item);
             $$7.setPos(pos);
             $$7.shootFromRotation(thrower, xRot, yRot, -3.0F, 2F*mult, getShotAccuracy);
             $$7.setRemainingFireTicks(thrower.fireTicksPrj);
             $$7.setOwner(thrower.getUser());
             thrower.level().addFreshEntity($$7);
         }else if (item.getItem() instanceof KnifeItem){
             KnifeEntity $$7 = new KnifeEntity(thrower.level(), thrower, item);
             $$7.setPos(pos);
             $$7.shootFromRotation(thrower, xRot, yRot, -3.0F, 2F*mult, getShotAccuracy);
             $$7.setRemainingFireTicks(thrower.fireTicksPrj);
             $$7.setOwner(thrower.getUser());
             thrower.level().addFreshEntity($$7);
         }else if (item.getItem() instanceof MatchItem) {
             KnifeEntity $$7 = new KnifeEntity(thrower.level(), thrower, item);
             $$7.setPos(pos);
             $$7.shootFromRotation(thrower, xRot, yRot, -3.0F, 2F * mult, getShotAccuracy);
             $$7.setRemainingFireTicks(thrower.fireTicksPrj);
             $$7.setOwner(thrower.getUser());
             thrower.level().addFreshEntity($$7);
         } else if(item.getItem() instanceof PotionItem){
             ThrownPotion $$4 = new ThrownPotion(thrower.level(), thrower);
             $$4.setPos(pos);
             $$4.setItem(item);
             $$4.setRemainingFireTicks(thrower.fireTicksPrj);
             $$4.setOwner(thrower.getUser());
             $$4.shootFromRotation(thrower, xRot, yRot, -3.0F, 1.4F*mult, getShotAccuracy);
             thrower.level().addFreshEntity($$4);
         } else if(item.getItem() instanceof BowlerHatItem){
             BladedBowlerHatEntity $$4 = new BladedBowlerHatEntity(thrower.level(), thrower, item);
             $$4.setPos(pos);
             $$4.setItem(item);
             $$4.setRemainingFireTicks(thrower.fireTicksPrj);
             $$4.setOwner(thrower.getUser());
             $$4.shootFromRotation(thrower, xRot, yRot, -3.0F, 1.4F*mult, getShotAccuracy);
             thrower.level().addFreshEntity($$4);
         } else {
            getCanPlace = false;
             ThrownObjectEntity $$14 = new ThrownObjectEntity(thrower, thrower.level(), item, getCanPlace);
             $$14.setPos(pos);
             $$14.shootFromRotation(thrower, xRot,
                     yRot, getThrowAngle1, 1.7F*mult, getThrowAngle2);
             if (canSnipe){
                 $$14.starThrowInit();
             }
             $$14.setRemainingFireTicks(thrower.fireTicksPrj);
             $$14.setOwner(thrower.getUser());
             thrower.level().addFreshEntity($$14);
         }
     }
        return  true;
    }

    public void changeMovementState(){
        if(this.getUserData(this.getUser()) != null && this.getUserData(this.getUser()).roundabout$getStandPowers() instanceof PowersManhattanTransfer PM){
            PM.isLoaded();
        }
    }

    @Override
    public void die(DamageSource $$0) {
        this.deathTime = 0;
        super.die($$0);
    }
    @Override
    protected void tickDeath() {
        super.die(this.damageSources().generic());
        super.tickDeath();
    }
    public StandUser getUserData(LivingEntity User) {
          return ((StandUser) User);
    }
    public int DodgeRainTicks = 0;
    public void setDodgeRainTicks(int val){DodgeRainTicks = val;};
    @Override
    public void tick() {
        validateUUID();
        float pitch = this.getXRot();
        float yaw = this.getYRot();
        super.tick();

          if(this.getUserData(this.getUser()) != null) {
            if (this.getUserData(this.getUser()).roundabout$getStandPowers() instanceof PowersManhattanTransfer PM) {
                if (stupidTicks >= 1) {
                    //setMaster(this.getUser());
                    stupidTicks--;
                    this.setXRot(this.getUser().getXRot() % 360);
                    this.setYRot(this.getUser().getYRot() % 360);
                    this.setYBodyRot(this.getUser().getYRot() % 360);
                    //this.setYBodyRot(yaw);
                }
                if (horizontalCollision || verticalCollision) {
                    if (!PM.isPiloting()) {
                        this.setXRot(pitch + 25);

                        this.setYBodyRot(yaw + 25);

                        this.setYRot(yaw);

                        if (yaw >= -90 && yaw <= 0) {
                            this.setYRot(yaw - 25);
                        }
                        if (yaw <= 90 && yaw > 0) {
                            this.setYRot(yaw + 25);
                        }
                    }
                }
            }
        }
            if (!this.level().isClientSide()) {
                if (!forceVisible) {
                    this.setXRot(pitch);
                    this.setYRot(yaw);
                    this.setYBodyRot(yaw);
                    this.xRotO = pitch;
                    this.yRotO = yaw;

                }
            }
            if(target != null){
                System.out.println(target);
            }
            searchTarget();
        rotationXHattan = this.getXRot();
        rotationYHattan = this.getYRot();
    }

    public LivingEntity target = null;

    public float manhattanDetectionRange = ClientNetworking.getAppropriateConfig().manhattanTransferSettings.manhattanAutoShootingRange;

    public void searchTarget(){
        if (this.level() != null) {
            List<LivingEntity> lvent = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(this.manhattanDetectionRange), (livingEntity) -> {
                return true;
            });
            if (lvent != null && !lvent.isEmpty()) {
                List<LivingEntity> rement = new ArrayList<>(lvent);
                for (LivingEntity value : lvent) {
                    IEntityAndData entityAndData = ((IEntityAndData) value);
                    if (value instanceof StandEntity || value.is(this.getUser())) {
                        rement.remove(value);
                        target = null;
                    }
                    if(entityAndData.roundabout$getTrueInvisibilityManhattan() < 1){
                        rement.remove(value);
                        target = null;
                    }
                }

                lvent = rement;
            }
            LivingEntity lv = this.level().getNearestEntity(lvent,
                    MainUtil.OFFER_TARGER_CONTEXT, null,
                    this.getX(), this.getY(), this.getZ());

            if(lv != null) {
                target = lv;
            }
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    float rotationXHattan = 0;
    float rotationYHattan = 0;

    int stupidTicks = 1;

    public boolean isInRain() {
        BlockPos $$0 = this.blockPosition();
        return this.level().isRainingAt($$0)
                || this.level().isRainingAt(BlockPos.containing((double)$$0.getX(), this.getBoundingBox().maxY, (double)$$0.getZ()));
    }

    public boolean stopsManhattanAnimationsWhenHeldItem = false;

    public final AnimationState rain_dodging_manhattan = new AnimationState();
    public final AnimationState slow_manhattan = new AnimationState();
    public final AnimationState forward_manhattan_incipit = new AnimationState();
    public final AnimationState forward_manhattan_loop = new AnimationState();
    public final AnimationState back_manhattan_incipit = new AnimationState();
    public final AnimationState back_manhattan_loop = new AnimationState();
    public final AnimationState back_manhattan_stop = new AnimationState();
    public final AnimationState forward_manhattan_stop = new AnimationState();
    public final AnimationState left_manhattan_incipit = new AnimationState();
    public final AnimationState left_manhattan_loop = new AnimationState();
    public final AnimationState left_manhattan_stop = new AnimationState();
    public final AnimationState right_manhattan_incipit = new AnimationState();
    public final AnimationState right_manhattan_loop = new AnimationState();
    public final AnimationState right_manhattan_stop = new AnimationState();

    public boolean isPressingW = false;
    public boolean isPressingA = false;
    public boolean isPressingS = false;
    public boolean isPressingD = false;

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        Options options = Minecraft.getInstance().options;
        if(this.getUserData(this.getUser()) != null && this.getUser() != null) {
            if (this.getUserData(this.getUser()).roundabout$getStandPowers() instanceof PowersManhattanTransfer PM) {
                AnimationState $$0 = this.slow_manhattan;
                AnimationState $$1 = this.forward_manhattan_incipit;
                AnimationState $$2 = this.forward_manhattan_loop;
                AnimationState $$3 = this.back_manhattan_incipit;
                AnimationState $$4 = this.back_manhattan_loop;
                AnimationState $$5 = this.back_manhattan_stop;
                AnimationState $$6 = this.forward_manhattan_stop;
                AnimationState $$7 = this.left_manhattan_incipit;
                AnimationState $$8 = this.left_manhattan_stop;
                AnimationState $$9 = this.left_manhattan_loop;
                AnimationState $$10 = this.right_manhattan_incipit;
                AnimationState $$11 = this.right_manhattan_stop;
                AnimationState $$12 = this.right_manhattan_loop;

                if (!PM.isActive()) {
                    //this.rain_dodging_manhattan.stop();
                } else {
                    if (isInRain()) {
                        this.rain_dodging_manhattan.startIfStopped(this.tickCount);
                        $$2.stop();
                        $$1.stop();
                        $$3.stop();
                        $$4.stop();
                        $$5.stop();
                        $$6.stop();
                        $$7.stop();
                        $$8.stop();
                        $$9.stop();
                        $$10.stop();
                        $$11.stop();
                        $$12.stop();
                    }
                    if (!isInRain()) {
                        this.rain_dodging_manhattan.stop();

                        if (PM.isClient() && !this.stopsManhattanAnimationsWhenHeldItem) {
                            if (PM.isPiloting()) {
                                if (options.keyUp.isDown()) {
                                    isPressingW = true;
                                }
                                if (!options.keyUp.isDown()) {
                                    isPressingW = false;
                                }
                                if (options.keyDown.isDown()) {
                                    isPressingS = true;
                                }
                                if (!options.keyDown.isDown()) {
                                    isPressingS = false;
                                }
                                if (options.keyLeft.isDown()) {
                                    isPressingA = true;
                                }
                                if (!options.keyLeft.isDown()) {
                                    isPressingA = false;
                                }
                                if (options.keyRight.isDown()) {
                                    isPressingD = true;
                                }
                                if (!options.keyRight.isDown()) {
                                    isPressingD = false;
                                }

                                if (isPressingW && !isPressingS) {
                                    $$1.startIfStopped(this.tickCount);
                                    $$2.startIfStopped(this.tickCount);
                                    $$4.stop();
                                    $$6.stop();
                                    $$0.stop();
                                } else {
                                    $$1.stop();
                                    $$2.stop();
                                    $$6.startIfStopped(this.tickCount);
                                    $$0.startIfStopped(this.tickCount);
                                }
                                if (isPressingS && !isPressingW) {
                                    $$3.startIfStopped(this.tickCount);
                                    $$4.startIfStopped(this.tickCount);
                                    $$2.stop();
                                    $$0.stop();
                                    $$5.stop();
                                } else {
                                    $$3.stop();
                                    $$4.stop();
                                    $$5.startIfStopped(this.tickCount);
                                    $$0.startIfStopped(this.tickCount);
                                }

                                if (isPressingW && isPressingS) {
                                    $$1.stop();
                                    $$2.stop();
                                    $$3.stop();
                                    $$4.stop();
                                    if ($$2.isStarted()) {
                                        $$6.startIfStopped(this.tickCount);
                                    }
                                    if ($$4.isStarted()) {
                                        $$5.startIfStopped(this.tickCount);
                                    }
                                    $$0.startIfStopped(this.tickCount);
                                }

                                if (isPressingA && !isPressingD) {
                                    $$7.startIfStopped(this.tickCount);
                                    $$9.startIfStopped(this.tickCount);
                                    $$12.stop();
                                    $$8.stop();
                                    $$0.stop();
                                } else {
                                    $$8.startIfStopped(this.tickCount);
                                    $$0.startIfStopped(this.tickCount);
                                    $$7.stop();
                                    $$9.stop();
                                }

                                if (isPressingD && !isPressingA) {
                                    $$10.startIfStopped(this.tickCount);
                                    $$12.startIfStopped(this.tickCount);
                                    $$9.stop();
                                    $$11.stop();
                                    $$0.stop();
                                } else {
                                    $$11.startIfStopped(this.tickCount);
                                    $$0.startIfStopped(this.tickCount);
                                    $$10.stop();
                                    $$12.stop();
                                }

                                if (isPressingD && isPressingA) {
                                    $$7.stop();
                                    $$9.stop();
                                    $$10.stop();
                                    $$12.stop();
                                    if ($$9.isStarted()) {
                                        $$8.startIfStopped(this.tickCount);
                                    }
                                    if ($$12.isStarted()) {
                                        $$11.startIfStopped(this.tickCount);
                                    }
                                    $$0.startIfStopped(this.tickCount);
                                }

                            } else {
                                $$0.startIfStopped(this.tickCount);
                                $$2.stop();
                                $$1.stop();
                                $$3.stop();
                                $$4.stop();
                                $$5.stop();
                                $$6.stop();
                                $$7.stop();
                                $$8.stop();
                                $$9.stop();
                                $$10.stop();
                                $$11.stop();
                                $$12.stop();
                            }
                        } else {
                            $$0.stop();
                            $$2.stop();
                            $$1.stop();
                            $$3.stop();
                            $$4.stop();
                            $$5.stop();
                            $$6.stop();
                            $$7.stop();
                            $$8.stop();
                            $$9.stop();
                            $$10.stop();
                            $$11.stop();
                            $$12.stop();
                        }
                    }
                }
            }
        }
    }
}
