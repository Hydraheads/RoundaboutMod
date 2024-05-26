package net.hydra.jojomod;

import net.hydra.jojomod.registry.ForgeEntities;
import net.hydra.jojomod.registry.ForgeEvents;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Roundabout.MOD_ID)
public class RoundaboutModForge {

    public RoundaboutModForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ForgeEntities.ENTITY_TYPES.register(bus);

        ForgeEntities.register();
        Roundabout.LOGGER.info("Hello Forge world!");
        Roundabout.init();
    }

}