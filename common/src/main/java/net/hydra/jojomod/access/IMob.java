package net.hydra.jojomod.access;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IMob {
    boolean sba$isWorthy();
    void sba$setWorthy(boolean $$0);
    boolean sba$isVampire();
    void sba$setVampire(boolean $$0);
    byte sba$getFate();
    void sba$setConfusionTicks(int set);
    int sba$getConfusionTicks();
    void rdbt$setStolen(boolean steal);
    boolean rdbt$getStolen();
    void sba$setFate(byte fate);
    void sba$deeplyRemoveTargets();
    void sba$deeplyEnforceTarget(Entity ent);
    int sba$getSightProtectionTicks();
    void sba$setSightProtectionTicks(int sightProt);
    boolean sba$getIsNaturalStandUser();
    void sba$maybeDisableShield(Player $$0, ItemStack $$1, ItemStack $$2);
    GoalSelector sba$getGoalSelector();
    void sba$setRetractTicks(int ticks);
    void sba$setIsNaturalStandUser(boolean set);

    void sba$toggleFightOrFlight(boolean flight);
    boolean sba$getFightOrFlight();
    void sba$resetAtkCD();
    boolean sba$getIsBred();
    void sba$setIsBred(boolean set);

    void sba$setHypnotizedBy(LivingEntity set, int time);
    void sba$setHypnotizedBy(LivingEntity set);
    LivingEntity sba$getHypnotizedBy();
    void sba$purgePiglinAggro(Entity purge);
}
