package net.hydra.jojomod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.mixin.object.builder.client.ModelPredicateProviderRegistryAccessor;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPermaCasting;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.FabricPacketManager;
import net.hydra.jojomod.particles.FabricParticlesClient;
import net.hydra.jojomod.registry.FabricEntityClient;
import net.hydra.jojomod.registry.FabricItems;
import net.hydra.jojomod.registry.FabricKeyInputs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

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
        ClientPlayConnectionEvents.JOIN.register((clientPlayNetworkHandler, packetSender, minecraftClient) -> ClientNetworking.sendHandshake());
        ItemProperties.register(FabricItems.HARPOON, new ResourceLocation(Roundabout.MOD_ID,"throwing"), (itemStack, clientLevel, livingEntity, i) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0f : 0.0f);
        Player pl = Minecraft.getInstance().player;
    }
}
