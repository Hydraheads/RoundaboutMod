package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersMetallica;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

@Mod.EventBusSubscriber(modid = Roundabout.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class MetallicaRenderEvents {

    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Post<LivingEntity, ?> event) {
        LivingEntity entity = event.getEntity();
        Minecraft mc = Minecraft.getInstance();
        if (entity == mc.player && mc.options.getCameraType().isFirstPerson()) return;
        handleMetallicaEffects(entity, event.getPartialTick(), entity.level());

        if (entity instanceof IEntityAndData data) {
            float meter = data.roundabout$getMetalMeter();
            if (meter > 0.1f) {
                if (mc.player == null) return;
                if (!(((StandUser)mc.player).roundabout$getStandPowers() instanceof PowersMetallica)) return;
                if (mc.player.distanceToSqr(entity) > 1024) return;

                PoseStack matrixStack = event.getPoseStack();
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
                font.drawInBatch(barText, x + 1, 1, 0xFF000000, false, matrixStack.last().pose(), event.getMultiBufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
                font.drawInBatch(barText, x, 0, color, false, matrixStack.last().pose(), event.getMultiBufferSource(), Font.DisplayMode.SEE_THROUGH, 0, 15728880);
                RenderSystem.enableDepthTest();
                matrixStack.popPose();
            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.side.isClient()) {
            Minecraft mc = Minecraft.getInstance();
            if (event.player == mc.player) {
                handleMetallicaEffects(event.player, 1.0f, event.player.level());
            }
        }
    }

    private static void handleMetallicaEffects(LivingEntity entity, float partialTick, net.minecraft.world.level.Level level) {
        if (entity instanceof IEntityAndData data) {


            if (((StandUser)entity).roundabout$getActive()) {
                boolean isInvisible = data.roundabout$getTrueInvisibility() > 0;

                if (!isInvisible) {
                    if (entity.tickCount % 17 == 0) {


                        float hpRatio = entity.getHealth() / entity.getMaxHealth();
                        int particleMultiplier = 1;

                        if (hpRatio <= 0.75f) particleMultiplier = 2;
                        if (hpRatio <= 0.50f) particleMultiplier = 3;
                        if (hpRatio <= 0.25f) particleMultiplier = 4;

                        boolean isBleeding = entity.hasEffect(ModEffects.BLEED);
                        double idToPass = isBleeding ? 0d : (double)entity.getId();

                        String[] types = {"metallica_a", "metallica_b", "metallica_c", "metallica_d"};

                        for (int i = 0; i < particleMultiplier; i++) {
                            for (String t : types) {
                                spawnParticleCustom(entity, t, partialTick, idToPass);
                            }
                        }
                    }
                }
            }

            if (((StandUser)entity).roundabout$getActive() && ((IEntityAndData)entity).roundabout$isMagneticField()) {
                int cycleLength = 50;
                int linesAmount = 8;
                double[] layers = {3.0, 6.0, 9.0};

                long time = entity.tickCount;
                double rawProgress = (time % cycleLength) / (double) cycleLength;
                double theta = 0.2 + rawProgress * (Math.PI - 0.4);

                for (double L : layers) {
                    for (int i = 0; i < linesAmount; i++) {
                        double phi = (2.0 * Math.PI / linesAmount) * i;
                        phi += (time * 0.02);

                        double r = L * Math.pow(Math.sin(theta), 2);
                        double yOffset = entity.getBbHeight() / 2.0;

                        double dy = r * Math.cos(theta);
                        double distHorizontal = r * Math.sin(theta);

                        double dx = distHorizontal * Math.cos(phi);
                        double dz = distHorizontal * Math.sin(phi);

                        double x = entity.getX() + dx;
                        double y = entity.getY() + yOffset + dy;
                        double z = entity.getZ() + dz;

                        if (!hasLineOfSight(entity, x, y, z)) continue;

                        String pName = "metallica_field_png";
                        ResourceLocation loc = new ResourceLocation(Roundabout.MOD_ID, pName);
                        if (BuiltInRegistries.PARTICLE_TYPE.containsKey(loc)) {
                            ParticleOptions opts = (ParticleOptions) BuiltInRegistries.PARTICLE_TYPE.get(loc);
                            level.addParticle(opts, x, y, z, 0, 0, 0);
                        }
                    }
                }
            }
        }
    }

    private static void spawnParticleCustom(LivingEntity entity, String type, float pt, double idParam) {
        ResourceLocation loc = new ResourceLocation(Roundabout.MOD_ID, type);
        if (BuiltInRegistries.PARTICLE_TYPE.containsKey(loc)) {
            ParticleOptions pOptions = (ParticleOptions) BuiltInRegistries.PARTICLE_TYPE.get(loc);
            double lerpX = Mth.lerp(pt, entity.xo, entity.getX());
            double lerpY = Mth.lerp(pt, entity.yo, entity.getY());
            double lerpZ = Mth.lerp(pt, entity.zo, entity.getZ());

            double width = entity.getBbWidth();
            double height = entity.getBbHeight();
            double x = lerpX + (entity.getRandom().nextDouble() - 0.5) * width * 0.8;
            double y = lerpY + (entity.getRandom().nextDouble() * height * 0.6) + 0.5;
            double z = lerpZ + (entity.getRandom().nextDouble() - 0.5) * width * 0.8;

            entity.level().addParticle(pOptions, x, y, z, idParam, 0d, 0d);
        }
    }

    private static boolean hasLineOfSight(LivingEntity entity, double x, double y, double z) {
        net.minecraft.world.level.ClipContext context = new net.minecraft.world.level.ClipContext(
                entity.getEyePosition(1.0f),
                new net.minecraft.world.phys.Vec3(x, y, z),
                net.minecraft.world.level.ClipContext.Block.COLLIDER,
                net.minecraft.world.level.ClipContext.Fluid.NONE,
                entity
        );
        return entity.level().clip(context).getType() == net.minecraft.world.phys.HitResult.Type.MISS;
    }
}