package net.hydra.jojomod.event.commands;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.hydra.jojomod.RoundaboutCommands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;

import static net.minecraft.commands.Commands.literal;

public class RoundaboutCom {

    protected static final int DEFAULT_LVL = 1;
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("roundaboutSetStandLevel")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.RoundaboutCommands.roundaboutSetStandLevel((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException()),DEFAULT_LVL))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .then(Commands.argument("level", IntegerArgumentType.integer())
                                .executes(context -> RoundaboutCommands.roundaboutSetStandLevel((CommandSourceStack)context.getSource(),
                                        EntityArgument.getEntities(context, "targets"),IntegerArgumentType.getInteger(context,"level")))
                        )
                ));
    }

}
