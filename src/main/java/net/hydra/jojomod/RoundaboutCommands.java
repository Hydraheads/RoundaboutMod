package net.hydra.jojomod;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.networking.MyComponents;
import net.hydra.jojomod.networking.component.StandUserComponent;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.KillCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Collection;

public class RoundaboutCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(dispatcher.register((((CommandManager.literal("heal").requires(source
                        -> source.hasPermissionLevel(2))).executes(context -> executeHeal((ServerCommandSource)context.getSource(),
                        ImmutableList.of(((ServerCommandSource)context.getSource()).getEntityOrThrow())))).then(CommandManager.argument("targets",
                        EntityArgumentType.entities()).executes(context -> executeHeal((ServerCommandSource)context.getSource(),
                        EntityArgumentType.getEntities(context, "targets")))))).createBuilder()));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(dispatcher.register((((CommandManager.literal("drSummon").requires(source
                        -> source.hasPermissionLevel(2))).executes(context -> executeDebugSummon((ServerCommandSource)context.getSource(),
                        ImmutableList.of(((ServerCommandSource)context.getSource()).getEntityOrThrow())))).then(CommandManager.argument("targets",
                        EntityArgumentType.entities()).executes(context -> executeDebugSummon((ServerCommandSource)context.getSource(),
                        EntityArgumentType.getEntities(context, "targets")))))).createBuilder()));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(dispatcher.register((((CommandManager.literal("drAttack").requires(source
                        -> source.hasPermissionLevel(2))).executes(context -> executeDebugAttack((ServerCommandSource)context.getSource(),
                        ImmutableList.of(((ServerCommandSource)context.getSource()).getEntityOrThrow())))).then(CommandManager.argument("targets",
                        EntityArgumentType.entities()).executes(context -> executeDebugAttack((ServerCommandSource)context.getSource(),
                        EntityArgumentType.getEntities(context, "targets")))))).createBuilder()));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(dispatcher.register((((CommandManager.literal("drBarrage").requires(source
                        -> source.hasPermissionLevel(2))).executes(context -> executeDebugBarrage((ServerCommandSource)context.getSource(),
                        ImmutableList.of(((ServerCommandSource)context.getSource()).getEntityOrThrow())))).then(CommandManager.argument("targets",
                        EntityArgumentType.entities()).executes(context -> executeDebugBarrage((ServerCommandSource)context.getSource(),
                        EntityArgumentType.getEntities(context, "targets")))))).createBuilder()));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(dispatcher.register((((CommandManager.literal("drGuard").requires(source
                        -> source.hasPermissionLevel(2))).executes(context -> executeDebugGuard((ServerCommandSource)context.getSource(),
                        ImmutableList.of(((ServerCommandSource)context.getSource()).getEntityOrThrow())))).then(CommandManager.argument("targets",
                        EntityArgumentType.entities()).executes(context -> executeDebugGuard((ServerCommandSource)context.getSource(),
                        EntityArgumentType.getEntities(context, "targets")))))).createBuilder()));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(dispatcher.register((((CommandManager.literal("drCancel").requires(source
                        -> source.hasPermissionLevel(2))).executes(context -> executeDebugCancel((ServerCommandSource)context.getSource(),
                        ImmutableList.of(((ServerCommandSource)context.getSource()).getEntityOrThrow())))).then(CommandManager.argument("targets",
                        EntityArgumentType.entities()).executes(context -> executeDebugCancel((ServerCommandSource)context.getSource(),
                        EntityArgumentType.getEntities(context, "targets")))))).createBuilder()));
    }
    private static int executeHeal(ServerCommandSource source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).setHealth(((LivingEntity) entity).getMaxHealth());
            }
        }
        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable(  "commands.roundabout.heal.single", ((Entity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable(  "commands.roundabout.heal.multiple", targets.size()), true);
        }
        return targets.size();
    }
    private static int executeDebugSummon(ServerCommandSource source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                StandUserComponent standUserData = MyComponents.STAND_USER.get(entity);
                standUserData.summonStand(entity.getWorld(), true,true);
            }
        }
        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable(  "commands.roundabout.force_summon.single", ((Entity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable(  "commands.roundabout.force_summon.multiple", targets.size()), true);
        }
        return targets.size();
    }

    private static int executeDebugAttack(ServerCommandSource source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                StandUserComponent standUserData = MyComponents.STAND_USER.get(entity);
                standUserData.tryPower(PowerIndex.ATTACK, true);
            }
        }
        return targets.size();
    }

    private static int executeDebugBarrage(ServerCommandSource source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                StandUserComponent standUserData = MyComponents.STAND_USER.get(entity);
                standUserData.tryPower(PowerIndex.BARRAGE, true);
            }
        }
        return targets.size();
    }

    private static int executeDebugGuard(ServerCommandSource source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                StandUserComponent standUserData = MyComponents.STAND_USER.get(entity);
                standUserData.tryPower(PowerIndex.GUARD, true);
            }
        }
        return targets.size();
    }

    private static int executeDebugCancel(ServerCommandSource source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                StandUserComponent standUserData = MyComponents.STAND_USER.get(entity);
                standUserData.tryPower(PowerIndex.NONE, true);
            }
        }
        return targets.size();
    }
}
