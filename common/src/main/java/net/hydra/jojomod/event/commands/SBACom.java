package net.hydra.jojomod.event.commands;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.hydra.jojomod.SBACommands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;

public class SBACom {

    protected static final int DEFAULT_LVL = 1;
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        JJFAirPressureCommand.register(dispatcher);
        dispatcher.register(Commands.literal("sbaSetStandLevel")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.SBACommands.sbaSetStandLevel((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException()),DEFAULT_LVL))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .then(Commands.argument("level", IntegerArgumentType.integer())
                                .executes(context -> SBACommands.sbaSetStandLevel((CommandSourceStack)context.getSource(),
                                        EntityArgument.getEntities(context, "targets"),IntegerArgumentType.getInteger(context,"level")))
                        )
                ));
        dispatcher.register(Commands.literal("sbaLevelUpStand")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.SBACommands.sbaSetLevelupStand((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))
                .then(Commands.argument("targets", EntityArgument.entities())
                                .executes(context -> SBACommands.sbaSetLevelupStand((CommandSourceStack)context.getSource(),
                                        EntityArgument.getEntities(context, "targets")))
                        )
                );
        dispatcher.register(Commands.literal("sbaLevelDownStand")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.SBACommands.sbaSetLeveldownStand((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .executes(context -> SBACommands.sbaSetLeveldownStand((CommandSourceStack)context.getSource(),
                                EntityArgument.getEntities(context, "targets")))
                )
        );
        dispatcher.register(Commands.literal("sbaLevelUpFate")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.SBACommands.sbaSetLevelupFate((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .executes(context -> SBACommands.sbaSetLevelupFate((CommandSourceStack)context.getSource(),
                                EntityArgument.getEntities(context, "targets")))
                )
        );
        dispatcher.register(Commands.literal("sbaLevelDownFate")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.SBACommands.sbaSetLeveldownFate((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .executes(context -> SBACommands.sbaSetLeveldownFate((CommandSourceStack)context.getSource(),
                                EntityArgument.getEntities(context, "targets")))
                )
        );
        dispatcher.register(Commands.literal("sbaSetStand")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.SBACommands.sbaSetStand((CommandSourceStack)context.getSource(),
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
                                                .executes(context -> SBACommands.sbaSetStand((CommandSourceStack)context.getSource(),
                                                        EntityArgument.getEntities(context, "targets"),
                                                        StringArgumentType.getString(context, "stand_name"),
                                                        IntegerArgumentType.getInteger(context,"level"),
                                                        ((byte)IntegerArgumentType.getInteger(context,"skin")),
                                                        ((byte)IntegerArgumentType.getInteger(context,"pose")),
                                                        BoolArgumentType.getBool(context,"hidden_skin_unlocked")))
                                        )
                                )))
                )));

        dispatcher.register(Commands.literal("sbaSetFate")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.SBACommands.sbaSetFate((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException()),
                        "human",
                        0, 0))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .then(Commands.argument("fate_name", StringArgumentType.word())
                                .then(Commands.argument("level", IntegerArgumentType.integer())
                                        .then(Commands.argument("exp", IntegerArgumentType.integer())
                                             .executes(context -> SBACommands.sbaSetFate((CommandSourceStack)context.getSource(),
                                                                        EntityArgument.getEntities(context, "targets"),
                                                                        StringArgumentType.getString(context, "fate_name"),
                                                                        IntegerArgumentType.getInteger(context,"level"),
                                                                        IntegerArgumentType.getInteger(context,"exp")))
                                                        )
                                )
                        )));
        dispatcher.register(Commands.literal("sbaMaxFateSkills")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.SBACommands.sbaMaxFateSkills((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))
                .then(Commands.argument("targets", EntityArgument.entities())
                                                .executes(context -> SBACommands.sbaMaxFateSkills(    (CommandSourceStack)context.getSource(),
                                                        EntityArgument.getEntities(context, "targets")
                                        )
                                )
                        ));
        dispatcher.register(Commands.literal("sbaClearFateSkills")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.SBACommands.sbaClearFateSkills((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .executes(context -> SBACommands.sbaClearFateSkills(    (CommandSourceStack)context.getSource(),
                                        EntityArgument.getEntities(context, "targets")
                                )
                        )
                ));
        dispatcher.register(Commands.literal("sbaResetVampireData")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.SBACommands.sbaResetVampireData((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .executes(context -> SBACommands.sbaResetVampireData(    (CommandSourceStack)context.getSource(),
                                        EntityArgument.getEntities(context, "targets")
                                )
                        )
                ));
        dispatcher.register(Commands.literal("sbaSetVampireSkills")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.SBACommands.sbaSetVampireSkills((CommandSourceStack)context.getSource(),
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
                                                .executes(context -> SBACommands.sbaSetVampireSkills((CommandSourceStack)context.getSource(),
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
        dispatcher.register(Commands.literal("sbaSetVampireSkill")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.SBACommands.sbaSetVampireSkill((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException()),
                        0,0))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .then(Commands.argument("skill_number", IntegerArgumentType.integer())
                                        .then(Commands.argument("value", IntegerArgumentType.integer())
                                                .executes(context -> SBACommands.sbaSetVampireSkill((CommandSourceStack)context.getSource(),
                                                        EntityArgument.getEntities(context, "targets"),
                                                        IntegerArgumentType.getInteger(context, "skill_number"),
                                                        IntegerArgumentType.getInteger(context,"value")
                                                ))
                                        )
                                )
                        ));

        dispatcher.register(Commands.literal("sbaSetStandExp")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.SBACommands.sbaSetStandExp((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException()),DEFAULT_LVL))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .then(Commands.argument("experience", IntegerArgumentType.integer())
                                .executes(context -> SBACommands.sbaSetStandExp((CommandSourceStack)context.getSource(),
                                        EntityArgument.getEntities(context, "targets"),IntegerArgumentType.getInteger(context,"experience")))
                        )
                ));
        dispatcher.register(Commands.literal("sbaSetHeat")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.SBACommands.sbaSetHeat((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException()),DEFAULT_LVL))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .then(Commands.argument("heat", IntegerArgumentType.integer())
                                .executes(context -> SBACommands.sbaSetHeat((CommandSourceStack)context.getSource(),
                                        EntityArgument.getEntities(context, "targets"),IntegerArgumentType.getInteger(context,"heat")))
                        )
                ));
        dispatcher.register(Commands.literal("sbaReplenish")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.SBACommands.executeReplenish((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))
                .then(Commands.argument("targets", EntityArgument.entities())
                                .executes(context -> SBACommands.executeReplenish((CommandSourceStack)context.getSource(),
                                        EntityArgument.getEntities(context, "targets")))
                        )
                );
        dispatcher.register(Commands.literal("sbaDaze")
                .requires(source
                        -> source.hasPermission(2))
                .executes(context -> net.hydra.jojomod.SBACommands.executeDaze((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .executes(context -> SBACommands.executeDaze((CommandSourceStack)context.getSource(),
                                EntityArgument.getEntities(context, "targets")))
                )
        );
        dispatcher.register(Commands.literal("sbaFogTrapRange")
                        .then(Commands.argument("radius",IntegerArgumentType.integer())
                            .executes(context -> SBACommands.sbaFogTrapRange(context.getSource(), context.getSource().getPlayerOrException() ,IntegerArgumentType.getInteger(context,"radius")))
                ));
    }

}
