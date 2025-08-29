package net.hydra.jojomod;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersJustice;
import net.hydra.jojomod.stand.powers.PowersTheWorld;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandDiscItem;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public class RoundaboutCommands {

    public static int roundaboutFogTrapRange(CommandSourceStack source, ServerPlayer plr, int rangeSet){
        if(plr instanceof StandUser se && se.roundabout$getStandPowers() instanceof PowersJustice justice){
            justice.fogTrapRange = rangeSet;
            source.sendSuccess(()->Component.translatable("commands.roundabout.justice_fogTrapRange",String.valueOf(rangeSet)),false);
        } else{
            source.sendSuccess(()->Component.translatable("commands.roundabout.justice_fogTrapRangeFail"),false);
        }
        return 0;
    }
    public static int roundaboutSetStandExp(CommandSourceStack source, Collection<? extends Entity> targets, int level) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                if (entity instanceof Player PE) {
                    IPlayerEntity ipe = ((IPlayerEntity)PE);
                    StandUser user = ((StandUser) PE);
                    ItemStack standDisc = user.roundabout$getStandDisc();
                    int standExp;
                    int lvl = ipe.roundabout$getStandLevel();
                    standExp = Mth.clamp(level,0,user.roundabout$getStandPowers().getExpForLevelUp(lvl));
                    if (!standDisc.isEmpty() && !(standDisc.getItem() instanceof MaxStandDiscItem)){
                        ipe.roundabout$setStandExp(standExp);
                    }
                }
            }
        }
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable(  "commands.roundabout.experience_specific.single", ((Entity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable(  "commands.roundabout.experience_specific.multiple", targets.size()), true);
        }
        return targets.size();
    }
    public static int roundaboutSetStand(CommandSourceStack source, Collection<? extends Entity> targets,
                                         String standType, int level, byte skin, byte pose, boolean hiddenUnlocked) {


        String name = "";
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity LE) {
                    StandUser user = ((StandUser) LE);
                    ItemStack disc = ItemStack.EMPTY;

                    for (Item i : BuiltInRegistries.ITEM)
                    {
                        // in the future: this code should be changed to support addons
                        if (BuiltInRegistries.ITEM.getKey(i).equals(Roundabout.location(standType.toLowerCase()+"_disc")))
                        {
                            disc = i.getDefaultInstance();
                            name = "item.roundabout."+standType.toLowerCase()+"_disc.desc";
                            break;
                        }
                    }

                    user.roundabout$setStandDisc(disc);
                if (disc != ItemStack.EMPTY && disc.getItem() instanceof StandDiscItem SD) {
                    SD.generateStandPowers(LE);

                    if (entity instanceof Player PE) {
                        ItemStack standDisc = user.roundabout$getStandDisc();
                        IPlayerEntity ipe = ((IPlayerEntity)PE);
                        int standLevel = ipe.roundabout$getStandLevel();
                        if (!standDisc.isEmpty() && !(standDisc.getItem() instanceof MaxStandDiscItem)){
                            ipe.roundabout$setStandExp(0);
                            level = (byte) Mth.clamp(level, 1, SD.standPowers.getMaxLevel());
                            ipe.roundabout$setStandLevel((byte) level);
                        }
                        ipe.roundabout$setUnlockedBonusSkin(hiddenUnlocked);
                    }

                    user.roundabout$setStandSkin(skin);
                    user.roundabout$setIdlePosX(pose);

                    if (user.roundabout$getActive()){
                        ((StandUser) entity).roundabout$summonStand(entity.level(), true,false);
                    }
                }
                else {
                    source.sendFailure(Component.translatable("commands.roundabout.argument.standtype.invalid", standType.toLowerCase()));
                    return targets.size();
                }
            }
        }

        final String nm = Component.translatable(name).getString();
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable("commands.roundabout.argument.standtype.valid_2", ((Entity)targets.iterator().next()).getDisplayName(),nm), true);
        } else {
            source.sendSuccess(() -> Component.translatable(  "commands.roundabout.argument.standtype.valid", targets.size(), nm), true);
        }
        return targets.size();
    }
    public static int roundaboutSetStandLevel(CommandSourceStack source, Collection<? extends Entity> targets, int level) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                if (entity instanceof Player PE) {
                    IPlayerEntity ipe = ((IPlayerEntity)PE);
                    StandUser user = ((StandUser) PE);
                    ItemStack standDisc = user.roundabout$getStandDisc();
                    int standLevel = ipe.roundabout$getStandLevel();
                    if (!standDisc.isEmpty() && !(standDisc.getItem() instanceof MaxStandDiscItem)){
                        ipe.roundabout$setStandExp(0);
                        ipe.roundabout$setStandLevel((byte) level);
                    }
                }
            }
        }
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable(  "commands.roundabout.levelup_specific.single", ((Entity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable(  "commands.roundabout.levelup_specific.multiple", targets.size()), true);
        }
        return targets.size();
    }
    public static int executeReplenish(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).setHealth(((LivingEntity) entity).getMaxHealth());
                if (entity instanceof Player PE){
                    PE.getFoodData().setFoodLevel(20);
                }
            }
        }
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable(  "commands.roundabout.heal.single", ((Entity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable(  "commands.roundabout.heal.multiple", targets.size()), true);
        }
        return targets.size();
    }
    public static int executeHeal(CommandSourceStack source, Collection<? extends Entity> targets) {
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
    static int levelup(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof Player PE) {
                IPlayerEntity ipe = ((IPlayerEntity)PE);
                StandUser user = ((StandUser) PE);
                ItemStack standDisc = user.roundabout$getStandDisc();
                int standLevel = ipe.roundabout$getStandLevel();
                if (!standDisc.isEmpty() && !(standDisc.getItem() instanceof MaxStandDiscItem) &&
                        standLevel < user.roundabout$getStandPowers().getMaxLevel()){
                    ipe.roundabout$setStandExp(0);
                    ipe.roundabout$setStandLevel((byte) (standLevel+1));
                }
            }
        }
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable(  "commands.roundabout.levelup.single", ((Entity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable(  "commands.roundabout.levelup.multiple", targets.size()), true);
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
