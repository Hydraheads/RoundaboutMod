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
