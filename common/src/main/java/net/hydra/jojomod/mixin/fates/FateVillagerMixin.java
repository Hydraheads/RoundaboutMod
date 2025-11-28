package net.hydra.jojomod.mixin.fates;

import net.hydra.jojomod.access.IMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public abstract class FateVillagerMixin extends AbstractVillager {

    /**Villagers do not alert while hypnotized*/
    @Inject(method = "onReputationEventFrom", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$onReputationEventFrom(ReputationEventType $$0, Entity $$1, CallbackInfo ci) {
        if ($$1 instanceof LivingEntity PL) {
            LivingEntity living = ((IMob)this).roundabout$getHypnotizedBy();
            if (living != null && PL.is(living)){
                if (!$$0.equals(ReputationEventType.VILLAGER_KILLED)){
                    ci.cancel();
                }
            }
        }
    }
    @Inject(method = "setLastHurtByMob(Lnet/minecraft/world/entity/LivingEntity;)V", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$setLastHurtByMob(LivingEntity $$0, CallbackInfo ci) {
        LivingEntity living = ((IMob)this).roundabout$getHypnotizedBy();
        if (living != null && $$0 != null && $$0.is(living)){
            ci.cancel();
            super.setLastHurtByMob($$0);
        }
    }
    public FateVillagerMixin(EntityType<? extends AbstractVillager> $$0, Level $$1) {
        super($$0, $$1);
    }
}
