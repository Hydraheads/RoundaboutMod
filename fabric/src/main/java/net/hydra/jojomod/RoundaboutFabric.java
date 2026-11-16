package net.hydra.jojomod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.hydra.jojomod.block.ModFeatures;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.commands.RoundaboutCom;
import net.hydra.jojomod.item.DispenserRegistry;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.networking.FabricJJFNetworking;
import net.hydra.jojomod.particles.FabricParticles;
import net.hydra.jojomod.registry.*;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.world.FabricGamerules;
import net.hydra.jojomod.world.gen.ModWorldGeneration;
import net.hydra.jojomod.worldgen.AutumnLeavesDecorator;
import net.hydra.jojomod.worldgen.ModTreeDecorators;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public class RoundaboutFabric implements ModInitializer {


    @Override
    public void onInitialize() {
        Roundabout.LOGGER.info("Loading Roundabout (Fabric)");

        ServerLifecycleEvents.SERVER_STARTED.register(RoundaboutLoadServer::onServerStarted);
        ConfigManager.loadConfigs(FabricLoader.getInstance()
                .getConfigDir()
                .resolve(Roundabout.MOD_ID + ".json"),
                //FabricLoader.getInstance()
                //.getConfigDir()
                //.resolve(Roundabout.MOD_ID + "-server.json"),
                FabricLoader.getInstance()
                        .getConfigDir()
                        .resolve(Roundabout.MOD_ID + "-advanced.json"),
                FabricLoader.getInstance()
                        .getConfigDir()
                        .resolve(Roundabout.MOD_ID + "-clientOnly.json"));

        /**
        BlockBlacklist.load(
                FabricLoader.getInstance().getConfigDir(),
                "roundabout-block_blacklist"
        );
         **/

        FabricLootTables.modifyLootTables();

        FabricEffects.register();
        FabricEntities.register();
        FabricJJFNetworking.register();
        FabricSounds.register();
        FabricBlocks.register();
        FabricItems.register();
        FabricMenus.register();
        FabricParticles.registerParticles();
        FabricGamerules.registerGamerules();
        CommandRegistryFabric.register();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            RoundaboutCom.register(dispatcher);
        });
        ModWorldGeneration.generateWorldGen();
        DispenserRegistry.init();
        //villager trades
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FISHERMAN, 1, factories -> {
            factories.add((trader, random) -> new MerchantOffer(
                    new ItemStack(ModItems.PIRANHA, 5),
                    new ItemStack(Items.EMERALD, 1),
                    16, 2, 0.05F
            ));
        });
        Roundabout.init(false);
        ModSounds.registerSoundEvents();
        ConfigManager.loadBlacklists2();
        ConfigManager.loadFoodClear();
        ConfigManager.foodParticleParser();
        ModTreeDecorators.AUTUMN_LEAVES = Registry.register(
                BuiltInRegistries.TREE_DECORATOR_TYPE,
                new ResourceLocation("roundabout", "autumn_leaves"),
                new TreeDecoratorType<>(AutumnLeavesDecorator.CODEC)
        );
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(
                        Biomes.FOREST,
                        Biomes.FLOWER_FOREST,
                        Biomes.BIRCH_FOREST,
                        Biomes.OLD_GROWTH_BIRCH_FOREST,
                        Biomes.DARK_FOREST
                ),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ResourceKey.create(
                        Registries.PLACED_FEATURE,
                        ModFeatures.GINKGO_TREE_PLACED
                )
        );
    }

    
}
