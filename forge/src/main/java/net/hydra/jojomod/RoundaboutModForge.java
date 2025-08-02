package net.hydra.jojomod;

import net.hydra.jojomod.Utils.commands.ForgeCommandRegistry;
import net.hydra.jojomod.biome_modifiers.BiomeCodec;
import net.hydra.jojomod.registry.*;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Roundabout.MOD_ID)
public class RoundaboutModForge {
    public RoundaboutModForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ConfigManager.loadConfigs(FMLPaths.CONFIGDIR.get().resolve(Roundabout.MOD_ID + ".json"),
                //FMLPaths.CONFIGDIR.get().resolve(Roundabout.MOD_ID + "-server.json"),
                FMLPaths.CONFIGDIR.get().resolve(Roundabout.MOD_ID + "-advanced.json"),
                FMLPaths.CONFIGDIR.get().resolve(Roundabout.MOD_ID + "-clientOnly.json"));
        //ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ForgeCommonConfig.SPEC, "roundabout-client.toml");
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ForgeCommonConfig.SPEC, "roundabout-common.toml");

        /**
        BlockBlacklist.load(
                FMLPaths.CONFIGDIR.get(),
                "roundabout-block_blacklist"
        );
         **/

        ForgeEffects.POTION_EFFECTS.register(bus);
        ForgeEntities.ENTITY_TYPES.register(bus);
        ForgeSounds.SOUNDS.register(bus);
        ForgeBlocks.BLOCKS.register(bus);
        ForgeBlocks.BLOCK_ENTITIES.register(bus);
        ForgeItems.ITEMS.register(bus);
        ForgeItems.POTIONS.register(bus);
        ForgeCreativeTab.TABS.register(bus);
        ForgeParticles.PARTICLES.register(bus);
        ForgeGamerules.registerGamerules();

        //
        BiomeCodec.BIOME_MODIFIER_SERIALIZERS.register(bus);
        ForgeLootModifiers.LOOT_MODIFIERS.register(bus);

        bus.addListener(this::commonSetup);
        bus.addListener(this::loadComplete);
        MinecraftForge.EVENT_BUS.addListener(this::entityLifeCycle);


        Roundabout.LOGGER.info("Hello Forge world!");
        Roundabout.init(true);

        //ForgeItems.assignStupidForge();
    }

    private void loadComplete(final FMLLoadCompleteEvent event) {
        MinecraftForge.EVENT_BUS.register(new ForgeCommandRegistry());
    }

    private void commonSetup(final FMLCommonSetupEvent event){
    }


    public void entityLifeCycle(ServerStartedEvent event) {
        RoundaboutModForgeServer.entityLifeCycle(event);
    }

}