package net.hydra.jojomod.event.commands;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.hydra.jojomod.RoundaboutCommands;
import net.minecraft.world.entity.Entity;
import net.zetalasis.world.DynamicWorld;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;

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

        dispatcher.register(Commands.literal("roundaboutSetFate")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.RoundaboutCommands.roundaboutSetFate((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException()),
                        "human",
                        0, 0))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .then(Commands.argument("fate_name", StringArgumentType.word())
                                .then(Commands.argument("level", IntegerArgumentType.integer())
                                        .then(Commands.argument("exp", IntegerArgumentType.integer())
                                             .executes(context -> RoundaboutCommands.roundaboutSetFate((CommandSourceStack)context.getSource(),
                                                                        EntityArgument.getEntities(context, "targets"),
                                                                        StringArgumentType.getString(context, "fate_name"),
                                                                        IntegerArgumentType.getInteger(context,"level"),
                                                                        IntegerArgumentType.getInteger(context,"exp")))
                                                        )
                                )
                        )));
        dispatcher.register(Commands.literal("roundaboutMaxFateSkills")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.RoundaboutCommands.roundaboutMaxFateSkills((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))
                .then(Commands.argument("targets", EntityArgument.entities())
                                                .executes(context -> RoundaboutCommands.roundaboutMaxFateSkills(    (CommandSourceStack)context.getSource(),
                                                        EntityArgument.getEntities(context, "targets")
                                        )
                                )
                        ));
        dispatcher.register(Commands.literal("roundaboutClearFateSkills")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.RoundaboutCommands.roundaboutClearFateSkills((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .executes(context -> RoundaboutCommands.roundaboutClearFateSkills(    (CommandSourceStack)context.getSource(),
                                        EntityArgument.getEntities(context, "targets")
                                )
                        )
                ));
        dispatcher.register(Commands.literal("roundaboutResetVampireData")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.RoundaboutCommands.roundaboutResetVampireData((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .executes(context -> RoundaboutCommands.roundaboutResetVampireData(    (CommandSourceStack)context.getSource(),
                                        EntityArgument.getEntities(context, "targets")
                                )
                        )
                ));
        dispatcher.register(Commands.literal("roundaboutSetVampireSkills")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.RoundaboutCommands.roundaboutSetVampireSkills((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException()),
                        0,0,0,0,0,0,
                        0,0,0,0,0,0))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .then(Commands.argument("strength", IntegerArgumentType.integer())
                                .then(Commands.argument("dexterity", IntegerArgumentType.integer())
                                        .then(Commands.argument("resilience", IntegerArgumentType.integer())
                                        .then(Commands.argument("hypnotism", IntegerArgumentType.integer())
                                                .then(Commands.argument("superHearing", IntegerArgumentType.integer())
                                                        .then(Commands.argument("bloodSpeed", IntegerArgumentType.integer())
                                                                .then(Commands.argument("grafting", IntegerArgumentType.integer())
                                                                        .then(Commands.argument("fleshBud", IntegerArgumentType.integer())
                                                                                .then(Commands.argument("daggerSplatter", IntegerArgumentType.integer())
                                                                                        .then(Commands.argument("jump", IntegerArgumentType.integer())
                                                                                                .then(Commands.argument("ripperEyes", IntegerArgumentType.integer())
                                                                                                        .then(Commands.argument("freeze", IntegerArgumentType.integer())
                                                .executes(context -> RoundaboutCommands.roundaboutSetVampireSkills((CommandSourceStack)context.getSource(),
                                                        EntityArgument.getEntities(context, "targets"),
                                                        IntegerArgumentType.getInteger(context, "strength"),
                                                        IntegerArgumentType.getInteger(context,"dexterity"),
                                                        IntegerArgumentType.getInteger(context,"resilience"),
                                                        IntegerArgumentType.getInteger(context,"hypnotism"),
                                                        IntegerArgumentType.getInteger(context,"superHearing"),
                                                        IntegerArgumentType.getInteger(context,"bloodSpeed"),
                                                        IntegerArgumentType.getInteger(context,"grafting"),
                                                        IntegerArgumentType.getInteger(context,"fleshBud"),
                                                        IntegerArgumentType.getInteger(context,"daggerSplatter"),
                                                        IntegerArgumentType.getInteger(context,"jump"),
                                                        IntegerArgumentType.getInteger(context,"ripperEyes"),
                                                        IntegerArgumentType.getInteger(context,"freeze")
                                                ))
                                        )
                                )
                        ))))))))))));
        dispatcher.register(Commands.literal("roundaboutSetVampireSkill")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.RoundaboutCommands.roundaboutSetVampireSkill((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException()),
                        0,0))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .then(Commands.argument("skill_number", IntegerArgumentType.integer())
                                        .then(Commands.argument("value", IntegerArgumentType.integer())
                                                .executes(context -> RoundaboutCommands.roundaboutSetVampireSkill((CommandSourceStack)context.getSource(),
                                                        EntityArgument.getEntities(context, "targets"),
                                                        IntegerArgumentType.getInteger(context, "skill_number"),
                                                        IntegerArgumentType.getInteger(context,"value")
                                                ))
                                        )
                                )
                        ));

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
                    w.broadcastPacketsToPlayers(context.getSource().getServer());
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
        dispatcher.register(Commands.literal("roundaboutFogTrapRange")
                        .then(Commands.argument("radius",IntegerArgumentType.integer())
                            .executes(context -> RoundaboutCommands.roundaboutFogTrapRange(context.getSource(), context.getSource().getPlayerOrException() ,IntegerArgumentType.getInteger(context,"radius")))
                ));
    }

}
