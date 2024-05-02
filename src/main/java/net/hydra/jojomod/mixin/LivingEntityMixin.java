package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.networking.MyComponents;
import net.hydra.jojomod.networking.component.StandUserComponent;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
            if (standUserData.isGuardingEffectively2()) {
                if (!standUserData.getGuardBroken()) {
                    ((Entity) (Object) this).playSound(ModSounds.STAND_GUARD_SOUND_EVENT, 0.8f, 0.9f + ((Entity) (Object) this).getWorld().random.nextFloat() * 0.3f);
                } else {
                    ((Entity) (Object) this).playSound(SoundEvents.ITEM_SHIELD_BREAK, 1f, 1.5f);
                }
                ci.cancel();
            }
        }
    }
    @Inject(method = "isBlocking", at = @At(value = "HEAD"), cancellable = true)
    private void isBlockingRoundabout(CallbackInfoReturnable<Boolean> ci) {
        StandUserComponent standUserData = MyComponents.STAND_USER.get(this);
        if (standUserData.isGuarding()){
            ci.setReturnValue(standUserData.isGuardingEffectively());
        }
    }

    @Inject(method = "applyArmorToDamage", at = @At(value = "HEAD"), cancellable = true)
    private void RoundaboutApplyArmorToDamage(DamageSource source, float amount,CallbackInfoReturnable<Float> ci){
        if (source.isOf(ModDamageTypes.STAND)) {
            ci.setReturnValue(amount);
        }
    }

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damageShield(F)V", shift = At.Shift.BEFORE))
    private void RoundaboutDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> ci) {
        StandUserComponent standUserData = MyComponents.STAND_USER.get(this);
        if (standUserData.isGuarding()) {
            if (!source.isIn(DamageTypeTags.BYPASSES_COOLDOWN) && standUserData.getGuardCooldown() > 0) {
                return;
            }
            standUserData.damageGuard(amount);
        }
    }
}
