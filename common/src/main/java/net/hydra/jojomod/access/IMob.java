package net.hydra.jojomod.access;

import net.minecraft.world.entity.ai.goal.GoalSelector;

public interface IMob {
    boolean roundabout$isWorthy();
    void roundabout$setWorthy(boolean $$0);
    boolean roundabout$getIsNaturalStandUser();
    GoalSelector roundabout$getGoalSelector();
    void roundabout$setRetractTicks(int ticks);
    void roundabout$setIsNaturalStandUser(boolean set);

    void roundabout$toggleFightOrFlight(boolean flight);
    boolean roundabout$getFightOrFlight();
    void roundabout$resetAtkCD();
}
