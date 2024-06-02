package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.access.IParticleAccess;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Map;
import java.util.Queue;

@Mixin(ParticleEngine.class)
public class ZParticleEngine {
    @Shadow
    protected ClientLevel level;

    @Shadow @Final
    private Map<ParticleRenderType, Queue<Particle>> particles;

    private float tickDeltaCache = 0;

    /**When a particle is created, mark it as created in a TS or not, particles made in a TS tick*/
    @ModifyVariable(
            method = "add(Lnet/minecraft/client/particle/Particle;)V", at = @At(value = "HEAD"))
    private Particle roundaboutMarkParticleTS(Particle $$0) {
        if (((TimeStop) level).inTimeStopRange(new Vec3i((int) ((ZParticleAccess) $$0).getX(),
                (int) ((ZParticleAccess) $$0).getY(),
                (int) ((ZParticleAccess) $$0).getZ()))){
            ((IParticleAccess) $$0).setRoundaboutIsTimeStopCreated(true);
        }
        return $$0;
    }

    @Inject(method = "tickParticle", at = @At("HEAD"), cancellable = true)
    void doNotTickParticleWhenTimeStopped(Particle particle, CallbackInfo ci) {
        ZParticleAccess particle1 = (ZParticleAccess) particle;
        if (!((IParticleAccess) particle1).getRoundaboutIsTimeStopCreated() && ((TimeStop) level).inTimeStopRange(new Vec3i((int) particle1.getX(),
                (int) particle1.getY(),
                (int) particle1.getZ()))) {
            particle1.setPrevX(particle1.getX());
            particle1.setPrevY(particle1.getY());
            particle1.setPrevZ(particle1.getZ());
            ci.cancel();
        }
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/Particle;render(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/Camera;F)V"))
    private void doNotDeltaTickParticlesWhenTimeIsStopped(Particle $$10, VertexConsumer $$9, Camera $$3, float $$4) {
        ZParticleAccess particle1 = ((ZParticleAccess) $$10);
        float tickDeltaFixed = $$4;
        if (!((IParticleAccess) particle1).getRoundaboutIsTimeStopCreated() && ((TimeStop) level).inTimeStopRange(new Vec3i((int) particle1.getX(),
                (int) particle1.getY(),
                (int) particle1.getZ()))) {
            tickDeltaFixed = 0;
        }
        $$10.render($$9,$$3,tickDeltaFixed);
    }
}
