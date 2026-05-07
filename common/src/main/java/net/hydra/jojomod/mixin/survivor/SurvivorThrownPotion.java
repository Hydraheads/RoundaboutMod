package net.hydra.jojomod.mixin.survivor;

import net.hydra.jojomod.entity.stand.SurvivorEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownPotion.class)
public abstract class SurvivorThrownPotion extends ThrowableItemProjectile implements ItemSupplier {

    /**Makes thrown potions activate survivor, this includes throwable water bottles which
     * are in fact classified as a potion, the custom normal water bottle throw entity for survivor
     * is not included in here but in its own class*/

    @Inject(method = "onHit", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$onHit(HitResult $$0,CallbackInfo ci) {
        HitResult.Type $$1 = $$0.getType();
        if ($$1 == HitResult.Type.ENTITY) {
            Entity $$2x = ((EntityHitResult)$$0).getEntity();
            if ($$2x instanceof LivingEntity LE){
                if (((StandUser)LE).roundabout$getStandPowers().dealWithProjectile(this,$$0)){
                    ci.cancel();
                    this.discard();
                    return;
                } else if (((StandUser)LE).roundabout$getStandPowers().dealWithProjectileNoDiscard(this,$$0)){
                    ci.cancel();
                    return;
                }
            }
        }

        AABB aab = this.getBoundingBox().inflate(4.0, 2.0, 4.0);
        for (SurvivorEntity $$5 : this.level().getEntitiesOfClass(SurvivorEntity.class, aab)) {
            $$5.setActivated(true);
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */


    public SurvivorThrownPotion(EntityType<? extends ThrowableItemProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }

}
