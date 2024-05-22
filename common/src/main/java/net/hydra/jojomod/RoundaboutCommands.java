package net.hydra.jojomod;

import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import java.util.Collection;

public class RoundaboutCommands {

    static int executeHeal(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).setHealth(((LivingEntity) entity).getMaxHealth());
            }
        }
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable(  "commands.roundabout.heal.single", ((Entity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable(  "commands.roundabout.heal.multiple", targets.size()), true);
        }
        return targets.size();
    }
    static int executeDebugSummon(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                ((StandUser) entity).summonStand(entity.level(), true,true);
            }
        }
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable(  "commands.roundabout.force_summon.single", ((Entity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable(  "commands.roundabout.force_summon.multiple", targets.size()), true);
        }
        return targets.size();
    }

    static int executeDebugAttack(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                ((StandUser) entity).tryPower(PowerIndex.ATTACK, true);
            }
        }
        return targets.size();
    }

    static int executeDebugBarrage(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                ((StandUser) entity).tryPower(PowerIndex.BARRAGE, true);
            }
        }
        return targets.size();
    }

    static int executeDebugGuard(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                ((StandUser) entity).tryPower(PowerIndex.GUARD, true);
            }
        }
        return targets.size();
    }

    static int executeDebugCancel(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                ((StandUser) entity).tryPower(PowerIndex.NONE, true);
            }
        }
        return targets.size();
    }
}
