package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.BubbleScaffoldBlockEntityRenderer;
import net.hydra.jojomod.block.D4CLightBlockEntityRenderer;
import net.hydra.jojomod.block.MirrorBlockEntityRenderer;
import net.hydra.jojomod.block.StandFireRenderer;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.client.gui.NoCancelInputScreen;
import net.hydra.jojomod.client.models.corpses.renderers.*;
import net.hydra.jojomod.client.models.layers.MagiciansRedSpinEffectLayer;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.mobs.TerrierEntityModel;
import net.hydra.jojomod.client.models.mobs.renderers.TerrierEntityRenderer;
import net.hydra.jojomod.client.models.npcs.ZombieAestheticianModel;
import net.hydra.jojomod.client.models.npcs.renderers.AestheticianRenderer;
import net.hydra.jojomod.client.models.npcs.renderers.ZombieAestheticianRenderer;
import net.hydra.jojomod.client.models.projectile.*;
import net.hydra.jojomod.client.models.projectile.renderers.*;
import net.hydra.jojomod.client.models.stand.*;
import net.hydra.jojomod.client.models.stand.renderers.*;
import net.hydra.jojomod.client.models.substand.LifeTrackerModel;
import net.hydra.jojomod.client.models.substand.renderers.D4CCloneRenderer;
import net.hydra.jojomod.client.models.substand.renderers.EncasementBubbleRenderer;
import net.hydra.jojomod.client.models.substand.renderers.FogCloneRenderer;
import net.hydra.jojomod.client.models.substand.renderers.LifeTrackerRenderer;
import net.hydra.jojomod.client.models.visages.*;
import net.hydra.jojomod.client.models.visages.renderers.*;
import net.hydra.jojomod.client.models.worn_stand.SoftAndWetShootingArmModel;
import net.hydra.jojomod.particles.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ExplodeParticle;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Roundabout.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientForgeForgeEvents {

    @SubscribeEvent
    public static void clientTickEvent(TickEvent.ClientTickEvent event) {
        /***
        Roundabout.LOGGER.info("1");
        if (event.phase == TickEvent.Phase.END) {
            Roundabout.LOGGER.info("2");
            Minecraft mc = Minecraft.getInstance();
                // Re-enable movement input
            Roundabout.LOGGER.info("3");
                if (mc.screen instanceof NoCancelInputScreen && mc.player != null && mc.player.input instanceof KeyboardInput keyboardInput) {
                    keyboardInput.tick(false,1); // false = no riding jump
                    Roundabout.LOGGER.info("4");
                }
        }
         **/
    }
}
