package net.hydra.jojomod.mixin.whitesnake.control;

import net.hydra.jojomod.entity.stand.WhitesnakeEntity;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerEntity.class)
public abstract class WhitesnakeControlMovementMixin {
    @Shadow @Final private Entity entity;
    @Shadow private Vec3 ap;

    @Inject(method = "sendChanges", at = @At("HEAD"))
    private void roundaboutWhitesnake$skipMovementEcho(CallbackInfo ci) {
        if (entity instanceof WhitesnakeEntity whitesnake && whitesnake.isControlModeActive()
                && !entity.hasImpulse && !entity.hurtMarked) {
            ap = entity.getDeltaMovement();
        }
    }
}
