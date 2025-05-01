package net.hydra.jojomod.entity.visages;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.raid.Raid;

import java.util.Optional;

public class JojoNPCGoalPackages {
    private static final float STROLL_SPEED_MODIFIER = 0.4F;

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super JojoNPC>>> getCorePackage(float $$1) {
        return ImmutableList.of(
                Pair.of(0, new Swim(0.8F)),
                Pair.of(0, InteractWithDoor.create()),
                Pair.of(0, new LookAtTargetSink(45, 90)),
                Pair.of(0, new JojoNPCPanicTrigger()),
                Pair.of(0, WakeUp.create()),
                Pair.of(0, ReactToBell.create()),
                Pair.of(0, SetRaidStatus.create()),
                Pair.of(1, new MoveToTargetSink()),
                Pair.of(5, GoToWantedItem.create($$1, false, 4)),
                Pair.of(10, AcquirePoi.create($$0x -> $$0x.is(PoiTypes.HOME), MemoryModuleType.HOME, false, Optional.of((byte)14))),
                Pair.of(10, AcquirePoi.create($$0x -> $$0x.is(PoiTypes.MEETING), MemoryModuleType.MEETING_POINT, true, Optional.of((byte)14)))
        );
    }

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super JojoNPC>>> getPlayPackage(float $$0) {
        return ImmutableList.of(
                Pair.of(0, new MoveToTargetSink(80, 120)),
                getFullLookBehavior(),
                Pair.of(5, PlayTagWithOtherKids.create()),
                Pair.of(
                        5,
                        new RunOne<>(
                                ImmutableMap.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryStatus.VALUE_ABSENT),
                                ImmutableList.of(
                                        Pair.of(InteractWith.of(EntityType.VILLAGER, 8, MemoryModuleType.INTERACTION_TARGET, $$0, 2), 2),
                                        Pair.of(InteractWith.of(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, $$0, 2), 1),
                                        Pair.of(VillageBoundRandomStroll.create($$0), 1),
                                        Pair.of(SetWalkTargetFromLookTarget.create($$0, 2), 1),
                                        Pair.of(new JumpOnBed($$0), 2),
                                        Pair.of(new DoNothing(20, 40), 2)
                                )
                        )
                ),
                Pair.of(99, UpdateActivityFromSchedule.create())
        );
    }

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super JojoNPC>>> getRestPackage( float $$1) {
        return ImmutableList.of(
                Pair.of(2, JojoNPCSetWalkTargetFromBlockMemory.create(MemoryModuleType.HOME, $$1, 1, 150, 1200)),
                Pair.of(3, ValidateNearbyPoi.create($$0x -> $$0x.is(PoiTypes.HOME), MemoryModuleType.HOME)),
                Pair.of(3, new SleepInBed()),
                Pair.of(
                        5,
                        new RunOne<>(
                                ImmutableMap.of(MemoryModuleType.HOME, MemoryStatus.VALUE_ABSENT),
                                ImmutableList.of(
                                        Pair.of(SetClosestHomeAsWalkTarget.create($$1), 1),
                                        Pair.of(InsideBrownianWalk.create($$1), 4),
                                        Pair.of(JojoNPCGoToClosestVillage.create($$1, 4), 2),
                                        Pair.of(new DoNothing(20, 40), 2)
                                )
                        )
                ),
                getMinimalLookBehavior(),
                Pair.of(99, UpdateActivityFromSchedule.create())
        );
    }

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super JojoNPC>>> getMeetPackage(float $$1) {
        return ImmutableList.of(
                Pair.of(
                        2,
                        TriggerGate.triggerOneShuffled(
                                ImmutableList.of(Pair.of(StrollAroundPoi.create(MemoryModuleType.MEETING_POINT, 0.4F, 40), 2), Pair.of(SocializeAtBell.create(), 2))
                        )
                ),
                Pair.of(10, SetLookAndInteract.create(EntityType.PLAYER, 4)),
                Pair.of(2, JojoNPCSetWalkTargetFromBlockMemory.create(MemoryModuleType.MEETING_POINT, $$1, 6, 100, 200)),
                Pair.of(3, ValidateNearbyPoi.create($$0x -> $$0x.is(PoiTypes.MEETING), MemoryModuleType.MEETING_POINT)),
                getFullLookBehavior(),
                Pair.of(99, UpdateActivityFromSchedule.create())
        );
    }

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super JojoNPC>>> getIdlePackage(float $$1) {
        return ImmutableList.of(
                Pair.of(
                        2,
                        new RunOne<>(
                                ImmutableList.of(
                                        Pair.of(InteractWith.of(EntityType.VILLAGER, 8, MemoryModuleType.INTERACTION_TARGET, $$1, 2), 2),
                                        Pair.of(InteractWith.of(EntityType.VILLAGER, 8, AgeableMob::canBreed, AgeableMob::canBreed, MemoryModuleType.BREED_TARGET, $$1, 2), 1),
                                        Pair.of(InteractWith.of(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, $$1, 2), 1),
                                        Pair.of(VillageBoundRandomStroll.create($$1), 1),
                                        Pair.of(SetWalkTargetFromLookTarget.create($$1, 2), 1),
                                        Pair.of(new JumpOnBed($$1), 1),
                                        Pair.of(new DoNothing(30, 60), 1)
                                )
                        )
                ),
                Pair.of(3, SetLookAndInteract.create(EntityType.PLAYER, 4)),
                getFullLookBehavior(),
                Pair.of(99, UpdateActivityFromSchedule.create())
        );
    }

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super JojoNPC>>> getPanicPackage(float $$1) {
        float $$2 = $$1 * 1.5F;
        return ImmutableList.of(
                Pair.of(0, VillagerCalmDown.create()),
                Pair.of(1, SetWalkTargetAwayFrom.entity(MemoryModuleType.NEAREST_HOSTILE, $$2, 6, false)),
                Pair.of(1, SetWalkTargetAwayFrom.entity(MemoryModuleType.HURT_BY_ENTITY, $$2, 6, false)),
                Pair.of(3, VillageBoundRandomStroll.create($$2, 2, 2)),
                getMinimalLookBehavior()
        );
    }

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super JojoNPC>>> getPreRaidPackage(float $$1) {
        return ImmutableList.of(
                Pair.of(0, RingBell.create()),
                Pair.of(
                        0,
                        TriggerGate.triggerOneShuffled(
                                ImmutableList.of(
                                        Pair.of(JojoNPCSetWalkTargetFromBlockMemory.create(MemoryModuleType.MEETING_POINT, $$1 * 1.5F, 2, 150, 200), 6),
                                        Pair.of(VillageBoundRandomStroll.create($$1 * 1.5F), 2)
                                )
                        )
                ),
                getMinimalLookBehavior(),
                Pair.of(99, ResetRaidStatus.create())
        );
    }

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super JojoNPC>>> getRaidPackage(float $$1) {
        return ImmutableList.of(
                Pair.of(
                        0,
                        BehaviorBuilder.sequence(
                                BehaviorBuilder.triggerIf(JojoNPCGoalPackages::raidExistsAndNotVictory),
                                TriggerGate.triggerOneShuffled(
                                        ImmutableList.of(Pair.of(MoveToSkySeeingSpot.create($$1), 5), Pair.of(VillageBoundRandomStroll.create($$1 * 1.1F), 2))
                                )
                        )
                ),
                Pair.of(0, new JojoNPCCelebrateVillagersSurvivedRaid(600, 600)),
                Pair.of(
                        2, BehaviorBuilder.sequence(BehaviorBuilder.triggerIf(JojoNPCGoalPackages::raidExistsAndActive), LocateHidingPlace.create(24, $$1 * 1.4F, 1))
                ),
                getMinimalLookBehavior(),
                Pair.of(99, ResetRaidStatus.create())
        );
    }

    public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super JojoNPC>>> getHidePackage(float $$1) {
        int $$2 = 2;
        return ImmutableList.of(Pair.of(0, SetHiddenState.create(15, 3)), Pair.of(1, LocateHidingPlace.create(32, $$1 * 1.25F, 2)), getMinimalLookBehavior());
    }

    private static Pair<Integer, BehaviorControl<LivingEntity>> getFullLookBehavior() {
        return Pair.of(
                5,
                new RunOne<>(
                        ImmutableList.of(
                                Pair.of(SetEntityLookTarget.create(EntityType.CAT, 8.0F), 8),
                                Pair.of(SetEntityLookTarget.create(EntityType.VILLAGER, 8.0F), 2),
                                Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 8.0F), 2),
                                Pair.of(SetEntityLookTarget.create(MobCategory.CREATURE, 8.0F), 1),
                                Pair.of(SetEntityLookTarget.create(MobCategory.WATER_CREATURE, 8.0F), 1),
                                Pair.of(SetEntityLookTarget.create(MobCategory.AXOLOTLS, 8.0F), 1),
                                Pair.of(SetEntityLookTarget.create(MobCategory.UNDERGROUND_WATER_CREATURE, 8.0F), 1),
                                Pair.of(SetEntityLookTarget.create(MobCategory.WATER_AMBIENT, 8.0F), 1),
                                Pair.of(SetEntityLookTarget.create(MobCategory.MONSTER, 8.0F), 1),
                                Pair.of(new DoNothing(30, 60), 2)
                        )
                )
        );
    }

    private static Pair<Integer, BehaviorControl<LivingEntity>> getMinimalLookBehavior() {
        return Pair.of(
                5,
                new RunOne<>(
                        ImmutableList.of(
                                Pair.of(SetEntityLookTarget.create(EntityType.VILLAGER, 8.0F), 2),
                                Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 8.0F), 2),
                                Pair.of(new DoNothing(30, 60), 8)
                        )
                )
        );
    }

    private static boolean raidExistsAndActive(ServerLevel $$0x, LivingEntity $$1x) {
        Raid $$2 = $$0x.getRaidAt($$1x.blockPosition());
        return $$2 != null && $$2.isActive() && !$$2.isVictory() && !$$2.isLoss();
    }

    private static boolean raidExistsAndNotVictory(ServerLevel $$0x, LivingEntity $$1x) {
        Raid $$2 = $$0x.getRaidAt($$1x.blockPosition());
        return $$2 != null && $$2.isVictory();
    }
}
