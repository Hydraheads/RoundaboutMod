package net.hydra.jojomod.mixin.time_stop;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.hydra.jojomod.access.IParticleAccess;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.mixin.access.AccessParticle;
import net.hydra.jojomod.util.config.ConfigManager;
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
public class TimeStopParticleEngine {

    /**Runs a slightly modified version of the particle engine code while time stop is activated, so
     * particles can render frozen in time.*/


    /**When a particle is created, mark it as created in a TS or not, particles made in a TS tick*/
    @ModifyVariable(
            method = "add(Lnet/minecraft/client/particle/Particle;)V", at = @At(value = "HEAD"))
    private Particle sbaMarkParticleTS(Particle $$0) {
        if ($$0 != null && level != null) {
            if (((TimeStop) level).inTimeStopRange(new Vec3i((int) ((AccessParticle) $$0).sba$getX(),
                    (int) ((AccessParticle) $$0).sba$getY(),
                    (int) ((AccessParticle) $$0).sba$getZ()))) {
                ((IParticleAccess) $$0).sba$setSBAIsTimeStopCreated(true);
            }
        }
        return $$0;
    }

    @Inject(method = "tickParticle", at = @At("HEAD"), cancellable = true)
    void doNotTickParticleWhenTimeStopped(Particle particle, CallbackInfo ci) {
        AccessParticle particle1 = (AccessParticle) particle;
        if (!(particle instanceof ItemPickupParticle) && particle1 != null) {
            if (!((IParticleAccess) particle1).sba$getSBAIsTimeStopCreated() && ((TimeStop) level).inTimeStopRange(new Vec3i((int) particle1.sba$getX(),
                    (int) particle1.sba$getY(),
                    (int) particle1.sba$getZ()))) {
                particle1.sba$setPrevX(particle1.sba$getX());
                particle1.sba$setPrevY(particle1.sba$getY());
                particle1.sba$setPrevZ(particle1.sba$getZ());
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
        if (!ClientUtil.canSilverChariotControlModeSeeParticles()) {
            ci.cancel();
            return;
        }

        if (ClientUtil.isUsingEpitaph() && !ConfigManager.getClientConfig().generalSettings.epitaphSeePresentEntitiesAndParticles){
            ci.cancel();
            return;
        }

        if (!((TimeStop) level).getTimeStoppingEntities().isEmpty()) {
            $$2.turnOnLightLayer();
            RenderSystem.enableDepthTest();
            PoseStack $$5 = RenderSystem.getModelViewStack();
            ClientUtil.pushPoseAndCooperate($$5,17);
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

                            AccessParticle particle1 = ((AccessParticle) $$10);
                            float tickDeltaFixed = $$4;
                            if (particle1 != null && !((IParticleAccess) particle1).sba$getSBAIsTimeStopCreated() && !($$10 instanceof ItemPickupParticle)) {
                                Vec3i range = new Vec3i((int) particle1.sba$getX(),
                                        (int) particle1.sba$getY(),
                                        (int) particle1.sba$getZ());
                                if (((TimeStop) level).inTimeStopRange(range)) {
                                    tickDeltaFixed = ((IParticleAccess) particle1).sba$getPreTSTick();
                                } else {
                                    ((IParticleAccess) particle1).sba$setPreTSTick();
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

            ClientUtil.popPoseAndCooperate($$5,17);
            RenderSystem.applyModelViewMatrix();
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
            //These are true when ts is happening and in the original particle engine
            $$2.turnOffLightLayer();
            ci.cancel();
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */


    @Shadow
    protected ClientLevel level;

    @Shadow @Final
    private Map<ParticleRenderType, Queue<Particle>> particles;
}
