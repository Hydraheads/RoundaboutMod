package net.hydra.jojomod.mixin.stand_users;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IGossipContainerAccess;
import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.IVillagerAccess;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.AnubisItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersLyinEyes;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
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
public abstract class ZVillager extends AbstractVillager implements ReputationEventHandler, VillagerDataHolder, IVillagerAccess {
    @Shadow
    public abstract boolean isClientSide();


    @Unique
    private static final EntityDataAccessor<Integer> ROUNDABOUT$ANUBIS_TICKS = SynchedEntityData.defineId(Villager.class,
            EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Byte> ROUNDABOUT$ANUBIS_TYPE = SynchedEntityData.defineId(Villager.class,
            EntityDataSerializers.BYTE);
    @Unique
    private static final EntityDataAccessor<Byte> ROUNDABOUT$TENSION_STATE = SynchedEntityData.defineId(Villager.class,
            EntityDataSerializers.BYTE
    );
    @Unique
    private static final EntityDataAccessor<Integer> ROUNDABOUT$CALLOUT_EFFECT_TICKS = SynchedEntityData.defineId(Villager.class,
            EntityDataSerializers.INT);

    @Unique
    @Override
    public int roundabout$getAnubisTicks() {
        return this.getEntityData().get(ROUNDABOUT$ANUBIS_TICKS);
    }
    @Unique
    @Override
    public void roundabout$setAnubisTicks(int i) {
        this.getEntityData().set(ROUNDABOUT$ANUBIS_TICKS,i);
    }
    @Unique
    @Override
    public void roundabout$clearGossips() {
        ((IGossipContainerAccess)this.gossips).rdbt$clearGossips();
    }
    @Unique
    public byte roundabout$getAnubisType() {
        return this.getEntityData().get(ROUNDABOUT$ANUBIS_TYPE);
    }
    @Unique
    public void roundabout$setAnubisType(byte i) {
        this.getEntityData().set(ROUNDABOUT$ANUBIS_TYPE,i);
    }




    @Inject(method = "addAdditionalSaveData",at = @At("HEAD"))
    private void roundabout$saveAdditionalData(CompoundTag $$0, CallbackInfo ci) {
        if (this.roundabout$getAnubisTicks() != -1) {
            $$0.putByte("roundabout.heldAnubis",this.roundabout$getAnubisType());
        }
        if (this.roundabout$getTensionState() != TENSION_UNROLLED) {
            $$0.putByte("roundabout.tensionState", this.roundabout$getTensionState());
        }
    }

    @Inject(method = "readAdditionalSaveData",at = @At("HEAD"))
    private void roundabout$readAddtionalData(CompoundTag $$0, CallbackInfo ci) {
        if ($$0.contains("roundabout.heldAnubis")) {
            this.roundabout$setAnubisTicks(30);
            this.roundabout$setAnubisType($$0.getByte("roundabout.heldAnubis"));
        }
        if ($$0.contains("roundabout.tensionState")) {
            this.roundabout$setTensionState($$0.getByte("roundabout.tensionState"));
        }
    }

    @Inject(method = "defineSynchedData",at=@At(value = "TAIL"))
    private void roundabout$addVillagerSynched(CallbackInfo ci) {
        if (!((Villager)(Object)this).getEntityData().hasItem(ROUNDABOUT$ANUBIS_TICKS) ) {
            ((Villager)(Object)this).getEntityData().define(ROUNDABOUT$ANUBIS_TICKS, -1);
            ((Villager)(Object)this).getEntityData().define(ROUNDABOUT$ANUBIS_TYPE, (byte)-1);
        }
        if (!((Villager)(Object)this).getEntityData().hasItem(ROUNDABOUT$TENSION_STATE)) {
            ((Villager)(Object)this).getEntityData().define(ROUNDABOUT$TENSION_STATE, TENSION_UNROLLED);
        }
        if (!this.getEntityData().hasItem(ROUNDABOUT$CALLOUT_EFFECT_TICKS)) {
            this.getEntityData().define(ROUNDABOUT$CALLOUT_EFFECT_TICKS, 0);
        }
    }
    /// currently broken, unsure why

    @Unique
    @Override
    public byte roundabout$getTensionState() {
        return this.getEntityData().get(ROUNDABOUT$TENSION_STATE);
    }

    @Unique
    @Override
    public void roundabout$setTensionState(byte state) {
        this.getEntityData().set(ROUNDABOUT$TENSION_STATE, state);
    }

    @Unique
    @Override
    public boolean roundabout$isTense() {
        return this.roundabout$getTensionState() == TENSION_TENSE;
    }

    @Unique
    @Override
    public void roundabout$setTense(boolean tense) {
        this.roundabout$setTensionState(tense ? TENSION_TENSE : TENSION_RELAXED);
    }

    @Unique
    @Override
    public boolean roundabout$isUnnerved() {
        return this.roundabout$getTensionState() == TENSION_UNNERVED;
    }

    @Unique
    @Override
    public void roundabout$setUnnerved() {
        this.roundabout$setTensionState(TENSION_UNNERVED);
        this.getEntityData().set(ROUNDABOUT$CALLOUT_EFFECT_TICKS, 160);
        this.level().playSound(null, this.blockPosition(), ModSounds.VILLAGER_CALLED_OUT_EVENT, SoundSource.NEUTRAL, 0.9F, 0.75F);
    }

    @Unique
    @Override
    public boolean roundabout$hasRolledTension() {
        return this.roundabout$getTensionState() != TENSION_UNROLLED;
    }

    @Unique
    @Override
    public void roundabout$rollTensionIfUnrolled() {
        if (!this.roundabout$hasRolledTension() && !this.isClientSide()) {
            // note to self: replace this with the lyin eyes config in the future - 88superguy
            boolean isTense = this.random.nextFloat() < 0.65F;
            this.roundabout$setTense(isTense);
        }
    }

    @Unique
    @Override
    public int roundabout$getCalloutTicks() {
        return this.getEntityData().get(ROUNDABOUT$CALLOUT_EFFECT_TICKS);
    }


    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void roundabout$villagerTick(CallbackInfo ci) {
        int at = this.roundabout$getAnubisTicks();
        int calloutTick = this.roundabout$getCalloutTicks();
        if (!isClientSide()) {
            if (at >= 0) {
                at--;

                ItemStack stack = new ItemStack(ModItems.ANUBIS_ITEM);
                stack.getOrCreateTag().putFloat("CustomModelData",this.roundabout$getAnubisType()+1);

                this.setItemSlot(EquipmentSlot.MAINHAND,stack);
                this.setDropChance(EquipmentSlot.MAINHAND, 1.0F);

                ((ServerLevel) this.level()).sendParticles(ParticleTypes.FIREWORK, this.getX(),
                        this.getY() + this.getEyeHeight(), this.getZ(),
                        1, 0, 0, 0, 0.4);

                this.roundabout$setAnubisTicks(at);
                if (at == 0) {
                    doAnubis();
                }
            }
            if (calloutTick >= 0) {
                calloutTick--;
                this.getEntityData().set(ROUNDABOUT$CALLOUT_EFFECT_TICKS, calloutTick);
                if (calloutTick % 2 == 0) {
                    ((ServerLevel) this.level()).sendParticles(ModParticles.SWEAT, this.getRandomX(0.45),
                            this.getY() + this.getEyeHeight() * 0.85, this.getRandomZ(0.45),
                            1, 0.02, 0.02, 0.02, 0.01);
                }
            }
        }

    }

    // handles tension for interaction
    @Inject(method = "mobInteract", at = @At("HEAD"))
    private void roundabout$handleTensionOnInteract(
            Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (!isClientSide()
                && (((StandUser) player).roundabout$getStandPowers() instanceof PowersLyinEyes lyinEyes)
                && lyinEyes.getVisionModeClient()) {
            boolean tensionWasAlreadyRolled = this.roundabout$hasRolledTension();
            if (!tensionWasAlreadyRolled) {
                this.roundabout$rollTensionIfUnrolled();
                return;
            }
            if (this.roundabout$isTense()) {
                this.roundabout$setUnnerved();
            }
        }
    }

    //taken from CinderellaVillager
    @Inject(method = "updateSpecialPrices(Lnet/minecraft/world/entity/player/Player;)V", at = @At(value = "TAIL"),cancellable = true)
    private void roundabout$updateSpecialPrices(Player player, CallbackInfo ci) {
        if (!player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) {
            //lyin eyes discount
            if (((StandUser)player).roundabout$getStandPowers() instanceof PowersLyinEyes
                    && this.roundabout$isUnnerved()) {
                for (MerchantOffer offer : this.getOffers()) {
                    // rn it's a 20% discount, turn this into a config later :thumbsup:
                    int discount = (int) Math.floor(0.20 * (double) offer.getBaseCostA().getCount());
                    offer.addToSpecialPriceDiff(-Math.max(discount, 1));
                }
            }
        }
    }

    @Inject(method = "mobInteract", at = @At(value = "HEAD"), cancellable = true)
    private void villagerAnubisInteraction(Player $$0, InteractionHand $$1, CallbackInfoReturnable<InteractionResult> cir) {
        if (!isClientSide() &&  this.entityData.get(ROUNDABOUT$ANUBIS_TICKS) <= 0  ) {
            Villager This = (Villager) (Object) this;
            ItemStack stack = $$0.getItemInHand($$1);
            if (This.getVillagerData().getProfession() == VillagerProfession.CLERIC) {
                if(stack.getItem() instanceof AnubisItem ) {

                    int get = ClientNetworking.getAppropriateConfig().itemSettings.levelsToGetStand;
                    if ($$0.experienceLevel >= get || $$0.isCreative()) {
                       // This.setItemSlot(EquipmentSlot.MAINHAND,$$0.getMainHandItem());
                      //  This.setGuaranteedDrop(EquipmentSlot.MAINHAND);
                        This.addEffect(new MobEffectInstance(MobEffects.REGENERATION,100));


                        This.level().playSound(null,This.blockPosition(), SoundEvents.EVOKER_PREPARE_SUMMON,SoundSource.NEUTRAL,1F,1F);
                        This.setVillagerXp(This.getVillagerXp()+2);

                        roundabout$setAnubisTicks(60);
                        CompoundTag tag = stack.getTagElement("SkinType");
                        if (tag != null) {
                            roundabout$setAnubisType(tag.getByte("SkinType"));
                        }

                        $$0.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(Items.AIR));
                        $$0.giveExperienceLevels(-get);

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
    private void doAnubis() {
        Villager This = ((Villager)(Object)this);
        This.level().playSound(null, This.blockPosition(), ModSounds.STAND_ARROW_USE_EVENT, SoundSource.PLAYERS, 1.5F, 1F);
        ((ServerLevel) This.level()).sendParticles(ParticleTypes.FIREWORK, This.getX(),
                This.getY() + This.getEyeHeight(), This.getZ(),
                20, 0, 0, 0, 0.4);
        ItemStack stack = new ItemStack(ModItems.STAND_DISC_ANUBIS,1);
        if (roundabout$getAnubisType() != (byte)-1) {
            CompoundTag $$4 = stack.getOrCreateTagElement("Memory");
            $$4.putByte("AnubisSkin",roundabout$getAnubisType());
        }
        roundabout$setAnubisType((byte)-1);

        this.spawnAtLocation(stack);

        Player player = This.level().getNearestPlayer(This,10);
        if (player != null) {
            player.displayClientMessage(Component.translatable("item.roundabout.anubis_item.sword_cleansed").withStyle(ChatFormatting.WHITE), true);
        }
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
