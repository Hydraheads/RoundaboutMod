package net.hydra.jojomod.mixin.fates;

import net.hydra.jojomod.event.index.FateTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class FateLivingEntity extends Entity {

    @Inject(method = "canBreatheUnderwater", at = @At(value = "HEAD"), cancellable = true)
    protected void rdbt$canBreatheUnderwater(CallbackInfoReturnable<Boolean> cir) {
        if (FateTypes.hasBloodHunger((LivingEntity) (Object) this)){
            cir.setReturnValue(true);
        }
    }
    @Inject(method = "addEatEffect(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)V", at = @At(value = "HEAD"), cancellable = true)
    protected void rdbt$addEatEffect(ItemStack $$0, Level $$1, LivingEntity $$2, CallbackInfo ci) {
        if (FateTypes.hasBloodHunger((LivingEntity) (Object) this)){
            ci.cancel();
        }
    }


    public FateLivingEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }
}
