package net.hydra.jojomod.mixin.manhattan;

import net.hydra.jojomod.access.AccessThrownTrident;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.entity.stand.BlackSabbathEntity;
import net.hydra.jojomod.entity.stand.ManhattanTransferEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersManhattanTransfer;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.Cancellable;

import java.util.List;

@Mixin(ThrownTrident.class)
    public abstract class ChannelingTridentFix extends AbstractArrow implements AccessThrownTrident {

        protected ChannelingTridentFix(EntityType<? extends AbstractArrow> $$0, Level $$1) {
            super($$0, $$1);
        }

        @Inject(method = "onHitEntity", at = @At(value = "HEAD"),cancellable = true)
        private void roundabout$onHit(EntityHitResult $$0, CallbackInfo ci) {
                Entity $$2x = $$0.getEntity();
                ThrownTrident thrownTr = (ThrownTrident) (Object) (this);
                if($$2x instanceof ManhattanTransferEntity ME){
                    ci.cancel();
                    thrownTr.discard();
                    if ((thrownTr.getOwner().is(ME.getUser()) && !ME.canOthersLoadMT || ME.canOthersLoadMT) && !ME.hasItem) {
                        ItemStack ii = this.getPickupItem();
                        if (!ii.isEmpty()) {
                            ci.cancel();
                            if(ci.isCancelled()) {
                                if(ME.getUser() instanceof Player PL && ((StandUser) PL).roundabout$getStandPowers() instanceof  PowersManhattanTransfer PM){
                                    if(ME.getHattanTarget() == 0 || PM.switchShootingMode()) {
                                        $$2x.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
                                        PM.getSelf().level().playSound(null, PM.getSelf().blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                                    } else {
                                        PM.getSelf().level().playSound(null, PM.getSelf().blockPosition(), ModSounds.BULLET_RICOCHET_EVENT, SoundSource.PLAYERS, 1F, (this.random.nextFloat() * 0.2F + 0.7F));
                                    }
                                }
                                ME.setHeldItemManhattan(ii.copyAndClear());
                                if (this.getOwner() == null || this.getOwner() instanceof Player) {
                                    if (thrownTr.pickup.equals(AbstractArrow.Pickup.ALLOWED)) {
                                        ME.canAcquireHeldItem = true;
                                    } else {
                                        ME.canAcquireHeldItem = false;
                                    }
                                    ME.hasItemTwo = false;
                                } else {
                                    ME.canAcquireHeldItem = false;
                                    ME.hasItemTwo = false;
                                }
                                ME.hasItem = true;
                                ME.changeMovementState();
                                this.discard();
                            }
                        }
                    } else {
                        ItemStack ii = this.getPickupItem();
                        if (!ii.isEmpty()) {
                            ci.cancel();
                            if(ci.isCancelled()) {
                                if(this.getOwner() != null) {
                                    if (this.getOwner() == null || this.getOwner() instanceof Player) {
                                        if (thrownTr.pickup.equals(AbstractArrow.Pickup.ALLOWED)) {
                                            ME.canAcquireHeldItem = true;
                                            ME.setHeldItemManhattanFull(ii.copyAndClear());
                                            ME.hasItemTwo = true;
                                            ME.itemEject();
                                        } else {
                                            this.discard();
                                        }
                                    } else {
                                        this.discard();
                                    }
                                    this.discard();
                                }
                            }
                        }
                    }
                } else if ($$2x instanceof BlackSabbathEntity be){
                        if (thrownTr.getOwner() != null && be.getUser() != null && thrownTr.getOwner().is(be.getUser())) {
                            if(!this.level().isClientSide() && this.roundabout$getReturnToBlackSabbath()) {
                                ci.cancel();
                            }
                        }
                }
            }

    private static final EntityDataAccessor<Byte> ID_LOYALTY_SABBATH = SynchedEntityData.defineId(ThrownTrident.class, EntityDataSerializers.BYTE);
        public final Byte getLoyalSabbath() {
            return this.entityData.get(ID_LOYALTY_SABBATH);
        }
            @Inject(method = "tick", at = @At(value = "HEAD"),cancellable = true)
            private void roundabout$tick(CallbackInfo ci) {
            if(this.inGroundTime > 4){
                dealtDamage = true;
            }

                ThrownTrident thrownTr = (ThrownTrident) (Object) (this);
                if(!this.level().isClientSide() && roundabout$getReturnToBlackSabbath()){
                    int loyalty = EnchantmentHelper.getLoyalty(this.getPickupItem());
                    if(getLoyalSabbath() == 0 && loyalty > 0) {
                        if(!this.level().isClientSide) {
                            setBlackSabbath((byte) loyalty);
                        }
                    }
                }
                if(getLoyalSabbath() > 0 && (dealtDamage || this.isNoPhysics()) && this.getOwner() != null){
                    if (!this.isAcceptibleReturnOwner()) {
                        if (!this.level().isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                            this.spawnAtLocation(this.getPickupItem(), 0.1F);
                        }
                        this.discard();
                    } else {
                        StandEntity stand = ((StandUser)thrownTr.getOwner()).roundabout$getStand();
                        if(stand != null && stand instanceof BlackSabbathEntity be) {
                            ci.cancel();
                            AABB beHitbox = be.getBoundingBox().inflate(1.5, 1.5, 1.5);
                            List<ThrownTrident> tr = this.level().getEntitiesOfClass(ThrownTrident.class, beHitbox, (livingEntity) -> {
                                return true;
                            });
                            if(tr.contains(thrownTr)){
                                if(be.getUser() instanceof Player pl) {
                                    ItemStack tridentCopy = this.getPickupItem().copy();
                                    IPlayerEntity play = ((IPlayerEntity)pl);
                                    play.roundabout$getBlckSabbathPlayerInventory().setItem(0, tridentCopy);
                                    this.level()
                                            .playSound(
                                                    null,
                                                    this.getX(),
                                                    this.getY(),
                                                    this.getZ(),
                                                    SoundEvents.ITEM_PICKUP,
                                                    SoundSource.PLAYERS,
                                                    0.2F,
                                                    ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F
                                            );
                                }
                                thrownTr.discard();
                                return;
                            }

                            int loyalty =this.getLoyalSabbath();
                            this.setNoPhysics(true);
                            Vec3 $$2 = be.getEyePosition().subtract(this.position());
                            this.setPosRaw(this.getX(), this.getY() + $$2.y * 0.015 * (double) loyalty, this.getZ());
                            if (this.level().isClientSide) {
                                this.yOld = this.getY();
                            }
                            double $$3 = 0.05 * (double) loyalty;
                            this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add($$2.normalize().scale($$3)));
                            if (thrownTr.clientSideReturnTridentTickCount == 0) {
                                this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                            }
                            thrownTr.clientSideReturnTridentTickCount++;
                            super.tick();
                        } else {
                            if(roundabout$getReturnToBlackSabbath()){
                                roundabout$setReturnToBlackSabbath(false);
                            }
                        }
                        return;
                    }
                }
            }

    @Inject(method = "tryPickup", at = @At(value = "HEAD"), cancellable = true, require = 0)
    public void roundabout$canBePickedUp(CallbackInfoReturnable<Boolean> cir) {
        if (!this.level().isClientSide() && roundabout$getReturnToBlackSabbath()) {
            cir.setReturnValue(false);
        }
    }
        public final void setBlackSabbath(Byte bt) {
            this.entityData.set(ID_LOYALTY_SABBATH, bt);
        }
        @Inject(method = "defineSynchedData", at = @At(value = "TAIL"), cancellable = true)
        protected void roundabout$defineSynchedData(CallbackInfo ci) {
            if (!((ThrownTrident)(Object)this).getEntityData().hasItem(ID_LOYALTY_SABBATH)) {
                this.entityData.define(ID_LOYALTY_SABBATH, (byte)0);
            }
        }
        protected ItemStack getPickupItem;
        private boolean dealtDamage;
        private boolean returnToSabbarh = false;
        public void roundabout$setReturnToBlackSabbath(boolean bool){
            returnToSabbarh = bool;
        }
       public  boolean roundabout$getReturnToBlackSabbath(){
            return returnToSabbarh;
        }

    //Shadows, ignore
    @Shadow
    private boolean isAcceptibleReturnOwner() {return false;}

    @Shadow
    protected boolean tryPickup(Player $$0) {
        return false;
    }

    }