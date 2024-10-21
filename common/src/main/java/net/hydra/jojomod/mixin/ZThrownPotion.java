package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownPotion.class)
public abstract class ZThrownPotion extends ThrowableItemProjectile implements ItemSupplier {

    public ZThrownPotion(EntityType<? extends ThrowableItemProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "onHit", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$onHit(HitResult $$0,CallbackInfo ci) {
        HitResult.Type $$1 = $$0.getType();
        if ($$1 == HitResult.Type.ENTITY) {
            Entity $$2 = ((EntityHitResult)$$0).getEntity();
            if ($$2 instanceof LivingEntity LE){
                if (((StandUser)LE).roundabout$getStandPowers().dealWithProjectile(this)){
                    ci.cancel();
                    this.discard();
                }
            }
        }
    }
}
