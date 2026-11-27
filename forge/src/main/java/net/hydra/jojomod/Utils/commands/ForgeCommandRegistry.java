package net.hydra.jojomod.Utils.commands;

import net.hydra.jojomod.event.commands.SBACom;
import net.hydra.jojomod.event.commands.SBADiscCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ForgeCommandRegistry{
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent e) {
        SBACom.register(e.getDispatcher());
        SBADiscCommand.register(e.getDispatcher());
    }
}
