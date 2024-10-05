package net.hydra.jojomod;

import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.event.powers.stand.PowersTheWorld;
import net.hydra.jojomod.item.ModItems;
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
            if (entity instanceof LivingEntity le) {
                ((StandUser) entity).roundabout$setStandDisc(ModItems.STAND_DISC_THE_WORLD.getDefaultInstance());
                ((StandUser) entity).roundabout$setStandPowers(new PowersTheWorld(le));
                ((StandUser) entity).roundabout$summonStand(entity.level(), true,true);
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
                ((StandUser) entity).roundabout$tryPower(PowerIndex.ATTACK, true);
            }
        }
        return targets.size();
    }
    static int executeDebugSpecial(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                ((StandUser) entity).roundabout$tryPower(PowerIndex.SPECIAL, true);
            }
        }
        return targets.size();
    }

    static int executeDebugBarrage(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                ((StandUser) entity).roundabout$tryPower(PowerIndex.BARRAGE, true);
            }
        }
        return targets.size();
    }

    static int executeDebugGuard(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                ((StandUser) entity).roundabout$tryPower(PowerIndex.GUARD, true);
            }
        }
        return targets.size();
    }

    static int executeDebugCancel(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity LE) {
                ((StandUser) entity).roundabout$tryPower(PowerIndex.NONE, true);
                if (((TimeStop) entity.level()).isTimeStoppingEntity(LE)){
                    ((StandUser) entity).roundabout$tryPower(PowerIndex.SPECIAL_FINISH, true);
                }
            }
        }
        return targets.size();
    }


    static int executeDebugAbility(CommandSourceStack source, Collection<? extends Entity> targets, int ability) {
        if (ability < 100) {
            for (Entity entity : targets) {
                if (entity instanceof LivingEntity) {
                    ((StandUser) entity).roundabout$tryPower((byte) ability, true);
                }
            }
        }
        return targets.size();
    }
}
