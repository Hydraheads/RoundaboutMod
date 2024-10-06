package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.hydra.jojomod.access.IParticleAccess;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ItemPickupParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.Vec3i;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.Queue;

@Mixin(ParticleEngine.class)
public class ZParticleEngine {
    @Shadow
    protected ClientLevel level;

    @Shadow @Final
    private Map<ParticleRenderType, Queue<Particle>> particles;


    /**When a particle is created, mark it as created in a TS or not, particles made in a TS tick*/
    @ModifyVariable(
            method = "add(Lnet/minecraft/client/particle/Particle;)V", at = @At(value = "HEAD"))
    private Particle roundaboutMarkParticleTS(Particle $$0) {
        if (((TimeStop) level).inTimeStopRange(new Vec3i((int) ((ZParticleAccess) $$0).roundabout$getX(),
                (int) ((ZParticleAccess) $$0).roundabout$getY(),
                (int) ((ZParticleAccess) $$0).roundabout$getZ()))){
            ((IParticleAccess) $$0).roundabout$setRoundaboutIsTimeStopCreated(true);
        }
        return $$0;
    }

    @Inject(method = "tickParticle", at = @At("HEAD"), cancellable = true)
    void doNotTickParticleWhenTimeStopped(Particle particle, CallbackInfo ci) {
        ZParticleAccess particle1 = (ZParticleAccess) particle;
        if (!(particle instanceof ItemPickupParticle)) {
            if (!((IParticleAccess) particle1).roundabout$getRoundaboutIsTimeStopCreated() && ((TimeStop) level).inTimeStopRange(new Vec3i((int) particle1.roundabout$getX(),
                    (int) particle1.roundabout$getY(),
                    (int) particle1.roundabout$getZ()))) {
                particle1.roundabout$setPrevX(particle1.roundabout$getX());
                particle1.roundabout$setPrevY(particle1.roundabout$getY());
                particle1.roundabout$setPrevZ(particle1.roundabout$getZ());
                ci.cancel();
            }
        }
    }

    @Shadow
    @Final
    private static List<ParticleRenderType> RENDER_ORDER;
    @Shadow
    @Final
    private TextureManager textureManager;
    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    private void doNotDeltaTickParticlesWhenTimeIsStopped(PoseStack $$0, MultiBufferSource.BufferSource $$1, LightTexture $$2, Camera $$3, float $$4, CallbackInfo ci) {

        if (!((TimeStop) level).getTimeStoppingEntities().isEmpty()) {
            $$2.turnOnLightLayer();
            RenderSystem.enableDepthTest();
            PoseStack $$5 = RenderSystem.getModelViewStack();
            $$5.pushPose();
            $$5.mulPoseMatrix($$0.last().pose());
            RenderSystem.applyModelViewMatrix();

            for (ParticleRenderType $$6 : RENDER_ORDER) {
                Iterable<Particle> $$7 = this.particles.get($$6);
                if ($$7 != null) {
                    RenderSystem.setShader(GameRenderer::getParticleShader);
                    Tesselator $$8 = Tesselator.getInstance();
                    BufferBuilder $$9 = $$8.getBuilder();
                    $$6.begin($$9, this.textureManager);

                    for (Particle $$10 : $$7) {
                        try {

                            ZParticleAccess particle1 = ((ZParticleAccess) $$10);
                            float tickDeltaFixed = $$4;
                            if (!((IParticleAccess) particle1).roundabout$getRoundaboutIsTimeStopCreated() && !($$10 instanceof ItemPickupParticle)) {
                                Vec3i range = new Vec3i((int) particle1.roundabout$getX(),
                                        (int) particle1.roundabout$getY(),
                                        (int) particle1.roundabout$getZ());
                                if (((TimeStop) level).inTimeStopRange(range)) {
                                    tickDeltaFixed = ((IParticleAccess) particle1).roundabout$getPreTSTick();
                                } else {
                                    ((IParticleAccess) particle1).roundabout$setPreTSTick();
                                }
                            }
                            $$10.render($$9, $$3, tickDeltaFixed);
                        } catch (Throwable var17) {
                            CrashReport $$12 = CrashReport.forThrowable(var17, "Rendering Particle");
                            CrashReportCategory $$13 = $$12.addCategory("Particle being rendered");
                            $$13.setDetail("Particle", $$10::toString);
                            $$13.setDetail("Particle Type", $$6::toString);
                            throw new ReportedException($$12);
                        }
                    }

                    $$6.end($$8);
                }
            }

            $$5.popPose();
            RenderSystem.applyModelViewMatrix();
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
            $$2.turnOffLightLayer();
            ci.cancel();
        }
    }
}
