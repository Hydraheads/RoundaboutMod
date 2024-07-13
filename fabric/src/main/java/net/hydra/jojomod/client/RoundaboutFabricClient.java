package net.hydra.jojomod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.networking.FabricPacketManager;
import net.hydra.jojomod.networking.FabricPackets;
import net.hydra.jojomod.particles.ModParticlesClient;
import net.hydra.jojomod.registry.FabricEntities;
import net.hydra.jojomod.registry.FabricEntityClient;
import net.hydra.jojomod.registry.FabricKeyInputs;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class RoundaboutFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FabricKeyInputs.register();
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.GASOLINE_SPLATTER,
                ModBlocks.BARBED_WIRE, ModBlocks.WIRE_TRAP, ModBlocks.BARBED_WIRE_BUNDLE,
                ModBlocks.LOCACACA_BLOCK);
        FabricPacketManager.registerS2CPackets();
        ModParticlesClient.registerClientParticles();
        FabricEntityClient.register();
    }
}
