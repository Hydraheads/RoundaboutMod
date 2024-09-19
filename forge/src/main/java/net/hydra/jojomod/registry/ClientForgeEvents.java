package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.Terrier.TerrierEntityModel;
import net.hydra.jojomod.entity.Terrier.TerrierEntityRenderer;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.stand.StarPlatinumModel;
import net.hydra.jojomod.entity.stand.StarPlatinumRenderer;
import net.hydra.jojomod.entity.stand.TheWorldModel;
import net.hydra.jojomod.entity.stand.TheWorldRenderer;
import net.hydra.jojomod.particles.AirCrackleParticle;
import net.hydra.jojomod.particles.BloodParticle;
import net.minecraft.client.particle.ExplodeParticle;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Roundabout.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientForgeEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ForgeEntities.TERRIER_DOG.get(), TerrierEntityRenderer::new);
        event.registerEntityRenderer(ForgeEntities.STAR_PLATINUM.get(), StarPlatinumRenderer::new);
        event.registerEntityRenderer(ForgeEntities.THE_WORLD.get(), TheWorldRenderer::new);
        event.registerEntityRenderer(ForgeEntities.THROWN_HARPOON.get(), HarpoonRenderer::new);
        event.registerEntityRenderer(ForgeEntities.THROWN_KNIFE.get(), KnifeRenderer::new);
        event.registerEntityRenderer(ForgeEntities.THROWN_MATCH.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ForgeEntities.GASOLINE_SPLATTER.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ForgeEntities.GASOLINE_CAN.get(), GasolineCanRenderer::new);
        event.registerEntityRenderer(ForgeEntities.THROWN_OBJECT.get(), ThrownObjectRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModEntityRendererClient.WOLF_LAYER, TerrierEntityModel::createBodyLayerTerrier);
        event.registerLayerDefinition(ModEntityRendererClient.THE_WORLD_LAYER, TheWorldModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.STAR_PLATINUM_LAYER, StarPlatinumModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.KNIFE_LAYER, KnifeModel::createBodyLayer);
        event.registerLayerDefinition(ModEntityRendererClient.HARPOON_LAYER, HarpoonModel::createBodyLayer);
        event.registerLayerDefinition(ModEntityRendererClient.GASOLINE_LAYER, GasolineCanModel::createBodyLayer);
    }
    @SubscribeEvent
    public static void registerParticleStuff(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ForgeParticles.HIT_IMPACT.get(), ExplodeParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.BLOOD.get(), BloodParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.BLUE_BLOOD.get(), BloodParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.ENDER_BLOOD.get(), BloodParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.AIR_CRACKLE.get(), AirCrackleParticle.Provider::new);
    }
}
