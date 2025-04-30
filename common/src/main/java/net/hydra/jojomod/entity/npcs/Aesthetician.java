package net.hydra.jojomod.entity.npcs;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.JojoNPCGoalPackages;
import net.hydra.jojomod.entity.visages.StandUsingNPC;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandDiscItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.level.Level;

public class Aesthetician extends StandUsingNPC {
    public Aesthetician(EntityType<? extends JojoNPC> p_35384_, Level p_35385_) {
        super(p_35384_, p_35385_);
    }

    @Override
    public StandDiscItem getDisc(){
        return ((StandDiscItem) ModItems.STAND_DISC_CINDERELLA);
    }

    @Override
    public void registerBrainGoals(Brain<JojoNPC> p_35425_) {
        if (this.isBaby()) {
            p_35425_.setSchedule(Schedule.VILLAGER_BABY);
            p_35425_.addActivity(Activity.PLAY, JojoNPCGoalPackages.getPlayPackage(1F));
        } else {
            p_35425_.setSchedule(Schedule.VILLAGER_DEFAULT);
            p_35425_.addActivityWithConditions(Activity.WORK, JojoNPCGoalPackages.getMeetPackage(1F), ImmutableSet.of(Pair.of(MemoryModuleType.MEETING_POINT, MemoryStatus.VALUE_PRESENT)));
        }

        p_35425_.addActivity(Activity.CORE, JojoNPCGoalPackages.getCorePackage(1F));
        p_35425_.addActivityWithConditions(Activity.MEET, JojoNPCGoalPackages.getMeetPackage(1F), ImmutableSet.of(Pair.of(MemoryModuleType.MEETING_POINT, MemoryStatus.VALUE_PRESENT)));
        p_35425_.addActivity(Activity.REST, JojoNPCGoalPackages.getRestPackage(1F));
        p_35425_.addActivity(Activity.IDLE, JojoNPCGoalPackages.getIdlePackage(1F));
        p_35425_.addActivity(Activity.PANIC, JojoNPCGoalPackages.getPanicPackage(1F));
        p_35425_.addActivity(Activity.PRE_RAID, JojoNPCGoalPackages.getPreRaidPackage(1F));
        p_35425_.addActivity(Activity.RAID, JojoNPCGoalPackages.getRaidPackage(1F));
        p_35425_.addActivity(Activity.HIDE, JojoNPCGoalPackages.getHidePackage(1F));
        p_35425_.setCoreActivities(ImmutableSet.of(Activity.CORE));
        p_35425_.setDefaultActivity(Activity.IDLE);
        p_35425_.setActiveActivityIfPossible(Activity.IDLE);
        p_35425_.updateActivityFromSchedule(this.level().getDayTime(), this.level().getGameTime());
    }
}
