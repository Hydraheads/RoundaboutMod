package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Roundabout.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientForgeForgeEvents {

    @SubscribeEvent
    public static void clientTickEvent(TickEvent.ClientTickEvent event) {
    }
}
