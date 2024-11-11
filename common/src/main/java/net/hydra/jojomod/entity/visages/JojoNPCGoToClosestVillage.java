package net.hydra.jojomod.entity.visages;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.phys.Vec3;

public class JojoNPCGoToClosestVillage {
        public static BehaviorControl<JojoNPC> create(float $$0, int $$1) {
            return BehaviorBuilder.create($$2 -> $$2.group($$2.absent(MemoryModuleType.WALK_TARGET)).apply($$2, $$2x -> ($$3, $$4, $$5) -> {
                if ($$3.isVillage($$4.blockPosition())) {
                    return false;
                } else {
                    PoiManager $$6 = $$3.getPoiManager();
                    int $$7 = $$6.sectionsToVillage(SectionPos.of($$4.blockPosition()));
                    Vec3 $$8 = null;

                    for (int $$9 = 0; $$9 < 5; $$9++) {
                        Vec3 $$10 = LandRandomPos.getPos($$4, 15, 7, $$1x -> (double)(-$$6.sectionsToVillage(SectionPos.of($$1x))));
                        if ($$10 != null) {
                            int $$11 = $$6.sectionsToVillage(SectionPos.of(BlockPos.containing($$10)));
                            if ($$11 < $$7) {
                                $$8 = $$10;
                                break;
                            }

                            if ($$11 == $$7) {
                                $$8 = $$10;
                            }
                        }
                    }

                    if ($$8 != null) {
                        $$2x.set(new WalkTarget($$8, $$0, $$1));
                    }

                    return true;
                }
            }));
        }
}
