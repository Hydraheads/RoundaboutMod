package net.hydra.jojomod.mixin;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ReputationEventHandler;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.VillagerGoalPackages;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Set;

@Mixin(Villager.class)
public abstract class ZVillager extends AbstractVillager implements ReputationEventHandler, VillagerDataHolder {
    public ZVillager(EntityType<? extends AbstractVillager> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Shadow public abstract Brain<Villager> getBrain();

    @Unique
    public boolean roundabout$initializedStandUser = false;
    @Inject(method = "customServerAiStep", at = @At(value = "HEAD"))
    private void roundabout$customServerAiStep(CallbackInfo ci) {
        if (!((StandUser)this).roundabout$getStandDisc().isEmpty()) {
            if (!roundabout$initializedStandUser){
                ((IMob)this).roundabout$toggleFightOrFlight(true);
                roundabout$initializedStandUser = true;
            }
            if (((IMob)this).roundabout$getFightOrFlight() && this.getHealth() > this.getMaxHealth()*0.6){
                this.refreshBrain(((ServerLevel)this.level()));
                ((IMob)this).roundabout$toggleFightOrFlight(false);
            } else if (!((IMob)this).roundabout$getFightOrFlight() && this.getHealth() < this.getMaxHealth()*0.35){
                this.roundabout$refreshBrainOG(((ServerLevel)this.level()));
                ((IMob)this).roundabout$toggleFightOrFlight(true);
            }

            if (!((IMob)this).roundabout$getFightOrFlight()){
            }
        }
    }
    @Inject(method = "getPlayerReputation", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$getPlayerRep(Player $$0, CallbackInfoReturnable<Integer> cir) {
        IPlayerEntity ple = ((IPlayerEntity) $$0);
        byte shape = ple.roundabout$getShapeShift();
        ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
        if (shift != ShapeShifts.PLAYER) {
            if (ShapeShifts.isVillager(shift)) {
                cir.setReturnValue(50);
            }
        }
    }
    @Inject(method = "onReputationEventFrom", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$onReputationEventFrom(ReputationEventType $$0, Entity $$1, CallbackInfo ci) {
        if ($$1 instanceof Player PL) {
            IPlayerEntity ple = ((IPlayerEntity) PL);
            byte shape = ple.roundabout$getShapeShift();
            ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
            if (shift != ShapeShifts.PLAYER) {
                    ci.cancel();
            }
        }
    }
    @Inject(method = "mobInteract(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$mobInteract(Player $$0, InteractionHand $$1, CallbackInfoReturnable<InteractionResult> cir) {

        IPlayerEntity ple = ((IPlayerEntity) $$0);
        byte shape = ple.roundabout$getShapeShift();
        ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
        if (shift != ShapeShifts.PLAYER) {
            if (shift == ShapeShifts.ZOMBIE || shift == ShapeShifts.SKELETON) {
                cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide));
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

    @Shadow
    private void registerBrainGoals(Brain<Villager> $$0) {
    }

    @Shadow public abstract void refreshBrain(ServerLevel $$0);

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
}
