package net.hydra.jojomod.mixin.manhattan;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.stand.ManhattanTransferEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersManhattanTransfer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownEgg.class)
public abstract class EggAccess extends ThrowableItemProjectile {

    /**For use to everyone who'll need this :)*/

    public EggAccess(EntityType<? extends ThrowableItemProjectile> $$0, LivingEntity $$1, Level $$2) {
        super($$0, $$1, $$2);
    }

    @Inject(method = "onHit", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$onHit(HitResult $$0,CallbackInfo ci) {
        HitResult.Type $$1 = $$0.getType();
        if ($$1 == HitResult.Type.ENTITY) {
            Entity $$2x = ((EntityHitResult) $$0).getEntity();
            ThrownEgg thrownEgg = (ThrownEgg) (Object) (this);
            if($$2x instanceof ManhattanTransferEntity ME){
                Roundabout.LOGGER.info("aaaaa");
                ci.cancel();
                thrownEgg.discard();
                if ((this.getOwner().is(ME.getUser()) && !ME.canOthersLoadMT || ME.canOthersLoadMT) && !ME.hasItem) {
                    ItemStack ii = this.getItem();
                    if (!ii.isEmpty()) {
                        ci.cancel();
                        if(ci.isCancelled()) {
                            if(ME.getUser() instanceof Player PL && ((StandUser) PL).roundabout$getStandPowers() instanceof  PowersManhattanTransfer PM){
                                if(ME.getHattanTarget() == 0 || PM.switchShootingMode()) {
                                    PM.getSelf().level().playSound(null, PM.getSelf().blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                                } else {
                                    PM.getSelf().level().playSound(null, PM.getSelf().blockPosition(), ModSounds.BULLET_RICOCHET_EVENT, SoundSource.PLAYERS, 1F, (this.random.nextFloat() * 0.2F + 0.7F));
                                }
                            }
                            ME.setHeldItemManhattan(ii.copyAndClear());
                            if (this.getOwner() == null || this.getOwner() instanceof Player) {
                                ME.canAcquireHeldItem = true;
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
                    ItemStack ii = this.getItem();
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

}