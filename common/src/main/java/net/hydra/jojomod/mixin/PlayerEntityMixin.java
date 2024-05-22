package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerEntityMixin {

    /**if your stand guard is broken, disable shields. Also, does not run takeshieldhit code if stand guarding.*/
    @Inject(method = "blockUsingShield", at = @At(value = "HEAD"), cancellable = true)
    protected void roundaboutTakeShieldHit(LivingEntity $$0, CallbackInfo ci) {
        if (((StandUser) this).isGuarding()) {
            if (((StandUser) this).getGuardBroken()){

                ItemStack itemStack = ((LivingEntity) (Object) this).getUseItem();
                Item item = itemStack.getItem();
                if (item.getUseAnimation(itemStack) == UseAnim.BLOCK) {
                    ((LivingEntity) (Object) this).releaseUsingItem();
                    ((Player) (Object) this).stopUsingItem();
                }
                ((Player) (Object) this).getCooldowns().addCooldown(Items.SHIELD, 100);
                ((Player) (Object) this).level().broadcastEntityEvent(((Player) (Object) this), EntityEvent.SHIELD_DISABLED);
            }
            ci.cancel();
        } else if (((StandUser) $$0).getMainhandOverride()){
            ci.cancel();
        }
    }

    /**your shield does not take damage if the stand blocks it*/
    @Inject(method = "hurtCurrentlyUsedShield", at = @At(value = "HEAD"), cancellable = true)
    protected void roundaboutDamageShield(float $$0, CallbackInfo ci) {
        if (((StandUser) this).isGuarding()) {
            ci.cancel();
        }
    }
    /**your shield does not take damage if the stand blocks it*/
    @Inject(method = "jumpFromGround", at = @At(value = "HEAD"), cancellable = true)
    protected void roundaboutJump(CallbackInfo ci) {
        if (((StandUser) this).isClashing()) {
            ci.cancel();
        }
    }

    /**If you are in a barrage, does not play the hurt sound*/
    @Inject(method = "getHurtSound", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutGetHurtSound(DamageSource $$0, CallbackInfoReturnable<SoundEvent> ci) {
        if (((StandUser) this).isDazed() && $$0.is(ModDamageTypes.STAND)) {
            ci.setReturnValue(null);
        }
    }
}
