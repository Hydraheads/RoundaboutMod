package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.access.IParticleAccess;
import net.hydra.jojomod.event.powers.StandUserClient;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;
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
                            if (!((IParticleAccess) particle1).getRoundaboutIsTimeStopCreated()) {
                                Vec3i range = new Vec3i((int) particle1.getX(),
                                        (int) particle1.getY(),
                                        (int) particle1.getZ());
                                if (((TimeStop) level).inTimeStopRange(range)) {
                                    tickDeltaFixed = ((IParticleAccess) particle1).getPreTSTick();
                                } else {
                                    ((IParticleAccess) particle1).setPreTSTick();
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
