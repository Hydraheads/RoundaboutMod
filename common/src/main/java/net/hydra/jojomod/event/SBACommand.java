package net.hydra.jojomod.event;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

import java.util.List;

public interface SBACommand {
    /**
     * If the command is not enabled, the register method will not be called.
     */
    boolean enabled();

    /**
     * Registers the command with the dispatcher.
     */
    List<LiteralArgumentBuilder<CommandSourceStack>> register();
}
