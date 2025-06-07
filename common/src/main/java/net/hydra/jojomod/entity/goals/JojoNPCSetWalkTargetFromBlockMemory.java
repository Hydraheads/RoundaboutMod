package net.hydra.jojomod.entity.goals;

import net.hydra.jojomod.entity.visages.JojoNPC;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class JojoNPCSetWalkTargetFromBlockMemory {
        public static OneShot<JojoNPC> create(MemoryModuleType<GlobalPos> $$0, float $$1, int $$2, int $$3, int $$4) {
            return BehaviorBuilder.create(
                    $$5 -> $$5.group($$5.registered(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE), $$5.absent(MemoryModuleType.WALK_TARGET), $$5.present($$0))
                            .apply($$5, ($$6, $$7, $$8) -> ($$9, $$10, $$11) -> {
                                GlobalPos $$12 = $$5.get($$8);
                                Optional<Long> $$13 = $$5.tryGet($$6);
                                if ($$12.dimension() == $$9.dimension() && (!$$13.isPresent() || $$9.getGameTime() - $$13.get() <= (long)$$4)) {
                                    if ($$12.pos().distManhattan($$10.blockPosition()) > $$3) {
                                        Vec3 $$14 = null;
                                        int $$15 = 0;
                                        int $$16 = 1000;

                                        while ($$14 == null || BlockPos.containing($$14).distManhattan($$10.blockPosition()) > $$3) {
                                            $$14 = DefaultRandomPos.getPosTowards($$10, 15, 7, Vec3.atBottomCenterOf($$12.pos()), (float) (Math.PI / 2));
                                            if (++$$15 == 1000) {
                                                $$10.releasePoi($$0);
                                                $$8.erase();
                                                $$6.set($$11);
                                                return true;
                                            }
                                        }

                                        $$7.set(new WalkTarget($$14, $$1, $$2));
                                    } else if ($$12.pos().distManhattan($$10.blockPosition()) > $$2) {
                                        $$7.set(new WalkTarget($$12.pos(), $$1, $$2));
                                    }
                                } else {
                                    $$10.releasePoi($$0);
                                    $$8.erase();
                                    $$6.set($$11);
                                }

                                return true;
                            })
            );
        }

}
