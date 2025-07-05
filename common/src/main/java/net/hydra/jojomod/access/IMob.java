package net.hydra.jojomod.access;

import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IMob {
    boolean roundabout$isWorthy();
    void roundabout$setWorthy(boolean $$0);
    void roundabout$deeplyRemoveTargets();
    int roundabout$getSightProtectionTicks();
    void roundabout$setSightProtectionTicks(int sightProt);
    boolean roundabout$getIsNaturalStandUser();
    void roundabout$maybeDisableShield(Player $$0, ItemStack $$1, ItemStack $$2);
    GoalSelector roundabout$getGoalSelector();
    void roundabout$setRetractTicks(int ticks);
    void roundabout$setIsNaturalStandUser(boolean set);

    void roundabout$toggleFightOrFlight(boolean flight);
    boolean roundabout$getFightOrFlight();
    void roundabout$resetAtkCD();
}
