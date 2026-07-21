package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.*;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.substand.LifeTrackerEntity;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.*;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.*;
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

    /*The 12 billion values I need for the stand to work as intended*/
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
    public boolean canBeHitByProjectile() {
        if (this.getUserData(this.getUser()) != null) {
            if (this.getUserData(this.getUser()).roundabout$getStandPowers() instanceof PowersManhattanTransfer PM) {
                if (isDesummoning) {
                    return false;
                }
            }
        }
        return true;
    }

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
    public float getFlyingSpeed() {
        if (this.getUserData(this.getUser()) != null && this.getUser() != null) {
            if (this.getUserData(this.getUser()).roundabout$getStandPowers() instanceof PowersManhattanTransfer PM) {
                if (this.level().isClientSide) {
                    Options key = Minecraft.getInstance().options;
                    if (key.keyDown.isDown() || key.keyRight.isDown() || key.keyUp.isDown() || key.keyLeft.isDown()) {
                        if (PM.XtraSpdTick > 7) {
                            return 0.30F;
                        } else if (PM.XtraSpdTick > 4) {
                            return 0.25F;
                        } else if (PM.XtraSpdTick > 1) {
                            return 0.20F;
                        }
                    }
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
    public void addAdditionalSaveData(CompoundTag $$0) {
        $$0.putBoolean("roundabout.AcquireHeldItem", this.canAcquireHeldItem);
        CompoundTag compoundtag = new CompoundTag();
        $$0.put("roundabout.HeldItem", this.getHeldItemManhattan().save(compoundtag));
        $$0.put("roundabout.HeldItem", this.getHeldItemManhattanFull().save(compoundtag));
        super.addAdditionalSaveData($$0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
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
        if (!this.entityData.hasItem(HELD_ITEM_MANHATTAN)) {
            super.defineSynchedData();
            this.entityData.define(HELD_ITEM_MANHATTAN, ItemStack.EMPTY);
            this.entityData.define(HELD_ITEM_MANHATTAN_FULL, ItemStack.EMPTY);
            this.entityData.define(MANHATTAN_TARGET, 0);
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
    public int getHattanTarget() {
        return this.entityData.get(MANHATTAN_TARGET);
    }
    public void setHattanTarget(int d) {
        this.entityData.set(MANHATTAN_TARGET, d);
    }
    protected static final EntityDataAccessor<Integer> MANHATTAN_TARGET = SynchedEntityData.defineId(ManhattanTransferEntity.class,
            EntityDataSerializers.INT);
    public boolean isDesummoning = false;
    public boolean hasItem = false;
    public boolean hasItemTwo = false;
    public boolean success = false;
    public boolean isSnubnose = false;
    public float manhattanDamageIncipit = 0;
    public boolean canAcquireHeldItem = false;
    public boolean getCanPlace() {
        return false;
    }
    public boolean canSnipe() {
        return false;
    }
    public float getShotAccuracy() {
        return 0.0F;
    }
    public float getBundleAccuracy() {
        return 0.0F;
    }
    public float getThrowAngle() {
        return 0.0F;
    }
    public float getThrowAngle2() {
        return 0.0F;
    }
    public float getThrowAngle3() {
        return 0.0F;
    }
    public boolean canOthersLoadMT = ClientNetworking.getAppropriateConfig().manhattanTransferSettings.canOtherMobsLoadManhattanTransfer;
    public int fireTicksPrj = 0;
    public Projectile hattanDeflected = null;
    public int fireworkLifeTicks = 0;
    public int setHatAnimDir = 1;
    public float heighHattanPilotNoMov = 0;
    private boolean isKeyEverPressed = false;
    int knockbackArrow = 0;
    void setKnockbackArrow(int necessary){knockbackArrow = necessary;}
    int piercingArrow = 0;
    public StandUser getUserData(LivingEntity User) {
        return ((StandUser) User);
    }
    public int DodgeRainTicks = 0;
    public void setDodgeRainTicks(int val) {
        DodgeRainTicks = val;
    }
    int stupidTicks = 10;
    public int tickInWater = 100;
    int dirPause = 0;
    int randomDirection = 0;
    public boolean isHattanPilotMode = false;
    public float autoMoveBoost = 1;
    public float shootRotationXHattan = 0;
    public float shootRotationYHattan = 0;
    public float manhattanDetectionRange = ClientNetworking.getAppropriateConfig().manhattanTransferSettings.manhattanAutoShootingRange;

    /*actual methods*/
    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity direct = source.getDirectEntity();
        Entity directEntityWho = source.getEntity();
        if (User != null) {
            if (directEntityWho != null && direct != null) {
                if (direct instanceof Projectile PR && !source.is(ModDamageTypes.STAND)) {
                    if (directEntityWho != this) {
                        if (PR instanceof AbstractArrow || PR instanceof ThrowableItemProjectile) {
                            if (((directEntityWho.is(User) && !canOthersLoadMT) || canOthersLoadMT) && !hasItem) {
                                hasItemTwo = false;
                                if(this.getUser() instanceof Player PL && ((StandUser) PL).roundabout$getStandPowers() instanceof  PowersManhattanTransfer PM){
                                    if(this.getHattanTarget() == 0 || PM.switchShootingMode()) {
                                        PM.getSelf().level().playSound(null, PM.getSelf().blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                                    } else {
                                        PM.getSelf().level().playSound(null, PM.getSelf().blockPosition(), ModSounds.BULLET_RICOCHET_EVENT, SoundSource.PLAYERS, 1F, (this.random.nextFloat() * 0.2F + 0.7F));
                                    }
                                }
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
                                        setKnockbackArrow(AA.getKnockback());
                                        piercingArrow = AA.getPierceLevel();
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
                                        if(TO.getOwner() instanceof Player) {
                                            this.canAcquireHeldItem = true;
                                        } else {
                                            this.canAcquireHeldItem = false;
                                        }
                                        this.fireTicksPrj = TO.getRemainingFireTicks();
                                        this.setHeldItemManhattan(ii.copyAndClear());
                                        TO.discard();
                                    }
                                }
                                this.changeMovementState();
                            } else {
                                success = false;
                                if (direct instanceof AbstractArrow AA) {
                                    ItemStack ii = ((IAbstractArrowAccess) AA).roundabout$GetPickupItem();
                                    if (AA instanceof IronBallEntity) {
                                        IronBallEntity $$7 = new IronBallEntity(this.getUser().level(), this.getUser(), ii);
                                        $$7.setPos(this.getX(), this.getY() - 0.15, this.getZ());
                                        $$7.shootFromRotation(this, this.getXRot(), this.getYRot(), -3.0F, 2F, 0.0F);
                                        $$7.setRemainingFireTicks(this.fireTicksPrj);
                                        this.level().addFreshEntity($$7);
                                        $$7.isHattanIronBall = true;
                                        this.hattanDeflected = $$7;
                                    }
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
                                        if(TH.getOwner() instanceof Player P && !(P.isCreative() || P.isSpectator())) {
                                            this.canAcquireHeldItem = true;
                                            hasItemTwo = true;
                                        } else {
                                            this.canAcquireHeldItem = false;
                                        }
                                        TH.discard();
                                    }
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


    public void itemEject() {
        if (hasItem && this.canAcquireHeldItem) {
            if (!this.getHeldItemManhattanFull().isEmpty() && !this.getHeldItemManhattanFull().isEmpty()) {
                double $$3 = this.getEyeY() - 0.3F;
                ItemEntity $$4 = new ItemEntity(this.level(), this.getX(), $$3, this.getZ(), this.getHeldItemManhattanFull());
                $$4.setThrower(this.getUUID());
                this.level().addFreshEntity($$4);
                this.setHeldItemManhattanFull(ItemStack.EMPTY);
            }
        } else if (!this.canAcquireHeldItem) {
        }
        if (hasItemTwo) {
            double $$3 = this.getEyeY() - 0.3F;
            ItemEntity $$4 = new ItemEntity(this.level(), this.getX(), $$3, this.getZ(), this.getHeldItemManhattanFull());
            $$4.setThrower(this.getUUID());
            this.level().addFreshEntity($$4);
            this.setHeldItemManhattanFull(ItemStack.EMPTY);
            hasItemTwo = false;
        }
    }

    public boolean shootHattan() {
        if (!getHeldItemManhattan().isEmpty()) {
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
                    getThrowAngle2(), getThrowAngle3(), getCanPlace(), this.shootRotationXHattan, this.shootRotationYHattan,
                    new Vec3(pos.x, pos.y, pos.z), true, 1, true);
        }
        return false;
    }

    public static boolean manhattanShoot(ManhattanTransferEntity thrower, boolean canSnipe, ItemStack item, float getShotAccuracy,
                                         float getBundleAccuracy,
                                         float getThrowAngle1, float getThrowAngle2, float getThrowAngle3,
                                         boolean getCanPlace, float xRot, float yRot, Vec3 pos,
                                         boolean playSounds, float mult, boolean canGiveYouItem) {
        thrower.playSound(ModSounds.BULLET_RICOCHET_EVENT, 1.0F, (thrower.random.nextFloat() * 0.2F + 0.7F));
        if(thrower.getUser() instanceof Player PL && ((StandUser) PL).roundabout$getStandPowers() instanceof  PowersManhattanTransfer PM && MainUtil.cheapDistanceTo2(thrower.getX(), thrower.getZ(), thrower.getUser().getX(), thrower.getUser().getZ()) > 16){
            if (PM.isClient()) {
                PM.self.playSound(ModSounds.BULLET_RICOCHET_EVENT, 100F, (thrower.random.nextFloat() * 0.2F + 0.7F));
            }
        }
        if (!thrower.level().isClientSide) {
            if (thrower.getUserData(thrower.getUser()) != null && thrower.getUserData(thrower.getUser()).roundabout$getStandPowers() instanceof PowersManhattanTransfer PM) {
                PM.isNotLoaded();
            }
            if (item.getItem() instanceof ArrowItem) {
                ArrowItem $$10 = (ArrowItem) item.getItem();
                AbstractArrow $$11 = $$10.createArrow(thrower.getUser().level(), item, thrower.getUser());
                $$11.setPos(pos.x, pos.y - 0.15, pos.z);
                $$11.shootFromRotation(thrower, xRot, yRot, 0.0F, 3F, getShotAccuracy);
                $$11.setKnockback(thrower.knockbackArrow);
                $$11.setPierceLevel((byte)thrower.piercingArrow);
                $$11.setCritArrow(false);
                if(thrower.canAcquireHeldItem){
                    $$11.pickup =AbstractArrow.Pickup.ALLOWED;
                }else {
                    $$11.pickup =AbstractArrow.Pickup.CREATIVE_ONLY;
                }
                thrower.level().addFreshEntity($$11);
                ((IAbstractArrowAccess) $$11).roundabout$SetIsManhattan(true);
                ((IAbstractArrowAccess) $$11).roundabout$setHattanDamage(thrower.manhattanDamageIncipit);
                $$11.setRemainingFireTicks(thrower.fireTicksPrj);
                thrower.hattanDeflected = $$11;
            } else if (item.getItem() instanceof AmmoItem) {
                RoundaboutBulletEntity $$7 = new RoundaboutBulletEntity(thrower.getUser().level(), thrower.getUser());
                $$7.shootFromRotation(thrower, xRot, yRot, 0.0F, 3.5F, getShotAccuracy);
                $$7.setPos(pos.x, pos.y - 0.15, pos.z);
                if (item.getItem() instanceof SnubnoseAmmoItem) {
                    if (thrower.isSnubnose) {
                        $$7.setAmmoType(RoundaboutBulletEntity.SNUBNOSE);
                    } else {
                        $$7.setAmmoType(RoundaboutBulletEntity.COLT);
                    }
                } else if (item.getItem() instanceof SniperAmmoItem) {
                    $$7.setAmmoType(RoundaboutBulletEntity.SNIPER);
                } else if (item.getItem() instanceof TommyAmmoItem) {
                    $$7.setAmmoType(RoundaboutBulletEntity.TOMMY_GUN);
                }
                $$7.setRemainingFireTicks(thrower.fireTicksPrj);
                $$7.isHattan = true;
                $$7.manhattanDamage = thrower.manhattanDamageIncipit;
                thrower.level().addFreshEntity($$7);
                thrower.hattanDeflected = $$7;
            } else if (item.getItem() instanceof EnderpearlItem) {
                ThrownEnderpearl $$7 = new ThrownEnderpearl(thrower.getUser().level(), thrower.getUser());
                $$7.setPos(pos.x, pos.y - 0.15, pos.z);
                $$7.setItem(item);
                $$7.shootFromRotation(thrower, xRot, yRot, -3.0F, 1.5F * mult, getShotAccuracy);
                $$7.setRemainingFireTicks(thrower.fireTicksPrj);
                thrower.level().addFreshEntity($$7);
                thrower.hattanDeflected = $$7;
            } else if (item.getItem() instanceof SnowballItem) {
                Snowball $$7 = new Snowball(thrower.getUser().level(), thrower.getUser());
                $$7.setPos(pos.x, pos.y - 0.15, pos.z);
                $$7.setItem(item);
                $$7.shootFromRotation(thrower, xRot, yRot, -3.0F, 1.5F * mult, getShotAccuracy);
                $$7.setRemainingFireTicks(thrower.fireTicksPrj);
                thrower.level().addFreshEntity($$7);
                thrower.hattanDeflected = $$7;
            } else if (item.getItem() instanceof EggItem) {
                ThrownEgg $$7 = new ThrownEgg(thrower.getUser().level(), thrower.getUser());
                $$7.setPos(pos.x, pos.y - 0.15, pos.z);
                $$7.setItem(item);
                $$7.shootFromRotation(thrower, xRot, yRot, -3.0F, 1.5F * mult, getShotAccuracy);
                $$7.setRemainingFireTicks(thrower.fireTicksPrj);
                thrower.level().addFreshEntity($$7);
                thrower.hattanDeflected = $$7;
            } else if (item.getItem() instanceof TridentItem || item.getItem() instanceof HarpoonItem) {
                if (item.getItem() instanceof TridentItem) {
                    ThrownTrident $$7 = new ThrownTrident(thrower.getUser().level(), thrower.getUser(), item);
                    $$7.setPos(pos.x, pos.y - 0.15, pos.z);
                    $$7.setRemainingFireTicks(thrower.fireTicksPrj);
                    $$7.shootFromRotation(thrower, xRot, yRot, -3.0F, 1.75F * mult, getShotAccuracy);
                    thrower.level().addFreshEntity($$7);
                    thrower.hattanDeflected = $$7;
                    if(!thrower.canAcquireHeldItem){
                        $$7.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    } else {
                        $$7.pickup = AbstractArrow.Pickup.ALLOWED;
                    }
                } else {
                    HarpoonEntity $$7 = new HarpoonEntity(thrower.getUser().level(), thrower.getUser(), item);
                    $$7.setPos(pos.x, pos.y - 0.15, pos.z);
                    $$7.shootFromRotation(thrower, xRot, yRot, -3.0F, 2F * mult, getShotAccuracy);
                    $$7.setRemainingFireTicks(thrower.fireTicksPrj);
                    thrower.level().addFreshEntity($$7);
                    $$7.isMahattan = true;
                    thrower.hattanDeflected = $$7;
                    if(!thrower.canAcquireHeldItem){
                        $$7.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    } else {
                        $$7.pickup = AbstractArrow.Pickup.ALLOWED;
                    }
                }
            } else if (item.is(Items.IRON_INGOT)) {
                IronBallEntity $$7 = new IronBallEntity(thrower.getUser().level(), thrower.getUser(), item);
                $$7.setPos(pos.x, pos.y - 0.15, pos.z);
                $$7.shootFromRotation(thrower, xRot, yRot, -3.0F, 2F * mult, getShotAccuracy);
                $$7.setRemainingFireTicks(thrower.fireTicksPrj);
                thrower.level().addFreshEntity($$7);
                $$7.isHattanIronBall = true;
                thrower.hattanDeflected = $$7;
            } else if (item.getItem() instanceof KnifeItem) {
                KnifeEntity $$7 = new KnifeEntity(thrower.getUser().level(), thrower.getUser(), item);
                $$7.setPos(pos.x, pos.y - 0.15, pos.z);
                $$7.shootFromRotation(thrower, xRot, yRot, -3.0F, 1.5F * mult, getShotAccuracy);
                $$7.setRemainingFireTicks(thrower.fireTicksPrj);
                thrower.level().addFreshEntity($$7);
                $$7.isHattanKnife = true;
                thrower.hattanDeflected = $$7;
            } else if (item.getItem() instanceof MatchItem) {
                MatchEntity $$7 = new MatchEntity(thrower.getUser(), thrower.getUser().level());
                $$7.setPos(pos.x, pos.y - 0.15, pos.z);
                $$7.shootFromRotation(thrower, xRot, yRot, -3.0F, 1.5F * mult, getShotAccuracy);
                $$7.setRemainingFireTicks(thrower.fireTicksPrj);
                $$7.isHattanMatch = true;
                thrower.level().addFreshEntity($$7);
                thrower.hattanDeflected = $$7;
            } else if (item.getItem() instanceof PotionItem) {
                ThrownPotion $$4 = new ThrownPotion(thrower.getUser().level(), thrower.getUser());
                $$4.setPos(pos.x, pos.y - 0.15, pos.z);
                $$4.setItem(item);
                $$4.setRemainingFireTicks(thrower.fireTicksPrj);
                $$4.shootFromRotation(thrower, xRot, yRot, -3.0F, 0.75F * mult, getShotAccuracy);
                thrower.level().addFreshEntity($$4);
                thrower.hattanDeflected = $$4;
            } else if (item.getItem() instanceof BowlerHatItem) {
                BladedBowlerHatEntity $$4 = new BladedBowlerHatEntity(thrower.getUser().level(), thrower.getUser(), item);
                $$4.setPos(pos.x, pos.y - 0.15, pos.z);
                $$4.setItem(item);
                $$4.setRemainingFireTicks(thrower.fireTicksPrj);
                $$4.shootFromRotation(thrower, xRot, yRot, -3.0F, 1.4F * mult, getShotAccuracy);
                $$4.isHattanHatProj = true;
                thrower.level().addFreshEntity($$4);
                thrower.hattanDeflected = $$4;
            } else if (item.getItem() instanceof FireworkRocketItem) {
                FireworkRocketEntity $$4 = new FireworkRocketEntity(thrower.getUser().level(), item, thrower.getUser(), thrower.getX(), thrower.getY() - 0.15, thrower.getZ(), true);
                $$4.setPos(pos.x, pos.y - 0.15, pos.z);
                $$4.setRemainingFireTicks(thrower.fireTicksPrj);
                $$4.shootFromRotation(thrower, xRot, yRot, 0.0F, 1.4F * mult, getShotAccuracy);
                thrower.level().addFreshEntity($$4);
                thrower.hattanDeflected = $$4;
            } else {
                getCanPlace = false;
                ThrownObjectEntity $$14 = new ThrownObjectEntity(thrower.getUser(), thrower.getUser().level(), item, getCanPlace);
                $$14.setPos(pos.x, pos.y - 0.15, pos.z);
                $$14.shootFromRotation(thrower, xRot,
                        yRot, getThrowAngle1, 1.5F * mult, getThrowAngle2);
                if (canSnipe) {
                    $$14.starThrowInit();
                }
                $$14.setRemainingFireTicks(thrower.fireTicksPrj);
                $$14.setOwner(thrower.getUser());
                thrower.level().addFreshEntity($$14);
                thrower.hattanDeflected = $$14;
            }
            thrower.powerUpProjectile();
        }
        return true;
    }

    public void powerUpProjectile() {
        if (this.hattanDeflected != null && !(this.hattanDeflected instanceof ThrownTrident) && !(this.hattanDeflected instanceof FireworkRocketEntity)) {
            ((IProjectileAccess) hattanDeflected).roundabout$setManhattanProjectile(true);
        } else if (this.hattanDeflected instanceof ThrownTrident TR) {
            ((ISuperThrownAbstractArrow) TR).roundabout$setIsTridentManhattan(true);
        } else if (this.hattanDeflected instanceof FireworkRocketEntity FER) {
            ((IFireworkRocketAccess) FER).setIsHattanProj(true);
            ((IFireworkRocketAccess) FER).roundabout$SetFireworkRemainingLifeTicks(this.fireworkLifeTicks);
        }
    }

    public void changeMovementState() {
        if (this.getUserData(this.getUser()) != null && this.getUserData(this.getUser()).roundabout$getStandPowers() instanceof PowersManhattanTransfer PM) {
            PM.isLoaded();
        }
    }

    public Vec2 getStrangeVector() {
        if (this.getUser() != null && this.getUserData(this.getUser()) != null && this.getUserData(this.getUser()).roundabout$getStandPowers() instanceof PowersManhattanTransfer PM) {
            if (this.level().isClientSide) {
                if (isKeyEverPressed) {
                    if (verticalLastPressed) {
                        if (pressS) {
                            if (this.getXRot() < 15 || this.getXRot() > -15) {
                                return new Vec2(this.getXRot() * -1 + heighHattanPilotNoMov, this.getYRot() - 180);
                            } else {
                                return new Vec2(this.getXRot() * -1, this.getYRot() - 180);
                            }
                        }
                        if (!pressS) {
                            if (this.getXRot() < 15 || this.getXRot() > -15) {
                                return new Vec2(this.getXRot() + heighHattanPilotNoMov, this.getYRot());
                            } else {
                                return new Vec2(this.getXRot(), this.getYRot());
                            }
                        }
                    } else {
                        if (pressA) {
                            ;
                            return new Vec2(this.getXRot() + heighHattanPilotNoMov, this.getYRot() - 90);
                        }
                        if (!pressA) {
                            return new Vec2(this.getXRot() + heighHattanPilotNoMov, this.getYRot() + 90);
                        }
                    }
                } else {
                    if (stupidTicks < 1) {
                        if (this.getXRot() < 15 || this.getXRot() > -15) {
                            return new Vec2(this.getXRot() + heighHattanPilotNoMov, this.getYRot());
                        } else {
                            return new Vec2(this.getXRot(), this.getYRot());
                        }
                    }
                }
            }
        }
        return new Vec2(this.getXRot() + heighHattanPilotNoMov, this.getYRot());
    }

    public Vec3 getHattanDirection() {
        return Vec3.directionFromRotation(this.getStrangeVector());
    }

    @Override
    public void die(DamageSource $$0) {
        this.deathTime = 0;
        super.die($$0);
    }

    int ticksFixRot = 0;

    private void setShootRotationX(float shootX){shootRotationXHattan = shootX;}
    private void setShootRotationY(float shootY){shootRotationYHattan = shootY;}

    @Override
    public void tick() {
        validateUUID();
        float pitch = this.getXRot();
        float yaw = this.getYRot();
        if (dirPause > 0) {
            dirPause--;
        }

        if (!getHeldItemManhattan().isEmpty()) {
            stopsManhattanAnimationsWhenHeldItem = true;
        } else {
            stopsManhattanAnimationsWhenHeldItem = false;
        }

        if (this.getUser() != null) {
            if (stupidTicks >= 1) {
                LivingEntity $$0 = this.getUser();
                Direction gravityDirection = GravityAPI.getGravityDirection($$0);
                if (stupidTicks == 10) {
                    if (gravityDirection == Direction.DOWN) {
                        this.moveTo(this.getUser().getX(), this.getUser().getEyeY(), this.getUser().getZ() - 0.25F);
                    } else if (gravityDirection == Direction.UP) {
                        this.moveTo(this.getUser().getX(), this.getUser().getY() - $$0.getBbHeight(), this.getUser().getZ() - 0.25F);
                    } else if (gravityDirection == Direction.NORTH) {
                        this.moveTo(this.getUser().getX(), this.getUser().getY(), this.getUser().getZ() + $$0.getBbHeight());
                    } else if (gravityDirection == Direction.SOUTH) {
                        this.moveTo(this.getUser().getX(), this.getUser().getY(), this.getUser().getZ() - $$0.getBbHeight());
                    } else if (gravityDirection == Direction.EAST) {
                        this.moveTo(this.getUser().getX() - $$0.getBbHeight(), this.getUser().getY(), this.getUser().getZ() - 0.25F);
                    } else if (gravityDirection == Direction.WEST) {
                        this.moveTo(this.getUser().getX() + $$0.getBbHeight(), this.getUser().getY(), this.getUser().getZ() - 0.25F);
                    }
                } else {
                    if((this.getUser() instanceof Player && stupidTicks == 9) || (!(this.getUser() instanceof Player) && stupidTicks > 1)) {
                        if (gravityDirection != Direction.DOWN) {
                            Vec2 vecGravity = RotationUtil.rotPlayerToWorld($$0.getYRot(), $$0.getXRot(), gravityDirection);
                            this.setXRot(vecGravity.y % 360);
                            this.setYRot(vecGravity.x % 360);
                            this.setYBodyRot(this.getUser().getYHeadRot() % 360);
                        } else {
                            this.setXRot(this.getUser().getXRot() % 360);
                            this.setYRot(this.getUser().getYHeadRot() % 360);
                            this.setYBodyRot(this.getUser().getYHeadRot() % 360);
                        }
                    }
                }
                stupidTicks--;
            }
        }

        if (!isHattanPilotMode) {
            if (horizontalCollision || verticalCollision) {
                if (dirPause == 0) {
                    randomDirection = random.nextInt(2) + 1;
                    dirPause = 40;
                }

                if (horizontalCollision) {
                    if(this.getUser() instanceof Player || this.getUser() instanceof ServerPlayer) {
                        if (randomDirection <= 1) {
                            this.setYRot(yaw - 15);
                            this.setYHeadRot(yaw - 15);
                        } else {
                            this.setYRot(yaw + 15);
                            this.setYHeadRot(yaw + 15);
                        }
                    } else {
                        if (randomDirection <= 1) {
                            this.setYRot(yaw - 15);
                        } else {
                            this.setYRot(yaw + 15);
                        }
                        ticksFixRot = 10;
                    }
                }
                if (verticalCollision && !verticalCollisionBelow) {
                    this.setXRot(pitch + 15);
                }
                if (verticalCollisionBelow) {
                    this.setXRot(pitch - 15);
                }
            } else {
                if(ticksFixRot > 1){
                    if(randomDirection < 1) {
                        setYRot(this.getYRot() + 1);
                    } else {
                        setYRot(this.getYRot() - 1);
                    }
                    ticksFixRot--;
                }
            }
        }

        if (!this.level().isClientSide()) {
            if (this.getUserData(this.getUser()) != null) {
                if (this.getUserData(this.getUser()).roundabout$getStandPowers() instanceof PowersManhattanTransfer PM) {
                    Vec3 rots = this.getRotations(PM.targetHattan);
                    if (PM.switchShootingMode() || this.getHattanTarget() == 0) {
                        setShootRotationX(this.getXRot());
                        setShootRotationY(this.getYRot());
                    } else {
                        setShootRotationX((float) rots.x() * 180 / (float) Math.PI + 180);
                        setShootRotationY((float) rots.y * 180 / (float) Math.PI);
                    }
                }
            }
        }

        if (this.level().isClientSide) {
            Options options = Minecraft.getInstance().options;
            if (this.getUserData(this.getUser()) != null) {
                if (this.getUserData(this.getUser()).roundabout$getStandPowers() instanceof PowersManhattanTransfer PM) {
                    if (PM.isPiloting()) {
                        if (options.keyJump.isDown() || options.keyShift.isDown()) {
                            if (options.keyJump.isDown()) {
                                heighHattanPilotNoMov = -45 * autoMoveBoost;
                            }
                            if (options.keyShift.isDown()) {
                                heighHattanPilotNoMov = 45 * autoMoveBoost;
                            }
                        }
                        if (!options.keyJump.isDown() && !options.keyShift.isDown()) {
                            heighHattanPilotNoMov = 0;
                        }
                    }
                }
            }
        }

        if (this.getUser() != null && ((StandUser) this.getUser()).roundabout$getStandPowers() instanceof PowersManhattanTransfer PM) {
            if (PM.isClient()) {
                Options options = Minecraft.getInstance().options;
                if (PM.isPiloting()) {
                    if (options.keyDown.isDown() || options.keyUp.isDown() || options.keyLeft.isDown() || options.keyRight.isDown()) {
                        isKeyEverPressed = true;
                    }
                    if (options.keyUp.isDown()) {
                        W = true;
                        pressS = false;
                        pressA = false;
                        verticalLastPressed = true;
                        this.setHatAnimDir = 1;
                    }
                    if (!options.keyUp.isDown()) {
                        W = false;
                    }
                    if (options.keyDown.isDown()) {
                        S = true;
                        pressS = true;
                        pressA = false;
                        verticalLastPressed = true;
                        this.setHatAnimDir = 2;
                    }
                    if (!options.keyDown.isDown()) {
                        S = false;
                    }

                    if ((options.keyUp.isDown() && options.keyDown.isDown())) {
                        W = false;
                        S = false;
                        pressS = false;
                        pressA = false;
                        verticalLastPressed = true;
                        this.setHatAnimDir = 1;
                    }

                    if(options.keyLeft.isDown() && options.keyRight.isDown()){
                        A = false;
                        D = false;
                        pressS = false;
                        pressA = false;
                        verticalLastPressed = true;
                        this.setHatAnimDir = 1;
                    }

                    if((options.keyUp.isDown() && options.keyDown.isDown() && (options.keyLeft.isDown() && options.keyRight.isDown()))){
                        W = false;
                        A = false;
                        S = false;
                        D = false;
                        pressS = false;
                        pressA = false;
                        verticalLastPressed = true;
                        this.setHatAnimDir = 1;
                    }

                    if (options.keyLeft.isDown() && !options.keyRight.isDown()) {
                        A = true;
                        pressA = true;
                        pressS = false;
                        verticalLastPressed = false;
                        this.setHatAnimDir = 3;
                    }
                    if (!options.keyLeft.isDown()) {
                        A = false;
                    }
                    if (options.keyRight.isDown() && !options.keyLeft.isDown()) {
                        D = true;
                        pressA = false;
                        pressS = false;
                        verticalLastPressed = false;
                        this.setHatAnimDir = 4;
                    }
                    if (!options.keyRight.isDown()) {
                        D = false;
                    }

                }
            }
        }

        searchTarget();
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.getUser() != null) {
                if (((StandUser) this.getUser()).roundabout$getStandPowers() instanceof PowersManhattanTransfer PM && PM.isPiloting()) {
                    this.setXRot(pitch);
                    this.setYRot(yaw);
                    this.setYBodyRot(yaw);
                    this.xRotO = pitch;
                    this.yRotO = yaw;
            }
        }
        }
    }

    /*This is how it searches the nearest moving target*/
    public void searchTarget() {
        if (this.level() != null) {
            List<LivingEntity> lvent = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(this.manhattanDetectionRange), (livingEntity) -> {
                return true;
            });
            if (lvent != null && !lvent.isEmpty()) {
                List<LivingEntity> targent = new ArrayList<>(lvent);
                for (LivingEntity value : lvent) {
                    IEntityAndData entityAndData = ((IEntityAndData) value);
                    if (value instanceof LifeTrackerEntity || value instanceof RoadRollerEntity || value instanceof StandEntity || value.is(this.getUser()) || !this.hasLineOfSight(value)) {
                        targent.remove(value);
                        this.setHattanTarget(0);
                        if (this.getUserData(this.getUser()) != null) {
                            if (this.getUserData(this.getUser()).roundabout$getStandPowers() instanceof PowersManhattanTransfer PM) {
                                PM.targetHattan = null;
                            }
                        }
                    }
                    if (entityAndData.roundabout$getTrueInvisibilityManhattan() < 1 || this.isInWater() || this.isInLava()) {
                        targent.remove(value);
                        this.setHattanTarget(0);
                        if (this.getUserData(this.getUser()) != null) {
                            if (this.getUserData(this.getUser()).roundabout$getStandPowers() instanceof PowersManhattanTransfer PM) {
                                PM.targetHattan = null;
                            }
                        }
                    }
                    if(this.getUser() instanceof Mob M && M.getTarget() != null && !(value.is(M.getTarget()))){
                        targent.remove(value);
                        this.setHattanTarget(0);
                        if (this.getUserData(this.getUser()) != null) {
                            if (this.getUserData(this.getUser()).roundabout$getStandPowers() instanceof PowersManhattanTransfer PM) {
                                PM.targetHattan = null;
                            }
                        }
                    }
                }

                lvent = targent;
            }
            LivingEntity lv = this.level().getNearestEntity(lvent,
                    MainUtil.OFFER_TARGER_CONTEXT, null,
                    this.getX(), this.getY(), this.getZ());

            if (lv != null) {
                setHattanTarget(lv.getId());
                if (this.getUserData(this.getUser()) != null) {
                    if (this.getUserData(this.getUser()).roundabout$getStandPowers() instanceof PowersManhattanTransfer PM) {
                        PM.targetHattan = lv;
                    }
                }
            }
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

        double hy = (targetPos.y() - (this.getEyeP(0).y()));
        double hd = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));

        float hrot = (float) (Math.atan2(hd, hy) + Math.PI / 2);

        if (target != null) {
            return new Vec3(hrot, rot, 0);
        }

        return new Vec3(0, 0, 0);


    }

    public boolean isInRain() {
        BlockPos $$0 = this.blockPosition();
        return this.level().isRainingAt($$0)
                || this.level().isRainingAt(BlockPos.containing((double) $$0.getX(), this.getBoundingBox().maxY, (double) $$0.getZ()));
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
    public final AnimationState slow_manhattan_back = new AnimationState();
    public final AnimationState slow_manhattan_left = new AnimationState();
    public final AnimationState slow_manhattan_right = new AnimationState();
    public final AnimationState manhattan_is_loaded = new AnimationState();


    public boolean W = false;
    public  boolean A = false;
    public  boolean S = false;
    public  boolean D = false;

    public  boolean pressS = false;
    public  boolean pressA = false;

    public  boolean verticalLastPressed = true;

    public  boolean isPressing = false;

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        AnimationState rd = this.rain_dodging_manhattan;
        AnimationState loaded = this.manhattan_is_loaded;

        AnimationState forBeg = this.forward_manhattan_incipit;
        AnimationState forLoop = this.forward_manhattan_loop;
        AnimationState forStop = this.forward_manhattan_stop;

        AnimationState backBeg = this.back_manhattan_incipit;
        AnimationState backLoop = this.back_manhattan_loop;
        AnimationState backStop = this.back_manhattan_stop;

        AnimationState leftBeg = this.left_manhattan_incipit;
        AnimationState leftLoop = this.left_manhattan_loop;
        AnimationState leftStop = this.left_manhattan_stop;

        AnimationState rightBeg = this.right_manhattan_incipit;
        AnimationState rightLoop = this.right_manhattan_loop;
        AnimationState rightStop = this.right_manhattan_stop;

        AnimationState forSlow = this.slow_manhattan;
        AnimationState backSlow = this.slow_manhattan_back;
        AnimationState leftSlow = this.slow_manhattan_left;
        AnimationState rightSlow = this.slow_manhattan_right;


        if (this.level().isClientSide) {
            if (!this.isInRain()) {
                if ((W || A || S || D) && isHattanPilotMode) {
                    isPressing = true;
                } else if ((W && S) || (A && D)){
                    isPressing = false;
                }else {
                    isPressing = false;
                }
                rd.stop();
                if (!this.stopsManhattanAnimationsWhenHeldItem) {
                    loaded.stop();
                    if (isPressing) {
                        forSlow.stop();
                        backSlow.stop();
                        leftSlow.stop();
                        rightSlow.stop();
                        if (W) {
                            backLoop.stop();
                            forStop.stop();
                            forBeg.startIfStopped(this.tickCount);
                            forLoop.startIfStopped(this.tickCount);
                        }
                        if (!W) {
                            forBeg.stop();
                            forLoop.stop();
                            forStop.startIfStopped(this.tickCount);
                        }
                        if (S) {
                            forLoop.stop();
                            backStop.stop();
                            backBeg.startIfStopped(this.tickCount);
                            backLoop.startIfStopped(this.tickCount);
                        }
                        if (!S) {
                            backLoop.stop();
                            backBeg.stop();
                            backStop.startIfStopped(this.tickCount);
                        }

                        if (A) {
                            rightLoop.stop();
                            leftStop.stop();
                            leftBeg.startIfStopped(this.tickCount);
                            leftLoop.startIfStopped(this.tickCount);
                        }
                        if (!A) {
                            leftBeg.stop();
                            leftLoop.stop();
                            leftStop.startIfStopped(this.tickCount);
                        }

                        if (D) {
                            leftLoop.stop();
                            rightStop.stop();
                            rightBeg.startIfStopped(this.tickCount);
                            rightLoop.startIfStopped(this.tickCount);
                        }
                        if (!D) {
                            rightBeg.stop();
                            rightLoop.stop();
                            rightStop.startIfStopped(this.tickCount);
                        }
                    } else {
                        if (forLoop.isStarted()) {
                            forBeg.stop();
                            forLoop.stop();
                            backLoop.stop();
                            backBeg.stop();
                            forStop.startIfStopped(this.tickCount);
                        }
                        if (backLoop.isStarted()) {
                            backLoop.stop();
                            backBeg.stop();
                            forBeg.stop();
                            forLoop.stop();
                            backStop.startIfStopped(this.tickCount);
                        }
                        if (leftLoop.isStarted()) {
                            leftLoop.stop();
                            leftBeg.stop();
                            rightBeg.stop();
                            rightLoop.stop();
                            leftStop.startIfStopped(this.tickCount);
                        }
                        if (rightLoop.isStarted()) {
                            leftLoop.stop();
                            leftBeg.stop();
                            rightBeg.stop();
                            rightLoop.stop();
                            rightStop.startIfStopped(this.tickCount);
                        }

                        if (setHatAnimDir == 1) {
                            forSlow.startIfStopped(this.tickCount);
                            backSlow.stop();
                            leftSlow.stop();
                            rightSlow.stop();
                        }
                        if (setHatAnimDir == 2) {
                            backSlow.startIfStopped(this.tickCount);
                            leftSlow.stop();
                            rightSlow.stop();
                            forSlow.stop();
                        }
                        if (setHatAnimDir == 3) {
                            leftSlow.startIfStopped(this.tickCount);
                            rightSlow.stop();
                            forSlow.stop();
                            backSlow.stop();
                        }
                        if (setHatAnimDir == 4) {
                            rightSlow.startIfStopped(this.tickCount);
                            leftSlow.stop();
                            backSlow.stop();
                            forSlow.stop();
                        }
                    }
                } else {
                    loaded.startIfStopped(this.tickCount);
                    forLoop.stop();
                    forBeg.stop();
                    forStop.stop();
                    forSlow.stop();
                    backBeg.stop();
                    backSlow.stop();
                    backLoop.stop();
                    backStop.stop();
                    leftSlow.stop();
                    leftBeg.stop();
                    leftStop.stop();
                    leftLoop.stop();
                    rightBeg.stop();
                    rightSlow.stop();
                    rightStop.stop();
                    rightLoop.stop();
                }
            } else {
                rd.startIfStopped(this.tickCount);
                loaded.stop();
                forLoop.stop();
                forBeg.stop();
                forStop.stop();
                forSlow.stop();
                backBeg.stop();
                backSlow.stop();
                backLoop.stop();
                backStop.stop();
                leftSlow.stop();
                leftBeg.stop();
                leftStop.stop();
                leftLoop.stop();
                rightBeg.stop();
                rightSlow.stop();
                rightStop.stop();
                rightLoop.stop();
            }
        }
    }
}
