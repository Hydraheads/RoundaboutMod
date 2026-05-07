package net.hydra.jojomod.entity.stand;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.networking.ServerToClientPackets;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersMagiciansRed;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.hydra.jojomod.stand.powers.PowersManhattanTransfer;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.apache.commons.compress.archivers.sevenz.CLI;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public class ManhattanTransferEntity extends StandEntity {
    private Entity Projectile;

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
    public boolean canBeHitByStands() {
        return true;
    }
    @Override
    public boolean canBeHitByProjectile() {return true;}
    @Override
    public boolean hasNoPhysics() {
        return false;
    }
    @Override
    public boolean standHasGravity() {
        return false;
    }
    @Override
    public boolean isInvulnerable() {
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

    @Override
    public boolean hurt(DamageSource source, float amount) {

        Entity direct = source.getDirectEntity();
        Entity directEntityWho = source.getEntity();

        boolean success = false;

        if(User != null){
            if (directEntityWho != null && direct != null) {
                if (direct instanceof Projectile PR && !source.is(ModDamageTypes.STAND)) {
                    if (directEntityWho.is(User)) {
                        if (!hasItem) {
                            success = true;
                            if (direct instanceof AbstractArrow AA && !(AA instanceof StandArrowEntity)) {
                                ItemStack ii = ((IAbstractArrowAccess) AA).roundabout$GetPickupItem();
                                if (!ii.isEmpty() && !ii.isDamageableItem()) {
                                    success = true;
                                    hasItem = true;
                                    if (AA.pickup.equals(AbstractArrow.Pickup.ALLOWED)) {
                                        this.canAcquireHeldItem = true;
                                    } else {
                                        this.canAcquireHeldItem = false;
                                    }
                                    this.setHeldItemManhattan(ii.copyAndClear());
                                    PR.discard();
                                } else if (AA instanceof RoundaboutBulletEntity BE) {
                                    success = true;
                                    hasItem = true;
                                    this.canAcquireHeldItem = true;
                                    ItemStack bulletItem = BE.getBulletItemStack();
                                    this.setHeldItemManhattan(bulletItem);
                                    PR.discard();
                                }
                            } else if (direct instanceof ThrownObjectEntity TO) {
                                ItemStack ii = TO.getItem();
                                if (!ii.isEmpty()) {
                                    success = true;
                                    hasItem = true;
                                    this.canAcquireHeldItem = true;
                                    this.setHeldItemManhattan(ii.copyAndClear());
                                    TO.discard();
                                }
                            } else if (direct instanceof ThrownPotion TP) {
                                ItemStack ii = TP.getItem();
                                if (!ii.isEmpty()) {
                                    success = true;
                                    hasItem = true;
                                    this.canAcquireHeldItem = true;
                                    this.setHeldItemManhattan(ii.copyAndClear());
                                    TP.discard();
                                }
                            }/* else if (direct instanceof FireworkRocketEntity RE) {
                            ItemStack ii = RE.getItem();
                            if (!ii.isEmpty()) {
                                success = true;
                                hasItem = true;
                                this.setHeldItemManhattan(ii.copyAndClear());
                                RE.getItem().shrink(1);
                                RE.discard();
                            }
                        }*/ else if (direct instanceof ThrowableItemProjectile TH) {
                                ItemStack ii = TH.getItem();
                                if (!ii.isEmpty()) {
                                    success = true;
                                    hasItem = true;
                                    this.canAcquireHeldItem = true;
                                    this.setHeldItemManhattan(ii.copyAndClear());
                                    TH.discard();
                                }
                            }
                        } else if (hasItem) {
                            success = true;
                            if (direct instanceof AbstractArrow AA) {
                                ItemStack ii = ((IAbstractArrowAccess) AA).roundabout$GetPickupItem();
                                if (!ii.isEmpty() && !ii.isDamageableItem()) {
                                    success = true;
                                    if (AA.pickup.equals(AbstractArrow.Pickup.ALLOWED)) {
                                        this.setHeldItemManhattanFull(ii.copyAndClear());
                                        PR.discard();
                                    } else {
                                        PR.discard();
                                    }
                                } else if (AA instanceof RoundaboutBulletEntity BE) {
                                    success = true;
                                    ItemStack bulletItem = BE.getBulletItemStack();
                                    this.setHeldItemManhattanFull(bulletItem);
                                    PR.discard();
                                }
                            } else if (direct instanceof ThrownObjectEntity TO) {
                                ItemStack ii = TO.getItem();
                                if (!ii.isEmpty()) {
                                    success = true;
                                    this.setHeldItemManhattanFull(ii.copyAndClear());
                                    TO.discard();
                                }
                            } else if (direct instanceof ThrownPotion TP) {
                                ItemStack ii = TP.getItem();
                                if (!ii.isEmpty()) {
                                    success = true;
                                    this.setHeldItemManhattanFull(ii.copyAndClear());
                                    TP.discard();
                                }
                            }/* else if (direct instanceof FireworkRocketEntity RE) {
                            ItemStack ii = RE.getItem();
                            if (!ii.isEmpty()) {
                                success = true;
                                hasItem = true;
                                this.setHeldItemManhattanFull(ii.copyAndClear());
                                RE.getItem().shrink(1);
                                RE.discard();
                            }
                        }*/ else if (direct instanceof ThrowableItemProjectile TH) {
                                ItemStack ii = TH.getItem();
                                if (!ii.isEmpty()) {
                                    success = true;
                                    hasItem = true;
                                    this.canAcquireHeldItem = true;
                                    this.setHeldItemManhattanFull(ii.copyAndClear());
                                    TH.discard();
                                }
                            }
                        }
                    } else if (directEntityWho != User) {
                        if(!hasItem) {
                            success = true;
                            if (direct instanceof AbstractArrow AA) {
                                ItemStack ii = ((IAbstractArrowAccess) AA).roundabout$GetPickupItem();
                                if (!ii.isEmpty() && !ii.isDamageableItem()) {
                                    success = true;
                                    hasItem = true;
                                    if (AA.pickup.equals(AbstractArrow.Pickup.ALLOWED)) {
                                        this.canAcquireHeldItem = true;
                                    } else {
                                        this.canAcquireHeldItem = false;
                                    }
                                    this.setHeldItemManhattanFull(ii.copyAndClear());
                                    PR.discard();
                                } else if (AA instanceof RoundaboutBulletEntity BE) {
                                    success = true;
                                    hasItem = true;
                                    this.canAcquireHeldItem = true;
                                    ItemStack bulletItem = BE.getBulletItemStack();
                                    this.setHeldItemManhattanFull(bulletItem);
                                    PR.discard();
                                }
                            } else if (direct instanceof ThrownObjectEntity TO) {
                                ItemStack ii = TO.getItem();
                                if (!ii.isEmpty()) {
                                    success = true;
                                    hasItem = true;
                                    this.canAcquireHeldItem = true;
                                    this.setHeldItemManhattanFull(ii.copyAndClear());
                                    TO.discard();
                                }
                            } else if (direct instanceof ThrownPotion TP) {
                                ItemStack ii = TP.getItem();
                                if (!ii.isEmpty()) {
                                    success = true;
                                    hasItem = true;
                                    this.canAcquireHeldItem = true;
                                    this.setHeldItemManhattanFull(ii.copyAndClear());
                                    TP.discard();
                                }
                            }/* else if (direct instanceof FireworkRocketEntity RE) {
                            ItemStack ii = RE.getItem();
                            if (!ii.isEmpty()) {
                                success = true;
                                hasItem = true;
                                this.setHeldItemManhattan(ii.copyAndClear());
                                RE.getItem().shrink(1);
                                RE.discard();
                            }
                        }*/ else if (direct instanceof ThrowableItemProjectile TH) {
                                ItemStack ii = TH.getItem();
                                if (!ii.isEmpty()) {
                                    success = true;
                                    hasItem = true;
                                    this.canAcquireHeldItem = true;
                                    this.setHeldItemManhattanFull(ii.copyAndClear());
                                    TH.discard();
                                }
                            }
                        }
                        else if (hasItem) {
                            success = true;
                            if (direct instanceof AbstractArrow AA) {
                                ItemStack ii = ((IAbstractArrowAccess) AA).roundabout$GetPickupItem();
                                if (!ii.isEmpty() && !ii.isDamageableItem()) {
                                    success = true;
                                    if (AA.pickup.equals(AbstractArrow.Pickup.ALLOWED)) {
                                        this.setHeldItemManhattanFull(ii.copyAndClear());
                                        PR.discard();
                                    } else {
                                        PR.discard();
                                    }
                                } else if (AA instanceof RoundaboutBulletEntity BE) {
                                    success = true;
                                    ItemStack bulletItem = BE.getBulletItemStack();
                                    this.setHeldItemManhattanFull(bulletItem);
                                    PR.discard();
                                }
                            } else if (direct instanceof ThrownObjectEntity TO) {
                                ItemStack ii = TO.getItem();
                                if (!ii.isEmpty()) {
                                    success = true;
                                    this.setHeldItemManhattanFull(ii.copyAndClear());
                                    TO.discard();
                                }
                            } else if (direct instanceof ThrownPotion TP) {
                                ItemStack ii = TP.getItem();
                                if (!ii.isEmpty()) {
                                    success = true;
                                    this.setHeldItemManhattanFull(ii.copyAndClear());
                                    TP.discard();
                                }
                            }/* else if (direct instanceof FireworkRocketEntity RE) {
                            ItemStack ii = RE.getItem();
                            if (!ii.isEmpty()) {
                                success = true;
                                hasItem = true;
                                this.setHeldItemManhattanFull(ii.copyAndClear());
                                RE.getItem().shrink(1);
                                RE.discard();
                            }
                        }*/ else if (direct instanceof ThrowableItemProjectile TH) {
                                ItemStack ii = TH.getItem();
                                if (!ii.isEmpty()) {
                                    success = true;
                                    this.setHeldItemManhattanFull(ii.copyAndClear());
                                    TH.discard();
                                }
                            }
                        }
                    }
                }
            }

            if(hasItem && this.canAcquireHeldItem){
              //  System.out.println("Occupied by" + this.getHeldItemManhattan());
                if(!this.getHeldItemManhattanFull().isEmpty() && !this.getHeldItemManhattanFull().isEmpty()){
                    double $$3 = this.getEyeY() - 0.3F;
                    ItemEntity $$4 = new ItemEntity(this.level(), this.getX(), $$3, this.getZ(), this.getHeldItemManhattanFull());
                    $$4.setThrower(this.getUUID());
                    this.level().addFreshEntity($$4);
                    this.setHeldItemManhattanFull(ItemStack.EMPTY);
                }
            }
            else if(!this.canAcquireHeldItem){
                hasItem = false;
            }

            if (success) {
               // System.out.println("loaded with " + this.getHeldItemManhattan());
                return super.hurt(source, 0);
            }
        }

        this.markHurt();
        return super.hurt(source, amount);
    }


    public boolean canAcquireHeldItem = false;

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
                   /* if (isInRain()) {
                        if (DodgeRainTicks > 0) {
                            DodgeRainTicks--;

                        } else {
                            setDodgeRainTicks(440);
                           // Roundabout.LOGGER.info("bwaah");
                          //  this.level().playSound(null, this.blockPosition(), ModSounds.MANHATTAN_DODGING_EVENT, SoundSource.NEUTRAL, 1F, (float) (0.9F + (Math.random() * 0.2F)));
                        }
                    }*/
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

                        this.setYBodyRot(pitch + 25);

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
      /*  if (horizontalCollision || verticalCollision) {
                this.getUserData(this.getUser()).roundabout$getStandPowers();
                if (this.getUserData(this.getUser()).roundabout$getStandPowers() instanceof PowersManhattanTransfer PM) {
                        if (!PM.isPiloting()) {
                            this.setXRot(pitch + 25);

                            this.setYBodyRot(pitch + 25);

                            this.setYRot(yaw);
                            if (yaw >= -90 && yaw <= 0) {
                                this.setYRot(yaw - 25);
                            }
                            if (yaw <= 90 && yaw > 0) {
                                this.setYRot(yaw + 25);
                            }
                        }
                    }
                }*/
            if (!this.level().isClientSide()) {
                if (!forceVisible) {
                    this.setXRot(pitch);
                    this.setYRot(yaw);
                    this.setYBodyRot(yaw);
                    this.xRotO = pitch;
                    this.yRotO = yaw;

                }
            }
        nextPathfind++;
        doBasicPathfind();
    }
    int stupidTicks = 1;
    int nextPathfind = 1;

    public void doBasicPathfind() {

        Vec3 vec3d = this.getEyePosition(0);
        Vec3 vec3d2 = this.getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x, vec3d2.y, vec3d2.z);
        BlockHitResult blockHit = this.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        BlockPos pos = blockHit.getBlockPos();
        this.navigation.moveTo(pos.getX(), pos.getY(), pos.getZ(), 0);
    }

    public boolean isInRain() {
        BlockPos $$0 = this.blockPosition();
        return this.level().isRainingAt($$0)
                || this.level().isRainingAt(BlockPos.containing((double)$$0.getX(), this.getBoundingBox().maxY, (double)$$0.getZ()));
    }

    public final AnimationState rain_dodging_manhattan = new AnimationState();

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if(this.getUserData(this.getUser()) != null && this.getUser() != null) {
            if (this.getUserData(this.getUser()).roundabout$getStandPowers() instanceof PowersManhattanTransfer PM) {
                if (!PM.isActive()) {
                    //this.rain_dodging_manhattan.stop();
                } else {
                    if (isInRain()) {
                        this.rain_dodging_manhattan.startIfStopped(this.tickCount);
                    }
                    if (!isInRain()) {
                        this.rain_dodging_manhattan.stop();
                    }
                }
            }
        }
    }

}
