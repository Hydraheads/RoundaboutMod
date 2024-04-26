package net.hydra.jojomod.mixin;

import net.hydra.jojomod.networking.MyComponents;
import net.hydra.jojomod.networking.component.StandUserComponent;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    /**If you are stand guarding, this controls you blocking enemy atttacks.
     * For the damage against stand guard, and sfx, see PlayerEntity mixin
     * damageShield
     */

    @Inject(method = "handleStatus", at = @At(value = "HEAD"), cancellable = true)
    public void roundaboutHandleStatus(byte status, CallbackInfo ci) {
        if (status == 29){
            StandUserComponent standUserData = MyComponents.STAND_USER.get(this);
            if (standUserData.isGuarding()) {
                ((Entity) (Object) this).playSound(ModSounds.STAND_GUARD_SOUND_EVENT, 1f, 0.9f + ((Entity) (Object) this).getWorld().random.nextFloat() * 0.3f);
                ci.cancel();
            }
        }
    }
    @Inject(method = "isBlocking", at = @At(value = "HEAD"), cancellable = true)
    private void isBlockingRoundabout(CallbackInfoReturnable<Boolean> ci) {
        StandUserComponent standUserData = MyComponents.STAND_USER.get(this);
        if (standUserData.isGuarding()){
            ci.setReturnValue(standUserData.getAttackTimeDuring() >= 5);
        }
    }
}
