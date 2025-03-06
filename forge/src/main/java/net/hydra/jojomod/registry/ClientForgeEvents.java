package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.FogCloneRenderer;
import net.hydra.jojomod.entity.Terrier.TerrierEntityModel;
import net.hydra.jojomod.entity.Terrier.TerrierEntityRenderer;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.entity.corpses.*;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.stand.*;
import net.hydra.jojomod.entity.visages.mobs.*;
import net.hydra.jojomod.particles.*;
import net.minecraft.client.particle.ExplodeParticle;
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
        event.registerEntityRenderer(ForgeEntities.STAR_PLATINUM_BASEBALL.get(), StarPlatinumBaseballRenderer::new);
        event.registerEntityRenderer(ForgeEntities.THE_WORLD.get(), TheWorldRenderer::new);
        event.registerEntityRenderer(ForgeEntities.JUSTICE.get(), JusticeRenderer::new);
        event.registerEntityRenderer(ForgeEntities.JUSTICE_PIRATE.get(), JusticePirateRenderer::new);
        event.registerEntityRenderer(ForgeEntities.DARK_MIRAGE.get(), DarkMirageRenderer::new);
        event.registerEntityRenderer(ForgeEntities.THROWN_HARPOON.get(), HarpoonRenderer::new);
        event.registerEntityRenderer(ForgeEntities.THROWN_KNIFE.get(), KnifeRenderer::new);
        event.registerEntityRenderer(ForgeEntities.THROWN_MATCH.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ForgeEntities.GASOLINE_SPLATTER.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ForgeEntities.STAND_ARROW.get(), StandArrowRenderer::new);
        event.registerEntityRenderer(ForgeEntities.GASOLINE_CAN.get(), GasolineCanRenderer::new);
        event.registerEntityRenderer(ForgeEntities.THROWN_OBJECT.get(), ThrownObjectRenderer::new);
        event.registerEntityRenderer(ForgeEntities.OVA_ENYA.get(), OVAEnyaRenderer::new);
        event.registerEntityRenderer(ForgeEntities.JOTARO.get(), JotaroRenderer::new);
        event.registerEntityRenderer(ForgeEntities.STEVE_NPC.get(), PlayerNPCRenderer::new);
        event.registerEntityRenderer(ForgeEntities.ALEX_NPC.get(), PlayerAlexRenderer::new);
        event.registerEntityRenderer(ForgeEntities.FOG_CLONE.get(), FogCloneRenderer::new);
        event.registerEntityRenderer(ForgeEntities.FALLEN_ZOMBIE.get(), FallenZombieRenderer::new);
        event.registerEntityRenderer(ForgeEntities.FALLEN_SKELETON.get(), FallenSkeletonRenderer::new);
        event.registerEntityRenderer(ForgeEntities.FALLEN_SPIDER.get(), FallenSpiderRenderer::new);
        event.registerEntityRenderer(ForgeEntities.FALLEN_VILLAGER.get(), FallenVillagerRenderer::new);
        event.registerEntityRenderer(ForgeEntities.FALLEN_CREEPER.get(), FallenCreeperRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModEntityRendererClient.WOLF_LAYER, TerrierEntityModel::createBodyLayerTerrier);
        event.registerLayerDefinition(ModEntityRendererClient.THE_WORLD_LAYER, TheWorldModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.STAR_PLATINUM_LAYER, StarPlatinumModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.STAR_PLATINUM_BASEBALL_LAYER, StarPlatinumBaseballModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.JUSTICE_LAYER, JusticeModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.JUSTICE_PIRATE_LAYER, JusticePirateModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.DARK_MIRAGE_LAYER, DarkMirageModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.KNIFE_LAYER, KnifeModel::createBodyLayer);
        event.registerLayerDefinition(ModEntityRendererClient.HARPOON_LAYER, HarpoonModel::createBodyLayer);
        event.registerLayerDefinition(ModEntityRendererClient.GASOLINE_LAYER, GasolineCanModel::createBodyLayer);
        event.registerLayerDefinition(ModEntityRendererClient.OVA_ENYA_LAYER, OVAEnyaModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.JOTARO_LAYER, JotaroModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.STEVE_LAYER, PlayerNPCModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.ALEX_LAYER, PlayerAlexModel::getTexturedModelData);
    }
    @SubscribeEvent
    public static void registerParticleStuff(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ForgeParticles.HIT_IMPACT.get(), ExplodeParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.BLOOD.get(), BloodParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.BLUE_BLOOD.get(), BloodParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.ENDER_BLOOD.get(), BloodParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.AIR_CRACKLE.get(), AirCrackleParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.MENACING.get(), MenacingParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.VACUUM.get(), VacuumParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.FOG_CHAIN.get(), FogChainParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.WARDEN_CLOCK.get(), WardenClockParticle.Provider::new);
    }
}
