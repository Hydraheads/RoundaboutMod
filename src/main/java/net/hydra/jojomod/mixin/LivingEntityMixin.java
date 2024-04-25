package net.hydra.jojomod.mixin;

import net.hydra.jojomod.networking.MyComponents;
import net.hydra.jojomod.networking.component.StandUserComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    /**If you are stand guarding, this controls you blocking enemy atttacks.
     * For the damgae against stand guard, and sfx, see PlayerEntity mixin
     * damageShield
     */
    @Inject(method = "isBlocking", at = @At(value = "HEAD"), cancellable = true)
    private void isBlockingRoundabout(CallbackInfoReturnable<Boolean> ci) {
        StandUserComponent standUserData = MyComponents.STAND_USER.get(this);
        if (standUserData.isGuarding()){
            ci.setReturnValue(standUserData.getAttackTimeDuring() >= 5);
        }
    }
}
