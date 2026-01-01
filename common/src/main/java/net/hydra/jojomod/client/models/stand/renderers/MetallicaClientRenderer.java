package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersMetallica;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;

public class MetallicaClientRenderer {

    private static final ParticleOptions[] METALLICA_VARIANTS = {
            ModParticles.METALLICA_A, ModParticles.METALLICA_B,
            ModParticles.METALLICA_C, ModParticles.METALLICA_D
    };

    public static void renderMetalMeterBar(LivingEntity entity, PoseStack matrixStack, MultiBufferSource buffer) {
        Minecraft mc = Minecraft.getInstance();
        if (entity == mc.player && mc.options.getCameraType().isFirstPerson()) return;

        if (entity instanceof IEntityAndData data) {
            float meter = data.roundabout$getMetalMeter();
            if (meter > 0.1f) {
                if (mc.player == null) return;
                if (!(((StandUser)mc.player).roundabout$getStandPowers() instanceof PowersMetallica)) return;
                if (mc.player.distanceToSqr(entity) > 1024) return;

                matrixStack.pushPose();
                float height = entity.getBbHeight() + 0.5F;
                matrixStack.translate(0.0D, height, 0.0D);
                matrixStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
                matrixStack.scale(-0.025F, -0.025F, 0.025F);

                String barText = "[";
                int bars = (int) (meter / 5);
                for(int i=0; i<20; i++) barText += (i < bars ? "|" : ".");
                barText += "]";

                int color = (meter >= 95) ? 0xFFFF0000 : 0xFF55FFFF;
                Font font = mc.font;
                float x = -font.width(barText) / 2.0f;

                RenderSystem.disableDepthTest();
                font.drawInBatch(barText, x + 1, 1, 0xFF000000, false, matrixStack.last().pose(), buffer, Font.DisplayMode.NORMAL, 0, 15728880);
                font.drawInBatch(barText, x, 0, color, false, matrixStack.last().pose(), buffer, Font.DisplayMode.SEE_THROUGH, 0, 15728880);
                RenderSystem.enableDepthTest();
                matrixStack.popPose();
            }
        }
    }

    public static void tickMetallicaEffects(LivingEntity entity) {
        if (entity instanceof IEntityAndData data && PowerTypes.hasStandActive(entity)) {
            handleMetallicaEffects(entity, entity.level());
        }
    }

    private static void handleMetallicaEffects(LivingEntity entity, Level level) {
        float partialTick = Minecraft.getInstance().getFrameTime();
        double interpX = Mth.lerp(partialTick, entity.xo, entity.getX());
        double interpY = Mth.lerp(partialTick, entity.yo, entity.getY());
        double interpZ = Mth.lerp(partialTick, entity.zo, entity.getZ());

        if (entity.tickCount % 17 == 0 && MainUtil.isUsingMetallica(entity)) {
            String[] types = {"metallica_a", "metallica_b", "metallica_c", "metallica_d"};
            for (String t : types) {
                spawnParticleWithID(entity, t, interpX, interpY, interpZ);
            }
        }

        if (((IEntityAndData)entity).roundabout$isMagneticField()) {
            spawnMagneticFieldWave(entity, level, interpX, interpY, interpZ);
        }
    }

    private static void spawnMagneticFieldWave(LivingEntity entity, Level level, double xBase, double yBase, double zBase) {
        int cycleLength = 50;
        int linesAmount = 8;
        double[] layers = {3.0, 6.0, 9.0};
        long time = entity.tickCount;
        double rawProgress = (time % cycleLength) / (double) cycleLength;
        double theta = 0.2 + rawProgress * (Math.PI - 0.4);

        for (double L : layers) {
            for (int i = 0; i < linesAmount; i++) {
                double phi = (2.0 * Math.PI / linesAmount) * i + (time * 0.02);

                double r = L * Math.pow(Math.sin(theta), 2);
                double dx = r * Math.sin(theta) * Math.cos(phi);
                double dy = r * Math.cos(theta);
                double dz = r * Math.sin(theta) * Math.sin(phi);

                double x = xBase + dx;
                double y = yBase + (entity.getBbHeight() / 2.0) + dy;
                double z = zBase + dz;

                if (hasLineOfSight(entity, x, y, z)) {
                    ResourceLocation loc = new ResourceLocation(Roundabout.MOD_ID, "metallica_field_png");
                    if (BuiltInRegistries.PARTICLE_TYPE.containsKey(loc)) {
                        level.addParticle((ParticleOptions) BuiltInRegistries.PARTICLE_TYPE.get(loc), x, y, z, 0, 0, 0);
                    }
                }
            }
        }
    }

    private static void spawnParticleWithID(LivingEntity entity, String type, double x, double y, double z) {
        ResourceLocation loc = new ResourceLocation(Roundabout.MOD_ID, type);
        if (BuiltInRegistries.PARTICLE_TYPE.containsKey(loc)) {
            ParticleOptions pOptions = (ParticleOptions) BuiltInRegistries.PARTICLE_TYPE.get(loc);

            entity.level().addParticle(pOptions, x, y + 1.5, z, (double)entity.getId(), 0d, 0d);
        }
    }

    private static boolean hasLineOfSight(LivingEntity entity, double x, double y, double z) {
        return entity.level().clip(new net.minecraft.world.level.ClipContext(
                entity.getEyePosition(1.0f), new net.minecraft.world.phys.Vec3(x, y, z),
                net.minecraft.world.level.ClipContext.Block.COLLIDER, net.minecraft.world.level.ClipContext.Fluid.NONE, entity
        )).getType() == net.minecraft.world.phys.HitResult.Type.MISS;
    }
}