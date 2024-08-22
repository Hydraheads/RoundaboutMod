package net.hydra.jojomod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.networking.FabricPacketManager;
import net.hydra.jojomod.particles.FabricParticlesClient;
import net.hydra.jojomod.registry.FabricEntityClient;
import net.hydra.jojomod.registry.FabricKeyInputs;
import net.minecraft.client.renderer.RenderType;

public class RoundaboutFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FabricKeyInputs.register();
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.GASOLINE_SPLATTER,
                ModBlocks.BARBED_WIRE, ModBlocks.WIRE_TRAP, ModBlocks.BARBED_WIRE_BUNDLE,
                ModBlocks.LOCACACA_BLOCK, ModBlocks.NEW_LOCACACA_BLOCK, ModBlocks.LOCACACA_CACTUS,
                ModBlocks.GODDESS_STATUE_BLOCK,
                ModBlocks.BLOOD_SPLATTER,
                ModBlocks.BLUE_BLOOD_SPLATTER,
                ModBlocks.ENDER_BLOOD_SPLATTER);
        FabricPacketManager.registerS2CPackets();
        FabricParticlesClient.registerClientParticles();
        FabricEntityClient.register();
    }
}
