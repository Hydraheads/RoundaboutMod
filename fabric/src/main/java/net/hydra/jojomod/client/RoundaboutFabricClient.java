package net.hydra.jojomod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.particles.FabricParticlesClient;
import net.hydra.jojomod.registry.FabricEntityClient;
import net.hydra.jojomod.registry.FabricItems;
import net.hydra.jojomod.registry.FabricKeyInputs;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class RoundaboutFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MainUtil.setClient();
        FabricKeyInputs.register();
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.GASOLINE_SPLATTER,
                ModBlocks.BARBED_WIRE, ModBlocks.WIRE_TRAP, ModBlocks.BARBED_WIRE_BUNDLE,
                ModBlocks.LOCACACA_BLOCK, ModBlocks.NEW_LOCACACA_BLOCK, ModBlocks.LOCACACA_CACTUS,
                ModBlocks.GODDESS_STATUE_BLOCK,
                ModBlocks.ORANGE_FIRE,
                ModBlocks.BLUE_FIRE,
                ModBlocks.PURPLE_FIRE,
                ModBlocks.GREEN_FIRE,
                ModBlocks.DREAD_FIRE,
                ModBlocks.CREAM_FIRE,
                ModBlocks.BLOOD_SPLATTER,
                ModBlocks.STREET_SIGN_DIO,
                ModBlocks.STREET_SIGN_RIGHT,
                ModBlocks.STREET_SIGN_STOP,
                ModBlocks.STREET_SIGN_YIELD,
                ModBlocks.STREET_SIGN_DANGER,
                ModBlocks.WALL_STREET_SIGN_DIO,
                ModBlocks.WALL_STREET_SIGN_RIGHT,
                ModBlocks.WALL_STREET_SIGN_STOP,
                ModBlocks.WALL_STREET_SIGN_YIELD,
                ModBlocks.WALL_STREET_SIGN_DANGER,
                ModBlocks.BLUE_BLOOD_SPLATTER,
                ModBlocks.ENDER_BLOOD_SPLATTER,
                ModBlocks.FOG_TRAP);
        FabricParticlesClient.registerClientParticles();
        FabricEntityClient.register();
        ClientPlayConnectionEvents.JOIN.register((clientPlayNetworkHandler, packetSender, minecraftClient) -> ClientNetworking.sendHandshake());
        ItemProperties.register(FabricItems.HARPOON, new ResourceLocation(Roundabout.MOD_ID,"throwing"), (itemStack, clientLevel, livingEntity, i) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0f : 0.0f);
    }
}
