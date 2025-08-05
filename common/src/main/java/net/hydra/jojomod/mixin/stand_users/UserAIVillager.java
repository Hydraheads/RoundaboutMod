package net.hydra.jojomod.mixin.stand_users;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ReputationEventHandler;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.VillagerGoalPackages;
import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public abstract class UserAIVillager extends AbstractVillager implements ReputationEventHandler, VillagerDataHolder {
    /**This class sets up a mode in villager ai called fight or flight mode,
     * it contains nuanced ai for villagers with stands to
     * essentially start running when they get low on health (see SurvivorBrain as well),
     * but behave differently when they have reasonable health. The goal is for stand user
     * villagers to basically keep themselves alive, but fight whenever they can*/


    @Unique
    public boolean roundabout$initializedViolence = false;
    @Inject(method = "customServerAiStep", at = @At(value = "HEAD"))
    private void roundabout$customServerAiStep(CallbackInfo ci) {
        StandUser user = ((StandUser)this);
        boolean hasAStand = user.roundabout$hasAStand();
        boolean isAggressive = MainUtil.forceAggression(this);
        if (isAggressive) {
            if (!roundabout$initializedViolence){
                ((IMob)this).roundabout$toggleFightOrFlight(true);
                roundabout$initializedViolence = true;
            }

            if (hasAStand) {
                if (((IMob) this).roundabout$getFightOrFlight() && this.getHealth() > this.getMaxHealth() * 0.6) {
                    this.refreshBrain(((ServerLevel) this.level()));
                    ((IMob) this).roundabout$toggleFightOrFlight(false);
                } else if (!((IMob) this).roundabout$getFightOrFlight() && this.getHealth() < this.getMaxHealth() * 0.35) {
                    this.roundabout$refreshBrainOG(((ServerLevel) this.level()));
                    ((IMob) this).roundabout$toggleFightOrFlight(true);
                }
            }

            if (!((IMob)this).roundabout$getFightOrFlight()){
            }
        } else {
            if (roundabout$initializedViolence){
                roundabout$initializedViolence = false;
                this.refreshBrain(((ServerLevel) this.level()));
            }
        }
    }

    @Inject(method = "refreshBrain", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$refreshBrain(ServerLevel $$0,CallbackInfo ci) {
        if (!((StandUser)this).roundabout$getStandDisc().isEmpty()) {
            Brain<Villager> $$1 = this.getBrain();
            $$1.stopAll($$0, ((Villager)(Object)this));
            this.brain = $$1.copyWithoutBehaviors();
            this.roundabout$registerBrainGoals(this.getBrain());
            ci.cancel();
        }
    }


    @Unique
    private void roundabout$refreshBrainOG(ServerLevel $$0) {
        Brain<Villager> $$1 = this.getBrain();
        $$1.stopAll($$0, ((Villager)(Object)this));
        this.brain = $$1.copyWithoutBehaviors();
        this.registerBrainGoals(this.getBrain());
    }


    @Unique
    private void roundabout$registerBrainGoals(Brain<Villager> $$0) {
        VillagerProfession $$1 = this.getVillagerData().getProfession();
        if (this.isBaby()) {
            $$0.setSchedule(Schedule.VILLAGER_BABY);
            $$0.addActivity(Activity.PLAY, VillagerGoalPackages.getPlayPackage(0.5F));
        } else {
            $$0.setSchedule(Schedule.VILLAGER_DEFAULT);
            $$0.addActivityWithConditions(
                    Activity.WORK, VillagerGoalPackages.getWorkPackage($$1, 0.5F), ImmutableSet.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT))
            );
        }

        $$0.addActivity(Activity.CORE, VillagerGoalPackages.getCorePackage($$1, 0.5F));
        $$0.addActivityWithConditions(
                Activity.MEET, VillagerGoalPackages.getMeetPackage($$1, 0.5F), ImmutableSet.of(Pair.of(MemoryModuleType.MEETING_POINT, MemoryStatus.VALUE_PRESENT))
        );
        $$0.addActivity(Activity.REST, VillagerGoalPackages.getRestPackage($$1, 0.5F));
        $$0.addActivity(Activity.IDLE, VillagerGoalPackages.getIdlePackage($$1, 0.5F));
        $$0.addActivity(Activity.PRE_RAID, VillagerGoalPackages.getPreRaidPackage($$1, 0.5F));
        $$0.addActivity(Activity.RAID, VillagerGoalPackages.getRaidPackage($$1, 0.5F));
        $$0.setCoreActivities(ImmutableSet.of(Activity.CORE));
        $$0.setDefaultActivity(Activity.IDLE);
        $$0.setActiveActivityIfPossible(Activity.IDLE);
        $$0.updateActivityFromSchedule(this.level().getDayTime(), this.level().getGameTime());
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    public UserAIVillager(EntityType<? extends AbstractVillager> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Shadow public abstract Brain<Villager> getBrain();

    @Shadow
    private void registerBrainGoals(Brain<Villager> $$0) {
    }

    @Shadow public abstract void refreshBrain(ServerLevel $$0);

    @Shadow @Final private GossipContainer gossips;
}
