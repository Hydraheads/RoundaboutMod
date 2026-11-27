package net.hydra.jojomod;

import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.VampireData;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.fates.powers.AbilityScapeBasis;
import net.hydra.jojomod.fates.powers.ZombieFate;
import net.hydra.jojomod.stand.powers.PowersJustice;
import net.hydra.jojomod.stand.powers.PowersTheWorld;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public class SBACommands {

    public static int sbaFogTrapRange(CommandSourceStack source, ServerPlayer plr, int rangeSet){
        if(plr instanceof StandUser se && se.sba$getStandPowers() instanceof PowersJustice justice){
            justice.fogTrapRange = rangeSet;
            source.sendSuccess(()->Component.translatable("commands.sba.justice_fogTrapRange",String.valueOf(rangeSet)),false);
        } else{
            source.sendSuccess(()->Component.translatable("commands.sba.justice_fogTrapRangeFail"),false);
        }
        return 0;
    }
    public static int sbaSetStandExp(CommandSourceStack source, Collection<? extends Entity> targets, int level) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                if (entity instanceof Player PE) {
                    IPlayerEntity ipe = ((IPlayerEntity)PE);
                    StandUser user = ((StandUser) PE);
                    ItemStack standDisc = user.sba$getStandDisc();
                    int standExp;
                    int lvl = ipe.sba$getStandLevel();
                    standExp = Mth.clamp(level,0,user.sba$getStandPowers().getExpForLevelUp(lvl));
                    if (!standDisc.isEmpty() && !(standDisc.getItem() instanceof MaxStandDiscItem)){
                        ipe.sba$setStandExp(standExp);
                    }
                }
            }
        }
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable(  "commands.sba.experience_specific.single", ((Entity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable(  "commands.sba.experience_specific.multiple", targets.size()), true);
        }
        return targets.size();
    }
    public static int sbaSetHeat(CommandSourceStack source, Collection<? extends Entity> targets, int level) {
        for (Entity entity : targets) {
            HeatUtil.setHeat(entity,level);
        }
        return targets.size();
    }
    public static int sbaSetStand(CommandSourceStack source, Collection<? extends Entity> targets,
                                         String standType, int level, byte skin, byte pose, boolean hiddenUnlocked) {
        String name = "";
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity LE) {
                StandUser user = ((StandUser) LE);
                ItemStack disc = ItemStack.EMPTY;

                if (user.sba$getStandPowers() != null) {
                    user.sba$getStandPowers().onStandSwitch();
                }

                for (Item i : BuiltInRegistries.ITEM)
                {
                    // in the future: this code should be changed to support addons
                    if (BuiltInRegistries.ITEM.getKey(i).equals(SBA.location(standType.toLowerCase()+"_disc")))
                    {
                        disc = i.getDefaultInstance();
                        name = "item.sba."+standType.toLowerCase()+"_disc.desc";
                        break;
                    }
                }

                user.sba$setStandDisc(disc);
                if (disc != ItemStack.EMPTY && disc.getItem() instanceof StandDiscItem SD) {

                    user.sba$setStandSkin(skin);
                    user.sba$setIdlePosX(pose);
                    if (entity instanceof Player PE) {
                        ItemStack standDisc = user.sba$getStandDisc();
                        IPlayerEntity ipe = ((IPlayerEntity)PE);
                        int standLevel = ipe.sba$getStandLevel();
                        ipe.sba$setUnlockedBonusSkin(hiddenUnlocked);
                        if (!standDisc.isEmpty() && !(standDisc.getItem() instanceof MaxStandDiscItem)){
                            ipe.sba$setStandExp(0);
                            level = (byte) Mth.clamp(level, 1, SD.standPowers.getMaxLevel());
                            ipe.sba$setStandLevel((byte) level);
                            user.sba$setStandDisc(MainUtil.saveToDiscData(PE,((StandUser)PE).sba$getStandDisc()));
                        }
                    }

                    if (PowerTypes.hasStandActive(entity)){
                        ((StandUser) entity).sba$summonStand(entity.level(), true,false);
                    }
                }
                else {
                    source.sendFailure(Component.translatable("commands.sba.argument.standtype.invalid", standType.toLowerCase()));
                    return targets.size();
                }
            }
        }

        final String nm = Component.translatable(name).getString();
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable("commands.sba.argument.standtype.valid_2", ((Entity)targets.iterator().next()).getDisplayName(),nm), true);
        } else {
            source.sendSuccess(() -> Component.translatable(  "commands.sba.argument.standtype.valid", targets.size(), nm), true);
        }
        return targets.size();
    }


    public static int sbaSetLevelupStand(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                if (entity instanceof Player PE) {
                    IPlayerEntity ipe = ((IPlayerEntity)PE);
                    StandUser user = ((StandUser) PE);
                    ItemStack standDisc = user.sba$getStandDisc();
                    int standLevel = ipe.sba$getStandLevel();
                    if (!standDisc.isEmpty() && !(standDisc.getItem() instanceof MaxStandDiscItem)){
                        ipe.sba$setStandExp(0);
                        ipe.sba$setStandLevel((byte) Math.min(user.sba$getStandPowers().getMaxLevel(),standLevel+1));
                        user.sba$updateStandDisc(MainUtil.saveToDiscData(PE,standDisc).copy());
                    }
                }
            }
        }
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable(  "commands.sba.levelup_specific.single", ((Entity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable(  "commands.sba.levelup_specific.multiple", targets.size()), true);
        }
        return targets.size();
    }

    public static int sbaSetLeveldownStand(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                if (entity instanceof Player PE) {
                    IPlayerEntity ipe = ((IPlayerEntity)PE);
                    StandUser user = ((StandUser) PE);
                    ItemStack standDisc = user.sba$getStandDisc();
                    int standLevel = ipe.sba$getStandLevel();
                    if (!standDisc.isEmpty() && !(standDisc.getItem() instanceof MaxStandDiscItem)){
                        ipe.sba$setStandExp(0);
                        ipe.sba$setStandLevel((byte) Math.max(0,standLevel-1));
                        user.sba$updateStandDisc(MainUtil.saveToDiscData(PE,standDisc).copy());
                    }
                }
            }
        }
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable(  "commands.sba.levelup_specific.single", ((Entity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable(  "commands.sba.levelup_specific.multiple", targets.size()), true);
        }
        return targets.size();
    }


    public static int sbaSetLevelupFate(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                if (entity instanceof Player PE) {
                    if (FateTypes.isVampire(PE)) {
                        ((IPlayerEntity) PE).sba$setFate((byte) FateTypes.VAMPIRE.ordinal());
                        ((IPlayerEntity) PE).rdbt$getVampireData().vampireLevel =
                                Mth.clamp(((IPlayerEntity) PE).rdbt$getVampireData().vampireLevel + 1, 0, 40);
                        S2CPacketUtil.beamVampireData(PE);
                    }
                }
            }
        }
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable(  "commands.sba.levelup_specific.single", ((Entity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable(  "commands.sba.levelup_specific.multiple", targets.size()), true);
        }
        return targets.size();
    }

    public static int sbaSetLeveldownFate(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                if (entity instanceof Player PE) {
                    if (FateTypes.isVampire(PE)) {
                        ((IPlayerEntity) PE).sba$setFate((byte) FateTypes.VAMPIRE.ordinal());
                        ((IPlayerEntity) PE).rdbt$getVampireData().vampireLevel =
                                Mth.clamp(((IPlayerEntity) PE).rdbt$getVampireData().vampireLevel - 1, 0, 40);
                        S2CPacketUtil.beamVampireData(PE);
                    }
                }
            }
        }
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable(  "commands.sba.levelup_specific.single", ((Entity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable(  "commands.sba.levelup_specific.multiple", targets.size()), true);
        }
        return targets.size();
    }

    public static int sbaSetFate(CommandSourceStack source, Collection<? extends Entity> targets,
                                         String fate, int level, int experience) {

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity LE) {
                if (fate.equalsIgnoreCase("vampire")){
                    if (LE instanceof Player PL){
                        ((IPlayerEntity)PL).sba$setFate((byte) FateTypes.VAMPIRE.ordinal());
                        ((IPlayerEntity)PL).rdbt$getVampireData().vampireLevel = Mth.clamp(level,0,40);
                        ((IPlayerEntity)PL).rdbt$getVampireData().bloodExp = experience;
                        S2CPacketUtil.beamVampireData(PL);
                    } else if (LE instanceof Mob mb){
                        ((IMob)mb).sba$setVampire(true);
                    }
                } else if (fate.equalsIgnoreCase("zombie")){
                    if (LE instanceof Player PL){
                        ((IPlayerEntity)PL).sba$setFate((byte) FateTypes.ZOMBIE.ordinal());
                    }
                } else if (fate.equalsIgnoreCase("human") || fate.equalsIgnoreCase("none")){
                    if (LE instanceof Player PL){
                        ((IPlayerEntity)PL).sba$setFate((byte) FateTypes.HUMAN.ordinal());
                    } else if (LE instanceof Mob mb){
                        ((IMob)mb).sba$setVampire(false);
                    }
                } else {
                    source.sendFailure(Component.translatable("commands.sba.argument.fate_type.invalid", fate.toLowerCase()));
                    return targets.size();
                }
            }
        }

        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable("commands.sba.argument.fate_type.valid_2", ((Entity)targets.iterator().next()).getDisplayName(),fate), true);
        } else {
            source.sendSuccess(() -> Component.translatable(  "commands.sba.argument.fate_type.valid", targets.size(), fate), true);
        }
        return targets.size();
    }

    public static int sbaClearFateSkills(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity LE) {
                if (FateTypes.isVampire(LE)){
                    if (LE instanceof Player PL){
                        VampireData vdata = ((IPlayerEntity)PL).rdbt$getVampireData();
                        vdata.npcExp = 0;
                        vdata.animalExp = 0;
                        vdata.monsterEXP = 0;
                        vdata.timeSinceMonster = 0;
                        vdata.timeSinceAnimal = 0;
                        vdata.timeSinceNpc = 0;
                        vdata.strengthLevel = 0;
                        vdata.dexterityLevel = 0;
                        vdata.resilienceLevel = 0;
                        vdata.hypnotismLevel = 0;
                        vdata.superHearingLevel = 0;
                        vdata.bloodSpeedLevel = 0;
                        vdata.graftingLevel = 0;
                        vdata.fleshBudLevel = 0;
                        vdata.daggerSplatterLevel = 0;
                        vdata.jumpLevel = 0;
                        vdata.ripperEyesLevel = 0;
                        vdata.freezeLevel = 0;
                        S2CPacketUtil.beamVampireData(PL);
                    }
                }
            }
        }
        return targets.size();
    }
    public static int sbaResetVampireData(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity LE) {
                    if (LE instanceof Player PL){
                        VampireData vdata = ((IPlayerEntity)PL).rdbt$getVampireData();
                        vdata.vampireLevel = 0;
                        vdata.bloodExp = 0;
                        vdata.npcExp = 0;
                        vdata.animalExp = 0;
                        vdata.monsterEXP = 0;
                        vdata.timeSinceMonster = 0;
                        vdata.timeSinceAnimal = 0;
                        vdata.timeSinceNpc = 0;
                        vdata.strengthLevel = 0;
                        vdata.dexterityLevel = 0;
                        vdata.resilienceLevel = 0;
                        vdata.hypnotismLevel = 0;
                        vdata.superHearingLevel = 0;
                        vdata.bloodSpeedLevel = 0;
                        vdata.graftingLevel = 0;
                        vdata.fleshBudLevel = 0;
                        vdata.daggerSplatterLevel = 0;
                        vdata.jumpLevel = 0;
                        vdata.ripperEyesLevel = 0;
                        vdata.freezeLevel = 0;
                        S2CPacketUtil.beamVampireData(PL);
                    }
            }
        }
        return targets.size();
    }
    public static int sbaMaxFateSkills(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity LE) {
                if (FateTypes.isVampire(LE)){
                    if (LE instanceof Player PL){
                        VampireData vdata = ((IPlayerEntity)PL).rdbt$getVampireData();
                        vdata.strengthLevel = VampireData.strengthMaxLevel;
                        vdata.dexterityLevel = VampireData.dexterityMaxLevel;
                        vdata.resilienceLevel = VampireData.reslienceMaxLevel;
                        vdata.hypnotismLevel = VampireData.hypnotismMaxLevel;
                        vdata.superHearingLevel = VampireData.superHearingMaxLevel;
                        vdata.bloodSpeedLevel = VampireData.bloodSpeedMaxLevel;
                        vdata.graftingLevel = VampireData.graftingMaxLevel;
                        vdata.fleshBudLevel = VampireData.fleshBudMaxLevel;
                        vdata.daggerSplatterLevel = VampireData.daggerSplatterMaxLevel;
                        vdata.jumpLevel = VampireData.jumpMaxLevel;
                        vdata.ripperEyesLevel = VampireData.ripperEyesMaxLevel;
                        vdata.freezeLevel = VampireData.freezeMaxLevel;
                        S2CPacketUtil.beamVampireData(PL);
                    }
                }
            }
        }
        return targets.size();
    }

    public static int sbaSetVampireSkills(CommandSourceStack source, Collection<? extends Entity> targets,
                                                 int strengthLevel, int dexterityLevel, int resilienceLevel,
                                                 int hypnotismLevel, int superHearingLevel, int bloodSpeedLevel,
                                                 int graftingLevel, int fleshBudLevel, int daggerSplatterLevel,
                                                 int jumpLevel, int ripperEyesLevel, int freezeLevel) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity LE) {
                if (FateTypes.isVampire(LE)){
                    if (LE instanceof Player PL){
                        VampireData vdata = ((IPlayerEntity)PL).rdbt$getVampireData();
                        vdata.setStrengthLevel(strengthLevel);
                        vdata.setDexterityLevel(dexterityLevel);
                        vdata.setResilienceLevel(resilienceLevel);
                        vdata.setHypnotismLevel(hypnotismLevel);
                        vdata.setSuperHearingLevel(superHearingLevel);
                        vdata.setBloodSpeedLevel(bloodSpeedLevel);
                        vdata.setGraftingLevel(graftingLevel);
                        vdata.setFleshBudLevel(fleshBudLevel);
                        vdata.setDaggerSplatterLevel(daggerSplatterLevel);
                        vdata.setJumpLevel(jumpLevel);
                        vdata.setRipperEyesLevel(ripperEyesLevel);
                        vdata.setFreezeLevel(freezeLevel);
                        S2CPacketUtil.beamVampireData(PL);
                    }
                }
            }
        }
        return targets.size();
    }

    public static int sbaSetVampireSkill(CommandSourceStack source, Collection<? extends Entity> targets,
                                                 int number, int value) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity LE) {
                if (FateTypes.isVampire(LE)){
                    if (LE instanceof Player PL){
                        VampireData vdata = ((IPlayerEntity)PL).rdbt$getVampireData();
                        if (number == 0){
                            vdata.setStrengthLevel(value);
                        } else if (number == 1){
                            vdata.setDexterityLevel(value);
                        } else if (number == 2){
                            vdata.setResilienceLevel(value);
                        } else if (number == 3){
                            vdata.setHypnotismLevel(value);
                        } else if (number == 4){
                            vdata.setSuperHearingLevel(value);
                        } else if (number == 5){
                            vdata.setBloodSpeedLevel(value);
                        } else if (number == 6){
                            vdata.setGraftingLevel(value);
                        } else if (number == 7){
                            vdata.setFleshBudLevel(value);
                        } else if (number == 8){
                            vdata.setDaggerSplatterLevel(value);
                        } else if (number == 9){
                            vdata.setJumpLevel(value);
                        } else if (number == 10){
                            vdata.setFreezeLevel(value);
                        } else if (number == 11){
                            vdata.setDexterityLevel(value);
                        }
                        S2CPacketUtil.beamVampireData(PL);
                    }
                }
            }
        }
        return targets.size();
    }


    public static int sbaSetStandLevel(CommandSourceStack source, Collection<? extends Entity> targets, int level) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                if (entity instanceof Player PE) {
                    IPlayerEntity ipe = ((IPlayerEntity)PE);
                    StandUser user = ((StandUser) PE);
                    ItemStack standDisc = user.sba$getStandDisc();
                    int standLevel = ipe.sba$getStandLevel();
                    if (!standDisc.isEmpty() && !(standDisc.getItem() instanceof MaxStandDiscItem)){
                        ipe.sba$setStandExp(0);
                        ipe.sba$setStandLevel((byte) Math.min(user.sba$getStandPowers().getMaxLevel(),level));
                        user.sba$updateStandDisc(MainUtil.saveToDiscData(PE,standDisc).copy());
                    }
                }
            }
        }
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable(  "commands.sba.levelup_specific.single", ((Entity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable(  "commands.sba.levelup_specific.multiple", targets.size()), true);
        }
        return targets.size();
    }
    public static int executeDaze(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity LE) {
                AbilityScapeBasis.setDazed(LE, (byte) 20);

            }
        }
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable(  "commands.sba.heal.single", ((Entity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable(  "commands.sba.heal.multiple", targets.size()), true);
        }
        return targets.size();
    }
    public static int executeReplenish(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity LE) {
                LE.removeAllEffects();
                ((LivingEntity) entity).setHealth(((LivingEntity) entity).getMaxHealth());
                if (entity instanceof Player PE){
                    ((StandUser) PE).sba$setLocacacaCurse((byte) -1);
                    PE.getFoodData().setFoodLevel(20);
                    PE.getFoodData().setSaturation(14.4F);
                    if (((IFatePlayer)PE).rdbt$getFatePowers() instanceof ZombieFate zf){
                        zf.setZombieFishCount(5);
                    }
                    ((StandUser) PE).sba$setHeat(0);
                    MainUtil.clearCooldowns(PE);
                    ((StandUser) PE).sba$updateSealedTicks(-1);
                    ((StandUser) PE).sba$setMaxSealedTicks(-1);
                    S2CPacketUtil.sendIntPowerDataPacket(PE,PacketDataIndex.S2C_INT_MAX_SEAL,-1);
                    ((IPlayerEntity) PE).rdbt$setLevelDecreaseTicks(0);
                    ((IPlayerEntity) PE).sba$setPurpleHazePods((byte) 6);
                    S2CPacketUtil.syncPurpleHazePods((ServerPlayer) PE, (byte) 6);

                }

            }
        }
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable(  "commands.sba.heal.single", ((Entity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable(  "commands.sba.heal.multiple", targets.size()), true);
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
            source.sendSuccess(() -> Component.translatable(  "commands.sba.heal.single", ((Entity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable(  "commands.sba.heal.multiple", targets.size()), true);
        }
        return targets.size();
    }
    static int levelup(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof Player PE) {
                IPlayerEntity ipe = ((IPlayerEntity)PE);
                StandUser user = ((StandUser) PE);
                ItemStack standDisc = user.sba$getStandDisc();
                int standLevel = ipe.sba$getStandLevel();
                if (!standDisc.isEmpty() && !(standDisc.getItem() instanceof MaxStandDiscItem) &&
                        standLevel < user.sba$getStandPowers().getMaxLevel()){
                    ipe.sba$setStandExp(0);
                    ipe.sba$setStandLevel((byte) (standLevel+1));
                }
            }
        }
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable(  "commands.sba.levelup.single", ((Entity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable(  "commands.sba.levelup.multiple", targets.size()), true);
        }
        return targets.size();
    }
    static int executeDebugSummon(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity le) {
                ((StandUser) entity).sba$setStandDisc(ModItems.STAND_DISC_THE_WORLD.getDefaultInstance());
                ((StandUser) entity).sba$setStandPowers(new PowersTheWorld(le));
                ((StandUser) entity).sba$summonStand(entity.level(), true,true);
            }
        }
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable(  "commands.sba.force_summon.single", ((Entity)targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable(  "commands.sba.force_summon.multiple", targets.size()), true);
        }
        return targets.size();
    }

    static int executeDebugAttack(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                ((StandUser) entity).sba$tryPower(PowerIndex.ATTACK, true);
            }
        }
        return targets.size();
    }
    static int executeDebugSpecial(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                ((StandUser) entity).sba$tryPower(PowerIndex.SPECIAL, true);
            }
        }
        return targets.size();
    }

    static int executeDebugBarrage(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                ((StandUser) entity).sba$tryPower(PowerIndex.BARRAGE, true);
            }
        }
        return targets.size();
    }

    static int executeDebugGuard(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                ((StandUser) entity).sba$tryPower(PowerIndex.GUARD, true);
            }
        }
        return targets.size();
    }

    static int executeDebugCancel(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity LE) {
                ((StandUser) entity).sba$tryPower(PowerIndex.NONE, true);
                if (((TimeStop) entity.level()).isTimeStoppingEntity(LE)){
                    ((StandUser) entity).sba$tryPower(PowerIndex.SPECIAL_FINISH, true);
                }
            }
        }
        return targets.size();
    }


    static int executeDebugAbility(CommandSourceStack source, Collection<? extends Entity> targets, int ability) {
        if (ability < 100) {
            for (Entity entity : targets) {
                if (entity instanceof LivingEntity) {
                    ((StandUser) entity).sba$tryPower((byte) ability, true);
                }
            }
        }
        return targets.size();
    }
}
