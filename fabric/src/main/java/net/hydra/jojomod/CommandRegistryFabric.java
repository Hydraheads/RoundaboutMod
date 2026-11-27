package net.hydra.jojomod;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.hydra.jojomod.event.commands.SBADiscCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.commands.ExperienceCommand;

public class CommandRegistryFabric {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                SBADiscCommand.register(dispatcher));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(dispatcher.register((((Commands.literal("heal").requires(source
                        -> source.hasPermission(2))).executes(context -> SBACommands.executeHeal((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))).then(Commands.argument("targets",
                        EntityArgument.entities()).executes(context -> SBACommands.executeHeal((CommandSourceStack)context.getSource(),
                        EntityArgument.getEntities(context, "targets")))))).createBuilder()));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(dispatcher.register((((Commands.literal("sbaLevelUpStand").requires(source
                        -> source.hasPermission(2))).executes(context -> SBACommands.levelup((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))).then(Commands.argument("targets",
                        EntityArgument.entities()).executes(context -> SBACommands.levelup((CommandSourceStack)context.getSource(),
                        EntityArgument.getEntities(context, "targets")))))).createBuilder()));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(dispatcher.register((((Commands.literal("drSummon").requires(source
                        -> source.hasPermission(2))).executes(context -> SBACommands.executeDebugSummon((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))).then(Commands.argument("targets",
                        EntityArgument.entities()).executes(context -> SBACommands.executeDebugSummon((CommandSourceStack)context.getSource(),
                        EntityArgument.getEntities(context, "targets")))))).createBuilder()));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(dispatcher.register((((Commands.literal("drAttack").requires(source
                        -> source.hasPermission(2))).executes(context -> SBACommands.executeDebugAttack((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))).then(Commands.argument("targets",
                        EntityArgument.entities()).executes(context -> SBACommands.executeDebugAttack((CommandSourceStack)context.getSource(),
                        EntityArgument.getEntities(context, "targets")))))).createBuilder()));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(dispatcher.register((((Commands.literal("drBarrage").requires(source
                        -> source.hasPermission(2))).executes(context -> SBACommands.executeDebugBarrage((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))).then(Commands.argument("targets",
                        EntityArgument.entities()).executes(context -> SBACommands.executeDebugBarrage((CommandSourceStack)context.getSource(),
                        EntityArgument.getEntities(context, "targets")))))).createBuilder()));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(dispatcher.register((((Commands.literal("drGuard").requires(source
                        -> source.hasPermission(2))).executes(context -> SBACommands.executeDebugGuard((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))).then(Commands.argument("targets",
                        EntityArgument.entities()).executes(context -> SBACommands.executeDebugGuard((CommandSourceStack)context.getSource(),
                        EntityArgument.getEntities(context, "targets")))))).createBuilder()));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(dispatcher.register((((Commands.literal("drCancel").requires(source
                        -> source.hasPermission(2))).executes(context -> SBACommands.executeDebugCancel((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))).then(Commands.argument("targets",
                        EntityArgument.entities()).executes(context -> SBACommands.executeDebugCancel((CommandSourceStack)context.getSource(),
                        EntityArgument.getEntities(context, "targets")))))).createBuilder()));


        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(dispatcher.register((((Commands.literal("drSpecial").requires(source
                        -> source.hasPermission(2))).executes(context -> SBACommands.executeDebugSpecial((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))).then(Commands.argument("targets",
                        EntityArgument.entities()).executes(context -> SBACommands.executeDebugSpecial((CommandSourceStack)context.getSource(),
                        EntityArgument.getEntities(context, "targets")))))).createBuilder()));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(dispatcher.register((((Commands.literal("debugSBA_tryPower").requires(source
                        -> source.hasPermission(2))).executes(context -> SBACommands.executeDebugCancel((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))).then(Commands.argument("number",
                        IntegerArgumentType.integer()).then(Commands.argument("targets",
                        EntityArgument.entities()).executes(context -> SBACommands.executeDebugAbility((CommandSourceStack)context.getSource(),
                        EntityArgument.getEntities(context, "targets"), IntegerArgumentType.getInteger(context, "ability")))))))
                        .createBuilder()));
    }
}
