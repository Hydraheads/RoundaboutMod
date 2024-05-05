package net.hydra.jojomod.mixin;

import net.hydra.jojomod.networking.MyComponents;
import net.hydra.jojomod.networking.component.StandUserComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public class MobEntityMixin {

    /**Mobs in a barrage should not be attacking*/
    @Inject(method = "tryAttack", at = @At(value = "HEAD"), cancellable = true)
    private void RoundaboutTryAttack(Entity target, CallbackInfoReturnable<Boolean> ci) {
        StandUserComponent standUserData = MyComponents.STAND_USER.get(this);
        if (standUserData.isDazed()) {
            ci.setReturnValue(false);
        }
    }

}
