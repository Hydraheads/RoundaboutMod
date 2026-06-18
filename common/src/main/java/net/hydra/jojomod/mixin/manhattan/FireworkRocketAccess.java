package net.hydra.jojomod.mixin.manhattan;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IFireworkRocketAccess;
import net.hydra.jojomod.access.ISuperThrownAbstractArrow;
import net.hydra.jojomod.entity.stand.ManhattanTransferEntity;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketAccess implements IFireworkRocketAccess {

    int remainingLifeTicks = 0;
    void setRemainingLifeTicks(int ticks){remainingLifeTicks = ticks;}

    boolean isHat = false;

    @Override
    public void setIsHattanProj(boolean proj){
        isHat = proj;
    }

    @Inject(method = "onHitEntity", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$onHitEntity(EntityHitResult $$0, CallbackInfo ci) {
        Entity Hattan = ((EntityHitResult) $$0).getEntity();
        FireworkRocketEntity fwork = (FireworkRocketEntity) (Object) (this);
        if(Hattan instanceof ManhattanTransferEntity ME){

            ci.cancel();
            fwork.discard();
            ItemStack ii = fwork.getItem();

                if ((fwork.getOwner().is(ME.getUser()) && !ME.canOthersLoadMT || ME.canOthersLoadMT) && !ME.hasItem) {
                    if (!ii.isEmpty()) {
                        ci.cancel();
                        if (ci.isCancelled()) {
                            ME.setHeldItemManhattan(ii.copyAndClear());
                            if (fwork.getOwner() == null || fwork.getOwner() instanceof Player) {
                                ME.canAcquireHeldItem = true;
                                ME.hasItemTwo = false;
                            } else {
                                ME.canAcquireHeldItem = false;
                                ME.hasItemTwo = false;
                            }
                            ME.hasItem = true;
                            ME.changeMovementState();
                            ME.fireworkLifeTicks = this.life;
                            fwork.discard();
                        }
                    }
                } else {
                    FireworkRocketEntity $$4 = new FireworkRocketEntity(ME.level(), ii, ME, ME.getX(), ME.getEyeY(), ME.getZ(), true);
                    $$4.setRemainingFireTicks(ME.fireTicksPrj);
                    $$4.setOwner(ME.getUser());
                    $$4.shootFromRotation(ME, ME.shootRotationXHattan, ME.shootRotationYHattan, 0.0F, 1.4F, 0.0F);
                    ((IFireworkRocketAccess) $$4).roundabout$SetFireworkRemainingLifeTicks(this.roundabout$GetFireworkRemainingLifeTicks());
                    ME.level().addFreshEntity($$4);
                }

        }
        if(isHat){
            Hattan.hurt(ModDamageTypes.of(fwork.level(), ModDamageTypes.STAND, fwork, fwork.getOwner()), 2);
        }
    }

    @Override
    public int roundabout$GetFireworkRemainingLifeTicks(){
        return life;
    }
    @Override
    public void roundabout$SetFireworkRemainingLifeTicks(int life2){
        this.life = life2;
    }
    @Shadow
    private int life;


}