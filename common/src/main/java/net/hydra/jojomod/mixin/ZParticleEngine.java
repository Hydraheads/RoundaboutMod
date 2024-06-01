package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.Vec3i;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Queue;

@Mixin(ParticleEngine.class)
public class ZParticleEngine {
    @Shadow
    protected ClientLevel level;

    @Shadow @Final
    private Map<ParticleRenderType, Queue<Particle>> particles;

    @Inject(method = "tickParticle", at = @At("HEAD"), cancellable = true)
    void doNotTickParticleWhenTimeStopped(Particle particle, CallbackInfo ci) {
        ZParticleAccess particle1 = (ZParticleAccess) particle;
        if (((TimeStop) level).inTimeStopRange(new Vec3i((int) ((ZParticleAccess) particle).getX(),
                (int) ((ZParticleAccess) particle).getY(),
                (int) ((ZParticleAccess) particle).getZ()))) {
            particle1.setPrevX(particle1.getX());
            particle1.setPrevY(particle1.getY());
            particle1.setPrevZ(particle1.getZ());
            ci.cancel();
        }
    }
}
