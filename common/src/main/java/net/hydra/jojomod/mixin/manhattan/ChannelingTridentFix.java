package net.hydra.jojomod.mixin.manhattan;

import net.hydra.jojomod.entity.stand.ManhattanTransferEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersManhattanTransfer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

    @Mixin(ThrownTrident.class)
    public abstract class ChannelingTridentFix extends AbstractArrow {

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
                                ME.setHeldItemManhattanFull(ii.copyAndClear());
                                if (this.getOwner() == null || this.getOwner() instanceof Player) {
                                    ME.canAcquireHeldItem = true;
                                } else {
                                    ME.canAcquireHeldItem = false;
                                }
                                ME.hasItemTwo = true;
                                ME.itemEject();
                                this.discard();
                            }
                        }
                    }
                }
            }
        }