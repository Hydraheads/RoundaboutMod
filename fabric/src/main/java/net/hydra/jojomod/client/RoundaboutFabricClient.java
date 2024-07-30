package net.hydra.jojomod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.fabricmc.fabric.mixin.object.builder.client.ModelPredicateProviderRegistryAccessor;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.FabricPacketManager;
import net.hydra.jojomod.networking.FabricPackets;
import net.hydra.jojomod.particles.ModParticlesClient;
import net.hydra.jojomod.registry.FabricEntities;
import net.hydra.jojomod.registry.FabricEntityClient;
import net.hydra.jojomod.registry.FabricItems;
import net.hydra.jojomod.registry.FabricKeyInputs;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class RoundaboutFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FabricKeyInputs.register();
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.GASOLINE_SPLATTER,
                ModBlocks.BARBED_WIRE, ModBlocks.WIRE_TRAP, ModBlocks.BARBED_WIRE_BUNDLE,
                ModBlocks.LOCACACA_BLOCK, ModBlocks.NEW_LOCACACA_BLOCK, ModBlocks.LOCACACA_CACTUS,
                ModBlocks.GODDESS_STATUE_BLOCK);
        FabricPacketManager.registerS2CPackets();
        ModParticlesClient.registerClientParticles();
        FabricEntityClient.register();
    }
}
