package net.hydra.jojomod.mixin;

import net.hydra.jojomod.networking.MyComponents;
import net.hydra.jojomod.networking.component.StandUserComponent;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "damageShield", at = @At(value = "HEAD"), cancellable = true)
    protected void roundaboutDamageShield(float amount, CallbackInfo ci) {
        StandUserComponent standUserData = MyComponents.STAND_USER.get(this);
        if (standUserData.isGuarding()) {
            ci.cancel();
        }
    }
}
