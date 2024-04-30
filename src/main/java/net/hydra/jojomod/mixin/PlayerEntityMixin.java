package net.hydra.jojomod.mixin;

import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.networking.MyComponents;
import net.hydra.jojomod.networking.component.StandUserComponent;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.shadowed.eliotlash.mclib.math.functions.rounding.Round;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "takeShieldHit", at = @At(value = "HEAD"), cancellable = true)
    protected void roundaboutTakeShieldHit(LivingEntity attacker, CallbackInfo ci) {
        StandUserComponent standUserData = MyComponents.STAND_USER.get(this);
        if (standUserData.isGuarding()) {
            if (standUserData.getGuardBroken()){
                ((PlayerEntity) (Object) this).getItemCooldownManager().set(Items.SHIELD, 100);
            }
            ci.cancel();
        }
    }

    @Inject(method = "damageShield", at = @At(value = "HEAD"), cancellable = true)
    protected void roundaboutDamageShield(float amount, CallbackInfo ci) {
        StandUserComponent standUserData = MyComponents.STAND_USER.get(this);
        if (standUserData.isGuarding()) {
            standUserData.damageGuard(amount);
            ci.cancel();
        }
    }
}
