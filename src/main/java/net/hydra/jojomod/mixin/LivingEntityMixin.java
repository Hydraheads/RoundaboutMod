package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IStandUser;
import net.hydra.jojomod.entity.StandEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.PlayerManager;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "travelControlled", at = @At(value = "TAIL"))
    private void injectTC(PlayerEntity controllingPlayer, Vec3d movementInput, CallbackInfo info){
        if (((IStandUser) controllingPlayer).hasStandOut()){
            ((IStandUser) controllingPlayer).updateStandOutPosition(((IStandUser) controllingPlayer).getStandOut());
            ((IStandUser) controllingPlayer).getStandOut().setVelocity(controllingPlayer.getVelocity());
        }
    }
}
