package net.hydra.jojomod.mixin.stand_users;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.AnubisItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Villager.class)
public abstract class ZVillager extends AbstractVillager implements ReputationEventHandler, VillagerDataHolder {
    @Shadow
    public abstract boolean isClientSide();



    /** At some point I'm gonna go and make the villager hold and inspect it for a sec, then drop the disc but whatever */
    @Inject(method = "mobInteract", at = @At(value = "HEAD"), cancellable = true)
    private void villagerAnubisInteraction(Player $$0, InteractionHand $$1, CallbackInfoReturnable<InteractionResult> cir) {
        if (!isClientSide()) {
            Villager This = (Villager) (Object) this;
            if (This.getVillagerData().getProfession() == VillagerProfession.CLERIC) {
                if($$0.getMainHandItem().getItem() instanceof AnubisItem ) {

                    int get = ClientNetworking.getAppropriateConfig().itemSettings.levelsToGetStand;
                    if ($$0.experienceLevel >= get || $$0.isCreative()) {
                       // This.setItemSlot(EquipmentSlot.MAINHAND,$$0.getMainHandItem());
                      //  This.setGuaranteedDrop(EquipmentSlot.MAINHAND);
                        This.addEffect(new MobEffectInstance(MobEffects.REGENERATION,100));

                        $$0.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(Items.AIR));
                        $$0.giveExperienceLevels(-get);

                        doAnubis($$0);
                        cir.setReturnValue(InteractionResult.SUCCESS);
                        return;
                    } else {
                        $$0.displayClientMessage(Component.translatable("container.enchant.level.requirement", get).withStyle(ChatFormatting.RED), true);
                        cir.setReturnValue(InteractionResult.FAIL);
                        return;
                    }


                }
            }
        }

    }


    @Unique
    private void doAnubis(Player $$0) {
        Villager This = ((Villager)(Object)this);
        $$0.level().playSound(null, $$0.blockPosition(), ModSounds.STAND_ARROW_USE_EVENT, SoundSource.PLAYERS, 1.5F, 1F);
        $$0.displayClientMessage(Component.translatable("item.roundabout.anubis_item.sword_cleansed").withStyle(ChatFormatting.WHITE), true);
        ((ServerLevel) $$0.level()).sendParticles(ParticleTypes.FIREWORK, $$0.getX(),
                $$0.getY() + $$0.getEyeHeight(), $$0.getZ(),
                20, 0, 0, 0, 0.4);
        if (This.getMainHandItem().is(ModItems.ANUBIS_ITEM)) {
            This.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(Items.AIR,3));
        }
        This.spawnAtLocation(ModItems.STAND_DISC_RATT);
    }

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

    public ZVillager(EntityType<? extends AbstractVillager> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Shadow public abstract Brain<Villager> getBrain();

    @Shadow
    private void registerBrainGoals(Brain<Villager> $$0) {
    }

    @Shadow public abstract void refreshBrain(ServerLevel $$0);

    @Shadow @Final private GossipContainer gossips;
}
