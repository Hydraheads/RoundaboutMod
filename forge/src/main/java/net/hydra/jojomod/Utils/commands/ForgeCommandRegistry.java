package net.hydra.jojomod.Utils.commands;

import net.hydra.jojomod.event.commands.RoundaboutCom;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ForgeCommandRegistry{
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent e) {
        RoundaboutCom.register(e.getDispatcher());
    }
}
