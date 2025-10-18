package net.hydra.jojomod.mixin.fates;

import net.hydra.jojomod.event.index.FateTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class FateLivingEntity extends Entity {

    @Inject(method = "canBreatheUnderwater", at = @At(value = "HEAD"), cancellable = true)
    protected void rdbt$canBreatheUnderwater(CallbackInfoReturnable<Boolean> cir) {
        if (FateTypes.hasBloodHunger((LivingEntity) (Object) this)){
            cir.setReturnValue(true);
        }
    }

    public FateLivingEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }
}
