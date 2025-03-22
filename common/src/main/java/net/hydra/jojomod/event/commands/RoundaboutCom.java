package net.hydra.jojomod.event.commands;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.sun.jdi.BooleanType;
import com.sun.jdi.connect.Connector;
import net.hydra.jojomod.RoundaboutCommands;
import net.hydra.jojomod.world.DynamicWorld;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.GameModeArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

import java.util.Collection;
import java.util.Collections;

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
        dispatcher.register(Commands.literal("roundaboutSetStand")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.RoundaboutCommands.roundaboutSetStand((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException()),
                        "none",
                        DEFAULT_LVL,
                        (byte) 0, (byte) 0, false))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .then(Commands.argument("stand_name", StringArgumentType.word())
                                .then(Commands.argument("level", IntegerArgumentType.integer())
                                        .then(Commands.argument("skin", IntegerArgumentType.integer())
                                                .then(Commands.argument("pose", IntegerArgumentType.integer())
                                                        .then(Commands.argument("hidden_skin_unlocked", BoolArgumentType.bool())
                                                .executes(context -> RoundaboutCommands.roundaboutSetStand((CommandSourceStack)context.getSource(),
                                                        EntityArgument.getEntities(context, "targets"),
                                                        StringArgumentType.getString(context, "stand_name"),
                                                        IntegerArgumentType.getInteger(context,"level"),
                                                        ((byte)IntegerArgumentType.getInteger(context,"skin")),
                                                        ((byte)IntegerArgumentType.getInteger(context,"pose")),
                                                        BoolArgumentType.getBool(context,"hidden_skin_unlocked")))
                                        )
                                )))
                )));
        dispatcher.register(Commands.literal("roundaboutSetStandExp")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.RoundaboutCommands.roundaboutSetStandExp((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException()),DEFAULT_LVL))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .then(Commands.argument("experience", IntegerArgumentType.integer())
                                .executes(context -> RoundaboutCommands.roundaboutSetStandExp((CommandSourceStack)context.getSource(),
                                        EntityArgument.getEntities(context, "targets"),IntegerArgumentType.getInteger(context,"experience")))
                        )
                ));
        dispatcher.register(Commands.literal("roundaboutReplenish")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.RoundaboutCommands.executeReplenish((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))
                .then(Commands.argument("targets", EntityArgument.entities())
                                .executes(context -> RoundaboutCommands.executeReplenish((CommandSourceStack)context.getSource(),
                                        EntityArgument.getEntities(context, "targets")))
                        )
                );
        dispatcher.register(Commands.literal("roundaboutGenerateD4CWorld")
                .requires(commandSourceStack ->
                        commandSourceStack.hasPermission(2))
                .executes(context->{
                    DynamicWorld w = DynamicWorld.generateD4CWorld(context.getSource().getServer());
                    Component worldText = ComponentUtils.wrapInSquareBrackets(
                            Component.literal(w.getName())
                    ).withStyle(
                            style->style.withColor(ChatFormatting.GREEN)
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/execute in roundabout:" + w.getName() + " run tp @s ~ ~ ~"))
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("commands.roundabout.d4c_world.tooltip")))
                    );

                    context.getSource().sendSuccess(()->Component.translatable("commands.roundabaout.d4c_world", worldText), false);
                    return 0;
                }));
    }

}
