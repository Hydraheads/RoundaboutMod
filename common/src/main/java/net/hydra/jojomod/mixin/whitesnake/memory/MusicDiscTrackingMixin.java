package net.hydra.jojomod.mixin.whitesnake.memory;

import net.hydra.jojomod.event.powers.whitesnake.disc.MusicDiscController;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerEntity.class)
public abstract class MusicDiscTrackingMixin {
    @Shadow @Final private Entity entity;

    @Inject(method = "addPairing", at = @At("TAIL"))
    private void startTracking(ServerPlayer player, CallbackInfo ci) {
        if (entity instanceof LivingEntity) {
            MusicDiscController.startTracking((LivingEntity) entity, player);
        }
    }
}
