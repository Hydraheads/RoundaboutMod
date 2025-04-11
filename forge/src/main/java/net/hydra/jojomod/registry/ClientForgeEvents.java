package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.StandFireRenderer;
import net.hydra.jojomod.entity.D4CCloneRenderer;
import net.hydra.jojomod.entity.FogCloneRenderer;
import net.hydra.jojomod.entity.Terrier.TerrierEntityModel;
import net.hydra.jojomod.entity.Terrier.TerrierEntityRenderer;
import net.hydra.jojomod.entity.client.MagiciansRedSpinEffectLayer;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.entity.corpses.*;
import net.hydra.jojomod.entity.pathfinding.NoRenderer;
import net.hydra.jojomod.entity.projectile.*;
import net.hydra.jojomod.entity.stand.*;
import net.hydra.jojomod.entity.substand.LifeTrackerModel;
import net.hydra.jojomod.entity.substand.LifeTrackerRenderer;
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
        event.registerEntityRenderer(ForgeEntities.MAGICIANS_RED.get(), MagiciansRedRenderer::new);
        event.registerEntityRenderer(ForgeEntities.MAGICIANS_RED_OVA.get(), MagiciansRedOVARenderer::new);
        event.registerEntityRenderer(ForgeEntities.D4C.get(), D4CRenderer::new);
        event.registerEntityRenderer(ForgeEntities.JUSTICE_PIRATE.get(), JusticePirateRenderer::new);
        event.registerEntityRenderer(ForgeEntities.DARK_MIRAGE.get(), DarkMirageRenderer::new);
        event.registerEntityRenderer(ForgeEntities.THROWN_HARPOON.get(), HarpoonRenderer::new);
        event.registerEntityRenderer(ForgeEntities.THROWN_KNIFE.get(), KnifeRenderer::new);
        event.registerEntityRenderer(ForgeEntities.THROWN_MATCH.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ForgeEntities.GASOLINE_SPLATTER.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ForgeEntities.STAND_ARROW.get(), StandArrowRenderer::new);
        event.registerEntityRenderer(ForgeEntities.CROSSFIRE_HURRICANE.get(), CrossfireHurricaneRenderer::new);
        event.registerEntityRenderer(ForgeEntities.LIFE_TRACKER.get(), LifeTrackerRenderer::new);
        event.registerEntityRenderer(ForgeEntities.STAND_FIREBALL.get(), StandFireballRenderer::new);
        event.registerEntityRenderer(ForgeEntities.GASOLINE_CAN.get(), GasolineCanRenderer::new);
        event.registerEntityRenderer(ForgeEntities.THROWN_OBJECT.get(), ThrownObjectRenderer::new);
        event.registerEntityRenderer(ForgeEntities.CONCEALED_FLAME_OBJECT.get(), ConcealedFlameObjectRenderer::new);
        event.registerEntityRenderer(ForgeEntities.GROUND_HURRICANE.get(), NoRenderer::new);
        event.registerEntityRenderer(ForgeEntities.OVA_ENYA.get(), OVAEnyaRenderer::new);
        event.registerEntityRenderer(ForgeEntities.JOTARO.get(), JotaroRenderer::new);
        event.registerEntityRenderer(ForgeEntities.STEVE_NPC.get(), PlayerNPCRenderer::new);
        event.registerEntityRenderer(ForgeEntities.ALEX_NPC.get(), PlayerAlexRenderer::new);
        event.registerEntityRenderer(ForgeEntities.FOG_CLONE.get(), FogCloneRenderer::new);
        event.registerEntityRenderer(ForgeEntities.D4C_CLONE.get(), D4CCloneRenderer::new);
        event.registerEntityRenderer(ForgeEntities.FALLEN_ZOMBIE.get(), FallenZombieRenderer::new);
        event.registerEntityRenderer(ForgeEntities.FALLEN_SKELETON.get(), FallenSkeletonRenderer::new);
        event.registerEntityRenderer(ForgeEntities.FALLEN_SPIDER.get(), FallenSpiderRenderer::new);
        event.registerEntityRenderer(ForgeEntities.FALLEN_VILLAGER.get(), FallenVillagerRenderer::new);
        event.registerEntityRenderer(ForgeEntities.FALLEN_CREEPER.get(), FallenCreeperRenderer::new);

        event.registerBlockEntityRenderer(ForgeBlocks.STAND_FIRE_BLOCK_ENTITY.get(), StandFireRenderer::new);
        //TSCoreShader.bootstrapShaders();
        //TSPostShader.bootstrapShaders();
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModEntityRendererClient.WOLF_LAYER, TerrierEntityModel::createBodyLayerTerrier);
        event.registerLayerDefinition(ModEntityRendererClient.THE_WORLD_LAYER, TheWorldModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.STAR_PLATINUM_LAYER, StarPlatinumModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.STAR_PLATINUM_BASEBALL_LAYER, StarPlatinumBaseballModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.JUSTICE_LAYER, JusticeModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.MAGICIANS_RED_LAYER, MagiciansRedModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.MAGICIANS_RED_OVA_LAYER, MagiciansRedOVAModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.JUSTICE_PIRATE_LAYER, JusticePirateModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.D4C_LAYER, D4CModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.DARK_MIRAGE_LAYER, DarkMirageModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.KNIFE_LAYER, KnifeModel::createBodyLayer);
        event.registerLayerDefinition(ModEntityRendererClient.HARPOON_LAYER, HarpoonModel::createBodyLayer);
        event.registerLayerDefinition(ModEntityRendererClient.CROSSFIRE_LAYER, CrossfireHurricaneModel::createBodyLayer);
        event.registerLayerDefinition(ModEntityRendererClient.CROSSFIRE_FIRESTORM_LAYER, CrossfireFirestormModel::createBodyLayer);
        event.registerLayerDefinition(ModEntityRendererClient.LIFE_DETECTOR, LifeTrackerModel::createBodyLayer);
        event.registerLayerDefinition(ModEntityRendererClient.STAND_FIREBALL_LAYER, StandFireballModel::createBodyLayer);
        event.registerLayerDefinition(ModEntityRendererClient.GASOLINE_LAYER, GasolineCanModel::createBodyLayer);
        event.registerLayerDefinition(ModEntityRendererClient.OVA_ENYA_LAYER, OVAEnyaModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.JOTARO_LAYER, JotaroModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.STEVE_LAYER, PlayerNPCModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.ALEX_LAYER, PlayerAlexModel::getTexturedModelData);
        event.registerLayerDefinition(ModEntityRendererClient.STAND_FIRE_LAYER, StandFireRenderer::createBodyLayer);
        event.registerLayerDefinition(ModEntityRendererClient.MR_SPIN_LAYER, MagiciansRedSpinEffectLayer::createLayer);

        //BlockEntityRenderers.register(ModBlocks.STAND_FIRE_BLOCK_ENTITY, StandFireRenderer::new);
    }
    @SubscribeEvent
    public static void registerParticleStuff(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ForgeParticles.HIT_IMPACT.get(), ExplodeParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.BLOOD.get(), BloodParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.BLUE_BLOOD.get(), BloodParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.ENDER_BLOOD.get(), BloodParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.POINTER.get(), PointerParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.AIR_CRACKLE.get(), AirCrackleParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.MENACING.get(), MenacingParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.VACUUM.get(), VacuumParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.ORANGE_FLAME.get(), StandFlameParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.BLUE_FLAME.get(), StandFlameParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.PURPLE_FLAME.get(), StandFlameParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.GREEN_FLAME.get(), StandFlameParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.DREAD_FLAME.get(), StandFlameParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.CREAM_FLAME.get(), StandFlameParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.FOG_CHAIN.get(), FogChainParticle.Provider::new);
        event.registerSpriteSet(ForgeParticles.WARDEN_CLOCK.get(), WardenClockParticle.Provider::new);
    }
}
